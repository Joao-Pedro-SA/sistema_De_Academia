package com.projetoUCB.GYM.dto.usuario;

public class UsuarioRequest {

    private String nome;
    private String email;
    private String telefone;
    private String cargo;
    private Integer grupoUsuarioId;
    private Integer modalidadeId;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Integer getGrupoUsuarioId() {
        return grupoUsuarioId;
    }

    public void setGrupoUsuarioId(Integer grupoUsuarioId) {
        this.grupoUsuarioId = grupoUsuarioId;
    }

    public Integer getModalidadeId() {
        return modalidadeId;
    }

    public void setModalidadeId(Integer modalidadeId) {
        this.modalidadeId = modalidadeId;
    }
}
