package com.example.ecommerceAPI.pedido;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class PedidoDaoImpl implements PedidoDAO {

    private final NamedParameterJdbcOperations jdbcTemplate;

    public PedidoDaoImpl(NamedParameterJdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Pedido> pedidoRowMapper = (rs, rowNum) -> Pedido.builder()
            .id(rs.getLong("id"))
            .dataPedido(rs.getTimestamp("data_pedido").toLocalDateTime())
            .status(rs.getString("status"))
            .total(rs.getBigDecimal("total"))
            .build();

    private final RowMapper<ItemPedido> itemPedidoRowMapper = (rs, rowNum) -> ItemPedido.builder()
            .id(rs.getLong("id"))
            .pedidoId(rs.getLong("pedido_id"))
            .produtoId(rs.getLong("produto_id"))
            .quantidade(rs.getInt("quantidade"))
            .precoUnitario(rs.getBigDecimal("preco_unitario"))
            .build();

    @Override
    @Transactional
    public void salvar(Pedido entidade) {
        if (entidade.getItens() == null || entidade.getItens().isEmpty()) {
            throw new IllegalArgumentException("O pedido precisa ter pelo menos um produto");
        }

        String sqlPedido = "INSERT INTO pedido (status, total) VALUES (:status, :total)";
        MapSqlParameterSource params_pedido = new MapSqlParameterSource()
                .addValue("status", entidade.getStatus())
                .addValue("total", entidade.getTotal());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sqlPedido, params_pedido, keyHolder);

        Long idPedidoGerado = keyHolder.getKey().longValue();
        entidade.setId(idPedidoGerado);

        String sqlItem = "INSERT INTO item_pedido (pedido_id, produto_id, quantidade, preco_unitario) " + "VALUES (:pedidoId, :produtoId, :quantidade, :precoUnitario)";

        for (ItemPedido item : entidade.getItens()) {
            MapSqlParameterSource params_ItemsPedidos = new MapSqlParameterSource()
                    .addValue("pedido_id", idPedidoGerado)
                    .addValue("produto_id", item.getProdutoId())
                    .addValue("quantidade", item.getQuantidade())
                    .addValue("preco_unitario", item.getPrecoUnitario());

            jdbcTemplate.update(sqlItem, params_ItemsPedidos);
        }
    }

    @Override
    public Pedido buscarPorId(Long id) {
        String sqlPedido = "SELECT * FROM pedido WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        Pedido pedido = jdbcTemplate.queryForStream(sqlPedido, params, pedidoRowMapper)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        String sqlItensPedidos = "SELECT * FROM item_pedido WHERE pedido_id = :pedido_id";
        MapSqlParameterSource paramItensPedidos = new MapSqlParameterSource("pedido_id", id);
        List<ItemPedido> itens = jdbcTemplate.query(sqlItensPedidos, paramItensPedidos, itemPedidoRowMapper);

        pedido.setItens(itens);
        return pedido;
    }

    @Override
    public List<Pedido> listartodos() {
        String sql = "SELECT * FROM pedido";
        List<Pedido> pedidos = jdbcTemplate.query(sql, pedidoRowMapper);

        for (Pedido p : pedidos) {
            String sqlItens = "SELECT * FROM item_pedido WHERE pedido_id = :pedido_id";
            List<ItemPedido> itensPedidos = jdbcTemplate.query(sqlItens, new MapSqlParameterSource("pedido_id", p.getId()), itemPedidoRowMapper);
            p.setItens(itensPedidos);
        }

        return pedidos;
    }

    @Override
    public void atualizar(Pedido entidade) {
        String sql = "UPDATE pedido SET status =:status, total = :total WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", entidade.getId())
                .addValue("status", entidade.getStatus())
                .addValue("total", entidade.getTotal());

        jdbcTemplate.update(sql, params);
    }

    @Override
    public void deletar(Long id) {
        String sql = "DELETE FROM pedido WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sql, params);
    }
}
