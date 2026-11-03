
import React from 'react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../../services/api/auth.service';
import { useAuth } from '../../context/AuthContext';
import  type {LoginBody} from '../../services/types/types';
import {toast} from 'react-toastify';
import '../ClaseForm/ClaseForm.css'; 
import './Login.css';

export const Login = () => {
    const navigate = useNavigate();
    const { loginContext } = useAuth();
    const [loading, setLoading] = useState(false);
    
    const [formData, setFormData] = useState<LoginBody>({
        mail: '',
        contraseña: ''
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        
        if (!formData.mail || !formData.contraseña) {
            toast.warning("Por favor, completá todos los campos.");
            return;
        }

        try {
            setLoading(true);
            const response = await login(formData);
            loginContext(response.token, response.usuario);
            toast.success("¡Bienvenido/a de vuelta!");
            navigate('/clases');
        } catch (error) {
            toast.error("Correo o contraseña incorrectos.");
            console.error("Error en login:", error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="form-page-container">
            {/* Agregamos la clase login-box junto con form-content */}
            <div className="form-content login-box">
                <h2 className="form-title">Iniciar Sesión</h2>
                
                <form onSubmit={handleSubmit} className="custom-form">
                    <div className="form-group">
                        <label>Email</label>
                        <input 
                            type="email" 
                            name="mail" 
                            value={formData.mail} 
                            onChange={handleChange} 
                            placeholder="correo@ejemplo.com"
                            disabled={loading}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Contraseña</label>
                        <input 
                            type="password" 
                            name="contraseña" 
                            value={formData.contraseña} 
                            onChange={handleChange} 
                            placeholder="********"
                            disabled={loading}
                            required
                        />
                    </div>
                    
                    {/* Usamos la nueva clase login-actions */}
                    <div className="login-actions">
                        <button 
                            type="submit" 
                            className="btn-ingresar" 
                            disabled={loading}
                        >
                            {loading ? 'Ingresando...' : 'Ingresar'}
                        </button>
                        
                        <button 
                            type="button" 
                            /* Combinamos tu clase btn-cancelar con btn-crear-cuenta */
                            className="btn-cancelar btn-crear-cuenta" 
                            onClick={() => navigate('/registrar/cliente')}
                        >
                            Creá tu cuenta
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default Login;