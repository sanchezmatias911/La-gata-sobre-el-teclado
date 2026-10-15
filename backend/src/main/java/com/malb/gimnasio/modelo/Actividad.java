package com.malb.gimnasio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter @Setter @NoArgsConstructor @ToString @EqualsAndHashCode
@Entity
public class Actividad {
    @Id @GeneratedValue
    private Long id;
    private String nombre;
    private String descripcion;
    @Range(min = 0, max = 50, message = "Debe haber entre 0 y 50 cupos")
    private int cupo;

    //@NonNull en el momento de copilacion
    public Actividad (@NonNull String nombre, @NonNull String descripcion, @NonNull int cupo){
        this.cupo = cupo;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }
}
