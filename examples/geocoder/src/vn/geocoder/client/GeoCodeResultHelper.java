package vn.geocoder.client;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import vn.geocoder.exception.GeoCodeException;

class GeoCodeResultHelper {

	public static String getAddressByRoad(String roadResponse) 
		throws GeoCodeException {

		JSONParser parser = new JSONParser();

		try {
			JSONObject jsonResponse = (JSONObject) parser.parse(roadResponse);

			Object error = jsonResponse.get(JSONResultKey.ERROR);

			if (error != null) {
				JSONObject jsonError = (JSONObject) error;

				String message = (String) jsonError.get(JSONResultKey.ERROR);

				throw new GeoCodeException(message, SUCCESS_STATUS);
			}

			JSONArray roads = (JSONArray) jsonResponse.get(JSONResultKey.GetRoad.ROADS);

			@SuppressWarnings("unchecked")
			Iterator<JSONObject> iterator = roads.iterator();

			if (iterator.hasNext()) {
				JSONObject road = iterator.next();

				return ((String) road.get(JSONResultKey.GetRoad.BEGIN_POINT)).trim();
			}
			else {
				return null;
			}
		}
		catch (ParseException e) {
			throw new GeoCodeException("Invalid JSON of roads response " + roadResponse);
		}
	}

	public static String getAddressByZone(String zoneResponse) 
		throws GeoCodeException {

		JSONParser parser = new JSONParser();

		try {
			JSONObject jsonResponse = (JSONObject) parser.parse(zoneResponse);

			Object error = jsonResponse.get(JSONResultKey.ERROR);

			if (error != null) {
				JSONObject jsonError = (JSONObject) error;

				String message = (String) jsonError.get(JSONResultKey.ERROR);

				throw new GeoCodeException(message, SUCCESS_STATUS);
			}

			JSONArray zones = (JSONArray) jsonResponse.get(JSONResultKey.GetZone.ZONES);

			@SuppressWarnings("unchecked")
			Iterator<JSONObject> iterator = zones.iterator();

			if (iterator.hasNext()) {
				JSONObject zone = iterator.next();

				String level1Name = (String) zone.get(JSONResultKey.GetZone.LEVEL1_NAME);
				String level2Name = (String) zone.get(JSONResultKey.GetZone.LEVEL2_NAME);
				String level3Name = (String) zone.get(JSONResultKey.GetZone.LEVEL3_NAME);

				StringBuilder sb = new StringBuilder(5);

				sb.append(level3Name.trim());
				sb.append(SPLIT_ADDRESS);
				sb.append(level2Name.trim());
				sb.append(SPLIT_ADDRESS);
				sb.append(level1Name.trim());

				return sb.toString();
			}
			else {
				return null;
			}
		}
		catch (ParseException e) {
			throw new GeoCodeException("Invalid JSON of zones response " + zoneResponse);
		}
	}

	public static final String SPLIT_ADDRESS = " - ";
	public static final int SUCCESS_STATUS = 200;
}
