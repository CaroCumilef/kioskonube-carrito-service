package cl.kioskonube.carrito.exception;

public class CarritoVacioException extends RuntimeException {

    public CarritoVacioException() {
        super("El carrito está vacío");
    }
}
