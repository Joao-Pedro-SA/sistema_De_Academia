package com.projetoUCB.GYM.repository;

import com.projetoUCB.GYM.model.Modalidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModalidadeRepository extends JpaRepository<Modalidade, Integer> {
    Optional<Modalidade> findByNomeContainingIgnoreCase(String nome);
}
