package vn.bakastar.main;

import java.util.List;

import vn.bakastar.db.DBConnection;
import vn.bakastar.db.DBConnectionUtil;
import vn.bakastar.exceptions.ConfigurationException;
import vn.bakastar.exceptions.DAOException;
import vn.bakastar.exceptions.GeoCodeException;
import vn.bakastar.model.PostEntry;
import vn.bakastar.util.Logger;
import vn.bakastar.util.PropsValue;

public class PostAction implements Runnable {

	@Override
	public void run() {
		try {
			while(true) {
				process();

				Logger.debug(getClass().getName(), 
					"Sleeping on " + (PropsValue.POST_SLEEPING_TIME / 1000) + " seconds...");

				Thread.sleep(PropsValue.POST_SLEEPING_TIME);
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

		Logger.debug(getClass().getName(), "Starting PostAction");

		int count = 0;

		try {
			String[] sourceDBNames = PropsValue.POST_SOURCE_DB_NAME;

			for (String sourceDBName : sourceDBNames) {
				DBConnection sourceDB = DBConnectionUtil.getDB(sourceDBName.trim(), true);

				List<PostEntry> posts = sourceDB.listPost();

				count = posts.size();

				for (PostEntry post : posts) {
					put(post, sourceDB);
				}
			}
		}
		finally {

			Logger.debug(getClass().getName(), 
				String.format("Processed %d records on %d ms", 
					count, (System.currentTimeMillis() - start)));
		}
	}

	protected void put(PostEntry postEntry, DBConnection sourceDB) {
		try {
			String dbName = postEntry.getDBName();

			DBConnection db = DBConnectionUtil.getDB(dbName.trim(), false);

			db.create(postEntry);

			sourceDB.deletePost(postEntry.getSeqID());
		}
		catch (DAOException e) {
			Logger.error(getClass().getName(), e);
		}
		catch (GeoCodeException e) {
			Logger.error(getClass().getName(), e);
		}
	}
}
