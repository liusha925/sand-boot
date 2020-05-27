-- ----------------------------
-- 菜单表
-- ----------------------------
DROP TABLE IF EXISTS sys_menu;
CREATE TABLE sys_menu
(
    `menu_id`     varchar(32) NOT NULL COMMENT '菜单ID',
    `parent_id`   varchar(32) NOT NULL COMMENT '父菜单ID',
    `menu_name`   varchar(64) NOT NULL COMMENT '菜单名称',
    `menu_type`   char(1)     NOT NULL COMMENT '菜单类型（M目录 C菜单 F按钮）',
    `menu_url`    varchar(128) DEFAULT '#' COMMENT '菜单URL',
    `order_num`   int(4)       DEFAULT 0 COMMENT '显示顺序',
    `target`      varchar(16)  DEFAULT '_item' COMMENT '打开方式（_item 页签中打开，_blank 新窗口打开，_current 当前窗口打开）',
    `visible`     char(1)      DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
    `purview`     varchar(128) DEFAULT NULL COMMENT '权限标识',
    `icon`        varchar(128) DEFAULT '#' COMMENT '菜单图标',
    `create_by`   varchar(64)  DEFAULT NULL COMMENT '创建者',
    `update_by`   varchar(64)  DEFAULT NULL COMMENT '更新者',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`      varchar(512) DEFAULT NULL COMMENT '备注信息',
    PRIMARY KEY (`menu_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = '菜单表';
-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单
INSERT INTO sys_menu
VALUES ('1', '0', '系统管理', 'M', '#', 1, '_item', '0', NULL, 'fa fa-gear', 'admin', 'liusha', sysdate(), sysdate(),
        '系统管理目录');
-- 二级菜单
INSERT INTO sys_menu
VALUES ('2', '1', '菜单管理', 'C', '/sys/menu', 1, '_item', '0', 'sys:menu:view', '#', 'admin', 'liusha', sysdate(),
        sysdate(), '菜单管理菜单');
-- 三级菜单
INSERT INTO sys_menu
VALUES ('3', '2', '菜单查询', 'F', '#', 1, '_item', '0', 'sys:menu:list', '#', 'admin', 'liusha', sysdate(), sysdate(),
        '菜单查询按钮');

-- ----------------------------
-- 角色表
-- ----------------------------
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role
(
    `role_id`     varchar(32) NOT NULL COMMENT '角色ID',
    `role_name`   varchar(64) NOT NULL COMMENT '角色名称',
    `role_key`    varchar(64) NOT NULL COMMENT '权限字符',
    `order_num`   int(4)       DEFAULT 0 COMMENT '显示顺序',
    `status`      char(1)      DEFAULT '0' COMMENT '角色状态（0正常 1停用）',
    `del_flag`    char(1)      DEFAULT '0' COMMENT '删除标志（0逻辑未删除 1逻辑已删除）',
    `data_scope`  char(1)      DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
    `create_by`   varchar(64)  DEFAULT NULL COMMENT '创建者',
    `update_by`   varchar(64)  DEFAULT NULL COMMENT '更新者',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`      varchar(512) DEFAULT NULL COMMENT '备注信息',
    PRIMARY KEY (`role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = '角色表';
-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
INSERT INTO `sys_role`
VALUES ('1', '超级管理员', 'admin', 0, '0', '0', '1', NULL, 'admin', '2019-09-01 08:56:40', '2019-09-01 18:41:38', '拥有至高权限');

-- ----------------------------
-- 角色-菜单关联表   角色1-N菜单
-- ----------------------------
DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu
(
    `role_id` varchar(32) NOT NULL COMMENT '角色ID',
    `menu_id` varchar(32) NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = '角色-菜单关联表';

-- ----------------------------
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user
(
    `user_id`     varchar(32) NOT NULL COMMENT '用户ID',
    `username`    varchar(16) NOT NULL COMMENT '用户名',
    `password`    varchar(64) NOT NULL COMMENT '密码',
    `real_name`   varchar(64) NOT NULL COMMENT '真实姓名',
    `is_admin` tinyint(1) DEFAULT '0' COMMENT '是否为超级管理员（0否 1是）',
    `create_by`   varchar(64)  DEFAULT NULL COMMENT '创建者',
    `update_by`   varchar(64)  DEFAULT NULL COMMENT '更新者',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`      varchar(512) DEFAULT NULL COMMENT '备注信息',
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = '用户表';

-- ----------------------------
-- 用户-角色关联表   用户1-N角色
-- ----------------------------
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role
(
    `user_id` varchar(32) NOT NULL COMMENT '用户ID',
    `role_id` varchar(32) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = '用户-角色关联表';

-- ----------------------------
-- 系统日志表
-- ----------------------------
DROP TABLE IF EXISTS sys_log;
CREATE TABLE sys_log
(
    `log_id`         varchar(32) NOT NULL COMMENT '日志ID',
    `symbol`         varchar(16) NOT NULL COMMENT '模块标识',
    `user_name`      varchar(64) NOT NULL COMMENT '请求用户',
    `os`             varchar(16)   DEFAULT NULL COMMENT '操作系统',
    `browser`        varchar(255)  DEFAULT NULL COMMENT '浏览器',
    `add_ip`         varchar(16)   DEFAULT NULL COMMENT '请求IP',
    `url`            varchar(1024) DEFAULT NULL COMMENT '请求URL',
    `method`         varchar(128)  DEFAULT NULL COMMENT '请求方法',
    `request_method` varchar(16)   DEFAULT NULL COMMENT '请求方式',
    `request_params` text          DEFAULT NULL COMMENT '请求参数',
    `exe_time`       varchar(8)    DEFAULT NULL COMMENT '执行时间（ms）',
    `exe_status`     int(1)        DEFAULT 0 COMMENT '执行状态（0-初始化 1-执行成功 -1-执行异常）',
    `exception_clz`  varchar(128)  DEFAULT NULL COMMENT '异常类',
    `exception_msg`  text          DEFAULT NULL COMMENT '异常信息',
    `create_by`      varchar(64)   DEFAULT NULL COMMENT '创建者',
    `update_by`      varchar(64)   DEFAULT NULL COMMENT '更新者',
    `create_time`    datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`         varchar(512)  DEFAULT NULL COMMENT '备注信息',
    PRIMARY KEY (`log_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = '系统日志表';
