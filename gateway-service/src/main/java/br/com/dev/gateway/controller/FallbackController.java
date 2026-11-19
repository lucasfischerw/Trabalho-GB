package br.com.dev.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

	@GetMapping("/github")
	public String githubServiceFallback() {
		return "Service GitHub está indisponível. Tente mais tarde.";
	}

	@GetMapping("/missao")
	public String missaoServiceGetFallback() {
		return "Service missão está indisponível. Tente mais tarde.";
	}

	@PostMapping("/missao")
	public String missaoServicePostFallback() {
		return "Service missão está indisponível. Tente mais tarde.";
	}
}
