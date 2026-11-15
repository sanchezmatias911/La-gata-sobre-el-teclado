import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import type { ActividadBody } from '../../services/types/types';
import { createActivity } from '../../services/api/actividad.service';
import { useDocumentTitle } from '../../hooks/useDocumentTitle.ts';
import './ActividadForm.css';

type FormErrors = Partial<Record<keyof ActividadBody, string>>;

export const ActividadForm = () => {
    useDocumentTitle('Registrar Actividad');
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        nombre: '',
        descripcion: '',
        cupo: 1
    });

    const [errors, setErrors] = useState<FormErrors>({});

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value, type } = e.target;

        setFormData((prev) => ({
            ...prev,
            [name]: type === 'number' ? (value === '' ? 0 : Number(value)) : value
        }));

        if (errors[name as keyof ActividadBody]) {
            setErrors((prev) => ({ ...prev, [name]: undefined }));
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const newErrors: FormErrors = {};

        if (!formData.nombre.trim()) {
            newErrors.nombre = "El nombre de la actividad es obligatorio.";
        }

        if (!formData.descripcion.trim()) {
            newErrors.descripcion = "La descripción de la actividad es obligatoria.";
        }

        if (formData.cupo === undefined || formData.cupo === null || isNaN(formData.cupo)) {
            newErrors.cupo = "El cupo máximo es obligatorio.";
        } else if (formData.cupo <= 0) {
            newErrors.cupo = "El cupo máximo debe ser mayor que cero.";
        }

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            toast.error("Por favor, revisa los campos ingresados.");
            return;
        }

        setErrors({});

        try {
            console.log(formData);
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

                        <form onSubmit={handleSubmit} className="actividad-form-actividad-form" noValidate>

                            <div className="form-group-actividad-form">
                                <label>Nombre de la Actividad *</label>
                                <input
                                    type="text"
                                    name="nombre"
                                    value={formData.nombre}
                                    onChange={handleChange}
                                    placeholder="Ej: Musculación, Funcional..."
                                    className={errors.nombre ? 'input-error-actividad-form' : ''}
                                />
                                {errors.nombre && <span className="error-message-actividad-form">{errors.nombre}</span>}
                            </div>

                            <div className="form-group-actividad-form">
                                <label>Descripción *</label>
                                <textarea
                                    name="descripcion"
                                    value={formData.descripcion}
                                    onChange={handleChange}
                                    placeholder="Breve descripción de la actividad..."
                                    rows={4}
                                    className={errors.descripcion ? 'input-error-actividad-form' : ''}
                                />
                                {errors.descripcion && <span className="error-message-actividad-form">{errors.descripcion}</span>}
                            </div>

                            <div className="form-group-actividad-form">
                                <label>Cupo *</label>
                                <input
                                    type="number"
                                    name="cupo"
                                    value={formData.cupo === 0 ? '' : formData.cupo}
                                    onChange={handleChange}
                                    placeholder="Capacidad máxima"
                                    min="1"
                                    className={errors.cupo ? 'input-error-actividad-form' : ''}
                                />
                                {errors.cupo && <span className="error-message-actividad-form">{errors.cupo}</span>}
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