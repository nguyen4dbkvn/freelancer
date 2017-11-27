
package org.opengts.war.track.page;

import java.sql.ResultSet;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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


public class JobDetails
    extends WebPageAdaptor
    implements Constants
{
    
    // ------------------------------------------------------------------------
    // Parameters

    // commands
    public  static final String COMMAND_INFO_UPDATE     = "update";

    // submit types
    public  static final String PARM_SUBMIT_CHANGE      = "a_subchg";

    // button types
    public  static final String PARM_BUTTON_CANCEL      = "a_btncan";
    public  static final String PARM_BUTTON_BACK        = "a_btnbak";

    // ------------------------------------------------------------------------
    // WebPage interface
    
    public JobDetails()
    {
        this.setBaseURI(RequestProperties.TRACK_BASE_URI());
        this.setPageName(PAGE_JOBDETAILS);
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
        return super._getMenuDescription(reqState,i18n.getString("AccountInfo.editMenuDesc","Picture of Device"));
    }
   
    public String getMenuHelp(RequestProperties reqState, String parentMenuName)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(Camera.class);
        return super._getMenuHelp(reqState,i18n.getString("AccountInfo.editMenuHelp","Picture of Device"));
    }

    // ------------------------------------------------------------------------

    public String getNavigationDescription(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(Camera.class);
        return super._getNavigationDescription(reqState,i18n.getString("AccountInfo.navDesc","Account"));
    }

    public String getNavigationTab(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(Camera.class);
        return i18n.getString("AccountInfo.navTab","Camera");
    }
    // ------------------------------------------------------------------------
    public void writePage(
        final RequestProperties reqState,
        String pageMsg)
        throws IOException
    {
        HttpServletRequest request = reqState.getHttpServletRequest();
    	final PrivateLabel privLabel = reqState.getPrivateLabel();
        final I18N    i18n     = privLabel.getI18N(Camera.class);
        final Locale  locale   = reqState.getLocale();
        final Account currAcct = reqState.getCurrentAccount();
        final User    currUser = reqState.getCurrentUser();
        final String  pageName = this.getPageName();
        String m = pageMsg;
        boolean error = false;
        String cameraCmd = reqState.getCommandName();
        boolean update = cameraCmd.equals(COMMAND_INFO_UPDATE);
        final String date = AttributeTools.getRequestString(request, "txtdate", "");
        final String timeh= AttributeTools.getRequestString(request, "Slgio", "");
        final String timep= AttributeTools.getRequestString(request, "Slphut", "");
        final String time= timeh+":"+timep+":"+":00";
        final String seta =AttributeTools.getRequestString(request, "txtEta", "");
        //final int eta = Integer.parseInt(seta);
        final String sertype =AttributeTools.getRequestString(request, "Slsertype", "");
        final  String po =AttributeTools.getRequestString(request, "txtnumber", "");
        final String cname =AttributeTools.getRequestString(request, "txtCusname", "");
        final String cphone =AttributeTools.getRequestString(request, "txtcusphone", "");
        final String sequi =AttributeTools.getRequestString(request, "txtequi", "");
       // final  int equi = Integer.parseInt(sequi);
        final  String  com=AttributeTools.getRequestString(request, "TextArea1", "");
        final String  vehic=AttributeTools.getRequestString(request, "txtvehicle", "");
        final String  syear=AttributeTools.getRequestString(request, "txtyear", "");
        //final int year = Integer.parseInt(syear);
        final String  make=AttributeTools.getRequestString(request, "txtmake", "");
        final String  model=AttributeTools.getRequestString(request, "txtmodel", "");
        final String  type=AttributeTools.getRequestString(request, "txttype", "");
        final String  vin=AttributeTools.getRequestString(request, "txtvin", "");
        final String  color=AttributeTools.getRequestString(request, "txtcolour", "");
        final String  state=AttributeTools.getRequestString(request, "txtstate", "");
        final String  feultype=AttributeTools.getRequestString(request, "txtfeultype", "");
        final String  callre=AttributeTools.getRequestString(request, "txtcallre", "");
        final String  reason=AttributeTools.getRequestString(request, "txtreason", "");
        final String des =AttributeTools.getRequestString(request, "txtdes", "");
        String stringID="13";
        //int ID;
         if(request.getParameter("autoID")!=null)
        {
            stringID = request.getParameter("autoID");
        }
        if(update)
       {
        	 //try
             //{
             DBCamera objcmr = new DBCamera();
        	 // objcmr.Update_Job(date, time,seta, type, po, cname, cphone, sequi, vehic, syear, make, model, type, color, vin, state, feultype, callre, des, "15", com,reason);
              objcmr.job_update(stringID,date,time,seta,sertype,po,cname,cphone,sequi,vehic,syear, make, model,type,color,vin,state,feultype,callre,des,com,des);
        	  // }
        	 //catch(Exception ex){}
       }
        	 
        /* Style */
        HTMLOutput HTML_CSS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                String cssDir = JobDetails.this.getCssDirectory();
                WebPageAdaptor.writeCssLink(out, reqState, "Lich.css", cssDir);
                WebPageAdaptor.writeCssLink(out, reqState, "lightbox.css", cssDir);
            }
        };
        /* JavaScript */
        	HTMLOutput HTML_JS = new HTMLOutput() {
           public void write(PrintWriter out) throws IOException {
               MenuBar.writeJavaScript(out, pageName, reqState);
               HttpServletRequest request = reqState.getHttpServletRequest();
               JavaScriptTools.writeJSInclude(out, JavaScriptTools.qualifyJSFileRef("PopCalendar.js"), request);
           }
        };
        /* Content */
            HTMLOutput HTML_CONTENT  = new HTMLOutput(CommonServlet.CSS_CONTENT_FRAME, m) {
            public void write(PrintWriter out) throws IOException {
            	 HttpServletRequest request = reqState.getHttpServletRequest();
                //Print.logStackTrace("here");
              //String menuURL = EncodeMakeURL(reqState,RequestProperties.TRACK_BASE_URI(),PAGE_MENU_TOP);
                String menuURL = privLabel.getWebPageURL(reqState, PAGE_MENU_TOP);
              //String chgURL  = EncodeMakeURL(reqState,RequestProperties.TRACK_BASE_URI(),pageName,COMMAND_INFO_UPDATE);
                String chgURL  = privLabel.getWebPageURL(reqState, pageName, COMMAND_INFO_UPDATE);
                String      devTitles[]      = currAcct.getDeviceTitles(locale, new String[]{"",""});
                String      grpTitles[]      = currAcct.getDeviceGroupTitles(locale, new String[]{"",""});
                String      adrTitles[]      = currAcct.getAddressTitles(locale, new String[]{"",""});
                
                //long time = Time.parse(t);
               String stringID="13";
               //int ID;
                if(request.getParameter("autoID")!=null)
               {
                   stringID = request.getParameter("autoID");
               }
               // ID  = Integer.parseInt(13);
                out.println("<form name='Job Details' method='post' action='"+chgURL+"' target='_self'>");
                out.print("<center>");
                try
                {
                DBCamera objcmr = new DBCamera();
        		//ResultSet rs = objcmr.getDEV_Jobdetail(13);
        		ResultSet rs = objcmr.getDEV_Jobdetail(Integer.parseInt(stringID));
        		ResultSet rs1 = objcmr.GetType();
        		 while(rs.next())
        		 {
                out.println(" <table class='table'>");
                out.println( "<tr class='tr'><td colspan='2' class='tdheader'>&nbsp;Job Details</td></tr>");
                out.println( " <tr class='tr'> <td class='tdleft' style='height: 38px'>&nbsp;Dispatch Date: </td>");
                out.println( "<td class='tdright' style='height: 38px'> <input id='txtdate' name='txtdate' type='text' class='textbox' style='height: 25px; width: 225px;' value='"+rs.getString("DispatchDate")+"'/>");
                out.println( "&nbsp; &nbsp;Time: ");
                out.println( "&nbsp; &nbsp;<select id='Slgio' name='Slgio' style='width: 49px;height: 22px' class='textbox'>");
                for(int i =0;i<25;i++)
                {
                	if(i<10)
                	{
                      out.print("<option>0"+i+"</option>");
                	}
                	else
                	{
                		out.print("<option>"+i+"</option>");
                	}
                  //out.println("<option>"+i+"</option>");
                }
                out.println("</select>");
               
                out.println( "&nbsp; &nbsp;:&nbsp; &nbsp;<select id='Slphut' name='Slphut' style='width: 49px;height: 22px' class='textbox'>");
                for(int j=0;j<61;j++)
                {
                	if(j<10)
                	{
                  out.print("<option>0"+j+"</option>");
                	}
                	else
                	{
                		out.print("<option>"+j+"</option>");
                	}
                }
                out.println("</select>");
              
                //out.println("<script type ='text/javascript' language ='javascript'> document.getElementById('txtvehicle').value = '"+rs.getString("ArrivalTime")+"';</script>\n");
                out.println( "</td> </tr>");
                out.println( " <tr class='tr'>");
                out.println( "<td class='tdleft'>&nbsp;ETA: </td>");
                out.println( "<td class='tdright'><input id='txtEta' name='txtEta' size='18' type='text' class='textbox' style='height: 22px' value='"+rs.getString("DispatchETA")+"'/>&nbsp;&nbsp;minutes</td>");
                out.println( "</tr>  <tr class='tr'>");
                out.println( " <td class='tdleft'>&nbsp;Service Type:</td>");
                out.println( " <td class='tdright'><select id='Slsertype' name='Slsertype' style='width: 142px;height: 22px' class='textbox'>");
                while(rs1.next())
                {
                 out.println(" <option>"+rs1.getString("TypeName")+"</option>");
                }
                out.println("</select>");
                out.println("<script type ='text/javascript' language ='javascript'> document.getElementById('Slsertype').value = '"+rs.getString("ServiceType")+"';</script>\n");
                out.println( "</td> </tr>");
                out.println( "<tr class='tr'>");
                out.println( "<td class='tdleft'>&nbsp;Purchace Order Number:</td>");
                out.println( "<td class='tdright'>");
                out.println( " <input id='txtnumber' name='txtnumber' size='60' class='textbox' type='text' style='height: 22px' value='"+rs.getString("PO")+"'/>");
                out.println( "</td></tr>");
                out.println( " <tr class='tr'>");
                out.println( "<td class='tdleft'>&nbsp;Customer Name:</td>");
                out.println( "<td class='tdright'><input id='txtCusname' name='txtCusname' size='60' type='text' class='textbox' style='height: 22px' value='"+rs.getString("CustomerName")+"'/></td>");
                out.println( " </tr>");
                out.println( "<tr class='tr'>");
                out.println( " <td class='tdleft'> &nbsp;Customer Phone: </td>");
                out.println( " <td class='tdright'><input id='txtcusphone' name='txtcusphone' size='60' type='text' class='textbox' style='height: 22px' value='"+rs.getString("CallbackNumber")+"'/></td>");
                out.println( "</tr>");
                out.println( "<tr class='tr'>");
                out.println( " <td class='tdleft'> &nbsp;Equipment: </td>");
                out.println( "<td class='tdright'><input id='txtequi' name='txtequi' size='60' type='text' style='height: 22px' class='textbox' value='"+rs.getString("Equipment")+"'/></td>");
                out.println( " </tr>");
                out.println( "<tr class='tr'>");
                out.println( " <td class='tdleft'>&nbsp;Comments:</td>");
                out.println( " <td class='tdright'>");
              
                out.println( "<textarea id='TextArea1' name='TextArea1' cols='50' rows='5' style='width: 510px;height:100px;' class='textbox' value='"+rs.getString(7)+"' >"+rs.getString(7)+"</textarea>");   
                out.println( "</td>");
                out.println( " </tr>");
                out.println("<tr><td class='tdleft1'> Vehicle Registration:</td>");
                out.println("<td class='tdright1'><input id='txtvehicle' name='txtvehicle' type='text' class='textbox1' value='"+rs.getString("VehicleRigistration")+"'/> </td>");
                out.println("  </tr>   <tr>");
                out.println("<td class='tdleft1'>Year: </td>");
                out.println(" <td class='tdright1'><input id='txtyear' name='txtyear' type='text' class='textbox1'  value='"+rs.getString("VehicleYear")+"' /> </td>");
                out.println(" </tr><tr>");
                out.println("<td class='tdleft1'>Make:</td>");
                out.println(" <td class='tdright1'><input id='txtmake' name='txtmake' type='text' class='textbox1' value='"+rs.getString("VehicleMake")+"'/></td>");
                out.println("  </tr><tr>");
                out.println("<td class='tdleft1' style='height: 19px'>Model:</td>");
                out.println(" <td class='tdright1' style='height: 19px'><input id='txtmodel' name='txtmodel' type='text' class='textbox1'  value='"+rs.getString("VehicleModel")+"' /></td>");
                out.println(" </tr> <tr>");
                out.println(" <td class='tdleft1'>Type:</td>");
                out.println("<td class='tdright1'><input id='txttype' name='txttype' type='text' class='textbox1'  value='"+rs.getString("VehicleType")+"'/></td>");
                out.println("</tr><tr>");
                out.println(" <td class='tdleft1'>VIN:</td>");
                out.println("<td class='tdright1'><input id='txtvin' name='txtvin' type='text' class='textbox1'  value='"+rs.getString("VehicleVIN")+"'/></td>");
                out.println("</tr><tr>");
                out.println("  <td class='tdleft1'>Colour:</td>");
                out.println("<td class='tdright1'><input id='txtcolour'  name='txtcolour' type='text' class='textbox1'  value='"+rs.getString("VehicleColor")+"'/></td>");
                out.println(" </tr><tr>");
                out.println(" <td class='tdleft1'>State of Issuance:</td>");
                out.println(" <td class='tdright1'><input id='txtstate' name='txtstate' type='text' class='textbox1'  value='"+rs.getString("StateOfIssuance")+"'/></td>");
                out.println(" </tr><tr>");
                out.println("<td class='tdleft1'>Fuel Type: </td>");
                out.println("<td class='tdright1'><input id='txtfeultype' name='txtfeultype' type='text' class='textbox1'  value='"+rs.getString("FuelType")+"'/></td>");
                out.println(" </tr><tr>");
                out.println(" <td class='tdleft1'>Call Reason: </td>");
                out.println(" <td class='tdright1'><input id='txtcallre' name='txtcallre' type='text' class='textbox1'  value='"+rs.getString("CallReason")+"'/></td>");
                out.println(" </tr><tr>");
                out.println(" <td class='tdleft1'>Reason For Disablement: </td>");
                out.println(" <td class='tdright1'><input id='txtreason' name='txtreason' type='text' class='textbox1'  value='"+rs.getString("Disablement")+"'/></td>");
                out.println(" </tr><tr>");
                out.println(" <td class='tdleft1'>Problem Desciption: </td>");
                out.println("<td class='tdright1'><input id='txtdes' name='txtdes' type='text' class='textbox1'  value='"+rs.getString("Problem")+"'/></td>");
                out.println(" </tr><tr>");
                out.println("<td class='tdleft1'>Cutomer Present?</td>");
                out.println("<td class='tdright1'><input id='txtpre' name='txtpre' type='text' class='textbox1' /></td>");
                out.println(" </tr><tr>");
                out.println("<td class='tdleft1'>Notes:</td>");
                out.println(" <td class='tdright1'><input id='txtnote' name='txtnote' type='text' class='textbox1' /></td>");
                out.println("</tr><tr>");
                out.println("<td></td><td style='text-align:right;margin-top:10px;'> <input type ='submit' id ='btnupdate' value ='Update' name ='btnupdate'/></td></tr>");
              
                out.println( "</table>");
                out.print("</center>");
                out.println("<script type ='text/javascript' language ='javascript'> document.getElementById('Slgio').value = '"+rs.getString("ArrivalTime").substring(0,2)+"';document.getElementById('Slphut').value = '"+rs.getString("ArrivalTime").substring(3,5)+"';" );
                out.println("</script>\n");
        	 }
                //out.println("<script type ='text/javascript' language ='javascript'> document.getElementById('Slphut').value = '"+rs.getString("ArrivalTime").substring(3,2)+"';</script>\n");
               // out.print("<img src='http://anh.24h.com.vn:8008/upload/1-2011/images/2011-01-29/1296301943-lam-tam-nhu--4-.jpg' width='200' height='200' />");           
                out.write("</form>\n");
           
                }catch(Exception ex)
                {}
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
