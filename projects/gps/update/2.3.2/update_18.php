<?
	error_reporting(E_ALL ^ E_DEPRECATED);

	session_start();
	set_time_limit(0);
	
	include ('../config.php');
	
	$ms = mysqli_connect($gsValues['DB_HOSTNAME'], $gsValues['DB_USERNAME'], $gsValues['DB_PASSWORD'], $gsValues['DB_NAME'], $gsValues['DB_PORT']);
	if (!$ms)
	{
	    echo "Error connecting to database.";
	    die;
	}
        
	// modify database tables
	
	$q = "CREATE TABLE IF NOT EXISTS `gs_billing_plans` (
		`plan_id` int(11) NOT NULL AUTO_INCREMENT,
		`name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
		`active` varchar(5) COLLATE utf8_bin NOT NULL,
		`objects` int(11) COLLATE utf8_bin NOT NULL,
		`period` int(11) COLLATE utf8_bin NOT NULL,
		`period_type` varchar(10) COLLATE utf8_bin NOT NULL,
		`price` double NOT NULL,
		PRIMARY KEY (`plan_id`)
	      ) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
	$r = mysqli_query($ms, $q);
	
	$q = "CREATE TABLE IF NOT EXISTS `gs_user_billing_plans` (
		`plan_id` int(11) NOT NULL AUTO_INCREMENT,
		`user_id` int(11) NOT NULL,
		`dt_purchase` datetime NOT NULL,
		`name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
		`objects` int(11) COLLATE utf8_bin NOT NULL,
		`period` int(11) COLLATE utf8_bin NOT NULL,
		`period_type` varchar(10) COLLATE utf8_bin NOT NULL,
		`price` double NOT NULL,
		PRIMARY KEY (`plan_id`)
	      ) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
	$r = mysqli_query($ms, $q);
	
	$q = "CREATE TABLE IF NOT EXISTS `gs_user_cmd_schedule` (
		`cmd_id` int(11) NOT NULL AUTO_INCREMENT,
		`user_id` int(11) NOT NULL,
		`name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
		`active` varchar(5) COLLATE utf8_bin NOT NULL,
		`exact_time` varchar(5) COLLATE utf8_bin NOT NULL,
		`exact_time_dt` datetime NOT NULL,
		`day_time` varchar(512) COLLATE utf8_bin NOT NULL,
		`protocol` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
		`imei` text COLLATE utf8_bin NOT NULL,
		`gateway` varchar(5) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
		`type` varchar(5) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
		`cmd` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
		`dt_schedule_e` datetime NOT NULL,
		`dt_schedule_d` datetime NOT NULL,
		PRIMARY KEY (`cmd_id`)
	      ) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
	$r = mysqli_query($ms, $q);
	
	$q = "CREATE TABLE IF NOT EXISTS `gs_user_usage` (
		`usage_id` int(11) NOT NULL AUTO_INCREMENT,
		`user_id` int(11) NOT NULL,
		`dt_usage` date NOT NULL,
		`login` int(11) NOT NULL,
		`email` int(11) NOT NULL,
		`sms` int(11) NOT NULL,
		`api` int(11) NOT NULL,
		PRIMARY KEY (`usage_id`)
	      ) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
	$r = mysqli_query($ms, $q);
	
	
	$q = "ALTER TABLE gs_user_events_data DROP obj_name";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events add column notify_arrow varchar(5) not null after `notify_sms_number`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events add column notify_arrow_color varchar(20) not null after `notify_arrow`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events add column notify_ohc varchar(5) not null after `notify_arrow_color`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events add column notify_ohc_color varchar(7) not null after `notify_ohc`";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_events SET notify_arrow='false'";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_events SET notify_arrow_color='arrow_yellow'";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_events SET notify_ohc='false'";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_events SET notify_ohc_color='#FFFF00'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events_data add column notify_arrow varchar(5) not null after `notify_system`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events_data add column notify_arrow_color varchar(20) not null after `notify_arrow`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events_data add column notify_ohc varchar(5) not null after `notify_arrow_color`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events_data add column notify_ohc_color varchar(7) not null after `notify_ohc`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_cmd modify column protocol varchar(50) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column manager_obj_add varchar(10) not null after `manager_id`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column manager_obj_dt date not null after `manager_obj_num`";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_users SET manager_obj_add='limited'";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_users SET manager_obj_dt='".gmdate("Y-m-d")."'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column manager_billing varchar(5) not null after `manager_obj_dt`";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_users SET manager_billing='false'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column usage_email_daily varchar(8) not null after `places_zones`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column usage_sms_daily varchar(8) not null after `usage_email_daily`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column usage_api_daily varchar(8) not null after `usage_sms_daily`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column usage_email_daily_cnt int(11) not null after `usage_api_daily`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column usage_sms_daily_cnt int(11) not null after `usage_email_daily_cnt`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column usage_api_daily_cnt int(11) not null after `usage_sms_daily_cnt`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column dt_usage_d date not null after `usage_api_daily_cnt`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_reports add column show_coordinates varchar(5) not null after `format`";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_reports SET show_coordinates='true'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column map_is double not null after `map_sp`";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_users SET map_is='1'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects modify column icon varchar(256) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_objects SET icon='img/markers/objects/land-truck.svg' WHERE `icon` LIKE '%img/markers/objects%'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_markers modify column marker_icon varchar(256) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_markers SET marker_icon='img/markers/places/pin-1.svg' WHERE `marker_icon` LIKE '%img/markers/places%'";
	$r = mysqli_query($ms, $q);
	
	// end modify database tables
	
	// modify config.custom.php
	$gsValuesNew = array();
	
	$gsValuesNew['BILLING'] = "true";
	$gsValuesNew['BILLING_GATEWAY'] = "paypal";
	$gsValuesNew['BILLING_CURRENCY'] = "";
	$gsValuesNew['BILLING_PAYPAL_ACCOUNT'] = "";
	$gsValuesNew['BILLING_PAYPAL_CUSTOM'] = "";
	$gsValuesNew['BILLING_CUSTOM_URL'] = "";
	$gsValuesNew['USAGE_EMAIL_DAILY'] = "10000";
	$gsValuesNew['USAGE_SMS_DAILY'] = "10000";
	$gsValuesNew['USAGE_API_DAILY'] = "10000";
	$gsValuesNew['LOGO'] = "logo.png";
	
	$config = '';
	foreach ($gsValuesNew as $key => $value)
	{
		$config .= '$gsValues[\''.strtoupper($key).'\'] = "'.$value.'";'."\r\n";
	}
	
	$config = "<?\r\n".$config. "?>";
	
	file_put_contents('../config.custom.php', $config, FILE_APPEND | LOCK_EX);
	// end modify config.custom.php
	
	echo 'This part has successfully finished!';
?>