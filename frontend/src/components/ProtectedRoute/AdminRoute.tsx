import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

export const AdminRoute = () => {
    const { user, isAuthenticated } = useAuth();

    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }

    if (user?.rol !== 'ADMIN') {
        return <Navigate to="/clases" replace />; 
    }

    return <Outlet />; 
};

export default AdminRoute;