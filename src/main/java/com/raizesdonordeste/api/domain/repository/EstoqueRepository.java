package com.raizesdonordeste.api.domain.repository;

import com.raizesdonordeste.api.domain.entity.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    Optional<Estoque> findByProdutoIdAndUnidadeId(Long produtoId, Long unidadeId);

}