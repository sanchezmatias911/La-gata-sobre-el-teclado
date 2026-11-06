import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { createActivity } from '../../services/api/actividad.service';
import type { ActividadBody } from '../../services/types/types';
import '../claseForm/ClaseForm.css';


export const ActividadForm = () => {
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

    //VALIDACIONES DE CUPO:

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
    <div className="form-page-container">
      <div className="form-content">
        <h2 className="form-title">Registrar Nueva Actividad</h2>
        
        <form onSubmit={handleSubmit} className="custom-form">
          <div className="form-group">
            <label>Nombre de la Actividad *</label>
            <input 
              type="text" 
              name="nombre" 
              value={formData.nombre} 
              onChange={handleChange} 
              placeholder="Ej: Musculación, Funcional..."
            />
          </div>

          <div className="form-group">
            <label>Descripción</label>
            <input 
              type="text" 
              name="descripcion" 
              value={formData.descripcion} 
              onChange={handleChange} 
              placeholder="Breve descripción de la actividad"
            />
          </div>

          <div className="form-group">
            <label>Cupo</label>
            <input 
              type="number" 
              name="cupo" 
              value={formData.cupo} 
              onChange={handleChange} 
              placeholder="Capacidad máxima"
              min="1"
            />
          </div>

          <div className="form-actions">
            <button type="button" className="btn-cancelar" onClick={() => navigate('/clases')}>
              Cancelar
            </button>
            <button type="submit" className="btn-guardar">
              Guardar Actividad
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ActividadForm