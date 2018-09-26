BEGIN;

CREATE TABLE IF NOT EXISTS sports(
  sportName       VARCHAR(100) PRIMARY KEY,
  playerQuantity  INTEGER
);

CREATE TABLE IF NOT EXISTS users(
  userId    SERIAL PRIMARY KEY,
  email     VARCHAR(100) NOT NULL,
  firstName VARCHAR(100),
  lastName  VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS accounts(
  userName    VARCHAR(100) PRIMARY KEY,
  userId      INTEGER REFERENCES users(userId) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
  password    VARCHAR(100),
  country     VARCHAR(100),
  state       VARCHAR(100),
  city        VARCHAR(100),
  street      VARCHAR(100),
  reputation  INTEGER,
  cellphone   VARCHAR(100),
  birthday    DATE,
  image       BYTEA,
  UNIQUE(userId)
);

CREATE TABLE IF NOT EXISTS friendOf(
  userName        VARCHAR(100) NOT NULL,
  friendsUserName VARCHAR(100) NOT NULL,
  FOREIGN KEY (userName) REFERENCES accounts(userName),
  FOREIGN KEY (friendsUserName) REFERENCES accounts(userName),
  PRIMARY KEY (userName, friendsUserName)
);

CREATE TABLE IF NOT EXISTS notification(
  startTime TIMESTAMP,
  content   VARCHAR(100),
  seen      INTEGER,
  userName  VARCHAR(100),
  FOREIGN KEY (userName) REFERENCES accounts(userName) ON DELETE CASCADE ON UPDATE CASCADE,
  PRIMARY KEY (startTime, content)
);

CREATE TABLE IF NOT EXISTS likes(
  userName  VARCHAR(100),
  sportName VARCHAR(100),
  FOREIGN KEY (userName) REFERENCES accounts(userName),
  FOREIGN KEY (sportName) REFERENCES sports(sportName),
  PRIMARY Key (userName, sportName)
);

CREATE TABLE IF NOT EXISTS teams(
  teamName    VARCHAR(100) PRIMARY KEY,
  acronym     VARCHAR(100),
  leaderName  VARCHAR(100) REFERENCES accounts(userName) NOT NULL,
  isTemp      INTEGER NOT NULL,
  sportName   VARCHAR (100) NOT NULL,
  image       BYTEA,
  FOREIGN KEY (sportName) REFERENCES sports(sportName)
  --Filters--
);

CREATE TABLE IF NOT EXISTS isPartOf (
  userId    INTEGER NOT NULL,
  teamName  VARCHAR(100) NOT NULL,
  PRIMARY KEY (userid, teamName),
  FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (teamName) REFERENCES teams(teamName) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS tornaments(
  tornamentName VARCHAR(100) PRIMARY KEY,
  type          VARCHAR(100),
  image         BYTEA
);

CREATE TABLE IF NOT EXISTS games (
  teamName1     VARCHAR(100) NOT NULL,
  teamName2     VARCHAR(100) NOT NULL,
  startTime     TIMESTAMP NOT NULL,
  finishTime    TIMESTAMP NOT NULL,
  type          VARCHAR(100) NOT NULL,
  result        VARCHAR(100) NOT NULL,
  country       VARCHAR(100) NOT NULL,
  state         VARCHAR(100) NOT NULL,
  city          VARCHAR(100) NOT NULL,
  street        VARCHAR(100) NOT NULL,
  tornamentName VARCHAR(100),
  description   VARCHAR(140),
  FOREIGN KEY (teamName1) REFERENCES teams(teamName),
  FOREIGN KEY (teamName2) REFERENCES teams(teamName),
  FOREIGN KEY (tornamentName) REFERENCES tornaments(tornamentName) ON DELETE CASCADE ON UPDATE CASCADE,
  PRIMARY KEY (teamName1, teamName2, startTime, finishTime)
);

COMMIT;