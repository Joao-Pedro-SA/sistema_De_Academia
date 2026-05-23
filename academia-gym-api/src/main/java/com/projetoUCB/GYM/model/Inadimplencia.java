package com.projetoUCB.GYM.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "inadimplencias")
public class Inadimplencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo_inadimplencia")
    private String codigoInadimplencia;

    @ManyToOne
    @JoinColumn(name = "matricula_id")
    private Matricula matricula;

    private BigDecimal valor;

    @Column(name = "dias_atraso")
    private Integer diasAtraso;

    private String status;

    @Column(name = "data_registro")
    private LocalDate dataRegistro;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoInadimplencia() {
        return codigoInadimplencia;
    }

    public void setCodigoInadimplencia(String codigoInadimplencia) {
        this.codigoInadimplencia = codigoInadimplencia;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getDiasAtraso() {
        return diasAtraso;
    }

    public void setDiasAtraso(Integer diasAtraso) {
        this.diasAtraso = diasAtraso;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}
