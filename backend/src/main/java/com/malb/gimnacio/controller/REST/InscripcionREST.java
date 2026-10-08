package com.malb.gimnacio.controller.REST;

import com.malb.gimnacio.controller.DTO.InscripcionDTO;
import com.malb.gimnacio.modelo.Inscripcion;
import com.malb.gimnacio.service.InscripcionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/inscripciones")

final public class InscripcionREST {
    private final InscripcionService inscripcionService;

    public InscripcionREST(InscripcionService inscripcionService){
        this.inscripcionService = inscripcionService;
    }

    @PostMapping()
    public ResponseEntity<InscripcionDTO> crear(@RequestBody InscripcionDTO dto) {
        Inscripcion inscripcion = dto.aModelo();
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
