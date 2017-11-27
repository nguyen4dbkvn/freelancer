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
import java.util.List;
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

public class BaocaoNhienLieuFuel
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
public static int record							= 0;

// ------------------------------------------------------------------------
// WebPage interface
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // Reports: "device.detail"
    //  - Event Detail
    //  - Temperature Monitoring
    //  - J1708 Fault codes

    public BaocaoNhienLieuFuel()
    {
        this.setBaseURI(RequestProperties.TRACK_BASE_URI());
        this.setPageName(PAGE_MENU_RPT_FUEL);
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
        I18N         i18n        = privLabel.getI18N(BaocaoNhienLieuFuel.class);
        String       devTitles[] = reqState.getDeviceTitles();
        return super._getMenuDescription(reqState,i18n.getString("BaocaoNhienLieuFuel.menuDesc","BC", devTitles));
    }

    public String getMenuHelp(RequestProperties reqState, String parentMenuName)
    {
        PrivateLabel privLabel   = reqState.getPrivateLabel();
        I18N         i18n        = privLabel.getI18N(BaocaoNhienLieuFuel.class);
        String       devTitles[] = reqState.getDeviceTitles();
        return super._getMenuHelp(reqState,i18n.getString("BaocaoNhienLieuFuel.menuHelp","BC", devTitles));
    }

    // ------------------------------------------------------------------------

    public String getNavigationDescription(RequestProperties reqState)
    {
        PrivateLabel privLabel   = reqState.getPrivateLabel();
        I18N         i18n        = privLabel.getI18N(BaocaoNhienLieuFuel.class);
        String       devTitles[] = reqState.getDeviceTitles();
        return super._getNavigationDescription(reqState,i18n.getString("BaocaoNhienLieuFuel.navDesc","BC", devTitles));
    }

    public String getNavigationTab(RequestProperties reqState)
    {
        PrivateLabel privLabel   = reqState.getPrivateLabel();
        I18N         i18n        = privLabel.getI18N(BaocaoNhienLieuFuel.class);
        String       devTitles[] = reqState.getDeviceTitles();
        return super._getNavigationTab(reqState,i18n.getString("BaocaoNhienLieuFuel.navTab","BC", devTitles));
    }
	 // ------------------------------------------------------------------------
	 	public boolean isOkToDisplay(RequestProperties reqState) {
	 		Account account = (reqState != null) ? reqState.getCurrentAccount()
	 				: null;
	 		if (account.getAccountID().equals("fuel")) {
	 			return true; // 
	 		} else {
	 			return false;
	 		}
	 	}
    // ---s---------------------------------------------------------------------
    String accfirst="";
    public String CreateCbbAccount( String idselect) throws IOException
    {
    	String strre = "<select id ='AccID' name = 'AccID' class='textReadOnly' style='width:130px;'>";
    	try 
    	{
    		DBCamera objcmr = new DBCamera();
    		ArrayList<String> list = objcmr.GetAccount();
    		accfirst=list.get(0).toString();
    		for (int d = 0; d < list.size(); d++) {
    			strre += "<option value ='" + list.get(d).toString() + "'>"
						+ list.get(d).toString() + "</option>\n";
    		}
    	}
    	catch (Exception e)
    	{
    		
    	}
    	strre +="</select>\n";
    	strre +="<script type ='text/javascript' language ='javascript'> document.getElementById('AccID').value = '"+idselect+"';" +
    			"document.getElementById('hidAccount').value = '"+idselect+"';</script>\n";
    	
    	return strre;
    }
    
    public String CreateCbbDevice(String accountid, String idselect, RequestProperties reqState, PrivateLabel privLabel) throws IOException
        {
            
         String strre = "<select id ='device' name = 'device' class='textReadOnly' style='width:130px;'>";
         DBCamera objcmr = new DBCamera();
	 		ArrayList<String> list = objcmr.GetDeviceByAccount(accountid);
	 		for (int d = 0; d < list.size(); d++) {
	 			strre += "<option value ='" + list.get(d).toString() + "'>"
							+ list.get(d).toString() + "</option>\n";
	 		}   
         
         strre +="</select>\n";
         strre +="<script type ='text/javascript' language ='javascript'> document.getElementById('device').value = '"+idselect+"';" +
         		" document.getElementById('hidDevice').value = '"+idselect+"';</script>\n";
         return strre;
        }
 
    public String doiGio(long tg)
    {
        String chuoi = "";
        long gio = tg / 3600;
        long phut = (tg % 3600) / 60;
        long giay = tg - gio * 3600 - phut * 60;
        String h="",m="",s="";
       
        h = Long.toString(gio);
       
        m = Long.toString(phut);
       
        s = Long.toString(giay);
        if(gio>0)
        	chuoi=h+" giờ, ";
        if(phut>0)
        	chuoi+=m+" phút, ";
        if(giay>0)
        	chuoi+=s + " giây";
        return chuoi ;
    
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
    private String ConvertFromEpoch(long epoch, String timezone){
    	  TimeZone tz = DateTime.getTimeZone(timezone, null);
    	  DateTime dt = new DateTime(epoch);
    	        String dtFmt = dt.format("dd/MM/yyyy HH:mm:ss", tz);
    	  return dtFmt;
    	 }
    public String report(String accid,String devid,String tuNgay,String denNgay,String timezone,PrivateLabel privLabel) throws IOException
    {
    	String strscr ="";
    	
    	try
    	{
    		//int num = 0,i=0;
    		//record=0;
    		DBCamera db = new  DBCamera();
    		List<String[]> list = db.SelectFromtblFuel(accid, devid, tuNgay, denNgay, timezone);

    		strscr =strscr+ "<table  id='myTable' width='100%' class='adminSelectTable_sortable' cellspacing='1' ><thead><tr  align='center'><th class='adminTableHeaderCol_sort'>STT</th><th  class='adminTableHeaderCol_sort'>Account</th>" +
    				"<th  class='adminTableHeaderCol_sort'>Xe</th><th width='130px' class='adminTableHeaderCol_sort'>Thời gian</th><th  class='adminTableHeaderCol_sort'>Địa chỉ</th>" +
    				"<th  class='adminTableHeaderCol_sort'>Thể thích bình xăng (l)</th><th  class='adminTableHeaderCol_sort'>Điện áp (V)</th><th class='adminTableHeaderCol_sort'>Điện áp thu nhận(V)</th>";
    		strscr=strscr+"</tr></thead>";
    		for (int j = 0; j < list.size(); j++) 
    		{
    			String css ="";
    			if(j%2==0)
    				css = "adminTableBodyRowOdd";
    			else
    				css = "adminTableBodyRowEven";
    		
    			strscr =strscr+"<tr class ="+css+">" +
    					"<td>"+(j+1)+"</td><td>"+list.get(j)[0] +"</td>" +
    					"<td>"+list.get(j)[1]+"</td>" +
    					"<td>"+list.get(j)[2]+"</td>" +
    					"<td>"+list.get(j)[3]+"</td>" +
    					"<td>"+list.get(j)[4]+"</td>" +
    					"<td>"+list.get(j)[5]+"</td>" +
    					"<td>"+list.get(j)[6]+"</td>";	
    			
    			strscr=strscr+"</tr>";
    		}
    		strscr =strscr+"</table>";
    	}
    	//}
    	catch (Exception e)
    	{
    	  System.out.print(e.toString());
    	}
    	return strscr;
    }
    
    public static String GetUTF8FromNCRDecimalString(String s) {
        String a, b;
        a = "";
        b = "";
        String[] arrStr = s.split("&#");
        int i = arrStr.length;
        for (int j = 0; j < i; j++) {
            int k = 0;
            k = s.indexOf("&#");
            if (k >= 0) {
                b = s.substring(0, k);
                a = a + b;
                s = s.substring(k);
            } else {
                a = a + s;
            }
            k = s.indexOf(";");
            if (k >= 2) {
                b = s.substring(2, k);
                s = s.substring(k + 1);
                try {
                    char str = (char) (Integer.parseInt(b));
                    a = a + str;
                } catch (Exception e) {
                    
                }
            }
        }
        return a;
    }
   
    public void writePage(
        final RequestProperties reqState,
        String pageMsg)
        throws IOException
    {
        final PrivateLabel privLabel = reqState.getPrivateLabel();
        final I18N    i18n     = privLabel.getI18N(baoCaoTramTheoNgay.class);
        final Locale  locale   = reqState.getLocale();
        final Account currAcct = reqState.getCurrentAccount();
        final User    currUser = reqState.getCurrentUser();
        final String  pageName = this.getPageName();
        String m = pageMsg;
        boolean error = false;
        
        HttpServletRequest request = reqState.getHttpServletRequest();
        HttpServletResponse response =reqState.getHttpServletResponse();
        String excel=    AttributeTools.getRequestString(request, "btnExcel", "");
        
  		String xe =AttributeTools.getRequestString(request, "device", "");
  		String tuNgay =AttributeTools.getRequestString(request, "tuNgay", "");
  		String denNgay =AttributeTools.getRequestString(request, "denNgay", "");
  		
  		int i=0;
        
        /* Style */
        HTMLOutput HTML_CSS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                String cssDir = BaocaoNhienLieuFuel.this.getCssDirectory();
                WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
                WebPageAdaptor.writeCssLink(out, reqState, "scrollbar.css", cssDir);
                WebPageAdaptor.writeCssLink(out, reqState,
						"jquery-ui-1.8.4.custom.css", cssDir);
                WebPageAdaptor.writeCssLink(out, reqState,
						"jquery-ui-drop.css", cssDir);
                
            }
        };

        /* javascript */
        HTMLOutput HTML_JS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                MenuBar.writeJavaScript(out, pageName, reqState);
               
                out.println("        <script type=\"text/javascript\" src=\"js/jquery-1.4.2.min.js\"></script>\n");
                out.println("        <script type=\"text/javascript\" src=\"js/PCalendar.js\"></script>\n");
                out.println( " <script type='text/javascript' src='js/jquery.tablesorter.min.js'></script>");
                out.println("<script type='text/javascript' src='js/sorttable.js'></script>");
                out.println("        <script type=\"text/javascript\" src=\"js/1jquery-1.9.1.js\"></script>\n");
				out.println("        <script type=\"text/javascript\" src=\"js/1jquery-ui.js\"></script>\n");
				out.println("        <script type=\"text/javascript\" src=\"js/1js.js\"></script>\n");
				out.println("        <script type=\"text/javascript\" src=\"js/GetDeviceByAccount.js\"></script>\n");
              out.print( "<script src='./js/jquery.tinyscrollbar.min.js' type=\"text/javascript\"></script>");
               out.print("<script type='text/javascript'>$(document).ready(function() {$('#scrollbar2').tinyscrollbar();}); </script>");
                out.println("<script type='text/javascript' > $(function(){$('#myTable').tablesorter(); }); </script>");
                
            }
        };

        /* Content */
       
        
        HTMLOutput HTML_CONTENT  = new HTMLOutput(CommonServlet.CSS_CONTENT_FRAME, m) {
            public void write(PrintWriter out) throws IOException {

                String menuURL = privLabel.getWebPageURL(reqState, PAGE_MENU_TOP);
                String chgURL  = privLabel.getWebPageURL(reqState, pageName, COMMAND_INFO_UPDATE);
                String frameTitle = i18n.getString("baocaoTram.PageTitle","Tr&#x1EA1;m");
                
                // frame content
             // view submit
                HttpServletRequest request = reqState.getHttpServletRequest();
                String tuNgay=AttributeTools.getRequestString(request, "tuNgay", "");
                String contentall="";
                String denNgay=AttributeTools.getRequestString(request, "denNgay", "");
          
                String urlmap="";
                //HttpServletRequest request = reqState.getHttpServletRequest();
                String xem=    AttributeTools.getRequestString(request, "btnview", "");
                contentall = AttributeTools.getRequestString(request, "hidDevice", "");
                String acc = AttributeTools.getRequestString(request, "hidAccount", "");
                String sql=report(acc,contentall,tuNgay,denNgay,currAcct.getTimeZone(),privLabel);
                if(acc!=null && !acc.equals(""))
                	accfirst=acc;
                
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String d = sdf.format(now);
                
                out.println("<span class='"+CommonServlet.CSS_MENU_TITLE+"'>Báo cáo nhiên liệu của xe</span><br/>");
                out.println("<hr/>");
                out.println("<form name='AccountInfo' method='post' action='"+chgURL+"' target='_self'>\n");
              
                out.println("<table class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE+"' cellspacing='0' callpadding='0' border='0' width='100%' style='padding:15 0 15'>\n");
                
                out.print  ("<tr>\n");
                out.print("<td width='10%' align='right'>Chọn Account:</td>\n");
                out.print("<td width='10%' align='left' style='padding:5px;'><input id='hidAccount' name='hidAccount' type ='hidden' value='' />"+CreateCbbAccount(acc)+"</td>");
	            out.print( "<td width='6%' align='right'>Ch&#x1ECD;n Xe:</td>" +
	            		"<td align='left' width='10%'><div id='divDevice'><input id='hidDevice' name='hidDevice' type ='hidden' value='' />\n");
	            out.print  (CreateCbbDevice(acc,contentall,reqState,privLabel));
                out.print  ("</div></td>");
                out.print  ("<td width='6%' align='right'>T&#x1EEB; ng&#x00E0;y:</td>\n");
                out.print  ("<td width='10%' align='left'>\n");
                out.print( "<input id='Text1' name='tuNgay' type='text' style='width:120px' class='textReadOnly' onclick=\"displayCalendar(this,'dd/mm/yyyy hh:ii',this,true,'','Text1')\" value='"+d+" 00:00' /></td>");
                if(tuNgay!="")
                {
				out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('Text1').value ='"+tuNgay+"'; </script>");
                }
                out.print  ("<td width='6%' align='right'>&#x0110;&#x1EBF;n ng&#x00E0;y:</td>\n");
                out.print  ("<td width='32%' align='left'>\n");
                out.print( "<input id='Text2' name='denNgay' type='text' class='textReadOnly' style='width:120px' onclick=\"displayCalendar(this,'dd/mm/yyyy hh:ii',this,true,'','Text2')\" value='"+d+" 23:59' /></td>");
                if(denNgay!="")
                {
                out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('Text2').value ='"+denNgay+"'; </script>");
                }
                out.println("</tr>");
                
                out.println("</table>");
                out.print("<table cellspacing='0' style='width:100%;padding:10px 0px 10px'><tbody><tr class='viewhoz'><td width='11%'></td><td width='100px'> <input type='submit' class='button1' name='btnview' value='Xem' id='btnview'></td><td width='105px'></td><td align='left' width='220px'><input type='buttn' style='width:170px' id='btnFuelDevice' value='Insert FuelDevice' name='btnFuelDevice' class='button1' onclick=\"RequestInsertFuelDevice();\"></td>"); 
              
            	out.print("<td align='left'></td>"); 
   
                out.print("</tr></tbody></table>");
           
                out.print(sql);
           
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
