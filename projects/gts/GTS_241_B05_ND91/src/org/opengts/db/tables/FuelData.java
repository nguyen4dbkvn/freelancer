package org.opengts.db.tables;

import java.sql.Date;
public class FuelData {
	String accountID, deviceID, address;
	double latitude, longitude, fuelLevel, odometerKM, changeValue;
	Integer timestamp, status, alarmType;
	
	public FuelData(String _accountID, String _deviceID, Integer _timestamp,
			double _latitude, double _longitude, String _address, double _fuelLevel,
			double _odometerKM, Integer _statusCode, double  _changeValue, Integer _alarmType) {
		this.accountID = _accountID;
		this.deviceID = _deviceID;
		this.latitude = _latitude;
		this.longitude = _longitude;
		this.timestamp = _timestamp;
		this.fuelLevel = _fuelLevel;
		this.odometerKM = _odometerKM;
		this.address = _address;
		this.status = _statusCode;
		this.changeValue = _changeValue;
		this.alarmType = _alarmType;
		//this.nhienLieu = _nhienLieu;
	}
	public FuelData(String _accountID, String _deviceID, Integer _timestamp,
			double _latitude, double _longitude, String _address, double _fuelLevel,
			double _odometerKM, Integer _statusCode, double  _changeValue) {
		this.accountID = _accountID;
		this.deviceID = _deviceID;
		this.latitude = _latitude;
		this.longitude = _longitude;
		this.timestamp = _timestamp;
		this.fuelLevel = _fuelLevel;
		this.odometerKM = _odometerKM;
		this.address = _address;
		this.status = _statusCode;
		this.changeValue = _changeValue;
	 
		//this.nhienLieu = _nhienLieu;
	}
	/*public double getNhienLieu()
	{
		return this.nhienLieu;
	}*/
	
	public Integer getAlarmType(){
		return this.alarmType;
	}
	
	public double getChangeValue()
	{
		return this.changeValue;
	}
	
	public String getAccountID() {
		return this.accountID;
	}

	public String getDeviceID() {
		return this.deviceID;
	}

	public String getAddress() {
		return this.address;
	}
	
	public Double getLatitude() {
		return this.latitude;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public Integer getTimeStamp() {
		return this.timestamp;
	}

	public Double GetFuelLevel() {
		return this.fuelLevel;
	}

	public Double GetOdometerKM() {
		return this.odometerKM;
	}
	
	public Integer GetStatusCode(){
		return this.status;
	}
	
}
