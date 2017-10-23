package vn.bakastar.geocode.connection;

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

import vn.bakastar.exceptions.GeoCodeException;
import vn.bakastar.util.Logger;
import vn.bakastar.util.PropsValue;

class GeoCodeAPIClientImpl implements GeoCodeAPIClient {

	GeoCodeAPIClientImpl() {

		_getRoadURL = PropsValue.GEOCODE_API_FQDN + PropsValue.GEOCODE_API_GET_ROAD_CONTEXT;
		_getZoneURL = PropsValue.GEOCODE_API_FQDN + PropsValue.GEOCODE_API_GET_ZONE_CONTEXT;

		PoolingHttpClientConnectionManager connectionManager = 
			new PoolingHttpClientConnectionManager(
				PropsValue.GEOCODE_API_TIME_TO_LIVE, TimeUnit.MILLISECONDS);
		connectionManager.setMaxTotal(200);
		connectionManager.setDefaultMaxPerRoute(100);

		CredentialsProvider provider = new BasicCredentialsProvider();
		UsernamePasswordCredentials credentials =
			new UsernamePasswordCredentials(
				PropsValue.GEOCODE_API_USERNAME, PropsValue.GEOCODE_API_PASSWORD);
		provider.setCredentials(AuthScope.ANY, credentials);

		_httpClient = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.setDefaultCredentialsProvider(provider)
				.build();
	}

	@Override
	public void destroy() {
		try {
			_httpClient.close();
		}
		catch (IOException e) {

			Logger.log(getClass().getName(), "Unable to close client", e);
		}

		_httpClient = null;
	}

	@Override
	public String getAddress(double latitude, double longitude) 
		throws GeoCodeException {

		String address = null;

		try {
			address = getAddressByRoad(latitude, longitude);

			if (address == null || address.trim().length() == 0) {

				address = getAddressByZone(latitude, longitude);
			}
		}
		catch (GeoCodeException e) {

			Logger.log(getClass().getName(), e);

			address = getAddressByZone(latitude, longitude);
		}

		return address;
	}

	protected String getAddressByRoad(double latitude, double longitude) 
		throws GeoCodeException {

		String response = null;

		try {
			URIBuilder builder = new URIBuilder(_getRoadURL)
				.addParameter(LATITUDE, String.valueOf(latitude))
				.addParameter(LONGITUDE, String.valueOf(longitude));

			URI uri = builder.build();

			response = doExcecute(uri);

			return GeoCodeResultHelper.getAddressByRoad(response);
		}
		catch (URISyntaxException e) {
			throw new GeoCodeException(e);
		}
	}

	protected String getAddressByZone(double latitude, double longitude) 
		throws GeoCodeException {

		String response = null;

		try {
			URIBuilder builder = new URIBuilder(_getZoneURL)
				.addParameter(LATITUDE, String.valueOf(latitude))
				.addParameter(LONGITUDE, String.valueOf(longitude));

			URI uri = builder.build();

			response = doExcecute(uri);

			return GeoCodeResultHelper.getAddressByZone(response);
		} 
		catch (URISyntaxException e) {
			throw new GeoCodeException(e);
		}
	}

	protected String doExcecute(URI uri) throws GeoCodeException {
		long start = System.currentTimeMillis();
		
		HttpGet httpGet = new HttpGet(uri);

		try {
			System.out.println("excecuting: " + uri.toString());
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
			System.out.println("excecuted: " + (System.currentTimeMillis() - start) + "ms");

			return EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new GeoCodeException(
					"Unable to commuticate with GeoCode API", e);
		} finally {
			if (httpGet != null)
				httpGet.releaseConnection();
		}
	}

	protected static final String LONGITUDE = "pos_lon";
	protected static final String LATITUDE = "pos_lat";

	protected CloseableHttpClient _httpClient;
	protected String _getRoadURL;
	protected String _getZoneURL;
}
