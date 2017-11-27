<? 
	session_start();
	include ('../init.php');
	include ('fn_common.php');
	checkUserSession();
	
	// check privileges
	if ($_SESSION["privileges"] == 'subuser')
	{
		$user_id = $_SESSION["manager_id"];
	}
	else
	{
		$user_id = $_SESSION["user_id"];
	}
	
	if(@$_POST['cmd'] == 'load_last_event')
	{
		$result = array();
		
		// check privileges		
		if ($_SESSION["privileges"] == 'subuser')
		{
			$q = "SELECT * FROM `gs_user_events_data`
			WHERE `user_id`='".$user_id."' AND `imei` IN (".$_SESSION["privileges_imei"].")
			ORDER BY event_id DESC LIMIT 1";
		}
		else
		{
			$q = "SELECT * FROM `gs_user_events_data`
			WHERE `user_id`='".$user_id."'
			ORDER BY event_id DESC LIMIT 1";
		}
		
		$r = mysqli_query($ms, $q);
		$row = mysqli_fetch_array($r);
		
		if($row)
		{
			$row['name'] = getObjectName($row['imei']);
			
			$row['dt_server'] = convUserTimezone($row['dt_server']);
			$row['dt_tracker'] = convUserTimezone($row['dt_tracker']);
			
			$result = $row;
		}
		
		header('Content-type: application/json');
		echo json_encode($result);
		die;	
	}
	
	if(@$_POST['cmd'] == 'delete_all_events')
	{
		$q = "DELETE FROM `gs_user_events_data` WHERE `user_id`='".$user_id."'";
		$r = mysqli_query($ms, $q);
		
		echo 'OK';
		die;
	}

	if(@$_POST['cmd'] == 'load_event_data')
	{
		$event_id = $_POST['event_id'];
		
		// check privileges		
		if ($_SESSION["privileges"] == 'subuser')
		{
			$q = "SELECT * FROM `gs_user_events_data`
			WHERE `user_id`='".$user_id."' AND `event_id`='".$event_id."' AND `imei` IN (".$_SESSION["privileges_imei"].") LIMIT 1";
		}
		else
		{
			$q = "SELECT * FROM `gs_user_events_data`
			WHERE `user_id`='".$user_id."' AND `event_id`='".$event_id."' LIMIT 1";
		}
		
		$r = mysqli_query($ms, $q);
		$row = mysqli_fetch_array($r);
		
		if($row)
		{
			$row['name'] = getObjectName($row['imei']);
			
			$row['speed'] = convSpeedUnits($row['speed'], 'km', $_SESSION["unit_distance"]);
			$row['altitude'] = convAltitudeUnits($row['altitude'], 'km', $_SESSION["unit_distance"]);
			
			$params = json_decode($row['params'],true);
			
			$result = array('name' => $row['name'],
					'imei' => $row['imei'],
					'event_desc' => $row['event_desc'],
					'dt_server' => convUserTimezone($row['dt_server']),
					'dt_tracker' => convUserTimezone($row['dt_tracker']),
					'lat' => $row['lat'],
					'lng' => $row['lng'],
					'altitude' => $row['altitude'],
					'angle' => $row['angle'],
					'speed' => $row['speed'],
					'params' => $params);	
		}
		
		header('Content-type: application/json');
		echo json_encode($result);
		die;	
	}

	if(@$_GET['cmd'] == 'load_event_list')
	{		
		$page = $_GET['page']; // get the requested page
		$limit = $_GET['rows']; // get how many rows we want to have into the grid
		$sidx = $_GET['sidx']; // get index row - i.e. user click to sort
		$sord = $_GET['sord']; // get the direction
		$search = strtoupper(@$_GET['s']); // get search
		
		if(!$sidx) $sidx =1;
			
		// check privileges		
		if ($_SESSION["privileges"] == 'subuser')
		{
			$q = "SELECT gs_user_events_data.*, gs_objects.name
			FROM gs_user_events_data
			INNER JOIN gs_objects ON gs_user_events_data.imei = gs_objects.imei
			WHERE gs_user_events_data.user_id='".$user_id."'
			AND (UPPER(gs_user_events_data.event_desc) LIKE '%$search%' OR UPPER(gs_objects.name) LIKE '%$search%')
			AND gs_user_events_data.imei IN (".$_SESSION["privileges_imei"].")";
		}
		else
		{
			$q = "SELECT gs_user_events_data.*, gs_objects.name
			FROM gs_user_events_data
			INNER JOIN gs_objects ON gs_user_events_data.imei = gs_objects.imei
			WHERE gs_user_events_data.user_id='".$user_id."'
			AND (UPPER(gs_user_events_data.event_desc) LIKE '%$search%' OR UPPER(gs_objects.name) LIKE '%$search%')";
		}
		
		$r = mysqli_query($ms, $q);
		$count = mysqli_num_rows($r);
		
		if ($count > 0)
		{
			$total_pages = ceil($count/$limit);
		}
		else
		{
			$total_pages = 1;
		}
		
		if ($page > $total_pages) $page=$total_pages;
		$start = $limit*$page - $limit; // do not put $limit*($page - 1)
		
		$q .= " ORDER BY gs_user_events_data.$sidx $sord LIMIT $start, $limit";
		$r = mysqli_query($ms, $q);
			
		$response = new stdClass();
		$response->page = $page;
		$response->total = $total_pages;
		$response->records = $count;
		
		if ($r)
		{
			$i=0;
			while($row = mysqli_fetch_array($r))
			{
				$imei = $row['imei'];
				
				if (checkObjectActive($imei) == true)
				{
					$dt_tracker = convUserTimezone($row['dt_tracker']);
					
					$q2 = "SELECT * FROM `gs_user_objects` WHERE `user_id`='".$user_id."' AND `imei`='".$row['imei']."'";
					$r2 = mysqli_query($ms, $q2);
					$user_data = mysqli_fetch_array($r2);
					
					$response->rows[$i]['id'] = $row['event_id'];
					$response->rows[$i]['cell']=array($dt_tracker, $row['name'], $row['event_desc']);
					$i++;	
				}	
			}
		}
		
		header('Content-type: application/json');
		echo json_encode($response);
		die;
	}
?>