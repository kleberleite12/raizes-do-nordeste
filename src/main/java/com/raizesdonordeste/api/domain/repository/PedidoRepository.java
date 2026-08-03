package com.raizesdonordeste.api.domain.repository;

import com.raizesdonordeste.api.domain.entity.Pedido;
import com.raizesdonordeste.api.domain.enums.CanalPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Page<Pedido> findByCanalPedido(CanalPedido canalPedido, Pageable pageable);

}
