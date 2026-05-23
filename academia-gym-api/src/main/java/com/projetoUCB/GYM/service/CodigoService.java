package com.projetoUCB.GYM.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class CodigoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String gerarCodigoGrupo() {
        return jdbcTemplate.queryForObject("SELECT fn_gerar_codigo_grupo()", String.class);
    }

    public String gerarCodigoUsuario() {
        return jdbcTemplate.queryForObject("SELECT fn_gerar_codigo_usuario()", String.class);
    }

    public String gerarCodigoAluno() {
        return jdbcTemplate.queryForObject("SELECT fn_gerar_codigo_aluno()", String.class);
    }

    public String gerarCodigoPagamento() {
        return jdbcTemplate.queryForObject("SELECT fn_gerar_codigo_pagamento()", String.class);
    }
}
