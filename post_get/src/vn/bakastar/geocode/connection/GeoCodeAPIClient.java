package vn.bakastar.geocode.connection;

import vn.bakastar.exceptions.GeoCodeException;

public interface GeoCodeAPIClient {
	
	public JSONObject certificateAuthentication(byte[] certFile)
		throws GeoCodeException;

	public JSONObject authConfirm(String hash, int infoRequest) 
		throws GeoCodeException;

	public JSONObject signatureVerification( 
		byte[] certFile, byte[] appFile, byte[] signFile)
		throws GeoCodeException;

	public void destroy();
}
