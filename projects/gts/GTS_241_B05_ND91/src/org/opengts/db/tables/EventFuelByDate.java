package org.opengts.db.tables;

public class EventFuelByDate {
	String accountID ;
	String deviceID ;
	int timeBegin;
	int timeEnd;
	int statusCode;
	double odometerKM;
	double distanceKM;
	int creationTime;
	double fuelBegin;
	double fuelEnd;
	double fuelAdd;
	double fuelConsumption;
	int drivingTime;
	String addressBegin;
	String addressEnd;
	/**
	 * @param accountID
	 * @param deviceID
	 * @param timeBegin
	 * @param timeEnd
	 * @param statusCode
	 * @param odometerKM
	 * @param distanceKM;
	 * @param creationTime
	 * @param fuelBegin
	 * @param fuelEnd
	 * @param fuelAdd
	 * @param fuelConsumption
	 * @param drivingTime
	 * @param addressBegin
	 * @param addressEnd
	 */
	public EventFuelByDate(String accountID, String deviceID, int timeBegin, int timeEnd, int statusCode,
			double odometerKM, double distanceKM, int creationTime, double fuelBegin, double fuelEnd, double fuelAdd,
			double fuelConsumption, int drivingTime, String addressBegin, String addressEnd) {
		super();
		this.accountID = accountID;
		this.deviceID = deviceID;
		this.timeBegin = timeBegin;
		this.timeEnd = timeEnd;
		this.statusCode = statusCode;
		this.odometerKM = odometerKM;
		this.distanceKM = distanceKM;
		this.creationTime = creationTime;
		this.fuelBegin = fuelBegin;
		this.fuelEnd = fuelEnd;
		this.fuelAdd = fuelAdd;
		this.fuelConsumption = fuelConsumption;
		this.drivingTime = drivingTime;
		this.addressBegin = addressBegin;
		this.addressEnd = addressEnd;
	}
	
	/**
	 * @param accountID
	 * @param deviceID
	 * @param timeBegin
	 * @param timeEnd
	 * @param statusCode
	 * @param fuelBegin
	 * @param fuelEnd
	 * @param drivingTime
	 * @param addressBegin
	 */
	//dung cho bao cao dau theo thoi gian dung do
	public EventFuelByDate(String accountID, String deviceID, int timeBegin, int timeEnd, double distanceKM, int statusCode, double fuelBegin,
			double fuelEnd, int drivingTime, String addressBegin) {
		super();
		this.accountID = accountID;
		this.deviceID = deviceID;
		this.timeBegin = timeBegin;
		this.timeEnd = timeEnd;
		this.distanceKM = distanceKM;
		this.statusCode = statusCode;
		this.fuelBegin = fuelBegin;
		this.fuelEnd = fuelEnd;
		this.drivingTime = drivingTime;
		this.addressBegin = addressBegin;		
	}

	public String getAccountID() {
		return accountID;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public int getTimeBegin() {
		return timeBegin;
	}
	public int getTimeEnd() {
		return timeEnd;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public double getOdometerKM() {
		return odometerKM;
	}
	public int getCreationTime() {
		return creationTime;
	}
	public double getFuelBegin() {
		return fuelBegin;
	}
	public double getFuelEnd() {
		return fuelEnd;
	}
	public double getFuelAdd() {
		return fuelAdd;
	}
	public double getFuelConsumption() {
		return fuelConsumption;
	}
	public int getDrivingTime() {
		return drivingTime;
	}
	public String getAddressBegin() {
		return addressBegin;
	}
	public String getAddressEnd() {
		return addressEnd;
	}
	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public void setTimeBegin(int timeBegin) {
		this.timeBegin = timeBegin;
	}
	public void setTimeEnd(int timeEnd) {
		this.timeEnd = timeEnd;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public void setOdometerKM(double odometerKM) {
		this.odometerKM = odometerKM;
	}
	public void setCreationTime(int creationTime) {
		this.creationTime = creationTime;
	}
	public void setFuelBegin(double fuelBegin) {
		this.fuelBegin = fuelBegin;
	}
	public void setFuelEnd(double fuelEnd) {
		this.fuelEnd = fuelEnd;
	}
	public void setFuelAdd(double fuelAdd) {
		this.fuelAdd = fuelAdd;
	}
	public void setFuelConsumption(double fuelConsumption) {
		this.fuelConsumption = fuelConsumption;
	}
	public void setDrivingTime(int drivingTime) {
		this.drivingTime = drivingTime;
	}
	public void setAddressBegin(String addressBegin) {
		this.addressBegin = addressBegin;
	}
	public void setAddressEnd(String addressEnd) {
		this.addressEnd = addressEnd;
	}

	public double getDistanceKM() {
		return distanceKM;
	}

	public void setDistanceKM(double distanceKM) {
		this.distanceKM = distanceKM;
	}
	
}
