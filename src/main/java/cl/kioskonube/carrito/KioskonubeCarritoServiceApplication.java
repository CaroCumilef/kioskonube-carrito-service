package cl.kioskonube.carrito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class KioskonubeCarritoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KioskonubeCarritoServiceApplication.class, args);
    }
}
