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
	
	$q = "CREATE TABLE IF NOT EXISTS `gs_themes` (
		`theme_id` int(11) NOT NULL AUTO_INCREMENT,
		`name` varchar(50) COLLATE utf8_bin NOT NULL,
		`active` varchar(5) COLLATE utf8_bin NOT NULL,
		`theme` varchar(2048) COLLATE utf8_bin NOT NULL,
		PRIMARY KEY (`theme_id`)
	      ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;";
	$r = mysqli_query($ms, $q);
	
	$q = "alter table gs_system modify `value` varchar(1024) not null after `key`"; 
	$r = mysqli_query($ms, $q);
	
	
	$gsValuesNew['LOGO_SMALL'] = 'logo_small.svg';
	
	$config = '';
	foreach ($gsValuesNew as $key => $value)
	{
		$config .= '$gsValues[\''.strtoupper($key).'\'] = "'.$value.'";'."\r\n";
	}
	
	$config = "<?\r\n".$config. "?>";
	
	file_put_contents('../config.custom.php', $config, FILE_APPEND | LOCK_EX);

	echo 'Script successfully finished!';
?>