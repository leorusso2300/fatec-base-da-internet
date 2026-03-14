package com.leonardo.apiusuarios.controller;

import com.leonardo.apiusuarios.model.Usuario;
import com.leonardo.apiusuarios.repository.UsuarioRepository;
import com.leonardo.apiusuarios.exception.UsuarioNaoEncontradoException;
import com.leonardo.apiusuarios.exception.CpfJaExisteException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "API de gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários", description = "Utilize o Get para listar todos os usuários")
    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    @GetMapping("/{cpf}")
    @Operation(summary = "Buscar usuário por CPF", description = "Utilize o GET para buscar um usuário específico pelo CPF")
    public Usuario buscarUsuario(@PathVariable String cpf) {
        return repository.findByCpf(cpf)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(cpf));
    }

    @GetMapping("/status")
    @Operation(summary = "Status da API", description = "Verifica se a API está funcionando")
    public String status() {
        return "API de usuários funcionando!";
    }

    @PostMapping
    @Operation(summary = "Criar novo usuário", description = "Utilize o POST para criar um novo usuário")
    public ResponseEntity<Map<String, Object>> criarUsuario(@Valid @RequestBody Usuario usuario) {
        
        if (repository.findByCpf(usuario.getCpf()).isPresent()) {
            throw new CpfJaExisteException(usuario.getCpf());
        }
        
        Usuario usuarioSalvo = repository.save(usuario);

        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Usuário cadastrado com sucesso");
        response.put("usuario", usuarioSalvo);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{cpf}")
    @Operation(summary = "Atualizar usuário", description = "Utilize o PUT para atualizar um usuário existente pelo CPF")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable String cpf, @Valid @RequestBody Usuario usuario) {

        Usuario usuarioExistente = repository.findByCpf(cpf)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(cpf));

        usuarioExistente.setNome(usuario.getNome());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setCpf(usuario.getCpf());

        Usuario atualizado = repository.save(usuarioExistente);

        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{cpf}")
    @Operation(summary = "Deletar usuário", description = "Utilize o DELETE para deletar um usuário pelo CPF")
    public ResponseEntity<Void> deletarUsuario(@PathVariable String cpf) {

        Usuario usuario = repository.findByCpf(cpf)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(cpf));

        repository.delete(usuario);

        return ResponseEntity.noContent().build();
    }
}
