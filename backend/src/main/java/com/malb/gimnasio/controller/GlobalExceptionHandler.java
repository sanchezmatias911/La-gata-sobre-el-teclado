package com.malb.gimnasio.controller;

import com.malb.gimnasio.controller.DTO.ApiError;
import com.malb.gimnasio.modelo.exception.HorarioInvalido;
import com.malb.gimnasio.service.exception.InscripcionNoPermitidaException;
import com.malb.gimnasio.service.exception.RecursoDuplicadoException;
import com.malb.gimnasio.service.exception.RecursoEnUsoException;
import com.malb.gimnasio.service.exception.RecursoNoEncontradoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404
    @ExceptionHandler({NoSuchElementException.class, RecursoNoEncontradoException.class})
    public ResponseEntity<ApiError> manejarNoEncontrado(RuntimeException ex) {

        ApiError error = new ApiError(404, ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    //400
    @ExceptionHandler(MethodArgumentNotValidException.class) //es lo que lanzan los dtos
    public ResponseEntity<ApiError> manejarValidacion(MethodArgumentNotValidException ex) {
        ApiError error = new ApiError(400, "Los datos enviados no son válidos.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler({HorarioInvalido.class}) //cuando la condicion no se cumple
    public ResponseEntity<ApiError> manejarRestricciones(HorarioInvalido ex) {
        ApiError error = new ApiError(400, ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> manejarJsonInvalido(HttpMessageNotReadableException ex) {
        ApiError error = new ApiError(400, "El cuerpo de la solicitud está vacío, mal formado o contiene valores inválidos.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> manejarArgumentoInvalido(IllegalArgumentException ex) {
        ApiError error = new ApiError(400, ex.getMessage() != null ? ex.getMessage() : "Uno o más argumentos son inválidos.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 405
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> manejarMetodoNoPermitido(HttpRequestMethodNotSupportedException ex) {

        ApiError error = new ApiError(405, "El método HTTP utilizado no está permitido para este recurso.");

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }

    //409
    @ExceptionHandler({
            RecursoEnUsoException.class,
            InscripcionNoPermitidaException.class,
            RecursoDuplicadoException.class
    })
    public ResponseEntity<ApiError> manejarIntegridad(RuntimeException ex) {
        ApiError error = new ApiError(409, ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarErrorInterno(Exception ex) {
        ApiError error = new ApiError(500, "Ocurrió un error interno. Intentá nuevamente más tarde.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
