CREATE TABLE IF NOT EXISTS sports(
  sportName       VARCHAR(100) PRIMARY KEY,
  playerQuantity  INTEGER
);

CREATE TABLE IF NOT EXISTS users(
    userId INTEGER IDENTITY PRIMARY KEY,
    email varchar(100) NOT NULL,
    firstName varchar(100),
    lastName varchar(100)
);

CREATE TABLE IF NOT EXISTS accounts(
  userName    VARCHAR(100) PRIMARY KEY,
  userId      INTEGER REFERENCES users(userId) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
  password    VARCHAR(100) NOT NULL,
  country     VARCHAR(100),
  state       VARCHAR(100),
  city        VARCHAR(100),
  street      VARCHAR(100),
  reputation  INTEGER,
  cellphone   VARCHAR(100),
  birthday    DATE,
  email       VARCHAR (100) NOT NULL,
  role        VARCHAR (100),
  enabled     INTEGER,
  image       BLOB,
  UNIQUE(email)
);

CREATE TABLE IF NOT EXISTS notification(
  startTime TIMESTAMP,
  content   VARCHAR(100),
  seen      INTEGER,
  userName  VARCHAR(100),
  FOREIGN KEY (userName) REFERENCES accounts(userName) ON DELETE CASCADE ON UPDATE CASCADE,
  PRIMARY KEY (startTime, content)
);

CREATE TABLE IF NOT EXISTS teams(
  teamName    VARCHAR(100) PRIMARY KEY,
  acronym     VARCHAR(100),
  leaderName  VARCHAR(100) REFERENCES accounts(userName) NOT NULL,
  isTemp      INTEGER NOT NULL,
  sportName   VARCHAR (100) NOT NULL,
  image       BLOB,
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
  image         BLOB
);

CREATE TABLE IF NOT EXISTS roles(
  roleId         INTEGER PRIMARY KEY,
  roleName      VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS userRoles(
  role          INTEGER NOT NULL,
  username      VARCHAR(100),
  FOREIGN KEY (role) REFERENCES roles(roleId) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (username) REFERENCES accounts(username) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS games (
  teamName1     VARCHAR(100) NOT NULL,
  teamName2     VARCHAR(100),
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
  title         VARCHAR(100),
  FOREIGN KEY (teamName1) REFERENCES teams(teamName),
  FOREIGN KEY (teamName2) REFERENCES teams(teamName),
  FOREIGN KEY (tornamentName) REFERENCES tornaments(tornamentName) ON DELETE CASCADE ON UPDATE CASCADE,
  PRIMARY KEY (teamName1, startTime, finishTime)
);
