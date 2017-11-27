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

import org.apache.poi.hssf.util.HSSFColor;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

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

public class MayXucLiveReport extends WebPageAdaptor implements Constants {

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

	public MayXucLiveReport() {
		this.setBaseURI(RequestProperties.TRACK_BASE_URI());
		this.setPageName(PAGE_REPORT_MAYXUC_LIVE);
		this.setPageNavigation(new String[] { PAGE_LOGIN, PAGE_MENU_TOP });
		this.setLoginRequired(true);
		// this.setReportType(ReportFactory.REPORT_TYPE_DEVICE_DETAIL);
	}

	// ------------------------------------------------------------------------

	public String getMenuName(RequestProperties reqState) {
		return MenuBar.PAGE_REPORT_MAYXUC_LIVE;
	}

	public String getMenuDescription(RequestProperties reqState,
			String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(MayXucLiveReport.class);
		String devTitles[] = reqState.getDeviceTitles();
		return super._getMenuDescription(reqState, i18n.getString(
				"ReportMenuWorktime.menuDesc", "{0} Detail Reports",
				"B\u00E1o c\u00E1o th\u1EDDi gian máy xúc"));
	}

	public String getMenuHelp(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(MayXucLiveReport.class);
		String devTitles[] = reqState.getDeviceTitles();
		return super._getMenuHelp(reqState, i18n.getString(
				"ReportMenuWorktime.menuHelp",
				"Display various {0} detail reports",
				"B\u00E1o c\u00E1o th\u1EDDi gian máy xúc"));
	}

	// ------------------------------------------------------------------------

	public String getNavigationDescription(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(MayXucLiveReport.class);
		String devTitles[] = reqState.getDeviceTitles();
		return super._getNavigationDescription(reqState, i18n.getString(
				"ReportMenuWorktime.navDesc", "{0}",
				"B\u00E1o c\u00E1o th\u1EDDi gian máy xúc"));
	}

	public String getNavigationTab(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(MayXucLiveReport.class);
		String devTitles[] = reqState.getDeviceTitles();
		return super._getNavigationTab(reqState, i18n.getString(
				"ReportMenuWorktime.navTab", "{0}",
				"B\u00E1o c\u00E1o th\u1EDDi gian máy xúc"));
	}
 

	public String CreateCbbDevice(String accountid, String idselect,
			RequestProperties reqState, PrivateLabel privLabel)
			throws IOException {
		IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
		java.util.List<IDDescription> idList = reqState
				.createIDDescriptionList(false, sortBy);
		IDDescription list[] = idList.toArray(new IDDescription[idList.size()]);

		String strre = "<select id ='device' name = 'device' class='textReadOnly'>";
		for (int d = 0; d < list.length; d++) {
 
			if (idselect.equalsIgnoreCase(list[d].getID()))
				strre += "<option value ='" + list[d].getID()
						+ "' selected =\"selected\">" + list[d].getID()
						+ "</option>\n";
			else
				strre += "<option value ='" + list[d].getID() + "'>"
						+ list[d].getID() + "</option>\n";
		}

		strre += "</select>\n";
		strre += "<script type ='text/javascript' language ='javascript'> document.getElementById('device ').value = '"
				+ idselect + "';</script>\n";
		return strre;
	}

	 
	public String doiGio(long tg) {
		String chuoi = "";
		long gio = tg / 3600;
		long phut = (tg % 3600) / 60;
		long giay = tg - gio * 3600 - phut * 60;
		String h = "", m = "", s = "";

		h = Long.toString(gio);

		m = Long.toString(phut);

		s = Long.toString(giay);
		if (gio > 0)
			chuoi = h + " giờ, ";
		if (phut > 0)
			chuoi += m + " phút, ";
		if (giay > 0)
			chuoi += s + " giây";
		return chuoi;

	}

	 
	public static double round(double value, int decimalPlace) {
		double power_of_ten = 1;
		// floating point arithmetic can be very tricky.
		// that's why I introduce a "fudge factor"
		double fudge_factor = 0.05;
		while (decimalPlace-- > 0) {
			power_of_ten *= 10.0d;
			fudge_factor /= 10.0d;
		}
		return Math.round((value + fudge_factor) * power_of_ten) / power_of_ten;
	}

	private String ConvertFromEpoch(long epoch, String timezone) {
		TimeZone tz = DateTime.getTimeZone(timezone, null);
		DateTime dt = new DateTime(epoch);
		String dtFmt = dt.format("dd/MM/yyyy HH:mm:ss", tz);
		return dtFmt;
	}

	private String formatElapsedTime(long elapsedSec) {
		StringBuffer sb = new StringBuffer();
		int h = (int) (elapsedSec / (60L * 60L)); // Hours
		int m = (int) ((elapsedSec / 60L) % 60); // Minutes
		int s = (int) (elapsedSec % 60); // Seconds
		sb.append(StringTools.format(h, "0"));
		sb.append(":");
		sb.append(StringTools.format(m, "00"));
		sb.append(":");
		sb.append(StringTools.format(s, "00"));
		return sb.toString();
	}

	public String LoadJobs(String accountID, String deviceID, String tuNgay,
			String denNgay, String timezone, I18N i18n) throws IOException {
		String strscr = "";
		try {
			DBCamera db = new DBCamera();
			ArrayList<LiveEvent> list = db.GetLiveReportEvent(accountID,
					deviceID, tuNgay, denNgay, timezone);
			strscr = strscr
					+ "	<table  id='myTable' width='100%' class='adminSelectTable_sortable' cellspacing='1' >"
					+ "			<thead><tr align='center'>"
					+ "				<th class = 'adminTableHeaderCol_sort' width='30px'>"
					+ i18n.getString("MayXucLiveReport.LiveNo", "STT")
					+ "</th>"
					+ "				<th class = 'adminTableHeaderCol_sort' width='80px'>"
					+ i18n.getString("MayXucLiveReport.DriverID", "Xe")
					+ "</th>"
					+ "				<th class = 'adminTableHeaderCol_sort' width='80px'>"
					+ i18n.getString("MayXucLiveReport.AltitudeStart",
							"Nhiên liệu <br/> khi bắt đầu (Lít)")
					+ "</th>"
					+ "				<th class = 'adminTableHeaderCol_sort' width='120px'>"
					+ i18n.getString("MayXucLiveReport.StartTime",
							"Thời gian bắt đầu")
					+ "</th>"
					+ "				<th class = 'adminTableHeaderCol_sort' width='120px'>"
					+ i18n.getString("MayXucLiveReport.EndTime",
							"Thời gian kết thúc")
					+ "</th>"
					+ "				<th class = 'adminTableHeaderCol_sort' width='80px'>"
					+ i18n.getString("MayXucLiveReport.AltitudeEnd",
							"Nhiên liệu <br/>khi kết thúc (Lít)")
					+ "</th>"
					+ "				<th class = 'adminTableHeaderCol_sort' width='80px'>"
					+ i18n.getString("MayXucLiveReport.AltitudeEnd",
							"Nhiên liệu <br/>tiêu thụ (Lít)")
					+ "</th>"
					+ "				<th class = 'adminTableHeaderCol_sort' width='100px'>"
					+ i18n.getString("MayXucLiveReport.LiveTime",
							"Thời gian làm việc")
					+ "</th>"
					+ "				<th class = 'adminTableHeaderCol_sort' width=''>"
					+ i18n.getString("MayXucLiveReport.StartAddress",
							"Địa chỉ bắt đầu")
					+ "</th>"
					+ "				<th class = 'adminTableHeaderCol_sort' >"
					+ i18n.getString("MayXucLiveReport.EndAddress",
							"Địa chỉ kết thúc") + "</th>" + "				</tr></thead>"
					+ "		<tbody>";
			boolean odd = true;
			int tong = 0;
			double TongNhienLieuTieuThu = 0;
			for (int i = 0; i < list.size(); i++) {
				String startTime = ConvertFromEpoch(list.get(i).getStartTime(),
						timezone);
				String endTime = ConvertFromEpoch(list.get(i).getEndTime(),
						timezone);
				double a = list.get(i).getAltitudeStart();
				double b = list.get(i).getAltitudeEnd();
				String AltitudeStart = String.valueOf(round(list.get(i)
						.getAltitudeStart(), 2));
				String AltitudeEnd = String.valueOf(round(list.get(i)
						.getAltitudeEnd(), 2));
				String SumAltilude = String.valueOf(round(list.get(i).getTieuThu(), 2));
				TongNhienLieuTieuThu +=list.get(i).getTieuThu();
				String strOdd = (odd) ? " class='adminTableBodyRowOdd' "
						: "class='adminTableBodyRowEven' ";
				odd = !odd;
				strscr = strscr + "<tr id='Row" + i + "' " + strOdd
						+ " align='center' onclick=\"ClickRow(" + i + ",'"
						+ (i + 1) + "')\"><td>" + (i + 1) + "</td><td>"
						+ list.get(i).getDriver() + "</td><td>" + AltitudeStart
						+ "</td><td>" + startTime + "</td><td>" + endTime
						+ "</td><td>" + AltitudeEnd + "</td><td>" + SumAltilude
						+ "</td><td>"
						+ formatElapsedTime(list.get(i).getLiveTime())
						+ "</td><td><span style='margin:0 2px'>" + list.get(i).getStartAdress()
						+ "</span></td><td><span style='margin:0 2px'>" + list.get(i).getEndAdress() + "</span></td>"
						+ "</tr>";
				tong += list.get(i).getLiveTime();
			}
			String strOdd = (odd) ? " class='adminTableBodyRowOdd' "
					: "class='adminTableBodyRowEven' ";
			if (list.size() > 0)
				strscr = strscr
						+ "<tr class='adminTableBodyRowOdd'><td style='font-weight:bold'>Tổng</td><td></td><td></td><td></td><td></td><td></td><td style='font-weight:bold;'>"
						+ String.valueOf(round(TongNhienLieuTieuThu, 2))
						+ "</td><td style='font-weight:bold;'>"
						+ formatElapsedTime(tong)
						+ "</td><td></td><td></td></tr>";
			
			strscr = strscr + "</tbody></table>";

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
		String excel = AttributeTools.getRequestString(request, "btnExcel", "");

		String xe = AttributeTools.getRequestString(request, "device", "");
		String tuNgay = AttributeTools.getRequestString(request, "tuNgay", "");
		String denNgay = AttributeTools
				.getRequestString(request, "denNgay", "");
		// String Tram =AttributeTools.getRequestString(request, "idTram", "");
		int i = 0;
		if (excel != "") {

			if (excel.equals("Export Excel")) {
				java.util.Calendar c = java.util.Calendar.getInstance();
				Date now = c.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				String d = sdf.format(now);
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setHeader("Content-Disposition",
						"attachment; filename=baoCaoThoiGianMayXuc_" + d
								+ ".xls");
				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFSheet sheet = wb.createSheet("Dispatch");
				HSSFRow title = sheet.createRow((short) 1);

				HSSFCellStyle cst = wb.createCellStyle();
				HSSFFont ft = wb.createFont();
				cst.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cst.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				ft.setFontHeightInPoints((short) 18);
				// make it red
				// f.setColor((short) HSSFColor.RED.index);
				// make it bold
				// arial is the default font
				ft.setBoldweight((short) ft.BOLDWEIGHT_BOLD);
				cst.setFont(ft);
				HSSFCell ct = title.createCell((short) 0);

				ct.setCellStyle(cst);
				ct.setCellValue("BÁO CÁO THỜI GIAN MÁY XÚC");
				title.setHeightInPoints(40);
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
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
				f.setFontHeightInPoints((short) 10);
				// make it red
				// f.setColor((short) HSSFColor.RED.index);
				// make it bold
				// arial is the default font
				f.setBoldweight((short) f.BOLDWEIGHT_BOLD);
				cellStyle.setFont(f);

				HSSFRow rowhead = sheet.createRow((short) i);
				HSSFCell h0 = rowhead.createCell((short) 0);
				h0.setCellStyle(cellStyle);
				h0.setCellValue("Xe ");
				HSSFCell h1 = rowhead.createCell((short) 1);
				h1.setCellStyle(cellStyle);
				h1.setCellValue("Thời gian bắt đầu");
				HSSFCell h2 = rowhead.createCell((short) 2);
				h2.setCellStyle(cellStyle);
				h2.setCellValue("Thời gian kết thúc");
				HSSFCell h3 = rowhead.createCell((short) 3);
				h3.setCellStyle(cellStyle);
				h3.setCellValue("Thời gian làm việc");
				HSSFCell h4 = rowhead.createCell((short) 4);
				h4.setCellStyle(cellStyle);
				h4.setCellValue("Địa chỉ bắt đầu");
				HSSFCell h5 = rowhead.createCell((short) 5);
				h5.setCellStyle(cellStyle);
				h5.setCellValue("Địa chỉ kết thúc");
				HSSFCell h6 = rowhead.createCell((short) 6);
				h6.setCellStyle(cellStyle);
				h6.setCellValue("Nhiên liệu bắt đầu(Lít)");
				HSSFCell h7 = rowhead.createCell((short) 7);
				h7.setCellStyle(cellStyle);
				h7.setCellValue("Nhiên liệu kết thúc(Lít)");
				HSSFCell h8 = rowhead.createCell((short) 8);
				h8.setCellStyle(cellStyle);
				h8.setCellValue("Nhiên liệu tiêu thụ(Lít)");
				try {
					// DBCamera objcmr = new DBCamera();

					rowhead.setHeightInPoints((short) 40); 

					DBCamera db = new DBCamera();
					ArrayList<LiveEvent> list = db.GetLiveReportEvent(
							currAcct.getAccountID(), xe, tuNgay, denNgay,
							currAcct.getTimeZone());
					for (int j = 0; j < list.size(); j++) {
						String startTime = ConvertFromEpoch(list.get(j)
								.getStartTime(), currAcct.getTimeZone());
						String endTime = ConvertFromEpoch(list.get(j)
								.getEndTime(), currAcct.getTimeZone());
						String AltitudeStart = String.valueOf(round(list.get(i)
								.getAltitudeStart(), 2));
						String AltitudeEnd = String.valueOf(round(list.get(i)
								.getAltitudeEnd(), 2));
						String SumAltilude = String.valueOf(round(list.get(i).getTieuThu(), 2));
						HSSFCellStyle csr = wb.createCellStyle();
						csr.setBorderTop((short) 1);
						csr.setBorderRight((short) 1);
						csr.setBorderLeft((short) 1);
						csr.setBorderBottom((short) 1);
						HSSFRow row = sheet.createRow((short) (i + 1));
						HSSFCell r0 = row.createCell((short) 0);
						r0.setCellStyle(csr);
						r0.setCellValue(list.get(j).getDriver());
						HSSFCell r1 = row.createCell((short) 1);
						r1.setCellStyle(csr);
						r1.setCellValue(startTime);
						// row.createCell((short)
						// 2).setCellValue(rs.getString(4));
						HSSFCell r2 = row.createCell((short) 2);
						r2.setCellStyle(csr);
						r2.setCellValue(endTime);
						HSSFCell r3 = row.createCell((short) 3);
						r3.setCellStyle(csr);
						r3.setCellValue(formatElapsedTime(list.get(j)
								.getLiveTime()));
						HSSFCell r4 = row.createCell((short) 4);
						r4.setCellStyle(csr);
						r4.setCellValue(GetUTF8FromNCRDecimalString(list.get(j)
								.getStartAdress()));
						HSSFCell r5 = row.createCell((short) 5);
						r5.setCellStyle(csr);
						r5.setCellValue(GetUTF8FromNCRDecimalString(list.get(j)
								.getEndAdress()));
						HSSFCell r6 = row.createCell((short) 6);
						r6.setCellStyle(csr);
						r6.setCellValue(AltitudeStart);
						HSSFCell r7 = row.createCell((short) 7);
						r7.setCellStyle(csr);
						r7.setCellValue(AltitudeEnd);
						HSSFCell r8 = row.createCell((short) 8);
						r8.setCellStyle(csr);
						r8.setCellValue(SumAltilude);

						i++;
						// out.print("\t"+rs1.getString(1)+"\t"+rs1.getString(2)+"\t"+rs1.getString(3)+"\t"+rs1.getString(4)+"\t"+rs1.getString(5)+"/"+rs1.getString(6)+"\t"+GetUTF8FromNCRDecimalString(rs1.getString(7))+"\n");
					}
					sheet.autoSizeColumn(0);
					sheet.autoSizeColumn(1);
					sheet.autoSizeColumn(2);
					sheet.autoSizeColumn(3);
					sheet.autoSizeColumn(4);
					sheet.autoSizeColumn(5);
					sheet.autoSizeColumn(6);
					sheet.autoSizeColumn(7);
					sheet.autoSizeColumn(8);
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
				String cssDir = MayXucLiveReport.this.getCssDirectory();
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
				String frameTitle = i18n.getString("baocaoTram.PageTitle",
						"Tr&#x1EA1;m");
 
				HttpServletRequest request = reqState.getHttpServletRequest();
				String tuNgay = AttributeTools.getRequestString(request,
						"tuNgay", "");
				String contentall = "";
				String denNgay = AttributeTools.getRequestString(request,
						"denNgay", "");
				int pindex = 1;
				int pindexl = 0;
				int pagesize = 1000;
				int tongtrang = 0;
				String flag = "0";
				int pagestatic = 1;
				String urlmap = "";
 
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
				if (sdevice != null) {
					if (sdevice != "") {
						contentall = sdevice;
					}
				}
				String sflag = request.getParameter("flag");
				if (sflag != null) {
					if (sflag != "") {
						flag = sflag;
					}
				}
 
				String sql = LoadJobs(currAcct.getAccountID(), contentall,
						tuNgay, denNgay, currAcct.getTimeZone(), i18n);
			 
				java.util.Calendar c = java.util.Calendar.getInstance();
				c.add(java.util.Calendar.DAY_OF_YEAR, -1);
				Date now = c.getTime();
			
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String d = sdf.format(now);

				out.println("<span class='" + CommonServlet.CSS_MENU_TITLE
						+ "'>B\u00E1o c\u00E1o thời gian máy xúc</span><br/>");
				out.println("<hr/>");
				out.println("<form name='AccountInfo' method='post' action='"
						+ chgURL + "' target='_self'>\n");

				out.println("<table class='"
						+ CommonServlet.CSS_ADMIN_VIEW_TABLE
						+ "' cellspacing='0' callpadding='0' border='0' width='100%' style='padding:15 0 15'>\n");

				out.print("<tr>\n");
				out.print("<td width='100px' align='right'>T&#x1EEB; ng&#x00E0;y:</td>\n");
				out.print("<td width='100px' align='left'>\n");
				out.print("<input id='Text1' name='tuNgay' type='text' class='textReadOnly' style='width:120px' onclick=\"displayCalendar(this,'dd/mm/yyyy hh:ii',this,true,'','Text1')\" value=\""
						+ d + " 00:00\" /></td>");
				if (tuNgay != "") {
					out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('Text1').value ='"
							+ tuNgay + "'; </script>");
				}
				out.print("<td width='85px' align='right'>&#x0110;&#x1EBF;n ng&#x00E0;y:</td>\n");
				out.print("<td width='100px' align='left'>\n");
				out.print("<input id='Text2' name='denNgay' type='text' class='textReadOnly' style='width:120px' onclick=\"displayCalendar(this,'dd/mm/yyyy hh:ii',this,true,'','Text2')\" value=\""
						+ d + " 23:59\" /></td>");
				if (denNgay != "") {
					out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('Text2').value ='"
							+ denNgay + "'; </script>");
				}

				out.print("<td width='100px' align='right'>Ch&#x1ECD;n Xe:</td><td align='left'>\n");
				out.print(CreateCbbDevice(currAcct.getAccountID(), contentall,
						reqState, privLabel));
				out.print("</td>");
 
				out.println("</tr>");
 

				out.println("</table>");
				out.print("<table cellspacing='0' style='width:100%;padding:10px 0px 10px'><tbody><tr class='viewhoz'><td width='100px'></td><td width='100px'> <input type='submit' class='button1' name='btnview' value='Xem' id='btnview'></td><td width='105px'></td><td align='left' width='220px'><input type='submit' id='btnExcel' value='Export Excel' name='btnExcel' class='button1'></td>");

				out.print("<td align='left'></td>");
				out.print("</tr></tbody></table>");
				 
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

	// ------------------------------------------------------------------------

}
