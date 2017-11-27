package org.opengts.db.tables;

public class ReportDate {
	String device;
	int quangduong;
	int count_quatoc;
	double avgSpeed;
	double maxSpeed;
	int count_mocua;
	int count_dungdo;
	int timedung;
	int timedriving;
	public ReportDate() {
	}
	/**
	 * @param device
	 * @param quangduong
	 * @param count_quatoc
	 * @param avgSpeed
	 * @param maxSpeed
	 * @param count_mocua
	 * @param count_dungdo
	 * @param timedung
	 * @param timedriving
	 */
	public ReportDate(int count_dungdo, int timedung, double avgSpeed, double maxSpeed, int timedriving){
		this.count_dungdo = count_dungdo;
		this.timedung = timedung;
		this.avgSpeed = avgSpeed;
		this.maxSpeed = maxSpeed;
		this.timedriving = timedriving;
	}
	public ReportDate(String device, int quangduong, int count_quatoc, double avgSpeed, double maxSpeed,
			int count_mocua, int count_dungdo, int timedung, int timedriving) {
		this.device = device;
		this.quangduong = quangduong;
		this.count_quatoc = count_quatoc;
		this.avgSpeed = avgSpeed;
		this.maxSpeed = maxSpeed;
		this.count_mocua = count_mocua;
		this.count_dungdo = count_dungdo;
		this.timedung = timedung;
		this.timedriving = timedriving;
	}
	public String getDevice() {
		return device;
	}
	public int getQuangduong() {
		return quangduong;
	}
	public int getCount_quatoc() {
		return count_quatoc;
	}
	public double getAvgSpeed() {
		return avgSpeed;
	}
	public double getMaxSpeed() {
		return maxSpeed;
	}
	public int getCount_mocua() {
		return count_mocua;
	}
	public int getCount_dungdo() {
		return count_dungdo;
	}
	public int getTimedung() {
		return timedung;
	}
	public int getTimedriving() {
		return timedriving;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public void setQuangduong(int quangduong) {
		this.quangduong = quangduong;
	}
	public void setCount_quatoc(int count_quatoc) {
		this.count_quatoc = count_quatoc;
	}
	public void setAvgSpeed(double avgSpeed) {
		this.avgSpeed = avgSpeed;
	}
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public void setCount_mocua(int count_mocua) {
		this.count_mocua = count_mocua;
	}
	public void setCount_dungdo(int count_dungdo) {
		this.count_dungdo = count_dungdo;
	}
	public void setTimedung(int timedung) {
		this.timedung = timedung;
	}
	public void setTimedriving(int timedriving) {
		this.timedriving = timedriving;
	}
	
}
