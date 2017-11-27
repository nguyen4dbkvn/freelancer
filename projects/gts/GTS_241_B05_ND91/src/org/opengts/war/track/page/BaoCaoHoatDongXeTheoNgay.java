package org.opengts.war.track.page;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.opengts.db.tables.Account;
import org.opengts.db.tables.DrivingTime;
import org.opengts.db.tables.EventRunStop;
import org.opengts.db.tables.StatusCode;
import org.opengts.db.tables.User;
import org.opengts.dbtools.DBCamera;
import org.opengts.util.DateTime;
import org.opengts.util.I18N;
import org.opengts.war.tools.AttributeTools;
import org.opengts.war.tools.CommonServlet;
import org.opengts.war.tools.HTMLOutput;
import org.opengts.war.tools.IDDescription;
import org.opengts.war.tools.MenuBar;
import org.opengts.war.tools.PrivateLabel;
import org.opengts.war.tools.RequestProperties;
import org.opengts.war.tools.WebPageAdaptor;
import org.opengts.war.track.Constants;
import org.opengts.war.track.DeviceChooser;

public class BaoCaoHoatDongXeTheoNgay extends WebPageAdaptor implements Constants {
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

	// create usedFuel object
	// reportFuel usedFuel = new reportFuel();
	//
	// public static String AccountID;
	// public static String DeviceID;
	// public static long time;
	// public static double fuel;

	// ------------------------------------------------------------------------
	// WebPage interface
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// Reports: "device.detail"
	// - Event Detail
	// - Temperature Monitoring
	// - J1708 Fault codes

	public BaoCaoHoatDongXeTheoNgay() {
		this.setBaseURI(RequestProperties.TRACK_BASE_URI());
		this.setPageName(PAGE_BAOCAOHOATDONGXETHEONGAY);
		this.setPageNavigation(new String[] { PAGE_LOGIN, PAGE_MENU_TOP });
		this.setLoginRequired(true);
		// this.setReportType(ReportFactory.REPORT_TYPE_DEVICE_DETAIL);
	}

	// ------------------------------------------------------------------------

	public String getMenuName(RequestProperties reqState) {
		return MenuBar.PAGE_BAOCAOHOATDONGXETHEONGAY;
	}

	public String getMenuDescription(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaoCaoHoatDongXeTheoNgay.class);
		String devTitles[] = reqState.getDeviceTitles();
		return super._getMenuDescription(reqState, i18n.getString("ReportMenuBaoCaoHoatDongCuaXeTheoNgay.menuDesc",
				"{0} Detail Reports", "Ho\u1EA1t \u0110\u1ED9ng Xe Theo Ngày"));
	}

	public String getMenuHelp(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaoCaoHoatDongXeTheoNgay.class);
		String devTitles[] = reqState.getDeviceTitles();
		return super._getMenuHelp(reqState, i18n.getString("ReportMenuBaoCaoHoatDongCuaXeTheoNgay.menuHelp",
				"Display various {0} detail reports", "Ho\u1EA1t \u0110\u1ED9ng Xe Theo Ngày"));
	}

	// ------------------------------------------------------------------------

	public String getNavigationDescription(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaoCaoHoatDongXeTheoNgay.class);
		String devTitles[] = reqState.getDeviceTitles();
		return super._getNavigationDescription(reqState, i18n.getString("ReportMenuBaoCaoHoatDongCuaXeTheoNgay.navDesc",
				"{0}", "Ho\u1EA1t \u0110\u1ED9ng Xe Theo Ngày"));
	}

	public String getNavigationTab(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(BaoCaoHoatDongXeTheoNgay.class);
		String devTitles[] = reqState.getDeviceTitles();
		return super._getNavigationTab(reqState, i18n.getString("ReportMenuBaoCaoHoatDongCuaXeTheoNgay.navTab", "{0}",
				"Ho\u1EA1t \u0110\u1ED9ng C\u1EE7a Xe Theo Ngày"));
	}

	// ------------------------------------------------------------------------

	public boolean isOkToDisplay(RequestProperties reqState) {
		Account account = (reqState != null) ? reqState.getCurrentAccount() : null;
		if (account == null) {
			return false; // no account?
		} else {
			int dem = 0;
			DBCamera objcmr = new DBCamera();
			dem = objcmr.phanQuyen(account.getAccountID(), "HoatDongCuaXeTheoNgay");

			if (dem > 0)
				return false;
			else
				return true;

		}
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
			chuoi = h + "h ";
		if (phut > 0)
			chuoi += m + "m ";
		if (giay > 0)
			chuoi += s + "s ";
		return chuoi;

	}

	public String CreateCbbDevice(String accountid, String idselect, RequestProperties reqState, PrivateLabel privLabel)
			throws IOException {
		IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
		java.util.List<IDDescription> idList = reqState.createIDDescriptionList(false, sortBy);
		IDDescription list[] = idList.toArray(new IDDescription[idList.size()]);

		String strre = "<select id ='device' name = 'device' class='textReadOnly' style='width:100px;'>";

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
		String dtFmt = dt.format("HH:mm:ss", tz);
		return dtFmt;
	}

	public String LoadReport(String accid, String ngay, String device, PrivateLabel privLabel,
			RequestProperties reqState, String timezone) throws IOException {

		String strscr = "";

		try {
			int num = 0, i = 0, count = 0;
			record = 0;
			DBCamera objcmr = new DBCamera();
			// mr viet tong hop truoc
			strscr = strscr
					+ "<table id='myTable' width='100%' class='adminSelectTable_sortable' cellspacing='1' ><thead>"
					+ "<th class='adminTableHeaderCol_sort'>Thời Điểm</th>"
					+ "<th class='adminTableHeaderCol_sort' width='120px'>Trạng Thái</th>"
					+ "<th class='adminTableHeaderCol_sort'>Tốc độ TB</th>"
					+ "<th class='adminTableHeaderCol_sort' width='80px'>Tốc độ<br/>Tối đa</th>"
					+ "<th class='adminTableHeaderCol_sort'>Quãng Đường</th>"
					+ "<th  class='adminTableHeaderCol_sort'>Địa điểm Bắt đầu</th>"
					+ "<th class='adminTableHeaderCol_sort'>Địa điểm Kết thúc</th>"
					+ "<th class='adminTableHeaderCol_sort'>Tọa độ</th>" + "</tr></thead>";
			ArrayList<EventRunStop> listreport = objcmr.BChoatdongxetheongay(accid, device, ngay, timezone);

			for (int t = 0; t < listreport.size(); t++) {
				String css = "";
				if (num % 2 == 0)
					css = "adminTableBodyRowOdd1";
				else
					css = "adminTableBodyRowEven1";
				String css1 = "adminTableBodyCol";

				num++;
				i++;
				count++;
				String tt = "";
				if (listreport.get(t).getStatusCode() == 0) {
					tt += "Bắt Đầu";
				}
				if (listreport.get(t).getStatusCode() == 61472) {
					tt += "Dừng: ";
				}
				if (listreport.get(t).getStatusCode() == 61714) {
					tt += "<b>Chạy:</b> ";
				}
				if (listreport.get(t).getStatusCode() == 90000) {
					tt += "Mất GPS: ";
				}
				if (listreport.get(t).getStatusCode() == 90001) {
					tt += "Không Có DL: ";
				}
				if (listreport.get(t).getStatusCode() == 99998 || listreport.get(t).getStatusCode() == 99999) {
					tt += "Kết Thúc";
				}
				if (listreport.get(t).getStatusLastingTime() > 0 && listreport.get(t).getStatusCode() != 0
						&& listreport.get(t).getStatusCode() != 99998 && listreport.get(t).getStatusCode() != 99999)
					tt = tt + " " + doiGio(listreport.get(t).getStatusLastingTime());
				String thoidiem = ConvertFromEpochTime(listreport.get(t).getTimestamp(), timezone);
				strscr = strscr + "<tr class =" + css + ">" + "<td>" + thoidiem + "</td>" + "<td>" + tt + "</td>"
						+ "<td class=" + css1 + ">" + listreport.get(t).getAvgSpeedKPH() + "</td>" + "<td class=" + css1
						+ ">" + listreport.get(t).getMaxSpeedKPH() + "</td>" + "<td class=" + css1 + ">"
						+ round(listreport.get(t).getOdometerKM(), 2) + "</td>" + "<td>"
						+ listreport.get(t).getAddress() + "</td>" + "<td>" + listreport.get(t).getAddress1() + "</td>"
						+ "<td>" + round(listreport.get(t).getLatitude(), 5) + "/"
						+ round(listreport.get(t).getLongitude(), 5) + "<br />"
						+ round(listreport.get(t).getLatitude1(), 5) + "/" + round(listreport.get(t).getLongitude1(), 5)
						+ "</td>" + "</tr>";
			}
			// }

			strscr = strscr + "</table>";

		} catch (Exception e) {
			strscr = strscr + e.toString();
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

	public void writePage(final RequestProperties reqState, String pageMsg) throws IOException {
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
		String ngay = AttributeTools.getRequestString(request, "ngay", "");
		String contentall = AttributeTools.getRequestString(request, "device", "");

		if (excel.equals("Export Excel")) {
			int num = 0, i = 0;
			java.util.Calendar c = java.util.Calendar.getInstance();
			Date now = c.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String d3 = sdf.format(now);
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=HoatDongXeTheoNgay_" + d3 + ".xls");
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
			ct.setCellValue("HOẠT ĐỘNG XE THEO NGÀY (" + xe + ")");
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
			cellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
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
			h0.setCellValue("Thời điểm");

			HSSFCell h1 = rowhead.createCell((short) 1);
			h1.setCellStyle(cellStyle);
			h1.setCellValue("Trạng Thái");

			HSSFCell h2 = rowhead.createCell((short) 2);
			h2.setCellStyle(cellStyle);
			h2.setCellValue("Tốc Độ Trung Bình");

			HSSFCell h3 = rowhead.createCell((short) 3);
			h3.setCellStyle(cellStyle);
			h3.setCellValue("Tốc Độ Tối Đa");

			HSSFCell h4 = rowhead.createCell((short) 4);
			h4.setCellStyle(cellStyle);
			h4.setCellValue("Quãng Đường");

			HSSFCell h5 = rowhead.createCell((short) 5);
			h5.setCellStyle(cellStyle);
			h5.setCellValue("Địa Điểm Bắt Đầu");

			HSSFCell h6 = rowhead.createCell((short) 6);
			h6.setCellStyle(cellStyle);
			h6.setCellValue("Địa điểm kết thúc");

			HSSFCell h7 = rowhead.createCell((short) 7);
			h7.setCellStyle(cellStyle);
			h7.setCellValue("Tọa Độ");

			rowhead.setHeightInPoints((short) 40);
			try {

				DBCamera objcmr = new DBCamera();
				ArrayList<EventRunStop> listreport = objcmr.BChoatdongxetheongay(currAcct.getAccountID(), contentall,
						ngay, currAcct.getTimeZone());

				for (int t = 0; t < listreport.size(); t++) {

					String tt = "";
					if (listreport.get(t).getStatusCode() == 0) {
						tt += "Bắt Đầu";
					}
					if (listreport.get(t).getStatusCode() == 61472) {
						tt += "Dừng: ";
					}
					if (listreport.get(t).getStatusCode() == 61714) {
						tt += "Chạy: ";
					}
					if (listreport.get(t).getStatusCode() == 90000) {
						tt += "Mất GPS: ";
					}
					if (listreport.get(t).getStatusCode() == 90001) {
						tt += "Không Có DL: ";
					}
					if (listreport.get(t).getStatusCode() == 99998 || listreport.get(t).getStatusCode() == 99999) {
						tt += "Kết Thúc";
					}
					if (listreport.get(t).getStatusLastingTime() > 0 && listreport.get(t).getStatusCode() != 0
							&& listreport.get(t).getStatusCode() != 99998 && listreport.get(t).getStatusCode() != 99999)
						tt = tt + " " + doiGio(listreport.get(t).getStatusLastingTime());

					String thoidiem = ConvertFromEpochTime(listreport.get(t).getTimestamp(), currAcct.getTimeZone());

					HSSFCellStyle csXe = wb.createCellStyle();
					csXe.setBorderTop((short) 1);
					csXe.setBorderRight((short) 1);
					csXe.setBorderLeft((short) 1);
					csXe.setBorderBottom((short) 1);

					HSSFRow row = sheet.createRow((short) (i + 1));

					HSSFCell r0 = row.createCell((short) 0);
					r0.setCellStyle(csXe);
					r0.setCellValue(thoidiem);
					//
					HSSFCell r1 = row.createCell((short) 1);
					r1.setCellStyle(csXe);
					r1.setCellValue(tt);
					//
					HSSFCell r2 = row.createCell((short) 2);
					r2.setCellStyle(csXe);
					r2.setCellValue(listreport.get(t).getAvgSpeedKPH());

					HSSFCell r3 = row.createCell((short) 3);
					r3.setCellStyle(csXe);
					r3.setCellValue(listreport.get(t).getMaxSpeedKPH());
					//
					HSSFCell r4 = row.createCell((short) 4);
					r4.setCellStyle(csXe);
					r4.setCellValue(round(listreport.get(t).getOdometerKM(), 2));
					//
					HSSFCell r5 = row.createCell((short) 5);
					r5.setCellStyle(csXe);
					r5.setCellValue(NCRToUnicode(listreport.get(t).getAddress()));
					//
					HSSFCell r6 = row.createCell((short) 6);
					r6.setCellStyle(csXe);
					r6.setCellValue(NCRToUnicode(listreport.get(t).getAddress1()));

					HSSFCell r7 = row.createCell((short) 7);
					r7.setCellStyle(csXe);
					r7.setCellValue(listreport.get(t).getLatitude() + "/" + listreport.get(t).getLongitude() + " - "
							+ listreport.get(t).getLatitude1() + "/" + listreport.get(t).getLongitude1());
					i++;
				} //
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

		/* Style */
		HTMLOutput HTML_CSS = new HTMLOutput() {
			public void write(PrintWriter out) throws IOException {
				String cssDir = BaoCaoHoatDongXeTheoNgay.this.getCssDirectory();
				WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
				WebPageAdaptor.writeCssLink(out, reqState, "scrollbar.css", cssDir);
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
				out.print(
						"<script type='text/javascript'>$(document).ready(function() {$('#scrollbar2').tinyscrollbar();}); </script>");

			}
		};

		/* Content */

		HTMLOutput HTML_CONTENT = new HTMLOutput(CommonServlet.CSS_CONTENT_FRAME, m) {
			public void write(PrintWriter out) throws IOException {
				// Print.logStackTrace("here");

				// String menuURL =
				// EncodeMakeURL(reqState,RequestProperties.TRACK_BASE_URI(),PAGE_MENU_TOP);
				String menuURL = privLabel.getWebPageURL(reqState, PAGE_MENU_TOP);
				// String chgURL =
				// EncodeMakeURL(reqState,RequestProperties.TRACK_BASE_URI(),pageName,COMMAND_INFO_UPDATE);
				String chgURL = privLabel.getWebPageURL(reqState, pageName, COMMAND_INFO_UPDATE);
				String frameTitle = i18n.getString("baocaoTram.PageTitle", "Tr&#x1EA1;m");

				// frame content
				// view submit
				HttpServletRequest request = reqState.getHttpServletRequest();
				String ngay = AttributeTools.getRequestString(request, "ngay", "");
				String contentall = "";
				String xem = AttributeTools.getRequestString(request, "btnview", "");
				contentall = AttributeTools.getRequestString(request, "device", "");
				String sql = LoadReport(currAcct.getAccountID(), ngay, contentall, privLabel, reqState,
						currAcct.getTimeZone());
				java.util.Calendar c = java.util.Calendar.getInstance();
//				c.add(java.util.Calendar.DAY_OF_YEAR, -1);
				Date now = c.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String d = sdf.format(now);
				out.println("<span class='" + CommonServlet.CSS_MENU_TITLE
						+ "' style='text-align:center;'>HOẠT ĐỘNG XE THEO NGÀY</span><br/>");
				out.println("<hr/>");
				out.println("<form name='AccountInfo' method='post' action='" + chgURL + "' target='_self'>\n");

				out.println("<table class='" + CommonServlet.CSS_ADMIN_VIEW_TABLE
						+ "' cellspacing='0' callpadding='0' border='0' width='100%' style='padding:15 0 15'>\n");

				out.print("<tr>\n");
				out.print("<td width='100px' align='right'>Ngày: </td>\n");
				out.print("<td width='100px' align='left'>\n");
				out.print(
						"<input id='Text1' name='ngay' type='text' style='width:100px' class='textReadOnly' onclick=\"displayCalendar(this,'dd/mm/yyyy',this,false,'','Text1')\" value='"
								+ d + "' /></td>");
				if (ngay != "") {
					out.print(
							"<script language ='javascript' type ='text/javascript'>document.getElementById('Text1').value ='"
									+ ngay + "'; </script>");
				}
				out.print("<td width='85px' align='right'></td>\n");
				out.print("<td width='100px' align='left'>\n");

				out.print("<td width='100px' align='right'>Ch&#x1ECD;n Xe: </td><td align='left'>\n");
				out.print(CreateCbbDevice(currAcct.getAccountID(), contentall, reqState, privLabel));
				out.print("</td>");
				out.println("</tr>");

				out.println("</table>");
				out.print(
						"<table cellspacing='0' style='width:100%;padding:10px 0px 10px'><tbody><tr class='viewhoz'><td width='80px'></td><td width='100px'> <input type='submit' class='button1' name='btnview' value='Xem' id='btnview'></td><td width='205px'></td><td align='left' width='220px'><input type='submit' id='btnExcel' value='Export Excel' name='btnExcel' class='button1' ></td>");
				out.print("<td align='left' ></td>");
				out.print("</tr></tbody></table>");
				if (ngay != "") {

					out.print(sql);
				}

				// }}
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

	public long ConvertToEpoch(String date, String timezone) {
		long res = 0;
		try {

			DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			DateFormat df12 = new SimpleDateFormat("dd/MM/yyyy HH:mm");

			TimeZone tz = TimeZone.getTimeZone(timezone);
			df1.setTimeZone(tz);
			Date d = df1.parse(date);
			date = df12.format(d);

			long ep = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").parse(date).getTime();
			ep = ep / 1000;
			res = ep;

		} catch (Exception e) {

		}
		return res;
	}

	// ------------------------------------------------------------------------

}
