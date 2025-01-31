CREATE TABLE roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(15),
    name VARCHAR(25)
);

INSERT INTO roles (code, name)
VALUES ('admin', 'ADMIN'),
 ('user', 'USER');