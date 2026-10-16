import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getAllClases } from '../../services/api/clase.services';
import type { ClaseProgramadaBody, LocalTime } from "../../services/types/types";
import { DAYS, HOURS, START_HOUR } from '../../data/scheduleMockData'
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

    const handleClassClick = (id: number) => {
        navigate(`/clases/${id}`);
    };

    const getColumnForDay = (dia: string) => {
        const index = DAYS.findIndex(d => d.toLowerCase() === dia.toLowerCase());
        return index >= 0 ? index + 2 : -1;
    };

    const getRowForTime = (hour: number, minute: number) => {
        const baseRow = (hour - START_HOUR) * 2 + 2;
        const extraRow = minute >= 30 ? 1 : 0;
        return baseRow + extraRow;
    };

    const getDurationInRows = (horaInicio: LocalTime, horaFin: LocalTime) => {
        const inicioEnMinutos = horaInicio.hour * 60 + horaInicio.minute;
        const finEnMinutos = horaFin.hour * 60 + horaFin.minute;
        const duracionEnMinutos = finEnMinutos - inicioEnMinutos;
        return Math.ceil(duracionEnMinutos / 30);
    };

    const getActivityBadgeClass = (estado: string) => {
        if (estado === 'Activa') return 'class-pill-grid pill-active';
        if (estado === 'Cancelada') return 'class-pill-grid pill-cancelled';
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
                </div>

                <div className="table-responsive">
                    <div className="schedule-grid">

                        <div className="grid-header time-col">Hora</div>
                        {DAYS.map((day) => (
                            <div key={day} className="grid-header">{day}</div>
                        ))}

                        {HOURS.map((hour) => {
                            const row = getRowForTime(hour, 0);
                            return (
                                <div key={`bg-${hour}`} className="grid-row-group" style={{ gridRow: `${row} / span 2`, gridColumn: '1 / -1' }}></div>
                            );
                        })}

                        {HOURS.map((hour) => {
                            const row = getRowForTime(hour, 0);
                            return (
                                <div key={`time-${hour}`} className="grid-time-label" style={{ gridRow: `${row} / span 2`, gridColumn: 1 }}>
                                    {hour < 10 ? `0${hour}:00` : `${hour}:00`}
                                </div>
                            );
                        })}

                        {clases.map((clase) => {
                            const col = getColumnForDay(clase.dia);
                            const rowStart = getRowForTime(clase.horaInicio.hour, clase.horaInicio.minute);
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
                                    {clase.actividad.nombre}
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