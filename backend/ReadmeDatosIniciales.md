# Datos iniciales de prueba — Backend Gimnasio

## ¿Qué es `DatosIniciales`?

`DatosIniciales` es una clase del backend que carga automáticamente datos de prueba al iniciar la aplicación Spring Boot.

Su objetivo es que el equipo de frontend pueda probar la aplicación sin tener que crear manualmente profesores, clientes, actividades, clases programadas e inscripciones.

Los datos se cargan utilizando los services del backend, por lo que se respetan las validaciones y reglas de negocio implementadas en ellos.

## 1. ¿Cómo se activa?

La carga de datos está desactivada por defecto. Para habilitarla, hay que agregar la siguiente propiedad en `src/main/resources/application.properties`:

```properties
cargar.datos.dummy=true
```

Una vez agregada la propiedad, hay que reiniciar el backend.

La clase `DatosIniciales` utiliza `@ConditionalOnProperty`, por lo que solo se ejecuta cuando `cargar.datos.dummy` está configurada en `true`.

**Importante:** esta configuración debe utilizarse únicamente en desarrollo. No debe habilitarse en producción.

## 2. ¿Qué datos se cargan?

Al iniciar el backend, se crean datos de prueba para las siguientes entidades:

- **Actividades:** Musculación, Funcional y Yoga.
- **Profesores:** tres profesores con sus datos de contacto y especialidades.
- **Administrador:** un usuario ADMIN para desarrollo (ver sección 7).
- **Clientes:** tres clientes con sus datos de prueba y contraseña encriptada (BCrypt).
- **Clases programadas:** tres clases distribuidas en distintos días y horarios.
- **Inscripciones:** tres inscripciones asociadas a los clientes y las clases correspondientes.

Los horarios de las clases de prueba no se superponen.

Los datos concretos, incluidos sus identificadores, pueden variar según el estado de la base de datos y la configuración del proyecto.

## 3. ¿Cómo se utilizan los datos desde el frontend?

Una vez iniciado el backend, el frontend puede consumir los endpoints habituales de la API para consultar los datos cargados.

Por ejemplo, para las clases programadas:

| Operación | Endpoint | Método HTTP |
|---|---|---|
| Listar clases | `/clases` | GET |
| Obtener una clase | `/clases/{id}` | GET |
| Crear una clase | `/clases` | POST |
| Eliminar una clase | `/clases/{id}` | DELETE |

Para consultar profesores, clientes, actividades e inscripciones, se deben utilizar los endpoints correspondientes definidos en el backend.

### Ejemplo: listar las clases programadas

```http
GET /clases
```

La respuesta contiene una lista de clases representadas mediante `ClaseProgramadaDTO`.

### Ejemplo: crear una clase programada

El DTO de creación utiliza los identificadores del profesor y de la actividad, en lugar de enviar las entidades completas.

```json
{
  "dia": "LUNES",
  "horaInicio": "10:00:00",
  "horaFin": "11:00:00",
  "idProfesor": 1,
  "idActividad": 1
}
```

Este ejemplo supone que el DTO de creación ya no recibe el estado y que los IDs corresponden a registros existentes. Si la versión actual del DTO exige otros campos, se deben incluir.

**No asuman que los IDs de prueba siempre serán `1`, `2` o `3`.** Para desarrollar el frontend, es preferible consultar primero los recursos disponibles y utilizar los identificadores que devuelve la API.

## 4. ¿Qué pasa cuando se apaga el backend?

Cuando la aplicación se cierra normalmente, `DatosIniciales` ejecuta su método de limpieza mediante `@PreDestroy`.

La limpieza intenta eliminar los datos creados por esa instancia, respetando el orden de las relaciones:

1. Inscripciones.
2. Clases programadas.
3. Clientes.
4. Profesores.
5. Actividades.

Esto evita, en condiciones normales, conflictos con las claves foráneas y elimina únicamente los registros cuyos identificadores fueron registrados durante la carga.

El **administrador no se borra** al apagar: si ya existe, no se vuelve a crear en el siguiente arranque.

**Importante:** la limpieza no está garantizada si la aplicación se cierra abruptamente, el proceso es forzado o se produce un corte de energía. En esos casos, algunos datos podrían permanecer en la base de datos.

Si quedan datos de una ejecución anterior, puede ser necesario revisar la base de datos antes de volver a iniciar el backend.

## 5. Recomendaciones para el equipo de frontend

- Iniciar el backend con `cargar.datos.dummy=true` durante el desarrollo.
- Consultar los endpoints para obtener los datos disponibles.
- No depender de identificadores fijos.
- Utilizar los IDs de profesores y actividades devueltos por la API al crear clases.
- Comprobar los códigos HTTP y los mensajes de error devueltos por el backend.
- Tener en cuenta que una clase no puede crearse si su horario entra en conflicto con otra clase.
- No utilizar los datos ficticios como información real.
- Si no aparecen los datos, comprobar que la propiedad esté activada y revisar los logs del backend.

## 6. ¿Cómo desactivar la carga de datos?

Para desactivar la funcionalidad, cambiar la propiedad a:

```properties
cargar.datos.dummy=false
```

También se puede quitar la propiedad, ya que la carga está condicionada explícitamente a que su valor sea `true`.

Después, reiniciar la aplicación.

Desactivar la propiedad evita nuevas cargas, pero no elimina los datos que ya hayan quedado persistidos en la base de datos.

## Resumen

`DatosIniciales` permite levantar un entorno de desarrollo con datos de prueba de forma automática. El frontend puede consumirlos mediante la API sin tener que cargarlos manualmente.

Para usarlo, basta con activar `cargar.datos.dummy=true` e iniciar el backend. Al apagar la aplicación normalmente, la clase intentará eliminar los registros que creó.

## 7. Credenciales de prueba por rol

Con `cargar.datos.dummy=true` se crean estos usuarios. Las contraseñas se guardan encriptadas (BCrypt); acá figuran en texto plano solo para que el frontend pueda iniciar sesión.

| Rol | Email | Contraseña |
|---|---|---|
| ADMIN | `admin@gimnasio.com` | `Admin123456` |
| CLIENTE | `maria.gonzalez@gmail.com` | `password123` |
| CLIENTE | `pedro.rodriguez@gmail.com` | `password123` |
| CLIENTE | `sofia.martinez@gmail.com` | `password123` |

Los profesores no tienen contraseña ni inician sesión.

El ADMIN es el único que puede registrar clases, profesores y actividades (`POST /clases`, `POST /actividades`, `POST /profesores`).

### Iniciar sesión

```http
POST /auth/login
```

```json
{
  "email": "admin@gimnasio.com",
  "password": "Admin123456"
}
```

La respuesta incluye el token, el rol y el vencimiento **en segundos**:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tipo": "Bearer",
  "vencimiento": 3600,
  "usuario": { "id": 1, "nombre": "Administrador", "email": "admin@gimnasio.com", "rol": "ADMIN" }
}
```

El token se envía en cada request protegido: `Authorization: Bearer <token>`.

La duración se configura con la variable de entorno `JWT_EXPIRACION_MINUTOS` (por defecto 60). No hay refresh token: al vencer, el usuario vuelve a iniciar sesión.

En Swagger (`/swagger-ui.html`) se pega el token en el botón **Authorize**.

Errores 401: `Mail o contraseña incorrectos.` (login), `Necesitás iniciar sesión...` (sin token), `Tu sesión venció...` (token vencido) y `Token inválido.`.

## 8. Administrador en otros ambientes (sin datos de prueba)

`DatosIniciales` solo corre con `cargar.datos.dummy=true`. Para tener un administrador sin datos de prueba, se definen estas variables de entorno (el administrador se crea solo si no existe ese email):

| Variable | Descripción |
|---|---|
| `ADMIN_EMAIL` | Email del administrador |
| `ADMIN_PASSWORD` | Contraseña inicial (mínimo 8 caracteres) |
| `ADMIN_DNI` | DNI del administrador |
| `ADMIN_NOMBRE` | Nombre (opcional) |
| `ADMIN_TELEFONO` | Teléfono (opcional) |

Si no están definidas, la aplicación arranca igual y no crea ningún administrador.

**Importante:** las credenciales de la sección 7 son solo para desarrollo. No se deben subir contraseñas reales al repositorio.
