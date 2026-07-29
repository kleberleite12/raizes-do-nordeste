package com.raizesdonordeste.api.api;

import com.raizesdonordeste.api.domain.entity.Pagamento;
import com.raizesdonordeste.api.domain.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoRepository pagamentoRepository;

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
            Map<String, String> resposta = new HashMap<>();
            resposta.put("status", "RECUSADO");
            resposta.put("mensagem", "Pagamento recusado pelo gateway mock. Valor acima do limite permitido.");
            return ResponseEntity.status(402).body(resposta);
        }

        pagamento.setStatus("APROVADO");
        return ResponseEntity.ok(pagamentoRepository.save(pagamento));
    }

}
