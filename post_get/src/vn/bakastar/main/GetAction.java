package vn.bakastar.main;

import java.util.List;

import vn.bakastar.db.DBConnection;
import vn.bakastar.db.DBConnectionUtil;
import vn.bakastar.exceptions.ConfigurationException;
import vn.bakastar.exceptions.DAOException;
import vn.bakastar.model.GetEntry;
import vn.bakastar.util.Logger;
import vn.bakastar.util.PropsValue;

public class GetAction implements Runnable {

	@Override
	public void run() {
		try {
			process();

			Thread.sleep(PropsValue.GET_SLEEPING_TIME);
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

	protected void process() {
		DBConnection originalDB = DBConnectionUtil.getOriginalDB();

		List<GetEntry> getEntries = 
	}

}
