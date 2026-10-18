package cl.kioskonube.carrito.dto;

import java.math.BigDecimal;

public record ItemResponse(
        Long productoId,
        String nombreProducto,
        int cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
