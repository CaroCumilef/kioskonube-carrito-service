package cl.kioskonube.carrito.messaging.producer;

import cl.kioskonube.carrito.config.MensajeriaProperties;
import cl.kioskonube.carrito.exception.PublicacionException;
import cl.kioskonube.carrito.messaging.event.CarritoCheckoutEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Productor del carrito-service. Única clase que conoce RabbitTemplate.
 */
@Component
public class CarritoEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(CarritoEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final MensajeriaProperties props;

    public CarritoEventPublisher(RabbitTemplate rabbitTemplate, MensajeriaProperties props) {
        this.rabbitTemplate = rabbitTemplate;
        this.props = props;
    }

    /** Si falla, lanza excepción y la transacción se revierte (el carrito no se vacía). */
    public void publicarCheckout(CarritoCheckoutEvent evento) {
        try {
            rabbitTemplate.convertAndSend(props.carrito().exchange(), props.carrito().routingKey(), evento);
            log.info("Evento carrito.checkout publicado cliente={} items={}",
                    evento.clienteEmail(), evento.items().size());
        } catch (AmqpException e) {
            log.error("No se pudo publicar carrito.checkout cliente={}: {}", evento.clienteEmail(), e.getMessage());
            throw new PublicacionException("No se pudo enviar la compra a RabbitMQ", e);
        }
    }
}
