package vn.bakasta.geocoder.db;

import vn.bakasta.geocoder.util.PropsValue;

public class DBConnectionUtil {

	public static DBConnection getDBConnection() {
		if (_dbConnection == null) {
			_dbConnection = new DBConnection(PropsValue.DB_JDBC_URL, 
				PropsValue.DB_JDBC_USERNAME, PropsValue.DB_JDBC_PASSWORD);
		}

		return _dbConnection;
	}

	private static DBConnection _dbConnection;
}
