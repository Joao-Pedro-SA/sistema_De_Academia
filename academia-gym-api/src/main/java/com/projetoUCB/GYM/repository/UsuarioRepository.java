package com.projetoUCB.GYM.repository;

import com.projetoUCB.GYM.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByNomeContainingIgnoreCase(String nome);

    Optional<Usuario> findByEmail(String email);
}
