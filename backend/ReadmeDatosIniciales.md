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
- **Clientes:** tres clientes con sus datos de prueba.
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