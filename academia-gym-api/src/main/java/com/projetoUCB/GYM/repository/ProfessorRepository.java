package com.projetoUCB.GYM.repository;

import com.projetoUCB.GYM.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Integer> {
    Optional<Professor> findByNomeContainingIgnoreCase(String nome);
}
