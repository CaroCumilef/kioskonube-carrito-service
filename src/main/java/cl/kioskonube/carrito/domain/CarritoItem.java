package cl.kioskonube.carrito.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Línea del carrito de un usuario. El propietario es el "sub" del JWT de Cognito.
 */
@Entity
@Table(name = "carrito_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"propietario", "producto_id"}))
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String propietario;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "nombre_producto", nullable = false, length = 150)
    private String nombreProducto;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private int cantidad;

    @Column(name = "fecha_actualizacion", nullable = false)
    private Instant fechaActualizacion;

    protected CarritoItem() {
        // requerido por JPA
    }

    public CarritoItem(String propietario, Long productoId, String nombreProducto,
                       BigDecimal precioUnitario, int cantidad) {
        this.propietario = propietario;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.fechaActualizacion = Instant.now();
    }

    public void sumar(int unidades) {
        this.cantidad += unidades;
        this.fechaActualizacion = Instant.now();
    }

    public void restarUna() {
        this.cantidad -= 1;
        this.fechaActualizacion = Instant.now();
    }

    public BigDecimal subtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public Long getId() { return id; }
    public String getPropietario() { return propietario; }
    public Long getProductoId() { return productoId; }
    public String getNombreProducto() { return nombreProducto; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public int getCantidad() { return cantidad; }
    public Instant getFechaActualizacion() { return fechaActualizacion; }
}
