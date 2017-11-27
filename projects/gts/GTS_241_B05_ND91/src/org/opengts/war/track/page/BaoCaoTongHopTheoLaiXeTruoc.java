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
import java.sql.SQLException;
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
import java.text.DecimalFormat;
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

public class BaoCaoTongHopTheoLaiXeTruoc extends WebPageAdaptor implements Constants {

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

	public BaoCaoTongHopTheoLaiXeTruoc() {
		this.setBaseURI(RequestProperties.TRACK_BASE_URI());
		this.setPageName(PAGE_MENU_RPT_BAOCAOTONGHOPTHEOLAIXETRUOC);
		this.setPageNavigation(new String[] { PAGE_LOGIN, PAGE_MENU_TOP });
		this.setLoginRequired(true);
		// this.setReportType(ReportFactory.REPORT_TYPE_DEVICE_DETAIL);
	}

	// ------------------------------------------------------------------------

	public String getMenuName(RequestProperties reqState) {
		return MenuBar.PAGE_MENU_RPT_BAOCAOTONGHOPTHEOLAIXETRUOC;
	}

	public String getMenuDescription(RequestProperties reqState,
			String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaoCaoTongHopTheoLaiXeTruoc.class);
		String devTitles[] = reqState.getDeviceTitles();
		return super._getMenuDescription(reqState,"Tổng hợp trước - Báo cáo tổng hợp theo lái xe");
	}

	public String getMenuHelp(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaoCaoTongHopTheoLaiXeTruoc.class);
		String devTitles[] = reqState.getDeviceTitles();
		return super._getMenuHelp(reqState, "Tổng hợp trước - Báo cáo tổng hợp theo lái xe");
	}

	// ------------------------------------------------------------------------

	public String getNavigationDescription(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaoCaoTongHopTheoLaiXeTruoc.class);
		String devTitles[] = reqState.getDeviceTitles();
		return super._getNavigationDescription(reqState,"Tổng hợp trước - Báo cáo tổng hợp theo lái xe");
	}

	public String getNavigationTab(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaoCaoTongHopTheoLaiXeTruoc.class);
		String devTitles[] = reqState.getDeviceTitles();
		return super._getNavigationTab(reqState, "Tổng hợp trước - Báo cáo tổng hợp theo lái xe");
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
			dem = objcmr.phanQuyen(account.getAccountID(), "baoCaotgLaiXe");

			if (dem > 0)
				return false;
			else
				return true;

		}
	}

	public String doiGio(int tg) {
		String chuoi = "";
		// int gio = tg / 3600;
		int phut = (tg) / 60;
		// int giay = tg - gio * 3600 - phut * 60;
		String h = "", m = "", s = "";
		// if (gio < 10)
		// h = "0" + Integer.toString(gio);
		// else
		// h = Integer.toString(gio);
		if (phut < 10)
			m = "0" + Integer.toString(phut);
		else
			m = Integer.toString(phut);
		// if (giay < 10)
		// s = "0" + Integer.toString(giay);
		// else
		// s = Integer.toString(giay);
		return chuoi = m + " phút";

	}

	public String CreateCbbDriver(String accountid, String idselect,
			RequestProperties reqState, PrivateLabel privLabel)
			throws IOException {

		DBCamera objcmr = new DBCamera();

		ResultSet data = objcmr.selectDriver(accountid);

		String strre = "<select id ='device' name = 'device' class='textReadOnly' style='width:150px;'>";
		if (idselect == "-1") {
			strre += "<option value ='-1' selected =\"selected\">Tất cả</option>\n";
		} else {
			strre += "<option value ='-1'>Tất cả</option>\n";
		}

		try {
			while (data.next()) {
				if (data.getString("deviceID") != "") {
					if (idselect.equalsIgnoreCase(data.getString("driverID")))
						strre += "<option value ='"
								+ data.getString("driverID")
								+ "' selected =\"selected\">"
								+ data.getString("displayName") + " ("
								+ (data.getString("driverID")) + ")"
								+ "</option>\n";
					else
						strre += "<option value ='"
								+ data.getString("driverID") + "'>"
								+ data.getString("displayName") + " ("
								+ (data.getString("driverID")) + ")"
								+ "</option>\n";
				}
			}

		} catch (SQLException ex) {
			strre +=ex.getMessage();
		}

		strre += "</select>\n";
		strre += "<script type ='text/javascript' language ='javascript'> document.getElementById('device ').value = '"
				+ idselect + "';</script>\n";
		return strre;
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

	private String ConvertFromEpochHour(long epoch, String timezone) {
		TimeZone tz = DateTime.getTimeZone(timezone, null);
		DateTime dt = new DateTime(epoch);
		String dtFmt = dt.format("dd/MM/yyyy", tz);
		return dtFmt;
	}

	private String ConvertFromEpochTime(long epoch, String timezone) {
		TimeZone tz = DateTime.getTimeZone(timezone, null);
		DateTime dt = new DateTime(epoch);
		String dtFmt = dt.format(" HH:mm dd/MM/yyyy", tz);
		return dtFmt;
	}

	public String LoadReport(String accid, String tungay, String denngay,
			String device, PrivateLabel privLabel, RequestProperties reqState,
			String timezone, String laixelientuc) throws IOException {

		String strscr = "";
		ArrayList<TongHopTheoLaiXe> rs = null;
		try {
			int count = 0;
			record = 0;
			DBCamera objcmr = new DBCamera();
			strscr = strscr
					+ "<table  width='100%' class='adminSelectTable_sortable' cellspacing='1' ><thead>"
					+ "<tr  align='center'>"
					+ "<th class='adminTableHeaderCol_sort' rowspan='2'>TT</th>"
					+ "<th class='adminTableHeaderCol_sort' rowspan='2'>Họ tên lái xe</th>"
					+ "<th  class='adminTableHeaderCol_sort' rowspan='2'>Số Giấy phép lái xe</th><th  class='adminTableHeaderCol_sort' rowspan='2'>Tổng Km</th>"
					+ "<th colspan='4' class='adminTableHeaderCol_sort'>Tỷ lệ km quá tốc độ/tổng <br/>km (%)</th><th colspan='6' class='adminTableHeaderCol_sort'>Tổng số lần quá tốc độ <br/>(lần)</th><th class='adminTableHeaderCol_sort' rowspan='2'>Tổng số lần lái xe <br/>liên tục quá 04 giờ</th><th class='adminTableHeaderCol_sort' rowspan='2'>Tổng số lần lái xe <br/>liên tục quá 10 giờ</th><th class='adminTableHeaderCol_sort' rowspan='2'>Ghi chú</th>"
					+ "</tr><tr>"
					+ "<th  class='adminTableHeaderCol_sort'>Tỷ lệ quá tốc độ từ<br/> 5 km/h đến dưới 10 km/h</th><th class='adminTableHeaderCol_sort'>Tỷ lệ quá tốc độ từ <br/> 10 km/h đến dưới 20 km/h</th>"
					+ "<th  class='adminTableHeaderCol_sort'>Tỷ lệ quá tốc độ từ<br/> 20  đến  35 km/h</th><th  class='adminTableHeaderCol_sort'>Tỷ lệ quá tốc độ trên 35 km/h</th>"
					+ "<th class='adminTableHeaderCol_sort'>Số lần quá tốc độ từ<br/> 5 km/h đến dưới 10 km/h</th><th class='adminTableHeaderCol_sort'>Số lần quá tốc độ từ<br/> 10 km/h đến dưới 20 km/h</th><th class='adminTableHeaderCol_sort'>Số lần quá tốc độ <br/> từ 20  đến 35 km/h</th><th class='adminTableHeaderCol_sort'>Số lần quá tốc độ <br/> trên 35 km/h</th>"
					+ "<th  class='adminTableHeaderCol_sort'>Tổng cộng</th><th  class='adminTableHeaderCol_sort'>Tỷ lệ % km <br/> vi phạm/km xe chạy</th>"
					+ "</tr></thead>";

			rs = objcmr.BCTongHopTheoLaiXeTruoc(accid, device, tungay, denngay,
					timezone);
			String css = "";
			DecimalFormat df = new DecimalFormat("#.#");

			for (int j = 0; j < rs.size(); j++) {
				try {
					count++;
					if (count % 2 == 0)
						css = "adminTableBodyRowOdd";
					else
						css = "adminTableBodyRowEven";

					double tongKM = Double.parseDouble(rs.get(j).TongKM);
					long count510 = Math
							.round(Double.parseDouble(rs.get(j).KM510)), count1020 = Math
							.round(Double.parseDouble(rs.get(j).KM1020)), count2035 = Math
							.round(Double.parseDouble(rs.get(j).KM2035)), count35 = Math
							.round(Double.parseDouble(rs.get(j).KM35));

					strscr = strscr
							+ "<tr class ="
							+ css
							+ "><td>"
							+ count
							+ "</td><td>"
							+ rs.get(j).displayName
							+ "</td>"
							+ "<td>"
							+ rs.get(j).driverLC
							+ "</td>"
							+ "<td>"
							+ df.format(tongKM)
							+ "</td>"
							+ "<td>"
							+ (tongKM == 0 ? 0 : df
									.format((count510 / tongKM) * 100))
							+ "</td>"
							+ "<td>"
							+ (tongKM == 0 ? 0 : df
									.format((count1020 / tongKM) * 100))
							+ "</td>"
							+ "<td>"
							+ (tongKM == 0 ? 0 : df
									.format((count2035 / tongKM) * 100))
							+ "</td>"
							+ "<td>"
							+ (tongKM == 0 ? 0 : df
									.format((count35 / tongKM) * 100))
							+ "</td>"
							+ "<td>"
							+ rs.get(j).Count510
							+ "</td>"
							+ "<td>"
							+ rs.get(j).Count1020
							+ "</td>"
							+ "<td>"
							+ rs.get(j).Count2035
							+ "</td>"
							+ "<td>"
							+ rs.get(j).Count35
							+ "</td>"
							+ "<td>"
							+ (Integer.parseInt(rs.get(j).Count510)
									+ Integer.parseInt(rs.get(j).Count1020)
									+ Integer.parseInt(rs.get(j).Count2035) + Integer
										.parseInt(rs.get(j).Count35)) + "</td>"
							+ "<td>" + 0 + "</td>" + "<td>"
							+ rs.get(j).LienTuc4Gio + "</td>" + "<td>"
							+ rs.get(j).LienTuc10Gio + "</td>" + "<td>" + ""
							+ "</td></tr>";
				} catch (Exception e) {
					strscr += e.getMessage();
				}
			}

			strscr = strscr + "</table>";

		} catch (Exception e) {
			strscr += e.getMessage() + " " + rs.size();
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
		String contentall = AttributeTools.getRequestString(request, "device",
				"");

		String laixelientuc = AttributeTools.getRequestString(request,
				"laixelientuc", "");

		if (excel.equals("Export Excel")) {
			int num = 0, i = 0;
			java.util.Calendar c = java.util.Calendar.getInstance();
			Date now = c.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String d3 = sdf.format(now);
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=baoCaoTongHopTheoLaiXe" + d3 + ".xls");
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
			ct.setCellValue("BÁO CÁO TỔNG HỢP THEO XE (" + xe + ")");
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
			cTuNgay.setCellValue("Từ ngày");
			rngay.createCell((short) 2).setCellValue(tuNgay);
			HSSFCell cdenNgay = rngay.createCell((short) 4);
			cdenNgay.setCellStyle(csNgay);
			cdenNgay.setCellValue("Đến ngày");
			rngay.createCell((short) 5).setCellValue(denNgay);

			i = i + 3;

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

			HSSFRow rowhead = sheet.createRow((short) i);
			HSSFCell h0 = rowhead.createCell((short) 0);
			h0.setCellStyle(cellStyle);
			h0.setCellValue("Họ tên lái xe");

			HSSFCell h1 = rowhead.createCell((short) 1);
			h1.setCellStyle(cellStyle);
			h1.setCellValue("Số Giấy phép lái xe");

			HSSFCell h2 = rowhead.createCell((short) 2);
			h2.setCellStyle(cellStyle);
			h2.setCellValue("Tổng km");

			HSSFCell h3 = rowhead.createCell((short) 3);
			h3.setCellStyle(cellStyle);
			h3.setCellValue("Tỷ lệ quá tốc độ từ 5 km/h đến dưới 10 km/h");

			HSSFCell h4 = rowhead.createCell((short) 4);
			h4.setCellStyle(cellStyle);
			h4.setCellValue("Tỷ lệ quá tốc độ từ  10 km/h đến dưới 20 km/h");

			HSSFCell h5 = rowhead.createCell((short) 5);
			h5.setCellStyle(cellStyle);
			h5.setCellValue("Tỷ lệ quá tốc độ từ 20  đến  35 km/h");

			HSSFCell h6 = rowhead.createCell((short) 6);
			h6.setCellStyle(cellStyle);
			h6.setCellValue("Tỷ lệ quá tốc độ trên 35 km/h");

			HSSFCell h7 = rowhead.createCell((short) 7);
			h7.setCellStyle(cellStyle);
			h7.setCellValue("Số lần quá tốc độ từ 5 km/h đến dưới 10 km/h");

			HSSFCell h8 = rowhead.createCell((short) 8);
			h8.setCellStyle(cellStyle);
			h8.setCellValue("Số lần quá tốc độ từ 10 km/h đến dưới 20 km/h");

			HSSFCell h9 = rowhead.createCell((short) 9);
			h9.setCellStyle(cellStyle);
			h9.setCellValue("Số lần quá tốc độ từ 20  đến 35 km/h");

			HSSFCell h10 = rowhead.createCell((short) 10);
			h10.setCellStyle(cellStyle);
			h10.setCellValue("Số lần quá tốc độ trên 35 km/h");

			HSSFCell h11 = rowhead.createCell((short) 11);
			h11.setCellStyle(cellStyle);
			h11.setCellValue("Tổng số lần lái xe liên tục quá 04 giờ");

			HSSFCell h12 = rowhead.createCell((short) 12);
			h12.setCellStyle(cellStyle);
			h12.setCellValue("Tổng số lần lái xe liên tục quá 10 giờ");

			HSSFCell h13 = rowhead.createCell((short) 13);
			h13.setCellStyle(cellStyle);
			h13.setCellValue("Ghi chú");

			rowhead.setHeightInPoints((short) 40);
			try {
				HSSFCellStyle csr = wb.createCellStyle();
				csr.setBorderTop((short) 1);
				csr.setBorderRight((short) 1);
				csr.setBorderLeft((short) 1);
				csr.setBorderBottom((short) 1);

				ArrayList<TongHopTheoLaiXe> listreport = null;
				DBCamera objcmr = new DBCamera();
				listreport = objcmr.BCTongHopTheoLaiXe(currAcct.getAccountID(),
						contentall, tuNgay, denNgay, currAcct.getTimeZone());
				String css = "";
				DecimalFormat df = new DecimalFormat("#0.#");

				for (int k = 0; k < listreport.size(); k++) {
				 
					double tongKM = Double
							.parseDouble(listreport.get(k).TongKM);
					long count510 = Math.round(Double.parseDouble(listreport
							.get(k).KM510)), count1020 = Math.round(Double
							.parseDouble(listreport.get(k).KM1020)), count2035 = Math
							.round(Double.parseDouble(listreport.get(k).KM2035)), count35 = Math
							.round(Double.parseDouble(listreport.get(k).KM35));

					HSSFRow row = sheet.createRow((short) (i + 1));
					HSSFCell r0 = row.createCell((short) 0);
					r0.setCellStyle(csr);
					r0.setCellValue(listreport.get(k).displayName);

					HSSFCell r1 = row.createCell((short) 1);
					r1.setCellStyle(csr);
					r1.setCellValue(listreport.get(k).driverLC);

					HSSFCell r2 = row.createCell((short) 2);
					r2.setCellStyle(csr);
					r2.setCellValue(df.format(tongKM));

					HSSFCell r3 = row.createCell((short) 3);
					r3.setCellStyle(csr);
					r3.setCellValue((tongKM == 0 ? 0 : df
							.format((count510 / tongKM) * 100)) + "");

					HSSFCell r4 = row.createCell((short) 4);
					r4.setCellStyle(csr);
					r4.setCellValue((tongKM == 0 ? 0 : df
							.format((count1020 / tongKM) * 100)) + "");

					HSSFCell r5 = row.createCell((short) 5);
					r5.setCellStyle(csr);
					r5.setCellValue((tongKM == 0 ? 0 : df
							.format((count2035 / tongKM) * 100)) + "");

					HSSFCell r6 = row.createCell((short) 6);
					r6.setCellStyle(csr);
					r6.setCellValue((tongKM == 0 ? 0 : df
							.format((count35 / tongKM) * 100)) + "");

					HSSFCell r7 = row.createCell((short) 7);
					r7.setCellStyle(csr);
					r7.setCellValue(listreport.get(k).Count510);

					HSSFCell r8 = row.createCell((short) 8);
					r8.setCellStyle(csr);
					r8.setCellValue(listreport.get(k).Count1020);
					HSSFCell r9 = row.createCell((short) 9);
					r9.setCellStyle(csr);
					r9.setCellValue(listreport.get(k).Count2035);
					HSSFCell r10 = row.createCell((short) 10);
					r10.setCellStyle(csr);
					r10.setCellValue(listreport.get(k).Count35);
					HSSFCell r11 = row.createCell((short) 11);
					r11.setCellStyle(csr);
					r11.setCellValue((Integer.parseInt(listreport.get(k).Count510)
							+ Integer.parseInt(listreport.get(k).Count1020)
							+ Integer.parseInt(listreport.get(k).Count2035) + Integer
							.parseInt(listreport.get(k).Count35)));
					HSSFCell r12 = row.createCell((short) 12);
					r12.setCellStyle(csr);
					r12.setCellValue(listreport.get(k).LienTuc4Gio);
					HSSFCell r13 = row.createCell((short) 13);
					r13.setCellStyle(csr);
					r13.setCellValue(listreport.get(k).LienTuc10Gio);
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
				sheet.autoSizeColumn(9);
				sheet.autoSizeColumn(10);
				sheet.autoSizeColumn(11);
				sheet.autoSizeColumn(12);
				sheet.autoSizeColumn(13);
				OutputStream out = response.getOutputStream();
				wb.write(out);
				out.close();

			} catch (Exception e) {

			}
			return;
		}

		/* Style */
		HTMLOutput HTML_CSS = new HTMLOutput() {
			public void write(PrintWriter out) throws IOException {
				String cssDir = BaoCaoTongHopTheoLaiXeTruoc.this.getCssDirectory();
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
				out.println("<script> $( document ).ready(function() { $('#running').remove();}) </script>");
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

				// frame content
				// view submit
				HttpServletRequest request = reqState.getHttpServletRequest();
				String tuNgay = AttributeTools.getRequestString(request,
						"tuNgay", "");
				String contentall = "";
				String denNgay = AttributeTools.getRequestString(request,
						"denNgay", "");
				// HttpServletRequest request =
				// reqState.getHttpServletRequest();
				String xem = AttributeTools.getRequestString(request,
						"btnview", "");
				contentall = AttributeTools.getRequestString(request, "device",
						"");
				String laixelientuc = AttributeTools.getRequestString(request,
						"laixelientuc", "");

				 
				java.util.Calendar c = java.util.Calendar.getInstance();
				c.add(java.util.Calendar.DAY_OF_YEAR, -1);
				Date now = c.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String d = sdf.format(now);
				out.println("<span class='"
						+ CommonServlet.CSS_MENU_TITLE
						+ "' style='text-align:center;'>BÁO CÁO TỔNG HỢP THEO LÁI XE TRƯỚC</span><br/>");
				out.println("<hr/>");
				out.println("<form name='AccountInfo' method='post' action='"
						+ chgURL + "' target='_self'>\n");

				out.println("<table class='"
						+ CommonServlet.CSS_ADMIN_VIEW_TABLE
						+ "' cellspacing='0' callpadding='0' border='0' width='100%' style='padding:15 0 15'>\n");

				out.print("<tr>\n");
				out.print("<td width='80px' align='right'>T&#x1EEB; ng&#x00E0;y:</td>\n");
				out.print("<td width='10%' align='left'>\n");
				out.print("<input id='Text1' name='tuNgay' type='text' style='width:120px' class='textReadOnly' onclick=\"displayCalendar(this,'dd/mm/yyyy',this,false,'','Text1')\" value='"
						+ d + "' /></td>");
				if (tuNgay != "") {
					out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('Text1').value ='"
							+ tuNgay + "'; </script>");
				}
				out.print("<td width='80px' align='right'>&#x0110;&#x1EBF;n ng&#x00E0;y:</td>\n");
				out.print("<td width='100px' align='left'>\n");
				out.print("<input id='Text2' name='denNgay' type='text' class='textReadOnly' style='width:120px' onclick=\"displayCalendar(this,'dd/mm/yyyy',this,false,'','Text2')\" value='"
						+ d + "' /></td>");
				if (denNgay != "") {
					out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('Text2').value ='"
							+ denNgay + "'; </script>");
				}

				out.print("<td align='right' width='50px'><span style='margin-left: 10px;margin-right:5px;'>"
						+ i18n.getString("DeviceSelect", "ch&#x1ECD;n lái xe:")
						+ "</span></td><td width='100px'>\n");

				out.print(CreateCbbDriver(currAcct.getAccountID(), contentall,
						reqState, privLabel));

				out.print("</td>");

				out.println("</tr>");
				out.println("</table>");
				out.print("<table cellspacing='0' style='width:100%;padding:10px 0px 10px'><tbody><tr class='viewhoz'><td width='80px'></td><td width='100px'> <input type='submit' class='button1' name='btnview' value='Tổng hợp' id='btnview'></td><td width='205px'></td>");
				out.print("<td align='left' ></td>");
				out.print("</tr></tbody></table>");
				
				if (xem.equals("Tổng hợp")) {
					out.println("<div id='running' style='padding:7px;font-weight:bold'>Đang tổng hợp dữ liệu...<br/> Vui lòng chờ trong giây lát.</div>");
					out.print( LoadReport(currAcct.getAccountID(), tuNgay,
							denNgay, contentall, privLabel, reqState,
							currAcct.getTimeZone(), laixelientuc));
				}
				
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
