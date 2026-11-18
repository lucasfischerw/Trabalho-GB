package br.com.dev.missao.client;

import br.com.dev.missao.dto.GithubUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "GITHUB-SERVICE")
public interface GithubClient {

    @GetMapping("/github/{username}")
    GithubUserDTO buscarPerfilGithub(@PathVariable("username") String username);
}