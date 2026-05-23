package com.projetoUCB.GYM.dto.pagamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PagamentoResponse {

    private Integer id;
    private String codigoPagamento;
    private Integer matriculaId;
    private BigDecimal valor;
    private LocalDate dataPagamento;
    private String formaPagamento;
    private String status;

    public PagamentoResponse(Integer id, String codigoPagamento, Integer matriculaId, BigDecimal valor,
                             LocalDate dataPagamento, String formaPagamento, String status) {
        this.id = id;
        this.codigoPagamento = codigoPagamento;
        this.matriculaId = matriculaId;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.formaPagamento = formaPagamento;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getCodigoPagamento() {
        return codigoPagamento;
    }

    public Integer getMatriculaId() {
        return matriculaId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public String getStatus() {
        return status;
    }
}
