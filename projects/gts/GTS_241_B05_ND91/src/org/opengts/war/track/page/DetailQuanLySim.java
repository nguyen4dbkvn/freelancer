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
// Change History:
//  2007/12/13  Martin D. Flynn
//     -Initial release
// ----------------------------------------------------------------------------
package org.opengts.war.track.page;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.TimeZone;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.opengts.util.*;
import org.opengts.dbtools.*;
import org.opengts.db.*;
import org.opengts.db.tables.*;
import org.opengts.war.tools.*;
import org.opengts.war.report.*;
import org.opengts.war.track.*;
import org.opengts.db.tables.EventData;

public class DetailQuanLySim extends WebPageAdaptor implements Constants {

	// ------------------------------------------------------------------------
	// Parameters
	public static final String COMMAND_INFO_UPDATE = "update";
	// button types
	public static final String PARM_BUTTON_CANCEL = "a_btncan";
	public static final String PARM_BUTTON_BACK = "a_btnbak";

	// parameters
	// thanhtq
	public static final String PARM_DATE_SL = "a_date";
	public static final String PARM_DEVICE_SL = "a_device";
	public static int record = 0;

	// ------------------------------------------------------------------------
	// WebPage interface
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// Reports: "device.detail"
	// - Event Detail
	// - Temperature Monitoring
	// - J1708 Fault codes

	public DetailQuanLySim() {
		this.setBaseURI(RequestProperties.TRACK_BASE_URI());
		this.setPageName(PAGE_MENU_RPT_QLSIM);
		this.setPageNavigation(new String[] { PAGE_LOGIN, PAGE_MENU_TOP });
		this.setLoginRequired(true);
		// this.setReportType(ReportFactory.REPORT_TYPE_DEVICE_DETAIL);
	}

	// ------------------------------------------------------------------------

	public String getMenuName(RequestProperties reqState) {
		return MenuBar.MENU_REPORTS_QLSIM;
	}

	public String getMenuDescription(RequestProperties reqState,
			String parentMenuName) {
		return "";
		/*
		 * PrivateLabel privLabel = reqState.getPrivateLabel(); I18N i18n =
		 * privLabel.getI18N(DetailQuanLySim.class); String devTitles[] =
		 * reqState.getDeviceTitles(); return
		 * super._getMenuDescription(reqState,
		 * i18n.getString("ReportMenuQuanLySIM.menuDesc","{0} Detail Reports",
		 * devTitles));
		 */
	}

	public String getMenuHelp(RequestProperties reqState, String parentMenuName) {
		return "";
		/*
		 * PrivateLabel privLabel = reqState.getPrivateLabel(); I18N i18n =
		 * privLabel.getI18N(DetailQuanLySim.class); String devTitles[] =
		 * reqState.getDeviceTitles(); return
		 * super._getMenuHelp(reqState,i18n.getString
		 * ("ReportMenuQuanLySIM.menuHelp","Display various {0} detail reports",
		 * devTitles));
		 */
	}

	// ------------------------------------------------------------------------

	public String getNavigationDescription(RequestProperties reqState) {
		return "";
		/*
		 * PrivateLabel privLabel = reqState.getPrivateLabel(); I18N i18n =
		 * privLabel.getI18N(DetailQuanLySim.class); String devTitles[] =
		 * reqState.getDeviceTitles(); return
		 * super._getNavigationDescription(reqState
		 * ,i18n.getString("ReportMenuQuanLySIM.navDesc","{0} Detail",
		 * devTitles));
		 */
	}

	public String getNavigationTab(RequestProperties reqState) {
		return "";
		/*
		 * PrivateLabel privLabel = reqState.getPrivateLabel(); I18N i18n =
		 * privLabel.getI18N(DetailQuanLySim.class); String devTitles[] =
		 * reqState.getDeviceTitles(); return
		 * super._getNavigationTab(reqState,i18n
		 * .getString("ReportMenuQuanLySIM.navTab","{0} Detail", devTitles));
		 */
	}

	private String ConvertFromEpoch(long epoch, String timezone) {
		TimeZone tz = DateTime.getTimeZone(timezone, null);
		DateTime dt = new DateTime(epoch);
		String dtFmt = dt.format("dd/MM/yyyy HH:mm:ss", tz);
		return dtFmt;
	}

	public String report(String tungay, String devid, String timezone,
			String accid, String reportType) throws IOException {
		String strscr = "";

		try {
			DBCamera objcmr = new DBCamera();
			// Lay thong tin chi tiet check sim
			ArrayList<CheckSIM> ed = objcmr.StaticReportDetailQLSIM(accid,
					devid, tungay, timezone, reportType);
			if (ed.size() > 0) {
				String day = ConvertFromEpoch(ed.get(0).gettimestamp(),
						timezone);
				strscr = "<div class='detailQLSIM' style='padding:10px;'><div class='item-data'><h3>Thông tin quản lý SIM thiết bị của xe "
						+ ed.get(0).getdeviceID() + "</h3></div><hr/>";
				strscr += "<div class='item-data'><label style='display:inline-block;text-align: right; padding: 5px; font-weight:bold;width:150px'>Biển số xe: </label><label>"
						+ ed.get(0).getdeviceID() + "</label></div>";
				strscr += "<div class='item-data'><label style='display:inline-block;text-align: right;padding: 5px; font-weight:bold;width:150px'>IMEI: </label><label>"
						+ ed.get(0).getimei() + "</label></div>";
				strscr += "<div class='item-data'><label style='display:inline-block;text-align: right; padding: 5px; font-weight:bold;width:150px'>Số SIM thiết bị: </label><label>"
						+ ed.get(0).getsimPhoneNumber() + "</label></div>";
				strscr += "<div class='item-data'><label style='display:inline-block;text-align: right; padding: 5px; font-weight:bold;width:150px'>Số điện thoại chủ xe: </label><label>"
						+ ed.get(0).getphoneCX() + "</label></div>";
				strscr += "<div class='item-data'><label style='display:inline-block;text-align: right; padding: 5px; font-weight:bold;width:150px'>Thông tin: </label><label>"
						+ ed.get(0).getcheckThongtin() + "</label></div>";
				strscr += "<div class='item-data'><label style='display:inline-block;text-align: right; padding: 5px; font-weight:bold;width:150px'>Thời điểm: </label><label>"
						+ day + "</label></div>";
				strscr += "</div><hr/><div style='padding: 10px; font-weight:bold;line-height:20px;'><i>Lưu ý: Bạn phải nạp tiền cho SIM thiết bị nếu Tài Khoản Chính < 10.000d <br/>và Tài Khoản Lưu Lượng(LLKM1) < 500Kb tính đến ngày 25 hàng tháng.</i></div>";
				strscr += "<div style='padding: 10px; font-weight:bold;line-height:20px;'><button type='submit' style='padding:7px;font-weight:bold;' name='nap_tien'>Nạp tiền</button></div>";
			} else {
				strscr = "<div>Không có dữ liệu.</div>";
			}
		} catch (Exception e) {
		}

		return strscr;
	}

	public static String GetUTF8FromNCRDecimalString(String s) {
		String a, b;
		a = "";
		b = "";
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
		return a;
	}

	public void writePage(final RequestProperties reqState, String pageMsg)
			throws IOException {
		final PrivateLabel privLabel = reqState.getPrivateLabel();
		final I18N i18n = privLabel.getI18N(baoCaoTramTheoNgay.class);
		final Locale locale = reqState.getLocale();
		final Account currAcct = reqState.getCurrentAccount();
		final User currUser = reqState.getCurrentUser();
		final String pageName = this.getPageName();
		String m = pageMsg;
		boolean error = false;

		HttpServletRequest request = reqState.getHttpServletRequest();
		HttpServletResponse response = reqState.getHttpServletResponse();

		String xe = AttributeTools.getRequestString(request, "device", "");

		String tuNgay = AttributeTools.getRequestString(request, "tuNgay", ""); //
		String reportType = AttributeTools.getRequestString(request,
				"check_sim", "");
		int i = 0;

		/* Style */
		HTMLOutput HTML_CSS = new HTMLOutput() {
			public void write(PrintWriter out) throws IOException {
				String cssDir = DetailQuanLySim.this.getCssDirectory();
				WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
				WebPageAdaptor.writeCssLink(out, reqState, "scrollbar.css",
						cssDir);
			}
		};

		/* javascript */
		HTMLOutput HTML_JS = new HTMLOutput() {
			public void write(PrintWriter out) throws IOException {
				MenuBar.writeJavaScript(out, pageName, reqState);

				out.println("        <script type=\"text/javascript\" src=\"js/jquery-1.4.2.min.js\"></script>\n");
				out.println("        <script type=\"text/javascript\" src=\"js/PCalendar.js\"></script>\n");
				out.println(" <script type='text/javascript' src='js/jquery.tablesorter.min.js'></script>");
				out.println("<script type='text/javascript' src='js/sorttable.js'></script>");
				out.print("<script src='./js/jquery.tinyscrollbar.min.js' type=\"text/javascript\"></script>");
				out.print("<script type='text/javascript'>$(document).ready(function() {$('#scrollbar2').tinyscrollbar();}); </script>");
				out.println("<script type='text/javascript' > $(function(){$('#myTable').tablesorter(); }); </script>");

			}
		};

		/* Content */

		HTMLOutput HTML_CONTENT = new HTMLOutput(
				CommonServlet.CSS_CONTENT_FRAME, m) {
			public void write(PrintWriter out) throws IOException {
				// Print.logStackTrace("here");

				// String menuURL =
				// EncodeMakeURL(reqState,RequestProperties.TRACK_BASE_URI(),PAGE_MENU_TOP);
				String menuURL = privLabel.getWebPageURL(reqState,
						PAGE_MENU_TOP);
				// String chgURL =
				// EncodeMakeURL(reqState,RequestProperties.TRACK_BASE_URI(),pageName,COMMAND_INFO_UPDATE);
				String chgURL = privLabel.getWebPageURL(reqState, pageName,
						COMMAND_INFO_UPDATE);

				// view submit
				HttpServletRequest request = reqState.getHttpServletRequest();
				String tuNgay = AttributeTools.getRequestString(request,
						"date_fr", "");
				String date_tz = AttributeTools.getRequestString(request,
						"date_tz", "");
				String contentall = "";

				String typeCheckSim = AttributeTools.getRequestString(request,
						"check_sim", "");
				int pindex = 1;
				int pindexl = 0;
				int pagesize = 1000;
				int tongtrang = 0;
				String flag = "0";
				int pagestatic = 1;
				String urlmap = "";
				//
				contentall = AttributeTools.getRequestString(request, "device",
						"");

				String sdate = request.getParameter("date_fr");
				if (sdate != null) {
					if (sdate != "") {
						tuNgay = sdate;
					}
				}
				String sdate_tz = request.getParameter("date_tz");
				if (sdate_tz != null) {
					if (sdate != "") {
						date_tz = sdate_tz;
					}
				}

				String sdevice = request.getParameter("device");
				String stypeCheckSIM = request.getParameter("check_sim");

				if (sdevice != null) {
					if (sdevice != "") {
						contentall = sdevice;
					}
				}

				if (stypeCheckSIM != null) {
					if (stypeCheckSIM != "") {
						typeCheckSim = stypeCheckSIM;
					}
				}

				String sql = report(tuNgay, contentall, date_tz,
						currAcct.getAccountID(), typeCheckSim);

				out.print(sql);
			}
		};

		/* write frame */
		String onload = error ? JS_alert(true, m) : null;
		CommonServlet.writePageFrame(reqState, onload, null, // onLoad/onUnload
				HTML_CSS, // Style sheets
				HTML_JS, // JavaScript
				null, // Navigation
				HTML_CONTENT); // Content

	}

	// ------------------------------------------------------------------------

}