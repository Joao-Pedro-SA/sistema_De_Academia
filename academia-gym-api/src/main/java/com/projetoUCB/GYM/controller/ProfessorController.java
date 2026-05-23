package com.projetoUCB.GYM.controller;

import com.projetoUCB.GYM.dto.professor.ProfessorRequest;
import com.projetoUCB.GYM.dto.professor.ProfessorResponse;
import com.projetoUCB.GYM.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @PostMapping
    public ResponseEntity<ProfessorResponse> criarProfessor(@RequestBody ProfessorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(professorService.saveProfessor(request));
    }

    @GetMapping
    public ResponseEntity<List<ProfessorResponse>> listarProfessores() {
        return ResponseEntity.ok().body(professorService.getProfessores());
    }

    @GetMapping("/buscar")
    public ResponseEntity<ProfessorResponse> buscarProfessorPorNome(@RequestParam String nome) {
        return ResponseEntity.ok().body(professorService.getProfessorByName(nome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponse> buscarProfessorPorId(@PathVariable int id) {
        return ResponseEntity.ok().body(professorService.getProfessorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarProfessor(@RequestBody ProfessorRequest request, @PathVariable int id) {
        professorService.putProfessor(request, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProfessor(@PathVariable int id) {
        professorService.deleteProfessorById(id);
        return ResponseEntity.noContent().build();
    }
}
