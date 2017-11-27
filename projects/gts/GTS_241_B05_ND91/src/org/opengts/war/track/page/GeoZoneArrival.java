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

import javax.servlet.*;
import javax.servlet.http.*;

import org.opengts.util.*;
import org.opengts.dbtools.*;
import org.opengts.db.*;
import org.opengts.db.tables.*;

import org.opengts.war.tools.*;
import org.opengts.war.track.*;

public class GeoZoneArrival
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
    
    public GeoZoneArrival()
    {
    	this.setBaseURI(RequestProperties.TRACK_BASE_URI());
        this.setPageName(PAGE_GEOZONEARRIVAL);
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
        I18N i18n = privLabel.getI18N(GeoZoneArrival.class);
        return super._getMenuDescription(reqState,i18n.getString("GeoZoneArrival.Menu","Quản lý trạm"));
    }
   
    public String getMenuHelp(RequestProperties reqState, String parentMenuName)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(GeoZoneArrival.class);
        return super._getMenuHelp(reqState,i18n.getString("GeoZoneArrival.MenuHelp","Quản lý trạm"));
    }

    // ------------------------------------------------------------------------

    public String getNavigationDescription(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(GeoZoneArrival.class);
        return super._getNavigationDescription(reqState,i18n.getString("GeoZoneArrival.NavDesc","Quản lý trạm"));
    }

    public String getNavigationTab(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(GeoZoneArrival.class);
        return i18n.getString("GeoZoneArrival.NavTab","Quản lý trạm");
    }

    // ------------------------------------------------------------------------
    
   
    public String chonTram(String Acc,String idselect)
    {
    	String strre = "<select id ='device' name = 'device' class='textReadOnly' style='width:100px'>";
    	try 
    	{
    		DBCamera objcmr = new DBCamera();
    		List<String[]> result = objcmr.getZone(Acc);
    		for(String[] rs:result)
    		{
    			if(idselect == rs[0])
    				strre +="<option value ='"+rs[0]+"' selected =\"selected\">"+rs[1]+"</option>\n";
    			else
    				strre +="<option value ='"+rs[0]+"'>"+rs[1]+"</option>\n";
    		}
    	}
    	catch (Exception e)
    	{
    		
    	}
    	strre +="</select>\n";
    	strre +="<script type ='text/javascript' language ='javascript'> document.getElementById('device').value = '"+idselect+"';</script>\n";
    	return strre;
    	
    }
   public String gio()
   {
	   String srt ="<select name ='gio' class='textReadOnly' width='100px'>";
	   for(int i=0;i<24;i++)
		   if(i>9)
		   srt =srt+"<option value='"+i+"'>"+i+"</option>";
		   else
			   srt =srt+"<option value='"+i+"'>0"+i+"</option>";
	   srt =srt+"</select>";
	   return srt;
   }  
   public String phut()
   {
	   String srt ="<select name ='phut' class='textReadOnly' width='100px'>";
	   for(int i=0;i<60;i ++)
		   if(i>9)
		   srt =srt+"<option value='"+i+"'>"+i+"</option>";
		   else
			   srt =srt+"<option value='"+i+"'>0"+i+"</option>";
	   srt =srt+"</select>";
	   return srt;
   }
    public String LoadGeoZoneArrival(String tenAccount) throws IOException
    {
    	
    	String strscr ="";
    
    	
    	
    	try
    	{
    		
    		
    		
    		int num = 0;
    		DBCamera objcmr = new  DBCamera();
    		//ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
    		ResultSet rs = objcmr.getGeoZoneArrival(tenAccount);
    		
    		strscr =strscr+ "<div class='adminSelectTable'><table id='myTable' width='100%' class='adminSelectTable_sortable' cellspacing='1'><thead><tr align='center'><th width='100px' class='adminTableHeaderCol_sort'>Ch&#x1ECD;n</th><th width='400px' class='adminTableHeaderCol_sort'>Tr&#x1EA1;m</th><th class='adminTableHeaderCol_sort'>Th&#x1EDD;i gian</th><th class='adminTableHeaderCol_sort'>&#x0110;i/&#x0111;&#x1EBF;n</th></tr></thead>";
    		while (rs.next())
    		{
    			
    			int tg =Integer.parseInt(rs.getString(3));
    			int gio =tg/3600;
    			int phut =(tg%3600)/60;
    			String css ="";
    			if(num%2==0)
    				css = "adminTableBodyRowOdd";
    			else
    				css = "adminTableBodyRowEven";
    			
    			num++;
    			//strscr =strscr+"<tr><td><input type='radio' name='group1' value="+rs.getString(1)+"></td><td>"+rs.getString(4)+"</td><td>"+Integer.toString(gio) +":"+Integer.toString(phut)+"</td><td>"+rs.getString(5)+"</td></tr>";	
    			if(gio>9)
    			{
    				if(phut>9)
    			strscr =strscr+"<tr  class ="+css+"><td><input type='radio' name='group1' value="+rs.getString(1)+"></td><td>"+rs.getString(4)+"</td><td>"+Integer.toString(gio) +":"+Integer.toString(phut)+"</td><td>"+rs.getString(5)+"</td></tr>";	
    			//strscr =strscr+"<tr><td><input type='radio' name='group1' value="+rs.getString(1)+"/></td><td>"+rs.getString(4)+"</td><td>"+rs.getString(3)+"</td></tr>";	
    				else
    					strscr =strscr+"<tr  class ="+css+"><td><input type='radio' name='group1' value="+rs.getString(1)+"></td><td>"+rs.getString(4)+"</td><td>"+Integer.toString(gio) +":0"+Integer.toString(phut)+"</td><td>"+rs.getString(5)+"</td></tr>";	
    			}
    			else
    			{
    				if(phut>9)
    	    			strscr =strscr+"<tr  class ="+css+"><td><input type='radio' name='group1' value="+rs.getString(1)+"></td><td>"+rs.getString(4)+"</td><td>0"+Integer.toString(gio) +":"+Integer.toString(phut)+"</td><td>"+rs.getString(5)+"</td></tr>";	
    	    			//strscr =strscr+"<tr><td><input type='radio' name='group1' value="+rs.getString(1)+"/></td><td>"+rs.getString(4)+"</td><td>"+rs.getString(3)+"</td></tr>";	
    	    				else
    	    					strscr =strscr+"<tr  class ="+css+"><td><input type='radio' name='group1' value="+rs.getString(1)+"></td><td>"+rs.getString(4)+"</td><td>0"+Integer.toString(gio) +":0"+Integer.toString(phut)+"</td><td>"+rs.getString(5)+"</td></tr>";	
    				
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
        final I18N    i18n     = privLabel.getI18N(GeoZoneArrival.class);
        final Locale  locale   = reqState.getLocale();
        final Account currAcct = reqState.getCurrentAccount();
        final User    currUser = reqState.getCurrentUser();
        final String  pageName = this.getPageName();
        String m = pageMsg;
        boolean error = false;
        
        
        
        /* Style */
        HTMLOutput HTML_CSS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                String cssDir = GeoZoneArrival.this.getCssDirectory();
                WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
            }
        };

        /* javascript */
        HTMLOutput HTML_JS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                MenuBar.writeJavaScript(out, pageName, reqState);
                out.println("        <script type=\"text/javascript\" src=\"js/PCalendar.js\"></script>\n");
                out.println("        <script type=\"text/javascript\" src=\"js/jquery-1.4.2.min.js\"></script>\n");
                out.println( " <script type='text/javascript' src='js/jquery.tablesorter.min.js'></script>");
                out.println("<script type='text/javascript' src='js/sorttable.js'></script>");
                out.println("<script type='text/javascript' > $(function(){$('#myTable').tablesorter(); }); </script>");
                
            }
        };

        /* Content */
       
        
        HTMLOutput HTML_CONTENT  = new HTMLOutput(CommonServlet.CSS_CONTENT_FRAME, m) {
            public void write(PrintWriter out) throws IOException {
                //Print.logStackTrace("here");

              //String menuURL = EncodeMakeURL(reqState,RequestProperties.TRACK_BASE_URI(),PAGE_MENU_TOP);
                String menuURL = privLabel.getWebPageURL(reqState, PAGE_MENU_TOP);
              //String chgURL  = EncodeMakeURL(reqState,RequestProperties.TRACK_BASE_URI(),pageName,COMMAND_INFO_UPDATE);
                String chgURL  = privLabel.getWebPageURL(reqState, pageName, COMMAND_INFO_UPDATE);
                String frameTitle = i18n.getString("GeoZoneArrival.PageTitle","GeoZone Arrival");
               
                // frame content
             // view submit
                String contentall="";
                HttpServletRequest request = reqState.getHttpServletRequest();
                //ngayxem = AttributeTools.getRequestString(request, "ngay", "");
                contentall = AttributeTools.getRequestString(request, "device", "");
                
               
                out.println("<span class='"+CommonServlet.CSS_MENU_TITLE+"'>Geozone Arrival</span><br/>");
                out.println("<hr/>");
                out.println("<form name='Geozone Arrival' method='post' action='"+chgURL+"' target='_self'>\n");
              
                out.println("<table class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE+"' cellspacing='0' callpadding='0' border='0' width='100%' style='padding:15 0 15'>\n");
                
                out.print  ("<tr>\n");
               
                out.print  ("<td align='right' width='100px'>"+i18n.getString("DeviceSelect","<b>Ch&#x1ECD;n Tr&#x1EA1;m:</b>")+"</td><td width='100px' align='left'>\n");
                out.print  (chonTram(currAcct.getAccountID(),contentall));
                out.print("</td><td align='right' width='50'><b>Gi&#x1EDD;:</b></td><td  width='60'>"+gio()+"</td><td  width='100'><b>Ph&#x00FA;t:</b>"+phut()
                		+"</td><td width='20'><input type='radio' name='kieu' value='61968'/></td><td width='20'><b>&#x0110;i</b></td><td width='20'><input type='radio' name='kieu' value='62000'/></td><td align='left'><b>&#x0110;&#x1EBF;n</b></td>");
                out.println("</tr>");
                //out.print  ("<tr>");
                //out.print  ("<td colspan='2' style ='height:400px; border:solid 1px silver;' >"+sql +currAcct.getAccountID()+ngayxem+"</td>");
                //out.println("</tr>");
                
                out.println("</table>");
              
                
                out.print  ("<table  cellspacing='0' style='width:100%;padding:10px 0px 10px' ><tr class='viewhoz'><td width='100px'></td><td width='100px'><input type ='submit' id ='btnAdd' value ='Thêm mới' name ='btnAdd' class='button1'/></td>");
                out.print  ("<td width='100px'></td><td width='100px'><input type ='submit' id ='btnEdit' value ='Sửa' name ='btnEdit' class='button1'/></td>");
                out.print  ("<td width='100px'></td><td align='left'><input type ='submit' id ='btnDelete' value ='Xóa' name ='btnDelete' class='button1'/></td></tr></table>");
                
                
              //  String load = LoadTram(ngayxem,contentall,currAcct.getAccountID());
               // out.print(load);
                
                
                /* end of form */
                
                out.write("<hr style='margin-bottom:5px;'>\n");
                out.write("<span style='padding-left:10px'>&nbsp;</span>\n");
                
                String Add="",Edit="",Delete="",autoID="",gio="",phut="",kieu="";
                Add = AttributeTools.getRequestString(request, "btnAdd", "");
               Edit = AttributeTools.getRequestString(request, "btnEdit", "");
              Delete = AttributeTools.getRequestString(request, "btnDelete", "");
              autoID = AttributeTools.getRequestString(request, "group1", "");
             gio = AttributeTools.getRequestString(request, "gio", "");
             phut = AttributeTools.getRequestString(request, "phut", "");
             kieu = AttributeTools.getRequestString(request, "kieu", "");
             
                int thoigian =1;
                try{
                	thoigian =Integer.parseInt(gio)*3600+Integer.parseInt(phut)*60;
                	
                }catch(Exception e)
                {}
                int type =1;
                try{
                type= Integer.parseInt(kieu);
                }catch(Exception e){}
                if(Add!="")
                	if(Add.equals("Thêm mới"))
                	{
                		DBCamera objcmr = new DBCamera();
                		objcmr.insertGeoZoneArrival(contentall,thoigian ,currAcct.getAccountID(),type);
                		 
                        // out.print(load);
                        // out.print(autoID);
                	}
                if(Delete!="")
                	if(Delete.equals("Xóa"))
                	{
                		//out.print("minh");
                		int id =1;
                		try{
                			id=Integer.parseInt(autoID);
                			
                		}catch(Exception e ){};
                		DBCamera objcmr = new DBCamera();
                		objcmr.DeleteGeoZoneArrival(id);
                		
                        // out.print(load);
                         //out.print(autoID);
                         
                	}
                
                if(Edit!="")
                	if(Edit.equals("Sửa"))
                	{
                		//out.print("minh");
                		int id =1;
                		try{
                			id=Integer.parseInt(autoID);
                			
                		}catch(Exception e ){};
                		DBCamera objcmr = new DBCamera();
                		objcmr.updateGeoZoneArrival(contentall,thoigian,id,type);
                		
                        // out.print(load);
                         //out.print(autoID);
                         
                	}
                String load = LoadGeoZoneArrival(currAcct.getAccountID());
                 out.print(load);
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
