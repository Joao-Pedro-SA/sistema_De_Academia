package com.projetoUCB.GYM.service;

import com.projetoUCB.GYM.dto.matricula.MatriculaRequest;
import com.projetoUCB.GYM.dto.matricula.MatriculaResponse;
import com.projetoUCB.GYM.model.Matricula;
import com.projetoUCB.GYM.repository.MatriculaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Types;
import java.util.List;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MatriculaResponse saveMatricula(MatriculaRequest request) {
        String mensagem = chamarProcedureRegistrarMatricula(request);

        if (!mensagem.startsWith("Matrícula cadastrada")) {
            throw new RuntimeException(mensagem);
        }

        Matricula matricula = matriculaRepository.findTopByAlunoIdOrderByIdDesc(request.getAlunoId())
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada após cadastro"));

        return toResponse(matricula);
    }

    public List<MatriculaResponse> getMatriculas() {
        return matriculaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<MatriculaResponse> getMatriculasByStatus(String status) {
        return matriculaRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MatriculaResponse getMatriculaById(int id) {
        return toResponse(buscarMatriculaPorId(id));
    }

    @Transactional
    public void cancelarMatricula(int id) {
        Matricula matricula = buscarMatriculaPorId(id);
        matricula.setStatus("CANCELADA");
        matriculaRepository.save(matricula);
    }

    @Transactional
    public int verificarMatriculasVencidas() {
        return matriculaRepository.atualizarMatriculasVencidas();
    }

    private String chamarProcedureRegistrarMatricula(MatriculaRequest request) {
        String sql = "{call sp_registrar_matricula(?, ?, ?, ?)}";

        return jdbcTemplate.execute((ConnectionCallback<String>) connection -> {
            CallableStatement callableStatement = connection.prepareCall(sql);
            callableStatement.setInt(1, request.getAlunoId());
            callableStatement.setInt(2, request.getPlanoId());
            callableStatement.setString(3, request.getFormaPagamento() == null ? "PIX" : request.getFormaPagamento().toUpperCase());
            callableStatement.registerOutParameter(4, Types.VARCHAR);
            callableStatement.execute();
            return callableStatement.getString(4);
        });
    }

    private Matricula buscarMatriculaPorId(int id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));
    }

    private MatriculaResponse toResponse(Matricula matricula) {
        return new MatriculaResponse(
                matricula.getId(),
                matricula.getCodigoMatricula(),
                matricula.getAluno().getId(),
                matricula.getAluno().getNome(),
                matricula.getPlano().getId(),
                matricula.getPlano().getNome(),
                matricula.getDataInicio(),
                matricula.getDataFim(),
                matricula.getStatus()
        );
    }
}
