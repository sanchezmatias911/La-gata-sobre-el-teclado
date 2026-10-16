import { useNavigate } from 'react-router-dom';
import { SCHEDULE_DATA, DAYS, type ScheduleCell } from '../../data/scheduleData.ts';
import './Schedule.css';

const getActivityBadgeClass = (status: ScheduleCell['status']): string => {
    if (!status) return 'class-pill';

    switch (status) {
        case 'Activa': return 'class-pill pill-active';
        case 'Cancelada': return 'class-pill pill-cancelled';
        default: return 'class-pill';
    }
};

export const Schedule = () => {
    const navigate = useNavigate();

    const handleClassClick = () => {
        navigate('/calendario/id_clase');
    };

    const legendItems: { status: ScheduleCell['status']; label: string }[] = [
        { status: 'Activa', label: 'Activa' },
        { status: 'Cancelada', label: 'Cancelada' },
    ];

    return (
        <div className="schedule-container">
            <div className="schedule-content">

                <div className="schedule-legend">
                    {legendItems.map((item) => (
                        <div key={item.status} className="legend-item">
                <span className={getActivityBadgeClass(item.status)}>
                  {item.label}
                </span>
                        </div>
                    ))}
                </div>

                <div className="table-responsive">
                    <table className="custom-table">
                        <thead>
                        <tr>
                            <th>Hora</th>
                            {DAYS.map((day) => (
                                <th key={day}>{day}</th>
                            ))}
                        </tr>
                        </thead>
                        <tbody>
                        {SCHEDULE_DATA.map((row, rowIndex) => (
                            <tr key={rowIndex}>
                                <td className="time-col">{row[0].time}</td>

                                {row.map((cell, colIndex) => (
                                    <td key={`${rowIndex}-${colIndex}`}>
                                        {cell.activity ? (
                                            <span
                                                className={getActivityBadgeClass(cell.status)}
                                                onClick={handleClassClick}
                                            >
                                {cell.activity}
                              </span>
                                        ) : (
                                            <span className="empty-slot">–</span>
                                        )}
                                    </td>
                                ))}
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default Schedule;