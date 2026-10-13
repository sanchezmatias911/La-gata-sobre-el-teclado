package com.malb.gimnasio.modelo;

import com.malb.gimnasio.modelo.exception.HorarioInvalido;
import com.malb.gimnasio.modelo.usuario.Profesor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

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
    private Profesor profesor;
    @ManyToOne
    private Actividad actividad;
    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER) @EqualsAndHashCode.Exclude
    private List<Inscripcion> inscripciones;

    public ClaseProgramada (@NonNull Dia dia, @NonNull LocalTime horaInicio, @NonNull LocalTime horaFin,
                            @NonNull Profesor profesor, @NonNull Actividad actividad, @NonNull EstadoClaseProgramada estado){
        this.validarHorario(horaInicio, horaFin);
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.profesor = profesor;
        this.actividad = actividad;
        this.estado = estado;
    }

    private void validarHorario(LocalTime inicio, LocalTime fin) {
        if (!inicio.isBefore(fin)) {
            throw new HorarioInvalido("La hora de inicio debe ser anterior a la hora de fin.");
        }
    }
}
