package br.com.dev.github.controller;

import br.com.dev.github.dto.GithubUserResponse;
import br.com.dev.github.service.GithubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/github")
@RequiredArgsConstructor
@Tag(name = "GitHub Integração", description = "Endpoints para consulta de dados públicos do desenvolvedor no GitHub")
public class GithubController {

    private final GithubService githubService;

    @GetMapping("/{username}")
    @Operation(summary = "Busca o perfil público de um desenvolvedor no GitHub")
    public GithubUserResponse buscarPerfil(@PathVariable String username) {
        return githubService.buscarPerfilDev(username);
    }
}