package br.com.dev.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GithubUserResponse {

    private String login;
    private String name;
    private String bio;

    @JsonProperty("public_repos")
    private Integer publicRepos;

    private Integer followers;
    private Integer following;

    @JsonProperty("html_url")
    private String htmlUrl;
}