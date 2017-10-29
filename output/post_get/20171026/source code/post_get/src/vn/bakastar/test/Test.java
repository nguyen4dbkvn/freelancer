package vn.bakastar.test;

import org.apache.log4j.Logger;

import vn.bakastar.db.DBConnection;
import vn.bakastar.db.DBConnectionUtil;
import vn.bakastar.model.PostEntry;

public class Test {

	public static void main(String[] args) {
		DBConnection debugDB = DBConnectionUtil.getDB("debug@127.0.0.1", true);

		PostEntry post1 = new PostEntry();
		post1.setAccountID("post1");
		post1.setAltitude(1.0D);
		post1.setCreationTime(12345678L);
		post1.setDBName("ihi");
		post1.setDeviceID("post1");
		post1.setDistanceKM(100.00D);
		post1.setDriverID("post1");
		post1.setDriverMessage("post1");
		post1.setDriverStatus(1L);
		post1.setGpsAge(1L);
		post1.setHeading(1.0D);
		post1.setLatitude(21.0042788D);
		post1.setLongitude(105.8437013D);
		post1.setOdometerKM(1.0D);
		post1.setParams("post1");
		post1.setSpeedKPH(60.00D);
		post1.setStatusCode(1L);
		post1.setSeqID(1L);
		post1.setStatusLastingTime(12345678L);
		post1.setTimestamp(12345678L);

		debugDB.create(post1);

		PostEntry post2 = new PostEntry();
		post2.setAccountID("post2");
		post2.setAltitude(2.0D);
		post2.setCreationTime(12345678L);
		post2.setDBName("lportal");
		post2.setDeviceID("post2");
		post2.setDistanceKM(100.00D);
		post2.setDriverID("post2");
		post2.setDriverMessage("post1");
		post2.setDriverStatus(2L);
		post2.setGpsAge(2L);
		post2.setHeading(2.0D);
		post2.setLatitude(21.0042788D);
		post2.setLongitude(105.8437013D);
		post2.setOdometerKM(2.0D);
		post2.setParams("post2");
		post2.setSpeedKPH(60.00D);
		post2.setStatusCode(2L);
		post2.setSeqID(2L);
		post2.setStatusLastingTime(12345678L);
		post2.setTimestamp(12345678L);

		debugDB.create(post2);
	}

	final static Logger _logger = Logger.getLogger(Test.class.getName());
}
