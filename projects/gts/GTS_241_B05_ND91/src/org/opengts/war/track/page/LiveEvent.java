package org.opengts.war.track.page;

public class LiveEvent {
	String driverID,StartAddress, EndAddress;
	long LiveTime, StartTime,EndTime;
	double AltitudeStart,AltitudeEnd,tieuThu;
	public LiveEvent() {
		// TODO Auto-generated constructor stub
	}
	public LiveEvent(String driverID_, long StartTime_ ,long EndTime_ ,String StartAddress_, String EndAddress_ ,long LiveTime_,double AltitudeStart_,double AltitudeEnd_) {
		this.driverID = driverID_;
		this.StartTime = StartTime_;
		this.EndTime = EndTime_;
		this.StartAddress = StartAddress_;
		this.EndAddress = EndAddress_;
		this.LiveTime = LiveTime_;
		this.AltitudeStart=AltitudeStart_;
		this.AltitudeEnd=AltitudeEnd_;
	} 
	public LiveEvent(String driverID_, long StartTime_ ,long EndTime_ ,String StartAddress_, String EndAddress_ ,long LiveTime_,double AltitudeStart_,double AltitudeEnd_,double tieuThu) {
		this.driverID = driverID_;
		this.StartTime = StartTime_;
		this.EndTime = EndTime_;
		this.StartAddress = StartAddress_;
		this.EndAddress = EndAddress_;
		this.LiveTime = LiveTime_;
		this.AltitudeStart=AltitudeStart_;
		this.AltitudeEnd=AltitudeEnd_;
		this.tieuThu=tieuThu;
	} 
	public String getDriver(){
		return this.driverID;
	}
	public long getStartTime(){
		return this.StartTime;
	}
	public double TieuThu(){
		return this.tieuThu;
	}
	public double getTieuThu(){
		return this.tieuThu;
	}
	public long getEndTime(){
		return this.EndTime;
	}
	public String getStartAdress(){
		return this.StartAddress;
	}	
	public String getEndAdress(){
		return this.EndAddress;
	}
	public long getLiveTime(){
		return this.LiveTime;
	}
	public double getAltitudeStart(){
		return this.AltitudeStart;
	}
	public double getAltitudeEnd(){
		return this.AltitudeEnd;
	}
}
