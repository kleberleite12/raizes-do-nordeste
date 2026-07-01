package com.raizesdonordeste.api.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clienteId;
    private Long unidadeId;
    private String status;
    private String canalPedido;
    private String formaPagamento;
    private Double total;
    private LocalDateTime dataPedido;

}
