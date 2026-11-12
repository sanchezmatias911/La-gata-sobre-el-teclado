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
  profesorResponse: SimpleProfesor; 
  idActividad: number;
  estado: string; 
  nombreActividad: string;
  cantidadInscriptos: number;
}

export interface ProfesorBody {
  dni: number;
  email: string;
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

export interface RegistroBody {
  nombre: string;
  apellido: string;
  mail: string;
  password: string; 
  telefono: string;
  dni: number;
}

export interface LoginBody {
    email: string;
    password: string; 
}

export type RolUsuario = 'CLIENTE' | 'ADMIN';

export interface UserInfo {
    id: number;
    nombre: string;
    email: string;
    rol: RolUsuario; 
}

export interface LoginResponse {
    token: string;
    tipo: string;
    vencimiento: number; 
    usuario: UserInfo;
}

export interface AuthContextType {
    user: UserInfo | null;
    isAuthenticated: boolean;
    loginContext: (data: LoginResponse) => void; 
    logoutContext: () => void;
}