# Trabalho GB — Plataforma Gamificada de Missões para Desenvolvedores (Microsserviços)

> **Disciplina:** Programação Backend — UNISINOS  
> **Professor:** Gilberto Irajá Müller  

---

## Sobre o Projeto (A Ideia)

Este projeto consiste em uma arquitetura de **microsserviços em Java com Spring Boot** que simula uma **plataforma gamificada para desenvolvedores**. 

A ideia central é permitir que desenvolvedores cumpram "missões" baseadas na sua atividade real no **GitHub** (como atingir uma meta mínima de repositórios públicos). Ao solicitar a validação de uma missão para um usuário do GitHub, o sistema consulta em tempo real a **API pública oficial do GitHub**, verifica se o desenvolvedor atingiu os requisitos, calcula a experiência (**XP**) ganha com base no multiplicador da temporada atual e envia um evento assíncrono via **Apache Kafka** para registrar essa conquista no histórico de auditoria.

---

## Resumo da Arquitetura (Os 7 Microsserviços)

O sistema foi dividido em **7 microsserviços independentes**, cada um com sua responsabilidade única:

| Microsserviço | Porta | Função no Sistema |
| :--- | :---: | :--- |
| **`registry-service`** | `8761` | **Service Discovery (Netflix Eureka):** Catálogo central onde todos os microsserviços se registram ao iniciar, permitindo que se comuniquem pelo nome lógico sem depender de IPs fixos. |
| **`config-service`** | `8006` | **Config Server:** Lê o diretório `config-repo` no GitHub e distribui configurações dinâmicas (como o multiplicador de XP e a linguagem da temporada) para os demais serviços. |
| **`gateway-service`** | `8005` | **API Gateway:** Ponto único de entrada do sistema. Roteia as chamadas externas para os serviços internos, valida o token JWT no filtro de segurança e aplica *Circuit Breaker* (Resilience4j) com *Fallback* caso algum serviço caia. |
| **`usuario-service`** | `8004` | **Autenticação e Usuários:** Gerencia o cadastro de usuários da plataforma (com senha criptografada em `BCrypt`), realiza o login gerando o token **JWT** e valida tokens para o Gateway. |
| **`github-service`** | `8002` | **Integração Externa:** Conecta-se à API externa do GitHub (`https://api.github.com/users/{username}`) usando `RestTemplate` para buscar os dados reais do desenvolvedor. |
| **`missao-service`** | `8001` | **Motor de Missões:** Serviço principal de negócio. Consulta o `github-service` via **OpenFeign**, aplica as regras de cálculo de XP (usando `@RefreshScope`), salva o resultado no banco H2 e publica o evento de conclusão no **Kafka**. |
| **`auditoria-service`** | `8003` | **Consumidor de Auditoria:** Escuta o tópico do **Kafka** em segundo plano (`@KafkaListener`) e grava um registro de auditoria toda vez que uma missão é concluída. |

---

## Aplicabilidade Prática dos Métodos e Endpoints

Abaixo está o papel prático de cada endpoint e método implementado no projeto:

### 1. `usuario-service` (`/usuario`)
- **`POST /usuario` (`save`)**: Cadastra um novo usuário na plataforma. Antes de salvar no banco H2, aplica o hash `BCryptPasswordEncoder` na senha para garantir que credenciais nunca fiquem expostas em texto puro.
- **`POST /usuario/login` (`authenticateAndGetToken`)**: Autentica o login e senha do usuário através do `AuthenticationManager` do Spring Security e devolve um **Access Token JWT** assinado (`HS256`) com validade de 30 minutos.
- **`GET /usuario/token/{token}` (`isValidToken`)**: Método de uso interno consumido pelo `gateway-service` a cada requisição para confirmar se o token JWT enviado pelo cliente ainda é válido e pertence a um usuário existente.

### 2. `github-service` (`/github`)
- **`GET /github/{username}` (`getOne`)**: Recebe o login de qualquer usuário do GitHub (ex.: `torvalds`), faz uma requisição HTTP externa para `https://api.github.com/users/{username}` e devolve apenas os dados relevantes mapeados no DTO (`GithubUserResponse`: login, nome, bio, total de repositórios públicos, seguidores e URL do perfil). Serve para isolar a API externa do restante da nossa arquitetura.

### 3. `missao-service` (`/missao`)
- **`POST /missao/{githubUsername}` (`save` / `getOne`)**: É o método principal do fluxo de negócio:
  1. Busca os dados do desenvolvedor no `github-service` de forma síncrona usando a interface declarativa `GithubClient` (**OpenFeign**).
  2. Verifica se o desenvolvedor possui pelo menos 5 repositórios públicos.
  3. Calcula o XP final (`calcularXpFinal`) multiplicando o XP base (`100`) pelo multiplicador configurado no `config-service`.
  4. Persiste a tentativa/conclusão da missão no banco de dados H2 (`MissaoDev`).
  5. Se a missão foi concluída, dispara uma mensagem `MissaoConcluidaEvent` no tópico `missao-concluida-topic` do **Kafka** para processamento assíncrono.
- **`GET /missao` (`getAll`)**: Lista todas as missões já validadas no sistema, permitindo filtrar opcionalmente por um desenvolvedor específico (`?githubUsername=torvalds`) para consultar seu histórico de progresso.

### 4. `auditoria-service` (`/auditoria`)
- **`ouvirAtualizacao(MissaoConcluidaEvent)` (`@KafkaListener`)**: Método consumidor que roda automaticamente assim que uma nova mensagem chega no tópico do Kafka. Ele desacopla o registro de auditoria do fluxo principal: mesmo que o `auditoria-service` fique fora do ar temporariamente, as missões continuam funcionando e os eventos são processados assim que o serviço volta.
- **`GET /auditoria` (`getAll`)**: Permite consultar todos os logs de XP recebidos via Kafka e salvos no banco H2 da auditoria (com filtro opcional por `githubUsername`).

### 5. `gateway-service` (`JwtAuthFilter` e `FallbackController`)
- **`JwtAuthFilter.filter()`**: Intercepta todas as chamadas que passam pela porta `8005`. Libera rotas públicas (`/usuario/**`) e exige um token Bearer válido nas rotas privadas (`/github/**`, `/missao/**`, `/auditoria/**`), barrando acessos não autorizados com `401 Unauthorized`.
- **`FallbackController` (`/fallback/github` e `/fallback/missao`)**: Quando o `github-service` ou o `missao-service` estão indisponíveis ou lentos, o **Circuit Breaker (Resilience4j)** abre o circuito e redireciona a chamada para estes métodos, que devolvem uma mensagem amigável de indisponibilidade em vez de travar o sistema.

---

## Tecnologias Utilizadas

- **Java 21**
- **Spring Cloud 2025.0.0** (Netflix Eureka, Config Server, Gateway WebFlux, OpenFeign, CircuitBreaker Resilience4j)
- **Spring Security** + **JWT (`jjwt`)** + **BCrypt**
- **Spring for Apache Kafka** (Mensageria assíncrona via Docker)
- **Spring Data JPA / Hibernate** + **H2 Database** (Banco em memória isolado por serviço)
- **SpringDoc OpenAPI 3 / Swagger UI** (Documentação interativa dos endpoints)
- **Spring Boot Actuator** + **Micrometer / Prometheus** (Monitoramento e métricas)
- **Lombok** (`@Builder`, `@Getter`, `@Setter`, `@EqualsAndHashCode`)

---

## Como Executar o Projeto

### 1. Subir o Kafka (Docker)
Na raiz do projeto, iniciar o Zookeeper e o Kafka:
```bash
docker compose up -d
```

### 2. Iniciar os Microsserviços
1. `registry-service` (`http://localhost:8761`)
2. `config-service` (`http://localhost:8006`)
3. `usuario-service` (`http://localhost:8004`)
4. `github-service` (`http://localhost:8002`)
5. `missao-service` (`http://localhost:8001`)
6. `auditoria-service` (`http://localhost:8003`)
7. `gateway-service` (`http://localhost:8005`)

### 3. Links Úteis com o Sistema Rodando
- **Painel do Eureka:** `http://localhost:8761`
- **Config Server (Propriedades da Missão):** `http://localhost:8006/missao-service/default`
- **Swagger UI (`github-service`):** `http://localhost:8002/swagger-ui/index.html`
- **Swagger UI (`missao-service`):** `http://localhost:8001/swagger-ui/index.html`
- **Swagger UI (`auditoria-service`):** `http://localhost:8003/swagger-ui/index.html`
- **Métricas Actuator / Prometheus (Gateway):** `http://localhost:8005/actuator/prometheus`