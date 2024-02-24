CREATE SEQUENCE time_slot_seq START WITH 0 MINVALUE 0;
CREATE TABLE time_slots
(
    slot_id         INT PRIMARY KEY NOT NULL DEFAULT nextval('time_slot_seq'),
    customer_id     INT,
    doctor_id       INT,
    date_time_start TIMESTAMP       NOT NULL,
    date_time_end   TIMESTAMP       NOT NULL,
    status          VARCHAR(25)     NOT NULL
);
