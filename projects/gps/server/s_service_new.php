<?
	include ('s_init.php');
	
	$data = json_decode(file_get_contents("php://input"), true);
	
	if ($data["op"] == "cmd_exec_get")
  	{
		$q = "SELECT gs_objects.*, gs_object_cmd_exec.*
			FROM gs_objects
			INNER JOIN gs_object_cmd_exec ON gs_objects.imei = gs_object_cmd_exec.imei
			WHERE gs_object_cmd_exec.status='0' ORDER BY gs_object_cmd_exec.cmd_id ASC";
		$r = mysqli_query($ms, $q);
		
		$result = array();
		
		while($row = mysqli_fetch_array($r))
		{
			$result[] = array("cmd_id" => intval($row['cmd_id']),
					  "protocol" => $row['protocol'],
					  "net_protocol" => $row['net_protocol'],
					  "ip" => $row['ip'],
					  "port" => intval($row['port']),
					  "imei" => $row['imei'],
					  "type" => $row['type'],
					  "cmd" => $row['cmd']);
		}
		
		header('Content-type: application/json');
		echo json_encode($result);
		die;
	}
	
	if (@$data["op"] == "cmd_exec_set")
  	{
		if (isset($data["re_hex"]))
		{
			$q = "UPDATE `gs_object_cmd_exec` SET `status`='".$data["status"]."', `re_hex`='".$data["re_hex"]."' WHERE `cmd_id`='".$data["cmd_id"]."'";
		}
		else
		{
			$q = "UPDATE `gs_object_cmd_exec` SET `status`='".$data["status"]."' WHERE `cmd_id`='".$data["cmd_id"]."'";
		}
		
		$r = mysqli_query($ms, $q);
		
		echo "OK";
		die;
	}
?>