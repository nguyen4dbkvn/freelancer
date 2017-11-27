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
	
	$q = "alter table gs_user_events add column day_time varchar(512) not null COLLATE utf8_bin after week_days";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column chat_notify varchar(40) not null COLLATE utf8_bin after map_rhc";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects modify tail_points int(4) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = 'UPDATE gs_users SET privileges=\'{"type":"super_admin"}\' WHERE privileges=\'{"type":"admin"}\'';
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column currency varchar(3) not null COLLATE utf8_bin after obj_dt";
	$r = mysqli_query($ms, $q);
	
	echo 'This part has successfully finished!';
?>