package br.com.dev.missao.repository;

import br.com.dev.missao.model.MissaoDev;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissaoRepository extends JpaRepository<MissaoDev, Long> {
    List<MissaoDev> findByGithubUsername(String githubUsername);
}
