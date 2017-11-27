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

public class Camera
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
    
    public Camera()
    {
    	this.setBaseURI(RequestProperties.TRACK_BASE_URI());
        this.setPageName(PAGE_CAMERA);
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
        I18N i18n = privLabel.getI18N(Camera.class);
        return super._getMenuDescription(reqState,i18n.getString("Camera.Menu","H&#x00EC;nh &#x1EA3;nh camera"));
    }
   
    public String getMenuHelp(RequestProperties reqState, String parentMenuName)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(Camera.class);
        return super._getMenuHelp(reqState,i18n.getString("Camera.MenuHelp","H&#x00EC;nh &#x1EA3;nh camera"));
    }

    // ------------------------------------------------------------------------

    public String getNavigationDescription(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(Camera.class);
        return super._getNavigationDescription(reqState,i18n.getString("Camera.NavDesc","H&#x00EC;nh &#x1EA3;nh camera"));
    }

    public String getNavigationTab(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(Camera.class);
        return i18n.getString("Camera.NavTab","H&#x00EC;nh &#x1EA3;nh camera");
    }

    // ------------------------------------------------------------------------
    public boolean isOkToDisplay(RequestProperties reqState)
    {
        Account account = (reqState != null)? reqState.getCurrentAccount() : null;
        if (account == null) {
            return false; // no account?
        } else
        {
        	int dem=0;
        	DBCamera objcmr = new  DBCamera();
    		dem = objcmr.phanQuyen(account.getAccountID(),"Camera" );
    		 
    		if(dem>0)
    			return true;
    		else
    			return false;
        	
        }
    }
    
    
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
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
    public String LoadImage(String ngay, String device, int page, int pagesize) throws IOException
    {
    	String strscr ="";
    	String strre =  "<ul>\n";
    	try
    	{
    		int num = 0;
    		DBCamera objcmr = new  DBCamera();
    		ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
    		while (rs.next())
    		{
    			strscr+="http://123.30.169.77/ImgGTS/"+rs.getString("LinkImage")+";";
    			strre +="<li><a href ='#?http://123.30.169.77/ImgGTS/"+rs.getString("LinkImage")+"' rel='popup1' class='poplight'>";
    			
    			strre +="<img style ='' src = 'http://123.30.169.77/ImgGTS/"+rs.getString("LinkImage")+"'/><br/>";
    			strre +=rs.getString("timecreated");
    			strre +="</a></li>\n";
    			
    		}
    		strre +="</ul>\n";
    		strre +="<script type ='text/javascript'> listimage = '"+strscr+"'; </script>";
    	}
    	catch (Exception e)
    	{
    	
    	}
    	return strre;
    }
    public int getTotal(String ngay, String device) throws IOException
    {
    	int tong1 =0;
    	try
    	{
    		
	    	DBCamera objcmr = new DBCamera();
	    	ResultSet rs = objcmr.GetCameraTotal(ngay, device);
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
        final I18N    i18n     = privLabel.getI18N(Camera.class);
        final Locale  locale   = reqState.getLocale();
        final Account currAcct = reqState.getCurrentAccount();
        final User    currUser = reqState.getCurrentUser();
        final String  pageName = this.getPageName();
        String m = pageMsg;
        boolean error = false;
        
        
        
        /* Style */
        HTMLOutput HTML_CSS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                String cssDir = Camera.this.getCssDirectory();
                WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
            }
        };

        /* javascript */
        HTMLOutput HTML_JS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                MenuBar.writeJavaScript(out, pageName, reqState);
               // JavaScriptTools.writeJSInclude(out, JavaScriptTools.qualifyJSFileRef("PCalendar.js"));
                //JavaScriptTools.writeJSInclude(out, JavaScriptTools.qualifyJSFileRef("jquery-1.4.2.min.js"));
                //JavaScriptTools.writeJSInclude(out, JavaScriptTools.qualifyJSFileRef("PopImage.js"));
                out.println("        <script type=\"text/javascript\" src=\"js/PCalendar.js\"></script>\n");
                out.println("        <script type=\"text/javascript\" src=\"js/jquery-1.4.2.min.js\"></script>\n");
                out.println("        <script type=\"text/javascript\" src=\"js/PopImage.js\"></script>\n");
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
                String frameTitle = i18n.getString("Camera.PageTitle","Hi&#x1EC3;n th&#x1ECB; h&#x00EC;nh &#x1EA3;nh camera");
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
                ngayxem = AttributeTools.getRequestString(request, "txtngay", "");
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
                tongtrang = getTotal(ngayxem, contentall)/pagesize +1;
                String sql = LoadImage(ngayxem, contentall, pindex, pagesize);
                if (ngayxem =="")
                	ngayxem = getCurDateTime();
                                            
                
                String 		popImage 		 = "<div id=\"popup1\" class=\"popup_block\">"
                							 + "<img style =\"width:500px;  border:none;\"  id = \"imgshow\"/>"
                							 + "<div class ='buttonleft' onclick =\"BackImage();\"><img src ='./images/left_1.GIF'/></div>"
                							 + "<div class ='buttonright' onclick =\"NextImage();\"><img src ='./images/right_1.GIF'/></div>"
                							 + "</div>\n";
                out.println("<span class='"+CommonServlet.CSS_MENU_TITLE+"'>"+frameTitle+"</span><br/>");
                out.println("<hr/>");
                out.println("<form name='AccountInfo' method='post' action='"+chgURL+"' target='_self'>\n");
                out.println(popImage);
                out.println("<table class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE+"' cellspacing='0' callpadding='0' border='0' width='100%' style='padding:15 0 15'>\n");
                
                out.print  ("<tr>\n");
                out.print  ("<td width='100px' align='right'>"+i18n.getString("Camera.DateSelect","Ch&#x1ECD;n ng&#x00E0;y:")+"</td>\n");
                out.print  ("<td width='100px' align='left'>\n");
                out.print  (Form_TextField("txtngay", "txtngay", true, ngayxem,"displayCalendar(this,'dd/mm/yyyy',this,false,'',this.id)", 20, 40,"textReadOnly")+"\n<script language ='javascript' type ='text/javascript'> var now=new Date();var nam=now.getFullYear();var thang=now.getMonth() + 1;var ngay=now.getDate(); document.getElementById('txtngay').value ='"+ngayxem+"'; </script>\n");
                out.print  ("</td><td align='right' width='100px'>"+i18n.getString("DeviceSelect","Ch&#x1ECD;n xe:")+"</td><td align='left'>\n");
                out.print  (CreateCbbDevice(currAcct.getAccountID(),contentall,reqState,privLabel));
                out.print  ("</td></tr> ");

                out.println("</table>");
                out.print("<table cellspacing='0' style='width:100%;padding:10px 0px 10px'><tr class='viewhoz'><td width='100px'></td><td><input type ='submit' id ='btnview' value ='Xem' name ='btnview' class='button1'/>");
                out.print  ("</td>");
                out.println("</tr></table>");
                out.print  ("<table width='100%'><tr>");
                out.print  ("<td   ><div class=\"img_container\">"+sql +"</div></td>");
                out.println("</tr></table>");
                if(pindex < tongtrang)
                	pindex++;
                if (pindexl>0)
                	pindexl--;
                
                
                out.write(NumPage(chgURL, ngayxem, contentall, tongtrang,pagestatic));
                /* end of form */
              //  out.write("<hr style='margin-bottom:5px;'>\n");
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
