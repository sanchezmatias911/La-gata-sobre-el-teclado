package com.malb.gimnasio.controller.DTO.request;
import jakarta.validation.constraints.NotNull;

public record SuscribirClaseDTO(
        @NotNull
        Long idCliente
) {
}
