import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAllClases } from '../../services/api/clase.services';
import type { ClaseProgramadaBody, LocalTime } from "../../services/types/types";
import { DAYS, START_HOUR, HOURS } from "../../constants/data.ts";
import './Schedule.css';

export const Schedule = () => {
    const navigate = useNavigate();
    const [clases, setClases] = useState<ClaseProgramadaBody[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchClases = async () => {
            try {
                const data = await getAllClases();
                setClases(data);
            } catch (error) {
                console.error(error);
            } finally {
                setLoading(false);
            }
        };

        fetchClases();
    }, []);

    const handleClassClick = (id_clase: number) => {
        navigate(`/clases/${id_clase}`);
    };

    const normalizeString = (str: string) => {
        if (!str) return '';
        return str
            .toLowerCase()
            .normalize("NFD")
            .replace(/[\u0300-\u036f]/g, "");
    };

    const parseTime = (time: LocalTime | string | any) => {
        if (typeof time === 'string') {
            const [h, m] = time.split(':').map(Number);
            return { hour: h || 0, minute: m || 0 };
        }
        if (time && typeof time === 'object' && 'hour' in time) {
            return time;
        }
        return { hour: 0, minute: 0 };
    };

    const getColumnForDay = (dia: string) => {
        if (!dia) return -1;
        const normalizedDia = normalizeString(dia);
        const index = DAYS.findIndex(d => normalizeString(d) === normalizedDia);
        return index >= 0 ? index + 2 : -1;
    };

    const getRowForTime = (time: LocalTime | string) => {
        const { hour, minute } = parseTime(time);
        const baseRow = (hour - START_HOUR) * 2 + 2;
        const extraRow = minute >= 30 ? 1 : 0;
        return baseRow + extraRow;
    };

    const getDurationInRows = (horaInicio: LocalTime | string, horaFin: LocalTime | string) => {
        const inicio = parseTime(horaInicio);
        const fin = parseTime(horaFin);

        const inicioEnMinutos = inicio.hour * 60 + inicio.minute;
        const finEnMinutos = fin.hour * 60 + fin.minute;
        const duracionEnMinutos = finEnMinutos - inicioEnMinutos;

        return Math.max(1, Math.ceil(duracionEnMinutos / 30));
    };

    const getActivityBadgeClass = (estado: string) => {
        const estadoUpper = estado?.toUpperCase();
        if (estadoUpper === 'ACTIVA') return 'class-pill-grid pill-active';
        if (estadoUpper === 'CANCELADA') return 'class-pill-grid pill-cancelled';
        if (estadoUpper === 'LLENA') return 'class-pill-grid pill-completa'
        return 'class-pill-grid';
    };

    if (loading) return <div>Cargando calendario...</div>;

    return (
        <div className="schedule-container">
            <div className="schedule-content">

                <div className="schedule-legend">
                    <div className="legend-item">
                        <span className="class-pill-grid pill-active legend-pill">Activa</span>
                    </div>
                    <div className="legend-item">
                        <span className="class-pill-grid pill-cancelled legend-pill">Cancelada</span>
                    </div>
                    <div className="legend-item">
                        <span className="class-pill-grid pill-completa legend-pill">Completa</span>
                    </div>
                </div>

                <div className="table-responsive">
                    <div className="schedule-grid">

                        <div className="grid-header time-col">Hora</div>
                        {DAYS.map((day) => (
                            <div key={day} className="grid-header">{day}</div>
                        ))}

                        {HOURS.map((hour) => {
                            const row = getRowForTime({ hour, minute: 0, second: 0, nano: 0 });
                            return (
                                <div key={`bg-${hour}`} className="grid-row-group" style={{ gridRow: `${row} / span 2`, gridColumn: '1 / -1' }}></div>
                            );
                        })}

                        {HOURS.map((hour) => {
                            const row = getRowForTime({ hour, minute: 0, second: 0, nano: 0 });
                            return (
                                <div key={`time-${hour}`} className="grid-time-label" style={{ gridRow: `${row} / span 2`, gridColumn: 1 }}>
                                    {hour < 10 ? `0${hour}:00` : `${hour}:00`}
                                </div>
                            );
                        })}

                        {clases.map((clase) => {
                            const col = getColumnForDay(clase.dia);
                            const rowStart = getRowForTime(clase.horaInicio);
                            const durationInRows = getDurationInRows(clase.horaInicio, clase.horaFin);

                            if (col === -1 || durationInRows <= 0) return null;

                            return (
                                <div
                                    key={clase.id}
                                    className={getActivityBadgeClass(clase.estado)}
                                    style={{
                                        gridColumn: col,
                                        gridRow: `${rowStart} / span ${durationInRows}`
                                    }}
                                    onClick={() => handleClassClick(clase.id)}
                                >
                                    {clase.nombreActividad || 'Clase'}
                                </div>
                            );
                        })}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Schedule;