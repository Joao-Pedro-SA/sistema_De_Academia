package com.projetoUCB.GYM.service;

import com.projetoUCB.GYM.dto.pagamento.PagamentoRequest;
import com.projetoUCB.GYM.dto.pagamento.PagamentoResponse;
import com.projetoUCB.GYM.model.Matricula;
import com.projetoUCB.GYM.model.Pagamento;
import com.projetoUCB.GYM.repository.MatriculaRepository;
import com.projetoUCB.GYM.repository.PagamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private CodigoService codigoService;

    @Transactional
    public PagamentoResponse savePagamento(PagamentoRequest request) {
        Matricula matricula = buscarMatriculaPorId(request.getMatriculaId());

        Pagamento pagamento = new Pagamento();
        pagamento.setCodigoPagamento(codigoService.gerarCodigoPagamento());
        pagamento.setMatricula(matricula);
        pagamento.setValor(request.getValor() == null ? matricula.getPlano().getValor() : request.getValor());
        pagamento.setDataPagamento(LocalDate.now());
        pagamento.setFormaPagamento(request.getFormaPagamento() == null ? "PIX" : request.getFormaPagamento().toUpperCase());
        pagamento.setStatus(request.getStatus() == null ? "PAGO" : request.getStatus().toUpperCase());

        pagamento = pagamentoRepository.save(pagamento);
        return toResponse(pagamento);
    }

    public List<PagamentoResponse> getPagamentos() {
        return pagamentoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<PagamentoResponse> getPagamentosByMatricula(int matriculaId) {
        return pagamentoRepository.findByMatriculaId(matriculaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PagamentoResponse getPagamentoById(int id) {
        return toResponse(buscarPagamentoPorId(id));
    }

    private Pagamento buscarPagamentoPorId(int id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
    }

    private Matricula buscarMatriculaPorId(int id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada"));
    }

    private PagamentoResponse toResponse(Pagamento pagamento) {
        return new PagamentoResponse(
                pagamento.getId(),
                pagamento.getCodigoPagamento(),
                pagamento.getMatricula().getId(),
                pagamento.getValor(),
                pagamento.getDataPagamento(),
                pagamento.getFormaPagamento(),
                pagamento.getStatus()
        );
    }
}
