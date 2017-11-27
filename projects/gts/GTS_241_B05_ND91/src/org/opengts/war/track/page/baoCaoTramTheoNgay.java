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
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

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

public class baoCaoTramTheoNgay
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
    
    public baoCaoTramTheoNgay()
    {
    	this.setBaseURI(RequestProperties.TRACK_BASE_URI());
        this.setPageName(PAGE_REPORT_ZONE_BY_DAY);
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
        I18N i18n = privLabel.getI18N(baoCaoTramTheoNgay.class);
        return super._getMenuDescription(reqState,i18n.getString("baoCaoTramTheoNgay.Menu","B&#x00E1;o c&#x00E1;o tuy&#x1EBF;n theo ng&#x00E0;y"));
    }
   
    public String getMenuHelp(RequestProperties reqState, String parentMenuName)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(baoCaoTramTheoNgay.class);
        return super._getMenuHelp(reqState,i18n.getString("baoCaoTramTheoNgay.MenuHelp","B&#x00E1;o c&#x00E1;o tuy&#x1EBF;n theo ng&#x00E0;y"));
    }

    // ------------------------------------------------------------------------

    public String getNavigationDescription(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(baoCaoTramTheoNgay.class);
        return super._getNavigationDescription(reqState,i18n.getString("baoCaoTramTheoNgay.NavDesc","B&#x00E1;o c&#x00E1;o tuy&#x1EBF;n theo ng&#x00E0;y"));
    }

    public String getNavigationTab(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(baoCaoTramTheoNgay.class);
        return i18n.getString("baoCaoTramTheoNgay.NavTab","B&#x00E1;o c&#x00E1;o tuy&#x1EBF;n theo ng&#x00E0;y");
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
   
    public String chonTram(String Acc,String idselect)
    {
    	String strre = "<select id ='idTram' name = 'idTram' style='width:100px' class='textReadOnly'>";
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
    	strre +="<script type ='text/javascript' language ='javascript'> document.getElementById('idTram').value = '"+idselect+"';</script>\n";
    	return strre;
    	
    }
    
    public String LoadTram(String tuNgay,String denNgay,String bienXe ,String Tram,String tenAccount,int currentPage,int Size ) throws IOException
    {
    	
    	String strscr ="";
    
    	
    	
    	try
    	{
    		
    		
    		
    		int num = 0,i=0;
    		DBCamera objcmr = new  DBCamera();
    		//ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
    		ResultSet rs = objcmr.getRecoreTram(bienXe, tuNgay, denNgay, Tram, tenAccount, currentPage, Size);
    		
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
    			
    			num++;
    			i++;
    			strscr =strscr+"<tr class ="+css+"><td>"+(((currentPage-1)*Size)+i)+"</td><td>"+rs.getString(1)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(6)+"</td><td>"+rs.getString(3)+"</td><td>"+rs.getString(4)+"</td><td>"+rs.getString(5)+"</td><td>"+chuoi+"</td></tr>";	
    			
    		}
    		strscr =strscr+"</table></div>";
    		
    	}
    	catch (Exception e)
    	{
    	
    	}
    	return strscr;
    }
    	
    public int getTotal(String tuNgay,String denNgay, String device,String tenAccount,String idTram) throws IOException
    {
    	
    	
		
    	int tong1 =0;
    	try
    	{
    		
	    	DBCamera objcmr = new DBCamera();
	    	ResultSet rs = objcmr.getTotalRecoreTram(device, tuNgay, denNgay, idTram, tenAccount);
	    	while(rs.next())
	    	{
	    		tong1++;
	    	}
    	}
    	catch (Exception e)
    	{
    		
    	}
    	return tong1;
    }
    
    
    public String NumPage(String url, String tuNgay,String denNgay,String idTram, String device,int totalpage)
    {
    	String strre ="<span>";
    	for(int i = 0; i<totalpage;i++)
    	{
    		int pshow = i+1;
    		strre +="<a class ='apaging' href =\"javascript:openURL('"+url+"&pageindex="+pshow+"&date="+tuNgay+"&dateTo="+denNgay+"&idTram="+idTram+"&device="+device+"','_self');\">"+pshow+"</a>";
    	}
    	strre +="</span>";
    	return strre;
    }
    
    public String NumPage(String url, String tuNgay,String denNgay,String idTram, String device,int totalpage, int pageindex)
    {
    	String strre ="<div style ='margin-top:8px; width:100%; margin-bottom:8px;' align ='center'><span>";
    	if (totalpage >1)
    	{
    		String cssf ="apaging";
    		String cssl ="apaging";
    		int pageb = pageindex -1;
    		String ahrefb = "javascript:openURL('"+url+"&pageindex="+pageb+"&date="+tuNgay+"&dateTo="+denNgay+"&idTram="+idTram+"&device="+device+"','_self');";
    		int pagen = pageindex + 1;
    		if (pagen > totalpage)
    			pagen = totalpage;
    		String ahrefn = "javascript:openURL('"+url+"&pageindex="+pagen+"&date="+tuNgay+"&dateTo="+denNgay+"&idTram="+idTram+"&device="+device+"','_self');";
    		String ahreff = "javascript:openURL('"+url+"&pageindex=1&date="+tuNgay+"&dateTo="+denNgay+"&idTram="+idTram+"&device="+device+"','_self');";
    		String ahrefl = "javascript:openURL('"+url+"&pageindex="+totalpage+"&date="+tuNgay+"&dateTo="+denNgay+"&idTram="+idTram+"&device="+device+"','_self');";
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
	    				strre +="<a class ='"+cssclass+"' href =\"javascript:openURL('"+url+"&pageindex="+pshow+"&date="+tuNgay+"&dateTo="+denNgay+"&idTram="+idTram+"&device="+device+"','_self');\">"+spshow+"</a>";
	    		}
	    		else
	    		{
	    			if(pageindex > totalpage - 2)
	    			{
	    				if(pshow >=totalpage - 5)
	    					strre +="<a class ='"+cssclass+"' href =\"javascript:openURL('"+url+"&pageindex="+pshow+"&date="+tuNgay+"&dateTo="+denNgay+"&idTram="+idTram+"&device="+device+"','_self');\">"+spshow+"</a>";
	    			}
	    			else
	    			{
	    				if (pshow >= pageindex - 2 && pshow <=pageindex +2)
	    				{
	    					if(pshow <=totalpage && pshow >0)
	    					strre +="<a class ='"+cssclass+"' href =\"javascript:openURL('"+url+"&pageindex="+pshow+"&date="+tuNgay+"&dateTo="+denNgay+"&idTram="+idTram+"&device="+device+"','_self');\">"+spshow+"</a>";
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
  		String Tram =AttributeTools.getRequestString(request, "idTram", "");
  		int i=0;
        if(excel!=""){
        	
        	if(excel.equals("Export Excel"))
        	{  
        		java.util.Calendar c =java.util.Calendar.getInstance();  
                Date now = c.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String d = sdf.format(now);
             	 response.setContentType("application/vnd.ms-excel;charset=utf-8");
                   response.setHeader("Content-Disposition", "attachment; filename=baoCaoTramTheoNgay_"+d+".xls");
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
               			ct.setCellValue("BÁO CÁO TRẠM THEO NGÀY");
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
              			  cTuNgay.setCellValue("Từ ngày");
              			  rngay.createCell((short) 2).setCellValue(tuNgay);
              			 HSSFCell cDenNgay = rngay.createCell((short) 3);
             			  cDenNgay.setCellStyle(csNgay);
             			  cDenNgay.setCellValue("Đến ngày");
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
        	       // out.println(xe+";"+tuNgay+";"+denNgay+";"+Tram);
        	        ResultSet rs = objcmr.getTotalRecoreTram(xe, tuNgay, denNgay, Tram, currAcct.getAccountID());
          	
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
     	        out.close();} catch(Exception e){}
        	return;
        }}
        
        
        
        
        
        
        
        
        
        /* Style */
        HTMLOutput HTML_CSS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                String cssDir = baoCaoTramTheoNgay.this.getCssDirectory();
                WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
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
                String frameTitle = i18n.getString("baocaoTram.PageTitle","Tr&#x1EA1;m");
                
                // frame content
             // view submit
                String tuNgay="",contentall="",denNgay="",Tram="";
                int pindex = 1;
                int pindexl = 0;
                int pagesize = 12;
                int tongtrang = 0;
                String flag ="0";
                int pagestatic = 1;
                HttpServletRequest request = reqState.getHttpServletRequest();
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
                
                
                String  sdevice = request.getParameter("device");
                if(sdevice !=null)
                {
                	if(sdevice !="")
                	{
                		contentall = sdevice;
                	}
                }
                
                String  sTram = request.getParameter("idTram");
                if(sTram !=null)
                {
                	if(sTram !="")
                	{
                		Tram = sTram;
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
                tongtrang = getTotal(tuNgay,denNgay, contentall,currAcct.getAccountID(),Tram)/pagesize +1;
               // String sql = LoadImage(ngayxem, contentall, pindex, pagesize,currAcct.getAccountID());
               // String LoadTram(String tuNgay,String denNgay,String bienXe ,String Tram,String tenAccount,int currentPage,int Size )
                String sql = LoadTram(tuNgay,denNgay,contentall,Tram,currAcct.getAccountID(), pindex, pagesize);
                
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String d = sdf.format(now);
                
               
                out.println("<span class='"+CommonServlet.CSS_MENU_TITLE+"'>B&#x00E1;o c&#x00E1;o tuy&#x1EBF;n theo ng&#x00E0;y</span><br/>");
                out.println("<hr/>");
                out.println("<form name='AccountInfo' method='post' action='"+chgURL+"' target='_self'>\n");
              
                out.println("<table class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE+"' cellspacing='10' callpadding='0' border='0' width='100%' style='padding:15 0 15'>\n");
                
                out.print  ("<tr>\n");
                out.print  ("<td width='80' align='right'>T&#x1EEB; ng&#x00E0;y:</td>\n");
                out.print  ("<td width='100px' align='left'>\n");
                out.print( "<input id='Text1' name='tuNgay' type='text' style='width:100px' class='textReadOnly' onclick=\"displayCalendar(this,'yyyy/mm/dd',this,false,'','Text1')\" value='"+d+"' /></td>");
                if(tuNgay!="")
                {
				out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('Text1').value ='"+tuNgay+"'; </script>");
                }
                out.print  ("<td width='85px' align='right'>&#x0110;&#x1EBF;n ng&#x00E0;y:</td>\n");
                out.print  ("<td  align='left'>\n");
                out.print( "<input id='Text2' name='denNgay' type='text' class='textReadOnly' style='width:100px' onclick=\"displayCalendar(this,'yyyy/mm/dd',this,false,'','Text2')\" value='"+d+"' /></td>");
                if(denNgay!="")
                {
                out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('Text2').value ='"+denNgay+"'; </script>");
                }
                out.print  ("</tr><tr><td  align='right' >Ch&#x1ECD;n Tr&#x1EA1;m:\n</td><td width='100px' align='left'>");
                out.print  (chonTram(currAcct.getAccountID(),Tram));
                 out.print( "</td><td  align='right'>Ch&#x1ECD;n Xe:\n</td><td  align='left'>");
                 out.print  (CreateCbbDevice(currAcct.getAccountID(),contentall,reqState,privLabel));
                out.print  ("</td>");
                out.println("</tr>");
                //out.print  ("<tr>");
                //out.print  ("<td colspan='2' style ='height:400px; border:solid 1px silver;' >"+sql +currAcct.getAccountID()+ngayxem+"</td>");
                //out.println("</tr>");
                
                out.println("</table>");
                out.print("<table cellspacing='0' style='width:100%;padding:10px 0px 10px'><tbody><tr class='viewhoz'><td width='100px'></td><td width='100px'> <input type='submit' class='button1' name='btnview' value='Xem' id='btnview'></td><td width='105px'></td><td align='left'><input type='submit' id='btnExcel' value='Export Excel' name='btnExcel' class='button1'></td><td></td></tr></tbody></table>");
                	if(xem.equals("Xem")||spindex!=null)
               out.print(sql);
                
                if(pindex < tongtrang)
                	pindex++;
                if (pindexl>0)
                	pindexl--;
               // String NumPage(String url, String tuNgay,String denNgay,String idTram, String device,int totalpage, int pageindex)
                
                out.write(NumPage(chgURL, tuNgay,denNgay,Tram, contentall, tongtrang,pagestatic));
              
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
