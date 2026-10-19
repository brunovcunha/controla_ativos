package br.com.brunov.controla_ativos.controla_ativos.config.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
}
