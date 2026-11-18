package br.com.dev.auditoria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissaoConcluidaEvent {
    private Long missaoId;
    private String githubUsername;
    private String tituloMissao;
    private Integer xpGanho;
    private String linguagemDestaque;
    private LocalDateTime dataConclusao;
}