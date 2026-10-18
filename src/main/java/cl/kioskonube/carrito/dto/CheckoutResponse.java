package cl.kioskonube.carrito.dto;

import java.math.BigDecimal;
import java.time.Instant;

/** La compra quedó publicada en RabbitMQ; ordenes-service creará la orden de forma asíncrona. */
public record CheckoutResponse(String estado, int unidades, BigDecimal total, Instant fecha) {
}
