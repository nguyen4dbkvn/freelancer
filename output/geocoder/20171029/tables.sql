CREATE TABLE `dv_geocoder` (
	`seq_ID`  int(20) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT ,
    `lat`  double NULL DEFAULT NULL ,
	`lng`  double NULL DEFAULT NULL ,
    `address`  varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
    /*! other colums define */
    PRIMARY KEY (`seq_ID`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT
;