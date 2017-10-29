package vn.bakastar.db.model.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vn.bakastar.dao.model.PostModelDAO;
import vn.bakastar.db.DBConnection;
import vn.bakastar.exceptions.DAOException;
import vn.bakastar.model.PostEntry;

public class PostModelDAOImpl extends BaseModelDAOImpl implements PostModelDAO {

	protected static final String SQL_INSERT = 
		"INSERT INTO [$TABLE_NAME$] (accountID, deviceID, timestamp, statusCode, statusLastingTime, latitude, longitude, gpsAge, speedKPH, heading, altitude, address, distanceKM, odometerKM, creationTime, driverID, driverStatus, driverMessage, params) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public PostModelDAOImpl(DBConnection daoFactory, String tableName) {
		super(daoFactory, tableName);
	}

	@Override
	public List<PostEntry> list() throws DAOException {
		List<PostEntry> list = new ArrayList<PostEntry>();

		try (
			Connection conn = _daoFactory.getConnection();
			PreparedStatement ps = conn.prepareStatement(replace(SQL_LIST_ORDER_BY_ID));
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
	public void create(PostEntry postEntry) throws DAOException {
		Object[] values = {
			postEntry.getAccountID(),
			postEntry.getDeviceID(),
			postEntry.getTimestamp(),
			postEntry.getStatusCode(),
			postEntry.getStatusLastingTime(),
			postEntry.getLatitude(),
			postEntry.getLongitude(),
			postEntry.getGpsAge(),
			postEntry.getSpeedKPH(),
			postEntry.getHeading(),
			postEntry.getAltitude(),
			postEntry.getAddress(),
			postEntry.getDistanceKM(),
			postEntry.getOdometerKM(),
			postEntry.getCreationTime(),
			postEntry.getDriverID(),
			postEntry.getDriverStatus(),
			postEntry.getDriverMessage(),
			postEntry.getParams()
		};

		try (
			Connection connection = _daoFactory.getConnection();
			PreparedStatement statement = preparedStatement(connection, replace(SQL_INSERT), true, values);
		) {
			int count = statement.executeUpdate();

			if (count == 0) {
				throw new DAOException(
					String.format("Creating %s failed, no rows affected.", _tableName));
			}
		}
		catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void delete(PostEntry postEntry) throws DAOException {

		delete(postEntry.getSeqID());
	}

	protected PostEntry map(ResultSet resultSet) throws SQLException {
		PostEntry post = new PostEntry();

		post.setSeqID(resultSet.getLong("seq_ID"));
		post.setAccountID(resultSet.getString("accountID"));
		post.setDeviceID(resultSet.getString("deviceID"));
		post.setTimestamp(resultSet.getLong("timestamp"));
		post.setStatusCode(resultSet.getLong("statusCode"));
		post.setStatusLastingTime(resultSet.getLong("statusLastingTime"));
		post.setLatitude(resultSet.getDouble("latitude"));
		post.setLongitude(resultSet.getDouble("longitude"));
		post.setGpsAge(resultSet.getLong("gpsAge"));
		post.setSpeedKPH(resultSet.getDouble("speedKPH"));
		post.setHeading(resultSet.getDouble("heading"));
		post.setAltitude(resultSet.getDouble("altitude"));
		post.setDistanceKM(resultSet.getDouble("distanceKM"));
		post.setOdometerKM(resultSet.getDouble("odometerKM"));
		post.setCreationTime(resultSet.getLong("creationTime"));
		post.setDriverID(resultSet.getString("driverID"));
		post.setDriverStatus(resultSet.getLong("driverStatus"));
		post.setDriverMessage(resultSet.getString("driverMessage"));
		post.setParams(resultSet.getString("params"));
		post.setDBName(resultSet.getString("db_name"));

		return post;
	}
}
