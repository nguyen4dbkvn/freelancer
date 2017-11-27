package org.opengts.db.tables;

public class MoCua {
	String deviceID,Address,accountID,endaddress;
	double latitude,longitude,endLat,endLon;
	int startTime,endTime;
    public MoCua() {
		// TODO Auto-generated constructor stub
	}
	public MoCua(String _deviceID,int _startTime,int _endTime,double _latitude,double _longitude,String _address,String _AccountID,String _endaddress,double _endLat,double _endLon)
	{
		this.deviceID= _deviceID;
		this.startTime =_startTime;
		this.endTime = _endTime;
		this.latitude=_latitude;
		this.longitude = _longitude;
		this.Address = _address;
		this.accountID = _AccountID;
		this.endaddress =_endaddress;
		this.endLat = _endLat;
		this.endLon = _endLon;
	}
	public String getdeviceID()
	{
		return this.deviceID;
	}
	public  int getstartTime() 
	{
	  return this.startTime;
	}
	public int getendTime()
	{
		return this.endTime;
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
	public String getaccountID()
	{
		return this.accountID;
	}
	public String getendaddress()
	{
		return this.endaddress;
	}
	public double getendLat()
	{
		return this.endLat;
	}
	public double getendLon()
	{
		return this.endLon;
	}
}
