package br.com.dev.missao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.dev.missao.model.MissaoDev;
import br.com.dev.missao.service.MissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/missao")
@Tag(name = "Missão", description = "Operações básicas para validação de missões")
public class MissaoController {

	@Autowired
	private MissaoService missaoService;

	@PostMapping("/{githubUsername}")
	@Operation(summary = "Validar e salvar missão", description = "Consulta o GitHub via OpenFeign, calcula XP e envia evento ao Kafka")
	@ApiResponse(responseCode = "200", description = "Missão processada com sucesso")
	public MissaoDev getOne(@PathVariable String githubUsername) {
		return missaoService.save(githubUsername);
	}

	@GetMapping
	@Operation(summary = "Obter lista de missões", description = "Obtém a lista de missões podendo filtrar por githubUsername")
	@Parameters({
			@Parameter(name = "githubUsername", description = "Login do desenvolvedor no GitHub", example = "torvalds", required = false)
	})
	@ApiResponse(responseCode = "200", description = "Lista de missões")
	public List<MissaoDev> getAll(@RequestParam(required = false) String githubUsername) {
		return missaoService.getAll(githubUsername);
	}
}
