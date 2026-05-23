package com.projetoUCB.GYM.service;

import com.projetoUCB.GYM.dto.relatorio.AlunoAtivoResponse;
import com.projetoUCB.GYM.dto.relatorio.InadimplenciaPendenteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<AlunoAtivoResponse> listarAlunosAtivos() {
        String sql = "SELECT * FROM vw_alunos_ativos";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new AlunoAtivoResponse(
                rs.getInt("aluno_id"),
                rs.getString("aluno_nome"),
                rs.getString("cpf"),
                rs.getString("email"),
                rs.getString("telefone"),
                rs.getString("plano_nome"),
                rs.getDate("data_inicio").toLocalDate(),
                rs.getDate("data_fim").toLocalDate(),
                rs.getInt("dias_restantes")
        ));
    }

    public List<InadimplenciaPendenteResponse> listarInadimplenciasPendentes() {
        String sql = "SELECT * FROM vw_inadimplencias_pendentes";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new InadimplenciaPendenteResponse(
                rs.getInt("inadimplencia_id"),
                rs.getInt("aluno_id"),
                rs.getString("aluno_nome"),
                rs.getString("email"),
                rs.getString("telefone"),
                rs.getString("plano_nome"),
                rs.getBigDecimal("valor"),
                rs.getInt("dias_atraso"),
                rs.getDate("data_registro").toLocalDate()
        ));
    }
}
