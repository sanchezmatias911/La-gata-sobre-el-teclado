import { z } from 'zod';

const empiezaConMayuscula = /^[A-ZÁÉÍÓÚÑ]/;
const telefonoArgRegex = /^\+54\s9\s\d{2,4}\s\d{4}-\d{4}$/;

export const registroSchema = z.object({
  nombre: z
    .string()
    .min(1, "El nombre es obligatorio")
    .refine((val) => empiezaConMayuscula.test(val), {
      message: "La primera letra debe ser mayúscula",
    }),
    
  apellido: z
    .string()
    .min(1, "El apellido es obligatorio")
    .refine((val) => empiezaConMayuscula.test(val), {
      message: "La primera letra debe ser mayúscula",
    }),
    
  mail: z
    .string()
    .trim()
    .min(1, "El email es obligatorio")
    .email("Debe ser un email válido"),
    
  contraseña: z
    .string()
    .min(8, "La contraseña debe tener un mínimo de 8 caracteres"),
    
  telefono: z // Corrigo tilde para coincidir exactamente con el DTO en Java
    .string()
    .min(1, "El teléfono es obligatorio")
    .regex(telefonoArgRegex, "Formato inválido. Ejemplo: +54 9 11 1234-5678"),
    
  dni: z
    .number({ invalid_type_error: "El DNI debe ser un número" })
    .int("Debe ser un número entero")
    .min(1000000, "EL dni debe ser valido")
    .positive("El DNI debe ser mayor a 0")
});

// Tipo inferido directamente del esquema
export type RegistroBody = z.infer<typeof registroSchema>;