
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import type { RegistroBody } from '../../services/types/types';
import { registroSchema } from '../../services/validations/registro.schema';
import '../claseForm/ClaseForm.css';
import { register } from '../../services/api/auth.service';


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
      // NUEVO: Usamos el servicio register
      await register(formData);
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
            <div className="form-group flex-2">
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
            
            <div className="form-group flex-1">
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