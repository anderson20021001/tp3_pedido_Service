package com.microservicos.pedidoservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicos.pedidoservice.dto.PedidoRequest;
import com.microservicos.pedidoservice.events.EventType;
import com.microservicos.pedidoservice.model.Pedido;
import com.microservicos.pedidoservice.outbox.OutboxEvent;
import com.microservicos.pedidoservice.outbox.OutboxRepository;
import com.microservicos.pedidoservice.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public PedidoService(PedidoRepository pedidoRepository,
                         OutboxRepository outboxRepository,
                         ObjectMapper objectMapper) {
        this.pedidoRepository = pedidoRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    @Transactional
    public Pedido criar(PedidoRequest request) {
        Pedido pedido = pedidoRepository.save(
                new Pedido(request.getDescricao(), request.getValor())
        );

        salvarEvento(pedido, EventType.PEDIDO_CRIADO);
        return pedido;
    }

    @Transactional
    public Pedido atualizar(Long id, PedidoRequest request) {
        Pedido pedido = buscarPorId(id);
        if (pedido == null) return null;

        pedido.setDescricao(request.getDescricao());
        pedido.setValor(request.getValor());
        pedidoRepository.save(pedido);

        salvarEvento(pedido, EventType.PEDIDO_ATUALIZADO);
        return pedido;
    }

    @Transactional
    public boolean deletar(Long id) {
        Pedido pedido = buscarPorId(id);
        if (pedido == null) return false;

        pedidoRepository.delete(pedido);
        salvarEvento(pedido, EventType.PEDIDO_CANCELADO);
        return true;
    }

    private void salvarEvento(Pedido pedido, EventType tipo) {
        try {
            String payload = objectMapper.writeValueAsString(pedido);
            outboxRepository.save(new OutboxEvent(
                    Instant.now(),
                    pedido.getId(),
                    tipo,
                    payload,
                    "PENDING"
            ));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
