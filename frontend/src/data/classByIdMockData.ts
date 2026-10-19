import type { ClaseProgramadaBody } from "../services/types/types.ts";

export const funcional: ClaseProgramadaBody = {
    id: 1,
    dia: "Lunes",
    horaInicio: { hour: 8, minute: 0, second: 0, nano: 0 },
    horaFin: { hour: 12, minute: 30, second: 0, nano: 0 },
    estado: "Activa",
    profesor: { id: 11111111, nombre: "Jorge" },
    actividad: { id: 10, nombre: "Funcional", descripcion: "Entrenamiento funcional", cupo: 30 },
    inscripciones: []
    // inscripciones: [{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}]
    // inscripciones: [{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}]
}

export const imageUrl = "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?q=80&w=1470&auto=format&fit=crop";