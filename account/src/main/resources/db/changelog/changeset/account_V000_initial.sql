CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    login TEXT NOT NULL,
    password TEXT NOT NULL,
    name TEXT NOT NULL,
    birthdate DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS currencies (
    id BIGSERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    code VARCHAR(3) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS accounts (
    id BIGSERIAL PRIMARY KEY,
    currency_id BIGINT NOT NULL REFERENCES currencies(id),
    balance NUMERIC(19, 4) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id),
    UNIQUE (currency_id, user_id)
);

INSERT INTO currencies (title, code)
VALUES
    ('Рубли', 'RUB'),
    ('Доллары', 'USD'),
    ('Юани', 'CNY');
