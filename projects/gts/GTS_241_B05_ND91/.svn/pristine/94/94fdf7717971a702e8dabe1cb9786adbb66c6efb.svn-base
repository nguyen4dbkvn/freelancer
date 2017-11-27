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

public class JobsManager
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
    
    public JobsManager()
    {
    	this.setBaseURI(RequestProperties.TRACK_BASE_URI());
        this.setPageName(PAGE_JOBS_MANAGER);
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
        I18N i18n = privLabel.getI18N(JobsManager.class);
        return super._getMenuDescription(reqState,i18n.getString("jobsManager.Menu","jobsManager"));
    }
   
    public String getMenuHelp(RequestProperties reqState, String parentMenuName)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(JobsManager.class);
        return super._getMenuHelp(reqState,i18n.getString("jobsManager.MenuHelp","jobsManager"));
    }

    // ------------------------------------------------------------------------

    public String getNavigationDescription(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(JobsManager.class);
        return super._getNavigationDescription(reqState,i18n.getString("jobsManager.NavDesc","jobsManager"));
    }

    public String getNavigationTab(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(JobsManager.class);
        return i18n.getString("jobsManager.NavTab","jobsManager");
    }

    // ------------------------------------------------------------------------
    
   
   
  
    
    public String LoadJobs(String tuNgay,String denNgay,String tenAccount,int type,int ChooseSort ) throws IOException
    {
    	
    	String strscr ="";
    
    	
    	
    	try
    	{
    		
    		
    		
    		 //<a href='./Track?page=menu.jobsManager&ChooseSort=2&tuNgay="+tuNgay+"&denNgay="+denNgay+"&type="+type+"'>Case no</a>
    		DBCamera objcmr = new  DBCamera();
    		//ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
    		ResultSet rs = objcmr.getJobs(tenAccount, tuNgay, denNgay, type, ChooseSort);
    		
    		strscr =strscr+  "<div class='mGrid' style='width:1220px'><table width='100%' ><tr style='font-weight:bold;color:white; background-color:#33aee7;' align='center'><td width='50px'><a  href='./Track?page=menu.jobsManager&ChooseSort=1&tuNgay="+tuNgay+"&denNgay="+denNgay+"&type="+type+"'>Case no</a></td><td ><a  href='./Track?page=menu.jobsManager&ChooseSort=2&tuNgay="+tuNgay+"&denNgay="+denNgay
    				+"&type="+type+"'>Customer Name</a></td><td >Customer Phone</td><td>Service Type</td><td ><a  href='./Track?page=menu.jobsManager&ChooseSort=3&tuNgay="+tuNgay+"&denNgay="+denNgay+"&type="+type+"'>Despatch Date/Time</a></td><td>Service Provider<td>Planed ETA</td><td>Actual ETA</td><td width='65px'>Status </td><td  width='70px'>Link</td></tr>";
    		while (rs.next())
    		{
    			
    			
    			
    			
    			strscr =strscr+"<tr align='center'><td>"+rs.getString(5)+"</td><td>"+ rs.getString(1)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(3)+"</td><td>"+rs.getString(6)+"</td><td>"+"chua co"+"</td><td>"+rs.getString(7)+"</td><td>"+rs.getString(8)+"</td><td>"+rs.getString(4)+"</td><td><a href='./Track?page=jobdetails.info&autoID="+rs.getString(5)+"'>Detail</a></td></tr>";	
    			
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
        final I18N    i18n     = privLabel.getI18N(JobsManager.class);
        final Locale  locale   = reqState.getLocale();
        final Account currAcct = reqState.getCurrentAccount();
        final User    currUser = reqState.getCurrentUser();
        final String  pageName = this.getPageName();
        String m = pageMsg;
        boolean error = false;
        
        
        
        /* Style */
        HTMLOutput HTML_CSS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                String cssDir = JobsManager.this.getCssDirectory();
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
                //Print.logStackTrace("here");

              //String menuURL = EncodeMakeURL(reqState,RequestProperties.TRACK_BASE_URI(),PAGE_MENU_TOP);
                String menuURL = privLabel.getWebPageURL(reqState, PAGE_MENU_TOP);
              //String chgURL  = EncodeMakeURL(reqState,RequestProperties.TRACK_BASE_URI(),pageName,COMMAND_INFO_UPDATE);
                String chgURL  = privLabel.getWebPageURL(reqState, pageName, COMMAND_INFO_UPDATE);
                String frameTitle = i18n.getString("baocaoTram.PageTitle","Tr&#x1EA1;m");
               
                // frame content
             // view submit
                String tuNgay="",contentall="",denNgay="";
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String d = sdf.format(now);
                Date now1 = new Date();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                String d1 = sdf1.format(now1);
                tuNgay=d;
                denNgay=d1;
                HttpServletRequest request = reqState.getHttpServletRequest();
                String  td =AttributeTools.getRequestString(request,"btnToDay","");
                String  ytd = AttributeTools.getRequestString(request,"btnYesterday","");
                String  lw =AttributeTools.getRequestString(request,"btnLastWeek","");
                String  g = AttributeTools.getRequestString(request,"btnGo","");
                String  CaseNo =AttributeTools.getRequestString(request,"ChooseSort","");
                int ChooseSort =1;
                if(CaseNo!=null)
                {
                	if(CaseNo.equals("1"))
                		ChooseSort =1;	
                	if(CaseNo.equals("2"))
                		ChooseSort =2;	
                	if(CaseNo.equals("3"))
                		ChooseSort =3;	
                	
                }
                	
                if(tuNgay==null)
                {
                	
                	return ;
                }
                
               
               
                String  sdate = request.getParameter("tuNgay");
                if(sdate !=null)
                {
                	if(sdate !="")
                	{
                		tuNgay = sdate;
                	}
                }
                
                String  sdateTo = request.getParameter("denNgay");
                if(sdateTo !=null)
                {
                	if(sdateTo !="")
                	{
                		denNgay = sdateTo;
                	}
                }
                
               
               // String sql = LoadImage(ngayxem, contentall, pindex, pagesize,currAcct.getAccountID());
               // String LoadTram(String tuNgay,String denNgay,String bienXe ,String Tram,String tenAccount,int currentPage,int Size )
                
                
               
                out.println("<span class='"+CommonServlet.CSS_MENU_TITLE+"'>Jobs Manager</span><br/>");
                out.println("<hr/>");
                out.println("<form name='AccountInfo' method='post' action='"+chgURL+"' target='_self'>\n");
              
                out.println("<table class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE+";font-weight:bold;font-family:arial; font-size:14px' cellspacing='10' callpadding='0' width='100%'>\n");
                
                out.print  ("<tr>\n");
                
                out.print  ("<td width=\"200px\">\n");
                out.print( "<input id='Text1' name='tuNgay' type='text' style=\"width:130px;height:20px; font-size:14px;font-family:arial\"  onclick=\"displayCalendar(this,'yyyy/mm/dd',this,false,'','Text1')\" value='"+d+"' /></td>");
              
                out.print  ("<td >\n");
                out.print( "<input id='Text2' name='denNgay' type='text' style=\"width:130px;height:20px; font-size:14px;font-family:arial\"  onclick=\"displayCalendar(this,'yyyy/mm/dd',this,false,'','Text2')\" value='"+d1+"' /></td>");
                out.print  ("<td style=\"font-family:arial; font-size:14px; font-weight:bold\">Get By Date Range\n</td><td>");
                 out.print( "<span style=\"font-family:arial; font-size:14px; font-weight:bold\">Remember Sorting:</span>\n");
                out.print  ("<input type='checkbox' name='Sort' value='Sort'>");
               
                out.print  ("</td>");
                out.println("</tr><tr>");
                out.print  ("<td colspan='2'> <input type ='submit' id ='btnToDay' value ='Today' name ='btnToDay' class='nutBamMinhnv'/>&nbsp;&nbsp;&nbsp;");
                out.print  (" <input type ='submit' id ='btnYesterday' value ='Yesterday' name ='btnYesterday' class='nutBamMinhnv'/>&nbsp;&nbsp;&nbsp;");
                out.print  (" <input type ='submit' id ='btnLastWeek' value ='LastWeek' name ='btnLastWeek' class='nutBamMinhnv'/></td>");
                out.print  ("<td> <input type ='submit' id ='btnGo' value ='Go' name ='btnGo' class='nutBamMinhnv'/></td><td style=\"font-family:arial; font-size:14px; font-weight:bold\">");
                //out.print  ("<tr>");
                //out.print  ("<td colspan='2' style ='height:400px; border:solid 1px silver;' >"+sql +currAcct.getAccountID()+ngayxem+"</td>");
                //out.println("</tr>");
                out.print("Auto Refresh:<select id ='auto' name='auto'><option value='on' >On</option><option value='off' >Off</option></select>");
               
                out.println("</td></tr></table>");
              int type=4;
              if(td!=null&&td!="")
              {
            	  String s1 = new String("Today");

                if(td.equals(s1))
                {
             	   type =1;
             	   
                }}
              if(ytd!=null&&ytd!="")
              {
            	  String s2 = new String("Yesterday");
                if(ytd.equals(s2))
                {
                	type =3;
             	   
                }}
              if(lw!=null&&lw!="")
              {
            	  String s3 = new String("LastWeek");
                  if(lw.equals(s3))
                
                {
                	type =2;
             	   
                }}
              if(g!=null&&g!="")
              {
            	  String s4 = new String("Go");
                if(g.equals(s4))
                {
                	type =4;
             	   
                }}
              String type1 =AttributeTools.getRequestString(request,"type","");
              int kieu=1;
              if(type1=="")
              {
                 String sql = LoadJobs( tuNgay,denNgay,currAcct.getAccountID(),type,ChooseSort );
               
              out.print(sql);
              }
              else
              {
            	  if(type1.equals("1"))
            		  kieu=1;
            	  if(type1.equals("2"))
            		  kieu=2;
            	  if(type1.equals("3"))
            		  kieu=3;
            	  if(type1.equals("4"))
            		  kieu=4;
            	  String sql = LoadJobs( tuNgay,denNgay,currAcct.getAccountID(),kieu,ChooseSort );
                  
                  out.print(sql); 
              }
              out.print("<script type ='text/javascript' language ='javascript'> document.getElementById('Text1').value = '"+tuNgay+"';</script>\n");
              out.print("<script type ='text/javascript' language ='javascript'> document.getElementById('Text2').value = '"+denNgay+"';</script>\n");
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
