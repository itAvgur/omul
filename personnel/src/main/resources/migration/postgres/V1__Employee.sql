CREATE TABLE employees
(
    employee_id   SERIAL PRIMARY KEY NOT NULL,
    first_name    VARCHAR(100),
    last_name     VARCHAR(100),
    gender        VARCHAR(10),
    birth_date    TIMESTAMP,
    document_id   VARCHAR(100),
    email         VARCHAR(100) UNIQUE,
    phone         VARCHAR(100),
    qualification VARCHAR            NOT NULL
);
CREATE INDEX customers_email_idx ON employees (email);