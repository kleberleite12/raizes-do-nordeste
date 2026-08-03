package com.raizesdonordeste.api.api;

import com.raizesdonordeste.api.domain.entity.Unidade;
import com.raizesdonordeste.api.domain.repository.UnidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    @Autowired
    private UnidadeRepository unidadeRepository;

    @GetMapping
    public Page<Unidade> listar(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int limit) {
        if (page < 1) {
            page = 1;
        }
        if (limit < 1) {
            limit = 10;
        }
        return unidadeRepository.findAll(PageRequest.of(page - 1, limit));
    }

    @PostMapping
    public ResponseEntity<Unidade> criar(@RequestBody Unidade unidade) {
        Unidade unidadeSalva = unidadeRepository.save(unidade);
        return ResponseEntity.status(201).body(unidadeSalva);
    }

}
