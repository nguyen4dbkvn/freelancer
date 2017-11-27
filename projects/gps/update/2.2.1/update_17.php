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
	$q = "CREATE TABLE IF NOT EXISTS `gs_user_places_groups` (
		`group_id` int(11) NOT NULL AUTO_INCREMENT,
		`user_id` int(11) NOT NULL,
		`group_name` varchar(50) COLLATE utf8_bin NOT NULL,
		`group_desc` varchar(1024) COLLATE utf8_bin NOT NULL,
		PRIMARY KEY (`group_id`)
	      ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_cmd SET gateway=LOWER(gateway)";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_cmd SET type=LOWER(type)";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_events SET cmd_gateway=LOWER(cmd_gateway)";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_user_events SET cmd_type=LOWER(cmd_type)";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_object_cmd_exec SET gateway=LOWER(gateway)";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_object_cmd_exec SET type=LOWER(type)";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column od varchar(10) not null COLLATE utf8_bin after `map_rhc`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_markers add column group_id int(11) not null after `user_id`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_routes add column group_id int(11) not null after `user_id`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_zones add column group_id int(11) not null after `user_id`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_maps add column type varchar(5) not null COLLATE utf8_bin after `active`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_maps add column layers varchar(256) not null COLLATE utf8_bin after `url`";
	$r = mysqli_query($ms, $q);
	// end modify database tables
	
	// modify config.custom.php
	$gsValuesNew = array();
	
	$gsValuesNew['LANGUAGES'] = "";
	
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