package com.example.ecommerceAPI.categoria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {
    private Long id;
    private String nome;
    private String descricao;
}
