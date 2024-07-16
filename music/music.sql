/*
 Navicat Premium Data Transfer

 Source Server         : user
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:3306
 Source Schema         : music

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 02/06/2024 17:19:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像访问地址',
  `avatar_local` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像本地地址',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色身份（管理员）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '$2a$10$M7OhEOo1FLbom2r4Mou6Huf/7fC5WSO0ckvmVa7XgJjWrE8/Ln1Ji', 'admin', '123456', '123@qq.com', '123456', 'http://localhost:9090/file/download/1714313940186_衫源.jpg', '1714313940186_衫源.jpg', '管理员');

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `consumer_id` int NULL DEFAULT NULL COMMENT '用户id',
  `song_id` int NULL DEFAULT NULL COMMENT '歌曲id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '收藏时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collect
-- ----------------------------
INSERT INTO `collect` VALUES (44, 13, 46, '2024-05-26 14:58:37');
INSERT INTO `collect` VALUES (45, 13, 47, '2024-05-26 14:58:39');
INSERT INTO `collect` VALUES (46, 13, 50, '2024-05-26 14:58:41');

-- ----------------------------
-- Table structure for consumer
-- ----------------------------
DROP TABLE IF EXISTS `consumer`;
CREATE TABLE `consumer`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `gender` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮件',
  `birth` datetime NULL DEFAULT NULL COMMENT '生日',
  `introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '个性签名',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像访问地址',
  `avatar_local` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像本地地址',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色身份（用户）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE COMMENT '用户名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of consumer
-- ----------------------------
INSERT INTO `consumer` VALUES (13, 'user', '$2a$10$GZmU13BJeOwTg.R86nsMie4iCMccbi36nYMZUE7RU6vIQdHy1UsW2', 'user', '男', '123456', '123456@qq.com', '2001-12-13 00:00:00', '哈哈哈哈', '123456', '2024-03-31 00:00:00', '2024-05-05 21:58:34', 'http://localhost:9090/file/download/1714375254459_avatar.jpg', '1714375254459_avatar.jpg', '用户');

-- ----------------------------
-- Table structure for list_song
-- ----------------------------
DROP TABLE IF EXISTS `list_song`;
CREATE TABLE `list_song`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `song_list_id` int NULL DEFAULT NULL COMMENT '歌单id',
  `song_id` int NULL DEFAULT NULL COMMENT '歌曲id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 129 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '歌单包含歌曲列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of list_song
-- ----------------------------
INSERT INTO `list_song` VALUES (29, 14, 29);
INSERT INTO `list_song` VALUES (30, 14, 27);
INSERT INTO `list_song` VALUES (31, 14, 28);
INSERT INTO `list_song` VALUES (36, 14, 48);
INSERT INTO `list_song` VALUES (37, 14, 49);
INSERT INTO `list_song` VALUES (38, 14, 50);
INSERT INTO `list_song` VALUES (39, 14, 47);
INSERT INTO `list_song` VALUES (40, 14, 46);
INSERT INTO `list_song` VALUES (41, 14, 40);
INSERT INTO `list_song` VALUES (42, 14, 41);
INSERT INTO `list_song` VALUES (43, 14, 42);
INSERT INTO `list_song` VALUES (44, 15, 58);
INSERT INTO `list_song` VALUES (45, 15, 56);
INSERT INTO `list_song` VALUES (46, 21, 63);
INSERT INTO `list_song` VALUES (47, 21, 62);
INSERT INTO `list_song` VALUES (48, 21, 61);
INSERT INTO `list_song` VALUES (49, 21, 60);
INSERT INTO `list_song` VALUES (50, 20, 59);
INSERT INTO `list_song` VALUES (51, 20, 58);
INSERT INTO `list_song` VALUES (52, 20, 57);
INSERT INTO `list_song` VALUES (53, 20, 56);
INSERT INTO `list_song` VALUES (54, 20, 55);
INSERT INTO `list_song` VALUES (55, 18, 48);
INSERT INTO `list_song` VALUES (56, 18, 47);
INSERT INTO `list_song` VALUES (57, 18, 45);
INSERT INTO `list_song` VALUES (58, 18, 46);
INSERT INTO `list_song` VALUES (59, 18, 44);
INSERT INTO `list_song` VALUES (60, 18, 42);
INSERT INTO `list_song` VALUES (61, 18, 43);
INSERT INTO `list_song` VALUES (62, 18, 41);
INSERT INTO `list_song` VALUES (63, 17, 54);
INSERT INTO `list_song` VALUES (64, 17, 53);
INSERT INTO `list_song` VALUES (65, 17, 52);
INSERT INTO `list_song` VALUES (66, 17, 51);
INSERT INTO `list_song` VALUES (67, 17, 50);
INSERT INTO `list_song` VALUES (68, 17, 49);
INSERT INTO `list_song` VALUES (69, 17, 48);
INSERT INTO `list_song` VALUES (70, 17, 47);
INSERT INTO `list_song` VALUES (71, 17, 46);
INSERT INTO `list_song` VALUES (72, 17, 45);
INSERT INTO `list_song` VALUES (73, 17, 44);
INSERT INTO `list_song` VALUES (74, 17, 43);
INSERT INTO `list_song` VALUES (75, 17, 42);
INSERT INTO `list_song` VALUES (76, 17, 41);
INSERT INTO `list_song` VALUES (77, 17, 40);
INSERT INTO `list_song` VALUES (78, 17, 39);
INSERT INTO `list_song` VALUES (79, 17, 38);
INSERT INTO `list_song` VALUES (80, 17, 37);
INSERT INTO `list_song` VALUES (81, 17, 36);
INSERT INTO `list_song` VALUES (82, 17, 35);
INSERT INTO `list_song` VALUES (83, 17, 30);
INSERT INTO `list_song` VALUES (84, 17, 29);
INSERT INTO `list_song` VALUES (85, 17, 27);
INSERT INTO `list_song` VALUES (86, 17, 28);
INSERT INTO `list_song` VALUES (87, 22, 63);
INSERT INTO `list_song` VALUES (88, 22, 62);
INSERT INTO `list_song` VALUES (89, 23, 63);
INSERT INTO `list_song` VALUES (90, 23, 62);
INSERT INTO `list_song` VALUES (91, 23, 61);
INSERT INTO `list_song` VALUES (92, 23, 60);
INSERT INTO `list_song` VALUES (95, 30, 63);
INSERT INTO `list_song` VALUES (96, 30, 62);
INSERT INTO `list_song` VALUES (97, 30, 61);
INSERT INTO `list_song` VALUES (98, 30, 60);
INSERT INTO `list_song` VALUES (99, 30, 59);
INSERT INTO `list_song` VALUES (100, 30, 58);
INSERT INTO `list_song` VALUES (101, 30, 57);
INSERT INTO `list_song` VALUES (102, 30, 56);
INSERT INTO `list_song` VALUES (103, 30, 55);
INSERT INTO `list_song` VALUES (104, 30, 54);
INSERT INTO `list_song` VALUES (105, 30, 53);
INSERT INTO `list_song` VALUES (106, 30, 52);
INSERT INTO `list_song` VALUES (107, 30, 51);
INSERT INTO `list_song` VALUES (108, 30, 50);
INSERT INTO `list_song` VALUES (109, 30, 49);
INSERT INTO `list_song` VALUES (110, 30, 48);
INSERT INTO `list_song` VALUES (111, 30, 47);
INSERT INTO `list_song` VALUES (112, 30, 46);
INSERT INTO `list_song` VALUES (113, 30, 45);
INSERT INTO `list_song` VALUES (114, 30, 44);
INSERT INTO `list_song` VALUES (115, 30, 43);
INSERT INTO `list_song` VALUES (116, 30, 42);
INSERT INTO `list_song` VALUES (117, 30, 41);
INSERT INTO `list_song` VALUES (118, 30, 40);
INSERT INTO `list_song` VALUES (119, 30, 39);
INSERT INTO `list_song` VALUES (120, 30, 38);
INSERT INTO `list_song` VALUES (121, 30, 37);
INSERT INTO `list_song` VALUES (122, 30, 36);
INSERT INTO `list_song` VALUES (123, 30, 35);
INSERT INTO `list_song` VALUES (124, 30, 34);
INSERT INTO `list_song` VALUES (125, 30, 30);
INSERT INTO `list_song` VALUES (126, 30, 29);
INSERT INTO `list_song` VALUES (127, 30, 28);
INSERT INTO `list_song` VALUES (128, 30, 27);
INSERT INTO `list_song` VALUES (129, 30, 66);

-- ----------------------------
-- Table structure for singer
-- ----------------------------
DROP TABLE IF EXISTS `singer`;
CREATE TABLE `singer`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌手姓名',
  `gender` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌手性别（男，女，组合，不明）',
  `birth` datetime NULL DEFAULT NULL COMMENT '歌手生日',
  `location` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌手所属地区',
  `introduction` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌手简介',
  `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌手图片访问地址',
  `pic_local` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌手图片本地地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '歌手表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of singer
-- ----------------------------
INSERT INTO `singer` VALUES (19, '张杰', '男', '1982-12-22 00:00:00', '内地', '外文名：Jason \n别名：张小杰\n国籍：中国\n主要成就：33次荣获最受欢迎男歌手奖、2014年全美音乐奖“年度国际艺人奖”、2010年韩国MAMA“亚洲之星”大奖、连续四年中歌榜最受欢迎男歌手奖、连续五届全球华语榜中榜最受欢迎男歌手、连续四年音乐之声最受欢迎男歌手、三届城市之音至尊金榜年度冠军王、香港TVB8“内地观众最爱男歌手”、蝉联两届中国TOP排行榜最佳男歌手、两届劲歌王内地最受欢迎男歌手、三届东南劲爆榜最受欢迎男歌手、两届雪碧榜最受欢迎男歌手、中国儿童慈善奖—-突出贡献奖\n民族：汉族\n出生地：四川成都\n出生日期：1982年12月20日\n职业：歌手', 'http://localhost:9090/file/download/1714909133247_张杰.jpg', '1714909133247_张杰.jpg');
INSERT INTO `singer` VALUES (20, '周杰伦', '男', '1979-01-18 00:00:00', '港台', '外文名：Jay Chou\n\n别名：周董\n\n国籍：中国\n\n出生地：台湾省新北市\n\n职业：音乐人、制作人、导演、企业家等\n\n出生日期：1979年1月18日（农历腊月二十）\n\n代表作品：《龙卷风》、《菊花台》、《青花瓷》、《晴天》', 'http://localhost:9090/file/download/1714909020161_周杰伦.jpg', '1714909020161_周杰伦.jpg');
INSERT INTO `singer` VALUES (21, '陈奕迅', '男', '1974-07-27 00:00:00', '港台', '外文名：Eason Chan\n\n别名：阿臣、医生、E神、E臣、陈小胖、所长\n\n国籍：中国\n\n出生地：香港\n\n出生日期：1974年7月27日\n\n职业：歌手、演员\n\n代表作品：《爱情转移》、《十年》、《浮夸》、《k歌之王》', 'http://localhost:9090/file/download/1714908939745_陈奕迅.jpg', '1714908939745_陈奕迅.jpg');
INSERT INTO `singer` VALUES (22, 'G.E.M. 邓紫棋', '女', '1991-08-16 01:00:00', '内地', '外文名：G.E.M.(Get Everybody Moving)、Gloria Tang\n\n别名：金鱼、G.E.M.、邓宝、棋宝、Gloria、棋棋、小金鱼、金鱼G.E.M.、小巨肺、解解\n\n原名：邓诗颖\n\n国籍：中国\n\n出生地：上海\n\n出生日期：1991年8月16日\n\n职业：歌手\n\n代表作品：《睡公主》、《回忆的沙漏》、《A.I.N.Y.》、《我的秘密》、《泡沫》、《奇迹》、《偶尔》', 'http://localhost:9090/file/download/1714909081803_G.E.M.邓紫棋.jpg', '1714909081803_G.E.M.邓紫棋.jpg');
INSERT INTO `singer` VALUES (25, 'Taylor Swift', '女', '1989-12-13 00:00:00', '欧美', '中文名：泰勒·斯威夫特\n\n别名：Taylor Alison Swift\n\n国籍：美国\n\n出生地：宾夕法尼亚州雷丁\n\n生日：1989年12月13日\n\n职业：歌手、演员、制作人、慈善家\n\n代表作：《Enchanted》、《All Too Well》、《Back to December》、《Wildest Dreams》、《Mean》、《Look What You Made Me Do》、《Love Story》、《You Belong With Me》、《I Knew You Were Trouble》、《Shake It Off》', 'http://localhost:9090/file/download/1714908858845_Taylor Swift.jpg', '1714908858845_Taylor Swift.jpg');
INSERT INTO `singer` VALUES (26, 'Justin Bieber', '男', '1994-03-01 00:00:00', '欧美', '中文名：贾斯汀·比伯\n\n别名：Justin Drew Bieber、JB、J-Beebs、Bieber、Beebs、JBiebs、B宝、小贾斯汀\n\n国籍：加拿大\n\n出生地：Stratford, Ontario, Canada\n\n生日：1994年3月1日\n\n职业：歌手、词曲作家、演员\n\n代表作：《Baby》《Love Yourself》《Sorry》《What Do You Mean?》', 'http://localhost:9090/file/download/1714911463845_Justin Bieber.jpg', '1714911463845_Justin Bieber.jpg');
INSERT INTO `singer` VALUES (27, 'Maroon 5', '组合', '1994-01-01 00:00:00', '欧美', '中文名：魔力红乐队\n\n国籍：美国\n\n团员组成：Adam Levine、James Burgon Valentine、Jesse Royal Carmichael、Mickey \"Mickey\" Madden 、Matt Flynn、PJ Morton\n\n团员人数：6\n\n职业：摇滚乐队\n\n代表作：《Moves Like Jagger》 《Misery》 《Payphone》\n\n主要成就：格莱美最佳舞台表现奖、格莱美最佳流行乐队/组合奖 、美国人民选择奖最受欢迎摇滚乐队、MTV音乐录影带大奖', 'http://localhost:9090/file/download/1714912130151_Maroon 5.jpg', '1714912130151_Maroon 5.jpg');
INSERT INTO `singer` VALUES (28, '米津玄師', '男', '1991-03-10 00:00:00', '日本', '外文名：よねづ けんし、Yonezu Kenshi\n\n别名：Hachi、八爷、ハチ\n\n国籍：日本\n\n出生地：日本德岛县德岛市\n\n生日：1991年3月10日\n\n职业：音乐家、创作歌手、插画家、摄影师、舞者\n\n代表作：《Lemon》、《打上花火》、《灰色と青》、《俄罗斯套娃》、《甜甜圈洞》、《砂之惑星》', 'http://localhost:9090/file/download/1714912891820_米津玄師.jpg', '1714912891820_米津玄師.jpg');
INSERT INTO `singer` VALUES (29, '初音ミク', '女', '2010-04-30 00:00:00', '日本', '外文名：初音ミク\n\n别名：はつねミク，Hatsune Miku\n\n国籍：日本\n\n代表作品：甩葱歌、世界第一的公主殿下\n\n开发商：Crypton Future Media\n\n初始版本：2007年8月31日（发售日）\n\n身 高：158cm\n\n体 重：42kg\n\n主程序：Vocaloid\n\n语 言：日语，英语\n\n擅长节奏：70～150BPM（原设80～150）\n\n擅长音域：A3～E5（原设定为C3～E4）\n\n擅长曲种：流行歌曲', 'http://localhost:9090/file/download/1714913968129_初音ミク.jpg', '1714913968129_初音ミク.jpg');
INSERT INTO `singer` VALUES (30, 'BLACKPINK (블랙핑크)', '组合', '2016-08-08 00:00:00', '韩国', '中文名：粉墨\n\n外文名：블랙핑크、ブラック・ピンク\n\n国籍：韩国、泰国、新西兰\n\n团员人数：4名\n\n团员组成：JISOO、JENNIE、LISA、ROSÉ\n\n出道日期：2016年8月8日\n\n职业：歌手、演员、主持、舞者\n\n代表作：《WHISTLE》、《PLAYING WITH FIRE》、《BOOMBAYAH》、《As If It\'s Your Last》\n\n主要成就：第26届Melon Music Awards－新人奖，第31届韩国金唱片奖－音源部门新人奖', 'http://localhost:9090/file/download/1717319490720_BLACKPINK .jpg', '1717319490720_BLACKPINK .jpg');
INSERT INTO `singer` VALUES (31, '汪苏泷', '男', '1989-09-17 01:00:00', '内地', '外文名：Silence.W\n\n别名：小泷\n\n国籍：中国\n\n出生地：辽宁省沈阳市\n\n生日：1989年9月17日\n\n职业：音乐人、潮牌主理人\n\n代表作：《年轮》、《一笑倾城》、《那个男孩》、《银河》、《小星星》、《有点甜》、《不分手的恋爱》、《风度》、《无名之辈》\n\n主要成就：东方风云榜东方新人、音乐先锋榜创作新人奖、百度沸点年度十大金曲、中国歌曲排行榜年度金曲奖、劲歌王颁奖典礼年度最佳唱作人、爱奇艺尖叫盛典年度影视金曲、移动视频风云盛典年度突破艺人、MusicRadio《银河》获CityFM城市之音点播冠军、至尊音乐盛典“至尊创作歌手', 'http://localhost:9090/file/download/1714914383182_汪苏泷.jpg', '1714914383182_汪苏泷.jpg');
INSERT INTO `singer` VALUES (32, '林俊杰', '男', '1981-03-27 00:00:00', '新加坡', '外文名：JJ Lin、Wayne Lim\n\n别名：JJ、老林、行走的CD、J神、林三岁\n\n国籍：新加坡\n\n出生地：新加坡\n\n生日：1981年3月27日\n\n职业：歌手、制作人、潮牌主理人\n\n代表作品：《江南》、《曹操》、《发现爱》、《编号89757》等\n\n主要成就：新加坡杰出青年 两届台湾金曲奖最佳国语男歌手 第15届台湾金曲奖最佳新人 30个台湾Hito流行音乐奖项 27个新加坡词曲版权协会奖项 25个中国TOP排行榜奖项 25个中国原创音乐流行榜奖项 24个全球华语歌曲排行榜奖项 第5届通商中国青年奖', 'http://localhost:9090/file/download/1714914516336_林俊杰.jpg', '1714914516336_林俊杰.jpg');
INSERT INTO `singer` VALUES (33, '薛之谦', '男', '1983-07-17 00:00:00', '内地', '外文名：Joker\n\n别名：谦谦、二谦\n\n国籍：中国\n\n出生地：上海\n\n出生日期：1983年7月17日\n\n职业：歌手、演员、制作人\n\n代表作品：《认真的雪》、《演员》、《丑八怪》、《绅士》\n\n主要成就：2008雪碧中国原创音乐流行榜最具潜质歌手大奖、2009MusicRadio中国TOP排行榜年度最佳舞台演绎奖、第二十届华语榜中榜最受欢迎唱作歌手 、2016Music Radio年度最佳男歌手 、2016酷音乐亚洲盛典年度最受欢迎创作人\n\n粉丝：谦友', 'http://localhost:9090/file/download/1714914655270_薛之谦.jpg', '1714914655270_薛之谦.jpg');
INSERT INTO `singer` VALUES (34, '毛不易', '男', '1994-10-01 00:00:00', '内地', '别名：毛巨星、毛毛、业余巨星、巨星、毛怼怼、毛甜甜、小水蜜桃、老毛、阿毛、毛总、毛老师\n\n国籍：中国\n\n出生地：黑龙江省齐齐哈尔市\n\n生日：1994年10月1日\n\n职业：歌手\n\n代表作：《消愁》、《借》、《芬芳一生》、《感觉自己是巨星》、《如果有一天我变得很有钱》等\n\n主要成就：2017年《明日之子》全国总决赛冠军、2017年腾讯娱乐白皮书音乐年度之星奖、2017年腾讯视频星光大赏年度音乐新势力奖、2017年网易年度有态度大赏年度最有态度唱作歌手奖、2018年首届金鲛奖年度十佳网络新星奖、2018年亚洲音乐盛典最具潜力男歌手奖、年度最佳唱作人奖\n\n原名：王维家', 'http://localhost:9090/file/download/1714914791531_毛不易.jpg', '1714914791531_毛不易.jpg');
INSERT INTO `singer` VALUES (35, '宇多田ヒカル', '女', '1983-01-19 00:00:00', '日本', '外文名：宇多田ヒカル、うただ ヒカル、Utada Hikaru\n\n别名：Utada、ヒッキー（Hikki）\n\n国籍：美国\n\n出生地：美国纽约曼哈顿\n\n出生日期：1983年1月19日\n\n职业：歌手、作词家、作曲家、音乐制作人\n\n代表作：《Automatic》、《First Love》、《光》、《Flavor Of Life》', 'http://localhost:9090/file/download/1714914955916_宇多田ヒカル.jpg', '1714914955916_宇多田ヒカル.jpg');
INSERT INTO `singer` VALUES (36, '王力宏', '男', '1976-05-17 00:00:00', '港台', '外文名：Leehom Wang\n\n别名：Home Boy\n\n国籍：美国\n\n出生地：纽约罗切斯特\n\n出生日期：1976年5月17日\n\n职业：歌手、音乐制作人、演员、导演\n\n代表作品：《唯一》、《大城小爱》、《我们的歌》、《龙的传人》、《你不知道的事》、《恋爱通告》', 'http://localhost:9090/file/download/1714915089034_王力宏.jpg', '1714915089034_王力宏.jpg');

-- ----------------------------
-- Table structure for song
-- ----------------------------
DROP TABLE IF EXISTS `song`;
CREATE TABLE `song`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `singer_id` int NULL DEFAULT NULL COMMENT '歌手id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌曲名称',
  `album` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌曲专辑',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片访问地址',
  `lyric` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌词访问地址',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌曲访问地址',
  `pic_local` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片本地地址',
  `lyric_local` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌词本地地址',
  `url_local` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌曲本地地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 67 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '歌曲' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of song
-- ----------------------------
INSERT INTO `song` VALUES (27, 19, '剑心-张杰', '拾', '2024-03-31 21:05:19', '2024-03-31 21:05:19', 'http://localhost:9090/file/download/1714909702742_剑心-张杰.jpg', 'http://localhost:9090/file/download/1714909915333_剑心-张杰.lrc', 'http://localhost:9090/file/download/1714910107948_剑心-张杰.mp3', '1714909702742_剑心-张杰.jpg', '1714909915333_剑心-张杰.lrc', '1714910107948_剑心-张杰.mp3');
INSERT INTO `song` VALUES (28, 19, '逆态度-张杰', '爱，不解释', '2024-03-31 21:25:39', '2024-03-31 21:25:39', 'http://localhost:9090/file/download/1714909706298_逆态度-张杰.jpg', 'http://localhost:9090/file/download/1714909920028_逆态度-张杰.lrc', 'http://localhost:9090/file/download/1714910098628_逆态度-张杰.mp3', '1714909706298_逆态度-张杰.jpg', '1714909920028_逆态度-张杰.lrc', '1714910098628_逆态度-张杰.mp3');
INSERT INTO `song` VALUES (29, 19, '他不懂-张杰', '听！我们的歌', '2024-03-31 00:00:00', '2024-05-05 19:43:14', 'http://localhost:9090/file/download/1714909711084_他不懂-张杰.jpg', 'http://localhost:9090/file/download/1714909928257_他不懂-张杰.lrc', 'http://localhost:9090/file/download/1714910086495_他不懂-张杰.mp3', '1714909711084_他不懂-张杰.jpg', '1714909928257_他不懂-张杰.lrc', '1714910086495_他不懂-张杰.mp3');
INSERT INTO `song` VALUES (30, 19, '何必要在一起-张杰', '穿越三部曲', '2024-04-23 00:00:00', '2024-05-05 15:50:51', 'http://localhost:9090/file/download/1714909726154_何必要在一起-张杰.jpg', 'http://localhost:9090/file/download/1714909934047_何必要在一起-张杰.lrc', 'http://localhost:9090/file/download/1714910092682_何必在一起-张杰.mp3', '1714909726154_何必要在一起-张杰.jpg', '1714909934047_何必要在一起-张杰.lrc', '1714910092682_何必在一起-张杰.mp3');
INSERT INTO `song` VALUES (34, 25, 'Love Story-Taylor Swift', 'Fearless (Platinum Edition)', '2024-04-30 00:00:00', '2024-05-05 20:00:53', 'http://localhost:9090/file/download/1714910464689_Love Story-Taylor Swift.jpg', 'http://localhost:9090/file/download/1714910478991_Love Story-Taylor Swift.lrc', 'http://localhost:9090/file/download/1714910473887_Love Story-Taylor Swift.mp3', '1714910464689_Love Story-Taylor Swift.jpg', '1714910478991_Love Story-Taylor Swift.lrc', '1714910473887_Love Story-Taylor Swift.mp3');
INSERT INTO `song` VALUES (35, 19, '看月亮爬上来-张杰', '穿越三部曲', '2024-04-30 00:00:00', '2024-05-05 16:05:24', 'http://localhost:9090/file/download/1714902830361_看月亮爬上来-张杰.jpg', 'http://localhost:9090/file/download/1714897318892_看月亮爬上来-张杰 .lrc', 'http://localhost:9090/file/download/1714897312833_看月亮爬上来-张杰.mp3', '1714902830361_看月亮爬上来-张杰.jpg', '1714897318892_看月亮爬上来-张杰 .lrc', '1714897312833_看月亮爬上来-张杰.mp3');
INSERT INTO `song` VALUES (36, 19, ' 穿越人海-张杰', '穿越三部曲', '2024-05-05 16:28:38', '2024-05-05 16:28:38', 'http://localhost:9090/file/download/1714897728627_穿越人海-张杰.jpg', 'http://localhost:9090/file/download/1714897739883_穿越人海-张杰.lrc', 'http://localhost:9090/file/download/1714897734528_穿越人海-张杰.mp3', '1714897728627_穿越人海-张杰.jpg', '1714897739883_穿越人海-张杰.lrc', '1714897734528_穿越人海-张杰.mp3');
INSERT INTO `song` VALUES (37, 19, '勿忘心安-张杰', '穿越三部曲', '2024-05-05 16:32:54', '2024-05-05 16:32:54', 'http://localhost:9090/file/download/1714897982567_勿忘心安-张杰.jpg', 'http://localhost:9090/file/download/1714898003789_勿忘心安-张杰.lrc', 'http://localhost:9090/file/download/1714897989812_勿忘心安-张杰.mp3', '1714897982567_勿忘心安-张杰.jpg', '1714898003789_勿忘心安-张杰.lrc', '1714897989812_勿忘心安-张杰.mp3');
INSERT INTO `song` VALUES (38, 19, '微笑-张杰', '穿越三部曲', '2024-05-05 16:38:35', '2024-05-05 16:38:35', 'http://localhost:9090/file/download/1714898342536_微笑-张杰.jpg', 'http://localhost:9090/file/download/1714898358792_微笑-张杰.lrc', 'http://localhost:9090/file/download/1714898348075_微笑-张杰.mp3', '1714898342536_微笑-张杰.jpg', '1714898358792_微笑-张杰.lrc', '1714898348075_微笑-张杰.mp3');
INSERT INTO `song` VALUES (39, 19, '量身订做-张杰', '穿越三部曲', '2024-05-05 16:42:09', '2024-05-05 16:42:09', 'http://localhost:9090/file/download/1714898544143_量身订做-张杰.jpg', 'http://localhost:9090/file/download/1714898563731_量身订做-张杰.lrc', 'http://localhost:9090/file/download/1714898577446_量身订做-张杰.mp3', '1714898544143_量身订做-张杰.jpg', '1714898563731_量身订做-张杰.lrc', '1714898577446_量身订做-张杰.mp3');
INSERT INTO `song` VALUES (40, 19, '龙鳞-张杰', '穿越三部曲', '2024-05-05 16:44:44', '2024-05-05 16:44:44', 'http://localhost:9090/file/download/1714898718044_龙鳞-张杰.jpg', 'http://localhost:9090/file/download/1714898812169_龙鳞-张杰.lrc', 'http://localhost:9090/file/download/1714898820389_龙鳞-张杰.mp3', '1714898718044_龙鳞-张杰.jpg', '1714898812169_龙鳞-张杰.lrc', '1714898820389_龙鳞-张杰.mp3');
INSERT INTO `song` VALUES (41, 20, '晴天-周杰伦', '叶惠美', '2024-05-05 00:00:00', '2024-05-05 17:25:03', 'http://localhost:9090/file/download/1714902614682_晴天-周杰伦.jpg', 'http://localhost:9090/file/download/1714901372742_晴天-周杰伦.lrc', 'http://localhost:9090/file/download/1714901383266_晴天-周杰伦.mp3', '1714902614682_晴天-周杰伦.jpg', '1714901372742_晴天-周杰伦.lrc', '1714901383266_晴天-周杰伦.mp3');
INSERT INTO `song` VALUES (42, 20, '稻香-周杰伦', '魔杰座', '2024-05-05 00:00:00', '2024-05-05 18:04:06', 'http://localhost:9090/file/download/1714903454684_稻香-周杰伦.jpg', 'http://localhost:9090/file/download/1714903468824_稻香-周杰伦.lrc', 'http://localhost:9090/file/download/1714903461776_稻香-周杰伦.mp3', '1714903454684_稻香-周杰伦.jpg', '1714903468824_稻香-周杰伦.lrc', '1714903461776_稻香-周杰伦.mp3');
INSERT INTO `song` VALUES (43, 20, '七里香-周杰伦', '七里香', '2024-05-05 00:00:00', '2024-05-05 18:07:52', 'http://localhost:9090/file/download/1714903702544_七里香-周杰伦.jpg', 'http://localhost:9090/file/download/1714903685628_七里香-周杰伦.lrc', 'http://localhost:9090/file/download/1714903694116_七里香-周杰伦.mp3', '1714903702544_七里香-周杰伦.jpg', '1714903685628_七里香-周杰伦.lrc', '1714903694116_七里香-周杰伦.mp3');
INSERT INTO `song` VALUES (44, 20, '花海-周杰伦', '魔杰座', '2024-05-05 00:00:00', '2024-05-05 18:10:49', 'http://localhost:9090/file/download/1714903871376_花海-周杰伦.jpg', 'http://localhost:9090/file/download/1714903884330_花海-周杰伦.lrc', 'http://localhost:9090/file/download/1714903877526_花海-周杰伦.mp3', '1714903871376_花海-周杰伦.jpg', '1714903884330_花海-周杰伦.lrc', '1714903877526_花海-周杰伦.mp3');
INSERT INTO `song` VALUES (45, 20, '兰亭序-周杰伦', '魔杰座', '2024-05-05 18:15:25', '2024-05-05 18:15:25', 'http://localhost:9090/file/download/1714904174720_兰亭序-周杰伦.jpg', 'http://localhost:9090/file/download/1714904138148_兰亭序-周杰伦.lrc', 'http://localhost:9090/file/download/1714904147257_兰亭序-周杰伦.mp3', '1714904174720_兰亭序-周杰伦.jpg', '1714904138148_兰亭序-周杰伦.lrc', '1714904147257_兰亭序-周杰伦.mp3');
INSERT INTO `song` VALUES (46, 20, '青花瓷-周杰伦', '我很忙', '2024-05-05 18:18:25', '2024-05-05 18:18:25', 'http://localhost:9090/file/download/1714904354839_青花瓷-周杰伦.jpg', 'http://localhost:9090/file/download/1714904373953_青花瓷-周杰伦.lrc', 'http://localhost:9090/file/download/1714904365066_青花瓷-周杰伦.mp3', '1714904354839_青花瓷-周杰伦.jpg', '1714904373953_青花瓷-周杰伦.lrc', '1714904365066_青花瓷-周杰伦.mp3');
INSERT INTO `song` VALUES (47, 20, '告白气球-周杰伦', '周杰伦的床边故事', '2024-05-05 00:00:00', '2024-05-05 18:23:04', 'http://localhost:9090/file/download/1714904732005_告白气球-周杰伦.jpg', 'http://localhost:9090/file/download/1714904748272_告白气球-周杰伦.lrc', 'http://localhost:9090/file/download/1714904739924_告白气球-周杰伦.mp3', '1714904732005_告白气球-周杰伦.jpg', '1714904748272_告白气球-周杰伦.lrc', '1714904739924_告白气球-周杰伦.mp3');
INSERT INTO `song` VALUES (48, 20, '发如雪-周杰伦', '十一月的萧邦', '2024-05-05 18:28:59', '2024-05-05 18:28:59', 'http://localhost:9090/file/download/1714904953094_发如雪-周杰伦.jpg', 'http://localhost:9090/file/download/1714904984636_发如雪-周杰伦.lrc', 'http://localhost:9090/file/download/1714904961345_发如雪-周杰伦.mp3', '1714904953094_发如雪-周杰伦.jpg', '1714904984636_发如雪-周杰伦.lrc', '1714904961345_发如雪-周杰伦.mp3');
INSERT INTO `song` VALUES (49, 21, '富士山下-陈奕迅', 'What\'s Going On...?', '2024-05-05 18:34:31', '2024-05-05 18:34:31', 'http://localhost:9090/file/download/1714907881766_富士山下-陈奕迅.jpg', 'http://localhost:9090/file/download/1714907949885_富士山下-陈奕迅.lrc', 'http://localhost:9090/file/download/1714907926846_富士山下-陈奕迅.mp3', '1714907881766_富士山下-陈奕迅.jpg', '1714907949885_富士山下-陈奕迅.lrc', '1714907926846_富士山下-陈奕迅.mp3');
INSERT INTO `song` VALUES (50, 21, '浮夸-陈奕迅', 'U 87', '2024-05-05 00:00:00', '2024-05-05 18:35:13', 'http://localhost:9090/file/download/1714907890427_浮夸-陈奕迅.jpg', 'http://localhost:9090/file/download/1714907946198_浮夸-陈奕迅.lrc', 'http://localhost:9090/file/download/1714907920878_浮夸-陈奕迅.mp3', '1714907890427_浮夸-陈奕迅.jpg', '1714907946198_浮夸-陈奕迅.lrc', '1714907920878_浮夸-陈奕迅.mp3');
INSERT INTO `song` VALUES (51, 21, '爱情转移-陈奕迅', '认了吧', '2024-05-05 18:35:54', '2024-05-05 18:35:54', 'http://localhost:9090/file/download/1714907894211_爱情转移-陈奕迅.jpg', 'http://localhost:9090/file/download/1714907942401_爱情转移-陈奕迅.lrc', 'http://localhost:9090/file/download/1714907916499_爱情转移-陈奕迅.mp3', '1714907894211_爱情转移-陈奕迅.jpg', '1714907942401_爱情转移-陈奕迅.lrc', '1714907916499_爱情转移-陈奕迅.mp3');
INSERT INTO `song` VALUES (52, 21, '红玫瑰-陈奕迅', '认了吧', '2024-05-05 00:00:00', '2024-05-05 18:36:32', 'http://localhost:9090/file/download/1714907897057_红玫瑰-陈奕迅.jpg', 'http://localhost:9090/file/download/1714907936614_红玫瑰-陈奕迅.lrc', 'http://localhost:9090/file/download/1714907907771_红玫瑰-陈奕迅.mp3', '1714907897057_红玫瑰-陈奕迅.jpg', '1714907936614_红玫瑰-陈奕迅.lrc', '1714907907771_红玫瑰-陈奕迅.mp3');
INSERT INTO `song` VALUES (53, 22, '句号-G.E.M. 邓紫棋', '摩天动物园', '2024-05-05 00:00:00', '2024-05-05 19:25:19', 'http://localhost:9090/file/download/1714908476062_句号-G.E.M.邓紫棋.jpg', 'http://localhost:9090/file/download/1714908500603_句号-G.E.M.邓紫棋.lrc', 'http://localhost:9090/file/download/1714908491670_句号-G.E.M.邓紫棋.mp3', '1714908476062_句号-G.E.M.邓紫棋.jpg', '1714908500603_句号-G.E.M.邓紫棋.lrc', '1714908491670_句号-G.E.M.邓紫棋.mp3');
INSERT INTO `song` VALUES (54, 22, '孤独-G.E.M. 邓紫棋', '孤独', '2024-05-05 19:27:08', '2024-05-05 19:27:08', 'http://localhost:9090/file/download/1714908481031_孤独-G.E.M.邓紫棋.jpg', 'http://localhost:9090/file/download/1714908497236_孤独-G.E.M.邓紫棋.lrc', 'http://localhost:9090/file/download/1714908487929_孤独-G.E.M.邓紫棋.mp3', '1714908481031_孤独-G.E.M.邓紫棋.jpg', '1714908497236_孤独-G.E.M.邓紫棋.lrc', '1714908487929_孤独-G.E.M.邓紫棋.mp3');
INSERT INTO `song` VALUES (55, 25, 'Lover-Taylor Swift', 'Lover', '2024-05-05 00:00:00', '2024-05-05 20:07:19', 'http://localhost:9090/file/download/1714910849594_Lover-Taylor Swift.jpg', 'http://localhost:9090/file/download/1714910865785_Lover-Taylor Swift.lrc', 'http://localhost:9090/file/download/1714910855652_Lover-Taylor Swift.mp3', '1714910849594_Lover-Taylor Swift.jpg', '1714910865785_Lover-Taylor Swift.lrc', '1714910855652_Lover-Taylor Swift.mp3');
INSERT INTO `song` VALUES (56, 26, 'Baby-Justin Bieber', 'Baby', '2024-05-05 00:00:00', '2024-05-05 20:18:10', 'http://localhost:9090/file/download/1714911506721_Baby-Justin Bieber,Ludacris.jpg', 'http://localhost:9090/file/download/1714911502443_Baby-Justin Bieber,Ludacris.lrc', 'http://localhost:9090/file/download/1714911495852_Baby-Justin Bieber,Ludacris.mp3', '1714911506721_Baby-Justin Bieber,Ludacris.jpg', '1714911502443_Baby-Justin Bieber,Ludacris.lrc', '1714911495852_Baby-Justin Bieber,Ludacris.mp3');
INSERT INTO `song` VALUES (57, 26, 'Love Yourself-Justin Bieber', 'Purpose', '2024-05-05 00:00:00', '2024-05-05 20:20:10', 'http://localhost:9090/file/download/1714911651770_Love Yourself-Justin Bieber.jpg', 'http://localhost:9090/file/download/1714911664159_Love Yourself-Justin Bieber.lrc', 'http://localhost:9090/file/download/1714911658415_Love Yourself-Justin Bieber.mp3', '1714911651770_Love Yourself-Justin Bieber.jpg', '1714911664159_Love Yourself-Justin Bieber.lrc', '1714911658415_Love Yourself-Justin Bieber.mp3');
INSERT INTO `song` VALUES (58, 27, 'Sugar-Maroon 5', 'V (Deluxe Version)', '2024-05-05 00:00:00', '2024-05-05 20:29:15', 'http://localhost:9090/file/download/1714912163079_Sugar-Maroon 5.jpg', 'http://localhost:9090/file/download/1714912209338_Sugar-Maroon 5.lrc', 'http://localhost:9090/file/download/1714912168641_Sugar-Maroon 5.mp3', '1714912163079_Sugar-Maroon 5.jpg', '1714912209338_Sugar-Maroon 5.lrc', '1714912168641_Sugar-Maroon 5.mp3');
INSERT INTO `song` VALUES (59, 27, 'Payphone-Maroon 5', 'Payphone', '2024-05-05 00:00:00', '2024-05-05 20:34:05', 'http://localhost:9090/file/download/1714912453593_Payphone-Maroon 5.jpg', 'http://localhost:9090/file/download/1714912467858_Payphone-Maroon 5.lrc', 'http://localhost:9090/file/download/1714912462219_Payphone-Maroon 5.mp3', '1714912453593_Payphone-Maroon 5.jpg', '1714912467858_Payphone-Maroon 5.lrc', '1714912462219_Payphone-Maroon 5.mp3');
INSERT INTO `song` VALUES (60, 28, 'Lemon-米津玄師', 'レモン', '2024-05-05 00:00:00', '2024-05-05 20:43:01', 'http://localhost:9090/file/download/1714913113757_Lemon-米津玄師.jpg', 'http://localhost:9090/file/download/1714913134603_Lemon-米津玄師.lrc', 'http://localhost:9090/file/download/1714913124962_Lemon-米津玄師.mp3', '1714913113757_Lemon-米津玄師.jpg', '1714913134603_Lemon-米津玄師.lrc', '1714913124962_Lemon-米津玄師.mp3');
INSERT INTO `song` VALUES (61, 28, '打上花火-米津玄師', 'BOOTLEG', '2024-05-05 00:00:00', '2024-05-05 20:42:03', 'http://localhost:9090/file/download/1714913119951_打上花火-米津玄師.jpg', 'http://localhost:9090/file/download/1714913138171_打上花火-米津玄師.lrc', 'http://localhost:9090/file/download/1714913128525_打上花火-米津玄師.mp3', '1714913119951_打上花火-米津玄師.jpg', '1714913138171_打上花火-米津玄師.lrc', '1714913128525_打上花火-米津玄師.mp3');
INSERT INTO `song` VALUES (62, 29, '神のまにまに-初音ミク', '聖槍爆裂ボーイ', '2024-05-05 00:00:00', '2024-05-05 20:59:42', 'http://localhost:9090/file/download/1714913975907_神のまにまに-初音ミク.jpg', 'http://localhost:9090/file/download/1714913999890_神のまにまに-初音ミク.lrc', 'http://localhost:9090/file/download/1714913990500_神のまにまに-初音ミク.mp3', '1714913975907_神のまにまに-初音ミク.jpg', '1714913999890_神のまにまに-初音ミク.lrc', '1714913990500_神のまにまに-初音ミク.mp3');
INSERT INTO `song` VALUES (63, 29, '深海少女-初音ミク', 'EXIT TUNES PRESENTS Vocalodream', '2024-05-05 00:00:00', '2024-05-05 21:00:25', 'http://localhost:9090/file/download/1714913979090_深海少女-初音ミク.jpg', 'http://localhost:9090/file/download/1714914002940_深海少女-初音ミク.lrc', 'http://localhost:9090/file/download/1714913994002_深海少女-初音ミク.mp3', '1714913979090_深海少女-初音ミク.jpg', '1714914002940_深海少女-初音ミク.lrc', '1714913994002_深海少女-初音ミク.mp3');
INSERT INTO `song` VALUES (66, 36, 'w-王力宏', '1', '2024-05-27 10:59:27', '2024-05-27 10:59:27', NULL, 'http://localhost:9090/file/download/1716778898824_他不懂-张杰.lrc', 'http://localhost:9090/file/download/1716778885456_何必在一起-张杰.mp3', NULL, '1716778898824_他不懂-张杰.lrc', '1716778885456_何必在一起-张杰.mp3');

-- ----------------------------
-- Table structure for song_list
-- ----------------------------
DROP TABLE IF EXISTS `song_list`;
CREATE TABLE `song_list`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌单标题',
  `introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌单简介',
  `style` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌单风格',
  `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌单图片访问地址',
  `pic_local` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '歌单图片本地地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '歌单\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of song_list
-- ----------------------------
INSERT INTO `song_list` VALUES (14, '华语rnb：别让世俗淹没生活的浪漫和热情', '别让世俗淹没生活的浪漫和热情', '华语', 'http://localhost:9090/file/download/1714314254455_华语rnb.jpg', '1714314254455_华语rnb.jpg');
INSERT INTO `song_list` VALUES (15, '忍唱 | QQ飞车必备 漂移到起飞！', '大家好，欢迎来到黑糖音乐秀的频道！今天我们书接上回，继续为各位带来我们的「忍唱大挑战」系列，本期的主题是QQ飞车里的大火歌曲，每一首歌都是飞车党满满的回忆~喜欢的小伙伴赶紧抱走歌单啦！', '流行', 'http://localhost:9090/file/download/1714916042283_2.jpg', '1714916042283_2.jpg');
INSERT INTO `song_list` VALUES (17, ' 华语80，90后经典老歌 | 怀念青春的模样', ' 华语80，90后经典老歌 | 怀念青春的模样', '华语', 'http://localhost:9090/file/download/1714916048121_1.jpg', '1714916048121_1.jpg');
INSERT INTO `song_list` VALUES (18, '90后青春回忆杀：周杰伦歌曲Jay经典', '90后青春回忆杀：周杰伦歌曲Jay经典', '华语', 'http://localhost:9090/file/download/1714916051176_3.jpg', '1714916051176_3.jpg');
INSERT INTO `song_list` VALUES (19, '[回忆享] 回不去的那个岁月时光', '童年像画笔，我们如同白纸，绘出了五彩斑斓的回忆。 歌单中汇总了本人幼时看过的动画片的原声带，如果你也很回忆童年的话，不妨一起来听听，试试不看动画片你可以听出来多少首音乐。', '经典', 'http://localhost:9090/file/download/1714916054119_4.jpg', '1714916054119_4.jpg');
INSERT INTO `song_list` VALUES (20, '欧美私藏·Discover Weekly', '欧美报刊亭诚意推荐：这里无关榜单，无关流量，无关艺人影响力。你可以真正去发掘那些值得被聆听的优秀作品。 歌单收录了自2022年以来的优质欧美单曲，并写下了推荐语以供听众阅读。目前累计更新超过两年，2024年仍在持续推荐。', '英语', 'http://localhost:9090/file/download/1714916181376_5.jpg', '1714916181376_5.jpg');
INSERT INTO `song_list` VALUES (21, '柠萌菌私藏丨二次元催泪神曲！', '又好听有催泪的日语歌曲！总有你喜欢的一首', 'ACG', 'http://localhost:9090/file/download/1714916279786_6.jpg', '1714916279786_6.jpg');
INSERT INTO `song_list` VALUES (22, '再见少年，尚好的青春才刚刚开始', '毕业快乐。我的生命里，无疑会有很多个夏天，但没有哪一个，会如今夏。正如五月天的干杯：也许会 有一天 世界真的有重点也要和你举起回忆酿的甜 和你再干一杯。万物可爱，人间值得，未来可期。', 'ACG', 'http://localhost:9090/file/download/1714916518702_7.jpg', '1714916518702_7.jpg');
INSERT INTO `song_list` VALUES (23, '路飞：“我是要成为海贼王的男人”', '《海贼王》是日本漫画家尾田荣一郎的作品，描述的是一个名叫路飞的少年想成为海贼王，因误食了香克斯收藏的恶魔果实，变成了橡皮人的故事。恶魔果实使路飞永远不能游泳，但他始终没有改变成为海贼王的理想“找到传说中哥尔•D•罗杰的秘宝，并且成为海贼王”开始了冒险之旅。', 'ACG', 'http://localhost:9090/file/download/1714916623476_8.jpg', '1714916623476_8.jpg');
INSERT INTO `song_list` VALUES (24, '小众英文| 迷人的温柔使人安静', '  对于爱安静的人，这里面有很多都很安静哒，如果喜欢学习的，都可以听听这里面的歌，因为温柔会让人安静下来', '英语', 'http://localhost:9090/file/download/1714916722963_9.jpg', '1714916722963_9.jpg');
INSERT INTO `song_list` VALUES (25, '集中精神不打瞌睡，搬砖必备歌单', '上班不摸鱼，老板喜欢你，努力去搬砖，争取早日让老板换台法拉利！ 今日份提神搬砖歌单，有节奏感但不燥哟~', '流行', 'http://localhost:9090/file/download/1714916931239_10.jpg', '1714916931239_10.jpg');
INSERT INTO `song_list` VALUES (26, '捕春幻影恋➢寄一封泰式春日来信', '在春日的温柔阳光下， 让音乐点亮你的心灵花园。 琅琅上口的旋律与情感丰富的歌词， 将带你穿越时空， 感受泰剧影视音乐的独特魅力。 从动听的插曲到经典的主题曲， 每一首歌都是一段难忘的故事， 每一个音符都是一种情感的流露。 捕捉春日的浪漫时光， 用音乐的方式来寄一封春天的信吧！', '流行', 'http://localhost:9090/file/download/1714917008415_11.jpg', '1714917008415_11.jpg');
INSERT INTO `song_list` VALUES (27, '舒压轻音乐｜温柔轻风吹散烦恼', '从清晨的阳光与风声中醒来，城市的喧嚣已消散，只想抓住每一份平静，偶尔去散步吹风，减压舒适。', '轻音乐', 'http://localhost:9090/file/download/1714917070689_12.jpg', '1714917070689_12.jpg');
INSERT INTO `song_list` VALUES (28, '暖心一刻，天凉有好景万事皆顺心', '天气渐凉，但好景却依旧存在，只不过是换了一种颜色。只要你有一双善于发现美好事物的眼睛，那美好的事就会与你常伴！', '流行', 'http://localhost:9090/file/download/1714917136160_13.jpg', '1714917136160_13.jpg');
INSERT INTO `song_list` VALUES (29, 'ACG｜新海诚全系列作品主题曲&插入曲', '收录了2000年之后新海诚所有导演动画的主题曲和插入曲，包括《铃芽户缔》《天气之子》《你的名字》《言叶之庭》《追逐繁星的孩子》《秒速5厘米》《云之彼端，约定的地方》《星之声》', 'ACG', 'http://localhost:9090/file/download/1714917207406_14.jpg', '1714917207406_14.jpg');
INSERT INTO `song_list` VALUES (30, '国漫古风掀起了谁的回忆', '在这些年里，国产动漫的制作已经越发成熟精致，并且带着浓郁的中国本土风格。对于现在起步不久的国漫市场来讲，国漫还有很大的发展空间。国产动漫的崛起中，不少古风向的动漫更是让人喜欢，古风国漫的歌曲会掀起谁的回忆呢？其中，会有你的吗？', '古风', 'http://localhost:9090/file/download/1714917261884_15.jpg', '1714917261884_15.jpg');

SET FOREIGN_KEY_CHECKS = 1;
