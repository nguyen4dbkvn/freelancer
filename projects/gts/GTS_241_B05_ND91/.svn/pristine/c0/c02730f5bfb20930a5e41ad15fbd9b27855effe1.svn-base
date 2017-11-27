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
//  2008/08/08  Martin D. Flynn
//     -Initial release
//  2008/08/17  Martin D. Flynn
//     -Added "Distance" title line (below "Cursor Location")
//     -Fix display of View/Edit buttons on creation of first user.
//  2008/09/01  Martin D. Flynn
//     -Added delete confirmation
//  2008/10/16  Martin D. Flynn
//     -Update with new ACL usage
//  2008/12/01  Martin D. Flynn
//     -Added ability to display multiple points
//  2009/08/23  Martin D. Flynn
//     -Convert new entered IDs to lowercase
//  2010/04/11  Martin D. Flynn
//     -Added support for drawing polygons and corridors, however, MapProvider
//      support for these features is also required (and in the case of corridors,
//      may also require add-on module support to use the corridor geozones
//      properly).
//  2011/05/13  Martin D. Flynn
//     -Added support for limiting the number of displayed vertices.
//      (see property "zoneInfo.maximumDisplayedVertices")
// ----------------------------------------------------------------------------
package org.opengts.war.track.page;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.opengts.db.tables.Account;
import org.opengts.db.tables.StatusCode;
import org.opengts.dbtools.DBConnection;
import org.opengts.dbtools.DBException;
import org.opengts.util.I18N;
import org.opengts.util.Print;
import org.opengts.util.StringTools;
import org.opengts.war.tools.MenuBar;
import org.opengts.war.tools.PrivateLabel;
import org.opengts.war.tools.RequestProperties;
import org.opengts.war.tools.WebPageAdaptor;
import org.opengts.war.track.Constants;

public class LoadLastDistanceAjax extends WebPageAdaptor implements Constants {

	public LoadLastDistanceAjax() {
		this.setBaseURI(RequestProperties.TRACK_BASE_URI());
		this.setPageName(PAGE_LAST_DISTANCE_AJAX);
		this.setPageNavigation(new String[] {});
		this.setLoginRequired(false);
	}

	// ------------------------------------------------------------------------

	public String getMenuName(RequestProperties reqState) {
		return MenuBar.MENU_ADMIN;
	}

	public String getMenuDescription(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(LoadLastDistanceAjax.class);
		return super._getMenuDescription(reqState, i18n.getString("DestinationInfo.editMenuDesc", "Allocate Jobs"));
	}

	public String getMenuHelp(RequestProperties reqState, String parentMenuName) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(LoadLastDistanceAjax.class);
		return super._getMenuHelp(reqState, i18n.getString("DestinationInfo.editMenuHelp", "Allocate Jobs"));
	}

	// ------------------------------------------------------------------------

	public String getNavigationDescription(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(LoadLastDistanceAjax.class);
		return super._getNavigationDescription(reqState,
				i18n.getString("DestinationInfo.navDesc", "Allocate Jobs"));
	}

	public String getNavigationTab(RequestProperties reqState) {
		PrivateLabel privLabel = reqState.getPrivateLabel();
		I18N i18n = privLabel.getI18N(LoadLastDistanceAjax.class);
		return super._getNavigationTab(reqState,
				i18n.getString("DestinationInfo.navTab", "Allocate Jobs"));
	}

	public void writePage(final RequestProperties reqState, String pageMsg)
			throws IOException {
		final PrivateLabel privLabel = reqState.getPrivateLabel();
		String accountId = "cd5";
		String deviceId= "speedlimit";
		String tooltip = "";
		DBConnection dbc = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {

			dbc = DBConnection.getDefaultConnection();
			String command = "SELECT distanceKM FROM `EventData` WHERE `accountID` = '"+ accountId + "' AND deviceID='"+deviceId+"' Order by timestamp DESC limit 1";
			try{
				stmt = dbc.execute(command);
				
				rs = stmt.getResultSet();
				if (rs.next()) {
					tooltip = "<span style='font-size:25px; font-weight:bold'> Last distance: " + rs.getString("distanceKM") +" Km</span>";
				}
			}catch(Exception ex){
			
			}

		} catch (Exception sqe) {
			Print.logException("GetFavouriteAlert: ", sqe.fillInStackTrace());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Throwable t) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Throwable t) {
				}
			}
			DBConnection.release(dbc);
		}
		
		HttpServletResponse res  = reqState.getHttpServletResponse();
		PrintWriter out = res.getWriter();
		out.println(tooltip);
	}


	
	protected static String formatElapsedTime(String strElapsedSec)
    {
    	long elapsedSec = Long.parseLong(strElapsedSec);
        StringBuffer sb = new StringBuffer();
        int h = (int)(elapsedSec / (60L * 60L));   // Hours
        int m = (int)((elapsedSec / 60L) % 60);    // Minutes
        int s = (int)(elapsedSec % 60);            // Seconds
        sb.append(StringTools.format(h,"0"));
        sb.append(":");
        sb.append(StringTools.format(m,"00"));
        sb.append(":");
        sb.append(StringTools.format(s,"00"));
        return sb.toString();
    }
	// ------------------------------------------------------------------------
}
