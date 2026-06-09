package com.example.ecommerceAPI.produto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ProdutoDaoImpl implements ProdutoDAO{
    private final NamedParameterJdbcOperations jdbcTemplate;

    public ProdutoDaoImpl(NamedParameterJdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Produto> produtoRowMapper = (rs, rowNum) -> Produto.builder()
            .id(rs.getLong("id"))
            .nome(rs.getString("nome"))
            .preco(rs.getBigDecimal("preco"))
            .estoque(rs.getInt("estoque"))
            .categoriaId(rs.getLong("categoria_id"))
            .build();


    @Override
    @Transactional
    public void salvar(Produto entidade) {
        String sql = "INSERT INTO produto (nome, preco, estoque, categoria_id) VALUES (:nome, :preco, :estoque, :categoriaId)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nome", entidade.getNome())
                .addValue("preco", entidade.getPreco())
                .addValue("estoque", entidade.getEstoque())
                .addValue("categoriaId", entidade.getCategoriaId());

        jdbcTemplate.update(sql, params);
    }

    @Override
    public Produto buscarPorId(Long id) {
        String sql = "SELECT * FROM produto WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        return jdbcTemplate.queryForStream(sql, params, produtoRowMapper)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Produto não encontardo"));
    }


    @Override
    public List<Produto> listartodos() {
        String sql = "SELECT * FROM produto";
        return jdbcTemplate.query(sql, produtoRowMapper);
    }

    @Override
    public void atualizar(Produto entidade) {
        String sql = "UPDATE produto SET nome = :nome, preco = :preco, estoque = :estoque, categoria_id = :categoriaId WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", entidade.getId())
                .addValue("nome", entidade.getNome())
                .addValue("preco", entidade.getPreco())
                .addValue("estoque", entidade.getEstoque())
                .addValue("categoriaId", entidade.getCategoriaId());

        jdbcTemplate.update(sql, params);
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM produto WHERE id = :id";
        MapSqlParameterSource param = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sql, param);
    }
}
