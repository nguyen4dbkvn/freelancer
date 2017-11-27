<?
	ob_start();
	echo "OK";
	header("Connection: close");
	header("Content-length: " . (string)ob_get_length());
	ob_end_flush();
	
        chdir('../');
        include ('s_insert.php');
	
	if (@$_POST["op"] == "img_loc")
        {
		$loc = array();
		
		$imei = $_POST['imei'];
		
		// check if object exists in system
		if (!checkObjectExistsSystem($imei))
		{
			return false;
		}
		
                $loc['dt_server'] = gmdate("Y-m-d H:i:s");
		$loc['dt_tracker'] = $_POST["dt_tracker"];
		$loc['lat'] = $_POST["lat"];
		$loc['lng'] = $_POST["lng"];
		$loc['altitude'] = $_POST["altitude"];
		$loc['angle'] = $_POST["angle"];
		$loc['speed'] = $_POST["speed"];
		$loc['params'] = $_POST["params"];
                
                $img_file = $imei.'_'.$loc['dt_tracker'].'.jpg';
                $img_file = str_replace('-', '', $img_file);
                $img_file = str_replace(':', '', $img_file);
                $img_file = str_replace(' ', '_', $img_file);
                
                // save to database
                $q = "INSERT INTO gs_object_img (img_file,
                                                imei,
                                                dt_server,
                                                dt_tracker,
                                                lat,
                                                lng,
                                                altitude,
                                                angle,
                                                speed,
                                                params
                                                ) VALUES (
                                                '".$img_file."',
                                                '".$imei."',
                                                '".$loc['dt_server']."',
                                                '".$loc['dt_tracker']."',
                                                '".$loc['lat']."',
                                                '".$loc['lng']."',
                                                '".$loc['altitude']."',
                                                '".$loc['angle']."',
                                                '".$loc['speed']."',
                                                '".json_encode($loc['params'])."')";
                                    
                $r = mysqli_query($ms, $q);
		
		 // save file
                $img_path = $gsValues['PATH_ROOT'].'/data/img/';
                $img_path = $img_path.basename($img_file);
                
		$postdata =  $_POST["img"];
		$postdata = hex2bin($postdata);
		                
                if(substr($postdata,0,3) == "\xFF\xD8\xFF")
                {
                        $fp = fopen($img_path,"w");
                        fwrite($fp,$postdata);
                        fclose($fp);
                }
	}
        
        if (@$_GET["op"] == "img")
        {
		// check for wrong IMEI
		if (!ctype_alnum($_GET['imei']))
		{
			return false;
		}
		
		// check if object exists in system
		if (!checkObjectExistsSystem($_GET['imei']))
		{
			return false;
		}
		
		$loc = array();
		
                // get previous known location
		$loc = get_gs_objects_data($_GET['imei']);
                
		if (!$loc) {die;}
		
                $loc['dt_server'] = gmdate("Y-m-d H:i:s");
                
                $img_file = $_GET['imei'].'_'.$loc['dt_server'].'.jpg';
                $img_file = str_replace('-', '', $img_file);
                $img_file = str_replace(':', '', $img_file);
                $img_file = str_replace(' ', '_', $img_file);
                
                // save to database
                $q = "INSERT INTO gs_object_img (img_file,
                                                imei,
                                                dt_server,
                                                dt_tracker,
                                                lat,
                                                lng,
                                                altitude,
                                                angle,
                                                speed,
                                                params
                                                ) VALUES (
                                                '".$img_file."',
                                                '".$_GET['imei']."',
                                                '".$loc['dt_server']."',
                                                '".$loc['dt_tracker']."',
                                                '".$loc['lat']."',
                                                '".$loc['lng']."',
                                                '".$loc['altitude']."',
                                                '".$loc['angle']."',
                                                '".$loc['speed']."',
                                                '".json_encode($loc['params'])."')";
						
                $r = mysqli_query($ms, $q);
                
                // save file
                $img_path = $gsValues['PATH_ROOT'].'/data/img/';
                $img_path = $img_path.basename($img_file);
                
                $postdata = file_get_contents('php://input', 'r');
                
                if(substr($postdata,0,3) == "\xFF\xD8\xFF")
                {
                        $fp = fopen($img_path,"w");
                        fwrite($fp,$postdata);
                        fclose($fp);
                } 
	}
	
	mysqli_close($ms);
	die;
?>