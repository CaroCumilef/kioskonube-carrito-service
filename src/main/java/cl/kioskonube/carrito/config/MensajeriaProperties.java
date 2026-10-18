package cl.kioskonube.carrito.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Nombres de exchanges y routing keys del carrito-service (application.yml: kioskonube.mensajeria).
 */
@Validated
@ConfigurationProperties(prefix = "kioskonube.mensajeria")
public record MensajeriaProperties(@NotNull @Valid Publicacion carrito) {

    public record Publicacion(@NotBlank String exchange, @NotBlank String routingKey) {
    }
}
