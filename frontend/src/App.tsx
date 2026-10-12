import { Schedule } from "./components/Schedule";
import { Routes, Route } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import { ClaseForm } from "./components/ClaseForm/ClaseForm";

const App = () => {
    return (
        <>
            <ToastContainer theme="dark" position="bottom-right" aria-label="Notificaciones" />
            <Routes>
                <Route path='/clases' element={<Schedule />} />
                <Route path='/clases/nueva' element={<ClaseForm />} />
            </Routes>
        </>
    );
}

export default App;