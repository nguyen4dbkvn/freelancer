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
		
		if (substr($row['params'], -1) == '|')
		{
			$params = json_encode(paramsToArray($row['params']));
			
			$q2 = "UPDATE gs_objects SET `params`='".$params."' WHERE imei='".$imei."'";
			$r2 = mysqli_query($ms, $q2) or die(mysqli_error($ms));
		}
	}
	
	function paramsToArray($params)
	{
		$params = explode("|", $params);
		$arr_params = array();
		
		for ($i = 0; $i < count($params)-1; ++$i)
		{
			$param = explode("=", $params[$i]);
			$arr_params[$param[0]] = $param[1];
		}
		
		return $arr_params;
	}
	
	echo 'This part has successfully finished!';
?>