package br.com.dev.missao.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import br.com.dev.missao.client.GithubClient;
import br.com.dev.missao.dto.GithubUserDTO;
import br.com.dev.missao.evento.MissaoConcluidaEvent;
import br.com.dev.missao.model.MissaoDev;
import br.com.dev.missao.repository.MissaoRepository;

@Service
@RefreshScope
public class MissaoService {

	private static final int REPOSITORIOS_MINIMOS = 5;
	private static final int XP_BASE = 100;

	@Autowired
	private GithubClient githubClient;

	@Autowired
	private MissaoRepository missaoRepository;

	@Autowired
	private KafkaTemplate<String, MissaoConcluidaEvent> kafkaTemplate;

	@Value("${gamification.xp-multiplier:2}")
	private Integer xpMultiplier;

	@Value("${gamification.featured-language:Java}")
	private String featuredLanguage;

	public MissaoDev save(String githubUsername) {
		GithubUserDTO githubDto = githubClient.getOne(githubUsername);

		boolean isMissaoConcluida = githubDto.getPublicRepos() >= REPOSITORIOS_MINIMOS;
		int xpCalculado = calcularXpFinal(isMissaoConcluida);

		MissaoDev missao = MissaoDev.builder()
				.githubUsername(githubUsername)
				.tituloMissao("Desafio Microsservicos")
				.repositoriosMinimos(REPOSITORIOS_MINIMOS)
				.repositoriosAtuaisNoGithub(githubDto.getPublicRepos())
				.xpBase(XP_BASE)
				.xpFinalCalculado(xpCalculado)
				.linguagemDestaqueTemporada(featuredLanguage)
				.concluida(isMissaoConcluida)
				.dataValidacao(LocalDateTime.now())
				.build();

		missao = missaoRepository.save(missao);

		if (isMissaoConcluida) {
			MissaoConcluidaEvent missaoEvent = MissaoConcluidaEvent.builder()
					.missaoId(missao.getId())
					.githubUsername(missao.getGithubUsername())
					.tituloMissao(missao.getTituloMissao())
					.xpGanho(missao.getXpFinalCalculado())
					.linguagemDestaque(missao.getLinguagemDestaqueTemporada())
					.dataConclusao(missao.getDataValidacao())
					.build();

			kafkaTemplate.send(MissaoConcluidaEvent.TOPIC, missao.getId().toString(), missaoEvent);
		}

		return missao;
	}

	private int calcularXpFinal(boolean isMissaoConcluida) {
		if (!isMissaoConcluida) {
			return 0;
		}
		return XP_BASE * xpMultiplier;
	}

	public List<MissaoDev> getAll(String githubUsername) {
		if (githubUsername != null) {
			return missaoRepository.findByGithubUsername(githubUsername);
		}
		return missaoRepository.findAll();
	}
}
