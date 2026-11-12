import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { createProfesor } from '../../services/api/profesor.service';
import type { ProfesorBody } from '../../services/types/types';
import './ProfesorForm.css';

export const ProfesorForm = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        dni: '',
        email: '',
        nombre: '',
        telefono: '',
        biografia: '',
        especialidades: ''
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!formData.dni || !formData.nombre || !formData.email ||
            !formData.telefono || !formData.biografia || !formData.especialidades) {
            toast.error("Todos los campos son obligatorios para registrar un profesor.");
            return;
        }

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
            toast.error("Hubo un error al registrar al profesor.");
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

                        <form onSubmit={handleSubmit} className="profesor-form-profesor-form">

                            <div className="form-row-profesor-form">
                                <div className="form-group-profesor-form">
                                    <label>DNI *</label>
                                    <input
                                        type="number"
                                        name="dni"
                                        value={formData.dni}
                                        onChange={handleChange}
                                        placeholder="Sin puntos ni espacios"
                                    />
                                </div>

                                <div className="form-group-profesor-form">
                                    <label>Teléfono</label>
                                    <input
                                        type="text"
                                        name="telefono"
                                        value={formData.telefono}
                                        onChange={handleChange}
                                        placeholder="+54 9 11 1234-5678"
                                    />
                                </div>
                            </div>

                            <div className="form-row-profesor-form">
                                <div className="form-group-profesor-form">
                                    <label>Nombre *</label>
                                    <input
                                        type="text"
                                        name="nombre"
                                        value={formData.nombre}
                                        onChange={handleChange}
                                        placeholder="Ej: Juan Pérez"
                                    />
                                </div>

                                <div className="form-group-profesor-form">
                                    <label>Email *</label>
                                    <input
                                        type="email"
                                        name="email"
                                        value={formData.email}
                                        onChange={handleChange}
                                        placeholder="correo@ejemplo.com"
                                    />
                                </div>
                            </div>

                            <div className="form-group-profesor-form">
                                <label>Especialidades</label>
                                <input
                                    type="text"
                                    name="especialidades"
                                    value={formData.especialidades}
                                    onChange={handleChange}
                                    placeholder="Ej: Yoga, Pilates, Musculación"
                                />
                            </div>

                            <div className="form-group-profesor-form">
                                <label>Biografía</label>
                                <textarea
                                    name="biografia"
                                    value={formData.biografia}
                                    onChange={handleChange}
                                    placeholder="Breve descripción de la trayectoria del profesor..."
                                    rows={4}
                                />
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