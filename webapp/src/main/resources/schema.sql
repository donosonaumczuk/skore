BEGIN;
--users table
CREATE TABLE IF NOT EXISTS users(
  userid SERIAL PRIMARY KEY,
  username varchar(100)
  );

COMMIT;