drop   database if exists javanet;
create database javanet;

grant all privileges on *.* to 'scott'@'localhost' identified by 'tiger';

use javanet;

drop table if exists Score;

create table Score (
  clientId char(4) not null, 
  name varchar(25) unique, 
  level varchar(10), 
  startTime date, 
  event varchar(25), 
  currnetscore double, 
  constraint pkScore primary key (clientId));