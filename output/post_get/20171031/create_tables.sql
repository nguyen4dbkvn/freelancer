/*
Bài toán POST: dv_ed_post 
*/
CREATE TABLE `dv_ed_post` (
`seq_ID`  int(20) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT ,
`accountID`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`deviceID`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`timestamp`  int(11) NOT NULL ,
`statusCode`  int(10) NOT NULL ,
`statusLastingTime`  int(12) NULL DEFAULT NULL ,
`latitude`  double NULL DEFAULT NULL ,
`longitude`  double NULL DEFAULT NULL ,
`gpsAge`  int(10) NULL DEFAULT NULL ,
`speedKPH`  double NULL DEFAULT NULL ,
`heading`  double NULL DEFAULT NULL ,
`altitude`  double NULL DEFAULT NULL ,
`address`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`distanceKM`  double NULL DEFAULT NULL ,
`odometerKM`  double NULL DEFAULT NULL ,
`creationTime`  int(11) NULL DEFAULT NULL ,
`driverID`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`driverStatus`  int(10) NULL DEFAULT NULL ,
`driverMessage`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`params`  varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`db_name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'gtse' ,
PRIMARY KEY (`seq_ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=255
ROW_FORMAT=COMPACT
;

/*
Bài toán POST: Bảng ED_Get
*/
CREATE TABLE `ED_Get` (
`seq_ID`  int(20) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT ,
`accountID`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`deviceID`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`timestamp`  int(11) NOT NULL ,
`statusCode`  int(10) NOT NULL ,
`statusLastingTime`  int(12) NULL DEFAULT NULL ,
`latitude`  double NULL DEFAULT NULL ,
`longitude`  double NULL DEFAULT NULL ,
`gpsAge`  int(10) NULL DEFAULT NULL ,
`speedKPH`  double NULL DEFAULT NULL ,
`heading`  double NULL DEFAULT NULL ,
`altitude`  double NULL DEFAULT NULL ,
`address`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`distanceKM`  double NULL DEFAULT NULL ,
`odometerKM`  double NULL DEFAULT NULL ,
`creationTime`  int(11) NULL DEFAULT NULL ,
`driverID`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`driverStatus`  int(10) NULL DEFAULT NULL ,
`driverMessage`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`params`  varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`db_name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'gtse' ,
PRIMARY KEY (`seq_ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT
;

/*
Bài toán GET: Bảng ED_Post
*/
CREATE TABLE `ED_Post` (
`seq_ID`  int(20) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT ,
`accountID`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`deviceID`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`timestamp`  int(11) NOT NULL ,
`statusCode`  int(10) NOT NULL ,
`statusLastingTime`  int(12) NULL DEFAULT NULL ,
`latitude`  double NULL DEFAULT NULL ,
`longitude`  double NULL DEFAULT NULL ,
`gpsAge`  int(10) NULL DEFAULT NULL ,
`speedKPH`  double NULL DEFAULT NULL ,
`heading`  double NULL DEFAULT NULL ,
`altitude`  double NULL DEFAULT NULL ,
`address`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`distanceKM`  double NULL DEFAULT NULL ,
`odometerKM`  double NULL DEFAULT NULL ,
`creationTime`  int(11) NULL DEFAULT NULL ,
`driverID`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`driverStatus`  int(10) NULL DEFAULT NULL ,
`driverMessage`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`Cua`  int(11) NULL DEFAULT NULL ,
`DieuHoa`  int(11) NULL DEFAULT NULL ,
`Speed30s`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' ,
`params`  varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`db_name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'gtse' ,
`imei`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '00000000000000000000' ,
PRIMARY KEY (`seq_ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=5
ROW_FORMAT=COMPACT
;

/*
Bài toán GET: Bảng dv_ed_get
*/
CREATE TABLE `dv_ed_get` (
`seq_ID`  int(20) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT ,
`accountID`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`deviceID`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`timestamp`  int(11) NOT NULL ,
`statusCode`  int(10) NOT NULL ,
`statusLastingTime`  int(12) NULL DEFAULT NULL ,
`latitude`  double NULL DEFAULT NULL ,
`longitude`  double NULL DEFAULT NULL ,
`gpsAge`  int(10) NULL DEFAULT NULL ,
`speedKPH`  double NULL DEFAULT NULL ,
`heading`  double NULL DEFAULT NULL ,
`altitude`  double NULL DEFAULT NULL ,
`address`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`distanceKM`  double NULL DEFAULT NULL ,
`odometerKM`  double NULL DEFAULT NULL ,
`creationTime`  int(11) NULL DEFAULT NULL ,
`driverID`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`driverStatus`  int(10) NULL DEFAULT NULL ,
`driverMessage`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`Cua`  int(11) NULL DEFAULT NULL ,
`DieuHoa`  int(11) NULL DEFAULT NULL ,
`Speed30s`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' ,
`params`  varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`db_name`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'gtse' ,
`imei`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '00000000000000000000' ,
PRIMARY KEY (`seq_ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT
;

/*
Bài toán GET: Bảng IMEI
XXXXXXXXXXXXXXX: imei of dv_ed_post
*/
CREATE TABLE `gs_object_data_XXXXXXXXXXXXXXX` (
`dt_server`  datetime NOT NULL ,
`dt_tracker`  datetime NOT NULL ,
`lat`  double NULL DEFAULT NULL ,
`lng`  double NULL DEFAULT NULL ,
`altitude`  double NULL DEFAULT NULL ,
`angle`  double NULL DEFAULT NULL ,
`speed`  double NULL DEFAULT NULL ,
`params`  varchar(2048) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
INDEX `dt_tracker` (`dt_tracker`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
ROW_FORMAT=COMPACT
;

