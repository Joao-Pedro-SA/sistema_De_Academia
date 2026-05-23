package com.projetoUCB.GYM.service;

import com.projetoUCB.GYM.dto.professor.ProfessorRequest;
import com.projetoUCB.GYM.dto.professor.ProfessorResponse;
import com.projetoUCB.GYM.model.Modalidade;
import com.projetoUCB.GYM.model.Professor;
import com.projetoUCB.GYM.repository.ModalidadeRepository;
import com.projetoUCB.GYM.repository.ProfessorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ModalidadeRepository modalidadeRepository;

    @Transactional
    public ProfessorResponse saveProfessor(ProfessorRequest request) {
        Modalidade modalidade = buscarModalidadePorId(request.getModalidadeId());

        Professor professor = new Professor();
        professor.setNome(request.getNome());
        professor.setEmail(request.getEmail());
        professor.setTelefone(request.getTelefone());
        professor.setModalidade(modalidade);
        professor.setAtivo(true);

        professor = professorRepository.save(professor);
        return toResponse(professor);
    }

    public List<ProfessorResponse> getProfessores() {
        return professorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProfessorResponse getProfessorByName(String nome) {
        return toResponse(buscarProfessorPorNome(nome));
    }

    public ProfessorResponse getProfessorById(int id) {
        return toResponse(buscarProfessorPorId(id));
    }

    @Transactional
    public void putProfessor(ProfessorRequest request, int id) {
        Professor professor = buscarProfessorPorId(id);
        Modalidade modalidade = buscarModalidadePorId(request.getModalidadeId());

        professor.setNome(request.getNome());
        professor.setEmail(request.getEmail());
        professor.setTelefone(request.getTelefone());
        professor.setModalidade(modalidade);

        professorRepository.save(professor);
    }

    @Transactional
    public void deleteProfessorById(int id) {
        Professor professor = buscarProfessorPorId(id);
        professor.setAtivo(false);
        professorRepository.save(professor);
    }

    private Professor buscarProfessorPorId(int id) {
        return professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
    }

    private Professor buscarProfessorPorNome(String nome) {
        return professorRepository.findByNomeContainingIgnoreCase(nome)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
    }

    private Modalidade buscarModalidadePorId(int id) {
        return modalidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modalidade não encontrada"));
    }

    private ProfessorResponse toResponse(Professor professor) {
        return new ProfessorResponse(
                professor.getId(),
                professor.getNome(),
                professor.getEmail(),
                professor.getTelefone(),
                professor.getModalidade().getId(),
                professor.getModalidade().getNome(),
                professor.getAtivo()
        );
    }
}
