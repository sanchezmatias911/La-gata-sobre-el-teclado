package com.malb.gimnasio.controller.REST;
import com.malb.gimnasio.controller.DTO.request.SuscribirClaseDTO;
import com.malb.gimnasio.controller.DTO.response.ClaseProgramadaDTO;
import com.malb.gimnasio.controller.DTO.request.CrearClaseProgramadaDTO;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.security.UsuarioAutenticado;
import com.malb.gimnasio.service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final InscripcionService inscripcionService;
    private final ClienteService clienteService;

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

    @PostMapping("/{id}/suscripcion")
    public ResponseEntity<Void> suscribir(@PathVariable Long id, @Valid @RequestBody SuscribirClaseDTO dto, @AuthenticationPrincipal UsuarioAutenticado usuario) {

        usuario.exigirAccesoACliente(dto.idCliente());

        ClaseProgramada clase = claseProgramadaService.recuperar(id);
        Cliente cliente = clienteService.recuperar(dto.idCliente());

        inscripcionService.crear(cliente, clase);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/desuscribir/{idCliente}")
    public ResponseEntity<ClaseProgramadaDTO> desuscribir(@PathVariable Long id, @PathVariable Long idCliente, @AuthenticationPrincipal UsuarioAutenticado usuario) {

        usuario.exigirAccesoACliente(idCliente);

        Inscripcion inscripcion = claseProgramadaService.recuperarInscripcionDeCliente(id, idCliente);
        inscripcionService.eliminar(inscripcion.getId());
        clienteService.eliminarSuscripcion(idCliente, inscripcion.getId(), id);
        claseProgramadaService.eliminarInscripcion(id, inscripcion);

        ClaseProgramada claseActualizada = claseProgramadaService.recuperar(id);

        return ResponseEntity.ok(ClaseProgramadaDTO.desdeModelo(claseActualizada));
    }

}