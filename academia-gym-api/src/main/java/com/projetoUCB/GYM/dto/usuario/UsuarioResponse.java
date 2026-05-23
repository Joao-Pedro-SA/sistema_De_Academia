package com.projetoUCB.GYM.dto.usuario;

public class UsuarioResponse {

    private Integer id;
    private String codigoUsuario;
    private String nome;
    private String email;
    private String telefone;
    private String cargo;
    private Integer grupoUsuarioId;
    private String grupoUsuarioNome;
    private Integer modalidadeId;
    private String modalidadeNome;
    private Boolean ativo;

    public UsuarioResponse(Integer id, String codigoUsuario, String nome, String email, String telefone,
                           String cargo, Integer grupoUsuarioId, String grupoUsuarioNome,
                           Integer modalidadeId, String modalidadeNome, Boolean ativo) {
        this.id = id;
        this.codigoUsuario = codigoUsuario;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cargo = cargo;
        this.grupoUsuarioId = grupoUsuarioId;
        this.grupoUsuarioNome = grupoUsuarioNome;
        this.modalidadeId = modalidadeId;
        this.modalidadeNome = modalidadeNome;
        this.ativo = ativo;
    }

    public Integer getId() {
        return id;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCargo() {
        return cargo;
    }

    public Integer getGrupoUsuarioId() {
        return grupoUsuarioId;
    }

    public String getGrupoUsuarioNome() {
        return grupoUsuarioNome;
    }

    public Integer getModalidadeId() {
        return modalidadeId;
    }

    public String getModalidadeNome() {
        return modalidadeNome;
    }

    public Boolean getAtivo() {
        return ativo;
    }
}
