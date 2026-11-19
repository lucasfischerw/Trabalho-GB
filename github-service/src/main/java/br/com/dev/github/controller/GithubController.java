package br.com.dev.github.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.dev.github.dto.GithubUserResponse;
import br.com.dev.github.service.GithubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RefreshScope
@RestController
@RequestMapping("/github")
@Tag(name = "GitHub Integração", description = "Operações para consulta de perfil público no GitHub")
public class GithubController {

	@Autowired
	private GithubService githubService;

	@GetMapping("/{username}")
	@Operation(summary = "Obter perfil do GitHub", description = "Obtém os dados públicos de um desenvolvedor na API externa do GitHub")
	@Parameters({
			@Parameter(name = "username", description = "Login do usuário no GitHub", example = "torvalds", required = true)
	})
	@ApiResponse(responseCode = "200", description = "Dados do perfil retornados com sucesso")
	public GithubUserResponse getOne(@PathVariable String username) {
		return githubService.getOne(username);
	}
}
