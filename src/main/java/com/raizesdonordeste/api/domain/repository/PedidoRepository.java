package com.raizesdonordeste.api.domain.repository;

import com.raizesdonordeste.api.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByCanalPedido(String canalPedido);

}
