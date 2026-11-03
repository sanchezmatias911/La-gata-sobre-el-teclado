import { z } from 'zod';

export const loginSchema = z.object({
  mail: z.string()
    .min(1, { message: "El correo es obligatorio." })
    .email({ message: "Ingresá un correo electrónico válido." }),
  contraseña: z.string()
    .min(8, { message: "La contraseña debe tener al menos 8 caracteres." })
});