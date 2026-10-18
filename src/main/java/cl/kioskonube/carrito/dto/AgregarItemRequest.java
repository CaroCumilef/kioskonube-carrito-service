package cl.kioskonube.carrito.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record AgregarItemRequest(
        @NotNull(message = "El productoId es obligatorio")
        @Positive(message = "El productoId debe ser positivo")
        Long productoId,

        @NotBlank(message = "El nombre del producto es obligatorio")
        @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
        String nombreProducto,

        @NotNull(message = "El precio unitario es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
        BigDecimal precioUnitario,

        @Min(value = 1, message = "La cantidad mínima es 1")
        @Max(value = 99, message = "La cantidad máxima es 99")
        int cantidad
) {
}
