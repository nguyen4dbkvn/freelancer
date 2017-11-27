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
import org.opengts.util.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

//import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class baoCaoNgay extends WebPageAdaptor implements Constants {

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

	// ------------------------------------------------------------------------
	// WebPage interface

	public baoCaoNgay() {
		this.setBaseURI(RequestProperties.TRACK_BASE_URI());
		this.setPageName(PAGE_BAO_CAO_NGAY);
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
		I18N i18n = privLabel.getI18N(baoCaoNgay.class);
		return super._getMenuDescription(reqState,
				i18n.getString("baoCaoNgay.Menu", "Báo cáo ngày"));
	}

	public String getMenuHelp(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(baoCaoNgay.class);
		return super._getMenuHelp(reqState,
				i18n.getString("baoCaoNgay.MenuHelp", "Báo cáo ngày"));
	}

	// ------------------------------------------------------------------------

	public String getNavigationDescription(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(baoCaoNgay.class);
		return super._getNavigationDescription(reqState,
				i18n.getString("baoCaoNgay.NavDesc", "Báo cáo ngày"));
	}

	public String getNavigationTab(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(baoCaoNgay.class);
		return i18n.getString("baoCaoNgay.NavTab", "Báo cáo ngày");
	}

	// ------------------------------------------------------------------------

	public boolean isOkToDisplay(RequestProperties reqState) {
		Account account = (reqState != null) ? reqState.getCurrentAccount()
				: null;
		if (account == null) {
			return false; // no account?
		} else {
			int dem = 0;
			DBCamera objcmr = new DBCamera();
			dem = objcmr.phanQuyen(account.getAccountID(),
					"baoCaoNgay");
			 
			if (dem > 0)
				return false;
			else
				return true;

		}
	}

	public String doiGio(int tg) {
		String chuoi = "";
		int gio = tg / 3600;
		int phut = (tg % 3600) / 60;
		int giay = tg - gio * 3600 - phut * 60;
		String h = "", m = "", s = "";
		if (gio < 9)
			h = "0" + Integer.toString(gio);
		else
			h = Integer.toString(gio);
		if (phut < 9)
			m = "0" + Integer.toString(phut);
		else
			m = Integer.toString(phut);
		if (giay < 9)
			s = "0" + Integer.toString(giay);
		else
			s = Integer.toString(giay);
		return chuoi = chuoi + h + ":" + m + ":" + s;

	}

	public String LoadbaoCaoNgay(String ngay, String accountID,
			RequestProperties reqState, PrivateLabel privLabel, String timezone) {
		int dem = 0;
		String chuoi = "";
		chuoi = chuoi
				+ "<div  id='dataTable'> <table cellspacing='1'width='100%' class='adminSelectTable_sortable'><thead><tr align='center'  ><th class='adminTableHeaderCol_sort'>Xe<br /><br />Car<br />plate<br /><br />(1)</th><th class='adminTableHeaderCol_sort'>Quãng đường vận hành<br />"
				+ "<br />Distance(km)<br /><br />(2)</th><th class='adminTableHeaderCol_sort'>Số lần quá tốc độ<br /><br />Number of over speed<br />"
				+ "<br />(3)</th><th class='adminTableHeaderCol_sort'>Vận tốc trung bình(km/h)<br /><br />Average speed(km/h)<br /><br />(4)</th><th class='adminTableHeaderCol_sort'>Vận tốc tối đa(km/h)<br /><br />"
				+ "Max speed(km/h)<br /><br />(5)</th><th class='adminTableHeaderCol_sort'>Số lần  mở cửa<br /><br />Number of open door<br /><br />(6)</th>"
				+ "<th class='adminTableHeaderCol_sort'>Số lần dừng đỗ<br /> <br />Number of idle time<br /><br />(7)</th><th class='adminTableHeaderCol_sort'>Tổng thời gian dừng<br /><br />Total idle time<br /> <br />"
				+ "(8)</th><th class='adminTableHeaderCol_sort'>Tổng thời gian lái xe<br /><br />Driving time<br /><br /><br />(9)</th>"
				+ "</tr></thead>";
		try {
			DBCamera objcmr = new DBCamera();
			IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
			java.util.List<IDDescription> idList = reqState
					.createIDDescriptionList(false, sortBy);
			IDDescription list[] = idList.toArray(new IDDescription[idList
					.size()]);

			int slvt = 0;
			//int dungDo = 0;
			//int tgd = 0;
			int moCua = 0;
			double quangduong = 0;
			for (int i = 0; i < list.length; i++) {
				objcmr.LoadHoatDongXeTheoNgay(accountID, list[i].getID(), ngay);
				slvt = objcmr.StaticCountDungDoEventRunStop(accountID, list[i].getID(), ngay, timezone);
				moCua = objcmr.StaticCountMoCua(accountID, list[i].getID(), ngay, timezone);
				quangduong = objcmr.StaticOdometerKM(accountID, list[i].getID(), ngay, timezone);
				ArrayList<ReportDate> listreport = objcmr.StaticDriving(accountID, list[i].getID(), ngay, timezone);
				//slvt = objcmr.StaticCountOverSpeed(accountID, list[i].getID(), ngay, timezone);
				//dungDo = objcmr.StaticCountDungDo(accountID, list[i].getID(), ngay, timezone);
				//tgd = objcmr.StaticSumDungDo(accountID, list[i].getID(), ngay, timezone);
 
				//ArrayList<DrivingTime1> listreport = objcmr.StaticReportDrivingTime1(accountID, list[i].getID(), ngay, timezone);
				String css = "";
				if (dem % 2 == 0)
					css = "adminTableBodyRowOdd";
				else
					css = "adminTableBodyRowEven";
				dem++;
				for (int j = 0; j < listreport.size(); j++) {
					chuoi = chuoi + "<tr class='" + css + "'><td>" + list[i].getID() + "</td><td>" + quangduong
							+ "</td><td>" + slvt + "</td><td>" + listreport.get(j).getAvgSpeed() + "</td><td>"
							+ listreport.get(j).getMaxSpeed() + "</td><td>" + moCua + "</td><td>"
							+ listreport.get(j).getCount_dungdo() + "</td><td>"
							+ doiGio(listreport.get(j).getTimedung()) + "</td><td>"
							+ doiGio(listreport.get(j).getTimedriving()) + "</td></tr>";
					/*chuoi = chuoi + "<tr class='" + css + "'><td>"
							+ list[i].getID() + "</td><td>"
							+ listreport.get(j).getquangduong() + "</td><td>"
							+ slvt + "</td><td>" + listreport.get(j).getvttb()
							+ "</td><td>" + listreport.get(j).getvttoida()
							+ "</td><td>" + dungDo //<td>" + moCua + "</td>
							+ "</td><td>" + doiGio(tgd) + "</td><td>"
							+ doiGio(listreport.get(j).gettgLai())
							+ "</td></tr>";*/
				}			 
			}
		} catch (Exception ex) {

			chuoi = "lỗi";
		}
		return chuoi + "</table></div>";

	}

	public void writePage(final RequestProperties reqState, String pageMsg)
			throws IOException {
		final PrivateLabel privLabel = reqState.getPrivateLabel();
		final I18N i18n = privLabel.getI18N(baoCaoNgay.class);
		final Locale locale = reqState.getLocale();
		final Account currAcct = reqState.getCurrentAccount();
		final User currUser = reqState.getCurrentUser();
		final String pageName = this.getPageName();
		String m = pageMsg;
		boolean error = false;
		HttpServletRequest request = reqState.getHttpServletRequest();
		HttpServletResponse response = reqState.getHttpServletResponse();
		String excel = AttributeTools.getRequestString(request, "btnExcel", "");
		String ngay = AttributeTools.getRequestString(request, "ngay", "");
		int i = 0;

		if (excel != "") {
			if (excel.equals("Export Excel")) {
				java.util.Calendar c = java.util.Calendar.getInstance();
				Date now = c.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String d = sdf.format(now);
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setHeader("Content-Disposition",
						"attachment; filename=baoCaoNgay_" + d + ".xls");
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
				ct.setCellValue("BÁO CÁO NGÀY");
				title.setHeightInPoints(40);
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 8));
				HSSFCellStyle csNgay = wb.createCellStyle();

				HSSFFont fngay = wb.createFont();
				HSSFRow rngay = sheet.createRow((short) 2);

				fngay.setFontHeightInPoints((short) 10);
				fngay.setBoldweight((short) fngay.BOLDWEIGHT_BOLD);
				csNgay.setFont(fngay);
				HSSFCell cTuNgay = rngay.createCell((short) 1);
				cTuNgay.setCellStyle(csNgay);
				cTuNgay.setCellValue("Ngày");
				rngay.createCell((short) 2).setCellValue(ngay);

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
				h0.setCellValue("Xe \n (Car plate)");
				HSSFCell h1 = rowhead.createCell((short) 1);
				h1.setCellStyle(cellStyle);
				h1.setCellValue("Quãng đường vận hành(km)\n(Distance(km))");
				HSSFCell h2 = rowhead.createCell((short) 2);
				h2.setCellStyle(cellStyle);
				h2.setCellValue("Số lần quá tốc độ\n(Number of over speed)");
				HSSFCell h3 = rowhead.createCell((short) 3);
				h3.setCellStyle(cellStyle);
				h3.setCellValue("Vận tốc trung bình(km/h)\n(Average speed(Km/h))");
				HSSFCell h4 = rowhead.createCell((short) 4);
				h4.setCellStyle(cellStyle);
				h4.setCellValue("Vận tốc tối đa\n(Max speed(Km/h))");
				HSSFCell h5 = rowhead.createCell((short) 5);
				h5.setCellStyle(cellStyle);
				h5.setCellValue("Số lần mở cửa\n(Number of open door)");
				HSSFCell h6 = rowhead.createCell((short) 6);
				h6.setCellStyle(cellStyle);
				h6.setCellValue("Số lần dừng đỗ\n(Number of idle time)");
				HSSFCell h8 = rowhead.createCell((short) 8);
				h8.setCellStyle(cellStyle);
				h8.setCellValue("Tổng thời gian lái xe\n (Driving time)");
				HSSFCell h7 = rowhead.createCell((short) 7);
				h7.setCellStyle(cellStyle);
				h7.setCellValue("Tổng thời gian dừng(h)\n (Total idle time(h)");

				rowhead.setHeightInPoints((short) 40);

				String accountID = currAcct.getAccountID();
				IDDescription.SortBy sortBy = DeviceChooser
						.getSortBy(privLabel);
				java.util.List<IDDescription> idList = reqState
						.createIDDescriptionList(false, sortBy);
				IDDescription list[] = idList.toArray(new IDDescription[idList
						.size()]);
				try {
					DBCamera objcmr = new DBCamera();
					// String
					// slvt="",dungDo="",tgd="",quangDuong="",vttb="",vttd="",tgl="",deviceID="",moCua="";
					int slvt = 0;
					int dungDo = 0;
					int tgd = 0;
					int moCua = 0;
					double quangDuong = 0;
					double vttb = 0;
					double vttd = 0;
					int tgl = 0;
					for (int k = 0; k < list.length; k++) {
						/*slvt = objcmr.StaticCountOverSpeed(accountID,
								list[k].getID(), ngay, currAcct.getTimeZone());
						dungDo = objcmr.StaticCountDungDo(accountID,
								list[k].getID(), ngay, currAcct.getTimeZone());
						tgd = objcmr.StaticSumDungDo(accountID,
								list[k].getID(), ngay, currAcct.getTimeZone());
						moCua = objcmr.StaticCountMoCua(accountID,
								list[k].getID(), ngay, currAcct.getTimeZone());
						ArrayList<DrivingTime1> listreport = objcmr
								.StaticReportDrivingTime1(accountID,
										list[k].getID(), ngay,
										currAcct.getTimeZone());*/
						slvt = objcmr.StaticCountDungDoEventRunStop(accountID, list[k].getID(), ngay, currAcct.getTimeZone());
						moCua = objcmr.StaticCountMoCua(accountID, list[k].getID(), ngay, currAcct.getTimeZone());
						quangDuong = objcmr.StaticOdometerKM(accountID, list[i].getID(), ngay, currAcct.getTimeZone());
						ArrayList<ReportDate> listreport = objcmr.StaticDriving(accountID, list[i].getID(), ngay, currAcct.getTimeZone());
						for (int j = 0; j < listreport.size(); j++) {
							/*quangDuong = listreport.get(j).getquangduong();
							vttb = listreport.get(j).getvttb();
							vttd = listreport.get(j).getvttoida();
							tgl = listreport.get(j).gettgLai();*/
							dungDo = listreport.get(j).getCount_dungdo();
							tgd = listreport.get(j).getTimedung();
							vttb = listreport.get(j).getAvgSpeed();
							vttd = listreport.get(j).getMaxSpeed();
							tgl = listreport.get(j).getTimedriving();
						}
						HSSFCellStyle csr = wb.createCellStyle();
						csr.setBorderTop((short) 1);
						csr.setBorderRight((short) 1);
						csr.setBorderLeft((short) 1);
						csr.setBorderBottom((short) 1);

						HSSFRow row = sheet.createRow((short) (i + 1));
						HSSFCell r0 = row.createCell((short) 0);
						r0.setCellStyle(csr);
						r0.setCellValue(list[k].getID());
						HSSFCell r1 = row.createCell((short) 1);
						r1.setCellStyle(csr);
						r1.setCellValue(quangDuong);
						// row.createCell((short)
						// 2).setCellValue(rs.getString(4));
						HSSFCell r2 = row.createCell((short) 2);
						r2.setCellStyle(csr);
						r2.setCellValue(slvt);
						HSSFCell r3 = row.createCell((short) 3);
						r3.setCellStyle(csr);
						r3.setCellValue(vttb);
						HSSFCell r4 = row.createCell((short) 4);
						r4.setCellStyle(csr);
						r4.setCellValue(vttd);
						HSSFCell r5 = row.createCell((short) 5);
						r5.setCellStyle(csr);
						r5.setCellValue(moCua);
						HSSFCell r6 = row.createCell((short) 6);
						r6.setCellStyle(csr);
						r6.setCellValue(dungDo);
						HSSFCell r8 = row.createCell((short) 8);
						r8.setCellStyle(csr);
						r8.setCellValue(doiGio(tgl));
						HSSFCell r7 = row.createCell((short) 7);
						r7.setCellStyle(csr);
						r7.setCellValue(doiGio(tgd));

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
					sheet.autoSizeColumn(8);

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
				String cssDir = baoCaoNgay.this.getCssDirectory();
				WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
			}
		};

		/* javascript */
		HTMLOutput HTML_JS = new HTMLOutput() {
			public void write(PrintWriter out) throws IOException {
				MenuBar.writeJavaScript(out, pageName, reqState);
				out.println("<script type=\"text/javascript\" src=\"js/PCalendar.js\"></script>\n");
				out.println("<script type=\"text/javascript\" src=\"js/jquery-1.4.2.min.js\"></script>\n");
				out.println(" <script type='text/javascript' src='js/jquery.tablesorter.min.js'></script>");
				out.println("<script type='text/javascript' src='js/sorttable.js'></script>");
				out.println("<script type='text/javascript' > $(function(){$('#myTable').tablesorter(); }); </script>");
				out.println("        <script type=\"text/javascript\" src=\"js/pdf.js\"></script>\n");
				out.println("<script> $(document).ready(function() { $('#running').remove(); "
						+ " $('#exportpdf').click(function (){ PrintContent('BÁO CÁO NGÀY')}); "
						+ " })</script>");
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
				String frameTitle = i18n.getString("baocaoNgay.PageTitle",
						"Báo cáo ngày");

				// frame content
				// view submit
				String ngayxem = "", btnXem;
				HttpServletRequest request = reqState.getHttpServletRequest();
				HttpServletResponse rp = reqState.getHttpServletResponse();
				ngayxem = AttributeTools.getRequestString(request, "ngay", "");
				btnXem = AttributeTools
						.getRequestString(request, "btnview", "");
				java.util.Calendar c = java.util.Calendar.getInstance();
				c.add(java.util.Calendar.DAY_OF_YEAR, -1);
				Date now = c.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String d = sdf.format(now);

				out.println("<span class='" + CommonServlet.CSS_MENU_TITLE
						+ "'>Báo cáo ngày</span><br/>");
				out.println("<hr/>");
				out.println("<form name='AccountInfo' method='post' action='"
						+ chgURL + "' target='_self'>\n");

				out.println("<table class='"
						+ CommonServlet.CSS_ADMIN_VIEW_TABLE
						+ "' cellspacing='0' callpadding='0' border='0'  style='padding:15 0 15'>\n");

				out.print("<tr>\n");
				out.print("<td class='"
						+ CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER
						+ "' style='width:90px' align='right'>"
						+ i18n.getString("Camera.DateSelect",
								"Ch&#x1ECD;n ng&#x00E0;y:") + "</td>\n");
				out.print("<td style='width:100px'>\n");
				out.print("<input id='Text1' name='ngay' type='text' class='textReadOnly' style='width:100px' onclick=\"displayCalendar(this,'dd/mm/yyyy',this,false,'','Text1')\" value='"
						+ d + "' /></td><td></td>");

				out.println("</tr>");
				if (ngayxem != "") {
					out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('Text1').value ='"
							+ ngayxem + "'; </script>");
				}
				out.println("</table>");

				out.print("<table cellspacing='0' style='width:100%;padding:10px 0px 10px'><tbody><tr class='viewhoz'><td width='100px'></td><td width='100px'> <input type='submit' class='button1' name='btnview' value='Xem' id='btnview'></td><td width='105px'></td><td align='left'><input type='submit' id='btnExcel' value='Export Excel' name='btnExcel' class='button1'></td><td align='left'><input type='button' id='exportpdf' value='Export PDF' name='exportpdf' class='button1' ></td></tr></tbody></table>");
				// String xem= AttributeTools.getRequestString(request,
				// "btnview", "");
				if (btnXem.equals("Xem")) {
					out.println("<div id='running' style='padding:7px;font-weight:bold'>Đang tổng hợp dữ liệu...<br/> Vui lòng chờ trong giây lát.</div>");
					out.print(LoadbaoCaoNgay(ngayxem, currAcct.getAccountID(),
							reqState, privLabel, currAcct.getTimeZone()));
					// out.print(currAcct.getAccountID());
				}
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
