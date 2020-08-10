/*
MySQL Backup
Database: ppjoke
Backup Time: 2020-01-15 17:12:07
*/

SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `ppjoke`.`hibernate_sequence`;
DROP TABLE IF EXISTS `ppjoke`.`table_feed_comment_like`;
DROP TABLE IF EXISTS `ppjoke`.`table_feed_comment_ugc`;
DROP TABLE IF EXISTS `ppjoke`.`table_feeds_comment`;
DROP TABLE IF EXISTS `ppjoke`.`table_hot_feeds`;
DROP TABLE IF EXISTS `ppjoke`.`table_tag_list`;
DROP TABLE IF EXISTS `ppjoke`.`table_tag_list_like`;
DROP TABLE IF EXISTS `ppjoke`.`table_ugc`;
DROP TABLE IF EXISTS `ppjoke`.`table_ugc_like`;
DROP TABLE IF EXISTS `ppjoke`.`table_user`;
DROP TABLE IF EXISTS `ppjoke`.`table_user_banner`;
DROP TABLE IF EXISTS `ppjoke`.`table_user_follow`;
DROP TABLE IF EXISTS `ppjoke`.`table_watch_history`;
DROP TABLE IF EXISTS `ppjoke`.`user`;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;
CREATE TABLE `table_feed_comment_like` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `has_like` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci COMMENT='帖子评论的点赞关系表，记录的是那些人给这个评论点赞了\n';
CREATE TABLE `table_feed_comment_ugc` (
  `comment_id` bigint(30) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `like_count` int(255) DEFAULT '0',
  `share_count` int(255) DEFAULT '0',
  `comment_count` int(255) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=769 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci COMMENT='帖子评论的互动数据表，记录了一条评论的点赞，分享，评论的数量';
CREATE TABLE `table_feeds_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment_count` int(11) DEFAULT NULL,
  `comment_id` bigint(20) DEFAULT NULL,
  `comment_text` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `comment_type` int(11) DEFAULT '1',
  `create_time` bigint(20) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `image_url` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `like_count` int(11) DEFAULT NULL,
  `video_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `width` int(11) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1139 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='记录下的是所有帖子的所有评论信息';
CREATE TABLE `table_hot_feeds` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_icon` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `activity_text` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `author_id` bigint(20) DEFAULT NULL,
  `video_cover` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_time` bigint(20) DEFAULT NULL,
  `feeds_text` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `video_height` int(11) DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `item_type` int(11) DEFAULT NULL,
  `video_url` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `video_width` int(11) DEFAULT NULL,
  `duration` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=429 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='记录所有帖子数据';
CREATE TABLE `table_tag_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_icon` varchar(1000) DEFAULT NULL,
  `background` varchar(1000) DEFAULT NULL,
  `enter_num` int(11) DEFAULT NULL,
  `feed_num` int(11) DEFAULT NULL,
  `follow_num` int(11) DEFAULT NULL,
  `icon` varchar(1000) DEFAULT NULL,
  `intro` varchar(255) DEFAULT NULL,
  `tag_id` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci COMMENT='所有标签信息';
CREATE TABLE `table_tag_list_like` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `has_like` bit(1) DEFAULT b'0',
  `tag_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci COMMENT='某个用户对该标签是否已喜欢';
CREATE TABLE `table_ugc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment_count` int(11) DEFAULT '0',
  `item_id` bigint(20) DEFAULT NULL,
  `like_count` int(11) DEFAULT '0',
  `share_count` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=423 DEFAULT CHARSET=utf8 COMMENT='用户行为互动';
CREATE TABLE `table_ugc_like` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `has_like` tinyint(255) DEFAULT '0',
  `has_diss` tinyint(255) DEFAULT '0',
  `has_favorite` tinyint(255) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8 COMMENT='记录的是帖子点赞信息';
CREATE TABLE `table_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `avatar` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `follow_count` int(11) DEFAULT NULL,
  `like_count` int(11) DEFAULT NULL,
  `top_count` int(11) DEFAULT NULL,
  `qq_openid` varchar(255) DEFAULT NULL,
  `comment_count` int(11) DEFAULT NULL,
  `expires_time` bigint(20) DEFAULT NULL,
  `favorite_count` int(11) DEFAULT NULL,
  `feed_count` int(11) DEFAULT NULL,
  `follower_count` int(11) DEFAULT NULL,
  `history_count` int(11) DEFAULT NULL,
  `score` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1260 DEFAULT CHARSET=utf8 COMMENT='用户信息表';
CREATE TABLE `table_user_banner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `banner_url` longtext,
  `detail_url` longtext,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;
CREATE TABLE `table_user_follow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `follow_user_id` bigint(20) DEFAULT NULL,
  `has_follow` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci COMMENT='维护用户之前关注的关系';
CREATE TABLE `table_watch_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_id` bigint(30) DEFAULT NULL,
  `user_id` bigint(30) DEFAULT NULL,
  `time` bigint(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci COMMENT='用户观看帖子的历史记录';
CREATE TABLE `user` (
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `number` decimal(13,0) DEFAULT NULL,
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
BEGIN;
LOCK TABLES `ppjoke`.`hibernate_sequence` WRITE;
DELETE FROM `ppjoke`.`hibernate_sequence`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `ppjoke`.`table_feed_comment_like` WRITE;
DELETE FROM `ppjoke`.`table_feed_comment_like`;
INSERT INTO `ppjoke`.`table_feed_comment_like` (`id`,`comment_id`,`user_id`,`has_like`) VALUES (40, 6740195088150134788, 1578644405, 0),(41, 1578983505334000, 1577435063, 0),(42, 1578985209807000, 1577435063, 1),(43, 1578922158910003, 1579076084, 1),(44, 1579007787804000, 1578919786, 1);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `ppjoke`.`table_feed_comment_ugc` WRITE;
DELETE FROM `ppjoke`.`table_feed_comment_ugc`;
INSERT INTO `ppjoke`.`table_feed_comment_ugc` (`comment_id`,`id`,`like_count`,`share_count`,`comment_count`) VALUES (6740195088150134788, 764, 0, 0, 0),(1578983505334000, 765, 0, 0, 0),(1578985209807000, 766, 1, 0, 0),(1578922158910003, 767, 102, 10, 10),(1579007787804000, 768, 103, 10, 10);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `ppjoke`.`table_feeds_comment` WRITE;
DELETE FROM `ppjoke`.`table_feeds_comment`;
INSERT INTO `ppjoke`.`table_feeds_comment` (`id`,`comment_count`,`comment_id`,`comment_text`,`comment_type`,`create_time`,`height`,`image_url`,`item_id`,`like_count`,`video_url`,`width`,`user_id`) VALUES (1087, NULL, 1578923348198000, '天黑了，要回家睡觉了', 1, 1578923348198, 0, '', 1578921560683, NULL, '', 0, 1578919786),(1086, NULL, 1578923315456000, '2019年那些小事', 1, 1578923315456, 0, '', 1578921537946, NULL, '', 0, 1578919786),(1085, NULL, 1578923300488000, '新年快乐啊', 1, 1578923300488, 0, '', 1578920778984, NULL, '', 0, 1578919786),(1084, NULL, 1578923286866000, '来一发评论看看', 1, 1578923286866, 0, '', 1578921430555, NULL, '', 0, 1578919786),(1076, NULL, 1578922158910000, '下过地，干过活，从小知道爱生活', 1, 1578922158910, 0, '', 1578921365712, NULL, '', 0, 1578919786),(1089, NULL, 1578922158910001, '来一杯咖啡，度过这个愉快的下午', 3, 1578922158911, 360, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/coffe.png', 1578921365712, NULL, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/Coffee%20-%2027230.mp4', 640, 1578919786),(1090, NULL, 1578922158910002, '上海外滩的灯光秀', 1, 1578922158912, NULL, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/shanghai-3616625_640.jpg', 1578921365712, NULL, NULL, NULL, 1578919786),(1123, NULL, 1579004980935000, 'jetpack mvvm架构', 2, 1579004980935, 1280, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/1579004960703.jpeg', 1578976510451, NULL, '', 720, 1578919786),(1092, NULL, 1578922158910003, '2020年新年快乐鸭~', 1, 1578922158913, NULL, NULL, 1578921365712, 1002, NULL, NULL, 1578919786),(1124, NULL, 1579005004124000, 'jetpack mvvm架构视频版介绍', 3, 1579005004124, 1280, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/1579005004706.jpeg', 1578976510451, NULL, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/1579004989263.mp4', 720, 1578919786),(1125, NULL, 1579005023371000, '友善的jetpack 开发者', 1, 1579005023371, 0, '', 1578976510451, NULL, '', 0, 1578919786),(1126, NULL, 1579007787804000, '2020他来了，就在眼前了~Happy New Year', 1, 1579007787804, 0, '', 1578976510452, 1001, '', 0, 1578919786);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `ppjoke`.`table_hot_feeds` WRITE;
DELETE FROM `ppjoke`.`table_hot_feeds`;
INSERT INTO `ppjoke`.`table_hot_feeds` (`id`,`activity_icon`,`activity_text`,`author_id`,`video_cover`,`create_time`,`feeds_text`,`video_height`,`item_id`,`item_type`,`video_url`,`video_width`,`duration`) VALUES (415, NULL, '2020新年快乐', 1578919786, NULL, 1578920778984, '2020跨时代的新年快乐', 0, 1578920778984, 1, NULL, 0, 0),(416, NULL, '2020新年快乐', 1578919786, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/1111.jpeg', 1578921365712, '夜空中最亮的熏熏', 1280, 1578921365712, 2, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/1578921330897.mp4', 720, 0),(417, NULL, '2019高光时刻', 1578919786, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/2222.jpeg', 1578921430555, '夜空中最亮的熏熏，2019的尾巴', 1280, 1578921430555, 1, NULL, 720, 0),(418, NULL, '2020新年快乐', 1578919786, NULL, 1578921483162, '加个话题更容易被发现哈之2020新年快乐', 0, 1578921483162, 1, NULL, 0, 0),(419, NULL, '2019高光时刻', 1578919786, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/1578921529358.jpeg', 1578921537946, '天黑黑', 1280, 1578921537946, 1, NULL, 720, 0),(420, NULL, '2020新年快乐', 1578919786, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/1578921564161.jpeg', 1578921560683, '渣渣', 1280, 1578921560683, 2, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/1578921549076.mp4', 720, 0),(421, NULL, '2020新年快乐', 1578919786, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/1578976503278.jpeg', 1578976510451, '2020年，我要我觉得，不要你觉得', 1280, 1578976510450, 1, NULL, 720, 0),(422, NULL, '2019高光时刻', 1578919786, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/%E6%BE%B3%E5%A4%A7%E5%88%A9%E4%BA%9A%E5%B1%B1%E7%81%AB.jpg', 1578977844351, '澳大利亚的山火', NULL, 1578976510451, 1, NULL, NULL, NULL),(424, NULL, '2019高光时刻', 1578919786, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/%E5%86%AC%E5%AD%A3%E7%9A%84%E5%A5%A5%E7%A7%98.png', 1578977844501, '感受下冬季的魔术吧', 360, 1578976510453, 2, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/%E5%86%AC%E5%AD%A3%E7%9A%84%E9%AD%94%E6%9C%AFvideo.mp4', 640, 30),(425, NULL, '2019高光时刻', 1578919786, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/1579005335522.jpeg', 1579005335868, '加个话题更容易被发现哈', 1280, 1579005335868, 2, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/1579005314878.mp4', 720, 0),(426, NULL, '2019高光时刻', 1578919786, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/music-3507317_640.jpg', 1579005335869, '大熏熏在唱歌', NULL, 1579005335869, 1, NULL, NULL, NULL),(428, NULL, '2020新年快乐', 1578919786, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/2020%E5%B0%81%E9%9D%A2%E5%9B%BE.png', 1578977844500, '2020他来了，就在眼前了', 540, 1578976510452, 2, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/New%20Year%20-%2029212-video.mp4', 960, 8);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `ppjoke`.`table_tag_list` WRITE;
DELETE FROM `ppjoke`.`table_tag_list`;
INSERT INTO `ppjoke`.`table_tag_list` (`id`,`activity_icon`,`background`,`enter_num`,`feed_num`,`follow_num`,`icon`,`intro`,`tag_id`,`title`) VALUES (62, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/51f076f662ef40c99f056a41b130c516.png', 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/2222.png', 1001, 101, 101, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/21434249C0E2090713793722702E4487.jpg', '2020年展望未来，许下美好的愿望', 2, '2020新年快乐'),(61, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/51f076f662ef40c99f056a41b130c516.png', 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/1111.png', 1000, 100, 100, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/icon_etpack.png', '2019年那些事,有哪些最让你怀念呢？', 1, '2019高光时刻'),(63, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/51f076f662ef40c99f056a41b130c516.png', 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/%E5%A4%9A%E5%BD%A9%E5%85%AC%E9%B8%A1.jpg', 1000, 100, 100, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/%E5%A4%9A%E5%BD%A9%E5%85%AC%E9%B8%A1.jpg', '多彩生活，由于这只多彩公鸡', 3, '多彩生活'),(64, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/51f076f662ef40c99f056a41b130c516.png', 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/%E8%80%83%E6%8B%89.jpg', 1000, 100, 100, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/%E8%80%83%E6%8B%89.jpg', '考拉 作为澳大利亚本土品牌，澳考拉雪地靴由麦克森（Bill.Decksen）于上世纪70年代初创立Decksen公司所有，是澳大利亚人最喜欢的雪地靴品牌之一。', 4, 'Aukoala'),(65, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/51f076f662ef40c99f056a41b130c516.png', 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/relaxing-1979674_640.jpg', 1000, 100, 100, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/relaxing-1979674_640.jpg', '放松时刻,放松下自己', 5, '放松时刻'),(66, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/51f076f662ef40c99f056a41b130c516.png', 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/music-3507317_640.jpg', 1000, 100, 100, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/music-3507317_640.jpg', '好听的音乐，给到你我', 6, '音乐时刻'),(67, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/51f076f662ef40c99f056a41b130c516.png', 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/lol.jpg', 1000, 100, 100, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/lol.jpg', '高能时刻，峡谷精彩瞬间', 7, '王者峡谷'),(68, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/51f076f662ef40c99f056a41b130c516.png', 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/geometry-1044090_640.jpg', 1000, 100, 100, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/geometry-1044090_640.jpg', '百年大计，教育大业', 8, '百年教育'),(69, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/51f076f662ef40c99f056a41b130c516.png', 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/food-984441_640.jpg', 1000, 100, 100, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/food-984441_640.jpg', '舌尖上的美食，纵览让人垂涎三尺的美食', 9, '舌尖上的美食'),(70, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/51f076f662ef40c99f056a41b130c516.png', 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/emotion-2167582_640.jpg', 1000, 100, 100, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/emotion-2167582_640.jpg', '看起来憨厚，据说当年蚩尤骑着这货打天下', 10, '国宝熊猫'),(71, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/51f076f662ef40c99f056a41b130c516.png', 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/chinese-opera-1702431_640.jpg', 1000, 100, 100, 'https://pipijoke.oss-cn-hangzhou.aliyuncs.com/chinese-opera-1702431_640.jpg', '国之精粹，中国戏曲，文化魁宝', 11, '中国戏曲');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `ppjoke`.`table_tag_list_like` WRITE;
DELETE FROM `ppjoke`.`table_tag_list_like`;
INSERT INTO `ppjoke`.`table_tag_list_like` (`id`,`has_like`,`tag_id`,`user_id`) VALUES (37, b'1', 1, 1578644405),(38, b'1', 2, 1578644405),(39, b'0', 1, 1578919786),(40, b'1', 5, 1578637295);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `ppjoke`.`table_ugc` WRITE;
DELETE FROM `ppjoke`.`table_ugc`;
INSERT INTO `ppjoke`.`table_ugc` (`id`,`comment_count`,`item_id`,`like_count`,`share_count`) VALUES (346, 11, 1578921560683, 11, 11),(345, 1, 1578921537946, -1, 0),(344, 1, 1578921483162, 0, 0),(343, 502, 1578921430555, 998, 1),(342, 504, 1578921365712, 1005, 0),(341, 502, 1578920778984, 1002, 0),(347, 6, 1578976510451, 0, 2),(417, 10, 1578977844351, 10, 10),(418, 504, 1578976510452, 1001, 12),(419, 10, 1578976510453, 8, 10),(420, 2, 1578976510450, 0, 1),(421, 0, 1579005335868, 0, 0),(422, 505, 1579005335869, 1004, 0);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `ppjoke`.`table_ugc_like` WRITE;
DELETE FROM `ppjoke`.`table_ugc_like`;
INSERT INTO `ppjoke`.`table_ugc_like` (`id`,`item_id`,`user_id`,`has_like`,`has_diss`,`has_favorite`) VALUES (55, 1578921560683, 1578919786, 1, 0, 0),(56, 1578921430555, 1578931600, 0, 0, 0),(57, 1578920778984, 1578985006, 1, 0, 0),(58, 1578976510453, 1578985006, 0, 0, 0),(59, 1578921365712, 1578919786, 1, 0, 0),(60, 1578920778984, 1578919786, 1, 0, 0),(61, 1578976510453, 1578919786, 1, 0, 0),(62, 1578976510451, 1578990116, 0, 1, 1),(63, 1578920778984, 1579017342, 0, 1, 0),(64, 1578921537946, 1579017342, 0, 1, 0),(65, 1578921430555, 1579017342, 0, 1, 0),(66, 1578976510452, 1578919786, 1, 0, 0);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `ppjoke`.`table_user` WRITE;
DELETE FROM `ppjoke`.`table_user`;
INSERT INTO `ppjoke`.`table_user` (`id`,`avatar`,`description`,`name`,`user_id`,`follow_count`,`like_count`,`top_count`,`qq_openid`,`comment_count`,`expires_time`,`favorite_count`,`feed_count`,`follower_count`,`history_count`,`score`) VALUES (1250, 'http://qzapp.qlogo.cn/qzapp/101794421/FE41683AD4ECF91B7736CA9DB8104A5C/100', '这是一只神秘的jetpack', '、蓅哖╰伊人为谁笑', 1578919786, 0, 3, 0, 'FE41683AD4ECF91B7736CA9DB8104A5C', 9, 1586695789903, 0, 0, 2, 222, 0),(1251, 'http://qzapp.qlogo.cn/qzapp/101794421/5EC62EBB45BAE9533F30DFB5426A832E/100', '这是一只神秘的jetpack', '愚  人', 1578931600, 1, 0, 0, '5EC62EBB45BAE9533F30DFB5426A832E', 0, 1586707599745, 0, 0, 0, 6, 0),(1252, 'http://qzapp.qlogo.cn/qzapp/101819313/7CF17828E428AE475933492B132E05B6/100', '这是一只神秘的jetpack', '魅力零度', 1578965030, 0, 0, 0, '7CF17828E428AE475933492B132E05B6', 0, 1586741030088, 0, 0, 0, 0, 0),(1253, 'http://qzapp.qlogo.cn/qzapp/101794421/A87DEB1E5B9E2481225996B34681D3DB/100', '这是一只神秘的jetpack', 'X-Y-Z', 1578976462, 0, 0, 0, 'A87DEB1E5B9E2481225996B34681D3DB', 0, 1586752461855, 0, 0, 0, 0, 0),(1254, 'http://qzapp.qlogo.cn/qzapp/101794421/82E0FCBD7AC46B8D52783D36823281E8/100', '这是一只神秘的jetpack', 'hao123', 1578985006, 0, 1, 0, '82E0FCBD7AC46B8D52783D36823281E8', 0, 1586761008672, 0, 0, 0, 0, 0),(1256, 'http://qzapp.qlogo.cn/qzapp/101794421/3ADF4A9C695B790F1AE9E232EF74BF85/100', NULL, '昊空', 1578990116, 1, 1, 0, '3ADF4A9C695B790F1AE9E232EF74BF85', 1, 1586766116450, 1, 0, 0, 223, 0),(1257, 'http://qzapp.qlogo.cn/qzapp/101794421/BC898E1C9DCEF508738AA4907BC15F2D/100', NULL, '轻轻地我', 1579017342, 0, 1, 0, 'BC898E1C9DCEF508738AA4907BC15F2D', 0, 1586793341485, 0, 0, 0, 0, 0),(1258, 'http://qzapp.qlogo.cn/qzapp/101794421/25AE64232726D9B94500F130297A7128/100', NULL, '    ，', 1579076084, 0, 0, 0, '25AE64232726D9B94500F130297A7128', 0, 1586852083675, 0, 0, 0, 2, 0),(1259, 'http://qzapp.qlogo.cn/qzapp/101794421/06DA49F953B2DDBB716D71EA0C34648A/100', NULL, '有一种执着叫无可替代ン', 1579076141, 0, 0, 0, '06DA49F953B2DDBB716D71EA0C34648A', 0, 1586852143325, 0, 0, 0, 0, 0);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `ppjoke`.`table_user_banner` WRITE;
DELETE FROM `ppjoke`.`table_user_banner`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `ppjoke`.`table_user_follow` WRITE;
DELETE FROM `ppjoke`.`table_user_follow`;
INSERT INTO `ppjoke`.`table_user_follow` (`id`,`user_id`,`follow_user_id`,`has_follow`) VALUES (22, 1578919786, 1578931600, b'1'),(23, 1578919786, 1578990116, b'1');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `ppjoke`.`table_watch_history` WRITE;
DELETE FROM `ppjoke`.`table_watch_history`;
INSERT INTO `ppjoke`.`table_watch_history` (`id`,`item_id`,`user_id`,`time`) VALUES (49, 1578921483162, 1578919786, 1578921490559),(50, 1578921365712, 1578919786, 1578921637084),(51, 1578921430555, 1578919786, 1578922174263),(52, 1578921537946, 1578919786, 1578922331347),(53, 1578920778984, 1578919786, 1578923295577),(54, 1578921560683, 1578919786, 1578923322713),(55, 1578921430555, 864296038222757, 1578928772257),(56, 1578920778984, 1578931600, 1578931607652),(57, 1578921537946, 1578931600, 1578931636624),(58, 1578921430555, 1578931600, 1578931644266),(59, 1578921560683, 1578837309, 1578932359037),(60, 1578921430555, 1578837309, 1578932369992),(61, 1578920778984, 1578837309, 1578932384173),(62, 1578921365712, 1578466481, 1578962348893),(63, 1578921537946, 1578466481, 1578962389817),(64, 1578921430555, 1578466481, 1578962394017),(65, 1578921430555, 1577435063, 1578966183199),(66, 1578921537946, 1577435063, 1578966749234),(67, 1578921483162, 1577435063, 1578966754820),(68, 1578920778984, 1577435063, 1578966757447),(69, 1578921537946, 1577803042, 1578978589622),(70, 1578976510453, 1578919786, 1578980027962),(71, 1578976510452, 1578919786, 1578982215838),(72, 1578976510451, 1577435063, 1578983319755),(73, 1578976510450, 1577435063, 1578984000755),(74, 1578976510451, 1578919786, 1578986353828),(75, 1578976510450, 1578919786, 1578986363848),(76, 1578921365712, 1578312312, 1578986620222),(77, 1578976510451, 1578990116, 1578990125133),(78, 1578976510450, 1578990116, 1578990343940),(79, 1578921483162, 1578990116, 1578991517238),(80, 1578920778984, 1578990116, 1578991601683),(81, 1578921430555, 1578990116, 1578991617359),(82, 1578921430555, 1577102330, 1578998546334),(83, 1578976510452, 1577102330, 1578998557489),(84, 1578976510452, 1577449370, 1579013613263),(85, 1578921365712, 1577449370, 1579013639745),(86, 1578976510452, 1578837309, 1579014705269),(87, 1578921365712, 1577097774, 1579015571006),(88, 1578921365712, 1577432411, 1579048989247),(89, 1579005335868, 1578919786, 1579050549505),(90, 1579005335869, 1578990116, 1579058248281),(91, 1579005335869, 1578919786, 1579068739321),(92, 426, 1578638842, 1579069013423),(93, 415, 1578638842, 1579069021458),(94, 1578976510452, 1578757386, 1579076549679),(95, 1579005335869, 1578757386, 1579076582923),(96, 1578921365712, 1578757386, 1579076731145),(97, 1579005335869, 1579076084, 1579076911562),(98, 1578921365712, 1579076084, 1579076926509);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `ppjoke`.`user` WRITE;
DELETE FROM `ppjoke`.`user`;
UNLOCK TABLES;
COMMIT;
