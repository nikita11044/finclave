CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    notification_id UUID NOT NULL,
    login TEXT NOT NULL,
    message TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_notifications_login ON notifications (login);
