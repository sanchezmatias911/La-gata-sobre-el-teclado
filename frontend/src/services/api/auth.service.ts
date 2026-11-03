import axiosInstance from "./axios";
import type { LoginBody } from "../types/types";

export const login = async (loginData : LoginBody) => {
    try {
        const respuesta = await axiosInstance.post(`/auth/login`, loginData);
        return respuesta.data;
    } catch (error) {
        console.error("Error loguearse", error);
        throw error;
    }
};