package com.raizesdonordeste.api.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "itens_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pedidoId;
    private Long produtoId;
    private Integer quantidade;
    private Double precoUnitario;

}