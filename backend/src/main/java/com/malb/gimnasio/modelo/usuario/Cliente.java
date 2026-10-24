package com.malb.gimnasio.modelo.usuario;

import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.exception.ConflictoHorarioException;
import com.malb.gimnasio.modelo.exception.RecursoDuplicadoException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("Cliente")
@NoArgsConstructor @Getter @Setter

public class Cliente extends Usuario {
    private LocalDateTime fechaDeRegistro;

    @OneToMany(mappedBy = "cliente")
    @EqualsAndHashCode.Exclude
    private List<Inscripcion> misInscripciones = new ArrayList<>();

    public Cliente (@NonNull Long dni, @NonNull String email, @NonNull String password, @NonNull String nombre, @NonNull String telefono){
        super(dni, email, password, nombre, telefono);
        this.fechaDeRegistro = LocalDateTime.now();
    }

    public void agregarInscripcion(Inscripcion i) {
        if (estaInscriptoEn(i.getClaseProgramada())) {
            throw new RecursoDuplicadoException(
                    "El cliente ya está inscripto en esta clase."
            );
        }
        if (tieneConflictoHorario(i.getClaseProgramada())) {
            throw new ConflictoHorarioException(
                    "El cliente ya está inscripto en otra clase en ese horario."
            );
        }

        this.misInscripciones.add(i);
    }

    private boolean tieneConflictoHorario(ClaseProgramada nuevaClase) {
        return this.misInscripciones.stream()
                .map(Inscripcion::getClaseProgramada)
                .anyMatch(claseExistente ->
                        !claseExistente.equals(nuevaClase)
                                && claseExistente.getDia() == nuevaClase.getDia()
                                && claseExistente.getHoraInicio()
                                .isBefore(nuevaClase.getHoraFin())
                                && claseExistente.getHoraFin()
                                .isAfter(nuevaClase.getHoraInicio())
                );
    }

    private boolean estaInscriptoEn(ClaseProgramada claseProgramada) {
        return misInscripciones.stream()
                .anyMatch(inscripcion ->
                        inscripcion.getClaseProgramada().getId().equals(claseProgramada.getId())
                );
    }
}
