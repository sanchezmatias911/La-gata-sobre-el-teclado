import axiosInstance from "./axios";
import type { ClaseProgramadaBody } from "../types/types";

export const getAllClases = async () => {
    try {  
        const respuesta = await axiosInstance.get('/clases')      
        return respuesta.data; 
    } catch (error) {
        console.error("Error al obtener las clases", error);
        throw error;
    }
};

export const createClass = async (nuevaClase: ClaseProgramadaBody) => {
    try {      
        const respuesta = await axiosInstance.post('/clases', nuevaClase);       
        return respuesta.data;
    } catch (error) {
        console.error("Error al crear la clase:", error);
        throw error;
    }
};

export const getClassById = async (id:number) => {
    try {
        const respuesta = await axiosInstance.get(`/clases/${id}`);
        return respuesta.data;
    } catch (error) {
        console.error("Error al obtener la clase:", error);
        throw error;
    }
};


export const deleteActivity = async (id:number) => {
    const respuesta = await axiosInstance.delete(`/inscripciones/${id}`);
    return respuesta.data;
};