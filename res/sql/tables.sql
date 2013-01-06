SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `permissions`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `permissions` (
  `id` int(11) NOT NULL,
  `permission` varchar(35) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for `players`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `players` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `playername` varchar(35) NOT NULL,
  `pass` varchar(32) NOT NULL DEFAULT '',
  `lastplayed` bigint(20) NOT NULL DEFAULT '0',
  `location` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `player_accounts`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `player_accounts` (
  `id` int(11) NOT NULL,
  `morgul_coins` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for `qued_msg`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `qued_msg` (
  `id` int(11) NOT NULL,
  `msg` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for `player_stats`
-- ----------------------------
CREATE TABLE IF NOT EXISTS `player_stats` (
  `id` int(11) NOT NULL,
  `stat` varchar(255) NOT NULL,
  `xp` int(128) NOT NULL,
  `lvl` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
