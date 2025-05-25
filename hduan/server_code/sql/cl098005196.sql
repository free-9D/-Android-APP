-- MySQL dump 10.13  Distrib 5.7.44, for Linux (x86_64)
--
-- Host: localhost    Database: cl098005196
-- ------------------------------------------------------
-- Server version	5.7.44

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

--
-- Current Database: `cl098005196`
--

/*!40000 DROP DATABASE IF EXISTS `cl098005196`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `cl098005196` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `cl098005196`;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `address` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '地址',
  `name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收货人',
  `phone` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `isdefault` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '否' COMMENT '是否默认地址',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='地址';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,'2025-05-16 07:40:34','地址1','李一','19819881111','是',11),(2,'2025-05-16 07:40:34','地址2','王二','19819882222','是',12),(3,'2025-05-16 07:40:34','地址3','张三','19819883333','是',13),(4,'2025-05-16 07:40:34','地址4','刘四','19819884444','是',14),(5,'2025-05-16 07:40:34','地址5','陈五','19819885555','是',15),(6,'2025-05-16 07:40:34','地址6','杨六','19819886666','是',16),(7,'2025-05-16 07:40:34','地址7','赵七','19819887777','是',17),(8,'2025-05-16 07:40:34','地址8','黄八','19819888888','是',18),(9,'2025-05-16 07:40:34','地址9','周九','19819889999','是',19),(10,'2025-05-16 07:40:34','地址10','吴十','19819880000','是',20);
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cart` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `tablename` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT 'shangpinxinxi' COMMENT '商品表名',
  `goodid` bigint(20) NOT NULL COMMENT '商品id',
  `goodname` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品名称',
  `picture` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片',
  `buynumber` int(11) DEFAULT NULL COMMENT '购买数量',
  `price` double DEFAULT NULL COMMENT '单价',
  `discountprice` double DEFAULT NULL COMMENT '折扣价',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `shangjiazhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商户名称',
  PRIMARY KEY (`id`),
  KEY `goodid` (`goodid`,`price`),
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`goodid`, `price`) REFERENCES `shangpinxinxi` (`id`, `price`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat`
--

DROP TABLE IF EXISTS `chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `adminid` bigint(20) DEFAULT NULL COMMENT '管理员id',
  `ask` longtext COLLATE utf8mb4_unicode_ci COMMENT '提问内容',
  `reply` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  `isreply` int(11) DEFAULT NULL COMMENT '是否回复',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='在线客服';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat`
--

LOCK TABLES `chat` WRITE;
/*!40000 ALTER TABLE `chat` DISABLE KEYS */;
INSERT INTO `chat` VALUES (1,'2025-05-16 07:40:34',1,'提问内容1','回复内容1',1,1),(2,'2025-05-16 07:40:34',2,'提问内容2','回复内容2',2,2),(3,'2025-05-16 07:40:34',3,'提问内容3','回复内容3',3,3),(4,'2025-05-16 07:40:34',4,'提问内容4','回复内容4',4,4),(5,'2025-05-16 07:40:34',5,'提问内容5','回复内容5',5,5),(6,'2025-05-16 07:40:34',6,'提问内容6','回复内容6',6,6),(7,'2025-05-16 07:40:34',7,'提问内容7','回复内容7',7,7),(8,'2025-05-16 07:40:34',8,'提问内容8','回复内容8',8,8),(9,'2025-05-16 07:40:34',9,'提问内容9','回复内容9',9,9),(10,'2025-05-16 07:40:34',10,'提问内容10','回复内容10',10,10);
/*!40000 ALTER TABLE `chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_friend`
--

DROP TABLE IF EXISTS `chat_friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chat_friend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `uid` bigint(20) NOT NULL COMMENT '用户id',
  `fid` bigint(20) NOT NULL COMMENT '好友id',
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `picture` longtext COLLATE utf8mb4_unicode_ci COMMENT '图片',
  `role` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色',
  `tablename` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '表名',
  `alias` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '别名',
  `type` int(11) DEFAULT '0' COMMENT '类型(0:好友申请，1:好友，2:消息)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='好友表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_friend`
--

LOCK TABLES `chat_friend` WRITE;
/*!40000 ALTER TABLE `chat_friend` DISABLE KEYS */;
INSERT INTO `chat_friend` VALUES (1,'2025-05-16 07:40:34',1,1,'名称1','file/chat_friendPicture1.jpg,file/chat_friendPicture2.jpg,file/chat_friendPicture3.jpg','角色1','表名1','别名1',1),(2,'2025-05-16 07:40:34',2,2,'名称2','file/chat_friendPicture2.jpg,file/chat_friendPicture3.jpg,file/chat_friendPicture4.jpg','角色2','表名2','别名2',2),(3,'2025-05-16 07:40:34',3,3,'名称3','file/chat_friendPicture3.jpg,file/chat_friendPicture4.jpg,file/chat_friendPicture5.jpg','角色3','表名3','别名3',3),(4,'2025-05-16 07:40:34',4,4,'名称4','file/chat_friendPicture4.jpg,file/chat_friendPicture5.jpg,file/chat_friendPicture6.jpg','角色4','表名4','别名4',4),(5,'2025-05-16 07:40:34',5,5,'名称5','file/chat_friendPicture5.jpg,file/chat_friendPicture6.jpg,file/chat_friendPicture7.jpg','角色5','表名5','别名5',5),(6,'2025-05-16 07:40:34',6,6,'名称6','file/chat_friendPicture6.jpg,file/chat_friendPicture7.jpg,file/chat_friendPicture8.jpg','角色6','表名6','别名6',6),(7,'2025-05-16 07:40:34',7,7,'名称7','file/chat_friendPicture7.jpg,file/chat_friendPicture8.jpg,file/chat_friendPicture9.jpg','角色7','表名7','别名7',7),(8,'2025-05-16 07:40:34',8,8,'名称8','file/chat_friendPicture8.jpg,file/chat_friendPicture9.jpg,file/chat_friendPicture10.jpg','角色8','表名8','别名8',8),(9,'2025-05-16 07:40:34',9,9,'名称9','file/chat_friendPicture9.jpg,file/chat_friendPicture10.jpg,file/chat_friendPicture11.jpg','角色9','表名9','别名9',9),(10,'2025-05-16 07:40:34',10,10,'名称10','file/chat_friendPicture10.jpg,file/chat_friendPicture11.jpg,file/chat_friendPicture12.jpg','角色10','表名10','别名10',10);
/*!40000 ALTER TABLE `chat_friend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat_message`
--

DROP TABLE IF EXISTS `chat_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chat_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `uid` bigint(20) NOT NULL COMMENT '用户id',
  `fid` bigint(20) NOT NULL COMMENT '好友id',
  `content` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '内容',
  `format` int(11) DEFAULT NULL COMMENT '格式(1:文字，2:图片)',
  `is_read` int(11) DEFAULT '0' COMMENT '消息已读(0:未读，1:已读)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat_message`
--

LOCK TABLES `chat_message` WRITE;
/*!40000 ALTER TABLE `chat_message` DISABLE KEYS */;
INSERT INTO `chat_message` VALUES (1,'2025-05-16 07:40:34',1,1,'内容1',1,1),(2,'2025-05-16 07:40:34',2,2,'内容2',2,2),(3,'2025-05-16 07:40:34',3,3,'内容3',3,3),(4,'2025-05-16 07:40:34',4,4,'内容4',4,4),(5,'2025-05-16 07:40:34',5,5,'内容5',5,5),(6,'2025-05-16 07:40:34',6,6,'内容6',6,6),(7,'2025-05-16 07:40:34',7,7,'内容7',7,7),(8,'2025-05-16 07:40:34',8,8,'内容8',8,8),(9,'2025-05-16 07:40:34',9,9,'内容9',9,9),(10,'2025-05-16 07:40:34',10,10,'内容10',10,10);
/*!40000 ALTER TABLE `chat_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config`
--

DROP TABLE IF EXISTS `config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `value` longtext COLLATE utf8mb4_unicode_ci COMMENT '值',
  `url` longtext COLLATE utf8mb4_unicode_ci COMMENT '链接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='轮播图';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config`
--

LOCK TABLES `config` WRITE;
/*!40000 ALTER TABLE `config` DISABLE KEYS */;
INSERT INTO `config` VALUES (1,'2025-05-16 07:40:34','swiper1','file/swiperPicture1.jpg',NULL),(2,'2025-05-16 07:40:34','swiper2','file/swiperPicture2.jpg',NULL),(3,'2025-05-16 07:40:34','swiper3','file/swiperPicture3.jpg',NULL);
/*!40000 ALTER TABLE `config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `discussduanshipin`
--

DROP TABLE IF EXISTS `discussduanshipin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `discussduanshipin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint(20) NOT NULL COMMENT '关联表id',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `avatarurl` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  `nickname` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `reply` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='短视频评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discussduanshipin`
--

LOCK TABLES `discussduanshipin` WRITE;
/*!40000 ALTER TABLE `discussduanshipin` DISABLE KEYS */;
/*!40000 ALTER TABLE `discussduanshipin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `duanshipin`
--

DROP TABLE IF EXISTS `duanshipin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `duanshipin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `fengmian` longtext COLLATE utf8mb4_unicode_ci COMMENT '封面',
  `biaoti` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `shipin` longtext COLLATE utf8mb4_unicode_ci COMMENT '视频',
  `fabushijian` datetime DEFAULT NULL COMMENT '发布时间',
  `jianjie` longtext COLLATE utf8mb4_unicode_ci COMMENT '简介',
  `shangjiazhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商家账号',
  `shangjiaxingming` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商家姓名',
  `discuss_number` int(11) DEFAULT NULL COMMENT '评论数',
  `thumbsup_number` int(11) DEFAULT '0' COMMENT '赞',
  `crazily_number` int(11) DEFAULT '0' COMMENT '踩',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='短视频';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `duanshipin`
--

LOCK TABLES `duanshipin` WRITE;
/*!40000 ALTER TABLE `duanshipin` DISABLE KEYS */;
INSERT INTO `duanshipin` VALUES (1,'2025-05-16 07:40:34','file/duanshipinFengmian1.jpg,file/duanshipinFengmian2.jpg,file/duanshipinFengmian3.jpg','标题1','','2025-05-16 15:40:34','简介1','商家账号1','商家姓名1',1,1,1),(2,'2025-05-16 07:40:34','file/duanshipinFengmian2.jpg,file/duanshipinFengmian3.jpg,file/duanshipinFengmian4.jpg','标题2','','2025-05-16 15:40:34','简介2','商家账号2','商家姓名2',2,2,2),(3,'2025-05-16 07:40:34','file/duanshipinFengmian3.jpg,file/duanshipinFengmian4.jpg,file/duanshipinFengmian5.jpg','标题3','','2025-05-16 15:40:34','简介3','商家账号3','商家姓名3',3,3,3),(4,'2025-05-16 07:40:34','file/duanshipinFengmian4.jpg,file/duanshipinFengmian5.jpg,file/duanshipinFengmian6.jpg','标题4','','2025-05-16 15:40:34','简介4','商家账号4','商家姓名4',4,4,4),(5,'2025-05-16 07:40:34','file/duanshipinFengmian5.jpg,file/duanshipinFengmian6.jpg,file/duanshipinFengmian7.jpg','标题5','','2025-05-16 15:40:34','简介5','商家账号5','商家姓名5',5,5,5),(6,'2025-05-16 07:40:34','file/duanshipinFengmian6.jpg,file/duanshipinFengmian7.jpg,file/duanshipinFengmian8.jpg','标题6','','2025-05-16 15:40:34','简介6','商家账号6','商家姓名6',6,6,6),(7,'2025-05-16 07:40:34','file/duanshipinFengmian7.jpg,file/duanshipinFengmian8.jpg,file/duanshipinFengmian9.jpg','标题7','','2025-05-16 15:40:34','简介7','商家账号7','商家姓名7',7,7,7),(8,'2025-05-16 07:40:34','file/duanshipinFengmian8.jpg,file/duanshipinFengmian9.jpg,file/duanshipinFengmian10.jpg','标题8','','2025-05-16 15:40:34','简介8','商家账号8','商家姓名8',8,8,8),(9,'2025-05-16 07:40:34','file/duanshipinFengmian9.jpg,file/duanshipinFengmian10.jpg,file/duanshipinFengmian11.jpg','标题9','','2025-05-16 15:40:34','简介9','商家账号9','商家姓名9',9,9,9),(10,'2025-05-16 07:40:34','file/duanshipinFengmian10.jpg,file/duanshipinFengmian11.jpg,file/duanshipinFengmian12.jpg','标题10','','2025-05-16 15:40:34','简介10','商家账号10','商家姓名10',10,10,10);
/*!40000 ALTER TABLE `duanshipin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fankuiyijian`
--

DROP TABLE IF EXISTS `fankuiyijian`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fankuiyijian` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `fankuibiaoti` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '反馈标题',
  `fankuineirong` longtext COLLATE utf8mb4_unicode_ci COMMENT '反馈内容',
  `fankuishijian` datetime DEFAULT NULL COMMENT '反馈时间',
  `shangjiazhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商家账号',
  `shangjiaxingming` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商家姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='反馈意见';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fankuiyijian`
--

LOCK TABLES `fankuiyijian` WRITE;
/*!40000 ALTER TABLE `fankuiyijian` DISABLE KEYS */;
INSERT INTO `fankuiyijian` VALUES (1,'2025-05-16 07:40:34','反馈标题1','反馈内容1','2025-05-16 15:40:34','商家账号1','商家姓名1'),(2,'2025-05-16 07:40:34','反馈标题2','反馈内容2','2025-05-16 15:40:34','商家账号2','商家姓名2'),(3,'2025-05-16 07:40:34','反馈标题3','反馈内容3','2025-05-16 15:40:34','商家账号3','商家姓名3'),(4,'2025-05-16 07:40:34','反馈标题4','反馈内容4','2025-05-16 15:40:34','商家账号4','商家姓名4'),(5,'2025-05-16 07:40:34','反馈标题5','反馈内容5','2025-05-16 15:40:34','商家账号5','商家姓名5'),(6,'2025-05-16 07:40:34','反馈标题6','反馈内容6','2025-05-16 15:40:34','商家账号6','商家姓名6'),(7,'2025-05-16 07:40:34','反馈标题7','反馈内容7','2025-05-16 15:40:34','商家账号7','商家姓名7'),(8,'2025-05-16 07:40:34','反馈标题8','反馈内容8','2025-05-16 15:40:34','商家账号8','商家姓名8'),(9,'2025-05-16 07:40:34','反馈标题9','反馈内容9','2025-05-16 15:40:34','商家账号9','商家姓名9'),(10,'2025-05-16 07:40:34','反馈标题10','反馈内容10','2025-05-16 15:40:34','商家账号10','商家姓名10');
/*!40000 ALTER TABLE `fankuiyijian` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `menujson` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'2025-05-16 07:40:34','[{\"backMenu\":[{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"私信\"],\"appFrontIcon\":\"cuIcon-copy\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"shangpinxinxi\",\"menu\":\"商品信息\",\"menuJump\":\"列表\",\"tableName\":\"shangpinxinxi\"}],\"fontClass\":\"icon-common21\",\"menu\":\"商品信息管理\",\"unicode\":\"&#xee03;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-time\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"shangpinfenlei\",\"menu\":\"商品分类\",\"menuJump\":\"列表\",\"tableName\":\"shangpinfenlei\"}],\"fontClass\":\"icon-common10\",\"menu\":\"商品分类管理\",\"unicode\":\"&#xedd1;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\",\"修改\",\"删除\",\"导出\"],\"menu\":\"全部订单\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\",\"修改\",\"删除\",\"导出\"],\"menu\":\"未支付订单\",\"menuJump\":\"未支付\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\",\"发货\",\"物流\",\"核销\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\",\"修改\",\"删除\",\"导出\",\"物流\"],\"menu\":\"已支付订单\",\"menuJump\":\"已支付\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\",\"物流\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\",\"修改\",\"删除\",\"导出\",\"物流\"],\"menu\":\"已完成订单\",\"menuJump\":\"已完成\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\",\"修改\",\"删除\",\"导出\"],\"menu\":\"已取消订单\",\"menuJump\":\"已取消\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\",\"物流\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\",\"修改\",\"删除\",\"导出\",\"物流\"],\"menu\":\"已退款订单\",\"menuJump\":\"已退款\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\",\"确认收货\",\"物流\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\",\"修改\",\"删除\",\"导出\",\"物流\"],\"menu\":\"已发货订单\",\"menuJump\":\"已发货\",\"tableName\":\"orders\"}],\"fontClass\":\"icon-common43\",\"menu\":\"订单管理\",\"unicode\":\"&#xef27;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-attentionfavor\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"chat\",\"menu\":\"在线客服\",\"menuJump\":\"列表\",\"tableName\":\"chat\"}],\"fontClass\":\"icon-common27\",\"menu\":\"在线客服管理\",\"unicode\":\"&#xee2c;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-link\",\"buttons\":[\"查看\",\"修改\",\"删除\"],\"classname\":\"yijianfankui\",\"menu\":\"意见反馈\",\"menuJump\":\"列表\",\"tableName\":\"yijianfankui\"}],\"fontClass\":\"icon-common29\",\"menu\":\"意见反馈管理\",\"unicode\":\"&#xee2e;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-brand\",\"buttons\":[\"查看\",\"修改\",\"删除\"],\"classname\":\"fankuiyijian\",\"menu\":\"反馈意见\",\"menuJump\":\"列表\",\"tableName\":\"fankuiyijian\"}],\"fontClass\":\"icon-common11\",\"menu\":\"反馈意见管理\",\"unicode\":\"&#xeded;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"查看评论\"],\"appFrontIcon\":\"cuIcon-clothes\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"查看评论\"],\"classname\":\"duanshipin\",\"menu\":\"短视频\",\"menuJump\":\"列表\",\"tableName\":\"duanshipin\"}],\"fontClass\":\"icon-common30\",\"menu\":\"短视频管理\",\"unicode\":\"&#xee30;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-discover\",\"buttons\":[\"查看\",\"修改\"],\"classname\":\"config\",\"menu\":\"轮播图\",\"menuJump\":\"列表\",\"tableName\":\"config\"}],\"fontClass\":\"icon-common33\",\"menu\":\"轮播图管理\",\"unicode\":\"&#xee6a;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-copy\",\"buttons\":[\"新增\",\"查看\",\"修改\"],\"classname\":\"users\",\"menu\":\"管理员\",\"menuJump\":\"列表\",\"tableName\":\"users\"}],\"fontClass\":\"icon-user8\",\"menu\":\"管理员管理\",\"unicode\":\"&#xef9e;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"审核\",\"私信\"],\"appFrontIcon\":\"cuIcon-wenzi\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"审核\"],\"classname\":\"shangjia\",\"menu\":\"商家\",\"menuJump\":\"列表\",\"tableName\":\"shangjia\"}],\"fontClass\":\"icon-user7\",\"menu\":\"商家管理\",\"unicode\":\"&#xef9d;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-explore\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"yonghu\",\"menu\":\"用户\",\"menuJump\":\"列表\",\"tableName\":\"yonghu\"}],\"fontClass\":\"icon-user1\",\"menu\":\"用户管理\",\"unicode\":\"&#xef97;\"}],\"frontMenu\":[{\"child\":[{\"appFrontIcon\":\"cuIcon-list\",\"buttons\":[\"私信\"],\"classname\":\"shangpinxinxi\",\"menu\":\"商品信息\",\"menuJump\":\"列表\",\"tableName\":\"shangpinxinxi\"}],\"menu\":\"商品信息管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-rank\",\"buttons\":[\"查看评论\"],\"classname\":\"duanshipin\",\"menu\":\"短视频\",\"menuJump\":\"列表\",\"tableName\":\"duanshipin\"}],\"menu\":\"短视频管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-brand\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"chat\",\"fontClass\":\"icon-customer-service--fill\",\"menu\":\"在线客服\",\"menuJump\":\"列表\",\"tableName\":\"chat\",\"unicode\":\"&#xedbf;\"}],\"fontClass\":\"icon-customer-service--fill\",\"menu\":\"在线客服\",\"unicode\":\"&#xedbf;\"}],\"hasBackLogin\":\"是\",\"hasBackRegister\":\"否\",\"hasFrontLogin\":\"否\",\"hasFrontRegister\":\"否\",\"pathName\":\"users\",\"roleName\":\"管理员\",\"tableName\":\"users\"},{\"backMenu\":[{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\"],\"menu\":\"全部订单\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\"],\"menu\":\"未支付订单\",\"menuJump\":\"未支付\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\",\"发货\",\"物流\",\"核销\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\",\"物流\"],\"menu\":\"已支付订单\",\"menuJump\":\"已支付\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\",\"物流\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\",\"物流\"],\"menu\":\"已完成订单\",\"menuJump\":\"已完成\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\"],\"menu\":\"已取消订单\",\"menuJump\":\"已取消\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\",\"物流\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\",\"物流\"],\"menu\":\"已退款订单\",\"menuJump\":\"已退款\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\",\"确认收货\",\"物流\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\",\"物流\",\"确认收货\"],\"menu\":\"已发货订单\",\"menuJump\":\"已发货\",\"tableName\":\"orders\"}],\"fontClass\":\"icon-common43\",\"menu\":\"订单管理\",\"unicode\":\"&#xef27;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-link\",\"buttons\":[\"查看\",\"新增\"],\"classname\":\"yijianfankui\",\"menu\":\"意见反馈\",\"menuJump\":\"列表\",\"tableName\":\"yijianfankui\"}],\"fontClass\":\"icon-common29\",\"menu\":\"意见反馈管理\",\"unicode\":\"&#xee2e;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-taxi\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"address\",\"menu\":\"地址\",\"menuJump\":\"列表\",\"tableName\":\"address\"}],\"fontClass\":\"icon-common4\",\"menu\":\"地址管理\",\"unicode\":\"&#xedab;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-full\",\"buttons\":[\"查看\"],\"classname\":\"storeup\",\"menu\":\"我的关注\",\"menuJump\":\"41\",\"tableName\":\"storeup\"}],\"fontClass\":\"icon-common47\",\"menu\":\"我的关注管理\",\"unicode\":\"&#xef63;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-time\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"cart\",\"menu\":\"购物车\",\"menuJump\":\"列表\",\"tableName\":\"cart\"}],\"fontClass\":\"icon-common11\",\"menu\":\"购物车管理\",\"unicode\":\"&#xeded;\"}],\"frontMenu\":[{\"child\":[{\"appFrontIcon\":\"cuIcon-list\",\"buttons\":[\"私信\"],\"classname\":\"shangpinxinxi\",\"menu\":\"商品信息\",\"menuJump\":\"列表\",\"tableName\":\"shangpinxinxi\"}],\"menu\":\"商品信息管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-rank\",\"buttons\":[\"查看评论\"],\"classname\":\"duanshipin\",\"menu\":\"短视频\",\"menuJump\":\"列表\",\"tableName\":\"duanshipin\"}],\"menu\":\"短视频管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-brand\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"chat\",\"fontClass\":\"icon-customer-service--fill\",\"menu\":\"在线客服\",\"menuJump\":\"列表\",\"tableName\":\"chat\",\"unicode\":\"&#xedbf;\"}],\"fontClass\":\"icon-customer-service--fill\",\"menu\":\"在线客服\",\"unicode\":\"&#xedbf;\"}],\"hasBackLogin\":\"否\",\"hasBackRegister\":\"否\",\"hasFrontLogin\":\"是\",\"hasFrontRegister\":\"是\",\"pathName\":\"yonghu\",\"roleName\":\"用户\",\"tableName\":\"yonghu\"},{\"backMenu\":[{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"私信\"],\"appFrontIcon\":\"cuIcon-copy\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"shangpinxinxi\",\"menu\":\"商品信息\",\"menuJump\":\"列表\",\"tableName\":\"shangpinxinxi\"}],\"fontClass\":\"icon-common21\",\"menu\":\"商品信息管理\",\"unicode\":\"&#xee03;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-time\",\"buttons\":[\"查看\"],\"classname\":\"shangpinfenlei\",\"menu\":\"商品分类\",\"menuJump\":\"列表\",\"tableName\":\"shangpinfenlei\"}],\"fontClass\":\"icon-common10\",\"menu\":\"商品分类管理\",\"unicode\":\"&#xedd1;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\"],\"menu\":\"全部订单\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\"],\"menu\":\"未支付订单\",\"menuJump\":\"未支付\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\",\"发货\",\"物流\",\"核销\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\",\"物流\",\"发货\"],\"menu\":\"已支付订单\",\"menuJump\":\"已支付\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\",\"物流\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\",\"物流\"],\"menu\":\"已完成订单\",\"menuJump\":\"已完成\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\"],\"menu\":\"已取消订单\",\"menuJump\":\"已取消\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\",\"物流\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\",\"物流\"],\"menu\":\"已退款订单\",\"menuJump\":\"已退款\",\"tableName\":\"orders\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\",\"确认收货\",\"物流\"],\"appFrontIcon\":\"cuIcon-circle\",\"buttons\":[\"查看\",\"物流\"],\"menu\":\"已发货订单\",\"menuJump\":\"已发货\",\"tableName\":\"orders\"}],\"fontClass\":\"icon-common43\",\"menu\":\"订单管理\",\"unicode\":\"&#xef27;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-attentionfavor\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"chat\",\"menu\":\"在线客服\",\"menuJump\":\"列表\",\"tableName\":\"chat\"}],\"fontClass\":\"icon-common27\",\"menu\":\"在线客服管理\",\"unicode\":\"&#xee2c;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-brand\",\"buttons\":[\"查看\",\"新增\"],\"classname\":\"fankuiyijian\",\"menu\":\"反馈意见\",\"menuJump\":\"列表\",\"tableName\":\"fankuiyijian\"}],\"fontClass\":\"icon-common11\",\"menu\":\"反馈意见管理\",\"unicode\":\"&#xeded;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"查看评论\"],\"appFrontIcon\":\"cuIcon-clothes\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"查看评论\"],\"classname\":\"duanshipin\",\"menu\":\"短视频\",\"menuJump\":\"列表\",\"tableName\":\"duanshipin\"}],\"fontClass\":\"icon-common30\",\"menu\":\"短视频管理\",\"unicode\":\"&#xee30;\"}],\"frontMenu\":[{\"child\":[{\"appFrontIcon\":\"cuIcon-list\",\"buttons\":[\"私信\"],\"classname\":\"shangpinxinxi\",\"menu\":\"商品信息\",\"menuJump\":\"列表\",\"tableName\":\"shangpinxinxi\"}],\"menu\":\"商品信息管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-rank\",\"buttons\":[\"查看评论\"],\"classname\":\"duanshipin\",\"menu\":\"短视频\",\"menuJump\":\"列表\",\"tableName\":\"duanshipin\"}],\"menu\":\"短视频管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-brand\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"chat\",\"fontClass\":\"icon-customer-service--fill\",\"menu\":\"在线客服\",\"menuJump\":\"列表\",\"tableName\":\"chat\",\"unicode\":\"&#xedbf;\"}],\"fontClass\":\"icon-customer-service--fill\",\"menu\":\"在线客服\",\"unicode\":\"&#xedbf;\"}],\"hasBackLogin\":\"是\",\"hasBackRegister\":\"是\",\"hasFrontLogin\":\"否\",\"hasFrontRegister\":\"否\",\"pathName\":\"shangjia\",\"roleName\":\"商家\",\"tableName\":\"shangjia\"}]');
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `orderid` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `tablename` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT 'shangpinxinxi' COMMENT '商品表名',
  `goodid` bigint(20) NOT NULL COMMENT '商品id',
  `goodname` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品名称',
  `picture` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片',
  `buynumber` int(11) DEFAULT NULL COMMENT '购买数量',
  `price` double DEFAULT NULL COMMENT '单价',
  `discountprice` double DEFAULT NULL COMMENT '折扣价',
  `total` double DEFAULT NULL COMMENT '总价',
  `discounttotal` double DEFAULT NULL COMMENT '折扣总价格',
  `type` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支付类型',
  `status` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单状态',
  `address` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
  `tel` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电话',
  `consignee` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收货人',
  `remark` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `logistics` longtext COLLATE utf8mb4_unicode_ci COMMENT '物流',
  `role` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户角色',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `shangjiazhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商户名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `orderid` (`orderid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品订单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shangjia`
--

DROP TABLE IF EXISTS `shangjia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shangjia` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `shangjiazhanghao` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商家账号',
  `mima` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `shangjiaxingming` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商家姓名',
  `touxiang` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像',
  `xingbie` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `lianxidianhua` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系电话',
  `zizhileixing` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '资质类型',
  `zizhizhengming` longtext COLLATE utf8mb4_unicode_ci COMMENT '资质证明',
  `sfsh` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '待审核' COMMENT '是否审核',
  `shhf` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  `pquestion` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密保问题',
  `panswer` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密保答案',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shangjiazhanghao` (`shangjiazhanghao`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shangjia`
--

LOCK TABLES `shangjia` WRITE;
/*!40000 ALTER TABLE `shangjia` DISABLE KEYS */;
INSERT INTO `shangjia` VALUES (61,'2025-05-16 07:40:34','商家账号1','123456','商家姓名1','file/shangjiaTouxiang1.jpg','男','19819881111','实人认证','file/shangjiaZizhizhengming1.jpg','是','','密保问题1','密保答案1'),(62,'2025-05-16 07:40:34','商家账号2','123456','商家姓名2','file/shangjiaTouxiang2.jpg','男','19819881112','实人认证','file/shangjiaZizhizhengming2.jpg','是','','密保问题2','密保答案2'),(63,'2025-05-16 07:40:34','商家账号3','123456','商家姓名3','file/shangjiaTouxiang3.jpg','男','19819881113','实人认证','file/shangjiaZizhizhengming3.jpg','是','','密保问题3','密保答案3'),(64,'2025-05-16 07:40:34','商家账号4','123456','商家姓名4','file/shangjiaTouxiang4.jpg','男','19819881114','实人认证','file/shangjiaZizhizhengming4.jpg','是','','密保问题4','密保答案4'),(65,'2025-05-16 07:40:34','商家账号5','123456','商家姓名5','file/shangjiaTouxiang5.jpg','男','19819881115','实人认证','file/shangjiaZizhizhengming5.jpg','是','','密保问题5','密保答案5'),(66,'2025-05-16 07:40:34','商家账号6','123456','商家姓名6','file/shangjiaTouxiang6.jpg','男','19819881116','实人认证','file/shangjiaZizhizhengming6.jpg','是','','密保问题6','密保答案6'),(67,'2025-05-16 07:40:34','商家账号7','123456','商家姓名7','file/shangjiaTouxiang7.jpg','男','19819881117','实人认证','file/shangjiaZizhizhengming7.jpg','是','','密保问题7','密保答案7'),(68,'2025-05-16 07:40:34','商家账号8','123456','商家姓名8','file/shangjiaTouxiang8.jpg','男','19819881118','实人认证','file/shangjiaZizhizhengming8.jpg','是','','密保问题8','密保答案8'),(69,'2025-05-16 07:40:34','商家账号9','123456','商家姓名9','file/shangjiaTouxiang9.jpg','男','19819881119','实人认证','file/shangjiaZizhizhengming9.jpg','是','','密保问题9','密保答案9'),(70,'2025-05-16 07:40:34','商家账号10','123456','商家姓名10','file/shangjiaTouxiang10.jpg','男','198198811110','实人认证','file/shangjiaZizhizhengming10.jpg','是','','密保问题10','密保答案10');
/*!40000 ALTER TABLE `shangjia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shangpinfenlei`
--

DROP TABLE IF EXISTS `shangpinfenlei`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shangpinfenlei` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `shangpinfenlei` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品分类',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shangpinfenlei`
--

LOCK TABLES `shangpinfenlei` WRITE;
/*!40000 ALTER TABLE `shangpinfenlei` DISABLE KEYS */;
INSERT INTO `shangpinfenlei` VALUES (1,'2025-05-16 07:40:34','商品分类1'),(2,'2025-05-16 07:40:34','商品分类2'),(3,'2025-05-16 07:40:34','商品分类3'),(4,'2025-05-16 07:40:34','商品分类4'),(5,'2025-05-16 07:40:34','商品分类5'),(6,'2025-05-16 07:40:34','商品分类6'),(7,'2025-05-16 07:40:34','商品分类7'),(8,'2025-05-16 07:40:34','商品分类8'),(9,'2025-05-16 07:40:34','商品分类9'),(10,'2025-05-16 07:40:34','商品分类10');
/*!40000 ALTER TABLE `shangpinfenlei` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shangpinxinxi`
--

DROP TABLE IF EXISTS `shangpinxinxi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shangpinxinxi` (
  `id`  bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `shangpinmingcheng` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品名称',
  `shangpinfenlei` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品分类',
  `shangpinpinpai` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品品牌',
  `shangpinguige` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品规格',
  `shangpintupian` longtext COLLATE utf8mb4_unicode_ci COMMENT '商品图片',
  `shangpinjianjie` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品简介',
  `shangpinxiangqing` longtext COLLATE utf8mb4_unicode_ci COMMENT '商品详情',
  `price` double DEFAULT NULL COMMENT '价格',
  `shangjiazhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商家账号',
  `shangjiaxingming` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商家姓名',
  PRIMARY KEY (`id`),
  KEY `shangpinxinxi_price` (`id`,`price`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shangpinxinxi`
--

LOCK TABLES `shangpinxinxi` WRITE;
/*!40000 ALTER TABLE `shangpinxinxi` DISABLE KEYS */;
INSERT INTO `shangpinxinxi` VALUES (1,'2025-05-16 07:40:34','商品名称1','商品分类1','商品品牌1','商品规格1','file/shangpinxinxiShangpintupian1.jpg,file/shangpinxinxiShangpintupian2.jpg,file/shangpinxinxiShangpintupian3.jpg','商品简介1','商品详情1',99.9,'商家账号1','商家姓名1'),(2,'2025-05-16 07:40:34','商品名称2','商品分类2','商品品牌2','商品规格2','file/shangpinxinxiShangpintupian2.jpg,file/shangpinxinxiShangpintupian3.jpg,file/shangpinxinxiShangpintupian4.jpg','商品简介2','商品详情2',99.9,'商家账号2','商家姓名2'),(3,'2025-05-16 07:40:34','商品名称3','商品分类3','商品品牌3','商品规格3','file/shangpinxinxiShangpintupian3.jpg,file/shangpinxinxiShangpintupian4.jpg,file/shangpinxinxiShangpintupian5.jpg','商品简介3','商品详情3',99.9,'商家账号3','商家姓名3'),(4,'2025-05-16 07:40:34','商品名称4','商品分类4','商品品牌4','商品规格4','file/shangpinxinxiShangpintupian4.jpg,file/shangpinxinxiShangpintupian5.jpg,file/shangpinxinxiShangpintupian6.jpg','商品简介4','商品详情4',99.9,'商家账号4','商家姓名4'),(5,'2025-05-16 07:40:34','商品名称5','商品分类5','商品品牌5','商品规格5','file/shangpinxinxiShangpintupian5.jpg,file/shangpinxinxiShangpintupian6.jpg,file/shangpinxinxiShangpintupian7.jpg','商品简介5','商品详情5',99.9,'商家账号5','商家姓名5'),(6,'2025-05-16 07:40:34','商品名称6','商品分类6','商品品牌6','商品规格6','file/shangpinxinxiShangpintupian6.jpg,file/shangpinxinxiShangpintupian7.jpg,file/shangpinxinxiShangpintupian8.jpg','商品简介6','商品详情6',99.9,'商家账号6','商家姓名6'),(7,'2025-05-16 07:40:34','商品名称7','商品分类7','商品品牌7','商品规格7','file/shangpinxinxiShangpintupian7.jpg,file/shangpinxinxiShangpintupian8.jpg,file/shangpinxinxiShangpintupian9.jpg','商品简介7','商品详情7',99.9,'商家账号7','商家姓名7'),(8,'2025-05-16 07:40:34','商品名称8','商品分类8','商品品牌8','商品规格8','file/shangpinxinxiShangpintupian8.jpg,file/shangpinxinxiShangpintupian9.jpg,file/shangpinxinxiShangpintupian10.jpg','商品简介8','商品详情8',99.9,'商家账号8','商家姓名8'),(9,'2025-05-16 07:40:34','商品名称9','商品分类9','商品品牌9','商品规格9','file/shangpinxinxiShangpintupian9.jpg,file/shangpinxinxiShangpintupian10.jpg,file/shangpinxinxiShangpintupian11.jpg','商品简介9','商品详情9',99.9,'商家账号9','商家姓名9'),(10,'2025-05-16 07:40:34','商品名称10','商品分类10','商品品牌10','商品规格10','file/shangpinxinxiShangpintupian10.jpg,file/shangpinxinxiShangpintupian11.jpg,file/shangpinxinxiShangpintupian12.jpg','商品简介10','商品详情10',99.9,'商家账号10','商家姓名10');
/*!40000 ALTER TABLE `shangpinxinxi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `storeup`
--

DROP TABLE IF EXISTS `storeup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `storeup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint(20) DEFAULT NULL COMMENT 'refid',
  `tablename` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '表名',
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `picture` longtext COLLATE utf8mb4_unicode_ci COMMENT '图片',
  `type` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '1' COMMENT '类型(1:收藏,21:赞,22:踩,31:竞拍参与,41:关注)',
  `inteltype` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '推荐类型',
  `remark` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='我的收藏';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `storeup`
--

LOCK TABLES `storeup` WRITE;
/*!40000 ALTER TABLE `storeup` DISABLE KEYS */;
/*!40000 ALTER TABLE `storeup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `token`
--

DROP TABLE IF EXISTS `token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `tablename` varchar(100) DEFAULT NULL COMMENT '表名',
  `role` varchar(100) DEFAULT NULL COMMENT '角色',
  `token` varchar(200) NOT NULL COMMENT '密码',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='token表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token`
--

LOCK TABLES `token` WRITE;
/*!40000 ALTER TABLE `token` DISABLE KEYS */;
INSERT INTO `token` VALUES (1,51,'用户账号1','yonghu','用户','lvongfsmfco7c5ndiu7kypojmql6zfa7','2025-05-16 07:46:46','2025-05-16 08:46:46');
/*!40000 ALTER TABLE `token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `username` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `role` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '管理员' COMMENT '角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2025-05-16 07:40:34','admin','admin','管理员');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `yijianfankui`
--

DROP TABLE IF EXISTS `yijianfankui`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `yijianfankui` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `fankuibiaoti` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '反馈标题',
  `fankuineirong` longtext COLLATE utf8mb4_unicode_ci COMMENT '反馈内容',
  `fankuishijian` datetime DEFAULT NULL COMMENT '反馈时间',
  `yonghuzhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户账号',
  `yonghuxingming` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='意见反馈';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `yijianfankui`
--

LOCK TABLES `yijianfankui` WRITE;
/*!40000 ALTER TABLE `yijianfankui` DISABLE KEYS */;
INSERT INTO `yijianfankui` VALUES (1,'2025-05-16 07:40:34','反馈标题1','反馈内容1','2025-05-16 15:40:34','用户账号1','用户姓名1'),(2,'2025-05-16 07:40:34','反馈标题2','反馈内容2','2025-05-16 15:40:34','用户账号2','用户姓名2'),(3,'2025-05-16 07:40:34','反馈标题3','反馈内容3','2025-05-16 15:40:34','用户账号3','用户姓名3'),(4,'2025-05-16 07:40:34','反馈标题4','反馈内容4','2025-05-16 15:40:34','用户账号4','用户姓名4'),(5,'2025-05-16 07:40:34','反馈标题5','反馈内容5','2025-05-16 15:40:34','用户账号5','用户姓名5'),(6,'2025-05-16 07:40:34','反馈标题6','反馈内容6','2025-05-16 15:40:34','用户账号6','用户姓名6'),(7,'2025-05-16 07:40:34','反馈标题7','反馈内容7','2025-05-16 15:40:34','用户账号7','用户姓名7'),(8,'2025-05-16 07:40:34','反馈标题8','反馈内容8','2025-05-16 15:40:34','用户账号8','用户姓名8'),(9,'2025-05-16 07:40:34','反馈标题9','反馈内容9','2025-05-16 15:40:34','用户账号9','用户姓名9'),(10,'2025-05-16 07:40:34','反馈标题10','反馈内容10','2025-05-16 15:40:34','用户账号10','用户姓名10');
/*!40000 ALTER TABLE `yijianfankui` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `yonghu`
--

DROP TABLE IF EXISTS `yonghu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `yonghu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `yonghuzhanghao` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户账号',
  `mima` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `yonghuxingming` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户姓名',
  `touxiang` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像',
  `xingbie` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `lianxidianhua` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系电话',
  `pquestion` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密保问题',
  `panswer` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密保答案',
  `money` double DEFAULT '0' COMMENT '余额',
  PRIMARY KEY (`id`),
  UNIQUE KEY `yonghuzhanghao` (`yonghuzhanghao`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `yonghu`
--

LOCK TABLES `yonghu` WRITE;
/*!40000 ALTER TABLE `yonghu` DISABLE KEYS */;
INSERT INTO `yonghu` VALUES (51,'2025-05-16 07:40:34','用户账号1','123456','用户姓名1','file/yonghuTouxiang1.jpg','男','19819881111','密保问题1','密保答案1',200),(52,'2025-05-16 07:40:34','用户账号2','123456','用户姓名2','file/yonghuTouxiang2.jpg','男','19819881112','密保问题2','密保答案2',200),(53,'2025-05-16 07:40:34','用户账号3','123456','用户姓名3','file/yonghuTouxiang3.jpg','男','19819881113','密保问题3','密保答案3',200),(54,'2025-05-16 07:40:34','用户账号4','123456','用户姓名4','file/yonghuTouxiang4.jpg','男','19819881114','密保问题4','密保答案4',200),(55,'2025-05-16 07:40:34','用户账号5','123456','用户姓名5','file/yonghuTouxiang5.jpg','男','19819881115','密保问题5','密保答案5',200),(56,'2025-05-16 07:40:34','用户账号6','123456','用户姓名6','file/yonghuTouxiang6.jpg','男','19819881116','密保问题6','密保答案6',200),(57,'2025-05-16 07:40:34','用户账号7','123456','用户姓名7','file/yonghuTouxiang7.jpg','男','19819881117','密保问题7','密保答案7',200),(58,'2025-05-16 07:40:34','用户账号8','123456','用户姓名8','file/yonghuTouxiang8.jpg','男','19819881118','密保问题8','密保答案8',200),(59,'2025-05-16 07:40:34','用户账号9','123456','用户姓名9','file/yonghuTouxiang9.jpg','男','19819881119','密保问题9','密保答案9',200),(60,'2025-05-16 07:40:34','用户账号10','123456','用户姓名10','file/yonghuTouxiang10.jpg','男','198198811110','密保问题10','密保答案10',200);
/*!40000 ALTER TABLE `yonghu` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-17 14:24:42
