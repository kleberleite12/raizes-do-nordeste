package com.raizesdonordeste.api.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "unidades")
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String endereco;
    private String cidade;
    private String estado;

}