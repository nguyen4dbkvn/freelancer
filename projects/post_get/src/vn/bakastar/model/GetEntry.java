package vn.bakastar.model;

public class GetEntry extends PostEntry {

	private static final long serialVersionUID = 6963601704847025699L;

	protected long cua;
	protected long dieuHoa;
	protected String speed30s;
	protected String imei;

	@Override
	public String getAddress() {
		return null;
	}

	public void setCua(long cua) {
		this.cua = cua;
	}

	public long getCua() {
		return cua;
	}

	public void setDieuHoa(long dieuHoa) {
		this.dieuHoa = dieuHoa;
	}

	public long getDieuHoa() {
		return dieuHoa;
	}

	public void setSpeed30s(String speed30s) {
		this.speed30s = speed30s;
	}

	public String getSpeed30s() {
		return speed30s;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImei() {
		return imei;
	}

	@Override
	public String toString() {
		return String.format(
			"GetEntry[seq_ID=%d, accountID=%s, deviceID=%s, timeStamp=%d, statusCode=%d, statusLastingTime=%d, latitude=%f, longitude=%f, gpsAge=%d, speedKPH=%f, heading=%f, altitude=%f, address=%s, distanceKM=%f, odometerKM=%f, creationTime=%d, driverID=%s, driverStatus=%d, driverMessage=%s, Cua=%d, DieuHoa=%d, Speed30s=%s, params=%s, db_name=%s, imei=%s]",
			seq_ID, accountID, deviceID, timestamp, statusCode, 
			statusLastingTime, latitude, longitude, gpsAge, speedKPH,
			heading, altitude, getAddress(), distanceKM, odometerKM, creationTime, 
			driverID, driverStatus, driverMessage, cua, dieuHoa, speed30s, params, 
			db_name, imei);
	}
}
