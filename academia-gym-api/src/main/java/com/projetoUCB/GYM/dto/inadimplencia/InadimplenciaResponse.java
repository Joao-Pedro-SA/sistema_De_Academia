package com.projetoUCB.GYM.dto.inadimplencia;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InadimplenciaResponse {

    private Integer id;
    private String codigoInadimplencia;
    private Integer matriculaId;
    private String alunoNome;
    private BigDecimal valor;
    private Integer diasAtraso;
    private String status;
    private LocalDate dataRegistro;

    public InadimplenciaResponse(Integer id, String codigoInadimplencia, Integer matriculaId,
                                 String alunoNome, BigDecimal valor, Integer diasAtraso,
                                 String status, LocalDate dataRegistro) {
        this.id = id;
        this.codigoInadimplencia = codigoInadimplencia;
        this.matriculaId = matriculaId;
        this.alunoNome = alunoNome;
        this.valor = valor;
        this.diasAtraso = diasAtraso;
        this.status = status;
        this.dataRegistro = dataRegistro;
    }

    public Integer getId() {
        return id;
    }

    public String getCodigoInadimplencia() {
        return codigoInadimplencia;
    }

    public Integer getMatriculaId() {
        return matriculaId;
    }

    public String getAlunoNome() {
        return alunoNome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Integer getDiasAtraso() {
        return diasAtraso;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }
}
