import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { login } from '../../services/api/auth.service';
import { useAuth } from '../../context/AuthContext';
import  type { LoginBody } from '../../services/types/types';
import { loginSchema }  from '../../services/validations/login.schema';
import './Login.css';

type FormErrors = Partial<Record<keyof LoginBody, string>>;

export const Login = () => {
    const navigate = useNavigate();
    const { loginContext } = useAuth();
    const [loading, setLoading] = useState(false);

    const [formData, setFormData] = useState<LoginBody>({
        email: '',
        password: ''
    });

    const [errors, setErrors] = useState<FormErrors>({});

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));

        if (errors[name as keyof LoginBody]) {
            setErrors(prev => ({ ...prev, [name]: undefined }));
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

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
        <div className="login-page-wrapper">

            <div className="login-welcome-panel">
                <h1 className="welcome-title">Bienvenido/a<br/>de nuevo al<br/>gimnasio.</h1>
                <p className="welcome-text">Retomá tu rutina. <br/> Ingresá para ver tus clases, administrar tus inscripciones y seguir superándote.</p>
            </div>

            <div className="login-form-panel">
                <div className="login-form-container">
                    <h2 className="login-form-title">INICIAR SESIÓN</h2>
                    <p className="login-form-subtitle">Ingresá tus credenciales para continuar.</p>

                    <form onSubmit={handleSubmit} className="login-form" noValidate>
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
                                type="button"
                                className="btn-modern btn-secondary"
                                onClick={() => navigate('/registrar/cliente')}
                                disabled={loading}
                            >
                                Creá tu cuenta
                            </button>

                            <button
                                type="submit"
                                className="btn-modern btn-primary"
                                disabled={loading}
                            >
                                {loading ? 'Ingresando...' : 'Ingresar'}
                            </button>

                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default Login;