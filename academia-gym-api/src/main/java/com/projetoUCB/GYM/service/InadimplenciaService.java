package com.projetoUCB.GYM.service;

import com.projetoUCB.GYM.dto.inadimplencia.InadimplenciaResponse;
import com.projetoUCB.GYM.model.Inadimplencia;
import com.projetoUCB.GYM.repository.InadimplenciaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InadimplenciaService {

    @Autowired
    private InadimplenciaRepository inadimplenciaRepository;

    public List<InadimplenciaResponse> getInadimplencias() {
        return inadimplenciaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<InadimplenciaResponse> getInadimplenciasPendentes() {
        return inadimplenciaRepository.findByStatus("PENDENTE")
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public InadimplenciaResponse getInadimplenciaById(int id) {
        return toResponse(buscarInadimplenciaPorId(id));
    }

    @Transactional
    public void resolverInadimplencia(int id) {
        Inadimplencia inadimplencia = buscarInadimplenciaPorId(id);
        inadimplencia.setStatus("RESOLVIDA");
        inadimplenciaRepository.save(inadimplencia);
    }

    private Inadimplencia buscarInadimplenciaPorId(int id) {
        return inadimplenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inadimplência não encontrada"));
    }

    private InadimplenciaResponse toResponse(Inadimplencia inadimplencia) {
        return new InadimplenciaResponse(
                inadimplencia.getId(),
                inadimplencia.getCodigoInadimplencia(),
                inadimplencia.getMatricula().getId(),
                inadimplencia.getMatricula().getAluno().getNome(),
                inadimplencia.getValor(),
                inadimplencia.getDiasAtraso(),
                inadimplencia.getStatus(),
                inadimplencia.getDataRegistro()
        );
    }
}
