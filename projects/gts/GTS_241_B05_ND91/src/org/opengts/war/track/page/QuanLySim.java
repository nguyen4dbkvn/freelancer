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

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.TimeZone;
import java.io.*;

import javax.servlet.*;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.opengts.servers.GPSEvent;
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
import org.opengts.extra.servers.sanav.Main;

public class QuanLySim extends WebPageAdaptor implements Constants {

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

	public QuanLySim() {
		this.setBaseURI(RequestProperties.TRACK_BASE_URI());
		this.setPageName(PAGE_MENU_RPT_QLSIM);
		this.setPageNavigation(new String[] { PAGE_LOGIN, PAGE_MENU_TOP });
		this.setLoginRequired(true);
	}

	// ------------------------------------------------------------------------

	public String getMenuName(RequestProperties reqState) {
		return MenuBar.MENU_REPORTS_QLSIM;
	}

	public String getMenuDescription(RequestProperties reqState,
			String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(QuanLySim.class);
		String devTitles[] = reqState.getDeviceTitles();
		return super._getMenuDescription(reqState, i18n
				.getString("ReportMenuQuanLySIM.menuDesc",
						"{0} Detail Reports", devTitles));
	}

	public String getMenuHelp(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(QuanLySim.class);
		String devTitles[] = reqState.getDeviceTitles();
		return super._getMenuHelp(reqState, i18n.getString(
				"ReportMenuQuanLySIM.menuHelp",
				"Display various {0} detail reports", devTitles));
	}

	// ------------------------------------------------------------------------

	public String getNavigationDescription(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(QuanLySim.class);
		String devTitles[] = reqState.getDeviceTitles();
		return super._getNavigationDescription(reqState, i18n.getString(
				"ReportMenuQuanLySIM.navDesc", "{0} Detail", devTitles));
	}

	public String getNavigationTab(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(QuanLySim.class);
		String devTitles[] = reqState.getDeviceTitles();
		return super._getNavigationTab(reqState, i18n.getString(
				"ReportMenuQuanLySIM.navTab", "{0} Detail", devTitles));
	}

	public String CreateCbbDevice(String accountid, String idselect)
			throws IOException {
		DBCamera db = new DBCamera();

		String strre = "<select id ='device' name = 'device' class='textReadOnly'>";

		if (idselect.equalsIgnoreCase("All"))
			strre += "<option value ='All' selected =\"selected\">Tất cả</option>\n";
		else
			strre += "<option value ='All'>Tất cả</option>\n";

		try {
			List<String[]> list = db.Device_SelectDrive(accountid);
			for (String[] rs : list) {
				if (idselect.equalsIgnoreCase(rs[0]))
					strre += "<option value ='" + rs[0]
							+ "' selected =\"selected\">" + rs[0]
							+ "</option>\n";
				else
					strre += "<option value ='" + rs[0] + "'>" + rs[0]
							+ "</option>\n";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		strre += "</select>\n";
		strre += "<script type ='text/javascript' language ='javascript'> document.getElementById('device ').value = '"
				+ idselect + "';</script>\n";
		return strre;
	}

	private String ConvertFromEpoch(long epoch, String timezone) {
		TimeZone tz = DateTime.getTimeZone(timezone, null);
		DateTime dt = new DateTime(epoch);
		String dtFmt = dt.format("dd/MM/yyyy HH:mm:ss", tz);
		return dtFmt;
	}

	public String report(String tungay, String denngay, String devid,
			String timezone, String accid, PrivateLabel privLabel,
			String reportType, RequestProperties reqState) throws IOException {
		String strscr = "<table  id='myTable' width='100%' class='adminSelectTable_sortable' cellspacing='1' ><thead><tr  align='center'><th width='15px' class='adminTableHeaderCol_sort'></th><th class='adminTableHeaderCol_sort' width='100px'>STT</th><th  class='adminTableHeaderCol_sort' width='110px'>Xe</th>"
				+ "<th class='adminTableHeaderCol_sort' width='110px'>Số SIM</th><th width='150px' class='adminTableHeaderCol_sort'>Th\u1EDDi \u0111i\u1EC3m</th><th class='adminTableHeaderCol_sort'>Nội dung</th><th width='120px' class='adminTableHeaderCol_sort'>Kiểm tra ngay</th>"
				+ "<th width='120px' class='adminTableHeaderCol_sort'>Thông tin chi tiết</th></tr></thead>";
		int count = 0;
		String urlmap = "", urlcurrent = "";
		User currUser = reqState.getCurrentUser();

		try {
			DBCamera objcmr = new DBCamera();

			if (devid.equals("All")) {

				List<String[]> list = objcmr.Device_SelectDrive(accid);

				for (String[] rs : list) {

					ArrayList<CheckSIM> ed = objcmr.StaticReportQLSIM(accid,
							rs[0].toString(), tungay, denngay, timezone,
							reportType);
					count++;
					if (ed.size() > 0) {

						if (currUser != null) {
							urlcurrent = "./Track?page=menu.rpt.qlSim&account="
									+ accid
									+ "&user="
									+ currUser.getUserID()
									+ "&device="
									+ ed.get(0).getdeviceID()
									+ "&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="
									+ ed.get(0).gettimestamp()
									+ "&fmt=map&checknow=CheckNow&simPhoneNumber="
									+ ed.get(0).getsimPhoneNumber();

							urlmap = "./Track?page=menu.rpt.detailQLSim&account="
									+ accid
									+ "&user="
									+ currUser.getUserID()
									// + "&check_sim="
									// + reportType
									+ "&device="
									+ ed.get(0).getdeviceID()
									+ "&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="
									+ ed.get(0).gettimestamp() + "&fmt=map";
						} else {

							urlcurrent = "./Track?page=menu.rpt.qlSim&account="
									+ accid
									+ "&user=admin"
									// + "&check_sim="
									// + reportType
									+ "&device="
									+ ed.get(0).getdeviceID()
									+ "&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="
									+ ed.get(0).gettimestamp()
									+ "&fmt=map&checknow=CheckNow&simPhoneNumber="
									+ ed.get(0).getsimPhoneNumber();

							urlmap = "./Track?page=menu.rpt.detailQLSim&account="
									+ accid
									+ "&user=admin"
									// + "&check_sim="
									// + reportType
									+ "&device="
									+ ed.get(0).getdeviceID()
									+ "&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="
									+ ed.get(0).gettimestamp() + "&fmt=map";
						}

						String day = ConvertFromEpoch(ed.get(0).gettimestamp(),
								timezone);

						strscr += "<tr class ='adminTableBodyRowOdd'"
								+ "><td> <input type='checkbox' />"
								+ "</td><td>"
								+ count
								+ "</td><td>"
								+ ed.get(0).getdeviceID()
								+ "</td><td>"
								+ ed.get(0).getsimPhoneNumber()
								+ "</td><td>"
								+ day
								+ "</td><td>"
								+ ed.get(0).getcheckThongtin()
								+ "</td><td><a style='color:#008AFF' href=\"javascript:openURL('"
								+ urlcurrent
								+ "','_self');\">Kiểm tra ngay</a></td>"
								+ "</td><td><a style='color:#008AFF' href=\"javascript:openURL('"
								+ urlmap + "','_blank');\">Chi tiết</a></td>";

						strscr += "</tr>";
						// }
					} else {
						if (currUser != null) {
							urlcurrent = "./Track?page=menu.rpt.qlSim&account="
									+ accid
									+ "&user="
									+ currUser.getUserID()
									+ "&device="
									+ rs[0].toString()
									+ "&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="
									+ "0"
									+ "&fmt=map&checknow=CheckNow&simPhoneNumber="
									+ rs[1].toString();

							urlmap = "./Track?page=menu.rpt.detailQLSim&account="
									+ accid
									+ "&user="
									+ currUser.getUserID()

									+ "&device="
									+ rs[0].toString()
									+ "&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="
									+ rs[1].toString() + "&fmt=map";
						} else {

							urlcurrent = "./Track?page=menu.rpt.qlSim&account="
									+ accid
									+ "&user=admin"

									+ "&device="
									+ rs[0].toString()
									+ "&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="
									+ "0"
									+ "&fmt=map&checknow=CheckNow&simPhoneNumber="
									+ rs[1].toString();

							urlmap = "./Track?page=menu.rpt.detailQLSim&account="
									+ accid
									+ "&user=admin"
									+ "&device="
									+ rs[0].toString()
									+ "&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="
									+ "0" + "&fmt=map";
						}

						strscr += "<tr class ='adminTableBodyRowOdd'"
								+ "><td> <input type='checkbox' />"
								+ "</td><td>" + count + "</td><td>"
								+ rs[0].toString() + "</td><td>"
								+ rs[1].toString().trim() == "" ? "Thiết bị không có SIM"
								: rs[1].toString()
										+ "</td><td>-</td><td>Không có dữ liệu, bấm 'Kiểm tra ngay' để xem hướng dẫn."
										+ "</td><td><a style='color:#008AFF' href=\"javascript:openURL('"
										+ urlcurrent
										+ "','_self');\">Kiểm tra ngay</a></td>"
										+ "</td><td>-</td>";

						strscr += "</tr>";
					}
				}
			} else {

				ArrayList<CheckSIM> ed = objcmr.StaticReportQLSIM(accid, devid,
						tungay, denngay, timezone, reportType);

				record = ed.size();

				for (int j = 0; j < record; j++) {
					if (currUser != null) {
						urlcurrent = "./Track?page=menu.rpt.qlSim&account="
								+ accid
								+ "&user="
								+ currUser.getUserID()
								+ "&device="
								+ ed.get(j).getdeviceID()
								+ "&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="
								+ ed.get(j).gettimestamp()
								+ "&fmt=map&checknow=CheckNow&simPhoneNumber="
								+ ed.get(j).getsimPhoneNumber();

						urlmap = "./Track?page=menu.rpt.detailQLSim&account="
								+ accid
								+ "&user="
								+ currUser.getUserID()
								+ "&device="
								+ ed.get(j).getdeviceID()
								+ "&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="
								+ ed.get(j).gettimestamp() + "&fmt=map";
					} else {

						urlcurrent = "./Track?page=menu.rpt.qlSim&account="
								+ accid
								+ "&user=admin"
								+ "&device="
								+ ed.get(j).getdeviceID()
								+ "&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="
								+ ed.get(j).gettimestamp()
								+ "&fmt=map&checknow=CheckNow&simPhoneNumber="
								+ ed.get(j).getsimPhoneNumber();

						urlmap = "./Track?page=menu.rpt.detailQLSim&account="
								+ accid
								+ "&user=admin"
								+ "&device="
								+ ed.get(j).getdeviceID()
								+ "&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="
								+ ed.get(j).gettimestamp() + "&fmt=map";
					}

					String css = "adminTableBodyRowOdd";
					String day = ConvertFromEpoch(ed.get(j).gettimestamp(),
							timezone);

					strscr = strscr
							+ "<tr class ="
							+ css
							+ "><td> <input type='checkbox' />"
							+ "</td><td>"
							+ (j + 1)
							+ "</td><td>"
							+ ed.get(j).getdeviceID()
							+ "</td><td>"
							+ ed.get(0).getsimPhoneNumber()
							+ "</td><td>"
							+ day
							+ "</td><td>"
							+ ed.get(j).getcheckThongtin()
							+ "</td><td><a style='color:#008AFF' href=\"javascript:openURL('"
							+ urlcurrent
							+ "','_self');\">Kiểm tra ngay</a></td>"
							+ "</td><td><a style='color:#008AFF' href=\"javascript:openURL('"
							+ urlmap + "','_blank');\">Chi tiết</a></td>";

					strscr = strscr + "</tr>";
				}

				if (record == 0) {
					if (currUser != null) {
						urlcurrent = "./Track?page=menu.rpt.qlSim&account="
								+ accid
								+ "&user="
								+ currUser.getUserID()
								+ "&device="
								+ devid
								+ "&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="
								+ "0"
								+ "&fmt=map&checknow=CheckNow&simPhoneNumber="
								+ objcmr.getSimPhoneNumber(devid);

						urlmap = "./Track?page=menu.rpt.detailQLSim&account="
								+ accid
								+ "&user="
								+ currUser.getUserID()
								+ "&device="
								+ devid
								+ "&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="
								+ objcmr.getSimPhoneNumber(devid) + "&fmt=map";
					} else {

						urlcurrent = "./Track?page=menu.rpt.qlSim&account="
								+ accid
								+ "&user=admin"

								+ "&device="
								+ devid
								+ "&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="
								+ "0"
								+ "&fmt=map&checknow=CheckNow&simPhoneNumber="
								+ objcmr.getSimPhoneNumber(devid);

						urlmap = "./Track?page=menu.rpt.detailQLSim&account="
								+ accid
								+ "&user=admin"
								+ "&device="
								+ devid
								+ "&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="
								+ "0" + "&fmt=map";
					}

					String simPhone = objcmr.getSimPhoneNumber(devid);
					strscr += "<tr class ='adminTableBodyRowOdd'"
							+ "><td> <input type='checkbox' />" + "</td><td>"
							+ 1 + "</td><td>" + devid + "</td><td>"
							+ simPhone.toString().trim() == "" ? "Thiết bị không có SIM"
							: simPhone
									+ "</td><td>-</td><td>Không có dữ liệu, bấm 'Kiểm tra ngay' để xem hướng dẫn."
									+ "</td><td><a style='color:#008AFF' href=\"javascript:openURL('"
									+ urlcurrent
									+ "','_self');\">Kiểm tra ngay</a></td>"
									+ "</td><td>-</td>";
					strscr += "</tr>";
				}
			}

			strscr = strscr + "</table>";
		}

		catch (Exception e) {
		}
		return strscr;

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
		String excel = AttributeTools.getRequestString(request, "btnExcel", "");

		String xe = AttributeTools.getRequestString(request, "device", "");
		String tuNgay = AttributeTools.getRequestString(request, "tuNgay", "");
		String denNgay = AttributeTools
				.getRequestString(request, "denNgay", "");
		String reportType = AttributeTools.getRequestString(request,
				"check_sim", "");
		int i = 0;

		// Bam nut CheckNow ----------------------

		if (excel != "") {

			if (excel.equals("Xuất Excel")) {
				java.util.Calendar c = java.util.Calendar.getInstance();
				Date now = c.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				String d = sdf.format(now);
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setHeader("Content-Disposition",
						"attachment; filename=baoCaoQuanLySIM_" + d + ".xls");
				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFSheet sheet = wb.createSheet("Dispatch");
				HSSFRow title = sheet.createRow((short) 1);

				HSSFCellStyle cst = wb.createCellStyle();
				HSSFFont ft = wb.createFont();
				cst.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cst.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				ft.setFontHeightInPoints((short) 18);

				ft.setBoldweight((short) ft.BOLDWEIGHT_BOLD);
				cst.setFont(ft);
				HSSFCell ct = title.createCell((short) 0);

				ct.setCellStyle(cst);
				ct.setCellValue("BÁO CÁO QUẢN LÝ SIM");
				title.setHeightInPoints(40);
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));
				HSSFCellStyle csNgay = wb.createCellStyle();

				HSSFFont fngay = wb.createFont();
				HSSFRow rngay = sheet.createRow((short) 2);

				fngay.setFontHeightInPoints((short) 10);
				fngay.setBoldweight((short) fngay.BOLDWEIGHT_BOLD);
				csNgay.setFont(fngay);
				HSSFCell cTuNgay = rngay.createCell((short) 1);
				cTuNgay.setCellStyle(csNgay);
				cTuNgay.setCellValue("Từ ");
				rngay.createCell((short) 2).setCellValue(tuNgay);
				HSSFCell cDenNgay = rngay.createCell((short) 3);
				cDenNgay.setCellStyle(csNgay);
				cDenNgay.setCellValue("Đến ");
				rngay.createCell((short) 4).setCellValue(denNgay);

				i = i + 3;
				HSSFCellStyle cellStyle = wb.createCellStyle();
				cellStyle.setBorderTop((short) 1);
				cellStyle.setBorderRight((short) 1);
				cellStyle.setBorderLeft((short) 1);
				cellStyle.setBorderBottom((short) 1);
				cellStyle
						.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cellStyle.setWrapText(true);
				HSSFFont f = wb.createFont();
				f.setFontHeightInPoints((short) 11);

				f.setBoldweight((short) f.BOLDWEIGHT_BOLD);
				cellStyle.setFont(f);

				HSSFRow rowhead = sheet.createRow((short) i);
				HSSFCell h0 = rowhead.createCell((short) 0);
				h0.setCellStyle(cellStyle);
				h0.setCellValue("Tài khoản");

				HSSFCell h1 = rowhead.createCell((short) 1);
				h1.setCellStyle(cellStyle);
				h1.setCellValue("Biển số xe");

				HSSFCell h2 = rowhead.createCell((short) 2);
				h2.setCellStyle(cellStyle);
				h2.setCellValue("Thời gian");

				HSSFCell h3 = rowhead.createCell((short) 3);
				h3.setCellStyle(cellStyle);
				h3.setCellValue("IMEI");

				HSSFCell h4 = rowhead.createCell((short) 4);
				h4.setCellStyle(cellStyle);
				h4.setCellValue("SĐT chủ xe");

				HSSFCell h5 = rowhead.createCell((short) 5);
				h5.setCellStyle(cellStyle);
				h5.setCellValue("Thông tin");

				HSSFCell h6 = rowhead.createCell((short) 6);
				h6.setCellStyle(cellStyle);
				h6.setCellValue("Mô tả");

				HSSFCell h7 = rowhead.createCell((short) 7);
				h7.setCellStyle(cellStyle);
				h7.setCellValue("SĐT thiết bị");

				try {
					DBCamera objcmr = new DBCamera();
					rowhead.setHeightInPoints((short) 40);

					ArrayList<CheckSIM> ed = objcmr.StaticReportQLSIM(
							currAcct.getAccountID(), xe, tuNgay, denNgay,
							currAcct.getTimeZone(), reportType);

					for (int j = 0; j < ed.size(); j++) {
						String day = ConvertFromEpoch(ed.get(j).gettimestamp(),
								currAcct.getTimeZone());

						HSSFCellStyle csr = wb.createCellStyle();
						csr.setBorderTop((short) 1);
						csr.setBorderRight((short) 1);
						csr.setBorderLeft((short) 1);
						csr.setBorderBottom((short) 1);
						HSSFRow row = sheet.createRow((short) (i + 1));

						HSSFCell r0 = row.createCell((short) 0);
						r0.setCellStyle(csr);
						r0.setCellValue(ed.get(j).getaccountID());

						HSSFCell r1 = row.createCell((short) 1);
						r1.setCellStyle(csr);
						r1.setCellValue(ed.get(j).getdeviceID());

						HSSFCell r2 = row.createCell((short) 2);
						r2.setCellStyle(csr);
						r2.setCellValue(day);

						HSSFCell r3 = row.createCell((short) 3);
						r3.setCellStyle(csr);
						r3.setCellValue(ed.get(j).getimei());

						HSSFCell r4 = row.createCell((short) 4);
						r4.setCellStyle(csr);
						r4.setCellValue(ed.get(j).getphoneCX());

						HSSFCell r5 = row.createCell((short) 5);
						r5.setCellStyle(csr);
						r5.setCellValue(ed.get(j).getcheckThongtin());

						HSSFCell r6 = row.createCell((short) 6);
						r6.setCellStyle(csr);
						r6.setCellValue(ed.get(j).getdecription());

						HSSFCell r7 = row.createCell((short) 7);
						r7.setCellStyle(csr);
						r7.setCellValue(ed.get(j).getsimPhoneNumber());

						i++;

					}
					sheet.autoSizeColumn(0);
					sheet.autoSizeColumn(1);
					sheet.autoSizeColumn(2);
					sheet.autoSizeColumn(3);
					sheet.autoSizeColumn(4);
					sheet.autoSizeColumn(5);
					sheet.autoSizeColumn(6);
					sheet.autoSizeColumn(7);

					OutputStream out = response.getOutputStream();
					wb.write(out);
					out.close();

				} catch (Exception e) {
				}
				return;
			}
		}

		/* Style */
		HTMLOutput HTML_CSS = new HTMLOutput() {
			public void write(PrintWriter out) throws IOException {
				String cssDir = QuanLySim.this.getCssDirectory();
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

				String menuURL = privLabel.getWebPageURL(reqState,
						PAGE_MENU_TOP);

				String chgURL = privLabel.getWebPageURL(reqState, pageName,
						COMMAND_INFO_UPDATE);
				String frameTitle = i18n.getString("baocaoTram.PageTitle",
						"Tr&#x1EA1;m");

				// frame content
				// view submit
				HttpServletRequest request = reqState.getHttpServletRequest();
				String tuNgay = AttributeTools.getRequestString(request,
						"tuNgay", "");
				String contentall = "";

				String denNgay = AttributeTools.getRequestString(request,
						"denNgay", "");
				// String typeCheckSim =
				// AttributeTools.getRequestString(request,
				// "check_sim", "");
				int pindex = 1;
				int pindexl = 0;
				int pagesize = 1000;
				int tongtrang = 0;
				String flag = "0";
				int pagestatic = 1;
				String urlmap = "";

				String checknow = AttributeTools.getRequestString(request,
						"btnCheckNow", "");

				String xem = AttributeTools.getRequestString(request,
						"btnview", "");
				contentall = AttributeTools.getRequestString(request, "device",
						"");

				String spindex = request.getParameter("pageindex");
				if (spindex != null) {
					if (spindex != "") {
						pindex = Integer.parseInt(spindex);
						pindexl = pindex;
						pagestatic = pindex;
					}
				}

				String sdate = request.getParameter("date");
				if (sdate != null) {
					if (sdate != "") {
						tuNgay = sdate;
					}
				}

				String sdateTo = request.getParameter("dateTo");
				if (sdateTo != null) {
					if (sdateTo != "") {
						denNgay = sdateTo;
					}
				}

				String sdevice = request.getParameter("device");
				String stypeCheckSIM = request.getParameter("check_sim");

				if (sdevice != null) {
					if (sdevice != "") {
						contentall = sdevice;
					}
				}

				String schecknow = request.getParameter("checknow");
				if (schecknow != null) {
					if (schecknow != "") {
						checknow = schecknow;
					}
				}

				String sflag = request.getParameter("flag");
				if (sflag != null) {
					if (sflag != "") {
						flag = sflag;
					}
				}

				String simPhoneNumber = "";
				try {
					String ssimPhoneNumber = request
							.getParameter("simPhoneNumber");
					if (ssimPhoneNumber != null) {
						if (ssimPhoneNumber != "") {
							simPhoneNumber = ssimPhoneNumber;
						}
					} else {
						DBCamera objcmr = new DBCamera();

						simPhoneNumber = objcmr.getSimPhoneNumber(contentall);
					}
				} catch (Exception e) {
				}

				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String d = sdf.format(now);
				out.println("<span class='"
						+ CommonServlet.CSS_MENU_TITLE
						+ "'>B\u00E1o c\u00E1o &#81;&#117;&#7843;&#110;&#32;&#108;&#253;&#32;&#83;&#73;&#77;</span><br/>");
				out.println("<hr/>");
				out.println("<form name='AccountInfo' method='post' action='"
						+ chgURL + "' target='_self'>\n");
				out.println("<table class='"
						+ CommonServlet.CSS_ADMIN_VIEW_TABLE
						+ "' cellspacing='0' callpadding='0' border='0' width='55%' style='padding:15 0 15'>\n");
				out.print("<tr>\n");
				out.print("<td width='100px' align='right'>Ch&#x1ECD;n Xe:</td><td align='left'  width='100px'>\n");
				out.print(CreateCbbDevice(currAcct.getAccountID(), contentall));
				out.print("</td>");

				String sql = "";

				if (xem != "") {
					if (xem.equalsIgnoreCase("Xem")) {
						sql = report(tuNgay, denNgay, contentall,
								currAcct.getTimeZone(),
								currAcct.getAccountID(), privLabel, "",
								reqState);
					}
				}

				out.println("</tr>");
				out.println("</table>");
				out.print("<table cellspacing='0' style='width:100%;padding:10px 0px 10px'><tbody><tr class='viewhoz'><td width='12%'></td><td width='20%'> <input type='submit' class='button1' name='btnview' value='Xem' id='btnview'></td><td align='left' width='12%'>"
						+ "<input type='submit' value='Kiểm tra ngay' name='btnCheckNow' class='button1'> </td><td align='left' width='12%'><td align='left' width='80%'><input type='submit' id='btnExcel' value='Xuất Excel' name='btnExcel' class='button1'></td></tr></tbody></table>");

				if (checknow != "") {
					out.println("<div class='div_huongdan'><h3> Để kiểm tra thông tin SIM soạn tin theo cú pháp: </h3>");

					if (simPhoneNumber == "") {
						DBCamera db = new DBCamera();
						try {
							List<String[]> list = db
									.Device_SelectDrive(currAcct.getAccountID());

							for (String[] rs : list) {
								simPhoneNumber += db.getSimPhoneNumber(rs[0])
										+ ", ";
							}

							simPhoneNumber = simPhoneNumber.substring(0,
									simPhoneNumber.length() - 2);

						} catch (Exception e) {

						}
					}

					out.println("<p class='huongdannhantin'>Kiểm tra tài khoản: <b>KTTK</b> gửi <b>"
							+ simPhoneNumber
							+ "</b></p>"
							+ "<p class='huongdannhantin'>Kiểm tra lưu lượng: <b>KTLL</b> gửi <b>"
							+ simPhoneNumber
							+ "</b> <i>(Đối với SIM Viettel)</i></p>");

					out.println("<p>Sau đó chờ trong giây lát bấm nút <b>Xem</b> để xem thông tin. Hoặc gọi đến tổng đài CSKH 19001853 để được hướng dẫn chi tiết.</p><div>");

					// if (simPhoneNumber != "") {
					//
					// // String stringType = " tài khoản ";
					// if (checknow.equalsIgnoreCase("Kiểm tra ngay")
					// || checknow.equalsIgnoreCase("CheckNow")) {
					// boolean bool1 = false, bool2 = false;
					// String mes = "";

					// int b = Integer.parseInt(typeCheckSim, 16);

					// "*123465,444,'*101#"

					// if (b == 1) {

					// bool2 = sendSMS("101", simPhoneNumber);
					//
					// bool1 = sendSMS("102", simPhoneNumber);
					// stringType = " lưu lượng ";
					// } else

					// if (!bool1 || !bool2) {
					// out.print("<h2>Có lỗi khi gửi tin nhắn.</h2>");
					// } else {
					//
					// DateFormat dateFormat = new SimpleDateFormat(
					// "dd/MM/yyyy HH:mm:ss");
					// Date date = new Date();
					//
					// long epf = ConvertToEpoch(
					// dateFormat.format(date),
					// currAcct.getTimeZone());
					//
					// String user = "admin";
					//
					// if (currUser != null)
					// user = currAcct.getAccountID();
					//
					// out.print("<div id='thongbao'> <h3>Đã gửi thành công tin nhắn kiểm tra thông tin SIM "
					// + "cho xe "
					// + contentall
					// +
					// ".</h3><br/><h3>Xin vui lòng chờ trong ít phút để hệ thống tự cập nhật thông tin.</h3>"
					// // +
					// //
					// "http://192.168.1.144:8080/WebsiteSendSMS/send8x68?sdt=&message="
					// // +
					// // URLEncoder.encode("*123465,444,'*101#","UTF-8")
					// // +"Mess: "+URLEncoder.encode("*123465,444,'*101#",
					// // "UTF-8")
					// + "<script>  "
					// + "  var xmlhttp;  setInterval(function(){"
					// + "	$.ajax({"
					// + "url: 'AjaxCheckSMS',"
					// + "type: 'GET',"
					// + "dataType: 'html',contentType: 'charset=utf-8', "
					// + "data:{time:'"
					// + epf
					// + "',account:'"
					// + currAcct.getAccountID()
					// + "'"
					// // + ",check_sim:'"
					// // + typeCheckSim
					// + ",device:'"
					// + contentall
					// + "',date_tz:'"
					// + currAcct.getTimeZone()
					// + "',user:'"
					// + user
					// + "'},"
					// + "success: function(data, textStatus, xhr) {"
					// + " if(data!='false'){"
					// +
					// "	document.getElementById('thongbao').innerHTML =data;}"
					// + "},"
					// + "error: function(xhr, textStatus, errorThrown) {"
					// + " alert('Có lỗi khi gửi thông tin. !!');"
					// + " }" + "});" + "}, 10000); </script>");
					// }
					// }
					// } else
					// out.print("<h2>Bạn chưa chọn xe để kiểm tra.</h2>");
				} else
					out.print(sql);

				out.write("<hr style='margin-bottom:5px;'>\n");
				out.write("<span style='padding-left:10px'>&nbsp;</span>\n");
				out.write("</form>\n");
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

	// ham gui tin nhan tu 3G
	public static boolean sendSMS(String smsMessage, String contactPhone) {
		boolean bool = false;

		try {

			URL myURL = new URL(

			// "http://119.15.168.101:8768/WebsiteSendSMS/send8x68?sdt="
			// URLEncoder.encode(smsMessage, "UTF-8")
					"http://192.168.1.144:8080/WebsiteSendSMS/send8x68?sdt="
							+ contactPhone
							+ "&message="
							+ URLEncoder.encode("*123465,444,'*" + smsMessage
									+ "#", "UTF-8"));
			HttpURLConnection myURLConnection = (HttpURLConnection) myURL
					.openConnection();

			if (myURLConnection.getResponseMessage().equals("OK")) {
				Print.logInfo(myURLConnection.getResponseMessage());
				bool = true;
			} else {
				Print.logInfo("URL " + myURL);
				Print.logInfo("Not oke");
				bool = false;
			}

			myURLConnection.disconnect();
		} catch (Exception e) {
			// openConnection() failed
			// ...
			Print.logInfo("Io exception  " + e.getMessage());
			bool = false;
		}
		return bool;

	}

	// public static boolean sendSMS(String smsMessage, String simPhone) {
	// boolean bool = false;
	//
	// Connection con;
	//
	// try {
	// con = Connect();
	// } catch (Exception e) {
	// return bool;
	// }
	// CallableStatement cs = null;
	// try {
	// cs = (CallableStatement) con
	// .prepareCall("{call sp_ChenVaoOutbox(?,?,?,?,?,?,?)}");
	// cs.setString(1, simPhone);
	// cs.setString(2, "*123465,444,'*"+smsMessage+"#");
	// cs.setString(3, "8068");
	// cs.setInt(4, 0);
	// cs.setInt(5, 3);
	// cs.setString(6, "XE");
	// cs.setInt(7, 3);
	// cs.execute();
	//
	// bool = true;
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// bool = false;
	//
	// return bool;
	// }
	// try {
	// cs.close();
	// con.close();
	// } catch (SQLException e) {
	//
	// // Print.logInfo(e.getMessage());
	// }
	// return bool;
	// }

	// ------------------------------------------------------------------------

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
}