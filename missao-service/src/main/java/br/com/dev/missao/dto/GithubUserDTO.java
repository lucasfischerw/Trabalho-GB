package br.com.dev.missao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GithubUserDTO {
    private String login;
    private String name;

    @JsonProperty("public_repos")
    private Integer publicRepos;

    private Integer followers;
}