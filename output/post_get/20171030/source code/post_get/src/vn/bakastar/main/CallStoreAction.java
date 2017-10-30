package vn.bakastar.main;

import org.apache.log4j.Logger;

import vn.bakastar.db.DBConnection;
import vn.bakastar.db.DBConnectionUtil;
import vn.bakastar.exceptions.ConfigurationException;
import vn.bakastar.exceptions.DAOException;
import vn.bakastar.util.PropsValue;

public class CallStoreAction implements Runnable {

	@Override
	public void run() {
		try {
			while(true) {
				process();

				_logger.info("Sleeping on " 
					+ (PropsValue.STORE_SLEEPING_TIME / 1000) + " second(s)...");

				Thread.sleep(PropsValue.STORE_SLEEPING_TIME);
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

		_logger.info("Starting CallStoreAction...");

		try {
			String[] sourceDBNames = PropsValue.GET_SOURCE_DB_NAME;

			for (String sourceDBName : sourceDBNames) {

				DBConnection sourceDB = DBConnectionUtil.getDB(sourceDBName.trim(), false);

				if (_logger.isDebugEnabled()) {
					_logger.debug(String.format("Calling store `%s`.`%s`", 
						sourceDBName, PropsValue.GET_STORE_PROCEDURE_NAME));
				}
				sourceDB.callStoreProcedure(PropsValue.GET_STORE_PROCEDURE_NAME);
			}
		}
		finally {
			_logger.info(String.format("Finish CallStoreAction on %dms", 
				(System.currentTimeMillis() - start)));
		}
	}

	private static Logger _logger = Logger.getLogger(CallStoreAction.class.getName());
}
