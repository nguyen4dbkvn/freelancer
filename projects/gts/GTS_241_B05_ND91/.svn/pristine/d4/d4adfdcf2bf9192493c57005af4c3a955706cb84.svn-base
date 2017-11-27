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

public class LaiXeHienTai
    extends WebPageAdaptor
    implements Constants
{
    
    // ------------------------------------------------------------------------
    // Parameters
	public  static final String COMMAND_INFO_UPDATE     = "update";
    // button types
    public  static final String PARM_BUTTON_CANCEL      = "a_btncan";
    public  static final String PARM_BUTTON_BACK        = "a_btnbak";
  
    // parameters
    //thanhtq
    public  static final String PARM_DATE_SL            = "a_date";
    public  static final String PARM_DEVICE_SL          = "a_device";
	
    
    // ------------------------------------------------------------------------
    // WebPage interface
    
    public LaiXeHienTai()
    {
    	this.setBaseURI(RequestProperties.TRACK_BASE_URI());
        this.setPageName(PAGE_LAI_XE_HIEN_TAI);
        this.setPageNavigation(new String[] { PAGE_LOGIN, PAGE_MENU_TOP });
        this.setLoginRequired(true);
    }
    
    // ------------------------------------------------------------------------
   
    public String getMenuName(RequestProperties reqState)
    {
        return MenuBar.MENU_ADMIN;
    }

    public String getMenuDescription(RequestProperties reqState, String parentMenuName)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(LaiXeHienTai.class);
        return super._getMenuDescription(reqState,i18n.getString("baoCaoTram.Menu","L&#x00E1;i xe hi&#x1EC7;n t&#x1EA1;i"));
    }
   
    public String getMenuHelp(RequestProperties reqState, String parentMenuName)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(LaiXeHienTai.class);
        return super._getMenuHelp(reqState,i18n.getString("baoCaoTram.MenuHelp","L&#x00E1;i xe hi&#x1EC7;n t&#x1EA1;i"));
    }

    // ------------------------------------------------------------------------

    public String getNavigationDescription(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(LaiXeHienTai.class);
        return super._getNavigationDescription(reqState,i18n.getString("baoCaoTram.NavDesc","L&#x00E1;i xe hi&#x1EC7;n t&#x1EA1;i"));
    }

    public String getNavigationTab(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(LaiXeHienTai.class);
        return i18n.getString("baoCaoTram.NavTab","L&#x00E1;i xe hi&#x1EC7;n t&#x1EA1;i");
    }

    // ------------------------------------------------------------------------
    
   
    public String CreateCbbDevice(String accountid, String idselect, RequestProperties reqState, PrivateLabel privLabel) throws IOException
    {
    	String strre = "<select id ='device' name = 'device' class='textReadOnly'><option value='all'>T&#x1EA5;t c&#x1EA3;</option>";
    	
    	IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
    	java.util.List<IDDescription> idList = reqState.createIDDescriptionList(false, sortBy);
        IDDescription list[] = idList.toArray(new IDDescription[idList.size()]);
     	  for (int d = 0; d < list.length ; d++) {
		        if(idselect.equalsIgnoreCase(list[d].getID()))
		          strre +="<option value ='"+list[d].getID()+"' selected =\"selected\">"+list[d].getID()+"</option>\n";
		         else
		          strre +="<option value ='"+list[d].getID()+"'>"+list[d].getID()+"</option>\n";
		     	}      
     
     strre +="</select>\n";
     strre +="<script type ='text/javascript' language ='javascript'> document.getElementById('device ').value = '"+idselect+"';</script>\n";
     return strre;
    }
    
    public String Load_LaiXeHienTai(String deviceID,String accountID,PrivateLabel privLabel,RequestProperties reqState ) throws IOException
    {
    	
    	String strscr ="";
    
    	int stt=0;
    	
    	try
    	{
    		
    		DBCamera objcmr = new  DBCamera();
    	
    		strscr =strscr+ "<div class='adminSelectTable' style='width:1000px' ><table  class='adminSelectTable_sortable' cellspacing='1'width='100%' ><thead><tr  align='center'>" +
    				"<th width='50px' class='adminTableHeaderCol_sort'>STT</th><th width='100px' class='adminTableHeaderCol_sort'>Xe</th>" +
    				"<th class='adminTableHeaderCol_sort' width='200'>L&#x00E1;i xe</th><th width='150px' class='adminTableHeaderCol_sort'>S&#x1ED1; &#x0111;i&#x1EC7;n tho&#x1EA1;i</th>" +
    				"<th class='adminTableHeaderCol_sort' width='150px'>M&#x00E3; s&#x1ED1; th&#x1EBB;</th><th class='adminTableHeaderCol_sort' width='150px'>S&#x1ED1; b&#x1EB1;ng l&#x00E1;i</th>" +
    				"<th class='adminTableHeaderCol_sort' width='150px'>Chi ti&#x1EBF;t</th></tr></thead>";

    		if(deviceID.equals("all"))
    		{	
    			IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
        	java.util.List<IDDescription> idList = reqState.createIDDescriptionList(false, sortBy);
            IDDescription list[] = idList.toArray(new IDDescription[idList.size()]);
         	  for (int d = 0; d < list.length ; d++) {
         		  
        		List<String[]> result = objcmr.select_LaiXeHienTai(list[d].getID(), accountID);
        		if(result.size()>0)
        		{
		    		for(String[] rs:result)
		    		{
		    			stt++;
		    			String css ="";
		    			if(stt%2==0)
		    				css = "adminTableBodyRowOdd";
		    			else
		    				css = "adminTableBodyRowEven";
		    			strscr =strscr+"<tr class='"+css+"'><td>"+stt+"</td><td>"+rs[0]+"</td><td>"+rs[2]+"</td><td>"+rs[1]+"</td><td>"
		    			+ rs[3]+"</td><td>"+ rs[4]+ "</td><td><a style='color:blue' href='./Track?page=menu.rpt.chiTietLaiXe&id="+rs[3]
		    			+"&device="+rs[0]+"'>Chi ti&#x1EBF;t</a></td></tr>";
		    		}
        		}
        		else
        		{
        			List<String[]> list1= objcmr.Laixehientai1(list[d].getID(), accountID);
        			for(String[] rs1:list1)
		    		{
		    			stt++;
		    			String css ="";
		    			if(stt%2==0)
		    				css = "adminTableBodyRowOdd";
		    			else
		    				css = "adminTableBodyRowEven";
		    			strscr =strscr+"<tr class='"+css+"'><td>"+stt+"</td><td>"+rs1[0]+"</td><td>"+rs1[2]+"</td><td>"+rs1[1]+"</td><td>"
		    			+ rs1[3]+"</td><td>"+ rs1[4]+ "</td><td><a style='color:blue' href='./Track?page=menu.rpt.chiTietLaiXe&id="+rs1[3]
		    			+"&device="+rs1[0]+"'>Chi ti&#x1EBF;t</a></td></tr>";
		    		}
        			
        		}
         	  }
    		}
    		else {
    			List<String[]> result = objcmr.select_LaiXeHienTai(deviceID, accountID);
    			if(result.size()>0)
        		{
		    		for(String[] rs:result)
		    		{
		    			stt++;
		    			String css ="";
		    			if(stt%2==0)
		    				css = "adminTableBodyRowOdd";
		    			else
		    				css = "adminTableBodyRowEven";
		    			strscr =strscr+"<tr class='"+css+"'><td>"+stt+"</td><td>"+rs[0]+"</td><td>"+rs[2]+"</td><td>"+rs[1]+"</td><td>"
		    			+ rs[3]+"</td><td>"+ rs[4]+ "</td><td><a style='color:blue' href='./Track?page=menu.rpt.chiTietLaiXe&id="+rs[3]
		    			+"&device="+rs[0]+"'>Chi ti&#x1EBF;t</a></td></tr>";
		    		}
        		}
        		else
        		{
        			List<String[]> list1= objcmr.Laixehientai1(deviceID, accountID);
        			for(String[] rs1:list1)
		    		{
		    			stt++;
		    			String css ="";
		    			if(stt%2==0)
		    				css = "adminTableBodyRowOdd";
		    			else
		    				css = "adminTableBodyRowEven";
		    			strscr =strscr+"<tr class='"+css+"'><td>"+stt+"</td><td>"+rs1[0]+"</td><td>"+rs1[2]+"</td><td>"+rs1[1]+"</td><td>"
		    			+ rs1[3]+"</td><td>"+ rs1[4]+ "</td><td><a style='color:blue' href='./Track?page=menu.rpt.chiTietLaiXe&id="+rs1[3]
		    			+"&device="+rs1[0]+"'>Chi ti&#x1EBF;t</a></td></tr>";
		    		}
        			
        		}
			}
    			
    		strscr =strscr+"</table></div>";
    		
    	}
    	catch (Exception e)
    	{
    	
    	}
    	return strscr;
    }

    
    public void writePage(
        final RequestProperties reqState,
        String pageMsg)
        throws IOException
    {
        final PrivateLabel privLabel = reqState.getPrivateLabel();
        final I18N    i18n     = privLabel.getI18N(LaiXeHienTai.class);
        final Locale  locale   = reqState.getLocale();
       
        final User    currUser = reqState.getCurrentUser();
        final String  pageName = this.getPageName();
        String m = pageMsg;
        boolean error = false;
        HttpServletRequest request = reqState.getHttpServletRequest();
        HttpServletResponse response =reqState.getHttpServletResponse();
      
       String contentall = AttributeTools.getRequestString(request, "device", "");
      
        /* Style */
        HTMLOutput HTML_CSS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                String cssDir = LaiXeHienTai.this.getCssDirectory();
                WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
                
            }
        };

        /* javascript */
        HTMLOutput HTML_JS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                MenuBar.writeJavaScript(out, pageName, reqState);
                out.println("        <script type=\"text/javascript\" src=\"js/PCalendar.js\"></script>\n");
                out.println("        <script type=\"text/javascript\" src=\"js/jquery-1.4.2.min.js\"></script>\n");
              
                
            }
        };

        /* Content */
       
        
        HTMLOutput HTML_CONTENT  = new HTMLOutput(CommonServlet.CSS_CONTENT_FRAME, m) {
            public void write(PrintWriter out) throws IOException {
              
                String chgURL  = privLabel.getWebPageURL(reqState, pageName, COMMAND_INFO_UPDATE);
               // String frameTitle = i18n.getString("LaiXeHienTai.PageTitle","L&#x00E1;i xe hi&#x1EC7;n t&#x1EA1;i");
               
                // frame content
             // view submit
                String contentall="";
                HttpServletRequest request = reqState.getHttpServletRequest();
                HttpServletResponse rp =reqState.getHttpServletResponse();
                final Account currAcct = reqState.getCurrentAccount();
                contentall = AttributeTools.getRequestString(request, "device", "");
              
                out.println("<span class='"+CommonServlet.CSS_MENU_TITLE+"'>L&#x00E1;i xe hi&#x1EC7;n t&#x1EA1;i</span><br/>");
                out.println("<hr/>");
                out.println("<form name='AccountInfo' method='post' action='"+chgURL+"' target='_self'>\n");
              
                out.println("<table class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE+"' cellspacing='0' callpadding='0' border='0' width='100%'>\n");
              
                out.print  ("<tr>\n");
               out.print("<td width='100px' align='right'>Ch&#x1ECD;n xe:</td><td width='100px' align='right'>"+CreateCbbDevice(currAcct.getAccountID(),contentall,reqState,privLabel));
                out.print  ("</td><td><input type ='submit' id ='btnview' value ='Xem' name ='btnview' class='button'/>");
                
                out.println("<td></td></tr>");
                
                out.println("</table>");
                String xem=    AttributeTools.getRequestString(request, "btnview", "");
                if(xem!="")
                	if(xem.equals("Xem"))
                	{ 
                String load = Load_LaiXeHienTai(contentall,currAcct.getAccountID(),privLabel,reqState);
               out.print(load);
                	}
                
                /* end of form */
                out.write("<hr style='margin-bottom:5px;'>\n");
                out.write("<span style='padding-left:10px'>&nbsp;</span>\n");
            
            
            
                out.write("</form>\n");
                
                
                
                
                
                

            }
        };

        
        
        
        /* write frame */
        String onload = error? JS_alert(true,m) : null;
        CommonServlet.writePageFrame(
            reqState,
            onload,null,                // onLoad/onUnload
            HTML_CSS,                   // Style sheets
            HTML_JS,                    // JavaScript
            null,                       // Navigation
            HTML_CONTENT);              // Content

    }
    
    // ------------------------------------------------------------------------
}
