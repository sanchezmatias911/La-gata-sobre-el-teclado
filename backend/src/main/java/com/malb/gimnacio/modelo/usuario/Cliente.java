package com.malb.gimnacio.modelo.usuario;

import com.malb.gimnacio.modelo.ClaseProgramada;
import com.malb.gimnacio.modelo.Inscripcion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@DiscriminatorValue("Cliente")
@NoArgsConstructor @Getter @Setter

public class Cliente extends Usuario {
    private LocalDateTime fechaDeRegistro;
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER) @EqualsAndHashCode.Exclude
    private List<Inscripcion> misInscripciones;

    public Cliente (@NonNull Long dni, @NonNull String email, @NonNull String password, @NonNull String nombre, @NonNull String telefono){
        super(dni, email, password, nombre, telefono);
        this.fechaDeRegistro = LocalDateTime.now();
    }
}
