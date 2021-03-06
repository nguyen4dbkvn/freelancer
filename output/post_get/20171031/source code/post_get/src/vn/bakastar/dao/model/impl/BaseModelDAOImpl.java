package vn.bakastar.dao.model.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import vn.bakastar.dao.model.ModelDAO;
import vn.bakastar.db.DAOUtil;
import vn.bakastar.db.DBConnection;
import vn.bakastar.db.SQLConstants;
import vn.bakastar.exceptions.DAOException;

public abstract class BaseModelDAOImpl implements ModelDAO {

	protected static final String SQL_COUNT = "SELECT COUNT(seq_ID) FROM [$TABLE_NAME$] WHERE seq_ID > ?";

	protected static final String SQL_LIST_ORDER_BY_ID = 
		"SELECT * FROM [$TABLE_NAME$] WHERE seq_ID > ? ORDER BY seq_ID ASC LIMIT ?";

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
		Object[] params = {_daoFactory.getLastSeqID()};
		int count = 0;

		try (
			Connection conn = _daoFactory.getConnection();
			PreparedStatement ps = preparedStatement(conn, replace(SQL_COUNT), false, params);
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
			PreparedStatement statement = preparedStatement(conn, replace(SQL_DELETE), false, values);
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

	abstract public List<?> list(int limit) throws DAOException;

	abstract protected Object map(ResultSet resultSet) throws SQLException;

	protected String replace(String query) {

		return DAOUtil.replace(query, SQLConstants.TABLE_NAME, _tableName);
	}

	protected PreparedStatement preparedStatement(Connection connection, String sql, 
		boolean returnGeneratedKeys, Object... values) throws SQLException {

		return DAOUtil.preparedStatement(connection, sql, returnGeneratedKeys, values);
	}
}
