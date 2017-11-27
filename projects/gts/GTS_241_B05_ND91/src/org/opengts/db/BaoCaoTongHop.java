package org.opengts.db;

public class BaoCaoTongHop {
	public Integer slvt = 0, dungDo = 0, tgd = 0, moCua = 0, lxlt = 0, tgl = 0;
	public double quangDuong = 0, vttd = 0, vttb = 0;
	public String driverId="";
	public BaoCaoTongHop(){};
	
	public BaoCaoTongHop(String driverId,Integer slvt, Integer dungDo, Integer tgd,
			Integer moCua, Integer lxlt, double quangDuong, double vttd,
			double vttb, Integer tgl) {
		this.slvt = slvt;
		this.dungDo = dungDo;
		this.tgd = tgd;
		this.moCua = moCua;
		this.lxlt = lxlt;
		this.quangDuong = quangDuong;
		this.vttd = vttd;
		this.vttb=vttb;
		this.tgl = tgl;	 
		this.driverId=driverId;
	}
}
