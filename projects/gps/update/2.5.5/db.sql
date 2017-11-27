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