package com.malb.gimnasio.controller.REST;
import com.malb.gimnasio.controller.DTO.request.CrearProfesorDTO;
import com.malb.gimnasio.controller.DTO.response.ProfesorDTO;
import com.malb.gimnasio.modelo.usuario.Profesor;
import com.malb.gimnasio.service.ProfesorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin
@RequestMapping("/profesores")
@AllArgsConstructor
final public class ProfesorREST {
    private final ProfesorService profesorService;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping()
    public ResponseEntity<ProfesorDTO> crear(@Valid @RequestBody CrearProfesorDTO dto) {
        Profesor profesor = dto.aModelo();
        profesorService.crear(profesor);
        return ResponseEntity.ok(ProfesorDTO.desdeModelo(profesor));
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<ProfesorDTO> recuperar(@PathVariable Long id) {
        Profesor profesor = profesorService.recuperar(id);
        return ResponseEntity.ok(ProfesorDTO.desdeModelo(profesor));
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping()
    public ResponseEntity<List<ProfesorDTO>> recuperarTodos() {
        List<Profesor> profesores= profesorService.recuperarTodos();
        List<ProfesorDTO> profesoresDTO = profesores.stream().map(ProfesorDTO::desdeModelo).toList();
        return ResponseEntity.ok((profesoresDTO));
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id){
        profesorService.eliminar(id);
        return ResponseEntity.ok("Tu usuario ha sido eliminado correctamente.");
    }
}
