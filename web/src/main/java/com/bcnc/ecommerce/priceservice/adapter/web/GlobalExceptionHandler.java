package com.bcnc.ecommerce.priceservice.adapter.web;

import com.bcnc.ecommerce.priceservice.adapter.web.dto.PriceErrorResponse;
import com.bcnc.ecommerce.priceservice.domain.exception.PriceNotFoundException;

import jakarta.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Manejador global de excepciones para la capa web del servicio de precios.
 * <p>
 * Captura y transforma excepciones comunes en respuestas HTTP estructuradas
 * en formato JSON, manteniendo coherencia y evitando la exposición de
 * errores internos al cliente.
 * </p>
 *
 * <p>Formato típico de respuesta:</p>
 * <pre>
 * {
 *   "timestamp": "2025-06-15T22:30:00",
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "No se encontró tarifa aplicable"
 * }
 * </pre>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Mensaje de error genérico para excepciones no controladas
     * o fallos internos.
     */
    private static final String MSG_INTERNAL_SERVER_ERROR =
            "Error interno del servidor";
    /**
     * Mensaje base para errores de conversión de tipo en
     * parámetros (type mismatch).
     * Se espera que se concatene con el nombre del parámetro.
     */
    private static final String MSG_TYPE_MISMATCH =
            "Formato de parámetro inválido: ";
    /**
     * Mensaje base para errores por parámetros requeridos que no
     * fueron enviados.
     * Se espera que se concatene con el nombre del parámetro faltante.
     */
    private static final String MSG_MISSING_PARAMETER =
            "Falta parámetro requerido: ";

    /**
     * Maneja errores cuando no se encuentra un precio aplicable.
     *
     * @param ex Excepción personalizada lanzada desde el caso de uso.
     * @return Respuesta HTTP 404 con mensaje informativo.
     */
    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<PriceErrorResponse> handlePriceNotFound(
            final PriceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Maneja errores cuando un parámetro tiene un tipo incorrecto.
     *
     * @param ex Excepción lanzada por Spring cuando hay
     *           incompatibilidad de tipos.
     * @return Respuesta HTTP 400 con mensaje sobre el parámetro conflictivo.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<PriceErrorResponse> handleTypeMismatch(
            final MethodArgumentTypeMismatchException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, MSG_TYPE_MISMATCH
                + ex.getName());
    }

    /**
     * Maneja errores cuando falta un parámetro requerido en la solicitud.
     *
     * @param ex Excepción lanzada por Spring si falta un parámetro obligatorio.
     * @return Respuesta HTTP 400 con nombre del parámetro faltante.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<PriceErrorResponse> handleMissingParams(
            final MissingServletRequestParameterException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, MSG_MISSING_PARAMETER
                + ex.getParameterName());
    }

    /**
     * Maneja argumentos no válidos pasados a los controladores.
     *
     * @param ex Excepción estándar de Java para argumentos ilegales.
     * @return Respuesta HTTP 400 con detalle del error.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<PriceErrorResponse> handleIllegalArgument(
            final IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Maneja errores de validación declarativa.
     * (anotaciones como @Min, @NotNull...).
     *
     * @param ex Excepción lanzada cuando se incumplen restricciones de
     *           validación.
     * @return Respuesta HTTP 400 con mensaje del validador.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<PriceErrorResponse> handleConstraintViolation(
            final ConstraintViolationException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Manejador genérico para cualquier otra excepción no controlada.
     *
     * @param ex Excepción de tipo general.
     * @return Respuesta HTTP 500 con mensaje genérico.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<PriceErrorResponse> handleGenericException(
            final Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                MSG_INTERNAL_SERVER_ERROR);
    }

    /**
     * Construye una respuesta estructurada en formato JSON para enviar
     * al cliente.
     *
     * @param status  Código de estado HTTP.
     * @param message Mensaje explicativo del error.
     * @return Objeto {@link ResponseEntity} con cuerpo detallado del error.
     */
    private ResponseEntity<PriceErrorResponse> buildResponse(
            final HttpStatus status,
            final String message) {
        PriceErrorResponse priceErrorResponse =
                new PriceErrorResponse(LocalDateTime.now(), status.value(),
                        status.getReasonPhrase(), message);
        return ResponseEntity.status(status).body(priceErrorResponse);
    }
}

