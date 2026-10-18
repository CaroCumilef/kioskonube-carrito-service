package cl.kioskonube.carrito.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> cuerpoInvalido(MethodArgumentNotValidException ex) {
        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .sorted()
                .toList();
        return construir(HttpStatus.BAD_REQUEST, "Datos de entrada inválidos", detalles);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> parametroInvalido(HandlerMethodValidationException ex) {
        List<String> detalles = ex.getAllErrors().stream()
                .map(e -> String.valueOf(e.getDefaultMessage()))
                .sorted()
                .toList();
        return construir(HttpStatus.BAD_REQUEST, "Parámetros inválidos", detalles);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> tipoInvalido(MethodArgumentTypeMismatchException ex) {
        return construir(HttpStatus.BAD_REQUEST, "Valor inválido para " + ex.getName(), List.of());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> jsonInvalido(HttpMessageNotReadableException ex) {
        return construir(HttpStatus.BAD_REQUEST, "El cuerpo de la petición no es un JSON válido", List.of());
    }

    @ExceptionHandler(ItemNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> noEncontrado(ItemNoEncontradoException ex) {
        return construir(HttpStatus.NOT_FOUND, ex.getMessage(), List.of());
    }

    @ExceptionHandler(CarritoVacioException.class)
    public ResponseEntity<ErrorResponse> vacio(CarritoVacioException ex) {
        return construir(HttpStatus.CONFLICT, ex.getMessage(), List.of());
    }

    @ExceptionHandler(PublicacionException.class)
    public ResponseEntity<ErrorResponse> publicacion(PublicacionException ex) {
        return construir(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), List.of());
    }

    private ResponseEntity<ErrorResponse> construir(HttpStatus status, String mensaje, List<String> detalles) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(Instant.now(), status.value(), mensaje, detalles));
    }
}
