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

public class BaoCaoDungDo extends WebPageAdaptor implements Constants {

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

	public BaoCaoDungDo() {
		this.setBaseURI(RequestProperties.TRACK_BASE_URI());
		this.setPageName(PAGE_BAOCAODUNGDO);
		this.setPageNavigation(new String[] { PAGE_LOGIN, PAGE_MENU_TOP });
		this.setLoginRequired(true);
	}

	// ------------------------------------------------------------------------

	public String getMenuName(RequestProperties reqState) {
		return MenuBar.MENU_ADMIN;
	}

	public String getMenuDescription(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaoCaoDungDo.class);
		return super._getMenuDescription(reqState, i18n.getString("BaoCaoDungDo.Menu", "Báo cáo dừng đỗ"));
	}

	public String getMenuHelp(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaoCaoDungDo.class);
		return super._getMenuHelp(reqState, i18n.getString("BaoCaoDungDo.MenuHelp", "Báo cáo dừng đỗ"));
	}

	// ------------------------------------------------------------------------

	public String getNavigationDescription(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaoCaoDungDo.class);
		return super._getNavigationDescription(reqState, i18n.getString("BaoCaoDungDo.NavDesc", "Báo cáo dừng đỗ"));
	}

	public String getNavigationTab(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaoCaoDungDo.class);
		return i18n.getString("BaoCaoDungDo.NavTab", "Báo cáo dừng đỗ");
	}

	// ------------------------------------------------------------------------

	public boolean isOkToDisplay(RequestProperties reqState) {
		Account account = (reqState != null) ? reqState.getCurrentAccount() : null;
		if (account == null) {
			return false; // no account?
		} else {
			int dem = 0;
			DBCamera objcmr = new DBCamera();
			dem = objcmr.phanQuyen(account.getAccountID(), "BaoCaoDongMo");

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

	public String DungDo(int tg) {
		String chuoi = "";
		int phut = tg / 60;
		String h = "", m = "", s = "";

		if (phut < 15)
			chuoi = "Dừng";
		else
			chuoi = "Đỗ";

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

	public String ReportDungDo(String Account, String tuNgay, String denNgay, String Device, PrivateLabel privLabel,
			RequestProperties reqState, String timezone) throws IOException {
		int dem = 0;
		String strscr = "", chuoi = "", chuoi1 = "";
		record = 0;
		String mauNen = "";

		try {
			DBCamera objcmr = new DBCamera();
			// ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
			IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
			java.util.List<IDDescription> idList = reqState.createIDDescriptionList(false, sortBy);
			IDDescription list[] = idList.toArray(new IDDescription[idList.size()]);
			// strscr =strscr+
			// "<div id='scrollbar2'><div class='scrollbar'><div
			// class='track'><div class='thumb'><div
			// class='end'></div></div></div></div><div class='viewport'><div
			// class='overview'>";
			strscr += "<table  class='adminSelectTable_sortable' cellspacing='1' id='myTable' ><thead><tr  align='center'><th width='50px' class='adminTableHeaderCol_sort' >STT</th>"
					+ "<th width='110px' class='adminTableHeaderCol_sort'>Biển kiểm soát</th><th width='200px' class='adminTableHeaderCol_sort'>Họ tên lái xe</th>"
					+ "<th width='200px' class='adminTableHeaderCol_sort'>Số giấy <br/> phép lái xe</th><th width='100px' class='adminTableHeaderCol_sort'>Loại hình <br/> hoạt động</th>"
					+ "<th class='adminTableHeaderCol_sort'>Thời điểm<br/> (giờ, phút, ngày, tháng, năm)</th><th class='adminTableHeaderCol_sort'>Thời gian dừng đỗ <br/> (phút)</th><th class='adminTableHeaderCol_sort'>Tọa độ dừng đỗ</th><th class='adminTableHeaderCol_sort'>Địa điểm dừng đỗ</th><th class='adminTableHeaderCol_sort'>Ghi chú</th></tr></thead>";

			int count = 1;
			if (Device.equals("-1")) {

				// strscr = strscr + Device +" --- " + list.length;

				for (int d = 0; d < list.length; d++) {

					ArrayList<dungdo> listreport = objcmr.StaticReportDungDo(Account, list[d].getID(), tuNgay, denNgay,
							timezone);

					for (int k = 0; k < listreport.size(); k++) {
						String css = "";
						if (k % 2 == 0)
							css = "adminTableBodyRowOdd";
						else
							css = "adminTableBodyRowEven";

						strscr = strscr + "<tr class =" + css + "><td>" + count + "</td><td> "
								+ listreport.get(k).getdeviceID() + "</td><td>" + listreport.get(k).getHoTenLaiXe()
								+ "</td><td> " + listreport.get(k).getSoGiayPhepLaiXe() + "</td><td>"
								+ listreport.get(k).getLoaiHinhHoatDong() + "</td><td>"
								+ ConvertFromEpoch(listreport.get(k).gettimestamp(), timezone) + "</td><td>"
								+ doiGio(listreport.get(k).getduration()) + "</td><td>"
								+ listreport.get(k).getlatitude() + " - " + listreport.get(k).getlongitude()
								+ "</td><td>" + listreport.get(k).getaddress() + "</td><td>"
								+ listreport.get(k).getNote() + "</td></tr>";
						dem++;
						count++;
						record++;
					}
					strscr += "<tr style='font-weight:bold; border-right: 1px solid #E2E2E2; height:30px;color:black !important'><td colspan='6'>Xe "
							+ list[d].getID() + ", Tổng: </td><td colspan='4'>" + listreport.size() + "</td></tr>";
				}

			} else {
				ArrayList<dungdo> listreport = objcmr.StaticReportDungDo(Account, Device, tuNgay, denNgay, timezone);

				/*
				 * if (listreport.size() == 0) { strscr = strscr +
				 * "<tr class =adminTableBodyRowOdd>Không có dữ liệu</tr>"; }
				 * else
				 */
				for (int k = 0; k < listreport.size(); k++) {
					String css = "";
					if (k % 2 == 0)
						css = "adminTableBodyRowOdd";
					else
						css = "adminTableBodyRowEven";
					chuoi1 = chuoi1 + "<tr class =" + css + "><td>" + count + "</td><td> "
							+ listreport.get(k).getdeviceID() + "</td><td>" + listreport.get(k).getHoTenLaiXe()
							+ "</td><td> " + listreport.get(k).getSoGiayPhepLaiXe() + "</td><td>"
							+ listreport.get(k).getLoaiHinhHoatDong() + "</td><td>"
							+ ConvertFromEpoch(listreport.get(k).gettimestamp(), timezone) + "</td><td>"
							+ doiGio(listreport.get(k).getduration()) + "</td><td>" + listreport.get(k).getlatitude()
							+ " - " + listreport.get(k).getlongitude() + "</td><td>" + listreport.get(k).getaddress()
							+ "</td><td>" + listreport.get(k).getNote() + "</td></tr>";
					dem++;
					record++;
					count++;
				}

				strscr += chuoi1;
				strscr += "<tr style='font-weight:bold; border-right: 1px solid #E2E2E2; height:30px;color:black !important'><td colspan='6'>Xe "
						+ Device + ", Tổng: </td><td colspan='4'>" + listreport.size() + "</td></tr>";
			}

			strscr += "</table>";

		} catch (Exception e) {

		}
		return strscr;
	}

	// public String ReportDungDo(String Account, String tuNgay, String denNgay,
	// String Device, PrivateLabel privLabel, RequestProperties reqState,
	// String timezone) throws IOException {
	// int dem = 0;
	// String strscr = "", chuoi = "", chuoi1 = "";
	// record = 0;
	// String mauNen = "";
	//
	// try {
	// DBCamera objcmr = new DBCamera();
	// // ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
	// IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
	// java.util.List<IDDescription> idList = reqState
	// .createIDDescriptionList(false, sortBy);
	// IDDescription list[] = idList.toArray(new IDDescription[idList
	// .size()]);
	// // strscr =strscr+
	// // "<div id='scrollbar2'><div class='scrollbar'><div class='track'><div
	// class='thumb'><div class='end'></div></div></div></div><div
	// class='viewport'><div class='overview'>";
	// strscr = strscr
	// + "<table class='adminSelectTable_sortable' cellspacing='1' id='myTable'
	// ><thead><tr align='center'><th width='50px'
	// class='adminTableHeaderCol_sort' >STT</th><th width='110px'
	// class='adminTableHeaderCol_sort'>Lần dừng</th><th width='200px'
	// class='adminTableHeaderCol_sort'>Thời gian</th><th width='200px'
	// class='adminTableHeaderCol_sort'>Thời gian dừng đỗ</th><th width='100px'
	// class='adminTableHeaderCol_sort'>Dừng/đỗ</th><th
	// class='adminTableHeaderCol_sort'>Địa điểm dừng/đỗ</th></tr></thead>";
	// if (Device.equals("-1")) {
	// for (int d = 0; d < list.length; d++) {
	// ArrayList<dungdo> listreport = objcmr
	// .StaticReportDungDo(Account, list[d].getID(),
	// tuNgay, denNgay, timezone);
	// strscr = strscr
	// + "<tr style='height:30px;color:black'><td colspan='6' align='left'><b>"
	// + list[d].getID() + "</b></td></tr>";
	// if (listreport.size() == 0) {
	// strscr = strscr
	// + "<tr class =adminTableBodyRowOdd><td colspan='6' align='left'>Không có
	// dữ liệu</td></tr>";
	// }
	// for (int k = 0; k < listreport.size(); k++) {
	// String css = "";
	// if (k % 2 == 0)
	// css = "adminTableBodyRowOdd";
	// else
	// css = "adminTableBodyRowEven";
	//
	// strscr = strscr
	// + "<tr class ="
	// + css
	// + "><td>"
	// + (k + 1)
	// + "</td><td> "
	// + (k + 1)
	// + "</td><td>"
	// + ConvertFromEpoch(listreport.get(k)
	// .gettimestamp(), timezone)
	// + "</td><td>"
	// + doiGio(listreport.get(k).getduration())
	// + "</td><td>"
	// + DungDo(listreport.get(k).getduration())
	// + "</td><td>" + listreport.get(k).getaddress()
	// + "</td></tr>";
	// dem++;
	// record++;
	// }
	// }
	// } else {
	// ArrayList<dungdo> listreport = objcmr.StaticReportDungDo(
	// Account, Device, tuNgay, denNgay, timezone);
	// chuoi = "<tr style='height:30px;color:black'><td colspan='6'
	// align='left'><b>"
	// + Device + "</b></td></tr>";
	// for (int k = 0; k < listreport.size(); k++) {
	// String css = "";
	// if (k % 2 == 0)
	// css = "adminTableBodyRowOdd";
	// else
	// css = "adminTableBodyRowEven";
	// chuoi1 = chuoi1
	// + "<tr class ="
	// + css
	// + "><td>"
	// + (k + 1)
	// + "</td><td> "
	// + (k + 1)
	// + "</td><td>"
	// + ConvertFromEpoch(
	// listreport.get(k).gettimestamp(), timezone)
	// + "</td><td>"
	// + doiGio(listreport.get(k).getduration())
	// + "</td><td>"
	// + DungDo(listreport.get(k).getduration())
	// + "</td><td>" + listreport.get(k).getaddress()
	// + "</td></tr>";
	// dem++;
	// record++;
	// }
	//
	// strscr = strscr + chuoi + chuoi1;
	// }
	// strscr = strscr + "</table>";// </div></div></div>";
	// // }
	// } catch (Exception e) {
	//
	// }
	// return strscr;
	// }

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
		final I18N i18n = privLabel.getI18N(BaoCaoDungDo.class);
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
		// view submit
		// datefrom = AttributeTools.getRequestString(request, "datefrom", "");
		// dateto = AttributeTools.getRequestString(request, "dateto", "");
		// contentall = AttributeTools.getRequestString(request, "device", "");
		// tocdo = AttributeTools.getRequestString(request, "tocdo", "");
		// String sdevice = request.getParameter("device");

		if (excel != "") {
			if (excel.equals("Export Excel")) {
				java.util.Calendar c = java.util.Calendar.getInstance();
				c.add(java.util.Calendar.DAY_OF_YEAR, 0);
				Date now = c.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				String d = sdf.format(now);
				int dem = 0, num = 0;
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setHeader("Content-Disposition", "attachment; filename=baoCaoDungDo_" + d + ".xls");

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
				ct.setCellValue("BÁO CÁO DỪNG ĐỖ (" + contentall + ")");

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
				ngay.createCell((short) 2).setCellValue(datefrom);
				HSSFCell cdenNgay = ngay.createCell((short) 4);

				cdenNgay.setCellStyle(csNgay);
				cdenNgay.setCellValue("Đến ngày");
				ngay.createCell((short) 5).setCellValue(dateto);

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
				HSSFFont f = wb.createFont();
				f.setFontHeightInPoints((short) 10);
				// make it red
				// f.setColor((short) HSSFColor.RED.index);
				// make it bold
				// arial is the default font
				f.setBoldweight((short) f.BOLDWEIGHT_BOLD);
				cellStyle.setFont(f);

				HSSFRow rowhead = sheet.createRow((short) dem);
				// rowhead.createCell((short) 0).setCellValue("STT");
				HSSFCell h0 = rowhead.createCell((short) 0);
				h0.setCellStyle(cellStyle);
				h0.setCellValue("STT");
				HSSFCell h1 = rowhead.createCell((short) 1);
				h1.setCellStyle(cellStyle);
				h1.setCellValue("Biển kiểm soát");
				HSSFCell h2 = rowhead.createCell((short) 2);
				h2.setCellStyle(cellStyle);
				h2.setCellValue("Họ tên lái xe");
				HSSFCell h3 = rowhead.createCell((short) 3);
				h3.setCellStyle(cellStyle);
				h3.setCellValue("Số giấy phép lái xe");
				HSSFCell h4 = rowhead.createCell((short) 4);
				h4.setCellStyle(cellStyle);
				h4.setCellValue("Loại hình hoạt động");
				HSSFCell h5 = rowhead.createCell((short) 5);
				h5.setCellStyle(cellStyle);
				h5.setCellValue("Thời điểm dừng đỗ \n (giờ, phút, ngày, tháng, năm)");

				HSSFCell h6 = rowhead.createCell((short) 6);
				h6.setCellStyle(cellStyle);
				h6.setCellValue("Thời gian dừng đỗ \n (phút)");

				HSSFCell h7 = rowhead.createCell((short) 7);
				h7.setCellStyle(cellStyle);
				h7.setCellValue("Tọa độ dừng đỗ");

				HSSFCell h8 = rowhead.createCell((short) 8);
				h8.setCellStyle(cellStyle);
				h8.setCellValue("Địa điểm dừng đỗ");

				HSSFCell h9 = rowhead.createCell((short) 9);
				h9.setCellStyle(cellStyle);
				h9.setCellValue("Ghi chú");

				rowhead.setHeightInPoints((short) 40);
				DBCamera objcmr = new DBCamera();

				try {
					String deviceID = "";
					IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
					java.util.List<IDDescription> idList = reqState.createIDDescriptionList(false, sortBy);
					IDDescription list[] = idList.toArray(new IDDescription[idList.size()]);
					// strscr =strscr+
					// "<div id='scrollbar2'><div class='scrollbar'><div
					// class='track'><div class='thumb'><div
					// class='end'></div></div></div></div><div
					// class='viewport'><div class='overview'>";
					if (contentall.equals("-1")) {
						for (int d1 = 0; d1 < list.length; d1++) {
							ArrayList<dungdo> listreport = objcmr.StaticReportDungDo(currAcct.getAccountID(),
									list[d1].getID(), datefrom, dateto, currAcct.getTimeZone());
							dem++;
							HSSFRow row1 = sheet.createRow((short) dem);
							row1.setHeightInPoints(25);
							sheet.addMergedRegion(new CellRangeAddress(dem, dem, 0, 5));
							HSSFCellStyle csXe = wb.createCellStyle();
							csXe.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
							csXe.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

							HSSFFont fxe = wb.createFont();
							fxe.setFontHeightInPoints((short) 10);
							// make it red
							// f.setColor((short) HSSFColor.RED.index);
							// make it bold
							// arial is the default font
							fxe.setBoldweight((short) fxe.BOLDWEIGHT_BOLD);
							cellStyle.setFont(fxe);
							HSSFCell cellA1 = row1.createCell((short) 0);
							cellA1.setCellValue(list[d1].getID());
							cellA1.setCellStyle(csXe);

							// row1.setRowStyle(cellStyle);

							for (int k = 0; k < listreport.size(); k++) {
								dem++;
								num++;
								// chuoi1=chuoi1+"<tr class
								// ="+css+"><td>"+num+"</td><td>
								// "+rs.getString(6)+"</td><td>"+rs.getString(3)+"</td><td>"+doiGio(Integer.parseInt(
								// rs.getString(4)))+"</td><td>"+
								// DungDo(Integer.parseInt(rs.getString(4)))
								// +"</td><td>"+rs.getString(5)+"</td></tr>";

								HSSFCellStyle csr = wb.createCellStyle();
								csr.setBorderTop((short) 1);
								csr.setBorderRight((short) 1);
								csr.setBorderLeft((short) 1);
								csr.setBorderBottom((short) 1);

								HSSFRow row = sheet.createRow((short) (dem));
								HSSFCell r0 = row.createCell((short) 0);
								r0.setCellStyle(csr);
								r0.setCellValue(k + 1);

								HSSFCell r1 = row.createCell((short) 1);
								r1.setCellStyle(csr);
								r1.setCellValue(listreport.get(k).getdeviceID());

								HSSFCell r2 = row.createCell((short) 2);
								r2.setCellStyle(csr);
								r2.setCellValue(listreport.get(k).getHoTenLaiXe());
								HSSFCell r3 = row.createCell((short) 3);
								r3.setCellStyle(csr);
								r3.setCellValue(listreport.get(k).getSoGiayPhepLaiXe());
								HSSFCell r4 = row.createCell((short) 4);
								r4.setCellStyle(csr);
								r4.setCellValue(listreport.get(k).getLoaiHinhHoatDong());

								HSSFCell r5 = row.createCell((short) 5);
								r5.setCellStyle(csr);
								r5.setCellValue(
										ConvertFromEpoch(listreport.get(k).gettimestamp(), currAcct.getTimeZone()));

								HSSFCell r6 = row.createCell((short) 6);
								r6.setCellStyle(csr);
								r6.setCellValue(doiGio(listreport.get(k).getduration()));

								HSSFCell r7 = row.createCell((short) 7);
								r7.setCellStyle(csr);
								r7.setCellValue(
										listreport.get(k).getlatitude() + " - " + listreport.get(k).getlongitude());
								HSSFCell r8 = row.createCell((short) 8);
								r8.setCellStyle(csr);
								r8.setCellValue(NCRToUnicode(listreport.get(k).getaddress()));

								HSSFCell r9 = row.createCell((short) 9);
								r9.setCellStyle(csr);
								r9.setCellValue(listreport.get(k).getNote());
							}
						}
					} else {
						ArrayList<dungdo> listreport = objcmr.StaticReportDungDo(currAcct.getAccountID(), contentall,
								datefrom, dateto, currAcct.getTimeZone());
						dem++;
						HSSFRow row1 = sheet.createRow((short) dem);
						row1.setHeightInPoints(25);
						sheet.addMergedRegion(new CellRangeAddress(dem, dem, 0, 5));
						HSSFCellStyle csXe = wb.createCellStyle();
						csXe.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
						csXe.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

						HSSFFont fxe = wb.createFont();
						fxe.setFontHeightInPoints((short) 10);
						// make it red
						// f.setColor((short) HSSFColor.RED.index);
						// make it bold
						// arial is the default font
						fxe.setBoldweight((short) fxe.BOLDWEIGHT_BOLD);
						cellStyle.setFont(fxe);
						HSSFCell cellA1 = row1.createCell((short) 0);
						cellA1.setCellValue(contentall);
						cellA1.setCellStyle(csXe);
						// row1.setRowStyle(cellStyle);

						for (int k = 0; k < listreport.size(); k++) {
							dem++;
							num++;
							// chuoi1=chuoi1+"<tr class
							// ="+css+"><td>"+num+"</td><td>
							// "+rs.getString(6)+"</td><td>"+rs.getString(3)+"</td><td>"+doiGio(Integer.parseInt(
							// rs.getString(4)))+"</td><td>"+
							// DungDo(Integer.parseInt(rs.getString(4)))
							// +"</td><td>"+rs.getString(5)+"</td></tr>";

							HSSFCellStyle csr = wb.createCellStyle();
							csr.setBorderTop((short) 1);
							csr.setBorderRight((short) 1);
							csr.setBorderLeft((short) 1);
							csr.setBorderBottom((short) 1);

							HSSFRow row = sheet.createRow((short) (dem));
							HSSFCell r0 = row.createCell((short) 0);
							r0.setCellStyle(csr);
							r0.setCellValue(k + 1);
							HSSFCell r1 = row.createCell((short) 1);
							r1.setCellStyle(csr);
							r1.setCellValue(listreport.get(k).getdeviceID());

							HSSFCell r2 = row.createCell((short) 2);
							r2.setCellStyle(csr);
							r2.setCellValue(listreport.get(k).getHoTenLaiXe());
							HSSFCell r3 = row.createCell((short) 3);
							r3.setCellStyle(csr);
							r3.setCellValue(listreport.get(k).getSoGiayPhepLaiXe());
							HSSFCell r4 = row.createCell((short) 4);
							r4.setCellStyle(csr);
							r4.setCellValue(listreport.get(k).getLoaiHinhHoatDong());

							HSSFCell r5 = row.createCell((short) 5);
							r5.setCellStyle(csr);
							r5.setCellValue(ConvertFromEpoch(listreport.get(k).gettimestamp(), currAcct.getTimeZone()));

							HSSFCell r6 = row.createCell((short) 6);
							r6.setCellStyle(csr);
							r6.setCellValue(doiGio(listreport.get(k).getduration()));

							HSSFCell r7 = row.createCell((short) 7);
							r7.setCellStyle(csr);
							r7.setCellValue(listreport.get(k).getlatitude() + " - " + listreport.get(k).getlongitude());
							HSSFCell r8 = row.createCell((short) 8);
							r8.setCellStyle(csr);
							r8.setCellValue(NCRToUnicode(listreport.get(k).getaddress()));

							HSSFCell r9 = row.createCell((short) 9);
							r9.setCellStyle(csr);
							r9.setCellValue(listreport.get(k).getNote());
						}
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
					sheet.autoSizeColumn(9);

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
				String cssDir = BaoCaoDungDo.this.getCssDirectory();
				WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
				WebPageAdaptor.writeCssLink(out, reqState, "scrollbar.css", cssDir);
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
				// out.println(
				// " <script type='text/javascript'
				// src='js/jquery.tablesorter.min.js'></script>");
				// out.println("<script type='text/javascript'
				// src='js/sorttable.js'></script>");
				// out.println("<script type='text/javascript' >
				// $(function(){$('#myTable').tablesorter(); }); </script>");
			}
		};

		/* Content */

		HTMLOutput HTML_CONTENT = new HTMLOutput(CommonServlet.CSS_CONTENT_FRAME, m) {
			public void write(PrintWriter out) throws IOException {
				// Print.logStackTrace("here");
				String menuURL = privLabel.getWebPageURL(reqState, PAGE_MENU_TOP);
				String chgURL = privLabel.getWebPageURL(reqState, pageName, COMMAND_INFO_UPDATE);
				String frameTitle = i18n.getString("Camera.PageTitle", "BaoCaoDungDo");
				HttpServletRequest request = reqState.getHttpServletRequest();
				String contentall = "";
				String datefrom = "homnay";
				String dateto = "homnay";
				String flag = "0";

				// view submit

				datefrom = AttributeTools.getRequestString(request, "datefrom", "");
				dateto = AttributeTools.getRequestString(request, "dateto", "");
				contentall = AttributeTools.getRequestString(request, "device", "");

				String sql = ReportDungDo(currAcct.getAccountID(), datefrom, dateto, contentall, privLabel, reqState,
						currAcct.getTimeZone());

				java.util.Calendar c = java.util.Calendar.getInstance();
				c.add(java.util.Calendar.DAY_OF_YEAR, -1);
				Date now = c.getTime();

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String d = sdf.format(now);

				out.println("<span class='" + CommonServlet.CSS_MENU_TITLE + "'>Báo cáo dừng đỗ</span><br/>");
				out.println("<hr/>");
				out.println("<form name='baocaodungdo' method='post' action='" + chgURL + "' target='_self'>\n");

				out.println("<table class='" + CommonServlet.CSS_ADMIN_VIEW_TABLE
						+ "' cellspacing='0' callpadding='0' border='0'  style='padding:15px 0 15px'>\n");

				out.print("<tr style='height:40px;'>\n");
				out.print("<td class='" + CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER
						+ "' width='100px' >T&#x1EEB; ng&#x00E0;y:</td>\n");
				out.print("<td class='" + CommonServlet.CSS_ADMIN_VIEW_TABLE_DATA + "' >\n");
				out.print(
						"<input id='datefrom' class='textReadOnly' name='datefrom' type='text' style='width:120px' onclick=\"displayCalendar(this,'dd/mm/yyyy hh:ii',this,true,'','datefrom')\" value='"
								+ d + " 00:00' />");
				if (datefrom != "") {
					out.print(
							"<script language ='javascript' type ='text/javascript'>document.getElementById('datefrom').value ='"
									+ datefrom + "'; </script>");
				}
				out.print("</td>");

				// out.print("</tr>");

				// out.print("<tr style='height:40px'>");
				out.print("<td class='" + CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER
						+ "' width='100px'  >&#x0110;&#x1EBF;n ng&#x00E0;y:</td>\n");
				out.print("<td>");
				out.print(
						"<input id='dateto' class='textReadOnly' name='dateto' type='text' style='width:120px' onclick=\"displayCalendar(this,'dd/mm/yyyy hh:ii',this,true,'','dateto')\" value='"
								+ d + " 23:59' />");
				if (dateto != "") {
					out.print(
							"<script language ='javascript' type ='text/javascript'>document.getElementById('dateto').value ='"
									+ dateto + "'; </script>");
				}
				out.print("</td>");

				out.print("<td align='right' width='100px'><span style='margin-left: 10px;margin-right:5px;'>"
						+ i18n.getString("DeviceSelect", "ch&#x1ECD;n xe:") + "</span></td><td>\n");
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