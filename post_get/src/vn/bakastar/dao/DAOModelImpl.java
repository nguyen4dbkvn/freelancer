package vn.bakastar.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import vn.bakastar.exceptions.DAOException;

public class DAOModelImpl implements DAOModel {

	private static final String SQL_COUNT = "SELECT * FROM ";

	private String _tableName;
	private DAOFactory _daoFactory;

	public DAOModelImpl(DAOFactory daoFactory, String tableName) {
		this._daoFactory = daoFactory;
		this._tableName = tableName;
	}

	public int count() throws DAOException {
		int count = 0;

		try (
			Connection conn = _daoFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(SQL_COUNT + _tableName);
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
}
