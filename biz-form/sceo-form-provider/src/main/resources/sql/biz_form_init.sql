/*
Navicat MySQL Data Transfer

Source Server         : 192.168.13.52
Source Server Version : 50629
Source Host           : 192.168.13.52:3306
Source Database       : biz_form

Target Server Type    : MYSQL
Target Server Version : 50629
File Encoding         : 65001

Date: 2017-07-06 17:46:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for biz_form_classify
-- ----------------------------
DROP TABLE IF EXISTS `biz_form_classify`;
CREATE TABLE `biz_form_classify` (
  `classify_id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '分类ID',
  `classify_code` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '分类编码',
  `classify_name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '分类名称',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `created_by` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '创建人',
  `updated_time` datetime DEFAULT NULL COMMENT '修改时间',
  `updated_by` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '修改人',
  `delete_flag` varchar(1) COLLATE utf8_bin DEFAULT '0' COMMENT '删除标示',
  PRIMARY KEY (`classify_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of biz_form_classify
-- ----------------------------

INSERT INTO `biz_form_classify` VALUES ('1', '0', '其它', '2017-07-10 09:52:05', 'system', '2017-06-16 16:52:25', null, '0');


-- ----------------------------
-- Table structure for biz_form_list_setting
-- ----------------------------
DROP TABLE IF EXISTS `biz_form_list_setting`;
CREATE TABLE `biz_form_list_setting` (
  `form_list_id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '表单列表主键ID',
  `form_list_title` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '表单列表标题',
  `related_version_id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '关联的模型表名称',
  `filter_data` text COLLATE utf8_bin NOT NULL COMMENT '查询条件',
  `column_data` text COLLATE utf8_bin NOT NULL COMMENT '需要显示的列',
  `btn_data` text COLLATE utf8_bin NOT NULL COMMENT '需要显示的按钮数据',
  `page_flag` char(1) COLLATE utf8_bin DEFAULT NULL COMMENT '是否要分页',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `created_by` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '创建人',
  `updated_time` datetime DEFAULT NULL COMMENT '修改时间',
  `updated_by` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`form_list_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of biz_form_list_setting
-- ----------------------------
-- ----------------------------
-- Table structure for biz_form_setting
-- ----------------------------
DROP TABLE IF EXISTS `biz_form_setting`;
CREATE TABLE `biz_form_setting` (
  `form_id` varchar(32) COLLATE utf8_bin NOT NULL,
  `form_code` varchar(32) COLLATE utf8_bin NOT NULL,
  `form_name` varchar(64) COLLATE utf8_bin NOT NULL,
  `form_status` char(2) COLLATE utf8_bin NOT NULL,
  `form_remarks` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `classify_code` varchar(32) COLLATE utf8_bin NOT NULL,
  `related_model_id` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `edmc_name_en` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `created_time` datetime NOT NULL,
  `created_by` varchar(32) COLLATE utf8_bin NOT NULL,
  `updated_time` datetime DEFAULT NULL,
  `updated_by` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `delete_flag` varchar(1) COLLATE utf8_bin DEFAULT '0',
  PRIMARY KEY (`form_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for biz_form_version
-- ----------------------------
DROP TABLE IF EXISTS `biz_form_version`;
CREATE TABLE `biz_form_version` (
  `version_id` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '0',
  `form_id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '表单关联ID',
  `form_version` int(10) NOT NULL COMMENT '表单版本号',
  `form_data` text COLLATE utf8_bin COMMENT '表单设计数据',
  `form_status` char(2) COLLATE utf8_bin DEFAULT NULL COMMENT '表单状态',
  `created_by` varchar(32) COLLATE utf8_bin NOT NULL,
  `created_time` datetime DEFAULT NULL,
  `updated_by` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `delete_flag` varchar(1) COLLATE utf8_bin NOT NULL DEFAULT '0',
  `form_list_flag` varchar(1) COLLATE utf8_bin DEFAULT NULL
  PRIMARY KEY (`version_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

