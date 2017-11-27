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