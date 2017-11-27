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