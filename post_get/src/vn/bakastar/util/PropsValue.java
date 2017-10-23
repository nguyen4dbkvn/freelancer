package vn.bakastar.util;

public class PropsValue {

	// ---------------- DEFAULT VALUE ---------------------
	public static final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";

	public static final long SLEEPING_TIME_DEFAULT = 15;		//unit: second
	public static final long SLEEPING_MIN_TIME_DEFAULT = 1;		//unit: second
	public static final long SLEEPING_MAX_TIME_DEFAULT = 60;	//unit: second

	public static final String POST_SOURCE_DB_NAME_DEFAULT = "gtse";

	public static final String POST_SOURCE_TABLE_NAME_DEFAULT = "dv_ed_post";
	public static final String POST_DESTINATION_TABLE_NAME_DEFAULT = "ED_Get";

	public static final String GET_SOURCE_TABLE_NAME_DEFAULT = "ED_Post";
	public static final String GET_DESTINATION_TABLE_NAME_DEFAULT = "dv_ed_get";

	public static final String GEOCODE_API_FQDN_DEFAULT = "https://esi.vn/geocode";
	public static final String GEOCODE_API_GET_ROAD_CONTEXT_DEFAULT = "/geo/road";
	public static final String GEOCODE_API_GET_ZONE_CONTEXT_DEFAULT = "/geo/zone";
	public static final String GEOCODE_API_USERNAME_DEFAULT = "vmg-geocode";
	public static final String GEOCODE_API_PASSWORD_DEFAULT = "TruyvanGe0";
	public static final long GEOCODE_TIME_TO_LIVE_DEFAULT = 1000;

	// ------------------ PROPERTIES VALUE --------------------
	public static final String[] POST_SOURCE_DB_NAME = 
		PropsUtil.get(PropsKey.POST_SOURCE_DB_NAME, new String[] {POST_SOURCE_DB_NAME_DEFAULT});
	public static final String[] GET_SOURCE_DB_NAME = 
		PropsUtil.get(PropsKey.GET_SOURCE_DB_NAME, new String[]{});

	public static final String POST_SOURCE_TABLE_NAME = 
		PropsUtil.get(PropsKey.POST_SOURCE_TABLE_NAME, POST_SOURCE_TABLE_NAME_DEFAULT);
	public static final String POST_DESTINATION_TABLE_NAME = 
		PropsUtil.get(PropsKey.POST_DESTINATION_TABLE_NAME, POST_DESTINATION_TABLE_NAME_DEFAULT);

	public static final String GET_SOURCE_TABLE_NAME = 
		PropsUtil.get(PropsKey.GET_SOURCE_TABLE_NAME, GET_SOURCE_TABLE_NAME_DEFAULT);
	public static final String GET_DESTINATION_TABLE_NAME = 
		PropsUtil.get(PropsKey.GET_DESTINATION_TABLE_NAME, GET_DESTINATION_TABLE_NAME_DEFAULT);

	public static final String GEOCODE_API_FQDN = 
		PropsUtil.get(PropsKey.GEOCODE_API_FQDN, GEOCODE_API_FQDN_DEFAULT);
	public static final String GEOCODE_API_GET_ROAD_CONTEXT = 
		PropsUtil.get(PropsKey.GEOCODE_API_GET_ROAD_CONTEXT, GEOCODE_API_GET_ROAD_CONTEXT_DEFAULT);
	public static final String GEOCODE_API_GET_ZONE_CONTEXT = 
		PropsUtil.get(PropsKey.GEOCODE_API_GET_ZONE_CONTEXT, GEOCODE_API_GET_ZONE_CONTEXT_DEFAULT);
	public static final String GEOCODE_API_USERNAME = 
		PropsUtil.get(PropsKey.GEOCODE_API_USERNAME, GEOCODE_API_USERNAME_DEFAULT);
	public static final String GEOCODE_API_PASSWORD = 
		PropsUtil.get(PropsKey.GEOCODE_API_PASSWORD, GEOCODE_API_PASSWORD_DEFAULT);
	public static final long GEOCODE_API_TIME_TO_LIVE =
		PropsUtil.get(PropsKey.GEOCODE_API_TIME_TO_LIVE, GEOCODE_TIME_TO_LIVE_DEFAULT);

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
