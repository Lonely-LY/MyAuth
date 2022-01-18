/*
 Navicat Premium Data Transfer

 Source Server         : 本地_MySQL_8
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : myauthtest

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 16/01/2022 15:21:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ma_admin
-- ----------------------------
DROP TABLE IF EXISTS `ma_admin`;
CREATE TABLE `ma_admin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pass` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `reg_time` int(10) NULL DEFAULT NULL,
  `last_time` int(10) NULL DEFAULT NULL,
  `last_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(2) NULL DEFAULT NULL COMMENT '0=禁用，1=正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ma_ban
-- ----------------------------
DROP TABLE IF EXISTS `ma_ban`;
CREATE TABLE `ma_ban`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '要封禁的机器码或者IP',
  `add_time` int(10) NULL DEFAULT NULL,
  `to_time` int(10) NULL DEFAULT NULL COMMENT '封禁到期时间，0=永久',
  `why` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封禁原因',
  `from_soft_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ma_card
-- ----------------------------
DROP TABLE IF EXISTS `ma_card`;
CREATE TABLE `ma_card`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ckey` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `point` int(10) NULL DEFAULT NULL COMMENT '点数',
  `seconds` int(10) NULL DEFAULT NULL COMMENT '秒数',
  `add_time` int(10) NULL DEFAULT NULL COMMENT '生成时间',
  `let_time` int(10) NULL DEFAULT NULL COMMENT '使用时间',
  `let_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '使用人账号',
  `status` int(2) NULL DEFAULT NULL COMMENT '卡密状态，0=未使用，1=已使用，2=被禁用',
  `from_soft_id` int(11) NULL DEFAULT NULL COMMENT '所属软件id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ma_config
-- ----------------------------
DROP TABLE IF EXISTS `ma_config`;
CREATE TABLE `ma_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dingbot_access_token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '钉钉机器人',
  `bot_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0=关闭通知，1=开启通知',
  `soft_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '添加软件扣除点数',
  `web_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0=维护，1=正常',
  `seo_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `seo_keywords` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `seo_description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ma_data
-- ----------------------------
DROP TABLE IF EXISTS `ma_data`;
CREATE TABLE `ma_data`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上报类型',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '上报内容',
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IP',
  `device_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备信息',
  `add_time` int(10) NULL DEFAULT NULL COMMENT '上报时间',
  `from_soft_id` int(11) NULL DEFAULT NULL,
  `from_ver_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ma_event
-- ----------------------------
DROP TABLE IF EXISTS `ma_event`;
CREATE TABLE `ma_event`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '事件名称，同一软件下禁止重复',
  `point` int(10) NULL DEFAULT NULL COMMENT '点数变动值，增加为正数，扣除为负数',
  `seconds` int(10) NULL DEFAULT NULL COMMENT '秒数变动值，增加为正数，扣除为负数',
  `add_time` int(10) NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0=禁用，1=正常',
  `from_soft_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ma_js
-- ----------------------------
DROP TABLE IF EXISTS `ma_js`;
CREATE TABLE `ma_js`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `js_fun` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `js_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `add_time` int(10) NULL DEFAULT NULL,
  `status` int(2) NULL DEFAULT NULL,
  `from_soft_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ma_msg
-- ----------------------------
DROP TABLE IF EXISTS `ma_msg`;
CREATE TABLE `ma_msg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `keyword` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `msg` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `status` int(2) NULL DEFAULT NULL COMMENT '0=禁用，1=正常',
  `from_soft_id` int(11) NULL DEFAULT NULL COMMENT '所属软件id',
  `from_ver_id` int(11) NULL DEFAULT NULL COMMENT '所属软件版本，留空则全版本可用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ma_plog
-- ----------------------------
DROP TABLE IF EXISTS `ma_plog`;
CREATE TABLE `ma_plog`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `point` int(10) NULL DEFAULT NULL COMMENT '变动点数，扣除为负数',
  `after_point` int(10) NULL DEFAULT NULL COMMENT '变动后的点数',
  `seconds` int(10) NULL DEFAULT NULL COMMENT '变动秒数，扣除为负数',
  `after_seconds` int(10) NULL DEFAULT NULL COMMENT '变动后的到期时间',
  `from_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变动账号',
  `add_time` int(10) NULL DEFAULT NULL COMMENT '变动时间',
  `from_event_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属事件名称',
  `from_event_id` int(11) NULL DEFAULT NULL COMMENT '所属事件ID',
  `from_soft_id` int(11) NULL DEFAULT NULL,
  `from_ver_id` int(11) NULL DEFAULT NULL,
  `remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '日志的说明内容，使用卡密时包含使用的卡密',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ma_soft
-- ----------------------------
DROP TABLE IF EXISTS `ma_soft`;
CREATE TABLE `ma_soft`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `skey` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(2) NULL DEFAULT NULL COMMENT '0=停用，1=正常，2=维护',
  `type` int(2) NULL DEFAULT NULL COMMENT '0=收费，1=免费',
  `add_time` int(10) NULL DEFAULT NULL,
  `gen_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据加密秘钥',
  `gen_status` int(2) NULL DEFAULT NULL COMMENT '0=数据不加密，1=数据加密',
  `batch_soft` int(2) NULL DEFAULT NULL COMMENT '0=不允许多开，1=允许多开',
  `multiple_login` int(2) NULL DEFAULT NULL COMMENT '0=不允许多地同时登录，1=允许多地同时登录',
  `heart_time` int(10) NULL DEFAULT NULL COMMENT '心跳有效时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ma_user
-- ----------------------------
DROP TABLE IF EXISTS `ma_user`;
CREATE TABLE `ma_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号，卡密登录时，为卡密，免费用户时为机器码',
  `pass` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `point` int(30) NULL DEFAULT NULL COMMENT '点数',
  `qq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `last_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后登录的IP',
  `last_time` int(10) NULL DEFAULT NULL COMMENT '最后登录的时间戳',
  `reg_time` int(10) NULL DEFAULT NULL COMMENT '用户注册的时间戳',
  `auth_time` int(10) NULL DEFAULT NULL COMMENT '授权到期的时间戳，-1=永久',
  `from_soft_id` int(10) NULL DEFAULT NULL COMMENT '所属软件id',
  `from_soft_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属软件key',
  `from_ver_id` int(10) NULL DEFAULT NULL COMMENT '最后登录的软件的版本id',
  `from_ver_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后登录的软件的版本key',
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录成功的token',
  `status` int(2) NULL DEFAULT 1 COMMENT '0=禁用，1=正常',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `device_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后登录的设备信息',
  `device_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机器码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ma_version
-- ----------------------------
DROP TABLE IF EXISTS `ma_version`;
CREATE TABLE `ma_version`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ver` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `vkey` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `from_soft_id` int(11) NULL DEFAULT NULL COMMENT '所属软件id',
  `upd_log` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '更新日志',
  `upd_time` int(10) NULL DEFAULT NULL,
  `upd_type` int(2) NULL DEFAULT NULL COMMENT '更新模式，0=可选，1=强制',
  `status` int(2) NULL DEFAULT NULL COMMENT '0=停用，1=正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;