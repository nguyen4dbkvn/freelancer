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
//  2007/01/25  Martin D. Flynn
//     -Initial release
//  2007/04/01  Martin D. Flynn
//     -Added "Distance Units" field
//  2007/06/03  Martin D. Flynn
//     -Added I18N support
//  2007/06/13  Martin D. Flynn
//     -Added support for browsers with disabled cookies
//  2007/07/27  Martin D. Flynn
//     -Added 'getNavigationTab(...)'
//  2007/09/16  Martin D. Flynn
//     -Fixed GeocoderMode field to display the proper value from the table
//  2007/11/28  Martin D. Flynn
//     -Added 'Notify EMail' address field
//     -Invalid entries are now indicated on the page (previously they were
//      quietly ignored).
//  2008/10/16  Martin D. Flynn
//     -Update with new ACL usage
//  2008/12/01  Martin D. Flynn
//     -Added temperature units
//  2009/01/01  Martin D. Flynn
//     -Added 'Plural' field for Device/Group titles.
//  2010/04/11  Martin D. Flynn
//     -Added "Enable Border Crossing"
//     -Added "Pressure Units" selection
//  2011/03/08  Martin D. Flynn
//     -Moved GeocoderMode and isBorderCrossing to SysAdminAccounts admin.
//  2011/07/01  Martin D. Flynn
//     -Updated call to getDeviceTitles/getDeviceGroupTitles to not return the
//      standard default titles.
// ----------------------------------------------------------------------------
package org.opengts.war.track.page;

import org.apache.poi.hssf.util.HSSFColor;
import java.util.Date;
import java.util.TimeZone;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.io.*;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Array;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.opengts.util.*;
import org.opengts.dbtools.*;
import org.opengts.db.*;
import org.opengts.db.tables.*;

import org.opengts.war.report.ReportData;
import org.opengts.war.tools.*;
import org.opengts.war.track.*;

//import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class CopyOfBaoCaoBatTatCanCau extends WebPageAdaptor implements Constants {

	// ------------------------------------------------------------------------
	// Parameters
	public static final String COMMAND_INFO_UPDATE = "update";
	// button types
	public static final String PARM_BUTTON_CANCEL = "a_btncan";
	public static final String PARM_BUTTON_BACK = "a_btnbak";
	// public static final String PARM_FORMAT[] = ReportMenu.PARM_FORMAT;
	// parameters
	// thanhtq
	public static final String PARM_DATE_SL = "a_date";
	public static final String PARM_DEVICE_SL = "a_device";
	public static int record = 0;

	// ------------------------------------------------------------------------
	// WebPage interface

	public CopyOfBaoCaoBatTatCanCau() {
		this.setBaseURI(RequestProperties.TRACK_BASE_URI());
		this.setPageName(PAGE_BAOCAOBATTAT);
		this.setPageNavigation(new String[] { PAGE_LOGIN, PAGE_MENU_TOP });
		this.setLoginRequired(true);
	}

	// ------------------------------------------------------------------------

	public String getMenuName(RequestProperties reqState) {
		return MenuBar.MENU_ADMIN;
	}

	public String getMenuDescription(RequestProperties reqState,
			String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(CopyOfBaoCaoBatTatCanCau.class);
		return super._getMenuDescription(reqState,
				"Báo cáo bật tắt điều khiển cần cẩu");
	}

	public String getMenuHelp(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(CopyOfBaoCaoBatTatCanCau.class);
		return super._getMenuHelp(reqState,
				"Báo cáo bật tắt điều khiển cần cẩu");
	}

	// ------------------------------------------------------------------------

	public String getNavigationDescription(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(CopyOfBaoCaoBatTatCanCau.class);
		return super._getNavigationDescription(reqState,
				"Báo cáo bật tắt điều khiển cần cẩu");
	}

	public String getNavigationTab(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(CopyOfBaoCaoBatTatCanCau.class);
		return "Báo cáo bật tắt điều khiển cần cẩu";
	}

	// ------------------------------------------------------------------------

	public String doiGio(int tg) {
		String chuoi = "";
		int gio = tg / 3600;
		int phut = (tg % 3600) / 60;
		int giay = tg - gio * 3600 - phut * 60;
		String h = "", m = "", s = "";

		h = Integer.toString(gio);

		m = Integer.toString(phut);

		s = Integer.toString(giay);
		if (gio > 0)
			chuoi = h + " giờ, ";
		if (phut > 0)
			chuoi += m + " phút, ";
		if (giay > 0)
			chuoi += s + " giây";
		return chuoi;

	}

	public String DongMo(int tg) {

		String chuoi = "";
		if (tg == 1)
			chuoi = "Tắt";
		else
			chuoi = "Bật";

		return chuoi;

	}

	public String CreateCbbDevice(String accountid, String idselect,
			RequestProperties reqState, PrivateLabel privLabel)
			throws IOException {
		IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
		java.util.List<IDDescription> idList = reqState
				.createIDDescriptionList(false, sortBy);
		IDDescription list[] = idList.toArray(new IDDescription[idList.size()]);

		String strre = "<select id ='device' name = 'device' class='textReadOnly' style='width:100px;'>";
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

	private String getCurDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		Date date = new Date();
		return dateFormat.format(date);
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

	private String ConvertFromEpoch(long epoch, String timezone) {
		TimeZone tz = DateTime.getTimeZone(timezone, null);
		DateTime dt = new DateTime(epoch);
		String dtFmt = dt.format("dd/MM/yyyy HH:mm:ss", tz);
		return dtFmt;
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

	public String LoadVuotTocDo(String Account, String tuNgay, String denNgay,
			String Device, String timezone, RequestProperties reqState,
			PrivateLabel privLabel) throws IOException {
		int dem = 0;
		String strscr = "", chuoi = "", chuoi1 = "";
		record = 1;

		try {
			DBCamera objcmr = new DBCamera();
			// ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
			String deviceID = "";
			String css = "";
			// ResultSet rs1 = objcmr.GetDiviceByAccountID1(Account,us, Device);
			IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
			java.util.List<IDDescription> idList = reqState
					.createIDDescriptionList(false, sortBy);
			IDDescription list[] = idList.toArray(new IDDescription[idList
					.size()]);

			strscr = strscr
					+ "<table  class='adminSelectTable_sortable' cellspacing='1' id='myTable' ><thead><tr  align='center'><th width='50px' class='adminTableHeaderCol_sort' >STT</th><th width='200px' class='adminTableHeaderCol_sort'>Thời gian</th><th width='100px' class='adminTableHeaderCol_sort'>Trạng thái</th><th width='100px' class='adminTableHeaderCol_sort'>Tốc độ (km/h)</th><th class='adminTableHeaderCol_sort'>Địa điểm bật/tắt</th></tr></thead>";

			chuoi = "<tr style='height:30px;color:black'><td colspan='5' align='left'><span style='margin-left:25px;'><b>"
					+ Device + "</b></span></td></tr>";
			EventData ed[] = objcmr.GetReportdongmo(tuNgay, denNgay, Device,
					timezone, Account);
			int status = 0;
			for (int k = 0; k < ed.length; k++) {
				if (status != ed[k].getDieuHoa()) {
					if (record % 2 == 0)
						css = "adminTableBodyRowOdd";
					else
						css = "adminTableBodyRowEven";

					String day = ConvertFromEpoch(ed[k].getTimestamp(),
							timezone);
					chuoi1 = chuoi1 + "<tr class =" + css + "><td>" + record
							+ "</td><td>" + day + "</td><td>"
							+ DongMo(ed[k].getDieuHoa()) + "</td><td>"
							+ round(ed[k].getSpeedKPH(), 2) + "</td><td>"
							+ ed[k].getAddress() + "</td></tr>";

					record++;
					status=ed[k].getDieuHoa();
				}
			}
			strscr = strscr + chuoi + chuoi1;

			strscr = strscr + "</table>";
		} catch (Exception e) {

		}
		return strscr;
	}

	public String NCRToUnicode(String strInput) {
		String TCVN = "&#225;,&#224;,&#7841;,&#7843;,&#227;,&#226;,&#7845;,&#7847;,&#7853;,&#7849;,&#7851;,&#259;,&#7855;,&#7857;,&#7863;,&#7859;,&#7861;,&#é;,&#232;,&#7865;,&#7867;,&#7869;,&#234;,&#7871;,&#7873;,&#7879;,&#7875;,&#7877;,&#243;,&#242;,&#7885;,&#7887;,&#245;,&#244;,&#7889;,&#7891;,&#7897;,&#7893;,&#7895;,&#417;,&#7899;,&#7901;,&#7907;,&#7903;,&#7905;,&#250;,&#249;,&#7909;,&#7911;,&#361;,&#432;,&#7913;,&#7915;,&#7921;,&#7917;,&#7919;,&#237;,&#236;,&#7883;,&#7881;,&#297;,&#273;,&#253;,&#7923;,&#7925;,&#7927;,&#7929;,h";
		TCVN += "&#193;,&#192;,&#7840;,&#7842;,&#195;,&#194;,&#7844;,&#7846;,&#7852;,&#7848;,&#7850;,&#258;,&#7854;,&#7856;,&#7862;,&#7858;,&#7860;,&#200;,&#7864;,&#7866;,&#7868;,&#7870;,&#7872;,&#7878;,&#7874;,&#7876;,&#211;,&#210;,&#7884;,&#7886;,&#213;,&#212;,&#7888;,&#7890;,&#7896;,&#7892;,&#7894;,&#416;,&#7898;,&#7900;,&#7906;,&#7902;,&#7904;,&#218;,&#217;,&#7908;,&#7910;,&#360;,&#431;,&#7912;,&#7914;,&#7920;,&#7916;,&#7918;,&#272;,&#221;,&#7922;,&#7924;,&#7926;,&#7928;,h";
		String UNICODE = "á,à,ạ,ả,ã,â,ấ,ầ,ậ,ẩ,ẫ,ă,ắ,ằ,ặ,ẳ,ẵ,é,è,ẹ,ẻ,ẽ,ê,ế,ề,ệ,ể,ễ,ó,ò,ọ,ỏ,õ,ô,ố,ồ,ộ,ổ,ỗ,ơ,ớ,ờ,ợ,ở,ỡ,ú,ù,ụ,ủ,ũ,ư,ứ,ừ,ự,ử,ữ,í,ì,ị,ỉ,ĩ,đ,ý,ỳ,ỵ,ỷ,ỹ,h";
		UNICODE += "Á,À,Ạ,Ả,Ã,Â,Ấ,Ầ,Ậ,Ẩ,Ẫ,Ă,Ắ,Ằ,Ặ,Ẳ,Ẵ,È,Ẹ,Ẻ,Ẽ,Ế,Ề,Ệ,Ể,Ễ,Ó,Ò,Ọ,Ỏ,Õ,Ô,Ố,Ồ,Ộ,Ổ,Ỗ,Ơ,Ớ,Ờ,Ợ,Ở,Ỡ,Ú,Ù,Ụ,Ủ,Ũ,Ư,Ứ,Ừ,Ự,Ử,Ữ,Đ,Ý,Ỳ,Ỵ,Ỷ,Ỹ,h";
		String[] str = TCVN.split(",");
		String[] str1 = UNICODE.split(",");
		for (int i = 0; i < str.length; i++) {
			if (str[i] != "") {
				strInput = strInput.replace(str[i], str1[i]);
			}
		}
		return strInput;
	}

	public void writePage(final RequestProperties reqState,

	String pageMsg) throws IOException {
		final PrivateLabel privLabel = reqState.getPrivateLabel();
		final I18N i18n = privLabel.getI18N(CopyOfBaoCaoBatTatCanCau.class);
		final Locale locale = reqState.getLocale();
		final Account currAcct = reqState.getCurrentAccount();
		final User currUser = reqState.getCurrentUser();
		final String pageName = this.getPageName();
		String m = pageMsg;
		boolean error = false;

		HttpServletRequest request = reqState.getHttpServletRequest();
		HttpServletResponse response = reqState.getHttpServletResponse();
		String excel = AttributeTools.getRequestString(request, "btnExcel", "");
		String datefrom = AttributeTools.getRequestString(request, "datefrom",
				"");
		String dateto = AttributeTools.getRequestString(request, "dateto", "");
		String contentall = AttributeTools.getRequestString(request, "device",
				"");

		if (excel != "") {
			if (excel.equals("Export Excel")) {
				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String d = sdf.format(now);
				int dem = 0, num = 0;
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setHeader("Content-Disposition",
						"attachment; filename=baoCaoBatTatDieuKhienCanCau_" + d + ".xls");

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
				ct.setCellValue("BÁO CÁO BẬT TẮT ĐIỀU KHIỂN CẦN CẨU");

				// row1.setRowStyle(cellStyle);
				title.setHeightInPoints(40);
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
				HSSFCellStyle csNgay = wb.createCellStyle();

				HSSFFont fngay = wb.createFont();
				HSSFRow ngay = sheet.createRow((short) 2);

				fngay.setFontHeightInPoints((short) 10);
				fngay.setBoldweight((short) fngay.BOLDWEIGHT_BOLD);
				csNgay.setFont(fngay);
				HSSFCell cTuNgay = ngay.createCell((short) 1);
				cTuNgay.setCellStyle(csNgay);
				cTuNgay.setCellValue("Từ ngày");
				ngay.createCell((short) 2).setCellValue(
						datefrom.replace("00:0", " "));
				HSSFCell cdenNgay = ngay.createCell((short) 3);

				cdenNgay.setCellStyle(csNgay);
				cdenNgay.setCellValue("Đến ngày");
				ngay.createCell((short) 4).setCellValue(
						dateto.replace("23:59", " "));

				dem = dem + 3;

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
				HSSFFont f = wb.createFont();
				f.setFontHeightInPoints((short) 10);

				f.setBoldweight((short) f.BOLDWEIGHT_BOLD);
				cellStyle.setFont(f);
				HSSFRow rowhead = sheet.createRow((short) dem);
				// rowhead.createCell((short) 0).setCellValue("STT");
				HSSFCell h0 = rowhead.createCell((short) 0);
				h0.setCellStyle(cellStyle);
				h0.setCellValue("STT");
				/*
				 * HSSFCell h1 = rowhead.createCell((short) 1);
				 * h1.setCellStyle(cellStyle); h1.setCellValue("Lần bật/tắt");
				 * HSSFCell h2 = rowhead.createCell((short) 2);
				 * h2.setCellStyle(cellStyle); h2.setCellValue("Thời gian");
				 */

				HSSFCell h3 = rowhead.createCell((short) 3);
				h3.setCellStyle(cellStyle);
				h3.setCellValue("Trạng thái");
				HSSFCell h4 = rowhead.createCell((short) 4);
				h4.setCellStyle(cellStyle);
				h4.setCellValue("Tốc độ (km/h)");
				HSSFCell h5 = rowhead.createCell((short) 5);
				h5.setCellStyle(cellStyle);
				h5.setCellValue("Địa điểm bật/tắt");
				dem = dem + 1;
				rowhead.setHeightInPoints((short) 40);
				DBCamera objcmr = new DBCamera();

				try {
					// String deviceID="";
					IDDescription.SortBy sortBy = DeviceChooser
							.getSortBy(privLabel);
					java.util.List<IDDescription> idList = reqState
							.createIDDescriptionList(false, sortBy);
					IDDescription list[] = idList
							.toArray(new IDDescription[idList.size()]);

					EventData ed[] = objcmr.GetReportdongmo(datefrom, dateto,
							contentall, currAcct.getTimeZone(),
							currAcct.getAccountID());

					HSSFRow row1 = sheet.createRow((short) dem);
					row1.setHeightInPoints(25);
					sheet.addMergedRegion(new CellRangeAddress(dem, dem, 0, 5));
					HSSFCellStyle csXe = wb.createCellStyle();
					csXe.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
					csXe.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

					HSSFFont fxe = wb.createFont();
					fxe.setFontHeightInPoints((short) 10);

					fxe.setBoldweight((short) fxe.BOLDWEIGHT_BOLD);
					cellStyle.setFont(fxe);
					HSSFCell cellA1 = row1.createCell((short) 0);
					cellA1.setCellValue(contentall);
					cellA1.setCellStyle(csXe);

					for (int i = 0; i < ed.length; i++) {
						dem++;

						String day = ConvertFromEpoch(ed[i].getTimestamp(),
								currAcct.getTimeZone());
						HSSFCellStyle csr = wb.createCellStyle();
						csr.setBorderTop((short) 1);
						csr.setBorderRight((short) 1);
						csr.setBorderLeft((short) 1);
						csr.setBorderBottom((short) 1);

						HSSFRow row = sheet.createRow((short) (dem));
						HSSFCell r0 = row.createCell((short) 0);
						r0.setCellStyle(csr);
						r0.setCellValue(i);
						/*
						 * HSSFCell r1 = row.createCell((short) 1);
						 * r1.setCellStyle(csr); r1.setCellValue(i);
						 */
						// row.createCell((short)
						// 2).setCellValue(rs.getString(4));
						HSSFCell r2 = row.createCell((short) 2);
						r2.setCellStyle(csr);
						r2.setCellValue(day);

						HSSFCell r3 = row.createCell((short) 3);
						r3.setCellStyle(csr);
						r3.setCellValue(DongMo(ed[i].getDieuHoa()));
						HSSFCell r4 = row.createCell((short) 4);
						r4.setCellStyle(csr);
						r4.setCellValue(round(ed[i].getSpeedKPH(), 2));
						HSSFCell r5 = row.createCell((short) 5);
						r5.setCellStyle(csr);
						r5.setCellValue(NCRToUnicode(ed[i].getAddress()));

					}
					sheet.autoSizeColumn(0);
					// sheet.autoSizeColumn(1);
					sheet.autoSizeColumn(2);
					sheet.autoSizeColumn(3);
					sheet.autoSizeColumn(4);
					sheet.autoSizeColumn(5);
					// sheet.autoSizeColumn(0);
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
				String cssDir = CopyOfBaoCaoBatTatCanCau.this.getCssDirectory();
				WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
				WebPageAdaptor.writeCssLink(out, reqState, "scrollbar.css",
						cssDir);
			}
		};

		/* javascript */
		HTMLOutput HTML_JS = new HTMLOutput() {
			public void write(PrintWriter out) throws IOException {
				MenuBar.writeJavaScript(out, pageName, reqState);
				out.println("        <script type=\"text/javascript\" src=\"js/PCalendar.js\"></script>\n");
				out.println("        <script type=\"text/javascript\" src=\"js/jquery-1.4.2.min.js\"></script>\n");
				out.print("<script src='./js/jquery.tinyscrollbar.min.js' type=\"text/javascript\"></script>");
				out.print("<script type='text/javascript'>$(document).ready(function() {$('#scrollbar2').tinyscrollbar();}); </script>");
				// out.println(
				// " <script type='text/javascript' src='js/jquery.tablesorter.min.js'></script>");
				// out.println("<script type='text/javascript' src='js/sorttable.js'></script>");
				// out.println("<script type='text/javascript' > $(function(){$('#myTable').tablesorter(); }); </script>");
			}
		};

		/* Content */

		HTMLOutput HTML_CONTENT = new HTMLOutput(
				CommonServlet.CSS_CONTENT_FRAME, m) {
			public void write(PrintWriter out) throws IOException {
				// Print.logStackTrace("here");
				String menuURL = privLabel.getWebPageURL(reqState,
						PAGE_MENU_TOP);
				String chgURL = privLabel.getWebPageURL(reqState, pageName,
						COMMAND_INFO_UPDATE);
				String frameTitle = i18n.getString("Camera.PageTitle",
						"BaoCaoDongMo");
				HttpServletRequest request = reqState.getHttpServletRequest();
				String contentall = "";
				String datefrom = "homnay";
				String dateto = "homnay";
				String flag = "0";

				// view submit

				datefrom = AttributeTools.getRequestString(request, "datefrom",
						"");
				dateto = AttributeTools.getRequestString(request, "dateto", "");
				contentall = AttributeTools.getRequestString(request, "device",
						"");

				String sql = LoadVuotTocDo(currAcct.getAccountID(), datefrom,
						dateto, contentall, currAcct.getTimeZone(), reqState,
						privLabel);

				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String d = sdf.format(now);

				out.println("<span class='" + CommonServlet.CSS_MENU_TITLE
						+ "'>Báo cáo bật tắt điều khiển cần cẩu</span><br/>");
				out.println("<hr/>");
				out.println("<form name='baocaodongmo' method='post' action='"
						+ chgURL + "' target='_self'>\n");

				out.println("<table class='"
						+ CommonServlet.CSS_ADMIN_VIEW_TABLE
						+ "' cellspacing='0' callpadding='0' border='0'  style='padding:15px 0 15px'>\n");

				out.print("<tr style='height:40px;'>\n");
				out.print("<td class='"
						+ CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER
						+ "' width='100px' >T&#x1EEB; ng&#x00E0;y:</td>\n");
				out.print("<td class='"
						+ CommonServlet.CSS_ADMIN_VIEW_TABLE_DATA + "' >\n");
				out.print("<input id='datefrom' class='textReadOnly' name='datefrom' type='text' style='width:120px' onclick=\"displayCalendar(this,'dd/mm/yyyy hh:ii',this,true,'','datefrom')\" value='"
						+ d + " 00:00' />");
				if (datefrom != "") {
					out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('datefrom').value ='"
							+ datefrom + "'; </script>");
				}
				out.print("</td>");

				// out.print("</tr>");

				// out.print("<tr style='height:40px'>");
				out.print("<td class='"
						+ CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER
						+ "' width='100px'  >&#x0110;&#x1EBF;n ng&#x00E0;y:</td>\n");
				out.print("<td>");
				out.print("<input id='dateto' class='textReadOnly' name='dateto' type='text' style='width:120px' onclick=\"displayCalendar(this,dd/mm/yyyy hh:ii',this,true,'','dateto')\" value='"
						+ d + " 23:59' />");
				if (dateto != "") {
					out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('dateto').value ='"
							+ dateto + "'; </script>");
				}
				out.print("</td>");

				out.print("<td align='right' width='100px'><span style='margin-left: 10px;margin-right:5px;'>"
						+ i18n.getString("DeviceSelect", "ch&#x1ECD;n xe:")
						+ "</span></td><td>\n");
				out.print(CreateCbbDevice(currAcct.getAccountID(), contentall,
						reqState, privLabel));

				out.print("</td>");

				out.print("</tr>");

				out.println("</table>");

				out.print("<div class='viewhoz'>");
				out.println("<table >");
				out.print("<tr >");
				out.print("<td width='105px'></td>");
				out.print("<td> <input type ='submit' id ='btnview' value ='Xem' name ='btnview' class='button1'/>");
				out.print("</td><td style='width:155px'></td>");

				out.print("<td ><input type ='submit' id ='btnExcel' value ='Export Excel' name ='btnExcel' class='button1'/></td>");
				out.print("<td ></td>");
				out.println("</tr>");

				out.println("</table>");
				out.print("</div>");
				String btnXem = AttributeTools.getRequestString(request,
						"btnview", "");
				if (btnXem.equals("Xem"))
					out.print(sql);

				/* end of form */
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
