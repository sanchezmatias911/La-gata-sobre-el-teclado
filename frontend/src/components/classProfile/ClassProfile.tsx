import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import type { ClaseProgramadaBody } from "../../services/types/types";
import { getClassById } from '../../services/api/clase.services';
import { imageUrl } from '../../data/classByIdMockData'
import './ClassProfile.css';

export const ClassProfile = () => {
    const { id_clase } = useParams();
    const [claseData, setClaseData] = useState<ClaseProgramadaBody | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
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

        fetchClase();
    }, [id_clase]);

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