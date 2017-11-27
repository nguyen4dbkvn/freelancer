package org.opengts.db.tables;

public class VuotToc {
	String deviceID,accountID,address,endaddress;
	int startTime,stopTime,num;
	double startSpeed,endSpeed,maxSpeed,latitude,longitude,endLat,endLon;
	
	String HoTenLaiXe, SoGiayPhepLaiXe,LoaiHinhHoatDong,TocDoTBKhiQuaTocDo,TocDoChoPhep,Note;
	
	public VuotToc()
	{}
	public  VuotToc(String _deviceID,int _startTime,int _stopTime,double _startSpeed,double _endSpeed,double _maxSpeed,double _lat,double _lon,int _num,
			String _address,String _accountID,String _endaddress, double _endlat,double _endlon)
	{
	   this.deviceID  = _deviceID;
	   this.startTime = _startTime;
	   this.stopTime = _stopTime;
	   this.startSpeed = _startSpeed;
	   this.endSpeed = _endSpeed;
	   this.maxSpeed = _maxSpeed;
	   this.latitude = _lat;
	   this.longitude=_lon;
	   this.num= _num;
	   this.address = _address;
	   this.accountID = _accountID;
	   this.endaddress = _endaddress;
	   this.endLat = _endlat;
	   this.endLon = _endlon;
	}
	
	public  VuotToc(String deviceID,String HoTenLaiXe,String SoGiayPhepLaiXe, String LoaiHinhHoatDong, int ThoiDiem,String TocDoTBKhiQuaTocDo, String TocDoChoPhep,
		    double _lat,double _lon,String _address,String note)
	{
	   this.deviceID  = deviceID;
	   this.HoTenLaiXe = HoTenLaiXe;
	   this.SoGiayPhepLaiXe = SoGiayPhepLaiXe;
	   this.LoaiHinhHoatDong = LoaiHinhHoatDong;
	   this.TocDoTBKhiQuaTocDo = TocDoTBKhiQuaTocDo;
	   this.TocDoChoPhep = TocDoChoPhep;
	   this.latitude = _lat;
	   this.longitude=_lon;
	   this.Note= note;
	   this.address = _address;
	   this.startTime = ThoiDiem;
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
	
	public String getdeviceID()
	{
		return this.deviceID;
	}
	public int getstartTime()
	{
		return this.startTime;
	}
	public int getstopTime()
	{
		return this.stopTime;
	}
	public double getstartSpeed()
	{
		return this.startSpeed;
	}
	public double getendSpeed()
	{
		return this.endSpeed;
	}
	public double getmaxSpeed()
	{
		return this.maxSpeed;
	}
	public double getlat()
	{
		return this.latitude;
	}
	public double getlon()
	{
		return this.longitude;
	}
	public int getnum()
	{
		return this.num;
	}
	public String getaddress()
	{
		return this.address;
	}
	public String getaccountID()
	{
		return this.accountID;
	}
	public String getendaddress()
	{
		return this.endaddress;
	}
	public double getendlat()
	{
		return this.endLat;
	}
	public double getendlon()
	{
		return this.endLon;
	}
}
