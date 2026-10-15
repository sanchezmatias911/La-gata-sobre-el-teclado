import { Schedule } from "./components/Schedule";
import { Routes, Route } from 'react-router-dom';
import 'react-toastify/dist/ReactToastify.css';
import { ClaseForm } from "./components/ClaseForm/ClaseForm";
import { ProfesorForm } from "./components/profesorForm/ProfesorForm";
import { ActividadForm } from "./components/ActividadForm/ActividadForm";

const App = () => {
    return (
        <>
           
            <Routes>
                <Route path='/clases' element={<Schedule />} />
                <Route path='/clases/nuevaClase' element={<ClaseForm />} />
                <Route path='/clases/nuevoProfesor' element={<ProfesorForm />} />
                <Route path='/clases/nuevaActividad' element={<ActividadForm />} />
            </Routes>
        </>
    );
}

export default App;

/*
 <ToastContainer theme="dark" position="bottom-right" aria-label="Notificaciones" />
*/