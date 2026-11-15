package com.malb.gimnasio.controller.DTO;

public record ApiError(
        int status,
        String message
) {
}