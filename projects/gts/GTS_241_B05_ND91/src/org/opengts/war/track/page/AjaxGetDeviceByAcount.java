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

import java.sql.ResultSet;
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
import org.opengts.db.tables.EventData;

public class AjaxGetDeviceByAcount
extends WebPageAdaptor
implements Constants
{

// ------------------------------------------------------------------------
// WebPage interface
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // Reports: "device.detail"
    //  - Event Detail
    //  - Temperature Monitoring
    //  - J1708 Fault codes

    public AjaxGetDeviceByAcount()
    {
        this.setBaseURI(RequestProperties.TRACK_BASE_URI());
        this.setPageName(PAGE_MENU_GETDEVICE_AJAX);
        this.setPageNavigation(new String[] { PAGE_LOGIN, PAGE_MENU_TOP });
        this.setLoginRequired(true);
        //this.setReportType(ReportFactory.REPORT_TYPE_DEVICE_DETAIL);
    }

    // ------------------------------------------------------------------------

    public String getMenuName(RequestProperties reqState)
    {
        return MenuBar.MENU_REPORTS_DEVDETAIL;
    }

    public String getMenuDescription(RequestProperties reqState, String parentMenuName)
    {
        PrivateLabel privLabel   = reqState.getPrivateLabel();
        I18N         i18n        = privLabel.getI18N(AjaxGetDeviceByAcount.class);
        String       devTitles[] = reqState.getDeviceTitles();
        return super._getMenuDescription(reqState,i18n.getString("BaocaoNhienLieuFuel.menuDesc","BC", devTitles));
    }

    public String getMenuHelp(RequestProperties reqState, String parentMenuName)
    {
        PrivateLabel privLabel   = reqState.getPrivateLabel();
        I18N         i18n        = privLabel.getI18N(AjaxGetDeviceByAcount.class);
        String       devTitles[] = reqState.getDeviceTitles();
        return super._getMenuHelp(reqState,i18n.getString("BaocaoNhienLieuFuel.menuHelp","BC", devTitles));
    }

    // ------------------------------------------------------------------------

    public String getNavigationDescription(RequestProperties reqState)
    {
        PrivateLabel privLabel   = reqState.getPrivateLabel();
        I18N         i18n        = privLabel.getI18N(AjaxGetDeviceByAcount.class);
        String       devTitles[] = reqState.getDeviceTitles();
        return super._getNavigationDescription(reqState,i18n.getString("BaocaoNhienLieuFuel.navDesc","BC", devTitles));
    }

    public String getNavigationTab(RequestProperties reqState)
    {
        PrivateLabel privLabel   = reqState.getPrivateLabel();
        I18N         i18n        = privLabel.getI18N(AjaxGetDeviceByAcount.class);
        String       devTitles[] = reqState.getDeviceTitles();
        return super._getNavigationTab(reqState,i18n.getString("BaocaoNhienLieuFuel.navTab","BC", devTitles));
    }
    
    public String CreateDeviceCombo(String Account)
			throws IOException {
		String strre = "";
		try {
		DBCamera db = new DBCamera();
		ArrayList<String> list = db.GetDeviceByAccount(Account);
		String valueFirst = "";
		valueFirst = list.get(0).toString();
		strre = "<input id='hidDevice' name='hidDevice' type ='hidden' value='" + valueFirst + "' /></select>\n";
		strre += "<select id ='device' style='width:130px;'>";
			for (int d = 0; d < list.size(); d++) {
					strre += "<option value ='" + list.get(d).toString() + "'>"
							+ list.get(d).toString() + "</option>\n";
			}
		

		}catch (Exception e) {
			e.printStackTrace();
		}
		return strre;
	}
   
    public void writePage(final RequestProperties reqState, String pageMsg)
			throws IOException {
		final HttpServletRequest request = reqState.getHttpServletRequest();
		final PrivateLabel privLabel = reqState.getPrivateLabel(); // never null
		final I18N i18n = privLabel.getI18N(AjaxGetDeviceByAcount.class);
		final Locale locale = reqState.getLocale();
		final Account currAcct = reqState.getCurrentAccount(); // never null
		final User currUser = reqState.getCurrentUser(); // may be null
		final String pageName = this.getPageName();
		
	
		String accid = AttributeTools.getRequestString(request, "accid", "");
	
		HttpServletResponse res = reqState.getHttpServletResponse();
		PrintWriter out = res.getWriter();
		String sql = CreateDeviceCombo(accid);
		
		out.println(sql);
    
    // ------------------------------------------------------------------------

}
}
