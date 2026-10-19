import { Routes, Route } from 'react-router-dom';
import { Schedule, ClassProfile } from "./components/";

const App = () => {
    return (
        <>
            <Routes>
                <Route path='/clases' element={<Schedule />} />
                <Route path='/clases/:id_clase' element={<ClassProfile />} />
            </Routes>
        </>
    );
}

export default App;