package com.malb.gimnasio.service.exception;

public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String message) {
        super("No existe un/a " + message + ".");
    }
}
