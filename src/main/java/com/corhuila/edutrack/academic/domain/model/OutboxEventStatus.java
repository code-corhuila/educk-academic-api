package com.corhuila.edutrack.academic.domain.model;

/** Lifecycle of an event stored in the outbox. */
public enum OutboxEventStatus {

    /** Persisted and waiting to be published to the broker. */
    PENDING,

    /** Successfully published to the broker. */
    PROCESSED
}