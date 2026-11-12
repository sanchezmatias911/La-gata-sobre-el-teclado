import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext'; 
import { getClienteById } from '../../services/api/cliente.service';
import type { ClienteData } from "../../services/types/types";

import { toast } from 'react-toastify';

export const DatosPersonales = () => {
    const { user } = useAuth();
    const [cliente, setCliente] = useState<ClienteData | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchCliente = async () => {
            try {
                const idParaBuscar = user?.id; 
                
                if (idParaBuscar) {
                    const data = await getClienteById(idParaBuscar);
                    setCliente(data);
                }
            } catch (error) {
                console.error("Error al cargar el cliente:", error);
                toast.error("No se pudieron cargar los datos personales.");
            } finally {
                setLoading(false);
            }
        };

        fetchCliente();
    }, [user]);

    if (loading) return <div className="text-white p-4">Cargando datos...</div>;
    if (!cliente) return <div className="text-white p-4">No se encontraron datos.</div>;

    return (
        <div className="card bg-dark text-white border-secondary p-4" style={{ maxWidth: '600px' }}>
            <h4 className="mb-4">Datos Personales</h4>
            
            <div className="mb-3 border-bottom border-secondary pb-2">
                <label className="form-label text-muted mb-0">Nombre Completo</label>
                <p className="fs-5 mb-0">{cliente.nombre}</p>
            </div>

            <div className="mb-3 border-bottom border-secondary pb-2">
                <label className="form-label text-muted mb-0">Email</label>
                <p className="fs-5 mb-0">{cliente.email}</p>
            </div>

            <div className="mb-3 border-bottom border-secondary pb-2">
                <label className="form-label text-muted mb-0">DNI</label>
                <p className="fs-5 mb-0">{cliente.dni}</p>
            </div>

            <div className="mb-3 border-bottom border-secondary pb-2">
                <label className="form-label text-muted mb-0">Teléfono</label>
                <p className="fs-5 mb-0">{cliente.telefono || 'No registrado'}</p>
            </div>
        </div>
    );
};