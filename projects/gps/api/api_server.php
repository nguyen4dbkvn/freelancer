<?
        if (@$api_access != true) { die; }
        
        // split command and params
        $cmd = explode(',', $cmd);
        $command = @$cmd[0];
        $command = strtoupper($command);
        
        if ($command == 'CHECK_USER_EXISTS')
        {
                // command validation
                if (count($cmd) < 2) { die; }
                
                // command parameters
                $email = strtolower($cmd[1]);
                
                if(checkUserExists($email))
                {
                        echo 'true';
                }
                else
                {
                        echo 'false';
                }
        }
        
        if ($command == 'ADD_USER')
        {
                loadLanguage('english');
                
                // command validation
                if (count($cmd) < 2) { die; }
                
                // command parameters
                $email = strtolower($cmd[1]);
                
                $privileges = array();
                $privileges['type'] = 'user';
                $privileges['history'] = stringToBool($gsValues['HISTORY']);
                $privileges['reports'] = stringToBool($gsValues['REPORTS']);
                $privileges['rilogbook'] = stringToBool($gsValues['RILOGBOOK']);
                $privileges['dtc'] = stringToBool($gsValues['DTC']);
                $privileges['object_control'] = stringToBool($gsValues['OBJECT_CONTROL']);
                $privileges['image_gallery'] = stringToBool($gsValues['IMAGE_GALLERY']);
                $privileges['chat'] = stringToBool($gsValues['CHAT']);
                $privileges['subaccounts'] = stringToBool($gsValues['SUBACCOUNTS']);
                $privileges = json_encode($privileges);
                
                addUser('true', 'true', 'false', '', $privileges, '', $email, '', $gsValues['OBJ_ADD'], $gsValues['OBJ_LIMIT'], $gsValues['OBJ_LIMIT_NUM'], $gsValues['OBJ_DAYS'], $gsValues['OBJ_DAYS_NUM'], $gsValues['OBJ_EDIT'], $gsValues['OBJ_HISTORY_CLEAR']);
        }
        
        if ($command == 'DEL_USER')
        {
                // command validation
                if (count($cmd) < 2) { die; }
                
                // command parameters
                $email = strtolower($cmd[1]);
                
                // get user id from email
                $user_id = getUserIdFromEmail($email);
                
                if (!$user_id)
                {
                      die;  
                }
                
                // delete user
                delUser($user_id);
        }
        
        if ($command == 'ADD_OBJECT')
        {
                // command validation
                if (count($cmd) < 5) { die; }
                
                // command parameters
                $imei = strtoupper($cmd[1]);
                $name = $cmd[2];
                $object_expire = $cmd[3];
                $object_expire_dt = $cmd[4];
                
                if($imei == '') die;
                if($name == '') die;
                if($object_expire == '') die;
                
                if(checkObjectLimitSystem()) die;
                
                // add object
                addObjectSystem($name, $imei, 'true', $object_expire, $object_expire_dt, '0');
                createObjectDataTable($imei);
        }
        
        if ($command == 'DEL_OBJECT')
        {
                // command validation
                if (count($cmd) < 2) { die; }
                
                // command parameters
                $imei = strtoupper($cmd[1]);
                
                // delete object
                delObjectSystem($imei);
        }
        
        if ($command == 'ADD_USER_OBJECT')
        {
                // command validation
                if (count($cmd) < 3) { die; }
                
                // command parameters
                $email = strtolower($cmd[1]);
                $imei = strtoupper($cmd[2]);
                
                if($email == '') die;
                if($imei == '') die;
                
                // get user id from email
                $user_id = getUserIdFromEmail($email);
                
                // add object to user
                addObjectUser($user_id, $imei, 0, 0, 0);    
        }
        
        if ($command == 'DEL_USER_OBJECT')
        {
                // command validation
                if (count($cmd) < 3) { die; }
                
                // command parameters
                $email = strtolower($cmd[1]);
                $imei = strtoupper($cmd[2]);
                
                // get user id from email
                $user_id = getUserIdFromEmail($email);
                
                if (!$user_id)
                {
                      die;  
                }
                
                // delete object from user
                delObjectUser($user_id, $imei);
        }
        
        if ($command == 'OBJECT_SET_ACTIVITY')
        {
                // command validation
                if (count($cmd) < 5) { die; }
                
                // command parameters
                $imei = strtoupper($cmd[1]);
                $active = strtolower($cmd[2]);
                $object_expire = $cmd[3];
                $object_expire_dt = $cmd[4];
                
                // command exec               
                if ($active == 'true')
                {                        
                        $q = "UPDATE `gs_objects` SET `active`='true', `object_expire`='".$object_expire."', `object_expire_dt`='".$object_expire_dt."' WHERE `imei`='".$imei."'";
                }
                else if ($active == 'false')
                {
                        $q = "UPDATE `gs_objects` SET `active`='false', `object_expire`='".$object_expire."', `object_expire_dt`='".$object_expire_dt."' WHERE `imei`='".$imei."'";
                }
                //error_log($q);
                $r = mysqli_query($ms, $q);
        }
        
        if ($command == 'ADD_USER_BILLING_PLAN')
        {
                // command validation
                if (count($cmd) < 3) { die; }
                
                // command parameters
                $email = strtolower($cmd[1]);
                $plan_id = $cmd[2];
                
                // command exec
                $user_id = getUserIdFromEmail($email);
                
                if (!$user_id)
                {
                      die;  
                }
                
		$dt_purchase = gmdate("Y-m-d H:i:s");
                
                $q = "SELECT * FROM `gs_billing_plans` WHERE `plan_id`='".$plan_id."'";
		$r = mysqli_query($ms, $q);
                
                if (!$r)
                {
                      die;  
                }
                
		$row = mysqli_fetch_array($r);
                
                $name = $row['name'];
                $active = $row['active'];
                $objects = $row['objects'];
                $period = $row['period'];
                $period_type = $row['period_type'];
                $price = $row['price'];
                
                if ($active == 'true')
                {
                        $q = "INSERT INTO `gs_user_billing_plans` (`user_id`,
                                                                `dt_purchase`,
                                                                `name`,
                                                                `objects`,
                                                                `period`,
                                                                `period_type`,
                                                                `price`
                                                                ) VALUES (
                                                                '".$user_id."',
                                                                '".$dt_purchase."',
                                                                '".$name."',
                                                                '".$objects."',
                                                                '".$period."',
                                                                '".$period_type."',
                                                                '".$price."')";
                      $r = mysqli_query($ms, $q);        
                }
        }
        
        if ($command == 'GET_USER_API_KEY')
        {
                // command validation
                if (count($cmd) < 2) { die; }
                
                // command parameters
                $email = strtolower($cmd[1]);
                
                // get user api key from email
                $api_key = getUserAPIKeyFromEmail($email);
                
                if (!$api_key)
                {
                      die;  
                }
                
                echo $api_key;
        }
        
        die;
?>