export interface ActividadBody{
    id: number,
    nombre: string,
    descripcion: string,
    cupo: number
}

export interface ClaseProgramadaBody {
    id: number,
    dia: string,
    horaInicio: LocalTime,
    horaFin: LocalTime,
    estado: string,
    profesor: SimpleProfesor,
    actividad: ActividadBody,
    inscripciones: SimpleInscipciones[]
}

export interface ProfesorBody {
    dni: number;
    email: string;
    password?: string;
    nombre: string;
    telefono: string;
    biografia: string;
    especialidades: string;
    misClases: SimpleClaseProgramada[];
}

export interface InscripcionBody {
    id: number,
    fechaDeInscripcion : string,
    cliente: SimpleCliente,
    claseProgramada: SimpleClaseProgramada
}

export interface SimpleInscipciones {id: number}
export interface Actividad {id:number}            //provisional
export interface SimpleCliente {id:number}        //provisional
export interface SimpleProfesor{id:number}        //provisional
export interface SimpleClaseProgramada{id:number} //provisional

export interface ClienteBody {
  dni: number;
  email: string;
  password:string
  nombre: string;
  telefono: string;
  fechaDeRegistro: string; 
  misInscripciones: SimpleInscipciones []
}

export interface LocalTime {
  hour: number;
  minute: number;
  second: number;
  nano: number;
}
