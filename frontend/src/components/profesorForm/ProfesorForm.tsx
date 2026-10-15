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
    nombre: '',
    apellido: ''
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!formData.dni || !formData.nombre || !formData.apellido) {
      toast.warning("Todos los campos son obligatorios.");
      return;
    }

    try {
      // Convertimos el DNI a número según el tipo esperado en el servicio
      const nuevoProfesor = {
        ...formData,
        dni: Number(formData.dni)
      };
      
      await createProfesor(nuevoProfesor as ProfesorBody);
      toast.success("¡Profesor registrado exitosamente!");
      setTimeout(() => navigate('/profesores'), 1500);
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
            <label>Apellido *</label>
            <input 
              type="text" 
              name="apellido" 
              value={formData.apellido} 
              onChange={handleChange} 
            />
          </div>

          <div className="form-actions">
            <button type="button" className="btn-cancelar" onClick={() => navigate('/profesores')}>
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