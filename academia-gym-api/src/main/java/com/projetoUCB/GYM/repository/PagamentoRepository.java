package com.projetoUCB.GYM.repository;

import com.projetoUCB.GYM.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
    List<Pagamento> findByMatriculaId(Integer matriculaId);
}
