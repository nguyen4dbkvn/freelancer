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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
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

public class BaocaoTieuthuNhienLieu extends WebPageAdaptor implements Constants {

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

	public BaocaoTieuthuNhienLieu() {
		this.setBaseURI(RequestProperties.TRACK_BASE_URI());
		this.setPageName(PAGE_FUELCONSUMPTION);
		this.setPageNavigation(new String[] { PAGE_LOGIN, PAGE_MENU_TOP });
		this.setLoginRequired(true);
	}

	// ------------------------------------------------------------------------

	public String getMenuName(RequestProperties reqState) {
		return MenuBar.MENU_ADMIN;
	}

	public String getMenuDescription(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaocaoTieuthuNhienLieu.class);
		return super._getMenuDescription(reqState,
				i18n.getString("Fuel.Consumption", "Báo cáo tiêu thụ nhiên liệu theo thời gian chạy"));
	}

	public String getMenuHelp(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaocaoTieuthuNhienLieu.class);
		return super._getMenuHelp(reqState,
				i18n.getString("Fuel.Consumption", "Báo cáo tiêu thụ nhiên liệu theo thời gian chạy"));
	}

	// ------------------------------------------------------------------------

	public String getNavigationDescription(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaocaoTieuthuNhienLieu.class);
		return super._getNavigationDescription(reqState,
				i18n.getString("Fuel.Consumption", "Báo cáo tiêu thụ nhiên liệu theo thời gian chạy"));
	}

	public String getNavigationTab(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaocaoTieuthuNhienLieu.class);
		return i18n.getString("Fuel.Consumption", "Báo cáo tiêu thụ nhiên liệu theo thời gian chạy");
	}

	// ------------------------------------------------------------------------

	public boolean isOkToDisplay(RequestProperties reqState) {
		Account account = (reqState != null) ? reqState.getCurrentAccount() : null;
		if (account == null) {
			return false; // no account?
		} else {
			int dem = 0;
			DBCamera objcmr = new DBCamera();
			dem = objcmr.phanQuyen(account.getAccountID(), "FuelConsumption");

			if (dem > 0)
				return false;
			else
				return true;
		}
	}

	public String doiGio(long tg) {
		String chuoi = "";
		if (tg == 0) {
			chuoi = "0 giây";
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

	public String DongMo(int tg) {

		String chuoi = "";
		if (tg == 1)
			chuoi = "Đóng";
		else
			chuoi = "Mở";

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

	public String ReportFuel(String Account, String Device, String tuNgay, String denNgay, PrivateLabel privLabel,
			RequestProperties reqState, String timezone) throws IOException {
		int dem = 0;
		String strscr = "", chuoi = "", chuoi1 = "";
		record = 0;
		String mauNen = "";
		long tg = 0, Tongtg = 0;
		double Km = 0, TongKm = 0, TongFuel = 0;
		DecimalFormat df = new DecimalFormat("#.##");

		try {
			DBCamera objcmr = new DBCamera();
			IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
			java.util.List<IDDescription> idList = reqState.createIDDescriptionList(false, sortBy);
			IDDescription list[] = idList.toArray(new IDDescription[idList.size()]);

			strscr += "<table  class='adminSelectTable_sortable' cellspacing='1' id='myTable' >"
					+ "<thead><tr  align='center'>" + "<th width='50px' class='adminTableHeaderCol_sort' >STT</th>"
					+ "<th width='80px' class='adminTableHeaderCol_sort'>Biển xe</th>"
					+ "<th width='150px' class='adminTableHeaderCol_sort'>Thời điểm bắt đầu chạy</th>"
					+ "<th width='150px' class='adminTableHeaderCol_sort'>Thời điểm kết thúc</th>"
					+ "<th width='150px' class='adminTableHeaderCol_sort'>Khoảng thời gian chạy</th>"
					+ "<th width='80px' class='adminTableHeaderCol_sort'>Quãng đường đi được(km)</th>"
					+ "<th width='80px' class='adminTableHeaderCol_sort'>Nhiên liệu tiêu thụ (lít)</th>"
					+ "<th width='250px' class='adminTableHeaderCol_sort'>Địa chỉ bắt đầu</th>"
					+ "<th class='adminTableHeaderCol_sort'>Địa chỉ kết thúc</th></tr>" + "</thead>";
			// if (Device.equals("-1")) {
			// for (int d = 0; d < list.length; d++) {
			// TongKm = 0;
			// TongFuel = 0;
			// Tongtg = 0;
			// ArrayList<DrivingTime> listreport =
			// objcmr.GetFuelConsumption(Account, list[d].getID(), tuNgay,
			// denNgay, timezone);
			// strscr += "<tr style='height:30px;color:black'><td colspan='9'
			// align='left'><b>" + list[d].getID()
			// + "</b></td></tr>";
			// if (listreport.size() == 0) {
			// strscr += "<tr class =adminTableBodyRowOdd><td colspan='9'
			// align='left'>Không có dữ liệu</td></tr>";
			// }
			//
			// for (int k = 0; k < listreport.size(); k++) {
			// String css = "";
			// if (k % 2 == 0)
			// css = "adminTableBodyRowOdd";
			// else
			// css = "adminTableBodyRowEven";
			//
			// strscr += "<tr class =" + css + "><td>" + (dem + 1) + "</td>";
			// strscr += "<td> " + listreport.get(k).getDeviceID() + "</td>";
			// strscr += "<td>" +
			// ConvertFromEpoch(listreport.get(k).getStartTime(), timezone) +
			// "</td>";
			// strscr += "<td>" +
			// ConvertFromEpoch(listreport.get(k).getStopTime(), timezone) +
			// "</td>";
			// tg = listreport.get(k).getStopTime() -
			// listreport.get(k).getStartTime();
			// Tongtg += tg;
			// strscr += "<td>" + doiGio(tg) + "</td>";
			// Km = listreport.get(k).getDistance() / 1000;
			// TongKm += Km;
			// strscr += "<td>" + df.format(Km) + "</td>";
			// strscr += "<td>" +
			// df.format(listreport.get(k).getFuelConsumption()) + "</td>";
			// TongFuel += listreport.get(k).getFuelConsumption();
			// strscr += "<td>" + listreport.get(k).getStartAddress() + "</td>";
			// strscr += "<td>" + listreport.get(k).getEndAddress() + "</td>";
			// strscr += "</tr>";
			// dem++;
			// record++;
			// }
			// strscr += "<tr align='center'>" + "<th colspan='4'
			// class='adminTableHeaderCol_sort'>Tổng</th>"
			// + "<th class='adminTableHeaderCol_sort'>" + doiGio(Tongtg) +
			// "</th>"
			// + "<th class='adminTableHeaderCol_sort'>" + df.format(TongKm) +
			// "</th>"
			// + "<th class='adminTableHeaderCol_sort'>" + df.format(TongFuel) +
			// "</th>"
			// + "<th class='adminTableHeaderCol_sort'></th>"
			// + "<th class='adminTableHeaderCol_sort'></th>" + "</tr>";
			// }
			// } else {
			// ArrayList<DrivingTime> listreport =
			// objcmr.GetFuelConsumption(Account, Device, tuNgay, denNgay,
			// timezone);
			ArrayList<EventFuelByDate> listreport = objcmr.reportFuelTimeRunByDate(Account, Device, tuNgay, denNgay,
					timezone);
			TongKm = 0;
			TongFuel = 0;
			Tongtg = 0;
			for (int k = 0; k < listreport.size(); k++) {
				String css = "";
				if (k % 2 == 0)
					css = "adminTableBodyRowOdd";
				else
					css = "adminTableBodyRowEven";
				strscr += "<tr class =" + css + "><td>" + (dem + 1) + "</td>";
				strscr += "<td> " + listreport.get(k).getDeviceID() + "</td>";
				strscr += "<td>" + ConvertFromEpoch(listreport.get(k).getTimeBegin(), timezone) + "</td>";
				strscr += "<td>" + ConvertFromEpoch(listreport.get(k).getTimeEnd(), timezone) + "</td>";
				tg = listreport.get(k).getTimeEnd() - listreport.get(k).getTimeBegin();
				Tongtg += tg;
				strscr += "<td>" + doiGio(tg) + "</td>";
				Km = listreport.get(k).getDistanceKM();
				TongKm += Km;
				strscr += "<td>" + df.format(Km) + "</td>";
				strscr += "<td>" + df.format(listreport.get(k).getFuelConsumption()) + "</td>";
				TongFuel += listreport.get(k).getFuelConsumption();
				strscr += "<td>" + listreport.get(k).getAddressBegin() + "</td>";
				strscr += "<td>" + listreport.get(k).getAddressEnd() + "</td>";
				strscr += "</tr>";
				dem++;
				record++;
			}
			strscr = strscr + chuoi + chuoi1;
			// strscr += "<tr align='center'>" + "<th colspan='4'
			// class='adminTableHeaderCol_sort'>Tổng</th>"
			// + "<th class='adminTableHeaderCol_sort'>" + doiGio(Tongtg) +
			// "</th>"
			// + "<th class='adminTableHeaderCol_sort'>" + df.format(TongKm) +
			// "</th>"
			// + "<th class='adminTableHeaderCol_sort'>" + df.format(TongFuel) +
			// "</th>"
			// + "<th class='adminTableHeaderCol_sort'></th>" + "<th
			// class='adminTableHeaderCol_sort'></th>"
			// + "</tr>";
			//// }
			// strscr = strscr + "</table>";
		} catch (Exception e) {
			strscr = e.getMessage();
		}
		return strscr;
	}

	public String fuelLevelStart(String AccountID, String DeviceID, long time) {
		DBCamera objcmr = new DBCamera();
		String str = "-";
		double fuelStart = 0;
		try {

			fuelStart = objcmr.FuelBeforeStop(AccountID, DeviceID, time);
			if (fuelStart == 0.0) {
				str = "-";
			} else {
				fuelStart = round(fuelStart, 2);
				str = Double.toString(fuelStart);
			}
		} catch (Exception e) {

		}

		return str;
	}

	public String fuelLevelEnd(String AccountID, String DeviceID, long time, double fuel) {
		DBCamera objcmr = new DBCamera();
		String str = "-";
		double fuelStart = 0;
		try {

			List<Double> list = objcmr.FuelAfterStop(AccountID, DeviceID, time);
			if (list.size() < 1) {
				if (fuel <= 0)
					str = "-";
				else
					str = Double.toString(round(fuel, 2));
			} else {
				fuelStart = list.get(0);
				if (fuelStart == 0)
					str = "-";
				else
					str = Double.toString(round(fuelStart, 2));
			}

		} catch (Exception e) {
			str = "aa";
		}

		return str;
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

	public String CreateCbbDevice(String accountid, String idselect, RequestProperties reqState, PrivateLabel privLabel)
			throws IOException {
		IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
		java.util.List<IDDescription> idList = reqState.createIDDescriptionList(false, sortBy);
		IDDescription list[] = idList.toArray(new IDDescription[idList.size()]);

		String strre = "<select id ='device' name = 'device' class='textReadOnly' style='width:100px;'>";
		if (idselect == "-1") {
			strre += "<option value ='-1' selected =\"selected\">Tất cả</option>\n";
		} else {
			strre += "<option value ='-1'>Tất cả</option>\n";
		}
		for (int d = 0; d < list.length; d++) {
			if (idselect.equalsIgnoreCase(list[d].getID()))
				strre += "<option value ='" + list[d].getID() + "' selected =\"selected\">" + list[d].getID()
						+ "</option>\n";
			else
				strre += "<option value ='" + list[d].getID() + "'>" + list[d].getID() + "</option>\n";
		}
		strre += "</select>\n";
		strre += "<script type ='text/javascript' language ='javascript'> document.getElementById('device ').value = '"
				+ idselect + "';</script>\n";
		return strre;
	}

	public void writePage(final RequestProperties reqState,

			String pageMsg) throws IOException {
		final PrivateLabel privLabel = reqState.getPrivateLabel();
		final I18N i18n = privLabel.getI18N(BaocaoTieuthuNhienLieu.class);
		final Locale locale = reqState.getLocale();
		final Account currAcct = reqState.getCurrentAccount();
		final User currUser = reqState.getCurrentUser();
		final String pageName = this.getPageName();
		String m = pageMsg;
		boolean error = false;

		HttpServletRequest request = reqState.getHttpServletRequest();
		HttpServletResponse response = reqState.getHttpServletResponse();
		String excel = AttributeTools.getRequestString(request, "btnExcel", "");
		String datefrom = AttributeTools.getRequestString(request, "datefrom", "");
		String dateto = AttributeTools.getRequestString(request, "dateto", "");
		String contentall = AttributeTools.getRequestString(request, "device", "");
		int dem = 0, num = 0;
		long tg = 0, Tongtg = 0;
		double Km = 0, TongKm = 0, TongFuel = 0;
		DecimalFormat df = new DecimalFormat("#.##");
		int j;

		if (excel != "") {
			if (excel.equals("Export Excel")) {
				java.util.Calendar c = java.util.Calendar.getInstance();
				Date now = c.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				String d = sdf.format(now);
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setHeader("Content-Disposition",
						"attachment; filename=baocaoTieuthuNhienlieuTheoTG_" + d + ".xls");
				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFSheet sheet = wb.createSheet("Report");
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
				ct.setCellValue("BÁO CÁO TIÊU THỤ NHIÊN LIỆU THEO THỜI GIAN CHẠY");
				title.setHeightInPoints(40);
				sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
				HSSFCellStyle csNgay = wb.createCellStyle();

				HSSFFont fngay = wb.createFont();
				HSSFRow rngay = sheet.createRow((short) 2);

				fngay.setFontHeightInPoints((short) 10);
				fngay.setBoldweight((short) fngay.BOLDWEIGHT_BOLD);
				csNgay.setFont(fngay);
				HSSFCell cTuNgay = rngay.createCell((short) 1);
				cTuNgay.setCellStyle(csNgay);
				cTuNgay.setCellValue("Từ ngày");
				rngay.createCell((short) 2).setCellValue(datefrom + "   00:00:00");
				HSSFCell cDenNgay = rngay.createCell((short) 3);
				cDenNgay.setCellStyle(csNgay);
				cDenNgay.setCellValue("Đến ngày");
				rngay.createCell((short) 4).setCellValue(dateto + "   23:59:59");

				dem = dem + 3;

				HSSFCellStyle cellStyle = wb.createCellStyle();
				cellStyle.setBorderTop((short) 1);
				cellStyle.setBorderRight((short) 1);
				cellStyle.setBorderLeft((short) 1);
				cellStyle.setBorderBottom((short) 1);
				cellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cellStyle.setWrapText(true);
				HSSFFont f = wb.createFont();
				f.setFontHeightInPoints((short) 10);
				f.setBoldweight((short) f.BOLDWEIGHT_BOLD);
				cellStyle.setFont(f);

				HSSFRow rowhead = sheet.createRow((short) dem);

				HSSFCell h0 = rowhead.createCell((short) 0);
				h0.setCellStyle(cellStyle);
				h0.setCellValue("Biển xe");
				HSSFCell h1 = rowhead.createCell((short) 1);
				h1.setCellStyle(cellStyle);
				h1.setCellValue("Thời điểm bắt đầu chạy");
				HSSFCell h2 = rowhead.createCell((short) 2);
				h2.setCellStyle(cellStyle);
				h2.setCellValue("Thời điểm kết thúc");
				HSSFCell h3 = rowhead.createCell((short) 3);
				h3.setCellStyle(cellStyle);
				h3.setCellValue("Khoảng thời gian chạy");
				HSSFCell h4 = rowhead.createCell((short) 4);
				h4.setCellStyle(cellStyle);
				h4.setCellValue("Quãng đường đi được (Km)");
				HSSFCell h5 = rowhead.createCell((short) 5);
				h5.setCellStyle(cellStyle);
				h5.setCellValue("Nhiên liệu tiêu thụ (lít)");
				HSSFCell h6 = rowhead.createCell((short) 6);
				h6.setCellStyle(cellStyle);
				h6.setCellValue("Địa chỉ bắt đầu");
				HSSFCell h7 = rowhead.createCell((short) 7);
				h7.setCellStyle(cellStyle);
				h7.setCellValue("Địa chỉ kết thúc");

				rowhead.setHeightInPoints((short) 40);

				DBCamera objcmr = new DBCamera();
				try {
					String deviceID = "";
					List<String> listDevice = objcmr.GetListDeviceByAccountID(currAcct.getAccountID());

					// if (contentall.equals("-1")) {
					// for (int d1 = 0; d1 < listDevice.size(); d1++) {
					// ArrayList<DrivingTime> listreport =
					// objcmr.GetFuelConsumption(currAcct.getAccountID(),
					// listDevice.get(d1), datefrom, dateto,
					// currAcct.getTimeZone());
					//
					// dem++;
					//
					// HSSFRow row1 = sheet.createRow((short) dem);
					// row1.setHeightInPoints(25);
					// sheet.addMergedRegion(new CellRangeAddress(dem, dem, 0,
					// 7));
					//
					// HSSFCellStyle csXe = wb.createCellStyle();
					// csXe.setBorderBottom((short) 1);
					// csXe.setBorderTop((short) 1);
					// csXe.setBorderRight((short) 1);
					// csXe.setBorderLeft((short) 1);
					// csXe.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
					// csXe.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					//
					// HSSFFont fxe = wb.createFont();
					// fxe.setFontHeightInPoints((short) 10);
					// fxe.setBoldweight((short) f.BOLDWEIGHT_BOLD);
					// csXe.setFont(fxe);
					//
					// HSSFCell cellA1 = row1.createCell((short) 0);
					// cellA1.setCellValue(listDevice.get(d1));
					// cellA1.setCellStyle(csXe);
					//
					// if (listreport.size() == 0) {
					// dem++;
					//
					// HSSFCellStyle csr = wb.createCellStyle();
					// csr.setBorderTop((short) 1);
					// csr.setBorderRight((short) 1);
					// csr.setBorderLeft((short) 1);
					// csr.setBorderBottom((short) 1);
					// csr.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					// csr.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					//
					// HSSFRow row5 = sheet.createRow((short) dem);
					// row5.setHeightInPoints(25);
					// sheet.addMergedRegion(new CellRangeAddress(dem, dem, 0,
					// 7));
					//
					// HSSFCell r = row5.createCell((short) 0);
					// r.setCellStyle(csr);
					// r.setCellValue("Không có dữ liệu");
					//
					// HSSFCell r1 = row5.createCell((short) 1);
					// r1.setCellStyle(csr);
					// HSSFCell r2 = row5.createCell((short) 2);
					// r2.setCellStyle(csr);
					// HSSFCell r3 = row5.createCell((short) 3);
					// r3.setCellStyle(csr);
					// HSSFCell r4 = row5.createCell((short) 4);
					// r4.setCellStyle(csr);
					// HSSFCell r5 = row5.createCell((short) 5);
					// r5.setCellStyle(csr);
					// HSSFCell r6 = row5.createCell((short) 6);
					// r6.setCellStyle(csr);
					// HSSFCell r7 = row5.createCell((short) 7);
					// r7.setCellStyle(csr);
					// }
					//
					// for (int k = 0; k < listreport.size(); k++) {
					// dem++;
					// num++;
					//
					// HSSFCellStyle csr = wb.createCellStyle();
					// csr.setBorderTop((short) 1);
					// csr.setBorderRight((short) 1);
					// csr.setBorderLeft((short) 1);
					// csr.setBorderBottom((short) 1);
					// csr.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
					// csr.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					//
					// HSSFRow row = sheet.createRow((short) dem);
					// HSSFCell r0 = row.createCell((short) 0);
					// r0.setCellStyle(csr);
					// r0.setCellValue(listreport.get(k).getDeviceID());
					//
					// HSSFCell r1 = row.createCell((short) 1);
					// r1.setCellStyle(csr);
					// r1.setCellValue(
					// ConvertFromEpoch(listreport.get(k).getStartTime(),
					// currAcct.getTimeZone()));
					//
					// HSSFCell r2 = row.createCell((short) 2);
					// r2.setCellStyle(csr);
					// r2.setCellValue(
					// ConvertFromEpoch(listreport.get(k).getStopTime(),
					// currAcct.getTimeZone()));
					//
					// HSSFCell r3 = row.createCell((short) 3);
					// r3.setCellStyle(csr);
					// tg = listreport.get(k).getStopTime() -
					// listreport.get(k).getStartTime();
					// Tongtg += tg;
					// r3.setCellValue(doiGio(tg));
					//
					// HSSFCell r4 = row.createCell((short) 4);
					// r4.setCellStyle(csr);
					// Km = listreport.get(k).getDistance() / 1000;
					// TongKm += Km;
					// r4.setCellValue(df.format(Km));
					//
					// HSSFCell r5 = row.createCell((short) 5);
					// r5.setCellStyle(csr);
					// TongFuel += listreport.get(k).getFuelConsumption();
					// r5.setCellValue(df.format(listreport.get(k).getFuelConsumption()));
					//
					// HSSFCell r6 = row.createCell((short) 6);
					// r6.setCellStyle(csr);
					// r6.setCellValue(GetUTF8FromNCRDecimalString(listreport.get(k).getStartAddress()));
					//
					// HSSFCell r7 = row.createCell((short) 7);
					// r7.setCellStyle(csr);
					// r7.setCellValue(GetUTF8FromNCRDecimalString(listreport.get(k).getEndAddress()));
					// }
					// }
					// } else {
					// ArrayList<DrivingTime> listreport =
					// objcmr.GetFuelConsumption(currAcct.getAccountID(),
					// contentall, datefrom, dateto, currAcct.getTimeZone());
					ArrayList<EventFuelByDate> listreport = objcmr.reportFuelTimeRunByDate(currAcct.getAccountID(),
							contentall, datefrom, dateto, currAcct.getTimeZone());
					dem++;

					HSSFRow row1 = sheet.createRow((short) dem);
					row1.setHeightInPoints(25);
					sheet.addMergedRegion(new CellRangeAddress(dem, dem, 0, 7));

					HSSFCellStyle csXe = wb.createCellStyle();
					csXe.setBorderRight((short) 1);
					csXe.setBorderLeft((short) 1);
					csXe.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
					csXe.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

					HSSFFont fxe = wb.createFont();
					fxe.setFontHeightInPoints((short) 10);
					fxe.setBoldweight((short) fxe.BOLDWEIGHT_BOLD);
					csXe.setFont(fxe);

					HSSFCell cellA1 = row1.createCell((short) 0);
					cellA1.setCellValue(contentall);
					cellA1.setCellStyle(csXe);

					for (int k = 0; k < listreport.size(); k++) {
						dem++;
						num++;

						HSSFCellStyle csr = wb.createCellStyle();
						csr.setBorderTop((short) 1);
						csr.setBorderRight((short) 1);
						csr.setBorderLeft((short) 1);
						csr.setBorderBottom((short) 1);
						csr.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
						csr.setAlignment(HSSFCellStyle.ALIGN_CENTER);

						HSSFRow row = sheet.createRow((short) dem);
						HSSFCell r0 = row.createCell((short) 0);
						r0.setCellStyle(csr);
						r0.setCellValue(listreport.get(k).getDeviceID());

						HSSFCell r1 = row.createCell((short) 1);
						r1.setCellStyle(csr);
						r1.setCellValue(ConvertFromEpoch(listreport.get(k).getTimeBegin(), currAcct.getTimeZone()));

						HSSFCell r2 = row.createCell((short) 2);
						r2.setCellStyle(csr);
						r2.setCellValue(ConvertFromEpoch(listreport.get(k).getTimeEnd(), currAcct.getTimeZone()));

						HSSFCell r3 = row.createCell((short) 3);
						r3.setCellStyle(csr);
						tg = listreport.get(k).getTimeEnd() - listreport.get(k).getTimeBegin();
						Tongtg += tg;
						r3.setCellValue(doiGio(tg));

						HSSFCell r4 = row.createCell((short) 4);
						r4.setCellStyle(csr);
						Km = listreport.get(k).getDistanceKM();
						TongKm += Km;
						r4.setCellValue(df.format(Km));

						HSSFCell r5 = row.createCell((short) 5);
						r5.setCellStyle(csr);
						TongFuel += listreport.get(k).getFuelConsumption();
						r5.setCellValue(df.format(listreport.get(k).getFuelConsumption()));

						HSSFCell r6 = row.createCell((short) 6);
						r6.setCellStyle(csr);
						r6.setCellValue(GetUTF8FromNCRDecimalString(listreport.get(k).getAddressBegin()));

						HSSFCell r7 = row.createCell((short) 7);
						r7.setCellStyle(csr);
						r7.setCellValue(GetUTF8FromNCRDecimalString(listreport.get(k).getAddressEnd()));
					}

					// }

					sheet.autoSizeColumn(0);
					sheet.autoSizeColumn(1);
					sheet.autoSizeColumn(2);
					sheet.autoSizeColumn(3);
					sheet.autoSizeColumn(4);
					sheet.autoSizeColumn(5);
					sheet.autoSizeColumn(6);

					dem++;
					HSSFRow rowhead2 = sheet.createRow((short) dem);
					sheet.addMergedRegion(new CellRangeAddress(dem, dem, 0, 2));

					HSSFCellStyle csr = wb.createCellStyle();
					csr.setBorderTop((short) 1);
					csr.setBorderBottom((short) 1);

					HSSFCell h8 = rowhead2.createCell((short) 0);
					h8.setCellStyle(cellStyle);
					h8.setCellValue("Tổng");

					HSSFCell h14 = rowhead2.createCell((short) 1);
					h14.setCellStyle(csr);

					HSSFCell h15 = rowhead2.createCell((short) 2);
					h15.setCellStyle(csr);

					HSSFCell h11 = rowhead2.createCell((short) 3);
					h11.setCellStyle(cellStyle);
					h11.setCellValue(doiGio(Tongtg));

					HSSFCell h12 = rowhead2.createCell((short) 4);
					h12.setCellStyle(cellStyle);
					h12.setCellValue(df.format(TongKm));

					HSSFCell h13 = rowhead2.createCell((short) 5);
					h13.setCellStyle(cellStyle);
					h13.setCellValue(df.format(TongFuel));

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
				String cssDir = BaocaoTieuthuNhienLieu.this.getCssDirectory();
				WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
			}
		};

		/* javascript */
		HTMLOutput HTML_JS = new HTMLOutput() {
			public void write(PrintWriter out) throws IOException {
				MenuBar.writeJavaScript(out, pageName, reqState);
				out.println("        <script type=\"text/javascript\" src=\"js/PCalendar.js\"></script>\n");
				out.println("        <script type=\"text/javascript\" src=\"js/jquery-1.4.2.min.js\"></script>\n");
				out.print("<script src='./js/jquery.tinyscrollbar.min.js' type=\"text/javascript\"></script>");
				out.print(
						"<script type='text/javascript'>$(document).ready(function() {$('#scrollbar2').tinyscrollbar();}); </script>");
			}
		};

		/* Content */

		HTMLOutput HTML_CONTENT = new HTMLOutput(CommonServlet.CSS_CONTENT_FRAME, m) {
			public void write(PrintWriter out) throws IOException {
				// Print.logStackTrace("here");
				String menuURL = privLabel.getWebPageURL(reqState, PAGE_MENU_TOP);
				String chgURL = privLabel.getWebPageURL(reqState, pageName, COMMAND_INFO_UPDATE);
				String frameTitle = i18n.getString("Camera.PageTitle", "reportFuel");
				HttpServletRequest request = reqState.getHttpServletRequest();
				String contentall = "";
				String datefrom = "homnay";
				String dateto = "homnay";
				String flag = "0";

				// view submit

				datefrom = AttributeTools.getRequestString(request, "datefrom", "");
				dateto = AttributeTools.getRequestString(request, "dateto", "");
				contentall = AttributeTools.getRequestString(request, "device", "");

				Date now = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String d = sdf.format(now);

				out.println("<span class='" + CommonServlet.CSS_MENU_TITLE
						+ "'>Báo cáo tiêu thụ nhiên liệu theo thời gian chạy</span><br/>");
				out.println("<hr/>");
				out.println("<form name='baocaotieuthu' method='post' action='" + chgURL + "' target='_self'>\n");

				out.println("<table class='" + CommonServlet.CSS_ADMIN_VIEW_TABLE
						+ "' cellspacing='0' callpadding='0' border='0'  style='padding:15px 0 15px'>\n");

				out.print("<tr style='height:40px;'>\n");
				out.print(
						"<td class='" + CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER + "' width='100px' >Từ ngày:</td>\n");
				out.print("<td class='" + CommonServlet.CSS_ADMIN_VIEW_TABLE_DATA + "' >\n");
				out.print(
						"<input id='datefrom' class='textReadOnly' name='datefrom' type='text' style='width:90px' onclick=\"displayCalendar(this,'dd/mm/yyyy',this,true,'','datefrom')\" value='"
								+ d + "' />");
				if (datefrom != "") {
					out.print(
							"<script language ='javascript' type ='text/javascript'>document.getElementById('datefrom').value ='"
									+ datefrom + "'; </script>");
				}
				out.print("</td>");

				out.print("<td class='" + CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER
						+ "' width='100px'  >Đến ngày:</td>\n");
				out.print("<td>");
				out.print(
						"<input id='dateto' class='textReadOnly' name='dateto' type='text' style='width:90px' onclick=\"displayCalendar(this,'dd/mm/yyyy',this,true,'','dateto')\" value='"
								+ d + "' />");
				if (dateto != "") {
					out.print(
							"<script language ='javascript' type ='text/javascript'>document.getElementById('dateto').value ='"
									+ dateto + "'; </script>");
				}
				out.print("</td>");

				out.print("<td align='right' width='100px'><span style='margin-left: 10px;margin-right:5px;'>"
						+ i18n.getString("DeviceSelect", "Chọn xe:") + "</span></td><td>\n");
				out.print(CreateCbbDevice(currAcct.getAccountID(), contentall, reqState, privLabel));

				out.print("</td>");
				out.print("</tr>");

				out.println("</table>");

				out.print("<div class='viewhoz'>");
				out.println("<table >");
				out.print("<tr >");
				out.print("<td width='105px'></td>");
				out.print("<td> <input type ='submit' id ='btnview' value ='Xem' name ='btnview' class='button1'/>");
				out.print("</td><td style='width:155px'></td>");

				out.print(
						"<td ><input type ='submit' id ='btnExcel' value ='Export Excel' name ='btnExcel' class='button1'/></td>");
				out.print("<td ></td>");
				out.println("</tr>");

				out.println("</table>");
				out.print("</div>");
				String btnXem = AttributeTools.getRequestString(request, "btnview", "");
				if (btnXem.equals("Xem")) {
					String sql = ReportFuel(currAcct.getAccountID(), contentall, datefrom, dateto, privLabel, reqState,
							currAcct.getTimeZone());
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
