package vn.bakastar.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import vn.bakastar.util.PropsValue;

public class DAOUtil {

	private static TimeZone timezone = TimeZone.getTimeZone(PropsValue.TIMEZONE);
	private static SimpleDateFormat tzFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static SimpleDateFormat localFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	static {
		tzFormatter.setTimeZone(timezone);
	}

	public static String replace(String string, String[] oldArray, String[] newArray) {

		for (int i=0; i<oldArray.length; i++) {
			string = replace(string, oldArray[i], newArray[i]);
		}

		return string;
	}

	public static String replace(String string, String oldString, String newString) {

		return string.replace(oldString, newString);
	}

	public static PreparedStatement preparedStatement(Connection connection, 
		String sql, boolean returnGeneratedKeys, Object... values) throws SQLException {

		PreparedStatement statement = connection.prepareStatement(sql, 
			returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);

		for (int i = 0; i < values.length; i++) {
			statement.setObject(i + 1, values[i]);
		}

		return statement;
	}

	public static String format(java.util.Date date) {

		return tzFormatter.format(date);
	}

	public static Timestamp convertTimezone(java.util.Date date) throws ParseException {

		return toSqlTimestamp(localFormatter.parse(format(date)));
	}

	public static Timestamp toSqlTimestamp(long timestamp) {
		
		return new Timestamp(timestamp);
	}

	public static Timestamp toSqlTimestamp(java.util.Date date) {

		return (date != null) ? toSqlTimestamp(date.getTime()) : null;
	}
}
