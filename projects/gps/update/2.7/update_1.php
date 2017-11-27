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
	$q = "CREATE TABLE IF NOT EXISTS `gs_objects_unused` (
		`imei` varchar(20) COLLATE utf8_bin NOT NULL,
		`protocol` varchar(50) COLLATE utf8_bin NOT NULL,
		`ip` varchar(50) COLLATE utf8_bin NOT NULL,
		`port` varchar(10) COLLATE utf8_bin NOT NULL,
		`dt_server` datetime NOT NULL,
		`count` int(11) NOT NULL,
		PRIMARY KEY (`imei`)
	      ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;";
	$r = mysqli_query($ms, $q);
	
	$q = "CREATE TABLE IF NOT EXISTS `gs_maps` (
		`map_id` int(11) NOT NULL AUTO_INCREMENT,
		`name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
		`active` varchar(5) COLLATE utf8_bin NOT NULL,
		`url` varchar(2048) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
		PRIMARY KEY (`map_id`)
	      ) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column map_sp varchar(7) not null COLLATE utf8_bin after `units`";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_users SET map_sp = 'default' WHERE `map_rmp`='false'";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_users SET map_sp = 'last' WHERE `map_rmp`='true'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users drop column map_rmp";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_templates add column language varchar(20) not null COLLATE utf8_bin after `name`";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_templates SET language = 'english'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_templates drop column type";
	$r = mysqli_query($ms, $q);
	
	$q = "SELECT * FROM `gs_objects`";
	$r = mysqli_query($ms, $q);
	
	while($row = mysqli_fetch_array($r))
	{
		$imei = $row['imei'];
		$fcr = json_decode($row['fcr'],true);
		
		$fcr_new = array();
		
		if ($fcr != '')
		{
			$fcr_new['measurement'] = $fcr['measurement'];
			$fcr_new['cost'] = $fcr['cost'];
			$fcr_new['summer'] = $fcr['summer'];
			$fcr_new['winter'] = $fcr['winter'];
			$fcr_new['winter_start'] = @$fcr['winter_sm'].'-'.@$fcr['winter_sd'];
			$fcr_new['winter_end'] = @$fcr['winter_em'].'-'.@$fcr['winter_ed'];
			
			$fcr_new = json_encode($fcr_new);
			
			$q2 = "UPDATE `gs_objects` SET `fcr`='".$fcr_new."' WHERE `imei`='".$imei."'";
			$r2 = mysqli_query($ms, $q2);
		}
	}
	
	$q = "alter table gs_users add column dst varchar(5) not null COLLATE utf8_bin after `timezone`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column dst_start varchar(20) not null COLLATE utf8_bin after `dst`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column dst_end varchar(20) not null COLLATE utf8_bin after `dst_start`";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_users SET dst = 'false'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_events add column route_trigger varchar(5) not null COLLATE utf8_bin after `checked_value`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column obj_edit varchar(5) not null COLLATE utf8_bin after `obj_history_clear`";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_users SET obj_edit = 'true'";
	$r = mysqli_query($ms, $q);
	// end modify database tables
	
	// modify config.custom.php
	$gsValuesNew = array();
	
	$gsValuesNew['OBJ_EDIT'] = "true";
	$gsValuesNew['SMS_GATEWAY_SERVER'] = "false";
	
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