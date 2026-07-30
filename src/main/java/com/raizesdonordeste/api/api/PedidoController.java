package com.raizesdonordeste.api.api;

import com.raizesdonordeste.api.domain.entity.Estoque;
import com.raizesdonordeste.api.domain.entity.ItemPedido;
import com.raizesdonordeste.api.domain.entity.Pedido;
import com.raizesdonordeste.api.domain.enums.CanalPedido;
import com.raizesdonordeste.api.domain.repository.EstoqueRepository;
import com.raizesdonordeste.api.domain.repository.ItemPedidoRepository;
import com.raizesdonordeste.api.domain.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @GetMapping
    public List<Pedido> listar(@RequestParam(required = false) CanalPedido canalPedido) {
        List<Pedido> pedidos;
        if (canalPedido != null) {
            pedidos = pedidoRepository.findByCanalPedido(canalPedido);
        } else {
            pedidos = pedidoRepository.findAll();
        }
        for (Pedido pedido : pedidos) {
            pedido.setItens(itemPedidoRepository.findByPedidoId(pedido.getId()));
        }
        return pedidos;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Pedido pedido) {
        if (pedido.getCanalPedido() == null) {
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

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        if (pedido.getItens() != null) {
            for (ItemPedido item : pedido.getItens()) {
                item.setPedidoId(pedidoSalvo.getId());
                itemPedidoRepository.save(item);

                Optional<Estoque> estoque = estoqueRepository
                        .findByProdutoIdAndUnidadeId(item.getProdutoId(), pedido.getUnidadeId());
                if (estoque.isPresent()) {
                    estoque.get().setQuantidade(estoque.get().getQuantidade() - item.getQuantidade());
                    estoqueRepository.save(estoque.get());
                }
            }
        }

        System.out.println("[LOG] " + LocalDateTime.now() + " - PEDIDO CRIADO - id: " + pedidoSalvo.getId() + " - canal: " + pedidoSalvo.getCanalPedido() + " - cliente: " + pedidoSalvo.getClienteId());

        return ResponseEntity.status(201).body(pedidoSalvo);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        List<String> statusPermitidos = List.of("AGUARDANDO_PAGAMENTO", "PAGO", "EM_PREPARO", "PRONTO", "ENTREGUE", "CANCELADO");

        if (!statusPermitidos.contains(status)) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Status inválido. Valores permitidos: " + statusPermitidos);
            return ResponseEntity.status(422).body(erro);
        }

        Pedido pedido = pedidoRepository.findById(id).orElseThrow();
        String statusAnterior = pedido.getStatus();
        pedido.setStatus(status);
        pedidoRepository.save(pedido);

        System.out.println("[LOG] " + LocalDateTime.now() + " - STATUS ATUALIZADO - pedido: " + id + " - de: " + statusAnterior + " para: " + status);

        return ResponseEntity.ok(pedido);
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

        System.out.println("[LOG] " + LocalDateTime.now() + " - PEDIDO CANCELADO - id: " + id);

        Map<String, String> resposta = new HashMap<>();
        resposta.put("mensagem", "Pedido cancelado com sucesso");
        return ResponseEntity.ok(resposta);
    }

}