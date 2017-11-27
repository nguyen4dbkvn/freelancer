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
        
        
	
	// create database tables
	$q = "CREATE TABLE IF NOT EXISTS `gs_sms_gateway_app` (
		`id` int(11) NOT NULL AUTO_INCREMENT,
		`dt_server` datetime NOT NULL,
		`identifier` varchar(20) COLLATE utf8_bin NOT NULL,
		`number` varchar(50) COLLATE utf8_bin NOT NULL,
		`message` varchar(1024) COLLATE utf8_bin NOT NULL,
		PRIMARY KEY (`id`),
		KEY `dt_server` (`dt_server`)
	      ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
	$r = mysqli_query($ms, $q);
	
	$q = "CREATE TABLE IF NOT EXISTS `gs_templates` (
		`template_id` int(11) NOT NULL AUTO_INCREMENT,
		`name` varchar(100) COLLATE utf8_bin NOT NULL,
		`type` varchar(10) COLLATE utf8_bin NOT NULL,
		`subject` varchar(512) COLLATE utf8_bin NOT NULL,
		`message` varchar(4096) COLLATE utf8_bin NOT NULL,
		PRIMARY KEY (`template_id`)
	      ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
	$r = mysqli_query($ms, $q);
	
	$q = "INSERT INTO `gs_templates` (`template_id`, `name`, `type`, `subject`, `message`) VALUES
		(1, 'account_registration', 'email', 'Registration at %SERVER_NAME%', 'Hello,\n\nThank you for registering at %SERVER_NAME%.\n\nAccess to GPS server: %URL_LOGIN%\n\nUsername: %EMAIL%\nPassword: %PASSWORD%'),
		(2, 'account_registration_au', 'email', 'Registration at %SERVER_NAME%', 'Hello,\n\nThank you for registering at %SERVER_NAME%.\n\nAccess to GPS server:\n%URL_AU%'),
		(3, 'account_recover', 'email', 'Lost login recovery to %SERVER_NAME%', 'Hello,\n\nAccess to GPS server: %URL_LOGIN%\n\nUsername: %EMAIL%\nPassword: %PASSWORD%'),
		(4, 'expiring_objects', 'email', 'Expiring objects', 'Hello,\n\nSome of your objects activation will expire soon. Please login into account for more details.\n\nIf you would like to continue using our service, please purchase access to %SERVER_NAME% at our shop:\n%URL_SHOP%'),
		(5, 'event_email', 'email', '%NAME% - %EVENT%', 'Hello,\n\nThis is event message, please do not reply to this message.\n\nObject: %NAME%\nEvent: %EVENT%\nPosition: %G_MAP%\nSpeed: %SPEED%\nTime (position): %DT_POS%'),
		(6, 'event_sms', 'sms', '%NAME% - %EVENT%', 'Hello,\nObject: %NAME%\nEvent: %EVENT%\nPosition: %LAT%, %LNG%\nSpeed: %SPEED%\nTime (position): %DT_POS%');";
	$r = mysqli_query($ms, $q);
	// end create database tables
	
	// modify database tables
	$q = "alter table gs_user_templates add column subject varchar(512) not null COLLATE utf8_bin after `desc`";
	$r = mysqli_query($ms, $q);	
	
	$q = "alter table gs_users add column sms_gateway_type varchar(5) not null COLLATE utf8_bin after sms_gateway";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_users add column sms_gateway_identifier varchar(20) not null COLLATE utf8_bin after sms_gateway_url";
	$r = mysqli_query($ms, $q);
	
	$q = "UPDATE gs_users SET sms_gateway_type = 'http'";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_user_markers modify marker_icon varchar(512) not null"; 
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects add column time_adj varchar(30) not null COLLATE utf8_bin after fcr"; 
	$r = mysqli_query($ms, $q);
	
	$q = "SELECT * FROM `gs_user_markers`";
	$r = mysqli_query($ms, $q);
	
	while($row=mysqli_fetch_array($r))
	{
		if (substr($row['marker_icon'], 0, 1) != 'i')
		{
			$q2 = "UPDATE gs_user_markers SET `marker_icon`='img/markers/places/".$row['marker_icon']."' WHERE marker_id='".$row['marker_id']."'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));
		}
	}
	// end modify database tables
	
	echo 'This part has successfully finished!';
?>