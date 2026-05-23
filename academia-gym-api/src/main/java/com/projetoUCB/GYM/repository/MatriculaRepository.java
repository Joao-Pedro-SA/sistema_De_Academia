package com.projetoUCB.GYM.repository;

import com.projetoUCB.GYM.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {

    List<Matricula> findByAlunoId(Integer alunoId);

    List<Matricula> findByStatus(String status);

    Optional<Matricula> findTopByAlunoIdOrderByIdDesc(Integer alunoId);

    @Modifying
    @Query(value = "UPDATE matriculas SET status = status WHERE status = 'ATIVA' AND data_fim < CURDATE()", nativeQuery = true)
    int atualizarMatriculasVencidas();
}
