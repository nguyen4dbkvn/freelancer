package org.opengts.db.tables;

public class DrivingTime {
	int startTime, stopTime,tgLai;
	double startLatitude, startLongitude, endLatitude, endLongitude, avgSpeed, maxSpeed, distance,qungduong,vttoida,vttb,fuelConsumption;
	String accountID, deviceID, startAddress, endAddress, displayNameDriver, descriptionDriver,driverID ;
	
	String HoTenLaiXe, SoGiayPhepLaiXe,LoaiHinhHoatDong,TocDoTBKhiQuaTocDo,TocDoChoPhep,Note="";
	public DrivingTime(){}
	
	public DrivingTime(String _accountId, String _deviceId, int _startTime, int _stopTime, String _startAddress,
			String _endAddress,double _distance, double _fuelConsumption){
		this.accountID = _accountId;
		this.deviceID = _deviceId;
		this.startTime = _startTime;
		this.stopTime = _stopTime;
		this.startAddress = _startAddress;
		this.endAddress = _endAddress;
		this.distance = _distance;
		this.fuelConsumption = _fuelConsumption;
	}
	
	public DrivingTime(String _accountId, String _deviceId, int _startTime, int _endTime, double _startLatitude,
			double _startLongitude, String _startAddress, double _endLatitude, 
			double _endLongitude, String _endAddress, double _avgSpeed, double _maxSpeed, double _distance ){
		this.accountID = _accountId;
		this.deviceID = _deviceId;
		this.startTime = _startTime;
		this.stopTime = _endTime;
		this.startLatitude = _startLatitude;
		this.startLongitude = _startLongitude;
		this.startAddress = _startAddress;
		this.endLatitude = _endLatitude;
		this.endLongitude = _endLongitude;
		this.endAddress = _endAddress;
		this.avgSpeed = _avgSpeed;
		this.maxSpeed = _maxSpeed;
		this.distance = _distance;
	}
	public DrivingTime(String _accountId, String _deviceId, int _startTime, int _endTime, double _startLatitude,
			double _startLongitude, String _startAddress, double _endLatitude, 
			double _endLongitude, String _endAddress, double _avgSpeed, double _maxSpeed, double _distance, String _displayNameDriver, String  _descriptionDriver){
		this.accountID = _accountId;
		this.deviceID = _deviceId;
		this.startTime = _startTime;
		this.stopTime = _endTime;
		this.startLatitude = _startLatitude;
		this.startLongitude = _startLongitude;
		this.startAddress = _startAddress;
		this.endLatitude = _endLatitude;
		this.endLongitude = _endLongitude;
		this.endAddress = _endAddress;
		this.avgSpeed = _avgSpeed;
		this.maxSpeed = _maxSpeed;
		this.distance = _distance;
		this.displayNameDriver= _displayNameDriver;
		this.descriptionDriver = _descriptionDriver;
	}
	
	public DrivingTime(String _accountId, String _deviceId, int _startTime, int _endTime, double _startLatitude,
			double _startLongitude, String _startAddress, double _endLatitude, 
			double _endLongitude, String _endAddress, double _avgSpeed, double _maxSpeed, double _distance, String _displayNameDriver, String  _descriptionDriver,String SoGiayPhepLaiXe, String LoaiHinhHoatDong){
		this.accountID = _accountId;
		this.deviceID = _deviceId;
		this.startTime = _startTime;
		this.stopTime = _endTime;
		this.startLatitude = _startLatitude;
		this.startLongitude = _startLongitude;
		this.startAddress = _startAddress;
		this.endLatitude = _endLatitude;
		this.endLongitude = _endLongitude;
		this.endAddress = _endAddress;
		this.avgSpeed = _avgSpeed;
		this.maxSpeed = _maxSpeed;
		this.distance = _distance;
		this.displayNameDriver= _displayNameDriver;
		this.descriptionDriver = _descriptionDriver;
		
		this.SoGiayPhepLaiXe=SoGiayPhepLaiXe;
		this.LoaiHinhHoatDong=LoaiHinhHoatDong;
	}
	
 
	public DrivingTime(double _quangduong,double _vttoida,double _vttb,int _tgLai)
	{
		this.qungduong =_quangduong;
		this.vttoida=_vttoida;
		this.vttb = _vttb;
		this.tgLai = _tgLai;
	}
	
	
	public String getNote()
	{
		return this.Note;
	}
	
	public String getTocDoChoPhep()
	{
		return this.TocDoChoPhep;
	}
	
	public String getTocDoTBKhiQuaTocDo()
	{
		return this.TocDoTBKhiQuaTocDo;
	}
	
	public String getLoaiHinhHoatDong()
	{
		return this.LoaiHinhHoatDong;
	}
	
	public String getSoGiayPhepLaiXe()
	{
		return this.SoGiayPhepLaiXe;
	}
	
	public String getHoTenLaiXe()
	{
		return this.HoTenLaiXe;
	}
	
	
	public String getAccountID() {
		return this.accountID;
	}
	public String getDeviceID() {
		return this.deviceID;
	}
	public String getStartAddress() {
		return this.startAddress;
	}
	public String getEndAddress() {
		return this.endAddress;
	}
	public int getStartTime() {
		return this.startTime;
	}
	public int getStopTime() {
		return this.stopTime;
	}
	
	public double getStartLatitude() {
		return this.startLatitude;
	}
	public double getStartLongitude() {
		return this.startLongitude;
	}
	public double getEndLatitude() {
		return this.endLatitude;
	}
	public double getEndLongitude() {
		return this.endLongitude;
	}
	public double getAvgSpeed() {
		return this.avgSpeed;
	}
	public double getMaxSpeed() {
		return this.maxSpeed;
	}
	public double getDistance() {
		return this.distance;
	}
	public double getquangduong() {
		return this.qungduong;
	}
	public double getvttoida() {
		return this.vttoida;
	}
	public double getvttb() {
		return this.vttb;
	}
	public double gettgLai() {
		return this.tgLai;
	}
	
	public String getDisplayNameDriver() {
		return this.displayNameDriver;
	}
	
	public String getDescriptionDriver() {
		return this.descriptionDriver;
	}
	public String getDriverID(){
		return this.driverID;
	}
	public double getFuelConsumption(){
		return this.fuelConsumption;
	}
}
