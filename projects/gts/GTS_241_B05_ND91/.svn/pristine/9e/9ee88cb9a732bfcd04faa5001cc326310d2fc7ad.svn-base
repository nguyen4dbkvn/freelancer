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
//  2007/06/03  Martin D. Flynn
//     -Initial release
//  2007/06/13  Martin D. Flynn
//     -Added support for browsers with disabled cookies
//  2007/07/27  Martin D. Flynn
//     -Menu bar descriptions changed to use 'getNavigationTab(...)'
//  2007/12/13  Martin D. Flynn
//     -Added support for pull-down tab menus
//  2008/02/21  Martin D. Flynn
//     -Moved menubar javascript tools to 'MenuBar.js'
//  2008/09/12  Martin D. Flynn
//     -Move "menuBar.usePullDownMenus" property definition to PrivateLabel.java
//  2008/09/19  Martin D. Flynn
//     -Tabs with no sub-options will no longer be displayed.
//  2011/03/08  Martin D. Flynn
//     -Fixed dynamic HTML/JavaScript issue when writing menu options that call
//      "openFixedWindow" and "menuBar.includeTextAnchor" is true.
// ----------------------------------------------------------------------------
// Helpful references:
//   http://www.scriptforest.com/javascript_cascading_menu.html
// ----------------------------------------------------------------------------
package org.opengts.war.tools;

import java.util.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.JspWriter;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import org.opengts.util.*;
import org.opengts.dbtools.*;
import org.opengts.db.*;
import org.opengts.db.tables.*;

import org.opengts.war.track.Track;

public class MenuBar
    implements org.opengts.war.track.Constants
{

    // ------------------------------------------------------------------------

    public static final boolean DFT_USE_PULL_DOWN_MENUS     = true;

    public static final int     MENU_TIMEOUT_MS             = 3700;

    // ------------------------------------------------------------------------

    public static final String  CSS_MENU_TAB_UNSEL_WIDE     = "menuBarUnsW";    // TD
    public static final String  CSS_MENU_TAB_SEL_WIDE       = "menuBarSelW";
    public static final String  CSS_MENU_TAB_UNSEL          = "menuBarUns";
    public static final String  CSS_MENU_TAB_SEL            = "menuBarSel";
    public static final String  CSS_MENU_BAR                = "menuBar";        // TD
    public static final String  CSS_MENU_TABLE              = "menuSubFrame";
    public static final String  CSS_MENU_TABLE_ROW          = "menuSubItemRow";
    public static final String  CSS_MENU_CELL               = "menuSubItemCol";
    public static final String  CSS_MENU_LINK               = "menuSubItemLink";

    // ------------------------------------------------------------------------

    public static final String MENU_MAIN                    = "menu.main";
        // Main Menu

    public static final String MENU_ADMIN                   = "menu.admin";
        // Account[X]
        // User[X]
        // Device[X]/Configuration
        // DeviceGroups
        // Entity/Trailer
        // GeoZones

    public static final String MENU_TRACK_DEVICE            = "menu.track.device";
        // Device Map[X]

    public static final String MENU_TRACK_FLEET             = "menu.track.fleet";
        // DeviceGroup Map

    public static final String MENU_REPORTS                 = "menu.rpts";
    public static final String MENU_REPORTS_DEVDETAIL       = "menu.rpts.devDetail";
    public static final String MENU_REPORTS_QLSIM       = "menu.rpts.qlSim";
        // Device Detail[X]
        //      Event Detail[X]
        //      Temperature Monitoring[X]
        //      J1708 Fault codes[X]
    public static final String MENU_REPORTS_GRPDETAIL       = "menu.rpts.grpDetail";
        // Group Detail[X]
        //      Trip Report[X]
        //      Geozone Report[X]
    public static final String MENU_REPORTS_GRPSUMMARY      = "menu.rpts.grpSummary";
        // Device Summary[X]
        //      Last known Device location[X]
        //      Last known Entity location[X]
    public static final String MENU_REPORTS_PERFORM         = "menu.rpts.performance";
        // Driver Performance
        //      Excess Speed[X]
        //      Hard Braking
        //      Driving/Stop Time Report[X]
    public static final String MENU_REPORTS_IFTA            = "menu.rpts.ifta";
        // IFTA
        //      Stateline Crossing Detail[X]
        //      State Mileage Summary[X]
        //      Fueling Detail
        //      Fueling Summary
    public static final String MENU_REPORTS_SYSADMIN        = "menu.rpts.sysadmin";
        // SysAdmin
        //      Unassigned Devices

    // ------------------------------------------------------------------------

    public static void writeJavaScript(JspWriter out, String pageName, RequestProperties reqState)
        throws IOException
    {
        MenuBar.writeJavaScript(new PrintWriter(out, out.isAutoFlush()), pageName, reqState);
    }

    public static String[] SubMenu = new String[40];
    public static void writeJavaScript(PrintWriter out, String pageName, RequestProperties reqState)
        throws IOException
    {
        PrivateLabel       privLabel = reqState.getPrivateLabel();
        HttpServletRequest request   = reqState.getHttpServletRequest();

        /* need JavaScript? */
        if (!privLabel.getBooleanProperty(PrivateLabel.PROP_MenuBar_usePullDownMenus,DFT_USE_PULL_DOWN_MENUS)) {
            // don't bother with JavaScript
            return;
        }

        /* MenuBar.js */
        JavaScriptTools.writeJSInclude(out, JavaScriptTools.qualifyJSFileRef("fixiePNG.js"), request);
        JavaScriptTools.writeJSInclude(out, JavaScriptTools.qualifyJSFileRef("MenuBar.js"), request);
        
        
        /* include anchor link */
        boolean inclAnchor = privLabel.getBooleanProperty(PrivateLabel.PROP_MenuBar_includeTextAnchor,false);

        /* specific menu items */
        int itemHeight = 13;
        JavaScriptTools.writeStartJavaScript(out);
        out.println("function mnubarCreateSubMenu(mainObj) {");
        out.println("  var itemHeight = "+itemHeight+";");
        out.println("  var id = mainObj.id;");
        out.println("  var absLoc = getElementPosition(mainObj);");
        out.println("  var absSiz = getElementSize(mainObj);");
        out.println("  for (;;) {");
        Map<String,MenuGroup> menuMap = privLabel.getMenuGroupMap();
        
        int i=0;
        for (String mgn : menuMap.keySet()) {
        	
        	
        	
        	
        	
            MenuGroup mg = menuMap.get(mgn);
            if (mg.showInMenuBar()) {
            	
            	
                java.util.List<WebPage> pageList = mg.getWebPageList(reqState);
                if (!ListTools.isEmpty(pageList)) {
                    out.println("    if (id == '"+mgn+"') {");
                    out.println("      mbSubMenuObj = mnubarCreateMenuFrame(absLoc,absSiz,(("+pageList.size()+"*itemHeight)+6));");
                    out.println("      mbSubMenuObj.innerHTML = ");
                    out.println("        \"<table class='"+CSS_MENU_TABLE+"' cellspacing='0' cellpadding='0'>\" +");
                    for (WebPage wp : pageList) {
                        String url  = wp.encodePageURL(reqState);
                        String desc = wp.getNavigationTab(reqState);
                        String help = wp.getMenuHelp(reqState, null);
                        
                        
                        
                        
                        
                        out.write("        ");
                        out.write("\"<tr class='"+CSS_MENU_TABLE_ROW+"'>");
                        String target  = StringTools.blankDefault(wp.getTarget(),"_self"); // (wp instanceof WebPageURL)? ((WebPageURL)wp).getTarget() : "_self";
                        String onclick = "openURL('"+url+"','"+target+"')";
                        if (!target.startsWith("_")) {
                            PixelDimension pixDim = wp.getWindowDimension();
                            if (pixDim != null) {
                                int W = pixDim.getWidth();
                                int H = pixDim.getHeight();
                                onclick = "openFixedWindow('"+url+"','"+target+"',"+W+","+H+")";
                            }
                        }
                        out.write("<td class='"+CSS_MENU_CELL+"' height='\"+itemHeight+\"' onclick=\\\"javascript:"+onclick+";\\\" title=\\\""+help+"\\\">");
                       
                       
                        
                        if (inclAnchor) { 
                            // "menuBar.includeTextAnchor"
                        	
                        	
                        	
                        	
                            out.write("<a class='"+CSS_MENU_LINK+"'");
                            if (target.startsWith("_")) {
                                out.write(" href='"+url+"' target='"+target+"'");
                            } else {
                                PixelDimension pixDim = wp.getWindowDimension();
                                if (pixDim == null) {
                                    out.write(" href='"+url+"' target='"+target+"'");
                                }
                            }
                            out.write(">");
                            out.write(desc);
                            out.write("</a>");
                        } else {
                            out.write(desc);
                        }
                        out.write("</td>");
                        out.write("</tr>\" +\n");
                    }
                    out.println("        \"</table>\";");
                    out.println("      break;");
                    out.println("    }");
                }
            }
            i++;
        }
        
        
        out.println("    break; // error");
        out.println("  }");	
        out.println("  if (mbSubMenuObj) { document.body.appendChild(mbSubMenuObj); }");	
        out.println("  return mbSubMenuObj;");	
        out.println("}");	
        JavaScriptTools.writeEndJavaScript(out);

    }
    
    // ------------------------------------------------------------------------

    public static void writeTableRow(JspWriter out, String pageName, RequestProperties reqState)
        throws IOException
    {
        MenuBar.writeTableRow(new PrintWriter(out, out.isAutoFlush()), pageName, reqState);
    }

    /* write out the menu bar */
    public static void writeTableRow(PrintWriter out, String pageName, RequestProperties reqState)
    {
    	String img[ ]={"images/menu/main.png","images/menu/map.png","images/menu/report.png","images/menu/setting.png","images/menu/setting1.png","images/menu/setting1.png","","",""};
    	int dem =0;
    	
        final PrivateLabel privLabel = reqState.getPrivateLabel(); 
        final Account currAcct = reqState.getCurrentAccount();
        final User    currUser = reqState.getCurrentUser();
        

        /* start menu bar row */
        out.write("\n");
        out.write("<!-- Begin Menu Bar -->\n");
        out.write("<td valign='top'>\n");

       
        if (privLabel.getBooleanProperty(PrivateLabel.PROP_MenuBar_usePullDownMenus,DFT_USE_PULL_DOWN_MENUS)) {
            
            Map<String,MenuGroup> menuMap = privLabel.getMenuGroupMap();
            boolean openOnMouseOver = privLabel.getBooleanProperty(PrivateLabel.PROP_MenuBar_openOnMouseOver,false);
            
          out.print("<div id='container'><ul>");
            for (String mgn : menuMap.keySet()) {
                MenuGroup mg = menuMap.get(mgn);
               
                if (mg.showInMenuBar() && !ListTools.isEmpty(mg.getWebPageList(reqState))) {
                	
                	 
                	
                    String desc = mg.getTitle(reqState.getLocale());
                    
                    out.print("<li><a href='#'><table><tr><td width='30' align='left'><img src='"+img[dem]+"' width='20px' height='18px'/></td><td style='font-weight:bold;'>"+desc+"</td><td width='15' align='center'><img src=\"images/menu/icon_drop_down.png\" alt=\"\" width=\"6\"/></td><td><img alt='' src='images/menu/line_menu.png'/></td></tr></table></a><ul>");
                  
                    //out.print(desc);
                    /*if(img[dem].equals("images/menu/report.png"))
                    {
                    	
                    	 out.print("<li onclick='javascript:openURL(\"http://baocao.gpstst.com/\",\"_blank\")'><a href='#'><table><tr><td ><img width='18px' height='18px' src='extra/images/mw/bcTram.png'></td><td>Báo cáo chi tiết</td></tr></table></a></li>");
                    }*/
                    java.util.List<WebPage> pageList = mg.getWebPageList(reqState);
                    for (WebPage wp : pageList) {
                        String url  = wp.encodePageURL(reqState);
                        String mota = wp.getNavigationTab(reqState);
                        String help = wp.getMenuHelp(reqState, null);
                      String icon=  wp.getMenuIconImage();
                      if(icon!=null)
                        out.print("<li onclick='javascript:openURL(\""+url+"\",\"_self\")'><a href='#'><table><tr><td><img src='"+icon+"' width='18px' height='18px'/></td><td >"+mota+"</td></tr></table></a></li>");
                      else
                    	  
                    	  out.print("<li onclick='javascript:openURL(\""+url+"\",\"_self\")'><a href='#'><table><tr><td width='18px'></td><td>"+mota+"</td></tr></table></a></li>");
                    }
                    
                    out.print("</ul></li>");
                    
                   dem++;
                    //MinhNV viet o day;
                   
                  //  buttonValue[dem]=desc;
                   // 
                   //  dem++;
                    
                    
                    //out.print(desc +mgn);
                 
                 //tam xoa cho nay
                   // out.write(" <td id='"+mgn+"' class='"+CSS_MENU_TAB_UNSEL_WIDE+"'");
                  //  out.write(" onmouseover=\"mnubarMouseOverTab('"+mgn+"',"+openOnMouseOver+")\"");
                   // out.write(" onmouseout=\"mnubarMouseOutTab('"+mgn+"')\"");
                   // out.write(" onclick=\"mnubarToggleMenu('"+mgn+"')\">");
                   // out.write(desc);
                   //out.write("</td>\n");
                }
                
            }
            
            
            
            if(currUser!=null)
           out.print("</ul><div class='MinhNVAccount'><table><tr><td align='left'>Account:</td><td align='left'>"+currAcct+"</td></tr><tr><td align='center' colspan='2'>("+currUser+")</td></tr></table></div></div>");
            else
            	 out.print("</ul><div class='MinhNVAccount'><table><tr><td align='left'>Account:</td><td align='left'>"+currAcct+"</td></tr><tr><td align='center' colspan='2'>(Admin)</td></tr></table></div></div>");	
            /* out.print(" <div class='menuMinh' id='menuMinhNV1'>"+
            		"<a href='./Track?page=login'><div class='btnExit'><table width='100%' height='100%'><tr><td align='center'><img src='images/menu/exit.png'  alt=''  /></td></tr><tr><td align='center' style='color:#002b6c'>Tho&#x00E1;t</td></tr></table></div></a>"+
            		//"<div class='btnShrink' onclick='javascript:Shrink();' ><table width='100%' height='100%'><tr><td align='center'><img src='images/menu/Shrink.png'  alt=''/></td></tr><tr><td align='center'>Shrink</td></tr></table></div>"+
            		//"<div class='btnPol'><table width='100%' height='100%'><tr><td align='center'><img src='images/menu/pol.png'  alt='' /></td></tr><tr><td align='center'>POL</td></tr></table></div>"+
            		"<div class='dropmenuMinh'>");
            
           
           
            
            
           out.print( " <div class=\"btnMain\" onmouseover=\"hideMenu();\">"+
            " <a href='./Track'><table><tr><td><img src=\"images/menu/main.png\" style='margin-top:-17' alt=\"\" width='20px' height='18px'/></td><td>"+
            "Trang ch&#x1EE7;"+"&nbsp;&nbsp; &nbsp; &nbsp;</td><td style=\"width:10px;\"></td></tr></table></a></div><img alt='' src='images/menu/thanhDung.png' style='margin-left:115px'>"); 
            
           out.print( " <div class=\"btnBanDo\" onmouseover=\"showMenu('banDo');\">"+
                   " <table><tr><td><img src=\"images/menu/map.png\" alt=\"\" width='20px' height='18px'/></td><td>"+
                   buttonValue[1]+"&nbsp;&nbsp;</td><td style=\"width:10px;\"><img src=\"images/menu/down_arrow.gif\" alt=\"\" width=\"9\"/></td></tr></table></div><img alt='' src='images/menu/thanhDung.png' style='margin-left:250px; margin-top:-70px'>"); 
               out.print("<div class='thanhNgang'></div>");     
           out.print( " <div class=\"btnBaoCao\" onmouseover=\"showMenu('baoCao');\">"+
                   " <table><tr><td><img src=\"images/menu/report.png\" alt=\"\" width='20px' height='18px'/></td><td>"+
                   buttonValue[2]+"&nbsp;</td><td style=\"width:10px;\"><img src=\"images/menu/down_arrow.gif\" alt=\"\" width=\"9\"/></td></tr></table></div><img alt='' src='images/menu/thanhDung.png' style='margin-left:380px; margin-top:-50px'>"); 
                   
           out.print( " <div class=\"btnQuanLy\" onmouseover=\"showMenu('quanLy');\">"+
                   " <table><tr><td><img src=\"images/menu/setting.png\" alt=\"\" width='20px' height='18px'/></td><td>"+
                   buttonValue[3]+"&nbsp;</td><td style=\"width:10px;\"><img src=\"images/menu/down_arrow.gif\" alt=\"\" width=\"9\"/></td></tr></table></div></div>"); 
                   
           
           out.print (  "<div id='banDo' class='banDoMinhNV'>"
                   
           +"<ul onclick='hideMenu();'>"
         +"<li><table><tr><td><img src='./images/menu/iDeviceMap.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=map.device.last'>Last localtion Map</a></td></tr></table><img src='images/menu/thanhNgang.png'/></li>"
         +"<li><table><tr><td><img src='./images/menu/iDeviceMap.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=map.device'>B&#x1EA3;n &#x0111;&#x1ED3; xe</a></td></tr></table><img src='images/menu/thanhNgang.png'/></li>"
          +"<li><table><tr><td><img src='./images/menu/iFleetMap.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=map.fleet'>B&#x1EA3;n &#x0111;&#x1ED3; &#x0111;&#x1ED9;i xe</a></td></tr></table></li>"
      +" </ul></div>"

+"<div id='baoCao' class='baoCaoMinhNV'>"
            
+"<ul onclick='hideMenu();'>"
           +" <li><table><tr><td><img src='./images/menu/iDeviceDetailReport.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=menu.rpt.devDetail'>Ho&#x1EA1;t &#x0111;&#x1ED9;ng c&#x1EE7;a xe</a></td></tr></table><img src='images/menu/thanhNgang.png'/></li>"
         +"<li> <table><tr><td><img src='./images/menu/iFleetDetailReport.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp;<a href='./Track?page=menu.rpt.grpDetail'>Chi ti&#x1EBF;t &#x0111;&#x1ED9;i xe</a></td></tr></table><img src='images/menu/thanhNgang.png'/></li>"
       +"<li><table><tr><td><img src='./images/menu/iFleetDetailReport.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=menu.rpt.grpSummary'>T&#x1ED5;ng h&#x1EE3;p &#x0111;&#x1ED9;i xe</a></td></tr></table><img src='images/menu/thanhNgang.png'/></li>"
         +" <li><table><tr><td><img src='./images/menu/iPerformanceReport.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=menu.rpt.devPerf'>Tr&#x1EA1;ng th&#x00E1;i xe</a></td></tr></table><img src='images/menu/thanhNgang.png'/></li> "  
         +" <li><table><tr><td><img src='./images/menu/baoCaoTram.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=menu.rpt.Report'>B&#x00E1;o c&#x00E1;o tuy&#x1EBF;n</a></td></tr></table><img src='images/menu/thanhNgang.png'/></li> "   
+" <li><table><tr><td><img src='./images/menu/baoCaoTram.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=menu.rpt.baoCaoTram'>B&#x00E1;o c&#x00E1;o tr&#x1EA1;m</a></td></tr></table><img src='images/menu/thanhNgang.png'/></li> "  

+" <li><table><tr><td><img src='./images/menu/baoCaoTram.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=menu.rpt.reportZoneByDay'>B&#x00E1;o c&#x00E1;o tr&#x1EA1;m theo ng&#x00E0;y</a></td></tr></table></li> "  

         
         +"</ul>"
            
+"</div><div id='quanLy' class='quanLyMinhNV'>"
            
+"<ul onclick='hideMenu();'>"
         +"<li><table><tr><td><img src='./images/menu/iAccountAdmin.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=acct.info'>Qu&#x1EA3;n l&#x00FD; Account</a></td></tr></table><img src='images/menu/thanhNgang.png'/></li>"
         +"<li><table><tr><td><img src='./images/menu/iUserAdmin.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=user.info'>Qu&#x1EA3;n l&#x00FD; User</a></td></tr></table><img src='images/menu/thanhNgang.png'/></li>"
         +" <li><table><tr><td><img src='./images/menu/iDeviceAdmin.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=dev.info'>Qu&#x1EA3;n l&#x00FD; xe</a></td></tr></table><img src='images/menu/thanhNgang.png'/></li>"
         +"<li><table><tr><td><img src='./images/menu/iFleetAdmin.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=group.info'>Qu&#x1EA3;n l&#x00FD; &#x0111;&#x1ED9;i xe</a></td></tr></table><img src='images/menu/thanhNgang.png'/></li>"
         +"<li><table><tr><td><img src='./images/menu/iZoneAdmin.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=zone.info'>Qu&#x1EA3;n l&#x00FD; v&#x00F9;ng</a></td></tr></table><img src='images/menu/thanhNgang.png'/></li>"
        +" <li><table><tr><td><img src='./images/menu/iCorridorAdmin.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=corridor.info'>GeoCorridor Admin</a></td></tr></table><img src='images/menu/thanhNgang.png'/></li>"
         +" <li><table><tr><td><img src='./images/menu/iRuleAdmin.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=rule.info'>Rule Admin</a></td></tr></table><img src='images/menu/thanhNgang.png'/></li>"
         +"<li><table><tr><td><img src='./images/menu/iStatusCodeAdmin.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=code.info'>StatusCode Admin</a></td></tr></table><img src='images/menu/thanhNgang.png'/></li>"
         +"<li><table><tr><td><img src='./images/menu/iChangePassword.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=passwd'>Thay &#x0111;&#x1ED5;i m&#x1EAD;t kh&#x1EA9;u</a></td></tr></table><img src='images/menu/thanhNgang.png'/></li>"
           +"<li><table><tr><td><img src='./images/menu/job.png'  alt='' width='18px' height='18px'  /></td><td>&nbsp;&nbsp; <a href='./Track?page=menu.jobsManager'>Jobs Manager</a></td></tr></table></li></ul></div>");

         //  out.print("<div id='main' class='mainMinhNV'>Trang ch&#x1EE7;</a></div>");
           
         
           out.print ("<div id='main' class='mainMinhNV'>"
                   +"<div style='float:left;'>"
                   +"<ul onclick='hideMenu();'>"
                   +"<li><table><tr><td><img src='images/menu/Home.png'  alt='' /></td><td>&nbsp;&nbsp;<a href='./Track'>Trang ch&#x1EE7;</a></td></tr></table> </li>"+
                   "<li> <img src='images/menu/exit.png'  alt='' />&nbsp;&nbsp;&nbsp;<a href='./Track?page=login'>Tho&#x00E1;t</a></li></ul></div>");
           
                   
       
            


            
            
            out.print( " </div>");*/
            
            
            
            
        
        } else {

            /* explicitly add main menu */
            Map pageMap = privLabel.getWebPageMap();
            WebPage mainMenu = (WebPage)pageMap.get(Track.PAGE_MENU_TOP);
            if (mainMenu != null) {
                String desc = mainMenu.getNavigationTab(reqState);
                if ((pageName == null) || !pageName.equals(Track.PAGE_MENU_TOP)) {
                    //String url = WebPageAdaptor.EncodeMakeURL(reqState,RequestProperties.TRACK_BASE_URI(),Track.PAGE_MENU_TOP);
                    String url = mainMenu.encodePageURL(reqState);
                  //  out.write(" <td class='"+CSS_MENU_TAB_UNSEL+"' onclick=\"javascript:window.open('"+url+"','_top')\">"+desc+"</td>\n");
                } else {
                  //  out.write(" <td class='"+CSS_MENU_TAB_SEL+"'>"+desc+"</td>\n");
                }
            }
        
            /* add all other menu items (except logout) */
            Map<String,MenuGroup> menuMap = privLabel.getMenuGroupMap();
            for (String mgn : menuMap.keySet()) {
                MenuGroup mg = menuMap.get(mgn);
                if (mg.showInMenuBar()) {
                    java.util.List<WebPage> menuItems = mg.getWebPageList(reqState);
                    for (WebPage wp : menuItems) {
                        String wpname = wp.getPageName();

                        // skip these pages if they show up
                        if (wpname.equals(Track.PAGE_LOGIN)   ) { continue; }
                        if (wpname.equals(Track.PAGE_MENU_TOP)) { continue; }
                        if (wpname.equals(Track.PAGE_PASSWD)  ) { continue; }

                        // add menu bar tab
                        String desc = wp.getNavigationTab(reqState);
                        if ((pageName == null) || !pageName.equals(wpname)) {
                            String url = wp.encodePageURL(reqState);
                          //  out.write(" <td class='"+CSS_MENU_TAB_UNSEL+"' onclick=\"javascript:window.open('"+url+"','_top')\">"+desc+"</td>\n");
                        } else {
                           // out.write(" <td class='"+CSS_MENU_TAB_SEL+"'>"+desc+"</td>\n");
                        }

                    }
                }
            }
            
        }
        
     
        out.write("</td>\n");
        out.write("<!-- End Menu Bar -->\n");
    }

    // ------------------------------------------------------------------------

}
