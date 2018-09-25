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
  FOREIGN KEY (userName) REFERENCES accounts(userName),
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
  teamName    VARCHAR(100) NOT NULL,
  acronym     VARCHAR(100) NOT NULL,
  leaderId    INTEGER REFERENCES users(userId),
  isTemp      INTEGER NOT NULL,
  sportName   VARCHAR (100) NOT NULL,
  PRIMARY KEY (teamName, sportName),
  FOREIGN KEY (sportName) REFERENCES sports(sportName)
  --Filters--
);

CREATE TABLE IF NOT EXISTS isPartOf (
  userId    INTEGER NOT NULL,
  teamName  VARCHAR(100) NOT NULL,
  sportName VARCHAR(100) NOT NULL,
  PRIMARY KEY (userid, teamName, sportName),
  FOREIGN KEY (userId) REFERENCES users(userId),
  FOREIGN KEY (teamName, sportName) REFERENCES teams(teamName, sportName)
);

CREATE TABLE IF NOT EXISTS tournaments(
  tournamentName VARCHAR(100) PRIMARY KEY,
  type          VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS games (
  teamName1     VARCHAR(100) NOT NULL,
  teamName2     VARCHAR(100) NOT NULL,
  sportName     VARCHAR(100) NOT NULL,
  startTime     TIMESTAMP NOT NULL,
  finishTime    TIMESTAMP NOT NULL,
  type          VARCHAR(100) NOT NULL,
  result        VARCHAR(100) NOT NULL,
  country       VARCHAR(100) NOT NULL,
  state         VARCHAR(100) NOT NULL,
  city          VARCHAR(100) NOT NULL,
  street        VARCHAR(100) NOT NULL,
  tournamentName VARCHAR(100),
  FOREIGN KEY (teamName1, sportName) REFERENCES teams(teamName, sportName),
  FOREIGN KEY (teamName2, sportName) REFERENCES teams(teamName, sportName),
  FOREIGN KEY (tournamentName) REFERENCES tournaments(tournamentName),
  PRIMARY KEY (teamName1, teamName2, sportName, startTime, finishTime)
);

COMMIT;