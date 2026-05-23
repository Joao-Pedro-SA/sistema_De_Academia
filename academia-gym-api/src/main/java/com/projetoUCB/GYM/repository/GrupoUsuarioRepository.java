package com.projetoUCB.GYM.repository;

import com.projetoUCB.GYM.model.GrupoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrupoUsuarioRepository extends JpaRepository<GrupoUsuario, Integer> {

    Optional<GrupoUsuario> findByNomeContainingIgnoreCase(String nome);
}
