CREATE TABLE appointments
(
    appointment_id SERIAL PRIMARY KEY NOT NULL,
    correlation_id VARCHAR(50),
    customer_id    INT                NOT NULL,
    time_slot_id   INT                NOT NULL,
    created        TIMESTAMP
);
CREATE INDEX appointments_correlation_key_idx ON appointments (correlation_id);

CREATE TABLE appointment_events
(
    appointment_event_id SERIAL PRIMARY KEY NOT NULL,
    appointment_id       INT                NOT NULL,
    status               VARCHAR(50)        NOT NULL,
    description          VARCHAR,
    created              TIMESTAMP          NOT NULL,
    FOREIGN KEY (appointment_id) REFERENCES appointments (appointment_id)
);
CREATE INDEX appointment_events_appointment_id_idx ON appointment_events (appointment_id);

CREATE TABLE appointment_event_status
(
    name           VARCHAR NOT NULL,
    order_priority INT     NOT NULL
);

INSERT INTO appointment_event_status
VALUES ('CREATED', 0),
       ('IN_PROGRESS', 5),
       ('CONFIRMED', 10),
       ('CANCELED', 20),
       ('CLOSED', 30)
