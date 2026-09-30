package com.corhuila.edutrack.academic.domain.exception;

/**
 * Raised when a domain event cannot be serialized into the outbox payload.
 * Being unchecked, it rolls back the surrounding business transaction.
 */
public class OutboxSerializationException extends RuntimeException {

    public OutboxSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}