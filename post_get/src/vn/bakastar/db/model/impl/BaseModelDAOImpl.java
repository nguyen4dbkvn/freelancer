package vn.bakastar.db.model.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import vn.bakastar.dao.model.ModelDAO;
import vn.bakastar.db.DBConnection;
import vn.bakastar.exceptions.DAOException;

public abstract class BaseModelDAOImpl implements ModelDAO {

	protected static final String SQL_COUNT = "SELECT * FROM [$TABLE_NAME$]";

	protected static final String SQL_LIST_ORDER_BY_ID = 
		"SELECT * FROM [$TABLE_NAME$] ORDER BY seq_ID";

	protected static final String SQL_DELETE = 
		"DELETE FROM [$TABLE_NAME$] WHERE seq_ID = ?";

	protected String _tableName;
	protected DBConnection _daoFactory;

	public BaseModelDAOImpl(DBConnection daoFactory, String tableName) {
		this._daoFactory = daoFactory;
		this._tableName = tableName;
	}

	@Override
	public int count() throws DAOException {
		int count = 0;

		try (
			Connection conn = _daoFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(replace(SQL_COUNT));
			ResultSet resultSet = ps.executeQuery();
		) {
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
		}
		catch (SQLException e) {
			throw new DAOException(e);
		}

		return count;
	}

	@Override
	public void delete(long primaryKey) throws DAOException {
		Object[] values = {primaryKey};

		try (
			Connection conn = _daoFactory.getConnection();
			PreparedStatement statement = preparedStatement(conn, SQL_DELETE, false, values);
		) {
			int affectedRows = statement.executeUpdate();

			if (affectedRows == 0) {
				throw new DAOException(String.format("Deleting %s failed, no rows affected", _tableName));
			}
		}
		catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	abstract public List<?> list() throws DAOException;

	abstract protected Object map(ResultSet resultSet) throws SQLException;

	protected String replace(String query) {

		String sql = query.replace("[$TABLE_NAME$]", _tableName);
		System.out.println(sql);
		return sql;
	}

	protected PreparedStatement preparedStatement(Connection connection, String sql, 
		boolean returnGeneratedKeys, Object... values) throws SQLException {

		PreparedStatement statement = connection.prepareStatement(sql,
				returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);

		for (int i = 0; i < values.length; i++) {
			statement.setObject(i + 1, values[i]);
		}

		return statement;
	}
}
