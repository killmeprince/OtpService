CREATE TABLE users (
   id            SERIAL PRIMARY KEY,
   username      VARCHAR(50) UNIQUE NOT NULL,
   password_hash VARCHAR(100) NOT NULL,
   role          VARCHAR(20)    NOT NULL
);
CREATE TABLE otp_config (
    id          BOOLEAN PRIMARY KEY DEFAULT TRUE CHECK (id),
    code_length INT     NOT NULL DEFAULT 6,
    ttl_seconds INT     NOT NULL DEFAULT 300
);
INSERT INTO otp_config (id, code_length, ttl_seconds)
VALUES (TRUE, 6, 300);
