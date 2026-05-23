package com.projetoUCB.GYM.controller;

import com.projetoUCB.GYM.dto.grupoUsuario.GrupoUsuarioRequest;
import com.projetoUCB.GYM.dto.grupoUsuario.GrupoUsuarioResponse;
import com.projetoUCB.GYM.service.GrupoUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grupos-usuarios")
public class GrupoUsuarioController {

    @Autowired
    private GrupoUsuarioService grupoUsuarioService;

    @PostMapping
    public ResponseEntity<GrupoUsuarioResponse> criarGrupoUsuario(@RequestBody GrupoUsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(grupoUsuarioService.saveGrupoUsuario(request));
    }

    @GetMapping
    public ResponseEntity<List<GrupoUsuarioResponse>> listarGruposUsuarios() {
        return ResponseEntity.ok().body(grupoUsuarioService.getGruposUsuarios());
    }

    @GetMapping("/buscar")
    public ResponseEntity<GrupoUsuarioResponse> buscarGrupoUsuarioPorNome(@RequestParam String nome) {
        return ResponseEntity.ok().body(grupoUsuarioService.getGrupoUsuarioByNome(nome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrupoUsuarioResponse> buscarGrupoUsuarioPorId(@PathVariable int id) {
        return ResponseEntity.ok().body(grupoUsuarioService.getGrupoUsuarioById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarGrupoUsuario(@RequestBody GrupoUsuarioRequest request, @PathVariable int id) {
        grupoUsuarioService.putGrupoUsuario(request, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarGrupoUsuario(@PathVariable int id) {
        grupoUsuarioService.deleteGrupoUsuarioById(id);
        return ResponseEntity.noContent().build();
    }
}
