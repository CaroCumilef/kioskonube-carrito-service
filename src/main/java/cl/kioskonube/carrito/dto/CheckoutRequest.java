package cl.kioskonube.carrito.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** Datos para confirmar la compra: el correo donde llegarán las notificaciones */
public record CheckoutRequest(
        @NotBlank(message = "El email del cliente es obligatorio")
        @Email(message = "El email del cliente no es válido")
        String clienteEmail
) {
}
