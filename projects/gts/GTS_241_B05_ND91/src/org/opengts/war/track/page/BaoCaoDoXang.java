//----------------------------------------------------------------------------
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
//import org.opengts.war.report.ReportData;
import org.opengts.war.tools.*;
import org.opengts.war.track.*;

//import sun.org.mozilla.javascript.internal.ast.ForInLoop;

//import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class BaoCaoDoXang extends WebPageAdaptor implements Constants {

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

	public BaoCaoDoXang() {
		this.setBaseURI(RequestProperties.TRACK_BASE_URI());
		this.setPageName(PAGE_MENU_RPT_DOXANG);
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
		I18N i18n = privLabel.getI18N(BaoCaoDoXang.class);
		return super._getMenuDescription(reqState,
				i18n.getString("BaoCaoDoXang.Menu", "Báo cáo sử dụng nhiên liệu"));
	}

	public String getMenuHelp(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaoCaoDoXang.class);
		return super._getMenuHelp(reqState,
				i18n.getString("BaoCaoDoXang.MenuHelp", "Báo cáo sử dụng nhiên liệu"));
	}

	// ------------------------------------------------------------------------

	public String getNavigationDescription(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaoCaoDoXang.class);
		return super._getNavigationDescription(reqState,
				i18n.getString("BaoCaoDoXang.NavDesc", "Báo cáo sử dụng nhiên liệu"));
	}

	public String getNavigationTab(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaoCaoDoXang.class);
		return i18n.getString("BaoCaoDoXang.NavTab", "Báo cáo sử dụng nhiên liệu");
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
					"Báo cáo sử dụng nhiên liệu");
			 
			if (dem > 0)
				return false;
			else
				return true;

		}
	}

	public String doiGio(long tg) {
		String chuoi = "";
		if (tg == 0) {
			chuoi = "30 giây";
		} else {
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
		}

		return chuoi;

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

	private String ConvertFromEpoch(long epoch, String timezone) {
		TimeZone tz = DateTime.getTimeZone(timezone, null);
		DateTime dt = new DateTime(epoch);
		String dtFmt = dt.format("dd/MM/yyyy HH:mm:ss", tz);
		return dtFmt;
	}

	/*
	 * method to print messages
	 */

	public String note(double fuel) {
		String str = "";
		if (fuel <= 10 && fuel >= 0)
			str = "Không đổ";
		else if (fuel > 10)
			str = "Cấp thêm";
		else
			str = "Tiêu thụ";
		return str;
	}
	// find max of value
	/*private double findMax(double... vals) {
		   double max = Double.NEGATIVE_INFINITY;

		   for (double d : vals) {
		      if (d > max) max = d;
		   }

		   return max;
		}*/
	//two problems:
	//make sure that mysql is true
	//find the way to get adding fuel(is the loop here appropriate or not?)
	public String ReportFuel(String Account, String Device, String tuNgay,
			String denNgay, String timezone) throws IOException {
		int dem = 0;
		String strscr = "";
		record = 0;
		//String mauNen = "";
		try {
			DBCamera objcmr = new DBCamera();
			// ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
			// IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
			// java.util.List<IDDescription> idList =
			// reqState.createIDDescriptionList(false, sortBy);
			// IDDescription list[] = idList.toArray(new
			// IDDescription[idList.size()]);
			// List<String> listDevice= objcmr.Device_reportFuel(Account);
			strscr = strscr
					+ "<table  class='adminSelectTable_sortable' cellspacing='1' id='myTable' ><thead><tr  align='center'><th width='50px' class='adminTableHeaderCol_sort' >STT</th><th width='110px' class='adminTableHeaderCol_sort'>Biển xe</th><th width='200px' class='adminTableHeaderCol_sort'>Ngày</th><th width='130px' class='adminTableHeaderCol_sort'>Lượng dầu trong bình(lít)</th><th width='130px' class='adminTableHeaderCol_sort'>Thay đổi(lít)</th><th width='150px' class='adminTableHeaderCol_sort'>Ghi chú</th><th class='adminTableHeaderCol_sort'>Địa điểm </th></tr></thead>";

			ArrayList<FuelData> listreport = objcmr.BaoCaoDoXang(Account, Device, tuNgay, denNgay, timezone);
			int k = 0;
			double lastValue = 0;
			int totalFuel = 0;
			int consumption = 0;
			long temp = 0;
			double lastDate = 0;
			for (FuelData fuelData : listreport) {
				double currentValue = fuelData.GetFuelLevel();
				double changeValue = lastValue == 0 ? 0 : currentValue
						- lastValue;
				lastValue = currentValue;
				double currentDate = fuelData.getTimeStamp();
				double changeDate = currentDate - lastDate;
				lastDate = currentDate;
					String css = "";
					if (k % 2 == 0)
						css = "adminTableBodyRowOdd";
					else
						css = "adminTableBodyRowEven";
					strscr = strscr + "<tr class =" + css + "><td>" + (dem + 1)
							+ "</td>";
					strscr += "<td>" + fuelData.getDeviceID() + "</td>";
					strscr += "<td>"
							+ ConvertFromEpoch(fuelData.getTimeStamp(),
									 timezone) + "</td>";
					strscr += "<td>" 
								+ Math.round(fuelData.GetFuelLevel())
								+ "</td>";
					strscr += "<td>"
							+ Math.round(changeValue)
							+ "</td>";
					
					strscr += "<td>"
							+ note(Math.round(changeValue))
							+ "</td>";
					strscr += "<td>"
							+ fuelData.getAddress()
							+ "</td></tr>";
					//finalResult += Math.round(fuelData.GetFuelLevel());
				
				dem++;
				record++;
				k++;
				temp = Math.round(changeValue);
				if((temp > 10 && changeDate > 7200) ||(temp > 10 && changeDate == 0) || (temp < 10 && temp > 0  && changeDate <= 600))
					totalFuel += temp;
				if(temp < 0 && changeDate > 300)
					consumption += temp;
			}
			strscr = strscr + "</table>";
			/////////////////////////////
			strscr += "<table  class='adminSelectTable_sortable' cellspacing='' id='myTable1' >";
			strscr += "<tr  align='center'>"
					+ "<th width='525px' class='adminTableHeaderCol_sort'>Tổng Cấp</th>"
					+ "<th width='130px' class='adminTableHeaderCol_sort'>"+totalFuel+"</th>"
					+ "<th class='adminTableHeaderCol_sort'></th>"
					+ "</tr>";
			 strscr =strscr+"</table>";
			/////////////////////////////
			strscr += "<table  class='adminSelectTable_sortable' cellspacing='' id='myTable2' >";
			strscr += "<tr  align='center'>"
					+ "<th width='525px' class='adminTableHeaderCol_sort'>Tổng tiêu thụ</th>"
					+ "<th width='130px' class='adminTableHeaderCol_sort'>"+consumption*(-1)+"</th>"
					+ "<th class='adminTableHeaderCol_sort'></th>"
					+ "</tr>";
				 strscr =strscr+"</table>";
		} catch (Exception e) {
			strscr = "aaa";
			e.printStackTrace();
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

	public String CreateCbbDevice(String accountid, String idselect,
			RequestProperties reqState, PrivateLabel privLabel)
			throws IOException {

		String strre = "<select id ='device' name = 'device' class='textReadOnly' style='width:100px;'>";
		try {
			DBCamera db = new DBCamera();

			List<String> list = db.Device_reportFuel(accountid);

			for (int d = 0; d < list.size(); d++) {
				if (idselect.equalsIgnoreCase(list.get(d)))
					strre += "<option value ='" + list.get(d)
							+ "' selected =\"selected\">" + list.get(d)
							+ "</option>\n";
				else
					strre += "<option value ='" + list.get(d) + "'>"
							+ list.get(d) + "</option>\n";
			}

			strre += "</select>\n";
			strre += "<script type ='text/javascript' language ='javascript'> document.getElementById('device').value = '"
					+ idselect + "';</script>\n";

		} catch (Exception e) {

		}
		return strre;
	}

	public void writePage(final RequestProperties reqState,

	String pageMsg) throws IOException {
		final PrivateLabel privLabel = reqState.getPrivateLabel();
		final I18N i18n = privLabel.getI18N(reportFuel.class);
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
		int i = 0;

		if (excel != "") {

			if (excel.equals("Export Excel")) {
				java.util.Calendar c = java.util.Calendar.getInstance();
				Date now = c.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				String d = sdf.format(now);
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setHeader("Content-Disposition",
						"attachment; filename=baoCaoNhienLieu_" + d + ".xls");
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

				/* Content */

				ct.setCellStyle(cst);
				ct.setCellValue("BÁO CÁO NHIÊN LIỆU");
				title.setHeightInPoints(40);
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
				HSSFCellStyle csNgay = wb.createCellStyle();

				HSSFFont fngay = wb.createFont();
				HSSFRow rngay = sheet.createRow((short) 2);

				fngay.setFontHeightInPoints((short) 10);
				fngay.setBoldweight((short) fngay.BOLDWEIGHT_BOLD);
				csNgay.setFont(fngay);
				HSSFCell cTuNgay = rngay.createCell((short) 1);
				cTuNgay.setCellStyle(csNgay);
				cTuNgay.setCellValue("Từ ");
				rngay.createCell((short) 2).setCellValue(datefrom);
				HSSFCell cDenNgay = rngay.createCell((short) 3);
				cDenNgay.setCellStyle(csNgay);
				cDenNgay.setCellValue("Đến ");
				rngay.createCell((short) 4).setCellValue(dateto);

				i = i + 3;

				/* Content */

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

				f.setBoldweight((short) f.BOLDWEIGHT_BOLD);
				cellStyle.setFont(f);

				HSSFRow rowhead = sheet.createRow((short) i);

				HSSFCell h0 = rowhead.createCell((short) 0);
				h0.setCellStyle(cellStyle);
				h0.setCellValue("Biển xe");
				HSSFCell h1 = rowhead.createCell((short) 1);
				h1.setCellStyle(cellStyle);
				h1.setCellValue("Ngày");
				HSSFCell h2 = rowhead.createCell((short) 2);
				h2.setCellStyle(cellStyle);
				h2.setCellValue("Lượng dầu trong bình(lít)");
				HSSFCell h3 = rowhead.createCell((short) 3);
				h3.setCellStyle(cellStyle);
				h3.setCellValue("Thay đổi(lít)");
				HSSFCell h4 = rowhead.createCell((short) 4);
				h4.setCellStyle(cellStyle);
				h4.setCellValue("Ghi chú");
				HSSFCell h5 = rowhead.createCell((short) 5);
				h5.setCellStyle(cellStyle);
				h5.setCellValue("Địa điểm");
		
				rowhead.setHeightInPoints((short) 40);

				DBCamera objcmr = new DBCamera();
				try {
					// String deviceID="";
					/*List<String> listDevice = objcmr.Device_reportFuel(currAcct
							.getAccountID());*/
					int k = 0;
					int totalFuel = 0;
					int consumption = 0;
					long temp = 0;
					ArrayList<FuelData> listreport = objcmr.BaoCaoDoXang(
							currAcct.getAccountID(), contentall,datefrom, dateto,currAcct.getTimeZone());
					double lastValue = 0;
					for (FuelData fuelData : listreport) {
						double currentValue = fuelData.GetFuelLevel();
						double changeValue = lastValue == 0 ? 0 : currentValue
								- lastValue;
						lastValue = currentValue;
						HSSFCellStyle csr = wb.createCellStyle();
						csr.setBorderTop((short) 1);
						csr.setBorderRight((short) 1);
						csr.setBorderLeft((short) 1);
						csr.setBorderBottom((short) 1);
						
						HSSFRow row = sheet.createRow((short) (i + 1));
						HSSFCell r0 = row.createCell((short) 0);
						r0.setCellStyle(csr);
						r0.setCellValue(fuelData.getDeviceID());
						HSSFCell r1 = row.createCell((short) 1);
						r1.setCellStyle(csr);
						r1.setCellValue(ConvertFromEpoch(fuelData
								.getTimeStamp(), currAcct.getTimeZone()));
						// row.createCell((short)
						// 2).setCellValue(rs.getString(4));
						HSSFCell r2 = row.createCell((short) 2);
						r2.setCellStyle(csr);
						r2.setCellValue(Math.round(fuelData.GetFuelLevel()));

						HSSFCell r3 = row.createCell((short) 3);
						r3.setCellStyle(csr);
						r3.setCellValue(Math.round(changeValue));

						HSSFCell r4 = row.createCell((short) 4);
						r4.setCellStyle(csr);
						r4.setCellValue(note(Math.round(changeValue)));

						HSSFCell r5 = row.createCell((short) 5);
						r5.setCellStyle(csr);
						r5.setCellValue(GetUTF8FromNCRDecimalString(fuelData.getAddress()));

						i++;
						k++;
						temp = Math.round(changeValue);
						if(temp > 0)
							totalFuel += temp;
						else
							consumption += temp;
					}

					sheet.autoSizeColumn(0);
					sheet.autoSizeColumn(1);
					sheet.autoSizeColumn(2);
					sheet.autoSizeColumn(3);
					sheet.autoSizeColumn(4);
					sheet.autoSizeColumn(5);
					//sheet.autoSizeColumn(6);
					// sheet.autoSizeColumn(0);
					
					/*k++;
					HSSFRow rowhead2 = sheet.createRow((short) k);
					sheet.addMergedRegion(new CellRangeAddress(k, k, 0, 2));
					
					HSSFCellStyle csr = wb.createCellStyle();
					csr.setBorderTop((short) 1);
					csr.setBorderBottom((short) 1);
					
					HSSFCell h7 = rowhead2.createCell((short) 0);
					h7.setCellStyle(cellStyle);
					h7.setCellValue("Tổng Cấp");
					
					HSSFCell h12 = rowhead2.createCell((short) 1);
					h12.setCellStyle(csr);

					HSSFCell h13 = rowhead2.createCell((short) 2);
					h13.setCellStyle(csr);
					
					HSSFCell h11 = rowhead2.createCell((short) 4);
					h11.setCellStyle(cellStyle);
					h11.setCellValue(totalFuel);
					*/
					/////////////////////////////////////////////////
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
				String cssDir = BaoCaoDoXang.this.getCssDirectory();
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
						"reportFuel");
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

				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String d = sdf.format(now);

				out.println("<span class='" + CommonServlet.CSS_MENU_TITLE
						+ "'>Báo cáo đổ nhiên liệu</span><br/>");
				out.println("<hr/>");
				out.println("<form name='baocaodungdo' method='post' action='"
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

				out.print("<td class='"
						+ CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER
						+ "' width='100px'  >&#x0110;&#x1EBF;n ng&#x00E0;y:</td>\n");
				out.print("<td>");
				out.print("<input id='dateto' class='textReadOnly' name='dateto' type='text' style='width:120px' onclick=\"displayCalendar(this,'dd/mm/yyyy hh:ii',this,true,'','dateto')\" value='"
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
				if (btnXem.equals("Xem")) {
					String sql = ReportFuel(currAcct.getAccountID(),contentall,datefrom, dateto, currAcct.getTimeZone());
					out.print(sql);
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