package vn.bakastar.util;

public class PropsValue {

	// ---------------- DEFAULT VALUE ---------------------
	public static final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";

	public static final long DEFAULT_SLEEPING_TIME = 15;

	public static final long DEFAULT_SLEEPING_MIN_TIME = 1;

	public static final long DEFAULT_SLEEPING_MAX_TIME = 60;

	public static final String DEFAULT_ORIGINAL_DB_NAME = "gtse";

	public static final String DEFAULT_POST_TABLE_POST_NAME = "dv_ed_post";
	public static final String DEFAULT_POST_TABLE_GET_NAME = "ED_Get";

	public static final String DEFAULT_GET_TABLE_POST_NAME = "ED_Post";
	public static final String DEFAULT_GET_TABLE_GET_NAME = "dv_ed_get";

	// ------------------ PROPERTIES VALUE --------------------
	public static final String ORIGINAL_DB_NAME = 
		PropsUtil.get(PropsKey.ORIGINAL_DB_NAME, DEFAULT_ORIGINAL_DB_NAME);

	public static final String POST_TABLE_POST_NAME = 
		PropsUtil.get(PropsKey.POST_TABLE_POST_NAME, DEFAULT_POST_TABLE_POST_NAME);
	public static final String POST_TABLE_GET_NAME = 
		PropsUtil.get(PropsKey.POST_TABLE_GET_NAME, DEFAULT_POST_TABLE_GET_NAME);

	public static final String GET_TABLE_POST_NAME = 
		PropsUtil.get(PropsKey.GET_TABLE_POST_NAME, DEFAULT_GET_TABLE_POST_NAME);
	public static final String GET_TABLE_GET_NAME = 
		PropsUtil.get(PropsKey.GET_TABLE_GET_NAME, DEFAULT_GET_TABLE_GET_NAME);
}
