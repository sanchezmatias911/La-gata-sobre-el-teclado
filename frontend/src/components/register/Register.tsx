import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import type { RegistroBody } from '../../services/types/types';
import { registroSchema } from '../../services/validations/registro.schema';
import { register } from '../../services/api/auth.service';
import { useDocumentTitle } from "../../hooks/useDocumentTitle.ts";
import './Register.css';

type FormErrors = Partial<Record<keyof RegistroBody, string>>;

export const Register = () => {
    useDocumentTitle('Registrarse');
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);
    const [formData, setFormData] = useState<RegistroBody>({
        nombre: '',
        apellido: '',
        mail: '',
        password: '',
        telefono: '',
        dni: 0
    });

    const [errors, setErrors] = useState<FormErrors>({});

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value, type } = e.target;

        setFormData((prev) => ({
            ...prev,
            [name]: type === 'number' ? (value === '' ? 0 : Number(value)) : value
        }));

        if (errors[name as keyof RegistroBody]) {
            setErrors((prev) => ({ ...prev, [name]: undefined }));
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const validacion = registroSchema.safeParse(formData);

        if (!validacion.success) {
            const newErrors: FormErrors = {};
            validacion.error.issues.forEach((issue) => {
                const fieldName = issue.path[0] as keyof RegistroBody;
                if (fieldName && !newErrors[fieldName]) {
                    newErrors[fieldName] = issue.message;
                }
            });

            setErrors(newErrors);
            toast.warning("Por favor, revisa los campos ingresados.");
            return;
        }

        setErrors({});

        try {
            await register(formData);
            toast.success("¡Bienvenido al Gimnasio la gata sobre el teclado!");
            setTimeout(() => navigate('/clases/'), 1500);
        } catch (error) {
            toast.error("Hubo un error al procesar el registro.");
        }
    };

    return (
        <div className="register-page-wrapper-register">
            <div className="register-content-wrapper-register">
                <div className="register-welcome-panel-register">
                    <h1 className="welcome-title-register">Creá tu cuenta<br/>y empezá a<br/>entrenar.</h1>
                    <p className="welcome-text-register">Unite a La Gata Sobre el Teclado. <br/> Gestioná tus clases, llevá el control de tu rutina y alcanzá tus metas con nosotros.</p>
                </div>

                <div className="register-form-panel-register">
                    <div className="register-form-container-register">
                        <h2 className="register-form-title-register">REGISTRARSE</h2>
                        <p className="register-form-subtitle-register">Completá tus datos para comenzar.</p>

                        <form onSubmit={handleSubmit} className="register-form-register" noValidate>
                            <div className="form-group-register">
                                <label>Nombre</label>
                                <input
                                    type="text"
                                    name="nombre"
                                    value={formData.nombre}
                                    onChange={handleChange}
                                    placeholder="Ej: Juan"
                                    className={errors.nombre ? 'input-error-register' : ''}
                                />
                                {errors.nombre && <span className="error-message-register">{errors.nombre}</span>}
                            </div>

                            <div className="form-group-register">
                                <label>Apellido</label>
                                <input
                                    type="text"
                                    name="apellido"
                                    value={formData.apellido}
                                    onChange={handleChange}
                                    placeholder="Ej: Pérez"
                                    className={errors.apellido ? 'input-error-register' : ''}
                                />
                                {errors.apellido && <span className="error-message-register">{errors.apellido}</span>}
                            </div>

                            <div className="form-group-register">
                                <label>Email</label>
                                <input
                                    type="email"
                                    name="mail"
                                    value={formData.mail}
                                    onChange={handleChange}
                                    placeholder="correo@ejemplo.com"
                                    className={errors.mail ? 'input-error-register' : ''}
                                />
                                {errors.mail && <span className="error-message-register">{errors.mail}</span>}
                            </div>

                            <div className="form-group-register">
                                <label>Contraseña</label>
                                <div className="password-input-wrapper-register">
                                    <input
                                        type={showPassword ? "text" : "password"}
                                        name="password"
                                        value={formData.password}
                                        onChange={handleChange}
                                        placeholder="********"
                                        className={errors.password ? 'input-error-register' : ''}
                                    />
                                    <button
                                        type="button"
                                        className="password-toggle-btn-register"
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
                                {errors.password && <span className="error-message-register">{errors.password}</span>}
                            </div>

                            <div className="form-row-register">
                                <div className="form-group-register">
                                    <label>Teléfono</label>
                                    <input
                                        type="text"
                                        name="telefono"
                                        value={formData.telefono}
                                        onChange={handleChange}
                                        placeholder="+54 9 11 1234-5678"
                                        className={errors.telefono ? 'input-error-register' : ''}
                                    />
                                    {errors.telefono && <span className="error-message-register">{errors.telefono}</span>}
                                </div>

                                <div className="form-group-register">
                                    <label>DNI</label>
                                    <input
                                        type="number"
                                        name="dni"
                                        value={formData.dni === 0 ? '' : formData.dni}
                                        onChange={handleChange}
                                        min="0"
                                        placeholder="Sin puntos"
                                        className={errors.dni ? 'input-error-register' : ''}
                                    />
                                    {errors.dni && <span className="error-message-register">{errors.dni}</span>}
                                </div>
                            </div>

                            <div className="register-actions-register">
                                <button type="submit" className="btn-modern-register btn-primary-register">
                                    Registrarse
                                </button>

                                <div className="login-link-container-register">
                                    ¿Ya tenés cuenta?
                                    <button
                                        type="button"
                                        className="login-link-register"
                                        onClick={() => navigate(-1)}
                                    >
                                        Ingresar
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

export default Register;