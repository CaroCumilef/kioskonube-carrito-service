package cl.kioskonube.carrito.exception;

public class ItemNoEncontradoException extends RuntimeException {

    public ItemNoEncontradoException(Long productoId) {
        super("El producto " + productoId + " no está en el carrito");
    }
}
