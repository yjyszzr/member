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
  `content_desc` text NOT NULL COMMENT '内容补充',
  `msg_desc` text NOT NULL COMMENT '消息附加信息',
  `msg_url` text NOT NULL COMMENT '消息详情地址',
  `msg_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '信息类型：0通知1消息',
  `push_type` varchar(255) DEFAULT NULL COMMENT '推送类型',
  `push_value` varchar(255) DEFAULT NULL COMMENT '推送值',
  `receiver` int(11) DEFAULT '-1' COMMENT '接收者',
  `receiver_mobile` varchar(11) DEFAULT NULL COMMENT '接受者手机号',
  `object_type` int(4) DEFAULT '0' COMMENT '业务类型',
  `is_mobile_success` tinyint(1) DEFAULT '0' COMMENT '短信是否发送成功',
  `is_push_success` tinyint(1) DEFAULT '0' COMMENT '推送是否发送成功',
  `is_read` tinyint(1) DEFAULT '0' COMMENT '是否读取',
  PRIMARY KEY (`msg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1443248 DEFAULT CHARSET=utf8 COMMENT='用户消息信息表';

-- ----------------------------
-- Table structure for dl_message
-- ----------------------------
DROP TABLE IF EXISTS `dl_complain`;
CREATE TABLE `dl_complain` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '投诉ID',
  `complainer` int(11) NOT NULL COMMENT '投诉者',
  `complain_time` int(11) NOT NULL COMMENT '投诉时间',
  `complain_content` text NOT NULL COMMENT '投诉内容',
  `is_read` tinyint(1) DEFAULT '0' COMMENT '是否读取',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COMMENT='用户投诉信息表';




-- 2018-05-24
alter table dl_user_bonus add is_read tinyint(1)  default 0 not null comment '用户是否读取：0未读取，1读取'; 

--  2018-06-01
alter table dl_order add device_channel varchar(16) comment '用户设备渠道';
alter table dl_user_login_log add device_channel varchar(16) comment '用户设备渠道';
alter table dl_user add device_channel varchar(16) comment '用户设备渠道';
alter table dl_user_login_log add login_result varchar(768) comment '登录结果';
alter table dl_order add award_time int(11) comment '派奖时间';
