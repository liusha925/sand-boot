DROP TABLE IF EXISTS `demo_user`;
CREATE TABLE `demo_user`
(
    `id`          bigint(20) NOT NULL COMMENT '主键ID',
    `name`        varchar(30)  DEFAULT NULL COMMENT '姓名',
    `age`         int(11)      DEFAULT NULL COMMENT '年龄',
    `email`       varchar(50)  DEFAULT NULL COMMENT '邮箱',
    `create_by`   varchar(64)  DEFAULT NULL COMMENT '创建者',
    `update_by`   varchar(64)  DEFAULT NULL COMMENT '更新者',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`      varchar(512) DEFAULT NULL COMMENT '备注信息',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT = '演示用户表';

-- ----------------------------
-- 演示用户表
-- ----------------------------
INSERT INTO `demo_user`
VALUES (1, 'Jone', 18, 'test1@126.com', 'admin', 'admin', sysdate(), sysdate(), '演示用户');
INSERT INTO `demo_user`
VALUES (2, 'Jack', 20, 'test2@126.com', 'admin', 'admin', sysdate(), sysdate(), '演示用户');
INSERT INTO `demo_user`
VALUES (3, 'Tom', 28, 'test3@126.com', 'admin', 'admin', sysdate(), sysdate(), '演示用户');
INSERT INTO `demo_user`
VALUES (4, 'Sandy', 21, 'test4@126.com', 'admin', 'admin', sysdate(), sysdate(), '演示用户');
INSERT INTO `demo_user`
VALUES (5, 'Billie', 24, 'test5@126.com', 'admin', 'admin', sysdate(), sysdate(), '演示用户');