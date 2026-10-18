package cl.kioskonube.carrito.controller;

import cl.kioskonube.carrito.dto.AgregarItemRequest;
import cl.kioskonube.carrito.dto.CarritoResponse;
import cl.kioskonube.carrito.dto.CheckoutRequest;
import cl.kioskonube.carrito.dto.CheckoutResponse;
import cl.kioskonube.carrito.service.CarritoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Carrito del usuario autenticado. El dueño del carrito sale del JWT ("sub"),
 * así un usuario nunca puede ver ni modificar el carrito de otro.
 */
@RestController
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService service;

    public CarritoController(CarritoService service) {
        this.service = service;
    }

    @GetMapping
    public CarritoResponse obtener(@AuthenticationPrincipal Jwt jwt) {
        return service.obtener(jwt.getSubject());
    }

    @PostMapping("/items")
    public CarritoResponse agregar(@AuthenticationPrincipal Jwt jwt,
                                   @Valid @RequestBody AgregarItemRequest request) {
        return service.agregar(jwt.getSubject(), request);
    }

    @DeleteMapping("/items/{productoId}")
    public CarritoResponse quitarUna(@AuthenticationPrincipal Jwt jwt,
                                     @PathVariable @Positive(message = "El productoId debe ser positivo") Long productoId) {
        return service.quitarUna(jwt.getSubject(), productoId);
    }

    @DeleteMapping
    public ResponseEntity<Void> vaciar(@AuthenticationPrincipal Jwt jwt) {
        service.vaciar(jwt.getSubject());
        return ResponseEntity.noContent().build();
    }

    /** 202 Accepted: la orden se crea de forma asíncrona en ordenes-service vía RabbitMQ */
    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(@AuthenticationPrincipal Jwt jwt,
                                                     @Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(service.checkout(jwt.getSubject(), request.clienteEmail()));
    }
}
