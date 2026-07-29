package com.raizesdonordeste.api.api;

import com.raizesdonordeste.api.domain.entity.Estoque;
import com.raizesdonordeste.api.domain.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @GetMapping
    public List<Estoque> listar() {
        return estoqueRepository.findAll();
    }

    @PostMapping
    public Estoque criar(@RequestBody Estoque estoque) {
        return estoqueRepository.save(estoque);
    }

    @PutMapping("/entrada")
    public ResponseEntity<?> entrada(@RequestBody Map<String, Object> dados) {
        Long produtoId = Long.valueOf(dados.get("produtoId").toString());
        Long unidadeId = Long.valueOf(dados.get("unidadeId").toString());
        Integer quantidade = (Integer) dados.get("quantidade");

        Optional<Estoque> estoque = estoqueRepository.findByProdutoIdAndUnidadeId(produtoId, unidadeId);

        if (estoque.isEmpty()) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Estoque não encontrado para esse produto e unidade");
            return ResponseEntity.status(404).body(erro);
        }

        estoque.get().setQuantidade(estoque.get().getQuantidade() + quantidade);
        estoqueRepository.save(estoque.get());

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mensagem", "Entrada de estoque registrada com sucesso");
        resposta.put("quantidadeAtual", estoque.get().getQuantidade());
        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/saida")
    public ResponseEntity<?> saida(@RequestBody Map<String, Object> dados) {
        Long produtoId = Long.valueOf(dados.get("produtoId").toString());
        Long unidadeId = Long.valueOf(dados.get("unidadeId").toString());
        Integer quantidade = (Integer) dados.get("quantidade");

        Optional<Estoque> estoque = estoqueRepository.findByProdutoIdAndUnidadeId(produtoId, unidadeId);

        if (estoque.isEmpty()) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Estoque não encontrado para esse produto e unidade");
            return ResponseEntity.status(404).body(erro);
        }

        if (estoque.get().getQuantidade() < quantidade) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Quantidade insuficiente em estoque");
            return ResponseEntity.status(409).body(erro);
        }

        estoque.get().setQuantidade(estoque.get().getQuantidade() - quantidade);
        estoqueRepository.save(estoque.get());

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mensagem", "Saída de estoque registrada com sucesso");
        resposta.put("quantidadeAtual", estoque.get().getQuantidade());
        return ResponseEntity.ok(resposta);
    }

}