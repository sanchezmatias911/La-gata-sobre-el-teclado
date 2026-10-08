package com.malb.gimnacio.controller.REST;
import com.malb.gimnacio.controller.DTO.ProfesorDTO;
import com.malb.gimnacio.modelo.usuario.Profesor;
import com.malb.gimnacio.service.ProfesorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin
@RequestMapping("/profesores")
final public class ProfesorREST {
    private final ProfesorService profesorService;

    public ProfesorREST(ProfesorService profesorService){
        this.profesorService = profesorService;
    }

    @PostMapping()
    public ResponseEntity<ProfesorDTO> crear(@RequestBody ProfesorDTO dto) {
        Profesor profesor = dto.aModelo();
        profesorService.crear(profesor);
        return ResponseEntity.ok(ProfesorDTO.desdeModelo(profesor));
    }

    @GetMapping("/{dni}")
    public ResponseEntity<ProfesorDTO> recuperar(@PathVariable Long dni) {
        Profesor profesor = profesorService.recuperar(dni);
        return ResponseEntity.ok(ProfesorDTO.desdeModelo(profesor));
    }

    @GetMapping()
    public ResponseEntity<List<ProfesorDTO>> recuperarTodos() {
        List<Profesor> profesores= profesorService.recuperarTodos();
        List<ProfesorDTO> profesoresDTO = profesores.stream().map(ProfesorDTO::desdeModelo).toList();
        return ResponseEntity.ok((profesoresDTO));
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<String> eliminar(@PathVariable Long dni){
        profesorService.eliminar(dni);
        return ResponseEntity.ok("Tu usuario ha sido eliminado correctamente.");
    }
}
