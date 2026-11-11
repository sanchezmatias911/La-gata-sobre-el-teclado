import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import type { ClaseProgramadaBody, ActividadResponseBody } from "../../services/types/types";
import { getClassById, subscribeClass, desuscribeClass } from '../../services/api/clase.services';
import { getActivityById } from '../../services/api/actividad.service';
import { useAuth } from '../../context/AuthContext';
import { imageUrl_profile } from "../../constants/data.ts"
import './ClassProfile.css';

export const ClassProfile = () => {
    const { id_clase } = useParams();
    const { user } = useAuth();

    const [claseData, setClaseData] = useState<ClaseProgramadaBody | null>(null);
    const [actividad, setActividad] = useState<ActividadResponseBody | null>(null);
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
            ]);
            setActividad(actividadData);
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
        if (!user?.id) {
            toast.warn("Debes iniciar sesión para inscribirte.");
            return;
        }
        try {
            setSubmitting(true);
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
        return <div className="class-container-loading"><div className="loader"></div>Cargando clase...</div>;
    }

    if (!claseData || !actividad || !claseData.profesorResponse) {
        return <div className="class-container-loading">No se encontró la clase solicitada.</div>;
    }

    const { dia, horaInicio, horaFin } = claseData;
    const enrolled = claseData.cantidadInscriptos || 0;
    const capacity = actividad.cupo || 50;
    const occupancyPercentage = Math.min((enrolled / capacity) * 100, 100);
    const isFull = enrolled >= capacity;

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
        <div className="class-page-wrapper">
            <div className="class-card-modern">
                <div className="class-image-column">
                    <img src={imageUrl_profile} alt={actividad.nombre} className="class-img" />
                    <div className="img-gradient"></div>
                </div>

                <div className="class-info-column">
                    <div className="class-header">
                        <span className="class-badge">Clase Presencial</span>
                        <h1 className="class-title">{actividad.nombre}</h1>
                        <p className="class-instructor">
                            <svg className="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" /></svg>
                            {claseData.profesorResponse.nombre}
                        </p>
                    </div>

                    <div className="class-details">
                        <div className="detail-item">
                            <div className="detail-icon-box">
                                <svg className="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                            </div>
                            <div className="detail-content">
                                <h4>Horario</h4>
                                <p>{dia} • {formatTime(horaInicio)} - {formatTime(horaFin)}</p>
                            </div>
                        </div>

                        <div className="detail-item">
                            <div className="detail-icon-box">
                                <svg className="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" /></svg>
                            </div>
                            <div className="detail-content w-full">
                                <div className="capacity-flex">
                                    <h4>Disponibilidad</h4>
                                    <span className={isFull ? 'text-danger' : 'text-success'}>
                                        {enrolled} / {capacity}
                                    </span>
                                </div>
                                <div className="progress-bar-bg">
                                    <div
                                        className={`progress-bar-fill ${isFull ? 'bg-danger' : ''}`}
                                        style={{ width: `${occupancyPercentage}%` }}
                                    ></div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="class-actions-modern">
                        <button
                            className="btn-modern btn-enroll"
                            onClick={handleSubscribe}
                            disabled={submitting || isFull}
                        >
                            {submitting ? 'Inscribiendo...' : isFull ? 'Clase Llena' : 'Confirmar Inscripción'}
                        </button>

                        {/* Asumimos que podes cancelar si estás inscripto (esto depende de tu lógica de negocio) */}
                        <button
                            className="btn-modern btn-cancel"
                            onClick={handleCancelSubscription}
                            disabled={cancelling}
                        >
                            {cancelling ? 'Cancelando...' : 'Darme de baja'}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ClassProfile;