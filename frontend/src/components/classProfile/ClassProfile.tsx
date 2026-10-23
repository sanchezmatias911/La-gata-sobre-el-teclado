import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import type { ClaseProgramadaBody } from "../../services/types/types";
import { getClassById } from '../../services/api/clase.services';
import { subscribeClass } from '../../services/api/clase.services';
import { imageUrl } from '../../data/classByIdMockData'
import { toast } from 'react-toastify';
import './ClassProfile.css';


export const ClassProfile = () => {
    const { id_clase } = useParams();
    const [claseData, setClaseData] = useState<ClaseProgramadaBody | null>(null);
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false); 
    const [userIdInput, setUserIdInput] = useState<number | string>(1);

    const fetchClase = async () => {
        if (!id_clase) return;
        try {
            const data = await getClassById(Number(id_clase));
            setClaseData(data);
        } catch (error) {
            toast.error("Error al cargar la clase:");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchClase();
    }, [id_clase]);


    const handleSubscribe = async () => {
        if (!id_clase) return;

        if (!userIdInput) {
            toast.warn("Por favor, ingresa un ID de usuario válido.");
            return;
        }

        try {
            setSubmitting(true);

            
          

            await subscribeClass(Number(id_clase), { id: Number(userIdInput) });

            toast.success("¡Te has inscripto correctamente a la clase!");
            
    
            await fetchClase(); 
        } catch (error) {
            toast.error("Error al inscribirse a la clase:");
          
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) {
        return <div style={{ color: 'white', textAlign: 'center', marginTop: '3rem' }}>Cargando información de la clase...</div>;
    }

    if (!claseData) {
        return <div style={{ color: 'white', textAlign: 'center', marginTop: '3rem' }}>No se encontró la clase solicitada.</div>;
    }

    const { actividad, profesor, dia, horaInicio, horaFin, inscripciones } = claseData;

    const enrolled = inscripciones ? inscripciones.length : 0;
    const capacity = actividad.cupo || 50;
    const occupancyPercentage = Math.min((enrolled / capacity) * 100, 100);

    const formatTime = (time: { hour: number; minute: number }) => {
        const h = time.hour < 10 ? `0${time.hour}` : time.hour;
        const m = time.minute < 10 ? `0${time.minute}` : time.minute;
        return `${h}:${m}`;
    };

   return (
        <div className="class-container">
            <div className="class-card">
                <div className="class-hero">
                    <img src={imageUrl} alt={actividad.nombre} className="class-hero-img" />
                    <div className="class-hero-overlay">
                        <h1 className="class-name">{actividad.nombre}</h1>
                        <p className="class-professor">Profesor: {profesor.nombre}</p>
                    </div>
                </div>

                <div className="class-body">
                    <div className="class-section">
                        <h3 className="section-title">Horario disponible</h3>
                        <div className="schedule-tag">
                            <span className="schedule-text">Desde</span>
                            <span className="tag-pill">{dia} {formatTime(horaInicio)}</span>
                            <span className="schedule-text">Hasta</span>
                            <span className="tag-pill">{dia} {formatTime(horaFin)}</span>
                        </div>
                    </div>

                    <div className="class-section">
                        <div className="capacity-header">
                            <h3 className="section-title">Estado del cupo</h3>
                            <span className="capacity-text">{enrolled} / {capacity} inscriptos</span>
                        </div>
                        <div className="progress-track">
                            <div className="progress-fill" style={{ width: `${occupancyPercentage}%` }}></div>
                        </div>
                    </div>

                    
                    <div className="class-section" style={{ marginTop: '1rem' }}>
                        <label style={{ color: 'white', display: 'block', marginBottom: '0.5rem' }}>
                            ID del usuario a inscribir (Modo Prueba):
                        </label>
                        <input
                            type="number"
                            value={userIdInput}
                            onChange={(e) => setUserIdInput(e.target.value)}
                            placeholder="Ej: 1"
                            style={{
                                padding: '0.5rem',
                                borderRadius: '6px',
                                border: '1px solid #ccc',
                                width: '100%',
                                maxWidth: '200px'
                            }}
                        />
                    </div>

                    <div className="class-actions">
                        <button 
                            className="btn btn-enroll"
                            onClick={handleSubscribe}
                            disabled={submitting}
                        >
                            {submitting ? 'Inscribiendo...' : 'Inscribirse a la clase'}
                        </button>
                        <button className="btn btn-cancel">
                            Cancelar inscripción
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ClassProfile;