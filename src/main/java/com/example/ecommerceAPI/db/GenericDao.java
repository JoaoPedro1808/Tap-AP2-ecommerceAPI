package com.example.ecommerceAPI.db;

import java.util.List;

public interface GenericDao<T, ID> {
    void salvar (T entidade);
    T buscarPorId(ID id);
    List<T> listartodos();
    void atualizar(T entidade);
    void deletar(ID id);
}
