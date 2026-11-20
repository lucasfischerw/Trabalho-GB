package br.com.dev.auditoria.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.dev.auditoria.model.AuditoriaXp;

@Repository
public interface AuditoriaRepository extends JpaRepository<AuditoriaXp, UUID> {
	List<AuditoriaXp> findByGithubUsername(String githubUsername);
}
