import { useState } from 'react';
import type { Actividad } from './../home/Home.tsx';
import './ActividadCard.css';

export const ActivityCard = ( { act }: { act: Actividad }) => {
    const [isExpanded, setIsExpanded] = useState(false);

    return (
        <article className="activity-card">
            <h3>{act.nombre}</h3>

            <p className={`activity-text ${isExpanded ? "text-expanded" : "text-clamped"}`}>
                {act.descripcion}
            </p>

            <button
                className="btn-mostrar-mas"
                onClick={() => setIsExpanded(!isExpanded)}
            >
                {isExpanded ? "Mostrar menos" : "Mostrar más"}
            </button>

            <div className="card-accent"></div>
        </article>
    );
};

export default ActivityCard;