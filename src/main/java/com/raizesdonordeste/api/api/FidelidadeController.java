package com.raizesdonordeste.api.api;

import com.raizesdonordeste.api.domain.entity.Fidelidade;
import com.raizesdonordeste.api.domain.repository.FidelidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/fidelidade")
public class FidelidadeController {

    @Autowired
    private FidelidadeRepository fidelidadeRepository;

    @PostMapping("/inscricao")
    public ResponseEntity<?> inscrever(@RequestBody Map<String, Object> dados) {
        Long clienteId = Long.valueOf(dados.get("clienteId").toString());
        Boolean consentimento = (Boolean) dados.get("consentimento");

        if (!consentimento) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "É necessário consentimento para participar do programa");
            return ResponseEntity.status(400).body(erro);
        }

        Optional<Fidelidade> existente = fidelidadeRepository.findByClienteId(clienteId);
        if (existente.isPresent()) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Cliente já está inscrito no programa de fidelidade");
            return ResponseEntity.status(409).body(erro);
        }

        Fidelidade fidelidade = new Fidelidade();
        fidelidade.setClienteId(clienteId);
        fidelidade.setPontos(0);
        fidelidade.setConsentimento(consentimento);
        fidelidadeRepository.save(fidelidade);

        Map<String, String> resposta = new HashMap<>();
        resposta.put("mensagem", "Cliente inscrito no programa de fidelidade com sucesso");
        return ResponseEntity.status(201).body(resposta);
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<?> consultar(@PathVariable Long clienteId) {
        Optional<Fidelidade> fidelidade = fidelidadeRepository.findByClienteId(clienteId);

        if (fidelidade.isEmpty()) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Cliente não encontrado no programa de fidelidade");
            return ResponseEntity.status(404).body(erro);
        }

        return ResponseEntity.ok(fidelidade.get());
    }

    @PostMapping("/adicionar-pontos")
    public ResponseEntity<?> adicionarPontos(@RequestBody Map<String, Object> dados) {
        Long clienteId = Long.valueOf(dados.get("clienteId").toString());
        Integer pontos = (Integer) dados.get("pontos");

        Optional<Fidelidade> fidelidade = fidelidadeRepository.findByClienteId(clienteId);

        if (fidelidade.isEmpty()) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Cliente não está inscrito no programa de fidelidade");
            return ResponseEntity.status(404).body(erro);
        }

        fidelidade.get().setPontos(fidelidade.get().getPontos() + pontos);
        fidelidadeRepository.save(fidelidade.get());

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("mensagem", "Pontos adicionados com sucesso");
        resposta.put("pontosAtuais", fidelidade.get().getPontos());
        return ResponseEntity.ok(resposta);
    }

}
