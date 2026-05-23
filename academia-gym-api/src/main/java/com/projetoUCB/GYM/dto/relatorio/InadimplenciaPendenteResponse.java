package com.projetoUCB.GYM.dto.relatorio;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InadimplenciaPendenteResponse {

    private Integer inadimplenciaId;
    private Integer alunoId;
    private String alunoNome;
    private String email;
    private String telefone;
    private String planoNome;
    private BigDecimal valor;
    private Integer diasAtraso;
    private LocalDate dataRegistro;

    public InadimplenciaPendenteResponse(Integer inadimplenciaId, Integer alunoId, String alunoNome, String email,
                                         String telefone, String planoNome, BigDecimal valor,
                                         Integer diasAtraso, LocalDate dataRegistro) {
        this.inadimplenciaId = inadimplenciaId;
        this.alunoId = alunoId;
        this.alunoNome = alunoNome;
        this.email = email;
        this.telefone = telefone;
        this.planoNome = planoNome;
        this.valor = valor;
        this.diasAtraso = diasAtraso;
        this.dataRegistro = dataRegistro;
    }

    public Integer getInadimplenciaId() {
        return inadimplenciaId;
    }

    public Integer getAlunoId() {
        return alunoId;
    }

    public String getAlunoNome() {
        return alunoNome;
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

    public BigDecimal getValor() {
        return valor;
    }

    public Integer getDiasAtraso() {
        return diasAtraso;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }
}
