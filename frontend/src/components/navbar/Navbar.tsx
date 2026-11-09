import { useState, useRef, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext.tsx';
import './Navbar.css';

function Navbar() {
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const dropdownRef = useRef<HTMLDivElement>(null);
    const { isAuthenticated, user, logoutContext } = useAuth();

    const navigate = useNavigate();

    const toggleDropdown = () => {
        setIsDropdownOpen(!isDropdownOpen);
    };

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
                setIsDropdownOpen(false);
            }
        };
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, []);

    const handleLogout = () => {
        logoutContext();
        setIsDropdownOpen(false);
        navigate('/');
    };

    const isAdmin = user?.rol === 'ADMIN';

    return (
        <nav className="custom-navbar">
            <div className="navbar-container">
                <Link to="/" className="navbar-logo">
                    <img src="/favicon.png" alt="Logo Gimnasio" />
                </Link>

                <div className="navbar-links">
                    <Link to="/">Inicio</Link>
                    <Link to="/clases">Clases</Link>

                    {isAuthenticated ? (
                        <div className="user-menu" ref={dropdownRef}>
                            <button
                                className={`user-name-btn ${isDropdownOpen ? 'active' : ''}`}
                                onClick={toggleDropdown}
                            >
                                Hola, {user?.nombre || 'Usuario'}
                                <span className="chevron">▼</span>
                            </button>

                            {isDropdownOpen && (
                                <div className="dropdown-menu">

                                    {isAdmin && (
                                        <>
                                            <Link
                                                to="/registrar/actividad"
                                                className="dropdown-item"
                                                onClick={() => setIsDropdownOpen(false)}
                                            >
                                                Registrar Actividad
                                            </Link>
                                            <div className="dropdown-divider"></div>
                                            <Link
                                                to="/registrar/profesor"
                                                className="dropdown-item"
                                                onClick={() => setIsDropdownOpen(false)}
                                            >
                                                Registrar Profesor
                                            </Link>
                                            <div className="dropdown-divider"></div>
                                            <Link
                                                to="/registrar/clase"
                                                className="dropdown-item"
                                                onClick={() => setIsDropdownOpen(false)}
                                            >
                                                Registrar Clase
                                            </Link>
                                            <div className="dropdown-divider"></div>
                                        </>
                                    )}

                                    <Link
                                        to="/perfil"
                                        className="dropdown-item"
                                        onClick={() => setIsDropdownOpen(false)}
                                    >
                                        Mi Perfil
                                    </Link>
                                    <div className="dropdown-divider"></div>
                                    <button
                                        className="dropdown-item text-danger"
                                        onClick={handleLogout}
                                    >
                                        Cerrar Sesión
                                    </button>
                                </div>
                            )}
                        </div>
                    ) : (
                        <>
                            <Link to="/registrar/cliente">Registrarse</Link>
                            <Link to="/login">Iniciar Sesión</Link>
                        </>
                    )}
                </div>
            </div>
        </nav>
    );
}

export default Navbar;