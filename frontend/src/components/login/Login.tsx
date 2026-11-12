import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import  type { LoginBody } from '../../services/types/types';
import { login } from '../../services/api/auth.service';
import { useAuth } from '../../context/AuthContext';
import { loginSchema }  from '../../services/validations/login.schema';
import { useDocumentTitle } from "../../hooks/useDocumentTitle.ts";
import './Login.css';

type FormErrors = Partial<Record<keyof LoginBody, string>>;

export const Login = () => {
    useDocumentTitle('Iniciar Sesión');
    const navigate = useNavigate();
    const { loginContext } = useAuth();
    const [loading, setLoading] = useState(false);
    const [showPassword, setShowPassword] = useState(false);

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
        <div className="login-page-wrapper-login">
            <div className="login-content-wrapper-login">
                <div className="login-welcome-panel-login">
                    <h1 className="welcome-title-login">Bienvenido/a<br/>de nuevo al<br/>gimnasio.</h1>
                    <p className="welcome-text-login">Retomá tu rutina. <br/> Ingresá para ver tus clases, administrar tus inscripciones y seguir superándote.</p>
                </div>

                <div className="login-form-panel-login">
                    <div className="login-form-container-login">
                        <h2 className="login-form-title-login">INICIAR SESIÓN</h2>
                        <p className="login-form-subtitle-login">Ingresá tus credenciales para continuar.</p>

                        <form onSubmit={handleSubmit} className="login-form-login" noValidate>
                            <div className="form-group-login">
                                <label>Email</label>
                                <input
                                    type="email"
                                    name="email"
                                    value={formData.email}
                                    onChange={handleChange}
                                    placeholder="correo@ejemplo.com"
                                    disabled={loading}
                                    className={errors.email ? 'input-error-login' : ''}
                                />
                                {errors.email && <span className="error-message-login">{errors.email}</span>}
                            </div>

                            <div className="form-group-login">
                                <label>Contraseña</label>
                                <div className="password-input-wrapper-login">
                                    <input
                                        type={showPassword ? "text" : "password"}
                                        name="password"
                                        value={formData.password}
                                        onChange={handleChange}
                                        placeholder="********"
                                        disabled={loading}
                                        className={errors.password ? 'input-error-login' : ''}
                                    />
                                    <button
                                        type="button"
                                        className="password-toggle-btn-login"
                                        onClick={() => setShowPassword(!showPassword)}
                                        tabIndex={-1}
                                    >
                                        {showPassword ? (
                                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path><line x1="1" y1="1" x2="23" y2="23"></line></svg>
                                        ) : (
                                            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle></svg>
                                        )}
                                    </button>
                                </div>
                                {errors.password && <span className="error-message-login">{errors.password}</span>}
                            </div>

                            <div className="login-actions-login">
                                <button
                                    type="submit"
                                    className="btn-modern-login btn-primary-login"
                                    disabled={loading}
                                >
                                    {loading ? 'Ingresando...' : 'Ingresar'}
                                </button>

                                <div className="register-link-container-login">
                                    ¿No tenés cuenta?
                                    <button
                                        type="button"
                                        className="register-link-login"
                                        onClick={() => navigate('/registrar/cliente')}
                                        disabled={loading}
                                    >
                                        Registrate
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Login;