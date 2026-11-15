import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { getAllActivities } from '../../services/api/actividad.service';
import { getAllProfesores } from '../../services/api/profesor.service';
import { createClass } from '../../services/api/clase.services';
import './ClaseForm.css';
import { useDocumentTitle } from "../../hooks/useDocumentTitle.ts";

type FormErrors = {
    idActividad?: string;
    idProfesor?: string;
    dia?: string;
    horaInicio?: string;
    horaFin?: string;
};

export const ClaseForm = () => {
    useDocumentTitle('Registrar Clase');
    const navigate = useNavigate();
    const [actividades, setActividades] = useState<any[]>([]);
    const [profesores, setProfesores] = useState<any[]>([]);

    const [formData, setFormData] = useState({
        dia: 'LUNES',
        horaInicio: '',
        horaFin: '',
        estado: 'ACTIVA',
        idProfesor: '',
        idActividad: ''
    });

    const [errors, setErrors] = useState<FormErrors>({});

    useEffect(() => {
        const fetchData = async () => {
            try {
                const dataActividades = await getAllActivities();
                const dataProfesores = await getAllProfesores();

                const actividadesArray = Array.isArray(dataActividades) ? dataActividades : (dataActividades?.content || []);
                const profesoresArray = Array.isArray(dataProfesores) ? dataProfesores : (dataProfesores?.content || []);

                setActividades(actividadesArray);
                setProfesores(profesoresArray);
            } catch (error) {
                toast.error("Error al cargar actividades y profesores.");
            }
        };
        fetchData();
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });

        if (errors[name as keyof FormErrors]) {
            setErrors((prev) => ({ ...prev, [name]: undefined }));
        }
    };

    const isTimeValid = (time: string) => {
        if (!time) return false;
        const [hours, minutes] = time.split(':').map(Number);

        const timeInMinutes = hours * 60 + minutes;
        const minTime = 8 * 60;
        const maxTime = 22 * 60;

        return timeInMinutes >= minTime && timeInMinutes <= maxTime;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const newErrors: FormErrors = {};

        if (!formData.idActividad) {
            newErrors.idActividad = "La actividad es obligatoria.";
        }

        if (!formData.idProfesor) {
            newErrors.idProfesor = "El profesor es obligatorio.";
        }

        if (!formData.horaInicio) {
            newErrors.horaInicio = "La hora de inicio es obligatoria.";
        } else if (!isTimeValid(formData.horaInicio)) {
            newErrors.horaInicio = "Debe estar entre las 08:00 y las 22:00.";
        }

        if (!formData.horaFin) {
            newErrors.horaFin = "La hora de fin es obligatoria.";
        } else if (!isTimeValid(formData.horaFin)) {
            newErrors.horaFin = "Debe estar entre las 08:00 y las 22:00.";
        }

        if (formData.horaInicio && formData.horaFin && formData.horaInicio >= formData.horaFin) {
            newErrors.horaFin = "La hora de fin debe ser posterior al inicio.";
        }

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            toast.error("Por favor, revisa los campos ingresados.");
            return;
        }

        setErrors({});

        const horaInicioFormateada = `${formData.horaInicio}:00`;
        const horaFinFormateada = `${formData.horaFin}:00`;

        const nuevaClase = {
            dia: formData.dia,
            horaInicio: horaInicioFormateada,
            horaFin: horaFinFormateada,
            estado: formData.estado,
            idProfesor: Number(formData.idProfesor),
            idActividad: Number(formData.idActividad)
        };

        try {
            await createClass(nuevaClase as any);
            toast.success("¡Clase registrada exitosamente!");
            setTimeout(() => navigate('/clases'), 1500);
        } catch (error) {
            console.error("Error al crear la clase:", error);
            toast.error("Hubo un error al registrar la clase.");
        }
    };

    return (
        <div className="class-form-page-wrapper-clase-form">
            <div className="class-form-content-wrapper-clase-form">

                <div className="class-form-welcome-panel-clase-form">
                    <h1 className="welcome-title-clase-form">Planificá<br/>la semana<br/>de entrenamiento.</h1>
                    <p className="welcome-text-clase-form">Administrá los horarios del gimnasio. <br/> Creá nuevas clases, asigná profesores y mantené el calendario actualizado para todos los clientes.</p>
                </div>

                <div className="class-form-panel-clase-form">
                    <div className="class-form-container-clase-form">
                        <h2 className="class-form-title-clase-form">Nueva Clase</h2>
                        <p className="class-form-subtitle-clase-form">Completá los detalles para agregarla al calendario.</p>

                        <form onSubmit={handleSubmit} className="class-form-clase-form" noValidate>

                            <div className="form-group-clase-form">
                                <label>Actividad *</label>
                                <select
                                    name="idActividad"
                                    value={formData.idActividad}
                                    onChange={handleChange}
                                    className={errors.idActividad ? 'input-error-clase-form' : ''}
                                >
                                    <option value="">Seleccione una actividad...</option>
                                    {actividades.map(act => (
                                        <option key={act.id} value={act.id}>
                                            {act.nombre || `Actividad ${act.id}`}
                                        </option>
                                    ))}
                                </select>
                                {errors.idActividad && <span className="error-message-clase-form">{errors.idActividad}</span>}
                            </div>

                            <div className="form-group-clase-form">
                                <label>Profesor *</label>
                                <select
                                    name="idProfesor"
                                    value={formData.idProfesor}
                                    onChange={handleChange}
                                    className={errors.idProfesor ? 'input-error-clase-form' : ''}
                                >
                                    <option value="">Seleccione un profesor...</option>
                                    {profesores.map(prof => (
                                        <option key={prof.id} value={prof.id}>
                                            {prof.nombre} (DNI: {prof.dni})
                                        </option>
                                    ))}
                                </select>
                                {errors.idProfesor && <span className="error-message-clase-form">{errors.idProfesor}</span>}
                            </div>

                            <div className="form-group-clase-form">
                                <label>Día de la semana *</label>
                                <select
                                    name="dia"
                                    value={formData.dia}
                                    onChange={handleChange}
                                    className={errors.dia ? 'input-error-clase-form' : ''}
                                >
                                    <option value="LUNES">Lunes</option>
                                    <option value="MARTES">Martes</option>
                                    <option value="MIERCOLES">Miércoles</option>
                                    <option value="JUEVES">Jueves</option>
                                    <option value="VIERNES">Viernes</option>
                                    <option value="SABADO">Sábado</option>
                                </select>
                                {errors.dia && <span className="error-message-clase-form">{errors.dia}</span>}
                            </div>

                            <div className="form-row-clase-form">
                                <div className="form-group-clase-form">
                                    <label>Hora Inicio *</label>
                                    <input
                                        type="time"
                                        name="horaInicio"
                                        value={formData.horaInicio}
                                        onChange={handleChange}
                                        className={errors.horaInicio ? 'input-error-clase-form' : ''}
                                    />
                                    {errors.horaInicio && <span className="error-message-clase-form">{errors.horaInicio}</span>}
                                </div>
                                <div className="form-group-clase-form">
                                    <label>Hora Fin *</label>
                                    <input
                                        type="time"
                                        name="horaFin"
                                        value={formData.horaFin}
                                        onChange={handleChange}
                                        className={errors.horaFin ? 'input-error-clase-form' : ''}
                                    />
                                    {errors.horaFin && <span className="error-message-clase-form">{errors.horaFin}</span>}
                                </div>
                            </div>

                            <div className="form-actions-clase-form">
                                <button
                                    type="button"
                                    className="btn-modern-clase-form btn-secondary-clase-form"
                                    onClick={() => navigate('/clases')}
                                >
                                    Cancelar
                                </button>
                                <button
                                    type="submit"
                                    className="btn-modern-clase-form btn-primary-clase-form"
                                >
                                    Guardar Clase
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ClaseForm;