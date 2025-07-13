CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    login TEXT NOT NULL,
    password TEXT NOT NULL,
    name TEXT NOT NULL,
    birthdate DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts (
    id BIGSERIAL PRIMARY KEY,
    currency TEXT NOT NULL,
    balance BIGINT NOT NULL,
    user_id BIGINT REFERENCES users(id),
    UNIQUE (currency, user_id)
);
