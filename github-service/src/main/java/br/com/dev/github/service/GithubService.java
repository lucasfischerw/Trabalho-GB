package br.com.dev.github.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.dev.github.dto.GithubUserResponse;

@Service
public class GithubService {

	@Value("${github.api.url:https://api.github.com/users/}")
	private String githubApiUrl;

	@Autowired
	private RestTemplate restTemplate;

	public GithubUserResponse getOne(String username) {
		return restTemplate.getForObject(githubApiUrl + username, GithubUserResponse.class);
	}
}
