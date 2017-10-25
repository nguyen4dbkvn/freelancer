package vn.bakastar.model;

import java.io.Serializable;

import vn.bakastar.geocoder.mapxtreme.GeoCodeAPIHelper;

public class PostEntry implements Serializable {

	private static final long serialVersionUID = 4727507474252873626L;

	protected Long primaryKey;
	protected long seq_ID;
	protected String accountID;
	protected String deviceID;
	protected long timestamp;
	protected long statusCode;
	protected long statusLastingTime;
	protected double latitude;
	protected double longitude;
	protected long gpsAge;
	protected double speedKPH;
	protected double heading;
	protected double altitude;
	protected double distanceKM;
	protected double odometerKM;
	protected long creationTime;
	protected String driverID;
	protected long driverStatus;
	protected String driverMessage;
	protected String params;
	protected String db_name;

	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	public Long getPrimaryKey() {
		return primaryKey;
	}

	public void setSeqID(long seqID) {
		this.seq_ID = seqID;
		setPrimaryKey(seqID);
	}

	public long getSeqID() {
		return seq_ID;
	}

	public void setAccountID(String accountId) {
		this.accountID = accountId;
	}

	public String getAccountID() {
		return accountID;
	}

	public void setDeviceID(String deviceId) {
		this.deviceID = deviceId;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setStatusCode(long statusCode) {
		this.statusCode = statusCode;
	}

	public long getStatusCode() {
		return statusCode;
	}

	public void setStatusLastingTime(long statusLastingTime) {
		this.statusLastingTime = statusLastingTime;
	}

	public long getStatusLastingTime() {
		return statusLastingTime;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setGpsAge(long gpsAge) {
		this.gpsAge = gpsAge;
	}

	public long getGpsAge() {
		return gpsAge;
	}

	public void setSpeedKPH(double speedKph) {
		this.speedKPH = speedKph;
	}

	public double getSpeedKPH() {
		return speedKPH;
	}

	public void setHeading(double heading) {
		this.heading = heading;
	}

	public double getHeading() {
		return heading;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public String getAddress() {
		return GeoCodeAPIHelper.getAddress(latitude, longitude);
	}

	public void setDistanceKM(double distanceKm) {
		this.distanceKM = distanceKm;
	}

	public double getDistanceKM() {
		return distanceKM;
	}

	public void setOdometerKM(double odometerKM) {
		this.odometerKM = odometerKM;
	}

	public double getOdometerKM() {
		return odometerKM;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setDriverID(String driverId) {
		this.driverID = driverId;
	}

	public String getDriverID() {
		return driverID;
	}

	public void setDriverStatus(long driverStatus) {
		this.driverStatus = driverStatus;
	}

	public long getDriverStatus() {
		return driverStatus;
	}

	public void setDriverMessage(String driverMessage) {
		this.driverMessage = driverMessage;
	}

	public String getDriverMessage() {
		return driverMessage;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getParams() {
		return params;
	}

	public void setDBName(String dbName) {
		this.db_name = dbName;
	}

	public String getDBName() {
		return db_name;
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof PostEntry) && (primaryKey != null)
			? primaryKey.equals(((PostEntry) other).primaryKey)
			: (other == this);
	}

	@Override
	public int hashCode() {
		return (primaryKey != null) 
			? (this.getClass().hashCode() + primaryKey.hashCode())
			: super.hashCode();
	}

	@Override
	public String toString() {
		return String.format(
			"PostEntry[seq_ID=%d, accountID=%s, deviceID=%s, timeStamp=%d, statusCode=%d, statusLastingTime=%d, latitude=%f, longitude=%f, gpsAge=%d, speedKPH=%f, heading=%f, altitude=%f, address=%s, distanceKM=%f, odometerKM=%f, creationTime=%d, driverID=%s, driverStatus=%d, driverMessage=%s, params=%s, db_name=%s]",
			seq_ID, accountID, deviceID, timestamp, statusCode, 
			statusLastingTime, latitude, longitude, gpsAge, speedKPH,
			heading, altitude, getAddress(), distanceKM, odometerKM, creationTime, 
			driverID, driverStatus, driverMessage, params, db_name);
	}
}
