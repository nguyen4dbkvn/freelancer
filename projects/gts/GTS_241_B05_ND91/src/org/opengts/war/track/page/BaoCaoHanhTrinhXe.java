

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

public class BaoCaoHanhTrinhXe
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

    public BaoCaoHanhTrinhXe()
    {
        this.setBaseURI(RequestProperties.TRACK_BASE_URI());
        this.setPageName(PAGE_MENU_RPT_DEVDETAIL);
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
        I18N         i18n        = privLabel.getI18N(BaoCaoHanhTrinhXe.class);
        String       devTitles[] = reqState.getDeviceTitles();
        return super._getMenuDescription(reqState,i18n.getString("ReportMenuDeviceDetail.hanhtrinhxe","{0} Detail Reports", devTitles));
    }

    public String getMenuHelp(RequestProperties reqState, String parentMenuName)
    {
        PrivateLabel privLabel   = reqState.getPrivateLabel();
        I18N         i18n        = privLabel.getI18N(BaoCaoHanhTrinhXe.class);
        String       devTitles[] = reqState.getDeviceTitles();
        return super._getMenuHelp(reqState,i18n.getString("ReportMenuDeviceDetail.hanhtrinhxe","Display various {0} detail reports", devTitles));
    }
 
    public String getNavigationDescription(RequestProperties reqState)
    {
        PrivateLabel privLabel   = reqState.getPrivateLabel();
        I18N         i18n        = privLabel.getI18N(BaoCaoHanhTrinhXe.class);
        String       devTitles[] = reqState.getDeviceTitles();
        return super._getNavigationDescription(reqState,i18n.getString("ReportMenuDeviceDetail.hanhtrinhxe","{0} Detail", devTitles));
    }

    public String getNavigationTab(RequestProperties reqState)
    {
        PrivateLabel privLabel   = reqState.getPrivateLabel();
        I18N         i18n        = privLabel.getI18N(BaoCaoHanhTrinhXe.class);
        String       devTitles[] = reqState.getDeviceTitles();
        return super._getNavigationTab(reqState,i18n.getString("ReportMenuDeviceDetail.hanhtrinhxe","{0} Detail", devTitles));
    }

    // ---s---------------------------------------------------------------------
    /*
    public String CreateCbbDevice(String accountid,String userid, String idselect) throws IOException
    {
    	String strre = "<select id ='device' name = 'device' class='textReadOnly'>";
    	try 
    	{
    		DBCamera objcmr = new DBCamera();
    		ResultSet rs = objcmr.GetDiviceByAccountID2(accountid,userid);
    		while(rs.next())
    		{
    			if(idselect == rs.getString("deviceid"))
    				strre +="<option value ='"+rs.getString("deviceid")+"' selected =\"selected\">"+rs.getString("description")+"</option>\n";
    			else
    				strre +="<option value ='"+rs.getString("deviceid")+"'>"+rs.getString("description")+"</option>\n";
    		}
    	}
    	catch (Exception e)
    	{
    		
    	}
    	strre +="</select>\n";
    	strre +="<script type ='text/javascript' language ='javascript'> document.getElementById('device').value = '"+idselect+"';</script>\n";
    	return strre;
    }*/
    public String CreateCbbDevice(String accountid, String idselect, RequestProperties reqState, PrivateLabel privLabel) throws IOException
        {
      IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
      java.util.List<IDDescription> idList = reqState.createIDDescriptionList(false, sortBy);
            IDDescription list[] = idList.toArray(new IDDescription[idList.size()]);
            
         String strre = "<select id ='device' name = 'device' class='textReadOnly'>";
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
    	        String dtFmt = dt.format("HH:mm:ss dd/MM/yyyy", tz);
    	  return dtFmt;
    	 }
    
    public String report(String tungay,String denngay,String devid,String timezone,String accid,PrivateLabel privLabel) throws IOException
    {
    	String strscr ="";
    	
    	try
    	{
    		DBCamera objcmr = new  DBCamera();
    		int bNeedManaged = objcmr.qryCheckIfAccountNeedManaging(accid);  
    		if (bNeedManaged>0){
    			objcmr.insertStopEvent(accid, devid);//ThanhNgC add to insert Stop Event
    		}
    		 
    		EventData ed[]= objcmr.GetReportDetail(tungay, denngay, devid, timezone, accid, new int[]{61714, 61472});
    		record=ed.length;
    		strscr =strscr+ "<div id='dataTable'> <table  id='myTable' width='100%' class='adminSelectTable_sortable' cellspacing='1' ><thead><tr  align='center'><th  class='adminTableHeaderCol_sort'>TT</th>" +
    				"<th  class='adminTableHeaderCol_sort'>Thời điểm <br/>(giờ, phút, giây, ngày, tháng, năm)</th><th width='110px' class='adminTableHeaderCol_sort'>Tọa độ</th><th  class='adminTableHeaderCol_sort'>Địa điểm</th>" +
    				"<th  class='adminTableHeaderCol_sort'>Ghi chú</th>";    		
    		strscr=strscr+"</tr></thead><tbody>"
    				;
    		for(int j =0;j<ed.length;j++)
    		{
    			
    			String css ="";
    			if(j%2==0)
    				css = "adminTableBodyRowOdd";
    			else
    				css = "adminTableBodyRowEven";
    			
    			String day = ConvertFromEpoch (ed[j].getTimestamp(),timezone);
    			strscr =strscr+"<tr class ="+css+"><td>"+(j+1)+"</td><td>"+day+"</td><td>"+round(ed[j].getLatitude(),5)+"/"+round(ed[j].getLongitude(),5)+"</td><td>"+ed[j].getAddress()+"</td><td>"+ed[j].getNotes()+"</td>";    					
    			
    			strscr=strscr+"</tr>";
    		}
    		if(ed.length==0){
    			strscr +="<tr class='adminTableBodyRowEven'><td colspan='5'> Không có dữ liệu.</td></tr>";
    		}
    		strscr =strscr+"</tbody></table></div>";
    	}

    	catch (Exception e)
    	{    	
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
    public String NumPage(String url, String tuNgay,String denNgay, String device,int totalpage)
    {
    	String strre ="<span>";
    	for(int i = 0; i<totalpage;i++)
    	{
    		int pshow = i+1;
    		strre +="<a class ='apaging' href =\"javascript:openURL('"+url+"&pageindex="+pshow+"&date="+tuNgay+"&dateTo="+denNgay+"&device="+device+"','_self');\">"+pshow+"</a>";
    	}
    	strre +="</span>";
    	return strre;
    }
    
    public String NumPage(String url, String tuNgay,String denNgay, String device,int totalpage, int pageindex)
    {
    	String strre ="<div style ='margin-top:8px; width:100%; margin-bottom:8px;' align ='center'><span>";
    	if (totalpage >1)
    	{
    		String cssf ="apaging";
    		String cssl ="apaging";
    		int pageb = pageindex -1;
    		String ahrefb = "javascript:openURL('"+url+"&pageindex="+pageb+"&date="+tuNgay+"&dateTo="+denNgay+"&device="+device+"','_self');";
    		int pagen = pageindex + 1;
    		if (pagen > totalpage)
    			pagen = totalpage;
    		String ahrefn = "javascript:openURL('"+url+"&pageindex="+pagen+"&date="+tuNgay+"&dateTo="+denNgay+"&device="+device+"','_self');";
    		String ahreff = "javascript:openURL('"+url+"&pageindex=1&date="+tuNgay+"&dateTo="+denNgay+"&device="+device+"','_self');";
    		String ahrefl = "javascript:openURL('"+url+"&pageindex="+totalpage+"&date="+tuNgay+"&dateTo="+denNgay+"&device="+device+"','_self');";
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
	    				strre +="<a class ='"+cssclass+"' href =\"javascript:openURL('"+url+"&pageindex="+pshow+"&date="+tuNgay+"&dateTo="+denNgay+"&device="+device+"','_self');\">"+spshow+"</a>";
	    		}
	    		else
	    		{
	    			if(pageindex > totalpage - 2)
	    			{
	    				if(pshow >=totalpage - 5)
	    					strre +="<a class ='"+cssclass+"' href =\"javascript:openURL('"+url+"&pageindex="+pshow+"&date="+tuNgay+"&dateTo="+denNgay+"&device="+device+"','_self');\">"+spshow+"</a>";
	    			}
	    			else
	    			{
	    				if (pshow >= pageindex - 2 && pshow <=pageindex +2)
	    				{
	    					if(pshow <=totalpage && pshow >0)
	    					strre +="<a class ='"+cssclass+"' href =\"javascript:openURL('"+url+"&pageindex="+pshow+"&date="+tuNgay+"&dateTo="+denNgay+"&device="+device+"','_self');\">"+spshow+"</a>";
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
        if(excel!=""){
        	
        	if(excel.equals("Export Excel"))
        	{  
        		java.util.Calendar c =java.util.Calendar.getInstance();  
                Date now = c.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String d = sdf.format(now);
             	response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.setHeader("Content-Disposition", "attachment; filename=baocaoHanhTrinhXe"+d +".xls");
                HSSFWorkbook wb = new HSSFWorkbook();
       	        HSSFSheet sheet = wb.createSheet("Dispatch");
       	        HSSFRow title=   sheet.createRow((short)1);
       	        
       	        
       			HSSFCellStyle cst = wb.createCellStyle();
       			HSSFFont ft = wb.createFont();
       			cst.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       			cst.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
       			ft.setFontHeightInPoints((short) 18);
       			     
		        ft.setBoldweight((short)ft.BOLDWEIGHT_BOLD);
		        cst.setFont(ft);
		     	HSSFCell ct = title.createCell((short) 0);
       			    
       			ct.setCellStyle(cst);
       			ct.setCellValue("BÁO CÁO HÀNH TRÌNH XE");
      			title.setHeightInPoints(40);
      			sheet.addMergedRegion(new CellRangeAddress(1,1, 0, 9));
      			HSSFCellStyle csNgay = wb.createCellStyle();
      			
      			HSSFFont fngay = wb.createFont();
      			HSSFRow rngay=   sheet.createRow((short)2);
  			 
      			fngay.setFontHeightInPoints((short)10);
      			fngay.setBoldweight((short)fngay.BOLDWEIGHT_BOLD);
      			csNgay.setFont(fngay);
      			HSSFCell cTuNgay = rngay.createCell((short) 1);
      			cTuNgay.setCellStyle(csNgay);
      			cTuNgay.setCellValue("Từ ");
      			rngay.createCell((short) 2).setCellValue(tuNgay);
      			HSSFCell cDenNgay = rngay.createCell((short) 3);
      			cDenNgay.setCellStyle(csNgay);
      			cDenNgay.setCellValue("Đến ");
      			rngay.createCell((short) 4).setCellValue(denNgay);
  			  
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
              	f.setFontHeightInPoints((short) 11);
              			      
              	f.setBoldweight((short)f.BOLDWEIGHT_BOLD);
              	cellStyle.setFont(f);
              			  
              			  
              	HSSFRow rowhead=   sheet.createRow((short)i);
            	HSSFCell h0=  rowhead.createCell((short) 0);
              	h0.setCellStyle(cellStyle);		
              	h0.setCellValue("TT");
              	
              	HSSFCell h1=  rowhead.createCell((short) 1);
              	h1.setCellStyle(cellStyle);		
              	h1.setCellValue("Thời điểm");
              	
              	HSSFCell h2= rowhead.createCell((short) 2);
              	h2.setCellStyle(cellStyle);	 
              	h2.setCellValue("Tọa độ");
              	
              	HSSFCell h3= rowhead.createCell((short) 3);
              	h3.setCellStyle(cellStyle); 
              	h3.setCellValue("Địa điểm");
              	
              	HSSFCell h4= rowhead.createCell((short) 4);
              	h4.setCellStyle(cellStyle);		 
              	h4.setCellValue("Ghi chú");              	             
       	    	
		       	try{
		       		DBCamera objcmr = new  DBCamera();
		       		rowhead.setHeightInPoints((short)40); 

               		EventData ed[] = objcmr.GetReportDetail(tuNgay, denNgay,xe,currAcct.getTimeZone(), currAcct.getAccountID(), new int[]{61714, 61472});
         			
            		
         			for(int j =0;j<ed.length;j++)
         			{

         				String day = ConvertFromEpoch (ed[j].getTimestamp(),currAcct.getTimeZone());
         				String tt=StatusCode.getDescription(currAcct.getAccountID(),ed[j].getStatusCode(), privLabel, null);
            			if(ed[j].getStatusLastingTime()>0) tt=tt+" "+ doiGio(ed[j].getStatusLastingTime());
         				
            			HSSFCellStyle csr = wb.createCellStyle();
            			csr.setBorderTop((short)1);
            			csr.setBorderRight((short)1);
            			csr.setBorderLeft((short)1);
            			csr.setBorderBottom((short)1);
            			HSSFRow row=   sheet.createRow((short)(i+1));
            			
            			HSSFCell r0=row.createCell((short) 0);
          				r0.setCellStyle(csr);
          				r0.setCellValue((i+1));
          				
          				HSSFCell r1=row.createCell((short) 1);
          				r1.setCellStyle(csr);		
          				r1.setCellValue(day);
      
          				HSSFCell r2=row.createCell((short) 2);
          				r2.setCellStyle(csr);
          				r2.setCellValue(round(ed[j].getLatitude(),5)+"/"+round(ed[j].getLongitude(),5));           
          				
          				HSSFCell r3=row.createCell((short) 3);
          				r3.setCellStyle(csr);
          				r3.setCellValue(GetUTF8FromNCRDecimalString(ed[j].getAddress()));
          				
          				HSSFCell r4=row.createCell((short) 4);
          				r4.setCellStyle(csr);
          				r4.setCellValue(ed[j].getNotes());
          				          				 
         				i++;         		
         			}
         			sheet.autoSizeColumn(0);
            		sheet.autoSizeColumn(1);
            		sheet.autoSizeColumn(2);
            		sheet.autoSizeColumn(3);
            		sheet.autoSizeColumn(4); 
            		
         		 OutputStream out = response.getOutputStream();
      	        wb.write(out);
      	        out.close();
		       	}
         		catch(Exception e){}
         		return;
        }}
        
        /* Style */
        HTMLOutput HTML_CSS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                String cssDir = BaoCaoHanhTrinhXe.this.getCssDirectory();
                WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
                WebPageAdaptor.writeCssLink(out, reqState, "scrollbar.css", cssDir);
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
              out.print( "<script src='./js/jquery.tinyscrollbar.min.js' type=\"text/javascript\"></script>");
               out.print("<script type='text/javascript'>$(document).ready(function() {$('#scrollbar2').tinyscrollbar();}); </script>");
                out.println("<script type='text/javascript' > $(function(){$('#myTable').tablesorter(); }); </script>");
                out.println("        <script type=\"text/javascript\" src=\"js/pdf.js\"></script>\n");
				out.println("<script> $(document).ready(function() { $('#running').remove(); "
						+ " $('#exportpdf').click(function (){ PrintContent('BÁO CÁO HÀNH TRÌNH XE')}); "
						+ " })</script>");
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
                HttpServletRequest request = reqState.getHttpServletRequest();
                String tuNgay=AttributeTools.getRequestString(request, "tuNgay", "");
                String contentall="";
                String denNgay=AttributeTools.getRequestString(request, "denNgay", "");
                int pindex = 1;
                int pindexl = 0;
                int pagesize = 1000;
                int tongtrang = 0;
                String flag ="0";
                int pagestatic = 1;
                String urlmap="";
                //HttpServletRequest request = reqState.getHttpServletRequest();
                String xem=    AttributeTools.getRequestString(request, "btnview", "");
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
                		tuNgay = sdate;
                	}
                }
                
                String  sdateTo = request.getParameter("dateTo");
                if(sdateTo !=null)
                {
                	if(sdateTo !="")
                	{
                		denNgay = sdateTo;
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
                
               //getTotal(String tuNgay,String denNgay, String device,String tenAccount,String idTram)
                //tongtrang = getTotal(tuNgay,denNgay, contentall,currAcct.getAccountID())/pagesize +1;
               // String sql = LoadImage(ngayxem, contentall, pindex, pagesize,currAcct.getAccountID());
               // String LoadTram(String tuNgay,String denNgay,String bienXe ,String Tram,String tenAccount,int currentPage,int Size )
              //  String sql = LoadReport(tuNgay,denNgay,contentall,currAcct.getAccountID(), pindex, pagesize,privLabel);
                  
                String sql=report(tuNgay,denNgay,contentall,currAcct.getTimeZone(),currAcct.getAccountID(),privLabel);
                if( currUser!=null)
                {
                	urlmap="./Track?account="+currAcct.getAccountID()+"&user="+currUser.getUserID()+"&device="+contentall+"&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="+tuNgay+"&r_menu=menu.rpt.devDetail&date_to="+denNgay+"&fmt=map";
                }
                else
                {
                	urlmap="./Track?account="+currAcct.getAccountID()+"&user=admin&device="+contentall+"&date_tz=GMT%2b07%3a00&r_report=EventDetail&page=report.show&date_fr="+tuNgay+"&r_menu=menu.rpt.devDetail&date_to="+denNgay+"&fmt=map";
                }
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String d = sdf.format(now);
                
               
                out.println("<span class='"+CommonServlet.CSS_MENU_TITLE+"'>Hành trình xe</span><br/>");
                out.println("<hr/>");
                out.println("<form name='AccountInfo' method='post' action='"+chgURL+"' target='_self'>\n");
              
                out.println("<table class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE+"' cellspacing='0' callpadding='0' border='0' width='100%' style='padding:15 0 15'>\n");
                
                out.print  ("<tr>\n");
                out.print  ("<td width='100px' align='right'>T&#x1EEB; ng&#x00E0;y:</td>\n");
                out.print  ("<td width='100px' align='left'>\n");
                out.print( "<input id='Text1' name='tuNgay' type='text' style='width:120px' class='textReadOnly' onclick=\"displayCalendar(this,'dd/mm/yyyy hh:ii',this,true,'','Text1')\" value='"+d+" 00:00' /></td>");
                if(tuNgay!="")
                {
				out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('Text1').value ='"+tuNgay+"'; </script>");
                }
                out.print  ("<td width='85px' align='right'>&#x0110;&#x1EBF;n ng&#x00E0;y:</td>\n");
                out.print  ("<td width='100px' align='left'>\n");
                out.print( "<input id='Text2' name='denNgay' type='text' class='textReadOnly' style='width:120px' onclick=\"displayCalendar(this,'dd/mm/yyyy hh:ii',this,true,'','Text2')\" value='"+d+" 23:59' /></td>");
                if(denNgay!="")
                {
                out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('Text2').value ='"+denNgay+"'; </script>");
                }
               
                 out.print( "<td width='100px' align='right'>Ch&#x1ECD;n Xe:</td><td align='left'>\n");
                 out.print  (CreateCbbDevice(currAcct.getAccountID(),contentall,reqState,privLabel));
                out.print  ("</td>");
              //  out.print( "<td width='100px' align='right'>Ch&#x1ECD;n Xe:</td><td align='left'>\n");
                //out.print  (user1);
                ///out.print  ("</td>");
                out.println("</tr>");
                //out.print  ("<tr>");
                //out.print  ("<td colspan='2' style ='height:400px; border:solid 1px silver;' >"+sql +currAcct.getAccountID()+ngayxem+"</td>");
                //out.println("</tr>");
                
                out.println("</table>");
                out.print("<table cellspacing='0' style='width:100%;padding:10px 0px 10px'><tbody><tr class='viewhoz'><td width='100px'></td><td width='100px'> <input type='submit' class='button1' name='btnview' value='Xem' id='btnview'></td><td width='105px'></td><td align='left' width='220px'><input type='submit' id='btnExcel' value='Export Excel' name='btnExcel' class='button1'></td><td align='left'><input type='button' id='exportpdf' value='Export PDF' name='exportpdf' class='button1' ></td>"); 
               if(record>0)
               {
                out.print("<td align='left'> <input type='button' class='button1' name='btnmap' value='B&#x1EA3;n &#x0111;&#x1ED3;' id='btnmap' onclick=\"javascript:openResizableWindow('"+urlmap+"','ReportMap',700,500)\"></td>");
               }
               else
               {
            	   out.print("<td align='left'></td>"); 
               }
                out.print("</tr></tbody></table>");
                //String load = LoadTram(ngayxem,contentall,currAcct.getAccountID());
                //out.print(load);
               // if(xem!="")
               // {
               // if(xem.equals("Xem"))
               // {
              
              // if(record>0)
              //  {
                out.print(sql);
             //   }
             
        		
              //  if(pindex < tongtrang)
                //	pindex++;
              //  if (pindexl>0)
               // 	pindexl--;
               // String NumPage(String url, String tuNgay,String denNgay,String idTram, String device,int totalpage, int pageindex)
                
               // out.write(NumPage(chgURL, tuNgay,denNgay, contentall, tongtrang,pagestatic));
               // }
                //}}
                /* end of form */
                out.write("<hr style='margin-bottom:5px;'>\n");
                out.write("<span style='padding-left:10px'>&nbsp;</span>\n");
               // out.print(request.getParameter("dateTo")+" "+request.getParameter("date")+" "+contentall+" "+currAcct.getAccountID()+" "+ pindex+" "+ pagesize);
                
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

