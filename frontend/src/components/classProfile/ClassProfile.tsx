import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import type { ClaseProgramadaBody } from "../../services/types/types";
import { getClassById } from '../../services/api/clase.services';
import { subscribeClass, desuscribeClass } from '../../services/api/clase.services';
import { imageUrl } from '../../data/classByIdMockData'
import { toast } from 'react-toastify';
import './ClassProfile.css';
import { getActivityById } from '../../services/api/actividad.service';
import type { ActividadResponseBody} from '../../services/types/types';
import { useAuth } from '../../context/AuthContext'; 

export const ClassProfile = () => {
    const { id_clase } = useParams();
    const { user } = useAuth(); 
    
    const [claseData, setClaseData] = useState<ClaseProgramadaBody | null>(null);
    const [actividad, setActividad] = useState<ActividadResponseBody | null>(null);
  /*  const [profesor, setProfesor] = useState<ProfesorResponseBody | null>(null);*/
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false); 
    const [cancelling, setCancelling] = useState(false);

    const fetchData = async () => {
        if (!id_clase) return;
        try {
            setLoading(true);
            
            const clase = await getClassById(Number(id_clase));
            setClaseData(clase);

            const [actividadData] = await Promise.all([
                getActivityById(clase.idActividad),
              /*  getProfesorById(clase.idProfesor)*/
            ]);

            setActividad(actividadData);
            // setProfesor(profesorData);
        } catch (error) {
            toast.error("Error al cargar los datos de la clase");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData();
    }, [id_clase]);

    const handleSubscribe = async () => {
        if (!id_clase) return;

        // 3. Validamos que exista el ID del usuario en el contexto
        if (!user?.id) {
            toast.warn("Debes iniciar sesión para inscribirte.");
            return;
        }

        try {
            setSubmitting(true);
            // Mandamos el ID del contexto
            await subscribeClass(Number(id_clase), { idCliente: user.id }); 
            toast.success("¡Te has inscripto correctamente a la clase!");
            await fetchData(); 
        } catch (error) {
            toast.error("Error al inscribirse a la clase.");
        } finally {
            setSubmitting(false);
        }
    };

    const handleCancelSubscription = async () => {
        if (!id_clase) return;
        
        if (!user?.id) {
            toast.error("No se pudo identificar tu usuario.");
            return;
        }

        try {
            setCancelling(true);
            // Mandamos el ID del contexto
            await desuscribeClass(Number(id_clase), user.id); 
            toast.success("Inscripción cancelada correctamente.");
            await fetchData(); 
        } catch (error) {
            toast.error("Error al cancelar la inscripción.");
        } finally {
            setCancelling(false);
        }
    };

    if (loading) {
        return <div style={{ color: 'white', textAlign: 'center', marginTop: '3rem' }}>Cargando información de la clase...</div>;
    }

    if (!claseData || !actividad || !claseData.profesorResponse) {
        return <div style={{ color: 'white', textAlign: 'center', marginTop: '3rem' }}>No se encontró la clase solicitada.</div>;
    }

    const { dia, horaInicio, horaFin } = claseData;
    const enrolled = claseData.cantidadInscriptos || 0;
    const capacity = actividad.cupo || 50;
    const occupancyPercentage = Math.min((enrolled / capacity) * 100, 100);

    const formatTime = (time: any) => {
        if (!time) return '';
        if (typeof time === 'string') {
            const [h, m] = time.split(':');
            return `${h.padStart(2, '0')}:${m.padStart(2, '0')}`;
        }
        if (typeof time === 'object' && 'hour' in time) {
            const h = String(time.hour).padStart(2, '0');
            const m = String(time.minute).padStart(2, '0');
            return `${h}:${m}`;
        }
        return String(time);
    };

    return (
        <div className="class-container">
            <div className="class-card">
                <div className="class-hero">
                    <img src={imageUrl} alt={actividad.nombre} className="class-hero-img" />
                    <div className="class-hero-overlay">
                        <h1 className="class-name">{actividad.nombre}</h1>
                        <p className="class-professor">Profesor: {claseData.profesorResponse.nombre}</p>
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

                    {/* 4. Borramos el input manual que estaba acá */}

                    <div className="class-actions">
                        <button 
                            className="btn btn-enroll"
                            onClick={handleSubscribe}
                            disabled={submitting || enrolled >= capacity}
                        >
                            {submitting ? 'Inscribiendo...' : 'Inscribirse a la clase'}
                        </button>
                        <button 
                            className="btn btn-cancel" 
                            onClick={handleCancelSubscription}
                            disabled={cancelling}
                        >
                            {cancelling ? 'Cancelando...' : 'Cancelar inscripción'}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ClassProfile;