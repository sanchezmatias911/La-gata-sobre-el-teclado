import axiosInstance from "./axios";
import type {  RegistroBody } from "../types/types";

export const getAllClientes = async () => {
    try {  
        const respuesta = await axiosInstance.get('/clientes')      
        return respuesta.data; 
    } catch (error) {
        console.error("Error al obtener al cliente", error);
        throw error;
    }
};

export const createCliente = async (nuevoCliente: RegistroBody) => {
    try {      
        const respuesta = await axiosInstance.post('/auth/registro', nuevoCliente);       
        return respuesta.data;
    } catch (error) {
        console.error("Error al crear el cliente:", error);
        throw error;
    }
};

export const getClienteById = async (dni:number) => {
    try {
        const respuesta = await axiosInstance.get(`/clientes/${dni}`);
        return respuesta.data;
    } catch (error) {
        console.error("Error al obtener al cliente:", error);
        throw error;
    }
};


export const deleteCliente = async (dni:number) => {
    const respuesta = await axiosInstance.delete(`/clientes/${dni}`);
    return respuesta.data;
};