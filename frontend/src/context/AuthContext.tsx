import type { ReactNode } from 'react';
import { createContext, useContext, useState } from 'react';
import type { LoginResponse, UserInfo, AuthContextType } from '../services/types/types';
import { setToken, USER_KEY, isTokenValid, clearStorage, setExpiration } from '../services/storage/storage';

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [user, setUser] = useState<UserInfo | null>(() => {

        const savedUser = localStorage.getItem(USER_KEY);
        if (!isTokenValid()) {
            clearStorage();
            return null;
        }
        return savedUser ? JSON.parse(savedUser) : null;
    });

   const loginContext = (data: LoginResponse) => {
    setToken(data.token); 
    
    const tiempoExpiracionMs = Date.now() + (data.vencimiento * 1000);
    setExpiration(tiempoExpiracionMs);
    
    localStorage.setItem(USER_KEY, JSON.stringify(data.usuario)); 
    setUser(data.usuario);
};
   
    const logoutContext = () => {
        clearStorage();
        setUser(null);
    };
   
    const isAuthenticated = isTokenValid();

    return (
        <AuthContext.Provider value={{ user, isAuthenticated, loginContext, logoutContext }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) throw new Error("useAuth debe usarse dentro de AuthProvider");
    return context;
};