BEGIN;

CREATE TABLE IF NOT EXISTS sports(
  sportName       VARCHAR(100) PRIMARY KEY,
  playerQuantity  INTEGER,
  displayName     VARCHAR(100)
)/;

CREATE TABLE IF NOT EXISTS users(
  userId    SERIAL PRIMARY KEY,
  email     VARCHAR(100) NOT NULL,
  firstName VARCHAR(100) NOT NULL,
  lastName  VARCHAR(100) NOT NULL
)/;

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
  code        VARCHAR(100) NOT NULL,
  image       BYTEA,
  UNIQUE(email)
)/;

CREATE TABLE IF NOT EXISTS friendOf(
  userName        VARCHAR(100) NOT NULL,
  friendsUserName VARCHAR(100) NOT NULL,
  FOREIGN KEY (userName) REFERENCES accounts(userName),
  FOREIGN KEY (friendsUserName) REFERENCES accounts(userName),
  PRIMARY KEY (userName, friendsUserName)
)/;

CREATE TABLE IF NOT EXISTS notification(
  startTime TIMESTAMP,
  content   VARCHAR(100),
  seen      INTEGER,
  userName  VARCHAR(100),
  FOREIGN KEY (userName) REFERENCES accounts(userName) ON DELETE CASCADE ON UPDATE CASCADE,
  PRIMARY KEY (startTime, content)
)/;

CREATE TABLE IF NOT EXISTS likes(
  userName  VARCHAR(100),
  sportName VARCHAR(100),
  FOREIGN KEY (userName) REFERENCES accounts(userName),
  FOREIGN KEY (sportName) REFERENCES sports(sportName),
  PRIMARY Key (userName, sportName)
)/;

CREATE TABLE IF NOT EXISTS teams(
  teamName    VARCHAR(100) PRIMARY KEY,
  acronym     VARCHAR(100),
  leaderName  VARCHAR(100) REFERENCES accounts(userName),
  isTemp      INTEGER NOT NULL,
  sportName   VARCHAR (100) NOT NULL,
  image       BYTEA,
  FOREIGN KEY (sportName) REFERENCES sports(sportName)
  --Filters--
)/;

CREATE TABLE IF NOT EXISTS isPartOf (
  userId    INTEGER NOT NULL,
  teamName  VARCHAR(100) NOT NULL,
  PRIMARY KEY (userid, teamName),
  FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (teamName) REFERENCES teams(teamName) ON DELETE CASCADE ON UPDATE CASCADE
)/;

CREATE TABLE IF NOT EXISTS tornaments(
  tornamentName VARCHAR(100) PRIMARY KEY,
  type          VARCHAR(100),
  image         BYTEA
)/;

CREATE TABLE IF NOT EXISTS roles(
  roleId         INTEGER PRIMARY KEY,
  roleName      VARCHAR(100) NOT NULL
)/;

CREATE TABLE IF NOT EXISTS userRoles(
  role          INTEGER NOT NULL,
  username      VARCHAR(100),
  FOREIGN KEY (role) REFERENCES roles(roleId) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (username) REFERENCES accounts(username) ON DELETE CASCADE ON UPDATE CASCADE
)/;

CREATE TABLE IF NOT EXISTS games (
  teamName1     VARCHAR(100) NOT NULL,
  teamName2     VARCHAR(100),
  startTime     TIMESTAMP NOT NULL,
  finishTime    TIMESTAMP NOT NULL,
  type          VARCHAR(100) NOT NULL,
  result        VARCHAR(100),
  country       VARCHAR(100) NOT NULL,
  state         VARCHAR(100) NOT NULL,
  city          VARCHAR(100),
  street        VARCHAR(100),
  tornamentName VARCHAR(100),
  description   VARCHAR(140),
  title         VARCHAR(100),
  FOREIGN KEY (teamName1) REFERENCES teams(teamName),
  FOREIGN KEY (teamName2) REFERENCES teams(teamName),
  FOREIGN KEY (tornamentName) REFERENCES tornaments(tornamentName) ON DELETE CASCADE ON UPDATE CASCADE,
  PRIMARY KEY (teamName1, startTime, finishTime)
)/;

DROP TRIGGER IF EXISTS checkTeamInterval ON teams/;
DROP FUNCTION IF EXISTS checkTeam() CASCADE /;

CREATE OR REPLACE FUNCTION checkTeam() RETURNS Trigger AS $$
DECLARE
startTimeAux    TIMESTAMP;
finishTimeAux   TIMESTAMP;
teamName1Aux    VARCHAR(100);
teamName2Aux    VARCHAR(100);

cursorTeam CURSOR FOR
SELECT teamName1, teamName2, startTime, finishTime
FROM games
WHERE teamName1 = new.teamName1 OR teamName2 = new.teamName1;

BEGIN
OPEN cursorTeam;

LOOP
    FETCH cursorTeam INTO startTimeAux, finishTimeAux, teamName1Aux, teamName2Aux;
    EXIT WHEN NOT FOUND;
    IF (new.teamName1 = teamName1Aux OR new.teamName1 = teamName2Aux) THEN
        IF (new.startTime <= startTimeAux AND startTimeAux <= new.finishTime) OR
           (new.startTime <= finishTimeAux AND finishTimeAux <= new.finishTime) OR
           (new.startTime >= startTimeAux AND finishTimeAux >= new.finishTime) THEN
            Raise exception 'team 1 already play in at that time' USING ERRCODE = 'PP111';
        END IF;
    ELSIF (new.teamName1 IS NOT NULL AND
           (new.teamName2 = teamName1Aux OR new.teamName2 = teamName2Aux)) THEN
        IF (new.startTime <= startTimeAux AND startTimeAux <= new.finishTime) OR
           (new.startTime <= finishTimeAux AND finishTimeAux <= new.finishTime) OR
           (new.startTime >= startTimeAux AND finishTimeAux >= new.finishTime) THEN
            Raise exception 'team 2 already play in at that time' USING ERRCODE = 'PP111';
        END IF;
    END IF;

END LOOP;

CLOSE cursorTeam;
RETURN new;
END;
$$ LANGUAGE plpgsql/;

CREATE TRIGGER checkTeamInterval BEFORE INSERT OR UPDATE OF teamName1, teamName2 ON games
FOR EACH ROW
EXECUTE PROCEDURE checkTeam()/;

COMMIT;