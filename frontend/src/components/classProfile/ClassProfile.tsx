import './ClassProfile.css';

interface ClassProfileProps {
    name?: string;
    professor?: string;
    imageUrl?: string;
    schedule?: string;
    enrolled?: number;
    capacity?: number;
}

export const ClassProfile = ({
                                 name = "Funcional",
                                 professor = "Martín Palermo",
                                 imageUrl = "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?q=80&w=1470&auto=format&fit=crop",
                                 schedule = "Lunes 18:00",
                                 enrolled = 45,
                                 capacity = 50
                             }: ClassProfileProps) => {

    const occupancyPercentage = Math.min((enrolled / capacity) * 100, 100);

    return (
        <div className="class-container">
            <div className="class-card">

                <div className="class-hero">
                    <img src={imageUrl} alt={name} className="class-hero-img" />
                    <div className="class-hero-overlay">
                        <h1 className="class-name">{name}</h1>
                        <p className="class-professor">Profesor: {professor}</p>
                    </div>
                </div>

                <div className="class-body">

                    <div className="class-section">
                        <h3 className="section-title">Horario disponible</h3>
                        <div className="schedule-tag">
                            <span className="tag-pill">{schedule}</span>
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