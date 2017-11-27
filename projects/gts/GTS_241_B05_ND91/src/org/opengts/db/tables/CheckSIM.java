package org.opengts.db.tables;

public class CheckSIM {
	String deviceID,Address,accountID,checkThongtin,phoneCX,simPhoneNumber,decription,imei,reportType;
	int timestamp;
	public CheckSIM(){}
	public CheckSIM(String _deviceID,int _timestamp,String _AccountID,String _checkThongtin,String _imei,
			String _phoneCX,String _simPhoneNumber,String _decription,String _reportType)
	{
		this.deviceID= _deviceID;
		this.accountID =_AccountID;
		this.timestamp =_timestamp;
		this.checkThongtin = _checkThongtin;
		this.imei=_imei;
		this.phoneCX = _phoneCX;
		this.simPhoneNumber = 	_simPhoneNumber;
		this.decription =_decription;
		this.reportType =_reportType;
		
	}
	public String getdeviceID()
	{
		return this.deviceID;
	}
	public  int gettimestamp() 
	{
	  return this.timestamp;
	}
	public String getaccountID()
	{
		return this.accountID;
	}
	public String getimei()
	{
		return this.imei;
	}
	public String getphoneCX()
	{
		return this.phoneCX;
	}
	public String getsimPhoneNumber()
	{
		return this.simPhoneNumber;
	}
	public String getdecription()
	{
		return this.decription;
	}
	public String getcheckThongtin()
	{
		return this.checkThongtin;
	}
	public String getreportType()
	{
		return this.reportType;
	}
}
