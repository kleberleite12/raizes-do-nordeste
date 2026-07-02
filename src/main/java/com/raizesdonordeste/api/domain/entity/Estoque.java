package com.raizesdonordeste.api.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "estoque")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long unidadeId;
    private Long produtoId;
    private Integer quantidade;

}