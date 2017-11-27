package org.opengts.db.tables;

public class TongHopTheoLaiXe {
	public String driverID,deviceID,displayName,driverLC,decription,TenLoaiXe,Count510,Count1020,
	Count2035,Count35,KM510,KM1020,KM2035,KM35,TongKM,LienTuc4Gio,LienTuc10Gio;
	public TongHopTheoLaiXe(){}
	
	public TongHopTheoLaiXe(String driverID,String deviceID,String displayName,String driverLC,String decription,String Count510,String Count1020,String Count2035,
			String Count35,String KM510,String KM1020,String KM2035,String KM35,String TongKM,String LienTuc4Gio,String LienTuc10Gio) {
		this.deviceID=deviceID;
		this.displayName=displayName;
		this.driverLC=driverLC;
		this.decription=decription;		
		this.Count510=Count510;
		this.Count1020=Count1020;
		this.Count2035=Count2035;
		this.Count35=Count35;
		this.KM510=KM510;
		this.KM1020=KM1020;
		this.KM2035=KM2035;
		this.KM35=KM35;
		this.TongKM=TongKM;		
		this.LienTuc4Gio=LienTuc4Gio;		
		this.LienTuc10Gio=LienTuc10Gio;
		this.driverID=driverID;
	}
}
