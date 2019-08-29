-- ----------------------------
-- 菜单权限表
-- ----------------------------
DROP TABLE IF EXISTS sys_menu;
CREATE TABLE sys_menu
(
    `menu_id`     varchar(32) NOT NULL COMMENT '菜单ID',
    `parent_id`   varchar(32)  DEFAULT '0' COMMENT '父菜单ID',
    `menu_name`   varchar(64) NOT NULL COMMENT '菜单名称',
    `menu_type`   char(1)      DEFAULT NULL COMMENT '菜单类型（M目录 C菜单 F按钮）',
    `menu_url`    varchar(200) DEFAULT '#' COMMENT '菜单URL',
    `order`       int(4)       DEFAULT 0 COMMENT '显示顺序',
    `target`      varchar(16)  DEFAULT '_item' COMMENT '打开方式（_item 页签中打开，_blank 新窗口打开，_current 当前窗口打开）',
    `visible`     char(1)      DEFAULT 0 COMMENT '菜单状态（0显示 1隐藏）',
    `permission`  varchar(128) DEFAULT NULL COMMENT '权限标识',
    `icon`        varchar(128) DEFAULT '#' COMMENT '菜单图标',
    `create_by`   varchar(64)  DEFAULT NULL COMMENT '创建者',
    `update_by`   varchar(64)  DEFAULT NULL COMMENT '更新者',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`      varchar(512) DEFAULT NULL COMMENT '备注信息',
    PRIMARY KEY (`menu_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = '菜单权限表';
-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单
INSERT INTO sys_menu
VALUES ('1', '0', '系统管理', 'M', '#', 1, '_item', '0', NULL, 'fa fa-gear', 'admin', 'liusha', sysdate(), sysdate(), '系统管理目录');
-- 二级菜单
INSERT INTO sys_menu
VALUES ('2', '1', '菜单管理', 'C', '/sys/menu', 1, '_item', '0', 'sys:menu:view', '#', 'admin', 'liusha', sysdate(), sysdate(), '菜单管理菜单');
-- 三级菜单
INSERT INTO sys_menu
VALUES ('3', '2', '菜单查询', 'F', '#', 1, '_item', '0', 'sys:menu:list', '#', 'admin', 'liusha', sysdate(), sysdate(), '菜单查询按钮');