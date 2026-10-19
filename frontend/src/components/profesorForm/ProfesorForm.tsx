import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { createProfesor } from '../../services/api/profesor.service';
import type { ProfesorBody } from '../../services/types/types';
import '../ClaseForm/ClaseForm.css';



export const ProfesorForm = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    dni: '',
    email: '',
    password: '',
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

    if (!formData.dni || !formData.nombre || !formData.email || !formData.password) {
      toast.warning("DNI, Nombre, Email y Contraseña son obligatorios.");
      return;
    }

    try {
    
      const nuevoProfesor: ProfesorBody = {

        dni: Number(formData.dni),
        email: formData.email,
        password: formData.password,
        nombre: formData.nombre,
        telefono: formData.telefono,
        biografia: formData.biografia,
        especialidades: formData.especialidades
      };
      
      // Ahora lo pasamos directamente al servicio
      await createProfesor(nuevoProfesor);
      
      toast.success("¡Profesor registrado exitosamente!");
      setTimeout(() => navigate('/clases'), 1500);
    } catch (error) {
      toast.error("Hubo un error al registrar al profesor.");
    }
  };

  return (
    <div className="form-page-container">
      <div className="form-content">
        <h2 className="form-title">Registrar Nuevo Profesor</h2>
        
        <form onSubmit={handleSubmit} className="custom-form">
          <div className="form-group">
            <label>DNI *</label>
            <input 
              type="number" 
              name="dni" 
              value={formData.dni} 
              onChange={handleChange} 
              placeholder="Sin puntos ni espacios"
            />
          </div>

          <div className="form-group">
            <label>Nombre *</label>
            <input 
              type="text" 
              name="nombre" 
              value={formData.nombre} 
              onChange={handleChange} 
            />
          </div>

          <div className="form-group">
            <label>Email *</label>
            <input 
              type="email" 
              name="email" 
              value={formData.email} 
              onChange={handleChange} 
              placeholder="correo@ejemplo.com"
            />
          </div>

          <div className="form-group">
            <label>Contraseña *</label>
            <input 
              type="password" 
              name="password" 
              value={formData.password} 
              onChange={handleChange} 
            />
          </div>

          <div className="form-group">
            <label>Teléfono</label>
            <input 
              type="text" 
              name="telefono" 
              value={formData.telefono} 
              onChange={handleChange} 
            />
          </div>

          <div className="form-group">
            <label>Especialidades</label>
            <input 
              type="text" 
              name="especialidades" 
              value={formData.especialidades} 
              onChange={handleChange} 
              placeholder="Ej: Yoga, Pilates"
            />
          </div>

          <div className="form-group">
            <label>Biografía</label>
            <textarea 
              name="biografia" 
              value={formData.biografia} 
              onChange={handleChange} 
              placeholder="Breve descripción del profesor..."
              rows={4}
              style={{ width: '100%', padding: '0.5rem', borderRadius: '4px', border: '1px solid #ccc' }}
            />
          </div>

          <div className="form-actions">
            <button type="button" className="btn-cancelar" onClick={() => navigate('/clases')}>
              Cancelar
            </button>
            <button type="submit" className="btn-guardar">
              Guardar Profesor
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};