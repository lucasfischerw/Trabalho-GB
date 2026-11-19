package br.com.dev.auditoria.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import br.com.dev.auditoria.model.AuditoriaXp;
import br.com.dev.auditoria.service.AuditoriaService;
import br.com.dev.missao.evento.MissaoConcluidaEvent;

@Component
public class AuditoriaConsumer {

	@Autowired
	private AuditoriaService auditoriaService;

	@KafkaListener(topics = MissaoConcluidaEvent.TOPIC, groupId = "meu-grupo-consumidor")
	public void ouvirAtualizacao(MissaoConcluidaEvent evento) {
		System.out.println("Mensagem recebida do Kafka!");
		System.out.println("ID Missão: " + evento.getMissaoId());
		System.out.println("Desenvolvedor: " + evento.getGithubUsername());
		System.out.println("XP Ganho: " + evento.getXpGanho());
		System.out.println("Atualizado em: " + evento.getDataConclusao());

		AuditoriaXp auditoria = AuditoriaXp.builder()
				.missaoId(evento.getMissaoId())
				.githubUsername(evento.getGithubUsername())
				.tituloMissao(evento.getTituloMissao())
				.xpGanho(evento.getXpGanho())
				.linguagemDestaque(evento.getLinguagemDestaque())
				.dataConclusao(evento.getDataConclusao())
				.build();

		auditoriaService.save(auditoria);
	}
}
