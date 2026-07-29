package com.raizesdonordeste.api.domain.repository;

import com.raizesdonordeste.api.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    List<ItemPedido> findByPedidoId(Long pedidoId);

}