// src/context/AuthContext.tsx
import { createContext, useContext, useState } from 'react';
import type { ReactNode } from 'react';
import { getToken, setToken, removeToken, USER_KEY } from '../services/storage/storage';
import type { UserInfo, AuthContextType } from '../services/types/types';

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [user, setUser] = useState<UserInfo | null>(() => {
        const savedUser = localStorage.getItem(USER_KEY);
        return savedUser ? JSON.parse(savedUser) : null;
    });

    const loginContext = (token: string, usuario: UserInfo) => {
        setToken(token); 
        localStorage.setItem(USER_KEY, JSON.stringify(usuario)); 
        setUser(usuario);
    };
   
    const logoutContext = () => {
        removeToken(); 
        localStorage.removeItem(USER_KEY);
        setUser(null);
    };
   
    const isAuthenticated = !!getToken(); 

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
