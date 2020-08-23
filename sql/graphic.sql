/*
 Navicat Premium Data Transfer

 Source Server         : 39.99.191.11
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : 39.99.191.11:3306
 Source Schema         : risk_evaluation

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 23/08/2020 11:23:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for arrears_info_tbl
-- ----------------------------
DROP TABLE IF EXISTS `arrears_info_tbl`;
CREATE TABLE `arrears_info_tbl`  (
  `merId` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户号',
  `merName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理人名称',
  `productName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '产品名称',
  `arrearsNum` int(255) NULL DEFAULT NULL COMMENT '欠费次数',
  PRIMARY KEY (`merId`, `productName`) USING BTREE,
  UNIQUE INDEX `merId and productName`(`merId`, `productName`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of arrears_info_tbl
-- ----------------------------
INSERT INTO `arrears_info_tbl` VALUES ('test', '商业银行', '产品1', 5);
INSERT INTO `arrears_info_tbl` VALUES ('test', '商业银行', '产品2', 3);
INSERT INTO `arrears_info_tbl` VALUES ('test1', '天地银行', '金元宝', 2);

-- ----------------------------
-- Table structure for daily_deposit_tbl
-- ----------------------------
DROP TABLE IF EXISTS `daily_deposit_tbl`;
CREATE TABLE `daily_deposit_tbl`  (
  `merId` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户号',
  `merName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理员名称',
  `productName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `dailyDeposit` decimal(18, 2) NULL DEFAULT NULL COMMENT '日均存款(元)',
  PRIMARY KEY (`merId`) USING BTREE,
  UNIQUE INDEX `merId and productName`(`merId`, `productName`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of daily_deposit_tbl
-- ----------------------------
INSERT INTO `daily_deposit_tbl` VALUES ('tes1t', '天地银行', '金元宝', 800000.13);
INSERT INTO `daily_deposit_tbl` VALUES ('test', '商业银行', '产品1', 200000.00);
INSERT INTO `daily_deposit_tbl` VALUES ('test1', '天地银行', '银元宝', 400000.13);

-- ----------------------------
-- Table structure for invest_supervise_tbl
-- ----------------------------
DROP TABLE IF EXISTS `invest_supervise_tbl`;
CREATE TABLE `invest_supervise_tbl`  (
  `merId` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户号',
  `merName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理人名称',
  `productName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `inveSupDate` timestamp(0) NULL DEFAULT NULL COMMENT '投资监督日期',
  `inveSupTips` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投资监督提示',
  PRIMARY KEY (`merId`) USING BTREE,
  UNIQUE INDEX `merId and productName`(`merId`, `productName`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of invest_supervise_tbl
-- ----------------------------
INSERT INTO `invest_supervise_tbl` VALUES ('test', '商业银行', '产品1', '2020-08-22 15:04:18', '第一次测试');
INSERT INTO `invest_supervise_tbl` VALUES ('test1', '天地银行', '银元宝', '2020-08-19 19:28:42', '重阳节没烧');

-- ----------------------------
-- Table structure for merchant_info_tbl
-- ----------------------------
DROP TABLE IF EXISTS `merchant_info_tbl`;
CREATE TABLE `merchant_info_tbl`  (
  `merId` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户号',
  `merName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理员名称',
  `busiScale` decimal(18, 2) NULL DEFAULT NULL COMMENT '业务规模',
  `busiIncome` decimal(18, 2) NULL DEFAULT NULL COMMENT '业务收入',
  `productNum` int(5) NULL DEFAULT NULL COMMENT '产品数量',
  `platfPCFlag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否开通服务平台PC',
  `platfAPPFlag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否开通杭银托管APP',
  `platfGZFlag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否开通杭银托管公众号',
  `perEvaluate` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否开通绩效评估',
  `valuaOutsourc` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否开通估值外包',
  `bankMemo` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否签订银行间备忘录',
  `electrOrder` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否电子指令直连',
  `fileTransfer` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否文件传输直连',
  `electrCheck` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否电子对账',
  `vitality` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '活动参与度是否活跃',
  PRIMARY KEY (`merId`) USING BTREE,
  UNIQUE INDEX `merId`(`merId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of merchant_info_tbl
-- ----------------------------
INSERT INTO `merchant_info_tbl` VALUES ('test', '商业银行', 300000.19, 3000000.46, 20, '1', '1', '1', '1', '1', '1', '1', '1', '1', '1');
INSERT INTO `merchant_info_tbl` VALUES ('test1', '天地银行', 1000.00, 200000.00, 4, '0', '1', '0', '2', '0', '1', '1', '0', '0', '1');

-- ----------------------------
-- Table structure for public_opinion_tbl
-- ----------------------------
DROP TABLE IF EXISTS `public_opinion_tbl`;
CREATE TABLE `public_opinion_tbl`  (
  `merId` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户号',
  `merName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理人名称',
  `productName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '产品名称',
  `pubOpinDate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '舆情日期',
  `massage` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '舆情描述',
  PRIMARY KEY (`merId`) USING BTREE,
  UNIQUE INDEX `merId and productName`(`merId`, `productName`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of public_opinion_tbl
-- ----------------------------
INSERT INTO `public_opinion_tbl` VALUES ('test', '商业银行', '产品测试', '2020-08-22 18:12:02', '消息联系不上，打电话没人接，手杭停机，产品营销不好，产品营销不好营销不好');
INSERT INTO `public_opinion_tbl` VALUES ('test1', '天地银行', '银元宝', '2020-08-19 19:27:02', '清明节烧纸过多');
INSERT INTO `public_opinion_tbl` VALUES ('test2', '天地银行', '金元宝', '2020-08-27 19:28:00', '没烧过');

SET FOREIGN_KEY_CHECKS = 1;
