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
	$q = "alter table gs_user_events modify column cmd_string varchar(256)";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_object_cmd_exec modify column cmd varchar(256)";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events add column duration_from_last_event varchar(5) not null COLLATE utf8_bin after `active`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events add column duration_from_last_event_minutes int(11) not null after `duration_from_last_event`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events_status add column dt_server datetime not null after `event_id`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects change chat_dt_server dt_chat datetime not null after accuracy";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects add column dt_last_stop datetime not null after `params`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects add column dt_last_move datetime not null after `dt_last_stop`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_zones add column zone_area int(11) not null after `zone_name_visible`";
	$r = mysqli_query($ms, $q);
	// end modify database tables
	
	// modify config.custom.php
	$gsValuesNew = array();
	
	$gsValuesNew['DST'] = "false";
	$gsValuesNew['DST_START'] = "";
	$gsValuesNew['DST_END'] = "";
	$gsValuesNew['PAGE_AFTER_LOGIN'] = "account";
	
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