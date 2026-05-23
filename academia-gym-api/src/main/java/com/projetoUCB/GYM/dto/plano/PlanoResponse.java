package com.projetoUCB.GYM.dto.plano;

import java.math.BigDecimal;

public class PlanoResponse {

    private Integer id;
    private String nome;
    private Integer duracaoDias;
    private BigDecimal valor;
    private Boolean ativo;

    public PlanoResponse(Integer id, String nome, Integer duracaoDias, BigDecimal valor, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.duracaoDias = duracaoDias;
        this.valor = valor;
        this.ativo = ativo;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getDuracaoDias() {
        return duracaoDias;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public Boolean getAtivo() {
        return ativo;
    }
}
