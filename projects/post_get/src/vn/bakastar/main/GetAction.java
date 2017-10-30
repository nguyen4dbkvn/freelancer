package vn.bakastar.main;

import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;

import vn.bakastar.db.DBConnection;
import vn.bakastar.db.DBConnectionUtil;
import vn.bakastar.exceptions.ConfigurationException;
import vn.bakastar.exceptions.DAOException;
import vn.bakastar.exceptions.GeoCodeException;
import vn.bakastar.model.GetEntry;
import vn.bakastar.model.ImeiEntry;
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
		finally {
			DBConnectionUtil.release();
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

			DBConnectionUtil.release();
		}
	}

	protected int process(String sourceDBName) {
		int count = 0;

		try {
			DBConnection sourceDB = DBConnectionUtil.getDB(sourceDBName.trim(), false);

			count = sourceDB.countGet();
			int rest = count;

			while (rest > 0) {
				int batchSize = get(sourceDB);

				rest -= batchSize;
			}

			return count;
		}
		catch (DAOException e) {
			_logger.error(e.getMessage(), e);
		}

		return count;
	}

	protected int get(DBConnection sourceDB) {
		int limit = PropsValue.SQL_QUERY_RECORDS_NUMBER_PER_PAGE;
		long minTimestamp = (System.currentTimeMillis() - (24 * 60 * 60 * 1000)) / 1000;

		String[] desDBNames = PropsValue.GET_DESTINATION_DB_NAME;

		int batchSize = 0;

		try {
			List<GetEntry> getEntries = sourceDB.listGet(limit);
			batchSize = getEntries.size();

			for (String desDBName : desDBNames) {

				if (_logger.isDebugEnabled()) {
					_logger.debug(String.format(
						"Inserting %d record from db[%s] to db[%s]...", 
						batchSize, sourceDB.getName(), desDBName));
				}

				for (GetEntry getEntry : getEntries) {

					sourceDB.setLastSeqID(getEntry.getSeqID());

					if (getEntry.getTimestamp() > minTimestamp) {

						get(getEntry, sourceDB, desDBName.trim());
					}
					else if (_logger.isDebugEnabled()) {
						_logger.debug(String.format(
							"Because timestamp < (now - 24h) skipped insert: %s ", 
							getEntry.toString()));
					}
				}
			}
		}
		catch (DAOException e) {
			_logger.error(e.getMessage(), e);
		}

		return batchSize;
	}

	protected void get(GetEntry getEntry, DBConnection sourceDB, String desDBName) {
		try {
			DBConnection desDB = DBConnectionUtil.getDB(desDBName, true);

			getEntry.setDBName(sourceDB.getName());

			if (_logger.isDebugEnabled()) {
				_logger.debug(">> " + getEntry.toString());
			}

			desDB.create(getEntry);

			ImeiEntry imeiEntry = new ImeiEntry(getEntry);

			if (_logger.isDebugEnabled()) {
				_logger.debug(">> " + imeiEntry.toString());
			}

			desDB.create(imeiEntry, getEntry.getImei());
		}
		catch (ParseException e) {
			_logger.error(e.getMessage(), e);
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
