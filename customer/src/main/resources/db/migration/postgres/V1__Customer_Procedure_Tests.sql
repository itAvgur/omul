CREATE TABLE customers
(
    customer_id SERIAL PRIMARY KEY NOT NULL,
    first_name  VARCHAR(100),
    last_name   VARCHAR(100),
    gender      VARCHAR(10),
    birth_date  TIMESTAMP,
    document_id VARCHAR(100),
    email       VARCHAR(100) UNIQUE,
    phone       VARCHAR(100)
);
CREATE INDEX customers_email_idx ON customers (email);

CREATE TABLE medical_procedures
(
    procedure_id SERIAL PRIMARY KEY NOT NULL,
    customer_id  INT                NOT NULL,
    summary      VARCHAR(100)       NOT NULL,
    description  VARCHAR,
    date         TIMESTAMP          NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (customer_id)
);
CREATE INDEX medical_procedures_customer_id_idx ON medical_procedures (customer_id);

CREATE TABLE medical_tests
(
    test_id     SERIAL PRIMARY KEY NOT NULL,
    customer_id INT                NOT NULL,
    summary     VARCHAR(100)       NOT NULL,
    description VARCHAR,
    date        TIMESTAMP          NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (customer_id)
);
CREATE INDEX medical_tests_customer_id_idx ON medical_tests (customer_id);

CREATE TABLE medical_test_results
(
    test_result_id SERIAL PRIMARY KEY NOT NULL,
    test_id        INT                NOT NULL,
    result         VARCHAR,
    date           TIMESTAMP          NOT NULL,
    FOREIGN KEY (test_id) REFERENCES medical_tests (test_id)
);
CREATE INDEX medical_procedures_test_id_idx ON medical_test_results (test_id);
