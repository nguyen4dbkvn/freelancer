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
	}

	protected void process() throws DAOException, ConfigurationException {
		long start = System.currentTimeMillis();

		_logger.info("Starting PostAction...");

		int count = 0;

		try {
			String[] sourceDBNames = PropsValue.POST_SOURCE_DB_NAME;

			long minTimestamp = start - (24 * 60 * 60 * 1000);

			for (String sourceDBName : sourceDBNames) {
				DBConnection sourceDB = DBConnectionUtil.getDB(sourceDBName.trim(), true);

				List<PostEntry> posts = sourceDB.listPost();

				count += posts.size();

				for (PostEntry post : posts) {

					if (post.getTimestamp() > minTimestamp) {

						put(post, sourceDB);
					}
					else if (_logger.isDebugEnabled()) {
						_logger.debug(String.format(
							"Because timestamp < (now - 24h) skipped: %s ", 
							post.toString()));
					}

					sourceDB.deletePost(post.getSeqID());
				}
			}
		}
		finally {

			_logger.info(String.format("Processed %d records on %dms", 
				count, (System.currentTimeMillis() - start)));
		}
	}

	protected void put(PostEntry postEntry, DBConnection sourceDB) {
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
