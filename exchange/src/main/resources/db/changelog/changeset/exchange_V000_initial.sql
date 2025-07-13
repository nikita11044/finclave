CREATE TABLE IF NOT EXISTS exchange_rates (
    id BIGSERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    code TEXT NOT NULL,
    rate NUMERIC(19, 4) NOT NULL,
    base BOOLEAN NOT NULL DEFAULT FALSE
);

INSERT INTO exchange_rates (title, code, rate, base)
VALUES
    ('Рубли', 'RUB', 1, true),
    ('Доллары', 'USD', 78.00, false),
    ('Юани', 'CNY', 10.88, false);
