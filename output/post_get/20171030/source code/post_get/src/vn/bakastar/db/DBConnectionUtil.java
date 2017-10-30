package vn.bakastar.db;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import vn.bakastar.util.ConfigFileUtil;

public class DBConnectionUtil {

	public static DBConnection getDB(String name, boolean original) {
		if (_dbMap.get(name) == null) {
			_dbMap.put(name, DBConnection.getInstance(name, original));
		}

		return _dbMap.get(name);
	}

	public static void release() {
		Map<String, Long> dbSeqIDMap = new HashMap<String, Long>();

		for (Map.Entry<String, DBConnection> entry : _dbMap.entrySet()) {
			String name = entry.getKey();
			DBConnection dbConnection = entry.getValue();

			dbSeqIDMap.put(name, dbConnection.getLastSeqID());
		}

		ConfigFileUtil.saveSeqID(dbSeqIDMap);
	}

	private static Map<String, DBConnection> _dbMap = new ConcurrentHashMap<String, DBConnection>();
}
