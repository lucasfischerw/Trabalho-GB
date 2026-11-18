package br.com.dev.auditoria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_auditoria_xp")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaXp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long missaoId;
    private String githubUsername;
    private String tituloMissao;
    private Integer xpGanho;
    private String linguagemDestaque;
    private LocalDateTime dataConclusao;
}