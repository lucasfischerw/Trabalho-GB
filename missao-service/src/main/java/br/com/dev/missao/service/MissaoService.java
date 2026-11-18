package br.com.dev.missao.service;

import br.com.dev.missao.client.GithubClient;
import br.com.dev.missao.dto.GithubUserDTO;
import br.com.dev.missao.dto.MissaoConcluidaEvent;
import br.com.dev.missao.kafka.MissaoProducer;
import br.com.dev.missao.model.MissaoDev;
import br.com.dev.missao.repository.MissaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RefreshScope
@RequiredArgsConstructor
@Slf4j
public class MissaoService {

    private final MissaoRepository missaoRepository;
    private final GithubClient githubClient;
    private final MissaoProducer missaoProducer;

    @Value("${gamification.xp-multiplier:2}")
    private Integer xpMultiplier;

    @Value("${gamification.featured-language:Java}")
    private String featuredLanguage;

    public MissaoDev validarEConcluirMissao(String githubUsername, String tituloMissao, Integer repositoriosMinimos, Integer xpBase) {
        // 1. Chama o github-service via OpenFeign
        GithubUserDTO githubUser = githubClient.buscarPerfilGithub(githubUsername);
        int reposAtuais = (githubUser != null && githubUser.getPublicRepos() != null) ? githubUser.getPublicRepos() : 0;

        boolean cumpriuMeta = reposAtuais >= repositoriosMinimos;
        int xpCalculado = calcularXpFinal(cumpriuMeta, xpBase);

        MissaoDev missao = new MissaoDev(
                null,
                githubUsername,
                tituloMissao,
                repositoriosMinimos,
                reposAtuais,
                xpBase,
                xpCalculado,
                featuredLanguage,
                cumpriuMeta,
                LocalDateTime.now()
        );

        MissaoDev missaoSalva = missaoRepository.save(missao);

        // 2. Se cumpriu a missão, publica evento assíncrono no Kafka
        if (cumpriuMeta) {
            dispararEventoKafka(missaoSalva);
        }

        return missaoSalva;
    }

    // Padrão Kent Beck (Aula 10b): Guard Clause sem if-else aninhado
    private int calcularXpFinal(boolean cumpriuMeta, Integer xpBase) {
        if (!cumpriuMeta) {
            return 0;
        }
        return xpBase * xpMultiplier;
    }

    private void dispararEventoKafka(MissaoDev missaoSalva) {
        try {
            MissaoConcluidaEvent event = new MissaoConcluidaEvent(
                    missaoSalva.getId(),
                    missaoSalva.getGithubUsername(),
                    missaoSalva.getTituloMissao(),
                    missaoSalva.getXpFinalCalculado(),
                    missaoSalva.getLinguagemDestaqueTemporada(),
                    missaoSalva.getDataValidacao()
            );
            missaoProducer.enviarEventoMissaoConcluida(event);
        } catch (Exception e) {
            log.warn("Missao salva no banco, mas o Kafka ainda nao esta ativo: {}", e.getMessage());
        }
    }

    public List<MissaoDev> listarTodas() {
        return missaoRepository.findAll();
    }

    public List<MissaoDev> listarPorDev(String githubUsername) {
        return missaoRepository.findByGithubUsername(githubUsername);
    }
}