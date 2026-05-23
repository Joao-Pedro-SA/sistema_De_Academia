package com.projetoUCB.GYM.service;

import com.projetoUCB.GYM.dto.grupoUsuario.GrupoUsuarioRequest;
import com.projetoUCB.GYM.dto.grupoUsuario.GrupoUsuarioResponse;
import com.projetoUCB.GYM.model.GrupoUsuario;
import com.projetoUCB.GYM.repository.GrupoUsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrupoUsuarioService {

    @Autowired
    private GrupoUsuarioRepository grupoUsuarioRepository;

    @Autowired
    private CodigoService codigoService;

    @Transactional
    public GrupoUsuarioResponse saveGrupoUsuario(GrupoUsuarioRequest request) {
        GrupoUsuario grupoUsuario = new GrupoUsuario();
        grupoUsuario.setCodigoGrupo(codigoService.gerarCodigoGrupo());
        grupoUsuario.setNome(request.getNome());
        grupoUsuario.setDescricao(request.getDescricao());
        grupoUsuario.setAtivo(true);

        grupoUsuario = grupoUsuarioRepository.save(grupoUsuario);
        return toResponse(grupoUsuario);
    }

    public List<GrupoUsuarioResponse> getGruposUsuarios() {
        return grupoUsuarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public GrupoUsuarioResponse getGrupoUsuarioById(int id) {
        return toResponse(buscarGrupoUsuarioPorId(id));
    }

    public GrupoUsuarioResponse getGrupoUsuarioByNome(String nome) {
        return toResponse(buscarGrupoUsuarioPorNome(nome));
    }

    @Transactional
    public void putGrupoUsuario(GrupoUsuarioRequest request, int id) {
        GrupoUsuario grupoUsuario = buscarGrupoUsuarioPorId(id);
        grupoUsuario.setNome(request.getNome());
        grupoUsuario.setDescricao(request.getDescricao());
        grupoUsuarioRepository.save(grupoUsuario);
    }

    @Transactional
    public void deleteGrupoUsuarioById(int id) {
        GrupoUsuario grupoUsuario = buscarGrupoUsuarioPorId(id);
        grupoUsuario.setAtivo(false);
        grupoUsuarioRepository.save(grupoUsuario);
    }

    private GrupoUsuario buscarGrupoUsuarioPorId(int id) {
        return grupoUsuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo de usuário não encontrado"));
    }

    private GrupoUsuario buscarGrupoUsuarioPorNome(String nome) {
        return grupoUsuarioRepository.findByNomeContainingIgnoreCase(nome)
                .orElseThrow(() -> new RuntimeException("Grupo de usuário não encontrado"));
    }

    private GrupoUsuarioResponse toResponse(GrupoUsuario grupoUsuario) {
        return new GrupoUsuarioResponse(
                grupoUsuario.getId(),
                grupoUsuario.getCodigoGrupo(),
                grupoUsuario.getNome(),
                grupoUsuario.getDescricao(),
                grupoUsuario.getAtivo()
        );
    }
}
