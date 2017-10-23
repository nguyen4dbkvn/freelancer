package vn.bakastar.geocode.connection;

import vn.bakastar.exceptions.GeoCodeException;

public class GeoCodeAPIHelper {

	public static GeoCodeAPIClient getGeoCodeAPIClient() {
		if (_client == null) {
			_client = new GeoCodeAPIClientImpl();
		}

		return _client;
	}

	public static String getAddress(double latitude, double longitude) 
		throws GeoCodeException {

		return getGeoCodeAPIClient().getAddress(latitude, longitude);
	}

	public static void release() {

		getGeoCodeAPIClient().destroy();
	}

	private static GeoCodeAPIClient _client;
}
