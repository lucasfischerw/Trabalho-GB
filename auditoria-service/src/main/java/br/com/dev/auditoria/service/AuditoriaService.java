package br.com.dev.auditoria.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.dev.auditoria.model.AuditoriaXp;
import br.com.dev.auditoria.repository.AuditoriaRepository;

@Service
public class AuditoriaService {

	@Autowired
	private AuditoriaRepository auditoriaRepository;

	public AuditoriaXp save(AuditoriaXp auditoria) {
		return auditoriaRepository.save(auditoria);
	}

	public List<AuditoriaXp> getAll(String githubUsername) {
		if (githubUsername != null) {
			return auditoriaRepository.findByGithubUsername(githubUsername);
		}
		return auditoriaRepository.findAll();
	}
}
