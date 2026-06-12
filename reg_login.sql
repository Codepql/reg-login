-- MySQL dump 10.13  Distrib 8.4.9, for Win64 (x86_64)
--
-- Host: localhost    Database: reg_login
-- ------------------------------------------------------
-- Server version	8.4.9

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `content` text NOT NULL,
  `author` varchar(50) NOT NULL,
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `cover_img` varchar(255) DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `like_count` bigint DEFAULT '0',
  `view_count` bigint DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
INSERT INTO `article` VALUES (1,'Spring Boot学习','今天完成了JWT登录认证。','admin','2026-06-05 10:49:24','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',1,50,1),(2,'Spring Boot太好玩了','JWT终于搞懂了','admins','2026-06-05 11:10:24','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',2,5,0),(3,'爱的那么认真，爱的那么深','JWT终于搞懂了11','pql','2026-06-05 11:11:00','http://localhost:8083/upload/eb4a8aae-92aa-4c16-940f-6f46143eebe8.jfif',3,0,0),(5,'山居秋暝','空山新雨后，天气晚来秋。明月松间照，清泉石上流。竹喧归浣女，莲动下渔舟。随意春芳歇，王孙自可留。','admin','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',1,0,3),(6,'枫桥夜泊','月落乌啼霜满天，江枫渔火对愁眠。姑苏城外寒山寺，夜半钟声到客船。','admin','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',1,6,6),(7,'出塞','秦时明月汉时关，万里长征人未还。但使龙城飞将在，不教胡马度阴山。','admin','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',2,1,1),(8,'渭城曲','渭城朝雨浥轻尘，客舍青青柳色新。劝君更尽一杯酒，西出阳关无故人。','admin','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',6,1,23),(9,'江雪','千山鸟飞绝，万径人踪灭。孤舟蓑笠翁，独钓寒江雪。','admin','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',7,1,2),(10,'虞美人·春花秋月','春花秋月何时了？往事知多少。小楼昨夜又东风，故国不堪回首月明中。雕栏玉砌应犹在，只是朱颜改。问君能有几多愁？恰似一江春水向东流。','admins','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',8,1,1),(11,'水调歌头·明月几时有','明月几时有？把酒问青天。不知天上宫阙，今夕是何年。我欲乘风归去，又恐琼楼玉宇，高处不胜寒。','admins','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',8,0,2),(12,'念奴娇·赤壁怀古','大江东去，浪淘尽，千古风流人物。故垒西边，人道是，三国周郎赤壁。乱石穿空，惊涛拍岸，卷起千堆雪。','admins','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',7,0,3),(13,'声声慢·寻寻觅觅','寻寻觅觅，冷冷清清，凄凄惨惨戚戚。乍暖还寒时候，最难将息。三杯两盏淡酒，怎敌他、晚来风急。','admins','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',6,0,0),(14,'雨霖铃·寒蝉凄切','寒蝉凄切，对长亭晚，骤雨初歇。都门帐饮无绪，留恋处，兰舟催发。执手相看泪眼，竟无语凝噎。','admins','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',6,8,0),(15,'饮酒·其五','结庐在人境，而无车马喧。问君何能尔？心远地自偏。采菊东篱下，悠然见南山。山气日夕佳，飞鸟相与还。','pql','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',7,0,0),(16,'春江花月夜节选','春江潮水连海平，海上明月共潮生。滟滟随波千万里，何处春江无月明。江流宛转绕芳甸，月照花林皆似霰。','pql','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',8,0,0),(17,'登高','风急天高猿啸哀，渚清沙白鸟飞回。无边落木萧萧下，不尽长江滚滚来。万里悲秋常作客，百年多病独登台。','pql','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',6,0,0),(18,'无题·相见时难','相见时难别亦难，东风无力百花残。春蚕到死丝方尽，蜡炬成灰泪始干。晓镜但愁云鬓改，夜吟应觉月光寒。','pql','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',7,0,0),(19,'题西林壁','横看成岭侧成峰，远近高低各不同。不识庐山真面目，只缘身在此山中。','pql','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',8,9,0),(20,'浣溪沙·一曲新词','一曲新词酒一杯，去年天气旧亭台。夕阳西下几时回？无可奈何花落去，似曾相识燕归来。小园香径独徘徊。','mnz','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',1,0,0),(21,'破阵子·春景','燕子来时新社，梨花落后清明。池上碧苔三四点，叶底黄鹂一两声，日长飞絮轻。','mnz','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',2,0,0),(22,'卜算子·咏梅','驿外断桥边，寂寞开无主。已是黄昏独自愁，更着风和雨。无意苦争春，一任群芳妒。零落成泥碾作尘，只有香如故。','mnz','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',3,0,0),(23,'临江仙·夜登小阁','忆昔午桥桥上饮，坐中多是豪英。长沟流月去无声。杏花疏影里，吹笛到天明。','mnz','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',1,0,0),(24,'清平乐·村居','茅檐低小，溪上青青草。醉里吴音相媚好，白发谁家翁媪？大儿锄豆溪东，中儿正织鸡笼。','mnz','2026-06-05 14:59:53','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',2,0,0),(25,'爱的那么认真，爱的那么深','JWT终于搞懂了11','pql','2026-06-08 11:38:49','http://localhost:8083/upload/eb4a8aae-92aa-4c16-940f-6f46143eebe8.jfif',3,0,0),(31,'静夜思.李白','曾经沧海难为水，除却巫山不是云，我本倾心向明月','pql','2026-06-08 16:44:25','http://localhost:8083/upload/eb4a8aae-92aa-4c16-940f-6f46143eebe8.jfif',1,0,0),(32,'今天学习Spring Boot1','雨过天晴！','pql','2026-06-09 10:20:18','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',1,0,0),(33,'今天学习Spring Boot1','雨过天晴！','pql','2026-06-09 10:21:10','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',2,0,0),(34,'静夜思.李白','曾经沧海难为水','pql','2026-06-09 10:25:14','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',1,0,0),(36,'静夜思.李白','曾经沧海难为水，除却巫山不是云','mnz','2026-06-09 17:42:10','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',1,0,0),(37,'静夜思.李白','曾经沧海难为水，除却巫山不是云','mnz','2026-06-09 17:42:13','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',1,0,0),(38,'静夜思.李白','曾经沧海难为水，除却巫山不是云，我本倾心向明月','pql','2026-06-10 11:14:28','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',1,0,0),(39,'静夜思.李白','曾经沧海难为水，除却巫山不是云，我本倾心向明月','pql','2026-06-10 11:18:07','http://localhost:8083/upload/849b4545-f6ff-4d94-b3d5-782adf8ca9e4.webp',1,0,0);
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_name` varchar(50) NOT NULL,
  `create_user` varchar(100) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'vue','admin','2026-06-08 18:02:06'),(2,'Spring Boot','admin','2026-06-08 18:02:06'),(3,'Redis','pql','2026-06-08 19:55:34'),(6,'古诗','pql','2026-06-09 10:38:06'),(7,'杜甫','pql','2026-06-09 10:38:16'),(8,'伤感','pql','2026-06-09 10:38:24');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `article_id` bigint NOT NULL,
  `content` varchar(1000) NOT NULL,
  `create_user` varchar(100) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,6,'写得不错','pql','2026-06-09 11:44:26'),(2,6,'写得很不错','pql','2026-06-09 11:44:51'),(3,8,'写得很不错','pql','2026-06-09 11:45:02'),(4,8,'写得很6','pql','2026-06-09 11:45:10'),(5,12,'写得很very good','pql','2026-06-09 11:45:25'),(6,16,'写得很very good','pql','2026-06-09 11:45:29'),(7,16,'写得很very good','mnz','2026-06-09 11:46:01'),(9,6,'写得很very good','mnz','2026-06-09 11:46:13'),(11,4,'写得很very good','mnz','2026-06-09 11:46:31');
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operation_log`
--

DROP TABLE IF EXISTS `operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `operation` varchar(100) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operation_log`
--

LOCK TABLES `operation_log` WRITE;
/*!40000 ALTER TABLE `operation_log` DISABLE KEYS */;
INSERT INTO `operation_log` VALUES (1,'pql','新增文章','2026-06-10 11:14:28'),(2,'pql','新增文章','2026-06-10 11:18:07'),(3,'pql','修改文章','2026-06-10 11:19:29');
/*!40000 ALTER TABLE `operation_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seckill_goods`
--

DROP TABLE IF EXISTS `seckill_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seckill_goods` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `stock` int NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seckill_goods`
--

LOCK TABLES `seckill_goods` WRITE;
/*!40000 ALTER TABLE `seckill_goods` DISABLE KEYS */;
INSERT INTO `seckill_goods` VALUES (1,'iPhone 17',8,'2026-06-11 11:03:54');
/*!40000 ALTER TABLE `seckill_goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seckill_order`
--

DROP TABLE IF EXISTS `seckill_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seckill_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `goods_id` bigint NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seckill_order`
--

LOCK TABLES `seckill_order` WRITE;
/*!40000 ALTER TABLE `seckill_order` DISABLE KEYS */;
INSERT INTO `seckill_order` VALUES (1,2,1,'2026-06-11 14:43:34'),(2,4,1,'2026-06-11 14:45:06'),(3,2,1,'2026-06-11 15:26:58'),(4,4,1,'2026-06-11 15:27:09');
/*!40000 ALTER TABLE `seckill_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `role` varchar(20) DEFAULT 'user',
  `status` tinyint DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (2,'pql','$2a$10$uXTUtraZ1vKv/RO9N6hh.uN9jFu5qUjW89wwNqSJ3k/CLUFMOSq96','2026-06-05 10:28:43','admin',1),(3,'admins','$2a$10$FjgRpQQsOk5P2idkkH36PeV5EQThlJZL0uG8SGZDXgO2Qj6hWHdge','2026-06-05 10:29:00','admin',1),(4,'mnz','$2a$10$vdTTaLd2WHPV5fY57FwXhegJYURdwBjvfPtDOJsswHGzsw5IDU50a','2026-06-05 10:29:10','user',0),(5,'admin','$2a$10$3cRL0zh6dnTzV8zJyAG7UuZFiVr/VGpa0lR1/Ft8SbRWyupi5KHUe','2026-06-05 14:57:01','admin',1),(6,'curry','$2a$10$Gyhlc2tIR2KCgtwJ.Nbop.c0HNA64zwFmEDyuuo3ze/Y5XpkYhV72','2026-06-09 19:55:40','user',1),(7,'james','$2a$10$f1OjZWwI9KRTH8AYPOca/uKuJgdNGI.uKj8iBVKHmMYIgJyVOnVXq','2026-06-09 19:56:09','user',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-12 10:33:31
