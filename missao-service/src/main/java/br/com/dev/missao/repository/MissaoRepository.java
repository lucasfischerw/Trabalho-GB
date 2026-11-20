package br.com.dev.missao.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.dev.missao.model.MissaoDev;

@Repository
public interface MissaoRepository extends JpaRepository<MissaoDev, UUID> {
	List<MissaoDev> findByGithubUsername(String githubUsername);
}
