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


public class Report
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
    
    public Report()
    {
    	this.setBaseURI(RequestProperties.TRACK_BASE_URI());
        this.setPageName(PAGE_REPORT);
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
        I18N i18n = privLabel.getI18N(Report.class);
        return super._getMenuDescription(reqState,i18n.getString("Report.Menu","B&#x00E1;o c&#x00E1;o tuy&#x1EBF;n"));
    }
   
    public String getMenuHelp(RequestProperties reqState, String parentMenuName)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(Report.class);
        return super._getMenuHelp(reqState,i18n.getString("Report.MenuHelp","B&#x00E1;o c&#x00E1;o tuy&#x1EBF;n"));
    }

    // ------------------------------------------------------------------------

    public String getNavigationDescription(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(Report.class);
        return super._getNavigationDescription(reqState,i18n.getString("Report.NavDesc","B&#x00E1;o c&#x00E1;o tuy&#x1EBF;n"));
    }

    public String getNavigationTab(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(Report.class);
        return i18n.getString("Report.NavTab","B&#x00E1;o c&#x00E1;o tuy&#x1EBF;n");
    }

    // ------------------------------------------------------------------------
    
   
    
    
public String CreateCbbDevice(String accountid, String idselect, RequestProperties reqState, PrivateLabel privLabel) throws IOException
    {
  IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
  java.util.List<IDDescription> idList = reqState.createIDDescriptionList(false, sortBy);
        IDDescription list[] = idList.toArray(new IDDescription[idList.size()]);
        
     String strre = "<select id ='device' name = 'device' class='textReadOnly'>";
     for (int d = 0; d < list.length ; d++) {
            /*String idVal = (d < list.length)? list[d].getID()          : ("v" + String.valueOf(d - list.length + 1));
            String desc  = (d < list.length)? list[d].getDescription() : (String.valueOf(d - list.length + 1) + " asset"*/
            if(idselect.equalsIgnoreCase(list[d].getID()))
          strre +="<option value ='"+list[d].getID()+"' selected =\"selected\">"+list[d].getID()+"</option>\n";
         else
          strre +="<option value ='"+list[d].getID()+"'>"+list[d].getID()+"</option>\n";
     }      
     
     strre +="</select>\n";
     strre +="<script type ='text/javascript' language ='javascript'> document.getElementById('device ').value = '"+idselect+"';</script>\n";
     return strre;
    }
    private String getCurDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
    public String LoadImage(String ngay, String device, int page, int pagesize,String tenAccount) throws IOException
    {

    	String strscr ="";
    
    	int dem=0;
    	try
    	{
    		
    		
    		
    		int num = 0,i=0;
    		DBCamera objcmr = new  DBCamera();
    		//ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
    		ResultSet rs = objcmr.getReport(device, ngay,tenAccount, page, pagesize);
    		
    		strscr =strscr+ "<div class='adminSelectTable'><table id ='myTable' width='100%'class='adminSelectTable_sortable' cellspacing='1' ><thead><tr  align='center'><th class='adminTableHeaderCol_sort' width='50px'>STT</th><th width='80px' class='adminTableHeaderCol_sort' >Xe</th><th width='150px' class='adminTableHeaderCol_sort'>Th&#x1EDD;i &#x0111;i&#x1EC3;m</th><th class='adminTableHeaderCol_sort'>Tr&#x1EA1;m</th><th width='100px' class='adminTableHeaderCol_sort'>T&#x1ED1;c &#x0111;&#x1ED9; (Km/h)</th><th width='50px' class='adminTableHeaderCol_sort'>C&#x1EED;a </th><th  width='50px' class='adminTableHeaderCol_sort'>&#x0110;i&#x1EC1;u h&#x00F2;a</th><th class='adminTableHeaderCol_sort' width='90px'>Th&#x1EDD;i gian l&#x1EC7;ch</th></tr></thead>";
    		while (rs.next())
    		{
    			
    			String tg =rs.getString(7),chuoi="";
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
    			i++;
    			num++;
    			strscr =strscr+"<tr class ="+css+"><td>"+(((page-1)*pagesize)+i)+"</td><td>"+rs.getString(1)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(6)+"</td><td>"+rs.getString(3)+"</td><td>"+rs.getString(4)+"</td><td>"+rs.getString(5)+"</td><td>"+chuoi+"</td></tr>";	
    			dem++;
    		}
    		strscr =strscr+"</table></div>";
    		
    	}
    	catch (Exception e)
    	{
    	
    	}
    	return strscr;
    }
    /*
    public int getTotal(String ngay, String device,String tenAccount) throws IOException
    {
    	
    	
		
    	int tong1 =0;
    	try
    	{
    		
	    	DBCamera objcmr = new DBCamera();
	    	ResultSet rs = objcmr.getTotalReport(device, ngay,tenAccount);
	    	while(rs.next())
	    	{
	    		tong1++;
	    	}
    	}
    	catch (Exception e)
    	{
    		
    	}
    	return tong1;
    }*/
    public String NumPage(String url, String ngay, String device,int totalpage)
    {
    	String strre ="<span>";
    	for(int i = 0; i<totalpage;i++)
    	{
    		int pshow = i+1;
    		strre +="<a class ='apaging' href =\"javascript:openURL('"+url+"&pageindex="+pshow+"&date="+ngay+"&device="+device+"','_self');\">"+pshow+"</a>";
    	}
    	strre +="</span>";
    	return strre;
    }
    public String NumPage(String url, String ngay, String device,int totalpage, int pageindex)
    {
    	String strre ="<div style ='margin-top:8px; width:100%; margin-bottom:8px;' align ='center'><span>";
    	if (totalpage >1)
    	{
    		String cssf ="apaging";
    		String cssl ="apaging";
    		int pageb = pageindex -1;
    		String ahrefb = "javascript:openURL('"+url+"&pageindex="+pageb+"&date="+ngay+"&device="+device+"','_self');";
    		int pagen = pageindex + 1;
    		if (pagen > totalpage)
    			pagen = totalpage;
    		String ahrefn = "javascript:openURL('"+url+"&pageindex="+pagen+"&date="+ngay+"&device="+device+"','_self');";
    		String ahreff = "javascript:openURL('"+url+"&pageindex=1&date="+ngay+"&device="+device+"','_self');";
    		String ahrefl = "javascript:openURL('"+url+"&pageindex="+totalpage+"&date="+ngay+"&device="+device+"','_self');";
    		if(pageindex == 1)
    		{
    			cssf = "apaging2";
    			ahrefb ="#";
    			ahreff="#";
    		}
    		if(pageindex == totalpage)
    		{
    			cssl ="apaging2";
    			ahrefl="#";
    			ahrefn="#";
    		}
    		strre +="<a class ='"+cssf+"' href =\""+ahreff+"\" style ='padding-left:4px; padding-right:4px;'><<</a>";
    		strre +="<a class ='"+cssf+"' href =\""+ahrefb+"\" style ='padding-left:8px; padding-right:8px;'><</a>";
	    	for(int i = 0; i<totalpage;i++)
	    	{
	    		int pshow = i+1;
	    		String spshow = Integer.toString(pshow);
	    		if(pshow <10)
	    			spshow = "0"+spshow;
	    		String cssclass = "apaging";
	    		if(pshow == pageindex)
	    			cssclass="apaging1";
	    		if(pageindex <=2)
	    		{
	    			if(pshow <=5)
	    				strre +="<a class ='"+cssclass+"' href =\"javascript:openURL('"+url+"&pageindex="+pshow+"&date="+ngay+"&device="+device+"','_self');\">"+spshow+"</a>";
	    		}
	    		else
	    		{
	    			if(pageindex > totalpage - 2)
	    			{
	    				if(pshow >=totalpage - 5)
	    					strre +="<a class ='"+cssclass+"' href =\"javascript:openURL('"+url+"&pageindex="+pshow+"&date="+ngay+"&device="+device+"','_self');\">"+spshow+"</a>";
	    			}
	    			else
	    			{
	    				if (pshow >= pageindex - 2 && pshow <=pageindex +2)
	    				{
	    					if(pshow <=totalpage && pshow >0)
	    					strre +="<a class ='"+cssclass+"' href =\"javascript:openURL('"+url+"&pageindex="+pshow+"&date="+ngay+"&device="+device+"','_self');\">"+spshow+"</a>";
	    				}
	    			}
	    		}
	    		
	    	}
	    	strre +="<a class ='"+cssl+"' href =\""+ahrefn+"\" style ='padding-left:8px; padding-right:8px;'>></a>";
    		strre +="<a class ='"+cssl+"' href =\""+ahrefl+"\" style ='padding-left:4px; padding-right:4px;'>>></a>";
    	}
    	strre +="</span></div>";
    	return strre;
    }
    
    
   
   
    
    public void writePage(
        final RequestProperties reqState,
        String pageMsg)
        throws IOException
    {
        final PrivateLabel privLabel = reqState.getPrivateLabel();
        final I18N    i18n     = privLabel.getI18N(Report.class);
        final Locale  locale   = reqState.getLocale();
        final Account currAcct = reqState.getCurrentAccount();
        final User    currUser = reqState.getCurrentUser();
        final String  pageName = this.getPageName();
        String m = pageMsg;
        boolean error = false;
        
        HttpServletRequest request = reqState.getHttpServletRequest();
        HttpServletResponse response =reqState.getHttpServletResponse();
        String excel=    AttributeTools.getRequestString(request, "btnExcel", "");
        
        String contentall = "";
        String ngayxem = "homnay";
     // view submit
        ngayxem = AttributeTools.getRequestString(request, "ngay", "");
        contentall = AttributeTools.getRequestString(request, "device", "");

        String  sdevice = request.getParameter("device");
       
        
        
        
        if(excel!=""){
        	int i=0;
        	if(excel.equals("Export Excel"))
        	{  
        		java.util.Calendar c =java.util.Calendar.getInstance();  
                Date now = c.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String d = sdf.format(now);
             	 response.setContentType("application/vnd.ms-excel;charset=utf-8");
                   response.setHeader("Content-Disposition", "attachment; filename=baoCaoTuyen_"+d+".xls");
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
               			ct.setCellValue("BÁO CÁO TUYẾN");
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
 
              
        	        DBCamera objcmr = new  DBCamera();
          		//ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
          		ResultSet rs = objcmr.getTotalReport(contentall, ngayxem,currAcct.getAccountID());
          		
          		try{
          			while (rs.next())
            		{
            			
            			String tg =rs.getString(7),chuoi="";
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
          				r0.setCellValue(rs.getString(1));
          				HSSFCell r1=row.createCell((short) 1);
          				r1.setCellStyle(csr);		
          				r1.setCellValue(rs.getString(2));
        	        	//row.createCell((short) 2).setCellValue(rs.getString(4));
          				HSSFCell r2=row.createCell((short) 2);
          				r2.setCellStyle(csr);
          				r2.setCellValue(rs.getString(6));
          				HSSFCell r3=row.createCell((short) 3);
          				r3.setCellStyle(csr);
          				r3.setCellValue(Double.parseDouble(rs.getString(3)));
          				HSSFCell r4=row.createCell((short) 4);
          				r4.setCellStyle(csr);
          				r4.setCellValue(rs.getString(4));
          				HSSFCell r5=row.createCell((short) 5);
          				r5.setCellStyle(csr);
          				r5.setCellValue(rs.getString(5));
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
                String cssDir = Report.this.getCssDirectory();
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
                String frameTitle = i18n.getString("Camera.PageTitle","Report");
                String contentall = "";
                String ngayxem = "homnay";
                int pindex = 1;
                int pindexl = 0;
                int pagesize = 12;
                int tongtrang = 0;
                String flag ="0";
                int pagestatic = 1;
                // frame content
             // view submit
                HttpServletRequest request = reqState.getHttpServletRequest();
                ngayxem = AttributeTools.getRequestString(request, "ngay", "");
                if(ngayxem==null)
                {
                	
                	return ;
                }
                contentall = AttributeTools.getRequestString(request, "device", "");
                String  spindex = request.getParameter("pageindex");
                if(spindex !=null)
                {
                	if(spindex !="")
                	{
                		pindex = Integer.parseInt(spindex);
                		pindexl = pindex;
                		pagestatic = pindex;
                	}
                }
                String  sdate = request.getParameter("date");
                if(sdate !=null)
                {
                	if(sdate !="")
                	{
                		ngayxem = sdate;
                	}
                }
                String  sdevice = request.getParameter("device");
                if(sdevice !=null)
                {
                	if(sdevice !="")
                	{
                		contentall = sdevice;
                	}
                }
                String  sflag = request.getParameter("flag");
                if(sflag !=null)
                {
                	if(sflag !="")
                	{
                		flag = sflag;
                	}
                }
              //  tongtrang = getTotal(ngayxem, contentall,currAcct.getAccountID())/pagesize +1;
                String sql = LoadImage(ngayxem, contentall, pindex, pagesize,currAcct.getAccountID());
                //if (ngayxem =="")
                //	ngayxem = getCurDateTime();
                                            
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String d = sdf.format(now);
               
                out.println("<span class='"+CommonServlet.CSS_MENU_TITLE+"'>B&#x00E1;o c&#x00E1;o tuy&#x1EBF;n</span><br/>");
                out.println("<hr/>");
                out.println("<form name='AccountInfo' method='post' action='"+chgURL+"' target='_self'>\n");
              
                out.println("<table class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE+"' cellspacing='0' callpadding='0' border='0' width='100%' style='padding:15 0 15'>\n");
                
                out.print  ("<tr>\n");
                out.print  ("<td width='100px' align='right'>"+i18n.getString("Camera.DateSelect","Ch&#x1ECD;n ng&#x00E0;y:")+"</td>\n");
                out.print  ("<td width='100px'>\n");
                out.print( "<input id='Text1' class='textReadOnly' name='ngay' type='text' style='width:100px' onclick=\"displayCalendar(this,'yyyy/mm/dd',this,false,'','Text1')\" value='"+d+"' />");
                if(ngayxem!="")
				{
			   out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('Text1').value ='"+ngayxem+"'; </script>");
                }
                out.print  ("</td><td align='right' width='105'>"+i18n.getString("DeviceSelect","Ch&#x1ECD;n xe:")+"</td><td>\n");
                  out.print  (CreateCbbDevice(currAcct.getAccountID(),contentall,reqState,privLabel));
                out.print  ("</td>");
                out.println("</tr>");
                //out.print  ("<tr>");
                //out.print  ("<td colspan='2' style ='height:400px; border:solid 1px silver;' >"+sql +currAcct.getAccountID()+ngayxem+"</td>");
                //out.println("</tr>");
                
                out.println("</table>");
                out.print("<table cellspacing='0' style='width:100%;padding:10px 0px 10px'><tbody><tr class='viewhoz'><td width='100px'></td><td width='100px'> <input type='submit' class='button1' name='btnview' value='Xem' id='btnview'></td><td width='105px'></td><td align='left'><input type='submit' id='btnExcel' value='Export Excel' name='btnExcel' class='button1'></td><td></td></tr></tbody></table>");
                out.print(sql);
                
               /* if(pindex < tongtrang)
                	pindex++;
                if (pindexl>0)
                	pindexl--;
                
                
                out.write(NumPage(chgURL, ngayxem, contentall, tongtrang,pagestatic));*/
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
