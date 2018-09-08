CREATE TABLE IF NOT EXISTS users(
    userid INTEGER IDENTITY PRIMARY KEY,
    email varchar(100) NOT NULL,
    firstName varchar(100),
    lastName varchar(100)
);

