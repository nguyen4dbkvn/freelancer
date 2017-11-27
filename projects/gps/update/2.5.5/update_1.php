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
        
        
	
	$q = "alter table gs_users drop column map_icon";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects add column map_icon varchar(5) not null COLLATE utf8_bin after name";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_objects SET map_icon = 'arrow'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_reports add column sensor_names text not null COLLATE utf8_bin after zone_ids";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_reports add column data_items text not null COLLATE utf8_bin after sensor_names";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects modify fcr varchar(512) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_object_drivers add column driver_img_file varchar(512) not null COLLATE utf8_bin after driver_desc";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects modify icon varchar(512) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events_data modify event_desc varchar(512) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events add column email_template_id int(11) not null after notify_sms_number";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events add column sms_template_id int(11) not null after email_template_id";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_objects add column trailer_id int(11) not null after driver_id";
	$r = mysqli_query($ms, $q);
	
	// icons
	$q = "SELECT * FROM `gs_objects`";
	$r = mysqli_query($ms, $q);
	
	while($row=mysqli_fetch_array($r))
	{
		if (substr($row['icon'], 0, 1) != 'i')
		{
			$q2 = "UPDATE gs_objects SET `icon`='img/markers/objects/".$row['icon']."' WHERE imei='".$row['imei']."'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));
		}
	}
	
	// fcr
	$q = "SELECT * FROM `gs_objects`";
	$r = mysqli_query($ms, $q);
	
	while($row=mysqli_fetch_array($r))
	{
		if (substr($row['fcr'], 0, 1) != '{')
		{
			// set default accuracy if not set in DB
			if ($row['fcr'] == '')
			{
				$fcr  = array();
				$fcr['measurement'] = '100km';
				$fcr['cost'] = 0;
				$fcr['summer'] = 0;
				$fcr['winter'] = 0;
				$fcr['winter_sm'] = 12;
				$fcr['winter_sd'] = 1;
				$fcr['winter_em'] = 3;
				$fcr['winter_ed'] = 1;
			}
			else
			{
				$temp = explode(",", $row['fcr']);
				
				$fcr = array();
				
				$fcr['measurement'] = '100km';
				$fcr['cost'] = 0;
				$fcr['summer'] = $temp[0];
				$fcr['winter'] = $temp[1];
				$fcr['winter_sm'] = $temp[2];
				$fcr['winter_sd'] = $temp[3];
				$fcr['winter_em'] = $temp[4];
				$fcr['winter_ed'] = $temp[5];
			}
			
			$q2 = "UPDATE gs_objects SET `fcr`='".json_encode($fcr)."' WHERE imei='".$row['imei']."'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));	
		}
	}
	// end fcr
	
	// reports
	$q = "SELECT * FROM `gs_user_reports`";
	$r = mysqli_query($ms, $q);
	
	while($row=mysqli_fetch_array($r))
	{
		if ($row['data_items'] == '')
		{
			// general
			$data_items = 'route_start,route_end,route_length,move_duration,stop_duration,top_speed,avg_speed,overspeed_count,fuel_consumption,engine_work,engine_idle,odometer,engine_hours,driver,trailer';
			$q2 = "UPDATE gs_user_reports SET `data_items`='".$data_items."' WHERE type='general'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));
			
			// general_merged
			$data_items = 'object,route_start,route_end,route_length,move_duration,stop_duration,top_speed,avg_speed,overspeed_count,fuel_consumption,engine_work,engine_idle,odometer,engine_hours,driver,trailer';
			$q2 = "UPDATE gs_user_reports SET `data_items`='".$data_items."' WHERE type='general_merged'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));
			
			// object_info
			$data_items = 'object,imei,transport_model,vin,plate_number,odometer,engine_hours,driver,trailer,gps_device,sim_card_number';
			$q2 = "UPDATE gs_user_reports SET `data_items`='".$data_items."' WHERE type='object_info'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));
			
			// drives_stops
			$data_items = 'status,start,end,duration,route_length,move_duration,stop_duration,top_speed,avg_speed,fuel_consumption,engine_work,engine_idle';
			$q2 = "UPDATE gs_user_reports SET `data_items`='".$data_items."' WHERE type='drives_stops'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));
			
			// travel_sheet
			$data_items = 'date,position_a,position_b,length,fuel_consumption';
			$q2 = "UPDATE gs_user_reports SET `data_items`='".$data_items."' WHERE type='travel_sheet'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));
			
			// overspeed
			$data_items = 'start,end,duration,top_speed,avg_speed,overspeed_position';
			$q2 = "UPDATE gs_user_reports SET `data_items`='".$data_items."' WHERE type='overspeed'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));
			
			// underspeed
			$data_items = 'start,end,duration,top_speed,avg_speed,underspeed_position';
			$q2 = "UPDATE gs_user_reports SET `data_items`='".$data_items."' WHERE type='underspeed'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));
			
			// zone_in_out
			$data_items = 'zone_in,zone_out,duration,zone_name,zone_position';
			$q2 = "UPDATE gs_user_reports SET `data_items`='".$data_items."' WHERE type='zone_in_out'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));
			
			// events
			$data_items = 'time,event,event_position,total';
			$q2 = "UPDATE gs_user_reports SET `data_items`='".$data_items."' WHERE type='events'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));
			
			// service
			$data_items = 'service,last_service,status';
			$q2 = "UPDATE gs_user_reports SET `data_items`='".$data_items."' WHERE type='service'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));
			
			// fuelfillings
			$data_items = 'time,position,before,after,filled,sensor,driver';
			$q2 = "UPDATE gs_user_reports SET `data_items`='".$data_items."' WHERE type='fuelfillings'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));
			
			// fuelthefts
			$data_items = 'time,position,before,after,stolen,sensor,driver';
			$q2 = "UPDATE gs_user_reports SET `data_items`='".$data_items."' WHERE type='fuelthefts'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));
		}
	}
	// end reports
	
	echo 'This part has successfully finished!';
?>