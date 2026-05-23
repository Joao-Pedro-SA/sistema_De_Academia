package com.projetoUCB.GYM.repository;

import com.projetoUCB.GYM.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Integer> {
    Optional<Aluno> findByNomeContainingIgnoreCase(String nome);
}
