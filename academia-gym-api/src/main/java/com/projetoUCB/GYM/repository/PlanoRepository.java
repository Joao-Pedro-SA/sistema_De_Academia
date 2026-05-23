package com.projetoUCB.GYM.repository;

import com.projetoUCB.GYM.model.Plano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanoRepository extends JpaRepository<Plano, Integer> {
    Optional<Plano> findByNomeContainingIgnoreCase(String nome);
}
