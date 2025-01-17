-- Step 1: Add a new UUID column with a default value
ALTER TABLE users ADD COLUMN id_new UUID DEFAULT gen_random_uuid();

-- Step 2: Populate the new column with deterministic UUIDs
UPDATE users
SET id_new = gen_random_uuid();

-- Step 3: Drop the primary key constraint on the old column
ALTER TABLE users DROP CONSTRAINT users_pkey;

-- Step 4: Rename columns
ALTER TABLE users RENAME COLUMN id TO id_old;
ALTER TABLE users RENAME COLUMN id_new TO id;

-- Step 5: Add the primary key constraint to the new column
ALTER TABLE users ADD CONSTRAINT users_pkey PRIMARY KEY (id);

-- Step 6: Add the new column `password`
ALTER TABLE users ADD COLUMN password VARCHAR(50);

-- Step 7: (Optional) Drop the old `id` column
ALTER TABLE users DROP COLUMN id_old;
