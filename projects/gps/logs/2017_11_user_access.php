<?
session_start();
include ('../init.php');
include ('../func/fn_common.php');
checkUserSession();
checkUserCPanelPrivileges();
header('Content-Type:text/plain');
?>
[2017-11-23 13:58:01] ::1 [1]admin - User login: successful
[2017-11-23 13:58:38] ::1 [1]admin - User logout
[2017-11-23 13:58:41] ::1 - User logout
[2017-11-23 13:58:41] ::1 - User logout
[2017-11-23 13:58:42] ::1 - User logout
[2017-11-23 13:58:59] ::1 - User login: unsuccessful. Username: "demo"
[2017-11-23 13:59:30] ::1 [1]admin - User login: successful
[2017-11-23 14:01:34] ::1 [2]demo - User login: successful
[2017-11-23 14:21:10] ::1 - User login: unsuccessful. Username: "demo"
[2017-11-23 14:21:15] ::1 [2]demo - User login: successful
[2017-11-25 07:48:34] ::1 [1]admin - User login: successful
[2017-11-25 16:01:21] ::1 [1]admin - User login: successful
[2017-11-25 16:46:10] ::1 [1]admin - User login: successful
[2017-11-25 16:55:02] ::1 [1]admin - User login: successful
