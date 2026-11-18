package br.com.dev.missao.controller;

import br.com.dev.missao.model.MissaoDev;
import br.com.dev.missao.service.MissaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/missao")
@RefreshScope
@RequiredArgsConstructor
@Tag(name = "Missões da Liga", description = "Validação de missões via GitHub (OpenFeign), cálculo de XP (@RefreshScope) e publicação no Kafka")
public class MissaoController {

    private final MissaoService missaoService;

    @Value("${gamification.xp-multiplier:2}")
    private Integer xpMultiplier;

    @Value("${gamification.featured-language:Java}")
    private String featuredLanguage;

    @PostMapping("/validar")
    @Operation(summary = "Valida uma missão consultando o GitHub via OpenFeign e publica evento no Kafka se concluída")
    public MissaoDev validarMissao(
            @RequestParam String githubUsername,
            @RequestParam(defaultValue = "Desafio Semanal Microsservicos") String tituloMissao,
            @RequestParam(defaultValue = "5") Integer repositoriosMinimos,
            @RequestParam(defaultValue = "100") Integer xpBase) {

        return missaoService.validarEConcluirMissao(githubUsername, tituloMissao, repositoriosMinimos, xpBase);
    }

    @GetMapping
    @Operation(summary = "Lista todas as missões validadas")
    public List<MissaoDev> listarTodas() {
        return missaoService.listarTodas();
    }

    @GetMapping("/dev/{githubUsername}")
    @Operation(summary = "Lista o histórico de missões de um desenvolvedor")
    public List<MissaoDev> listarPorDev(@PathVariable String githubUsername) {
        return missaoService.listarPorDev(githubUsername);
    }

    @GetMapping("/temporada-config")
    @Operation(summary = "Exibe as configurações ativas vindas do Config Server (@RefreshScope)")
    public String verConfiguracaoTemporada() {
        return "Linguagem em destaque: " + featuredLanguage + " | Multiplicador de XP: " + xpMultiplier;
    }
}