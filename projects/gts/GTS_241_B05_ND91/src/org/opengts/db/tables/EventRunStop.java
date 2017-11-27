package org.opengts.db.tables;

public class EventRunStop {
	String accountID, deviceID, address, address1, driverID, driverName, driverMessage; 
	int timestamp, statusCode, statusLastingTime, gpsAge, numberOVSpeed, numberldleTime, totalldleTime, totalDrivingTime, numberOV4h, cretationTime, driverStatus;
	double latitude, longitude , latitude1, longitude1, speedKPH, avgSpeedKPH, maxSpeedKPH, heading, distanceKM, odometerKM, fuelLevel;
	
	
	
	/**
	 * 
	 */
	public EventRunStop() {
		
	}
	
	public EventRunStop(String accountID, String deviceID, String address, String address1, String driverID,
			String driverName, String driverMessage, int timestamp, int statusCode, int statusLastingTime, int gpsAge,
			int numberOVSpeed, int numberldleTime, int totalldleTime, int totalDrivingTime, int numberOV4h,
			int cretationTime, int driverStatus, double latitude, double longitude, double latitude1, double longitude1,
			double speedKPH, double avgSpeedKPH, double maxSpeedKPH, double heading, double distanceKM,
			double odometerKM, double fuelLevel) {
		super();
		this.accountID = accountID;
		this.deviceID = deviceID;
		this.address = address;
		this.address1 = address1;
		this.driverID = driverID;
		this.driverName = driverName;
		this.driverMessage = driverMessage;
		this.timestamp = timestamp;
		this.statusCode = statusCode;
		this.statusLastingTime = statusLastingTime;
		this.gpsAge = gpsAge;
		this.numberOVSpeed = numberOVSpeed;
		this.numberldleTime = numberldleTime;
		this.totalldleTime = totalldleTime;
		this.totalDrivingTime = totalDrivingTime;
		this.numberOV4h = numberOV4h;
		this.cretationTime = cretationTime;
		this.driverStatus = driverStatus;
		this.latitude = latitude;
		this.longitude = longitude;
		this.latitude1 = latitude1;
		this.longitude1 = longitude1;
		this.speedKPH = speedKPH;
		this.avgSpeedKPH = avgSpeedKPH;
		this.maxSpeedKPH = maxSpeedKPH;
		this.heading = heading;
		this.distanceKM = distanceKM;
		this.odometerKM = odometerKM;
		this.fuelLevel = fuelLevel;
	}

	public EventRunStop(String accountID, String deviceID, String address, String address1, int timestamp,
			int statusCode, double latitude, double longitude, double latitude1, double longitude1, double avgSpeedKPH,
			double maxSpeedKPH, int statusLastingTime, double odometerKM) {
		super();
		this.accountID = accountID;
		this.deviceID = deviceID;
		this.address = address;
		this.address1 = address1;
		this.timestamp = timestamp;
		this.statusCode = statusCode;
		this.latitude = latitude;
		this.longitude = longitude;
		this.latitude1 = latitude1;
		this.longitude1 = longitude1;
		this.avgSpeedKPH = avgSpeedKPH;
		this.maxSpeedKPH = maxSpeedKPH;
		this.statusLastingTime = statusLastingTime;
		this.odometerKM = odometerKM;
	}

	public String getAccountID() {
		return accountID;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public String getAddress() {
		return address;
	}
	public String getAddress1() {
		return address1;
	}
	public String getDriverID() {
		return driverID;
	}
	public String getDriverName() {
		return driverName;
	}
	public String getDriverMessage() {
		return driverMessage;
	}
	public int getTimestamp() {
		return timestamp;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public int getStatusLastingTime() {
		return statusLastingTime;
	}
	public int getGpsAge() {
		return gpsAge;
	}
	public int getNumberOVSpeed() {
		return numberOVSpeed;
	}
	public int getNumberldleTime() {
		return numberldleTime;
	}
	public int getTotalldleTime() {
		return totalldleTime;
	}
	public int getTotalDrivingTime() {
		return totalDrivingTime;
	}
	public int getNumberOV4h() {
		return numberOV4h;
	}
	public int getCretationTime() {
		return cretationTime;
	}
	public int getDriverStatus() {
		return driverStatus;
	}
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public double getLongitude1() {
		return longitude1;
	}
	public double getSpeedKPH() {
		return speedKPH;
	}
	public double getAvgSpeedKPH() {
		return avgSpeedKPH;
	}
	public double getMaxSpeedKPH() {
		return maxSpeedKPH;
	}
	public double getHeading() {
		return heading;
	}
	public double getDistanceKM() {
		return distanceKM;
	}
	public double getOdometerKM() {
		return odometerKM;
	}
	public double getFuelLevel() {
		return fuelLevel;
	}
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public void setDriverID(String driverID) {
		this.driverID = driverID;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public void setDriverMessage(String driverMessage) {
		this.driverMessage = driverMessage;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public void setStatusLastingTime(int statusLastingTime) {
		this.statusLastingTime = statusLastingTime;
	}
	public void setGpsAge(int gpsAge) {
		this.gpsAge = gpsAge;
	}
	public void setNumberOVSpeed(int numberOVSpeed) {
		this.numberOVSpeed = numberOVSpeed;
	}
	public void setNumberldleTime(int numberldleTime) {
		this.numberldleTime = numberldleTime;
	}
	public void setTotalldleTime(int totalldleTime) {
		this.totalldleTime = totalldleTime;
	}
	public void setTotalDrivingTime(int totalDrivingTime) {
		this.totalDrivingTime = totalDrivingTime;
	}
	public void setNumberOV4h(int numberOV4h) {
		this.numberOV4h = numberOV4h;
	}
	public void setCretationTime(int cretationTime) {
		this.cretationTime = cretationTime;
	}
	public void setDriverStatus(int driverStatus) {
		this.driverStatus = driverStatus;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public void setLongitude1(double longitude1) {
		this.longitude1 = longitude1;
	}
	public void setSpeedKPH(double speedKPH) {
		this.speedKPH = speedKPH;
	}
	public void setAvgSpeedKPH(double avgSpeedKPH) {
		this.avgSpeedKPH = avgSpeedKPH;
	}
	public void setMaxSpeedKPH(double maxSpeedKPH) {
		this.maxSpeedKPH = maxSpeedKPH;
	}
	public void setHeading(double heading) {
		this.heading = heading;
	}
	public void setDistanceKM(double distanceKM) {
		this.distanceKM = distanceKM;
	}
	public void setOdometerKM(double odometerKM) {
		this.odometerKM = odometerKM;
	}
	public void setFuelLevel(double fuelLevel) {
		this.fuelLevel = fuelLevel;
	}

	public double getLatitude1() {
		return latitude1;
	}

	public void setLatitude1(double latitude1) {
		this.latitude1 = latitude1;
	}
	
}
