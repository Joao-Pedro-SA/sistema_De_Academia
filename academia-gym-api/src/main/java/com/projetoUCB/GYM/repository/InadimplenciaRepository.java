package com.projetoUCB.GYM.repository;

import com.projetoUCB.GYM.model.Inadimplencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InadimplenciaRepository extends JpaRepository<Inadimplencia, Integer> {
    List<Inadimplencia> findByStatus(String status);
}
