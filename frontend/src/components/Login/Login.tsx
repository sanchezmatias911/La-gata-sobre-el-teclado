
import React from 'react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../../services/api/auth.service';
import { useAuth } from '../../context/AuthContext';
import  type {LoginBody} from '../../services/types/types';
import {toast} from 'react-toastify';
import '../ClaseForm/ClaseForm.css'; 
import './Login.css';
import {loginSchema}  from '../../services/validations/login.schema';
type FormErrors = Partial<Record<keyof LoginBody, string>>;

export const Login = () => {
    const navigate = useNavigate();
    const { loginContext } = useAuth();
    const [loading, setLoading] = useState(false);
    
    const [formData, setFormData] = useState<LoginBody>({
        mail: '',
        contraseña: ''
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
        
        // 4. Validación con Zod
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
            return; // Cortamos la ejecución si hay errores
        }

        setErrors({}); // Limpiamos errores si Zod da luz verde

        try {
            setLoading(true);
            const response = await login(formData);
            loginContext(response.token, response.usuario);
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
                            className={errors.mail ? 'input-error' : ''} // Clase de error
                        />
                        {/* Mensaje de error de Zod */}
                        {errors.mail && <span className="error-message">{errors.mail}</span>}
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
                            className={errors.contraseña ? 'input-error' : ''} // Clase de error
                        />
                        {/* Mensaje de error de Zod */}
                        {errors.contraseña && <span className="error-message">{errors.contraseña}</span>}
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