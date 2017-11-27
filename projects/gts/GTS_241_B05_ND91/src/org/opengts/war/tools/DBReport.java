package org.opengts.war.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.opengts.db.tables.FuelData;
import org.opengts.dbtools.DBConnection;
import org.opengts.dbtools.DBException;
import org.opengts.util.Print;

public class DBReport {

	public long ConvertToEpoch(String date, String timezone) {
		long res = 0;
		try {

			DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			DateFormat df12 = new SimpleDateFormat("dd/MM/yyyy HH:mm");

			TimeZone tz = TimeZone.getTimeZone(timezone);
			df1.setTimeZone(tz);
			Date d = df1.parse(date);
			date = df12.format(d);

			long ep = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").parse(
					date).getTime();
			ep = ep / 1000;
			res = ep;

		} catch (Exception e) {

		}
		return res;
	}

	public String getdate(String str) {
		String date = "";
		try {

			String[] arrdate = str.split("/");
			if (arrdate.length > 2) {
				date = arrdate[2] + "-" + arrdate[1] + "-" + arrdate[0];
			}
		} catch (Exception e) {

		}
		return date;
	}

	public ArrayList<FuelData> FuelReport(String accountID, String deviceID,
			String datefrom, String dateto, String timezone, String timefrom,
			String timeto) throws DBException {
		ArrayList<FuelData> list = new ArrayList<FuelData>();
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			long epf = ConvertToEpoch(datefrom + " " + timefrom, timezone);
			long epl = ConvertToEpoch(dateto + " " + timeto, timezone);

			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT * FROM `tblFuel` F " + " WHERE F.timestamp >= "
					+ epf + " and F.timestamp <= " + epl
					+ " and  F.accountID = '" + accountID
					+ "' and F.deviceID = '" + deviceID+ "'";
					//+ " and F.alarmType != '60' ";
			stmt = dbc.execute(command);
			//Print.logInfo("sbc: "+command);
			rs = stmt.getResultSet();
			FuelData fuelData;
			double lastValue = 0;
			while (rs.next()) {
					double currentValue = rs.getDouble("fuelLevel");
					double changeValue = currentValue == 0 ? 0 : currentValue - lastValue;
					lastValue = currentValue;
					fuelData = new FuelData(rs.getString("accountID"),
							rs.getString("deviceID"), rs.getInt("timestamp"),
							rs.getDouble("latitude"), rs.getDouble("longitude"),
							rs.getString("address"),currentValue,
							rs.getDouble("odometerKM"), rs.getInt("status"),
							changeValue,rs.getInt("alarmType"));
					list.add(fuelData);
					// list.put(rs.getString("fuelLevel"),
					//rs.getString("odometerKM");
			}
		} catch (SQLException sqe) {
			throw new DBException("ReportByStatusCode", sqe);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Throwable t) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Throwable t) {
				}
			}
			DBConnection.release(dbc);
		}
		return list;
	}

	public ResultSet GetDiviceByAccountID(String accountid) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select * from Device where accountID = '" + accountid
					+ "'";
			dbc = DBConnection.getDefaultConnection();
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			
		} catch (Exception e) {
			throw new DBException(e.getMessage());
		}
		return rs;
	}
	// get deviceCode that equal TSTZ05RS232
	public String GetDeviceCode(String accountID, String deviceID) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		String deviceCode = "";
		try {
			String sql = "select deviceCode from Device "
					+ " where accountID ='"+accountID+"'"
					+ " and deviceID ='"+deviceID+"'";
			dbc = DBConnection.getDefaultConnection();
			
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			while(rs.next()){
				deviceCode = rs.getString("deviceCode");
				break;
			}
		} catch (Exception e) {
			throw new DBException(e.getMessage());
		} finally{
			DBConnection.release(dbc, stmt, rs);
		}
		return deviceCode;
	}
	//get odometer value base on day
	public double GetOdometer(String accountID, String deviceID, long timestamp) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		double odometer = 0.0;
		try {
			String sql = "select E.odometerKM from EventData E inner join tblFuel F"
					+ " on F.timestamp = E.timestamp and E.deviceID=F.deviceID and E.accountID=F.accountID where E.accountID ='" +accountID+ "'"
					+ " and E.deviceID ='"+deviceID+"' and E.timestamp ='"+timestamp+"'";
			dbc = DBConnection.getDefaultConnection();
			
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet(); 
			while(rs.next()){
				odometer = rs.getDouble("odometerKM");
				break;
			}
		} catch (Exception e) {
			throw new DBException(e.getMessage());
		} finally{
			if (rs != null) {
				try {
					rs.close();
				} catch (Throwable t) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Throwable t) {
				}
			}
			DBConnection.release(dbc);
		}
		return odometer;
	}
	/*
	 * 
	 */
	public ArrayList<String> GetDeviceIdByDeviceCode(String accountID)
			throws DBException {
		ArrayList<String> list = new ArrayList<String>();
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {

			dbc = DBConnection.getDefaultConnection();
			String command = "select deviceID from Device where accountID = '"
					+ accountID + "' and deviceCode = 'camera'";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();

			while (rs.next()) {
				list.add(rs.getString("deviceID"));
				// list.put(rs.getString("fuelLevel"),
				// rs.getString("odometerKM"));
			}
		} catch (SQLException sqe) {
			throw new DBException("ReportByStatusCode", sqe);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Throwable t) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Throwable t) {
				}
			}
			DBConnection.release(dbc);
		}
		return list;
	}

}
