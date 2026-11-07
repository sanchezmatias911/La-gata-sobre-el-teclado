package com.malb.gimnasio.controller.REST;

import com.malb.gimnasio.controller.DTO.request.CrearClienteDTO;
import com.malb.gimnasio.controller.DTO.response.ClienteDTO;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.security.UsuarioAutenticado;
import com.malb.gimnasio.service.ClienteService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/clientes")
@AllArgsConstructor
final public class ClienteREST {
    private final ClienteService clienteService;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping()
    public ResponseEntity<ClienteDTO> crear(@Valid @RequestBody CrearClienteDTO dto) {
        Cliente cliente = dto.aModelo();
        clienteService.crear(cliente);
        return ResponseEntity.ok(ClienteDTO.desdeModelo(cliente));
    }
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> recuperar(
            @PathVariable Long id,
            @AuthenticationPrincipal UsuarioAutenticado usuario
    ) {
        usuario.exigirAccesoACliente(id);

        Cliente cliente = clienteService.recuperar(id);

        return ResponseEntity.ok(ClienteDTO.desdeModelo(cliente));
    }
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping()
    public ResponseEntity<List<ClienteDTO>> recuperarTodos() {
        List<Cliente> clientes= clienteService.recuperarTodos();
        List<ClienteDTO> clientesDTO = clientes.stream().map(ClienteDTO::desdeModelo).toList();
        return ResponseEntity.ok((clientesDTO));
    }
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id){
        clienteService.eliminar(id);
        return ResponseEntity.ok("Tu usuario ha sido eliminado correctamente.");
    }
}
