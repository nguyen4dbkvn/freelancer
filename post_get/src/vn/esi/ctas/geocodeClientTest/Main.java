package vn.esi.ctas.geocodeClientTest;

import java.util.Properties;

import javax.json.JsonObject;

import vn.esi.ctas.geocodeClient.ESIGeoCode;

public class Main {

	public static void main(String[] args) {
		Properties prop = new Properties();

		prop.put("http.url", "https://hcm.esi.vn/geocode");
	    prop.put("http.username", "demo");
	    prop.put("http.password", "esireport3");

	    try {
	    	long start = System.currentTimeMillis();
	    	System.out.println("starting");
	    	ESIGeoCode geocode = new ESIGeoCode(prop);

	    	 
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

	         JsonObject result2 = geocode.queryRoads(107.23634719848633D, 10.837519070738601D);
	        
	         System.out.println(result2.getJsonArray("roads").toString());
	         System.out.println("time: " + (System.currentTimeMillis() - start));
	    }
	    catch (Exception e) {
			e.printStackTrace();
		}
	}
}
