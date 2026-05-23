package com.projetoUCB.GYM.controller;

import com.projetoUCB.GYM.dto.aluno.AlunoRequest;
import com.projetoUCB.GYM.dto.aluno.AlunoResponse;
import com.projetoUCB.GYM.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @PostMapping
    public ResponseEntity<AlunoResponse> criarAluno(@RequestBody AlunoRequest alunoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoService.saveAluno(alunoRequest));
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponse>> listarAlunos() {
        return ResponseEntity.ok().body(alunoService.getAlunos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<AlunoResponse> buscarAlunoPorNome(@RequestParam String nome) {
        return ResponseEntity.ok().body(alunoService.getAlunoByName(nome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponse> buscarAlunoPorId(@PathVariable int id) {
        return ResponseEntity.ok().body(alunoService.getAlunoById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarAluno(@RequestBody AlunoRequest alunoRequest, @PathVariable int id) {
        alunoService.putAluno(alunoRequest, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable int id) {
        alunoService.deleteAlunoById(id);
        return ResponseEntity.noContent().build();
    }
}
