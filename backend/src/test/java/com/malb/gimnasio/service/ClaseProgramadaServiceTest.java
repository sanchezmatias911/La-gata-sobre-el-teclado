package com.malb.gimnasio.service;

import com.malb.gimnasio.dao.ClaseProgramadaDAO;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Dia;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.exception.*;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.service.exception.RecursoNoEncontradoException;
import com.malb.gimnasio.service.exception.RecursoEnUsoException;
import com.malb.gimnasio.service.impl.ClaseProgramadaServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClaseProgramadaServiceImplTest {

    private ClaseProgramadaDAO dao;
    private ClaseProgramadaServiceImpl service;

    @BeforeEach
    void setUp() {
        dao = mock(ClaseProgramadaDAO.class);
        service = new ClaseProgramadaServiceImpl(dao);
    }

    private ClaseProgramada claseValida() {
        ClaseProgramada clase = mock(ClaseProgramada.class);

        when(clase.getDia()).thenReturn(Dia.LUNES);
        when(clase.getHoraInicio()).thenReturn(LocalTime.of(10, 0));
        when(clase.getHoraFin()).thenReturn(LocalTime.of(11, 0));

        return clase;
    }

    @Test
    void crear_claseNula_lanzaExcepcion() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.crear(null)
        );

        verifyNoInteractions(dao);
    }

    @Test
    void crear_diaNulo_lanzaExcepcion() {
        ClaseProgramada clase = claseValida();
        when(clase.getDia()).thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.crear(clase)
        );

        verify(dao, never()).save(any());
    }

    @Test
    void crear_horaInicioNula_lanzaExcepcion() {
        ClaseProgramada clase = claseValida();
        when(clase.getHoraInicio()).thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.crear(clase)
        );

        verify(dao, never()).save(any());
    }

    @Test
    void crear_horaFinNula_lanzaExcepcion() {
        ClaseProgramada clase = claseValida();
        when(clase.getHoraFin()).thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.crear(clase)
        );

        verify(dao, never()).save(any());
    }

    @Test
    void crear_horaFinAnteriorAlInicio_lanzaExcepcion() {
        ClaseProgramada clase = claseValida();

        when(clase.getHoraInicio()).thenReturn(LocalTime.of(11, 0));
        when(clase.getHoraFin()).thenReturn(LocalTime.of(10, 0));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.crear(clase)
        );

        verify(dao, never()).save(any());
    }

    @Test
    void crear_horariosIguales_lanzaExcepcion() {
        ClaseProgramada clase = claseValida();

        when(clase.getHoraInicio()).thenReturn(LocalTime.of(10, 0));
        when(clase.getHoraFin()).thenReturn(LocalTime.of(10, 0));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.crear(clase)
        );

        verify(dao, never()).save(any());
    }

    @Test
    void crear_conflictoHorario_lanzaExcepcion() {
        ClaseProgramada clase = claseValida();

        when(dao.existeConflictoHorario(
                Dia.LUNES,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        )).thenReturn(true);

        assertThrows(
                RecursoEnUsoException.class,
                () -> service.crear(clase)
        );

        verify(dao, never()).save(any());
    }

    @Test
    void crear_sinConflicto_guardaClase() {
        ClaseProgramada clase = claseValida();

        when(dao.existeConflictoHorario(
                Dia.LUNES,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0)
        )).thenReturn(false);

        service.crear(clase);

        verify(dao).save(clase);
    }

    @Test
    void recuperar_idNulo_lanzaExcepcion() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.recuperar(null)
        );
    }

    @Test
    void recuperar_claseInexistente_lanzaExcepcion() {
        when(dao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                RecursoNoEncontradoException.class,
                () -> service.recuperar(1L)
        );
    }

    @Test
    void recuperar_claseExistente_devuelveClase() {
        ClaseProgramada clase = claseValida();
        when(dao.findById(1L)).thenReturn(Optional.of(clase));

        assertSame(clase, service.recuperar(1L));
    }

    @Test
    void recuperarTodos_devuelveLista() {
        List<ClaseProgramada> clases = List.of(claseValida());
        when(dao.findAll()).thenReturn(clases);

        assertEquals(clases, service.recuperarTodos());
    }

    @Test
    void recuperarInscripcion_clienteInscripto_devuelveInscripcion() {
        ClaseProgramada clase = claseValida();
        Inscripcion inscripcion = mock(Inscripcion.class);
        Cliente cliente = mock(Cliente.class);

        when(dao.findById(1L)).thenReturn(Optional.of(clase));
        when(clase.getInscripciones()).thenReturn(List.of(inscripcion));
        when(inscripcion.getCliente()).thenReturn(cliente);
        when(cliente.getId()).thenReturn(2L);

        assertSame(
                inscripcion,
                service.recuperarInscripcionDeCliente(1L, 2L)
        );
    }

    @Test
    void recuperarInscripcion_clienteNoInscripto_lanzaExcepcion() {
        ClaseProgramada clase = claseValida();

        when(dao.findById(1L)).thenReturn(Optional.of(clase));
        when(clase.getInscripciones()).thenReturn(List.of());

        assertThrows(
                RecursoNoEncontradoException.class,
                () -> service.recuperarInscripcionDeCliente(1L, 2L)
        );
    }

    @Test
    void recuperarInscripcion_idClaseNulo_lanzaExcepcion() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.recuperarInscripcionDeCliente(null, 2L)
        );
    }

    @Test
    void eliminarInscripcion_eliminaYGuardaClase() {
        ClaseProgramada clase = claseValida();
        Inscripcion inscripcion = mock(Inscripcion.class);

        when(dao.findById(1L)).thenReturn(Optional.of(clase));

        service.eliminarInscripcion(1L, inscripcion);

        verify(clase).eliminarInscripcion(inscripcion);
        verify(dao).save(clase);
    }

    @Test
    void eliminarInscripcion_idNulo_lanzaExcepcion() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.eliminarInscripcion(null, mock(Inscripcion.class))
        );

        verifyNoInteractions(dao);
    }

    @Test
    void eliminar_eliminaClaseExistente() {
        ClaseProgramada clase = claseValida();
        when(dao.findById(1L)).thenReturn(Optional.of(clase));

        service.eliminar(1L);

        verify(dao).delete(clase);
    }

    @Test
    void eliminar_idNulo_lanzaExcepcion() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.eliminar(null)
        );

        verifyNoInteractions(dao);
    }

    @Test
    void eliminarTodos_eliminaTodasLasClases() {
        service.eliminarTodos();

        verify(dao).deleteAll();
    }
}

