CREATE TABLE IF NOT EXISTS sports(
  sportName       VARCHAR(100) PRIMARY KEY,
  playerQuantity  INTEGER
);

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

CREATE TABLE IF NOT EXISTS teams(
  teamName    VARCHAR(100) PRIMARY KEY,
  acronym     VARCHAR(100),
  leaderId    INTEGER REFERENCES users(userId),
  isTemp      INTEGER NOT NULL,
  sportName   VARCHAR (100) NOT NULL,
  FOREIGN KEY (sportName) REFERENCES sports(sportName)
  --Filters--
);

CREATE TABLE IF NOT EXISTS isPartOf (
  userId    INTEGER NOT NULL,
  teamName  VARCHAR(100) NOT NULL,
  PRIMARY KEY (userid, teamName),
  FOREIGN KEY (userId) REFERENCES users(userId),
  FOREIGN KEY (teamName) REFERENCES teams(teamName)
);

CREATE TABLE IF NOT EXISTS tornaments(
  tornamentName VARCHAR(100) PRIMARY KEY,
  type          VARCHAR(100)
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
  FOREIGN KEY (teamName1) REFERENCES teams(teamName),
  FOREIGN KEY (teamName2) REFERENCES teams(teamName),
  FOREIGN KEY (tornamentName) REFERENCES tornaments(tornamentName),
  PRIMARY KEY (teamName1, teamName2, startTime, finishTime)
);
