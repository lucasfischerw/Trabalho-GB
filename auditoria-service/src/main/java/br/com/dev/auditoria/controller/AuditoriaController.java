package br.com.dev.auditoria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.dev.auditoria.model.AuditoriaXp;
import br.com.dev.auditoria.service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auditoria")
@Tag(name = "Auditoria", description = "Operações básicas para consulta de auditoria de XP recebida via Kafka")
public class AuditoriaController {

	@Autowired
	private AuditoriaService auditoriaService;

	@GetMapping
	@Operation(summary = "Obter lista de auditorias", description = "Obtém a lista de registros de auditoria podendo filtrar por githubUsername")
	@Parameters({
			@Parameter(name = "githubUsername", description = "Login do desenvolvedor no GitHub", example = "torvalds", required = false)
	})
	@ApiResponse(responseCode = "200", description = "Lista de registros de auditoria")
	public List<AuditoriaXp> getAll(@RequestParam(required = false) String githubUsername) {
		return auditoriaService.getAll(githubUsername);
	}
}
