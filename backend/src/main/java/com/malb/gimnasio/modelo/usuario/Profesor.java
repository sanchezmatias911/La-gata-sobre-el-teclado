package com.malb.gimnasio.modelo.usuario;

import com.malb.gimnasio.modelo.ClaseProgramada;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@DiscriminatorValue("Profesor")
@NoArgsConstructor @Getter @Setter

public class Profesor extends Usuario {
    private String biografia;
    private String especialidades;
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER) @EqualsAndHashCode.Exclude
    private List<ClaseProgramada> misClases;

    public Profesor (@NonNull Long dni, @NonNull String email, @NonNull String password, @NonNull String nombre,
                     @NonNull String telefono, String biografia, String especialidades){
        super(dni, email, password, nombre, telefono);
        this.biografia = biografia;
        this.especialidades = especialidades;
    }
}
