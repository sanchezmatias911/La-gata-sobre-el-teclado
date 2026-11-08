import { Routes, Route } from 'react-router-dom';
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {
    Home,
    Navbar,
    Register,
    Schedule,
    ClassProfile,
    ActividadForm,
    ProfesorForm,
    ClaseForm,
    Login
} from "./components/"; 

import { AuthProvider } from './context/AuthContext';
import { ProtectedRoute } from './components/ProtectedRoute/ProtectedRoute';
import SwaggerRedirect from './services/api/SwaggerRedirect';
import { AdminRoute } from './components/ProtectedRoute/AdminRoute';

const App = () => {
    return (
        <AuthProvider>
            <Navbar />
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path='/clases' element={<Schedule />} />
                <Route path='/login' element={<Login />} />
                <Route path='/registrar/cliente' element={<Register />} />
                <Route path="/docs" element={<SwaggerRedirect />} />
                
                <Route element={<ProtectedRoute />}>
                    <Route path='/clases/:id_clase' element={<ClassProfile />} />
                </Route>

                <Route element={<AdminRoute />}>
                    <Route path='/registrar/clase' element={<ClaseForm />} />
                    <Route path='/registrar/profesor' element={<ProfesorForm />} />
                     <Route path='/registrar/actividad' element={<ActividadForm />} />
                </Route>
            </Routes>
            <ToastContainer
                position="bottom-right"
                autoClose={3000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                pauseOnHover
                theme="light" aria-label={undefined}            
            />
        </AuthProvider>
    );
}

export default App;