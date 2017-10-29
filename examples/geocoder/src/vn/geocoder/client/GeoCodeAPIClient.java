package vn.geocoder.client;

import vn.geocoder.exception.GeoCodeException;

interface GeoCodeAPIClient {

	public String getAddress(double latitude, double longitude) throws GeoCodeException;

	public void destroy();
}
