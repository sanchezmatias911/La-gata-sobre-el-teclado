package com.malb.gimnasio.controller.REST;

import com.malb.gimnasio.controller.DTO.ClaseProgramadaDTO;
import com.malb.gimnasio.controller.DTO.CrearClaseProgramadaDTO;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.service.ActividadService;
import com.malb.gimnasio.service.ClaseProgramadaService;
import com.malb.gimnasio.service.InscripcionService;
import com.malb.gimnasio.service.ProfesorService;
import jakarta.validation.Valid;
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
}
