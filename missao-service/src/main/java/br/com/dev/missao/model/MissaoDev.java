package br.com.dev.missao.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_missao_dev")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissaoDev {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String githubUsername;
    private String tituloMissao;
    private Integer repositoriosMinimos;
    private Integer repositoriosAtuaisNoGithub;
    private Integer xpBase;
    private Integer xpFinalCalculado;
    private String linguagemDestaqueTemporada;
    private boolean concluida;
    private LocalDateTime dataValidacao;
}