import axiosInstance from "./axios";
import type { ActividadBody } from "../types/types";

export const getAllActivities = async () => {
    try {  
        const respuesta = await axiosInstance.get('/actividades')      
        return respuesta.data; 
    } catch (error) {
        console.error("Error al obtener las actividades", error);
        throw error;
    }
};

export const createActivity = async (nuevaActividad: ActividadBody) => {
    try {      
        const respuesta = await axiosInstance.post('/actividades', nuevaActividad);       
        return respuesta.data;
    } catch (error) {
        console.error("Error al crear el producto:", error);
        throw error;
    }
};

export const getActivityById = async (id:number) => {
    try {
        const respuesta = await axiosInstance.get(`/actividades/${id}`);
        return respuesta.data;
    } catch (error) {
        console.error("Error al obtener la actividad:", error);
        throw error;
    }
};


export const deleteActivity = async (id:number) => {
    const respuesta = await axiosInstance.delete(`/actividades/${id}`);
    return respuesta.data;
};