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

import java.util.Date;
import java.util.List;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.io.*;

import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.opengts.util.*;
import org.opengts.dbtools.*;
import org.opengts.db.*;
import org.opengts.db.tables.*;

import org.opengts.war.tools.*;
import org.opengts.war.track.*;

public class chiTietLaiXe extends WebPageAdaptor implements Constants {

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

	public chiTietLaiXe() {
		this.setBaseURI(RequestProperties.TRACK_BASE_URI());
		this.setPageName(PAGE_CHI_TIET_LAI_XE);
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
		I18N i18n = privLabel.getI18N(chiTietLaiXe.class);
		return super._getMenuDescription(reqState, i18n.getString(
				"chiTietLaiXe.Menu", "Chi ti&#x1EBF;t l&#x00E1;i xe"));
	}

	public String getMenuHelp(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(chiTietLaiXe.class);
		return super._getMenuHelp(reqState, i18n.getString(
				"chiTietLaiXe.MenuHelp", "Chi ti&#x1EBF;t l&#x00E1;i xe"));
	}

	// ------------------------------------------------------------------------

	public String getNavigationDescription(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(chiTietLaiXe.class);
		return super._getNavigationDescription(reqState, i18n.getString(
				"chiTietLaiXe.NavDesc", "Chi ti&#x1EBF;t l&#x00E1;i xe"));
	}

	public String getNavigationTab(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(chiTietLaiXe.class);
		return i18n.getString("chiTietLaiXe.NavTab",
				"Chi ti&#x1EBF;t l&#x00E1;i xe");
	}

	// ------------------------------------------------------------------------

	private static String FormatDayNumber(long dn) {
		if (dn < 0L) {
			return "";
		} else {
			return (new DayNumber(dn)).format(DayNumber.DATE_FORMAT_2);
		}
	}

	public void writePage(final RequestProperties reqState, String pageMsg)
			throws IOException {
		final PrivateLabel privLabel = reqState.getPrivateLabel();
		final I18N i18n = privLabel.getI18N(chiTietLaiXe.class);
		final Locale locale = reqState.getLocale();

		final User currUser = reqState.getCurrentUser();
		final String pageName = this.getPageName();
		String m = pageMsg;
		boolean error = false;
		HttpServletRequest request = reqState.getHttpServletRequest();
		HttpServletResponse response = reqState.getHttpServletResponse();

		String contentall = AttributeTools.getRequestString(request, "device",
				"");

		/* Style */
		HTMLOutput HTML_CSS = new HTMLOutput() {
			public void write(PrintWriter out) throws IOException {
				String cssDir = chiTietLaiXe.this.getCssDirectory();
				WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
			}
		};

		/* javascript */
		HTMLOutput HTML_JS = new HTMLOutput() {
			public void write(PrintWriter out) throws IOException {
				MenuBar.writeJavaScript(out, pageName, reqState);
				out.println("        <script type=\"text/javascript\" src=\"js/PCalendar.js\"></script>\n");
				out.println("        <script type=\"text/javascript\" src=\"js/jquery-1.4.2.min.js\"></script>\n");
				// JavaScriptTools.writeJSInclude(out,
				// JavaScriptTools.qualifyJSFileRef("PCalendar.js"));
				// JavaScriptTools.writeJSInclude(out,
				// JavaScriptTools.qualifyJSFileRef("jquery-1.4.2.min.js"));
				// JavaScriptTools.writeJSInclude(out,
				// JavaScriptTools.qualifyJSFileRef("PopImage.js"));

			}
		};

		/* Content */

		HTMLOutput HTML_CONTENT = new HTMLOutput(
				CommonServlet.CSS_CONTENT_FRAME, m) {
			public void write(PrintWriter out) throws IOException {
				// Print.logStackTrace("here");

				// String menuURL =
				// EncodeMakeURL(reqState,RequestProperties.TRACK_BASE_URI(),PAGE_MENU_TOP);
				// String menuURL = privLabel.getWebPageURL(reqState,
				// PAGE_MENU_TOP);
				// String chgURL =
				// EncodeMakeURL(reqState,RequestProperties.TRACK_BASE_URI(),pageName,COMMAND_INFO_UPDATE);
				String chgURL = privLabel.getWebPageURL(reqState, pageName,
						COMMAND_INFO_UPDATE);
				// String frameTitle =
				// i18n.getString("LaiXeHienTai.PageTitle","L&#x00E1;i xe hi&#x1EC7;n t&#x1EA1;i");

				// frame content
				// view submit

				HttpServletRequest request = reqState.getHttpServletRequest();
				final Account currAcct = reqState.getCurrentAccount();
				String DriverID = AttributeTools.getRequestString(request,
						"id", "");
				String bienXe = AttributeTools.getRequestString(request,
						"device", "");
				DBCamera objcmr = new DBCamera();
				List<String[]> result = objcmr.selectDriverbyDriverID(DriverID,
						currAcct.getAccountID());
				out.println("<span class='"
						+ CommonServlet.CSS_MENU_TITLE
						+ "'>Th&#x00F4;ng tin l&#x00E1;i xe hi&#x1EC7;n t&#x1EA1;i c&#x1EE7;a xe "
						+ bienXe + "</span><br/>");
				out.println("<hr/>");
				out.println("<form name='AccountInfo' method='post' action='"
						+ chgURL + "' target='_self'>\n");
				try {
					out.print("<table>");
					for (String[] rs : result) {
						out.print("<tr><td align='right'><b>H&#x1ECD; t&#x00EA;n:</b></td><td align='left'>"
								+ rs[10] + "</td></tr>");
						out.print("<tr><td align='right'><b>S&#x1ED1; &#x0111;i&#x1EC7;n tho&#x1EA1;i:</b></td><td align='left'>"
								+ rs[2] + "</td></tr>");
						out.print("<tr><td align='right'><b>Email:</b></td><td align='left'>"
								+ rs[3] + "</td></tr>");
						out.print("<tr><td align='right'><b>&#x0110;&#x1ECB;a ch&#x1EC9;:</b></td><td align='left'>"
								+ rs[8] + "</td></tr>");
						out.print("<tr><td align='right'><b>Ng&#x00E0;y sinh:</b></td><td align='left'>"
								+ FormatDayNumber(Long.parseLong(rs[9]))
								+ "</td></tr>");
						out.print("<tr><td align='right'><b>Lo&#x1EA1;i gi&#x1EA5;y ph&#x00E9;p:</b></td><td align='left'>"
								+ rs[4] + "</td></tr>");
						out.print("<tr><td align='right'><b>Gi&#x1EA5;y ph&#x00E9;p s&#x1ED1;:</b></td><td align='left'>"
								+ rs[5] + "</td></tr>");
						out.print("<tr><td align='right'><b>M&#x00F4; t&#x1EA3;:</b></td><td align='left'>"
								+ rs[11] + "</td></tr>");
						out.print("<tr><td align='right'><b>Ch&#x00FA; th&#x00ED;ch:</b></td><td align='left'>"
								+ rs[12] + "</td></tr>");
						out.print("<tr><td align='right'><b>L&#x1EA7;n cu&#x1ED1;i c&#x1EAD;p nh&#x1EAD;t:</b></td><td align='left'>"
								+ rs[13] + "</td></tr>");
						out.print("<tr><td align='right'><b>Ng&#x00E0;y t&#x1EA1;o:</b></td><td align='left'>"
								+ rs[14] + "</td></tr>");
						out.print("<tr><td align='right'><b>LicenseExpire:</b></td><td align='left'>"
								+ FormatDayNumber(Long.parseLong(rs[6]))
								+ "</td></tr>");
						out.print("<tr><td align='right'><b>LadgeID:</b></td><td align='left'>"
								+ rs[7] + "</td></tr>");
					}
					out.print("</table>");
				} catch (Exception ex) {
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
