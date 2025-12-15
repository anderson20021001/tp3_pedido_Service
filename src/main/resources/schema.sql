CREATE TABLE pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(255),
    valor DOUBLE
);

CREATE TABLE outbox_event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP,
    aggregate_id BIGINT,
    event_type VARCHAR(50),
    payload CLOB,
    status VARCHAR(20),
    event_version VARCHAR(10)
);
