import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { getClienteById } from '../../services/api/cliente.service';
import { toast } from 'react-toastify';
import type { InscripcionDTO } from '../../services/types/types';

export const MisInscripciones = () => {
    const { user } = useAuth();
    const [inscripciones, setInscripciones] = useState<InscripcionDTO[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchInscripciones = async () => {
            try {
                const idParaBuscar = user?.id; 
                if (idParaBuscar) {
                    const data = await getClienteById(idParaBuscar);
                    setInscripciones(data.misInscripciones || []);
                }
            } catch (error) {
                toast.error("Error al cargar las inscripciones");
            } finally {
                setLoading(false);
            }
        };
        fetchInscripciones();
    }, [user]);

    if (loading) return <div className="text-white p-4">Cargando...</div>;

    if (inscripciones.length === 0) {
        return (
            <div className="text-white p-4 bg-dark rounded">
                No estás inscripto a ninguna clase todavía.
            </div>
        );
    }

    return (
        <div className="row g-3">
            {inscripciones.map((inscripcion) => (
                <div className="col-12 col-md-6" key={inscripcion.id}>
                    <div className="card bg-dark text-white border-secondary p-3">
                        <h5 className="mb-2">Inscripción #{inscripcion.id}</h5>
                        <p className="mb-3 text-muted small">
                            Fecha: {new Date(inscripcion.fechaDeInscripcion).toLocaleDateString('es-AR')}
                        </p>
                        <Link to={`/clases/${inscripcion.idClaseProgramada}`} className="btn btn-outline-light mt-auto">
                            Ver detalle de la clase
                        </Link>
                    </div>
                </div>
            ))}
        </div>
    );
};