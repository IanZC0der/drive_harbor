SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for r_pan_file
-- ----------------------------
DROP TABLE IF EXISTS `r_pan_file`;

CREATE TABLE `r_pan_file`
(
    `file_id`                   bigint                                                 NOT NULL COMMENT 'file_id',
    `filename`                  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'file_name',
    `real_path`                 varchar(700) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'file_path',
    `file_size`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'file_size',
    `file_size_desc`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'file_size_desc',
    `file_suffix`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'file_suffix',
    `file_preview_content_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'file_preview_content_type',
    `identifier`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'unique_identifier',
    `create_user`               bigint                                                 NOT NULL COMMENT 'creator',
    `create_time`               datetime                                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'creation_time',
    PRIMARY KEY (`file_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT ='physical_files_information_table';

-- ----------------------------
-- Table structure for r_pan_share
-- ----------------------------
DROP TABLE IF EXISTS `r_pan_share`;
CREATE TABLE `r_pan_share`
(
    `share_id`       bigint(0) NOT NULL COMMENT 'share_id',
    `share_name`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'share_name',
    `share_type`     tinyint(1) NOT NULL DEFAULT 0 COMMENT 'share_type_0_meaning_password',
    `share_day_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'type, 0: permanent sharing, 1: valid for 7 days, 2: valid for 30days',
    `share_day`      tinyint(1) NOT NULL DEFAULT 0 COMMENT 'sharing duration, 0 means permanent',
    `share_end_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP (0) COMMENT 'share end time',
    `share_url`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'sharing url',
    `share_code`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'sharing password',
    `share_status`   tinyint(1) NOT NULL DEFAULT 0 COMMENT 'sharing status, 0: normal, 1: file deleted already',
    `create_user`    bigint(0) NOT NULL COMMENT 'sharer',
    `create_time`    datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP (0) COMMENT 'sharing time',
    PRIMARY KEY (`share_id`) USING BTREE,
    UNIQUE INDEX `uk_create_user_time` (`create_user`, `create_time`) USING BTREE COMMENT 'unique index (create_user, create_time)'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = 'User_Sharing_History_Table'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_pan_share_file
-- ----------------------------
DROP TABLE IF EXISTS `r_pan_share_file`;
CREATE TABLE `r_pan_share_file`
(
    `id`          bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'primary key',
    `share_id`    bigint(0) NOT NULL COMMENT 'share id',
    `file_id`     bigint(0) NOT NULL COMMENT 'file id',
    `create_user` bigint(0) NOT NULL COMMENT 'sharer',
    `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP (0) COMMENT 'share time',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_share_id_file_id` (`share_id`, `file_id`) USING BTREE COMMENT 'unique index (share_id, file_id)'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = 'files_shared_by_user'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_pan_user
-- ----------------------------
DROP TABLE IF EXISTS `r_pan_user`;
CREATE TABLE `r_pan_user`
(
    `user_id`     bigint(0) NOT NULL COMMENT 'user id',
    `username`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'user name',
    `password`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'password',
    `salt`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'salt',
    `question`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'security question',
    `answer`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'security answer',
    `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP (0) COMMENT 'account creation time',
    `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP (0) COMMENT 'update time',
    PRIMARY KEY (`user_id`) USING BTREE,
    UNIQUE INDEX `uk_username` (`username`) USING BTREE COMMENT 'unique index for user'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = 'user information table'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_pan_user_file
-- ----------------------------
DROP TABLE IF EXISTS `r_pan_user_file`;
CREATE TABLE `r_pan_user_file`
(
    `file_id`        bigint(20) NOT NULL COMMENT 'file id',
    `user_id`        bigint(20) NOT NULL COMMENT 'user id',
    `parent_id`      bigint(20) NOT NULL COMMENT 'parent folder id, root folder has id 0',
    `real_file_id`   bigint(20) NOT NULL DEFAULT '0' COMMENT 'real file id',
    `filename`       varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'file name',
    `folder_flag`    tinyint(1) NOT NULL DEFAULT '0' COMMENT 'if folder, 1 for yes',
    `file_size_desc` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '--' COMMENT 'file size desc',
    `file_type`      tinyint(1) NOT NULL DEFAULT '0' COMMENT 'file types, 1: normal file, 2: zipped file, 3: excel, 4: word, 5: pdf, 6: txt, 7: pics, 8: audios, 9: videos, 10: PPT, 11: code, 12: csv',
    `del_flag`       tinyint(1) NOT NULL DEFAULT '0' COMMENT 'if deleted, 1 for true',
    `create_user`    bigint(20) NOT NULL COMMENT 'creator',
    `create_time`    datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'create time',
    `update_user`    bigint(20) NOT NULL COMMENT 'updater',
    `update_time`    datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
    PRIMARY KEY (`file_id`) USING BTREE,
    KEY              `index_file_list` (`user_id`, `del_flag`, `parent_id`, `file_type`, `file_id`, `filename`, `folder_flag`,
        `file_size_desc`, `create_time`, `update_time`) USING BTREE COMMENT 'index for files'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC COMMENT ='user file information table';

-- ----------------------------
-- Table structure for r_pan_user_search_history
-- ----------------------------
DROP TABLE IF EXISTS `r_pan_user_search_history`;
CREATE TABLE `r_pan_user_search_history`
(
    `id`             bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'primary key',
    `user_id`        bigint(0) NOT NULL COMMENT 'user id',
    `search_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'search content',
    `create_time`    datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP (0) COMMENT 'create time',
    `update_time`    datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP (0) COMMENT 'update time',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_user_id_search_content_update_time` (`user_id`, `search_content`, `update_time`) USING BTREE COMMENT 'index1',
    UNIQUE INDEX `uk_user_id_search_content` (`user_id`, `search_content`) USING BTREE COMMENT 'index2'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = 'user search history table'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_pan_file_chunk
-- ----------------------------

DROP TABLE IF EXISTS `r_pan_file_chunk`;
CREATE TABLE `r_pan_file_chunk`
(
    `id`              bigint                           NOT NULL AUTO_INCREMENT COMMENT 'primary key',
    `identifier`      varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'file identifier',
    `real_path`       varchar(700) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'real path for the file chunk',
    `chunk_number`    int                              NOT NULL DEFAULT '0' COMMENT 'chunk number',
    `expiration_time` datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'expiration time',
    `create_user`     bigint                           NOT NULL COMMENT 'creator',
    `create_time`     datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_identifier_chunk_number_create_user` (`identifier`, `chunk_number`, `create_user`) USING BTREE COMMENT 'indexed by (`identifier`, `chunk_number`, `create_user`)'
) ENGINE = InnoDB
  AUTO_INCREMENT = 101
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='file chunks information table';

-- ----------------------------
-- Table structure for r_pan_error_log
-- ----------------------------

DROP TABLE IF EXISTS `r_pan_error_log`;
CREATE TABLE `r_pan_error_log`
(
    `id`          bigint                           NOT NULL AUTO_INCREMENT COMMENT 'primary key',
    `log_content` varchar(900) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'log contents',
    `log_status`  tinyint                                   DEFAULT '0' COMMENT 'log status, 0: unaddressed, 1: addressed',
    `create_user` bigint                           NOT NULL COMMENT 'creator',
    `create_time` datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `update_user` bigint                           NOT NULL COMMENT 'updater',
    `update_time` datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'update time',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='error log table';

SET
FOREIGN_KEY_CHECKS = 1;