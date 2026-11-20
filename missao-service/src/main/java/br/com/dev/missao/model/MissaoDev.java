package br.com.dev.missao.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class MissaoDev {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private String githubUsername;
	private String tituloMissao;
	private Integer repositoriosMinimos;
	private Integer repositoriosAtuaisNoGithub;
	private Integer xpBase;
	private Integer xpFinalCalculado;
	private String linguagemDestaqueTemporada;
	private boolean concluida;
	private LocalDateTime dataValidacao;
}
