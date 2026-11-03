export interface ActividadResponseBody{
    id: number,
    nombre: string,
    descripcion: string,
    cupo: number
}
export interface ActividadBody{
    nombre: string,
    descripcion: string,
    cupo: number
}

export interface ProfesorResponseBody {
   id: number,
  dni: number,
  email: string,
  password: string,
  nombre: string,
  telefono: string,
  biografia: string,
  especialidades: string
}

export interface ClaseProgramadaBody {
  id:number;
  dia: string; 
  horaInicio: LocalTime;
  horaFin: LocalTime;
  idProfesor: number; 
  idActividad: number;
  estado: string; 
  nombreActividad:string;
  cantidadInscriptos: number;
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



export interface LocalTime {
  hour: number;
  minute: number;
  second: number;
  nano: number;
}


export interface SubscribirClaseBody {
   idCliente: number
}


export interface RegistroBody  {
  nombre: string;
  apellido: string;
  mail: string;
  contraseña: string;
  telefono: string;
  dni: number;
}

export interface LoginBody  {
  mail: string;
  contraseña: string;
}


// 1. Definimos los tipos de datos
export interface UserInfo {
    id: number;
    nombre: string;
    email: string;
    rol: string;
}

export interface AuthContextType {
    user: UserInfo | null;
    isAuthenticated: boolean;
    loginContext: (token: string, usuario: UserInfo) => void;
    logoutContext: () => void;
}