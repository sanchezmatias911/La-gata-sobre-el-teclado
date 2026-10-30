
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import type { RegistroBody } from '../../services/types/types';
import { registroSchema } from '../../services/validations/registro.schema';
import '../ClaseForm/ClaseForm.css'; // Reutilizamos los estilos generales del proyecto
import { createCliente } from '../../services/api/cliente.service';

type FormErrors = Partial<Record<keyof RegistroBody, string>>;

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

  const [errors, setErrors] = useState<FormErrors>({});

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type } = e.target;
    
    setFormData((prev) => ({
      ...prev,
      [name]: type === 'number' ? (value === '' ? 0 : Number(value)) : value
    }));

    // Limpia el mensaje de error individual al escribir
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
      await createCliente(formData);
      toast.success("¡Bienvenido al Gimnasio la gata sobre el teclado!");
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
              style={errors.nombre ? { borderColor: '#ef4444' } : undefined}
            />
            {errors.nombre && <span style={{ color: '#ef4444', fontSize: '0.8rem', marginTop: '0.2rem' }}>{errors.nombre}</span>}
          </div>

          <div className="form-group">
            <label>Apellido</label>
            <input 
              type="text" 
              name="apellido" 
              value={formData.apellido} 
              onChange={handleChange} 
              placeholder="Ej: Pérez"
              style={errors.apellido ? { borderColor: '#ef4444' } : undefined}
            />
            {errors.apellido && <span style={{ color: '#ef4444', fontSize: '0.8rem', marginTop: '0.2rem' }}>{errors.apellido}</span>}
          </div>

          <div className="form-group">
            <label>Email</label>
            <input 
              type="email" 
              name="mail" 
              value={formData.mail} 
              onChange={handleChange} 
              placeholder="correo@ejemplo.com"
              style={errors.mail ? { borderColor: '#ef4444' } : undefined}
            />
            {errors.mail && <span style={{ color: '#ef4444', fontSize: '0.8rem', marginTop: '0.2rem' }}>{errors.mail}</span>}
          </div>

          <div className="form-group">
            <label>Contraseña</label>
            <input 
              type="password" 
              name="contraseña" 
              value={formData.contraseña} 
              onChange={handleChange} 
              placeholder="********"
              style={errors.contraseña ? { borderColor: '#ef4444' } : undefined}
            />
            {errors.contraseña && <span style={{ color: '#ef4444', fontSize: '0.8rem', marginTop: '0.2rem' }}>{errors.contraseña}</span>}
          </div>
          
          <div style={{ display: 'flex', gap: '1rem' }}>
            <div className="form-group" style={{ flex: 2 }}>
              <label>Teléfono</label>
              <input 
                type="text" 
                name="teléfono" 
                value={formData.teléfono} 
                onChange={handleChange} 
                placeholder="+54 9 11 1234-5678"
                style={errors.teléfono ? { borderColor: '#ef4444' } : undefined}
              />
              {errors.teléfono && <span style={{ color: '#ef4444', fontSize: '0.8rem', marginTop: '0.2rem' }}>{errors.teléfono}</span>}
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
                style={errors.dni ? { borderColor: '#ef4444' } : undefined}
              />
              {errors.dni && <span style={{ color: '#ef4444', fontSize: '0.8rem', marginTop: '0.2rem' }}>{errors.dni}</span>}
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