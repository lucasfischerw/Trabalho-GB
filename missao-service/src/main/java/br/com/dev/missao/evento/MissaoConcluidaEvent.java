package br.com.dev.missao.evento;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MissaoConcluidaEvent {

	public static final String TOPIC = "missao-concluida-topic";

	private UUID missaoId;
	private String githubUsername;
	private String tituloMissao;
	private Integer xpGanho;
	private String linguagemDestaque;
	private LocalDateTime dataConclusao;
}
