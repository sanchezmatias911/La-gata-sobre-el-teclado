package com.malb.gimnasio.modelo;

import com.malb.gimnasio.modelo.exception.HorarioInvalido;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.modelo.usuario.Profesor;
import com.malb.gimnasio.modelo.exception.InscripcionNoPermitidaException;
import com.malb.gimnasio.modelo.exception.RecursoDuplicadoException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.malb.gimnasio.modelo.EstadoClaseProgramada.ACTIVA;

@Getter @Setter @NoArgsConstructor @ToString @EqualsAndHashCode
@Entity
public class ClaseProgramada {
    @Id @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private Dia dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    @Enumerated(EnumType.STRING)
    private EstadoClaseProgramada estado;
    @ManyToOne
    @JoinColumn(name = "profesor_id", nullable = false)
    private Profesor profesor;
    @ManyToOne
    @JoinColumn(name = "actividad_id", nullable = false)
    private Actividad actividad;
    @OneToMany(mappedBy = "claseProgramada")
    @EqualsAndHashCode.Exclude
    private List<Inscripcion> inscripciones = new ArrayList<>();


    public ClaseProgramada (@NonNull Dia dia, @NonNull LocalTime horaInicio, @NonNull LocalTime horaFin,
                            @NonNull Profesor profesor, @NonNull Actividad actividad, @NonNull EstadoClaseProgramada estado){
        this.validarHorario(horaInicio, horaFin);
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.profesor = profesor;
        this.actividad = actividad;
        this.estado = estado;
        this.inscripciones = new ArrayList<>();
    }

    private void validarHorario(LocalTime inicio, LocalTime fin) {
        if (!inicio.isBefore(fin)) {
            throw new HorarioInvalido("La hora de inicio debe ser anterior a la hora de fin.");
        }
    }

    public int cantidadInscriptos() { return this.inscripciones.size(); }

    public void agregarInscripcion(Inscripcion i) {
        if (!puedeRecibirInscripciones()) {
            throw new InscripcionNoPermitidaException(
                    "La clase no está disponible para nuevas inscripciones"
            );
        }

        if (tieneInscripto(i.getCliente())) {
            throw new RecursoDuplicadoException(
                    "El cliente ya está inscripto en esta clase."
            );
        }

        this.inscripciones.add(i);;
    }

    private boolean puedeRecibirInscripciones() {
        return this.estado == ACTIVA && this.actividad.getCupo() > this.cantidadInscriptos();
    }

    private boolean tieneInscripto(Cliente cliente) {
        return inscripciones.stream()
                .anyMatch(inscripcion ->
                        inscripcion.getCliente().getId().equals(cliente.getId())
                );
    }

}
