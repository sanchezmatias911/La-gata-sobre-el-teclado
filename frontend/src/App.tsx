import { Routes, Route } from 'react-router-dom';
import { ToastContainer } from "react-toastify";
import Navbar from './components/navbar/Navbar';
import "react-toastify/dist/ReactToastify.css";
import {
    Home,
    Schedule,
    ClassProfile,
    ActividadForm,
    ProfesorForm,
    ClaseForm
} from "./components/"; 
import { Register } from './components/register/Register';

const App = () => {
    return (
        <>
            <Navbar />
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path='/clases' element={<Schedule />} />
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
        </>
    );
}

export default App;