package com.example.ecommerceAPI.pedido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {
    private Long id;
    private LocalDateTime dataPedido;
    private String status;
    private BigDecimal total;
    private List<ItemPedido> itens;
}
