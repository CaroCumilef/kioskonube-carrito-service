package cl.kioskonube.carrito.messaging.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Evento publicado en carrito.exchange (carrito.checkout).
 * Sus campos coinciden con lo que espera ordenes-service para crear la orden
 * (clienteEmail + items con productoId, nombreProducto, cantidad, precioUnitario).
 */
public record CarritoCheckoutEvent(
        String clienteEmail,
        List<Item> items,
        Instant fecha
) {
    public record Item(Long productoId, String nombreProducto, int cantidad, BigDecimal precioUnitario) {
    }
}
