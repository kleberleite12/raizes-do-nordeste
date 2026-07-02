package com.raizesdonordeste.api.api;

import com.raizesdonordeste.api.domain.entity.Pagamento;
import com.raizesdonordeste.api.domain.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public Pagamento criar(@RequestBody Pagamento pagamento) {
        pagamento.setStatus("APROVADO");
        return pagamentoRepository.save(pagamento);
    }

}
