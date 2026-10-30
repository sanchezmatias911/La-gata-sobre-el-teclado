import { Link } from 'react-router-dom';
import './Navbar.css';

function Navbar() {
    return (
        <nav className="custom-navbar">
            <div className="navbar-container">
                {/* Logo a la izquierda */}
                <Link to="/" className="navbar-logo">
                    <img src="/favicon.png" alt="Logo Gimnasio" />
                </Link>

                {/* Links a la derecha */}
                <div className="navbar-links">
                    <Link to="/">Inicio</Link>
                    <Link to="/clases">Clases</Link>
                    <Link to="/registrar">Registrarse</Link>
                </div>
            </div>
        </nav>
    );
}

export default Navbar;