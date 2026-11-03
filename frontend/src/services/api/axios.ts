import axios from "axios";
import { removeToken, TOKEN_KEY, } from "../storage/storage";



const axiosInstance = axios.create({ 
    baseURL: "http://localhost:8080/" 
});

axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response && error.response.status === 401) {
            removeToken();
            localStorage.removeItem(TOKEN_KEY);
            window.location.href = "/login";
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;




