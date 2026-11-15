package com.malb.gimnasio.controller.REST;

import com.malb.gimnasio.controller.DTO.request.CrearActividadDTO;
import com.malb.gimnasio.controller.DTO.response.ActividadDTO;
import com.malb.gimnasio.modelo.Actividad;
import com.malb.gimnasio.service.ActividadService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/actividades")
@AllArgsConstructor
final public class ActividadREST {
    private final ActividadService actividadService;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping()
    public ResponseEntity<ActividadDTO> crear(@Valid @RequestBody CrearActividadDTO dto) {
        Actividad actividad = dto.aModelo();
        actividadService.crear(actividad);
        return ResponseEntity.ok(ActividadDTO.desdeModelo(actividad));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadDTO> recuperar(@PathVariable Long id) {
        Actividad actividad = actividadService.recuperar(id);
        return ResponseEntity.ok(ActividadDTO.desdeModelo(actividad));
    }

    @GetMapping()
    public ResponseEntity<List<ActividadDTO>> recuperarTodos() {
        List<Actividad> actividades= actividadService.recuperarTodos();
        List<ActividadDTO> actividadDTO = actividades.stream().map(ActividadDTO::desdeModelo).toList();
        return ResponseEntity.ok((actividadDTO));
    }
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id){
        actividadService.eliminar(id);
        return ResponseEntity.ok("Tu actividad ha sido eliminada correctamente.");
    }
}
