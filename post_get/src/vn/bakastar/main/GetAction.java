package vn.bakastar.main;

import java.util.List;

import vn.bakastar.db.DBConnection;
import vn.bakastar.db.DBConnectionUtil;
import vn.bakastar.exceptions.ConfigurationException;
import vn.bakastar.exceptions.DAOException;
import vn.bakastar.exceptions.GeoCodeException;
import vn.bakastar.model.GetEntry;
import vn.bakastar.util.Logger;
import vn.bakastar.util.PropsValue;

public class GetAction implements Runnable {

	@Override
	public void run() {
		try {
			while(true) {
				process();

				Logger.debug(getClass().getName(), 
					"Sleeping " + (PropsValue.GET_SLEEPING_TIME / 1000) + " seconds...");

				Thread.sleep(PropsValue.GET_SLEEPING_TIME);
			}
		}
		catch (ConfigurationException e) {
			Logger.error(getClass().getName(), e);
		}
		catch (DAOException e) {
			Logger.error(getClass().getName(), e);
		}
		catch (InterruptedException e) {
			Logger.error(getClass().getName(), e);
		}
	}

	protected void process() throws DAOException, ConfigurationException {
		long start = System.currentTimeMillis();

		Logger.debug(getClass().getName(), "Starting GetAction");

		int count = 0;

		try {
			String[] sourceDBNames = PropsValue.GET_SOURCE_DB_NAME;
			String[] desDBNames = PropsValue.GET_DESTINATION_DB_NAME;

			for (String sourceDBName : sourceDBNames) {
				DBConnection sourceDB = DBConnectionUtil.getDB(sourceDBName.trim(), false);

				List<GetEntry> getEntries = sourceDB.listGet();

				count = getEntries.size();

				for (GetEntry getEntry : getEntries) {

					for (String desDBName : desDBNames) {

						put(getEntry, sourceDB, desDBName.trim());
					}
				}
			}
		}
		finally {
			Logger.debug(getClass().getName(), 
				String.format("Processed %d records on %d ms", count, 
					(System.currentTimeMillis() - start)));
		}
	}

	protected void put(GetEntry getEntry, DBConnection sourceDB, String desDBName) 
		throws DAOException {
		try {
			DBConnection desDB = DBConnectionUtil.getDB(desDBName, true);

			getEntry.setDBName(sourceDB.getName());

			desDB.create(getEntry);

			sourceDB.deleteGet(getEntry.getSeqID());
		}
		catch (DAOException e) {
			Logger.error(getClass().getName(), e);
		}
		catch (GeoCodeException e) {
			Logger.error(getClass().getName(), e);
		}
	}
}
