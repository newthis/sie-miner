# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.6.13)
# Database: sie
# Generation Time: 2014-05-29 12:02:30 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table catched_exceptions
# ------------------------------------------------------------

DROP TABLE IF EXISTS `catched_exceptions`;

CREATE TABLE `catched_exceptions` (
  `method` int(11) DEFAULT NULL,
  `exception_type` int(11) DEFAULT NULL,
  KEY `method_id` (`method`),
  KEY `type_id` (`exception_type`),
  KEY `FK_ep7vp9dm7s9ku75tbfxk2m3i5` (`exception_type`),
  CONSTRAINT `catched_exc` FOREIGN KEY (`method`) REFERENCES `methods` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `exc_catch` FOREIGN KEY (`exception_type`) REFERENCES `types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_ep7vp9dm7s9ku75tbfxk2m3i5` FOREIGN KEY (`exception_type`) REFERENCES `types` (`import_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table changes
# ------------------------------------------------------------

DROP TABLE IF EXISTS `changes`;

CREATE TABLE `changes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hash` varchar(50) DEFAULT NULL,
  `commit_date` date DEFAULT NULL,
  `dev_mail` varchar(100) DEFAULT NULL,
  `dev_id` varchar(100) DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL,
  `project` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `project_id` (`project`),
  CONSTRAINT `change_project` FOREIGN KEY (`project`) REFERENCES `projects` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `changes` WRITE;
/*!40000 ALTER TABLE `changes` DISABLE KEYS */;

INSERT INTO `changes` (`id`, `hash`, `commit_date`, `dev_mail`, `dev_id`, `message`, `project`)
VALUES
	(1,'dc079bb','2014-05-28','granogiovanni90@gmail.com','giograno','Merge branch \'master\' of https://github.com/mattmezza/minerTest',1),
	(2,'a073fe6','2014-05-28','granogiovanni90@gmail.com','giograno','base classes added',1),
	(3,'9cfe9b5','2014-05-28','mattmezza@gmail.com','Matteo Merola','Refactoring method of base class',1),
	(4,'e123b07','2014-05-28','granogiovanni90@gmail.com','giograno','Merge branch \'master\' of https://github.com/mattmezza/minerTest',1),
	(5,'0754499','2014-05-28','granogiovanni90@gmail.com','giograno','Refactoring class ICulculator.java',1),
	(6,'84f7c1d','2014-05-28','granogiovanni90@gmail.com','giograno','fix for cube feature',1),
	(7,'3b7fb53','2014-05-28','granogiovanni90@gmail.com','giograno','Merge branch \'master\' of https://github.com/mattmezza/minerTest',1),
	(8,'60bc577','2014-05-28','granogiovanni90@gmail.com','giograno','feature introduction for cube operation',1),
	(9,'9697f89','2014-05-28','mattmezza@gmail.com','Matteo Merola','Merge branch \'master\' of https://github.com/mattmezza/minerTest',1),
	(10,'62f95e2','2014-05-28','mattmezza@gmail.com','Matteo Merola','BugFix in specialized sum method',1),
	(11,'5f85793','2014-05-28','mattmezza@gmail.com','Matteo Merola','Aggiunto modulo matt',1),
	(12,'a3cf182','2014-05-28','mattmezza@gmail.com','Matteo Merola','Initial commit',1),
	(13,'aeae915','2014-05-28','mattmezza@gmail.com','Matteo Merola','feature introduction: specialized can sum 2 numbers',1);

/*!40000 ALTER TABLE `changes` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table changes_for_commit
# ------------------------------------------------------------

DROP TABLE IF EXISTS `changes_for_commit`;

CREATE TABLE `changes_for_commit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `change_hash` int(11) DEFAULT NULL,
  `modified_file` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `change_hash_fk1` (`change_hash`),
  CONSTRAINT `change_hash_fk` FOREIGN KEY (`change_hash`) REFERENCES `changes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `changes_for_commit` WRITE;
/*!40000 ALTER TABLE `changes_for_commit` DISABLE KEYS */;

INSERT INTO `changes_for_commit` (`id`, `change_hash`, `modified_file`)
VALUES
	(1,1,'/src/minerTest/matt/JustAClass.java'),
	(2,1,'/src/minerTest/matt/ASpecializedClass.java'),
	(3,1,'/src/minerTest/matt/AMoreSpecializedOne.java'),
	(4,2,'/src/minerTest/gio/ICulculator.java'),
	(5,2,'/src/minerTest/matt/AMoreSpecializedOne.java'),
	(6,2,'/src/minerTest/gio/EvolvedCalculator.java'),
	(7,2,'/src/minerTest/gio/ConcreteBaseCalculator.java'),
	(8,2,'/src/minerTest/matt/ASpecializedClass.java'),
	(9,2,'/src/minerTest/matt/JustAClass.java'),
	(10,3,'/src/minerTest/matt/JustAClass.java'),
	(11,3,'/src/minerTest/gio/ConcreteBaseCalculator.java'),
	(12,3,'/src/minerTest/gio/ICalculator.java'),
	(13,3,'/src/minerTest/matt/ASpecializedClass.java'),
	(14,3,'/src/minerTest/gio/EvolvedCalculator.java'),
	(15,4,'/src/minerTest/gio/EvolvedCalculator.java'),
	(16,5,'/src/minerTest/gio/ICalculator.java'),
	(17,5,'/src/minerTest/gio/ICulculator.java'),
	(18,5,'/src/minerTest/gio/ConcreteBaseCalculator.java'),
	(19,6,'/src/minerTest/gio/EvolvedCalculator.java'),
	(20,7,'/src/minerTest/matt/ASpecializedClass.java'),
	(21,7,'/src/minerTest/matt/JustAClass.java'),
	(22,8,'/src/minerTest/matt/ASpecializedClass.java'),
	(23,8,'/src/minerTest/matt/JustAClass.java'),
	(24,8,'/src/minerTest/gio/EvolvedCalculator.java'),
	(25,9,'/src/minerTest/gio/EvolvedCalculator.java'),
	(26,9,'/src/minerTest/gio/ICalculator.java'),
	(27,9,'/src/minerTest/gio/ConcreteBaseCalculator.java'),
	(28,10,'/src/minerTest/gio/EvolvedCalculator.java'),
	(29,10,'/src/minerTest/matt/ASpecializedClass.java'),
	(30,11,'/src/minerTest/matt/JustAClass.java'),
	(31,11,'/src/minerTest/matt/AMoreSpecializedOne.java'),
	(32,11,'/src/minerTest/matt/ASpecializedClass.java'),
	(33,13,'/src/minerTest/matt/ASpecializedClass.java');

/*!40000 ALTER TABLE `changes_for_commit` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table class_invocations
# ------------------------------------------------------------

DROP TABLE IF EXISTS `class_invocations`;

CREATE TABLE `class_invocations` (
  `invoker_class` int(11) DEFAULT NULL,
  `invoked_class` int(11) DEFAULT NULL,
  KEY `invoker_class` (`invoker_class`),
  KEY `invoked_class` (`invoked_class`),
  KEY `FK_hj3t6wqlw60src2pc0m223mk1` (`invoked_class`),
  KEY `FK_pnb3x70u4b1qnfss5egh5kmnk` (`invoker_class`),
  CONSTRAINT `class_invocations_ibfk_1` FOREIGN KEY (`invoker_class`) REFERENCES `types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `class_invocations_ibfk_2` FOREIGN KEY (`invoked_class`) REFERENCES `types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_hj3t6wqlw60src2pc0m223mk1` FOREIGN KEY (`invoked_class`) REFERENCES `types` (`import_id`),
  CONSTRAINT `FK_pnb3x70u4b1qnfss5egh5kmnk` FOREIGN KEY (`invoker_class`) REFERENCES `types` (`import_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table comments
# ------------------------------------------------------------

DROP TABLE IF EXISTS `comments`;

CREATE TABLE `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `text` text,
  `belonging_type` int(11) DEFAULT NULL,
  `field` int(11) DEFAULT NULL,
  `method` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Type_id` (`belonging_type`),
  KEY `FK_ab7mbxunucnq7sgs6aa5kja4u` (`field`),
  KEY `FK_kn77rei06n51pvd8o57dnwk73` (`method`),
  KEY `FK_gf40ep18w36ajfmwqmfn2vmtv` (`belonging_type`),
  CONSTRAINT `comment_type` FOREIGN KEY (`belonging_type`) REFERENCES `types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_ab7mbxunucnq7sgs6aa5kja4u` FOREIGN KEY (`field`) REFERENCES `fields` (`id`),
  CONSTRAINT `FK_gf40ep18w36ajfmwqmfn2vmtv` FOREIGN KEY (`belonging_type`) REFERENCES `types` (`import_id`),
  CONSTRAINT `FK_kn77rei06n51pvd8o57dnwk73` FOREIGN KEY (`method`) REFERENCES `methods` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;

INSERT INTO `comments` (`id`, `text`, `belonging_type`, `field`, `method`)
VALUES
	(1,'',NULL,NULL,1),
	(2,'',NULL,NULL,2),
	(3,'',NULL,NULL,NULL),
	(4,'',NULL,NULL,3),
	(5,'',NULL,NULL,4),
	(6,'',NULL,NULL,NULL),
	(7,'',NULL,NULL,5),
	(8,'',NULL,NULL,6),
	(9,'',NULL,NULL,7),
	(10,'',NULL,NULL,NULL),
	(11,'',NULL,NULL,8),
	(12,'',NULL,NULL,NULL),
	(13,'',NULL,NULL,NULL),
	(14,'',NULL,NULL,9),
	(15,'',NULL,NULL,10),
	(16,'',NULL,NULL,NULL),
	(17,'',NULL,NULL,NULL),
	(18,'',NULL,NULL,11),
	(19,'',NULL,NULL,NULL);

/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table extends
# ------------------------------------------------------------

DROP TABLE IF EXISTS `extends`;

CREATE TABLE `extends` (
  `subclass` int(10) DEFAULT NULL,
  `superclass` int(10) DEFAULT NULL,
  KEY `subclass` (`subclass`),
  KEY `superclass` (`superclass`),
  KEY `FK_33b56bi04r3hbgfyw5tflstwe` (`superclass`),
  KEY `FK_q4bobirj70f2lsnonq0h94365` (`subclass`),
  CONSTRAINT `extends_ibfk_1` FOREIGN KEY (`subclass`) REFERENCES `types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `extends_ibfk_2` FOREIGN KEY (`superclass`) REFERENCES `types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_33b56bi04r3hbgfyw5tflstwe` FOREIGN KEY (`superclass`) REFERENCES `types` (`import_id`),
  CONSTRAINT `FK_q4bobirj70f2lsnonq0h94365` FOREIGN KEY (`subclass`) REFERENCES `types` (`import_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table field_comments
# ------------------------------------------------------------

DROP TABLE IF EXISTS `field_comments`;

CREATE TABLE `field_comments` (
  `field` int(11) DEFAULT NULL,
  `comment` int(11) DEFAULT NULL,
  KEY `field_id` (`field`,`comment`),
  KEY `comments_id` (`comment`),
  CONSTRAINT `comment_field` FOREIGN KEY (`comment`) REFERENCES `comments` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `field_comment` FOREIGN KEY (`field`) REFERENCES `fields` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table fields
# ------------------------------------------------------------

DROP TABLE IF EXISTS `fields`;

CREATE TABLE `fields` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `visibility` varchar(50) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `owner_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `type_id` (`type`),
  KEY `owner_type` (`owner_type`),
  KEY `FK_h7qxl4v3envsy43lcv78h72xi` (`owner_type`),
  CONSTRAINT `field_type` FOREIGN KEY (`type`) REFERENCES `types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_h7qxl4v3envsy43lcv78h72xi` FOREIGN KEY (`owner_type`) REFERENCES `types` (`import_id`),
  CONSTRAINT `owner_type` FOREIGN KEY (`owner_type`) REFERENCES `types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `fields` WRITE;
/*!40000 ALTER TABLE `fields` DISABLE KEYS */;

INSERT INTO `fields` (`id`, `name`, `visibility`, `type`, `owner_type`)
VALUES
	(1,'service','protected',NULL,9);

/*!40000 ALTER TABLE `fields` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table import
# ------------------------------------------------------------

DROP TABLE IF EXISTS `import`;

CREATE TABLE `import` (
  `id` int(100) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `import` WRITE;
/*!40000 ALTER TABLE `import` DISABLE KEYS */;

INSERT INTO `import` (`id`, `name`)
VALUES
	(1,'minerTest.gio'),
	(2,'ConcreteBaseCalculator'),
	(3,'Object'),
	(4,'ICalculator'),
	(5,'Object'),
	(6,'EvolvedCalculator'),
	(7,'minerTest.matt'),
	(8,'AMoreSpecializedOne'),
	(9,'ASpecializedClass'),
	(10,'String'),
	(11,'JustAClass'),
	(12,'Object'),
	(13,'Object');

/*!40000 ALTER TABLE `import` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table imports
# ------------------------------------------------------------

DROP TABLE IF EXISTS `imports`;

CREATE TABLE `imports` (
  `importer` int(10) DEFAULT NULL,
  `imported` int(10) DEFAULT NULL,
  KEY `importer` (`importer`),
  KEY `imported` (`imported`),
  KEY `FK_77cgy2ajse6r9kw0670x7o4q9` (`imported`),
  KEY `FK_94ysdu0jxvo2coe31p4ax6272` (`importer`),
  CONSTRAINT `FK_77cgy2ajse6r9kw0670x7o4q9` FOREIGN KEY (`imported`) REFERENCES `import` (`id`),
  CONSTRAINT `FK_94ysdu0jxvo2coe31p4ax6272` FOREIGN KEY (`importer`) REFERENCES `types` (`import_id`),
  CONSTRAINT `imports_ibfk_1` FOREIGN KEY (`importer`) REFERENCES `import` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `imports_ibfk_2` FOREIGN KEY (`imported`) REFERENCES `types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table issue_attachments
# ------------------------------------------------------------

DROP TABLE IF EXISTS `issue_attachments`;

CREATE TABLE `issue_attachments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `belonging_issue` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ID_issue` (`belonging_issue`),
  CONSTRAINT `issue_attachment` FOREIGN KEY (`belonging_issue`) REFERENCES `issues` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table issue_comments
# ------------------------------------------------------------

DROP TABLE IF EXISTS `issue_comments`;

CREATE TABLE `issue_comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dev_id` varchar(100) DEFAULT NULL,
  `dev_mail` varchar(100) DEFAULT NULL,
  `text` varchar(1000) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `belonging_issue` int(11) DEFAULT NULL,
  `dev_email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `issue_id` (`belonging_issue`),
  CONSTRAINT `attachment_issue` FOREIGN KEY (`belonging_issue`) REFERENCES `issues` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table issues
# ------------------------------------------------------------

DROP TABLE IF EXISTS `issues`;

CREATE TABLE `issues` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) DEFAULT NULL,
  `priority` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `resolution` varchar(50) DEFAULT NULL,
  `affected_version` varchar(50) DEFAULT NULL,
  `component` varchar(50) DEFAULT NULL,
  `fix_version` varchar(50) DEFAULT NULL,
  `assignee` varchar(100) DEFAULT NULL,
  `reporter` varchar(100) DEFAULT NULL,
  `updated` date DEFAULT NULL,
  `created` date DEFAULT NULL,
  `closed` date DEFAULT NULL,
  `project` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `projectID_proj` (`project`),
  CONSTRAINT `project_issue` FOREIGN KEY (`project`) REFERENCES `projects` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table local_variables
# ------------------------------------------------------------

DROP TABLE IF EXISTS `local_variables`;

CREATE TABLE `local_variables` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `initialization` varchar(100) DEFAULT NULL,
  `belonging_method` int(11) DEFAULT NULL,
  `variable_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `type` (`variable_type`),
  KEY `method` (`belonging_method`),
  KEY `method_2` (`belonging_method`),
  CONSTRAINT `method_var` FOREIGN KEY (`variable_type`) REFERENCES `types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `var_method` FOREIGN KEY (`belonging_method`) REFERENCES `methods` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table method_comments
# ------------------------------------------------------------

DROP TABLE IF EXISTS `method_comments`;

CREATE TABLE `method_comments` (
  `method` int(11) DEFAULT NULL,
  `comment` int(11) DEFAULT NULL,
  KEY `comment_id` (`comment`),
  KEY `method_id` (`method`),
  CONSTRAINT `comment_method` FOREIGN KEY (`comment`) REFERENCES `comments` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `method_comment` FOREIGN KEY (`method`) REFERENCES `methods` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table method_invocations
# ------------------------------------------------------------

DROP TABLE IF EXISTS `method_invocations`;

CREATE TABLE `method_invocations` (
  `invoker_method` int(11) DEFAULT NULL,
  `invoked_method` int(11) DEFAULT NULL,
  KEY `callee` (`invoked_method`),
  KEY `caller` (`invoker_method`),
  CONSTRAINT `caller_method` FOREIGN KEY (`invoked_method`) REFERENCES `methods` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `method_caller` FOREIGN KEY (`invoker_method`) REFERENCES `methods` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table methods
# ------------------------------------------------------------

DROP TABLE IF EXISTS `methods`;

CREATE TABLE `methods` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lines_number` int(11) DEFAULT NULL,
  `is_constructor` varchar(1) DEFAULT NULL,
  `belonging_type` int(11) DEFAULT NULL,
  `return_type` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `type_id` (`belonging_type`),
  KEY `return_type` (`return_type`),
  KEY `FK_dk4y15vto38chkwx9vgccykoy` (`belonging_type`),
  KEY `FK_73nwpnvk9t09olm8osdm4er4` (`type`),
  CONSTRAINT `FK_73nwpnvk9t09olm8osdm4er4` FOREIGN KEY (`type`) REFERENCES `types` (`import_id`),
  CONSTRAINT `FK_dk4y15vto38chkwx9vgccykoy` FOREIGN KEY (`belonging_type`) REFERENCES `types` (`import_id`),
  CONSTRAINT `methods_ibfk_2` FOREIGN KEY (`return_type`) REFERENCES `types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `method_type` FOREIGN KEY (`belonging_type`) REFERENCES `types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `methods` WRITE;
/*!40000 ALTER TABLE `methods` DISABLE KEYS */;

INSERT INTO `methods` (`id`, `lines_number`, `is_constructor`, `belonging_type`, `return_type`, `name`, `type`)
VALUES
	(1,3,'N',NULL,NULL,'sum(int,int)',NULL),
	(2,3,'N',NULL,NULL,'min(int,int)',NULL),
	(3,1,'N',NULL,NULL,'min(int,int)',NULL),
	(4,1,'N',NULL,NULL,'sum(int,int)',NULL),
	(5,3,'N',NULL,NULL,'multi(int,int)',NULL),
	(6,3,'N',NULL,NULL,'div(int,int)',NULL),
	(7,3,'N',NULL,NULL,'cube(int)',NULL),
	(8,4,'N',NULL,NULL,'LMFAO()',NULL),
	(9,3,'N',NULL,NULL,'callMeIfYouWantToSum2Numbers(double,double)',NULL),
	(10,4,'N',NULL,NULL,'recallMe()',NULL),
	(11,3,'N',NULL,NULL,'youCanCallMe()',NULL);

/*!40000 ALTER TABLE `methods` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table methods_change_in_commit
# ------------------------------------------------------------

DROP TABLE IF EXISTS `methods_change_in_commit`;

CREATE TABLE `methods_change_in_commit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `modified_method` varchar(1024) DEFAULT NULL,
  `proprietary_file` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `file_changed_fk` (`proprietary_file`),
  CONSTRAINT `prop_file_fk` FOREIGN KEY (`proprietary_file`) REFERENCES `changes_for_commit` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `methods_change_in_commit` WRITE;
/*!40000 ALTER TABLE `methods_change_in_commit` DISABLE KEYS */;

INSERT INTO `methods_change_in_commit` (`id`, `modified_method`, `proprietary_file`)
VALUES
	(1,'public void callMe()',1),
	(2,'public void recallMe()',2),
	(3,'public void LMFAO()',3),
	(4,'public void LMFAO()',5),
	(5,'public float div(int pNum1, int pNum2)',6),
	(6,'public int multi (int pNum1, int pNum2)',6),
	(7,'public int min(int pNum1, int pNum2)',7),
	(8,'public int sum(int pNum1, int pNum2)',7),
	(9,'public void recallMe()',8),
	(10,'public void callMe()',9),
	(11,'public void callMe()',10),
	(12,'public void youCanCallMe()',10),
	(13,'public int min(int pNum1, int pNum2)',11),
	(14,'public int sum(int pNum1, int pNum2)',11),
	(15,'public void recallMe()',13),
	(16,'public float div(int pNum1, int pNum2)',14),
	(17,'public int multi (int pNum1, int pNum2)',14),
	(18,'public float cube(int pNum)',15),
	(19,'public int sum(int pNum1, int pNum2)',18),
	(20,'public float cube(int pNum)',19),
	(21,'public void recallMe()',20),
	(22,'public void callMe()',21),
	(23,'public void youCanCallMe()',21),
	(24,'public void recallMe()',22),
	(25,'public void callMe()',23),
	(26,'public void youCanCallMe()',23),
	(27,'public float div(int pNum1, int pNum2)',24),
	(28,'public float cube(int pNum)',24),
	(29,'public float div(int pNum1, int pNum2)',25),
	(30,'public int multi (int pNum1, int pNum2)',25),
	(31,'public int min(int pNum1, int pNum2)',27),
	(32,'public int sum(int pNum1, int pNum2)',27),
	(33,'public float cube(int pNum)',28),
	(34,'public double callMeIfYouWantToSum2Numbers(double first, double second)',29),
	(35,'public void callMe()',30),
	(36,'public void LMFAO()',31),
	(37,'public void recallMe()',32),
	(38,'public double callMeIfYouWantToSum2Numbers(double first, double second)',33);

/*!40000 ALTER TABLE `methods_change_in_commit` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table metrics
# ------------------------------------------------------------

DROP TABLE IF EXISTS `metrics`;

CREATE TABLE `metrics` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `description` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table metrics_method
# ------------------------------------------------------------

DROP TABLE IF EXISTS `metrics_method`;

CREATE TABLE `metrics_method` (
  `method` int(11) DEFAULT NULL,
  `metric` int(11) DEFAULT NULL,
  `value` double DEFAULT NULL,
  KEY `method` (`method`),
  KEY `metric` (`metric`),
  CONSTRAINT `metrics_method_ibfk_1` FOREIGN KEY (`method`) REFERENCES `methods` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `metrics_method_ibfk_2` FOREIGN KEY (`metric`) REFERENCES `metrics` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table parameters
# ------------------------------------------------------------

DROP TABLE IF EXISTS `parameters`;

CREATE TABLE `parameters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `belonging_method` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `type_id` (`type`),
  KEY `method_id` (`belonging_method`),
  CONSTRAINT `method_parameter` FOREIGN KEY (`type`) REFERENCES `types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `parameter_method` FOREIGN KEY (`belonging_method`) REFERENCES `methods` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table projects
# ------------------------------------------------------------

DROP TABLE IF EXISTS `projects`;

CREATE TABLE `projects` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `versioning_url` varchar(255) DEFAULT NULL,
  `bugtracker_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `projects` WRITE;
/*!40000 ALTER TABLE `projects` DISABLE KEYS */;

INSERT INTO `projects` (`id`, `name`, `versioning_url`, `bugtracker_url`)
VALUES
	(1,'minerTest','https://github.com/mattmezza/minerTest.git',NULL);

/*!40000 ALTER TABLE `projects` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table reviews
# ------------------------------------------------------------

DROP TABLE IF EXISTS `reviews`;

CREATE TABLE `reviews` (
  `versioning_url` varchar(255) NOT NULL,
  `name_app` varchar(150) NOT NULL,
  `author` varchar(150) NOT NULL,
  `title` varchar(150) NOT NULL,
  `review` varchar(800) NOT NULL,
  `rating` varchar(5) NOT NULL,
  `date` varchar(20) NOT NULL,
  UNIQUE KEY `name_app` (`name_app`,`author`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table source_container_metrics
# ------------------------------------------------------------

DROP TABLE IF EXISTS `source_container_metrics`;

CREATE TABLE `source_container_metrics` (
  `source_container` int(11) DEFAULT '0',
  `metric` int(11) DEFAULT '0',
  `value` int(11) DEFAULT NULL,
  KEY `source_container` (`source_container`,`metric`),
  KEY `metric` (`metric`),
  CONSTRAINT `source_container_metrics_ibfk_1` FOREIGN KEY (`source_container`) REFERENCES `source_containers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `source_container_metrics_ibfk_2` FOREIGN KEY (`metric`) REFERENCES `metrics` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table source_containers
# ------------------------------------------------------------

DROP TABLE IF EXISTS `source_containers`;

CREATE TABLE `source_containers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project` int(11) DEFAULT NULL,
  `import_id` int(11) DEFAULT NULL,
  `lines_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `project_id` (`project`),
  KEY `import_id` (`import_id`),
  CONSTRAINT `container_project` FOREIGN KEY (`project`) REFERENCES `projects` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `source_containers_ibfk_1` FOREIGN KEY (`import_id`) REFERENCES `import` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `source_containers` WRITE;
/*!40000 ALTER TABLE `source_containers` DISABLE KEYS */;

INSERT INTO `source_containers` (`id`, `project`, `import_id`, `lines_number`)
VALUES
	(1,1,1,23),
	(2,1,7,22);

/*!40000 ALTER TABLE `source_containers` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table throwed_exceptions
# ------------------------------------------------------------

DROP TABLE IF EXISTS `throwed_exceptions`;

CREATE TABLE `throwed_exceptions` (
  `method` int(11) DEFAULT NULL,
  `exception_type` int(11) DEFAULT NULL,
  KEY `method_id` (`method`),
  KEY `type_id` (`exception_type`),
  KEY `FK_6xw665uh0iau0dgu636eypbl7` (`exception_type`),
  CONSTRAINT `exc_throw` FOREIGN KEY (`exception_type`) REFERENCES `types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_6xw665uh0iau0dgu636eypbl7` FOREIGN KEY (`exception_type`) REFERENCES `types` (`import_id`),
  CONSTRAINT `throw_exc` FOREIGN KEY (`method`) REFERENCES `methods` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table type_invocations
# ------------------------------------------------------------

DROP TABLE IF EXISTS `type_invocations`;

CREATE TABLE `type_invocations` (
  `invoker_class` int(11) DEFAULT NULL,
  `invoked_class` int(11) DEFAULT NULL,
  KEY `caller` (`invoker_class`),
  KEY `callee` (`invoked_class`),
  CONSTRAINT `callee_type` FOREIGN KEY (`invoked_class`) REFERENCES `types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `caller_type` FOREIGN KEY (`invoker_class`) REFERENCES `types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table types
# ------------------------------------------------------------

DROP TABLE IF EXISTS `types`;

CREATE TABLE `types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `import_id` int(11) DEFAULT NULL,
  `lines_number` int(11) DEFAULT NULL,
  `src_file_location` varchar(255) DEFAULT NULL,
  `header_file_location` varchar(255) DEFAULT NULL,
  `source_container` int(11) DEFAULT NULL,
  `is_interface` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `header_file_location` (`header_file_location`),
  KEY `import_id` (`import_id`),
  KEY `FK_sgpjfojlaxsoyu0d1kxrwhu0v` (`source_container`),
  CONSTRAINT `types_ibfk_1` FOREIGN KEY (`import_id`) REFERENCES `import` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `types_ibfk_2` FOREIGN KEY (`source_container`) REFERENCES `source_containers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `types` WRITE;
/*!40000 ALTER TABLE `types` DISABLE KEYS */;

INSERT INTO `types` (`id`, `import_id`, `lines_number`, `src_file_location`, `header_file_location`, `source_container`, `is_interface`)
VALUES
	(1,2,8,'minerTest/src/minerTest/gio/ConcreteBaseCalculator.java',NULL,1,'N'),
	(3,4,4,'minerTest/src/minerTest/gio/ICalculator.java',NULL,1,'Y'),
	(5,6,11,'minerTest/src/minerTest/gio/EvolvedCalculator.java',NULL,1,'N'),
	(6,8,6,'minerTest/src/minerTest/matt/AMoreSpecializedOne.java',NULL,2,'N'),
	(7,9,10,'minerTest/src/minerTest/matt/ASpecializedClass.java',NULL,2,'N'),
	(9,11,6,'minerTest/src/minerTest/matt/JustAClass.java',NULL,2,'N');

/*!40000 ALTER TABLE `types` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table types_metrics
# ------------------------------------------------------------

DROP TABLE IF EXISTS `types_metrics`;

CREATE TABLE `types_metrics` (
  `type` int(11) DEFAULT NULL,
  `metric` int(11) DEFAULT NULL,
  `value` double DEFAULT NULL,
  KEY `type` (`type`),
  KEY `metric` (`metric`),
  CONSTRAINT `types_metrics_ibfk_1` FOREIGN KEY (`type`) REFERENCES `types` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `types_metrics_ibfk_2` FOREIGN KEY (`metric`) REFERENCES `metrics` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table used_fields
# ------------------------------------------------------------

DROP TABLE IF EXISTS `used_fields`;

CREATE TABLE `used_fields` (
  `method` int(11) DEFAULT NULL,
  `field_id` int(11) DEFAULT NULL,
  `used_field` int(11) NOT NULL,
  KEY `method_id` (`method`,`field_id`),
  KEY `field_id` (`field_id`),
  KEY `FK_3caqi644wpsltbygxfb5mwdls` (`used_field`),
  CONSTRAINT `field_used_method` FOREIGN KEY (`field_id`) REFERENCES `fields` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_3caqi644wpsltbygxfb5mwdls` FOREIGN KEY (`used_field`) REFERENCES `fields` (`id`),
  CONSTRAINT `method_use_field` FOREIGN KEY (`method`) REFERENCES `methods` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
