package vn.bakastar.model;

import java.util.Date;

import vn.bakastar.db.DAOUtil;

public class ImeiEntry {

	private Date dtServer;		// dt_server
	private Date dtTracker;		// dt_tracker
	private double latitude;	// lat
	private double longitude;	// lng
	private double altitude;	// altitude
	private double angle;		// angle
	private double speed;		// speed
	private String params;		// params

	public ImeiEntry() {
		
	}

	public ImeiEntry(GetEntry getEntry) {
		
		dtServer = new Date();
		dtTracker = new Date(getEntry.getTimestamp() * 1000);
		latitude = getEntry.getLatitude();
		longitude = getEntry.getLongitude();
		altitude = getEntry.getAltitude();
		angle = getEntry.getHeading();
		speed = getEntry.getSpeedKPH();
		params = getEntry.getParams();
	}

	public void setDtServer(Date dtServer) {
		this.dtServer = dtServer;
	}

	public Date getDtServer() {
		return dtServer;
	}

	public void setDtTracker(Date dtTracker) {
		this.dtTracker = dtTracker;
	}

	public void setDtTracker(long timestamp) {
		this.dtTracker = new Date(timestamp * 1000);
	}

	public Date getDtTracker() {
		return dtTracker;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public double getAngle() {
		return angle;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getSpeed() {
		return speed;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getParams() {
		return params;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.toString().hashCode();
	}

	@Override
	public String toString() {

		return String.format("ImeiEntry[dt_server=%s, dt_tracker=%s, lat=%f, lng=%f, altitude=%f, angle=%f, speed=%f, params=%s]", 
			DAOUtil.format(dtServer), DAOUtil.format(dtTracker), 
			latitude, longitude, 
			altitude, angle, speed, params);
	}
}
