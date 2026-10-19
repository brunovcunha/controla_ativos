package br.com.brunov.controla_ativos.controla_ativos.config.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiError> handleNotFound(
                        ResourceNotFoundException ex,
                        HttpServletRequest request) {

                return buildResponse(
                                HttpStatus.NOT_FOUND,
                                ex.getMessage(),
                                request,
                                List.of());
        }

        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ApiError> handleBusiness(
                        BusinessException ex,
                        HttpServletRequest request) {

                return buildResponse(
                                HttpStatus.CONFLICT,
                                ex.getMessage(),
                                request,
                                List.of());
        }

        @ExceptionHandler(ResourceConflictException.class)
        public ResponseEntity<ApiError> handleConflict(
                        ResourceConflictException ex,
                        HttpServletRequest request) {

                return buildResponse(
                                HttpStatus.CONFLICT,
                                ex.getMessage(),
                                request,
                                List.of());
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiError> handleValidation(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                List<ApiError.FieldErrorDetail> details = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> new ApiError.FieldErrorDetail(
                                                error.getField(),
                                                error.getDefaultMessage() != null
                                                                ? error.getDefaultMessage()
                                                                : "Valor inválido"))
                                .toList();

                return buildResponse(
                                HttpStatus.BAD_REQUEST,
                                "Um ou mais campos são inválidos.",
                                request,
                                details);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ApiError> handleConstraintViolation(
                        ConstraintViolationException ex,
                        HttpServletRequest request) {

                List<ApiError.FieldErrorDetail> details = ex.getConstraintViolations()
                                .stream()
                                .map(violation -> new ApiError.FieldErrorDetail(
                                                violation.getPropertyPath().toString(),
                                                violation.getMessage()))
                                .toList();

                return buildResponse(
                                HttpStatus.BAD_REQUEST,
                                "Um ou mais parâmetros são inválidos.",
                                request,
                                details);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ApiError> handleInvalidBody(
                        HttpMessageNotReadableException ex,
                        HttpServletRequest request) {

                return buildResponse(
                                HttpStatus.BAD_REQUEST,
                                "O corpo da requisição está ausente ou contém dados inválidos.",
                                request,
                                List.of());
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ApiError> handleDataIntegrity(
                        DataIntegrityViolationException ex,
                        HttpServletRequest request) {

                return buildResponse(
                                HttpStatus.CONFLICT,
                                "A operação viola uma restrição dos dados.",
                                request,
                                List.of());
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ApiError> handleIllegalArgument(
                        IllegalArgumentException ex,
                        HttpServletRequest request) {

                return buildResponse(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage() != null
                                                ? ex.getMessage()
                                                : "Parâmetro inválido.",
                                request,
                                List.of());
        }

        @ExceptionHandler(IllegalStateException.class)
        public ResponseEntity<ApiError> handleIllegalState(
                        IllegalStateException ex,
                        HttpServletRequest request) {

                return buildResponse(
                                HttpStatus.CONFLICT,
                                ex.getMessage() != null
                                                ? ex.getMessage()
                                                : "A operação não pode ser realizada no estado atual.",
                                request,
                                List.of());
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleUnexpected(
                        Exception ex,
                        HttpServletRequest request) {

                log.error(
                                "Erro inesperado ao processar {} {}",
                                request.getMethod(),
                                request.getRequestURI(),
                                ex);

                return buildResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Ocorreu um erro interno. Tente novamente mais tarde.",
                                request,
                                List.of());
        }

        private ResponseEntity<ApiError> buildResponse(
                        HttpStatus status,
                        String message,
                        HttpServletRequest request,
                        List<ApiError.FieldErrorDetail> details) {

                ApiError body = new ApiError(
                                LocalDateTime.now(),
                                status.value(),
                                status.getReasonPhrase(),
                                message,
                                request.getRequestURI(),
                                details);

                return ResponseEntity
                                .status(status)
                                .body(body);
        }
}