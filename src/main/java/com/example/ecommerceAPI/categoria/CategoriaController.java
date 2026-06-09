package com.example.ecommerceAPI.categoria;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/categoria")
public class CategoriaController {
    private final CategoriaDAO categoriaDao;

    public CategoriaController(CategoriaDAO categoriaDao) {
        this.categoriaDao = categoriaDao;
    }

    @PostMapping("/criar-categoria")
    @Operation(summary = "Criar categoria", description = "Criar uma nova categoria para o comercio")
    public ResponseEntity<String> criarCategoria(@RequestBody Categoria categoria) {
        try {
            categoriaDao.salvar(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body("Categoria criada");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/listar-categorias")
    @Operation(summary = "Listar as categorias", description = "Mostra todas as categoria do sistema")
    public ResponseEntity<List<Categoria>> listarTodasAsCategorias() {
        return ResponseEntity.ok(categoriaDao.listartodos());
    }

    @GetMapping("/busca-categoria/{id}")
    @Operation(summary = "Buscar por id", description = "Buscar uma categoria pelo ID")
    public ResponseEntity<Categoria> buscarCtageoriaPorId(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(categoriaDao.buscarPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/atualizar-categoria/{id}")
    @Operation(summary = "Atualizar categoria", description = "Atualizar a categoria do produto")
    public ResponseEntity<String> atualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        categoria.setId(id);
        categoriaDao.atualizar(categoria);
        return ResponseEntity.ok("Categoria criado com sucesso!!");
    }

    @DeleteMapping("/deletar-categoria/{id}")
    @Operation(summary = "Deletar uma categoria", description = "Delete uma categoria pelo id")
    public ResponseEntity<String> deletarCategoria(@RequestParam Long id) {
        categoriaDao.deletar(id);
        return ResponseEntity.ok("Categoria deletada");
    }

}
