package com.projetoUCB.GYM.service;

import com.projetoUCB.GYM.dto.modalidade.ModalidadeRequest;
import com.projetoUCB.GYM.dto.modalidade.ModalidadeResponse;
import com.projetoUCB.GYM.model.Modalidade;
import com.projetoUCB.GYM.repository.ModalidadeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModalidadeService {

    @Autowired
    private ModalidadeRepository modalidadeRepository;

    @Transactional
    public ModalidadeResponse saveModalidade(ModalidadeRequest request) {
        Modalidade modalidade = new Modalidade();
        modalidade.setNome(request.getNome());
        modalidade.setDescricao(request.getDescricao());
        modalidade.setAtiva(true);

        modalidade = modalidadeRepository.save(modalidade);
        return toResponse(modalidade);
    }

    public List<ModalidadeResponse> getModalidades() {
        return modalidadeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ModalidadeResponse getModalidadeById(int id) {
        return toResponse(buscarModalidadePorId(id));
    }

    @Transactional
    public void putModalidade(ModalidadeRequest request, int id) {
        Modalidade modalidade = buscarModalidadePorId(id);
        modalidade.setNome(request.getNome());
        modalidade.setDescricao(request.getDescricao());
        modalidadeRepository.save(modalidade);
    }

    @Transactional
    public void deleteModalidadeById(int id) {
        Modalidade modalidade = buscarModalidadePorId(id);
        modalidade.setAtiva(false);
        modalidadeRepository.save(modalidade);
    }

    public Modalidade buscarModalidadePorId(int id) {
        return modalidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modalidade não encontrada"));
    }

    private ModalidadeResponse toResponse(Modalidade modalidade) {
        return new ModalidadeResponse(
                modalidade.getId(),
                modalidade.getNome(),
                modalidade.getDescricao(),
                modalidade.getAtiva()
        );
    }
}
