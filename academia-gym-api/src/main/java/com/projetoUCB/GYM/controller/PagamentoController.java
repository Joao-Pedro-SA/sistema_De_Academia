package com.projetoUCB.GYM.controller;

import com.projetoUCB.GYM.dto.pagamento.PagamentoRequest;
import com.projetoUCB.GYM.dto.pagamento.PagamentoResponse;
import com.projetoUCB.GYM.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<PagamentoResponse> criarPagamento(@RequestBody PagamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoService.savePagamento(request));
    }

    @GetMapping
    public ResponseEntity<List<PagamentoResponse>> listarPagamentos(@RequestParam(required = false) Integer matriculaId) {
        if (matriculaId != null) {
            return ResponseEntity.ok().body(pagamentoService.getPagamentosByMatricula(matriculaId));
        }
        return ResponseEntity.ok().body(pagamentoService.getPagamentos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponse> buscarPagamentoPorId(@PathVariable int id) {
        return ResponseEntity.ok().body(pagamentoService.getPagamentoById(id));
    }
}
