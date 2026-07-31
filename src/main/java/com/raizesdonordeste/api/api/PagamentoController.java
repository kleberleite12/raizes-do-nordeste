package com.raizesdonordeste.api.api;

import com.raizesdonordeste.api.domain.entity.Pagamento;
import com.raizesdonordeste.api.domain.entity.Pedido;
import com.raizesdonordeste.api.domain.repository.PagamentoRepository;
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
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping
    public List<Pagamento> listar() {
        return pagamentoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Pagamento pagamento) {
        if (pagamento.getValor() == null || pagamento.getValor() <= 0) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Valor do pagamento inválido");
            return ResponseEntity.status(422).body(erro);
        }

        if (pagamento.getPedidoId() == null) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "pedidoId é obrigatório");
            return ResponseEntity.status(422).body(erro);
        }

        Optional<Pedido> pedidoEncontrado = pedidoRepository.findById(pagamento.getPedidoId());
        if (pedidoEncontrado.isEmpty()) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Pedido não encontrado");
            return ResponseEntity.status(404).body(erro);
        }

        Pedido pedido = pedidoEncontrado.get();
        pagamento.setDataPagamento(LocalDateTime.now());

        if (pagamento.getValor() > 10000) {
            pagamento.setStatus("RECUSADO");
            pagamentoRepository.save(pagamento);

            pedido.setStatus("AGUARDANDO_PAGAMENTO");
            pedidoRepository.save(pedido);

            System.out.println("[LOG] " + LocalDateTime.now() + " - PAGAMENTO RECUSADO - pedido: " + pagamento.getPedidoId() + " - valor: " + pagamento.getValor());

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("pagamentoId", pagamento.getId());
            resposta.put("pedidoId", pagamento.getPedidoId());
            resposta.put("status", "RECUSADO");
            resposta.put("statusPedido", pedido.getStatus());
            resposta.put("mensagem", "Pagamento recusado pelo gateway mock. Valor acima do limite permitido.");
            return ResponseEntity.ok(resposta);
        }

        pagamento.setStatus("APROVADO");
        pagamentoRepository.save(pagamento);

        pedido.setStatus("PAGO");
        pedidoRepository.save(pedido);

        System.out.println("[LOG] " + LocalDateTime.now() + " - PAGAMENTO APROVADO - pedido: " + pagamento.getPedidoId() + " - valor: " + pagamento.getValor());

        return ResponseEntity.ok(pagamento);
    }

}