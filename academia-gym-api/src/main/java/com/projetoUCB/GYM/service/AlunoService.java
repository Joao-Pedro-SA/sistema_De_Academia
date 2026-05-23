package com.projetoUCB.GYM.service;

import com.projetoUCB.GYM.dto.aluno.AlunoRequest;
import com.projetoUCB.GYM.dto.aluno.AlunoResponse;
import com.projetoUCB.GYM.model.Aluno;
import com.projetoUCB.GYM.repository.AlunoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CodigoService codigoService;

    @Transactional
    public AlunoResponse saveAluno(AlunoRequest alunoRequest) {
        Aluno aluno = new Aluno();
        aluno.setCodigoAluno(codigoService.gerarCodigoAluno());
        aluno.setNome(alunoRequest.getNome());
        aluno.setCpf(alunoRequest.getCpf());
        aluno.setEmail(alunoRequest.getEmail());
        aluno.setTelefone(alunoRequest.getTelefone());
        aluno.setDataNascimento(alunoRequest.getDataNascimento());
        aluno.setSexo(alunoRequest.getSexo());
        aluno.setEndereco(alunoRequest.getEndereco());
        aluno.setAtivo(true);

        aluno = alunoRepository.save(aluno);
        return toResponse(aluno);
    }

    public List<AlunoResponse> getAlunos() {
        return alunoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AlunoResponse getAlunoByName(String alunoName) {
        return toResponse(buscarAlunoPorNome(alunoName));
    }

    public AlunoResponse getAlunoById(int id) {
        return toResponse(buscarAlunoPorId(id));
    }

    @Transactional
    public void putAluno(AlunoRequest alunoRequest, int id) {
        Aluno alunoSaved = buscarAlunoPorId(id);

        alunoSaved.setNome(alunoRequest.getNome());
        alunoSaved.setCpf(alunoRequest.getCpf());
        alunoSaved.setEmail(alunoRequest.getEmail());
        alunoSaved.setTelefone(alunoRequest.getTelefone());
        alunoSaved.setDataNascimento(alunoRequest.getDataNascimento());
        alunoSaved.setSexo(alunoRequest.getSexo());
        alunoSaved.setEndereco(alunoRequest.getEndereco());

        alunoRepository.save(alunoSaved);
    }

    @Transactional
    public void deleteAlunoById(int idAluno) {
        Aluno alunoSaved = buscarAlunoPorId(idAluno);
        alunoSaved.setAtivo(false);
        alunoRepository.save(alunoSaved);
    }

    private Aluno buscarAlunoPorId(int id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
    }

    private Aluno buscarAlunoPorNome(String nome) {
        return alunoRepository.findByNomeContainingIgnoreCase(nome)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
    }

    private AlunoResponse toResponse(Aluno aluno) {
        return new AlunoResponse(
                aluno.getId(),
                aluno.getCodigoAluno(),
                aluno.getNome(),
                aluno.getCpf(),
                aluno.getEmail(),
                aluno.getTelefone(),
                aluno.getDataNascimento(),
                aluno.getSexo(),
                aluno.getEndereco(),
                aluno.getAtivo()
        );
    }
}
