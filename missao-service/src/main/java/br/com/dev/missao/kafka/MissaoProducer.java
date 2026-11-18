package br.com.dev.missao.kafka;

import br.com.dev.missao.dto.MissaoConcluidaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissaoProducer {

    private static final String TOPICO = "missao-concluida-topic";
    private final KafkaTemplate<String, MissaoConcluidaEvent> kafkaTemplate;

    public void enviarEventoMissaoConcluida(MissaoConcluidaEvent event) {
        log.info("Publicando evento de missao concluida no Kafka para o dev: {}", event.getGithubUsername());
        kafkaTemplate.send(TOPICO, event.getGithubUsername(), event);
    }
}