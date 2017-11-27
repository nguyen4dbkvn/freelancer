<?
	error_reporting(E_ALL ^ E_DEPRECATED);

	session_start();
	set_time_limit(0);
	
	include ('../config.php');
	include ('../config.custom.php');
	
	$ms = mysqli_connect($gsValues['DB_HOSTNAME'], $gsValues['DB_USERNAME'], $gsValues['DB_PASSWORD'], $gsValues['DB_NAME'], $gsValues['DB_PORT']);
	if (!$ms)
	{
	    echo "Error connecting to database.";
	    die;
	}
        
	// modify database tables
	
	$q = "CREATE TABLE IF NOT EXISTS `gs_system` (
		`id` int(11) NOT NULL AUTO_INCREMENT,
		`key` varchar(32) COLLATE utf8_bin NOT NULL,
		`value` varchar(64) COLLATE utf8_bin NOT NULL,
		PRIMARY KEY (`id`)
	      ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
	$r = mysqli_query($ms, $q);
	
	$q = "CREATE TABLE IF NOT EXISTS `gs_object_custom_fields` (
		`field_id` int(11) NOT NULL AUTO_INCREMENT,
		`imei` varchar(20) COLLATE utf8_bin NOT NULL,
		`name` varchar(50) COLLATE utf8_bin NOT NULL,
		`value` varchar(100) COLLATE utf8_bin NOT NULL,
		`data_list` varchar(5) COLLATE utf8_bin NOT NULL,
		`popup` varchar(5) COLLATE utf8_bin NOT NULL,
		PRIMARY KEY (`field_id`),
		KEY `imei` (`imei`)
	      ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
	$r = mysqli_query($ms, $q);
	
	$q = "CREATE TABLE IF NOT EXISTS `gs_user_failed_logins` (
		`fail_id` int(11) NOT NULL AUTO_INCREMENT,
		`ip` varchar(100) COLLATE utf8_bin NOT NULL,
		`dt_login` datetime NOT NULL,
		PRIMARY KEY (`fail_id`),
		KEY `ip` (`ip`)
	      ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
	$r = mysqli_query($ms, $q);
	
	$q = "CREATE TABLE IF NOT EXISTS `gs_user_account_recover` (
		`recover_id` int(11) NOT NULL AUTO_INCREMENT,
		`token` varchar(100) COLLATE utf8_bin NOT NULL,
		`email` varchar(100) COLLATE utf8_bin NOT NULL,
		`dt_recover` datetime NOT NULL,
		PRIMARY KEY (`recover_id`),
		KEY `token` (`token`)
	      ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
	$r = mysqli_query($ms, $q);
	$q = "CREATE TABLE IF NOT EXISTS `gs_dtc_data` (
		`dtc_id` int(11) NOT NULL AUTO_INCREMENT,
		`dt_server` datetime NOT NULL,
		`dt_tracker` datetime NOT NULL,
		`imei` varchar(20) COLLATE utf8_bin NOT NULL,
		`code` varchar(20) COLLATE utf8_bin NOT NULL,
		`lat` double DEFAULT NULL,
		`lng` double DEFAULT NULL,
		`address` varchar(256) COLLATE utf8_bin NOT NULL,
		PRIMARY KEY (`dtc_id`),
		KEY `imei` (`imei`)
	      ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
	$r = mysqli_query($ms, $q);
	
	$q = "INSERT INTO `gs_templates` (`name`, `language`, `subject`, `message`) VALUES";
	$q .= " ('account_recover_url', 'english', 'Lost login recovery to %SERVER_NAME%', 'Hello,\n\nA request has been made to recover account on %SERVER_NAME%.\n\nUse the link to complete the process: %URL_RECOVER%')";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column groups_collapsed varchar(100) not null after `map_rhc`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_object_sensors add column data_list varchar(5) not null after `param`";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_object_sensors SET `data_list`='true' WHERE `type`!='da' AND `type`!='pa' AND `type`!='ta' AND `type`!='odo' AND `type`!='engh' AND `type`!='fuelcons'";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_object_sensors SET `data_list`='false' WHERE `type`='da' OR `type`='pa' OR `type`='ta' OR `type`='odo' OR `type`='engh' OR `type`='fuelcons'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_object_services add column data_list varchar(5) not null after `name`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_object_services add column popup varchar(5) not null after `data_list`";
	$r = mysqli_query($ms, $q);
	
	$q = "ALTER TABLE `gs_user_billing_plans` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_usage` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_cmd` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_cmd_schedule` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_templates` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_reports` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_events` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_events_data` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_events_data` ADD INDEX imei (`imei`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_events_status` ADD INDEX event_id (`event_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_events_status` ADD INDEX imei (`imei`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_objects` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_objects` ADD INDEX imei (`imei`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_object_groups` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_object_drivers` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_object_passengers` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_object_trailers` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_places_groups` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_markers` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_routes` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_user_zones` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_object_cmd_exec` ADD INDEX user_id (`user_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_object_cmd_exec` ADD INDEX imei (`imei`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_object_img` ADD INDEX imei (`imei`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_object_chat` ADD INDEX imei (`imei`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_object_sensors` ADD INDEX imei (`imei`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_object_services` ADD INDEX imei (`imei`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_rfid_swipe_data` ADD INDEX imei (`imei`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_rilogbook_data` ADD INDEX imei (`imei`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_objects` ADD INDEX manager_id (`manager_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_users` ADD INDEX manager_id (`manager_id`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_geocoder_cache` ADD INDEX lat (`lat`);";
	$r = mysqli_query($ms, $q);
	$q = "ALTER TABLE `gs_geocoder_cache` ADD INDEX lng (`lng`);";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects add column object_expire varchar(5) not null after `active`";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_objects SET `object_expire`='true'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects change active_dt object_expire_dt date not null after object_expire";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users change obj_num obj_limit_num int(11) not null after obj_add";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users change obj_dt obj_days_dt date not null after obj_limit_num";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column obj_limit varchar(5) not null after `obj_add`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column obj_days varchar(5) not null after `obj_limit_num`";
	$r = mysqli_query($ms, $q);
	
	$q = "SELECT * FROM `gs_users` WHERE `privileges` NOT LIKE '%subuser%'";
	$r = mysqli_query($ms, $q);
	
	while ($row = mysqli_fetch_array($r))
	{
		if ($row['obj_add'] == 'false')
		{
			$obj_add = 'false';
			$obj_limit = 'false';
			$obj_days = 'false';
		}
		else if ($row['obj_add'] == 'trial')
		{
			$obj_add = 'trial';
			$obj_limit = 'false';
			$obj_days = 'false';
		}
		else if ($row['obj_add'] == 'limited')
		{
			$obj_add = 'true';
			$obj_limit = 'true';
			$obj_days = 'true';
		}
		else if ($row['obj_add'] == 'unlimited')
		{
			$obj_add = 'true';
			$obj_limit = 'false';
			$obj_days = 'true';
		}
		
		$q2 = "UPDATE gs_users SET `obj_add`='".$obj_add."', `obj_limit`='".$obj_limit."', `obj_days`='".$obj_days."' WHERE `id`='".$row['id']."'";
		$r2 = mysqli_query($ms, $q2);
	}
	
	$q = "alter table gs_users modify obj_add varchar(5) not null after obj_edit"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users modify obj_edit varchar(5) not null after obj_days_dt";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users modify obj_history_clear varchar(5) not null after obj_edit";
	$r = mysqli_query($ms, $q);
	
	$q = "SELECT * FROM `gs_users` WHERE `privileges` LIKE '%manager%'";
	$r = mysqli_query($ms, $q);
	
	while ($row = mysqli_fetch_array($r))
	{
		$manager_obj_num = $row['manager_obj_num'];
		$manager_obj_dt = $row['manager_obj_dt'];
		
		if ($row['manager_obj_add'] == 'false')
		{
			$obj_add = 'false';
			$obj_limit = 'false';
			$obj_limit_num = 0;
			$obj_days = 'false';
			$obj_days_dt = '';
		}
		else if ($row['manager_obj_add'] == 'limited')
		{
			$obj_add = 'true';
			$obj_limit = 'true';
			$obj_limit_num = $manager_obj_num;
			$obj_days = 'true';
			$obj_days_dt = $manager_obj_dt;
		}
		else if ($row['manager_obj_add'] == 'unlimited')
		{
			$obj_add = 'true';
			$obj_limit = 'false';
			$obj_limit_num = 0;
			$obj_days = 'true';
			$obj_days_dt = $manager_obj_dt;
		}
		
		$q2 = "UPDATE gs_users SET `obj_add`='".$obj_add."', `obj_limit`='".$obj_limit."', `obj_limit_num`='".$obj_limit_num."', `obj_days`='".$obj_days."', `obj_days_dt`='".$obj_days_dt."' WHERE `id`='".$row['id']."'";
		$r2 = mysqli_query($ms, $q2);
	}
	
	$q = "ALTER TABLE gs_users DROP manager_obj_add";
	$r = mysqli_query($ms, $q);
	
	$q = "ALTER TABLE gs_users DROP manager_obj_num";
	$r = mysqli_query($ms, $q);
	
	$q = "ALTER TABLE gs_users DROP manager_obj_dt";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users modify dt_usage_d date not null after usage_api_daily_cnt"; 
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_reports SET `type`='logic_sensors' WHERE `type`='logic_sensor_info'";
	$r = mysqli_query($ms, $q);
	
	$q = "SELECT * FROM `gs_user_events` WHERE `type`='param'";
	$r = mysqli_query($ms, $q);
	
	while ($row = mysqli_fetch_array($r))
	{
		$checked_value = explode("|", $row['checked_value']);
		
		$items = array();
		
		if (count($checked_value) == 3)
		{
			$items[] = array('src' => $checked_value[0], 'cn' => $checked_value[1], 'val' => $checked_value[2]);
		}
		
		$q2 = "UPDATE gs_user_events SET `checked_value`='".json_encode($items)."' WHERE `event_id`='".$row['event_id']."'";
		$r2 = mysqli_query($ms, $q2);
	}
	
	$q = "SELECT * FROM `gs_user_events` WHERE `type`='sensor'";
	$r = mysqli_query($ms, $q);
	
	while ($row = mysqli_fetch_array($r))
	{
		$checked_value = explode("|", $row['checked_value']);
		
		$items = array();
		
		if (count($checked_value) == 3)
		{
			$items[] = array('src' => $checked_value[0], 'cn' => $checked_value[1], 'val' => $checked_value[2]);
		}
		
		$q2 = "UPDATE gs_user_events SET `checked_value`='".json_encode($items)."' WHERE `event_id`='".$row['event_id']."'";
		$r2 = mysqli_query($ms, $q2);
	}
	
	$q = "SELECT * FROM `gs_users` WHERE `privileges` NOT LIKE '%subuser%'";
	$r = mysqli_query($ms, $q);
	
	while ($row = mysqli_fetch_array($r))
	{
		$api_key = genUserAPIKey($row['email']);
		
		$q2 = "UPDATE gs_users SET `api_key`='".$api_key."' WHERE `id`='".$row['id']."'";
		$r2 = mysqli_query($ms, $q2);
	}
	
	// modify config.custom.php
	$gsValuesNew = array();
	
	$gsValuesNew['API'] = "false";
	$gsValuesNew['SERVER_API_KEY'] = "";
	$gsValuesNew['DTC'] = "true";
	$gsValuesNew['DB_BACKUP_TIME'] = "00:00";
	
	if ($gsValues['OBJ_ADD'] == 'false')
	{
		$gsValuesNew['OBJ_ADD'] = 'false';
		
		$gsValuesNew['OBJ_LIMIT'] = 'false';
		$gsValuesNew['OBJ_LIMIT_NUM'] = '';
		$gsValuesNew['OBJ_DAYS'] = 'false';
		$gsValuesNew['OBJ_DAYS_NUM'] = '';
	}
	else if ($gsValues['OBJ_ADD'] == 'trial')
	{
		$gsValuesNew['OBJ_ADD'] = 'trial';
		
		$gsValuesNew['OBJ_LIMIT'] = 'false';
		$gsValuesNew['OBJ_LIMIT_NUM'] = '';
		$gsValuesNew['OBJ_DAYS'] = 'false';
		$gsValuesNew['OBJ_DAYS_NUM'] = '';
	}
	else if ($gsValues['OBJ_ADD'] == 'limited')
	{
		$gsValuesNew['OBJ_ADD'] = 'true';
		
		$gsValuesNew['OBJ_LIMIT'] = 'true';
		$gsValuesNew['OBJ_LIMIT_NUM'] = $gsValues['OBJ_NUM'];
		$gsValuesNew['OBJ_DAYS'] = 'true';
		$gsValuesNew['OBJ_DAYS_NUM'] = $gsValues['OBJ_DT'];
	}
	else if ($gsValues['OBJ_ADD'] == 'unlimited')
	{
		$gsValuesNew['OBJ_ADD'] = 'true';
		
		$gsValuesNew['OBJ_LIMIT'] = 'false';
		$gsValuesNew['OBJ_LIMIT_NUM'] = '';
		$gsValuesNew['OBJ_DAYS'] = 'true';
		$gsValuesNew['OBJ_DAYS_NUM'] = $gsValues['OBJ_DT'];
	}
	
	$gsValuesNew['OBJ_DAYS_TRIAL'] = @$gsValues['OBJ_TRIAL_PERIOD'];
	
	$config = '';
	foreach ($gsValuesNew as $key => $value)
	{
		$config .= '$gsValues[\''.strtoupper($key).'\'] = "'.$value.'";'."\r\n";
	}
	
	$config = "<?\r\n".$config. "?>";
	
	file_put_contents('../config.custom.php', $config, FILE_APPEND | LOCK_EX);
	// end modify config.custom.php
	
	echo 'This part has successfully finished!';
	
	function genUserAPIKey($email)
	{
		global $ms;
		
		while(true)
		{
			$api_key = strtoupper(md5(rand().$email.gmdate("Y-m-d H:i:s").rand()));
			
			$q = "SELECT * FROM `gs_users` WHERE `api_key`='".$api_key."'";
			$r = mysqli_query($ms, $q);
			$num = mysqli_num_rows($r);
			
			if ($num == 0)
			{
				return $api_key;
			}	
		}
	}
?>