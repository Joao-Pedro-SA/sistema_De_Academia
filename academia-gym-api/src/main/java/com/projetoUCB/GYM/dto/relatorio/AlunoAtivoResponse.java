package com.projetoUCB.GYM.dto.relatorio;

import java.time.LocalDate;

public class AlunoAtivoResponse {

    private Integer alunoId;
    private String alunoNome;
    private String cpf;
    private String email;
    private String telefone;
    private String planoNome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Integer diasRestantes;

    public AlunoAtivoResponse(Integer alunoId, String alunoNome, String cpf, String email, String telefone,
                              String planoNome, LocalDate dataInicio, LocalDate dataFim, Integer diasRestantes) {
        this.alunoId = alunoId;
        this.alunoNome = alunoNome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.planoNome = planoNome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.diasRestantes = diasRestantes;
    }

    public Integer getAlunoId() {
        return alunoId;
    }

    public String getAlunoNome() {
        return alunoNome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getPlanoNome() {
        return planoNome;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public Integer getDiasRestantes() {
        return diasRestantes;
    }
}
