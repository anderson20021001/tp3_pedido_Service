package com.microservicos.pedidoservice.controller;

import com.microservicos.pedidoservice.dto.PedidoRequest;
import com.microservicos.pedidoservice.model.Pedido;
import com.microservicos.pedidoservice.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pedido> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscar(@PathVariable Long id) {
        Pedido p = service.buscarPorId(id);
        return (p == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(p);
    }

    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody PedidoRequest request) {
        Pedido criado = service.criar(request);
        return ResponseEntity.created(URI.create("/api/pedidos/" + criado.getId())).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizar(@PathVariable Long id, @RequestBody PedidoRequest request) {
        Pedido atualizado = service.atualizar(id, request);
        if (atualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        boolean removed = service.deletar(id);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
