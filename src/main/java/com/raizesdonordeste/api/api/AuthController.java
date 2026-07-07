package com.raizesdonordeste.api.api;

import com.raizesdonordeste.api.domain.entity.Usuario;
import com.raizesdonordeste.api.domain.repository.UsuarioRepository;
import com.raizesdonordeste.api.infrastructure.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> dados) {
        String email = dados.get("email");
        String senha = dados.get("senha");

        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isEmpty() || !passwordEncoder.matches(senha, usuario.get().getSenha())) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Email ou senha inválidos");
            return ResponseEntity.status(401).body(erro);
        }

        String token = jwtUtil.gerarToken(email);
        Map<String, String> resposta = new HashMap<>();
        resposta.put("token", token);
        resposta.put("email", email);
        resposta.put("perfil", usuario.get().getPerfil());

        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);
        Map<String, String> resposta = new HashMap<>();
        resposta.put("mensagem", "Usuário cadastrado com sucesso");
        return ResponseEntity.status(201).body(resposta);
    }

}
