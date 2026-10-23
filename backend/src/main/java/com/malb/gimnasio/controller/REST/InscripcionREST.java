package com.malb.gimnasio.controller.REST;

import com.malb.gimnasio.controller.DTO.request.CrearInscripcionDTO;
import com.malb.gimnasio.controller.DTO.response.InscripcionDTO;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.service.ClaseProgramadaService;
import com.malb.gimnasio.service.ClienteService;
import com.malb.gimnasio.service.InscripcionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/inscripciones")
@AllArgsConstructor
final public class InscripcionREST {
    private final InscripcionService inscripcionService;
    private final ClaseProgramadaService claseProgramadaService;
    private final ClienteService clienteService;

    @PostMapping()
    public ResponseEntity<InscripcionDTO> crear(@Valid @RequestBody CrearInscripcionDTO dto) {
        //se podria hacer un mapper o que el servicio tenga el request jaja
        ClaseProgramada clase = claseProgramadaService.recuperar(dto.idClaseProgramada());
        Cliente cliente = clienteService.recuperar(dto.idCliente());

        Inscripcion inscripcion = dto.aModelo(cliente, clase);
        inscripcionService.crear(inscripcion);
        return ResponseEntity.ok(InscripcionDTO.desdeModelo(inscripcion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InscripcionDTO> recuperar(@PathVariable Long id) {
        Inscripcion inscripcion = inscripcionService.recuperar(id);
        return ResponseEntity.ok(InscripcionDTO.desdeModelo(inscripcion));
    }

    @GetMapping()
    public ResponseEntity<List<InscripcionDTO>> recuperarTodos() {
        List<Inscripcion> inscripciones= inscripcionService.recuperarTodos();
        List<InscripcionDTO> inscripcionDTO = inscripciones.stream().map(InscripcionDTO::desdeModelo).toList();
        return ResponseEntity.ok((inscripcionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id){
        inscripcionService.eliminar(id);
        return ResponseEntity.ok("Tu actividad ha sido eliminada correctamente.");
    }
}
