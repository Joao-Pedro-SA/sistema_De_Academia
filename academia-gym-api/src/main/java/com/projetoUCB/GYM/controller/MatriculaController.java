package com.projetoUCB.GYM.controller;

import com.projetoUCB.GYM.dto.MensagemResponse;
import com.projetoUCB.GYM.dto.matricula.MatriculaRequest;
import com.projetoUCB.GYM.dto.matricula.MatriculaResponse;
import com.projetoUCB.GYM.service.MatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @PostMapping
    public ResponseEntity<MatriculaResponse> criarMatricula(@RequestBody MatriculaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculaService.saveMatricula(request));
    }

    @GetMapping
    public ResponseEntity<List<MatriculaResponse>> listarMatriculas(@RequestParam(required = false) String status) {
        if (status != null) {
            return ResponseEntity.ok().body(matriculaService.getMatriculasByStatus(status));
        }
        return ResponseEntity.ok().body(matriculaService.getMatriculas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResponse> buscarMatriculaPorId(@PathVariable int id) {
        return ResponseEntity.ok().body(matriculaService.getMatriculaById(id));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarMatricula(@PathVariable int id) {
        matriculaService.cancelarMatricula(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/verificar-vencidas")
    public ResponseEntity<MensagemResponse> verificarMatriculasVencidas() {
        int quantidade = matriculaService.verificarMatriculasVencidas();
        return ResponseEntity.ok().body(new MensagemResponse("Matrículas verificadas: " + quantidade));
    }
}
