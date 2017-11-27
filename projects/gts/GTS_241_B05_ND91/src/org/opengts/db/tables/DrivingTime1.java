package org.opengts.db.tables;

public class DrivingTime1 {
int tgLai;
double quangduong,vttoida,vttb;

public DrivingTime1(){}

public DrivingTime1(double _quangduong,double _vttoida,double _vttb,int _tgLai)
{
	this.quangduong =_quangduong;
	this.vttoida=_vttoida;
	this.vttb = _vttb;
	this.tgLai = _tgLai;
}
public double getquangduong() {
	return this.quangduong;
}
public double getvttoida() {
	return this.vttoida;
}
public double getvttb() {
	return this.vttb;
}
public int gettgLai() {
	return this.tgLai;
}
}
