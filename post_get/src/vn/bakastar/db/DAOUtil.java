package vn.bakastar.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOUtil {

	public static PreparedStatement preparedStatement(Connection connection, 
		String sql, boolean returnGeneratedKeys, Object... values) throws SQLException {

		PreparedStatement statement = connection.prepareStatement(sql, 
			returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);

		for (int i = 0; i < values.length; i++) {
			statement.setObject(i + 1, values[i]);
		}

		return statement;
	}
}
