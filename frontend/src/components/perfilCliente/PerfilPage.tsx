import { NavLink, Outlet, Navigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext.tsx';
import './PerfilPage.css';

export const PerfilPage = () => {
    const { user } = useAuth();

    
    if (!user || user.rol === 'ADMIN') {
        return <Navigate to="/" replace />;
    }

    return (
        <div className="container py-5">
            <h2 className="text-white mb-4">Mi Perfil</h2>
            
            {/* Botones de navegación */}
            <div className="perfil-tabs mb-4">
                <NavLink 
                    to="/perfil/inscripciones" 
                    className={({ isActive }) => isActive ? "perfil-tab active" : "perfil-tab"}
                >
                    Mis Inscripciones
                </NavLink>
                <NavLink 
                    to="/perfil/datos" 
                    className={({ isActive }) => isActive ? "perfil-tab active" : "perfil-tab"}
                >
                    Datos Personales
                </NavLink>
            </div>

            {/* Acá se va a renderizar el componente hijo seleccionado */}
            <div className="perfil-content">
                <Outlet />
            </div>
        </div>
    );
};