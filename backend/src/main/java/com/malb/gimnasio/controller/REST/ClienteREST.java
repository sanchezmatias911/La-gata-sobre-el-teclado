package com.malb.gimnasio.controller.REST;

import com.malb.gimnasio.controller.DTO.ClienteDTO;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/clientes")

final public class ClienteREST {
    private final ClienteService clienteService;

    public ClienteREST(ClienteService clienteService){
        this.clienteService = clienteService;
    }

    @PostMapping()
    public ResponseEntity<ClienteDTO> crear(@Valid @RequestBody ClienteDTO dto) {
        Cliente cliente = dto.aModelo();
        clienteService.crear(cliente);
        return ResponseEntity.ok(ClienteDTO.desdeModelo(cliente));
    }

    @GetMapping("/{dni}")
    public ResponseEntity<ClienteDTO> recuperar(@PathVariable Long dni) {
        Cliente cliente = clienteService.recuperar(dni);
        return ResponseEntity.ok(ClienteDTO.desdeModelo(cliente));
    }

    @GetMapping()
    public ResponseEntity<List<ClienteDTO>> recuperarTodos() {
        List<Cliente> clientes= clienteService.recuperarTodos();
        List<ClienteDTO> clientesDTO = clientes.stream().map(ClienteDTO::desdeModelo).toList();
        return ResponseEntity.ok((clientesDTO));
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<String> eliminar(@PathVariable Long dni){
        clienteService.eliminar(dni);
        return ResponseEntity.ok("Tu usuario ha sido eliminado correctamente.");
    }
}
