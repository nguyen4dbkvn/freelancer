package vn.bakasta.geocoder;

import java.util.List;

import org.apache.log4j.Logger;

import vn.bakasta.geocoder.db.DBConnection;
import vn.bakasta.geocoder.db.DBConnectionUtil;
import vn.bakasta.geocoder.exceptions.ConfigurationException;
import vn.bakasta.geocoder.exceptions.DAOException;
import vn.bakasta.geocoder.exceptions.GeoCodeException;
import vn.bakasta.geocoder.mapxtreme.GeoCodeAPIHelper;
import vn.bakasta.geocoder.model.GeoCoder;
import vn.bakasta.geocoder.util.PropsValue;

public class GeoCoderAction implements Runnable {

	@Override
	public void run() {
		try {
			while(true) {
				process();

				_logger.info(String.format("Sleeping %d second(s)...", 
					(PropsValue.SLEEPING_TIME / 1000)));

				Thread.sleep(PropsValue.SLEEPING_TIME);
			}
		}
		catch (ConfigurationException e) {
			_logger.error(e.getMessage(), e);
		}
		catch (DAOException e) {
			_logger.error(e.getMessage(), e);
		}
		catch (InterruptedException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	protected void process() {
		long start = System.currentTimeMillis();

		_logger.info("Starting GetAction...");

		DBConnection dbConnection = DBConnectionUtil.getDBConnection();

		int count = dbConnection.count();
		int limit = PropsValue.SQL_RECORD_NUMBER_PER_PAGE;
		int rest = count;

		try {
			while (rest > 0) {
				System.out.println(String.format("Rest %d ", rest));

				List<GeoCoder> list = dbConnection.list(limit);

				for (GeoCoder geoCoder : list) {

					updateAddress(dbConnection, geoCoder);
				}

				rest -= limit;
			}
		}
		finally {
			_logger.info(String.format("Processed %d records on %dms", 
				count, (System.currentTimeMillis() - start)));
		}
	}

	protected void updateAddress(DBConnection dbConnection, GeoCoder geoCoder) {
		try {
			if (_logger.isDebugEnabled()) {
				_logger.debug("Updating " + geoCoder.toString());
			}

			 String address = GeoCodeAPIHelper.getAddress(
				geoCoder.getLatitude(), geoCoder.getLongitude());

			 dbConnection.update(geoCoder.getSeqId(), address);
		}
		catch (DAOException e) {
			_logger.error(e.getMessage(), e);
		}
		catch (GeoCodeException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private static Logger _logger = Logger.getLogger(GeoCoderAction.class.getName());
}
