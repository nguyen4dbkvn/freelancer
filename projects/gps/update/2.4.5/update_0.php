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
	
	
	
	$q = "alter table gs_user_reports add column zones_addresses varchar(5) not null COLLATE utf8_bin after show_addresses";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add odometer_dt datetime not null after engine_hours";
	$r = mysqli_query($ms, $q);
	
	$q = 'UPDATE gs_trackers SET `odometer_dt` = dt_tracker';
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_trackers add engine_hours_dt datetime not null after odometer_dt";
	$r = mysqli_query($ms, $q);
	
	$q = 'UPDATE gs_trackers SET `engine_hours_dt` = dt_tracker';
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column places_routes varchar(4) not null COLLATE utf8_bin after places_zones";
	$r = mysqli_query($ms, $q);
	
	print 'This part has successfully finished!';
?>