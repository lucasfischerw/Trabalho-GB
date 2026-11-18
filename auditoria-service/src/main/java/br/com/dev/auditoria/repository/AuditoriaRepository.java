package br.com.dev.auditoria.repository;

import br.com.dev.auditoria.model.AuditoriaXp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<AuditoriaXp, Long> {
    List<AuditoriaXp> findByGithubUsername(String githubUsername);
}