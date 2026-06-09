package com.example.ecommerceAPI.produto;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final ProdutoDAO produtoDAO;

    public ProdutoController(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    @PostMapping("criar-produto")
    @Operation(summary = "Criar produto", description = "Criar um produto para o sistema")
    public ResponseEntity<Produto> criarProduto(@RequestBody Produto produto) {
        produtoDAO.salvar(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @GetMapping("/buscar-id-produto")
    @Operation(summary = "Buscar produto", description = "Buscar por um produto pelo id")
    public ResponseEntity<Produto> buscarProdutoPorId (@RequestParam Long id) {
        Produto produto = produtoDAO.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }

    @PutMapping("/atualizar-produto/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualizar um dos produtos do comercio")
    public ResponseEntity<String> atualizarPedido (@PathVariable Long id, @RequestBody Produto produto) {
        produto.setId(id);
        produtoDAO.atualizar(produto);
        return ResponseEntity.ok("Produto atualizado com sucesso");
    }

    @GetMapping("/listar-produtos")
    @Operation(summary = "Listar produtos", description = "Lista todos os produtos do comercio")
    public ResponseEntity<List<Produto>> listarTodosProdutos() {
        List<Produto> produtos = produtoDAO.listartodos();
        return ResponseEntity.ok(produtos);
    }

    @DeleteMapping("/deletar-produto")
    @Operation(summary = "Deletar produtos", description = "Deletar um produto do coemrcio")
    public ResponseEntity<String> deletarProduto(@RequestParam Long id) {
        produtoDAO.deletar(id);
        return ResponseEntity.ok("Produto deletado");
    }
}
