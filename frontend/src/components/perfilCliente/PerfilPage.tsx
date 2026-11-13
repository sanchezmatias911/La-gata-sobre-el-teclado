import { NavLink, Outlet, Navigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext.tsx';
import './PerfilPage.css';

export const PerfilPage = () => {
    const { user } = useAuth();

    
    if (!user || user.rol === 'ADMIN') {
        return <Navigate to="/" replace />;
    }

    return (
        <div className="perfil-page-wrapper">
            <div className="perfil-content-wrapper">
                
                <div className="perfil-welcome-panel">
                    <h1 className="perfil-welcome-title">
                        Mi <br /> Perfil
                    </h1>
                    <p className="perfil-welcome-text">
                        Administrá tus datos personales y revisá las clases a las que estás inscripto.
                    </p>
                </div>

                <div className="perfil-main-panel">
                    <div className="perfil-tabs">
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

                    <div className="perfil-outlet-container">
                        <Outlet />
                    </div>
                </div>

            </div>
        </div>
    );
};