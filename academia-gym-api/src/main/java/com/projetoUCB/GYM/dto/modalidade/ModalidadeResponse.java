package com.projetoUCB.GYM.dto.modalidade;

public class ModalidadeResponse {

    private Integer id;
    private String nome;
    private String descricao;
    private Boolean ativa;

    public ModalidadeResponse(Integer id, String nome, String descricao, Boolean ativa) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.ativa = ativa;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Boolean getAtiva() {
        return ativa;
    }
}
