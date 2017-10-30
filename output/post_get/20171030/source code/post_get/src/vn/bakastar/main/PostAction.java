package vn.bakastar.main;

import java.util.List;

import org.apache.log4j.Logger;

import vn.bakastar.db.DBConnection;
import vn.bakastar.db.DBConnectionUtil;
import vn.bakastar.exceptions.ConfigurationException;
import vn.bakastar.exceptions.DAOException;
import vn.bakastar.exceptions.GeoCodeException;
import vn.bakastar.model.PostEntry;
import vn.bakastar.util.PropsValue;

public class PostAction implements Runnable {

	@Override
	public void run() {
		try {
			while(true) {
				process();

				_logger.info("Sleeping on " 
					+ (PropsValue.POST_SLEEPING_TIME / 1000) + " second(s)...");

				Thread.sleep(PropsValue.POST_SLEEPING_TIME);
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

		_logger.info("Starting PostAction...");

		int count = 0;

		try {
			String[] sourceDBNames = PropsValue.POST_SOURCE_DB_NAME;

			for (String sourceDBName : sourceDBNames) {
				post(sourceDBName);
			}
		}
		finally {
			_logger.info(String.format("Processed %d records on %dms", 
				count, (System.currentTimeMillis() - start)));

			DBConnectionUtil.release();
		}
	}

	protected int post(String sourceDBName) {
		DBConnection sourceDB = null;
		int count = 0;

		try {
			sourceDB = DBConnectionUtil.getDB(sourceDBName.trim(), true);

			int rest = sourceDB.countPost();
			count += rest;

			while (rest > 0) {
				int batchSize = post(sourceDB);

				rest -= batchSize;
			}
		}
		catch (DAOException e) {
			_logger.error(e.getMessage(), e);
		}

		return count;
	}

	protected int post(DBConnection sourceDB) {
		long minTimestamp = (System.currentTimeMillis() - (24 * 60 * 60 * 1000)) / 1000;

		int limit = PropsValue.SQL_QUERY_RECORDS_NUMBER_PER_PAGE;

		int batchSize = 0;

		try {
			List<PostEntry> posts = sourceDB.listPost(limit);
			batchSize = posts.size();

			for (PostEntry post : posts) {

				sourceDB.setLastSeqID(post.getSeqID());

				if (post.getTimestamp() > minTimestamp) {

					post(post, sourceDB);
				}
				else if (_logger.isDebugEnabled()) {
					_logger.debug(String.format(
						"Because timestamp < (now - 24h) skipped: %s ", 
						post.toString()));
				}
			}
		}
		catch (DAOException e) {
			_logger.error(e.getMessage(), e);
		}

		return batchSize;
	}

	protected void post(PostEntry postEntry, DBConnection sourceDB) {
		try {
			String dbName = postEntry.getDBName();

			DBConnection db = DBConnectionUtil.getDB(dbName.trim(), false);

			if (_logger.isDebugEnabled()) {
				_logger.debug(String.format("Inserting [%s --> %s]: %s", 
					sourceDB.getName(), dbName, postEntry.toString()));
			}

			db.create(postEntry);
		}
		catch (DAOException e) {
			_logger.error(e.getMessage(), e);
		}
		catch (GeoCodeException e) {
			_logger.error(e.getMessage(), e);
		}
	}

	private static final Logger _logger = Logger.getLogger(PostAction.class.getName());
}
