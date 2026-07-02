package com.raizesdonordeste.api.api;

import com.raizesdonordeste.api.domain.entity.Unidade;
import com.raizesdonordeste.api.domain.repository.UnidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    @Autowired
    private UnidadeRepository unidadeRepository;

    @GetMapping
    public List<Unidade> listar() {
        return unidadeRepository.findAll();
    }

    @PostMapping
    public Unidade criar(@RequestBody Unidade unidade) {
        return unidadeRepository.save(unidade);
    }

}
