package com.projetoUCB.GYM.controller;

import com.projetoUCB.GYM.dto.modalidade.ModalidadeRequest;
import com.projetoUCB.GYM.dto.modalidade.ModalidadeResponse;
import com.projetoUCB.GYM.service.ModalidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modalidades")
public class ModalidadeController {

    @Autowired
    private ModalidadeService modalidadeService;

    @PostMapping
    public ResponseEntity<ModalidadeResponse> criarModalidade(@RequestBody ModalidadeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(modalidadeService.saveModalidade(request));
    }

    @GetMapping
    public ResponseEntity<List<ModalidadeResponse>> listarModalidades() {
        return ResponseEntity.ok().body(modalidadeService.getModalidades());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModalidadeResponse> buscarModalidadePorId(@PathVariable int id) {
        return ResponseEntity.ok().body(modalidadeService.getModalidadeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarModalidade(@RequestBody ModalidadeRequest request, @PathVariable int id) {
        modalidadeService.putModalidade(request, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarModalidade(@PathVariable int id) {
        modalidadeService.deleteModalidadeById(id);
        return ResponseEntity.noContent().build();
    }
}
