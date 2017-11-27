// ----------------------------------------------------------------------------
// Copyright 2006-2010, GeoTelematic Solutions, Inc.
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
// Change History:
// 2007/01/25 Martin D. Flynn
// -Initial release
// 2007/06/03 Martin D. Flynn
// -Added I18N support
// 2007/06/13 Martin D. Flynn
// -Added support for browsers with disabled cookies
// 2007/07/27 Martin D. Flynn
// -Added 'getNavigationTab(...)'
// 2010/04/11 Martin D. Flynn
// -Not displayed as an option if the logn "Password" field is hidden
// ----------------------------------------------------------------------------
package org.opengts.war.track.page;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.opengts.util.*;
import org.opengts.dbtools.*;
import org.opengts.db.*;
import org.opengts.db.tables.*;
import org.opengts.war.tools.*;
import org.opengts.war.track.*;

public class NewFuel extends WebPageAdaptor implements Constants {
	public static final String PARM_ACCT_DESC = "a_desc";
	public static final String COMMAND_INFO_UPDATE = "update";
	// button types
	public static final String PARM_BUTTON_CANCEL = "a_btncan";
	public static final String PARM_BUTTON_BACK = "a_btnbak";
	// parameters
	// thanhtq
	public static final String PARM_DATE_FROM = "txtdatefrom";
	public static final String PARM_DATE_TO = "txtdateto";
	public static final String PARM_DEVICE = "cbbdevice";
	public static final String PARM_TIME_FROM = "txttimefrom";
	public static final String PARM_TIME_TO = "txttimeto";

	public NewFuel() {
		this.setBaseURI(RequestProperties.TRACK_BASE_URI());
		this.setPageName(PAGE_NEWFUEL);
		this.setPageNavigation(new String[] { PAGE_LOGIN, PAGE_MENU_TOP });
		this.setLoginRequired(true);
	}

	// ------------------------------------------------------------------------
	public String getMenuName(RequestProperties reqState) {
		return "";
	}

	public String getMenuDescription(RequestProperties reqState,
			String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(NewFuelTG.class);
		// return "&#x1EA2;nh ch&#x1EE5;p t&#x1EEB; xe";
		return i18n.getString("NewFuelTG.Menu", "Biểu đồ nhiên liệu RS232");
	}

	public String getMenuHelp(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(NewFuelTG.class);
		// return "&#x1EA2;nh ch&#x1EE5;p t&#x1EEB; xe";
		return i18n.getString("NewFuelTG..MenuHelp", "Biểu đồ nhiên liệu RS232");
	}

	// ------------------------------------------------------------------------

	public String getNavigationDescription(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(NewFuelTG.class);
		// return "&#x1EA2;nh ch&#x1EE5;p t&#x1EEB; xe";
		return i18n.getString("NewFuelTG.NavDesc", "Biểu đồ nhiên liệu RS232");
	}

	public String getNavigationTab(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(NewFuelTG.class);
		// return "&#x1EA2;nh ch&#x1EE5;p t&#x1EEB; xe";
		return i18n.getString("NewFuelTG.NavTab", "Biểu đồ nhiên liệu RS232");
	}

	public boolean isOkToDisplay(RequestProperties reqState) {
		Account account = (reqState != null) ? reqState.getCurrentAccount()
				: null;
		if (account == null) {
			return false; // no account?
		} else {
			int dem = 0;
			DBCamera objcmr = new DBCamera();
			dem = objcmr.phanQuyen(account.getAccountID(),
					"Biều đồ nhiên liệu RS232");
			 
			if (dem > 0)
				return false;
			else
				return true;

		}
	}

	// ------------------------------------------------------------------------
	public String CreateCbbDevice(String accountid, String userid,
			String idselect) throws IOException {
		String strre = "<select id ='" + NewFuel.PARM_DEVICE + "' name = '"
				+ NewFuel.PARM_DEVICE + "' class='textReadOnly'>";
		try {
			DBCamera objcmr = new DBCamera();
			ResultSet rs = objcmr.GetDiviceByAccountID2(accountid, userid);
			while (rs.next()) {
				if (idselect == rs.getString("deviceid"))
					strre += "<option value ='" + rs.getString("deviceid")
							+ "' selected =\"selected\">"
							+ rs.getString("description") + "</option>\n";
				else
					strre += "<option value ='" + rs.getString("deviceid")
							+ "'>" + rs.getString("description")
							+ "</option>\n";
			}
		} catch (Exception e) {
		}
		strre += "</select>\n";
		strre += "<script type ='text/javascript' language ='javascript'> document.getElementById('"
				+ NewFuel.PARM_DEVICE
				+ "').value = '"
				+ idselect
				+ "';</script>\n";
		return strre;
	}

	private String CreateCbbTime(String id, String idselect) {
		String strre = "<select id =\"" + id + "\" name ='" + id + "'>";
		try {
			for (int i = 0; i < 24; i++) {
				String time = Integer.toString(i);
				if (i < 10)
					time = "0" + time;
				for (int j = 0; j <= 45; j += 15) {
					String min = Integer.toString(j);
					if (j < 10)
						min = "0" + min;
					String timeall = time + ":" + min;
					if (timeall.equals(idselect))// changed here
						strre += "<option value = '" + timeall
								+ "' selected =\"selected\">" + timeall
								+ "</option> \n ";
					else
						strre += "<option value = '" + timeall + "'>" + timeall
								+ "</option> \n ";
				}
			}
		} catch (Exception e) {
		}
		strre += "</select>";
		strre += "<script type ='text/javascript' language ='javascript'> document.getElementById('"
				+ id + "').value = '" + idselect + "';</script>\n";
		return strre;
	}

	private String getCurDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		return dateFormat.format(date);
	}

	/*
*
*
*
*/
	public String GetNumScoring(String datefrom, String dateto,
			String AccountID, String deviceID, String timefrom, String timeto,
			String timezone, PrivateLabel privLabel) throws IOException {
		String strscr = "<script type ='text/javascript'>";
		strscr += "var chartData = [];";
		try {
			DBReport objrp = new DBReport();
			ArrayList<FuelData> arrValue = objrp.FuelReport(AccountID,
					deviceID, datefrom, dateto, timezone, timefrom, timeto);
			long mileage = 0;
			// long temp = 0;
			// int c = 0;

			for (int i = 0; i < arrValue.size(); i++) {
				String statusDescription = StatusCode.getDescription(AccountID,
						arrValue.get(i).GetStatusCode(), privLabel, null);
				 

				if (arrValue.get(i).getAlarmType() != 60) {

					String detail = "Mức nhiên liệu:"
							+ Math.round(arrValue.get(i).GetFuelLevel())
							+ " l\\n Q.Đường đi được trong ngày:"
							// + Math.round(objrp.GetOdometer(AccountID,
							// deviceID,
							// (long)arrValue.get(i).getTimeStamp()))//here
							+ Math.round(arrValue.get(i).GetOdometerKM())
							+ " km\\nThời gian: "
							+ ConvertFromEpoch(arrValue.get(i).getTimeStamp())
							+ "\\nĐịa điểm:"
							+ GetUTF8FromNCRDecimalString(arrValue.get(i)
									.getAddress()) + "\\nTrạng thái: "
							+ GetUTF8FromNCRDecimalString(statusDescription);
					strscr += "chartData.push({fuelLevel: "
							+ arrValue.get(i).GetFuelLevel() + ",timestamp: '"
							+ ConvertFromEpoch(arrValue.get(i).getTimeStamp())
							+ "', detail:\"" + detail + "\"});\n";
				}
			}

		} catch (Exception e) {
		}
		strscr += "</script>";
		strscr += "<script type ='text/javascript' src='js/linechart1.js'></script>";
		return strscr;
	}

	public String GetUTF8FromNCRDecimalString(String s) {
		String a, b;
		a = "";
		b = "";
		try {
			String[] arrStr = s.split("&#");
			int i = arrStr.length;
			for (int j = 0; j < i; j++) {
				int k = 0;
				k = s.indexOf("&#");
				if (k >= 0) {
					b = s.substring(0, k);
					a = a + b;
					s = s.substring(k);
				} else {
					a = a + s;
				}
				k = s.indexOf(";");
				if (k >= 2) {
					b = s.substring(2, k);
					s = s.substring(k + 1);
					try {
						char str = (char) (Integer.parseInt(b));
						a = a + str;
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
			a = e.getMessage();
			// TODO: handle exception
		}
		return a;
	}

	private String ConvertFromEpoch(Integer epoch) {
		String date = new java.text.SimpleDateFormat("dd HH:mm")
				.format(new java.util.Date((long) epoch * 1000));
		return date;
	}

	public void writePage(final RequestProperties reqState, final String pageMsg)
			throws IOException {
		final HttpServletRequest request = reqState.getHttpServletRequest();
		final PrivateLabel privLabel = reqState.getPrivateLabel();
		final I18N i18n = privLabel.getI18N(NewFuel.class);
		final String pageName = this.getPageName();
		String m = "";
		boolean error = false;
		final Account currAcct = reqState.getCurrentAccount();
		final User currUser = reqState.getCurrentUser();
		DBReport objrp = new DBReport();
		HTMLOutput HTML_CSS = new HTMLOutput() {
			public void write(PrintWriter out) throws IOException {
				String cssDir = NewFuel.this.getCssDirectory();
				WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
			}
		};
		/* JavaScript */
		HTMLOutput HTML_JS = new HTMLOutput() {
			public void write(PrintWriter out) throws IOException {
				MenuBar.writeJavaScript(out, pageName, reqState);
				JavaScriptTools.writeJSInclude(out,
						JavaScriptTools.qualifyJSFileRef("PCalendar.js"),
						request);
				JavaScriptTools.writeJSInclude(out,
						JavaScriptTools.qualifyJSFileRef("amcharts.js"),
						request);
			}
		};
		/* write frame */
		HTMLOutput HTML_CONTENT = new HTMLOutput(
				CommonServlet.CSS_CONTENT_FRAME, m) {
			public void write(PrintWriter out) throws IOException {
				String acctName = reqState.getCurrentAccountID();
				String frameTitle = i18n.getString(
						"DriverScoringPie.PageTitle",
						"Ki&#x1EC3;m so&#x00E1;t nhi&#x00EA;n li&#x1EC7;u.");
				String chgURL = privLabel.getWebPageURL(reqState, pageName,
						COMMAND_INFO_UPDATE);
				String pageName = NewFuel.this.getPageName();
				String menuURL = privLabel.getWebPageURL(reqState,
						PAGE_MENU_TOP);
				String device = "";
				String ngaybatdau = "";
				String ngayketthuc = "";
				String giobatdau = "";
				String gioketthuc = "";
				String timezone = "0";
				String timestamp = "";
				int spunit = 1;
				int dsunit = 1;
				// frame content
				// view submit
				timezone = currAcct.getTimeZone();
				spunit = currAcct.getSpeedUnits();
				dsunit = currAcct.getDistanceUnits();
				String kph = "km/h";
				if (spunit == 0 && dsunit == 0)
					kph = "mph";
				ngaybatdau = AttributeTools.getRequestString(request,
						NewFuel.PARM_DATE_FROM, "");
				device = AttributeTools.getRequestString(request,
						NewFuel.PARM_DEVICE, "");
				ngayketthuc = AttributeTools.getRequestString(request,
						NewFuel.PARM_DATE_TO, "");
				giobatdau = "00:00";// AttributeTools.getRequestString(request,NewFuel.PARM_TIME_FROM,
				// "");
				gioketthuc = "23:59";// AttributeTools.getRequestString(request,
				// NewFuel.PARM_TIME_TO, "");
				DBReport objrp = new DBReport();
				String btnXem = AttributeTools.getRequestString(request,
						"btnview", "");

				String sql = "<div id=\"chartdiv\" style=\"width: 100%; height: 400px;\"></div>"
						+ GetNumScoring(ngaybatdau, ngayketthuc,
								currAcct.getAccountID(), device, giobatdau,
								gioketthuc, timezone, privLabel) + "";
				if (ngaybatdau == "")
					ngaybatdau = getCurDateTime();
				if (ngayketthuc == "")
					ngayketthuc = getCurDateTime();
				Date cda = new Date();
				int hour = cda.getHours();
				int min = cda.getMinutes();
				String page_cmd = request.getParameter("page_cmd");
				// String linkMap="";
				// String urlMap = "";
				if (page_cmd == "" || page_cmd == null) {
					int hour1 = 0;
					String sh = Integer.toString(hour);
					if (hour < 10)
						sh = "0" + sh;
					if (min <= 15)
						min = 15;
					else if (min <= 30 && min > 15)
						min = 30;
					else if (min <= 45 && min > 30)
						min = 45;
					else
						min = 45;
					String sm = Integer.toString(min);
					// sm ="00";
					gioketthuc = sh + ":" + sm;
					if (hour > 3) {
						hour1 = hour - 3;
					}
					String sh1 = Integer.toString(hour1);
					if (hour1 < 10) {
						sh1 = "0" + sh1;
					} else
						hour1 = 0;
					giobatdau = sh1 + ":" + sm;
				} else {
					String accID = currAcct.toString();
					String userID = "admin";
					if (currUser != null) {
						String userAcc = currUser.toString();
						userID = userAcc.substring(userAcc.indexOf("/") + 1);
					}
					// DBReport objrp = new DBReport();
					String str1 = ngaybatdau + " " + giobatdau;
					String str2 = ngayketthuc + " " + gioketthuc;
					long startDate = objrp.ConvertToEpoch(str1, timezone);
					long endDate = objrp.ConvertToEpoch(str2, timezone);
					// urlMap = "Track?account="+ accID + "&user=" + userID +
					// "&device=" + device +
					// "&page_cmd_arg&r_limit&date_tz="+timezone+"&r_report=EventDetailByStatusCode&r_text&page=report.show&r_emailAddr=&r_limType&date_fr="+
					// String.valueOf(startDate)
					// +"&page_cmd=rptsel&r_menu=menu.rpt.devDetail&date_to=" +
					// String.valueOf(endDate)+ "&fmt=map_scoring";
					// linkMap="<span style='margin-left: 10px;margin-right:5px;'><input class='btn' type ='button' id ='btnviewMap' onclick=\"javascript:openResizableWindow('Track?account="+
					// accID + "&user=" + userID + "&device=" + device +
					// "&page_cmd_arg&r_limit&date_tz="+timezone+"&r_report=EventDetailByStatusCode&r_text&page=report.show&r_emailAddr=&r_limType&date_fr="+
					// String.valueOf(startDate)
					// +"&page_cmd=rptsel&r_menu=menu.rpt.devDetail&date_to=" +
					// String.valueOf(endDate)+
					// "&fmt=map_scoring&r_option=All','ReportMap',700,500);\" value ='View Map' /></span>";
				}
				out.println("<span class='" + CommonServlet.CSS_MENU_TITLE
						+ "'>" + frameTitle + "</span><br/><hr>");
				out.println("<form name='AccountInfo' method='post' action='"
						+ chgURL + "' target='_self'>\n");
				out.println("<table class='"
						+ CommonServlet.CSS_ADMIN_VIEW_TABLE
						+ "' cellspacing='0' callpadding='0' border='0' width='100%' style='padding:15px 0 15px'>\n");
				out.print("<tr>\n");
				out.print("<td>" + i18n.getString("DeviceSelect", "Chọn xe:")
						+ "\n");
				String user1 = "";
				if (currAcct.getCurrentUser() != null) {
					String[] user = currAcct.getCurrentUser().toString()
							.split("/");
					user1 = user[1];
				} else {
					user1 = "Admin";
				}
				out.print(CreateCbbDevice(currAcct.getAccountID(), user1,
						device));
				out.print("<span style='margin-left: 10px;margin-right:5px; margin-bottom:4px;'>"
						+ i18n.getString("ReportPieChar.DateFrom", " Từ ngày: ")
						+ "</span>\n");
				out.print(Form_TextField(
						NewFuel.PARM_DATE_FROM,
						NewFuel.PARM_DATE_FROM,
						true,
						ngaybatdau,
						"displayCalendar(this,'dd/mm/yyyy',this,false,'',this.id)",
						20, 40)
						+ "\n<script language ='javascript' type ='text/javascript'> var now=new Date();var nam=now.getFullYear();var thang=now.getMonth() + 1;var ngay=now.getDate(); document.getElementById('"
						+ NewFuel.PARM_DATE_FROM
						+ "').value ='"
						+ ngaybatdau
						+ "'; </script>\n");
				// out.print (CreateCbbTime(NewFuel.PARM_TIME_FROM, giobatdau));
				out.print("<span style='margin-left: 10px;margin-right:5px;'>"
						+ i18n.getString("ReportPieChar.DateTo", " Đến ngày: ")
						+ "</span>\n");
				out.print(Form_TextField(
						NewFuel.PARM_DATE_TO,
						NewFuel.PARM_DATE_TO,
						true,
						ngaybatdau,
						"displayCalendar(this,'dd/mm/yyyy',this,false,'',this.id)",
						20, 40)
						+ "\n<script language ='javascript' type ='text/javascript'> var now=new Date();var nam=now.getFullYear();var thang=now.getMonth() + 1;var ngay=now.getDate(); document.getElementById('"
						+ NewFuel.PARM_DATE_TO
						+ "').value ='"
						+ ngayketthuc
						+ "'; </script>\n");
				// out.print (CreateCbbTime(NewFuel.PARM_TIME_TO, gioketthuc));
				// out.print (linkMap);
				// DBReport objrp = new DBReport();
				out.print("</td>");
				out.println("</tr>");
				out.print("<tr>");
				out.print("<td>");
				out.println("<div class=\"viewhoz\" style='padding:5px 0; margin:5px 0'>");

				out.print("<span style='margin-left: 55px'> <input type ='submit' id ='btnview' value ='Xem' name ='btnview' class='button1'/></span>");
				out.println("</div>");
				out.println("</td>");
				out.println("</tr>");
				out.print("<tr>");

				// objrp.GetDeviceCode(currAcct.getAccountID(), device) == null;
				String temp = null;
				try {
					temp = objrp.GetDeviceCode(currAcct.getAccountID(), device);
				} catch (DBException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (btnXem.equals("Xem")) {
					if (temp.equals("TSTZ05RS232")) {
						out.print("<td style ='height:400px; border:solid 1px silver;background-color:white;' ><div style ='position:relative;'>"
								+ sql
								+ "<div style ='height:20px; position:absolute; width:200px; top:0px; left:0px; z-index:30000; background-color:white;'></div></div></td>");
					} else if (!temp.equals("TSTZ05RS232")) {
						out.print("<script type=\"text/javascript\">");
						out.print("alert('Tài khoản của bạn không thể xem được báo cáo này');");
						out.print("</script>");
					}
				}
				out.println("</tr>");
				out.print("<tr>");
				out.print("<td>");
				out.print("<span style='margin-left: 55px; font-weight:bold'>Chú thích:</span><br/>");
				out.print("<span style='margin-left: 65px;'>- Trục thẳng đứng là mức nhiên liệu</span><br/>");
				out.print("<span style='margin-left: 65px;'>- Trục nằm ngang là thời gian(giờ)</pan><br/>");
				out.println("</td>");
				out.println("</tr>");
				out.println("</table>");
				/* end of form */
				out.write("<span style='padding-left:10px'>&nbsp;</span>\n");
				out.write("</form>\n");
			}
		};
		/* onload alert message? */
		String onload = null;
		onload = "";
		/* write frame */
		CommonServlet.writePageFrame(reqState, onload, null, // onLoad/onUnload
				HTML_CSS, // Style sheets
				HTML_JS, // JavaScript
				null, // Navigation
				HTML_CONTENT); // Content
	}
}