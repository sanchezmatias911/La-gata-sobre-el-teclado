package com.malb.gimnacio.controller.REST;

import com.malb.gimnacio.controller.DTO.ClaseProgramadaDTO;
import com.malb.gimnacio.modelo.ClaseProgramada;
import com.malb.gimnacio.service.ActividadService;
import com.malb.gimnacio.service.ClaseProgramadaService;
import com.malb.gimnacio.service.InscripcionService;
import com.malb.gimnacio.service.ProfesorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/clases")
final public class ClaseProgramadaREST {
    private final ClaseProgramadaService claseProgramadaService;
    private final ProfesorService profesorService;
    private final ActividadService actividadService;
    private final InscripcionService inscripcionService;

    public ClaseProgramadaREST(ClaseProgramadaService claseProgramadaService, ProfesorService profesorService,
                               ActividadService actividadService, InscripcionService inscripcionService){
        this.claseProgramadaService = claseProgramadaService;
        this.profesorService = profesorService;
        this.actividadService = actividadService;
        this.inscripcionService = inscripcionService;
    }

    @PostMapping()
    public ResponseEntity<ClaseProgramadaDTO> crear(@RequestBody ClaseProgramadaDTO dto) {
        ClaseProgramada clase = dto.aModelo();
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
}
