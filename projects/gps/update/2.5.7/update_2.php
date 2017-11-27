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
        
        
	
	$q = "SELECT * FROM `gs_objects`";
	$r = mysqli_query($ms, $q);
	
	while($row = mysqli_fetch_array($r))
	{
		$imei = $row['imei'];
		
		createObjectDataTable($imei);
	}
	
	function createObjectDataTable($imei)
	{
		global $ms;
		
		$q = "CREATE TABLE IF NOT EXISTS gs_object_data_".$imei."(	dt_server datetime NOT NULL,
										dt_tracker datetime NOT NULL,
										lat double,
										lng double,
										altitude double,
										angle double,
										speed double,
										params varchar(2048) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
										KEY `dt_tracker` (`dt_tracker`)
										) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin";
		$r = mysqli_query($ms, $q);
	}
	
	echo 'This part has successfully finished!';
?>