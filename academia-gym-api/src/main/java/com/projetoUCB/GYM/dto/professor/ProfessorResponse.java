package com.projetoUCB.GYM.dto.professor;

public class ProfessorResponse {

    private Integer id;
    private String nome;
    private String email;
    private String telefone;
    private Integer modalidadeId;
    private String modalidadeNome;
    private Boolean ativo;

    public ProfessorResponse(Integer id, String nome, String email, String telefone,
                             Integer modalidadeId, String modalidadeNome, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.modalidadeId = modalidadeId;
        this.modalidadeNome = modalidadeNome;
        this.ativo = ativo;
    }

    public Integer getId() {
        return id;
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
