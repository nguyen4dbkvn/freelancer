package org.opengts.servers;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.nio.ByteBuffer;

import org.opengts.dbtools.DBConnection;
import org.opengts.util.Print;

public class Engine {

	public static Connection Connect() {
		Connection conn = null;
		try {
			String url = "jdbc:sqlserver://123.30.169.115:1433;databaseName=SMSPlus";
			String userName = "gts";
			String password = "opengts";
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = (Connection) DriverManager.getConnection(url, userName,
					password);
			return conn;
		} catch (Exception e) {
			return null;
		}
	}

	public static String sendSMS(String smsMessage, String simPhone) {
		String bool = "false";

		Connection con;
				
		try {
			con = Engine.Connect();
		} catch (Exception e) {

			return e.toString();
			//Print.logInfo(e.getMessage());
		}
		CallableStatement cs = null;
		try {
			cs = (CallableStatement) con
					.prepareCall("{call sp_ChenVaoOutbox(?,?,?,?,?,?,?)}");
			cs.setString(1, simPhone);
			cs.setString(2, smsMessage);
			cs.setString(3, "8068");
			cs.setInt(4, 0);
			cs.setInt(5, 3);
			cs.setString(6, "XE");
			cs.setInt(7, 3);
			cs.execute();
			bool = "true";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			bool = "false";
	
			return bool;
			//Print.logInfo(e.getMessage());
		}
		try {
			cs.close();
			con.close();
		} catch (SQLException e) {
			return e.getMessage();
			//Print.logInfo(e.getMessage());
		}
		return bool;
	}
 
	public static double getLatitudeDouble(String s) {
		try {
			double _lat = Double.parseDouble(s);
			if (_lat < 99999.0) {
				double lat = (double) ((long) _lat / 100L); // _lat is always
															// positive here
				lat += (_lat - (lat * 100.0)) / 60.0;
				return lat;
			} else {
				return 90.0; // invalid latitude
			}
		} catch (Exception e) {
			return 0;
		}
	}

	public static void _insertIntotblSendCmdConfig(String accountID,
			String deviceID, String imei, String ipaddr, long timestamp,
			String command, int status, int retry, String decription) {
		DBConnection dbc = null;
		Statement statment = null;
		try {
			dbc = DBConnection.getDefaultConnection();
			statment = dbc.createStatement();
			String strInsert = "Insert into tblSendCmdConfig(accountID, deviceID, imei, ipaddress,"
					+ " timestamp, command, status, retry, decription) "
					+ " values ('"
					+ accountID
					+ "','"
					+ deviceID
					+ "','"
					+ imei
					+ "','"
					+ ipaddr
					+ "',"
					+ timestamp
					+ ",'"
					+ command
					+ "'," + status + "," + retry + ",'" + decription + "')";
			Print.logInfo("strInsert: " + strInsert);
			statment.executeUpdate(strInsert);
		} catch (SQLException sqe) {
			Print.logInfo(sqe.getMessage());
		} finally {
			if (statment != null) {
				try {
					statment.close();
				} catch (Throwable t) {
				}
			}
			DBConnection.release(dbc);
		}
	}

	public static void _insertcheckSim(String accountID, String deviceID,
			long timestamp, String imei, String phonecx, String checkThongtin,
			String decription, String simPhoneNumber, int reportType) {
		DBConnection dbc = null;
		Statement statment = null;
		try {
			dbc = DBConnection.getDefaultConnection();
			statment = dbc.createStatement();
			String strInsert = "Insert into tblCheckSim(accountID, deviceID, timestamp,imei, phoneCX,"
					+ "  checkThongtin, decription, simPhoneNumber, reportType) "
					+ " values ('"
					+ accountID
					+ "','"
					+ deviceID
					+ "',"
					+ timestamp
					+ ",'"
					+ imei
					+ "','"
					+ phonecx
					+ "','"
					+ checkThongtin
					+ "','"
					+ decription
					+ "','"
					+ simPhoneNumber + "'," + reportType + ")";
			Print.logInfo("strInsert: " + strInsert);
			statment.executeUpdate(strInsert);
		} catch (SQLException sqe) {
			Print.logInfo(sqe.getMessage());
		} finally {
			if (statment != null) {
				try {
					statment.close();
				} catch (Throwable t) {
				}
			}
			DBConnection.release(dbc);
		}
	}

	public static double getLongitudeDouble(String s) {
		try {
			double _lon = Double.parseDouble(s);
			if (_lon < 99999.0) {
				double lon = (double) ((long) _lon / 100L); // _lon is always
															// positive here
				lon += (_lon - (lon * 100.0)) / 60.0;
				return lon;
			} else {
				return 180.0; // invalid longitude
			}
		} catch (Exception e) {
			return 0;
		}
	}

	public static void updateEventData(String accountID, String deviceID,
			long statusLastingTime, long timestamp) {
		DBConnection dbc = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			dbc = DBConnection.getDefaultConnection();
			statement = dbc.createStatement();

			String strUpdate = "update EventData set statusLastingTime = "
					+ statusLastingTime + " where timestamp >= " + timestamp
					+ " and accountID = '" + accountID + "' and deviceID = '"
					+ deviceID + "'";
			statement.executeUpdate(strUpdate);
		} catch (SQLException sqe) {
			Print.logInfo(sqe.getMessage());
		} finally {
			if (statement != null || rs != null) {
				try {
					rs.close();
					statement.close();
				} catch (Throwable t) {
				}
			}
			DBConnection.release(dbc);
		}
	}

	public static String GetNCRDecimalString(String s) {
		String def = " abcdefghijklmnopqrstuvxwzyABCDEFGHIJKLMNOPQRSTUVXWZY0123456789'~!@#$%^&*()-_=+\\|]}[{?/>.,<:;'\"";
		String a = s;
		char[] ab = a.toCharArray();
		a = "";
		for (int i = 0; i < ab.length; i++) {
			char c = ab[i];
			if (c == (char) 13) {
				a = a + c;
			} else {
				if (def.indexOf(c) >= 0) {
					a = a + c;
				} else {
					a = a + "&#" + (int) c + ";";
				}
			}
		}
		return a;
	}

	public static String NCRToUnicodeKoDau(String strInput) {
		String TCVN = "&#225;,&#224;,&#7841;,&#7843;,&#227;,&#226;,&#7845;,&#7847;,&#7853;,&#7849;,&#7851;,&#259;,&#7855;,&#7857;,&#7863;,&#7859;,&#7861;,&#�;,&#232;,&#7865;,&#7867;,&#7869;,&#234;,&#7871;,&#7873;,&#7879;,&#7875;,&#7877;,&#243;,&#242;,&#7885;,&#7887;,&#245;,&#244;,&#7889;,&#7891;,&#7897;,&#7893;,&#7895;,&#417;,&#7899;,&#7901;,&#7907;,&#7903;,&#7905;,&#250;,&#249;,&#7909;,&#7911;,&#361;,&#432;,&#7913;,&#7915;,&#7921;,&#7917;,&#7919;,&#237;,&#236;,&#7883;,&#7881;,&#297;,&#2979;,&#273;,&#253;,&#7923;,&#7925;,&#7927;,&#7929;";
		TCVN += "&#193;,&#192;,&#7840;,&#7842;,&#195;,&#194;,&#7844;,&#7846;,&#7852;,&#7848;,&#7850;,&#258;,&#7854;,&#7856;,&#7862;,&#7858;,&#7860;,&#200;,&#7864;,&#7866;,&#7868;,&#7870;,&#7872;,&#7878;,&#7874;,&#7876;,&#211;,&#210;,&#7884;,&#7886;,&#213;,&#212;,&#7888;,&#7890;,&#7896;,&#7892;,&#7894;,&#416;,&#7898;,&#7900;,&#7906;,&#7902;,&#7904;,&#218;,&#217;,&#7908;,&#7910;,&#360;,&#431;,&#7912;,&#7914;,&#7920;,&#7916;,&#7918;,&#272;,&#221;,&#7922;,&#7924;,&#7926;,&#7928;,&#272;";
		String UNICODE = "a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,a,e,e,e,e,e,e,e,e,e,e,e,e,o,o,o,o,o,o,o,o,o,o,o,o,o,o,o,o,u,u,u,u,u,u,u,u,u,u,u,i,i,i,i,i,i,d,y,y,y,y,y";
		UNICODE += "A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,A,E,E,E,E,E,E,E,E,E,O,O,O,O,O,O,O,O,O,O,O,O,O,O,O,O,O,U,U,U,U,U,U,U,U,U,U,U,D,Y,Y,Y,Y,Y,D";
		String[] str = TCVN.split(",");
		String[] str1 = UNICODE.split(",");
		for (int i = 0; i < str.length; i++) {
			if (!str[i].equals("")) {
				strInput = strInput.replace(str[i], str1[i]);
			}
		}
		return strInput;
	}

	public static void InsertIntoEventData(String accountID, String deviceID,
			long timestamp, int statuscode, double latitude, double longitude,
			String address, double speedKPH, int cua, int dieuhoa,
			double odometerKM, double altitude) {
		DBConnection dbc = null;
		Statement statment = null;
		try {
			dbc = DBConnection.getDefaultConnection();
			statment = dbc.createStatement();
			String strInsert = "INSERT INTO EventData(accountID, deviceID, timestamp, statuscode, latitude, longitude, address, speedKPH, cua, dieuhoa, odometerKM, altitude) VALUES ('"
					+ accountID
					+ "', '"
					+ deviceID
					+ "', "
					+ timestamp
					+ ", "
					+ statuscode
					+ ", "
					+ latitude
					+ ", "
					+ longitude
					+ ", '"
					+ address
					+ "', "
					+ speedKPH
					+ ", "
					+ cua
					+ ", "
					+ dieuhoa
					+ ", " + odometerKM + ", " + altitude + ")";
			Print.logInfo(strInsert);
			statment.executeUpdate(strInsert);
		} catch (SQLException sqe) {
			Print.logInfo("Insert error: " + sqe.getMessage());
		} finally {
			if (statment != null) {
				try {
					statment.close();
				} catch (Throwable t) {
				}
			}
			DBConnection.release(dbc);
		}
	}

	/*
	 * Author: Thanhngc
	 */
	public static boolean updatePacketACK(String accountID, String deviceID,
			String ipaddr) {
		DBConnection dbc = null;
		try {
			dbc = DBConnection.getDefaultConnection();
			String sql = "Update tblGFMISendCommon set status=5 where accountID='"
					+ accountID
					+ "' and deviceID='"
					+ deviceID
					+ "' and ipaddress='" + ipaddr + "' and status=2";
			dbc.executeUpdate(sql);
		} catch (Exception ex) {
			Print.logException(
					"Thanhngc: exception when parse PrintGPRS packet",
					ex.fillInStackTrace());
			Print.logException(
					"Thanhngc: exception when parse PrintGPRS packet",
					ex.getCause());
			Print.logException(
					"Thanhngc: exception when parse PrintGPRS packet",
					ex.initCause(ex.getCause()));
		} finally {
			DBConnection.release(dbc);
		}
		return true;
	}

	/*
	 * Author: Thanhngc
	 */
	public static boolean updatePacketACK(String ipaddr) {
		DBConnection dbc = null;
		Statement statment = null;
		try {
			dbc = DBConnection.getDefaultConnection();
			statment = dbc.createStatement();
			String sql = "Update tblGFMISendCommon set Status=5 where ipaddress='"
					+ ipaddr + "' and Status=2;";
			// dbc.executeUpdate(sql);
			statment.executeUpdate(sql);
			Print.logInfo(sql);
		} catch (SQLException sqe) {
			Print.logInfo("Update ACK error: " + sqe.getMessage());
		} finally {
			if (statment != null) {
				try {
					statment.close();
				} catch (Throwable t) {
				}
			}
			DBConnection.release(dbc);
		}
		return true;
	}

	public static String decryptReceivedPacket(char[] g_nbuf) {
		// String a = s;
		// char[] g = a.toCharArray();
		String ret = "";
		int len = g_nbuf.length;
		char[] decodedPacket = new char[len];
		int[] idecodedPacket = new int[len];

		for (int i = 5; i < len;) {

			for (int j = 2; (j < 5) && (i < len); j++, i++) {

				// g[i] -= 31;

				idecodedPacket[i] = (int) g_nbuf[i] - 31;

				// g[i] += g[ntj];
				idecodedPacket[i] -= (int) g_nbuf[j];
				// decodedPacket[i] = (char) decodedPacket[i] + g_nbuf[j]);
				if (idecodedPacket[i] < 32) {
					idecodedPacket[i] += 95;
				}

				idecodedPacket[i] += 31;
				decodedPacket[i] = (char) idecodedPacket[i];
			}
		}
		ret = String.valueOf(decodedPacket);
		return ret;
		

	}
}
