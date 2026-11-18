package br.com.dev.auditoria.controller;

import br.com.dev.auditoria.model.AuditoriaXp;
import br.com.dev.auditoria.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaRepository auditoriaRepository;

    @GetMapping
    public List<AuditoriaXp> listarTodasAuditorias() {
        return auditoriaRepository.findAll();
    }

    @GetMapping("/dev/{githubUsername}")
    public List<AuditoriaXp> listarPorDev(@PathVariable String githubUsername) {
        return auditoriaRepository.findByGithubUsername(githubUsername);
    }
}