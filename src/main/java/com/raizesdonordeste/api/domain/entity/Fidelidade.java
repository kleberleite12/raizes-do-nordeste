package com.raizesdonordeste.api.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fidelidade")
public class Fidelidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clienteId;
    private Integer pontos;
    private Boolean consentimento;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Integer getPontos() { return pontos; }
    public void setPontos(Integer pontos) { this.pontos = pontos; }

    public Boolean getConsentimento() { return consentimento; }
    public void setConsentimento(Boolean consentimento) { this.consentimento = consentimento; }

}
