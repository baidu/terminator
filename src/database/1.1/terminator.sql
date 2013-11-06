-- MySQL dump 10.13  Distrib 5.1.45, for Win32 (ia32)
--
-- Host: localhost    Database: terminator
-- ------------------------------------------------------
-- Server version	5.1.45-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

create database if not exists `terminator`;

USE `terminator`;

--
-- Table structure for table `data_index`
--

DROP TABLE IF EXISTS `data_index`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_index` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `link_id` int(11) NOT NULL,
  `offset` bigint(30) NOT NULL,
  `signature` varchar(255) NOT NULL DEFAULT '',
  `add_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `linkid` (`link_id`),
  KEY `signature` (`signature`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `link`
--

DROP TABLE IF EXISTS `link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(31) COLLATE utf8_bin NOT NULL,
  `local_port` int(11) NOT NULL,
  `remote_address` varchar(127) COLLATE utf8_bin NOT NULL,
  `remote_port` int(11) NOT NULL,
  `work_mode` varchar(15) COLLATE utf8_bin NOT NULL,
  `protocol_type` varchar(63) COLLATE utf8_bin NOT NULL,
  `storage_type` varchar(15) COLLATE utf8_bin NOT NULL,
  `sign_class` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `extract_class` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `work_status` varchar(15) COLLATE utf8_bin NOT NULL DEFAULT 'stopped',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0',
  `add_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stub_condition`
--

DROP TABLE IF EXISTS `stub_condition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stub_condition` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stub_data_id` int(11) NOT NULL,
  `key` varchar(1023) NOT NULL,
  `value` varchar(2047) NOT NULL,
  `operator` varchar(31) NOT NULL,
  `sequence` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stub_data`
--

DROP TABLE IF EXISTS `stub_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stub_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `link_id` int(11) NOT NULL,
  `delay` int(11) NOT NULL,
  `response` varchar(4095) NOT NULL DEFAULT '""',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-10-16 16:59:15
