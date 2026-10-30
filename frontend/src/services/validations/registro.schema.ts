import { z } from 'zod';

const empiezaConMayuscula = /^[A-ZÁÉÍÓÚÑ]/;

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
		.email("Debe ser un email valido"),
    
  contraseña: z
    .string()
    .min(8, "La contraseña debe tener un mínimo de 8 caracteres"),
    
  teléfono: z
    .string()
    .min(10, "El teléfono debe tener un mínimo de 10 caracteres"),
    
  dni: z
    .number()
    .int("Debe ser un número entero")
    .positive("El DNI debe ser mayor a 0")
});

export type RegistroBody = z.infer<typeof registroSchema>;