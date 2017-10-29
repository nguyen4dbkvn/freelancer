package vn.bakasta.geocoder.model;

import java.io.Serializable;

public class GeoCoder implements Serializable {

	private static final long serialVersionUID = 1L;

	private long seqId;

	private double longitude;

	private double latitude;

	private String address;

	public long getSeqId() {
		return seqId;
	}

	public void setSeqId(long seqId) {
		this.seqId = seqId;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof GeoCoder) && (seqId > 0)
			? (seqId == ((GeoCoder) other).getSeqId())
			: (other == this);
	}

	@Override
	public int hashCode() {
		return (seqId > 0) 
			? (this.getClass().hashCode() + Long.valueOf(seqId).hashCode()) 
			:super.hashCode();
	}

	@Override
	public String toString() {
		return String.format("GeoCode[seq_ID=%d, lat=%f, lng=%f, address=%s]", 
				seqId, latitude, longitude, address);
	}
}
