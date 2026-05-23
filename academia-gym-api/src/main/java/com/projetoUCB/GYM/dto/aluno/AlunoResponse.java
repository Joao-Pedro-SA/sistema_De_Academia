package com.projetoUCB.GYM.dto.aluno;

import java.time.LocalDate;

public class AlunoResponse {

    private Integer id;
    private String codigoAluno;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private String sexo;
    private String endereco;
    private Boolean ativo;

    public AlunoResponse(Integer id, String codigoAluno, String nome, String cpf, String email, String telefone,
                         LocalDate dataNascimento, String sexo, String endereco, Boolean ativo) {
        this.id = id;
        this.codigoAluno = codigoAluno;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.endereco = endereco;
        this.ativo = ativo;
    }

    public Integer getId() {
        return id;
    }

    public String getCodigoAluno() {
        return codigoAluno;
    }

    public String getNome() {
        return nome;
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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public String getEndereco() {
        return endereco;
    }

    public Boolean getAtivo() {
        return ativo;
    }
}
