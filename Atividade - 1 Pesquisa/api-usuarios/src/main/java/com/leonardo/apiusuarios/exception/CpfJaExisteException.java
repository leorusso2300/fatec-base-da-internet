package com.leonardo.apiusuarios.exception;

public class CpfJaExisteException extends RuntimeException {
    public CpfJaExisteException(String cpf) {
        super("Já existe um usuário cadastrado com o CPF " + cpf);
    }
}
