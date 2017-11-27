<?
	session_start();
	set_time_limit(0);
	
	include ('../config.php');
	
	$ms = mysqli_connect($gsValues['DB_HOSTNAME'], $gsValues['DB_USERNAME'], $gsValues['DB_PASSWORD'], $gsValues['DB_NAME'], $gsValues['DB_PORT']);
	if (!$ms)
	{
	    echo "Error connecting to database.";
	    die;
	}
	
	$q = "SELECT * FROM `gs_trackers`";
	$r = mysqli_query($ms, $q);
	
	while($row = mysqli_fetch_array($r)) {
		$imei = $row['imei'];
		
		// rebuid table
		$q = "alter table gs_tracker_data_".$imei." drop column port_data";
		$r = mysqli_query($ms, $q);
		
		$q = "alter table gs_tracker_data_".$imei." add column params varchar(1000) not null COLLATE utf8_bin";
		$r = mysqli_query($ms, $q);
		
		// rebuid table
		$q = "alter table gs_tracker_data_".$imei." drop column signal_gsm";
		$r = mysqli_query($ms, $q);
		
		$q = "alter table gs_tracker_data_".$imei." drop column signal_gps";
		$r = mysqli_query($ms, $q);
	}
	
	// gs_users
	$q = "alter table gs_users add column manager_id int(11) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column map_icon varchar(5) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column info varchar(1000) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users modify info varchar(1000) not null after ip"; 
	$r = mysqli_query($ms, $q);
	// gs_users
	
	
	// gs_user_events_data
	$q = "alter table gs_user_events_data drop column port_data";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events_data add column params varchar(1000) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events_data add column obj_name varchar(50) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events_data modify obj_name varchar(50) not null after imei"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events_data drop column signal_gsm";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events_data drop column signal_gps";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events modify notify_email_address varchar(500) not null"; 
	$r = mysqli_query($ms, $q);
	// gs_user_events_data
	
	// gs_user_events
	$q = "alter table gs_user_events add column cmd_send varchar(5) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events add column cmd_type varchar(5) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events add column cmd_string varchar(100) not null";
	$r = mysqli_query($ms, $q);
	// gs_user_events
	
	// gs_trackers
	$q = "alter table gs_trackers drop column port_data";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add column params varchar(1000) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add manager_id int(11) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add dt_odometer datetime not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers modify params varchar(1000) not null after dt_server"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add signal_gsm int(1) not null after params";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add signal_gps int(1) not null after signal_gsm";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add odometer double not null after signal_gps";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add engine_hours int(11) not null after odometer";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add dt_engine_hours datetime not null after dt_odometer";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add accuracy varchar(50) not null after dt_engine_hours";
	$r = mysqli_query($ms, $q);
	
	// reset accuracy
	$q = "UPDATE `gs_trackers` SET `accuracy`=''";
	$r = mysqli_query($ms, $q);
	// gs_trackers
	
	
	// gs_user_trackers
	$q = "alter table gs_user_trackers add odometer double not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_trackers add accuracy varchar(50) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_trackers drop column odometer";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_trackers drop column accuracy";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_trackers modify fcr_summer double not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_trackers modify fcr_winter double not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_trackers modify device varchar(30) not null"; 
	$r = mysqli_query($ms, $q);
	// gs_user_trackers
	
	
	// gs_user_tracker_sensors
	$q = "alter table gs_user_tracker_sensors drop column port";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_tracker_sensors add column param varchar(20) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_tracker_sensors modify param varchar(20) not null after type"; 
	$r = mysqli_query($ms, $q);
	// gs_user_tracker_sensors
	
	
	// gs_user_tracker_drivers
	$q = "alter table gs_user_tracker_drivers add driver_ibutrfid varchar(30) not null";
	$r = mysqli_query($ms, $q);
	// gs_user_tracker_drivers
	
	// to 2.2.1
	
	// gs_trackers
	$q = "alter table gs_trackers add dt_tracker datetime not null after dt_server";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add lat double not null after dt_tracker";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add lng double not null after lat";
	$r = mysqli_query($ms, $q);
	// gs_trackers
	
	// gs_tracker_sensors
	$q = "ALTER TABLE gs_user_tracker_sensors RENAME TO gs_tracker_sensors";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_tracker_sensors drop column user_id";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_tracker_sensors change type result_type varchar(10) not null after param";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_tracker_sensors add column type varchar(10) not null after name";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE `gs_tracker_sensors` SET `type`='acc' WHERE param='acc'";
	$r = mysqli_query($ms, $q);
	// gs_tracker_sensors
	
	// fcr
	$q = "alter table gs_user_trackers drop column fcr_summer";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_trackers drop column fcr_winter";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_trackers drop column fcr_winter_period";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add column fcr varchar(30) not null after dt_engine_hours";
	$r = mysqli_query($ms, $q);
	// fcr
	
	// reset accuracy
	$q = "UPDATE `gs_trackers` SET `accuracy`=''";
	$r = mysqli_query($ms, $q);
	// reset accuracy
	
	// gs_trackers
	$q = "alter table gs_trackers add column altitude double not null after lng"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add column angle double not null after altitude"; 
	$r = mysqli_query($ms, $q);

	$q = "alter table gs_trackers add column speed double not null after angle"; 
	$r = mysqli_query($ms, $q);
	// gs_trackers
	
	// gs_tracker_sensors
	$q = "alter table gs_tracker_sensors add column formula varchar(20) not null after hv";
	$r = mysqli_query($ms, $q);
	// gs_tracker_sensors
	
	// gs_users
	$q = "alter table gs_users add column notify_object_expire varchar(5) not null after ip"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users modify privileges varchar(5000) not null"; 
	$r = mysqli_query($ms, $q);
	// gs_users
	
	// gs_user_trackers
	$q = "alter table gs_user_trackers add column vin varchar(50) not null after model"; 
	$r = mysqli_query($ms, $q);
	// gs_user_trackers
	
	// gs_trackers
	$q = "alter table gs_trackers add column odometer_type varchar(10) not null after signal_gps"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add column engine_hours_type varchar(10) not null after odometer_type"; 
	$r = mysqli_query($ms, $q);
	// gs_trackers
	
	// gs_user_events
	$q = "alter table gs_user_events modify notify_system varchar(40) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events modify imei varchar(5000) not null"; 
	$r = mysqli_query($ms, $q);
	// gs_user_events
	
	// gs_user_events_data
	$q = "alter table gs_user_events_data modify notify_system varchar(40) not null"; 
	$r = mysqli_query($ms, $q);
	// gs_user_events_data
	
	// gs_user_reports
	$q = "alter table gs_user_reports modify imei varchar(5000) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_reports add dt_schedule_d datetime not null after schedule_email_address";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_reports add dt_schedule_w datetime not null after dt_schedule_d";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_reports drop column schedule_active";
	$r = mysqli_query($ms, $q);
	// gs_user_reports
	
	// gs_tracker_sensors
	$q = "alter table gs_tracker_sensors add column calibration varchar(1000) not null after formula";
	$r = mysqli_query($ms, $q);
	// gs_tracker_sensors
	
	// gs_tracker_service
	$q = "alter table gs_tracker_service add column update_last varchar(5) not null after days_left_num";
	$r = mysqli_query($ms, $q);
	// gs_tracker_service
	
	// reset accuracy
	$q = "UPDATE `gs_trackers` SET `accuracy`=''";
	$r = mysqli_query($ms, $q);
	// reset accuracy
	
	// gs_user_reports
	$q = "alter table gs_user_reports CHANGE dt_schedule_m dt_schedule_w datetime not null"; 
	$r = mysqli_query($ms, $q);
	// gs_user_reports
	
	// gs_trackers
	$q = "alter table gs_trackers drop column dt_odometer";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers drop column dt_engine_hours";
	$r = mysqli_query($ms, $q);
	// gs_trackers
	
	// gs_tracker_cmd_exec
	$q = "alter table gs_tracker_cmd_exec add column re_hex varchar(200) not null after status";
	$r = mysqli_query($ms, $q);
	// gs_tracker_cmd_exec
	
		$q = "alter table gs_users add column sess_hash varchar(100) not null after password";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column sms_gateway_server varchar(5) not null after map_icon";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column sms_gateway varchar(5) not null after sms_gateway_server";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column sms_gateway_url varchar(200) not null after sms_gateway";
	$r = mysqli_query($ms, $q);
	
	
	$q = "alter table gs_tracker_cmd_exec add column gateway varchar(5) not null after name";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_cmd add column gateway varchar(5) not null after name";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events add column cmd_gateway varchar(5) not null after cmd_send";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_tracker_cmd_exec modify re_hex varchar(1000) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers drop column signal_gsm";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers drop column signal_gps";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add column loc_valid int(1) not null COLLATE utf8_bin after speed";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_trackers SET loc_valid = 1";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers drop column signal_gsm";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers drop column signal_gps";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add column loc_valid int(1) not null COLLATE utf8_bin after speed";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_trackers SET loc_valid = 1";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events add column week_days varchar(50) not null COLLATE utf8_bin after active";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_events SET week_days = 'true;true;true;true;true;true;true;'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column places_zones varchar(4) not null COLLATE utf8_bin after sms_gateway_url";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column places_markers varchar(4) not null COLLATE utf8_bin after places_zones";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add column chat_dt_server datetime not null COLLATE utf8_bin after accuracy";
	$r = mysqli_query($ms, $q);
	
	
	$q = "alter table gs_user_reports add column zones_addresses varchar(5) not null COLLATE utf8_bin after show_addresses";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add odometer_dt datetime not null after engine_hours";
	$r = mysqli_query($ms, $q);
	
	$q = 'UPDATE gs_trackers SET `odometer_dt` = dt_tracker';
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add engine_hours_dt datetime not null after odometer_dt";
	$r = mysqli_query($ms, $q);
	
	$q = 'UPDATE gs_trackers SET `engine_hours_dt` = dt_tracker';
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column places_routes varchar(4) not null COLLATE utf8_bin after places_zones";
	$r = mysqli_query($ms, $q);
	
	print 'This part has successfully finished!';
?>