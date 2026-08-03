package com.raizesdonordeste.api.api;

import com.raizesdonordeste.api.domain.entity.Usuario;
import com.raizesdonordeste.api.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public Page<Usuario> listar(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int limit) {
        if (page < 1) {
            page = 1;
        }
        if (limit < 1) {
            limit = 10;
        }
        return usuarioRepository.findAll(PageRequest.of(page - 1, limit));
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return ResponseEntity.status(201).body(usuarioSalvo);
    }

}