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
import java.util.TimeZone;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.io.*;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Array;
import java.net.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.opengts.util.*;
import org.opengts.dbtools.*;
import org.opengts.db.*;
import org.opengts.db.tables.*;

import org.opengts.war.report.ReportData;
import org.opengts.war.tools.*;
import org.opengts.war.track.*;

public class VuotTocDo
    extends WebPageAdaptor
    implements Constants
{
    
    // ------------------------------------------------------------------------
    // Parameters
	public  static final String COMMAND_INFO_UPDATE     = "update";
    // button types
    public  static final String PARM_BUTTON_CANCEL      = "a_btncan";
    public  static final String PARM_BUTTON_BACK        = "a_btnbak";
   // public  static final String PARM_FORMAT[]               = ReportMenu.PARM_FORMAT;
    // parameters
    //thanhtq
    public  static final String PARM_DATE_SL            = "a_date";
    public  static final String PARM_DEVICE_SL          = "a_device";
    public static int record							= 0;
    // ------------------------------------------------------------------------
    // WebPage interface
    
    public VuotTocDo()
    {
    	this.setBaseURI(RequestProperties.TRACK_BASE_URI());
        this.setPageName(PAGE_VUOTTOCDO);
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
        I18N i18n = privLabel.getI18N(VuotTocDo.class);
        return super._getMenuDescription(reqState,i18n.getString("VuotTocDo.Menu","B&#x00E1;o c&#x00E1;o v&#x01B0;&#x1EE3;t t&#x1ED1;c &#x0111;&#x1ED9;"));
    }
   
    public String getMenuHelp(RequestProperties reqState, String parentMenuName)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(VuotTocDo.class);
        return super._getMenuHelp(reqState,i18n.getString("VuotTocDo.MenuHelp","B&#x00E1;o c&#x00E1;o v&#x01B0;&#x1EE3;t t&#x1ED1;c &#x0111;&#x1ED9;"));
    }

    // ------------------------------------------------------------------------

    public String getNavigationDescription(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(VuotTocDo.class);
        return super._getNavigationDescription(reqState,i18n.getString("VuotTocDo.NavDesc","B&#x00E1;o c&#x00E1;o v&#x01B0;&#x1EE3;t t&#x1ED1;c &#x0111;&#x1ED9;"));
    }

    public String getNavigationTab(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(VuotTocDo.class);
        return i18n.getString("VuotTocDo.NavTab","B&#x00E1;o c&#x00E1;o v&#x01B0;&#x1EE3;t t&#x1ED1;c &#x0111;&#x1ED9;");
    }

    // ------------------------------------------------------------------------
    
   
    /*
    public String CreateCbbDevice(String accountid,String us, String idselect) throws IOException
    {
    	String strre = "<select id ='device' name = 'device' class='textReadOnly' style='width:100px;'>";
    	try 
    	{
    		DBCamera objcmr = new DBCamera();
    		ResultSet rs = objcmr.GetDiviceByAccountID2(accountid,us);
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
    }
    */
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
    public String CreateSpeed(String idselect) throws IOException
    {
    	String strre = "<select id ='tocdo' name = 'tocdo' class='textReadOnly' style='width:100px'>";
    	try 
    	{
    		//DBCamera objcmr = new DBCamera();
    		//ResultSet rs = new ResultSet();
    		String[] arr = {"40","50","60","80"};
    		for(int i=0; i< arr.length; i++)
    		{
    			if(idselect == arr[i].toString())
    				strre +="<option value ='"+arr[i].toString()+"' selected =\"selected\">"+arr[i].toString()+" Km/h</option>\n";
    			else
    				strre +="<option value ='"+arr[i].toString()+"'>"+arr[i].toString()+" Km/h</option>\n";
    		
    		}
    			
    	}
    	catch (Exception e)
    	{
    		
    	}
    	strre +="</select>\n";
    	strre +="<script type ='text/javascript' language ='javascript'> document.getElementById('tocdo').value = '"+idselect+"';</script>\n";
    	return strre;
    }
   
    private String getCurDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date date = new Date();
        return dateFormat.format(date);
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
    /*
    public String LoadVuotTocDo(String Account,  String device, String tuNgay, String denNgay,int tocDo, int page, int pagesize,PrivateLabel privLabel) throws IOException
    {
    	int dem=0;
    	String strscr ="";
    	record=0;
    	String mauNen="";
    	
    	try
    	{
    			
    		int num = 0;
    		DBCamera objcmr = new  DBCamera();
    		//ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
    		ResultSet rs = objcmr.reportVuotTocDo(Account,device, tuNgay,denNgay,tocDo, page, pagesize);
    		
    		//strscr =strscr+ "<div id='scrollbar2'><div class='scrollbar'><div class='track'><div class='thumb'><div class='end'></div></div></div></div><div class='viewport'><div class='overview'>";
    		strscr =strscr+ "<table width='100%'class='adminSelectTable_sortable' cellspacing='1' id='myTable' ><thead><tr  align='center'><th width='50px' class='adminTableHeaderCol_sort' >STT</th><th  class='adminTableHeaderCol_sort'>Th\u1EDDi \u0111i\u1EC3m</th><th width='110px' class='adminTableHeaderCol_sort'>Tr&#x1EA1;ng th&#x00E1;i</th><th width='100px' class='adminTableHeaderCol_sort'>T&#x1ED1;c &#x0111;&#x1ED9; (Km/h)</th><th width='110px' class='adminTableHeaderCol_sort'>Cửa</th><th width='200px' class='adminTableHeaderCol_sort'>T\u1ECDa \u0111\u1ED9</th><th class='adminTableHeaderCol_sort'>&#x0110;&#x1ECB;a ch&#x1EC9;</th></tr></thead>";
    		String cua = "Đóng";
    		while (rs.next())
    		{
    			if(rs.getInt(10) == 1) cua = "Đóng";
    			else cua = "Mở";	
    			String css ="";
    			if(num%2==0)
    				css = "adminTableBodyRowOdd";
    			else
    				css = "adminTableBodyRowEven";
    			
    			num++;
    			strscr =strscr+"<tr class ="+css+"><td>"+num+"</td><td>"+rs.getString(3)+" "+rs.getString(4)+"</td><td>"+StatusCode.getDescription(Account,Integer.parseInt(rs.getString(6)), privLabel, null)+"</td><td>"+rs.getString(5)+"</td><td>"+cua+"</td><td>"+rs.getString(7)+"/"+rs.getString(8)+"</td><td>"+rs.getString(9)+"</td></tr>";	
    			dem++;
    			record++;
    		}
    		strscr =strscr+"</table></div></div></div>";
    		
    	}
    	
    	catch (Exception e)
    	{
    	
    	}
    	return strscr;
    }*/
    public String Loadreport(String Account,  String device, String tuNgay, String denNgay,String tocDo, String timezone,PrivateLabel privLabel) throws IOException
    {
    	String strscr ="";
    	record=0;
     	try
    	{
    			
    		int num = 0;
    		DBCamera objcmr = new  DBCamera();
    		//ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
    		EventData ed[]= objcmr.GetTotalSpeedLine(tuNgay, denNgay, device, tocDo, timezone, Account);  
    		record= ed.length;
    		//strscr =strscr+ "<div id='scrollbar2'><div class='scrollbar'><div class='track'><div class='thumb'><div class='end'></div></div></div></div><div class='viewport'><div class='overview'>";
    		strscr =strscr+ "<table width='100%'class='adminSelectTable_sortable' cellspacing='1' id='myTable' ><thead><tr  align='center'><th width='50px' class='adminTableHeaderCol_sort' >STT</th><th  class='adminTableHeaderCol_sort'>Th\u1EDDi \u0111i\u1EC3m</th><th width='110px' class='adminTableHeaderCol_sort'>Tr&#x1EA1;ng th&#x00E1;i</th><th width='100px' class='adminTableHeaderCol_sort'>T&#x1ED1;c &#x0111;&#x1ED9; (Km/h)</th><th width='110px' class='adminTableHeaderCol_sort'>Cửa</th><th width='200px' class='adminTableHeaderCol_sort'>T\u1ECDa \u0111\u1ED9</th><th class='adminTableHeaderCol_sort'>&#x0110;&#x1ECB;a ch&#x1EC9;</th></tr></thead>";
    		String cua = "Đóng";
    		for(int i=0;i<ed.length;i++)
    		{
    			if(ed[i].getCua() == 1) cua = "Đóng";
    			else cua = "Mở";	
    			String css ="";
    			if(i %2==0)
    				css = "adminTableBodyRowOdd";
    			else
    				css = "adminTableBodyRowEven";
    			
    			String day = ConvertFromEpoch (ed[i].getTimestamp(),timezone);
    			strscr =strscr+"<tr class ="+css+"><td>"+(i+1)+"</td><td>"+day+"</td><td>"+StatusCode.getDescription(Account,ed[i].getStatusCode(), privLabel, null)+"</td><td>"+round(ed[i].getSpeedKPH(),2)+"</td><td>"+cua+"</td><td>"+round(ed[i].getLatitude(),5)+"/"+round(ed[i].getLongitude(),5)+"</td><td>"+ed[i].getAddress()+"</td></tr>";	
    		
    		}
    		strscr =strscr+"</table></div></div></div>";
    		
    	}
    	
    	catch (Exception e)
    	{
    	
    	}
    	return strscr;
    }
    /*
    public int getTotal(String Account,  String device, String tuNgay, String denNgay,int tocDo) throws IOException
    {
    	
    	
		
    	int tong1 =0;
    	try
    	{
    		
	    	DBCamera objcmr = new DBCamera();
	    	ResultSet rs = objcmr.TotalVuotTocDo(Account, device, tuNgay, denNgay, tocDo);
	    	while(rs.next())
	    	{
	    		tong1=rs.getInt(1);
	    	}
    	}
    	catch (Exception e)
    	{
    		
    	}
    	return tong1;
    }*/
    public String NumPage(String url, String tuNgay,String denNgay, String device,int tocdo,int totalpage)
    {
    	String strre ="<span>";
    	for(int i = 0; i<totalpage;i++)
    	{
    		int pshow = i+1;
    		strre +="<a class ='apaging' href =\"javascript:openURL('"+url+"&pageindex="+pshow+"&datefrom="+tuNgay+"&dateto="+denNgay+"&device="+device+"&tocdo="+tocdo+"','_self');\">"+pshow+"</a>";
    	}
    	strre +="</span>";
    	return strre;
    }
    public String NumPage(String url,String tuNgay, String denNgay, String device,int tocdo,int totalpage, int pageindex)
    {
    	String strre ="<div style ='margin-top:8px; width:100%; margin-bottom:8px;' align ='center'><span>";
    	if (totalpage >1)
    	{
    		String cssf ="apaging";
    		String cssl ="apaging";
    		int pageb = pageindex -1;
    		String ahrefb = "javascript:openURL('"+url+"&pageindex="+pageb+"&datefrom="+tuNgay+"&dateto="+denNgay+"&device="+device+"&tocdo="+tocdo+"','_self');";
    		int pagen = pageindex + 1;
    		if (pagen > totalpage)
    			pagen = totalpage;
    		String ahrefn = "javascript:openURL('"+url+"&pageindex="+pagen+"&datefrom="+tuNgay+"&dateto="+denNgay+"&device="+device+"&tocdo="+tocdo+"','_self');";
    		String ahreff = "javascript:openURL('"+url+"&pageindex=1&datefrom="+tuNgay+"&dateto="+denNgay+"&device="+device+"&tocdo="+tocdo+"','_self');";
    		String ahrefl = "javascript:openURL('"+url+"&pageindex="+totalpage+"&datefrom="+tuNgay+"&dateto="+denNgay+"&device="+device+"&tocdo="+tocdo+"','_self');";
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
	    				strre +="<a class ='"+cssclass+"' href =\"javascript:openURL('"+url+"&pageindex="+pshow+"&datefrom="+tuNgay+"&dateto="+denNgay+"&device="+device+"&tocdo="+tocdo+"','_self');\">"+spshow+"</a>";
	    		}
	    		else
	    		{
	    			if(pageindex > totalpage - 2)
	    			{
	    				if(pshow >=totalpage - 5)
	    					strre +="<a class ='"+cssclass+"' href =\"javascript:openURL('"+url+"&pageindex="+pshow+"&datefrom="+tuNgay+"&dateto="+denNgay+"&device="+device+"&tocdo="+tocdo+"','_self');\">"+spshow+"</a>";
	    			}
	    			else
	    			{
	    				if (pshow >= pageindex - 2 && pshow <=pageindex +2)
	    				{
	    					if(pshow <=totalpage && pshow >0)
	    					strre +="<a class ='"+cssclass+"' href =\"javascript:openURL('"+url+"&pageindex="+pshow+"&datefrom="+tuNgay+"&dateto="+denNgay+"&device="+device+"&tocdo="+tocdo+"','_self');\">"+spshow+"</a>";
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
    
    public String NCRToUnicode(String strInput)
    {
        String TCVN = "&#225;,&#224;,&#7841;,&#7843;,&#227;,&#226;,&#7845;,&#7847;,&#7853;,&#7849;,&#7851;,&#259;,&#7855;,&#7857;,&#7863;,&#7859;,&#7861;,&#é;,&#232;,&#7865;,&#7867;,&#7869;,&#234;,&#7871;,&#7873;,&#7879;,&#7875;,&#7877;,&#243;,&#242;,&#7885;,&#7887;,&#245;,&#244;,&#7889;,&#7891;,&#7897;,&#7893;,&#7895;,&#417;,&#7899;,&#7901;,&#7907;,&#7903;,&#7905;,&#250;,&#249;,&#7909;,&#7911;,&#361;,&#432;,&#7913;,&#7915;,&#7921;,&#7917;,&#7919;,&#237;,&#236;,&#7883;,&#7881;,&#297;,&#273;,&#253;,&#7923;,&#7925;,&#7927;,&#7929;,h";
        TCVN += "&#193;,&#192;,&#7840;,&#7842;,&#195;,&#194;,&#7844;,&#7846;,&#7852;,&#7848;,&#7850;,&#258;,&#7854;,&#7856;,&#7862;,&#7858;,&#7860;,&#200;,&#7864;,&#7866;,&#7868;,&#7870;,&#7872;,&#7878;,&#7874;,&#7876;,&#211;,&#210;,&#7884;,&#7886;,&#213;,&#212;,&#7888;,&#7890;,&#7896;,&#7892;,&#7894;,&#416;,&#7898;,&#7900;,&#7906;,&#7902;,&#7904;,&#218;,&#217;,&#7908;,&#7910;,&#360;,&#431;,&#7912;,&#7914;,&#7920;,&#7916;,&#7918;,&#272;,&#221;,&#7922;,&#7924;,&#7926;,&#7928;,h";
        String UNICODE = "á,à,ạ,ả,ã,â,ấ,ầ,ậ,ẩ,ẫ,ă,ắ,ằ,ặ,ẳ,ẵ,é,è,ẹ,ẻ,ẽ,ê,ế,ề,ệ,ể,ễ,ó,ò,ọ,ỏ,õ,ô,ố,ồ,ộ,ổ,ỗ,ơ,ớ,ờ,ợ,ở,ỡ,ú,ù,ụ,ủ,ũ,ư,ứ,ừ,ự,ử,ữ,í,ì,ị,ỉ,ĩ,đ,ý,ỳ,ỵ,ỷ,ỹ,h";
        UNICODE += "Á,À,Ạ,Ả,Ã,Â,Ấ,Ầ,Ậ,Ẩ,Ẫ,Ă,Ắ,Ằ,Ặ,Ẳ,Ẵ,È,Ẹ,Ẻ,Ẽ,Ế,Ề,Ệ,Ể,Ễ,Ó,Ò,Ọ,Ỏ,Õ,Ô,Ố,Ồ,Ộ,Ổ,Ỗ,Ơ,Ớ,Ờ,Ợ,Ở,Ỡ,Ú,Ù,Ụ,Ủ,Ũ,Ư,Ứ,Ừ,Ự,Ử,Ữ,Đ,Ý,Ỳ,Ỵ,Ỷ,Ỹ,h";
        String[] str = TCVN.split(",");
        String[] str1 = UNICODE.split(",");
        for (int i = 0; i < str.length; i++)
        {
            if (str[i] != "")
            {
                strInput = strInput.replace(str[i], str1[i]);
            }
        }
        return strInput;
    }
   
   
    
   public void writePage(
        final RequestProperties reqState,
       
        String pageMsg)
        throws IOException
    {
        final PrivateLabel privLabel = reqState.getPrivateLabel();
        final I18N    i18n     = privLabel.getI18N(VuotTocDo.class);
        final Locale  locale   = reqState.getLocale();
        final Account currAcct = reqState.getCurrentAccount();
        final User    currUser = reqState.getCurrentUser();
        final String  pageName = this.getPageName();
        String m = pageMsg;
        boolean error = false;
       
       
       
        
        HttpServletRequest request = reqState.getHttpServletRequest();
        HttpServletResponse response =reqState.getHttpServletResponse();
        String excel= AttributeTools.getRequestString(request, "btnExcel", "");
        String map =  AttributeTools.getRequestString(request, "btnmap", "");
        String contentall =  AttributeTools.getRequestString(request, "device", "");
        String datefrom =AttributeTools.getRequestString(request, "datefrom", "");
        String dateto = AttributeTools.getRequestString(request, "dateto", "");
        String tocdo= request.getParameter("tocdo");
        
     // view submit
        //datefrom = AttributeTools.getRequestString(request, "datefrom", "");
        //dateto = AttributeTools.getRequestString(request, "dateto", "");
       // contentall = AttributeTools.getRequestString(request, "device", "");
        //tocdo = AttributeTools.getRequestString(request, "tocdo", "");
        //String  sdevice = request.getParameter("device");
       
       
 
        
        if(excel!=""){
        	if(excel.equals("Export Excel"))
        	{  
        		 int i=0;
        		java.util.Calendar c =java.util.Calendar.getInstance();  
                Date now = c.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String d = sdf.format(now);
             	 response.setContentType("application/vnd.ms-excel;charset=utf-8");
                   response.setHeader("Content-Disposition", "attachment; filename=baoCaoVuotTocDo_"+d+".xls");
                   HSSFWorkbook wb = new HSSFWorkbook();
       	        HSSFSheet sheet = wb.createSheet("Dispatch");
       	        HSSFRow title=   sheet.createRow((short)1);
       	        
       	        i=i+3;
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
               			ct.setCellValue("BÁO CÁO VƯỢT TỐC ĐỘ");
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
              			  cTuNgay.setCellValue("Từ ");
              			  rngay.createCell((short) 2).setCellValue(datefrom);
              			 HSSFCell cDenNgay = rngay.createCell((short) 3);
             			  cDenNgay.setCellStyle(csNgay);
             			  cDenNgay.setCellValue("Đến ");
             			  rngay.createCell((short) 4).setCellValue(dateto);
              			  
              			  i=i+1;
              			  
              			  
              			  
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
       	     h2.setCellValue("Trạng thái");
       	    HSSFCell h3= rowhead.createCell((short) 3);
       	    h3.setCellStyle(cellStyle);		 
       	    h3.setCellValue("Tốc độ");
       	    HSSFCell h4= rowhead.createCell((short) 4);
       	    h4.setCellStyle(cellStyle);	
       	    h4.setCellValue("Cửa");
       	     HSSFCell h5=  rowhead.createCell((short) 5);
       	     h5.setCellStyle(cellStyle);	
       	     h5.setCellValue("Tọa độ");
       	    HSSFCell h6= rowhead.createCell((short) 6);
       	    	h6.setCellStyle(cellStyle);	
       	    	h6.setCellValue("Địa chỉ");
  	i=i+1;
       	    	rowhead.setHeightInPoints((short)40); 
       	 	try{
    	     
        	     
        	        DBCamera objcmr = new  DBCamera();
          		//ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
          		//ResultSet rs = objcmr.getTotalVuotTocDo(currAcct.getAccountID(),contentall, datefrom,dateto,Integer.parseInt(tocdo));
                  EventData ed[]= objcmr.GetTotalSpeedLine(datefrom+" 00:00", dateto+ " 23:59", contentall, tocdo, currAcct.getTimeZone(), currAcct.getAccountID());
          		//out.println("STT\t"+GetUTF8FromNCRDecimalString("Ng&#x00E0;y")+"\t"+GetUTF8FromNCRDecimalString("Gi&#x1EDD;")+"\t"+GetUTF8FromNCRDecimalString("Tr&#x1EA1;ng th&#x00E1;i")+"\tLat\tLon\t"+GetUTF8FromNCRDecimalString("T&#x1ED1;c &#x0111;&#x1ED9; (Km/h)")+"\t"+GetUTF8FromNCRDecimalString("&#x0110;&#x1ECB;a ch&#x1EC9;")+"");
          		String cua="Đóng";
          	
          			for(int j=0;j<ed.length;j++)
            		{
          				
          				if(ed[j].getCua() == 1) cua = "Đóng";
            			else cua = "Mở";
          				String day = ConvertFromEpoch (ed[j].getTimestamp(),currAcct.getTimeZone());
          				
          			
          				HSSFCellStyle csr = wb.createCellStyle();
            			csr.setBorderTop((short)1);
            			csr.setBorderRight((short)1);
            			csr.setBorderLeft((short)1);
            			csr.setBorderBottom((short)1);
            			HSSFRow row=   sheet.createRow((short)(i+1));
            			
            			HSSFCell r0=row.createCell((short) 0);
          				r0.setCellStyle(csr);
          				r0.setCellValue(ed[j].getDeviceID());
          				HSSFCell r1=row.createCell((short) 1);
          				r1.setCellStyle(csr);		
          				r1.setCellValue(day);
        	        	//row.createCell((short) 2).setCellValue(rs.getString(4));
          				HSSFCell r2=row.createCell((short) 2);
          				r2.setCellStyle(csr);
          				r2.setCellValue(NCRToUnicode(StatusCode.getDescription(currAcct.getAccountID(),ed[j].getStatusCode(), privLabel, null)));
          				HSSFCell r3=row.createCell((short) 3);
          				r3.setCellStyle(csr);
          				r3.setCellValue(round(ed[j].getSpeedKPH(), 2));
          				HSSFCell r4=row.createCell((short) 4);
          				r4.setCellStyle(csr);
          				r4.setCellValue(cua);
          				HSSFCell r5=row.createCell((short) 5);
          				r5.setCellStyle(csr);
          				r5.setCellValue(round(ed[j].getLatitude(), 5)+"/"+round(ed[j].getLongitude(),5));
          				HSSFCell r6=row.createCell((short) 6);
          				r6.setCellStyle(csr);
          				r6.setCellValue(NCRToUnicode(ed[j].getAddress()));
          				
          				i++;
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
       
        //if(map!=""){
        	
       // }
        
            
        
        /* Style */
        HTMLOutput HTML_CSS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                String cssDir = VuotTocDo.this.getCssDirectory();
                WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
                WebPageAdaptor.writeCssLink(out, reqState, "scrollbar.css", cssDir);
            }
        };

        /* javascript */
        HTMLOutput HTML_JS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                MenuBar.writeJavaScript(out, pageName, reqState);
                out.println("        <script type=\"text/javascript\" src=\"js/PCalendar.js\"></script>\n");
                out.println("        <script type=\"text/javascript\" src=\"js/jquery-1.4.2.min.js\"></script>\n");
                out.print( "<script src='./js/jquery.tinyscrollbar.min.js' type=\"text/javascript\"></script>");
                out.print("<script type='text/javascript'>$(document).ready(function() {$('#scrollbar2').tinyscrollbar();}); </script>");
                out.println( " <script type='text/javascript' src='js/jquery.tablesorter.min.js'></script>");
                out.println("<script type='text/javascript' src='js/sorttable.js'></script>");
                out.println("<script type='text/javascript' > $(function(){$('#myTable').tablesorter(); }); </script>");
            }
        };

        /* Content */
       
        
        HTMLOutput HTML_CONTENT  = new HTMLOutput(CommonServlet.CSS_CONTENT_FRAME, m) {
            public void write(PrintWriter out) throws IOException {
                //Print.logStackTrace("here");           
                String menuURL = privLabel.getWebPageURL(reqState, PAGE_MENU_TOP);
                String chgURL  = privLabel.getWebPageURL(reqState, pageName, COMMAND_INFO_UPDATE);
                String frameTitle = i18n.getString("Camera.PageTitle","VuotTocDo");
                HttpServletRequest request = reqState.getHttpServletRequest();
                String contentall = "";
                String datefrom = "homnay";
                String dateto = "homnay";
                String tocdo="";
                String urlmap="";
                int pindex = 1;
                int pindexl = 0;
                int pagesize = 50;
                int tongtrang = 0;
                String flag ="0";
                int pagestatic = 1;
                // frame content
             // view submit
               
                datefrom = AttributeTools.getRequestString(request, "datefrom", "");
                dateto = AttributeTools.getRequestString(request, "dateto", "");
                tocdo=AttributeTools.getRequestString(request, "tocdo", "0");
                if(datefrom==null || dateto==null )
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
                String  fdate = request.getParameter("datefrom");
                String  tdate = request.getParameter("dateto");
                String speed = request.getParameter("tocdo");
                if(fdate !=null)
                {
                	if(fdate !="")
                	{
                		datefrom = fdate;
                	}
                }
                if(speed !=null)
                {
                	if(speed !="")
                	{
                		tocdo = speed;
                	}
                }
                
                if(tdate !=null)
                {
                	if(tdate !="")
                	{
                		dateto= tdate;
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
                
                
                
               
                
                
               // tongtrang = getTotal(currAcct.getAccountID(),contentall,datefrom, dateto,Integer.parseInt(tocdo))/pagesize +1;
                String sql = Loadreport(currAcct.getAccountID(), contentall, datefrom+" 00:00", dateto +" 23:59", tocdo, currAcct.getTimeZone(), privLabel);
               //String sql=Loadreport("htxvt-dldongtien", "53n-4022", "15/11/2012 00:00", "16/11/2012 23:59", "40", currAcct.getTimeZone(), privLabel);
                if( currUser==null)
                {
                	urlmap="./Track?account="+currAcct.getAccountID()+"&user=admin&device="+contentall+"&r_option="+(Integer.parseInt(tocdo))+"mph&page=report.show&date_fr="+datefrom.replace("/", "%2f").concat("%2f00%3a00")+"&page_cmd_arg&r_limit&date_tz=GMT%2b07%3a00&r_report=EventSpeedOption&r_text&date_to="+dateto.replace("/", "%2f").concat("%2f23%3a59")+"&fmt=map";
                }
                else
                {
                	urlmap="./Track?account="+currAcct.getAccountID()+"&user="+currUser.getUserID()+"&device="+contentall+"&r_option="+(Integer.parseInt(tocdo))+"mph&page=report.show&date_fr="+datefrom.replace("/", "%2f").concat("%2f00%3a00")+"&page_cmd_arg&r_limit&date_tz=GMT%2b07%3a00&r_report=EventSpeedOption&r_text&date_to="+dateto.replace("/", "%2f").concat("%2f23%3a59")+"&fmt=map";
                }
                
               
                
                //if (ngayxem =="")
                //	ngayxem = getCurDateTime();
                                            
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String d = sdf.format(now);
               
                out.println("<span class='"+CommonServlet.CSS_MENU_TITLE+"'>B&#x00E1;o c&#x00E1;o v&#x01B0;&#x1EE3;t t&#x1ED1;c &#x0111;&#x1ED9;</span><br/>");
                out.println("<hr/>");
                out.println("<form name='AccountInfo' method='post' action='"+chgURL+"' target='_self'>\n");
              
                out.println("<table class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE+"' cellspacing='0' callpadding='0' border='0' width='100%' style='padding:15px 0 15px'>\n");
                
                out.print  ("<tr style='height:40px;'>\n");
                out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER+"' width='100px' >T&#x1EEB; ng&#x00E0;y:</td>\n");
                out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_DATA+"' width='200px'>\n");
                out.print  ("<input id='datefrom' class='textReadOnly' name='datefrom' type='text' style='width:100px' onclick=\"displayCalendar(this,'dd/mm/yyyy',this,false,'','datefrom')\" value='"+d+"' />");
                if(datefrom!="")
				{
                	out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('datefrom').value ='"+datefrom+"'; </script>");
                }
                out.print("</td>");
                out.print("<td align='right' width='100px'><span style='margin-left: 10px;margin-right:5px;'>Ch&#x1ECD;n t&#x1ED1;c &#x0111;&#x1ED9;:</span></td><td>\n");
                //out.print  ("<select name='tocdo' class='adminComboBox' id='tocdo' style='width:100px' ><option value='40' selected =\"selected\">40 km/h</option><option value='50'>50 km/h</option><option value='60'>60 km/h</option><option value='80'>80 km/h</option></select>");
                out.print(CreateSpeed(tocdo));
                out.print("</td>");
                
                out.print("</tr>");
                
                out.print("<tr style='height:40px'>");
                out.print("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER+"' width='100px' width='200px' >&#x0110;&#x1EBF;n ng&#x00E0;y:</td>\n");
                out.print("<td>");
                out.print( "<input id='dateto' class='textReadOnly' name='dateto' type='text' style='width:100px' onclick=\"displayCalendar(this,'dd/mm/yyyy',this,false,'','dateto')\" value='"+d+"' />");
                if(dateto!="")
				{
                	out.print("<script language ='javascript' type ='text/javascript'>document.getElementById('dateto').value ='"+dateto+"'; </script>");
                }
                out.print("</td>");
                out.print  ("<td align='right' width='100px'><span style='margin-left: 10px;margin-right:5px;'>"+i18n.getString("DeviceSelect","ch&#x1ECD;n xe:")+"</span></td><td>\n");  
              
                out.print  (CreateCbbDevice(currAcct.getAccountID(),contentall,reqState,privLabel));
               
                out.print("</td></tr>");
                
                out.println("</table>");
                
                out.print("<div class='viewhoz'>");
                out.println("<table >");
                out.print("<tr >");
                out.print("<td width='100px'></td>");
                out.print("<td> <input type ='submit' id ='btnview' value ='Xem' name ='btnview' class='button1'/>");
                out.print("</td>");
                //if(record>0)
                //{
                out.print  ("<td ><input type ='submit' id ='btnExcel' value ='Export Excel' name ='btnExcel' class='button1'/></td>");
                //out.print("<td > <input type='submit' class='button1' name='btnmap' value='B&#x1EA3;n &#x0111;&#x1ED3;' id='btnmap' onclick=\"javascript:openResizableWindow('Track?account="+currAcct.getAccountID()+"&user="+currUser.getUserID()+"&r_format=map&r_limType&date_tz=GMT%2b07%3a00&page_cmd_arg&date_fr="+datefrom+" 00:00&r_limit&page=report.show&date_to="+dateto+" 23:59&device="+contentall+"&page_cmd=rptsel&r_menu=menu.rpt.devPerf&r_emailAddr&r_option="+Integer.parseInt(tocdo+5)+"mph&r_report=EventSpeedOption','ReportMap',900,700)\"></td>");
                //}
                if(record>0)
                {
                    out.print  ("<td ><input type ='button' id ='btnmap' value ='B&#x1EA3;n &#x0111;&#x1ED3;' name ='btnmap' class='button1' onclick=\"javascript:openResizableWindow('"+urlmap+"','ReportMap',"+700+","+500+");\"/></td>");
                } 
                out.println("</tr>");
                
                out.println("</table>");
                out.print("</div>");
                out.print(sql);
                
              // if(pindex < tongtrang)
               // 	pindex++;
             //   if (pindexl>0)
              //  	pindexl--;
                
                
               // out.write(NumPage(chgURL, datefrom, dateto, contentall, Integer.parseInt(tocdo),tongtrang,pagestatic));
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