package org.opengts.db.tables;

public class DoXang {
	String accountID, deviceID, address;
	long  time;
	double latitude, longtitude, fuelLevel;

	public DoXang(String _accountID, String _deviceID, long _time,double  _latitude,
			String _address, double _longtitude, double _fuelLevel) {
		this.accountID = _accountID;
		this.deviceID = _deviceID;
		this.time = _time;
		this.latitude = _latitude;
		this.longtitude = _longtitude;
		this.address = _address;
		this.fuelLevel = _fuelLevel;
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

	public long getTime() {
		return this.time;
	}

	public double getLatitude() {
		return this.latitude;
	}
	
	public double getFuelLevel(){
		return this.fuelLevel;
	}
}
