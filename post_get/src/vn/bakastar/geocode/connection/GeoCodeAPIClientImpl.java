package vn.bakastar.geocode.connection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import vn.bakastar.exceptions.GeoCodeException;

public class GeoCodeAPIClientImpl implements GeoCodeAPIClient {

	public GeoCodeAPIClientImpl(TopicAPIConfiguration configuation) {

		_configuation = configuation;

		StringBuffer sb = new StringBuffer(4);

		sb.append("https://");
		sb.append(configuation.topicApiFQDN());
		if (configuation.topicApiPort() != 443) {
			sb.append(":");
			sb.append(configuation.topicApiPort());
		}
		_rootURL = sb.toString();
		
		PoolingHttpClientConnectionManager connectionManager = 
			new PoolingHttpClientConnectionManager(3000, TimeUnit.MILLISECONDS);
		connectionManager.setMaxTotal(200);
		connectionManager.setDefaultMaxPerRoute(100);

		_httpClient = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.build();
	}

	@Override
	public void destroy() {
		try {
			_httpClient.close();
		}
		catch (IOException e) {
			_logger.error("Unable to close client", e);
		}

		_httpClient = null;
		_configuation = null;
		_rootURL = null;
	}

	@Override
	public JSONObject certificateAuthentication(byte[] certFile) throws GeoCodeException {

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody(API_KEY, _configuation.topicApiApiKey());
		builder.addTextBody(SYSTEM_CD, _configuation.topicApiSystemCd());
		builder.addBinaryBody(CERT_FILE, certFile, ContentType.DEFAULT_BINARY, "certfile.txt");

		HttpEntity multipart = builder.build();

		String respone = doExcecute(
			_configuation.topicApiCertificationAuthentication(), multipart);

		JSONObject result;
		try {
			result = parseJSON(respone);
		} catch (JSONException e) {
			throw new TopicAPIException.CertificateAuthenticationFailure(
				"Invalid JSON response: " + respone);
		}

		String resultCode = result.getString(
			TopicAPIResponseParams.CertificateAuthentication.RESULT);

		if (Validator.isNull(resultCode) || !resultCode.equals("00")) {
			throw new TopicAPIException.CertificateAuthenticationFailure(
					respone, resultCode);
		}

		return result;
	}

	@Override
	public JSONObject authConfirm(String hash, int infoRequest) throws GeoCodeException {

		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair(API_KEY, _configuation.topicApiApiKey()));
		params.add(new BasicNameValuePair(SYSTEM_CD, _configuation.topicApiSystemCd()));
		params.add(new BasicNameValuePair(HASH, hash));
		params.add(new BasicNameValuePair(INFO_REQUEST, String.valueOf(infoRequest)));

		String respone = null;
		JSONObject result;

		try {
			HttpEntity entity = new UrlEncodedFormEntity(params);

			respone = doExcecute(
					_configuation.topicApiAuthConfirm(), entity);

			result = parseJSON(respone);

			String resultCode = result.getString(
				TopicAPIResponseParams.AuthConfirm.RESULT);

			if (Validator.isNull(resultCode) || !resultCode.equals("00")) {
				throw new TopicAPIException.AuthConfirmFailure(
						respone, resultCode);
			}

			return result;
		} catch (UnsupportedEncodingException e) {
			throw new TopicAPIException.AuthConfirmFailure(e);
		} catch (JSONException e) {
			throw new TopicAPIException.AuthConfirmFailure(
				"Invalid JSON response: " + respone);
		}
	}

	@Override
	public JSONObject signatureVerification(byte[] certFile, byte[] appFile,
			byte[] signFile) throws GeoCodeException {

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody(API_KEY, _configuation.topicApiApiKey());
		builder.addTextBody(SYSTEM_CD, _configuation.topicApiSystemCd());
		builder.addBinaryBody(CERT_FILE, certFile, ContentType.DEFAULT_BINARY, "certfile.txt");
		builder.addBinaryBody(APP_FILE, appFile, ContentType.DEFAULT_BINARY, "appfile.txt");
		builder.addBinaryBody(SIGN_FILE, signFile, ContentType.DEFAULT_BINARY, "signfile.txt");

		HttpEntity multipart = builder.build();

		String respone = doExcecute(
			_configuation.topicApiSignatureVerification(), multipart);

		JSONObject result;
		try {
			result = parseJSON(respone);
		} catch (JSONException e) {
			throw new TopicAPIException.SignatureVerificationFailure(
				"Invalid JSON response: " + respone);
		}

		String resultCode = result.getString(
			TopicAPIResponseParams.SignatureVerification.RESULT);

		if (Validator.isNull(resultCode) || !(resultCode.equals("00") 
				|| resultCode.equals("01"))) {
			throw new TopicAPIException.SignatureVerificationFailure(
					respone, resultCode);
		}

		return result;
	}

	protected String doExcecute(String apiContext, HttpEntity requestEntity) {
		HttpPost httpPost = new HttpPost(getApiURL(apiContext));

		try {
			httpPost.setEntity(requestEntity);

			CloseableHttpResponse httpResponse = _httpClient.execute(httpPost);

			StatusLine statusLine = httpResponse.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode >= 400) {
				String message = null;

				if (httpResponse.getEntity() != null) {
					HttpEntity resEntity = httpResponse.getEntity();

					message = EntityUtils.toString(resEntity, StandardCharsets.UTF_8);
				}

				throw new TopicAPIException.CommunicationFailure(message, statusCode);
			}

			return EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new TopicAPIException.CommunicationFailure(
					"Unable to commuticate with TOPIC API", e);
		} finally {
			httpPost.releaseConnection();
		}
	}

	protected String getApiURL(String context) {

		return (_rootURL + context);
	}

	protected static final String API_KEY = "api_key";
	protected static final String SYSTEM_CD = "system_cd";
	protected static final String CERT_FILE = "certfile";
	protected static final String APP_FILE = "appfile";
	protected static final String SIGN_FILE = "signfile";
	protected static final String HASH = "hash";
	protected static final String INFO_REQUEST = "inforequest";

	private CloseableHttpClient _httpClient;
	private TopicAPIConfiguration _configuation;
	private String _rootURL;

	private static final Log _logger = LogFactoryUtil.getLog(GeoCodeAPIClientImpl.class);
}
