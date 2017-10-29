package vn.bakasta.geocoder.util;

public class PropsValue {

	// ---------------- DEFAULT VALUE ---------------------
	public static final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";

	public static final String DB_TABLE_NAME_DEFAULT = "dv_geocoder";

	public static final long SLEEPING_TIME_DEFAULT = 15; // unit: second
	public static final long SLEEPING_MIN_TIME_DEFAULT = 1; // unit: second
	public static final long SLEEPING_MAX_TIME_DEFAULT = 60; // unit: second

	public static final String GEOCODE_API_HOST_DEFAULT = "localhost";
	public static final int GEOCODE_API_PORT_DEFAULT = 30000;
	public static final int GEOCODE_API_READ_TIMEOUT_DEFAULT = 1000;

	public static final int SQL_RECORD_NUMBER_PER_PAGE_DEFAULT = 20;

	public static final String LOG_FILE_PATH_DEFAULT = "./geocoder.log";

	// ------------------ PROPERTIES VALUE --------------------
	public static final String DB_JDBC_URL = PropsUtil.get(PropsKey.DB_JDBC_URL);
	public static final String DB_JDBC_USERNAME = PropsUtil.get(PropsKey.DB_JDBC_USERNAME);
	public static final String DB_JDBC_PASSWORD = PropsUtil.get(PropsKey.DB_JDBC_PASSWORD);
	public static final String DB_JDBC_DRIVER = PropsUtil.get(PropsKey.DB_JDBC_DRIVER, MYSQL_JDBC_DRIVER);
	public static final String DB_TABLE_NAME = PropsUtil.get(PropsKey.DB_TABLE_NAME, DB_TABLE_NAME_DEFAULT);

	public static final String GEOCODE_API_HOST = PropsUtil.get(PropsKey.GEOCODE_API_HOST, GEOCODE_API_HOST_DEFAULT);
	public static final int GEOCODE_API_PORT = PropsUtil.get(PropsKey.GEOCODE_API_PORT, GEOCODE_API_PORT_DEFAULT);
	public static final int GEOCODE_API_READ_TIMEOUT = PropsUtil.get(PropsKey.GEOCODE_API_READ_TIMEOUT,
			GEOCODE_API_READ_TIMEOUT_DEFAULT);

	// unit: second
	public static final long SLEEPING_MIN_TIME = PropsUtil.get(PropsKey.SLEEPING_MIN_TIME, SLEEPING_MIN_TIME_DEFAULT);
	public static final long SLEEPING_MAX_TIME = PropsUtil.get(PropsKey.SLEEPING_MAX_TIME, SLEEPING_MAX_TIME_DEFAULT);
	public static final long SLEEPING_TIME = PropsUtil.get(PropsUtil.get(PropsKey.SLEEPING_TIME, SLEEPING_TIME_DEFAULT),
			SLEEPING_MAX_TIME, SLEEPING_MIN_TIME) * 1000;

	public static final int SQL_RECORD_NUMBER_PER_PAGE =
		PropsUtil.get(PropsKey.SQL_RECORD_NUMBER_PER_PAGE, SQL_RECORD_NUMBER_PER_PAGE_DEFAULT);

	public static final String LOG_FILE_PATH =
			PropsUtil.get(PropsKey.LOG_FILE_PATH, LOG_FILE_PATH_DEFAULT);
}
