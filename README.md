# kioskonube-carrito-service

Microservicio de carrito de KioskoNube (puerto 8082).

- Guarda el carrito de cada usuario (identificado por el `sub` del JWT de Cognito) en PostgreSQL (RDS), schema `carrito`.
- `POST /carrito/checkout` publica el evento `carrito.checkout` en `carrito.exchange` (topic).
  ordenes-service lo consume desde `ordenes.checkout.queue` y crea la orden.

| Método | Ruta | Descripción |
|---|---|---|
| GET | /carrito | Ver mi carrito |
| POST | /carrito/items | Agregar producto |
| DELETE | /carrito/items/{productoId} | Quitar una unidad |
| DELETE | /carrito | Vaciar carrito |
| POST | /carrito/checkout | Confirmar compra (publica en RabbitMQ) |
