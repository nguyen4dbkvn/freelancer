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
	
	
	
	$q = 'UPDATE gs_tracker_sensors SET `formula` = ""';
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_tracker_sensors modify formula varchar(50) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events modify notify_sms_number varchar(500) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_tracker_sensors add column popup varchar(5) not null COLLATE utf8_bin after param";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers drop column odometer_dt";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers drop column engine_hours_dt";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add column name varchar(50) not null COLLATE utf8_bin after params";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add column icon varchar(30) not null COLLATE utf8_bin after name";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add column device varchar(30) not null COLLATE utf8_bin after icon";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add column sim_number varchar(50) not null COLLATE utf8_bin after device";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add column model varchar(50) not null COLLATE utf8_bin after sim_number";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add column vin varchar(50) not null COLLATE utf8_bin after model";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add column plate_number varchar(50) not null COLLATE utf8_bin after vin";
	$r = mysqli_query($ms, $q);
	
	$q2 = "SELECT * FROM `gs_user_trackers`";
	$r2 = mysqli_query($ms, $q2);
	
	if ($r2)
	{
		while($row2 = mysqli_fetch_array($r2))
		{
			$imei= $row2['imei'];
			$name = $row2['name'];
			$icon = $row2['icon'];
			$device = $row2['device'];
			$sim_number = $row2['sim_number'];
			$model = $row2['model'];
			$vin = $row2['vin'];
			$plate_number = $row2['plate_number'];
			
			$q = "UPDATE gs_trackers SET name = '".$name."' WHERE `imei`='".$imei."'";
			$r = mysqli_query($ms, $q);
			
			$q = "UPDATE gs_trackers SET icon = '".$icon."' WHERE `imei`='".$imei."'";
			$r = mysqli_query($ms, $q);
			
			$q = "UPDATE gs_trackers SET device = '".$device."' WHERE `imei`='".$imei."'";
			$r = mysqli_query($ms, $q);
			
			$q = "UPDATE gs_trackers SET sim_number = '".$sim_number."' WHERE `imei`='".$imei."'";
			$r = mysqli_query($ms, $q);
			
			$q = "UPDATE gs_trackers SET model = '".$model."' WHERE `imei`='".$imei."'";
			$r = mysqli_query($ms, $q);
			
			$q = "UPDATE gs_trackers SET vin = '".$vin."' WHERE `imei`='".$imei."'";
			$r = mysqli_query($ms, $q);
			
			$q = "UPDATE gs_trackers SET plate_number = '".$plate_number."' WHERE `imei`='".$imei."'";
			$r = mysqli_query($ms, $q);
		}
	}
	
	$q = "alter table gs_user_trackers drop column name";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_trackers drop column icon";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_trackers drop column device";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_trackers drop column sim_number";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_trackers drop column model";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_trackers drop column vin";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_trackers drop column plate_number";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_users SET api_key = ''";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers rename to gs_objects";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_tracker_cmd_exec rename to gs_object_cmd_exec";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_tracker_img rename to gs_object_img";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_tracker_chat rename to gs_object_chat";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_trackers rename to gs_user_objects";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_tracker_drivers rename to gs_user_object_drivers";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_tracker_groups rename to gs_user_object_groups";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_tracker_sensors rename to gs_object_sensors";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_tracker_service rename to gs_object_services";
	$r = mysqli_query($ms, $q);
	
	$q2 = "SELECT * FROM `gs_objects`";
	$r2 = mysqli_query($ms, $q2);
	
	while($row2 = mysqli_fetch_array($r2))
	{
		$q = "alter table gs_tracker_data_".$row2['imei']." rename to gs_object_data_".$row2['imei'];
		$r = mysqli_query($ms, $q);
	}
	
	$q = "alter table gs_users drop column map_ts";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users drop column map_tc";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects add column tail_color varchar(7) not null COLLATE utf8_bin after icon";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects add column tail_points int(2) not null COLLATE utf8_bin after tail_color";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_objects SET tail_color = '#00FF44'";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_objects SET tail_points = 7";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects add column ip varchar(50) not null COLLATE utf8_bin after imei";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects add column port varchar(10) not null COLLATE utf8_bin after ip";
	$r = mysqli_query($ms, $q);
	
	$q = "ALTER TABLE gs_user_objects ADD object_id int NOT NULL AUTO_INCREMENT primary key FIRST";
	$r = mysqli_query($ms, $q);
	
	$q = "ALTER TABLE gs_user_events_status ADD status_id int NOT NULL AUTO_INCREMENT primary key FIRST";
	$r = mysqli_query($ms, $q);
	
	$q = "ALTER TABLE gs_user_object_drivers CHANGE `driver_ibutrfid` `driver_rfid` VARCHAR(30) NOT NULL";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_object_drivers add column driver_ibutton VARCHAR(30) not null COLLATE utf8_bin after driver_rfid";
	$r = mysqli_query($ms, $q);
	
	echo 'This part has successfully finished!';
?>