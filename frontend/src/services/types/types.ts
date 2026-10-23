export interface ActividadBody{
    id: number,
    nombre: string,
    descripcion: string,
    cupo: number
}

export interface ClaseProgramadaBody {
  dia: string; 
  horaInicio: LocalTime;
  horaFin: LocalTime;
  idProfesor: number; 
  idActividad: number;
  estado: string; 
}

export interface ProfesorBody {
  dni: number;
  email: string;
  password?: string;
  nombre: string;
  telefono: string;
  biografia: string;
  especialidades: string;
}

export interface InscripcionBody {
    id: number,
    fechaDeInscripcion : string,
    cliente: SimpleCliente,
    claseProgramada: SimpleClaseProgramada
}

export interface SimpleProfesor{
    id:number,
    nombre: string
} //provisional

export interface SimpleInscipciones {id: number}
export interface Actividad {id:number}            //provisional
export interface SimpleCliente {id:number}        //provisional
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


export interface SubscribirClaseBody {
   id: number
}