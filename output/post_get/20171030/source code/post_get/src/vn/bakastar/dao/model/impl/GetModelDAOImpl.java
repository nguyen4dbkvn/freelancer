package vn.bakastar.dao.model.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vn.bakastar.dao.model.GetModelDAO;
import vn.bakastar.db.DBConnection;
import vn.bakastar.exceptions.DAOException;
import vn.bakastar.model.GetEntry;

public class GetModelDAOImpl extends BaseModelDAOImpl implements GetModelDAO {

	protected static final String SQL_INSERT = 
		"INSERT INTO [$TABLE_NAME$] (accountID, deviceID, timestamp, statusCode, statusLastingTime, latitude, longitude, gpsAge, speedKPH, heading, altitude, address, distanceKM, odometerKM, creationTime, driverID, driverStatus, driverMessage, Cua, DieuHoa, Speed30s, params, db_name, imei) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public GetModelDAOImpl(DBConnection daoFactory, String tableName) {
		super(daoFactory, tableName);
	}

	@Override
	public List<GetEntry> list(int limit) throws DAOException {
		Object[] params = {
			_daoFactory.getLastSeqID(),
			limit
		};

		List<GetEntry> list = new ArrayList<GetEntry>();

		try (
			Connection conn = _daoFactory.getConnection();
			PreparedStatement ps = preparedStatement(conn, replace(SQL_LIST_ORDER_BY_ID), false, params);
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
	public void create(GetEntry getEntry) throws DAOException {
		Object[] values = {
			getEntry.getAccountID(),
			getEntry.getDeviceID(),
			getEntry.getTimestamp(),
			getEntry.getStatusCode(),
			getEntry.getStatusLastingTime(),
			getEntry.getLatitude(),
			getEntry.getLongitude(),
			getEntry.getGpsAge(),
			getEntry.getSpeedKPH(),
			getEntry.getHeading(),
			getEntry.getAltitude(),
			getEntry.getAddress(),
			getEntry.getDistanceKM(),
			getEntry.getOdometerKM(),
			getEntry.getCreationTime(),
			getEntry.getDriverID(),
			getEntry.getDriverStatus(),
			getEntry.getDriverMessage(),
			getEntry.getCua(),
			getEntry.getDieuHoa(),
			getEntry.getSpeed30s(),
			getEntry.getParams(),
			getEntry.getDBName(),
			getEntry.getImei()
		};

		try (
			Connection conn = _daoFactory.getConnection();
			PreparedStatement statement = preparedStatement(conn, replace(SQL_INSERT), true, values);
		) {
			int count = statement.executeUpdate();

			if (count == 0) {
				throw new DAOException(
					String.format("Inserting to table[%s] failed, no rows affected.", _tableName));
			}
		}
		catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void delete(GetEntry getEntry) throws DAOException {

		delete(getEntry.getSeqID());
	}

	@Override
	protected GetEntry map(ResultSet resultSet) throws SQLException {
		GetEntry get = new GetEntry();

		get.setSeqID(resultSet.getLong("seq_ID"));
		get.setAccountID(resultSet.getString("accountID"));
		get.setDeviceID(resultSet.getString("deviceID"));
		get.setTimestamp(resultSet.getLong("timestamp"));
		get.setStatusCode(resultSet.getLong("statusCode"));
		get.setStatusLastingTime(resultSet.getLong("statusLastingTime"));
		get.setLatitude(resultSet.getDouble("latitude"));
		get.setLongitude(resultSet.getDouble("longitude"));
		get.setGpsAge(resultSet.getLong("gpsAge"));
		get.setSpeedKPH(resultSet.getDouble("speedKPH"));
		get.setHeading(resultSet.getDouble("heading"));
		get.setAltitude(resultSet.getDouble("altitude"));
		get.setDistanceKM(resultSet.getDouble("distanceKM"));
		get.setOdometerKM(resultSet.getDouble("odometerKM"));
		get.setCreationTime(resultSet.getLong("creationTime"));
		get.setDriverID(resultSet.getString("driverID"));
		get.setDriverStatus(resultSet.getLong("driverStatus"));
		get.setDriverMessage(resultSet.getString("driverMessage"));
		get.setCua(resultSet.getLong("Cua"));
		get.setDieuHoa(resultSet.getLong("DieuHoa"));
		get.setSpeed30s(resultSet.getString("Speed30s"));
		get.setParams(resultSet.getString("params"));
		get.setDBName(resultSet.getString("db_name"));
		get.setImei(resultSet.getString("imei"));

		return get;
	}
}
