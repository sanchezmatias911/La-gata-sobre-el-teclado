package com.malb.gimnasio.exception;

public record ApiError(
        int status,
        String message
) {
}