export const TOKEN_KEY = "token-gym";
export const USER_KEY = "USUARIO_GYMNACIO";

export const getToken = (): string | null => {
	return localStorage.getItem(TOKEN_KEY);
};

export const setToken = (token: string): void => {
	localStorage.setItem(TOKEN_KEY, token);
};

export const removeToken = (): void => {
	localStorage.removeItem(TOKEN_KEY);
};
