-- Step 1: Add a new UUID column
ALTER TABLE users ADD COLUMN id_new UUID DEFAULT gen_random_uuid();

-- Step 2: Populate the new column
UPDATE users SET id_new = gen_random_uuid();

-- Step 3: Drop constraints on the old column
ALTER TABLE users DROP CONSTRAINT users_pkey;

-- Step 4: Rename the old column
ALTER TABLE users RENAME COLUMN id TO id_old;

-- Step 5: Rename the new column
ALTER TABLE users RENAME COLUMN id_new TO id;

-- Step 6: Add constraints to the new column
ALTER TABLE users ADD CONSTRAINT users_pkey PRIMARY KEY (id);

-- Step 7: Update foreign key references (if necessary)
-- Example: ALTER TABLE another_table ...

-- Step 8: Drop the old column (optional)
ALTER TABLE users DROP COLUMN id_old;
