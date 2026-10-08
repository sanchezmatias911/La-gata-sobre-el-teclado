package com.malb.gimnacio.controller.REST;

import com.malb.gimnacio.controller.DTO.ClienteDTO;
import com.malb.gimnacio.modelo.usuario.Cliente;
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
    public ResponseEntity<ClienteDTO> crear(@RequestBody ClienteDTO dto) {
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
