import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { getClienteById } from '../../services/api/cliente.service';
import { getClassById } from '../../services/api/clase.services';
import { toast } from 'react-toastify';
import type { ClaseDetalle } from '../../services/types/types';

export const MisInscripciones = () => {
    const { user } = useAuth();
    const [clases, setClases] = useState<ClaseDetalle[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchInscripcionesYClases = async () => {
            try {
                const idParaBuscar = user?.id;
                if (!idParaBuscar) return;

                const clienteData = await getClienteById(idParaBuscar);
                const inscripciones = clienteData.misInscripciones || [];

                if (inscripciones.length === 0) {
                    setClases([]);
                    return;
                }

                const promesasClases = inscripciones.map((insc: any) => 
                    getClassById(insc.idClaseProgramada)
                );

                const clasesData = await Promise.all(promesasClases);
                setClases(clasesData);

            } catch (error) {
                console.error(error);
                toast.error("Error al cargar las clases");
            } finally {
                setLoading(false);
            }
        };
        fetchInscripcionesYClases();
    }, [user]);

    const formatHora = (time: any) => {
        if (!time) return '--:--';
        
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


    if (loading) return <div className="text-white p-4">Cargando...</div>;

        if (clases.length === 0) {
        return (
            <div className="perfil-card" style={{ textAlign: 'center', padding: '3rem 1rem' }}>
                <p style={{ margin: 0 }}>No estás inscripto a ninguna clase todavía.</p>
            </div>
        );
    }

    return (
                <div>
            <h3 style={{ color: '#fff', marginBottom: '1.5rem', fontSize: '1.2rem' }}>
                Tus Clases
            </h3>
            
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                {clases.map((clase) => (
                    <div className="perfil-card" key={clase.id}>
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: '1rem' }}>
                            
                            <div>
                                <h5 style={{ margin: '0 0 0.5rem 0', color: '#fff', fontSize: '1.2rem', fontWeight: 'bold' }}>
                                    {clase.nombreActividad}
                                </h5>
                                
                                <div style={{ display: 'flex', gap: '1.5rem', color: '#a0a0a0', fontSize: '0.9rem', flexWrap: 'wrap' }}>
                                    <span style={{ display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
                                        📅 {clase.dia}
                                    </span>
                                    <span style={{ display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
                                        ⏰ {formatHora(clase.horaInicio)} - {formatHora(clase.horaFin)}
                                    </span>
                                    <span style={{ display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
                                        👨‍🏫 Prof. {clase.profesorResponse?.nombre || 'Sin asignar'}
                                    </span>
                                </div>
                            </div>

                            <Link to={`/clases/${clase.id}`} className="btn-perfil-action">
                                Ver clase
                            </Link>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );

};