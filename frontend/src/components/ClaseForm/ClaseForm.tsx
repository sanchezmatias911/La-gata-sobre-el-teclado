import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { getAllActivities } from '../../services/api/actividad.service';
import { getAllProfesores } from '../../services/api/profesor.service';
import { createClass } from '../../services/api/clase.services';
import './ClaseForm.css';


export const ClaseForm = () => {
  const navigate = useNavigate();
  const [actividades, setActividades] = useState<any[]>([]);
  const [profesores, setProfesores] = useState<any[]>([]);

  // El estado espera los objetos JSON completos para profesor y actividad
  const [formData, setFormData] = useState({
    id: '',
    dia: 'LUNES',
    horaInicio: '',
    horaFin: '',
    estado: 'BORRADOR',
    profesor: null as any,
    actividad: null as any
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const dataActividades = await getAllActivities();
        const dataProfesores = await getAllProfesores();
        
        const actividadesArray = Array.isArray(dataActividades) ? dataActividades : (dataActividades?.content || []);
        const profesoresArray = Array.isArray(dataProfesores) ? dataProfesores : (dataProfesores?.content || []);

        setActividades(actividadesArray);
        setProfesores(profesoresArray);
      } catch (error) {
        toast.error("Error al cargar actividades y profesores.");
      }
    };
    fetchData();
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;

    // Si es el select de profesor o actividad, parseamos el JSON que guardamos en el value
    if (name === 'profesor' || name === 'actividad') {
      if (value) {
        setFormData(prev => ({ ...prev, [name]: JSON.parse(value) }));
      } else {
        setFormData(prev => ({ ...prev, [name]: null }));
      }
    } else {
      setFormData(prev => ({ ...prev, [name]: value }));
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!formData.id || !formData.actividad || !formData.profesor || !formData.horaInicio || !formData.horaFin) {
      toast.warning("Faltan completar campos obligatorios.");
      return;
    }

    // Agregamos los segundos (":00") para asegurarnos de que cumpla el formato de hora estándar de Java (HH:mm:ss)
    const horaInicioFormateada = `${formData.horaInicio}:00`;
    const horaFinFormateada = `${formData.horaFin}:00`;

    // Como formData.profesor y formData.actividad ya son los JSON completos
    // los mandamos directo al body, junto con las horas formateadas como string.
    const nuevaClase = {
      id: Number(formData.id),
      dia: formData.dia,
      horaInicio: horaInicioFormateada,
      horaFin: horaFinFormateada,
      estado: formData.estado,
      profesor: formData.profesor,
      actividad: formData.actividad
    };

    try {
      // Usamos "as any" para saltarnos temporalmente la validación de tu interfaz LocalTime en TypeScript
      // ya que el backend te exige un string.
      await createClass(nuevaClase as any);
      toast.success("¡Clase registrada exitosamente!");
      setTimeout(() => navigate('/clases'), 1500);
    } catch (error) {
      console.error("Error al crear la clase:", error);
      toast.error("Hubo un error al registrar la clase.");
    }
  };

  return (
    <div className="form-page-container">
      <div className="form-content">
        <h2 className="form-title">Registrar Nueva Clase</h2>
        
        <form onSubmit={handleSubmit} className="custom-form">
          <div className="form-group">
            <label>ID de la Clase *</label>
            <input 
              type="number" 
              name="id" 
              value={formData.id} 
              onChange={handleChange} 
              placeholder="Ej: 101"
            />
          </div>

          <div className="form-group">
            <label>Actividad *</label>
            <select 
              name="actividad" 
              value={formData.actividad ? JSON.stringify(formData.actividad) : ''} 
              onChange={handleChange}
            >
              <option value="">Seleccione una actividad...</option>
              {actividades.map(act => (
                <option key={act.id} value={JSON.stringify(act)}>
                  {act.nombre || `Actividad ${act.id}`}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Profesor *</label>
            <select 
              name="profesor" 
              value={formData.profesor ? JSON.stringify(formData.profesor) : ''} 
              onChange={handleChange}
            >
              <option value="">Seleccione un profesor...</option>
              {profesores.map(prof => (
                <option key={prof.dni} value={JSON.stringify(prof)}>
                  {prof.nombre} (DNI: {prof.dni})
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Día de la semana *</label>
            <select name="dia" value={formData.dia} onChange={handleChange}>
              <option value="LUNES">Lunes</option>
              <option value="MARTES">Martes</option>
              <option value="MIERCOLES">Miércoles</option>
              <option value="JUEVES">Jueves</option>
              <option value="VIERNES">Viernes</option>
              <option value="SABADO">Sábado</option>
              <option value="DOMINGO">Domingo</option>
            </select>
          </div>

          <div style={{ display: 'flex', gap: '1rem' }}>
            <div className="form-group" style={{ flex: 1 }}>
              <label>Hora Inicio (ej: 08:00) *</label>
              <input 
                type="time" 
                name="horaInicio" 
                value={formData.horaInicio} 
                onChange={handleChange} 
              />
            </div>
            <div className="form-group" style={{ flex: 1 }}>
              <label>Hora Fin (ej: 10:00) *</label>
              <input 
                type="time" 
                name="horaFin" 
                value={formData.horaFin} 
                onChange={handleChange} 
              />
            </div>
          </div>

          <div className="form-group">
            <label>Estado</label>
            <select name="estado" value={formData.estado} onChange={handleChange}>
              <option value="ACTIVA">ACTIVA</option>
              <option value="BORRADOR">BORRADOR</option>
            </select>
          </div>

          <div className="form-actions">
            <button type="button" className="btn-cancelar" onClick={() => navigate('/clases')}>
              Cancelar
            </button>
            <button type="submit" className="btn-guardar">
              Guardar Clase
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};