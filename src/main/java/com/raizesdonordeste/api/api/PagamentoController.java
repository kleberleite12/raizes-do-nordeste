package com.raizesdonordeste.api.api;

import com.raizesdonordeste.api.domain.entity.Pagamento;
import com.raizesdonordeste.api.domain.entity.Pedido;
import com.raizesdonordeste.api.domain.repository.PagamentoRepository;
import com.raizesdonordeste.api.domain.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

        if (pagamento.getValor() > 10000) {
            pagamento.setStatus("RECUSADO");
            pagamentoRepository.save(pagamento);

            Optional<Pedido> pedido = pedidoRepository.findById(pagamento.getPedidoId());
            if (pedido.isPresent()) {
                pedido.get().setStatus("AGUARDANDO_PAGAMENTO");
                pedidoRepository.save(pedido.get());
            }

            Map<String, String> resposta = new HashMap<>();
            resposta.put("status", "RECUSADO");
            resposta.put("mensagem", "Pagamento recusado pelo gateway mock. Valor acima do limite permitido.");
            return ResponseEntity.status(402).body(resposta);
        }

        pagamento.setStatus("APROVADO");
        pagamentoRepository.save(pagamento);

        Optional<Pedido> pedido = pedidoRepository.findById(pagamento.getPedidoId());
        if (pedido.isPresent()) {
            pedido.get().setStatus("PAGO");
            pedidoRepository.save(pedido.get());
        }

        return ResponseEntity.ok(pagamento);
    }

}
