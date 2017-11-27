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

public class baoCaoTongHop extends WebPageAdaptor implements Constants {

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

	public baoCaoTongHop() {
		this.setBaseURI(RequestProperties.TRACK_BASE_URI());
		this.setPageName(PAGE_BAO_CAO_TONG_HOP);
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
		I18N i18n = privLabel.getI18N(baoCaoTongHop.class);
		return super._getMenuDescription(reqState, i18n.getString(
				"baoCaoTongHop.Menu",
				"B&#x00E1;o c&#x00E1;o t&#x1ED5;ng h&#x1EE3;p"));
	}

	public String getMenuHelp(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(baoCaoTongHop.class);
		return super._getMenuHelp(reqState, i18n.getString(
				"baoCaoTongHop.MenuHelp",
				"B&#x00E1;o c&#x00E1;o t&#x1ED5;ng h&#x1EE3;p"));
	}

	// ------------------------------------------------------------------------

	public String getNavigationDescription(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(baoCaoTongHop.class);
		return super._getNavigationDescription(reqState, i18n.getString(
				"baoCaoTongHop.NavDesc",
				"B&#x00E1;o c&#x00E1;o t&#x1ED5;ng h&#x1EE3;p"));
	}

	public String getNavigationTab(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(baoCaoTongHop.class);
		return i18n.getString("baoCaoTongHop.NavTab",
				"B&#x00E1;o c&#x00E1;o t&#x1ED5;ng h&#x1EE3;p");
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
			dem = objcmr.phanQuyen(account.getAccountID(), "baoCaoTongHop");

			if (dem > 0)
				return false;
			else
				return true;

		}
	}

	public String doiGio(int tg) {//tg tinh theo don vi giay
		String chuoi = "";
		int ngay = tg / (86400);
		int gio = (tg - ngay * 3600 * 24) / 3600;
		int phut = (tg - ngay * 3600 * 24 - gio * 3600) / 60;
		int giay = tg - ngay * 3600 * 24 - gio * 3600 - phut * 60;
		String date = "", h = "", m = "", s = "";
		if (gio < 9)
			h = "0" + Integer.toString(gio);//dinh dang gio theo 24h vd  02:30:00 
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
		if (ngay > 0)
			date = Integer.toString(ngay) + " Ngày";
		return chuoi = chuoi + date + h + "h " + m + "' ";

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

	private String ConvertFromEpochHour(long epoch, String timezone) {
		TimeZone tz = DateTime.getTimeZone(timezone, null);
		DateTime dt = new DateTime(epoch);
		String dtFmt = dt.format("dd/MM/yyyy", tz);
		return dtFmt;
	}

	public String LoadbaoCaoTongHop(String tuNgay, String denNgay,
			String accountID, String DeviceID, RequestProperties reqState,
			PrivateLabel privLabel, String timezone) {
		int dem = 0, sumSLVT = 0, sumDungDo = 0, thoiGianLai = 0, thoiGianDung = 0, sumMoCua = 0, SumLXLT = 0;
		double sumQD = 0, AVGvanToc = 0, MaxVanToc = 0, sumTimeDung = 0, sumTimeLai = 0;
		String chuoi = "";
		chuoi = chuoi
				+ "<div id='dataTable'> <table  cellspacing='1' class='adminSelectTable_sortable'><thead><tr align='center'  ><th class='adminTableHeaderCol_sort'>Lái xe<br /><br />Driver<br /><br /><br />(0)</th><th class='adminTableHeaderCol_sort'>Ngày<br /><br />Date<br /><br /><br />(1)</th><th class='adminTableHeaderCol_sort'>Quãng đường vận hành<br />"
				+ "<br />Distance(km)<br /><br />(2)</th><th class='adminTableHeaderCol_sort'>Số lần quá tốc độ<br /><br />Number of over speed<br />"
				+ "<br />(3)</th><th class='adminTableHeaderCol_sort'>Vận tốc trung bình(km/h)<br /><br />Average speed(km/h)<br /><br />(4)</th><th class='adminTableHeaderCol_sort'>Vận tốc tối đa(km/h)<br /><br />"
				+ "Max speed(km/h)<br /><br />(5)</th><th class='adminTableHeaderCol_sort'>Số lần  mở cửa<br /><br />Number of open door<br /><br />(6)</th>"
				+ "<th class='adminTableHeaderCol_sort'>Số lần dừng đỗ<br /> <br />Number of idle time<br /><br />(7)</th><th class='adminTableHeaderCol_sort'>Tổng thời gian dừng<br /><br />Total idle time<br /> <br />"
				+ "(8)</th><th class='adminTableHeaderCol_sort'>Tổng thời gian lái xe<br /><br />Driving time<br /><br /><br />(9)</th><th class='adminTableHeaderCol_sort'>Số lần vi phạm<br /><br />lái xe liên tục <br />(lớn hơn 4h)<br />(10)</th>"
				+ "</tr></thead>";
		try {
			DBCamera objcmr = new DBCamera();
			BaoCaoTongHop data = new BaoCaoTongHop();

			if (DeviceID.endsWith("-1")) {
				IDDescription.SortBy sortBy = DeviceChooser
						.getSortBy(privLabel);
				java.util.List<IDDescription> idList = reqState
						.createIDDescriptionList(false, sortBy);
				IDDescription list[] = idList.toArray(new IDDescription[idList
						.size()]);
				for (int i = 0; i < list.length; i++) {

					sumQD = 0;
					sumDungDo = 0;
					sumSLVT = 0;
					MaxVanToc = 0;
					AVGvanToc = 0;
					thoiGianLai = 0;
					thoiGianDung = 0;
					SumLXLT = 0;
					SimpleDateFormat ft1 = new SimpleDateFormat("dd/MM/yyyy");
					Date d = ft1.parse(tuNgay);
					long fr = d.getTime() / 1000;
					Date d1 = ft1.parse(denNgay);
					long to = d1.getTime() / 1000;

					chuoi = chuoi
							+ "<tr style='background-color:white;color:black'><td colspan='11' align='left' style='height:30px'><b>"
							+ list[i].getID() + "</b></td></tr>";

					for (long ngay = fr; ngay <= to; ngay = ngay + 86400) {//86400s la 1 ngay

						/*int slvt = 0;
						int dungDo = 0;
						int tgd = 0;
						// int moCua = 0;
						double quangDuong = 0;
						double vttb = 0;
						double vttd = 0;
						int tgl = 0;
						int lxlt = 0;

						data = objcmr.loadBaoCaoTongHop(accountID,
								list[i].getID(), ngay);
						slvt = data.slvt;
						sumSLVT = sumSLVT + slvt;
						dungDo = data.dungDo;
						tgd = data.tgd;
						sumDungDo = sumDungDo + dungDo;
						thoiGianDung = thoiGianDung + tgd;

						lxlt = data.lxlt;
						SumLXLT = SumLXLT + lxlt;

						quangDuong = data.quangDuong;
						vttd = data.vttd;
						vttb = data.vttb;
						tgl = data.tgl;
						sumQD = sumQD + quangDuong;
						MaxVanToc = MaxVanToc + vttd;
						AVGvanToc = AVGvanToc + vttb;
						thoiGianLai = thoiGianLai + tgl;*/
						String date = ConvertFromEpochHour(ngay, timezone);
						objcmr.LoadHoatDongXeTheoNgay(accountID, list[i].getID(), date);
						int slvt = 0;
						int dungDo = 0;
						int tgd = 0;
						int moCua = 0;
						double quangDuong = 0;
						double vttb = 0;
						double vttd = 0;
						int tgl = 0;
						int lxlt = 0;
						slvt = objcmr.StaticCountDungDoEventRunStop(accountID, list[i].getID(), date, timezone);
						sumSLVT = sumSLVT + slvt;
						moCua = objcmr.StaticCountMoCua1(accountID, list[i].getID(), ngay, "");
						sumMoCua = sumMoCua + moCua;
						// phan nay sep chua tinh
						lxlt = objcmr.CheckLXLienTuc(accountID, list[i].getID(), ngay, "");
						SumLXLT = SumLXLT + lxlt;
						quangDuong = objcmr.StaticOdometerKM(accountID, list[i].getID(), date, timezone);
						sumQD = sumQD + quangDuong;
						ArrayList<ReportDate> listreport = objcmr.StaticDriving(accountID, list[i].getID(), date,
								timezone);
						for (int j = 0; j < listreport.size(); j++) {
							dungDo = listreport.get(j).getCount_dungdo();
							tgd = listreport.get(j).getTimedung();
							vttd = listreport.get(j).getMaxSpeed();
							vttb = listreport.get(j).getAvgSpeed();
							tgl = listreport.get(j).getTimedriving();

							sumDungDo = sumDungDo + dungDo;
							thoiGianDung = thoiGianDung + tgd;
							MaxVanToc = MaxVanToc + vttd;
							AVGvanToc = AVGvanToc + vttb;
							thoiGianLai = thoiGianLai + tgl;

						}

						String css = "";
						if (dem % 2 == 0)
							css = "adminTableBodyRowOdd";
						else
							css = "adminTableBodyRowEven";
						dem++;

						//String date = ConvertFromEpochHour(ngay, timezone);
						chuoi = chuoi
								+ "<tr class='"
								+ css
								+ "'><td>"
								+ objcmr.getDescriptionDriver(accountID,
										data.driverId) + "</td><td>" + date
								+ "</td><td>" + quangDuong + "</td><td>" + slvt
								+ "</td><td>" + vttb + "</td><td>" + vttd
								+ "</td><td>" + moCua
								+ "</td><td>" + dungDo + "</td><td>"
								+ doiGio(tgd) + "</td><td>" + doiGio(tgl)
								+ "</td><td>" + lxlt + "</td></tr>";
					}
					AVGvanToc = round(AVGvanToc / ((to + 86400 - fr) / 86400),
							2);
					MaxVanToc = round(MaxVanToc / ((to + 86400 - fr) / 86400),
							2);
					chuoi = chuoi
							+ "<tr style='color:black; font-weight:bold'><td></td><td>T&#x1ED5;ng</td><td>"
							+ round(sumQD, 2) + "</td><td>" + sumSLVT
							+ "</td><td>" + AVGvanToc + "</td><td>" + MaxVanToc
							+ "</td><td>" + sumMoCua
							+ "</td><td>" + sumDungDo + "</td><td>"
							+ doiGio(thoiGianDung) + "</td><td>"
							+ doiGio(thoiGianLai) + "</td><td>" + SumLXLT
							+ "</td></tr>";
				}
			} else {
				// sumMoCua = 0;
				sumQD = 0;
				sumDungDo = 0;
				sumSLVT = 0;
				MaxVanToc = 0;
				AVGvanToc = 0;
				thoiGianLai = 0;
				thoiGianDung = 0;
				SumLXLT = 0;
				SimpleDateFormat ft1 = new SimpleDateFormat("dd/MM/yyyy");
				Date d = ft1.parse(tuNgay);
				long fr = d.getTime() / 1000;
				Date d1 = ft1.parse(denNgay);
				long to = d1.getTime() / 1000;

				chuoi = chuoi
						+ "<tr style='background-color:white;color:black'><td colspan='11' align='left' style='height:30px'><b>"
						+ DeviceID + "</b></td></tr>";

				for (long ngay = fr; ngay <= to; ngay = ngay + 86400) {

					/*int slvt = 0;
					int dungDo = 0;
					int tgd = 0;
					// int moCua = 0;
					double quangDuong = 0;
					double vttb = 0;
					double vttd = 0;
					int tgl = 0;
					int lxlt = 0;
					data = objcmr.loadBaoCaoTongHop(accountID, DeviceID, ngay);

					slvt = data.slvt;
					sumSLVT = sumSLVT + slvt;

					dungDo = data.dungDo;
					tgd = data.tgd;
					sumDungDo = sumDungDo + dungDo;
					thoiGianDung = thoiGianDung + tgd;

					lxlt = data.lxlt;
					SumLXLT = SumLXLT + lxlt;

					quangDuong = data.quangDuong;
					vttd = data.vttd;
					vttb = data.vttb;
					tgl = data.tgl;
					sumQD = sumQD + quangDuong;
					MaxVanToc = MaxVanToc + vttd;
					AVGvanToc = AVGvanToc + vttb;
					thoiGianLai = thoiGianLai + tgl;*/
					String date = ConvertFromEpochHour(ngay, timezone);
					objcmr.LoadHoatDongXeTheoNgay(accountID, DeviceID, date);
					int slvt = 0;
					int dungDo = 0;
					int tgd = 0;
					int moCua = 0;
					double quangDuong = 0;
					double vttb = 0;
					double vttd = 0;
					int tgl = 0;
					int lxlt = 0;
					slvt = objcmr.StaticCountDungDoEventRunStop(accountID, DeviceID, date, timezone);
					sumSLVT = sumSLVT + slvt;
					moCua = objcmr.StaticCountMoCua1(accountID, DeviceID, ngay, "");
					sumMoCua = sumMoCua + moCua;
					// phan nay sep chua tinh
					lxlt = objcmr.CheckLXLienTuc(accountID, DeviceID, ngay, "");
					SumLXLT = SumLXLT + lxlt;
					quangDuong = objcmr.StaticOdometerKM(accountID, DeviceID, date, timezone);
					sumQD = sumQD + quangDuong;
					ArrayList<ReportDate> listreport = objcmr.StaticDriving(accountID, DeviceID, date, timezone);
					for (int j = 0; j < listreport.size(); j++) {
						dungDo = listreport.get(j).getCount_dungdo();
						tgd = listreport.get(j).getTimedung();
						vttd = listreport.get(j).getMaxSpeed();
						vttb = listreport.get(j).getAvgSpeed();
						tgl = listreport.get(j).getTimedriving();

						sumDungDo = sumDungDo + dungDo;
						thoiGianDung = thoiGianDung + tgd;
						MaxVanToc = MaxVanToc + vttd;
						AVGvanToc = AVGvanToc + vttb;
						thoiGianLai = thoiGianLai + tgl;

					}

					String css = "";
					if (dem % 2 == 0)
						css = "adminTableBodyRowOdd";
					else
						css = "adminTableBodyRowEven";
					dem++;

					//String date = ConvertFromEpochHour(ngay, timezone);
					chuoi = chuoi + "<tr class='" + css + "'><td></td><td>"
							+ date + "</td><td>" + quangDuong + "</td><td>"
							+ slvt + "</td><td>" + vttb + "</td><td>" + vttd
							+ "</td><td>" + moCua
							+ "</td><td>" + dungDo + "</td><td>" + doiGio(tgd)
							+ "</td><td>" + doiGio(tgl) + "</td><td>" + lxlt
							+ "</td></tr>";
				}
				AVGvanToc = round(AVGvanToc / ((to + 86400 - fr) / 86400), 2);
				MaxVanToc = round(MaxVanToc / ((to + 86400 - fr) / 86400), 2);
				chuoi = chuoi
						+ "<tr style='color:black; font-weight:bold'><td></td><td>T&#x1ED5;ng</td><td>"
						+ round(sumQD, 2) + "</td><td>" + sumSLVT + "</td><td>"
						+ AVGvanToc + "</td><td>" + MaxVanToc + "</td><td>" + sumMoCua + "</td><td>"
						+ sumDungDo + "</td><td>" + doiGio(thoiGianDung)
						+ "</td><td>" + doiGio(thoiGianLai) + "</td><td>"
						+ SumLXLT + "</td></tr>";

			}
		}

		catch (Exception ex) {

			chuoi = "lỗi" + ex.getMessage();
		}

		return chuoi + "</table></div>";
	}

	public String CreateCbbDevice(String accountid, String idselect,
			RequestProperties reqState, PrivateLabel privLabel)
			throws IOException {
		IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
		java.util.List<IDDescription> idList = reqState
				.createIDDescriptionList(false, sortBy);
		IDDescription list[] = idList.toArray(new IDDescription[idList.size()]);

		String strre = "<select id ='device' name = 'device' class='textReadOnly' style='width:100px;'>";
		if (idselect == "-1") {
			strre += "<option value ='-1' selected =\"selected\">Tất cả</option>\n";
		} else {
			strre += "<option value ='-1'>Tất cả</option>\n";
		}
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

	public void writePage(final RequestProperties reqState, String pageMsg)
			throws IOException {
		final PrivateLabel privLabel = reqState.getPrivateLabel();
		final I18N i18n = privLabel.getI18N(baoCaoTongHop.class);
		final Locale locale = reqState.getLocale();
		final Account currAcct = reqState.getCurrentAccount();
		final User currUser = reqState.getCurrentUser();
		final String pageName = this.getPageName();
		String m = pageMsg;
		boolean error = false;
		HttpServletRequest request = reqState.getHttpServletRequest();
		HttpServletResponse response = reqState.getHttpServletResponse();
		String excel = AttributeTools.getRequestString(request, "btnExcel", "");
		String tuNgay = "", btnXem, denNgay = "";
		tuNgay = AttributeTools.getRequestString(request, "tuNgay", "");
		denNgay = AttributeTools.getRequestString(request, "denNgay", "");
		String contentall = AttributeTools.getRequestString(request, "device",
				"");
		int i = 0;

		int dem = 0, sumSLVT = 0, sumDungDo = 0, thoiGianLai = 0, thoiGianDung = 0, sumLXLT = 0;
		double sumQD = 0, AVGvanToc = 0, MaxVanToc = 0, sumTimeDung = 0, sumTimeLai = 0, sumMoCua = 0;
		BaoCaoTongHop data = new BaoCaoTongHop();
		if (excel != "") {
			if (excel.equals("Export Excel")) {
				java.util.Calendar c = java.util.Calendar.getInstance();
				Date now = c.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String d3 = sdf.format(now);
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setHeader("Content-Disposition",
						"attachment; filename=baoCaoTongHop_" + d3 + ".xls");
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
				ct.setCellValue("BÁO CÁO TỔNG HỢP");
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
				cTuNgay.setCellValue("Từ Ngày");
				rngay.createCell((short) 2).setCellValue(tuNgay);
				HSSFCell cdenNgay = rngay.createCell((short) 4);
				cdenNgay.setCellStyle(csNgay);
				cdenNgay.setCellValue("Đến Ngày");
				rngay.createCell((short) 5).setCellValue(denNgay);

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

				HSSFCell h10 = rowhead.createCell((short) 10);
				h10.setCellStyle(cellStyle);
				h10.setCellValue("Lái Xe \n (Driver)");

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
				h7.setCellValue("Tổng thời gian dừng(h)\n (Total idle time(h))");

				HSSFCell h9 = rowhead.createCell((short) 9);
				h9.setCellStyle(cellStyle);
				h9.setCellValue("Số lần vi phạm \n lái xe liên tục\n(lớn hơn 4h))");
				rowhead.setHeightInPoints((short) 40);

				String accountID = currAcct.getAccountID();
				try {
					DBCamera objcmr = new DBCamera();
					IDDescription.SortBy sortBy = DeviceChooser
							.getSortBy(privLabel);
					java.util.List<IDDescription> idList = reqState
							.createIDDescriptionList(false, sortBy);
					IDDescription list[] = idList
							.toArray(new IDDescription[idList.size()]);
					i--;
					if (contentall.equals("-1")) {
						for (int k = 0; k < list.length; k++) {
							sumMoCua = 0;
							sumQD = 0;
							sumDungDo = 0;
							sumSLVT = 0;
							MaxVanToc = 0;
							AVGvanToc = 0;
							thoiGianLai = 0;
							thoiGianDung = 0;
							sumLXLT = 0;
							SimpleDateFormat ft1 = new SimpleDateFormat(
									"dd/MM/yyyy");
							Date d = ft1.parse(tuNgay);
							long fr = d.getTime() / 1000;
							Date d1 = ft1.parse(denNgay);
							long to = d1.getTime() / 1000;
							i++;
							HSSFRow row1 = sheet.createRow((short) (i + 1));
							row1.setHeightInPoints(25);
							sheet.addMergedRegion(new CellRangeAddress(i + 1,
									i + 1, 0, 10));
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
							cellA1.setCellValue(list[k].getID());
							cellA1.setCellStyle(csXe);
							for (long ngay = fr; ngay <= to; ngay = ngay
									+ (24 * 3600)) {

								/*int slvt = 0;
								int dungDo = 0;
								int tgd = 0;
								int moCua = 0;
								double quangDuong = 0;
								double vttb = 0;
								double vttd = 0;
								int tgl = 0;
								int lxlt = 0;

								data = objcmr.loadBaoCaoTongHop(accountID,
										list[k].getID(), ngay);
								slvt = data.slvt;
								sumSLVT = sumSLVT + slvt;
								dungDo = data.dungDo;
								tgd = data.tgd;
								sumDungDo = sumDungDo + dungDo;
								thoiGianDung = thoiGianDung + tgd;

								lxlt = data.lxlt;
								sumLXLT = sumLXLT + lxlt;

								quangDuong = data.quangDuong;
								vttd = data.vttd;
								vttb = data.vttb;
								tgl = data.tgl;
								sumQD = sumQD + quangDuong;
								MaxVanToc = MaxVanToc + vttd;
								AVGvanToc = AVGvanToc + vttb;
								thoiGianLai = thoiGianLai + tgl;*/
								String date = ConvertFromEpochHour(ngay, currAcct.getTimeZone());
								int slvt = 0;
								int dungDo = 0;
								int tgd = 0;
								int moCua = 0;
								double quangDuong = 0;
								double vttb = 0;
								double vttd = 0;
								int tgl = 0;
								int lxlt = 0;
								slvt = objcmr.StaticCountDungDoEventRunStop(accountID, list[k].getID(), date,
										currAcct.getTimeZone());
								sumSLVT = sumSLVT + slvt;
								moCua = objcmr.StaticCountMoCua1(accountID, list[k].getID(), ngay, "");
								sumMoCua = sumMoCua + moCua;
								// phan nay sep chua tinh
								lxlt = objcmr.CheckLXLienTuc(accountID, list[k].getID(), ngay, "");
								sumLXLT = sumLXLT + lxlt;
								quangDuong = objcmr.StaticOdometerKM(accountID, list[k].getID(), date,
										currAcct.getTimeZone());
								sumQD = sumQD + quangDuong;
								ArrayList<ReportDate> listreport = objcmr.StaticDriving(accountID, list[k].getID(),
										date, currAcct.getTimeZone());
								for (int j = 0; j < listreport.size(); j++) {
									dungDo = listreport.get(j).getCount_dungdo();
									tgd = listreport.get(j).getTimedung();
									vttd = listreport.get(j).getMaxSpeed();
									vttb = listreport.get(j).getAvgSpeed();
									tgl = listreport.get(j).getTimedriving();

									sumDungDo = sumDungDo + dungDo;
									thoiGianDung = thoiGianDung + tgd;
									MaxVanToc = MaxVanToc + vttd;
									AVGvanToc = AVGvanToc + vttb;
									thoiGianLai = thoiGianLai + tgl;

								}

								String css = "";
								if (dem % 2 == 0)
									css = "adminTableBodyRowOdd";
								else
									css = "adminTableBodyRowEven";
								dem++;

								//String date = ConvertFromEpochHour(ngay, currAcct.getTimeZone());
								i++;
								HSSFRow row = sheet.createRow((short) (i + 1));

								HSSFCellStyle csr = wb.createCellStyle();
								csr.setBorderTop((short) 1);
								csr.setBorderRight((short) 1);
								csr.setBorderLeft((short) 1);
								csr.setBorderBottom((short) 1);

								HSSFCell r10 = row.createCell((short) 10);
								r10.setCellStyle(csr);
								r10.setCellValue(objcmr.getDescriptionDriver(
										accountID, list[k].getID()));

								HSSFCell r0 = row.createCell((short) 0);
								r0.setCellStyle(csr);
								r0.setCellValue(date);
								HSSFCell r1 = row.createCell((short) 1);
								r1.setCellStyle(csr);
								r1.setCellValue(quangDuong);
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
								HSSFCell r9 = row.createCell((short) 9);
								r9.setCellStyle(csr);
								r9.setCellValue(lxlt);
							}
							AVGvanToc = round(AVGvanToc
									/ ((to + 86400 - fr) / 86400), 2);
							MaxVanToc = round(MaxVanToc
									/ ((to + 86400 - fr) / 86400), 2);
							i++;
							HSSFRow tong = sheet.createRow((short) (i + 1));
							HSSFCellStyle csTong = wb.createCellStyle();
							csTong.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
							csTong.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
							csTong.setBorderTop((short) 1);
							csTong.setBorderRight((short) 1);
							csTong.setBorderLeft((short) 1);
							csTong.setBorderBottom((short) 1);
							HSSFFont fTong = wb.createFont();
							fTong.setFontHeightInPoints((short) 10);
							// make it red
							fTong.setColor((short) HSSFColor.WHITE.index);
							// make it bold
							// arial is the default font
							fTong.setBoldweight((short) fTong.BOLDWEIGHT_BOLD);
							csTong.setFont(fTong);

							HSSFCell rt10 = tong.createCell((short) 10);
							rt10.setCellStyle(csTong);
							rt10.setCellValue("");

							HSSFCell rt0 = tong.createCell((short) 0);
							rt0.setCellStyle(csTong);
							rt0.setCellValue("Tổng");
							HSSFCell rt1 = tong.createCell((short) 1);
							rt1.setCellStyle(csTong);
							rt1.setCellValue(round(sumQD, 2));
							HSSFCell rt2 = tong.createCell((short) 2);
							rt2.setCellStyle(csTong);
							rt2.setCellValue(sumSLVT);
							HSSFCell rt3 = tong.createCell((short) 3);
							rt3.setCellStyle(csTong);
							rt3.setCellValue(AVGvanToc);
							HSSFCell rt4 = tong.createCell((short) 4);
							rt4.setCellStyle(csTong);
							rt4.setCellValue(MaxVanToc);
							HSSFCell rt5 = tong.createCell((short) 5);
							rt5.setCellStyle(csTong);
							rt5.setCellValue(sumMoCua);
							HSSFCell rt6 = tong.createCell((short) 6);
							rt6.setCellStyle(csTong);
							rt6.setCellValue(sumDungDo);
							HSSFCell rt8 = tong.createCell((short) 8);
							rt8.setCellStyle(csTong);
							rt8.setCellValue(doiGio(thoiGianLai));
							HSSFCell rt7 = tong.createCell((short) 7);
							rt7.setCellStyle(csTong);
							rt7.setCellValue(doiGio(thoiGianDung));

							HSSFCell rt9 = tong.createCell((short) 9);
							rt9.setCellStyle(csTong);
							rt9.setCellValue(sumLXLT);

						}
					} else {
						sumMoCua = 0;
						sumQD = 0;
						sumDungDo = 0;
						sumSLVT = 0;
						MaxVanToc = 0;
						AVGvanToc = 0;
						thoiGianLai = 0;
						thoiGianDung = 0;
						sumLXLT = 0;
						SimpleDateFormat ft1 = new SimpleDateFormat(
								"dd/MM/yyyy");
						Date d = ft1.parse(tuNgay);
						long fr = d.getTime() / 1000;
						Date d1 = ft1.parse(denNgay);
						long to = d1.getTime() / 1000;
						i++;
						HSSFRow row1 = sheet.createRow((short) (i + 1));
						row1.setHeightInPoints(25);
						sheet.addMergedRegion(new CellRangeAddress(i + 1,
								i + 1, 0, 9));
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
						for (long ngay = fr; ngay <= to; ngay = ngay
								+ (24 * 3600)) {

							/*int slvt = 0;
							int dungDo = 0;
							int tgd = 0;
							int moCua = 0;
							double quangDuong = 0;
							double vttb = 0;
							double vttd = 0;
							int tgl = 0;
							int lxlt = 0;

							data = objcmr.loadBaoCaoTongHop(accountID,
									contentall, ngay);

							slvt = data.slvt;
							sumSLVT = sumSLVT + slvt;

							dungDo = data.dungDo;
							tgd = data.tgd;
							sumDungDo = sumDungDo + dungDo;
							thoiGianDung = thoiGianDung + tgd;

							lxlt = data.lxlt;
							sumLXLT = sumLXLT + lxlt;

							quangDuong = data.quangDuong;
							vttd = data.vttd;
							vttb = data.vttb;
							tgl = data.tgl;
							sumQD = sumQD + quangDuong;
							MaxVanToc = MaxVanToc + vttd;
							AVGvanToc = AVGvanToc + vttb;
							thoiGianLai = thoiGianLai + tgl;*/

							int slvt = 0;
							int dungDo = 0;
							int tgd = 0;
							int moCua = 0;
							double quangDuong = 0;
							double vttb = 0;
							double vttd = 0;
							int tgl = 0;
							int lxlt = 0;
							String date = ConvertFromEpochHour(ngay, currAcct.getTimeZone());
							// contentall
							slvt = objcmr.StaticCountDungDoEventRunStop(accountID, contentall, date,
									currAcct.getTimeZone());
							sumSLVT = sumSLVT + slvt;
							moCua = objcmr.StaticCountMoCua1(accountID, contentall, ngay, "");
							sumMoCua = sumMoCua + moCua;
							// phan nay sep chua tinh
							lxlt = objcmr.CheckLXLienTuc(accountID, contentall, ngay, "");
							sumLXLT = sumLXLT + lxlt;
							quangDuong = objcmr.StaticOdometerKM(accountID, contentall, date, currAcct.getTimeZone());
							sumQD = sumQD + quangDuong;
							ArrayList<ReportDate> listreport = objcmr.StaticDriving(accountID, contentall, date,
									currAcct.getTimeZone());
							for (int j = 0; j < listreport.size(); j++) {
								dungDo = listreport.get(j).getCount_dungdo();
								tgd = listreport.get(j).getTimedung();
								vttd = listreport.get(j).getMaxSpeed();
								vttb = listreport.get(j).getAvgSpeed();
								tgl = listreport.get(j).getTimedriving();

								sumDungDo = sumDungDo + dungDo;
								thoiGianDung = thoiGianDung + tgd;
								MaxVanToc = MaxVanToc + vttd;
								AVGvanToc = AVGvanToc + vttb;
								thoiGianLai = thoiGianLai + tgl;

							}

							String css = "";
							if (dem % 2 == 0)
								css = "adminTableBodyRowOdd";
							else
								css = "adminTableBodyRowEven";
							dem++;

							//String date = ConvertFromEpochHour(ngay, currAcct.getTimeZone());
							i++;
							HSSFRow row = sheet.createRow((short) (i + 1));

							HSSFCellStyle csr = wb.createCellStyle();
							csr.setBorderTop((short) 1);
							csr.setBorderRight((short) 1);
							csr.setBorderLeft((short) 1);
							csr.setBorderBottom((short) 1);

							HSSFCell r10 = row.createCell((short) 10);
							r10.setCellStyle(csr);
							r10.setCellValue(objcmr.getDescriptionDriver(
									accountID, contentall));

							HSSFCell r0 = row.createCell((short) 0);
							r0.setCellStyle(csr);
							r0.setCellValue(date);
							HSSFCell r1 = row.createCell((short) 1);
							r1.setCellStyle(csr);
							r1.setCellValue(quangDuong);
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

							HSSFCell r9 = row.createCell((short) 9);
							r9.setCellStyle(csr);
							r9.setCellValue(lxlt);

						}
						AVGvanToc = round(AVGvanToc
								/ ((to + 86400 - fr) / 86400), 2);
						MaxVanToc = round(MaxVanToc
								/ ((to + 86400 - fr) / 86400), 2);
						i++;
						HSSFRow tong = sheet.createRow((short) (i + 1));
						HSSFCellStyle csTong = wb.createCellStyle();
						csTong.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
						csTong.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						csTong.setBorderTop((short) 1);
						csTong.setBorderRight((short) 1);
						csTong.setBorderLeft((short) 1);
						csTong.setBorderBottom((short) 1);
						HSSFFont fTong = wb.createFont();
						fTong.setFontHeightInPoints((short) 10);
						// make it red
						fTong.setColor((short) HSSFColor.WHITE.index);
						// make it bold
						// arial is the default font
						fTong.setBoldweight((short) fTong.BOLDWEIGHT_BOLD);
						csTong.setFont(fTong);

						HSSFCell rt10 = tong.createCell((short) 10);
						rt10.setCellStyle(csTong);
						rt10.setCellValue("");

						HSSFCell rt0 = tong.createCell((short) 0);
						rt0.setCellStyle(csTong);
						rt0.setCellValue("Tổng");
						HSSFCell rt1 = tong.createCell((short) 1);
						rt1.setCellStyle(csTong);
						rt1.setCellValue(round(sumQD, 2));
						HSSFCell rt2 = tong.createCell((short) 2);
						rt2.setCellStyle(csTong);
						rt2.setCellValue(sumSLVT);
						HSSFCell rt3 = tong.createCell((short) 3);
						rt3.setCellStyle(csTong);
						rt3.setCellValue(AVGvanToc);
						HSSFCell rt4 = tong.createCell((short) 4);
						rt4.setCellStyle(csTong);
						rt4.setCellValue(MaxVanToc);
						HSSFCell rt5 = tong.createCell((short) 5);
						rt5.setCellStyle(csTong);
						rt5.setCellValue(sumMoCua);
						HSSFCell rt6 = tong.createCell((short) 6);
						rt6.setCellStyle(csTong);
						rt6.setCellValue(sumDungDo);
						HSSFCell rt8 = tong.createCell((short) 8);
						rt8.setCellStyle(csTong);
						rt8.setCellValue(doiGio(thoiGianLai));
						HSSFCell rt7 = tong.createCell((short) 7);
						rt7.setCellStyle(csTong);
						rt7.setCellValue(doiGio(thoiGianDung));
						HSSFCell rt9 = tong.createCell((short) 9);
						rt9.setCellStyle(csTong);
						rt9.setCellValue(sumLXLT);
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
					OutputStream out = response.getOutputStream();
					wb.write(out);
					out.close();
				} catch (Exception ex) {
					OutputStream out = response.getOutputStream();
					wb.write(out);
					out.close();
				}
				return;
			}
		}

		/* Style */
		HTMLOutput HTML_CSS = new HTMLOutput() {
			public void write(PrintWriter out) throws IOException {
				String cssDir = baoCaoTongHop.this.getCssDirectory();
				WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
			}
		};

		/* javascript */
		HTMLOutput HTML_JS = new HTMLOutput() {
			public void write(PrintWriter out) throws IOException {
				MenuBar.writeJavaScript(out, pageName, reqState);
				out.println("<script type=\"text/javascript\" src=\"js/PCalendar.js\"></script>\n");
				out.println("<script type=\"text/javascript\" src=\"js/jquery-1.4.2.min.js\"></script>\n");
				out.println("        <script type=\"text/javascript\" src=\"js/pdf.js\"></script>\n"); 
				out.println("<script> $(document).ready(function() { $('#running').remove(); "
						+ " $('#exportpdf').click(function (){ PrintContent('BÁO CÁO TỔNG HỢP')}); "
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
				String frameTitle = i18n.getString("baocaoTongHop.PageTitle",
						"Báo cáo t?ng h?p");
				String contentall = "";
				// frame content
				// view submit
				String tuNgay = "", btnXem, denNgay = "";
				HttpServletRequest request = reqState.getHttpServletRequest();
				HttpServletResponse rp = reqState.getHttpServletResponse();
				tuNgay = AttributeTools.getRequestString(request, "tuNgay", "");
				denNgay = AttributeTools.getRequestString(request, "denNgay",
						"");
				contentall = AttributeTools.getRequestString(request, "device",
						"");
				btnXem = AttributeTools
						.getRequestString(request, "btnview", "");
				java.util.Calendar c = java.util.Calendar.getInstance();
				c.add(java.util.Calendar.DAY_OF_YEAR, -1);
				Date now = c.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String d = sdf.format(now);

				out.println("<span class='"
						+ CommonServlet.CSS_MENU_TITLE
						+ "'>B&#x00E1;o c&#x00E1;o t&#x1ED5;ng h&#x1EE3;p</span><br/>");
				out.println("<hr/>");
				out.println("<form name='AccountInfo' method='post' action='"
						+ chgURL + "' target='_self'>\n");

				out.println("<table class='"
						+ CommonServlet.CSS_ADMIN_VIEW_TABLE
						+ "' cellspacing='0' callpadding='0' border='0' style='padding:15 0 15'>\n");

				out.print("<tr>\n");
				out.print("<td class='"
						+ CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER
						+ "' style='width:90px' align='right'>T&#x1EEB; ng&#x00E0;y:</td>\n");
				out.print("<td style='width:100px'>\n");
				out.print("<input id='Text1' name='tuNgay' type='text' class='textReadOnly' style='width:100px' onclick=\"displayCalendar(this,'dd/mm/yyyy',this,false,'','Text1')\" value='"
						+ d + "' /></td>");
				if (tuNgay != "") {
					out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('Text1').value ='"
							+ tuNgay + "'; </script>");
				}
				out.print("<td class='"
						+ CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER
						+ "' style='width:90px' align='right'>&#x0110;&#x1EBF;n ng&#x00E0;y:</td>\n");
				out.print("<td style='width:100px'>\n");
				out.print("<input id='Text2' name='denNgay' type='text' class='textReadOnly' style='width:100px' onclick=\"displayCalendar(this,'dd/mm/yyyy',this,false,'','Text2')\" value='"
						+ d + "' /></td><td></td>");
				if (tuNgay != "") {
					out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('Text2').value ='"
							+ denNgay + "'; </script>");
				}
				out.print("<td align='right' width='100px'><span style='margin-left: 10px;margin-right:5px;'>"
						+ i18n.getString("DeviceSelect", "ch&#x1ECD;n xe:")
						+ "</span></td><td>\n");
				out.print(CreateCbbDevice(currAcct.getAccountID(), contentall,
						reqState, privLabel));

				out.print("</td>");

				out.println("</tr>");
				// out.print ("<tr>");
				// out.print
				// ("<td colspan='2' style ='height:400px; border:solid 1px silver;' >"+sql
				// +currAcct.getAccountID()+ngayxem+"</td>");
				// out.println("</tr>");

				out.println("</table>");
				out.print("<div class='viewhoz'>");
				out.print("<table cellspacing='0' style='padding:0px 0px 0px'><tbody><tr ><td width='100px'></td><td width='100px'> <input type='submit' class='button1' name='btnview' value='Xem' id='btnview'></td><td width='105px'></td><td align='left'><input type='submit' id='btnExcel' value='Export Excel' name='btnExcel' class='button1'></td><td align='left' width='220px'><input type='button' id='exportpdf' value='Export PDF' name='exportpdf' class='button1' ></td></tr></tbody></table></div>");
				// String xem= AttributeTools.getRequestString(request,
				// "btnview", "");
				if (btnXem.equals("Xem")) {
					out.println("<div id='running' style='padding:7px;font-weight:bold'>Đang tổng hợp dữ liệu. Vui lòng chờ trong giây lát.</div>");
					out.print(LoadbaoCaoTongHop(tuNgay, denNgay,
							currAcct.getAccountID(), contentall, reqState,
							privLabel, currAcct.getTimeZone()));
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

	public long ConvertToEpoch(String date, String timezone) {
		long res = 0;
		try {

			DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			DateFormat df12 = new SimpleDateFormat("dd/MM/yyyy HH:mm");

			TimeZone tz = TimeZone.getTimeZone(timezone);
			df1.setTimeZone(tz);
			Date d = df1.parse(date);
			date = df12.format(d);

			long ep = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").parse(
					date).getTime();
			ep = ep / 1000;
			res = ep;

		} catch (Exception e) {

		}
		return res;
	}

	// ------------------------------------------------------------------------
}