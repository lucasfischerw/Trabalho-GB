package br.com.dev.auditoria.kafka;

import br.com.dev.auditoria.dto.MissaoConcluidaEvent;
import br.com.dev.auditoria.model.AuditoriaXp;
import br.com.dev.auditoria.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditoriaConsumer {

    private final AuditoriaRepository auditoriaRepository;

    @KafkaListener(topics = "missao-concluida-topic", groupId = "liga-auditoria-group")
    public void consumirMissaoConcluida(MissaoConcluidaEvent event) {
        log.info("Evento recebido do Kafka para o dev: {} com XP: {}", event.getGithubUsername(), event.getXpGanho());

        AuditoriaXp auditoria = new AuditoriaXp(
                null,
                event.getMissaoId(),
                event.getGithubUsername(),
                event.getTituloMissao(),
                event.getXpGanho(),
                event.getLinguagemDestaque(),
                event.getDataConclusao()
        );

        auditoriaRepository.save(auditoria);
        log.info("Auditoria de XP salva com sucesso no banco de dados para a missao ID: {}", event.getMissaoId());
    }
}