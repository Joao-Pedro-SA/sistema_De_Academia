package com.projetoUCB.GYM.controller;

import com.projetoUCB.GYM.dto.inadimplencia.InadimplenciaResponse;
import com.projetoUCB.GYM.service.InadimplenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inadimplencias")
public class InadimplenciaController {

    @Autowired
    private InadimplenciaService inadimplenciaService;

    @GetMapping
    public ResponseEntity<List<InadimplenciaResponse>> listarInadimplencias() {
        return ResponseEntity.ok().body(inadimplenciaService.getInadimplencias());
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<InadimplenciaResponse>> listarInadimplenciasPendentes() {
        return ResponseEntity.ok().body(inadimplenciaService.getInadimplenciasPendentes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InadimplenciaResponse> buscarInadimplenciaPorId(@PathVariable int id) {
        return ResponseEntity.ok().body(inadimplenciaService.getInadimplenciaById(id));
    }

    @PutMapping("/{id}/resolver")
    public ResponseEntity<Void> resolverInadimplencia(@PathVariable int id) {
        inadimplenciaService.resolverInadimplencia(id);
        return ResponseEntity.noContent().build();
    }
}
