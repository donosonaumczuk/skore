DROP SCHEMA PUBLIC CASCADE;

CREATE SEQUENCE users_userid_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE jwtIdSeq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE;

CREATE TABLE IF NOT EXISTS Blacklist(
  id INT PRIMARY KEY,
  token VARCHAR(500) NOT NULL,
  expiry TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS sports(
  sportName       VARCHAR(100) PRIMARY KEY,
  playerQuantity  INTEGER,
  displayName     VARCHAR(100),
  imageSport      BLOB
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
  email       VARCHAR(100),
  password    VARCHAR(100) NOT NULL,
  country     VARCHAR(100),
  state       VARCHAR(100),
  city        VARCHAR(100),
  street      VARCHAR(100),
  reputation  INTEGER,
  cellphone   VARCHAR(100),
  birthday    DATE,
  role        VARCHAR (100),
  enabled     BOOLEAN,
  code        VARCHAR(100) NOT NULL,
  image       BLOB,
  UNIQUE(email)
);

CREATE TABLE IF NOT EXISTS notification(
  startTime TIMESTAMP,
  content   VARCHAR(100),
  seen      INTEGER,
  userName  VARCHAR(100),
  FOREIGN KEY (userName) REFERENCES accounts(userName) ON DELETE CASCADE ON UPDATE CASCADE,
  PRIMARY KEY (startTime, content, userName)
);

CREATE TABLE IF NOT EXISTS teams(
  teamName    VARCHAR(100) PRIMARY KEY,
  acronym     VARCHAR(100),
  leaderName  VARCHAR(100) REFERENCES accounts(userName) ON DELETE CASCADE ON UPDATE CASCADE,
  isTemp      INTEGER NOT NULL,
  sportName   VARCHAR (100) NOT NULL,
  image       BLOB,
  FOREIGN KEY (sportName) REFERENCES sports(sportName) ON DELETE CASCADE ON UPDATE CASCADE
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
  result        VARCHAR(100),
  country       VARCHAR(100) NOT NULL,
  state         VARCHAR(100) NOT NULL,
  city          VARCHAR(100) NOT NULL,
  street        VARCHAR(100) NOT NULL,
  tornamentName VARCHAR(100),
  description   VARCHAR(140),
  title         VARCHAR(100),
  FOREIGN KEY (teamName1) REFERENCES teams(teamName) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (teamName2) REFERENCES teams(teamName) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (tornamentName) REFERENCES tornaments(tornamentName) ON DELETE CASCADE ON UPDATE CASCADE,
  PRIMARY KEY (teamName1, startTime, finishTime)
);

CREATE TABLE IF NOT EXISTS likes (
  userName  VARCHAR(100),
  sportName VARCHAR(100),
  FOREIGN KEY (userName) REFERENCES accounts(userName),
  FOREIGN KEY (sportName) REFERENCES sports(sportName),
  PRIMARY Key (userName, sportName)
);

CREATE TABLE IF NOT EXISTS friendOf(
  userName        VARCHAR(100) NOT NULL,
  friendsUserName VARCHAR(100) NOT NULL,
  FOREIGN KEY (userName) REFERENCES accounts(userName),
  FOREIGN KEY (friendsUserName) REFERENCES accounts(userName),
  PRIMARY KEY (userName, friendsUserName)
);

