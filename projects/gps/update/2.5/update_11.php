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
        
        
	
	$q = "CREATE TABLE IF NOT EXISTS `gs_user_object_passengers` (
		`passenger_id` int(11) NOT NULL AUTO_INCREMENT,
		`user_id` int(11) NOT NULL,
		`passenger_name` varchar(100) COLLATE utf8_bin NOT NULL,
		`passenger_assign_id` varchar(30) COLLATE utf8_bin NOT NULL,
		`passenger_idn` varchar(100) COLLATE utf8_bin NOT NULL,
		`passenger_address` varchar(200) COLLATE utf8_bin NOT NULL,
		`passenger_phone` varchar(50) COLLATE utf8_bin NOT NULL,
		`passenger_email` varchar(100) COLLATE utf8_bin NOT NULL,
		`passenger_desc` varchar(1024) COLLATE utf8_bin NOT NULL,
		PRIMARY KEY (`passenger_id`)
	      ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
	$r = mysqli_query($ms, $q);
	
	$q = "CREATE TABLE IF NOT EXISTS `gs_rilogbook_data` (
		`rilogbook_id` int(11) NOT NULL AUTO_INCREMENT,
		`dt_server` datetime NOT NULL,
		`dt_tracker` datetime NOT NULL,
		`imei` varchar(20) COLLATE utf8_bin NOT NULL,
		`group` varchar(2) COLLATE utf8_bin NOT NULL,
		`assign_id` varchar(30) COLLATE utf8_bin NOT NULL,
		`lat` double DEFAULT NULL,
		`lng` double DEFAULT NULL,
		`address` varchar(256) COLLATE utf8_bin NOT NULL,
		PRIMARY KEY (`rilogbook_id`)
	      ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
	$r = mysqli_query($ms, $q);
	
	// modify database tables
	$q = "alter table gs_user_object_drivers add column driver_assign_id varchar(30) not null COLLATE utf8_bin after `driver_name`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_object_trailers add column trailer_assign_id varchar(30) not null COLLATE utf8_bin after `trailer_name`";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_object_drivers SET driver_assign_id = UPPER(driver_rfid)";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_object_drivers SET driver_assign_id = UPPER(driver_ibutton) WHERE `driver_assign_id`=''";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_object_drivers drop column driver_rfid";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_object_drivers drop column driver_ibutton";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_object_cmd_exec modify cmd varchar(256) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_cmd modify cmd varchar(256) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "SELECT * FROM `gs_users` WHERE `privileges` NOT LIKE ('%subuser%')";
	$r = mysqli_query($ms, $q);
	
	while($row=mysqli_fetch_array($r))
	{
		$privileges = json_decode(stripslashes($row['privileges']), true);
		
		if (!isset($privileges["history"])) { $privileges["history"] = true; }
		if (!isset($privileges["reports"])) { $privileges["reports"] = true; }
		if (!isset($privileges["rilogbook"])) { $privileges["rilogbook"] = true; }
		if (!isset($privileges["object_control"])) { $privileges["object_control"] = true; }
		if (!isset($privileges["image_gallery"])) { $privileges["image_gallery"] = true; }
		if (!isset($privileges["chat"])) { $privileges["chat"] = true; }
		
		$privileges = json_encode($privileges);
		
		$q2 = "UPDATE gs_users SET privileges = '".$privileges."' WHERE id = '".$row['id']."'";
		$r2 = mysqli_query($ms, $q2);
	}
	
	$q = "alter table gs_user_cmd add column protocol varchar(256) not null COLLATE utf8_bin after `name`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events_data add column type varchar(10) not null COLLATE utf8_bin after `user_id`";
	$r = mysqli_query($ms, $q);
	
	// end modify database tables
	
	echo 'This part has successfully finished!';
?>