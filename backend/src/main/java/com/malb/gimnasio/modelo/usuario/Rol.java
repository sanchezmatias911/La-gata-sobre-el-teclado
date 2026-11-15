package com.malb.gimnasio.modelo.usuario;

public enum Rol {
    CLIENTE, ADMIN;

    public String autoridad() {
        return "ROLE_" + name();
    }
}
