package vn.bakastar.main;

import vn.bakastar.db.DBConnection;
import vn.bakastar.db.DBConnectionUtil;
import vn.bakastar.geocode.connection.GeoCodeAPIHelper;
import vn.bakastar.model.GetEntry;
import vn.bakastar.model.PostEntry;

public class Application {

	public static void main(String[] args) {

		try {
			/*double latitude1 = 21.117851491216644D;
			double longitude1 = 106.39501333236694D;

			System.out.println("Address: " + GeoCodeAPIHelper.getAddress(latitude1, longitude1));
			double latitude2 = 21.0042788;
			double longitude2 = 105.8437013;
			System.out.println("Address: " + GeoCodeAPIHelper.getAddress(latitude2, longitude2));*/

			DBConnection originalDB = DBConnectionUtil.getOriginalDB();

			PostEntry post = new PostEntry();
			post.setAccountID("post2");
			post.setAltitude(2.0D);
			post.setCreationTime(12345678L);
			post.setDBName("lf7ga3");
			post.setDeviceID("post2");
			post.setDistanceKM(200.00D);
			post.setDriverID("post2");
			post.setDriverMessage("post2");
			post.setDriverStatus(2L);
			post.setGpsAge(2L);
			post.setHeading(2.0D);
			post.setLatitude(21.0042788D);
			post.setLongitude(105.8437013D);
			post.setOdometerKM(2.0D);
			post.setParams("post2");
			post.setSpeedKPH(60.00D);
			post.setStatusCode(2L);
			post.setSeqID(2L);
			post.setStatusLastingTime(12345678L);
			post.setTimestamp(12345678L);

			originalDB.create(post);

			GetEntry get = new GetEntry();
			get.setAccountID("get2");
			get.setAltitude(2.0D);
			get.setCreationTime(12345678L);
			get.setDBName("lf7ga3");
			get.setDeviceID("get2");
			get.setDistanceKM(200.00D);
			get.setDriverID("get2");
			get.setDriverMessage("get2");
			get.setDriverStatus(2L);
			get.setGpsAge(2L);
			get.setHeading(2.0D);
			get.setLatitude(21.0042788D);
			get.setLongitude(105.8437013D);
			get.setOdometerKM(2.0D);
			get.setParams("get2");
			get.setSpeedKPH(60.00D);
			get.setStatusCode(2L);
			get.setSeqID(2L);
			get.setStatusLastingTime(12345678L);
			get.setTimestamp(12345678L);
			get.setCua(2L);
			get.setDieuHoa(2L);
			get.setSpeed30s("get2");
			get.setImei("get2");

			originalDB.create(get);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			GeoCodeAPIHelper.release();
		}
	}
}
