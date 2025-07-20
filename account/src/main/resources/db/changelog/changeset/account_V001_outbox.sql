CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE notification_outbox (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    login TEXT NOT NULL,
    message TEXT NOT NULL,
    delivered BOOLEAN NOT NULL DEFAULT FALSE
);
