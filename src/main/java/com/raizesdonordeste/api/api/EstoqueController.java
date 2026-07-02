package com.raizesdonordeste.api.api;

import com.raizesdonordeste.api.domain.entity.Estoque;
import com.raizesdonordeste.api.domain.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

}
