
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import type { RegistroBody } from '../../services/types/types';
import { registroSchema } from '../../services/validations/registro.schema';
import '../ClaseForm/ClaseForm.css'; // Reutilizamos los estilos generales del proyecto
import { createCliente } from '../../services/api/cliente.service';


export const Register = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<RegistroBody>({
    nombre: '',
    apellido: '',
    mail: '',
    contraseña: '',
    teléfono: '',
    dni: 0
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type } = e.target;
    setFormData({
      ...formData,
      [name]: type === 'number' ? Number(value) : value
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const validacion = registroSchema.safeParse(formData);
    
    if (!validacion.success) {
      toast.warning("Por favor, revisa los campos ingresados.");
      console.error(validacion.error.format());
      return;
    }

    try {
      await createCliente(formData);
      toast.success("¡Bienvenido al Gimnacio la gata sobre el teclado!");
      {/* Provisorio esa navegacion a clases */}
      setTimeout(() => navigate('/clases/'), 1500); 
    } catch (error) {
      toast.error("Hubo un error al procesar el registro.");
    }
  };

  return (
    <div className="form-page-container">
      <div className="form-content">
        <h2 className="form-title">Crea tu cuenta y empezá a entrenar</h2>
        
        <form onSubmit={handleSubmit} className="custom-form">
          <div className="form-group">
            <label>Tu nombre guerrero!</label>
            <input 
              type="text" 
              name="nombre" 
              value={formData.nombre} 
              onChange={handleChange} 
              placeholder="Ej: Juan"
            />
          </div>

          <div className="form-group">
            <label>Apellido</label>
            <input 
              type="text" 
              name="apellido" 
              value={formData.apellido} 
              onChange={handleChange} 
              placeholder="Ej: Pérez"
            />
          </div>

          <div className="form-group">
            <label>Email</label>
            <input 
              type="email" 
              name="mail" 
              value={formData.mail} 
              onChange={handleChange} 
              placeholder="correo@ejemplo.com"
            />
          </div>

          <div className="form-group">
            <label>Contraseña</label>
            <input 
              type="password" 
              name="contraseña" 
              value={formData.contraseña} 
              onChange={handleChange} 
              placeholder="********"
            />
          </div>
          
          <div style={{ display: 'flex', gap: '1rem' }}>
            <div className="form-group" style={{ flex: 2 }}>
              <label>Teléfono</label>
              <input 
                type="text" 
                name="teléfono" 
                value={formData.teléfono} 
                onChange={handleChange} 
                placeholder="Ej: 1123456789"
              />
            </div>
            
            <div className="form-group" style={{ flex: 1 }}>
              <label>DNI</label>
              <input 
                type="number" 
                name="dni" 
                value={formData.dni || ''} 
                onChange={handleChange} 
                min="0"
                placeholder="Sin puntos"
              />
            </div>
          </div>

          <div className="form-actions">
            <button type="button" className="btn-cancelar" onClick={() => navigate('/clases/')}>
              Volver
            </button>
            <button type="submit" className="btn-guardar">
              Registrarse
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Register;