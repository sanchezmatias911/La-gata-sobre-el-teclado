import axiosInstance from "./axios";
import type { ProfesorBody } from "../types/types";

export const getAllProfesores = async () => {
    try {  
        const respuesta = await axiosInstance.get('/profesores')      
        return respuesta.data; 
    } catch (error) {
        console.error("Error al obtener a los profesores", error);
        throw error;
    }
};

export const createProfesor = async (nuevoProfesor: ProfesorBody) => {
    try {      
        const respuesta = await axiosInstance.post('/profesores', nuevoProfesor);       
        return respuesta.data;
    } catch (error) {
        console.error("Error al crear al profesor:", error);
        throw error;
    }
};

export const getProfesorById = async (dni:number) => {
    try {
        const respuesta = await axiosInstance.get(`/profesores/${dni}`);
        return respuesta.data;
    } catch (error) {
        console.error("Error al obtener al profesor:", error);
        throw error;
    }
};


export const deleteProfesor = async (dni:number) => {
    const respuesta = await axiosInstance.delete(`/profesores/${dni}`);
    return respuesta.data;
};