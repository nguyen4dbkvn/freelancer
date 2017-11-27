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
        
        $gsValues['ACCURACY'] =   array('stops' => 'gps',
                                        'min_moving_speed' => 6,
                                        'min_diff_points' => 0.0005,
                                        'use_gpslev' => false,
                                        'min_gpslev' => 5,
                                        'use_hdop' => false,
                                        'max_hdop' => 3,
                                        'min_ff' => 10,
                                        'min_ft' => 10); // default accuracy settings
        
	$q = "alter table gs_users modify sms_gateway_url varchar(2048) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users modify privileges text not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects modify accuracy varchar(1024) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects modify accuracy varchar(1024) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_object_cmd_exec modify re_hex varchar(1024) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users modify info varchar(1024) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users modify comment varchar(2048) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events modify imei text not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events modify checked_value varchar(1024) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events modify notify_email_address varchar(1024) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events modify notify_sms_number varchar(1024) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_reports modify imei text not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_reports modify zone_ids text not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_reports modify schedule_email_address varchar(1024) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_object_chat modify msg varchar(2048) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_object_drivers modify driver_desc varchar(1024) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_object_groups modify group_desc varchar(1024) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_object_sensors modify calibration varchar(1024) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_markers modify marker_desc varchar(1024) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_zones modify zone_vertices varchar(2048) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_routes modify route_points varchar(5120) not null";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects modify params varchar(2048) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events_data modify params varchar(2048) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_object_img modify params varchar(2048) not null"; 
	$r = mysqli_query($ms, $q);
	
	// accuracy
	$q = "SELECT * FROM `gs_objects`";
	$r = mysqli_query($ms, $q);
	
	while($row=mysqli_fetch_array($r))
	{
		if (substr($row['accuracy'], 0, 1) != '{')
		{
			// set default accuracy if not set in DB
			if ($row['accuracy'] == '')
			{
				$accuracy = $gsValues['ACCURACY'];
			}
			else
			{
				$temp = explode(",", $row['accuracy']);
				
				$accuracy = array();
				
				$accuracy['stops'] = $temp[0];
				$accuracy['min_moving_speed'] = $temp[1];
				$accuracy['min_diff_points'] = $temp[2];
				$accuracy['use_gpslev'] = $temp[3];
				$accuracy['min_gpslev'] = $temp[4];
				$accuracy['use_hdop'] = $temp[5];
				$accuracy['max_hdop'] = $temp[6];
				$accuracy['min_ff'] = $temp[7];
				$accuracy['min_ft'] = $temp[8];
			}
			
			$q2 = "UPDATE gs_objects SET `accuracy`='".json_encode($accuracy)."' WHERE imei='".$row['imei']."'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));	
		}
	}
	// end accuracy
	
	// privileges
	$q = "SELECT * FROM `gs_users`";
	$r = mysqli_query($ms, $q);
	
	while($row=mysqli_fetch_array($r))
	{
		if (substr($row['privileges'], 0, 1) != '{')
		{
			$privileges_arr = array();
			
			$privileges = explode("|", $row['privileges']);
			
			if ($privileges[0] == 'subuser')
			{
				$privileges_arr["type"] = $privileges[0];
				
				//$privileges[1] = explode(",", $privileges[1]);
				$privileges_arr["imei"] = $privileges[1];
				
				//$privileges[3] = explode(",", $privileges[3]);
				$privileges_arr["marker"] = $privileges[3];
				
				if (!isset($privileges[9])) {$privileges[9] = '';}
				//$privileges[9] = explode(",", $privileges[9]);
				$privileges_arr["route"] = $privileges[9];
				
				//$privileges[2] = explode(",", $privileges[2]);
				$privileges_arr["zone"] = $privileges[2];
				
				if (!isset($privileges[4])) {$privileges[4] = '';}
				$privileges_arr["history"] = $privileges[4];
				
				if (!isset($privileges[5])) {$privileges[5] = '';}
				$privileges_arr["reports"] = $privileges[5];
				
				if (!isset($privileges[6])) {$privileges[6] = '';}
				$privileges_arr["object_control"] = $privileges[6];
				
				if (!isset($privileges[7])) {$privileges[7] = '';}
				$privileges_arr["image_gallery"] = $privileges[7];
				
				if (!isset($privileges[8])) {$privileges[8] = '';}
				$privileges_arr["chat"] = $privileges[8];
			}
			else
			{
				$privileges_arr["type"] = $row["privileges"];
			}
			
			$q2 = "UPDATE gs_users SET `privileges`='".json_encode($privileges_arr)."' WHERE id='".$row['id']."'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));
		}
	}
	// end privileges
	
	echo 'This part has successfully finished!';
?>