package com.projetoUCB.GYM.controller;

import com.projetoUCB.GYM.dto.usuario.UsuarioRequest;
import com.projetoUCB.GYM.dto.usuario.UsuarioResponse;
import com.projetoUCB.GYM.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> criarUsuario(@RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.saveUsuario(request));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        return ResponseEntity.ok().body(usuarioService.getUsuarios());
    }

    @GetMapping("/buscar")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorNome(@RequestParam String nome) {
        return ResponseEntity.ok().body(usuarioService.getUsuarioByNome(nome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(@PathVariable int id) {
        return ResponseEntity.ok().body(usuarioService.getUsuarioById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarUsuario(@RequestBody UsuarioRequest request, @PathVariable int id) {
        usuarioService.putUsuario(request, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable int id) {
        usuarioService.deleteUsuarioById(id);
        return ResponseEntity.noContent().build();
    }
}
