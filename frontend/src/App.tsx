import { Schedule } from "./components/Schedule";
import { Routes, Route } from 'react-router-dom';

const App = () => {
    return (
        <>
            <Routes>
                <Route path='/clases' element={<Schedule />} />
            </Routes>
        </>
    );
}

export default App;