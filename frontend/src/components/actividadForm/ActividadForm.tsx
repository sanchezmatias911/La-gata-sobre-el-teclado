import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import type { ActividadBody } from '../../services/types/types';
import { createActivity } from '../../services/api/actividad.service';
import { useDocumentTitle } from '../../hooks/useDocumentTitle.ts';
import './ActividadForm.css';

export const ActividadForm = () => {
    useDocumentTitle('Registrar Actividad');
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        nombre: '',
        descripcion: '',
        cupo: 1
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.type === 'number' ? Number(e.target.value) : e.target.value
        });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        if (!formData.nombre) {
            toast.warning("El nombre de la actividad es obligatorio.");
            return;
        }

        if (formData.cupo === undefined || formData.cupo === null || isNaN(formData.cupo)) {
            toast.warning("El cupo máximo es obligatorio.");
            return;
        }

        if (formData.cupo <= 0) {
            toast.error("El cupo máximo debe ser mayor que cero.");
            return;
        }

        try {
            console.log(formData)
            await createActivity(formData as ActividadBody);
            toast.success("¡Actividad registrada exitosamente!");
            setTimeout(() => navigate('/clases'), 1500);
        } catch (error) {
            toast.error("Hubo un error al registrar la actividad.");
        }
    };

    return (
        <div className="actividad-form-page-wrapper-actividad-form">
            <div className="actividad-form-content-wrapper-actividad-form">

                <div className="actividad-form-welcome-panel-actividad-form">
                    <h1 className="welcome-title-actividad-form">Ampliá la<br/>oferta de<br/>entrenamiento.</h1>
                    <p className="welcome-text-actividad-form">Renová las opciones de tu gimnasio. <br/> Creá nuevas actividades, definí sus cupos y ofreceles a tus clientes más alternativas para superarse.</p>
                </div>

                <div className="actividad-form-panel-actividad-form">
                    <div className="actividad-form-container-actividad-form">
                        <h2 className="actividad-form-title-actividad-form">Nueva Actividad</h2>
                        <p className="actividad-form-subtitle-actividad-form">Definí los detalles de la nueva disciplina.</p>

                        <form onSubmit={handleSubmit} className="actividad-form-actividad-form">

                            <div className="form-group-actividad-form">
                                <label>Nombre de la Actividad *</label>
                                <input
                                    type="text"
                                    name="nombre"
                                    value={formData.nombre}
                                    onChange={handleChange}
                                    placeholder="Ej: Musculación, Funcional..."
                                />
                            </div>

                            <div className="form-group-actividad-form">
                                <label>Descripción</label>
                                <textarea
                                    name="descripcion"
                                    value={formData.descripcion}
                                    onChange={handleChange}
                                    placeholder="Breve descripción de la actividad..."
                                    rows={4}
                                />
                            </div>

                            <div className="form-group-actividad-form">
                                <label>Cupo *</label>
                                <input
                                    type="number"
                                    name="cupo"
                                    value={formData.cupo}
                                    onChange={handleChange}
                                    placeholder="Capacidad máxima"
                                    min="1"
                                />
                            </div>

                            <div className="form-actions-actividad-form">
                                <button
                                    type="button"
                                    className="btn-modern-actividad-form btn-secondary-actividad-form"
                                    onClick={() => navigate('/clases')}
                                >
                                    Cancelar
                                </button>
                                <button
                                    type="submit"
                                    className="btn-modern-actividad-form btn-primary-actividad-form"
                                >
                                    Guardar Actividad
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ActividadForm;