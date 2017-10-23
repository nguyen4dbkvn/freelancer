package vn.bakastar.db;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DBConnectionUtil {

	public static DBConnection getDB(String name, boolean original) {
		if (_dbMap.get(name) == null) {
			_dbMap.put(name, DBConnection.getInstance(name, original));
		}

		return _dbMap.get(name);
	}

	private static Map<String, DBConnection> _dbMap = new ConcurrentHashMap<String, DBConnection>();
}
