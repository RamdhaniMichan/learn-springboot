-- Rename the column
ALTER TABLE users
RENAME COLUMN name TO username;

-- Add new columns
ALTER TABLE users
ADD COLUMN firstname VARCHAR(50),
ADD COLUMN lastname VARCHAR(50),
ADD COLUMN token_activated VARCHAR(10),
ADD COLUMN attempt_activated INTEGER,
ADD COLUMN status BOOL DEFAULT FALSE,
ADD COLUMN created_at TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP,
ADD COLUMN deleted_at TIMESTAMP;