package com.projetoUCB.GYM.controller;

import com.projetoUCB.GYM.dto.plano.PlanoRequest;
import com.projetoUCB.GYM.dto.plano.PlanoResponse;
import com.projetoUCB.GYM.service.PlanoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planos")
public class PlanoController {

    @Autowired
    private PlanoService planoService;

    @PostMapping
    public ResponseEntity<PlanoResponse> criarPlano(@RequestBody PlanoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planoService.savePlano(request));
    }

    @GetMapping
    public ResponseEntity<List<PlanoResponse>> listarPlanos() {
        return ResponseEntity.ok().body(planoService.getPlanos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<PlanoResponse> buscarPlanoPorNome(@RequestParam String nome) {
        return ResponseEntity.ok().body(planoService.getPlanoByName(nome));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoResponse> buscarPlanoPorId(@PathVariable int id) {
        return ResponseEntity.ok().body(planoService.getPlanoById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarPlano(@RequestBody PlanoRequest request, @PathVariable int id) {
        planoService.putPlano(request, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPlano(@PathVariable int id) {
        planoService.deletePlanoById(id);
        return ResponseEntity.noContent().build();
    }
}
