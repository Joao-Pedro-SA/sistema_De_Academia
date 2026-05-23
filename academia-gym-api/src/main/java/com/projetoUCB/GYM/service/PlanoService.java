package com.projetoUCB.GYM.service;

import com.projetoUCB.GYM.dto.plano.PlanoRequest;
import com.projetoUCB.GYM.dto.plano.PlanoResponse;
import com.projetoUCB.GYM.model.Plano;
import com.projetoUCB.GYM.repository.PlanoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanoService {

    @Autowired
    private PlanoRepository planoRepository;

    @Transactional
    public PlanoResponse savePlano(PlanoRequest request) {
        Plano plano = new Plano();
        plano.setNome(request.getNome());
        plano.setDuracaoDias(request.getDuracaoDias());
        plano.setValor(request.getValor());
        plano.setAtivo(true);

        plano = planoRepository.save(plano);
        return toResponse(plano);
    }

    public List<PlanoResponse> getPlanos() {
        return planoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PlanoResponse getPlanoByName(String nome) {
        return toResponse(buscarPlanoPorNome(nome));
    }

    public PlanoResponse getPlanoById(int id) {
        return toResponse(buscarPlanoPorId(id));
    }

    @Transactional
    public void putPlano(PlanoRequest request, int id) {
        Plano plano = buscarPlanoPorId(id);
        plano.setNome(request.getNome());
        plano.setDuracaoDias(request.getDuracaoDias());
        plano.setValor(request.getValor());
        planoRepository.save(plano);
    }

    @Transactional
    public void deletePlanoById(int id) {
        Plano plano = buscarPlanoPorId(id);
        plano.setAtivo(false);
        planoRepository.save(plano);
    }

    private Plano buscarPlanoPorId(int id) {
        return planoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));
    }

    private Plano buscarPlanoPorNome(String nome) {
        return planoRepository.findByNomeContainingIgnoreCase(nome)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));
    }

    private PlanoResponse toResponse(Plano plano) {
        return new PlanoResponse(
                plano.getId(),
                plano.getNome(),
                plano.getDuracaoDias(),
                plano.getValor(),
                plano.getAtivo()
        );
    }
}
