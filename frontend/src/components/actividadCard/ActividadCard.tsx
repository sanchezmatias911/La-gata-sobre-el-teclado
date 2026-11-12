import { useState } from 'react';
import type { Actividad } from './../home/Home.tsx';
import './ActividadCard.css';

export const ActivityCard = ( { act }: { act: Actividad }) => {
    const [isExpanded, setIsExpanded] = useState(false);

    return (
        <article className="activity-card-actividad-card">
            <h3>{act.nombre}</h3>

            <p className={`activity-text-actividad-card ${isExpanded ? "text-expanded-actividad-card" : "text-clamped-actividad-card"}`}>
                {act.descripcion}
            </p>

            <button
                className="btn-mostrar-mas-actividad-card"
                onClick={() => setIsExpanded(!isExpanded)}
            >
                {isExpanded ? "Mostrar menos" : "Mostrar más"}
            </button>

            <div className="card-accent-actividad-card"></div>
        </article>
    );
};

export default ActivityCard;