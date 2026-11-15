import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { createProfesor } from '../../services/api/profesor.service';
import type { ProfesorBody } from '../../services/types/types';
import { useDocumentTitle } from "../../hooks/useDocumentTitle.ts";
import './ProfesorForm.css';

type FormErrors = Partial<Record<keyof ProfesorBody, string>>;

export const ProfesorForm = () => {
    useDocumentTitle('Registrar Profesor');
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        dni: '',
        email: '',
        nombre: '',
        telefono: '',
        biografia: '',
        especialidades: ''
    });

    const [errors, setErrors] = useState<FormErrors>({});

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });

        if (errors[name as keyof ProfesorBody]) {
            setErrors((prev) => ({ ...prev, [name]: undefined }));
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const newErrors: FormErrors = {};

        if (!formData.dni) {
            newErrors.dni = "El DNI es obligatorio.";
        }

        if (!formData.telefono.trim()) {
            newErrors.telefono = "El teléfono es obligatorio.";
        }

        if (!formData.nombre.trim()) {
            newErrors.nombre = "El nombre es obligatorio.";
        }

        if (!formData.email.trim()) {
            newErrors.email = "El email es obligatorio.";
        }

        if (!formData.especialidades.trim()) {
            newErrors.especialidades = "Las especialidades son obligatorias.";
        }

        if (!formData.biografia.trim()) {
            newErrors.biografia = "La biografía es obligatoria.";
        }

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            toast.error("Por favor, revisa los campos ingresados.");
            return;
        }

        setErrors({});

        try {
            const nuevoProfesor: ProfesorBody = {
                dni: Number(formData.dni),
                email: formData.email,
                nombre: formData.nombre,
                telefono: formData.telefono,
                biografia: formData.biografia,
                especialidades: formData.especialidades
            };

            await createProfesor(nuevoProfesor);

            toast.success("¡Profesor registrado exitosamente!");
            setTimeout(() => navigate('/clases'), 1500);
        } catch (error) {
            toast.error("Hubo un error al registrar al profesor. Revisá el formato de los datos ingresados.");
        }
    };

    return (
        <div className="profesor-form-page-wrapper-profesor-form">
            <div className="profesor-form-content-wrapper-profesor-form">

                <div className="profesor-form-welcome-panel-profesor-form">
                    <h1 className="welcome-title-profesor-form">Sumá talento<br/>a tu equipo.</h1>
                    <p className="welcome-text-profesor-form">Hacé crecer tu staff. <br/> Registrá nuevos profesores, definí sus especialidades y sumalos a las actividades del gimnasio.</p>
                </div>

                <div className="profesor-form-panel-profesor-form">
                    <div className="profesor-form-container-profesor-form">
                        <h2 className="profesor-form-title-profesor-form">Nuevo Profesor</h2>
                        <p className="profesor-form-subtitle-profesor-form">Completá los datos del nuevo integrante.</p>

                        <form onSubmit={handleSubmit} className="profesor-form-profesor-form" noValidate>

                            <div className="form-row-profesor-form">
                                <div className="form-group-profesor-form">
                                    <label>Nombre *</label>
                                    <input
                                        type="text"
                                        name="nombre"
                                        value={formData.nombre}
                                        onChange={handleChange}
                                        placeholder="Ej: Juan Pérez"
                                        className={errors.nombre ? 'input-error-profesor-form' : ''}
                                    />
                                    {errors.nombre && <span className="error-message-profesor-form">{errors.nombre}</span>}
                                </div>

                                <div className="form-group-profesor-form">
                                    <label>Email *</label>
                                    <input
                                        type="email"
                                        name="email"
                                        value={formData.email}
                                        onChange={handleChange}
                                        placeholder="correo@ejemplo.com"
                                        className={errors.email ? 'input-error-profesor-form' : ''}
                                    />
                                    {errors.email && <span className="error-message-profesor-form">{errors.email}</span>}
                                </div>
                            </div>

                            <div className="form-row-profesor-form">
                                <div className="form-group-profesor-form">
                                    <label>DNI *</label>
                                    <input
                                        type="number"
                                        name="dni"
                                        value={formData.dni}
                                        onChange={handleChange}
                                        placeholder="Sin puntos ni espacios"
                                        className={errors.dni ? 'input-error-profesor-form' : ''}
                                    />
                                    {errors.dni && <span className="error-message-profesor-form">{errors.dni}</span>}
                                </div>

                                <div className="form-group-profesor-form">
                                    <label>Teléfono *</label>
                                    <input
                                        type="text"
                                        name="telefono"
                                        value={formData.telefono}
                                        onChange={handleChange}
                                        placeholder="+54 9 11 1234-5678"
                                        className={errors.telefono ? 'input-error-profesor-form' : ''}
                                    />
                                    {errors.telefono && <span className="error-message-profesor-form">{errors.telefono}</span>}
                                </div>
                            </div>

                            <div className="form-group-profesor-form">
                                <label>Especialidades *</label>
                                <input
                                    type="text"
                                    name="especialidades"
                                    value={formData.especialidades}
                                    onChange={handleChange}
                                    placeholder="Ej: Yoga, Pilates, Musculación"
                                    className={errors.especialidades ? 'input-error-profesor-form' : ''}
                                />
                                {errors.especialidades && <span className="error-message-profesor-form">{errors.especialidades}</span>}
                            </div>

                            <div className="form-group-profesor-form">
                                <label>Biografía *</label>
                                <textarea
                                    name="biografia"
                                    value={formData.biografia}
                                    onChange={handleChange}
                                    placeholder="Breve descripción de la trayectoria del profesor..."
                                    rows={4}
                                    className={errors.biografia ? 'input-error-profesor-form' : ''}
                                />
                                {errors.biografia && <span className="error-message-profesor-form">{errors.biografia}</span>}
                            </div>

                            <div className="form-actions-profesor-form">
                                <button
                                    type="button"
                                    className="btn-modern-profesor-form btn-secondary-profesor-form"
                                    onClick={() => navigate('/clases')}
                                >
                                    Cancelar
                                </button>
                                <button
                                    type="submit"
                                    className="btn-modern-profesor-form btn-primary-profesor-form"
                                >
                                    Guardar Profesor
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ProfesorForm;