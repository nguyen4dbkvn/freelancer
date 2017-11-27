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

import org.opengts.util.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
 
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

//import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;


public class baoCaoTram
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
    
    public baoCaoTram()
    {
    	this.setBaseURI(RequestProperties.TRACK_BASE_URI());
        this.setPageName(PAGE_BAOCAOTRAM);
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
        I18N i18n = privLabel.getI18N(baoCaoTram.class);
        return super._getMenuDescription(reqState,i18n.getString("baoCaoTram.Menu","B&#x00E1;o c&#x00E1;o tr&#x1EA1;m"));
    }
   
    public String getMenuHelp(RequestProperties reqState, String parentMenuName)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(baoCaoTram.class);
        return super._getMenuHelp(reqState,i18n.getString("baoCaoTram.MenuHelp","B&#x00E1;o c&#x00E1;o tr&#x1EA1;m"));
    }

    // ------------------------------------------------------------------------

    public String getNavigationDescription(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(baoCaoTram.class);
        return super._getNavigationDescription(reqState,i18n.getString("baoCaoTram.NavDesc","B&#x00E1;o c&#x00E1;o tr&#x1EA1;m"));
    }

    public String getNavigationTab(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(baoCaoTram.class);
        return i18n.getString("baoCaoTram.NavTab","B&#x00E1;o c&#x00E1;o tr&#x1EA1;m");
    }

    // ------------------------------------------------------------------------
    
   
    public String chonTram(String Acc,String idselect)
    {
    	String strre = "<select id ='device' class='textReadOnly' name = 'device' style='width:100px'>";
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
    
    public String LoadTram(String ngay, String Tram,String tenAccount) throws IOException
    {
    	
    	String strscr ="";
    	try
    	{
    		int num = 0,i=0;
    		DBCamera objcmr = new  DBCamera();
    		//ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
    		List<String[]> result = objcmr.getTram(tenAccount, ngay, Tram);
    		
    		strscr =strscr+ "<div class='adminSelectTable'><table id ='myTable' width='100%'class='adminSelectTable_sortable' cellspacing='1' ><thead><tr  align='center'><th class='adminTableHeaderCol_sort' width='50px'>STT</th><th width='80px' class='adminTableHeaderCol_sort' >Xe</th><th width='150px' class='adminTableHeaderCol_sort'>Th&#x1EDD;i &#x0111;i&#x1EC3;m</th><th class='adminTableHeaderCol_sort'>Tr&#x1EA1;m</th><th width='100px' class='adminTableHeaderCol_sort'>T&#x1ED1;c &#x0111;&#x1ED9; (Km/h)</th><th width='50px' class='adminTableHeaderCol_sort'>C&#x1EED;a </th><th  width='50px' class='adminTableHeaderCol_sort'>&#x0110;i&#x1EC1;u h&#x00F2;a</th><th class='adminTableHeaderCol_sort' width='90px'>Th&#x1EDD;i gian l&#x1EC7;ch</th></tr></thead>";
    		for(String[] rs:result)
    		{    			    			
    			String tg =rs[6],chuoi="";
    			int gio=0,phut =0;
    			gio = Integer.parseInt(tg)/3600;
    			phut =(Integer.parseInt(tg)%3600)/60;
    			if(gio>9)
    			{
    				if(phut>9)
    					chuoi=chuoi+Integer.toString(gio)+":" +Integer.toString(phut);
    				else
    					chuoi=chuoi+Integer.toString(gio)+":0" +Integer.toString(phut);    					
    			}
    			else
    			{
    				if(phut>9)
    					chuoi=chuoi+"0"+Integer.toString(gio)+":" +Integer.toString(phut);
    				else
    					chuoi=chuoi+"0"+Integer.toString(gio)+":0" +Integer.toString(phut);    				
    			}
    			String css ="";
    			if(num%2==0)
    				css = "adminTableBodyRowOdd";
    			else
    				css = "adminTableBodyRowEven";
    			
    			num++;
    			i++;
    			strscr =strscr+"<tr class ="+css+"><td>"+i+"</td><td>"+rs[0]+"</td><td>"+rs[1]+"</td><td>"+rs[5]+"</td><td>"+rs[2]+"</td><td>"+rs[3]+"</td><td>"+rs[4]+"</td><td>"+chuoi+"</td></tr>";	
    			
    		}
    		strscr =strscr+"</table></div>";
    		
    	}
    	catch (Exception e)
    	{
    	
    	}
    	return strscr;
    }
    	
    public static String def = " abcdefghijklmnopqrstuvxwzyABCDEFGHIJKLMNOPQRSTUVXWZY0123456789'~!@#$%^&*()-_=+\\|]}[{?/>.,<:;'\"";
  
    
    public static String GetNCRDecimalString(String s) {
        String a = s;
        char[] ab = a.toCharArray();
        a = "";
        for (int i = 0; i < ab.length; i++) {
//            System.out.println(i + ": " + ab[i]);
//            System.out.println(i + ": " + (int) ab[i]);
            char c = ab[i];
            if (c == (char) 13) {
//                System.out.println(c);
                a = a + c;
            } else {
                if (def.indexOf(c) >= 0) {
                    a = a + c;
                } else {
                    a = a + "&#" + (int) c + ";";
                }
            }
        }
        return a;
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
        final I18N    i18n     = privLabel.getI18N(baoCaoTram.class);
        final Locale  locale   = reqState.getLocale();
        final Account currAcct = reqState.getCurrentAccount();
        final User    currUser = reqState.getCurrentUser();
        final String  pageName = this.getPageName();
        String m = pageMsg;
        boolean error = false;
        HttpServletRequest request = reqState.getHttpServletRequest();
        HttpServletResponse response =reqState.getHttpServletResponse();
        String excel=    AttributeTools.getRequestString(request, "btnExcel", "");
       String ngayxem = AttributeTools.getRequestString(request, "ngay", "");
       String contentall = AttributeTools.getRequestString(request, "device", "");
       int i=0;
        if(excel!=""){
        	if(excel.equals("Export Excel"))
        	{  
        		java.util.Calendar c =java.util.Calendar.getInstance();  
                Date now = c.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String d = sdf.format(now);
             	 response.setContentType("application/vnd.ms-excel;charset=utf-8");
                   response.setHeader("Content-Disposition", "attachment; filename=baoCaoTram_"+d + ".xls");
                   HSSFWorkbook wb = new HSSFWorkbook();
       	        HSSFSheet sheet = wb.createSheet("Dispatch");
       	        HSSFRow title=   sheet.createRow((short)1);
       	        
       	        
       			HSSFCellStyle cst = wb.createCellStyle();
       			 HSSFFont ft = wb.createFont();
       			 cst.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       			 cst.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
       			 ft.setFontHeightInPoints((short) 18);
       			         //make it red
       			      //   f.setColor((short) HSSFColor.RED.index);
       			        // make it bold
       			        //arial is the default font
       			         ft.setBoldweight((short)ft.BOLDWEIGHT_BOLD);
       			         cst.setFont(ft);
       			     	HSSFCell ct = title.createCell((short) 0);
       			    
               			ct.setCellStyle(cst);
               			ct.setCellValue("BÁO CÁO TRẠM");
              			title.setHeightInPoints(40);
              			sheet.addMergedRegion(new CellRangeAddress(1,1, 0, 6));
              			HSSFCellStyle csNgay = wb.createCellStyle();
              			
              			HSSFFont fngay = wb.createFont();
              			  HSSFRow rngay=   sheet.createRow((short)2);
              			 
              			  fngay.setFontHeightInPoints((short)10);
              			  fngay.setBoldweight((short)fngay.BOLDWEIGHT_BOLD);
              			  csNgay.setFont(fngay);
              			  HSSFCell cTuNgay = rngay.createCell((short) 1);
              			  cTuNgay.setCellStyle(csNgay);
              			  cTuNgay.setCellValue("Ngày");
              			  rngay.createCell((short) 2).setCellValue(ngayxem);
              			  
              			  i=i+3;
              			  
              			  
              			  
                			HSSFCellStyle cellStyle = wb.createCellStyle();
              			cellStyle.setBorderTop((short)1);
              			cellStyle.setBorderRight((short)1);
              			cellStyle.setBorderLeft((short)1);
              			cellStyle.setBorderBottom((short)1);
              			cellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
              			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
              			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
              			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
              			cellStyle.setWrapText(true);
              			 HSSFFont f = wb.createFont();
              			 f.setFontHeightInPoints((short) 10);
              			         //make it red
              			      //   f.setColor((short) HSSFColor.RED.index);
              			        // make it bold
              			        //arial is the default font
              			         f.setBoldweight((short)f.BOLDWEIGHT_BOLD);
              			         cellStyle.setFont(f);
              			  
              			  
              HSSFRow rowhead=   sheet.createRow((short)i);
       	      HSSFCell h0=  rowhead.createCell((short) 0);
       	     h0.setCellStyle(cellStyle);		
       	      h0.setCellValue("Xe ");
       	     HSSFCell h1= rowhead.createCell((short) 1);
       	    h1.setCellStyle(cellStyle);	 
       	     h1.setCellValue("Thời điểm");
       	     HSSFCell h2= rowhead.createCell((short) 2);
       	    h2.setCellStyle(cellStyle); 
       	     h2.setCellValue("Trạm");
       	    HSSFCell h3= rowhead.createCell((short) 3);
       	    h3.setCellStyle(cellStyle);		 
       	    h3.setCellValue("Tốc độ");
       	    HSSFCell h4= rowhead.createCell((short) 4);
       	    h4.setCellStyle(cellStyle);	
       	    h4.setCellValue("Cửa");
       	     HSSFCell h5=  rowhead.createCell((short) 5);
       	     h5.setCellStyle(cellStyle);	
       	     h5.setCellValue("Điều hòa");
       	    HSSFCell h6= rowhead.createCell((short) 6);
       	    	h6.setCellStyle(cellStyle);	
       	    	h6.setCellValue("Thời gian lệch");
  	
       	    	rowhead.setHeightInPoints((short)40);               
        	      //PrintWriter out = response.getWriter();
        	    //  out.println("\tXe\tThời Điểm\tTrạm\tTốc độ(Km/h)\tCửa\tĐiều hòa\tThời gian lệch");
        	        DBCamera objcmr = new  DBCamera();
          		//ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
        	        List<String[]> result = objcmr.getTram(currAcct.getAccountID(), ngayxem, contentall);
          		
          		try{
          			for(String[] rs:result)
        		{
        			
        			
        			String tg =rs[6],chuoi="";
        			int gio=0,phut =0;
        			gio = Integer.parseInt(tg)/3600;
        			phut =(Integer.parseInt(tg)%3600)/60;
        			if(gio>9)
        			{
        				if(phut>9)
        					chuoi=chuoi+Integer.toString(gio)+":" +Integer.toString(phut);
        				else
        					chuoi=chuoi+Integer.toString(gio)+":0" +Integer.toString(phut);
        					
        			}
        			else
        			{
        				if(phut>9)
        					chuoi=chuoi+"0"+Integer.toString(gio)+":" +Integer.toString(phut);
        				else
        					chuoi=chuoi+"0"+Integer.toString(gio)+":0" +Integer.toString(phut);
        				
        			}
        			
        			HSSFCellStyle csr = wb.createCellStyle();
        			csr.setBorderTop((short)1);
        			csr.setBorderRight((short)1);
        			csr.setBorderLeft((short)1);
        			csr.setBorderBottom((short)1);
        			HSSFRow row=   sheet.createRow((short)(i+1));
        			
        			HSSFCell r0=row.createCell((short) 0);
      				r0.setCellStyle(csr);
      				r0.setCellValue(rs[0]);
      				HSSFCell r1=row.createCell((short) 1);
      				r1.setCellStyle(csr);		
      				r1.setCellValue(rs[1]);
    	        	//row.createCell((short) 2).setCellValue(rs.getString(4));
      				HSSFCell r2=row.createCell((short) 2);
      				r2.setCellStyle(csr);
      				r2.setCellValue(rs[5]);
      				HSSFCell r3=row.createCell((short) 3);
      				r3.setCellStyle(csr);
      				r3.setCellValue(Double.parseDouble(rs[2]));
      				HSSFCell r4=row.createCell((short) 4);
      				r4.setCellStyle(csr);
      				r4.setCellValue(rs[3]);
      				HSSFCell r5=row.createCell((short) 5);
      				r5.setCellStyle(csr);
      				r5.setCellValue(rs[4]);
      				HSSFCell r6=row.createCell((short) 6);
      				r6.setCellStyle(csr);
      				r6.setCellValue(chuoi);
        			
        			i++;
        			
        	    //  out.println("\t"+rs.getString(1)+"\t"+rs.getString(2)+"\t"+rs.getString(6)+"\t"+
        	    	//	  rs.getString(3)+"\t"+rs.getString(4)+"\t"+rs.getString(5)+"\t"+chuoi);
        	     
        	      
        	}
          		sheet.autoSizeColumn(0);
        		sheet.autoSizeColumn(1);
        		sheet.autoSizeColumn(2);
        		sheet.autoSizeColumn(3);
        		sheet.autoSizeColumn(4);
        		sheet.autoSizeColumn(5);
        		sheet.autoSizeColumn(6);
          		 OutputStream out = response.getOutputStream();
     	        wb.write(out);
     	        out.close();
          		
          		} catch(Exception e){}
        	return;
        }}
        
        
        /* Style */
        HTMLOutput HTML_CSS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                String cssDir = baoCaoTram.this.getCssDirectory();
                WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
            }
        };

        /* javascript */
        HTMLOutput HTML_JS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                MenuBar.writeJavaScript(out, pageName, reqState);
                out.println("<script type=\"text/javascript\" src=\"js/PCalendar.js\"></script>\n");
                out.println("<script type=\"text/javascript\" src=\"js/jquery-1.4.2.min.js\"></script>\n");
               out.println( " <script type='text/javascript' src='js/jquery.tablesorter.min.js'></script>");
                out.println("<script type='text/javascript' src='js/sorttable.js'></script>");
                out.println("<script type='text/javascript' > $(function(){$('#myTable').tablesorter(); }); </script>");
                // JavaScriptTools.writeJSInclude(out, JavaScriptTools.qualifyJSFileRef("PCalendar.js"));
               // JavaScriptTools.writeJSInclude(out, JavaScriptTools.qualifyJSFileRef("jquery-1.4.2.min.js"));
               // JavaScriptTools.writeJSInclude(out, JavaScriptTools.qualifyJSFileRef("PopImage.js"));
                
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
                String ngayxem="",contentall="";
                HttpServletRequest request = reqState.getHttpServletRequest();
                HttpServletResponse rp =reqState.getHttpServletResponse();
                ngayxem = AttributeTools.getRequestString(request, "ngay", "");
                contentall = AttributeTools.getRequestString(request, "device", "");
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String d = sdf.format(now);
               
                out.println("<span class='"+CommonServlet.CSS_MENU_TITLE+"'>B&#x00E1;o c&#x00E1;o tr&#x1EA1;m</span><br/>");
                out.println("<hr/>");
                out.println("<form name='AccountInfo' method='post' action='"+chgURL+"' target='_self'>\n");
              
                out.println("<table class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE+"' cellspacing='0' callpadding='0' border='0' width='100%' style='padding:15 0 15'>\n");
                
                out.print  ("<tr>\n");
                out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER+"' width='90px' align='right'>"+i18n.getString("Camera.DateSelect","Ch&#x1ECD;n ng&#x00E0;y:")+"</td>\n");
                out.print  ("<td width='100px'>\n");
                out.print( "<input id='Text1' name='ngay' type='text' class='textReadOnly' style='width:100px' onclick=\"displayCalendar(this,'yyyy/mm/dd',this,false,'','Text1')\" value='"+d+"' />");
                out.print  ("</td><td width='105px' align='right'><span style='margin-left: 10px;margin-right:5px;'>"+i18n.getString("DeviceSelect","Ch&#x1ECD;n Tr&#x1EA1;m:")+"</span></td><td>\n");
                out.print  (chonTram(currAcct.getAccountID(),contentall));
                out.print  ("</td>");
                out.println("</tr>");
    
                if(ngayxem!="")
                {
                out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('Text1').value ='"+ngayxem+"'; </script>");
                }
                out.println("</table>");
                
                out.print("<table cellspacing='0' style='width:100%;padding:10px 0px 10px'><tbody><tr class='viewhoz'><td width='100px'></td><td width='100px'> <input type='submit' class='button1' name='btnview' value='Xem' id='btnview'></td><td width='105px'></td><td align='left'><input type='submit' id='btnExcel' value='Export Excel' name='btnExcel' class='button1'></td><td></td></tr></tbody></table>");
               String xem=    AttributeTools.getRequestString(request, "btnview", "");
                
                
                if(xem.equals("Xem"))
                {
                String load = LoadTram(ngayxem,contentall,currAcct.getAccountID());
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
