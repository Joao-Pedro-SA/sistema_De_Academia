package com.projetoUCB.GYM.controller;

import com.projetoUCB.GYM.dto.relatorio.AlunoAtivoResponse;
import com.projetoUCB.GYM.dto.relatorio.InadimplenciaPendenteResponse;
import com.projetoUCB.GYM.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/alunos-ativos")
    public ResponseEntity<List<AlunoAtivoResponse>> listarAlunosAtivos() {
        return ResponseEntity.ok().body(relatorioService.listarAlunosAtivos());
    }

    @GetMapping("/inadimplencias-pendentes")
    public ResponseEntity<List<InadimplenciaPendenteResponse>> listarInadimplenciasPendentes() {
        return ResponseEntity.ok().body(relatorioService.listarInadimplenciasPendentes());
    }
}
