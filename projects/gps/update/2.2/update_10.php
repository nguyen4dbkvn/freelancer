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
	$q = "alter table gs_objects modify column icon varchar(512) after `name`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users modify column places_markers varchar(4) after `sms_gateway_identifier`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users modify column places_routes varchar(4) after `places_markers`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users modify column places_zones varchar(4) after `places_routes`";
	$r = mysqli_query($ms, $q);	
	
	$q = "UPDATE gs_user_events SET type = 'connno' WHERE `type`='noconn'";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_events SET type = 'param' WHERE `type`='sensor'";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_events SET type = 'hbrake' WHERE `type`='sbrake'";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_events SET type = 'haccel' WHERE `type`='saccel'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_object_sensors modify calibration varchar(4096) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_objects SET odometer_type = 'gps' WHERE `odometer_type`=''";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_objects SET engine_hours_type = 'acc' WHERE `engine_hours_type`=''";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects add column map_arrows varchar(512) not null COLLATE utf8_bin after `icon`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events add column routes varchar(4096) not null COLLATE utf8_bin after `checked_value`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events add column zones varchar(4096) not null COLLATE utf8_bin after `routes`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events add column zone_trigger varchar(5) not null COLLATE utf8_bin after `checked_value`";
	$r = mysqli_query($ms, $q);	
	
	$q = "UPDATE gs_user_events SET routes = checked_value WHERE `type`='route_in'";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_events SET routes = checked_value WHERE `type`='route_out'";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_events SET zones = checked_value WHERE `type`='zone_in'";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_events SET zones = checked_value WHERE `type`='zone_out'";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_events SET checked_value = '' WHERE `type`='route_in' OR `type`='route_out' OR `type`='zone_in' OR `type`='zone_out'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events_status drop column user_id";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column obj_history_clear varchar(5) not null COLLATE utf8_bin after `comment`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column manager_obj_num varchar(5) not null COLLATE utf8_bin after `manager_id`";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_users SET manager_obj_num = obj_num WHERE `privileges` LIKE '%manager%'";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_users SET obj_num = '' WHERE `privileges` LIKE '%manager%'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column account_expire varchar(5) not null COLLATE utf8_bin after `active`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column account_expire_dt date NOT NULL after `account_expire`";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_users SET account_expire = 'false'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column notify_account_expire varchar(5) not null COLLATE utf8_bin after `ip`";
	$r = mysqli_query($ms, $q);
	
	$q = "INSERT INTO `gs_templates` (`template_id`, `name`, `type`, `subject`, `message`) VALUES (7, 'expiring_account', 'email', 'Expiring account', 'Hello,\n\nYour account will expire soon. Please contact us for more details.')";
	$r = mysqli_query($ms, $q);
	
	// end modify database tables
	
	echo 'This part has successfully finished!';
?>