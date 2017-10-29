package vn.bakasta.geocoder.util;

public interface PropsKey {

	public static final String DB_JDBC_URL = "db.jdbc.url";
	public static final String DB_JDBC_DRIVER = "db.jdbc.driver";
	public static final String DB_JDBC_USERNAME = "db.jdbc.username";
	public static final String DB_JDBC_PASSWORD = "db.jdbc.password";
	public static final String DB_TABLE_NAME = "db.table.name";

	public static final String GEOCODE_API_HOST = "geocode.api.host";
	public static final String GEOCODE_API_PORT = "geocode.api.port";
	public static final String GEOCODE_API_READ_TIMEOUT = "geocode.api.read.timeout";

	public static final String SLEEPING_TIME = "sleeping.time";
	public static final String SLEEPING_MIN_TIME = "sleeping.min.time";
	public static final String SLEEPING_MAX_TIME = "sleeping.max.time";

	public static final String SQL_RECORD_NUMBER_PER_PAGE = "sql.record.number.per.page";

	public static final String LOG_FILE_PATH = "log.file.path";
}
