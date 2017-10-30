package vn.bakastar.dao.model.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import vn.bakastar.dao.model.ImeiModelDAO;
import vn.bakastar.db.DAOUtil;
import vn.bakastar.db.DBConnection;
import vn.bakastar.exceptions.DAOException;
import vn.bakastar.model.ImeiEntry;

public class ImeiModelDAOImpl extends BaseModelDAOImpl implements ImeiModelDAO {

	protected static final String SQL_INSERT = 
		"INSERT INTO [$TABLE_NAME$] (dt_server, dt_tracker, lat, lng, altitude, angle, speed, params) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	public ImeiModelDAOImpl(DBConnection daoFactory, String tableName) {
		super(daoFactory, tableName);
	}

	@Override
	public void create(ImeiEntry imeiEntry) throws DAOException, ParseException {
		Object[] values = {
			DAOUtil.convertTimezone(imeiEntry.getDtServer()),
			DAOUtil.convertTimezone(imeiEntry.getDtTracker()),
			imeiEntry.getLatitude(),
			imeiEntry.getLongitude(),
			imeiEntry.getAltitude(),
			imeiEntry.getAngle(),
			imeiEntry.getSpeed(),
			imeiEntry.getParams()
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
	public List<ImeiEntry> list(int limit) throws DAOException {
		Object[] params = {
			0,
			limit
		};

		List<ImeiEntry> list = new ArrayList<ImeiEntry>();

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
	protected ImeiEntry map(ResultSet resultSet) throws SQLException {
		ImeiEntry imei = new ImeiEntry();

		imei.setDtServer(resultSet.getDate("dt_server"));
		imei.setDtTracker(resultSet.getDate("dt_tracker"));
		imei.setLatitude(resultSet.getDouble("lat"));
		imei.setLongitude(resultSet.getDouble("lng"));
		imei.setAltitude(resultSet.getDouble("altitude"));
		imei.setAngle(resultSet.getDouble("angle"));
		imei.setSpeed(resultSet.getDouble("speed"));
		imei.setParams(resultSet.getString("params"));

		return imei;
	}
}
