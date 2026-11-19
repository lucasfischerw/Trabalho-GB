package br.com.dev.auditoria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.dev.auditoria.model.AuditoriaXp;
import br.com.dev.auditoria.service.AuditoriaService;

@RestController
@RequestMapping("/auditoria")
public class AuditoriaController {

	@Autowired
	private AuditoriaService auditoriaService;

	@GetMapping
	public List<AuditoriaXp> getAll(@RequestParam(required = false) String githubUsername) {
		return auditoriaService.getAll(githubUsername);
	}

	@GetMapping("/dev/{githubUsername}")
	public List<AuditoriaXp> getByDev(@PathVariable String githubUsername) {
		return auditoriaService.getAll(githubUsername);
	}
}
