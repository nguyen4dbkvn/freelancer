--
-- Table structure for table `gs_object_data`
--

CREATE TABLE IF NOT EXISTS `gs_object_data` (
  `imei` varchar(20) COLLATE utf8_bin NOT NULL,
  `dt_server` datetime NOT NULL,
  `dt_tracker` datetime NOT NULL,
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  `altitude` double DEFAULT NULL,
  `angle` double DEFAULT NULL,
  `speed` double DEFAULT NULL,
  `params` varchar(2048) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  KEY `imei` (`imei`),
  KEY `dt_tracker` (`dt_tracker`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Table structure for table `gs_user_routes`
--

CREATE TABLE IF NOT EXISTS `gs_user_routes` (
  `route_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `route_name` varchar(50) COLLATE utf8_bin NOT NULL,
  `route_color` varchar(7) COLLATE utf8_bin NOT NULL,
  `route_visible` varchar(5) COLLATE utf8_bin NOT NULL,
  `route_name_visible` varchar(5) COLLATE utf8_bin NOT NULL,
  `route_deviation` varchar(5) COLLATE utf8_bin NOT NULL,
  `route_points` varchar(5000) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`route_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `gs_tracker_chat`
--

CREATE TABLE IF NOT EXISTS `gs_tracker_chat` (
  `msg_id` int(11) NOT NULL AUTO_INCREMENT,
  `dt_server` datetime NOT NULL,
  `imei` varchar(20) COLLATE utf8_bin NOT NULL,
  `side` varchar(1) COLLATE utf8_bin NOT NULL,
  `msg` varchar(1000) COLLATE utf8_bin NOT NULL,
  `status` int(1) NOT NULL,
  PRIMARY KEY (`msg_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

-- --------------------------------------------------------

--
-- Table structure for table `gs_user_reports`
--

CREATE TABLE IF NOT EXISTS `gs_user_reports` (
  `report_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `format` varchar(4) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `show_addresses` varchar(5) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `stop_duration` int(11) NOT NULL,
  `speed_limit` int(11) NOT NULL,
  `imei` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `zone_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `schedule_active` varchar(5) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `schedule_period` varchar(7) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `schedule_email_address` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `gs_geocoder_cache`
--

CREATE TABLE IF NOT EXISTS `gs_geocoder_cache` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `address` varchar(100) COLLATE utf8_bin NOT NULL,
  `count` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

-- --------------------------------------------------------

--
-- Table structure for table `gs_rfid_swipe_data`
--

CREATE TABLE IF NOT EXISTS `gs_rfid_swipe_data` (
  `swipe_id` int(11) NOT NULL AUTO_INCREMENT,
  `dt_server` datetime NOT NULL,
  `dt_swipe` datetime NOT NULL,
  `imei` varchar(20) COLLATE utf8_bin NOT NULL,
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  `rfid` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`swipe_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

-- --------------------------------------------------------

--
-- Table structure for table `gs_tracker_service`
--

CREATE TABLE IF NOT EXISTS `gs_tracker_service` (
  `service_id` int(11) NOT NULL AUTO_INCREMENT,
  `imei` varchar(20) COLLATE utf8_bin NOT NULL,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `odo` varchar(5) COLLATE utf8_bin NOT NULL,
  `odo_interval` double NOT NULL,
  `odo_last` double NOT NULL,
  `engh` varchar(5) COLLATE utf8_bin NOT NULL,
  `engh_interval` int(11) NOT NULL,
  `engh_last` int(11) NOT NULL,
  `days` varchar(5) COLLATE utf8_bin NOT NULL,
  `days_interval` int(11) NOT NULL,
  `days_last` date NOT NULL,
  `odo_left` varchar(5) NOT NULL,
  `odo_left_num` int(11) NOT NULL,
  `engh_left` varchar(5) NOT NULL,
  `engh_left_num` int(11) NOT NULL,
  `days_left` varchar(5) NOT NULL,
  `days_left_num` int(11) NOT NULL,
  `notify_service_expire` varchar(5) NOT NULL,
  PRIMARY KEY (`service_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

-- --------------------------------------------------------

--
-- Table structure for table `gs_user_img_data`
--

CREATE TABLE IF NOT EXISTS `gs_tracker_img` (
  `img_id` int(11) NOT NULL AUTO_INCREMENT,
  `img_file` varchar(50) COLLATE utf8_bin NOT NULL,
  `imei` varchar(20) COLLATE utf8_bin NOT NULL,
  `dt_server` datetime NOT NULL,
  `dt_tracker` datetime NOT NULL,
  `lat` double NOT NULL,
  `lng` double NOT NULL,
  `altitude` double NOT NULL,
  `angle` double NOT NULL,
  `speed` double NOT NULL,
  `params` varchar(1000) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`img_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `gs_user_templates`
--

CREATE TABLE IF NOT EXISTS `gs_user_templates` (
  `template_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `desc` varchar(1024) COLLATE utf8_bin NOT NULL,
  `message` varchar(4096) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `gs_user_object_trailers`
--

CREATE TABLE IF NOT EXISTS `gs_user_object_trailers` (
  `trailer_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `trailer_name` varchar(100) COLLATE utf8_bin NOT NULL,
  `trailer_model` varchar(50) COLLATE utf8_bin NOT NULL,
  `trailer_vin` varchar(50) COLLATE utf8_bin NOT NULL,
  `trailer_plate_number` varchar(50) COLLATE utf8_bin NOT NULL,
  `trailer_desc` varchar(1024) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`trailer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------