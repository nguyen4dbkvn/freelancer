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
	$q = "alter table gs_objects add column dt_last_idle datetime not null after `dt_last_stop`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column ohc varchar(256) not null COLLATE utf8_bin after `map_rhc`";
	$r = mysqli_query($ms, $q);
	// end modify database tables
	
	// modify config.custom.php
	$gsValuesNew = array();
	
	$gsValuesNew['GENERATOR'] = "";
	
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