import axiosInstance from "./axios";
import type { LoginBody, RegistroBody } from "../types/types";

export const login = async (loginData : LoginBody) => {
    try {
        const respuesta = await axiosInstance.post(`/auth/login`, loginData);
        return respuesta.data;
    } catch (error) {
        console.error("Error loguearse", error);
        throw error;
    }
};

export const register = async (nuevoCliente: RegistroBody) => {
    try {      
        const respuesta = await axiosInstance.post('/auth/registro', nuevoCliente);       
        return respuesta.data;
    } catch (error) {
        console.error("Error al registrarse:", error);
        throw error;
    }
};