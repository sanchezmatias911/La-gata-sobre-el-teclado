import { useState, useRef, useEffect } from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css';

function Navbar() {
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const dropdownRef = useRef<HTMLDivElement>(null);

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

    return (
        <nav className="custom-navbar">
            <div className="navbar-container">
                <Link to="/" className="navbar-logo">
                    <img src="/favicon.png" alt="Logo Gimnasio" />
                </Link>

                <div className="navbar-links">
                    <Link to="/">Inicio</Link>
                    <Link to="/clases">Clases</Link>

                    <div className="user-menu" ref={dropdownRef}>
                        <button
                            className={`user-name-btn ${isDropdownOpen ? 'active' : ''}`}
                            onClick={toggleDropdown}
                        >
                            Hola, [Nombre]
                            <span className="chevron">▼</span>
                        </button>

                        {isDropdownOpen && (
                            <div className="dropdown-menu">
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
                                    onClick={() => setIsDropdownOpen(false)}
                                >
                                    Cerrar Sesión
                                </button>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </nav>
    );
}

export default Navbar;