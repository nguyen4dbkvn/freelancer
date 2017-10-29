package vn.bakasta.geocoder.dao.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vn.bakasta.geocoder.db.DAOUtil;
import vn.bakasta.geocoder.db.DBConnection;
import vn.bakasta.geocoder.exceptions.DAOException;
import vn.bakasta.geocoder.model.GeoCoder;

public class GeoCoderModelDAOImpl implements GeoCoderModelDAO {

	protected static final String SQL_COUNT = "SELECT COUNT(seq_ID) FROM [$TABLE_NAME$] WHERE address IS NULL;";

	protected static final String SQL_LIST_ORDER_BY_ID = 
		"SELECT seq_ID, lat, lng, address FROM [$TABLE_NAME$] WHERE address IS NULL ORDER BY seq_ID LIMIT ?;";

	protected static final String SQL_UPDATE =
		"UPDATE [$TABLE_NAME$] SET address = ? WHERE seq_ID = ?;";

	public GeoCoderModelDAOImpl(DBConnection dbConnection, String tableName) {
		this._dbConnection = dbConnection;
		this._tableName = tableName;
	}

	@Override
	public int count() throws DAOException {

		int count = 0;

		try (
			Connection conn = _dbConnection.getConnection();
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
	public List<GeoCoder> list(int limit) throws DAOException {
		Object[] params = {
			limit
		};

		List<GeoCoder> list = new ArrayList<GeoCoder>();

		try (
			Connection conn = _dbConnection.getConnection();
			PreparedStatement ps = DAOUtil.preparedStatement(
				conn, replace(SQL_LIST_ORDER_BY_ID), false, params);
			ResultSet resultSet = ps.executeQuery();
		) {
			while (resultSet.next()) {
				list.add(map(resultSet));
			}
		}
		catch (SQLException e) {
			throw new DAOException(e);
		}

		return list;
	}

	@Override
	public void update(long seqId, String address) throws DAOException {
		Object[] params = {
			address,
			seqId
		};

		try (
			Connection conn = _dbConnection.getConnection();
			PreparedStatement statement = DAOUtil.preparedStatement(conn, replace(SQL_UPDATE), false, params);
		) {
			int count = statement.executeUpdate();

			if (count == 0) {
				throw new DAOException(
					String.format("Updating to table[%s] failed, no rows affected.", _tableName));
			}
		}
		catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	protected String replace(String query) {

		return DAOUtil.replace(query, "[$TABLE_NAME$]", _tableName);
	}

	protected GeoCoder map(ResultSet resultSet) throws SQLException {
		GeoCoder geoCoder = new GeoCoder();

		geoCoder.setSeqId(resultSet.getLong("seq_ID"));
		geoCoder.setLatitude(resultSet.getDouble("lat"));
		geoCoder.setLongitude(resultSet.getDouble("lng"));
		geoCoder.setAddress(resultSet.getString("Address"));

		return geoCoder;
	}

	protected String _tableName;
	protected DBConnection _dbConnection;
}
