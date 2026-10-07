import React from 'react';
import { SCHEDULE_DATA, DAYS, type ClassType } from '../data/scheduleData.ts'; 

const getActivityBadgeClass = (activity: ClassType | null): string => {
  if (!activity) return '';
  
  switch (activity) {
    case 'Funcional': return 'badge bg-primary'; // Azul
    case 'Fitcombat': return 'badge bg-warning text-dark'; // Naranja
    case 'Zumba': return 'badge bg-danger'; // Rojo
    case 'Yoga': return 'badge bg-success'; // Verde
    case 'Stretching': return 'badge bg-success'; // Verde
    case 'Full local': return 'badge bg-warning text-dark'; // Naranja
    case 'Gap': return 'badge bg-success'; // Verde
    default: return 'badge bg-secondary';
  }
};

export const Schedule: React.FC = () => {
  return (
    <div className="container mt-4">
      <div className="table-responsive">
        <table className="table table-dark table-bordered text-center align-middle">
          <thead>
            <tr>
              {/* Hora */}
              <th scope="col">Hora</th>
              {/*  Días */}
              {DAYS.map((day) => (
                <th key={day} scope="col">{day}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {SCHEDULE_DATA.map((row, rowIndex) => (
              <tr key={rowIndex}>
                <td className="fw-bold">{row[0].time}</td>
                
                {row.map((cell, colIndex) => (
                  <td key={`${rowIndex}-${colIndex}`}>
                    {cell.activity ? (
                      <span className={getActivityBadgeClass(cell.activity)}>
                        {cell.activity}
                      </span>
                    ) : (
                      <span className="text-muted">-</span>
                    )}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};