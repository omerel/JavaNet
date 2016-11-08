-- Create DB
DROP   database IF EXISTS gun;
CREATE database gun;
USE gun;

-- Create user
DROP USER IF EXISTS	'scott'@'localhost';
CREATE USER 'scott'@'localhost' IDENTIFIED BY 'tiger';
GRANT ALL ON *.* TO 'scott'@'localhost';

--
-- Table structure for table `players`
--

DROP TABLE IF EXISTS `players`;
CREATE TABLE `players` (
  `id` decimal(10,0) NOT NULL,
  `name` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
);

--
-- Dumping data for table `players`
--

LOCK TABLES `players` WRITE;
INSERT INTO `players` VALUES (1,'player1'),(2,'player2'),(3,'player3'),(4,'player4');
UNLOCK TABLES;

--
-- Table structure for table `games`
--

DROP TABLE IF EXISTS `games`;

CREATE TABLE `games` (
  `id` decimal(10,0) NOT NULL,
  `player` decimal(10,0) DEFAULT NULL,
  `level` varchar(10) DEFAULT NULL,
  `mode` varchar(10) DEFAULT NULL,
  `startTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `score` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fkPlayer` (`player`),
  CONSTRAINT `fkPlayer` FOREIGN KEY (`player`) REFERENCES `players` (`id`)
);

--
-- Dumping data for table `games`
--

LOCK TABLES `games` WRITE;
INSERT INTO `games` VALUES (1,1,'Beginner','Game','2016-11-06 15:25:48',32),(2,1,'Beginner','Game','2016-11-06 15:27:52',40),(3,1,'Beginner','Game','2016-11-06 15:36:35',14),(4,1,'Normal','Game','2016-11-06 20:28:17',22),(5,1,'Hard','Game','2016-11-06 20:32:30',26),(6,2,'Beginner','Training','2016-11-07 03:51:22',29),(7,3,'Normal','Game','2016-11-08 21:05:54',14),(8,3,'Normal','Game','2016-11-08 21:08:08',21),(9,4,'Beginner','Game','2016-11-08 21:08:52',22),(10,4,'Beginner','Game','2016-11-08 21:10:57',36),(11,4,'Beginner','Game','2016-11-08 21:11:26',4),(12,3,'Normal','Game','2016-11-08 21:12:47',13),(13,4,'Beginner','Game','2016-11-08 21:20:54',38),(14,4,'Beginner','Game','2016-11-08 21:34:02',20);
UNLOCK TABLES;

-- Table structure for table `events`
--
DROP TABLE IF EXISTS `events`;

CREATE TABLE `events` (
  `id` decimal(10,0) NOT NULL,
  `game` decimal(10,0) DEFAULT NULL,
  `eventType` varchar(25) DEFAULT NULL,
  `eventTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `message` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fkGame` (`game`),
  CONSTRAINT `fkGame` FOREIGN KEY (`game`) REFERENCES `games` (`id`)
);

--
-- Dumping data for table `events`
--

LOCK TABLES `events` WRITE;
INSERT INTO `events` VALUES (1,1,'START GAME','2016-11-06 15:23:48','Game started'),(2,1,'NEW HIT','2016-11-06 15:23:52','Current score is 1'),(3,1,'NEW HIT','2016-11-06 15:23:55','Current score is 2'),(4,1,'NEW HIT','2016-11-06 15:23:55','Current score is 3'),(5,1,'NEW HIT','2016-11-06 15:24:00','Current score is 4'),(6,1,'NEW HIT','2016-11-06 15:24:04','Current score is 5'),(7,1,'NEW HIT','2016-11-06 15:24:09','Current score is 6'),(8,1,'NEW HIT','2016-11-06 15:24:14','Current score is 7'),(9,1,'NEW HIT','2016-11-06 15:24:15','Current score is 8'),(10,1,'NEW HIT','2016-11-06 15:24:15','Current score is 9'),(11,1,'NEW HIT','2016-11-06 15:24:20','Current score is 10'),(12,1,'NEW HIT','2016-11-06 15:24:23','Current score is 11'),(13,1,'NEW HIT','2016-11-06 15:24:28','Current score is 12'),(14,1,'NEW HIT','2016-11-06 15:24:32','Current score is 13'),(15,1,'NEW HIT','2016-11-06 15:24:37','Current score is 14'),(16,1,'NEW HIT','2016-11-06 15:24:40','Current score is 15'),(17,1,'NEW HIT','2016-11-06 15:24:44','Current score is 16'),(18,1,'NEW HIT','2016-11-06 15:24:52','Current score is 17'),(19,1,'NEW HIT','2016-11-06 15:24:56','Current score is 18'),(20,1,'NEW HIT','2016-11-06 15:24:57','Current score is 19'),(21,1,'NEW HIT','2016-11-06 15:24:57','Current score is 20'),(22,1,'NEW HIT','2016-11-06 15:25:01','Current score is 21'),(23,1,'NEW HIT','2016-11-06 15:25:01','Current score is 22'),(24,1,'NEW HIT','2016-11-06 15:25:05','Current score is 23'),(25,1,'NEW HIT','2016-11-06 15:25:08','Current score is 24'),(26,1,'NEW HIT','2016-11-06 15:25:12','Current score is 25'),(27,1,'NEW HIT','2016-11-06 15:25:12','Current score is 26'),(28,1,'NEW HIT','2016-11-06 15:25:16','Current score is 27'),(29,1,'NEW HIT','2016-11-06 15:25:24','Current score is 28'),(30,1,'NEW HIT','2016-11-06 15:25:28','Current score is 29'),(31,1,'NEW HIT','2016-11-06 15:25:33','Current score is 30'),(32,1,'NEW HIT','2016-11-06 15:25:38','Current score is 31'),(33,1,'NEW HIT','2016-11-06 15:25:43','Current score is 32'),(34,1,'GAME FINISHED','2016-11-06 15:25:48','Final score is 32'),(35,1,'NEW HIT','2016-11-06 15:25:48','Current score is 33'),(36,2,'START GAME','2016-11-06 15:25:52','Game started'),(37,2,'NEW HIT','2016-11-06 15:25:56','Current score is 1'),(38,2,'NEW HIT','2016-11-06 15:25:58','Current score is 2'),(39,2,'NEW HIT','2016-11-06 15:26:01','Current score is 3'),(40,2,'NEW HIT','2016-11-06 15:26:02','Current score is 4'),(41,2,'NEW HIT','2016-11-06 15:26:02','Current score is 5'),(42,2,'NEW HIT','2016-11-06 15:26:07','Current score is 6'),(43,2,'NEW HIT','2016-11-06 15:26:09','Current score is 7'),(44,2,'NEW HIT','2016-11-06 15:26:13','Current score is 9'),(45,2,'NEW HIT','2016-11-06 15:26:13','Current score is 9'),(46,2,'NEW HIT','2016-11-06 15:26:17','Current score is 10'),(47,2,'NEW HIT','2016-11-06 15:26:17','Current score is 11'),(48,2,'NEW HIT','2016-11-06 15:26:20','Current score is 12'),(49,2,'NEW HIT','2016-11-06 15:26:24','Current score is 14'),(50,2,'NEW HIT','2016-11-06 15:26:24','Current score is 14'),(51,2,'NEW HIT','2016-11-06 15:26:33','Current score is 15'),(52,2,'NEW HIT','2016-11-06 15:26:36','Current score is 16'),(53,2,'NEW HIT','2016-11-06 15:26:36','Current score is 17'),(54,2,'NEW HIT','2016-11-06 15:26:41','Current score is 19'),(55,2,'NEW HIT','2016-11-06 15:26:41','Current score is 19'),(56,2,'NEW HIT','2016-11-06 15:26:44','Current score is 20'),(57,2,'NEW HIT','2016-11-06 15:26:48','Current score is 21'),(58,2,'NEW HIT','2016-11-06 15:26:55','Current score is 22'),(59,2,'NEW HIT','2016-11-06 15:27:01','Current score is 23'),(60,2,'NEW HIT','2016-11-06 15:27:05','Current score is 24'),(61,2,'NEW HIT','2016-11-06 15:27:07','Current score is 25'),(62,2,'NEW HIT','2016-11-06 15:27:10','Current score is 26'),(63,2,'NEW HIT','2016-11-06 15:27:17','Current score is 27'),(64,2,'NEW HIT','2016-11-06 15:27:23','Current score is 28'),(65,2,'NEW HIT','2016-11-06 15:27:27','Current score is 29'),(66,2,'NEW HIT','2016-11-06 15:27:31','Current score is 30'),(67,2,'NEW HIT','2016-11-06 15:27:31','Current score is 31'),(68,2,'NEW HIT','2016-11-06 15:27:34','Current score is 32'),(69,2,'NEW HIT','2016-11-06 15:27:35','Current score is 33'),(70,2,'NEW HIT','2016-11-06 15:27:38','Current score is 34'),(71,2,'NEW HIT','2016-11-06 15:27:42','Current score is 35'),(72,2,'NEW HIT','2016-11-06 15:27:43','Current score is 36'),(73,2,'NEW HIT','2016-11-06 15:27:45','Current score is 37'),(74,2,'NEW HIT','2016-11-06 15:27:48','Current score is 38'),(75,2,'NEW HIT','2016-11-06 15:27:49','Current score is 39'),(76,2,'NEW HIT','2016-11-06 15:27:49','Current score is 40'),(77,2,'GAME FINISHED','2016-11-06 15:27:52','Final score is 40'),(78,3,'START GAME','2016-11-06 15:34:35','Game started'),(79,3,'NEW HIT','2016-11-06 15:34:38','Current score is 1'),(80,3,'NEW HIT','2016-11-06 15:34:42','Current score is 2'),(81,3,'NEW HIT','2016-11-06 15:34:45','Current score is 3'),(82,3,'NEW HIT','2016-11-06 15:34:46','Current score is 4'),(83,3,'NEW HIT','2016-11-06 15:34:49','Current score is 5'),(84,3,'NEW HIT','2016-11-06 15:34:50','Current score is 7'),(85,3,'NEW HIT','2016-11-06 15:34:51','Current score is 7'),(86,3,'NEW HIT','2016-11-06 15:34:51','Current score is 8'),(87,3,'NEW HIT','2016-11-06 15:34:55','Current score is 9'),(88,3,'NEW HIT','2016-11-06 15:34:59','Current score is 10'),(89,3,'NEW HIT','2016-11-06 15:35:02','Current score is 11'),(90,3,'NEW HIT','2016-11-06 15:35:02','Current score is 12'),(91,3,'NEW HIT','2016-11-06 15:35:03','Current score is 13'),(92,3,'NEW HIT','2016-11-06 15:35:06','Current score is 14'),(93,3,'GAME FINISHED','2016-11-06 15:36:35','Final score is 14'),(94,4,'START GAME','2016-11-06 20:26:16','Game started'),(95,4,'NEW HIT','2016-11-06 20:26:24','Current score is 1'),(96,4,'NEW HIT','2016-11-06 20:26:28','Current score is 2'),(97,4,'NEW HIT','2016-11-06 20:26:34','Current score is 3'),(98,4,'NEW HIT','2016-11-06 20:26:41','Current score is 4'),(99,4,'NEW HIT','2016-11-06 20:26:47','Current score is 5'),(100,4,'NEW HIT','2016-11-06 20:26:51','Current score is 6'),(101,4,'NEW HIT','2016-11-06 20:26:52','Current score is 7'),(102,4,'NEW HIT','2016-11-06 20:26:57','Current score is 8'),(103,4,'NEW HIT','2016-11-06 20:27:01','Current score is 9'),(104,4,'NEW HIT','2016-11-06 20:27:05','Current score is 10'),(105,4,'NEW HIT','2016-11-06 20:27:08','Current score is 11'),(106,4,'NEW HIT','2016-11-06 20:27:11','Current score is 12'),(107,4,'NEW HIT','2016-11-06 20:27:16','Current score is 13'),(108,4,'NEW HIT','2016-11-06 20:27:20','Current score is 14'),(109,4,'NEW HIT','2016-11-06 20:27:24','Current score is 15'),(110,4,'NEW HIT','2016-11-06 20:27:26','Current score is 16'),(111,4,'NEW HIT','2016-11-06 20:27:35','Current score is 17'),(112,4,'NEW HIT','2016-11-06 20:27:37','Current score is 18'),(113,4,'NEW HIT','2016-11-06 20:27:40','Current score is 19'),(114,4,'NEW HIT','2016-11-06 20:27:49','Current score is 20'),(115,4,'NEW HIT','2016-11-06 20:28:06','Current score is 21'),(116,4,'NEW HIT','2016-11-06 20:28:16','Current score is 22'),(117,4,'GAME FINISHED','2016-11-06 20:28:17','Final score is 22'),(118,5,'START GAME','2016-11-06 20:30:30','Game started'),(119,5,'NEW HIT','2016-11-06 20:30:33','Current score is 1'),(120,5,'NEW HIT','2016-11-06 20:30:39','Current score is 2'),(121,5,'NEW HIT','2016-11-06 20:30:39','Current score is 3'),(122,5,'NEW HIT','2016-11-06 20:30:41','Current score is 4'),(123,5,'NEW HIT','2016-11-06 20:30:42','Current score is 5'),(124,5,'NEW HIT','2016-11-06 20:30:43','Current score is 6'),(125,5,'NEW HIT','2016-11-06 20:30:43','Current score is 7'),(126,5,'NEW HIT','2016-11-06 20:30:50','Current score is 8'),(127,5,'NEW HIT','2016-11-06 20:30:55','Current score is 9'),(128,5,'NEW HIT','2016-11-06 20:30:58','Current score is 10'),(129,5,'NEW HIT','2016-11-06 20:31:02','Current score is 11'),(130,5,'NEW HIT','2016-11-06 20:31:07','Current score is 12'),(131,5,'NEW HIT','2016-11-06 20:31:31','Current score is 13'),(132,5,'NEW HIT','2016-11-06 20:31:32','Current score is 14'),(133,5,'NEW HIT','2016-11-06 20:31:35','Current score is 15'),(134,5,'NEW HIT','2016-11-06 20:31:40','Current score is 16'),(135,5,'NEW HIT','2016-11-06 20:31:46','Current score is 17'),(136,5,'NEW HIT','2016-11-06 20:31:52','Current score is 18'),(137,5,'NEW HIT','2016-11-06 20:31:55','Current score is 19'),(138,5,'NEW HIT','2016-11-06 20:32:01','Current score is 20'),(139,5,'NEW HIT','2016-11-06 20:32:05','Current score is 21'),(140,5,'NEW HIT','2016-11-06 20:32:09','Current score is 22'),(141,5,'NEW HIT','2016-11-06 20:32:15','Current score is 23'),(142,5,'NEW HIT','2016-11-06 20:32:20','Current score is 24'),(143,5,'NEW HIT','2016-11-06 20:32:24','Current score is 25'),(144,5,'NEW HIT','2016-11-06 20:32:29','Current score is 26'),(145,5,'GAME FINISHED','2016-11-06 20:32:30','Final score is 26'),(146,5,'PLAYER LEFT GAME','2016-11-06 20:49:41','Final score is 26'),(147,6,'START GAME','2016-11-06 21:01:44','Game started'),(148,6,'NEW HIT','2016-11-06 21:01:49','Current score is 1'),(149,6,'NEW HIT','2016-11-06 21:01:53','Current score is 2'),(150,6,'NEW HIT','2016-11-06 21:01:53','Current score is 3'),(151,6,'NEW HIT','2016-11-06 21:01:57','Current score is 4'),(152,6,'NEW HIT','2016-11-06 21:02:00','Current score is 5'),(153,6,'NEW HIT','2016-11-06 21:02:02','Current score is 6'),(154,6,'NEW HIT','2016-11-06 21:02:07','Current score is 7'),(155,6,'NEW HIT','2016-11-06 21:02:10','Current score is 8'),(156,6,'NEW HIT','2016-11-06 21:02:14','Current score is 9'),(157,6,'NEW HIT','2016-11-06 21:02:18','Current score is 11'),(158,6,'NEW HIT','2016-11-06 21:02:18','Current score is 11'),(159,6,'NEW HIT','2016-11-06 21:02:20','Current score is 12'),(160,6,'NEW HIT','2016-11-06 21:02:20','Current score is 13'),(161,6,'NEW HIT','2016-11-06 21:02:22','Current score is 14'),(162,6,'NEW HIT','2016-11-06 21:02:25','Current score is 15'),(163,6,'NEW HIT','2016-11-06 21:02:26','Current score is 17'),(164,6,'NEW HIT','2016-11-06 21:02:26','Current score is 17'),(165,6,'NEW HIT','2016-11-06 21:02:27','Current score is 18'),(166,6,'NEW HIT','2016-11-06 21:02:33','Current score is 19'),(167,6,'NEW HIT','2016-11-06 21:02:39','Current score is 20'),(168,6,'NEW HIT','2016-11-06 21:02:41','Current score is 21'),(169,6,'NEW HIT','2016-11-06 21:02:47','Current score is 23'),(170,6,'NEW HIT','2016-11-06 21:02:47','Current score is 24'),(171,6,'NEW HIT','2016-11-06 21:02:47','Current score is 24'),(172,6,'NEW HIT','2016-11-06 21:02:48','Current score is 25'),(173,6,'NEW HIT','2016-11-06 21:02:51','Current score is 26'),(174,6,'NEW HIT','2016-11-06 21:02:56','Current score is 27'),(175,6,'NEW HIT','2016-11-06 21:02:59','Current score is 28'),(176,6,'NEW HIT','2016-11-06 21:03:03','Current score is 29'),(177,6,'NEW HIT','2016-11-06 21:03:03','Current score is 30'),(178,6,'NEW HIT','2016-11-06 21:03:07','Current score is 31'),(179,6,'NEW HIT','2016-11-06 21:03:14','Current score is 32'),(180,6,'NEW HIT','2016-11-06 21:03:16','Current score is 33'),(181,6,'NEW HIT','2016-11-06 21:03:19','Current score is 34'),(182,6,'NEW HIT','2016-11-06 21:03:19','Current score is 36'),(183,6,'NEW HIT','2016-11-06 21:03:19','Current score is 36'),(184,6,'NEW HIT','2016-11-06 21:03:22','Current score is 37'),(185,6,'NEW HIT','2016-11-06 21:03:26','Current score is 38'),(186,6,'NEW HIT','2016-11-06 21:03:27','Current score is 39'),(187,6,'NEW HIT','2016-11-06 21:03:27','Current score is 40'),(188,6,'NEW HIT','2016-11-06 21:03:31','Current score is 41'),(189,6,'NEW HIT','2016-11-06 21:03:34','Current score is 42'),(190,6,'NEW HIT','2016-11-06 21:03:36','Current score is 43'),(191,6,'NEW HIT','2016-11-06 21:03:39','Current score is 44'),(192,6,'NEW HIT','2016-11-06 21:03:43','Current score is 45'),(193,6,'GAME FINISHED','2016-11-06 21:03:43','Final score is 45'),(194,6,'NEW HIT','2016-11-07 03:49:26','Current score is 1'),(195,6,'NEW HIT','2016-11-07 03:49:30','Current score is 2'),(196,6,'NEW HIT','2016-11-07 03:49:35','Current score is 3'),(197,6,'NEW HIT','2016-11-07 03:49:36','Current score is 4'),(198,6,'NEW HIT','2016-11-07 03:49:42','Current score is 5'),(199,6,'NEW HIT','2016-11-07 03:49:43','Current score is 6'),(200,6,'NEW HIT','2016-11-07 03:49:55','Current score is 7'),(201,6,'NEW HIT','2016-11-07 03:49:58','Current score is 8'),(202,6,'NEW HIT','2016-11-07 03:50:02','Current score is 9'),(203,6,'NEW HIT','2016-11-07 03:50:03','Current score is 10'),(204,6,'NEW HIT','2016-11-07 03:50:07','Current score is 11'),(205,6,'NEW HIT','2016-11-07 03:50:10','Current score is 12'),(206,6,'NEW HIT','2016-11-07 03:50:10','Current score is 13'),(207,6,'NEW HIT','2016-11-07 03:50:15','Current score is 14'),(208,6,'NEW HIT','2016-11-07 03:50:19','Current score is 15'),(209,6,'NEW HIT','2016-11-07 03:50:25','Current score is 16'),(210,6,'NEW HIT','2016-11-07 03:50:29','Current score is 17'),(211,6,'NEW HIT','2016-11-07 03:50:36','Current score is 18'),(212,6,'NEW HIT','2016-11-07 03:50:41','Current score is 19'),(213,6,'NEW HIT','2016-11-07 03:50:45','Current score is 20'),(214,6,'NEW HIT','2016-11-07 03:50:47','Current score is 22'),(215,6,'NEW HIT','2016-11-07 03:50:47','Current score is 22'),(216,6,'NEW HIT','2016-11-07 03:50:51','Current score is 23'),(217,6,'NEW HIT','2016-11-07 03:50:51','Current score is 24'),(218,6,'NEW HIT','2016-11-07 03:51:08','Current score is 25'),(219,6,'NEW HIT','2016-11-07 03:51:12','Current score is 26'),(220,6,'NEW HIT','2016-11-07 03:51:13','Current score is 27'),(221,6,'NEW HIT','2016-11-07 03:51:17','Current score is 28'),(222,6,'NEW HIT','2016-11-07 03:51:21','Current score is 29'),(223,6,'GAME FINISHED','2016-11-07 03:51:22','Final score is 29'),(224,7,'START GAME','2016-11-08 21:04:27','Game started'),(225,7,'NEW HIT','2016-11-08 21:04:30','Current score is 1'),(226,7,'NEW HIT','2016-11-08 21:04:33','Current score is 2'),(227,7,'NEW HIT','2016-11-08 21:04:37','Current score is 3'),(228,7,'NEW HIT','2016-11-08 21:04:40','Current score is 4'),(229,7,'NEW HIT','2016-11-08 21:04:41','Current score is 5'),(230,7,'NEW HIT','2016-11-08 21:04:46','Current score is 6'),(231,7,'NEW HIT','2016-11-08 21:04:50','Current score is 7'),(232,7,'NEW HIT','2016-11-08 21:04:53','Current score is 8'),(233,7,'NEW HIT','2016-11-08 21:05:21','Current score is 9'),(234,7,'NEW HIT','2016-11-08 21:05:39','Current score is 10'),(235,7,'NEW HIT','2016-11-08 21:05:42','Current score is 11'),(236,7,'NEW HIT','2016-11-08 21:05:46','Current score is 12'),(237,7,'NEW HIT','2016-11-08 21:05:49','Current score is 13'),(238,7,'NEW HIT','2016-11-08 21:05:53','Current score is 14'),(239,7,'GAME FINISHED','2016-11-08 21:05:54','Final score is 14'),(240,8,'START GAME','2016-11-08 21:06:08','Game started'),(241,8,'NEW HIT','2016-11-08 21:06:13','Current score is 1'),(242,8,'GAME FINISHED','2016-11-08 21:06:27','Final score is 1'),(243,8,'NEW HIT','2016-11-08 21:06:42','Current score is 2'),(244,9,'START GAME','2016-11-08 21:06:52','Game started'),(245,9,'NEW HIT','2016-11-08 21:06:56','Current score is 1'),(246,9,'NEW HIT','2016-11-08 21:06:56','Current score is 2'),(247,8,'NEW HIT','2016-11-08 21:06:59','Current score is 3'),(248,8,'NEW HIT','2016-11-08 21:07:00','Current score is 4'),(249,8,'NEW HIT','2016-11-08 21:07:00','Current score is 5'),(250,9,'NEW HIT','2016-11-08 21:07:02','Current score is 3'),(251,8,'NEW HIT','2016-11-08 21:07:10','Current score is 6'),(252,8,'NEW HIT','2016-11-08 21:07:11','Current score is 8'),(253,8,'NEW HIT','2016-11-08 21:07:11','Current score is 8'),(254,9,'NEW HIT','2016-11-08 21:07:14','Current score is 4'),(255,8,'NEW HIT','2016-11-08 21:07:17','Current score is 9'),(256,9,'NEW HIT','2016-11-08 21:07:20','Current score is 5'),(257,8,'NEW HIT','2016-11-08 21:07:23','Current score is 10'),(258,9,'NEW HIT','2016-11-08 21:07:26','Current score is 6'),(259,8,'NEW HIT','2016-11-08 21:07:30','Current score is 11'),(260,9,'NEW HIT','2016-11-08 21:07:32','Current score is 7'),(261,8,'NEW HIT','2016-11-08 21:07:34','Current score is 12'),(262,9,'NEW HIT','2016-11-08 21:07:37','Current score is 8'),(263,8,'NEW HIT','2016-11-08 21:07:40','Current score is 13'),(264,9,'NEW HIT','2016-11-08 21:07:42','Current score is 9'),(265,8,'NEW HIT','2016-11-08 21:07:46','Current score is 14'),(266,9,'NEW HIT','2016-11-08 21:07:50','Current score is 10'),(267,8,'NEW HIT','2016-11-08 21:07:54','Current score is 15'),(268,8,'NEW HIT','2016-11-08 21:07:54','Current score is 16'),(269,8,'NEW HIT','2016-11-08 21:07:54','Current score is 17'),(270,8,'NEW HIT','2016-11-08 21:07:54','Current score is 18'),(271,9,'NEW HIT','2016-11-08 21:07:58','Current score is 11'),(272,8,'NEW HIT','2016-11-08 21:08:00','Current score is 19'),(273,9,'NEW HIT','2016-11-08 21:08:03','Current score is 12'),(274,8,'NEW HIT','2016-11-08 21:08:06','Current score is 20'),(275,8,'NEW HIT','2016-11-08 21:08:07','Current score is 21'),(276,8,'GAME FINISHED','2016-11-08 21:08:08','Final score is 21'),(277,9,'NEW HIT','2016-11-08 21:08:12','Current score is 13'),(278,9,'NEW HIT','2016-11-08 21:08:19','Current score is 14'),(279,9,'NEW HIT','2016-11-08 21:08:28','Current score is 15'),(280,9,'NEW HIT','2016-11-08 21:08:37','Current score is 16'),(281,9,'NEW HIT','2016-11-08 21:08:37','Current score is 17'),(282,9,'NEW HIT','2016-11-08 21:08:41','Current score is 18'),(283,9,'NEW HIT','2016-11-08 21:08:41','Current score is 19'),(284,9,'NEW HIT','2016-11-08 21:08:44','Current score is 20'),(285,9,'NEW HIT','2016-11-08 21:08:49','Current score is 21'),(286,9,'GAME FINISHED','2016-11-08 21:08:52','Final score is 21'),(287,9,'NEW HIT','2016-11-08 21:08:52','Current score is 22'),(288,10,'START GAME','2016-11-08 21:08:57','Game started'),(289,10,'NEW HIT','2016-11-08 21:09:02','Current score is 1'),(290,10,'NEW HIT','2016-11-08 21:09:04','Current score is 2'),(291,10,'NEW HIT','2016-11-08 21:09:10','Current score is 3'),(292,10,'NEW HIT','2016-11-08 21:09:13','Current score is 4'),(293,10,'NEW HIT','2016-11-08 21:09:15','Current score is 5'),(294,10,'NEW HIT','2016-11-08 21:09:18','Current score is 6'),(295,10,'NEW HIT','2016-11-08 21:09:19','Current score is 7'),(296,10,'NEW HIT','2016-11-08 21:09:20','Current score is 8'),(297,10,'NEW HIT','2016-11-08 21:09:24','Current score is 9'),(298,10,'NEW HIT','2016-11-08 21:09:27','Current score is 10'),(299,10,'NEW HIT','2016-11-08 21:09:30','Current score is 11'),(300,10,'NEW HIT','2016-11-08 21:09:34','Current score is 12'),(301,10,'NEW HIT','2016-11-08 21:09:35','Current score is 13'),(302,10,'NEW HIT','2016-11-08 21:09:39','Current score is 14'),(303,10,'NEW HIT','2016-11-08 21:09:40','Current score is 15'),(304,10,'NEW HIT','2016-11-08 21:09:43','Current score is 16'),(305,10,'NEW HIT','2016-11-08 21:09:43','Current score is 17'),(306,10,'NEW HIT','2016-11-08 21:09:44','Current score is 18'),(307,10,'NEW HIT','2016-11-08 21:09:45','Current score is 19'),(308,10,'NEW HIT','2016-11-08 21:09:46','Current score is 20'),(309,10,'NEW HIT','2016-11-08 21:09:50','Current score is 21'),(310,10,'NEW HIT','2016-11-08 21:09:50','Current score is 22'),(311,10,'NEW HIT','2016-11-08 21:09:52','Current score is 23'),(312,10,'NEW HIT','2016-11-08 21:09:56','Current score is 24'),(313,10,'NEW HIT','2016-11-08 21:09:56','Current score is 25'),(314,10,'NEW HIT','2016-11-08 21:09:57','Current score is 26'),(315,10,'NEW HIT','2016-11-08 21:10:02','Current score is 27'),(316,10,'NEW HIT','2016-11-08 21:10:02','Current score is 28'),(317,10,'NEW HIT','2016-11-08 21:10:06','Current score is 29'),(318,10,'NEW HIT','2016-11-08 21:10:07','Current score is 30'),(319,10,'NEW HIT','2016-11-08 21:10:11','Current score is 31'),(320,10,'NEW HIT','2016-11-08 21:10:22','Current score is 32'),(321,10,'NEW HIT','2016-11-08 21:10:35','Current score is 33'),(322,10,'NEW HIT','2016-11-08 21:10:35','Current score is 34'),(323,10,'NEW HIT','2016-11-08 21:10:50','Current score is 35'),(324,10,'NEW HIT','2016-11-08 21:10:54','Current score is 36'),(325,10,'GAME FINISHED','2016-11-08 21:10:57','Final score is 36'),(326,10,'NEW HIT','2016-11-08 21:10:57','Current score is 37'),(327,11,'START GAME','2016-11-08 21:11:09','Game started'),(328,11,'NEW HIT','2016-11-08 21:11:13','Current score is 1'),(329,11,'NEW HIT','2016-11-08 21:11:17','Current score is 2'),(330,11,'NEW HIT','2016-11-08 21:11:20','Current score is 3'),(331,11,'NEW HIT','2016-11-08 21:11:23','Current score is 4'),(332,11,'GAME FINISHED','2016-11-08 21:11:26','Final score is 4'),(333,12,'START GAME','2016-11-08 21:11:53','Game started'),(334,12,'NEW HIT','2016-11-08 21:11:56','Current score is 1'),(335,12,'NEW HIT','2016-11-08 21:12:00','Current score is 2'),(336,12,'NEW HIT','2016-11-08 21:12:04','Current score is 3'),(337,12,'NEW HIT','2016-11-08 21:12:08','Current score is 4'),(338,12,'NEW HIT','2016-11-08 21:12:12','Current score is 5'),(339,12,'NEW HIT','2016-11-08 21:12:15','Current score is 6'),(340,12,'NEW HIT','2016-11-08 21:12:15','Current score is 7'),(341,12,'NEW HIT','2016-11-08 21:12:18','Current score is 8'),(342,12,'NEW HIT','2016-11-08 21:12:19','Current score is 9'),(343,12,'NEW HIT','2016-11-08 21:12:26','Current score is 10'),(344,12,'NEW HIT','2016-11-08 21:12:30','Current score is 11'),(345,12,'NEW HIT','2016-11-08 21:12:42','Current score is 12'),(346,12,'NEW HIT','2016-11-08 21:12:47','Current score is 13'),(347,12,'GAME FINISHED','2016-11-08 21:12:47','Final score is 13'),(348,11,'GAME FINISHED','2016-11-08 21:13:09','Final score is 4'),(349,12,'GAME FINISHED','2016-11-08 21:13:53','Final score is 13'),(350,13,'START GAME','2016-11-08 21:18:54','Game started'),(351,13,'NEW HIT','2016-11-08 21:18:58','Current score is 1'),(352,13,'NEW HIT','2016-11-08 21:18:59','Current score is 3'),(353,13,'NEW HIT','2016-11-08 21:18:59','Current score is 3'),(354,13,'NEW HIT','2016-11-08 21:19:07','Current score is 4'),(355,13,'NEW HIT','2016-11-08 21:19:10','Current score is 5'),(356,13,'NEW HIT','2016-11-08 21:19:12','Current score is 6'),(357,13,'NEW HIT','2016-11-08 21:19:16','Current score is 7'),(358,13,'NEW HIT','2016-11-08 21:19:16','Current score is 8'),(359,13,'NEW HIT','2016-11-08 21:19:18','Current score is 9'),(360,13,'NEW HIT','2016-11-08 21:19:21','Current score is 10'),(361,13,'NEW HIT','2016-11-08 21:19:24','Current score is 11'),(362,13,'NEW HIT','2016-11-08 21:19:27','Current score is 12'),(363,13,'NEW HIT','2016-11-08 21:19:31','Current score is 13'),(364,13,'NEW HIT','2016-11-08 21:19:31','Current score is 14'),(365,13,'NEW HIT','2016-11-08 21:19:38','Current score is 15'),(366,13,'NEW HIT','2016-11-08 21:19:41','Current score is 16'),(367,13,'NEW HIT','2016-11-08 21:19:41','Current score is 17'),(368,13,'NEW HIT','2016-11-08 21:19:41','Current score is 18'),(369,13,'NEW HIT','2016-11-08 21:19:48','Current score is 20'),(370,13,'NEW HIT','2016-11-08 21:19:48','Current score is 20'),(371,13,'NEW HIT','2016-11-08 21:19:55','Current score is 21'),(372,13,'NEW HIT','2016-11-08 21:20:02','Current score is 22'),(373,13,'NEW HIT','2016-11-08 21:20:08','Current score is 23'),(374,13,'NEW HIT','2016-11-08 21:20:08','Current score is 24'),(375,13,'NEW HIT','2016-11-08 21:20:18','Current score is 25'),(376,13,'NEW HIT','2016-11-08 21:20:18','Current score is 26'),(377,13,'NEW HIT','2016-11-08 21:20:22','Current score is 27'),(378,13,'NEW HIT','2016-11-08 21:20:26','Current score is 28'),(379,13,'NEW HIT','2016-11-08 21:20:29','Current score is 29'),(380,13,'NEW HIT','2016-11-08 21:20:29','Current score is 30'),(381,13,'NEW HIT','2016-11-08 21:20:34','Current score is 31'),(382,13,'NEW HIT','2016-11-08 21:20:36','Current score is 32'),(383,13,'NEW HIT','2016-11-08 21:20:39','Current score is 33'),(384,13,'NEW HIT','2016-11-08 21:20:42','Current score is 34'),(385,13,'NEW HIT','2016-11-08 21:20:46','Current score is 35'),(386,13,'NEW HIT','2016-11-08 21:20:49','Current score is 36'),(387,13,'NEW HIT','2016-11-08 21:20:52','Current score is 37'),(388,13,'NEW HIT','2016-11-08 21:20:52','Current score is 38'),(389,13,'GAME FINISHED','2016-11-08 21:20:54','Final score is 38'),(390,14,'START GAME','2016-11-08 21:33:02','Game started'),(391,14,'NEW HIT','2016-11-08 21:33:06','Current score is 1'),(392,14,'NEW HIT','2016-11-08 21:33:10','Current score is 2'),(393,14,'NEW HIT','2016-11-08 21:33:14','Current score is 3'),(394,14,'NEW HIT','2016-11-08 21:33:14','Current score is 4'),(395,14,'NEW HIT','2016-11-08 21:33:17','Current score is 5'),(396,14,'NEW HIT','2016-11-08 21:33:21','Current score is 6'),(397,14,'NEW HIT','2016-11-08 21:33:26','Current score is 7'),(398,14,'NEW HIT','2016-11-08 21:33:26','Current score is 8'),(399,14,'NEW HIT','2016-11-08 21:33:29','Current score is 10'),(400,14,'NEW HIT','2016-11-08 21:33:29','Current score is 10'),(401,14,'NEW HIT','2016-11-08 21:33:29','Current score is 11'),(402,14,'NEW HIT','2016-11-08 21:33:34','Current score is 12'),(403,14,'NEW HIT','2016-11-08 21:33:36','Current score is 13'),(404,14,'NEW HIT','2016-11-08 21:33:41','Current score is 14'),(405,14,'NEW HIT','2016-11-08 21:33:43','Current score is 15'),(406,14,'NEW HIT','2016-11-08 21:33:46','Current score is 16'),(407,14,'NEW HIT','2016-11-08 21:33:46','Current score is 17'),(408,14,'NEW HIT','2016-11-08 21:33:48','Current score is 18'),(409,14,'NEW HIT','2016-11-08 21:33:52','Current score is 19'),(410,14,'NEW HIT','2016-11-08 21:34:01','Current score is 20'),(411,14,'GAME FINISHED','2016-11-08 21:34:02','Final score is 20'),(412,14,'PLAYER LEFT GAME','2016-11-08 21:34:04','Final score is 20'),(413,12,'PLAYER LEFT GAME','2016-11-08 21:34:08','Final score is 13');
UNLOCK TABLES;

COMMIT;