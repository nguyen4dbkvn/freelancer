package org.opengts.db;

public class TinHieuCau {

	public Integer DieuHoa = 0, timestamp = 0;
 
	public String accountID="",deviceID="",address="";
	public TinHieuCau(){};
	
	public TinHieuCau(String accountID,String deviceID, String address, Integer DieuHoa,Integer timestamp) {
		this.accountID = accountID;
		this.deviceID = deviceID;
		this.address = address;
		this.DieuHoa = DieuHoa;
		this.timestamp = timestamp;		 
	}
}
