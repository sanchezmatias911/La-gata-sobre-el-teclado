package com.malb.gimnasio.controller.REST;

import com.malb.gimnasio.controller.DTO.request.CrearClienteDTO;
import com.malb.gimnasio.controller.DTO.request.CrearInscripcionDTO;
import com.malb.gimnasio.controller.DTO.response.ClaseProgramadaDTO;
import com.malb.gimnasio.controller.DTO.request.CrearClaseProgramadaDTO;
import com.malb.gimnasio.controller.DTO.response.ClienteDTO;
import com.malb.gimnasio.controller.DTO.response.InscripcionDTO;
import com.malb.gimnasio.modelo.Actividad;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.modelo.usuario.Profesor;
import com.malb.gimnasio.service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/clases")
@AllArgsConstructor
final public class ClaseProgramadaREST {
    private final ClaseProgramadaService claseProgramadaService;
    private final ProfesorService profesorService;
    private final ActividadService actividadService;
    private final ClienteService clienteService;
    private final InscripcionService inscripcionService;

    @PostMapping()
    public ResponseEntity<ClaseProgramadaDTO> crear(@Valid @RequestBody CrearClaseProgramadaDTO dto) {
        ClaseProgramada clase = dto.aModelo(profesorService.recuperar(dto.idProfesor()), actividadService.recuperar(dto.idActividad()));
        claseProgramadaService.crear(clase);
        return ResponseEntity.ok(ClaseProgramadaDTO.desdeModelo(clase));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaseProgramadaDTO> recuperar(@PathVariable Long id) {
        ClaseProgramada clase = claseProgramadaService.recuperar(id);
        return ResponseEntity.ok(ClaseProgramadaDTO.desdeModelo(clase));
    }

    @GetMapping()
    public ResponseEntity<List<ClaseProgramadaDTO>> recuperarTodos() {
        List<ClaseProgramada> clases= claseProgramadaService.recuperarTodos();
        List<ClaseProgramadaDTO> clasesDTO = clases.stream().map(ClaseProgramadaDTO::desdeModelo).toList();
        return ResponseEntity.ok((clasesDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id){
        claseProgramadaService.eliminar(id);
        return ResponseEntity.ok("Tu clase ha sido eliminada correctamente.");
    }

    @PostMapping("/{id}/inscripciones")
    public ResponseEntity<Void> suscribir(@PathVariable Long id, @Valid @RequestBody CrearClienteDTO dtoCliente, @Valid @RequestBody CrearClaseProgramadaDTO dtoClase) {
        Cliente cliente = dtoCliente.aModelo();

        Profesor p = profesorService.recuperar(dtoClase.idActividad());
        Actividad a = actividadService.recuperar(dtoClase.idActividad());
        ClaseProgramada clase = dtoClase.aModelo(p, a);

        inscripcionService.crear(cliente, clase); //esto implica que el request de inscripciones no es necesario, aunque no me gusta poner tanta logica en el metodo

        return ResponseEntity.noContent().build();
    }
}
