package vn.geocoder.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import vn.geocoder.exception.GeoCodeException;

class GeoCodeAPIClientImpl implements GeoCodeAPIClient {

	GeoCodeAPIClientImpl() {

		PoolingHttpClientConnectionManager connectionManager = 
			new PoolingHttpClientConnectionManager(
				3000, TimeUnit.MILLISECONDS);
		connectionManager.setMaxTotal(200);
		connectionManager.setDefaultMaxPerRoute(100);

		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials =
			new UsernamePasswordCredentials(
				"vmg-geocode", "TruyvanGe0");
		provider.setCredentials(AuthScope.ANY, credentials);

		_httpClient = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.setDefaultCredentialsProvider(provider)
				.build();
	}

	@Override
	public void destroy() {
		try {
			if (_httpClient != null) {
				_httpClient.close();
			}
		}
		catch (IOException e) {

			e.printStackTrace();
		}

		_httpClient = null;
	}

	@Override
	public String getAddress(double latitude, double longitude) 
		throws GeoCodeException {

		System.out.println("----------------------------------");
		String address = null;

		try {
			address = getAddressByRoad(latitude, longitude);

			if (address == null || address.trim().length() == 0) {

				System.out.println( 
					String.format(
						"Not found location of (%f, %f) by QueryRoad", 
						latitude, longitude));

				address = getAddressByZone(latitude, longitude);
			}
		}
		catch (GeoCodeException e) {

			e.printStackTrace();

			address = getAddressByZone(latitude, longitude);
		}

		System.out.println("----------------------------------");
		return address;
	}

	protected String getAddressByRoad(double latitude, double longitude) 
		throws GeoCodeException {

		long start = System.currentTimeMillis();

		String response = null;

		try {
			URIBuilder builder = new URIBuilder(_getRoadURL)
				.addParameter(LATITUDE, String.valueOf(latitude))
				.addParameter(LONGITUDE, String.valueOf(longitude));

			URI uri = builder.build();

			response = doExcecute(uri);

			String address = GeoCodeResultHelper.getAddressByRoad(response);

			System.out.println(String.format(
					"Method[%s] - executedTime: %d ms - POINT(%f, %f): %s", 
					"QueryRoad", (System.currentTimeMillis() - start), 
					latitude, longitude, address));

			return address;
		}
		catch (URISyntaxException e) {
			throw new GeoCodeException(e);
		}
	}

	protected String getAddressByZone(double latitude, double longitude) 
		throws GeoCodeException {

		long start = System.currentTimeMillis();

		String response = null;

		try {
			URIBuilder builder = new URIBuilder(_getZoneURL)
				.addParameter(LATITUDE, String.valueOf(latitude))
				.addParameter(LONGITUDE, String.valueOf(longitude));

			URI uri = builder.build();

			response = doExcecute(uri);

			String address =  GeoCodeResultHelper.getAddressByZone(response);

			System.out.println(String.format(
					"Method[%s] - executedTime: %d ms - POINT(%f, %f): %s", 
					"QueryZone", (System.currentTimeMillis() - start), 
					latitude, longitude, address));

			return address;
		} 
		catch (URISyntaxException e) {
			throw new GeoCodeException(e);
		}
	}

	protected String doExcecute(URI uri) throws GeoCodeException {
		HttpGet httpGet = new HttpGet(uri);

		try {
			CloseableHttpResponse httpResponse = _httpClient.execute(httpGet);

			StatusLine statusLine = httpResponse.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode < 200 || statusCode >= 300) {
				String message = null;

				if (httpResponse.getEntity() != null) {
					HttpEntity resEntity = httpResponse.getEntity();

					message = EntityUtils.toString(resEntity, StandardCharsets.UTF_8);
				}

				throw new GeoCodeException(message, statusCode);
			}

			return EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
		} 
		catch (IOException e) {
			throw new GeoCodeException(
					"Unable to commuticate with GeoCode API", e);
		} 
		finally {
			if (httpGet != null)
				httpGet.releaseConnection();
		}
	}

	protected static final String LONGITUDE = "pos_lon";
	protected static final String LATITUDE = "pos_lat";

	protected CloseableHttpClient _httpClient;
	protected String _getRoadURL = "https://esi.vn/geocode/geo/road";
	protected String _getZoneURL = "https://esi.vn/geocode/geo/zone";
}
