package com.malb.gimnasio.controller.DTO.response;

public record LoginDTO(
        String token,
        String tipo,
        long vencimiento,
        UsuarioLoginDTO usuario
) {
}
