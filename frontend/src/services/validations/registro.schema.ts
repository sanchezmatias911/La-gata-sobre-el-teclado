import { z } from 'zod';

export const registroSchema = z.object({
  nombre: z.string().min(1, "El nombre es requerido"),
  apellido: z.string().min(1, "El apellido es requerido"),
  mail: z.string().email("Debe ser un email válido"),
  contraseña: z.string().min(1, "La contraseña es requerida"),
 teléfono: z.number().min(1, "El teléfono es requerido"),
  edad: z.number().min(0, "La edad debe ser un número válido")
});

export type RegistroBody = z.infer<typeof registroSchema>;