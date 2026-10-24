import { Routes, Route } from 'react-router-dom';
import { ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import { 
  Schedule,
  ClassProfile,
  ActividadForm,
  ProfesorForm,
  ClaseForm
} from "./components/"; // Areglar en index.ts para agregar ActividadForm, ProfesorForm, ClaseForm.

const App = () => {
    return (
        <>
            <Routes>
                <Route path='/clases' element={<Schedule />} />
                <Route path='/clases/nuevaClase' element={<ClaseForm />} />
                <Route path='/clases/nuevoProfesor' element={<ProfesorForm />} />
                <Route path='/clases/nuevaActividad' element={<ActividadForm />} />
                <Route path='/clases/:id_clase' element={<ClassProfile />} />
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