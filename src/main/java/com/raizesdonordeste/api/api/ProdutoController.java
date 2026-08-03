package com.raizesdonordeste.api.api;

import com.raizesdonordeste.api.domain.entity.Produto;
import com.raizesdonordeste.api.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public Page<Produto> listar(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int limit) {
        if (page < 1) {
            page = 1;
        }
        if (limit < 1) {
            limit = 10;
        }
        return produtoRepository.findAll(PageRequest.of(page - 1, limit));
    }

    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
        Produto produtoSalvo = produtoRepository.save(produto);
        return ResponseEntity.status(201).body(produtoSalvo);
    }

}