package com.projetoUCB.GYM.dto.grupoUsuario;

public class GrupoUsuarioResponse {

    private Integer id;
    private String codigoGrupo;
    private String nome;
    private String descricao;
    private Boolean ativo;

    public GrupoUsuarioResponse(Integer id, String codigoGrupo, String nome, String descricao, Boolean ativo) {
        this.id = id;
        this.codigoGrupo = codigoGrupo;
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public Integer getId() {
        return id;
    }

    public String getCodigoGrupo() {
        return codigoGrupo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Boolean getAtivo() {
        return ativo;
    }
}
