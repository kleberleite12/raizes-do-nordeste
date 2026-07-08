package com.raizesdonordeste.api.api;

import com.raizesdonordeste.api.domain.entity.Estoque;
import com.raizesdonordeste.api.domain.entity.ItemPedido;
import com.raizesdonordeste.api.domain.entity.Pedido;
import com.raizesdonordeste.api.domain.repository.EstoqueRepository;
import com.raizesdonordeste.api.domain.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @GetMapping
    public List<Pedido> listar(@RequestParam(required = false) String canalPedido) {
        if (canalPedido != null) {
            return pedidoRepository.findByCanalPedido(canalPedido);
        }
        return pedidoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Pedido pedido) {
        if (pedido.getCanalPedido() == null || pedido.getCanalPedido().isEmpty()) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "canalPedido é obrigatório");
            return ResponseEntity.status(422).body(erro);
        }

        if (pedido.getItens() != null) {
            for (ItemPedido item : pedido.getItens()) {
                Optional<Estoque> estoque = estoqueRepository
                        .findByProdutoIdAndUnidadeId(item.getProdutoId(), pedido.getUnidadeId());

                if (estoque.isEmpty() || estoque.get().getQuantidade() < item.getQuantidade()) {
                    Map<String, String> erro = new HashMap<>();
                    erro.put("erro", "Estoque insuficiente para o produto " + item.getProdutoId());
                    return ResponseEntity.status(409).body(erro);
                }
            }
        }

        return ResponseEntity.status(201).body(pedidoRepository.save(pedido));
    }

    @PutMapping("/{id}/status")
    public Pedido atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow();
        pedido.setStatus(status);
        return pedidoRepository.save(pedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);

        if (pedido.isEmpty()) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Pedido não encontrado");
            return ResponseEntity.status(404).body(erro);
        }

        if (pedido.get().getStatus().equals("ENTREGUE")) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Pedido já entregue não pode ser cancelado");
            return ResponseEntity.status(409).body(erro);
        }

        pedido.get().setStatus("CANCELADO");
        pedidoRepository.save(pedido.get());

        Map<String, String> resposta = new HashMap<>();
        resposta.put("mensagem", "Pedido cancelado com sucesso");
        return ResponseEntity.ok(resposta);
    }

}