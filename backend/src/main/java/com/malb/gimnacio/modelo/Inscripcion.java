package com.malb.gimnacio.modelo;

import com.malb.gimnacio.modelo.usuario.Cliente;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @ToString
@Entity
public class Inscripcion {
    @Id @GeneratedValue
    private Long id;
    private LocalDateTime fechaDeInscripcion;
    @ManyToOne
    private Cliente cliente;
    @ManyToOne
    private ClaseProgramada claseProgramada;

    public Inscripcion (@NonNull Cliente cliente, @NonNull ClaseProgramada claseProgramada){
        this.fechaDeInscripcion = LocalDateTime.now();
        this.cliente = cliente;
        this.claseProgramada = claseProgramada;
    }

}
