import { Routes, Route } from 'react-router-dom';
import { ToastContainer } from "react-toastify";
import Navbar from './components/navbar/Navbar';
import "react-toastify/dist/ReactToastify.css";
import { 
  Schedule,
  ClassProfile,
  ActividadForm,
  ProfesorForm,
  ClaseForm,
  Login
} from "./components/"; 
import { Register } from './components/Register/Register';

import { AuthProvider } from './context/AuthContext';
import { ProtectedRoute } from './components/ProtectedRoute/ProtectedRoute';

const App = () => {
    return (
        <AuthProvider>
            <Navbar />
            <Routes>
                <Route path='/clases' element={<Schedule />} />
                <Route path='/login' element={<Login />} />
                <Route path='/clases/:id_clase' element={<ClassProfile />} />

                <Route path='/registrar/clase' element={<ClaseForm />} />
                <Route path='/registrar/profesor' element={<ProfesorForm />} />
                <Route path='/registrar/actividad' element={<ActividadForm />} />
                <Route path='/registrar/cliente' element={<Register />} />
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