package com.malb.gimnasio.security.error;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public enum MotivoRechazoJwt {
    VENCIDO(MensajesDeSeguridad.SESION_VENCIDA),
    INVALIDO(MensajesDeSeguridad.TOKEN_INVALIDO);

    private static final String ATRIBUTO_DEL_REQUEST = "jwt.motivo-rechazo";

    private final String mensaje;

    MotivoRechazoJwt(String mensaje) {
        this.mensaje = mensaje;
    }

    public String mensaje() {
        return mensaje;
    }

    public void registrarEn(HttpServletRequest request) {
        request.setAttribute(ATRIBUTO_DEL_REQUEST, this);
    }

    public static Optional<MotivoRechazoJwt> registradoEn(HttpServletRequest request) {
        Object motivo = request.getAttribute(ATRIBUTO_DEL_REQUEST);

        return motivo instanceof MotivoRechazoJwt registrado
                ? Optional.of(registrado)
                : Optional.empty();
    }
}
