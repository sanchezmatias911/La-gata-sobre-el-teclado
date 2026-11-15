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
         <div>
            <h3 style={{ color: '#fff', marginBottom: '1.5rem', fontSize: '1.2rem' }}>
                Información Personal
            </h3>
            
            <div className="data-row">
                <span className="data-label">Nombre Completo</span>
                <span className="data-value">{cliente.nombre}</span>
            </div>
            
            <div className="data-row">
                <span className="data-label">Email</span>
                <span className="data-value">{cliente.email}</span>
            </div>
            
            <div className="data-row">
                <span className="data-label">DNI</span>
                <span className="data-value">{cliente.dni}</span>
            </div>
            
            <div className="data-row">
                <span className="data-label">Teléfono</span>
                <span className="data-value">{cliente.telefono || 'No registrado'}</span>
            </div>
        </div>
    );
};