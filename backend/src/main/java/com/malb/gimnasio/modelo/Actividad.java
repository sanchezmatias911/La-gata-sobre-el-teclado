package com.malb.gimnasio.modelo;

import com.malb.gimnasio.modelo.exception.CupoInvalidoException;
import com.malb.gimnasio.modelo.exception.RecursoInvalidoException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter @NoArgsConstructor @ToString @EqualsAndHashCode
@Entity
public class Actividad {
    @Id @GeneratedValue
    @Setter //solo para test
    private Long id;
    private String nombre;
    private String descripcion;
    @Range(min = 0, max = 50, message = "Debe haber entre 0 y 50 cupos")
    private int cupo;

    //@NonNull en el momento de compilación
    public Actividad (@NonNull String nombre, @NonNull String descripcion, int cupo){
        this.validarActividad(nombre, descripcion, cupo);

        this.cupo = cupo;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }

    private void validarActividad(String nombre, String descripcion, int cupo) {
        this.validarNombre(nombre);
        this.validarDescripcion(descripcion);
        this.validarCupo(cupo);
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new RecursoInvalidoException("el nombre es obligatorio");
        }
    }

    private void validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new RecursoInvalidoException("la descripcion es obligatoria");
        }
    }

    private void validarCupo(int cupo) {
        if (cupo <= 0 || cupo > 50) {
            throw new CupoInvalidoException("el cupo debe ser entre 1 - 50");
        }
    }

    public void setNombre(String nombre) {
        this.validarNombre(nombre);
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.validarDescripcion(descripcion);
        this.descripcion = descripcion;
    }

    public void setCupo(int cupo) {
        this.validarCupo(cupo);
        this.cupo = cupo;
    }
}
