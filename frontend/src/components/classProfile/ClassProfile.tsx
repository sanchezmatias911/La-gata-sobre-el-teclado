import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import type { ClaseProgramadaBody } from "../../services/types/types";
import { deleteActivity, getClassById } from '../../services/api/clase.services';
import { imageUrl } from '../../data/classByIdMockData'
import './ClassProfile.css';
import { toast } from 'react-toastify';

export const ClassProfile = () => {
    const { id_clase } = useParams();
    const [claseData, setClaseData] = useState<ClaseProgramadaBody | null>(null);
    const [loading, setLoading] = useState(true);
    const [cancelling, setCancelling] = useState(false);

    const fetchClase = async () => {
            if (!id_clase) return;
            try {
                const data = await getClassById(Number(id_clase));
                setClaseData(data);
            } catch (error) {
                console.error("Error al cargar la clase:", error);
            } finally {
                setLoading(false);
            }
        };

    useEffect(() => {
        fetchClase();
    }, [id_clase]);

    if (loading) {
        return <div style={{ color: 'white', textAlign: 'center', marginTop: '3rem' }}>Cargando información de la clase...</div>;
    }

    if (!claseData) {
        return <div style={{ color: 'white', textAlign: 'center', marginTop: '3rem' }}>No se encontró la clase solicitada.</div>;
    }

    const { dia, horaInicio, horaFin, idActividad, idProfesor } = claseData;

    // El backend manda horas como string "18:00:00"
    const formatTime = (time: string) => {
        if (!time) return "--:--";
        // "18:00:00" -> "18:00"
        return time.slice(0, 5);
    };

    // No hay actividad ni profesor en el DTO, uso placeholders
    const nombreActividad = `Actividad #${idActividad ?? "?"}`;
    const nombreProfesor = `Profesor #${idProfesor ?? "?"}`;
    const enrolled = 0;        // el DTO no trae inscripciones
    const capacity = 50;       // valor por defecto
    const occupancyPercentage = 0;


    const handleCancelSubscription = async () => {
    if (!id_clase) return;
    
    try {
        setCancelling(true);
        await deleteActivity(Number(id_clase));
        toast.success("Inscripción cancelada correctamente.");
        
        // Volvemos a cargar los datos para que se actualice la barra de progreso
        await fetchClase(); 
    } catch (error) {
        toast.error("Error al cancelar la inscripción.");
    } finally {
        setCancelling(false);
    }
    };

    return (
        <div className="class-container">
            <div className="class-card">

                <div className="class-hero">
                    <img src={imageUrl} alt={nombreActividad} className="class-hero-img" />
                    <div className="class-hero-overlay">
                        <h1 className="class-name">{nombreActividad}</h1>
                        <p className="class-professor">Profesor: {nombreProfesor}</p>
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
                            <div
                                className="progress-fill"
                                style={{ width: `${occupancyPercentage}%` }}
                            ></div>
                        </div>
                    </div>

                    <div className="class-actions">
                        <button className="btn btn-enroll">
                            Inscribirse a la clase
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