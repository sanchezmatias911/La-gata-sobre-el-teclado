import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import './Home.css';
import { ActividadCard } from "../";
import { getAllActivities } from "../../services/api/actividad.service.ts";

export interface Actividad {
    id: number;
    nombre: string;
    descripcion: string;
}

export const Home = () => {
    const [actividades, setActividades] = useState<Actividad[]>([]);

    useEffect(() => {
        const fetchActividades = async () => {
            const activities = await getAllActivities();
            setActividades(activities);
        };

        fetchActividades();
    }, []);

    return (
        <div className="home-layout">
            <header className="hero">
                <div className="hero-background"></div>
                <div className="hero-content">
                    <span className="hero-badge">Tu entrenamiento, tus reglas</span>
                    <h1 className="hero-title">La gata sobre el teclado</h1>
                    <p className="hero-subtitle">
                        Gestioná tus clases y horarios de forma ágil, intuitiva y sin complicaciones. <br/>
                        Dejá atrás el papel y tomá el control total de tu rutina.
                    </p>
                    <Link to="/clases" className="btn-primary">
                        Ver Calendario Semanal
                    </Link>
                </div>
            </header>

            <section className="features-section">
                <div className="container">
                    <h2 className="section-heading">¿Por qué elegirnos?</h2>
                    <div className="features-grid">
                        <div className="feature-card">
                            <div className="feature-icon">⚡</div>
                            <h3>Gestión Rápida</h3>
                            <p>Inscribite y organizá tus clases en segundos. Una interfaz pensada para optimizar tu tiempo.</p>
                        </div>
                        <div className="feature-card">
                            <div className="feature-icon">🎯</div>
                            <h3>Control Total</h3>
                            <p>Elegí tus horarios y profesores favoritos. Tenés el dominio absoluto de tu plan de entrenamiento.</p>
                        </div>
                        <div className="feature-card">
                            <div className="feature-icon">💪</div>
                            <h3>Para Deportistas</h3>
                            <p>Diseñado para quienes buscan resultados sin fricciones tecnológicas. Entrá, reservá y entrená.</p>
                        </div>
                    </div>
                </div>
            </section>

            <section className="activities-section">
                <div className="container">
                    <h2 className="section-heading">Nuestras Actividades</h2>
                    <div className="activities-grid">
                        {actividades.map(act => (
                            <ActividadCard key={act.id} act={act} />
                        ))}
                    </div>
                </div>
            </section>

            <footer className="footer">
                <div className="container footer-grid">
                    <div className="footer-brand">
                        <h4>La gata sobre el teclado</h4>
                        <p>El gimnasio que se adapta a tu ritmo.</p>
                    </div>
                    <div className="footer-info">
                        <h4>Sede Central</h4>
                        <p>Roque Sáenz Peña 352, Bernal</p>
                        <p>Buenos Aires, Argentina</p>
                    </div>
                    <div className="footer-info">
                        <h4>Horarios</h4>
                        <p>Lunes a Sábados: 08:00 - 22:00</p>
                    </div>
                </div>
                <div className="footer-bottom">
                    <p>&copy; {new Date().getFullYear()} La gata sobre el teclado. Todos los derechos reservados.</p>
                </div>
            </footer>
        </div>
    );
};

export default Home;