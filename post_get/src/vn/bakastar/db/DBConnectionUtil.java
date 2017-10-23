package vn.bakastar.db;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import vn.bakastar.util.PropsValue;

public class DBConnectionUtil {

	public static DBConnection getOriginalDB() {

		if (_originalDB == null) {
			_originalDB = DBConnection.getInstance(PropsValue.ORIGINAL_DB_NAME, true);
		}

		return _originalDB;
	}

	public static DBConnection getDB(String name) {
		if (_dbMap.get(name) == null) {
			_dbMap.put(name, DBConnection.getInstance(name, false));
		}

		return _dbMap.get(name);
	}

	private static DBConnection _originalDB;
	private static Map<String, DBConnection> _dbMap = new ConcurrentHashMap<String, DBConnection>();
}
