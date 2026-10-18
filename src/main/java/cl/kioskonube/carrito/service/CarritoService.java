package cl.kioskonube.carrito.service;

import cl.kioskonube.carrito.domain.CarritoItem;
import cl.kioskonube.carrito.dto.AgregarItemRequest;
import cl.kioskonube.carrito.dto.CarritoResponse;
import cl.kioskonube.carrito.dto.CheckoutResponse;
import cl.kioskonube.carrito.exception.CarritoVacioException;
import cl.kioskonube.carrito.exception.ItemNoEncontradoException;
import cl.kioskonube.carrito.messaging.event.CarritoCheckoutEvent;
import cl.kioskonube.carrito.messaging.producer.CarritoEventPublisher;
import cl.kioskonube.carrito.repository.CarritoItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Lógica de negocio del carrito. No conoce RabbitMQ: delega en CarritoEventPublisher.
 */
@Service
public class CarritoService {

    private static final Logger log = LoggerFactory.getLogger(CarritoService.class);

    private final CarritoItemRepository repository;
    private final CarritoEventPublisher publisher;

    public CarritoService(CarritoItemRepository repository, CarritoEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Transactional(readOnly = true)
    public CarritoResponse obtener(String propietario) {
        return CarritoResponse.desde(repository.findByPropietarioOrderByIdAsc(propietario));
    }

    @Transactional
    public CarritoResponse agregar(String propietario, AgregarItemRequest request) {
        repository.findByPropietarioAndProductoId(propietario, request.productoId())
                .ifPresentOrElse(
                        item -> item.sumar(request.cantidad()),
                        () -> repository.save(new CarritoItem(propietario, request.productoId(),
                                request.nombreProducto().trim(), request.precioUnitario(), request.cantidad())));
        log.info("Producto {} agregado al carrito ({} unidades)", request.productoId(), request.cantidad());
        return obtener(propietario);
    }

    @Transactional
    public CarritoResponse quitarUna(String propietario, Long productoId) {
        CarritoItem item = repository.findByPropietarioAndProductoId(propietario, productoId)
                .orElseThrow(() -> new ItemNoEncontradoException(productoId));
        if (item.getCantidad() <= 1) {
            repository.delete(item);
        } else {
            item.restarUna();
        }
        return obtener(propietario);
    }

    @Transactional
    public void vaciar(String propietario) {
        repository.deleteByPropietario(propietario);
    }

    /**
     * Confirma la compra: publica carrito.checkout y vacía el carrito.
     * Ambas cosas ocurren en la misma transacción: si RabbitMQ falla, el carrito NO se vacía.
     */
    @Transactional
    public CheckoutResponse checkout(String propietario, String clienteEmail) {
        List<CarritoItem> lineas = repository.findByPropietarioOrderByIdAsc(propietario);
        if (lineas.isEmpty()) {
            throw new CarritoVacioException();
        }
        CarritoResponse resumen = CarritoResponse.desde(lineas);

        CarritoCheckoutEvent evento = new CarritoCheckoutEvent(
                clienteEmail.trim().toLowerCase(),
                lineas.stream()
                        .map(i -> new CarritoCheckoutEvent.Item(i.getProductoId(), i.getNombreProducto(),
                                i.getCantidad(), i.getPrecioUnitario()))
                        .toList(),
                Instant.now());

        repository.deleteByPropietario(propietario);
        publisher.publicarCheckout(evento);
        log.info("Checkout realizado cliente={} unidades={} total={}",
                evento.clienteEmail(), resumen.unidades(), resumen.total());

        return new CheckoutResponse("ENVIADA", resumen.unidades(), resumen.total(), evento.fecha());
    }
}
