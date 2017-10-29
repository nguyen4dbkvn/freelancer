package vn.bakastar.util;

public class PropsValue {

	// ---------------- DEFAULT VALUE ---------------------
	public static final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";

	public static final long SLEEPING_TIME_DEFAULT = 15;		//unit: second
	public static final long SLEEPING_MIN_TIME_DEFAULT = 1;		//unit: second
	public static final long SLEEPING_MAX_TIME_DEFAULT = 60;	//unit: second

	public static final String POST_SOURCE_DB_NAME_DEFAULT = "gpst@123.30.168.68";

	public static final String GET_STORE_PROCEDURE_NAME_DEFAULT = "insert_ED_Post";

	public static final String POST_SOURCE_TABLE_NAME_DEFAULT = "dv_ed_post";
	public static final String POST_DESTINATION_TABLE_NAME_DEFAULT = "ED_Get";

	public static final String GET_SOURCE_TABLE_NAME_DEFAULT = "ED_Post";
	public static final String GET_DESTINATION_TABLE_NAME_DEFAULT = "dv_ed_get";

	public static final String GEOCODE_API_HOST_DEFAULT = "localhost";
	public static final int GEOCODE_API_PORT_DEFAULT = 30000;
	public static final int GEOCODE_API_READ_TIMEOUT_DEFAULT = 1000;

	// ------------------ PROPERTIES VALUE --------------------
	public static final String[] POST_SOURCE_DB_NAME = 
		PropsUtil.get(PropsKey.POST_SOURCE_DB_NAME, new String[] {POST_SOURCE_DB_NAME_DEFAULT});

	public static final String[] GET_SOURCE_DB_NAME = 
		PropsUtil.get(PropsKey.GET_SOURCE_DB_NAME, new String[]{});
	public static final String[] GET_DESTINATION_DB_NAME =
		PropsUtil.get(PropsKey.GET_DESTINATION_DB_NAME, new String[]{POST_SOURCE_DB_NAME_DEFAULT});

	public static final String GET_STORE_PROCEDURE_NAME =
		PropsUtil.get(PropsKey.GET_STORE_PROCEDURE_NAME, GET_STORE_PROCEDURE_NAME_DEFAULT);

	public static final String POST_SOURCE_TABLE_NAME = 
		PropsUtil.get(PropsKey.POST_SOURCE_TABLE_NAME, POST_SOURCE_TABLE_NAME_DEFAULT);
	public static final String POST_DESTINATION_TABLE_NAME = 
		PropsUtil.get(PropsKey.POST_DESTINATION_TABLE_NAME, POST_DESTINATION_TABLE_NAME_DEFAULT);

	public static final String GET_SOURCE_TABLE_NAME = 
		PropsUtil.get(PropsKey.GET_SOURCE_TABLE_NAME, GET_SOURCE_TABLE_NAME_DEFAULT);
	public static final String GET_DESTINATION_TABLE_NAME = 
		PropsUtil.get(PropsKey.GET_DESTINATION_TABLE_NAME, GET_DESTINATION_TABLE_NAME_DEFAULT);

	public static final String GEOCODE_API_HOST = 
		PropsUtil.get(PropsKey.GEOCODE_API_HOST, GEOCODE_API_HOST_DEFAULT);
	public static final int GEOCODE_API_PORT = 
			PropsUtil.get(PropsKey.GEOCODE_API_PORT, GEOCODE_API_PORT_DEFAULT);
	public static final int GEOCODE_API_READ_TIMEOUT =
		PropsUtil.get(PropsKey.GEOCODE_API_READ_TIMEOUT, GEOCODE_API_READ_TIMEOUT_DEFAULT);

	// unit: second
	public static final long SLEEPING_MIN_TIME =
		PropsUtil.get(PropsKey.SLEEPING_MIN_TIME, SLEEPING_MIN_TIME_DEFAULT);
	public static final long SLEEPING_MAX_TIME =
		PropsUtil.get(PropsKey.SLEEPING_MAX_TIME, SLEEPING_MAX_TIME_DEFAULT);
	public static final long GET_SLEEPING_TIME =
		PropsUtil.get(PropsUtil.get(PropsKey.GET_SLEEPING_TIME, SLEEPING_TIME_DEFAULT), 
			SLEEPING_MAX_TIME, SLEEPING_MIN_TIME) * 1000;
	public static final long POST_SLEEPING_TIME =
		PropsUtil.get(PropsUtil.get(PropsKey.POST_SLEEPING_TIME, SLEEPING_TIME_DEFAULT), 
			SLEEPING_MAX_TIME, SLEEPING_MIN_TIME) * 1000;
}
