import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getAllActivities } from "../../services/api/actividad.service.ts";
import { ActividadCard } from "../";
import { useDocumentTitle } from "../../hooks/useDocumentTitle.ts";
import './Home.css';

export interface Actividad {
    id: number;
    nombre: string;
    descripcion: string;
}

export const Home = () => {
    useDocumentTitle('Inicio');
    const [actividades, setActividades] = useState<Actividad[]>([]);

    useEffect(() => {
        const fetchActividades = async () => {
            const activities = await getAllActivities();
            setActividades(activities);
        };

        fetchActividades();
    }, []);

    return (
        <div className="home-layout-home">
            <header className="hero-home">
                <div className="hero-background-home"></div>
                <div className="hero-content-home">
                    <span className="hero-badge-home">Tu entrenamiento, tus reglas</span>
                    <h1 className="hero-title-home">La gata sobre el teclado</h1>
                    <p className="hero-subtitle-home">
                        Gestioná tus clases y horarios de forma ágil, intuitiva y sin complicaciones. <br/>
                        Dejá atrás el papel y tomá el control total de tu rutina.
                    </p>
                    <Link to="/clases" className="btn-primary-home">
                        Ver Calendario Semanal
                    </Link>
                </div>
            </header>

            <section className="features-section-home">
                <div className="container-home">
                    <h2 className="section-heading-home">¿Por qué elegirnos?</h2>
                    <div className="features-grid-home">
                        <div className="feature-card-home">
                            <div className="feature-icon-home">⚡</div>
                            <h3>Gestión Rápida</h3>
                            <p>Inscribite y organizá tus clases en segundos. Una interfaz pensada para optimizar tu tiempo.</p>
                        </div>
                        <div className="feature-card-home">
                            <div className="feature-icon-home">🎯</div>
                            <h3>Control Total</h3>
                            <p>Elegí tus horarios y profesores favoritos. Tenés el dominio absoluto de tu plan de entrenamiento.</p>
                        </div>
                        <div className="feature-card-home">
                            <div className="feature-icon-home">💪</div>
                            <h3>Para Deportistas</h3>
                            <p>Diseñado para quienes buscan resultados sin fricciones tecnológicas. Entrá, reservá y entrená.</p>
                        </div>
                    </div>
                </div>
            </section>

            <section className="activities-section-home">
                <div className="container-home">
                    <h2 className="section-heading-home">Nuestras Actividades</h2>
                    <div className="activities-grid-home">
                        {actividades.slice(0, 6).map(act => (
                            <ActividadCard key={act.id} act={act} />
                        ))}
                    </div>
                </div>
            </section>

            <footer className="footer-home">
                <div className="container-home footer-grid-home">
                    <div className="footer-brand-home">
                        <h4>La gata sobre el teclado</h4>
                        <p>El gimnasio que se adapta a tu ritmo.</p>
                    </div>
                    <div className="footer-info-home">
                        <h4>Sede Central</h4>
                        <p>Roque Sáenz Peña 352, Bernal</p>
                        <p>Buenos Aires, Argentina</p>
                    </div>
                    <div className="footer-info-home">
                        <h4>Horarios</h4>
                        <p>Lunes a Sábados: 08:00 - 22:00</p>
                    </div>
                </div>
                <div className="footer-bottom-home">
                    <p>&copy; {new Date().getFullYear()} La gata sobre el teclado. Todos los derechos reservados.</p>
                </div>
            </footer>
        </div>
    );
};

export default Home;