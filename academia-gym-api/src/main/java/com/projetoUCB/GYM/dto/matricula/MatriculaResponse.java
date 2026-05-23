package com.projetoUCB.GYM.dto.matricula;

import java.time.LocalDate;

public class MatriculaResponse {

    private Integer id;
    private String codigoMatricula;
    private Integer alunoId;
    private String alunoNome;
    private Integer planoId;
    private String planoNome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String status;

    public MatriculaResponse(Integer id, String codigoMatricula, Integer alunoId, String alunoNome,
                             Integer planoId, String planoNome, LocalDate dataInicio,
                             LocalDate dataFim, String status) {
        this.id = id;
        this.codigoMatricula = codigoMatricula;
        this.alunoId = alunoId;
        this.alunoNome = alunoNome;
        this.planoId = planoId;
        this.planoNome = planoNome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getCodigoMatricula() {
        return codigoMatricula;
    }

    public Integer getAlunoId() {
        return alunoId;
    }

    public String getAlunoNome() {
        return alunoNome;
    }

    public Integer getPlanoId() {
        return planoId;
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

    public String getStatus() {
        return status;
    }
}
