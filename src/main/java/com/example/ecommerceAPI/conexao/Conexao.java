package com.example.ecommerceAPI.conexao;

import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import org.springframework.jdbc.datasource.DataSourceUtils;

@Component
public class Conexao {
    private final DataSource datasource;

    public Conexao(DataSource datasource) {
        this.datasource = datasource;
    }

    public Connection getConnection() {
        return DataSourceUtils.getConnection(datasource);
    }
}
