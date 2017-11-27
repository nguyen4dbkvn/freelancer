package org.opengts.db.tables;

public class dungdo {
	String deviceID,Address,accountID;
	double latitude,longitude;
	int timestamp,duration,type,num;
	
	String HoTenLaiXe, SoGiayPhepLaiXe,LoaiHinhHoatDong,TocDoTBKhiQuaTocDo,TocDoChoPhep,Note="";
	
	public dungdo(){}
	public dungdo(String _deviceID,int _timestamp,int _duration,int _type,double _latitude,double _longitude,String _address,int _num,String _AccountID)
	{
		this.deviceID= _deviceID;
		this.timestamp =_timestamp;
		this.duration = _duration;
		this.type=_type;
		this.latitude=_latitude;
		this.longitude = _longitude;
		this.Address = _address;
		this.num = _num;
		this.accountID =_AccountID;
		
	}
	public dungdo(String _deviceID,int _timestamp,int _duration,String _type,double _latitude,double _longitude,String _address,String HoTenLaiXe,String SoGiayPhepLaiXe,String decription)
	{
		this.deviceID= _deviceID;
		this.timestamp =_timestamp;
		this.duration = _duration;
		this.LoaiHinhHoatDong= _type +"";
		this.latitude=_latitude;
		this.longitude = _longitude;
		this.Address = _address;
		this.HoTenLaiXe = HoTenLaiXe;
		this.SoGiayPhepLaiXe =SoGiayPhepLaiXe;		
		this.Note=decription;
	}
	public dungdo(String _deviceID,int _timestamp,int _duration,String _type,double _latitude,double _longitude,String _address,String HoTenLaiXe,String SoGiayPhepLaiXe)
	{
		this.deviceID= _deviceID;
		this.timestamp =_timestamp;
		this.duration = _duration;
		this.LoaiHinhHoatDong= _type +"";
		this.latitude=_latitude;
		this.longitude = _longitude;
		this.Address = _address;
		this.HoTenLaiXe = HoTenLaiXe;
		this.SoGiayPhepLaiXe =SoGiayPhepLaiXe;		
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
	public  int gettimestamp() 
	{
	  return this.timestamp;
	}
	public int getduration()
	{
		return this.duration;
	}
	public int gettype()
	{
		return this.duration;
	}
	public double getlatitude()
	{
		return this.latitude;
	}
	public double getlongitude()
	{
		return this.longitude;
	}
	public String getaddress()
	{
		return this.Address;
	}
	public int getnum()
	{
		return this.num;
	}
	public String getaccountID()
	{
		return this.accountID;
	}
}
