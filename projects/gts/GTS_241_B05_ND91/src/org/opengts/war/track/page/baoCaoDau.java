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
import java.util.TimeZone;
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

public class baoCaoDau extends WebPageAdaptor implements Constants {

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

	public baoCaoDau() {
		this.setBaseURI(RequestProperties.TRACK_BASE_URI());
		this.setPageName(PAGE_BAOCAODAU);
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
		I18N i18n = privLabel.getI18N(baoCaoDau.class);
		return super._getMenuDescription(reqState,
				i18n.getString("baoCaoDau.Menu", "Báo cáo dầu"));
	}

	public String getMenuHelp(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(baoCaoDau.class);
		return super._getMenuHelp(reqState,
				i18n.getString("baoCaoDau.MenuHelp", "Báo cáo dầu"));
	}

	// ------------------------------------------------------------------------

	public String getNavigationDescription(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(baoCaoDau.class);
		return super._getNavigationDescription(reqState,
				i18n.getString("baoCaoDau.NavDesc", "Báo cáo dầu"));
	}

	public String getNavigationTab(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(baoCaoDau.class);
		return i18n.getString("baoCaoDau.NavTab", "Báo cáo dầu");
	}

	// ------------------------------------------------------------------------

	public String ChonDoi(String accountid, String us, String idselect)
			throws IOException {
		String strre = "<select id ='deviceGroup' name = 'deviceGroup' class='textReadOnly' onchange='this.form.submit()' >";
		strre = strre + "<option value='all'>Tất cả</option>";
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

	public String ChonXe(String accountID, String groupID, String idselect) {

		String strre = "<select id ='device' name = 'device' class='textReadOnly'>";
		strre = strre + "<option value='all'>Tất cả</option>";
		try {
			DBCamera objcmr = new DBCamera();
			ResultSet rs = objcmr.selectXeByDoiXe1(accountID, groupID);
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
		strre += "<script type ='text/javascript' language ='javascript'> document.getElementById('device').value = '"
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

	private String ConvertFromEpochHour(long epoch, String timezone) {
		TimeZone tz = DateTime.getTimeZone(timezone, null);
		DateTime dt = new DateTime(epoch);
		String dtFmt = dt.format("yyyy-MM-dd", tz);
		return dtFmt;
	}

	public String Load_NhienLieu(String tuNgay, String denNgay, String doiXe,
			String xe, String acc, String tz) throws IOException {
		double xangLucDau = 0, xangLucCuoi = 0, xang = 0, xangDo;

		DBCamera db = new DBCamera();
		ResultSet rs = db.selectXeByDoiXe1(acc, doiXe);
		int num = 0, stt = 0;
		String str = "";
		str = str
				+ "<table id ='myTable' width='100%' class='adminSelectTable_sortable' cellspacing='1' ><thead><tr  align='center'><th class='adminTableHeaderCol_sort' width='50'>STT</th><th width='80px' class='adminTableHeaderCol_sort'>Xe</th><th  class='adminTableHeaderCol_sort'>Thời điểm</th><th width='100px' class='adminTableHeaderCol_sort'>Lượng nhiên liệu tiêu thụ(Lít)</th></tr></thead>";
		try {
			SimpleDateFormat ft1 = new SimpleDateFormat("yyyy/MM/dd");
			Date d = ft1.parse(tuNgay);
			long fr = d.getTime() / 1000;
			Date d1 = ft1.parse(denNgay);
			long to = d1.getTime() / 1000;
			if (xe.equals("all")) {
				while (rs.next()) {
					str = str
							+ "<tr style ='color:white; background-color:black;height:20px;' align='left' ><td></td><td colspan='3'>"
							+ rs.getString(1) + "</td></tr>";

					for (long ngay = fr; ngay <= to; ngay = ngay + (24 * 3600)) {

						String css = "";
						if (num % 2 == 0)
							css = "adminTableBodyRowOdd";
						else
							css = "adminTableBodyRowEven";

						stt++;
						num++;
						xangLucDau = db
								.xangLucDau(acc,
										ConvertFromEpochHour(ngay, tz),
										rs.getString(1));

						xangLucCuoi = db
								.xangLucCuoi(acc,
										ConvertFromEpochHour(ngay, tz),
										rs.getString(1));
						xangDo = db.xangDo(acc, ConvertFromEpochHour(ngay, tz),
								rs.getString(1));
						xang = xangLucDau + xangDo - xangLucCuoi;

						str = str + "<tr class=" + css + "><td>" + stt
								+ "</td><td></td><td>"
								+ ConvertFromEpochHour(ngay, tz) + "</td><td>"
								+ round(xang, 2) + "</td></tr>";

					}
					stt = 0;
				}
				rs.close();
			} else {
				str = str
						+ "<tr style ='color:white; background-color:black;height:20px;' align='left' ><td></td><td colspan='3'>"
						+ xe + "</td></tr>";
				for (long ngay = fr; ngay <= to; ngay = ngay + (24 * 3600)) {
					String css = "";
					if (num % 2 == 0)
						css = "adminTableBodyRowOdd";
					else
						css = "adminTableBodyRowEven";
					stt++;
					num++;
					xangLucDau = db.xangLucDau(acc,
							ConvertFromEpochHour(ngay, tz), xe);
					xangLucCuoi = db.xangLucCuoi(acc,
							ConvertFromEpochHour(ngay, tz), xe);
					xangDo = db.xangDo(acc, ConvertFromEpochHour(ngay, tz), xe);
					xang = xangLucDau + xangDo - xangLucCuoi;

					str = str + "<tr class=" + css + "><td>" + stt
							+ "</td><td></td><td>"
							+ ConvertFromEpochHour(ngay, tz) + "</td><td>"
							+ round(xang, 2) + "</td></tr>";
				}
				stt = 0;
			}
		} catch (Exception ex) {
		}
		str = str + "</table>";

		return str;
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
		final I18N i18n = privLabel.getI18N(baoCaoDau.class);
		final Locale locale = reqState.getLocale();
		final Account currAcct = reqState.getCurrentAccount();
		final User currUser = reqState.getCurrentUser();
		final String pageName = this.getPageName();
		String m = pageMsg;
		boolean error = false;

		// / HttpServletRequest request = reqState.getHttpServletRequest();
		HttpServletResponse response = reqState.getHttpServletResponse();
		HttpServletRequest request = reqState.getHttpServletRequest();
		String excel = AttributeTools.getRequestString(request, "btnExcel", "");

		// String doiXe =AttributeTools.getRequestString(request, "deviceGroup",
		// "");
		// String tuNgay =AttributeTools.getRequestString(request, "tuNgay",
		// "");
		// String denNgay =AttributeTools.getRequestString(request, "denNgay",
		// "");

		String tuNgay = "", contentall = "all", denNgay = "", xeall = "all";

		String xem = AttributeTools.getRequestString(request, "btnview", "");
		contentall = AttributeTools
				.getRequestString(request, "deviceGroup", "");
		xeall = AttributeTools.getRequestString(request, "device", "");

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
		} else
			contentall = "all";

		String dv = request.getParameter("device");
		if (dv != null) {
			if (dv != "") {
				xeall = dv;
			}
		} else
			xeall = "all";

		if (excel != "") {

			if (excel.equals("Export Excel")) {
				int i = 0;
				java.util.Calendar c = java.util.Calendar.getInstance();
				Date now = c.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				String d0 = sdf.format(now);
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setHeader("Content-Disposition",
						"attachment; filename=NhienLieu_" + d0 + ".xls");
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
				ct.setCellValue("TIÊU HAO NHIÊN LIỆU");
				title.setHeightInPoints(40);
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
				HSSFCellStyle csNgay = wb.createCellStyle();

				HSSFFont fngay = wb.createFont();
				HSSFRow rngay = sheet.createRow((short) 2);

				fngay.setFontHeightInPoints((short) 10);
				fngay.setBoldweight((short) fngay.BOLDWEIGHT_BOLD);

				csNgay.setFont(fngay);
				HSSFCell cTuNgay = rngay.createCell((short) 0);
				cTuNgay.setCellStyle(csNgay);
				cTuNgay.setCellValue("Từ ");
				rngay.createCell((short) 1).setCellValue(tuNgay);
				HSSFCell cDenNgay = rngay.createCell((short) 2);
				cDenNgay.setCellStyle(csNgay);
				cDenNgay.setCellValue("Đến ");
				rngay.createCell((short) 3).setCellValue(denNgay);

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
				h0.setCellValue(" STT ");
				HSSFCell h1 = rowhead.createCell((short) 1);
				h1.setCellStyle(cellStyle);
				h1.setCellValue("Xe");
				HSSFCell h2 = rowhead.createCell((short) 2);
				h2.setCellStyle(cellStyle);
				h2.setCellValue("Thời điểm");
				HSSFCell h3 = rowhead.createCell((short) 3);
				h3.setCellStyle(cellStyle);
				h3.setCellValue("Lượng nhiên liệu tiêu thụ(Lít)");

				rowhead.setHeightInPoints((short) 40);

				double xangLucDau = 0, xangLucCuoi = 0, xang = 0, xangDo;

				DBCamera db = new DBCamera();
				ResultSet rs = db.selectXeByDoiXe1(currAcct.getAccountID(),
						contentall);
				int num = 0, stt = 0;

				try {
					SimpleDateFormat ft1 = new SimpleDateFormat("yyyy/MM/dd");
					Date d = ft1.parse(tuNgay);
					long fr = d.getTime() / 1000;
					Date d1 = ft1.parse(denNgay);
					long to = d1.getTime() / 1000;
					if (xeall.equals("all")) {
						while (rs.next()) {

							i++;

							sheet.addMergedRegion(new CellRangeAddress(i, i, 1,
									3));
							HSSFCellStyle cellXe = wb.createCellStyle();
							cellXe.setBorderTop((short) 1);
							cellXe.setBorderRight((short) 1);
							cellXe.setBorderLeft((short) 1);
							cellXe.setBorderBottom((short) 1);
							cellXe.setFillForegroundColor(HSSFColor.BLACK.index);
							cellXe.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
							cellXe.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
							cellXe.setAlignment(HSSFCellStyle.ALIGN_LEFT);
							cellXe.setWrapText(true);
							HSSFFont fxe = wb.createFont();
							fxe.setFontHeightInPoints((short) 10);
							// make it red
							// f.setColor((short) HSSFColor.RED.index);
							// make it bold
							// arial is the default font
							fxe.setColor((short) HSSFColor.WHITE.index);
							fxe.setBoldweight((short) fxe.BOLDWEIGHT_BOLD);
							cellXe.setFont(fxe);

							HSSFRow rowxe = sheet.createRow((short) i);
							HSSFCell rxe = rowxe.createCell((short) 1);
							rxe.setCellStyle(cellXe);
							rxe.setCellValue(rs.getString(1));

							for (long ngay = fr; ngay <= to; ngay = ngay
									+ (24 * 3600)) {

								stt++;

								xangLucDau = db.xangLucDau(
										currAcct.getAccountID(),
										ConvertFromEpochHour(ngay,
												currAcct.getTimeZone()),
										rs.getString(1));

								xangLucCuoi = db.xangLucCuoi(
										currAcct.getAccountID(),
										ConvertFromEpochHour(ngay,
												currAcct.getTimeZone()),
										rs.getString(1));
								xangDo = db.xangDo(
										currAcct.getAccountID(),
										ConvertFromEpochHour(ngay,
												currAcct.getTimeZone()),
										rs.getString(1));
								xang = xangLucDau + xangDo - xangLucCuoi;

								// str=str+"<tr class="+css+"><td>"+stt+"</td><td></td><td>"+ConvertFromEpochHour(ngay,tz)+"</td><td>"+round(xang,2)+"</td></tr>";

								HSSFCellStyle csr = wb.createCellStyle();
								csr.setBorderTop((short) 1);
								csr.setBorderRight((short) 1);
								csr.setBorderLeft((short) 1);
								csr.setBorderBottom((short) 1);
								HSSFRow row = sheet.createRow((short) (i + 1));

								HSSFCell r0 = row.createCell((short) 0);
								r0.setCellStyle(csr);
								r0.setCellValue(stt);
								HSSFCell r1 = row.createCell((short) 1);
								r1.setCellStyle(csr);
								r1.setCellValue("");
								// row.createCell((short)
								// 2).setCellValue(rs.getString(4));
								HSSFCell r2 = row.createCell((short) 2);
								r2.setCellStyle(csr);
								r2.setCellValue(ConvertFromEpochHour(ngay,
										currAcct.getTimeZone()));
								HSSFCell r3 = row.createCell((short) 3);
								r3.setCellStyle(csr);
								r3.setCellValue(round(xang, 2));

								i++;

							}
							stt = 0;

						}
					} else {

						i++;

						sheet.addMergedRegion(new CellRangeAddress(i, i, 1, 3));
						HSSFCellStyle cellXe = wb.createCellStyle();
						cellXe.setBorderTop((short) 1);
						cellXe.setBorderRight((short) 1);
						cellXe.setBorderLeft((short) 1);
						cellXe.setBorderBottom((short) 1);
						cellXe.setFillForegroundColor(HSSFColor.BLACK.index);
						cellXe.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						cellXe.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						cellXe.setAlignment(HSSFCellStyle.ALIGN_LEFT);
						cellXe.setWrapText(true);
						HSSFFont fxe = wb.createFont();
						fxe.setFontHeightInPoints((short) 10);
						// make it red
						// f.setColor((short) HSSFColor.RED.index);
						// make it bold
						// arial is the default font
						fxe.setColor((short) HSSFColor.WHITE.index);
						fxe.setBoldweight((short) fxe.BOLDWEIGHT_BOLD);
						cellXe.setFont(fxe);

						HSSFRow rowxe = sheet.createRow((short) i);
						HSSFCell rxe = rowxe.createCell((short) 1);
						rxe.setCellStyle(cellXe);
						rxe.setCellValue(xeall);
 
						for (long ngay = fr; ngay <= to; ngay = ngay
								+ (24 * 3600)) {

							stt++;

							xangLucDau = db.xangLucDau(
									currAcct.getAccountID(),
									ConvertFromEpochHour(ngay,
											currAcct.getTimeZone()), xeall);

							xangLucCuoi = db.xangLucCuoi(
									currAcct.getAccountID(),
									ConvertFromEpochHour(ngay,
											currAcct.getTimeZone()), xeall);
							xangDo = db.xangDo(
									currAcct.getAccountID(),
									ConvertFromEpochHour(ngay,
											currAcct.getTimeZone()), xeall);
							xang = xangLucDau + xangDo - xangLucCuoi;
 
							HSSFCellStyle csr = wb.createCellStyle();
							csr.setBorderTop((short) 1);
							csr.setBorderRight((short) 1);
							csr.setBorderLeft((short) 1);
							csr.setBorderBottom((short) 1);
							HSSFRow row = sheet.createRow((short) (i + 1));

							HSSFCell r0 = row.createCell((short) 0);
							r0.setCellStyle(csr);
							r0.setCellValue(stt);
							HSSFCell r1 = row.createCell((short) 1);
							r1.setCellStyle(csr);
							r1.setCellValue("");
							// row.createCell((short)
							// 2).setCellValue(rs.getString(4));
							HSSFCell r2 = row.createCell((short) 2);
							r2.setCellStyle(csr);
							r2.setCellValue(ConvertFromEpochHour(ngay,
									currAcct.getTimeZone()));
							HSSFCell r3 = row.createCell((short) 3);
							r3.setCellStyle(csr);
							r3.setCellValue(round(xang, 2));

							i++;

						}
						stt = 0;
					}

					sheet.setColumnWidth((short) 1, (short) 4000);
					sheet.setColumnWidth((short) 2, (short) 6000);
					sheet.setColumnWidth((short) 3, (short) 8000);
					 
					OutputStream out = response.getOutputStream();
					wb.write(out);
					out.close();
					
				} catch (Exception ex) {
				}
				return;
			}
		}

		/* Style */
		HTMLOutput HTML_CSS = new HTMLOutput() {
			public void write(PrintWriter out) throws IOException {
				String cssDir = baoCaoDau.this.getCssDirectory();
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
				String tuNgay = "", contentall = "all", denNgay = "", xeall = "all";

				HttpServletRequest request = reqState.getHttpServletRequest();
				String xem = AttributeTools.getRequestString(request,
						"btnview", "");
				contentall = AttributeTools.getRequestString(request,
						"deviceGroup", "");
				xeall = AttributeTools.getRequestString(request, "device", "");

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
				} else
					contentall = "all";

				String dv = request.getParameter("device");
				if (dv != null) {
					if (dv != "") {
						xeall = dv;
					}
				} else
					xeall = "all";

				String user1 = "";
				if (currAcct.getCurrentUser() != null) {
					String[] user = currAcct.getCurrentUser().toString()
							.split("/");
					user1 = user[1];
				} else {
					user1 = "Admin";
				}
				// String sql = LoadImage(ngayxem, contentall, pindex,
				// pagesize,currAcct.getAccountID());
				// String LoadTram(String tuNgay,String denNgay,String bienXe
				// ,String Tram,String tenAccount,int currentPage,int Size )
				String sql = Load_NhienLieu(tuNgay, denNgay, contentall, xeall,
						currAcct.getAccountID(), currAcct.getTimeZone());

				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				String d = sdf.format(now);

				out.println("<span class='" + CommonServlet.CSS_MENU_TITLE
						+ "'>Báo cáo xăng dầu</span><br/>");
				out.println("<hr/>");
				out.println("<form name='AccountInfo' method='post' action='"
						+ chgURL + "' target='_self'>\n");

				out.println("<table class='"
						+ CommonServlet.CSS_ADMIN_VIEW_TABLE
						+ "' cellspacing='0' callpadding='0' border='0' width='100%' style='padding:15 0 15'>\n");

				out.print("<tr>\n");
				out.print("<td width='100' align='right'>T&#x1EEB; ng&#x00E0;y:</td>\n");
				out.print("<td width='100' align='left'>\n");
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

				out.print("<td width ='100px' align='right'>Ch&#x1ECD;n &#x0110;&#x1ED9;i Xe:</td><td width='100px'>\n");
				out.print(ChonDoi(currAcct.getAccountID(), user1, contentall));
				out.print("</td>");

				out.print("<td width ='100px' align='right'>Ch&#x1ECD;n  Xe:</td><td>\n");
				out.print(ChonXe(currAcct.getAccountID(), contentall, xeall));
				out.print("</td>");

				out.println("</tr>");
				// out.print ("<tr>");
				// out.print
				// ("<td colspan='2' style ='height:400px; border:solid 1px silver;' >"+sql
				// +currAcct.getAccountID()+ngayxem+"</td>");
				// out.println("</tr>");
				String user = "";

				if (currUser == null)
					user = "admin";
				else
					user = currUser.getUserID();

				out.println("</table>");
				out.print("<table cellspacing='0' style='width:100%;padding:10px 0px 10px'><tbody><tr class='viewhoz'><td width='100px'></td><td width='100px'> <input type='submit' class='button1' name='btnview' value='Xem' id='btnview'></td><td width='105px'></td><td align='left' width='100px'><input type='submit' id='btnExcel' value='Export Excel' name='btnExcel' class='button1'></td>");

				out.print("<td></td></tr></tbody></table>");

				if (xem.equals("Xem"))
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
