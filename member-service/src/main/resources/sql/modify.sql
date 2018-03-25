/************************************************20170909  新增报表功能*************************************************/
#  sql script


-- ----------------------------
-- Table structure for dl_message
-- ----------------------------
DROP TABLE IF EXISTS `dl_message`;
CREATE TABLE `dl_message` (
  `msg_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `sender` int(11) NOT NULL COMMENT '发送者',
  `send_time` int(11) NOT NULL COMMENT '发送时间',
  `title` varchar(64) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `msg_desc` text NOT NULL COMMENT '消息附加信息',
  `msg_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '信息类型：0通知1消息',
  `push_type` varchar(255) DEFAULT NULL COMMENT '推送类型',
  `push_value` varchar(255) DEFAULT NULL COMMENT '推送值',
  `receiver` int(11) DEFAULT '-1' COMMENT '接收者',
  `receiver_mobile` varchar(11) DEFAULT NULL COMMENT '接受者手机号',
  `object_type` varchar(60) DEFAULT NULL COMMENT '业务类型',
  `is_mobile_success` tinyint(1) DEFAULT '0' COMMENT '短信是否发送成功',
  `is_push_success` tinyint(1) DEFAULT '0' COMMENT '推送是否发送成功',
  `is_read` tinyint(1) DEFAULT '0' COMMENT '是否读取',
  PRIMARY KEY (`msg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1443248 DEFAULT CHARSET=utf8 COMMENT='用户消息信息表';