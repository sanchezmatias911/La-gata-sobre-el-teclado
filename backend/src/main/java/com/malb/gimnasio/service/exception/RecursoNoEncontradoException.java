package com.malb.gimnasio.service.exception;

public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String tipo, Long id) {
        super("No existe un/a " + tipo + " con " + id + ".");
    }
}
