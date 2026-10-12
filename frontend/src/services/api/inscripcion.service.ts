import axiosInstance from "./axios";
import type { InscripcionBody } from "../types/types";

export const getAllInscripciones = async () => {
    try {  
        const respuesta = await axiosInstance.get('/inscripciones')      
        return respuesta.data; 
    } catch (error) {
        console.error("Error al obtener las inscripciones", error);
        throw error;
    }
};

export const createInscipcion = async (nuevaInscripcion: InscripcionBody) => {
    try {      
        const respuesta = await axiosInstance.post('/inscripciones', nuevaInscripcion);       
        return respuesta.data;
    } catch (error) {
        console.error("Error al crear la inscripcion:", error);
        throw error;
    }
};

export const getInscipcionById = async (id:number) => {
    try {
        const respuesta = await axiosInstance.get(`/inscipciones/${id}`);
        return respuesta.data;
    } catch (error) {
        console.error("Error al obtener la inscripcion:", error);
        throw error;
    }
};


export const deleteInscipcion = async (id:number) => {
    const respuesta = await axiosInstance.delete(`/inscripciones/${id}`);
    return respuesta.data;
};