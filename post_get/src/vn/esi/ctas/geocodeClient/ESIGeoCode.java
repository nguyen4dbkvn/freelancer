package vn.esi.ctas.geocodeClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class ESIGeoCode
{
  public static final String PROP_URL = "http.url";
  public static final String PROP_USERNAME = "http.username";
  public static final String PROP_PASSWORD = "http.password";
  public static final String PROP_CONNECTION_POOL = "http.connection.pool";
  public static final String PROP_CONNECT_TIMEOUT = "http.timeout.connect";
  public static final String PROP_READ_TIMEOUT = "http.timeout.read";
  public static final String PROP_PROXY_HOST = "http.proxy.host";
  public static final String PROP_PROXY_PORT = "http.proxy.port";
  private final OkHttpClient httpClient;
  private final HttpUrl baseURL;
  private final String basePath;
  private final String credential;
  
  public ESIGeoCode(Properties prop)
    throws MalformedURLException
  {
    this.httpClient = new OkHttpClient();
    this.httpClient.setConnectTimeout(Integer.parseInt(prop.getProperty("http.timeout.connect", "5")), 
      TimeUnit.SECONDS);
    this.httpClient.setReadTimeout(Integer.parseInt(prop.getProperty("http.timeout.read", "30")), 
      TimeUnit.SECONDS);
    this.httpClient.setFollowRedirects(false);
    this.httpClient.setRetryOnConnectionFailure(true);
    if (prop.containsKey("http.proxy.host"))
    {
      SocketAddress sa = new InetSocketAddress(prop.getProperty("http.proxy.host"), 
        Integer.parseInt(prop.getProperty("http.proxy.port", "8080")));
      Proxy proxy = new Proxy(Proxy.Type.HTTP, sa);
      this.httpClient.setProxy(proxy);
    }
    this.baseURL = HttpUrl.parse(prop.getProperty("http.url", "http://localhost:8080/geocode"));
    StringBuilder sb = new StringBuilder();
    List<String> paths = this.baseURL.pathSegments();
    for (int i = 0; i < this.baseURL.pathSize(); i++) {
      sb.append('/').append((String)paths.get(i));
    }
    sb.append('/');
    this.basePath = sb.toString();
    if (prop.containsKey("http.username")) {
      this.credential = Credentials.basic(prop.getProperty("http.username").trim(), 
        prop.getProperty("http.password"));
    } else {
      this.credential = null;
    }
    this.httpClient.setAuthenticator(new Authenticator()
    {
      public Request authenticateProxy(Proxy arg0, Response arg1)
        throws IOException
      {
        return null;
      }
      
      public Request authenticate(Proxy arg0, Response response)
        throws IOException
      {
        return 
        
          response.request().newBuilder().header("Authorization", ESIGeoCode.this.credential).build();
      }
    });
  }
  
  JsonObject restGet(String path, Map<String, String> namevalue)
    throws IOException
  {
    HttpUrl.Builder ub = this.baseURL.resolve(this.basePath + path).newBuilder();
    if (namevalue != null) {
      for (String k : namevalue.keySet()) {
        ub.addQueryParameter(k, (String)namevalue.get(k));
      }
    }
    HttpUrl url = ub.build();
    Request.Builder rb = new Request.Builder();
    rb.url(url);
    

    Call call = this.httpClient.newCall(rb.build());
    Response httpRes = call.execute();
    if ((httpRes.code() < 200) || (httpRes.code() >= 300)) {
      return 
      

        Json.createObjectBuilder().add("error", Json.createObjectBuilder().add("message", url.toString() + ", get HTTP error (" + httpRes.code() + ") " + httpRes.message()).add("code", httpRes.code()).build()).build();
    }
    String rtype = httpRes.header("Content-Type", "text/plain");
    if ((rtype.contains("text/json")) || (rtype.contains("application/json")))
    {
      Object localObject1 = null;Object localObject4 = null;
      Object localObject3;
      try
      {
        InputStream resstream = httpRes.body().byteStream();
        try
        {
          JsonReader reader = Json.createReader(resstream);
          return reader.readObject();
        }
        finally
        {
          if (resstream != null) {
            resstream.close();
          }
        }
      }
      finally
      {
        /*if (localObject2 == null) {
          localObject3 = localThrowable;
        } else if (localObject3 != localThrowable) {
          localObject3.addSuppressed(localThrowable);
        }*/
      }
    }
    return 
    


      Json.createObjectBuilder().add("error", Json.createObjectBuilder().add("message", "Invalid JSON Response data content-type (" + rtype.toString() + ") ").add("code", -1).build()).build();
  }
  
  public boolean isError(JsonObject obj)
  {
    return obj.containsKey("error");
  }
  
  public JsonObject queryZones(double lon, double lat)
    throws IOException
  {
    Map<String, String> params = new HashMap(3);
    params.put("pos_lon", String.valueOf(lon));
    params.put("pos_lat", String.valueOf(lat));
    return restGet("geo/zone", params);
  }
  
  public JsonObject queryZones(String wkt, String SRID)
    throws IOException
  {
    Map<String, String> params = new HashMap(3);
    params.put("position", wkt);
    if ((SRID != null) && (!SRID.isEmpty())) {
      params.put("SRID", SRID);
    }
    return restGet("geo/zone", params);
  }
  
  public JsonObject getZone(int zoneid)
    throws IOException
  {
    return restGet("geo/zone/" + String.valueOf(zoneid), null);
  }
  
  public JsonObject queryRoads(double lon, double lat)
    throws IOException
  {
    Map<String, String> params = new HashMap(3);
    params.put("pos_lon", String.valueOf(lon));
    params.put("pos_lat", String.valueOf(lat));
    return restGet("geo/road", params);
  }
  
  public JsonObject queryRoads(String wkt, String SRID)
    throws IOException
  {
    Map<String, String> params = new HashMap(3);
    params.put("position", wkt);
    if ((SRID != null) && (!SRID.isEmpty())) {
      params.put("SRID", SRID);
    }
    return restGet("geo/road", params);
  }
  
  public JsonObject getRoad(int roadid)
    throws IOException
  {
    return restGet("geo/road/" + String.valueOf(roadid), null);
  }
}
