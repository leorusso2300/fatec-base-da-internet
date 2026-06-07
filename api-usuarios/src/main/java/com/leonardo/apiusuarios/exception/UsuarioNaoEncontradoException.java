package com.leonardo.apiusuarios.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException(String cpf) {
        super("Usuário com CPF " + cpf + " não foi encontrado");
    }
}
