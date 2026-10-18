package cl.kioskonube.carrito.dto;

import cl.kioskonube.carrito.domain.CarritoItem;

import java.math.BigDecimal;
import java.util.List;

public record CarritoResponse(List<ItemResponse> items, int unidades, BigDecimal total) {

    public static CarritoResponse desde(List<CarritoItem> lineas) {
        List<ItemResponse> items = lineas.stream()
                .map(i -> new ItemResponse(i.getProductoId(), i.getNombreProducto(), i.getCantidad(),
                        i.getPrecioUnitario(), i.subtotal()))
                .toList();
        int unidades = lineas.stream().mapToInt(CarritoItem::getCantidad).sum();
        BigDecimal total = lineas.stream().map(CarritoItem::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CarritoResponse(items, unidades, total);
    }
}
