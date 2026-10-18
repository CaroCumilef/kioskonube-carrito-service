package cl.kioskonube.carrito.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Topología RabbitMQ del carrito-service. Solo configuración, sin lógica de negocio.
 * La cola consumidora (ordenes.checkout.queue + DLQ) la declara ordenes-service, que es quien consume.
 */
@Configuration
public class RabbitConfig {

    private final MensajeriaProperties props;

    public RabbitConfig(MensajeriaProperties props) {
        this.props = props;
    }

    /** Exchange del dominio carrito (topic, durable) */
    @Bean
    public TopicExchange carritoExchange() {
        return new TopicExchange(props.carrito().exchange(), true, false);
    }

    /** Envío de mensajes en JSON (solo el productor) */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new JacksonJsonMessageConverter());
        return template;
    }
}
