package br.com.dev.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/missao")
    public String missaoFallback() {
        return "⚠️ O Serviço de Missões está temporariamente indisponível (Circuit Breaker Ativo). Tente novamente mais tarde!";
    }
}