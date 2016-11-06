-- Create DB
DROP   database IF EXISTS gun;
CREATE database gun;
USE gun;

-- Create user
DROP USER IF EXISTS	'scott'@'localhost' 
CREATE USER 'scott'@'localhost' IDENTIFIED BY 'tiger';
GRANT ALL ON *.* TO 'scott'@'localhost';


-- Create Tables
-- Players
DROP TABLE IF EXISTS Players;
CREATE TABLE Players(id numeric not null, name varchar(25) unique, constraint pkPlayer primary key (id));

-- Games
DROP TABLE IF EXISTS Games;
CREATE TABLE Games(id numeric not null, player numeric, level numeric, mode varchar(10), startTime timestamp, score numeric, constraint pkGame primary key (id), constraint fkPlayer foreign key (player) references Players(id));

-- Game Events
DROP TABLE IF EXISTS Events;
CREATE TABLE Events(game numeric, eventType varchar(25), eventTime timestamp, constraint pkEvent primary key (game, eventType, eventTime), constraint fkGame foreign key (game) references Games(id));

-- Insert data
COMMIT;