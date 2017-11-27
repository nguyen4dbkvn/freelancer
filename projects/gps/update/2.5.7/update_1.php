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
        
        
	
	$q = "alter table gs_objects add column protocol varchar(50) not null COLLATE utf8_bin after imei";
	$r = mysqli_query($ms, $q);
	
	echo 'This part has successfully finished!';
?>