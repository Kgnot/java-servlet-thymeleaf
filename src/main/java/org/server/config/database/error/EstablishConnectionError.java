package org.server.config.database.error;

public class EstablishConnectionError extends RuntimeException {
    public EstablishConnectionError(String message) {
        super(message);
    }
}
