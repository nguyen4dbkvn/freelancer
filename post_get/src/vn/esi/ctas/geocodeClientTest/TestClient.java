package vn.esi.ctas.geocodeClientTest;

import java.io.IOException;
import java.util.Properties;

import javax.json.JsonObject;

import vn.esi.ctas.geocodeClient.ESIGeoCode;

public class TestClient
{
  Properties prop = new Properties();
  
  public TestClient()
  {
    this.prop.put("http.url", "https://hcm.esi.vn/geocode");
    this.prop.put("http.username", "demo");
    this.prop.put("http.password", "esireport3");
  }
  
  public void testNotFound()
  {
    try
    {
      ESIGeoCode geocode = new ESIGeoCode(this.prop);
      

      JsonObject result = geocode.queryRoads(0.0D, 0.0D);
      if (result.containsKey("error"))
      {
       System.out.println(result.getJsonObject("error").getString("message"));
        return;
      }
      if ((!result.containsKey("roads")) || 
        (result.getJsonArray("roads").size() != 0)) {
       System.out.println("Failed no road");
      }
      result = geocode.queryRoads("POINT(40.758871 -73.985114)", null);
      if (result.containsKey("error"))
      {
       System.out.println(result.getJsonObject("error").getString("message"));
        return;
      }
      if ((!result.containsKey("roads")) || 
        (result.getJsonArray("roads").size() != 0)) {
       System.out.println("Failed no road");
      }
      result = geocode.queryZones(0.0D, 0.0D);
      if (result.containsKey("error"))
      {
       System.out.println(result.getJsonObject("error").getString("message"));
        return;
      }
      if ((!result.containsKey("zones")) || 
        (result.getJsonArray("zones").size() != 0)) {
       System.out.println("Failed no road");
      }
      result = geocode.queryZones("POINT(40.758871 -73.985114)", null);
      if (result.containsKey("error"))
      {
       System.out.println(result.getJsonObject("error").getString("message"));
        return;
      }
      if ((!result.containsKey("zones")) || 
        (result.getJsonArray("zones").size() != 0)) {
       System.out.println("Failed no road");
      }
    }
    catch (IOException e)
    {
     System.out.println(e.getMessage());
    }
  }
  
  public void testFound()
  {
    try
    {
      ESIGeoCode geocode = new ESIGeoCode(this.prop);
      
      long start = System.currentTimeMillis();
      JsonObject result = geocode.queryRoads(106.39501333236694D, 21.117851491216644D);
      if (result.containsKey("error"))
      {
       System.out.println(result.getJsonObject("error").getString("message"));
        return;
      }
      if ((!result.containsKey("roads")) || 
        (result.getJsonArray("roads").size() == 0)) {
       System.out.println("Failed no road");
      }
      System.out.println(result.getJsonArray("roads").toString());
      System.out.println("time: " + (System.currentTimeMillis() - start));
      
      result = geocode.queryRoads("POINT(106.39501333236694 21.117851491216644)", null);
      if (result.containsKey("error"))
      {
       System.out.println(result.getJsonObject("error").getString("message"));
        return;
      }
      if ((!result.containsKey("roads")) || 
        (result.getJsonArray("roads").size() == 0)) {
       System.out.println("Failed no road");
      }
      int roadid = ((JsonObject)result.getJsonArray("roads").get(0)).getInt("roadid");
      result = geocode.getRoad(roadid);
      System.out.println(result.toString());
      
      result = geocode.queryZones(106.39501333236694D, 21.117851491216644D);
      if (result.containsKey("error"))
      {
       System.out.println(result.getJsonObject("error").getString("message"));
        return;
      }
      if ((!result.containsKey("zones")) || 
        (result.getJsonArray("zones").size() == 0)) {
       System.out.println("Failed no zone");
      }
      System.out.println(result.getJsonArray("zones").toString());
      
      result = geocode.queryZones("POINT(106.39501333236694 21.117851491216644)", null);
      if (result.containsKey("error"))
      {
       System.out.println(result.getJsonObject("error").getString("message"));
        return;
      }
      if ((!result.containsKey("zones")) || 
        (result.getJsonArray("zones").size() == 0)) {
       System.out.println("Failed no zone");
      }
      int zoneid = ((JsonObject)result.getJsonArray("zones").get(0)).getInt("zoneid");
      result = geocode.getZone(zoneid);
      System.out.println(result.toString());
    }
    catch (IOException e)
    {
     System.out.println(e.getMessage());
    }
  }
}
