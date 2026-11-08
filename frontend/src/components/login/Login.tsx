
import React from 'react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../../services/api/auth.service';
import { useAuth } from '../../context/AuthContext';
import  type {LoginBody} from '../../services/types/types';
import {toast} from 'react-toastify';
import '../claseForm/ClaseForm.css'; 
import './Login.css';
import {loginSchema}  from '../../services/validations/login.schema';
type FormErrors = Partial<Record<keyof LoginBody, string>>;

export const Login = () => {
    const navigate = useNavigate();
    const { loginContext } = useAuth();
    const [loading, setLoading] = useState(false);
    
    const [formData, setFormData] = useState<LoginBody>({
        email: '',
        password: '' 
    });

    // 3. Estado para guardar los errores
    const [errors, setErrors] = useState<FormErrors>({});

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));

        // Limpiamos el error del campo específico cuando el usuario empieza a escribir
        if (errors[name as keyof LoginBody]) {
            setErrors(prev => ({ ...prev, [name]: undefined }));
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        
        
        // Validación con Zod
        const validacion = loginSchema.safeParse(formData);
        
        if (!validacion.success) {
            const newErrors: FormErrors = {};
            validacion.error.issues.forEach((issue) => {
                const fieldName = issue.path[0] as keyof LoginBody;
                if (fieldName && !newErrors[fieldName]) {
                    newErrors[fieldName] = issue.message;
                }
            });
            
            setErrors(newErrors);
            toast.warning("Por favor, revisá los campos ingresados.");
            return; 
        }

        setErrors({}); 

        try {
            setLoading(true);
            const response = await login(formData);
            
            loginContext(response);
            toast.success("¡Bienvenido/a de vuelta! A entrenar se ha dicho.");
            navigate('/clases');
        } catch (error) {
            toast.error("Correo o contraseña incorrectos.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="form-page-container">
            <div className="form-content login-box">
                <h2 className="form-title">Iniciar Sesión</h2>
                
                <form onSubmit={handleSubmit} className="custom-form" noValidate>
                    <div className="form-group">
                        <label>Email</label>
                        <input 
                            type="email" 
                            name="email" 
                            value={formData.email} 
                            onChange={handleChange} 
                            placeholder="correo@ejemplo.com"
                            disabled={loading}
                            className={errors.email ? 'input-error' : ''} 
                        />
                        {errors.email && <span className="error-message">{errors.email}</span>}
                    </div>

                    <div className="form-group">
                        <label>Contraseña</label>
                        <input 
                            type="password" 
                            name="password" 
                            value={formData.password} 
                            onChange={handleChange} 
                            placeholder="********"
                            disabled={loading}
                            className={errors.password ? 'input-error' : ''} 
                        />
                        {errors.password && <span className="error-message">{errors.password}</span>}
                    </div>
                    
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