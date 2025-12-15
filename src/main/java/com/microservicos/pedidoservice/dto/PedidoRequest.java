package com.microservicos.pedidoservice.dto;

public class PedidoRequest {
    private String descricao;
    private Double valor;

    public PedidoRequest() {}

    public PedidoRequest(String descricao, Double valor) {
        this.descricao = descricao;
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
