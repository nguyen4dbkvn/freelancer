package vn.bakastar.geocode.connection;

interface JSONResultKey {

	public static final String ERROR = "error";

	public static final String MESSGAGE = "message";

	interface GetRoad {
		public static final String ROADS = "roads";

		public static final String ROAD_ID = "roadid";

		public static final String REF_ID = "ref_id";

		public static final String ROAD_NAME = "roadname";

		public static final String BEGIN_POINT = "beginpoint";

		public static final String END_POINT = "endpoint";

		public static final String POS_DISTANCE = "pos_distance";
	}

	interface GetZone {
		public static final String ZONES = "zones";

		public static final String ZONE_ID = "zoneid";

		public static final String LEVEL1_REFID = "level1_refid";

		public static final String LEVEL2_REFID = "level2_refid";

		public static final String LEVEL3_REFID = "level3_refid";

		public static final String LEVEL1_NAME = "level1_name";

		public static final String LEVEL2_NAME = "level2_name";

		public static final String LEVEL3_NAME = "level3_name";
	}
}
