import type { ClaseProgramadaBody } from "../services/types/types.ts";
import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";

export const DAYS = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'];
export const START_HOUR = 8;
export const END_HOUR = 21;
export const HOURS = Array.from({ length: END_HOUR - START_HOUR + 1 }, (_, i) => START_HOUR + i);

export const mockData: ClaseProgramadaBody[] = [
    {
        id: 1,
        dia: "Lunes",
        horaInicio: { hour: 8, minute: 0, second: 0, nano: 0 },
        horaFin: { hour: 9, minute: 0, second: 0, nano: 0 },
        estado: "Activa",
        profesor: { id: 11111111 },
        actividad: { id: 10, nombre: "Funcional", descripcion: "Entrenamiento funcional", cupo: 30 },
        inscripciones: []
    },
    {
        id: 2,
        dia: "Martes",
        horaInicio: { hour: 18, minute: 0, second: 0, nano: 0 },
        horaFin: { hour: 20, minute: 0, second: 0, nano: 0 },
        estado: "Activa",
        profesor: { id: 22222222 },
        actividad: { id: 11, nombre: "Zumba", descripcion: "Clase de baile", cupo: 40 },
        inscripciones: []
    },
    {
        id: 3,
        dia: "Miércoles",
        horaInicio: { hour: 10, minute: 0, second: 0, nano: 0 },
        horaFin: { hour: 12, minute: 0, second: 0, nano: 0 },
        estado: "Cancelada",
        profesor: { id: 33333333 },
        actividad: { id: 12, nombre: "Fitcombat", descripcion: "Boxeo y cardio", cupo: 25 },
        inscripciones: []
    },
    {
        id: 4,
        dia: "Jueves",
        horaInicio: { hour: 16, minute: 0, second: 0, nano: 0 },
        horaFin: { hour: 17, minute: 0, second: 0, nano: 0 },
        estado: "Activa",
        profesor: { id: 44444444 },
        actividad: { id: 13, nombre: "Stretching", descripcion: "Estiramiento", cupo: 20 },
        inscripciones: []
    },
    {
        id: 5,
        dia: "Viernes",
        horaInicio: { hour: 19, minute: 0, second: 0, nano: 0 },
        horaFin: { hour: 20, minute: 0, second: 0, nano: 0 },
        estado: "Cancelada",
        profesor: { id: 11111111 },
        actividad: { id: 14, nombre: "Gap", descripcion: "Glúteos, abdomen y piernas", cupo: 30 },
        inscripciones: []
    },
    {
        id: 6,
        dia: "Sábado",
        horaInicio: { hour: 10, minute: 0, second: 0, nano: 0 },
        horaFin: { hour: 12, minute: 0, second: 0, nano: 0 },
        estado: "Activa",
        profesor: { id: 55555555 },
        actividad: { id: 15, nombre: "Yoga", descripcion: "Hatha Yoga", cupo: 20 },
        inscripciones: []
    },
    {
        id: 7,
        dia: "Lunes",
        horaInicio: { hour: 9, minute: 0, second: 0, nano: 0 },
        horaFin: { hour: 12, minute: 0, second: 0, nano: 0 },
        estado: "Cancelada",
        profesor: { id: 11111111 },
        actividad: { id: 16, nombre: "Yoga", descripcion: "Hatha Yoga", cupo: 30 },
        inscripciones: []
    },
    {
        id: 8,
        dia: "Martes",
        horaInicio: { hour: 12, minute: 0, second: 0, nano: 0 },
        horaFin: { hour: 13, minute: 30, second: 0, nano: 0 },
        estado: "Activa",
        profesor: { id: 11111111 },
        actividad: { id: 17, nombre: "Fitcombat", descripcion: "Entrenamiento funcional", cupo: 30 },
        inscripciones: []
    },
    {
        id: 9,
        dia: "Lunes",
        horaInicio: { hour: 13, minute: 30, second: 0, nano: 0 },
        horaFin: { hour: 17, minute: 30, second: 0, nano: 0 },
        estado: "Cancelada",
        profesor: { id: 11111111 },
        actividad: { id: 18, nombre: "Stretching", descripcion: "Glúteos, abdomen y piernas", cupo: 30 },
        inscripciones: []
    },
    {
        id: 10,
        dia: "Lunes",
        horaInicio: { hour: 12, minute: 0, second: 0, nano: 0 },
        horaFin: { hour: 13, minute: 30, second: 0, nano: 0 },
        estado: "Cancelada",
        profesor: { id: 11111111 },
        actividad: { id: 17, nombre: "Funcional", descripcion: "Entrenamiento funcional", cupo: 30 },
        inscripciones: []
    }
];