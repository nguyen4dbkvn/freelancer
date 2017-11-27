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

public class baoCaoTongHopTruoc extends WebPageAdaptor implements Constants {

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

	public baoCaoTongHopTruoc() {
		this.setBaseURI(RequestProperties.TRACK_BASE_URI());
		this.setPageName(PAGE_BCTH_TRUOC);
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
		I18N i18n = privLabel.getI18N(baoCaoTongHopTruoc.class);
		return super._getMenuDescription(reqState, i18n.getString(
				"baoCaoTongHop.Menu",
				"Tổng hợp trước - B&#x00E1;o c&#x00E1;o t&#x1ED5;ng h&#x1EE3;p"));
	}

	public String getMenuHelp(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(baoCaoTongHopTruoc.class);
		return super._getMenuHelp(reqState, i18n.getString(
				"baoCaoTongHop.MenuHelp",
				"Tổng hợp trước - B&#x00E1;o c&#x00E1;o t&#x1ED5;ng h&#x1EE3;p"));
	}

	// ------------------------------------------------------------------------

	public String getNavigationDescription(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(baoCaoTongHopTruoc.class);
		return super._getNavigationDescription(reqState, i18n.getString(
				"baoCaoTongHop.NavDesc",
				"Tổng hợp trước - B&#x00E1;o c&#x00E1;o t&#x1ED5;ng h&#x1EE3;p"));
	}

	public String getNavigationTab(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(baoCaoTongHopTruoc.class);
		return i18n.getString("baoCaoTongHop.NavTab",
				"Tổng hợp trước - B&#x00E1;o c&#x00E1;o t&#x1ED5;ng h&#x1EE3;p");
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

	public String doiGio(int tg) {
		String chuoi = "";
		int ngay = tg / (86400);
		int gio = (tg - ngay * 3600 * 24) / 3600;
		int phut = (tg - ngay * 3600 * 24 - gio * 3600) / 60;
		int giay = tg - ngay * 3600 * 24 - gio * 3600 - phut * 60;
		String date = "", h = "", m = "", s = "";
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
				+ "<table  cellspacing='1' class='adminSelectTable_sortable'>"
				+ "<thead><tr align='center'  >"
				+ "<th class='adminTableHeaderCol_sort'>Lái xe<br /><br />Driver<br /><br /><br />(0)</th><th class='adminTableHeaderCol_sort'>Ngày<br /><br />Date<br /><br /><br />(1)</th><th class='adminTableHeaderCol_sort'>Quãng đường vận hành<br />"
				+ "<br />Distance(km)<br /><br />(2)</th><th class='adminTableHeaderCol_sort'>Số lần quá tốc độ<br /><br />Number of over speed<br />"
				+ "<br />(3)</th><th class='adminTableHeaderCol_sort'>Vận tốc trung bình(km/h)<br /><br />Average speed(km/h)<br /><br />(4)</th><th class='adminTableHeaderCol_sort'>Vận tốc tối đa(km/h)<br /><br />"
				+ "Max speed(km/h)<br /><br />(5)</th>"
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

					for (long ngay = fr; ngay <= to; ngay = ngay + 86400) {
						ArrayList<String> listDriver = objcmr.GetLX(accountID,
								list[i].getID(), ngay);
						int sizeDriver = listDriver.size();
						if (sizeDriver == 0) {
							int slvt = 0;
							int dungDo = 0;
							int tgd = 0;
							// int moCua = 0;
							double quangDuong = 0;
							double vttb = 0;
							double vttd = 0;
							int tgl = 0;
							int lxlt = 0;

							data = objcmr.baoCaoTongHopTruoc(accountID,list[i].getID(), ngay, "");
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
							thoiGianLai = thoiGianLai + tgl;

							String css = "";
							if (dem % 2 == 0)
								css = "adminTableBodyRowOdd";
							else
								css = "adminTableBodyRowEven";
							dem++;

							String date = ConvertFromEpochHour(ngay, timezone);
							chuoi = chuoi + "<tr class='" + css
									+ "'><td></td><td>" + date + "</td><td>"
									+ quangDuong + "</td><td>" + slvt
									+ "</td><td>" + vttb + "</td><td>" + vttd
									+ "</td><td>" + dungDo + "</td><td>"
									+ doiGio(tgd) + "</td><td>" + doiGio(tgl)
									+ "</td><td>" + lxlt + "</td></tr>";

						} else {
							for (int sizeInt = 0; sizeInt < sizeDriver; sizeInt++) {
								int slvt = 0;
								int dungDo = 0;
								int tgd = 0;
								// int moCua = 0;
								double quangDuong = 0;
								double vttb = 0;
								double vttd = 0;
								int tgl = 0;
								int lxlt = 0;

								data = objcmr.baoCaoTongHopTruoc(accountID,list[i].getID(), ngay, listDriver.get(sizeInt));

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
								thoiGianLai = thoiGianLai + tgl;

								String css = "";
								if (dem % 2 == 0)
									css = "adminTableBodyRowOdd";
								else
									css = "adminTableBodyRowEven";
								dem++;

								String date = ConvertFromEpochHour(ngay,
										timezone);
								chuoi = chuoi
										+ "<tr class='"
										+ css
										+ "'><td>"
										+ objcmr.getDescriptionDriver(
												accountID,
												listDriver.get(sizeInt))
										+ "</td><td>" + date + "</td><td>"
										+ quangDuong + "</td><td>" + slvt
										+ "</td><td>" + vttb + "</td><td>"
										+ vttd + "</td><td>" + dungDo
										+ "</td><td>" + doiGio(tgd)
										+ "</td><td>" + doiGio(tgl)
										+ "</td><td>" + lxlt + "</td></tr>";

							}
						}

					}

					AVGvanToc = round(AVGvanToc / ((to + 86400 - fr) / 86400),
							2);
					MaxVanToc = round(MaxVanToc / ((to + 86400 - fr) / 86400),
							2);
					chuoi = chuoi
							+ "<tr style='color:black; font-weight:bold'><td></td><td>T&#x1ED5;ng</td><td>"
							+ round(sumQD, 2) + "</td><td>" + sumSLVT
							+ "</td><td>" + AVGvanToc + "</td><td>" + MaxVanToc
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
					ArrayList<String> listDriver = objcmr.GetLX(accountID,
							DeviceID, ngay);
					int sizeDriver = listDriver.size();
					if (sizeDriver == 0) {
						int slvt = 0;
						int dungDo = 0;
						int tgd = 0;
						// int moCua = 0;
						double quangDuong = 0;
						double vttb = 0;
						double vttd = 0;
						int tgl = 0;
						int lxlt = 0;
						data = objcmr.baoCaoTongHopTruoc(accountID, DeviceID, ngay,"");

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
						thoiGianLai = thoiGianLai + tgl;

						String css = "";
						if (dem % 2 == 0)
							css = "adminTableBodyRowOdd";
						else
							css = "adminTableBodyRowEven";
						dem++;

						String date = ConvertFromEpochHour(ngay, timezone);
						chuoi = chuoi + "<tr class='" + css + "'><td></td><td>"
								+ date + "</td><td>" + quangDuong + "</td><td>"
								+ slvt + "</td><td>" + vttb + "</td><td>"
								+ vttd + "</td><td>" + dungDo + "</td><td>"
								+ doiGio(tgd) + "</td><td>" + doiGio(tgl)
								+ "</td><td>" + lxlt + "</td></tr>";
					} else {
						for (int sizeInt = 0; sizeInt < sizeDriver; sizeInt++) {
							int slvt = 0;
							int dungDo = 0;
							int tgd = 0;
							// int moCua = 0;
							double quangDuong = 0;
							double vttb = 0;
							double vttd = 0;
							int tgl = 0;
							int lxlt = 0;

							data = objcmr.baoCaoTongHopTruoc(accountID, DeviceID,ngay, listDriver.get(sizeInt));

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
							thoiGianLai = thoiGianLai + tgl;

							String css = "";
							if (dem % 2 == 0)
								css = "adminTableBodyRowOdd";
							else
								css = "adminTableBodyRowEven";
							dem++;

							String date = ConvertFromEpochHour(ngay, timezone);
							chuoi = chuoi
									+ "<tr class='"
									+ css
									+ "'><td>"
									+ objcmr.getDescriptionDriver(accountID,
											listDriver.get(sizeInt))
									+ "</td><td>" + date + "</td><td>"
									+ quangDuong + "</td><td>" + slvt
									+ "</td><td>" + vttb + "</td><td>" + vttd
									+ "</td><td>" + dungDo + "</td><td>"
									+ doiGio(tgd) + "</td><td>" + doiGio(tgl)
									+ "</td><td>" + lxlt + "</td></tr>";
						}
					}
				}
				AVGvanToc = round(AVGvanToc / ((to + 86400 - fr) / 86400), 2);
				MaxVanToc = round(MaxVanToc / ((to + 86400 - fr) / 86400), 2);
				chuoi = chuoi
						+ "<tr style='color:black; font-weight:bold'><td></td><td>T&#x1ED5;ng</td><td>"
						+ round(sumQD, 2) + "</td><td>" + sumSLVT + "</td><td>"
						+ AVGvanToc + "</td><td>" + MaxVanToc + "</td><td>"
						+ sumDungDo + "</td><td>" + doiGio(thoiGianDung)
						+ "</td><td>" + doiGio(thoiGianLai) + "</td><td>"
						+ SumLXLT + "</td></tr>";
			}
		}

		catch (Exception ex) {

			chuoi = "lỗi" + ex.getMessage();
		}

		return chuoi + "</table>";
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
		final I18N i18n = privLabel.getI18N(baoCaoTongHopTruoc.class);
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
		/* Style */
		HTMLOutput HTML_CSS = new HTMLOutput() {
			public void write(PrintWriter out) throws IOException {
				String cssDir = baoCaoTongHopTruoc.this.getCssDirectory();
				WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
			}
		};

		/* javascript */
		HTMLOutput HTML_JS = new HTMLOutput() {
			public void write(PrintWriter out) throws IOException {
				MenuBar.writeJavaScript(out, pageName, reqState);
				out.println("<script type=\"text/javascript\" src=\"js/PCalendar.js\"></script>\n");
				out.println("<script type=\"text/javascript\" src=\"js/jquery-1.4.2.min.js\"></script>\n");
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
				String frameTitle = i18n.getString("baocaoTongHop.PageTitle",
						"Báo cáo tổng hợp trước");
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
						+ "'>B&#x00E1;o c&#x00E1;o t&#x1ED5;ng h&#x1EE3;p trước</span><br/>");
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

				out.println("</table>");
				out.print("<div class='viewhoz'>");
				out.print("<table cellspacing='0' style='padding:0px 0px 0px'><tbody><tr ><td width='100px'></td><td width='100px'> <input type='submit' class='button1' name='btnview' value='Tổng hợp' id='btnview'></td><td width='105px'></td><td align='left'></td><td></td></tr></tbody></table></div>");
				// String xem= AttributeTools.getRequestString(request,
				// "btnview", "");
				if (btnXem.equals("Tổng hợp")) {
					out.println("<div id='running' style='padding:7px;font-weight:bold'>Đang tổng hợp dữ liệu...<br/> Vui lòng chờ trong giây lát.</div>");
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
}