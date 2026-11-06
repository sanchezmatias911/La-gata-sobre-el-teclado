export const TOKEN_KEY = "token-gym";
export const USER_KEY = "USUARIO_GYMNACIO";
export const EXPIRATION_KEY = "TOKEN_EXPIRATION";

export const getToken = (): string | null => {
	return localStorage.getItem(TOKEN_KEY);
};

export const setToken = (token: string): void => {
	localStorage.setItem(TOKEN_KEY, token);
};

export const removeToken = (): void => {
	localStorage.removeItem(TOKEN_KEY);
};

export const setExpiration = (vencimiento: number): void => {
    localStorage.setItem(EXPIRATION_KEY, vencimiento.toString());
};

export const isTokenValid = (): boolean => {
    const token = getToken();
    const expiration = localStorage.getItem(EXPIRATION_KEY);
    
    if (!token || !expiration) return false;
    return Date.now() < Number(expiration); 
};

export const clearStorage = (): void => {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    localStorage.removeItem(EXPIRATION_KEY);
};