package br.com.brunov.controla_ativos.controla_ativos.config.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    List<FieldErrorDetail> detatails
) {
    public record FieldErrorDetail(
        String field,
        String message
    ) {
    }  
}
