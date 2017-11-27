// ----------------------------------------------------------------------------
// Copyright 2007-2012, GeoTelematic Solutions, Inc.
// All rights reserved
// ----------------------------------------------------------------------------
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// ----------------------------------------------------------------------------
// Database specific 'DELETE' handler.
// ----------------------------------------------------------------------------
// Change History:
//  2007/09/16  Martin D. Flynn
//     -Initial release
//     -NOTE: This module is not thread safe (this is typically not an issue, since
//      the use of this class is limited to the creation for a specific 'DELETE'
//      statement within a given thread).
//  2012/01/29  Martin D. Flynn
//     -PostgreSQL support added (by Gaurav Kohli)
// ----------------------------------------------------------------------------
package org.opengts.dbtools;

import java.lang.*;
import java.util.*;
import java.io.*;
import java.text.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

import org.opengts.util.*;
import org.opengts.war.track.page.LiveEvent;
import org.opengts.db.tables.CheckSIM;
import org.opengts.db.tables.Device;
import org.opengts.db.tables.EventData;
import org.opengts.db.tables.Fuel;
import org.opengts.db.tables.FuelData;
import org.opengts.db.tables.dungdo;
import org.opengts.db.tables.MoCua;
import org.opengts.db.tables.VuotToc;
import org.opengts.db.tables.DrivingTime;
import org.opengts.db.tables.DrivingTime1;

/**
 *** <code>DBCamera</code> create thanhtq use select device and imagedata device
 * select camera statement.
 **/

public class CopyOfDBCamera {

	/*
	 * Contructor
	 */
	public CopyOfDBCamera() {

	}

	public ResultSet GetCameraTotal(String date, String device)
			throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {

			String sql = "select * from imagedata where date(createdtime) = str_to_date( '"
					+ date + "', '%d/%m/%Y' ) and DeviceID = '" + device + "'";
			dbc = DBConnection.getDefaultConnection();
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();

		} catch (Exception e) {
			throw new DBException(e.getMessage());
		}
		return rs;
	}

	/*
     * 
     */
	public ResultSet GetDiviceByAccountID(String accountid) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {

			String sql = "select * from Device where AccountID = '"
					+ accountid
					+ "' and (devicecode = 'camera' or devicecode = 'TSTcamera')";

			dbc = DBConnection.getDefaultConnection();
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
		} catch (Exception e) {
			throw new DBException(e.getMessage());
		}
		return rs;
	}

	// MinhNV bat dau tu day
	public Connection Connect() {
		Connection conn = null;
		try {

			String userName = DBProvider.getDBUsername();
			String password = DBProvider.getDBPassword();
			String url = "jdbc:mysql://" + DBProvider.getDBHost() + "/"
					+ DBProvider.getDBName();

			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = (Connection) DriverManager.getConnection(url, userName,
					password);
			// System.out.println("Da ket noi CSDL");
			return conn;
		} catch (Exception e) {
			// System.err.println("KHONG KET NOI DUOC");
			return null;
		}
	}

	public ResultSet GetDiviceByAccountID1(String accountid, String us,
			String DeviceID) throws DBException {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call Hienntd_GetDeviceID(?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accountid);
			cstmt.setString(2, us);
			cstmt.setString(3, DeviceID);
			rs = cstmt.executeQuery();
		} catch (Exception e) {
			throw new DBException(e.getMessage());
		}
		return rs;
	}

	public ResultSet GetDiviceByAccountID2(String accountid, String userid)
			throws DBException {

		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call Hienntd_GetDevicebyAccount(?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accountid);
			cstmt.setString(2, userid);
			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}
		return rs;
	}

	public ResultSet GetType() throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT TypeID,TypeName from servicetype";
			dbc = DBConnection.getDefaultConnection();
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
		} catch (Exception e) {
			throw new DBException(e.getMessage());
		}
		return rs;
	}

	public ResultSet getDEV_Jobdetail(int ID) {

		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call DEVJob_SelectByID(?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setInt(1, ID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}

		return rs;
	}

	public void job_update(String id, String date, String time, String eta,
			String type, String po, String cname, String phone, String equi,
			String vehic, String vyear, String make, String model,
			String vtype, String vcolor, String vin, String state,
			String fuelt, String callre, String pro, String com, String dis) {
		Connection con = Connect();

		try {
			String sql = "{call DEVJob(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			// int i= Integer.parseInt(id);
			cstmt.setString(1, id);
			cstmt.setString(2, date);
			cstmt.setString(3, time);
			cstmt.setString(4, eta);
			cstmt.setString(5, type);
			cstmt.setString(6, po);
			cstmt.setString(7, cname);
			cstmt.setString(8, phone);
			cstmt.setString(9, equi);
			cstmt.setString(10, vehic);
			cstmt.setString(11, vyear);
			cstmt.setString(12, make);
			cstmt.setString(13, model);
			cstmt.setString(14, vtype);
			cstmt.setString(15, vcolor);
			cstmt.setString(16, vin);
			cstmt.setString(17, state);
			cstmt.setString(18, fuelt);
			cstmt.setString(19, callre);
			cstmt.setString(20, pro);
			cstmt.setString(21, com);
			cstmt.setString(22, dis);
			cstmt.executeUpdate();

		} catch (Exception e) {

		}
	}

	// MinhNV viet cho nay
	public ResultSet getTotalReport(String xe, String ngay, String tenAccount) {

		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call MinhNV_getTotalReport(?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, xe);
			cstmt.setString(2, ngay);
			cstmt.setString(3, tenAccount);
			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}

		return rs;
	}

	public ResultSet getReport(String xe, String ngay, String tenAccount,
			int page, int pagesize) {

		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call MinhNV_getReport(?,?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, xe);
			cstmt.setString(2, ngay);
			cstmt.setString(3, tenAccount);
			cstmt.setInt(4, page);
			cstmt.setInt(5, pagesize);
			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}

		return rs;
	}

	public List<String[]> getZone(String acc) {
		ResultSet rs = null;
		Connection con = Connect();
		CallableStatement cstmt = null;
		List<String[]> GetZone = new ArrayList<String[]>();
		try {
			String sql = "{call MinhNV_getZone(?)}";
			cstmt = con.prepareCall(sql);
			cstmt.setString(1, acc);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				String[] gz = new String[4];
				gz[0] = rs.getString("geozoneID");
				gz[1] = rs.getString("description");
				gz[2] = rs.getString("latitude1");
				gz[3] = rs.getString("longitude1");
				GetZone.add(gz);
			}
		} catch (Exception e) {
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) { /* ignored */
				}
			}
			if (cstmt != null) {
				try {
					cstmt.close();
				} catch (SQLException e) { /* ignored */
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) { /* ignored */
				}
			}
		}
		return GetZone;
	}

	public List<String[]> getTram(String acc, String ngay, String tram) {
		ResultSet rs = null;
		Connection con = Connect();
		CallableStatement cstmt = null;
		List<String[]> Getreport = new ArrayList<String[]>();
		try {
			String sql = "{call MinhNV_getTram(?,?,?)}";
			cstmt = con.prepareCall(sql);
			cstmt.setString(1, tram);
			cstmt.setString(2, ngay);
			cstmt.setString(3, acc);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				String[] gr = new String[7];
				gr[0] = rs.getString("deviceID");
				gr[1] = rs.getString("thoiDiem");
				gr[2] = rs.getString("VT");
				gr[3] = rs.getString("Cua");
				gr[4] = rs.getString("DieuHoa");
				gr[5] = rs.getString("description");
				gr[6] = rs.getString("altitude");
				Getreport.add(gr);
			}
		} catch (Exception e) {
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) { /* ignored */
				}
			}
			if (cstmt != null) {
				try {
					cstmt.close();
				} catch (SQLException e) { /* ignored */
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) { /* ignored */
				}
			}
		}
		return Getreport;
	}

	public ResultSet getTotalRecoreTram(String xe, String tuNgay,
			String denNgay, String Tram, String tenAccount) {

		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call MinhNV_getTotalRecoreTram(?,?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, xe);
			cstmt.setString(2, tuNgay);
			cstmt.setString(3, denNgay);
			cstmt.setString(4, Tram);
			cstmt.setString(5, tenAccount);
			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}

		return rs;
	}

	public ResultSet getJobs(String tenAccount, String tuNgay, String denNgay,
			int type, int ChooseSort) {

		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call MinhNV_getJobs(?,?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, tenAccount);
			cstmt.setString(2, tuNgay);
			cstmt.setString(3, denNgay);
			cstmt.setInt(4, type);
			cstmt.setInt(5, ChooseSort);
			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}

		return rs;
	}

	public ResultSet getRecoreTram(String xe, String tuNgay, String denNgay,
			String Tram, String tenAccount, int currentPage, int Size) {

		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call MinhNV_getRecoreTram(?,?,?,?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, xe);
			cstmt.setString(2, tuNgay);
			cstmt.setString(3, denNgay);
			cstmt.setString(4, Tram);
			cstmt.setString(5, tenAccount);
			cstmt.setInt(6, currentPage);
			cstmt.setInt(7, Size);
			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}

		return rs;
	}

	public ResultSet getGeoZoneArrival(String acc) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call MinhNV_selecttblGeoZoneArrival(?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, acc);
			rs = cstmt.executeQuery();
		} catch (Exception e) {
		}
		return rs;
	}

	public ResultSet insertGeoZoneArrival(String geoID, int time, String acc,
			int kieu) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call MinhNV_insertTblGeoZoneArrival(?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, geoID);
			cstmt.setInt(2, time);
			cstmt.setString(3, acc);
			cstmt.setInt(4, kieu);
			rs = cstmt.executeQuery();
		} catch (Exception e) {
		}
		return rs;
	}

	public ResultSet DeleteGeoZoneArrival(int autoID) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call MinhNV_deleteTblGeoZoneArrivalBy(?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setInt(1, autoID);
			rs = cstmt.executeQuery();
		} catch (Exception e) {
		}
		return rs;
	}

	public void updateGeoZoneArrival(String geoID, int time, int id, int kieu) {
		Connection con = Connect();
		try {
			String sql = "{call MinhNV_updateTblGeoZoneArrivalBy(?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, geoID);
			cstmt.setInt(2, time);
			cstmt.setInt(3, id);
			cstmt.setInt(4, kieu);
			cstmt.executeUpdate();
		} catch (Exception e) {
		}

	}

	public ResultSet getImgLink(String deviceID) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call Minhnv_getLastImgLink(?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, deviceID);
			rs = cstmt.executeQuery();
		} catch (Exception e) {
		}
		return rs;

	}

	public String getLastFuel(String deviceID, String acc) {
		String fuel = "Không có dữ liệu";
		ResultSet rs = null;
		Connection con = Connect();
		CallableStatement cstmt = null;
		try {
			String sql = "{call MinhNV_selectLastFuelByDeviceID(?,?)}";
			cstmt = con.prepareCall(sql);
			cstmt.setString(1, deviceID);
			cstmt.setString(2, acc);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}

		try {
			while (rs.next()) {
				fuel = rs.getString(1) + "/" + rs.getString(2);
			}

		} catch (Exception e) {

			fuel = "0";
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) { /* ignored */
				}
			}
			if (cstmt != null) {
				try {
					cstmt.close();
				} catch (SQLException e) { /* ignored */
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) { /* ignored */
				}
			}
		}
		return fuel;
	}

	public ResultSet selectDoiXe(String AccountID, String usID) {

		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call Hienntd_selectDoiXe(?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, AccountID);
			cstmt.setString(2, usID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;
	}

	public ResultSet selectXeByDoiXe(String AccountID, String GroupID) {

		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call MinhNV_selectXeTheoDoi(?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, AccountID);
			cstmt.setString(2, GroupID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;
	}

	public ResultSet selectXeByDoiXe1(String AccountID, String GroupID) {

		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call MinhNV_selectXeTheoDoi_xang(?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, AccountID);
			cstmt.setString(2, GroupID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;
	}

	public ResultSet selectXeTheoDoi(String AccountID, String us, String groupID) {

		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call Hienntd_selectXeTheoDoi(?,?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, AccountID);
			cstmt.setString(2, us);
			cstmt.setString(3, groupID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;
	}

	// Lienptk - vuot toc do

	public ResultSet reportVuotTocDo(String accountid, String xe,
			String tuNgay, String denNgay, int tocDo, int currentPage, int Size) {

		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call Lienptk_reportVuotTocDo(?,?,?,?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accountid);
			cstmt.setString(2, xe);
			cstmt.setString(3, tuNgay);
			cstmt.setString(4, denNgay);
			cstmt.setInt(5, tocDo);
			cstmt.setInt(6, currentPage);
			cstmt.setInt(7, Size);
			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}

		return rs;
	}

	public ResultSet TotalVuotTocDo(String accountid, String xe, String tuNgay,
			String denNgay, int tocDo) {

		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call Lienptk_TotalVuotTocDo(?,?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accountid);
			cstmt.setString(2, xe);
			cstmt.setString(3, tuNgay);
			cstmt.setString(4, denNgay);
			cstmt.setInt(5, tocDo);
			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}

		return rs;
	}

	public ResultSet getTotalVuotTocDo(String accountid, String xe,
			String tuNgay, String denNgay, int tocDo) {

		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call Lienptk_getTotalVuotTocDo(?,?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accountid);
			cstmt.setString(2, xe);
			cstmt.setString(3, tuNgay);
			cstmt.setString(4, denNgay);
			cstmt.setInt(5, tocDo);
			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}

		return rs;
	}

	// hienntd add GetTotalReportDevDetail
	public ResultSet GetTotalReportDevDetail(String DevID, String tungay,
			String denngay, String AccID) {
		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call Hienntd_GetDevDetail(?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, DevID);
			cstmt.setString(2, tungay);
			cstmt.setString(3, denngay);
			cstmt.setString(4, AccID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}

		return rs;
	}

	public ResultSet GetReportDevDetail(String DevID, String tungay,
			String denngay, String AccID, int CurPage, int Size) {
		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call Hienntd_GetReportDevDetail(?,?,?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, DevID);
			cstmt.setString(2, tungay);
			cstmt.setString(3, denngay);
			cstmt.setString(4, AccID);
			cstmt.setInt(5, CurPage);
			cstmt.setInt(6, Size);
			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}

		return rs;
	}

	public ResultSet TongHopDoiXe(String AccountID, String tuNgay,
			String denNgay, String bienXe) {

		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call MinhNV_TongHopDoiXe(?,?,?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, AccountID);
			cstmt.setString(2, tuNgay);
			cstmt.setString(3, denNgay);
			cstmt.setString(4, bienXe);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;
	}

	public ArrayList<String> getAllDevice(String AccountID) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();

		try {

			String sql = "select * from Device where AccountID = '" + AccountID
					+ "'";
			dbc = DBConnection.getDefaultConnection();
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();

			while (rs.next()) {

				list.add(rs.getString("deviceID"));

			}
		} catch (Exception e) {
			throw new DBException(e.getMessage());
		}
		return list;
	}

	// MinhNV driver and device start
	public ResultSet Insert_DeviceDriver(String deviceID, String driverID,
			String vaccountID) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call MinhNV_Insert_DeviceDriver(?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, deviceID);
			cstmt.setString(2, driverID);
			cstmt.setString(3, vaccountID);

			rs = cstmt.executeQuery();
		} catch (Exception e) {
		}
		return rs;

	}

	public ResultSet Delete_DeviceDriver(String deviceID, String accountID) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call MinhNV_Delete_DeviceDriver(?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, deviceID);
			cstmt.setString(2, accountID);
			rs = cstmt.executeQuery();
		} catch (Exception e) {
		}
		return rs;

	}

	public ResultSet select_DeviceDriver(String deviceID, String driverID,
			String accountID) {
		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call MinhNV_select_DeviceDriver(?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, deviceID);
			cstmt.setString(2, driverID);
			cstmt.setString(3, accountID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}

		return rs;
	}

	public ResultSet selectDriver(String accountID) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call MinhNV_selectDriver(?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accountID);
			rs = cstmt.executeQuery();
		} catch (Exception e) {

		}
		return rs;

	}

	public List<String[]> selectDriverbyDriverID(String driverID,
			String accountID) {
		ResultSet rs = null;
		Connection con = Connect();
		CallableStatement cstmt = null;
		List<String[]> Getreport = new ArrayList<String[]>();
		try {
			String sql = "{call MinhNV_selectDriverbyDriverID(?,?)}";
			cstmt = con.prepareCall(sql);
			cstmt.setString(1, driverID);
			cstmt.setString(2, accountID);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				String[] gr = new String[15];
				gr[0] = rs.getString(1);
				gr[1] = rs.getString(2);
				gr[2] = rs.getString(3);
				gr[3] = rs.getString(4);
				gr[4] = rs.getString(5);
				gr[5] = rs.getString(6);
				gr[6] = rs.getString(7);
				gr[7] = rs.getString(8);
				gr[8] = rs.getString(9);
				gr[9] = rs.getString(10);
				gr[10] = rs.getString(11);
				gr[11] = rs.getString(12);
				gr[12] = rs.getString(13);
				gr[13] = rs.getString(14);
				gr[14] = rs.getString(15);
				Getreport.add(gr);
			}
		} catch (Exception e) {
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) { /* ignored */
				}
			}
			if (cstmt != null) {
				try {
					cstmt.close();
				} catch (SQLException e) { /* ignored */
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) { /* ignored */
				}
			}
		}

		return Getreport;

	}

	// Hienntd
	public List<String[]> select_LaiXeHienTai(String deviceID, String accountID) {
		ResultSet rs = null;
		Connection con = Connect();
		CallableStatement cstmt = null;
		List<String[]> Getreport = new ArrayList<String[]>();
		try {
			String sql = "call Hienntd_select_LaiXeHienTai_1(?,?)";
			cstmt = con.prepareCall(sql);
			cstmt.setString(1, deviceID);
			cstmt.setString(2, accountID);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				String[] gr = new String[8];
				gr[0] = rs.getString(1);
				gr[1] = rs.getString(2);
				gr[2] = rs.getString(3);
				gr[3] = rs.getString(4);
				gr[4] = rs.getString(5);
				gr[5] = rs.getString(6);
				// if (rs.wasNull()) {
				// gr[5]="";
				// }
				gr[6] = rs.getString(7);
				// if (rs.wasNull()) {
				// gr[6]="";
				// }
				gr[7] = rs.getString(8);
				Getreport.add(gr);
			}
		} catch (Exception e) {
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) { /* ignored */
				}
			}
			if (cstmt != null) {
				try {
					cstmt.close();
				} catch (SQLException e) { /* ignored */
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) { /* ignored */
				}
			}
		}

		return Getreport;
	}

	// Lái xe hiện tại select radom driverid by devid,accid
	public String Device_SelectDriverID(String devid, String accid)
			throws DBException {
		ResultSet rs = null;
		DBConnection dbc = null;
		Statement stmt = null;
		String DriverID = "";
		try {
			dbc = DBConnection.getDefaultConnection();
			String sql = "SELECT driverID FROM Device_Driver WHERE accountID='"
					+ accid
					+ "' and deviceID='"
					+ devid
					+ "' and driverID not IN (select dv.driverID FROM Device dv WHERE "
					+ " dv.accountID='" + accid + "') LIMIT 0,1;";
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			while (rs.next()) {
				DriverID = rs.getString("driverID");
			}
		} catch (SQLException sqe) {
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
		return DriverID;
	}

	public List<String[]> Laixehientai1(String devid, String accid)
			throws DBException {
		ResultSet rs = null;
		DBConnection dbc = null;
		Statement stmt = null;
		List<String[]> GetDriver = new ArrayList<String[]>();
		String driverid = Device_SelectDriverID(devid, accid);
		try {
			dbc = DBConnection.getDefaultConnection();
			String sql = "SELECT * FROM Driver WHERE accountID='" + accid
					+ "' and driverID='" + driverid + "' ";
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			while (rs.next()) {
				String[] gz = new String[6];
				gz[0] = devid;
				gz[1] = rs.getString("contactphone");
				gz[2] = rs.getString("description");
				gz[3] = driverid;
				gz[4] = rs.getString("licenseNumber");
				gz[5] = rs.getString("badgeID");
				GetDriver.add(gz);
			}
		} catch (SQLException sqe) {
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
		return GetDriver;
	}

	/*
	 * Bao Cao Do Xang
	 * 
	 * @param
	 */
	public ArrayList<FuelData> BaoCaoDoXang(String accountID, String deviceID,
			String fromTime, String toTime, String timezone)
			throws DBException, SQLException {

		ArrayList<FuelData> list = new ArrayList<FuelData>();
		ResultSet rs = null;
		DBConnection dbc = null;
		Statement stmt = null;
		FuelData reportFuel;
		try {
			dbc = DBConnection.getDefaultConnection();
			long epf = ConvertToEpoch(fromTime + " 00:00:00", timezone);
			long epl = ConvertToEpoch(toTime + " 23:59:00", timezone);
			String sql = " (SELECT F.accountID, F.deviceID, F.latitude, F.longitude, F.timestamp as col_time,"
					+ " F.fuelLevel, F.odometerKM, F.address, F.status, F.alarmType "
					+ " from tblFuel F " + " where (F.timestamp BETWEEN "
					+ epf
					+ " and "
					+ epl
					+ ") and F.accountID ='"
					+ accountID
					+ "'"
					+ " and F.deviceID ='"
					+ deviceID
					+ "'"
					+ " limit 1 )"
					+ " UNION "
					+ "(SELECT distinct F.accountID, F.deviceID, F.latitude, F.longitude, F.timestamp as col_time,"
					+ " F.fuelLevel as fuelLevel, F.odometerKM, F.address, F.status, F.alarmType "
					+ " FROM tblFuel F INNER JOIN tblDrivingTime2 D "
					+ " ON D.accountID = F.accountID and D.deviceID = F.deviceID "
					+ " where (F.timestamp BETWEEN "
					+ epf
					+ " and "
					+ epl
					+ ") "
					+ " and F.accountID = '"
					+ accountID
					+ "' and F.deviceID = '"
					+ deviceID
					+ "' and (D.reportType = '2' or D.reportType = '3')"
					+ " and F.timestamp = D.stopTime"
					+ " order by F.timestamp, F.fuelLevel asc) "
					+ " UNION "
					+ " ( SELECT distinct F.accountID, F.deviceID, F.latitude, F.longitude, F.timestamp as col_time,"
					+ " F.fuelLevel - if(DX.nhienLieu = 'null',0, DX.nhienLieu), F.odometerKM, F.address, F.status, F.alarmType "
					+ " from tblDoXang1 DX"
					+ " inner join tblFuel F "
					+ " on DX.accountID = F.accountID and DX.deviceID = F.deviceID "
					+ " where "
					+ " (F.timestamp BETWEEN "
					+ epf
					+ " and "
					+ epl
					+ ") and F.accountID ='"
					+ accountID
					+ "'"
					+ " and F.deviceID ='"
					+ deviceID
					+ "'"
					+ " and DX.thoiGian = F.timestamp "
					+ " order by F.timestamp, fuelLevel asc"
					+ ")"
					+ " UNION "
					+ " ( SELECT distinct F.accountID, F.deviceID, F.latitude, F.longitude, F.timestamp as col_time,"
					+ " (F.fuelLevel) , F.odometerKM, F.address, F.status, F.alarmType "
					+ " from tblDoXang1 DX"
					+ " inner join tblFuel F "
					+ " on DX.accountID = F.accountID and DX.deviceID = F.deviceID "
					+ " where (F.timestamp BETWEEN "
					+ epf
					+ " and "
					+ epl
					+ ") and F.accountID ='"
					+ accountID
					+ "'"
					+ " and F.deviceID ='"
					+ deviceID
					+ "'"
					+ " and F.timestamp = DX.thoiGian "
					+ " order by F.timestamp asc"
					+ ")"
					+ " UNION "
					+ " ( SELECT distinct F.accountID, F.deviceID, F.latitude, F.longitude, F.timestamp as col_time,"
					+ " F.fuelLevel as fuelLevel , F.odometerKM, F.address, F.status, F.alarmType "
					+ " from tblDoXang1 DX"
					+ " inner join tblFuel F "
					+ " on DX.accountID = F.accountID and DX.deviceID = F.deviceID "
					+ " where "
					+ "(F.timestamp BETWEEN "
					+ epf
					+ " and "
					+ epl
					+ ") and F.accountID ='"
					+ accountID
					+ "'"
					+ " and F.deviceID ='"
					+ deviceID
					+ "'"
					+ " and (F.timestamp < DX.thoiGian + '300' "
					+ " and F.timestamp > DX.thoiGian - '300') "
					+ " order by F.timestamp, F.fuelLevel asc"
					+ ")"
					+ " order by col_time, fuelLevel asc ";
			// Print.logInfo(sql);
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			double lastValue = 0;
			while (rs.next()) {
				{
					double currentValue = rs.getDouble("fuelLevel");
					double changeValue = lastValue == 0 ? 0 : currentValue
							- lastValue;
					lastValue = currentValue;
					reportFuel = new FuelData(rs.getString("accountID"),
							rs.getString("deviceID"), rs.getInt("col_time"),
							rs.getDouble("latitude"),
							rs.getDouble("longitude"), rs.getString("address"),
							currentValue, rs.getDouble("odometerKM"),
							rs.getInt("status"), changeValue,
							rs.getInt("alarmType"));
					list.add(reportFuel);
					/*
					 * if(fuel > 0){ changeValue = fuel; }
					 */
				}
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

	// MinhNV driver and device end

	// minhnv hanhTrinhCuaXe start

	public ResultSet hanhTrinhCuaXe(String AccountID, String tuNgay,
			String denNgay, String bienXe) {

		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call MinhNV_HoatDongCuaXe(?,?,?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, AccountID);
			cstmt.setString(2, bienXe);
			cstmt.setString(3, tuNgay);
			cstmt.setString(4, denNgay);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;
	}

	/*
	 * Mr Duan
	 */
	// DuanNP add
	public List<String> GetListDeviceByAccountID(String accountID)
			throws DBException {
		ResultSet rs = null;
		DBConnection dbc = null;
		Statement stmt = null;

		List<String> list = new ArrayList<String>();
		try {
			dbc = DBConnection.getDefaultConnection();
			String sql = "SELECT D.deviceID from gtse.Device D where accountID = '"
					+ accountID + "'";
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			while (rs.next()) {
				list.add(rs.getString(1));
			}
		} catch (SQLException sqe) {
			// TODO: handle exception
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

	// End DuanNP add

	// DuanNP add
	public ArrayList<DrivingTime> GetFuelConsumption(String accountID,
			String deviceID, String fromTime, String toTime, String timezone)
			throws DBException {
		ArrayList<DrivingTime> list = new ArrayList<DrivingTime>();
		ResultSet rs = null;
		DBConnection dbc = null;
		Statement stmt = null;
		DrivingTime fuelConsumption;
		try {

			dbc = DBConnection.getDefaultConnection();
			long epf = ConvertToEpoch(fromTime + " 00:00:00", timezone);
			long epl = ConvertToEpoch(toTime + " 23:59:59", timezone);
			String sql = "SELECT D.accountID,D.deviceID,D.startTime,D.stopTime,D.startAddress,D.endAddress,D.distance,D.fuelConsumption "
					+ "FROM gtse.tblDrivingTime2 D "
					+ "WHERE reportType = '1' and D.accountID = '"
					+ accountID
					+ "' AND D.deviceID ='"
					+ deviceID
					+ "'"
					+ "AND D.startTime BETWEEN '"
					+ epf
					+ "' AND '"
					+ epl
					+ "' order by D.startTime asc";

			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			while (rs.next()) {
				fuelConsumption = new DrivingTime(rs.getString("accountID"),
						rs.getString("deviceID"), rs.getInt("startTime"),
						rs.getInt("stopTime"), rs.getString("startAddress"),
						rs.getString("endAddress"), rs.getDouble("distance"),
						rs.getDouble("fuelConsumption"));
				list.add(fuelConsumption);
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

	public ArrayList<DrivingTime> GetFuelConsumption2(String accountID,
			String deviceID, String fromTime, String toTime, String timezone)
			throws DBException {
		ArrayList<DrivingTime> list = new ArrayList<DrivingTime>();
		ResultSet rs = null;
		DBConnection dbc = null;
		Statement stmt = null;
		DrivingTime fuelConsumption;
		try {
			dbc = DBConnection.getDefaultConnection();
			long epf = ConvertToEpoch(fromTime + " 00:00:00", timezone);
			long epl = ConvertToEpoch(toTime + " 23:59:59", timezone);
			String sql = "SELECT D.accountID,D.deviceID,D.startTime,D.stopTime,D.startAddress,D.endAddress,D.distance,D.fuelConsumption "
					+ "FROM gtse.tblDrivingTime2 D "
					+ "WHERE reportType = '2' and D.accountID = '"
					+ accountID
					+ "' AND D.deviceID ='"
					+ deviceID
					+ "'"
					+ "AND D.startTime BETWEEN '"
					+ epf
					+ "' AND '"
					+ epl
					+ "' order by D.startTime asc";

			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			while (rs.next()) {
				fuelConsumption = new DrivingTime(rs.getString("accountID"),
						rs.getString("deviceID"), rs.getInt("startTime"),
						rs.getInt("stopTime"), rs.getString("startAddress"),
						rs.getString("endAddress"), rs.getDouble("distance"),
						rs.getDouble("fuelConsumption"));
				list.add(fuelConsumption);
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

	// End DuanNP add
	// Mr Phuc Duan
	public List<String> GetListDeviceByAccountID232(String accountID)
			throws DBException {
		ResultSet rs = null;
		DBConnection dbc = null;
		Statement stmt = null;

		List<String> list = new ArrayList<String>();
		try {
			dbc = DBConnection.getDefaultConnection();
			String sql = "SELECT D.deviceID from gtse.Device D where deviceCode='TSTZ05RS232' and accountID = '"
					+ accountID + "' order by D.deviceID asc";
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			while (rs.next()) {
				list.add(rs.getString(1));
			}
		} catch (SQLException sqe) {
			// TODO: handle exception
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

	// *********************************************
	public ArrayList<DrivingTime> GetFuelConsumption232(String accountID,
			String deviceID, String fromTime, String toTime, String timezone)
			throws DBException {
		ArrayList<DrivingTime> list = new ArrayList<DrivingTime>();
		ResultSet rs = null;
		DBConnection dbc = null;
		Statement stmt = null;
		DrivingTime fuelConsumption;
		try {
			dbc = DBConnection.getDefaultConnection();
			long epf = ConvertToEpoch(fromTime + " 00:00:00", timezone);
			long epl = ConvertToEpoch(toTime + " 23:59:59", timezone);
			String sql = "SELECT D.accountID,D.deviceID,D.startTime,D.stopTime,D.startAddress,D.endAddress,D.distance,D.fuelConsumption "
					+ "FROM gtse.tblDrivingTime2 D "
					+ "WHERE reportType = '3' and D.accountID = '"
					+ accountID
					+ "' AND D.deviceID ='"
					+ deviceID
					+ "'"
					+ "AND D.startTime BETWEEN '"
					+ epf
					+ "' AND '"
					+ epl
					+ "' order by D.startTime asc";

			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			while (rs.next()) {
				fuelConsumption = new DrivingTime(rs.getString("accountID"),
						rs.getString("deviceID"), rs.getInt("startTime"),
						rs.getInt("stopTime"), rs.getString("startAddress"),
						rs.getString("endAddress"), rs.getDouble("distance"),
						rs.getDouble("fuelConsumption"));
				list.add(fuelConsumption);
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

	// minhnv hanhTrinhCuaXe End
	// Minhnv báo cáo ngày start
	public ResultSet baoCaoNgay(String ngay, String accountID, String deviceID) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call MinhNV_baoCaoNgay(?,?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, ngay);
			cstmt.setString(2, accountID);
			cstmt.setString(3, deviceID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;

	}

	public ResultSet baoCaoNgay_vuotToc(String ngay, String accountID,
			String deviceID) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call MinhNV_baoCaoNgay_vuotToc(?,?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, ngay);
			cstmt.setString(2, accountID);
			cstmt.setString(3, deviceID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;

	}

	public ResultSet baoCaoNgay_dungDo(String ngay, String accountID,
			String deviceID) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call MinhNV_baoCaoNgay_dungDo(?,?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, ngay);
			cstmt.setString(2, accountID);
			cstmt.setString(3, deviceID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;

	}

	public ResultSet baoCaoNgay_moCua(String ngay, String accountID,
			String deviceID) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call Minhnv_baoCaoNgay_moCua(?,?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, ngay);
			cstmt.setString(2, accountID);
			cstmt.setString(3, deviceID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;

	}

	// Minh nv báo cáo ngày end

	// Minhnv báo cáo tổng hợp start
	public ResultSet baoCaoTongHop(long ngay, String accountID, String deviceID) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call MinhNV_baoCaoTongHop(?,?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setLong(1, ngay);

			cstmt.setString(2, accountID);
			cstmt.setString(3, deviceID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;

	}

	public ResultSet baoCaoTongHop_vuotToc(long ngay, String accountID,
			String deviceID) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call MinhNV_baoCaoTongHop_vuotToc(?,?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setLong(1, ngay);

			cstmt.setString(2, accountID);
			cstmt.setString(3, deviceID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;

	}

	public ResultSet baoCaoTongHop_dungDo(long ngay, String accountID,
			String deviceID) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call MinhNV_baoCaoTongHop_dungDo(?,?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setLong(1, ngay);
			cstmt.setString(2, accountID);
			cstmt.setString(3, deviceID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;

	}

	public ResultSet baoCaoTongHop_moCua(long ngay, String accountID,
			String deviceID) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call Minhnv_baoCaoTongHop_moCua(?,?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setLong(1, ngay);
			cstmt.setString(2, accountID);
			cstmt.setString(3, deviceID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;

	}

	// Minh nv báo cáo tổng hợp end
	// Minhnv phan quyen start

	public ResultSet phanQuyen(String acc, String trangID) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call Minhnv_phanQuyen(?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, acc);
			cstmt.setString(2, trangID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;

	}

	public ResultSet selectTblTrang() {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call Minhnv_selectTblTrang()";
			CallableStatement cstmt = con.prepareCall(sql);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;

	}

	public ResultSet selectTblQuyen(String acc, String trangID) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call MinhNV_selectTblQuyen(?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, acc);
			cstmt.setString(2, trangID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;

	}

	public ResultSet deleteTblQuyen(String acc) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call MinhNV_deleteTblQuyen(?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, acc);

			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;

	}

	// ThanhNgC
	public ResultSet insertStopEvent(String acc, String dev) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call ThanhNgC_Insert_STOPEvent(?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, acc);
			cstmt.setString(2, dev);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;

	}

	public ResultSet insertTblQuyen(String acc, String trangID) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call Minhnv_insertTblQuyen(?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, acc);
			cstmt.setString(2, trangID);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;

	}

	// Minhnv phan quyen end

	// Minhnv bao cao nhien lieu start

	public ResultSet nhienLieu(String AccountID, String bienXe, String tuNgay,
			String denNgay) {

		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call Minhnv_nhienLieu(?,?,?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, AccountID);
			cstmt.setString(2, bienXe);
			cstmt.setString(3, tuNgay);
			cstmt.setString(4, denNgay);
			rs = cstmt.executeQuery();

		} catch (Exception e) {
		}
		return rs;
	}

	// Minhnv bao cao nhien lieu end

	// Hienntd add store baocaoquatocdo
	public ResultSet Select_Device(String accid) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call Hienntd_selectDevID(?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accid);
			rs = cstmt.executeQuery();
		} catch (Exception ex) {
		}
		return rs;
	}

	public ResultSet Select_Device1(String accid, String device) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call Hienntd_selectDevID1(?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accid);
			cstmt.setString(2, device);
			rs = cstmt.executeQuery();
		} catch (Exception ex) {
		}
		return rs;
	}

	public ResultSet bcquatocdo(String accid, String devid, Long ngay) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call Hienntd_bcquatocdo1(?,?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accid);
			cstmt.setString(2, devid);
			cstmt.setLong(3, ngay);
			rs = cstmt.executeQuery();
		} catch (Exception ex) {
		}
		return rs;
	}

	// Hienntd end
	public ResultSet Gettotaltime(String accid, String devid, Long ngay) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "call Hienntd_gettotaltime(?,?,?)";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accid);
			cstmt.setString(2, devid);
			cstmt.setLong(3, ngay);
			rs = cstmt.executeQuery();
		} catch (Exception ex) {
		}
		return rs;
	}

	public ResultSet reportDungDo(String accountid, String tuNgay,
			String denNgay, String deviceID) {

		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call Baocaodungdo(?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accountid);
			cstmt.setString(2, tuNgay);
			cstmt.setString(3, denNgay);
			cstmt.setString(4, deviceID);

			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}

		return rs;
	}

	public ResultSet reportDongMo(String accountid, String tuNgay,
			String denNgay, String deviceID) {

		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call Baocaodongmo(?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accountid);
			cstmt.setString(2, tuNgay);
			cstmt.setString(3, denNgay);
			cstmt.setString(4, deviceID);

			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}

		return rs;
	}

	// Hienntd add bao cao dung do nghi dinh 91

	public ResultSet reportDongMoCua(String accountid, String tuNgay,
			String denNgay, String deviceID) {

		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call Hienntd_Baocaodongmo(?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accountid);
			cstmt.setString(2, tuNgay);
			cstmt.setString(3, denNgay);
			cstmt.setString(4, deviceID);

			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}

		return rs;
	}

	// Hiennntd add bao cao thoi gian lai xe
	public String ChangelongtoDate(Long ngay) {
		ResultSet rs = null;
		Connection con = Connect();
		String ngay1 = "";
		try {
			String sql = "{call Hienntd_Changelongtodate(?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setLong(1, ngay);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				ngay1 = rs.getString(1);
			}
		} catch (Exception e) {

		}
		return ngay1;
	}

	public ResultSet GetDevIDFromDrivingTime(String accid) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call Hienntd_SelectDevIDFromDriving(?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accid);
			rs = cstmt.executeQuery();
		} catch (Exception e) {

		}
		return rs;
	}

	public ResultSet GetDevIDFromDrivingTime1(String accid, String device) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call Hienntd_SelectDevIDFromDriving1(?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accid);
			cstmt.setString(2, device);
			rs = cstmt.executeQuery();
		} catch (Exception e) {

		}
		return rs;
	}

	public ResultSet GetReportFromDrivingTime(String accid, String devid,
			Long ngay) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call Hienntd_bctglaixe(?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accid);
			cstmt.setString(2, devid);
			cstmt.setLong(3, ngay);
			rs = cstmt.executeQuery();
		} catch (Exception e) {

		}
		return rs;
	}

	public int CountDrivingTime(String accid, String devid, Long ngay) {
		ResultSet rs = null;
		Connection con = Connect();
		int count = 0;
		try {
			String sql = "{call Hienntd_CountDrivingTime(?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accid);
			cstmt.setString(2, devid);
			cstmt.setLong(3, ngay);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {

		}
		return count;
	}

	// Hienntd end

	// Lienptk device info
	public ResultSet Device_selectAll(String accountid, int dieuhoa, int cua,
			int nd91, int xang) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call Device_filter(?,?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accountid);
			cstmt.setInt(2, dieuhoa);
			cstmt.setInt(3, cua);
			cstmt.setInt(4, nd91);
			cstmt.setInt(5, xang);
			rs = cstmt.executeQuery();
		} catch (Exception e) {

		}
		return rs;
	}

	public ResultSet Hienntd_Device_selectAll(String devid, String st) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call Hienntd_Device_filter(?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, devid);
			cstmt.setString(2, st);
			rs = cstmt.executeQuery();
		} catch (Exception e) {

		}
		return rs;
	}

	public ResultSet Device_select(String DeviceID, String accountid) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call Lienptk_deviceSelect(?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, DeviceID);
			cstmt.setString(2, accountid);
			rs = cstmt.executeQuery();
		} catch (Exception e) {

		}
		return rs;
	}

	public ResultSet Device_selectAll_(String accid) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call Lienptl_deviceSelectAll(?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, accid);
			rs = cstmt.executeQuery();
		} catch (Exception e) {

		}
		return rs;
	}

	public ResultSet demoText() {
		Print.logDebug("Thang dai ngo ");
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "SELECT * FROM tblGetStopEvent1;";
			CallableStatement cstmt = con.prepareCall(sql);
			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}
		return rs;
	}

	public ResultSet Device_selectAll_1(String devid, String accid) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call Hienntd_deviceAll1(?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, devid);
			cstmt.setString(2, accid);
			rs = cstmt.executeQuery();
		} catch (Exception e) {

		}
		return rs;
	}

	public ResultSet Device_update(String DeviceID, String accountid,
			int dieuhoa, int cua, int nd91, int xang) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call Lienptk_DeviceUpdate(?,?,?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, DeviceID);
			cstmt.setString(2, accountid);
			cstmt.setInt(3, dieuhoa);
			cstmt.setInt(4, cua);
			cstmt.setInt(5, nd91);
			cstmt.setInt(6, xang);
			rs = cstmt.executeQuery();
		} catch (Exception e) {

		}
		return rs;
	}

	public ResultSet Device_update2(String DeviceID, String accountid,
			int dieuhoa, int cua, int nd91, int xang, int qlSIM) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call Lienptk_DeviceUpdate(?,?,?,?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, DeviceID);
			cstmt.setString(2, accountid);
			cstmt.setInt(3, dieuhoa);
			cstmt.setInt(4, cua);
			cstmt.setInt(5, nd91);
			cstmt.setInt(6, xang);
			cstmt.setInt(7, qlSIM);
			rs = cstmt.executeQuery();
		} catch (Exception e) {

		}
		return rs;
	}

	public ResultSet updateLoaiXe(String Dev, String acc, int loaiXeID) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call MinhNV_updateLoaiXe(?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, Dev);
			cstmt.setString(2, acc);
			cstmt.setInt(3, loaiXeID);
			rs = cstmt.executeQuery();
		} catch (Exception e) {

		}
		return rs;
	}

	// Lienptk end

	// Hienntd add
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

	// báo cáo chi tiết hoạt động xe
	public EventData[] GetReportDetail(String ngaybatdau, String ngayketthuc,
			String device, String timezone, String accountID)
			throws DBException {
		EventData ed[] = null;
		try {
			long epf = ConvertToEpoch(ngaybatdau, timezone);
			long epl = ConvertToEpoch(ngayketthuc, timezone);
			EventData.LimitType lmType = EventData.LimitType.FIRST;
			ed = EventData.getRangeEvents(accountID, device, epf, epl, null,
					false, lmType, 2000, true, null);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ed;
	}

	// báo cáo thời gian máy xúc
	public EventData[] GetReportDetail1(String ngaybatdau, String ngayketthuc,
			String device, String timezone, String accountID)
			throws DBException {
		EventData ed[] = null;
		try {
			long epf = ConvertToEpoch(ngaybatdau, timezone);
			long epl = ConvertToEpoch(ngayketthuc, timezone);
			EventData.LimitType lmType = EventData.LimitType.FIRST;
			ed = EventData.getRangeEvents(accountID, device, epf, epl, null,
					false, lmType, 2000, true, "(statusCode = 64791)", null);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ed;
	}

	// báo cáo vượt tốc độ
	public EventData[] GetTotalSpeedLine(String ngaybatdau, String ngayketthuc,
			String device, String speed, String timezone, String accountID)
			throws DBException {
		EventData ed[] = null;
		try {
			long epf = ConvertToEpoch(ngaybatdau, timezone);
			long epl = ConvertToEpoch(ngayketthuc, timezone);
			EventData.LimitType lmType = EventData.LimitType.FIRST;
			ed = EventData.getRangeEvents(accountID, device, epf, epl, null,
					false, lmType, 2000, true, "(speedkph > " + speed + ")",
					null);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ed;

	}

	// báo cáo đóng mở cửa
	public EventData[] GetReportdongmo(String ngaybatdau, String ngayketthuc,
			String device, String timezone, String accountID)
			throws DBException {
		EventData ed[] = null;
		try {
			long epf = ConvertToEpoch(ngaybatdau, timezone);
			long epl = ConvertToEpoch(ngayketthuc, timezone);
			EventData.LimitType lmType = EventData.LimitType.FIRST;
			ed = EventData.getRangeEvents(accountID, device, epf, epl, null,
					false, lmType, 2000, true, null);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ed;

	}

	/*
	 * get camera list by date and device
	 * 
	 * @date : date input
	 * 
	 * @divice: deviceid input out: resultset.
	 */
	public ResultSet GetCamera(String date, String device, int page,
			int pagesize) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			int start = (page - 1) * pagesize;
			String sql = "select * ,time(createdtime) as timecreated from imagedata where date(createdtime) = str_to_date( '"
					+ date
					+ "', '%d/%m/%Y' ) and DeviceID = '"
					+ device
					+ "' order by createdtime desc limit "
					+ start
					+ ","
					+ pagesize;
			dbc = DBConnection.getDefaultConnection();
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
		} catch (Exception e) {
			throw new DBException(e.getMessage());
		}
		return rs;
	}

	// Hình ảnh camera đóng kết nối
	// Báo cáo dừng đỗ nghị định 91
	public ArrayList<dungdo> StaticReportDungDo(String accountID,
			String deviceID, String fromTime, String toTime, String timezone)
			throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<dungdo> list = new ArrayList<dungdo>();
		dungdo reportdungdo;
		try {
			long epf = ConvertToEpoch(fromTime + " 00:00:00", timezone);
			long epl = ConvertToEpoch(toTime + " 23:59:59", timezone);

			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT * FROM tblDungDo ed WHERE  (ed.`timestamp` BETWEEN "
					+ epf
					+ " and "
					+ epl
					+ ") and ed.accountId= '"
					+ accountID
					+ "' and ed.deviceID = '"
					+ deviceID
					+ "' ORDER BY ed.`timestamp` desc";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				reportdungdo = new dungdo(rs.getString("deviceID"),
						rs.getInt("timestamp"), rs.getInt("duration"),
						rs.getInt("type"), rs.getDouble("latitude"),
						rs.getDouble("longitude"), rs.getString("address"),
						rs.getInt("num"), rs.getString("accountID"));
				list.add(reportdungdo);
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

	public ArrayList<CheckSIM> StaticReportQLSIM(String accountID,
			String deviceID, String fromTime, String toTime, String timezone,
			String reportType) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null, stmt2 = null;
		ResultSet rs = null, rs2 = null;
		ArrayList<CheckSIM> list = new ArrayList<CheckSIM>();
		CheckSIM reportCheckSIM;
		try {

			dbc = DBConnection.getDefaultConnection();

			String command = "SELECT ed.deviceID,ed.decription,ed.AccountID,ed.reportType,ed.timestamp,ed.imei,ed.phoneCX,ed.simPhoneNumber,ed.decription,ed.checkThongtin "
					+ " FROM tblCheckSim ed  WHERE   "
					+ " ed.accountId= '"
					+ accountID
					+ "' and ed.deviceID = '"
					+ deviceID
					+ "' and ed.reportType = '2' "
					+ " ORDER BY ed.`timestamp` desc LIMIT 1";

			String command2 = "SELECT * "
					+ "  FROM tblCheckSim tbcs  WHERE tbcs.reportType = '1' and tbcs.accountId='"
					+ accountID + "' and tbcs.deviceID = '" + deviceID + "'"
					+ " ORDER BY timestamp desc LIMIT 1";

			stmt = dbc.execute(command);
			stmt2 = dbc.execute(command2);

			rs = stmt.getResultSet();
			rs2 = stmt2.getResultSet();

			String checkThongTin = "", _deviceID = "", AccountID = "", imei = "", phoneCX = "", simPhoneNumber = "", _reportType = "", decription = "";
			int timestamp = 0, timestamp2 = 0;

			while (rs.next()) {
				_deviceID = rs.getString("deviceID");
				timestamp = rs.getInt("timestamp");
				AccountID = rs.getString("AccountID");
				checkThongTin = rs.getString("checkThongtin");
				imei = rs.getString("imei");
				phoneCX = rs.getString("phoneCX");
				simPhoneNumber = rs.getString("simPhoneNumber");
				decription = rs.getString("decription");
				_reportType = rs.getString("reportType");
			}

			while (rs2.next()) {
				checkThongTin += " " + rs2.getString("checkThongtin");
				timestamp2 = rs2.getInt("timestamp");
			}

			if (_deviceID != "") {

				if (timestamp2 > timestamp)
					timestamp = timestamp2;

				reportCheckSIM = new CheckSIM(_deviceID, timestamp, AccountID,
						checkThongTin, imei, phoneCX, simPhoneNumber,
						decription, _reportType);
				list.add(reportCheckSIM);
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
			dbc.closeConnection();
			DBConnection.release(dbc);
		}

		return list;
	}

	public CheckSIM StaticInforlQLSIMSMS(String accountID, String deviceID,
			String timezone, String reportType, String timestamp)
			throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<CheckSIM> list = new ArrayList<CheckSIM>();
		CheckSIM reportCheckSIM = null;
		try {

			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT * FROM tblCheckSim ed WHERE  "
					+ " ed.accountId= '" + accountID + "' and ed.deviceID = '"
					+ deviceID + "' and ed.reportType = '" + reportType
					+ "' AND ed.`timestamp` >= " + timestamp + "- 300 "// 1423888600
																		// 1423896540
					+ " ORDER BY ed.`timestamp` desc LIMIT 0,1";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();

			while (rs.next()) {
				reportCheckSIM = new CheckSIM(rs.getString("deviceID"),
						rs.getInt("timestamp"), rs.getString("AccountID"),
						rs.getString("checkThongtin"), rs.getString("imei"),
						rs.getString("phoneCX"),
						rs.getString("simPhoneNumber"),
						rs.getString("decription"), rs.getString("reportType"));
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

		return reportCheckSIM;
	}

	public CheckSIM StaticInforlQLSIM(String accountID, String deviceID,
			String timezone, String reportType) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<CheckSIM> list = new ArrayList<CheckSIM>();
		CheckSIM reportCheckSIM = null;
		try {

			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT * FROM tblCheckSim ed WHERE  "
					+ " ed.accountId= '" + accountID + "' and ed.deviceID = '"
					+ deviceID + "' and ed.reportType = '" + reportType
					+ "' ORDER BY ed.`timestamp` desc LIMIT 0,1";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();

			while (rs.next()) {
				reportCheckSIM = new CheckSIM(rs.getString("deviceID"),
						rs.getInt("timestamp"), rs.getString("AccountID"),
						rs.getString("checkThongtin"), rs.getString("imei"),
						rs.getString("phoneCX"),
						rs.getString("simPhoneNumber"),
						rs.getString("decription"), rs.getString("reportType"));

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

		return reportCheckSIM;
	}
	

	public String getSimPhoneNumber(String deviceID) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;

		String reportCheckSIM = "";
		try {

			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT * FROM Device ed WHERE  ed.`deviceID` ='"
					+ deviceID + "'";

			stmt = dbc.execute(command);
			rs = stmt.getResultSet();

			while (rs.next()) {
				reportCheckSIM = rs.getString("simPhoneNumber");
			}
		} catch (SQLException sqe) {
			reportCheckSIM = sqe.toString();
			// throw new DBException("ReportByStatusCode", sqe);
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
					reportCheckSIM = t.toString();
				}
			}
			DBConnection.release(dbc);
		}

		return reportCheckSIM;
	}

	// Báo cáo mở cửa nghị định 91
	public ArrayList<MoCua> StaticReportMoCua(String accountID,
			String deviceID, String fromTime, String toTime, String timezone)
			throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<MoCua> list = new ArrayList<MoCua>();
		MoCua reportmocua;
		try {
			long epf = ConvertToEpoch(fromTime + " 00:00:00", timezone);
			long epl = ConvertToEpoch(toTime + " 23:59:59", timezone);

			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT * FROM tblMoCua ed WHERE  (ed.`startTime` BETWEEN "
					+ epf
					+ " and "
					+ epl
					+ ") and ed.accountId= '"
					+ accountID
					+ "' and ed.deviceID = '"
					+ deviceID
					+ "' ORDER BY ed.`startTime` ASC;";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				reportmocua = new MoCua(rs.getString("deviceID"),
						rs.getInt("startTime"), rs.getInt("endTime"),
						rs.getDouble("latitude"), rs.getDouble("longitude"),
						rs.getString("address"), rs.getString("accountID"),
						rs.getString("endAddress"), rs.getDouble("endLat"),
						rs.getDouble("endLon"));
				list.add(reportmocua);
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

	// Báo cáo quá tốc độ nghị định 91
	public ArrayList<VuotToc> StaticReportVuotToc(String accountID,
			String deviceID, long Time) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<VuotToc> list = new ArrayList<VuotToc>();
		VuotToc reportvuottoc;
		try {

			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT * FROM tblVuotToc ed WHERE  (ed.`startTime` BETWEEN "
					+ Time
					+ " and "
					+ (Time + 86399)
					+ ") and ed.accountId= '"
					+ accountID
					+ "' and ed.deviceID = '"
					+ deviceID
					+ "' ORDER BY ed.`startTime` ASC";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				reportvuottoc = new VuotToc(rs.getString("deviceID"),
						rs.getInt("startTime"), rs.getInt("stopTime"),
						rs.getDouble("startSpeed"), rs.getDouble("endSpeed"),
						rs.getDouble("maxSpeed"), rs.getDouble("latitude"),
						rs.getDouble("longitude"), rs.getInt("num"),
						rs.getString("address"), rs.getString("accountID"),
						rs.getString("endAddress"), rs.getDouble("endLat"),
						rs.getDouble("endLon"));
				list.add(reportvuottoc);
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

	// Báo cáo thời gian lái xe nghị định 91
	public ArrayList<DrivingTime> StaticReportDrivingTime(String accountID,
			String deviceID, long Time) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<DrivingTime> list = new ArrayList<DrivingTime>();
		DrivingTime reportDrivingTime;
		try {

			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT * FROM tblDrivingTime ed WHERE  (ed.`startTime` BETWEEN "
					+ Time
					+ " and "
					+ (Time + 86399)
					+ ") and ed.accountId= '"
					+ accountID
					+ "' and ed.deviceID = '"
					+ deviceID
					+ "' ORDER BY ed.`startTime` ASC";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				reportDrivingTime = new DrivingTime(rs.getString("accountID"),
						rs.getString("deviceID"), rs.getInt("startTime"),
						rs.getInt("stopTime"), rs.getDouble("startLatitude"),
						rs.getDouble("startLongitude"),
						rs.getString("startAddress"),
						rs.getDouble("endLatitude"),
						rs.getDouble("endLongitude"),
						rs.getString("endAddress"), rs.getDouble("avgSpeed"),
						rs.getDouble("maxSpeed"),
						rs.getDouble("distance") / 1000);
				list.add(reportDrivingTime);
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

	// Báo cáo thời gian lái xe nghị định 91 Lienptk sua
	public ArrayList<DrivingTime> StaticReportDrivingTime1(String accountID,
			String deviceID, long Time) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<DrivingTime> list = new ArrayList<DrivingTime>();
		DrivingTime reportDrivingTime;
		try {

			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT * FROM tblDrivingTime ed left join Driver d on ed.driverID=d.driverID and ed.accountID=d.accountID WHERE  (ed.`startTime` BETWEEN "
					+ Time
					+ " and "
					+ (Time + 86399)
					+ ") and ed.accountId= '"
					+ accountID
					+ "' and ed.deviceID = '"
					+ deviceID
					+ "' ORDER BY ed.`startTime` ASC";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				reportDrivingTime = new DrivingTime(rs.getString("accountID"),
						rs.getString("deviceID"), rs.getInt("startTime"),
						rs.getInt("stopTime"), rs.getDouble("startLatitude"),
						rs.getDouble("startLongitude"),
						rs.getString("startAddress"),
						rs.getDouble("endLatitude"),
						rs.getDouble("endLongitude"),
						rs.getString("endAddress"), rs.getDouble("avgSpeed"),
						rs.getDouble("maxSpeed"),
						rs.getDouble("distance") / 1000,
						rs.getString("displayName"),
						rs.getString("description"));
				list.add(reportDrivingTime);
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

	public Integer StaticCounttDrivingTime(String accountID, long Time)
			throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		// ArrayList<String> list = new ArrayList<String>();
		int counttDrivingTime = 0;
		try {

			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT count(*) FROM tblDrivingTime ed WHERE  (ed.`startTime` BETWEEN "
					+ Time
					+ " and "
					+ (Time + 86399)
					+ ") and ed.accountId= '"
					+ accountID + "'";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				// countDrivingTime = new DrivingTime(
				// rs.getString("accountID"), rs.getString("deviceID"),
				// rs.getInt("startTime"), rs.getInt("stopTime"),
				// rs.getDouble("startLatitude"),
				// rs.getDouble("startLongitude"), rs.getString("startAddress"),
				// rs.getDouble("endLatitude"),rs.getDouble("endLongitude"),rs.getString("endAddress"),rs.getDouble("avgSpeed"),rs.getDouble("maxSpeed"),rs.getDouble("distance"));
				// list.add(reportDrivingTime);
				counttDrivingTime = rs.getInt(1);
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

		return counttDrivingTime;
	}

	// Báo cáo ngày nghị định 91 Số lần vượt tốc độ
	public Integer StaticCountOverSpeed(String accountID, String DeviceID,
			String Time, String timezone) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		// ArrayList<String> list = new ArrayList<String>();
		int counttOverSpeed = 0;
		try {
			long fr = ConvertToEpoch(Time + " 00:00:00", timezone);
			long to = ConvertToEpoch(Time + " 23:59:59", timezone);
			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT count(tv.deviceID) FROM tblVuotToc tv WHERE  (tv.stopTime BETWEEN "
					+ fr
					+ " and "
					+ to
					+ ") and tv.accountId= '"
					+ accountID
					+ "' AND tv.deviceID ='" + DeviceID + "'";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				counttOverSpeed = rs.getInt(1);
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

		return counttOverSpeed;
	}

	// Báo cáo ngày nghị định 91 Số lần dừng dỗ
	public Integer StaticCountDungDo(String accountID, String DeviceID,
			String Time, String timezone) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		// ArrayList<String> list = new ArrayList<String>();
		int counttOverSpeed = 0;
		try {
			long fr = ConvertToEpoch(Time + " 00:00:00", timezone);
			long to = ConvertToEpoch(Time + " 23:59:59", timezone);
			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT count(tv.deviceID) FROM tblDungDo tv WHERE  (tv.`timestamp` BETWEEN "
					+ fr
					+ " and "
					+ to
					+ ") and tv.accountId= '"
					+ accountID
					+ "' AND tv.deviceID ='" + DeviceID + "'";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				counttOverSpeed = rs.getInt(1);
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

		return counttOverSpeed;
	}

	// Báo cáo ngày nghị định 91 Số lần mở cửa
	public Integer StaticCountMoCua(String accountID, String DeviceID,
			String Time, String timezone) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		// ArrayList<String> list = new ArrayList<String>();
		int countMoCua = 0;
		try {
			long fr = ConvertToEpoch(Time + " 00:00:00", timezone);
			long to = ConvertToEpoch(Time + " 23:59:59", timezone);
			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT COUNT(cua) as 'moCua' FROM EventData tv WHERE  (tv.`timestamp` BETWEEN "
					+ fr
					+ " and "
					+ to
					+ ") and tv.accountID= '"
					+ accountID
					+ "' AND tv.deviceID ='" + DeviceID + "' and tv.cua =2";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				countMoCua = rs.getInt(1);
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

		return countMoCua;
	}

	// Báo cáo ngày nghị định 91 Tổng thời gian dừng đỗ
	public Integer StaticSumDungDo(String accountID, String DeviceID,
			String Time, String timezone) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		// ArrayList<String> list = new ArrayList<String>();
		int counttOverSpeed = 0;
		try {
			long fr = ConvertToEpoch(Time + " 00:00:00", timezone);
			long to = ConvertToEpoch(Time + " 23:59:59", timezone);
			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT SUM(tv.duration) FROM tblDungDo tv WHERE  (tv.`timestamp` BETWEEN "
					+ fr
					+ " and "
					+ to
					+ ") and tv.accountId= '"
					+ accountID
					+ "' AND tv.deviceID ='" + DeviceID + "'";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				counttOverSpeed = rs.getInt(1);
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

		return counttOverSpeed;
	}

	// Báo cáo ngày nghị định 91 Báo cáo ngày
	public ArrayList<DrivingTime1> StaticReportDrivingTime1(String accountID,
			String deviceID, String Time, String timezone) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<DrivingTime1> list = new ArrayList<DrivingTime1>();
		DrivingTime1 reportDrivingTime;
		try {
			long fr = ConvertToEpoch(Time + " 00:00:00", timezone);
			long to = ConvertToEpoch(Time + " 23:59:59", timezone);
			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT ROUND((SUM(Distance)/1000),2) as 'quangDuong',ROUND(MAX(td.maxSpeed),2) as 'vtToiDa',ROUND(AVG(avgSpeed),2) as 'vtTrungBinh',SUM(td.stopTime-td.startTime) as 'tgLai' FROM tblDrivingTime td WHERE "
					+ " td.accountID like '"
					+ accountID
					+ "' AND td.deviceID like '"
					+ deviceID
					+ "' and  (td.stopTime BETWEEN " + fr + " and " + to + ")";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				reportDrivingTime = new DrivingTime1(
						rs.getDouble("quangDuong"), rs.getDouble("vtToiDa"),
						rs.getDouble("vtTrungBinh"), rs.getInt("tgLai"));
				list.add(reportDrivingTime);
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

	// Báo cáo tổng hợp nghị định 91 Số lần vượt tốc độ
	public Integer StaticCountOverSpeed1(String accountID, String DeviceID,
			Long Time, String driverID) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		// ArrayList<String> list = new ArrayList<String>();
		int counttOverSpeed = 0;
		try {
			dbc = DBConnection.getDefaultConnection();
			String command = "";
			if (driverID.equals("")) {
				command = "SELECT count(tv.deviceID) FROM tblVuotToc tv WHERE  (tv.stopTime BETWEEN "
						+ Time
						+ " and "
						+ (Time + 86399)
						+ ") and tv.accountId= '"
						+ accountID
						+ "' AND tv.deviceID ='" + DeviceID + "'";
			} else {
				command = "SELECT count(tv.deviceID) FROM tblVuotToc tv WHERE  (tv.stopTime BETWEEN "
						+ Time
						+ " and "
						+ (Time + 86399)
						+ ") and tv.accountId= '"
						+ accountID
						+ "' AND tv.deviceID ='"
						+ DeviceID
						+ "' and driverID = '" + driverID + "'";
			}
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				counttOverSpeed = rs.getInt(1);
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

		return counttOverSpeed;
	}

	// Báo cáo tổng hợp nghị định 91 Số lần dừng dỗ
	public Integer StaticCountDungDo1(String accountID, String DeviceID,
			Long Time, String driverID) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		// ArrayList<String> list = new ArrayList<String>();
		int countdungdo = 0;
		try {
			dbc = DBConnection.getDefaultConnection();
			String command = "";
			if (driverID.equals("")) {
				command = "SELECT count(tv.deviceID) FROM tblDungDo tv WHERE  (tv.`timestamp` BETWEEN "
						+ Time
						+ " and "
						+ (Time + 86399)
						+ ") and tv.accountId= '"
						+ accountID
						+ "' AND tv.deviceID ='" + DeviceID + "'";
			} else {
				command = "SELECT count(tv.deviceID) FROM tblDungDo tv WHERE  (tv.`timestamp` BETWEEN "
						+ Time
						+ " and "
						+ (Time + 86399)
						+ ") and tv.accountId= '"
						+ accountID
						+ "' AND tv.deviceID ='"
						+ DeviceID
						+ "' and driverID = '" + driverID + "'";
			}

			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				countdungdo = rs.getInt(1);
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

		return countdungdo;
	}

	// Báo cáo tổng hợp nghị định 91 Tổng thời gian dừng đỗ
	public Integer StaticSumDungDo1(String accountID, String DeviceID,
			Long Time, String driverID) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		// ArrayList<String> list = new ArrayList<String>();
		int tgd = 0;
		try {
			dbc = DBConnection.getDefaultConnection();
			String command = "";
			if (driverID.equals("")) {
				command = "SELECT SUM(tv.duration) FROM tblDungDo tv WHERE  (tv.`timestamp` BETWEEN "
						+ Time
						+ " and "
						+ (Time + 86399)
						+ ") and tv.accountId= '"
						+ accountID
						+ "' AND tv.deviceID ='" + DeviceID + "'";
			} else {
				command = "SELECT SUM(tv.duration) FROM tblDungDo tv WHERE  (tv.`timestamp` BETWEEN "
						+ Time
						+ " and "
						+ (Time + 86399)
						+ ") and tv.accountId= '"
						+ accountID
						+ "' AND tv.deviceID ='"
						+ DeviceID
						+ "' and driverID = '" + driverID + "'";
			}

			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				tgd = rs.getInt(1);
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

		return tgd;
	}

	// Báo cáo tổng hợp nghị định 91 Số lần mở cửa
	public Integer StaticCountMoCua1(String accountID, String DeviceID,
			Long Time, String driverID) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		// ArrayList<String> list = new ArrayList<String>();
		int countMoCua = 0;
		try {
			dbc = DBConnection.getDefaultConnection();
			String command = "";
			if (driverID.equals("")) {
				command = "SELECT COUNT(cua) as 'moCua' FROM EventData ev WHERE (ev.`timestamp` BETWEEN "
						+ Time
						+ " and "
						+ (Time + 86399)
						+ ") and ev.accountID ='"
						+ accountID
						+ "' AND ev.deviceID = '"
						+ DeviceID
						+ "' and  ev.Cua=2;";
			} else {
				command = "SELECT COUNT(cua) as 'moCua' FROM EventData ev WHERE (ev.`timestamp` BETWEEN "
						+ Time
						+ " and "
						+ (Time + 86399)
						+ ") and ev.accountID ='"
						+ accountID
						+ "' AND ev.deviceID = '"
						+ DeviceID
						+ "' and driverID = '" + driverID + "'  and  ev.Cua=2;";
			}

			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				countMoCua = rs.getInt(1);
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

		return countMoCua;
	}

	// Báo cáo tổng hợp nghị định 91 Báo cáo ngày
	public ArrayList<DrivingTime1> StaticReportTonghop(String accountID,
			String deviceID, Long Time, String driverID) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<DrivingTime1> list = new ArrayList<DrivingTime1>();
		DrivingTime1 reportDrivingTime;
		try {
			dbc = DBConnection.getDefaultConnection();
			String command = "";
			if (driverID.equals("")) {
				command = "SELECT  ROUND((SUM(Distance)/1000),2) as 'quangDuong',ROUND(MAX(td.maxSpeed),2) as 'vtToiDa',ROUND(AVG(avgSpeed),2) as 'vtTrungBinh',SUM(td.stopTime-td.startTime) as 'tgLai'"
						+ "from tblDrivingTime td WHERE (td.startTime BETWEEN "
						+ Time
						+ " and "
						+ (Time + 86399)
						+ ")and td.accountID = '"
						+ accountID
						+ "' AND td.deviceID = '" + deviceID + "'";
			} else {
				command = "SELECT  ROUND((SUM(Distance)/1000),2) as 'quangDuong',ROUND(MAX(td.maxSpeed),2) as 'vtToiDa',ROUND(AVG(avgSpeed),2) as 'vtTrungBinh',SUM(td.stopTime-td.startTime) as 'tgLai'"
						+ "from tblDrivingTime td WHERE (td.startTime BETWEEN "
						+ Time
						+ " and "
						+ (Time + 86399)
						+ ")and td.accountID = '"
						+ accountID
						+ "' AND td.deviceID = '"
						+ deviceID
						+ "' and driverID = '" + driverID + "'";
			}

			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				reportDrivingTime = new DrivingTime1(
						rs.getDouble("quangDuong"), rs.getDouble("vtToiDa"),
						rs.getDouble("vtTrungBinh"), rs.getInt("tgLai"));
				list.add(reportDrivingTime);
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

	// Báo cáo tổng hợp nghị định 91 Báo cáo ngày
	public List<String[]> Device_SelectFilter(String devid, String accid,
			String dk) throws DBException {
		ResultSet rs = null;
		DBConnection dbc = null;
		Statement stmt = null;
		// String id="";
		// if(acc.equals(" ")||acc.toUpperCase().equals("ADMIN"))
		// {
		// id="ADMIN";
		// }
		List<String[]> GetGroup = new ArrayList<String[]>();
		try {
			dbc = DBConnection.getDefaultConnection();
			String sql = "select deviceid, imeiNumber, description, simPhoneNumber, deviceCode, dieuhoa, cua, nd91, isActive, equipmentType,xang,CheckSim"
					+ " from Device  where deviceID='"
					+ devid
					+ "' and accountID='" + accid + "' " + dk;
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			while (rs.next()) {
				String[] gz = new String[11];
				gz[0] = rs.getString("deviceid");
				gz[1] = rs.getString("imeiNumber");
				gz[2] = rs.getString("description");
				gz[3] = rs.getString("simPhoneNumber");
				gz[4] = rs.getString("deviceCode");
				gz[5] = rs.getString("dieuhoa");
				gz[6] = rs.getString("cua");
				gz[7] = rs.getString("nd91");
				gz[8] = rs.getString("isActive");
				gz[9] = rs.getString("equipmentType");
				gz[10] = rs.getString("xang");
				gz[11] = rs.getString("CheckSim");
				GetGroup.add(gz);
			}
		} catch (SQLException sqe) {
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
		return GetGroup;
	}

	public Integer qryCheckIfAccountNeedManaging(String accountID)
			throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		// ArrayList<String> list = new ArrayList<String>();
		int status = 0;
		try {

			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT bNeedManaged FROM gtse.tblGetStopEvent WHERE accountID = '"
					+ accountID + "'";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				// countDrivingTime = new DrivingTime(
				// rs.getString("accountID"), rs.getString("deviceID"),
				// rs.getInt("startTime"), rs.getInt("stopTime"),
				// rs.getDouble("startLatitude"),
				// rs.getDouble("startLongitude"), rs.getString("startAddress"),
				// rs.getDouble("endLatitude"),rs.getDouble("endLongitude"),rs.getString("endAddress"),rs.getDouble("avgSpeed"),rs.getDouble("maxSpeed"),rs.getDouble("distance"));
				// list.add(reportDrivingTime);
				status = rs.getInt(1);
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

		return status;
	}

	// Báo cáo tổng hợp đội xe
	public Integer StaticSelectstatus(String accountID, String deviceID)
			throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		// ArrayList<String> list = new ArrayList<String>();
		int status = 0;
		try {

			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT stopStatus FROM Device  WHERE   accountID= '"
					+ accountID + "' and deviceID='" + deviceID + "'";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				// countDrivingTime = new DrivingTime(
				// rs.getString("accountID"), rs.getString("deviceID"),
				// rs.getInt("startTime"), rs.getInt("stopTime"),
				// rs.getDouble("startLatitude"),
				// rs.getDouble("startLongitude"), rs.getString("startAddress"),
				// rs.getDouble("endLatitude"),rs.getDouble("endLongitude"),rs.getString("endAddress"),rs.getDouble("avgSpeed"),rs.getDouble("maxSpeed"),rs.getDouble("distance"));
				// list.add(reportDrivingTime);
				status = rs.getInt(1);
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

		return status;
	}

	//
	public Integer StaticSelectstatus(String deviceID) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		// ArrayList<String> list = new ArrayList<String>();
		int status = 0;
		try {

			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT stopStatus FROM Device  WHERE  deviceID='"
					+ deviceID + "'";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				// countDrivingTime = new DrivingTime(
				// rs.getString("accountID"), rs.getString("deviceID"),
				// rs.getInt("startTime"), rs.getInt("stopTime"),
				// rs.getDouble("startLatitude"),
				// rs.getDouble("startLongitude"), rs.getString("startAddress"),
				// rs.getDouble("endLatitude"),rs.getDouble("endLongitude"),rs.getString("endAddress"),rs.getDouble("avgSpeed"),rs.getDouble("maxSpeed"),rs.getDouble("distance"));
				// list.add(reportDrivingTime);
				status = rs.getInt(1);
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

		return status;
	}

	public ArrayList<LiveEvent> GetLiveReportEvent(String accountID,
			String deviceID, String fromTime, String toTime, String timezone)
			throws DBException {
		ArrayList<LiveEvent> list = new ArrayList<LiveEvent>();
		try {
			EventData ed[] = null;
			long epf = ConvertToEpoch(fromTime + " 00:00:00", timezone);
			long epl = ConvertToEpoch(toTime + " 23:59:59", timezone);
			EventData.LimitType lmType = EventData.LimitType.FIRST;
			ed = EventData.getRangeEvents(accountID, deviceID, epf, epl, null,
					false, lmType, 4000, true, "",// (statusCode in(62465,
													// 62467, 62476, 62477))
					null);
			LiveEvent eventDataTag;
			long StartTime = 0, EndTime = 0;
			double AltitudeStart = 0, AltitudeEnd = 0;
			int flag = 0;
			boolean Check = true;
			if (ed.length > 0 && ed[0].getStatusCode() != 64791) {
				StartTime = ed[0].getTimestamp();
				AltitudeStart = ed[0].getAltitude();
				for (int i = 1; i < ed.length; i++) {
					if (ed[i].getStatusCode() == 64791) {
						Check = false;
						EndTime = ed[i].getTimestamp();
						AltitudeEnd = ed[i].getAltitude();
						eventDataTag = new LiveEvent(ed[0].getDeviceID(),
								StartTime, EndTime, ed[0].getAddress(),
								ed[0].getAddress(), EndTime - StartTime,
								AltitudeStart, AltitudeEnd);
						list.add(eventDataTag);
						break;
					}
				}
			}
			for (int i = 0; i < ed.length - 1; i++) {
				// boolean Check = false;
				if (ed[i].getStatusCode() == 64791) {
					Check = false;
					flag = i + 1;
					StartTime = ed[flag].getTimestamp();
					AltitudeStart = ed[flag].getAltitude();
					for (int j = i + 1; j < ed.length; j++) {
						if (ed[j].getStatusCode() == 64791) {
							EndTime = ed[j].getTimestamp();
							AltitudeEnd = ed[j].getAltitude();
							eventDataTag = new LiveEvent(
									ed[flag].getDeviceID(), StartTime, EndTime,
									ed[flag].getAddress(),
									ed[flag].getAddress(), EndTime - StartTime,
									AltitudeStart, AltitudeEnd);
							list.add(eventDataTag);
							i = j - 1;
							break;
						} else if (j == ed.length - 1) {
							EndTime = ed[j].getTimestamp();
							AltitudeEnd = ed[j].getAltitude();
							eventDataTag = new LiveEvent(
									ed[flag].getDeviceID(), StartTime, EndTime,
									ed[flag].getAddress(),
									ed[flag].getAddress(), EndTime - StartTime,
									AltitudeStart, AltitudeEnd);
							list.add(eventDataTag);
						}
					}
				}
				/*
				 * if(Check){ eventDataTag = new
				 * LiveEvent(ed[flag].getDeviceID(), StartTime, EndTime,
				 * ed[flag].getAddress(), ed[flag].getAddress(), EndTime -
				 * StartTime); list.add(eventDataTag); }
				 */
			}
			if (ed.length > 0 && Check) {
				StartTime = ed[0].getTimestamp();
				EndTime = ed[ed.length - 1].getTimestamp();
				AltitudeStart = ed[0].getAltitude();
				AltitudeEnd = ed[ed.length - 1].getAltitude();
				eventDataTag = new LiveEvent(ed[flag].getDeviceID(), StartTime,
						EndTime, ed[0].getAddress(),
						ed[ed.length - 1].getAddress(), EndTime - StartTime,
						AltitudeStart, AltitudeEnd);
				list.add(eventDataTag);
			}
		} catch (DBException dbe) {
			Print.logException("Unable to obtain EventData records", dbe);
		}
		return list;
	}

	public ArrayList<Fuel> GetListStop(String accountID, String deviceID,
			String fromTime, String toTime, String timezone) throws DBException {

		ArrayList<Fuel> list = new ArrayList<Fuel>();
		ResultSet rs = null;
		DBConnection dbc = null;
		Statement stmt = null;
		Fuel reportFuel;
		try {

			dbc = DBConnection.getDefaultConnection();
			long epf = ConvertToEpoch(fromTime + " 00:00:00", timezone);
			long epl = ConvertToEpoch(toTime + " 23:59:59", timezone);
			String sql = "SELECT E.`accountID`, E.`deviceID`,FROM_UNIXTIME(E.`timestamp`,'%d-%m-%Y %T') as thoigian, E.`timestamp`, E.`statusCode`, E.`statusLastingTime`, E.`altitude`, E.`Cua`, E.`address`"
					+ " FROM gtse.EventData E "
					+ " where (E.`timestamp` BETWEEN "
					+ epf
					+ " and "
					+ epl
					+ ")"
					+ " and  E.`statusCode`=61472"
					+ " and E.deviceID='"
					+ deviceID + "' and E.`accountID`='" + accountID + "';";

			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			while (rs.next()) {
				reportFuel = new Fuel(rs.getString("accountID"),
						rs.getString("deviceID"), rs.getLong("timestamp"),
						rs.getLong("statusLastingTime"),
						rs.getDouble("altitude"), rs.getString("address"),
						rs.getInt("statusCode"), rs.getInt("Cua"));
				list.add(reportFuel);
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

	public List<String> Device_reportFuel(String accountID) throws DBException {
		ResultSet rs = null;
		DBConnection dbc = null;
		Statement stmt = null;

		List<String> GetDevice = new ArrayList<String>();
		try {
			dbc = DBConnection.getDefaultConnection();
			String sql = "(select D.deviceID"
					+ " from gtse.Device D inner join  gtse.FuelDevice F on D.deviceID=F.deviceID  where "
					+ "D.deviceCode='TSTZ05'  and"
					+ " D.accountID='"
					+ accountID
					+ "' and D.deviceCode <> 'TSTMayXuc')"
					+ " UNION "
					+ "(select D.deviceID"
					+ " from gtse.Device D inner join gtse.tblFuelData1 DT on D.deviceID=DT.deviceID where"
					+ " D.accountID='" + accountID + "')" + " UNION "
					+ "(SELECT deviceID FROM `gtse`.`Device`"
					+ " where accountID = '" + accountID + "')";
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			while (rs.next()) {

				GetDevice.add(rs.getString(1));
			}
		} catch (SQLException sqe) {
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
		return GetDevice;
	}

	public double FuelBeforeStop(String account, String device, long time)
			throws DBException {
		double fuelstart = 0.0;

		ResultSet rs = null;
		DBConnection dbc = null;
		Statement stmt = null;

		try {

			dbc = DBConnection.getDefaultConnection();

			String sql = "SELECT E.`accountID`, E.`deviceID`,E.`timestamp`, E.`statusCode`, E.`statusLastingTime`, E.`altitude`, E.`Cua`, E.`address`"
					+ " FROM gtse.EventData E "
					+ " where  E.deviceID='"
					+ device
					+ "' and E.`accountID`='"
					+ account
					+ "'"
					+ "and `timestamp` <"
					+ time
					+ " order by `timestamp` desc  limit 0,1 ;";

			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			while (rs.next()) {
				fuelstart = rs.getDouble("altitude");

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

		return fuelstart;
	}

	public List<Double> FuelAfterStop(String account, String device, long time)
			throws DBException {
		// double fuelstart = 0.0;

		ResultSet rs = null;
		DBConnection dbc = null;
		Statement stmt = null;
		List<Double> list = new ArrayList<Double>();
		try {

			dbc = DBConnection.getDefaultConnection();

			String sql = "SELECT `altitude`" + " FROM gtse.EventData E "
					+ " where  E.deviceID='" + device + "' and E.`accountID`='"
					+ account + "'" + "  and `timestamp` > " + time
					+ " order by `timestamp` asc  limit 0,1 ;";

			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			while (rs.next()) {
				list.add(rs.getDouble("altitude"));
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

	// Quản lý xe select driver by devid,accid
	public List<String[]> Device_SelectDriver(String devid, String accid)
			throws DBException {
		ResultSet rs = null;
		DBConnection dbc = null;
		Statement stmt = null;
		List<String[]> GetDriver = new ArrayList<String[]>();
		try {
			dbc = DBConnection.getDefaultConnection();
			String sql = "SELECT DISTINCT dr.driverID,dr.description FROM Device_Driver dd,Driver dr"
					+ " WHERE dd.accountID='"
					+ accid
					+ "' AND dd.driverID=dr.driverID and dd.deviceID='"
					+ devid
					+ "'";
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			while (rs.next()) {
				String[] gz = new String[2];
				gz[0] = rs.getString("driverID");
				gz[1] = rs.getString("description");
				GetDriver.add(gz);
			}
		} catch (SQLException sqe) {
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
		return GetDriver;
	}

	public List<String[]> Device_SelectDrive(String accid) throws DBException {
		ResultSet rs = null;
		DBConnection dbc = null;
		Statement stmt = null;
		List<String[]> GetDriver = new ArrayList<String[]>();
		try {

			dbc = DBConnection.getDefaultConnection();
			String sql = "SELECT  * FROM Device dd" + " WHERE dd.accountID='"
					+ accid + "' AND CheckSim=1";
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			while (rs.next()) {
				String[] gz = new String[2];
				gz[0] = rs.getString("deviceID");
				GetDriver.add(gz);
			}
		} catch (SQLException sqe) {
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
		return GetDriver;
	}

	public double xangLucDau(String acc, String Ngay, String xe) {
		double chuoi = 0;
		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call Minhnv_nhienLieuDauCuoi(?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, acc);
			cstmt.setString(2, Ngay);
			cstmt.setString(3, xe);
			cstmt.setInt(4, 1);

			rs = cstmt.executeQuery();

			while (rs.next()) {
				chuoi = Double.parseDouble(rs.getString("fuelLevel"));

			}

		} catch (Exception e) {

		}

		return chuoi;
	}

	public double xangLucCuoi(String acc, String Ngay, String xe) {
		double chuoi = 0;
		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call Minhnv_nhienLieuDauCuoi(?,?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, acc);
			cstmt.setString(2, Ngay);
			cstmt.setString(3, xe);
			cstmt.setInt(4, 2);

			rs = cstmt.executeQuery();

			while (rs.next()) {
				chuoi = Double.parseDouble(rs.getString("fuelLevel"));

			}

		} catch (Exception e) {

		}

		return chuoi;
	}

	public double xangDo(String acc, String Ngay, String xe) {
		double xang = 0;

		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call MinhNV_DoXang(?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, acc);
			cstmt.setString(2, Ngay);
			cstmt.setString(3, xe);

			rs = cstmt.executeQuery();

			while (rs.next()) {
				xang = Double.parseDouble(rs.getString("nhienLieu"));

			}

		} catch (Exception e) {

		}

		return xang;

	}

	public String doiGio(int tg) {
		String chuoi = "";
		int gio = tg / 3600;
		int phut = (tg % 3600) / 60;
		int giay = tg - gio * 3600 - phut * 60;
		String h = "", m = "", s = "";
		if (gio < 10)
			h = "0" + Integer.toString(gio);
		else
			h = Integer.toString(gio);
		if (phut < 10)
			m = "0" + Integer.toString(phut);
		else
			m = Integer.toString(phut);
		if (giay < 10)
			s = "0" + Integer.toString(giay);
		else
			s = Integer.toString(giay);
		return chuoi = chuoi + h + ":" + m + ":" + s;

	}

	public ResultSet ThoiGian(String acc, String bienXe) {
		ResultSet rs = null;
		Connection con = Connect();
		try {
			String sql = "{call MinhNV_ThoiGian(?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, acc);
			cstmt.setString(2, bienXe);
			rs = cstmt.executeQuery();
		} catch (Exception e) {
		}
		return rs;
	}

	public ResultSet thoiGianLaiXe(String acc, String dev, String date) {
		ResultSet rs = null;
		Connection con = Connect();

		try {

			String sql = "{call MinhNV_LXLT(?,?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, acc);
			cstmt.setString(2, date);
			cstmt.setString(3, dev);

			rs = cstmt.executeQuery();

		} catch (Exception e) {

		}

		return rs;

	}

	public String LoaiXe(String allow, String acc, String dev) {
		ResultSet rs = null;
		Connection con = Connect();
		Statement stmt = null;
		String chuoi = "<select id='loaiXe' name='loaiXe' " + allow
				+ "  style='width: 260px;' class='adminComboBox' >";
		try {
			String sql = "{call MinhNV_loaiXe}";
			CallableStatement cstmt = con.prepareCall(sql);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				chuoi = chuoi + "<option value='" + rs.getString(1) + "'>"
						+ rs.getString(2) + "</option>";

			}

		} catch (Exception e) {
		}
		return chuoi
				+ "</select>"
				+ "<script type ='text/javascript' language ='javascript'> document.getElementById('loaiXe').value = '"
				+ loaiXeID(acc, dev) + "';</script>\n";
	}

	public String loaiXeID(String acc, String dev) {
		String chuoi = "";

		ResultSet rs = null;
		Connection con = Connect();
		Statement stmt = null;

		try {
			String sql = "{call Hienntd_selectDevID1(?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(1, acc);
			cstmt.setString(2, dev);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				chuoi = chuoi + rs.getString("loaiXeID");

			}

		} catch (Exception e) {
		}

		return chuoi;

	}

	public String tenLoaiXe(String acc, String dev) {
		String chuoi = "";

		ResultSet rs = null;
		Connection con = Connect();
		Statement stmt = null;

		try {
			String sql = "{call MinhNV_tenLoaiXe(?,?)}";
			CallableStatement cstmt = con.prepareCall(sql);
			cstmt.setString(2, acc);
			cstmt.setString(1, dev);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				chuoi = chuoi + rs.getString("tenloaiXe");

			}

		} catch (Exception e) {
		}

		return chuoi;
	}

	// Autocomplete
	public ArrayList<String> GetAccount() {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		try {
			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT accountID FROM Account ";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				list.add(rs.getString("accountID"));
			}
		} catch (Exception sqe) {
			Print.logError("GetAccount:" + sqe.getMessage());
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

	public ArrayList<String> GetDeviceByAccount(String accid) {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		try {
			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT * FROM Device WHERE accountID='" + accid
					+ "'";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				list.add(rs.getString("deviceID"));
			}
		} catch (Exception sqe) {
			Print.logError("GetDeviceByAccount:" + sqe.getMessage());
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

	private String ConvertFromEpoch(long epoch, String timezone) {
		TimeZone tz = DateTime.getTimeZone(timezone, null);
		DateTime dt = new DateTime(epoch);
		String dtFmt = dt.format("dd/MM/yyyy HH:mm:ss", tz);
		return dtFmt;
	}

	public List<String[]> SelectFromtblFuel(String accid, String devid,
			String tuNgay, String denNgay, String timezone) throws DBException {
		ResultSet rs = null;
		DBConnection dbc = null;
		Statement stmt = null;
		List<String[]> GetFuel = new ArrayList<String[]>();
		long tungay = ConvertToEpoch(tuNgay, timezone);
		long denngay = ConvertToEpoch(denNgay, timezone);
		try {
			dbc = DBConnection.getDefaultConnection();
			String sql = "SELECT * FROM tblFuel WHERE accountID='" + accid
					+ "' AND deviceID='" + devid + "' AND `timestamp` BETWEEN "
					+ tungay + " AND " + denngay;
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			while (rs.next()) {
				String[] gz = new String[7];
				gz[0] = rs.getString("accountID");
				gz[1] = rs.getString("deviceID");
				gz[2] = ConvertFromEpoch(rs.getLong("timestamp"), timezone);
				gz[3] = rs.getString("address");
				gz[4] = rs.getString("fuelLevel");
				gz[5] = rs.getString("fuelVol");
				gz[6] = rs.getString("volReceive");
				GetFuel.add(gz);
			}
		} catch (SQLException sqe) {
			System.out.print(sqe.toString());
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
		return GetFuel;
	}

	public boolean SelectFuelDevice(String accid, String devid)
			throws DBException {
		ResultSet rs = null;
		DBConnection dbc = null;
		Statement stmt = null;
		boolean kt = true;
		try {
			dbc = DBConnection.getDefaultConnection();
			String sql = "SELECT * FROM FuelDevice WHERE accountID='" + accid
					+ "' AND deviceID='" + devid + "'";
			stmt = dbc.execute(sql);
			rs = stmt.getResultSet();
			while (rs.next()) {
				kt = false;
			}
		} catch (SQLException sqe) {
			System.out.print(sqe.toString());
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
		return kt;
	}

	// Hienntd add Add FuelDevice
	public void InsertFuelDevice(String accid, String devid, double maxvol,
			double minvol, double volume, int thuan) throws DBException {
		DBConnection dbc = null;
		try {
			dbc = DBConnection.getDefaultConnection();
			boolean kt = SelectFuelDevice(accid, devid);
			String command = "";
			if (kt)
				command = "INSERT INTO FuelDevice(accountID,deviceID,maxVol,minVol,volume,thuan) "
						+ "VALUES('"
						+ accid
						+ "','"
						+ devid
						+ "',"
						+ maxvol
						+ "," + minvol + "," + volume + "," + thuan + ")";
			else
				command = "UPDATE FuelDevice SET maxVol=" + maxvol + ",minVol="
						+ minvol + ",volume=" + volume + ",thuan=" + thuan + ""
						+ " WHERE accountID='" + accid + "' AND deviceID='"
						+ devid + "'";
			dbc.executeUpdate(command);
		} catch (SQLException sqe) {
			Print.logError("InsertFuelDevice: " + sqe.getMessage());
		} finally {
			DBConnection.release(dbc);
		}
	}

	// phuongldh add: kiem tra so lan lai xe lien tuc
	public int CheckLXLienTuc(String accountID, String deviceID, long Time,
			String driverID) throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		int tong = 0;
		try {
			dbc = DBConnection.getDefaultConnection();
			String command = "";
			if (driverID.equals("")) {
				command = "SELECT  count(*) as tong "
						+ " from tblDrivingTime td WHERE (td.startTime BETWEEN "
						+ Time + " and " + (Time + 86399)
						+ ") and td.accountID = '" + accountID
						+ "' AND td.deviceID = '" + deviceID
						+ "'  and td.stopTime-td.startTime > 4*3600";
			} else {
				command = "SELECT  count(*) as tong "
						+ " from tblDrivingTime td WHERE (td.startTime BETWEEN "
						+ Time + " and " + (Time + 86399)
						+ ") and td.accountID = '" + accountID
						+ "' AND td.deviceID = '" + deviceID
						+ "' and driverID = '" + driverID
						+ "' and td.stopTime-td.startTime > 4*3600";
			}

			stmt = dbc.execute(command);
			rs = stmt.getResultSet();
			while (rs.next()) {
				tong = rs.getInt("tong");
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

		return tong;
	}

	// kiem tra so lai xe lai trong nagy
	public ArrayList<String> GetLX(String accountID, String deviceID, long Time)
			throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<String> list = new ArrayList<String>();
		try {
			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT  distinct td.driverID  "
					+ " from tblDrivingTime td inner join Driver d on td.driverID = d.driverID WHERE (td.startTime BETWEEN "
					+ Time + " and " + (Time + 86399)
					+ ") and td.accountID = '" + accountID
					+ "' AND td.deviceID = '" + deviceID + "'";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();

			while (rs.next()) {
				list.add(rs.getString("driverID"));
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

	// get description driver
	public String getDescriptionDriver(String accountID, String driver)
			throws DBException {
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		String description = "";
		try {
			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT description " + " from  Driver"
					+ " where accountID = '" + accountID + "' AND driverID = '"
					+ driver + "'";
			stmt = dbc.execute(command);
			rs = stmt.getResultSet();

			while (rs.next()) {
				description = rs.getString("description");
			}
		} catch (SQLException sqe) {
			throw new DBException("getDescriptionDriver", sqe);
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

		return description;
	}
	
	public ArrayList<CheckSIM> StaticReportDetailQLSIM(String accountID, String deviceID,
			String fromTime, String timezone, String reportType)
			throws DBException {
		DBConnection dbc = null;
		Statement stmt = null, stmt2 = null;
		ResultSet rs = null, rs2 = null;
		ArrayList<CheckSIM> list = new ArrayList<CheckSIM>();
		CheckSIM reportCheckSIM;
		try {

			dbc = DBConnection.getDefaultConnection();

			String command = "SELECT ed.deviceID,ed.decription,ed.AccountID,ed.reportType,ed.timestamp,ed.imei,ed.phoneCX,ed.simPhoneNumber,ed.decription,ed.checkThongtin "
					+ " FROM tblCheckSim  ed , Device d WHERE   "
					+ " ed.simPhoneNumber =d.simPhoneNumber and ed.accountId= '"
					+ accountID
					+ "' and ed.deviceID = '"
					+ deviceID
					+ "' and ed.reportType = '2' "
					+ " ORDER BY ed.`timestamp` desc LIMIT 1";

			String command2 = "SELECT * "
					+ "  FROM tblCheckSim tbcs,  Device d  WHERE tbcs.simPhoneNumber =d.simPhoneNumber and tbcs.reportType = '1' and tbcs.accountId='"
					+ accountID + "' and tbcs.deviceID = '" + deviceID + "'"
					+ " ORDER BY timestamp desc LIMIT 1";

			stmt = dbc.execute(command);
			stmt2 = dbc.execute(command2);

			rs = stmt.getResultSet();
			rs2 = stmt2.getResultSet();

			String checkThongTin = "", _deviceID = "", AccountID = "", imei = "", phoneCX = "", simPhoneNumber = "", _reportType = "", decription = "";
			int timestamp = 0, timestamp2 = 0;

			while (rs.next()) {
				_deviceID = rs.getString("deviceID");
				timestamp = rs.getInt("timestamp");
				AccountID = rs.getString("AccountID");
				checkThongTin = rs.getString("checkThongtin");
				imei = rs.getString("imei");
				phoneCX = rs.getString("phoneCX");
				simPhoneNumber = rs.getString("simPhoneNumber");
				decription = rs.getString("decription");
				_reportType = rs.getString("reportType");
			}

			while (rs2.next()) {
				checkThongTin += " " + rs2.getString("checkThongtin");
				timestamp2 = rs2.getInt("timestamp");
			}

			if (_deviceID != "") {

				if (timestamp2 > timestamp)
					timestamp = timestamp2;

				reportCheckSIM = new CheckSIM(_deviceID, timestamp, AccountID,
						checkThongTin, imei, phoneCX, simPhoneNumber,
						decription, _reportType);
				list.add(reportCheckSIM);
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
			dbc.closeConnection();
			DBConnection.release(dbc);
		}

		return list;
	}
}
