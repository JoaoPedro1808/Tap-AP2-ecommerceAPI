package com.example.ecommerceAPI.categoria;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoriaDaoImpl implements CategoriaDAO {
    private final NamedParameterJdbcOperations jdbcTemplate;

    public CategoriaDaoImpl(NamedParameterJdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Categoria> categoriaRowMapper = (rs, rowNum) -> Categoria.builder()
            .id(rs.getLong("id"))
            .nome(rs.getString("nome"))
            .descricao(rs.getString("descricao"))
            .build();

    @Override
    public void salvar(Categoria entidade)  {
        if (entidade.getNome() == null || entidade.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da categoria não pode estar vazio");
        }

        String sql = "INSERT INTO categoria (nome, descricao) VALUES (:nome, :descricao)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nome", entidade.getNome())
                .addValue("descricao", entidade.getDescricao());

        jdbcTemplate.update(sql, params);
    }

    @Override
    public Categoria buscarPorId(Long id) {
        String sql = "SELECT * FROM categoria WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForStream(sql, params, categoriaRowMapper)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com o ID: " + id));
    }

    @Override
    public List<Categoria> listartodos() {
        String sql = "SELECT * FROM categoria";
        return jdbcTemplate.query(sql, categoriaRowMapper);
    }

    @Override
    public void atualizar(Categoria entidade) {
        String sql = "UPDATE categoria SET nome = :nome, descricao = :descricao WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", entidade.getId())
                .addValue("nome", entidade.getNome())
                .addValue("descricao", entidade.getDescricao());

        jdbcTemplate.update(sql, params);
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM categoria WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sql, params);
    }

}
