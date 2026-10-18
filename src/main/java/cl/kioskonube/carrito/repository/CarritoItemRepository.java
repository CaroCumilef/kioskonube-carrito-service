package cl.kioskonube.carrito.repository;

import cl.kioskonube.carrito.domain.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {

    List<CarritoItem> findByPropietarioOrderByIdAsc(String propietario);

    Optional<CarritoItem> findByPropietarioAndProductoId(String propietario, Long productoId);

    void deleteByPropietario(String propietario);
}
