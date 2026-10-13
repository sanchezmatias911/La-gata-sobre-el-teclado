package com.malb.gimnasio.exception;

import com.malb.gimnasio.modelo.exception.HorarioInvalido;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //404
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> manejarNoEncontrado(NoSuchElementException ex, HttpServletRequest request) {
        ApiError error = new ApiError(404, ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    //400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ApiError error = new ApiError(400, "Los datos enviados no son válidos.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler({
            ConstraintViolationException.class,
            HorarioInvalido.class
    })
    public ResponseEntity<ApiError> manejarRestricciones(ConstraintViolationException ex, HttpServletRequest request) {
        ApiError error = new ApiError(400, "Uno o más parámetros no cumplen las restricciones.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> manejarJsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest request) {
        ApiError error = new ApiError(400, "El cuerpo de la solicitud está vacío, mal formado o contiene valores inválidos.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> manejarArgumentoInvalido(IllegalArgumentException ex, HttpServletRequest request) {
        ApiError error = new ApiError(400,
                ex.getMessage() != null
                        ? ex.getMessage()
                        : "Uno o más argumentos son inválidos."
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    //409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> manejarIntegridad(DataIntegrityViolationException ex, HttpServletRequest request) {
        ApiError error = new ApiError(409, "La operación entra en conflicto con los datos existentes o sus relaciones.");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarErrorInterno(Exception ex, HttpServletRequest request) {
        ApiError error = new ApiError(500, "Ocurrió un error interno. Intentá nuevamente más tarde.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}