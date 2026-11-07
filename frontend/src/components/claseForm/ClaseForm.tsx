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

  // Eliminamos el id de la clase del estado inicial
  const [formData, setFormData] = useState({
    dia: 'LUNES',
    horaInicio: '',
    horaFin: '',
    estado: 'BORRADOR',
    idProfesor: '',
    idActividad: ''
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
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!formData.idActividad || !formData.idProfesor || !formData.horaInicio || !formData.horaFin) {
      toast.warning("Faltan completar campos obligatorios.");
      return;
    }

    // Formateamos la hora para que Spring Boot la acepte (HH:mm:ss)
    const horaInicioFormateada = `${formData.horaInicio}:00`;
    const horaFinFormateada = `${formData.horaFin}:00`;

    // Armamos el payload exacto sin enviar ningún ID de clase
    const nuevaClase = {
      dia: formData.dia,
      horaInicio: horaInicioFormateada,
      horaFin: horaFinFormateada,
      estado: formData.estado,
      idProfesor: Number(formData.idProfesor),
      idActividad: Number(formData.idActividad)
    };

    try {
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
          {/* Eliminado el input del ID de la clase */}

          <div className="form-group">
            <label>Actividad *</label>
            <select name="idActividad" value={formData.idActividad} onChange={handleChange}>
              <option value="">Seleccione una actividad...</option>
              {actividades.map(act => (
                <option key={act.id} value={act.id}>
                  {act.nombre || `Actividad ${act.id}`}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label>Profesor *</label>
            <select name="idProfesor" value={formData.idProfesor} onChange={handleChange}>
              <option value="">Seleccione un profesor...</option>
              {profesores.map(prof => (
                <option key={prof.id} value={prof.id}>
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

export default ClaseForm;