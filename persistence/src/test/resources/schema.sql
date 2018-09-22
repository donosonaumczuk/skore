CREATE TABLE IF NOT EXISTS users(
    userid INTEGER IDENTITY PRIMARY KEY,
    email varchar(100) NOT NULL,
    firstName varchar(100),
    lastName varchar(100)
);

CREATE TABLE IF NOT EXISTS accounts(
  userName    VARCHAR(100) PRIMARY KEY,
  userId      INTEGER REFERENCES users(userId) NOT NULL,
  password    VARCHAR(100),
  country     VARCHAR(100),
  state       VARCHAR(100),
  city        VARCHAR(100),
  street      VARCHAR(100),
  reputation  INTEGER,
  cellphone   VARCHAR(100),
  birthday    DATE,
  UNIQUE(userId)
);
