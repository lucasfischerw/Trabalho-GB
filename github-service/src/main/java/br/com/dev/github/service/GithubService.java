package br.com.dev.github.service;

import br.com.dev.github.dto.GithubUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubService {

    @Value("${github.api.url}")
    private String githubApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public GithubUserResponse buscarPerfilDev(String username) {
        String url = githubApiUrl + username;
        return restTemplate.getForObject(url, GithubUserResponse.class);
    }
}