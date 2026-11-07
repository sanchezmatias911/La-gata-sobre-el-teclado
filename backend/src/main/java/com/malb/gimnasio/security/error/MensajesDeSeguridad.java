package com.malb.gimnasio.security.error;

public final class MensajesDeSeguridad {

    public static final String SIN_TOKEN = "Necesitás iniciar sesión para acceder a este recurso.";
    public static final String SESION_VENCIDA = "Tu sesión venció. Iniciá sesión nuevamente.";
    public static final String TOKEN_INVALIDO = "Token inválido.";
    public static final String CREDENCIALES_INCORRECTAS = "Mail o contraseña incorrectos.";
    public static final String ACCESO_DENEGADO = "No tenés permisos para realizar esta acción.";
    public static final String OPERAR_SOBRE_OTRO_CLIENTE = "No podés operar sobre otro cliente.";

    private MensajesDeSeguridad() {
    }
}
