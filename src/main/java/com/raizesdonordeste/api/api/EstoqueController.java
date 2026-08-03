package com.raizesdonordeste.api.api;

import com.raizesdonordeste.api.domain.entity.Estoque;
import com.raizesdonordeste.api.domain.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @GetMapping
    public Page<Estoque> listar(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int limit) {
        if (page < 1) {
            page = 1;
        }
        if (limit < 1) {
            limit = 10;
        }
        return estoqueRepository.findAll(PageRequest.of(page - 1, limit));
    }

    @PostMapping
    public ResponseEntity<Estoque> criar(@RequestBody Estoque estoque) {
        Estoque estoqueSalvo = estoqueRepository.save(estoque);
        return ResponseEntity.status(201).body(estoqueSalvo);
    }

    @PutMapping("/entrada")
    public ResponseEntity<?> entrada(@RequestBody Map<String, Object> dados) {
        if (dados.get("produtoId") == null || dados.get("unidadeId") == null || dados.get("quantidade") == null) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "produtoId, unidadeId e quantidade são obrigatórios");
            return ResponseEntity.status(422).body(erro);
        }

        Long produtoId = Long.valueOf(dados.get("produtoId").toString());
        Long unidadeId = Long.valueOf(dados.get("unidadeId").toString());
        Integer quantidade = Integer.valueOf(dados.get("quantidade").toString());

        if (quantidade <= 0) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "A quantidade deve ser maior que zero");
            return ResponseEntity.status(422).body(erro);
        }

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
        if (dados.get("produtoId") == null || dados.get("unidadeId") == null || dados.get("quantidade") == null) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "produtoId, unidadeId e quantidade são obrigatórios");
            return ResponseEntity.status(422).body(erro);
        }

        Long produtoId = Long.valueOf(dados.get("produtoId").toString());
        Long unidadeId = Long.valueOf(dados.get("unidadeId").toString());
        Integer quantidade = Integer.valueOf(dados.get("quantidade").toString());

        if (quantidade <= 0) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "A quantidade deve ser maior que zero");
            return ResponseEntity.status(422).body(erro);
        }

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