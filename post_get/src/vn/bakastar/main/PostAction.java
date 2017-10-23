package vn.bakastar.main;

import java.util.List;

import vn.bakastar.db.DBConnection;
import vn.bakastar.db.DBConnectionUtil;
import vn.bakastar.exceptions.ConfigurationException;
import vn.bakastar.exceptions.DAOException;
import vn.bakastar.exceptions.GeoCodeException;
import vn.bakastar.geocode.connection.GeoCodeAPIHelper;
import vn.bakastar.model.PostEntry;
import vn.bakastar.util.Logger;
import vn.bakastar.util.PropsValue;

public class PostAction implements Runnable {

	@Override
	public void run() {
		try {
			process();

			Thread.sleep(PropsValue.POST_SLEEPING_TIME);
		}
		catch (ConfigurationException e) {
			Logger.log(getClass().getName(), e);
		}
		catch (DAOException e) {
			Logger.log(getClass().getName(), e);
		}
		catch (InterruptedException e) {
			Logger.log(getClass().getName(), e);
		}
	}

	protected void process() throws DAOException, ConfigurationException {
		DBConnection originalDB = DBConnectionUtil.getOriginalDB();

		try {
			List<PostEntry> posts = originalDB.listPost();

			for (PostEntry post : posts) {
				put(post, originalDB);
			}
		}
		finally {
			GeoCodeAPIHelper.release();
		}
	}

	protected void put(PostEntry postEntry, DBConnection originalDB) {
		try {
			String dbName = postEntry.getDBName();

			DBConnection db = DBConnectionUtil.getDB(dbName);

			db.create(postEntry);

			originalDB.delete(postEntry);
		}
		catch (DAOException e) {
			Logger.log(getClass().getName(), e);
		}
		catch (GeoCodeException e) {
			Logger.log(getClass().getName(), e);
		}
	}
}
