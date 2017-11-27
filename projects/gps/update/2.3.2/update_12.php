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
        
        
	
	// modify config.custom.php
	$gsValuesNew = array();
	
	$gsValuesNew['HISTORY'] = "true";
	$gsValuesNew['REPORTS'] = "true";
	$gsValuesNew['RILOGBOOK'] = "true";
	$gsValuesNew['OBJECT_CONTROL'] = "true";
	$gsValuesNew['IMAGE_GALLERY'] = "true";
	$gsValuesNew['CHAT'] = "true";
	$gsValuesNew['OBJ_HISTORY_CLEAR'] = "true";
	$gsValuesNew['ACCOUNT_EXPIRE'] = "false";
	$gsValuesNew['ACCOUNT_EXPIRE_PERIOD'] = "14";
	$gsValuesNew['NOTIFY_OBJ_EXPIRE'] = "true";
	$gsValuesNew['NOTIFY_ACCOUNT_EXPIRE'] = "true";
	$gsValuesNew['NOTIFY_ACCOUNT_EXPIRE_PERIOD'] = "7";
	$gsValuesNew['SMS_GATEWAY_NUMBER_FILTER'] = "";
	$gsValuesNew['SERVER_CLEANUP_USERS_AE'] = "false";
	$gsValuesNew['SERVER_CLEANUP_OBJECTS_NOT_ACTIVATED_AE'] = "false";
	$gsValuesNew['SERVER_CLEANUP_OBJECTS_NOT_USED_AE'] = "false";
	$gsValuesNew['SERVER_CLEANUP_DB_JUNK_AE'] = "false";
	$gsValuesNew['SERVER_CLEANUP_USERS_DAYS'] = "30";
	$gsValuesNew['SERVER_CLEANUP_OBJECTS_NOT_ACTIVATED_DAYS'] = "30";

	$config = '';
	foreach ($gsValuesNew as $key => $value)
	{
		$config .= '$gsValues[\''.strtoupper($key).'\'] = "'.$value.'";'."\r\n";
	}
	
	$config = "<?\r\n".$config. "?>";
	
	file_put_contents('../config.custom.php', $config, FILE_APPEND | LOCK_EX);
	// end modify config.custom.php
	
	echo 'This part has successfully finished!';
?>