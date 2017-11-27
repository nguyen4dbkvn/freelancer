package org.opengts.db.tables;

public class Fuel {
	String accountID, deviceID, address;
	long  timeStamp, statusLastingTime;
	Integer  statusCode, Cua;
	double altitude ;

	public Fuel(String _accountID, String _deviceID, long _timeStamp,
			long _statusLastingTime,double  _altitude, String _address,
			Integer _statusCode, Integer _Cua) {
		this.accountID = _accountID;
		this.deviceID = _deviceID;
		this.statusLastingTime = _statusLastingTime;
		this.timeStamp = _timeStamp;
		this.altitude = _altitude;
		this.Cua = _Cua;
		this.address = _address;
		this.statusCode = _statusCode;
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
	
	public long getStatusLastingTime() {
		return this.statusLastingTime;
	}


	public long getTimeStamp() {
		return this.timeStamp;
	}

	public Integer GetCua() {
		return this.Cua;
	}

	public double getAltitude() {
		return this.altitude;
	}
	
	public Integer GetStatusCode(){
		return this.statusCode;
	}
}
