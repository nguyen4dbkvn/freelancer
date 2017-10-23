package vn.bakastar.geocode.connection;

import vn.bakastar.exceptions.GeoCodeException;

interface GeoCodeAPIClient {

	public String getAddress(double latitude, double longitude)  throws GeoCodeException;

	public void destroy();
}
