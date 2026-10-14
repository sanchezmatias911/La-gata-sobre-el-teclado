import { Routes, Route } from 'react-router-dom';
import { Schedule, ClassProfile } from "./components/";

const App = () => {
    return (
        <>
            <Routes>
                <Route path='/calendario' element={<Schedule />} />
                <Route path='/calendario/:id_clase' element={<ClassProfile />} />
            </Routes>
        </>
    );
}

export default App;