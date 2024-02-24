CREATE TABLE workflow
(
    workflow_id    SERIAL PRIMARY KEY NOT NULL,
    correlation_id VARCHAR            NOT NULL,
    name           VARCHAR(100)       NOT NULL,
    status         VARCHAR(50)        NOT NULL,
    context        VARCHAR
);
CREATE INDEX workflow_status_idx ON workflow (status, correlation_id);
