<?
	ob_start();
	echo "OK";
	//header("Connection: close");
	header("Content-length: " . (string)ob_get_length());
	ob_end_flush();
	
	chdir('../');
	include ('s_insert.php');
	
	// check if data comes packaged
	if (isset($_POST["data"]))
	{
		$_POST["data"] = preg_replace( '/'.chr(31).'/', '&', $_POST["data"]);
		
		$loc_arr = array();
		$loc_arr = explode(chr(30), $_POST["data"]);
		
		foreach ($loc_arr as $key=>$loc_str)
		{
			parse_str($loc_str, $loc);
			execLoc($loc);
		}
	}
	else
	{
		execLoc($_POST);
	}
	
	function execLoc($loc)
	{
		if (@$loc["op"] == "loc")
		{
			$loc['protocol'] = @$loc['protocol'];
			$loc['net_protocol'] = '';
			$loc['ip'] = @$loc['ip'];			
			$loc['port'] = @$loc['port'];
			$loc['dt_server'] = gmdate("Y-m-d H:i:s");
			$loc['params'] = paramsToArray($loc['params']);
			
			insert_db_loc($loc);
		}
		
		if (@$loc["op"] == "noloc")
		{
			$loc['protocol'] = @$loc['protocol'];
			$loc['net_protocol'] = '';
			$loc['ip'] = @$loc['ip'];			
			$loc['port'] = @$loc['port'];
			$loc['dt_server'] = gmdate("Y-m-d H:i:s");
			$loc['params'] = paramsToArray($loc['params']);
			
			insert_db_noloc($loc);
		}
	}

	mysqli_close($ms);
	die;
?>