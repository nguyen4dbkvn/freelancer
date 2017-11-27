<?
	error_reporting(E_ALL ^ E_DEPRECATED);

	session_start();
	set_time_limit(0);
	
	include ('../config.php');
	include ('../config.custom.php');
	
	$ms = mysqli_connect($gsValues['DB_HOSTNAME'], $gsValues['DB_USERNAME'], $gsValues['DB_PASSWORD'], $gsValues['DB_NAME'], $gsValues['DB_PORT']);
	if (!$ms)
	{
	    echo "Error connecting to database.";
	    die;
	}
	
	$q = "SET @@global.sql_mode= '';";
	$r = mysqli_query($ms, $q);
	
	// --------------------------------------------------------
	// modify database tables
	// --------------------------------------------------------
	
	$q = "CREATE TABLE IF NOT EXISTS `gs_user_reports_generated` (
		`report_id` int(11) NOT NULL AUTO_INCREMENT,
		`user_id` int(11) NOT NULL,
		`dt_report` datetime NOT NULL,
		`name` varchar(50) COLLATE utf8_bin NOT NULL,
		`type` varchar(20) COLLATE utf8_bin NOT NULL,
		`format` varchar(4) COLLATE utf8_bin NOT NULL,
		`objects` int(11) NOT NULL,
		`zones` int(11) NOT NULL,
		`sensors` int(11) NOT NULL,
		`schedule` varchar(5) COLLATE utf8_bin NOT NULL,
		`report_file` varchar(50) COLLATE utf8_bin NOT NULL,
		`filename` varchar(100) COLLATE utf8_bin NOT NULL,
		PRIMARY KEY (`report_id`),
		KEY `user_id` (`user_id`)
	      ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects add column net_protocol varchar(3) not null after `protocol`";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_objects_unused add column net_protocol varchar(3) not null after `protocol`";
	$r = mysqli_query($ms, $q);
	
	$q = "DROP TABLE gs_rfid_swipe_data";
	$r = mysqli_query($ms, $q);
		
	$q = "ALTER TABLE gs_geocoder_cache DROP COLUMN count";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_sms_gateway_app change dt_server dt_sms datetime not null";
	$r = mysqli_query($ms, $q);
	
	$q = "SELECT * FROM `gs_user_events`";
	$r = mysqli_query($ms, $q);
	
	while ($row = mysqli_fetch_array($r))
	{
		$week_days = str_replace(";", ",", $row['week_days']);
		
		$q2 = "UPDATE gs_user_events SET `week_days`='".$week_days."' WHERE `event_id`='".$row['event_id']."'";
		$r2 = mysqli_query($ms, $q2);
	}
	
	$q = "INSERT INTO `gs_templates` (`name`, `language`, `subject`, `message`) VALUES";
	$q .= " ('database_backup', 'english', 'Database backup', 'Hello,\n\nThis is database backup, please do not reply to this e-mail.')";
	$r = mysqli_query($ms, $q);
	
	
	
	$gsValuesNew['SUBACCOUNTS'] = 'true';
	
	$config = '';
	foreach ($gsValuesNew as $key => $value)
	{
		$config .= '$gsValues[\''.strtoupper($key).'\'] = "'.$value.'";'."\r\n";
	}
	
	$config = "<?\r\n".$config. "?>";
	
	file_put_contents('../config.custom.php', $config, FILE_APPEND | LOCK_EX);
	
	// --------------------------------------------------------

	echo 'Script successfully finished!';
?>