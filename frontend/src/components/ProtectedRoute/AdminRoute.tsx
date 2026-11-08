import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { toast } from 'react-toastify';

export const AdminRoute = () => {
    const { user, isAuthenticated } = useAuth();

    if (!isAuthenticated) {
        toast.error("Debes iniciar sesión para acceder a esta sección.");
        return <Navigate to="/login" replace />;
    }

    if (user?.rol !== 'ADMIN') {
        toast.error("No tenés permisos para acceder a esta sección.");
        return <Navigate to="/clases" replace />; 
    }

    return <Outlet />; 
};

export default AdminRoute;