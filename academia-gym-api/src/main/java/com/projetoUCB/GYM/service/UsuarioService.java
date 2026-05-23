package com.projetoUCB.GYM.service;

import com.projetoUCB.GYM.dto.usuario.UsuarioRequest;
import com.projetoUCB.GYM.dto.usuario.UsuarioResponse;
import com.projetoUCB.GYM.model.GrupoUsuario;
import com.projetoUCB.GYM.model.Modalidade;
import com.projetoUCB.GYM.model.Usuario;
import com.projetoUCB.GYM.repository.GrupoUsuarioRepository;
import com.projetoUCB.GYM.repository.ModalidadeRepository;
import com.projetoUCB.GYM.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private GrupoUsuarioRepository grupoUsuarioRepository;

    @Autowired
    private ModalidadeRepository modalidadeRepository;

    @Autowired
    private CodigoService codigoService;

    @Transactional
    public UsuarioResponse saveUsuario(UsuarioRequest request) {
        GrupoUsuario grupoUsuario = buscarGrupoUsuarioPorId(request.getGrupoUsuarioId());
        Modalidade modalidade = buscarModalidadeSeInformada(request.getModalidadeId());

        Usuario usuario = new Usuario();
        usuario.setCodigoUsuario(codigoService.gerarCodigoUsuario());
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setTelefone(request.getTelefone());
        usuario.setCargo(request.getCargo());
        usuario.setGrupoUsuario(grupoUsuario);
        usuario.setModalidade(modalidade);
        usuario.setAtivo(true);

        usuario = usuarioRepository.save(usuario);
        return toResponse(usuario);
    }

    public List<UsuarioResponse> getUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UsuarioResponse getUsuarioById(int id) {
        return toResponse(buscarUsuarioPorId(id));
    }

    public UsuarioResponse getUsuarioByNome(String nome) {
        return toResponse(buscarUsuarioPorNome(nome));
    }

    @Transactional
    public void putUsuario(UsuarioRequest request, int id) {
        Usuario usuario = buscarUsuarioPorId(id);
        GrupoUsuario grupoUsuario = buscarGrupoUsuarioPorId(request.getGrupoUsuarioId());
        Modalidade modalidade = buscarModalidadeSeInformada(request.getModalidadeId());

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setTelefone(request.getTelefone());
        usuario.setCargo(request.getCargo());
        usuario.setGrupoUsuario(grupoUsuario);
        usuario.setModalidade(modalidade);

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void deleteUsuarioById(int id) {
        Usuario usuario = buscarUsuarioPorId(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    private Usuario buscarUsuarioPorId(int id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private Usuario buscarUsuarioPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private GrupoUsuario buscarGrupoUsuarioPorId(Integer id) {
        if (id == null) {
            throw new RuntimeException("Grupo de usuário é obrigatório");
        }

        return grupoUsuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo de usuário não encontrado"));
    }

    private Modalidade buscarModalidadeSeInformada(Integer id) {
        if (id == null) {
            return null;
        }

        return modalidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modalidade não encontrada"));
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        Integer modalidadeId = usuario.getModalidade() == null ? null : usuario.getModalidade().getId();
        String modalidadeNome = usuario.getModalidade() == null ? null : usuario.getModalidade().getNome();

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getCodigoUsuario(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getCargo(),
                usuario.getGrupoUsuario().getId(),
                usuario.getGrupoUsuario().getNome(),
                modalidadeId,
                modalidadeNome,
                usuario.getAtivo()
        );
    }
}
