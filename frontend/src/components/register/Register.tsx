import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import type { RegistroBody } from '../../services/types/types';
import { registroSchema } from '../../services/validations/registro.schema';
import { register } from '../../services/api/auth.service';
import './Register.css';

type FormErrors = Partial<Record<keyof RegistroBody, string>>;

export const Register = () => {
    const navigate = useNavigate();
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
        <div className="register-page-wrapper">

            <div className="register-welcome-panel">
                <h1 className="welcome-title">Creá tu cuenta<br/>y empezá a<br/>entrenar.</h1>
                <p className="welcome-text">Unite a La gata sobre el teclado. <br/> Gestioná tus clases, llevá el control de tu rutina y alcanzá tus metas con nosotros.</p>
            </div>

            <div className="register-form-panel">
                <div className="register-form-container">
                    <h2 className="register-form-title">REGISTRARSE</h2>
                    <p className="register-form-subtitle">Completá tus datos para comenzar.</p>

                    <form onSubmit={handleSubmit} className="register-form">
                        <div className="form-group">
                            <label>Nombre</label>
                            <input
                                type="text"
                                name="nombre"
                                value={formData.nombre}
                                onChange={handleChange}
                                placeholder="Ej: Juan"
                                className={errors.nombre ? 'input-error' : ''}
                            />
                            {errors.nombre && <span className="error-message">{errors.nombre}</span>}
                        </div>

                        <div className="form-group">
                            <label>Apellido</label>
                            <input
                                type="text"
                                name="apellido"
                                value={formData.apellido}
                                onChange={handleChange}
                                placeholder="Ej: Pérez"
                                className={errors.apellido ? 'input-error' : ''}
                            />
                            {errors.apellido && <span className="error-message">{errors.apellido}</span>}
                        </div>

                        <div className="form-group">
                            <label>Email</label>
                            <input
                                type="email"
                                name="mail"
                                value={formData.mail}
                                onChange={handleChange}
                                placeholder="correo@ejemplo.com"
                                className={errors.mail ? 'input-error' : ''}
                            />
                            {errors.mail && <span className="error-message">{errors.mail}</span>}
                        </div>

                        <div className="form-group">
                            <label>Contraseña</label>
                            <input
                                type="password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                placeholder="********"
                                className={errors.password ? 'input-error' : ''}
                            />
                            {errors.password && <span className="error-message">{errors.password}</span>}
                        </div>

                        <div className="form-row">
                            <div className="form-group">
                                <label>Teléfono</label>
                                <input
                                    type="text"
                                    name="telefono"
                                    value={formData.telefono}
                                    onChange={handleChange}
                                    placeholder="+54 9 11 1234-5678"
                                    className={errors.telefono ? 'input-error' : ''}
                                />
                                {errors.telefono && <span className="error-message">{errors.telefono}</span>}
                            </div>

                            <div className="form-group">
                                <label>DNI</label>
                                <input
                                    type="number"
                                    name="dni"
                                    value={formData.dni === 0 ? '' : formData.dni}
                                    onChange={handleChange}
                                    min="0"
                                    placeholder="Sin puntos"
                                    className={errors.dni ? 'input-error' : ''}
                                />
                                {errors.dni && <span className="error-message">{errors.dni}</span>}
                            </div>
                        </div>

                        <div className="register-actions">
                            <button type="button" className="btn-modern btn-secondary" onClick={() => navigate(-1)}>
                                Volver
                            </button>
                            <button type="submit" className="btn-modern btn-primary">
                                Registrarse
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default Register;