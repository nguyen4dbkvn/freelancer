package vn.bakastar.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import vn.bakastar.util.PropsValue;

public class DAOFactoryUtil {

	public static DAOFactory getOriginalDB() {

		if (_originalDB == null) {
			_originalDB = DAOFactory.getInstance(PropsValue.ORIGINAL_DB_NAME, true);
		}

		return _originalDB;
	}

	public static DAOFactory getDB(String name) {
		if (_dbMap.get(name) == null) {
			_dbMap.put(name, DAOFactory.getInstance(name, false));
		}

		return _dbMap.get(name);
	}

	private static DAOFactory _originalDB;
	private static Map<String, DAOFactory> _dbMap = new ConcurrentHashMap<String, DAOFactory>();
}
