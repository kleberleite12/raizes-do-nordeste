package com.raizesdonordeste.api.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pedidoId;
    private String status;
    private String formaPagamento;
    private Double valor;
    private LocalDateTime dataPagamento;

}
