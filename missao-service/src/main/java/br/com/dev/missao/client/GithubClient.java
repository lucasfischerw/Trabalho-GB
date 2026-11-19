package br.com.dev.missao.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.dev.missao.dto.GithubUserDTO;

@FeignClient(name = "GITHUB-SERVICE")
public interface GithubClient {

	@GetMapping("/github/{username}")
	GithubUserDTO getOne(@PathVariable("username") String username);
}
