import { Schedule } from "./components/Schedule";
import {BrowserRouter, Routes, Route} from 'react-router-dom';



function App() {
    return (
        <BrowserRouter>
            <Routes>
                    <Route path="/" element={<h2>Home</h2>} />
                    <Route path='/clases' element={<Schedule />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;