package com.example.ecommerceAPI.pedido;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoDAO pedidoDAO;


    public PedidoController(PedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }

    @PostMapping("/criar-pedido")
    @Operation(summary = "Criar um pedido", description = "Criar um novo pedido para o comercio")
    public ResponseEntity<Pedido> criarPedido(@RequestBody Pedido pedido) {
        pedidoDAO.salvar(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @GetMapping("/buscar-id-pedido")
    @Operation(summary = "Buscar pedido", description = "Buscar pedido por ID")
    public ResponseEntity<Pedido> buscarPedidoPorId (@RequestParam Long id) {
        Pedido pedido = pedidoDAO.buscarPorId(id);
        return ResponseEntity.ok(pedido);
    }

    @PutMapping("/atualizar-pedido")
    @Operation(summary = "Atualizar pedido", description = "Atualizar um pedido do comercio")
    public ResponseEntity<String> atualizarPedido (@RequestParam Long id, @RequestBody Pedido pedido) {
        pedido.setId(id);
        pedidoDAO.atualizar(pedido);
        return ResponseEntity.ok("Pedido atualizado com sucesso");
    }

    @GetMapping("/listar-pedidos")
    @Operation(summary = "Listar pedidos", description = "Listar todos os pedidos")
    public ResponseEntity<List<Pedido>> listarTodosPedidos() {
        List<Pedido> pedidos = pedidoDAO.listartodos();
        return ResponseEntity.ok(pedidos);
    }

    @DeleteMapping("/deletar-pedido")
    @Operation(summary = "Deletar pedido", description = "Deletar um pedido do comercio")
    public ResponseEntity<String> deletarPedido(@RequestParam Long id) {
        pedidoDAO.deletar(id);
        return ResponseEntity.ok("Pedido deletado");
    }
}