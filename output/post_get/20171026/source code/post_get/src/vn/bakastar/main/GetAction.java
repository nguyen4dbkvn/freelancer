package vn.bakastar.main;

import java.util.List;

import org.apache.log4j.Logger;

import vn.bakastar.db.DBConnection;
import vn.bakastar.db.DBConnectionUtil;
import vn.bakastar.exceptions.ConfigurationException;
import vn.bakastar.exceptions.DAOException;
import vn.bakastar.exceptions.GeoCodeException;
import vn.bakastar.model.GetEntry;
import vn.bakastar.util.PropsValue;

public class GetAction implements Runnable {

	@Override
	public void run() {
		try {
			while(true) {
				process();

				_logger.info("Sleeping " + (PropsValue.GET_SLEEPING_TIME / 1000) 
					+ " second(s)...");

				Thread.sleep(PropsValue.GET_SLEEPING_TIME);
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

	protected void process() throws DAOException, ConfigurationException {
		long start = System.currentTimeMillis();

		_logger.info("Starting GetAction...");

		int count = 0;

		try {
			String[] sourceDBNames = PropsValue.GET_SOURCE_DB_NAME;

			for (String sourceDBName : sourceDBNames) {

				count += process(sourceDBName);
			}
		}
		finally {
			_logger.info(String.format("Processed %d records on %dms", 
				count, (System.currentTimeMillis() - start)));
		}
	}

	protected int process(String sourceDBName) {
		String[] desDBNames = PropsValue.GET_DESTINATION_DB_NAME;

		int processedCount = 0;

		try {
			DBConnection sourceDB = DBConnectionUtil.getDB(sourceDBName.trim(), false);

			sourceDB.callStoreProcedure(PropsValue.GET_STORE_PROCEDURE_NAME);

			List<GetEntry> getEntries = sourceDB.listGet();

			processedCount = getEntries.size();

			long minTimestamp = System.currentTimeMillis() - (24 * 60 * 60 * 1000);

			for (String desDBName : desDBNames) {

				if (_logger.isDebugEnabled()) {
					_logger.debug(String.format(
						"Inserting %d record from db[%s] to db[%s]...", 
						processedCount, sourceDBName, desDBName));
				}

				for (GetEntry getEntry : getEntries) {

					if (getEntry.getTimestamp() > minTimestamp) {

						put(getEntry, sourceDB, desDBName.trim());
					}
					else if (_logger.isDebugEnabled()) {
						_logger.debug(String.format(
							"Because timestamp < (now - 24h) skipped insert: %s ", 
							getEntry.toString()));
					}

					sourceDB.deleteGet(getEntry.getSeqID());
				}
			}

			return processedCount;
		}
		catch (DAOException e) {
			_logger.error(e.getMessage(), e);

			return processedCount;
		}
	}

	protected void callStoreProcedure(DBConnection sourceDB) {

		try {
			sourceDB.callStoreProcedure(PropsValue.GET_STORE_PROCEDURE_NAME);
		}
		catch (DAOException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	protected void put(GetEntry getEntry, DBConnection sourceDB, String desDBName) {
		try {
			DBConnection desDB = DBConnectionUtil.getDB(desDBName, true);

			getEntry.setDBName(sourceDB.getName());

			if (_logger.isDebugEnabled()) {
				_logger.debug(">> " + getEntry.toString());
			}

			desDB.create(getEntry);
		}
		catch (DAOException e) {
			_logger.error(e.getMessage(), e);
		}
		catch (GeoCodeException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private static final Logger _logger = Logger.getLogger(GetAction.class.getName());
}
