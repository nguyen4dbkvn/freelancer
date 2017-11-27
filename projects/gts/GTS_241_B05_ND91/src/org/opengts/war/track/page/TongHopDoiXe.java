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

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Date;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.opengts.util.*;
import org.opengts.dbtools.*;
import org.opengts.db.*;
import org.opengts.db.tables.*;

import org.opengts.war.tools.*;
import org.opengts.war.track.*;

public class TongHopDoiXe extends WebPageAdaptor implements Constants {

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
	public static int i = 0;

	// ------------------------------------------------------------------------
	// WebPage interface

	public TongHopDoiXe() {
		this.setBaseURI(RequestProperties.TRACK_BASE_URI());
		this.setPageName(PAGE_TONGHOPDOIXE);
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
		I18N i18n = privLabel.getI18N(TongHopDoiXe.class);
		return super._getMenuDescription(reqState, i18n.getString(
				"tongHopDoiXe.Menu",
				"Tr&#x1EA1;ng th&#x00E1;i cu&#x1ED1;i c&#x00F9;ng"));
	}

	public String getMenuHelp(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(TongHopDoiXe.class);
		return super._getMenuHelp(reqState, i18n.getString(
				"tongHopDoiXe.MenuHelp",
				"Tr&#x1EA1;ng th&#x00E1;i cu&#x1ED1;i c&#x00F9;ng"));
	}

	// ------------------------------------------------------------------------

	public String getNavigationDescription(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(TongHopDoiXe.class);
		return super._getNavigationDescription(reqState, i18n.getString(
				"tongHopDoiXe.NavDesc",
				"Tr&#x1EA1;ng th&#x00E1;i cu&#x1ED1;i c&#x00F9;ng"));
	}

	public String getNavigationTab(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(TongHopDoiXe.class);
		return i18n.getString("tongHopDoiXe.NavTab",
				"Tr&#x1EA1;ng th&#x00E1;i cu&#x1ED1;i c&#x00F9;ng");
	}

	// ------------------------------------------------------------------------

	public String ChonDoi(String accountid, String us, String idselect)
			throws IOException {
		String strre = "<select id ='deviceGroup' name = 'deviceGroup' class='textReadOnly'>";
		strre = strre + "<option value='all'>T&#x1EA5;t c&#x1EA3;</option>";
		try {
			DBCamera objcmr = new DBCamera();
			ResultSet rs = objcmr.selectDoiXe(accountid, us);
			while (rs.next()) {
				if (idselect == rs.getString(1))
					strre += "<option value ='" + rs.getString(1)
							+ "' selected =\"selected\">" + rs.getString(1)
							+ "</option>\n";
				else
					strre += "<option value ='" + rs.getString(1) + "'>"
							+ rs.getString(1) + "</option>\n";
			}
			rs.close();
		} catch (Exception e) {

		}
		strre += "</select>\n";
		strre += "<script type ='text/javascript' language ='javascript'> document.getElementById('deviceGroup').value = '"
				+ idselect + "';</script>\n";
		return strre;
	}

	public static double round(double number, int digit) {
		if (digit > 0) {
			int temp = 1, i;
			for (i = 0; i < digit; i++)
				temp = temp * 10;
			number = number * temp;
			number = Math.round(number);
			number = number / temp;
			return number;
		} else
			return 0.0;
	}

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

	public String Load_TongHopDoiXe(String tuNgay, String denNgay,
			String doiXe, String tenAccount, String us, PrivateLabel privLabel)
			throws IOException {
		//
		String strscr = "";
		try {

			int num = 0;
			DBCamera objcmr = new DBCamera();

			int bNeedManaged = objcmr.qryCheckIfAccountNeedManaging(tenAccount);

			// ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);

			ResultSet rs = objcmr.selectXeTheoDoi(tenAccount, us, doiXe);
			// strscr =strscr+
			// "<div id='scrollbar2'><div class='scrollbar'><div class='track'><div class='thumb'><div class='end'></div></div></div></div><div class='viewport'><div class='overview'>";
			strscr = strscr
					+ "<table id ='myTable' width='100%' class='adminSelectTable_sortable' cellspacing='1' ><thead><tr  align='center'><th class='adminTableHeaderCol_sort' width='50'>STT</th><th width='80px' class='adminTableHeaderCol_sort'>Xe</th><th width='80px' class='adminTableHeaderCol_sort'>Loại xe</th><th  class='adminTableHeaderCol_sort'>Th\u1EDDi \u0111i\u1EC3m</th><th width='100px' class='adminTableHeaderCol_sort'>Trạng thái</th><th width='100px' class='adminTableHeaderCol_sort'>T&#x1ED1;c &#x0111;&#x1ED9; (Km/h)</th><th width='100px' class='adminTableHeaderCol_sort'>Cửa</th><th width='150px' class='adminTableHeaderCol_sort'>T&#x1ECD;a &#x0111;&#x1ED9; </th><th  class='adminTableHeaderCol_sort'>&#x0110;&#x1ECB;a ch&#x1EC9;</th><th  class='adminTableHeaderCol_sort'>Nhiên liệu<br/>(Lít)</th><th  class='adminTableHeaderCol_sort'>"+(tenAccount.equalsIgnoreCase("ctyconghoan")==false?"Điều hòa":"Tín hiệu cẩu")+"</th></tr></thead>";
			String cua = "Đóng";
			String dieuhoa = "-";
			String nhienlieu = "";
			while (rs.next()) {
				String css = "";
				if (num % 2 == 0)
					css = "adminTableBodyRowOdd";
				else
					css = "adminTableBodyRowEven";

				num++;
				String devID = rs.getString(1);

				if (bNeedManaged > 0) {
					objcmr.insertStopEvent(tenAccount, devID);// ThanhNgC add to
																// insert Stop
																// Event
				}

				ResultSet rs1 = objcmr.TongHopDoiXe(tenAccount, tuNgay,
						denNgay, rs.getString(1));
				ResultSet rs2 = objcmr.Device_select(devID, tenAccount);
				int status = objcmr.StaticSelectstatus(tenAccount,
						rs.getString(1));
				String stop = "", loaiXe = "";
				int xang = 0;
				int dh = 0;
				loaiXe = objcmr.tenLoaiXe(tenAccount, rs.getString(1));
				while (rs2.next()) {
					xang = rs2.getInt("xang");
					dh = rs2.getInt("DieuHoa");

				}
				rs2.close();
				if (dh != 1)
					dieuhoa = "--";
				while (rs1.next()) {
					if (rs1.getInt(8) == 1)
						cua = "Đóng";
					else if (rs1.getInt(8) == 0)
						cua = "Mở";
					
					if (rs1.getInt("DieuHoa") == 1)
						dieuhoa = "Tắt";
					else if (rs1.getInt("DieuHoa") == 2)
						dieuhoa = "Bật";
					if (xang != 1)
						nhienlieu = "-";
					else
						nhienlieu = Double.toString(round(
								rs1.getDouble("altitude"), 2));
					i++;
					String tt = "";
					if (Integer.parseInt(rs1.getString(9)) == 61718)
						tt = "Đang hoạt động";
					else {
						tt = StatusCode.getDescription(tenAccount,
								Integer.parseInt(rs1.getString(9)), privLabel,
								null);
						if (rs1.getInt(4) == 0) {
							if (status == 1) {
								long h = System.currentTimeMillis() / 1000
										- rs1.getLong("timestamp")
										+ rs1.getInt("tt");
								tt = tt + " " + doiGio(h);
							} else
								tt = tt + " " + doiGio(rs1.getInt("tt"));
						}
					}
					strscr = strscr + "<tr class =" + css + "><td>" + i
							+ "</td><td>" + rs1.getString(1) + "</td><td>"
							+ loaiXe + "</td><td>" + rs1.getString(2) + " "
							+ rs1.getString(3) + "</td><td>" + tt + "</td><td>"
							+ rs1.getString(4) + "</td><td>" + cua
							+ "</td><td>" + rs1.getString(5) + "/"
							+ rs1.getString(6) + "</td><td>" + rs1.getString(7)
							+ "</td><td>" + nhienlieu + "</td><td>" + dieuhoa
							+ "</td></tr>";
				}
				rs1.close();
			}
			i = 0;
			rs.close();
			strscr = strscr + "</table>";// </div></div></div>";

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
		final I18N i18n = privLabel.getI18N(TongHopDoiXe.class);
		final Locale locale = reqState.getLocale();
		final Account currAcct = reqState.getCurrentAccount();
		final User currUser = reqState.getCurrentUser();
		final String pageName = this.getPageName();
		String m = pageMsg;
		boolean error = false;

		HttpServletRequest request = reqState.getHttpServletRequest();
		HttpServletResponse response = reqState.getHttpServletResponse();
		String excel = AttributeTools.getRequestString(request, "btnExcel", "");

		String doiXe = AttributeTools.getRequestString(request, "deviceGroup",
				"");
		String tuNgay = AttributeTools.getRequestString(request, "tuNgay", "");
		String denNgay = AttributeTools
				.getRequestString(request, "denNgay", "");

		if (excel != "") {

			if (excel.equals("Export Excel")) {
				int i = 0;
				java.util.Calendar c = java.util.Calendar.getInstance();
				Date now = c.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				String d = sdf.format(now);
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setHeader("Content-Disposition",
						"attachment; filename=trangThaiCuoiCung_" + d + ".xls");
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
				ct.setCellValue("TRẠNG THÁI CUỐI CÙNG CỦA XE");
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
				h1.setCellValue("Thời điểm");
				HSSFCell h2 = rowhead.createCell((short) 2);
				h2.setCellStyle(cellStyle);
				h2.setCellValue("Trạng thái");
				HSSFCell h3 = rowhead.createCell((short) 3);
				h3.setCellStyle(cellStyle);
				h3.setCellValue("Tốc độ");
				HSSFCell h4 = rowhead.createCell((short) 4);
				h4.setCellStyle(cellStyle);
				h4.setCellValue("Cửa");
				HSSFCell h5 = rowhead.createCell((short) 5);
				h5.setCellStyle(cellStyle);
				h5.setCellValue("Tọa độ");
				HSSFCell h6 = rowhead.createCell((short) 6);
				h6.setCellStyle(cellStyle);
				h6.setCellValue("Địa chỉ");

				rowhead.setHeightInPoints((short) 40);
				DBCamera objcmr = new DBCamera();
				// ResultSet rs = objcmr.GetCamera(ngay, device, page,
				// pagesize);
				String user1 = "";
				if (currAcct.getCurrentUser() != null) {
					String[] user = currAcct.getCurrentUser().toString()
							.split("/");
					user1 = user[1];
				} else {
					user1 = "Admin";
				}
				ResultSet rs = objcmr.selectXeTheoDoi(currAcct.getAccountID(),
						user1, doiXe);

				try {
					while (rs.next()) {
						ResultSet rs1 = objcmr.TongHopDoiXe(
								currAcct.getAccountID(), tuNgay, denNgay,
								rs.getString(1));
						String cua = "Đóng";
						while (rs1.next()) {

							if (rs1.getInt(8) == 1)
								cua = "Đóng";
							else
								cua = "Mở";
							HSSFCellStyle csr = wb.createCellStyle();
							csr.setBorderTop((short) 1);
							csr.setBorderRight((short) 1);
							csr.setBorderLeft((short) 1);
							csr.setBorderBottom((short) 1);
							HSSFRow row = sheet.createRow((short) (i + 1));

							HSSFCell r0 = row.createCell((short) 0);
							r0.setCellStyle(csr);
							r0.setCellValue(rs1.getString(1));
							HSSFCell r1 = row.createCell((short) 1);
							r1.setCellStyle(csr);
							r1.setCellValue(rs1.getString(2) + " "
									+ rs1.getString(3));
							// row.createCell((short)
							// 2).setCellValue(rs.getString(4));
							HSSFCell r2 = row.createCell((short) 2);
							r2.setCellStyle(csr);
							r2.setCellValue(GetUTF8FromNCRDecimalString(StatusCode
									.getDescription(currAcct.getAccountID(),
											Integer.parseInt(rs1.getString(9)),
											privLabel, null)));
							HSSFCell r3 = row.createCell((short) 3);
							r3.setCellStyle(csr);
							r3.setCellValue(Double.parseDouble(rs1.getString(4)));
							HSSFCell r4 = row.createCell((short) 4);
							r4.setCellStyle(csr);
							r4.setCellValue(cua);
							HSSFCell r5 = row.createCell((short) 5);
							r5.setCellStyle(csr);
							r5.setCellValue(rs1.getString(5) + "/"
									+ rs1.getString(6));
							HSSFCell r6 = row.createCell((short) 6);
							r6.setCellStyle(csr);
							r6.setCellValue(GetUTF8FromNCRDecimalString(rs1
									.getString(7)));

							i++;
						}
						rs1.close();
					}
					rs.close();
					sheet.autoSizeColumn(0);
					sheet.autoSizeColumn(1);
					sheet.autoSizeColumn(2);
					sheet.autoSizeColumn(3);
					sheet.autoSizeColumn(4);
					sheet.autoSizeColumn(5);
					sheet.autoSizeColumn(6);
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
				String cssDir = TongHopDoiXe.this.getCssDirectory();
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
				out.print("<script src='./js/jquery.tinyscrollbar.min.js' type=\"text/javascript\"></script>");
				out.print("<script type='text/javascript'>$(document).ready(function() {$('#scrollbar2').tinyscrollbar();}); </script>");
				out.println("<script type='text/javascript' src='js/sorttable.js'></script>");
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
				String frameTitle = i18n.getString("TongHopDoiXe.PageTitle",
						"Tr&#x1EA1;ng th&#x00E1;i cu&#x1ED1;i c&#x00F9;ng");

				// frame content
				// view submit
				String tuNgay = "", contentall = "", denNgay = "";

				HttpServletRequest request = reqState.getHttpServletRequest();
				String xem = AttributeTools.getRequestString(request,
						"btnview", "");
				contentall = AttributeTools.getRequestString(request,
						"deviceGroup", "");

				String sdate = request.getParameter("tuNgay");
				if (sdate != null) {
					if (sdate != "") {
						tuNgay = sdate;
					}
				}

				String sdateTo = request.getParameter("denNgay");
				if (sdateTo != null) {
					if (sdateTo != "") {
						denNgay = sdateTo;
					}
				}

				String sdevice = request.getParameter("deviceGroup");
				if (sdevice != null) {
					if (sdevice != "") {
						contentall = sdevice;
					}
				}

				String user1 = "";
				if (currAcct.getCurrentUser() != null) {
					String[] user = currAcct.getCurrentUser().toString()
							.split("/");
					user1 = user[1];
				} else {
					user1 = "Admin";
				}

				String sql = Load_TongHopDoiXe(tuNgay, denNgay, contentall,
						currAcct.getAccountID(), user1, privLabel);

				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				String d = sdf.format(now);

				out.println("<span class='"
						+ CommonServlet.CSS_MENU_TITLE
						+ "'>T&#x1ED5;ng h&#x1EE3;p &#x0111;&#x1ED9;i xe</span><br/>");
				out.println("<hr/>");
				out.println("<form name='AccountInfo' method='post' action='"
						+ chgURL + "' target='_self'>\n");

				out.println("<table class='"
						+ CommonServlet.CSS_ADMIN_VIEW_TABLE
						+ "' cellspacing='0' callpadding='0' border='0' width='100%' style='padding:15 0 15'>\n");

				out.print("<tr>\n");
				out.print("<td width='100' align='right' style='display:none'>T&#x1EEB; ng&#x00E0;y:</td>\n");
				out.print("<td width='100' align='left' style='display:none'>\n");
				out.print("<input id='Text1' name='tuNgay' type='text' style='width:100px' class='textReadOnly' onclick=\"displayCalendar(this,'yyyy/mm/dd',this,false,'','Text1')\" value='"
						+ d + "' /></td>");
				if (tuNgay != "") {
					out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('Text1').value ='"
							+ tuNgay + "'; </script>");
				}
				out.print("<td width='105px' align='right'>&#x0110;&#x1EBF;n ng&#x00E0;y:</td>\n");
				out.print("<td width='100px' align='left'>\n");
				out.print("<input id='Text2' name='denNgay' type='text' class='textReadOnly' style='width:100px' onclick=\"displayCalendar(this,'yyyy/mm/dd',this,false,'','Text2')\" value='"
						+ d + "' /></td>");
				if (denNgay != "") {
					out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('Text2').value ='"
							+ denNgay + "'; </script>");
				}

				out.print("<td width ='100px' align='right'>Ch&#x1ECD;n &#x0110;&#x1ED9;i Xe:</td><td>\n");
				out.print(ChonDoi(currAcct.getAccountID(), user1, contentall));
				out.print("</td>");
				out.println("</tr>");

				String user = "";

				if (currUser == null)
					user = "admin";
				else
					user = currUser.getUserID();

				out.println("</table>");
				out.print("<table cellspacing='0' style='width:100%;padding:10px 0px 10px'><tbody><tr class='viewhoz'><td width='100px'></td><td width='100px'> <input type='submit' class='button1' name='btnview' value='Xem' id='btnview'></td><td width='105px'></td><td align='left' width='100px'><input type='submit' id='btnExcel' value='Export Excel' name='btnExcel' class='button1'></td>");
				if (i > 0)
					out.print("<td width='100px'></td><td><input type='button' class='button1' name='btnmap' value='B&#x1EA3;n &#x0111;&#x1ED3;' id='btnmap' onclick=\"javascript:openResizableWindow('Track?account="
							+ currAcct.getAccountID()
							+ "&user="
							+ user
							+ "&page_cmd_arg&r_limit&date_tz=GMT%2b07%3a00&r_report=EventSummary&r_text&r_option=all&page=report.show&r_emailAddr&r_limType&date_fr='+document.getElementById('Text1').value+' 00:00&page_cmd=rptsel&r_menu=menu.rpt.tongHopDoiXe&group='+document.getElementById('deviceGroup').value+'&date_to='+document.getElementById('Text2').value+' 23:59&fmt=map','ReportMap',900,700)\"></td></tr></tbody></table>");
				else
					out.print("<td></td></tr></tbody></table>");

				out.print(sql);

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
