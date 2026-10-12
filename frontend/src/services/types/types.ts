export interface ActividadBody{
    id: number,
    nombre: string,
    descripcion: string,
    cupo: 0

}
export interface ClaseProgramadaBody {
  id: number;
  dia: string; 
  horaInicio: LocalTime;
  horaFin: LocalTime;
  estado: string; 
  profesor: Profesor; 
  actividad: Actividad;
  inscripciones: Inscripcion[]; 
}

export interface Profesor {}
export interface Inscripcion {}
export interface Actividad {}


export interface LocalTime {
  hour: number;
  minute: number;
  second: number;
  nano: number;
}

/*
export interface Actividad {
  id: number;
  nombre: string;
  descripcion: string;
  cupo: number;
}

export interface Cliente {
  dni: number;
  email: string;
  password?: string; // Es buena práctica hacer opcional el password en el front
  nombre: string;
  telefono: string;
  fechaDeRegistro: string; // Viene como string ISO ("2026-10-12T...")
}




export interface Profesor {
  dni: number;
  email: string;
  password?: string;
  nombre: string;
  telefono: string;
  biografia: string;
  especialidades: string;
  misClases: ClaseProgramadaResumen[];
}


export interface Inscripcion {
  id: number;
  fechaDeInscripcion: string;
  cliente: Cliente;
  claseProgramada: ClaseProgramadaResumen;
}


*/
