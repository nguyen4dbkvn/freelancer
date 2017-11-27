package org.opengts.war.track.page;

import java.sql.ResultSet;
import java.util.*;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.opengts.util.*;
import org.opengts.dbtools.*;
import org.opengts.db.*;
import org.opengts.db.AclEntry.AccessLevel;
import org.opengts.db.tables.*;

import org.opengts.war.tools.*;
import org.opengts.war.track.*;
import org.opengts.war.maps.JSMap;

import org.opengts.war.track.page.devcmd.*;

public class DeviceInfo
    extends WebPageAdaptor
    implements Constants
{

    
    // ------------------------------------------------------------------------

    private static final boolean EDIT_SERVER_ID             = false;
    private static final boolean SHOW_LAST_CONNECT          = false;
    private static final boolean SHOW_NOTES                 = false;
    private static final boolean SHOW_DATA_KEY              = false;
    private static final boolean SHOW_PUSHPIN_ID            = true;
    private static final boolean SHOW_IGNITION_NDX          = true;
    private static final boolean SHOW_DISPLAY_COLOR         = true;
    private static final boolean SHOW_SPEED_LIMIT           = false;
    private static final boolean SHOW_MAINTENANCE_NOTES     = true;  // still requires maintenance support
    private static final boolean SHOW_FIXED_LOCATION        = false;
    private static final boolean SHOW_NOTIFY_SELECTOR       = false; // only if RuleFactory defined
    private static final boolean OPTIMIZE_IGNITION_STATE    = false;

    // ------------------------------------------------------------------------

    public  static final String _ACL_SERVERID               = "serverID";
    public  static final String _ACL_FIRMWARE               = "firmware";
    public  static final String _ACL_UNIQUEID               = "uniqueID";
    public  static final String _ACL_RULES                  = "rules";
    public  static final String _ACL_COMMANDS               = "commands";
    public  static final String _ACL_SMS                    = "sms";
    public  static final String _ACL_ACTIVE                 = "active";
    public  static final String _ACL_EDIT_SMS               = "editSMS";
    public  static final String _ACL_EDIT_SIM               = "editSIM";
    public  static final String _ACL_EDIT_IMEI              = "editIMEI";
    public  static final String _ACL_EDIT_SERIAL            = "editSerial";
    public  static final String _ACL_EDIT_DATKEY            = "editDatKey";
    public  static final String _ACL_EDIT_CUA               = "editCua";
    public  static final String _ACL_EDIT_DIEUHOA           = "editDieuHoa";
    public  static final String _ACL_EDIT_ND91              = "editND91";
    public  static final String _ACL_EDIT_XANG              = "editXang";
    private static final String _ACL_LIST[]                 = new String[] { 
        _ACL_RULES, 
        _ACL_UNIQUEID, 
        _ACL_ACTIVE,
        _ACL_COMMANDS, 
        _ACL_SMS,
        _ACL_EDIT_SMS, 
        _ACL_EDIT_SIM,
        _ACL_EDIT_IMEI, 
        _ACL_EDIT_SERIAL,
        _ACL_EDIT_DATKEY,
        
    };

    // ------------------------------------------------------------------------
    // allow new device modes

    public  static final int    NEWDEV_DENY                 = 0;
    public  static final int    NEWDEV_ALLOW                = 1;
    public  static final int    NEWDEV_SYSADMIN             = 2;

    // ------------------------------------------------------------------------
    // Parameters

    // forms
    public  static final String FORM_DEVICE_SELECT          = "DeviceInfoSelect";
    public  static final String FORM_DEVICE_EDIT            = "DeviceInfoEdit";
    public  static final String FORM_DEVICE_NEW             = "DeviceInfoNew";

    // commands
    public  static final String COMMAND_INFO_UPD_DEVICE     = "updateDev";
    public  static final String COMMAND_INFO_UPD_PROPS      = "updateProps"; // see DeviceCmdHandler
    public  static final String COMMAND_INFO_UPD_SMS        = "updateSms";   // see DeviceCmd_SMS
  //public  static final String COMMAND_INFO_REFRESH        = "refreshList";
    public  static final String COMMAND_INFO_SEL_DEVICE     = "selectDev";
    public  static final String COMMAND_INFO_NEW_DEVICE     = "newDev";

    // submit
    public  static final String PARM_SUBMIT_EDIT            = "d_subedit";
    public  static final String PARM_SUBMIT_VIEW            = "d_subview";
    public  static final String PARM_SUBMIT_CHG             = "d_subchg";
    public  static final String PARM_SUBMIT_DEL             = "d_subdel";
    public  static final String PARM_SUBMIT_NEW             = "d_subnew";
    public  static final String PARM_SUBMIT_QUE             = "d_subque";
    public  static final String PARM_SUBMIT_PROP            = "d_subprop";
    public  static final String PARM_SUBMIT_SMS             = "d_subsms";

    // buttons
    public  static final String PARM_BUTTON_CANCEL          = "d_btncan";
    public  static final String PARM_BUTTON_BACK            = "d_btnbak";

    // device table fields
    public  static final String PARM_NEW_NAME               = "d_newname";
    public  static final String PARM_CREATE_DATE            = "d_creation";
    public  static final String PARM_SERVER_ID              = "d_servid";
    public  static final String PARM_CODE_VERS              = "d_codevers";
    public  static final String PARM_DEV_UNIQ               = "d_uniq";
    public  static final String PARM_DEV_DESC               = "d_desc";
    public  static final String PARM_DEV_NAME               = "d_name";
    public  static final String PARM_VEHICLE_ID             = "d_vehicid";
    public  static final String PARM_LICENSE_PLATE          = "d_licPlate";
    public  static final String PARM_DEV_EQUIP_TYPE         = "d_equipt";
    public  static final String PARM_DEV_FUEL_CAP           = "d_fuelcap";
    public  static final String PARM_DEV_FUEL_ECON          = "d_fuelecon";
    public  static final String PARM_DEV_SPEED_LIMIT        = "d_speedLim";
    public  static final String PARM_DEV_IMEI               = "d_imei";
    public  static final String PARM_DEV_SERIAL_NO          = "d_sernum";
    public  static final String PARM_DATA_KEY               = "d_datakey";
    public  static final String PARM_DEV_SIMPHONE           = "d_simph";
    public  static final String PARM_SMS_EMAIL              = "d_smsemail";
    public  static final String PARM_ICON_ID                = "d_iconid";
    public  static final String PARM_DISPLAY_COLOR          = "d_dcolor";
    public  static final String PARM_DEV_ACTIVE             = "d_actv";
    public  static final String PARM_DEV_SERIAL             = "d_ser";
    public  static final String PARM_DEV_LAST_CONNECT       = "d_lconn";
    public  static final String PARM_DEV_LAST_EVENT         = "d_levnt";
    public  static final String PARM_DEV_NOTES              = "d_notes";
    public  static final String PARM_LINK_URL               = "d_linkurl";
    public  static final String PARM_LINK_DESC              = "d_linkdesc";
    public  static final String PARM_FIXED_LAT              = "d_fixlat";
    public  static final String PARM_FIXED_LON              = "d_fixlon";
    public  static final String PARM_IGNITION_INDEX         = "d_ignndx";
    public  static final String PARM_DRIVER_ID              = "d_driver";
    public  static final String PARM_DEV_GROUP_             = "d_grp_";

    public  static final String PARM_REPORT_ODOM            = "d_rptodom";
    public  static final String PARM_REPORT_HOURS           = "d_rpthours";
    public  static final String PARM_MAINT_INTERVKM_        = "d_mntintrkm";
    public  static final String PARM_MAINT_LASTKM_          = "d_mntlastkm";    // read-only
    public  static final String PARM_MAINT_NEXTKM_          = "d_mntnextkm";    // read-only
    public  static final String PARM_MAINT_RESETKM_         = "d_mntreskm";      // checkbox
    public  static final String PARM_MAINT_INTERVKM_0       = PARM_MAINT_INTERVKM_+"0";
    public  static final String PARM_MAINT_LASTKM_0         = PARM_MAINT_LASTKM_+"0";
    public  static final String PARM_MAINT_NEXTKM_0         = PARM_MAINT_NEXTKM_+"0";
    public  static final String PARM_MAINT_RESETKM_0        = PARM_MAINT_RESETKM_+"0";
    public  static final String PARM_MAINT_INTERVKM_1       = PARM_MAINT_INTERVKM_+"1";
    public  static final String PARM_MAINT_LASTKM_1         = PARM_MAINT_LASTKM_+"1";
    public  static final String PARM_MAINT_NEXTKM_1         = PARM_MAINT_NEXTKM_+"1";
    public  static final String PARM_MAINT_RESETKM_1        = PARM_MAINT_RESETKM_+"1";
    public  static final String PARM_MAINT_INTERVHR_        = "d_mntintrhr";
    public  static final String PARM_MAINT_LASTHR_          = "d_mntlasthr";    // read-only
    public  static final String PARM_MAINT_RESETHR_         = "d_mntreshr";      // checkbox
    public  static final String PARM_MAINT_INTERVHR_0       = PARM_MAINT_INTERVHR_+"0";
    public  static final String PARM_MAINT_LASTHR_0         = PARM_MAINT_LASTHR_+"0";
    public  static final String PARM_MAINT_RESETHR_0        = PARM_MAINT_RESETHR_+"0";
    public  static final String PARM_MAINT_NOTES            = "d_mntnotes";

    public  static final String PARM_DEV_RULE_ALLOW         = "d_ruleallw";
    public  static final String PARM_DEV_RULE_EMAIL         = "d_rulemail";
    public  static final String PARM_DEV_RULE_SEL           = "d_rulesel";
    public  static final String PARM_DEV_RULE_DESC          = "d_ruledesc";
    public  static final String PARM_DEV_RULE_SUBJ          = "d_rulesubj";
    public  static final String PARM_DEV_RULE_TEXT          = "d_ruletext";
  //public  static final String PARM_DEV_RULE_WRAP          = "d_rulewrap";
    public  static final String PARM_LAST_ALERT_TIME        = "d_alertTime";
    public  static final String PARM_LAST_ALERT_RESET       = "d_alertReset";
    public  static final String PARM_ACTIVE_CORRIDOR        = "d_actvcorr";

  //public  static final String PARM_WORKORDER_ID           = "d_workid";

    public  static final String PARM_DEV_CUSTOM_            = "d_c_";

    // ------------------------------------------------------------------------
    // Device command handlers

    private static Map<String,DeviceCmdHandler> DCMap = new HashMap<String,DeviceCmdHandler>();
    private static DeviceCmd_SMS                SMSCommandHandler = null;

    static {
        _initDeviceCommandHandlers();
    };

    private static void _addDeviceCommandHandler(DeviceCmdHandler dch)
    {
        String servID = dch.getServerID();
        if (!DCMap.containsKey(servID)) {
            Print.logDebug("Installing DeviceCmdHandler: %s", servID);
            DCMap.put(servID, dch);
        } else {
            Print.logStackTrace("Duplicate ServerID found: " + servID + " ["+StringTools.className(dch)+"]");
        }
    }

    private static void _initDeviceCommandHandlers()
    {

        /* list of possible DeviceCmdHandler UI pages */
        // NOTE: Not all of these DeviceCmd_<DCS> pages may be available.
        String optDevCmdHandlerClasses[] = new String[] {
            "DeviceCmd_antares",
            "DeviceCmd_calamp",     // "DeviceCmd_calamp:1,2,3"
            "DeviceCmd_eloc",
            "DeviceCmd_citgt06",
            "DeviceCmd_gosafe",
            "DeviceCmd_qgl200",
            "DeviceCmd_qgv100",
            "DeviceCmd_qgv200",
            "DeviceCmd_qgv300",
            "DeviceCmd_enfora",
            "DeviceCmd_gtsdmtp",    // always available
            "DeviceCmd_pgt3000",
            "DeviceCmd_rvct03",
            "DeviceCmd_rvct04",
            "DeviceCmd_teltonika",
            "DeviceCmd_telgh3000",
            "DeviceCmd_xirgo",
        };
        String devCmdPackage = DeviceCmd_gtsdmtp.class.getPackage().getName(); // always present

        /* add available Device command pages */
        for (String _dchn : optDevCmdHandlerClasses) {

            /* separate any class:args */
            // DeviceInfo.DeviceCmdAlternate.calamp=1,2,3
            int p = _dchn.indexOf(":");
            String dcn = (p >= 0)? _dchn.substring(0,p).trim() : _dchn;
            String dca = (p >= 0)? _dchn.substring(p+1).trim() : null;
            String dchArgs[] = RTConfig.getStringArray(DBConfig.PROP_DeviceInfo_DeviceCmdAlternate_+dcn, null);
            if (ListTools.isEmpty(dchArgs) && !StringTools.isBlank(dca)) {
                dchArgs = StringTools.split(dca,',');
            }

            /* get class */
            String dchClassName = devCmdPackage + "." + dcn;
            Class  dchClass     = null;
            try {
                //Print.logInfo("Getting Class.forName: " + dchClassName);
                dchClass = Class.forName(dchClassName);
            } catch (Throwable th) { // ClassNotFoundException
                // quietly ignore
                //Print.logWarn("DeviceCmdHandler UI class not found: " + dchClassName);
                continue;
            }

            /* load command handler UI modules */
            try {

                // add standard DCS name
                DeviceCmdHandler stdDCH = (DeviceCmdHandler)dchClass.newInstance();
                _addDeviceCommandHandler(stdDCH);
                //Print.logInfo("Added standard command handler: " + dcn);

                // check for additional DCS name args
                if (!ListTools.isEmpty(dchArgs)) {
                    for (int a = 0; a < dchArgs.length; a++) {
                        String arg = StringTools.trim(dchArgs[a]);
                        if (!StringTools.isBlank(arg)) {
                            DeviceCmdHandler argDCH = (DeviceCmdHandler)dchClass.newInstance();
                            argDCH.setServerIDArg(arg);
                            _addDeviceCommandHandler(argDCH);
                            Print.logInfo("Added custom command handler: " + dcn + " [" + arg + "]");
                        }
                    }
                }

            } catch (Throwable th) { // ClassNotFoundException, ...
                Print.logException("DeviceCmdHandler UI instantiation error: " + dchClassName, th);
            }

        }

        /* SMS */
        SMSCommandHandler = new DeviceCmd_SMS();

    }
    
    private DeviceCmdHandler getDeviceCommandHandler(String dcid)
    {
        DCServerConfig dcs = DCServerFactory.getServerConfig(dcid);
        if (dcs != null) {
            // found a DCS configuration
            return DCMap.get(dcid); // may still be null
        } else {
            // no DCS configuration found
            return null;
        }
    }
    
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    private static boolean ShowNotifySelector()
    {
        return Device.CheckNotifySelector();
    }
    
    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------
    // WebPage interface
    
    public DeviceInfo()
    {
        this.setBaseURI(RequestProperties.TRACK_BASE_URI());
        this.setPageName(PAGE_DEVICE_INFO);
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
        I18N i18n = privLabel.getI18N(DeviceInfo.class);
        String devTitles[] = reqState.getDeviceTitles();
        return super._getMenuDescription(reqState,i18n.getString("DeviceInfo.editMenuDesc","View/Edit {0} Information", devTitles));
    }
   
    public String getMenuHelp(RequestProperties reqState, String parentMenuName)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(DeviceInfo.class);
        String devTitles[] = reqState.getDeviceTitles();
        return super._getMenuHelp(reqState,i18n.getString("DeviceInfo.editMenuHelp","View and Edit {0} information", devTitles));
    }

    // ------------------------------------------------------------------------

    public String getNavigationDescription(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(DeviceInfo.class);
        String devTitles[] = reqState.getDeviceTitles();
        return super._getNavigationDescription(reqState,i18n.getString("DeviceInfo.navDesc","{0}", devTitles));
    }

    public String getNavigationTab(RequestProperties reqState)
    {
        PrivateLabel privLabel = reqState.getPrivateLabel();
        I18N i18n = privLabel.getI18N(DeviceInfo.class);
        String devTitles[] = reqState.getDeviceTitles();
        return i18n.getString("DeviceInfo.navTab","{0} Admin", devTitles);
    }

    // ------------------------------------------------------------------------
    
    public String[] getChildAclList()
    {
        return _ACL_LIST;
    }

    // ------------------------------------------------------------------------

	public String load_LaiXe(String deviceID, String accountID, String allow) {
		String driverID = "";
		String chuoi = "<table style='color:#000000'>";
		DBCamera objcmr = new DBCamera();
		// ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
		ResultSet rs = objcmr.selectDriver(accountID);
		try {
			while (rs.next()) {
				driverID = rs.getString(2);

				ResultSet rs1 = objcmr.select_DeviceDriver(deviceID, driverID,
						accountID);
				while (rs1.next()) {
					String s = rs1.getString(1);
					if (s.equals("1"))
						chuoi = chuoi
								+ "<tr><td align='left'>"
								+ rs.getString(11)
								+ "("
								+ rs.getString(2)
								+ ")</td>"
								+ "<td align='left'><input name='checkDriver' type='checkbox' checked='checked' value='"
								+ rs.getString(2) + "' " + allow
								+ " /></td></tr>";
					else
						chuoi = chuoi
								+ "<tr><td align='left'>"
								+ rs.getString(11)
								+ "("
								+ rs.getString(2)
								+ ")</td>"
								+ "<td align='left'><input name='checkDriver' type='checkbox'  value='"
								+ rs.getString(2) + "'  " + allow
								+ "/></td></tr>";

				}
				rs1.close();
			}
			rs.close();
		} catch (Exception e) {
			chuoi = e.getMessage();
		}

		return chuoi + "</table>";

	}

    //hienntd combo lái xe hiện tại
    public String CreateCbbDriver(String accountid, String idselect,String devid,boolean edit) throws IOException
    {
    	DBCamera db = new DBCamera();
        
     String strre = "<select id ='driver' name = 'driver' class='adminComboBox' style='width: 260px;'";
     if(edit)
    	 strre+="disabled";
     strre+=	">";
     strre +="<option value ='0'>Chọn lái xe</option>\n";
     try
     {
    	 List<String[]> list= db.Device_SelectDriver(devid, accountid);
    	 for(String[] rs:list)
 		{
            /*String idVal = (d < list.length)? list[d].getID()          : ("v" + String.valueOf(d - list.length + 1));
            String desc  = (d < list.length)? list[d].getDescription() : (String.valueOf(d - list.length + 1) + " asset"*/
            if(idselect.equalsIgnoreCase(rs[0]))
          strre +="<option value ='"+rs[0]+"' selected =\"selected\">"+rs[1]+" ["+rs[0]+"]</option>\n";
         else
          strre +="<option value ='"+rs[0]+"'>"+rs[1]+" ["+rs[0]+"]</option>\n";
     }      
     }
     catch (Exception e) {
		// TODO: handle exception
	}
     strre +="</select>\n";
     strre +="<script type ='text/javascript' language ='javascript'> document.getElementById('device ').value = '"+idselect+"';</script>\n";
     return strre;
    }
    
    public String load_(String deviceID,String accountID,String allow)
    {
    	String driverID="";
    	String chuoi="";
    	DBCamera objcmr = new  DBCamera();
  		ResultSet rs = objcmr.Device_select(deviceID, accountID);
  		try{
	  		while(rs.next())
	  		{
  				//dieuhoa
  				String s=rs.getString(1);
  				chuoi +="<tr><td class='adminViewTableHeader'>Điều hòa:</td>";
				if(s==null || !s.equals("1"))
					chuoi+="<td class='adminViewTableData'><input name='checkDieuhoa' type='checkbox'  value='1' "+allow+" /></td></tr>";
				else
					chuoi +="<td class='adminViewTableData'><input name='checkDieuhoa' type='checkbox' checked='checked'  value='1'  "+allow+"/></td></tr>";
				
				//cua
				String s1=rs.getString(2);
				chuoi +="<tr><td class='adminViewTableHeader'>Cửa:</td>";
				if(s1==null || !s1.equals("1"))
					chuoi+="<td class='adminViewTableData'><input name='checkCua' type='checkbox'  value='1' "+allow+" /></td></tr>";
				else
					chuoi +="<td class='adminViewTableData'><input name='checkCua' type='checkbox' checked='checked' value='1'  "+allow+"/></td></tr>";
				
				//nghi dinh 91
				String s2=rs.getString(3);
				chuoi +="<tr><td class='adminViewTableHeader'>Nghị định 91:</td>";
				if(s2==null || !s2.equals("1"))
					chuoi+="<td class='adminViewTableData'><input name='checkND' type='checkbox'  value='1' "+allow+" /></td></tr>";
				else
					chuoi +="<td class='adminViewTableData'><input name='checkND' type='checkbox' checked='checked' value='1'  "+allow+"/></td></tr>";
				
  				//Hienntd add xang
				String s3=rs.getString(4);
				chuoi +="<tr><td class='adminViewTableHeader'>Đo nhiên liệu:</td>";
				if(s3==null || !s3.equals("1"))
					chuoi+="<td class='adminViewTableData'><input name='checkxang' type='checkbox'  value='1' "+allow+" /></td></tr>";
				else
					chuoi +="<td class='adminViewTableData'><input name='checkxang' type='checkbox' checked='checked' value='1'  "+allow+"/></td></tr>";
				
				chuoi +="<tr><td class='adminViewTableHeader'>Quản lý SIM:</td>";
				String s4=rs.getString(5);
				if(s4==null || !s4.equals("1"))
					chuoi+="<td class='adminViewTableData'><input name='qlSIM' type='checkbox'  value='1' "+allow+" /></td></tr>";
				else
					chuoi +="<td class='adminViewTableData'><input name='qlSIM' type='checkbox' checked='checked' value='1'  "+allow+"/></td></tr>";
				
				chuoi +="<tr><td class='adminViewTableHeader'>Truyền dữ liệu:</td>";
				String s5=rs.getString(6);
				if(s5==null || !s5.equals("1"))
					chuoi+="<td class='adminViewTableData'><input name='truyendulieu' type='checkbox'  value='1' "+allow+" /></td></tr>";
				else
					chuoi +="<td class='adminViewTableData'><input name='truyendulieu' type='checkbox' checked='checked' value='1'  "+allow+"/></td></tr>";
  		}
	  		
	  		rs.close();
	  		
  		}catch( Exception e)
  		{ chuoi = e.toString();}
    	
    	return chuoi;
    }
    
    public String rewrite(String n)
    {
    	String str="";
    	if(n.equals("0")||n=="")
    		str="không";
    	else if(n.equals("1"))
    		str="có";
    	
    	
    	return str;
    }
    
    public String FillterDevice(int dieuhoa, int cua, int nd91,int xang,String accid, RequestProperties reqState, PrivateLabel privLabel) throws IOException
    {
    	int dem=0;
    	String strscr ="";
    	String mauNen="";
    	
    	try
    	{
    			
    		int num = 0;
    		DBCamera objcmr = new  DBCamera();
    		//ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
    		
    		//<th class='adminTableHeaderCol_sort'>Quản lý SIM</th>
    		strscr =strscr+ "<div class='adminSelectTable'><table width='100%'class='adminSelectTable_sortable' cellspacing='1' id='myTable' ><thead><tr  align='center'><td width='50px' class='adminTableHeaderCol_sort' >Chọn</td><th  class='adminTableHeaderCol_sort'>Biển số</th><th width='110px' class='adminTableHeaderCol_sort'>IMEI thiết bị</th><th width='100px' class='adminTableHeaderCol_sort'>Mô tả</th><th  class='adminTableHeaderCol_sort'>Lái Xe</th><th width='150px' class='adminTableHeaderCol_sort'>Số SIM</th><th class='adminTableHeaderCol_sort'>Server ID</th><th class='adminTableHeaderCol_sort'>Kích hoạt</th><th class='adminTableHeaderCol_sort'>Điều hòa</th><th class='adminTableHeaderCol_sort'>Cửa</th><th class='adminTableHeaderCol_sort'>Nghị định 91</th><th class='adminTableHeaderCol_sort'>Đo nhiên liệu</th><th class='adminTableHeaderCol_sort'>Quản lý SIM</th><th class='adminTableHeaderCol_sort'>Truyền dữ liệu</th></tr></thead>";
    		 IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
             java.util.List<IDDescription> idList = reqState.createIDDescriptionList(false, sortBy);
             IDDescription list[] = idList.toArray(new IDDescription[idList.size()]);
             String dk ="";
             if(dieuhoa==1) dk = dk+"and dieuhoa=1 ";
             if(cua==1) dk=dk+"and cua=1 ";
             if(nd91==1) dk = dk+"and nd91=1 ";
             if(xang==1) dk = dk+"and xang=1 ";
             for (int d = 0; d < list.length ; d++) {
            	
            	List<String[]> result = objcmr.Device_SelectFilter(list[d].getID(),accid, dk);
             for(String[] rs:result)
    		{    			
    			String css ="";
    			if(num%2==0)
    				css = "adminTableBodyRowOdd";
    			else
    				css = "adminTableBodyRowEven";
    			
    			num++;
    			//<td>"+rewrite(rs[11])+"</td>
    			strscr =strscr+"<tr class ="+css+"><td><input id='"+rs[0]+"' type='radio' value='"+rs[0]+"' name='device'></td><td>"+rs[0]+"</td><td>"+rs[1]+"</td><td>"+rs[2]+"</td><td>"+rs[9]+"</td><td>"+rs[3]+"</td><td>"+rs[4]+"</td><td>"+rewrite(rs[8])+"</td><td>"+rewrite(rs[5])+"</td><td>"+rewrite(rs[6])+"</td><td>"+rewrite(rs[7])+"</td><td>"+rewrite(rs[11])+"</td><td>"+rewrite(rs[12])+"</td><td>"+rewrite(rs[13])+"</td></tr>";	
    			dem++;
    		}
           }
    		strscr =strscr+"</table></div>";
    		
    	}
    	catch (Exception e)
    	{
    	
    	}
    	return strscr;
    }
    
    
    public String LoadDeviceAll( String accid,RequestProperties reqState, PrivateLabel privLabel) throws IOException
    {
    	int dem=0;
    	String strscr ="";
    	String mauNen="";
    	 IDDescription.SortBy sortBy = DeviceChooser.getSortBy(privLabel);
         java.util.List<IDDescription> idList = reqState.createIDDescriptionList(false, sortBy);
               IDDescription list[] = idList.toArray(new IDDescription[idList.size()]);
    	try
    	{    		 
    		int num = 0;
    		DBCamera objcmr = new  DBCamera();
    		//ResultSet rs = objcmr.GetCamera(ngay, device, page, pagesize);
    		
    		//<th class='adminTableHeaderCol_sort'>Quản lý SIM</th>
    		strscr =strscr+ "<div class='adminSelectTable'><table width='100%'class='adminSelectTable_sortable' cellspacing='1' id='myTable' ><thead><tr  align='center'><td width='50px' class='adminTableHeaderCol_sort' >Chọn</td><th  class='adminTableHeaderCol_sort'>Biển số</th><th width='110px' class='adminTableHeaderCol_sort'>IMEI thiết bị</th><th width='100px' class='adminTableHeaderCol_sort'>Mô tả</th><th  class='adminTableHeaderCol_sort'>Lái Xe</th><th width='150px' class='adminTableHeaderCol_sort'>Số SIM</th><th class='adminTableHeaderCol_sort'>Server ID</th><th class='adminTableHeaderCol_sort'>Kích hoạt</th><th class='adminTableHeaderCol_sort'>Điều hòa</th><th class='adminTableHeaderCol_sort'>Cửa</th><th class='adminTableHeaderCol_sort'>Nghị định 91</th><th class='adminTableHeaderCol_sort'>Đo nhiên liệu</th><th class='adminTableHeaderCol_sort'>Quản lý SIM</th><th class='adminTableHeaderCol_sort'>Truyền dữ liệu</th></tr></thead>";
    	  for (int d = 0; d < list.length ; d++) {
    	     ResultSet rs = objcmr.Device_selectAll_1(list[d].getID(),accid);
    		while (rs.next())
    		{    			
    			String css ="";
    			if(num%2==0)
    				css = "adminTableBodyRowOdd";
    			else
    				css = "adminTableBodyRowEven";
    			
    			num++;
    		//<td>"+rewrite(rs.getString(12))+"</td>
    			strscr =strscr+"<tr class ="+css+"><td><input type='radio' name='"+PARM_DEVICE+"' id='"+rs.getString(1)+"' value='"+rs.getString(1)+"'></td><td>"+rs.getString(1)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(3)+"</td><td>"+rs.getString(10)+"</td><td>"+rs.getString(4)+"</td><td>"+rs.getString(5)+"</td><td>"+rewrite(rs.getString(9))+"</td><td>"+rewrite(rs.getString(6))+"</td><td>"+rewrite(rs.getString(7))+"</td><td>"+rewrite(rs.getString(8))+"</td><td>"+rewrite(rs.getString(11))+"</td><td>"+rewrite(rs.getString(12))+"</td><td>"+rewrite(rs.getString(13))+"</td></tr>";	
    			dem++;
    		}
    		rs.close();
    	  }
    		strscr =strscr+"</table></div>";
    	}
    	catch (Exception e)
    	{    	
    	}
    	return strscr;
    }
    
    private String _filterPhoneNum(String simPhone)
    {
        if (StringTools.isBlank(simPhone)) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < simPhone.length(); i++) {
                char ch = simPhone.charAt(i);
                if (Character.isDigit(ch)) {
                    sb.append(ch);
                }
            }
            return sb.toString();
        }
    }

    private boolean _showNotificationFields(PrivateLabel privLabel)
    {
        String propShowNotify = PrivateLabel.PROP_DeviceInfo_showNotificationFields;
        String snf = (privLabel != null)? privLabel.getStringProperty(propShowNotify,null) : null;

        /* has notification fields? */
        if (!Device.supportsNotification()) {
            if ((snf != null) && snf.equalsIgnoreCase(StringTools.STRING_TRUE)) {
                Print.logWarn("Property "+propShowNotify+" is 'true', but Device notification fields are not supported");
            }
            return false;
        }

        /* show notification fields? */
        if (StringTools.isBlank(snf) || snf.equalsIgnoreCase("default")) {
            return Device.hasRuleFactory(); // default value
        } else {
            return StringTools.parseBoolean(snf,false);
        }
        
    }

    /*
    private boolean _showWorkOrderID(PrivateLabel privLabel)
    {
        String propShowWorkOrder = PrivateLabel.PROP_DeviceInfo_showWorkOrderField;
        String swo = (privLabel != null)? privLabel.getStringProperty(propShowWorkOrder,null) : null;

        //* has workOrderID field?
        if (!Device.getFactory().hasField(Device.FLD_workOrderID)) {
            if ((swo != null) && swo.equalsIgnoreCase(StringTools.STRING_TRUE)) {
                Print.logWarn("Property "+propShowWorkOrder+" is 'true', but Device workOrderID not supported");
            }
            return false;
        }

        //* show workOrder field?
        if (StringTools.isBlank(swo) || swo.equalsIgnoreCase("default")) {
            return true; // default value
        } else {
            return StringTools.parseBoolean(swo,false);
        }

    }
    */

    private int _allowNewDevice(Account currAcct, PrivateLabel privLabel)
    {
        if (currAcct == null) {
            // always deny if no account
            Print.logDebug("Allow New Device? Deny - no account");
            return NEWDEV_DENY;
        } else
        if (Account.isSystemAdmin(currAcct)) {
            // always allow if "sysadmin"
            Print.logDebug("Allow New Device? Allow - SystemAdmin");
            return NEWDEV_ALLOW;
        } else {
            String globAND = privLabel.getStringProperty(PrivateLabel.PROP_DeviceInfo_allowNewDevice,"");
            if (globAND.equalsIgnoreCase("sysadmin")) {
                // only allow if logged-in from "sysadmin"
                Print.logDebug("Allow New Device? Allow - if SystemAdmin");
                return NEWDEV_SYSADMIN;
            } else
            if (StringTools.parseBoolean(globAND,false) == false) {
                // explicit deny
                Print.logDebug("Allow New Device? Deny - disallowed");
                return NEWDEV_DENY;
            } else
            if (currAcct.exceedsMaximumDevices(currAcct.getDeviceCount()+1,true)) {
                // deny if over limit
                Print.logDebug("Allow New Device? Deny - over limit");
                return NEWDEV_DENY;
            } else {
                // otherwise allow
                Print.logDebug("Allow New Device? Allow - as specified");
                return NEWDEV_ALLOW;
            }
        }
    }

    /* update Device table with user entered information */
    private String _updateDeviceTable(RequestProperties reqState)
    {
        final Account      currAcct      = reqState.getCurrentAccount();
        final User         currUser      = reqState.getCurrentUser();
        final PrivateLabel privLabel     = reqState.getPrivateLabel();
        final Device       selDev        = reqState.getSelectedDevice(); // 'selDev' is non-null
        final HttpServletRequest request = reqState.getHttpServletRequest();
        final I18N         i18n          = privLabel.getI18N(DeviceInfo.class);
        final Locale       locale        = reqState.getLocale();
        final String       devTitles[]   = reqState.getDeviceTitles();
        final boolean      showFuelEcon  = DBConfig.hasExtraPackage();
        String  msg       = null;
        boolean groupsChg = false;
        String  serverID  = AttributeTools.getRequestString(request, PARM_SERVER_ID         , "");
        String  codeVers  = AttributeTools.getRequestString(request, PARM_CODE_VERS         , "");
        String  uniqueID  = AttributeTools.getRequestString(request, PARM_DEV_UNIQ          , "");
        String  vehicleID = AttributeTools.getRequestString(request, PARM_VEHICLE_ID        , null);
        String  licPlate  = AttributeTools.getRequestString(request, PARM_LICENSE_PLATE     , null);
        String  devActive = AttributeTools.getRequestString(request, PARM_DEV_ACTIVE        , "");
        String  devDesc   = AttributeTools.getRequestString(request, PARM_DEV_DESC          , "");
        String  devName   = AttributeTools.getRequestString(request, PARM_DEV_NAME          , "");
        String  pushpinID = AttributeTools.getRequestString(request, PARM_ICON_ID           , "");
        String  dispColor = AttributeTools.getRequestString(request, PARM_DISPLAY_COLOR     , "");
        String  equipType = AttributeTools.getRequestString(request, PARM_DEV_EQUIP_TYPE    , "");
        double  fuelCap   = AttributeTools.getRequestDouble(request, PARM_DEV_FUEL_CAP      , 0.0);
        double  fuelEcon  = AttributeTools.getRequestDouble(request, PARM_DEV_FUEL_ECON     , 0.0);
        double  speedLimU = AttributeTools.getRequestDouble(request, PARM_DEV_SPEED_LIMIT   , 0.0);
        String  driverID  = AttributeTools.getRequestString(request, PARM_DRIVER_ID         , "");
        String  imeiNum   = AttributeTools.getRequestString(request, PARM_DEV_IMEI          , "");
        String  serialNum = AttributeTools.getRequestString(request, PARM_DEV_SERIAL_NO     , "");
        String  dataKey   = AttributeTools.getRequestString(request, PARM_DATA_KEY          , "");
        String  simPhone  = this._filterPhoneNum(AttributeTools.getRequestString(request,PARM_DEV_SIMPHONE,""));
        String  smsEmail  = AttributeTools.getRequestString(request, PARM_SMS_EMAIL         , "");
        String  noteText  = AttributeTools.getRequestString(request, PARM_DEV_NOTES         , "");
        String  linkURL   = AttributeTools.getRequestString(request, PARM_LINK_URL          , "");
        String  linkDesc  = AttributeTools.getRequestString(request, PARM_LINK_DESC         , "");
        double  fixedLat  = AttributeTools.getRequestDouble(request, PARM_FIXED_LAT         , 0.0);
        double  fixedLon  = AttributeTools.getRequestDouble(request, PARM_FIXED_LON         , 0.0);
        String  ignition  = AttributeTools.getRequestString(request, PARM_IGNITION_INDEX    , "");
        String  grpKeys[] = AttributeTools.getMatchingKeys( request, PARM_DEV_GROUP_);
        String  cstKeys[] = AttributeTools.getMatchingKeys( request, PARM_DEV_CUSTOM_);
        double  rptOdom   = AttributeTools.getRequestDouble(request, PARM_REPORT_ODOM       , 0.0);
        double  rptEngHrs = AttributeTools.getRequestDouble(request, PARM_REPORT_HOURS      , 0.0);
        String  actvCorr  = AttributeTools.getRequestString(request, PARM_ACTIVE_CORRIDOR   , "");
        String  drivernow  = AttributeTools.getRequestString(request, "driver"   , "");
        
      //String  worderID  = AttributeTools.getRequestString(request, PARM_WORKORDER_ID      , "");
        try {
            // unique id
            boolean editUniqID = privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_UNIQUEID));
            if (editUniqID && !selDev.getUniqueID().equals(uniqueID)) {
                if (StringTools.isBlank(uniqueID)) {
                    selDev.setUniqueID("");
                } else {
                    try {
                        Device dev = Transport.loadDeviceByUniqueID(uniqueID);
                        if (dev == null) {
                            selDev.setUniqueID(uniqueID);
                        } else {
                            String devAcctID = dev.getAccountID();
                            String devDevID  = dev.getDeviceID();
                            if (devAcctID.equals(reqState.getCurrentAccountID())) {
                                // same account, this user can fix this himself
                                msg = i18n.getString("DeviceInfo.uniqueIdAlreadyAssignedToDevice",
                                    "UniqueID is already assigned to {0}: {1}", devTitles[0], devDevID);
                                selDev.setError(msg);
                            } else {
                                // different account, this user cannot fix this himself
                                Print.logWarn("UniqueID '%s' already assigned: %s/%s", uniqueID, devAcctID, devDevID);
                                msg = i18n.getString("DeviceInfo.uniqueIdAlreadyAssigned",
                                    "UniqueID is already assigned to another Account");
                                selDev.setError(msg);
                            }
                        }
                    } catch (DBException dbe) {
                        msg = i18n.getString("DeviceInfo.errorReadingUniqueID",
                            "Error while looking for other matching UniqueIDs");
                        selDev.setError(msg);
                    }
                }
            }
            // server id
            boolean editServID = privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_allowEditServerID,EDIT_SERVER_ID) && (selDev.getLastConnectTime() <= 0L);
            if (editServID && !selDev.getDeviceCode().equals(serverID) && DCServerFactory.hasServerConfig(serverID)) {
                selDev.setDeviceCode(serverID);
            }
            // active
            boolean editActive = privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_ACTIVE));
            boolean devActv = ComboOption.parseYesNoText(locale, devActive, true);
            if (editActive && (selDev.getIsActive() != devActv)) { 
                selDev.setIsActive(devActv); 
            }
            // description
            if (!selDev.getDescription().equals(devDesc)) {
                selDev.setDescription(devDesc);
            }
            //driverid
            if (!selDev.getDriverID().equals(drivernow)) {
                selDev.setDriverID(drivernow);
            }
            // display name
            if (!selDev.getDisplayName().equals(devName)) {
                selDev.setDisplayName(devName);
            }
            // vehicle ID (VIN)
            if ((vehicleID != null) && !selDev.getVehicleID().equals(vehicleID)) {
                selDev.setVehicleID(vehicleID);
            }
            // License Plate
            if ((licPlate != null) && !selDev.getLicensePlate().equals(licPlate)) {
                selDev.setLicensePlate(licPlate);
            }
            // equipment type
            if (!selDev.getEquipmentType().equals(equipType)) {
                selDev.setEquipmentType(equipType);
            }
            // fuel capacity
            double fuelCapLiters = Account.getVolumeUnits(currAcct).convertToLiters(fuelCap);
            if (selDev.getFuelCapacity() != fuelCapLiters) {
                selDev.setFuelCapacity(fuelCapLiters);
            }
            // fuel economy (km/L)
            if (showFuelEcon) {
                double fuelEconomy = Account.getEconomyUnits(currAcct).convertToKPL(fuelEcon);
                if (selDev.getFuelEconomy() != fuelEconomy) {
                    selDev.setFuelEconomy(fuelEconomy);
                }
            }
            // Driver ID
//            if (!selDev.getDriverID().equals(driverID)) {
//                selDev.setDriverID(driverID);
//            }
            // IMEI number
            boolean editIMEI = privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_EDIT_IMEI));
            if (editIMEI && !selDev.getImeiNumber().equals(imeiNum)) {
                selDev.setImeiNumber(imeiNum);
            }
            // Serial number
            boolean editSERIAL = privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_EDIT_SERIAL));
            if (!selDev.getSerialNumber().equals(serialNum)) {
                selDev.setSerialNumber(serialNum);
            }
            // SIM phone number
            boolean editSIM = privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_EDIT_SIM));
            if (editSIM && !selDev.getSimPhoneNumber().equals(simPhone)) {
                selDev.setSimPhoneNumber(simPhone);
            }
            // SMS EMail address
            boolean editSMSEm = privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_EDIT_SMS));
            if (editSMSEm && !selDev.getSmsEmail().equals(smsEmail)) { // EMail.validateAddress(smsEmail)
                String se[] = StringTools.split(smsEmail,',');
                if (ListTools.size(se) > 0) {
                    for (String s : se) {
                        if (!StringTools.isBlank(s)) {
                            // save first non-blank entry
                            smsEmail = s;
                            break;
                        }
                    }
                }
                selDev.setSmsEmail(smsEmail);
            }
            // Notes
            boolean notesOK = privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showNotes,SHOW_NOTES);
            if (notesOK && !selDev.getNotes().equals(noteText)) {
                selDev.setNotes(noteText);
            }
            // Link URL/Description
            if (Device.supportsLinkURL()) {
                if (!selDev.getLinkURL().equals(linkURL)) {
                    selDev.setLinkURL(linkURL);
                }
                if (!selDev.getLinkDescription().equals(linkDesc)) {
                    selDev.setLinkDescription(linkDesc);
                }
            }
            // Fixed Latitude/Longitude
            if (Device.supportsFixedLocation()) {
                if ((fixedLat != 0.0) || (fixedLon != 0.0)) {
                    selDev.setFixedLatitude(fixedLat);
                    selDev.setFixedLongitude(fixedLon);
                }
            }
            // Ignition index
            boolean ignOK = privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showIgnitionIndex,SHOW_IGNITION_NDX);
            if (ignOK && !StringTools.isBlank(ignition)) {
                String ign = ignition.toLowerCase();
                int ignNdx = -1;
                if (ign.equals("n/a")) {
                    ignNdx = -1;
                } else
                if (ign.startsWith("ign")) {
                    ignNdx = StatusCodes.IGNITION_INPUT_INDEX;
                } else {
                    ignNdx = StringTools.parseInt(ignition,-1);
                }
                selDev.setIgnitionIndex(ignNdx); // may also clear "lastIgnitionOnTime"/"lastIgnitionOffTime"
            }
            // speed limit
            boolean speedLimOK = Device.hasRuleFactory(); // || privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showSpeedLimit,SHOW_SPEED_LIMIT);
            double speedLimKPH = Account.getSpeedUnits(currAcct).convertToKPH(speedLimU);
            if (speedLimOK && (selDev.getSpeedLimitKPH() != speedLimKPH)) {
                selDev.setSpeedLimitKPH(speedLimKPH);
            }
            // Data Key
            boolean editDATKEY = privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_EDIT_DATKEY));
            boolean dataKeyOK = privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showDataKey,SHOW_DATA_KEY);
            if (editDATKEY && dataKeyOK && !selDev.getDataKey().equals(dataKey)) {
                selDev.setDataKey(dataKey);
            }
            // Pushpin ID
            boolean ppidOK = privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showPushpinID,SHOW_PUSHPIN_ID);
            if (ppidOK && !selDev.getPushpinID().equals(pushpinID)) {
                selDev.setPushpinID(pushpinID);
            }
            // Display Color
            boolean dcolorOK = privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showDisplayColor,SHOW_DISPLAY_COLOR);
            if (dcolorOK && !selDev.getDisplayColor().equals(dispColor)) {
                selDev.setDisplayColor(dispColor);
            }
            // Reported Odometer
            if (rptOdom >= 0.0) {
                Account.DistanceUnits distUnits = Account.getDistanceUnits(currAcct);
                double rptOdomKM  = distUnits.convertToKM(rptOdom);
                double lastOdomKM = selDev.getLastOdometerKM();
                double offsetKM   = rptOdomKM - lastOdomKM;
                if (Math.abs(offsetKM - selDev.getOdometerOffsetKM()) >= 0.1) {
                    selDev.setOdometerOffsetKM(offsetKM);
                }
            }
            // Reported EngineHours
            if (rptEngHrs >= 0.0) {
                double lastEngHrs = selDev.getLastEngineHours();
                double offsetHrs  = rptEngHrs - lastEngHrs;
                if (Math.abs(offsetHrs - selDev.getEngineHoursOffset()) >= 0.01) {
                    selDev.setEngineHoursOffset(offsetHrs);
                }
            }
            // Maintenance Interval
            if (Device.supportsPeriodicMaintenance()) {
                Account.DistanceUnits distUnits = Account.getDistanceUnits(currAcct);
                // Odometer Maintenance #0
                long    mIntrvKM0 = AttributeTools.getRequestLong(request, PARM_MAINT_INTERVKM_0, 0L);
                double  intrvKM0  = distUnits.convertToKM((double)mIntrvKM0);
                boolean mResetKM0 = !StringTools.isBlank(AttributeTools.getRequestString(request,PARM_MAINT_RESETKM_0,null));
                selDev.setMaintIntervalKM0(intrvKM0);
                if (mResetKM0) {
                    selDev.setMaintOdometerKM0(selDev.getLastOdometerKM());
                }
                // Odometer Maintenane #1
                long    mIntrvKM1 = AttributeTools.getRequestLong(request, PARM_MAINT_INTERVKM_1, 0L);
                double  intrvKM1  = distUnits.convertToKM((double)mIntrvKM1);
                boolean mResetKM1 = !StringTools.isBlank(AttributeTools.getRequestString(request,PARM_MAINT_RESETKM_1,null));
                selDev.setMaintIntervalKM1(intrvKM1);
                if (mResetKM1) {
                    selDev.setMaintOdometerKM1(selDev.getLastOdometerKM());
                }
                // EngineHours Maintenane #0
                double  mIntrvHR0 = AttributeTools.getRequestDouble(request, PARM_MAINT_INTERVHR_0, 0.0);
                double  intrvHR0  = mIntrvHR0;
                boolean mResetHR0 = !StringTools.isBlank(AttributeTools.getRequestString(request,PARM_MAINT_RESETHR_0,null));
                selDev.setMaintIntervalHR0(intrvHR0);
                if (mResetHR0) {
                    selDev.setMaintEngHoursHR0(selDev.getLastEngineHours());
                }
                // maintenance notes
                boolean maintNotesOK = privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showMaintenanceNotes,SHOW_MAINTENANCE_NOTES);
                String  maintText = AttributeTools.getRequestString(request, PARM_MAINT_NOTES   , "");
                if (maintNotesOK && !selDev.getMaintNotes().equals(maintText)) {
                    selDev.setMaintNotes(maintText);
                }
            }
            // Rule Engine Notification
            if (_showNotificationFields(privLabel)) {
                String  ruleAllow   = AttributeTools.getRequestString( request, PARM_DEV_RULE_ALLOW  , null);
                String  notifyEmail = AttributeTools.getRequestString( request, PARM_DEV_RULE_EMAIL  , null);
                boolean alertReset  = AttributeTools.getRequestBoolean(request, PARM_LAST_ALERT_RESET, false);
                String  ruleSel     = AttributeTools.getRequestString( request, PARM_DEV_RULE_SEL    ,  null);
                String  ruleDesc    = AttributeTools.getRequestString( request, PARM_DEV_RULE_DESC   ,  null);
                String  ruleSubj    = AttributeTools.getRequestString( request, PARM_DEV_RULE_SUBJ   ,  null);
                String  ruleText    = AttributeTools.getRequestString( request, PARM_DEV_RULE_TEXT   ,  null);
              //String  ruleWrap    = AttributeTools.getRequestString( request, PARM_DEV_RULE_WRAP   ,  null);
                 // Allow Notification
                boolean allowNtfy = ComboOption.parseYesNoText(locale, ruleAllow, true);
                if (selDev.getAllowNotify() != allowNtfy) { 
                    selDev.setAllowNotify(allowNtfy); 
                }
                // Notification email
                if (notifyEmail != null) {
                    if (StringTools.isBlank(notifyEmail)) {
                        if (!selDev.getNotifyEmail().equals(notifyEmail)) {
                            selDev.setNotifyEmail(notifyEmail);
                        }
                    } else
                    if (EMail.validateAddresses(notifyEmail,true)) {
                        if (!selDev.getNotifyEmail().equals(notifyEmail)) {
                            selDev.setNotifyEmail(notifyEmail);
                        }
                    } else {
                        msg = i18n.getString("DeviceInfo.enterEMail","Please enter a valid notification email/sms address");
                        selDev.setError(msg);
                    }
                }
                // notification selector
                if (ruleSel != null) {
                    if (DeviceInfo.ShowNotifySelector() && !selDev.checkSelectorSyntax(ruleSel)) {
                        Print.logInfo("Notification selector has a syntax error: " + ruleSel);
                        msg = i18n.getString("DeviceInfo.ruleError","Notification rule contains a syntax error");
                        selDev.setError(msg);
                    } else {
                        // update rule selector (if changed)
                        if (!selDev.getNotifySelector().equals(ruleSel)) {
                            selDev.setNotifySelector(ruleSel);
                        }
                        //selDev.setAllowNotify(!StringTools.isBlank(ruleSel));
                    }
                }
                // notification description
                if (ruleDesc != null) {
                    if (!selDev.getNotifyDescription().equals(ruleDesc)) {
                        selDev.setNotifyDescription(ruleDesc);
                    }
                }
                // notification subject
                if (ruleSubj != null) {
                    if (!selDev.getNotifySubject().equals(ruleSubj)) {
                        selDev.setNotifySubject(ruleSubj);
                    }
                }
                // notification message
                if (ruleText != null) {
                    if (!selDev.getNotifyText().equals(ruleText)) {
                        selDev.setNotifyText(ruleText);
                    }
                }
                // notify wrapper
                boolean ntfyWrap = false; // ComboOption.parseYesNoText(locale, ruleWrap, true);
                if (selDev.getNotifyUseWrapper() != ntfyWrap) { 
                    selDev.setNotifyUseWrapper(ntfyWrap); 
                }
                // last-notify-time reset
                if ((selDev.getLastNotifyTime() != 0L) && alertReset) {
                    selDev.setLastNotifyTime(0L);
                    selDev.setLastNotifyCode(StatusCodes.STATUS_NONE);
                }
            }
            // Active Corridor
            if (Device.supportsActiveCorridor()) {
                if (!selDev.getActiveCorridor().equals(actvCorr)) {
                    selDev.setActiveCorridor(actvCorr);
                }
            }
            // WorkOrder ID
            /*
            if (_showWorkOrderID(privLabel)) {
                if (!selDev.getWorkOrderID().equals(worderID)) {
                    selDev.setWorkOrderID(worderID);
                }
            }
            */
            // Custom Attributes
            if (!ListTools.isEmpty(cstKeys)) {
                String oldCustAttr = selDev.getCustomAttributes();
                RTProperties rtp = selDev.getCustomAttributesRTP();
                for (int i = 0; i < cstKeys.length; i++) {
                    String cstKey = cstKeys[i];
                    String rtpVal = AttributeTools.getRequestString(request, cstKey, "");
                    String rtpKey = cstKey.substring(PARM_DEV_CUSTOM_.length());
                    rtp.setString(rtpKey, rtpVal);
                }
                String rtpStr = rtp.toString();
                if (!rtpStr.equals(oldCustAttr)) {
                    //Print.logInfo("Setting custom attributes: " + rtpStr);
                    selDev.setCustomAttributes(rtpStr);
                }
            }
            // DeviceGroups
            if (!selDev.hasError()) {
                String accountID = selDev.getAccountID();
                String deviceID  = selDev.getDeviceID();
                // 'grpKey' may only contain 'checked' items!
                OrderedSet<String> fullGroupSet = reqState.getDeviceGroupIDList(true);
                // add checked groups
                if (!ListTools.isEmpty(grpKeys)) {
                    for (int i = 0; i < grpKeys.length; i++) {
                        String grpID = grpKeys[i].substring(PARM_DEV_GROUP_.length());
                        if (!grpID.equalsIgnoreCase(DeviceGroup.DEVICE_GROUP_ALL)) {
                            String chkStr = AttributeTools.getRequestString(request,grpKeys[i],"");
                            boolean chked = chkStr.equalsIgnoreCase("on");
                            boolean exists = DeviceGroup.isDeviceInDeviceGroup(accountID, grpID, deviceID);
                            //Print.logInfo("Checking group : " + grpID + " [checked=" + chked + "]");
                            if (chked) {
                                if (!exists) {
                                    DeviceGroup.addDeviceToDeviceGroup(accountID, grpID, deviceID);
                                    groupsChg = true;
                                }
                            } else {
                                if (exists) {
                                    DeviceGroup.removeDeviceFromDeviceGroup(accountID, grpID, deviceID);
                                    groupsChg = true;
                                }
                            }
                            fullGroupSet.remove(grpID);
                        }
                    }
                }
                // delete remaining (unchecked) groups
                for (Iterator i = fullGroupSet.iterator(); i.hasNext();) {
                    String grpID = (String)i.next();
                    if (!grpID.equalsIgnoreCase(DeviceGroup.DEVICE_GROUP_ALL)) {
                        boolean exists = DeviceGroup.isDeviceInDeviceGroup(accountID, grpID, deviceID);
                        //Print.logInfo("Removing group: " + grpID + " [" + exists + "]");
                        if (exists) {
                            DeviceGroup.removeDeviceFromDeviceGroup(accountID, grpID, deviceID);
                            groupsChg = true;
                        }
                    }
                }
            }
            // save
            if (selDev.hasError()) {
                // should stay on same page
                Print.logInfo("An error occured during Edit ...");   
            } else
            if (selDev.hasChanged()) {
                selDev.save();
                msg = i18n.getString("DeviceInfo.updatedDevice","{0} information updated", devTitles);
            } else
            if (groupsChg) {
                String grpTitles[] = reqState.getDeviceGroupTitles();
                msg = i18n.getString("DeviceInfo.updatedDeviceGroups","{0} membership updated", grpTitles);
            } else {
                // nothing changed
                Print.logInfo("Nothing has changed for this Device ...");
            }
        } catch (Throwable t) {
            Print.logException("Updating Device", t);
            msg = i18n.getString("DeviceInfo.errorUpdate","Internal error updating {0}", devTitles);
            selDev.setError(msg);
        }
        
      //minhnv hihi
        
        
        String[] checkbox;
        checkbox =request.getParameterValues("checkDriver");
        String deviceID=    AttributeTools.getRequestString(request, "device", "");
        DBCamera objcmr = new  DBCamera();
        objcmr.Delete_DeviceDriver(deviceID, currAcct.toString());
        	if(checkbox!=null)
        	{
        		for(int i=0;i<checkbox.length;i++)        			
        		{
        			try {
        				objcmr.Insert_DeviceDriver(deviceID,checkbox[i], currAcct.toString()).close();
        		} catch (Exception e) {
					// TODO: handle exception
				}
        		}        			        		       
        	}
        	//String cbdie =request(request,"checkDieuhoa","");
        	String cbdieuhoa =AttributeTools.getRequestString(request, "checkDieuhoa", "0");	
        	String cbcua =AttributeTools.getRequestString(request,"checkCua","0");
        	String cbnd =AttributeTools.getRequestString(request,"checkND","0");
        	String cbxang =AttributeTools.getRequestString(request,"checkxang","0");
        	String loaiXe =AttributeTools.getRequestString(request,"loaiXe","");
            String  qlSIM  = AttributeTools.getRequestString(request, "qlSIM"   , "0");
            String  truyendulieu  = AttributeTools.getRequestString(request, "truyendulieu"   , "0");
            
        	if(cbdieuhoa!=null && cbcua!=null && cbnd!=null && cbxang!=null)
        	{
        		try {
        			objcmr.Device_update2(deviceID, currAcct.toString(), Integer.parseInt(cbdieuhoa), Integer.parseInt(cbcua), Integer.parseInt(cbnd), Integer.parseInt(cbxang),Integer.parseInt(qlSIM),Integer.parseInt(truyendulieu)).close();
            		objcmr.updateLoaiXe(deviceID, currAcct.getAccountID(), Integer.parseInt(loaiXe)).close();  
				} catch (Exception e) {
					// TODO: handle exception
				}        		        	
        	}
     // lienptk

        return msg;
    }

    // ------------------------------------------------------------------------

    /* write html */
    public void writePage(
        final RequestProperties reqState,
        String pageMsg)
        throws IOException
    {
        final HttpServletRequest request = reqState.getHttpServletRequest();
        final boolean      sysadminLogin = reqState.isLoggedInFromSysAdmin();
        final PrivateLabel privLabel     = reqState.getPrivateLabel();
        final I18N         i18n          = privLabel.getI18N(DeviceInfo.class);
        final Locale       locale        = reqState.getLocale();
        final String       devTitles[]   = reqState.getDeviceTitles();
        final String       grpTitles[]   = reqState.getDeviceGroupTitles();
        final Account      currAcct      = reqState.getCurrentAccount();
        final User         currUser      = reqState.getCurrentUser(); // may be null
        final String       pageName      = this.getPageName();
        String m = pageMsg;
        boolean error = false;
        String cmdBtnTitle = i18n.getString("DeviceInfo.commands","Commands"); // included here to maintain I18N string

        /* device */
        OrderedSet<String> devList = reqState.getDeviceIDList(true/*inclInactv*/);
        if (devList == null) { devList = new OrderedSet<String>(); }
        Device selDev   = reqState.getSelectedDevice();
        String selDevID = (selDev != null)? selDev.getDeviceID() : "";

        /* new device creation */
        final int allowNewDevMode = this._allowNewDevice(currAcct, privLabel);
        boolean allowNew = privLabel.hasAllAccess(currUser,this.getAclName()) && (allowNewDevMode != NEWDEV_DENY);
        if (allowNew && (allowNewDevMode == NEWDEV_SYSADMIN) && !sysadminLogin) {
            // disallow new device creation if the runtime config indicates this is only allowed for "sysadmin" login transfer
            allowNew = false;
        }

        /* ACL allow edit/view */
        boolean allowDelete  = allowNew;
        boolean allowEdit    = allowNew  || privLabel.hasWriteAccess(currUser, this.getAclName());
        boolean allowView    = allowEdit || privLabel.hasReadAccess(currUser, this.getAclName());

        /* "Commands"("Properties") */
        boolean allowCommand = allowView && privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_COMMANDS)) &&
            privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showPropertiesButton,true);
        //Print.logDebug("Allow 'Commands' = " + allowCommand);

        /* "SMS" Commands */
        boolean allowSmsCmd  = allowView && privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_SMS)) &&
            privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showSmsButton,false) && 
            (SMSCommandHandler != null) && SMSCommandHandler.hasSmsCommands(reqState);

        /* submit buttons */
        String  submitEdit   = AttributeTools.getRequestString(request, PARM_SUBMIT_EDIT, "");
        String  submitSearch   = AttributeTools.getRequestString(request, "timkiem", "");
        String  submitView   = AttributeTools.getRequestString(request, PARM_SUBMIT_VIEW, "");
        String  submitChange = AttributeTools.getRequestString(request, PARM_SUBMIT_CHG , "");
        String  submitNew    = AttributeTools.getRequestString(request, PARM_SUBMIT_NEW , "");
        String  submitDelete = AttributeTools.getRequestString(request, PARM_SUBMIT_DEL , "");
        String  submitQueue  = AttributeTools.getRequestString(request, PARM_SUBMIT_QUE , "");
        String  submitProps  = AttributeTools.getRequestString(request, PARM_SUBMIT_PROP, "");
        String  submitSms    = AttributeTools.getRequestString(request, PARM_SUBMIT_SMS , "");

        /* ACL view/edit  */
        boolean viewServID   =                  privLabel.hasReadAccess( currUser, this.getAclName(_ACL_SERVERID   ));
        boolean viewCodeVer  =                  privLabel.hasReadAccess( currUser, this.getAclName(_ACL_FIRMWARE   ));
        boolean editUniqID   = sysadminLogin || privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_UNIQUEID   ));
        boolean viewUniqID   = editUniqID    || privLabel.hasReadAccess( currUser, this.getAclName(_ACL_UNIQUEID   ));
        boolean editActive   = sysadminLogin || privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_ACTIVE     ));
       // boolean editActive =false;
        boolean viewActive   = editActive    || privLabel.hasReadAccess( currUser, this.getAclName(_ACL_ACTIVE     ));
        boolean editRules    = sysadminLogin || privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_RULES      ));
        boolean viewRules    = editRules     || privLabel.hasReadAccess( currUser, this.getAclName(_ACL_RULES      ));
        boolean editSMSEm    = sysadminLogin || privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_EDIT_SMS   ));
        boolean viewSMSEm    = editSMSEm     || privLabel.hasReadAccess( currUser, this.getAclName(_ACL_EDIT_SMS   ));
        boolean editSIM      = sysadminLogin || privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_EDIT_SIM   ));
        boolean viewSIM      = editSIM       || privLabel.hasReadAccess( currUser, this.getAclName(_ACL_EDIT_SIM   ));
        boolean editIMEI     = sysadminLogin || privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_EDIT_IMEI  ));
        boolean viewIMEI     = editIMEI      || privLabel.hasReadAccess( currUser, this.getAclName(_ACL_EDIT_IMEI  ));
        boolean editSERIAL   = sysadminLogin || privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_EDIT_SERIAL));
        boolean viewSERIAL   = editSERIAL    || privLabel.hasReadAccess( currUser, this.getAclName(_ACL_EDIT_SERIAL));
        boolean editDATKEY   = sysadminLogin || privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_EDIT_DATKEY));
        boolean viewDATKEY   = editDATKEY    || privLabel.hasReadAccess( currUser, this.getAclName(_ACL_EDIT_DATKEY));
        boolean editCUA      = sysadminLogin || privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_EDIT_CUA));
        boolean viewCUA      = editCUA    || privLabel.hasReadAccess( currUser, this.getAclName(_ACL_EDIT_CUA));
        boolean editDIEUHOA  = sysadminLogin || privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_EDIT_DIEUHOA));
        boolean viewDIEUHOA  = editDIEUHOA    || privLabel.hasReadAccess( currUser, this.getAclName(_ACL_EDIT_DIEUHOA));
        boolean editND91     = sysadminLogin || privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_EDIT_ND91));
        boolean viewND91     = editND91    || privLabel.hasReadAccess( currUser, this.getAclName(_ACL_EDIT_ND91));
        boolean editXang     = sysadminLogin || privLabel.hasWriteAccess(currUser, this.getAclName(_ACL_EDIT_XANG));
        boolean viewXang     = editXang    || privLabel.hasReadAccess( currUser, this.getAclName(_ACL_EDIT_XANG));
        /* command */
        String  deviceCmd    = reqState.getCommandName();
      //boolean refreshList  = deviceCmd.equals(COMMAND_INFO_REFRESH);
        boolean selectDevice = deviceCmd.equals(COMMAND_INFO_SEL_DEVICE);
        boolean newDevice    = deviceCmd.equals(COMMAND_INFO_NEW_DEVICE);
        boolean updateDevice = deviceCmd.equals(COMMAND_INFO_UPD_DEVICE);
        boolean sendCommand  = deviceCmd.equals(COMMAND_INFO_UPD_PROPS);
        boolean updateSms    = deviceCmd.equals(COMMAND_INFO_UPD_SMS);
    
        boolean deleteDevice = false;

        /* ui display */
        boolean uiList       = false;
        boolean uiEdit       = false;
        boolean uiView       = false;
        boolean uiCmd        = false;
        boolean uiSms        = false;
        boolean uiSearch     = false;
        /* DeviceCmdHandler */
        DeviceCmdHandler dcHandler = null;

        /* pre-qualify commands */
        String newDeviceID = null;
        if (newDevice) {
            if (!allowNew) {
                newDevice = false; // not authorized
            } else {
                if (allowNewDevMode == NEWDEV_SYSADMIN) {
                    // TODO: need to check some "Create Device Password" field
                }
                HttpServletRequest httpReq = reqState.getHttpServletRequest();
                newDeviceID = AttributeTools.getRequestString(httpReq,PARM_NEW_NAME,"").trim();
              //minhnv bo doi thanh chu thuong
                // newDeviceID = newDeviceID.toLowerCase();
                if (StringTools.isBlank(newDeviceID)) {
                    m = i18n.getString("DeviceInfo.enterNewDevice","Please enter a new {0} ID.", devTitles);
                    error = true;
                    newDevice = false;
                }
                //minhnv bo kiem tra chu thuong
                /* else
                if (!WebPageAdaptor.isValidID(reqState,PrivateLabel.PROP_DeviceInfo_validateNewIDs,newDeviceID)) {
                    m = i18n.getString("DeviceInfo.invalidIDChar","ID contains invalid characters");
                    error = true;
                    newDevice = false;
                }*/
            }
        } else
        if (updateDevice) {
            if (!allowEdit) {
                updateDevice = false; // not authorized
            } else
            if (!SubmitMatch(submitChange,i18n.getString("DeviceInfo.change","Change"))) {
                updateDevice = false;
            } else
            if (selDev == null) {
                // should not occur
                m = i18n.getString("DeviceInfo.unableToUpdate","Unable to update Device, ID not found");
                error = true;
                updateDevice = false;
            }
        } else
        if (sendCommand) {
            if (!allowCommand) {
                sendCommand = false; // not authorized
            } else
            if (!SubmitMatch(submitQueue,i18n.getString("DeviceInfo.queue","Queue"))) {
                sendCommand = false; // button not pressed
            } else
            if (selDev == null) {
                m = i18n.getString("DeviceInfo.pleaseSelectDevice","Please select a {0}", devTitles);
                error = true;
                sendCommand = false; // no device selected
            } else
            if (StringTools.isBlank(selDev.getDeviceCode())) {
                Print.logInfo("DeviceCode/ServerID is blank");
                sendCommand = false;
            } else {
                dcHandler = this.getDeviceCommandHandler(selDev.getDeviceCode());
                if (dcHandler == null) {
                    Print.logWarn("DeviceCmdHandler not found: " + selDev.getDeviceCode());
                    sendCommand = false; // not found
                } else
                if (!dcHandler.deviceSupportsCommands(selDev)) {
                    Print.logWarn("DeviceCode/ServerID not supported by handler: " + selDev.getDeviceCode());
                    sendCommand = false; // not supported
                }
            }
        } else
        if (updateSms) {
            if (!allowSmsCmd) {
                updateSms = false; // not authorized
            } else
            if (!SubmitMatch(submitQueue,i18n.getString("DeviceInfo.queue","Queue"))) {
                updateSms = false; // button not pressed
            } else
            if (selDev == null) {
                m = i18n.getString("DeviceInfo.pleaseSelectDevice","Please select a {0}", devTitles);
                error = true;
                updateSms = false; // no device selected
            //} else
            //if (StringTools.isBlank(selDev.getDeviceCode())) {
            //    Print.logInfo("DeviceCode/ServerID is blank");
            //    updateSms = false;
            } else {
                dcHandler = SMSCommandHandler;
                if (dcHandler == null) {
                    Print.logWarn("SMS DeviceCmdHandler not found");
                    updateSms = false; // not found
                } else
                if (!dcHandler.deviceSupportsCommands(selDev)) {
                    Print.logWarn("DeviceCode/ServerID not supported by handler: " + selDev.getDeviceCode());
                    updateSms = false; // not supported
                }
            }
        } else
        if (selectDevice) {
            if (SubmitMatch(submitDelete,i18n.getString("DeviceInfo.delete","Delete"))) {
                if (!allowDelete) {
                    deleteDevice = false; // not authorized
                } else
                if (selDev == null) {
                    m = i18n.getString("DeviceInfo.pleaseSelectDevice","Please select a {0}", devTitles);
                    error = true;
                    deleteDevice = false; // not selected
                } else {
                    deleteDevice = true;
                }
            } else
        	if(SubmitMatch(submitSearch,"Tìm kiếm"))
        	{
        		uiSearch=true;
        	}
        	else
            if (SubmitMatch(submitEdit,i18n.getString("DeviceInfo.edit","Edit"))) {
                if (!allowEdit) {
                    uiEdit = false; // not authorized
                } else
                if (selDev == null) {
                    m = i18n.getString("DeviceInfo.pleaseSelectDevice","Please select a {0}", devTitles);
                    error = true;
                    uiEdit = false; // not selected
                } else {
                    uiEdit = true;
                }
            } else
            if (SubmitMatch(submitView,i18n.getString("DeviceInfo.view","View"))) {
                if (!allowView) {
                    uiView = false; // not authorized
                } else
                if (selDev == null) {
                    m = i18n.getString("DeviceInfo.pleaseSelectDevice","Please select a {0}", devTitles);
                    error = true;
                    uiView = false; // not selected
                } else {
                    uiView = true;
                }
            } else
            if (SubmitMatch(submitProps,i18n.getString("DeviceInfo.properties","Commands"))) {
                if (!allowCommand) {
                    uiCmd = false; // not authorized
                } else
                if (selDev == null) {
                    m = i18n.getString("DeviceInfo.pleaseSelectDevice","Please select a {0}", devTitles);
                    error = true;
                    uiCmd = false; // not selected
                } else {
                    String dcsName = selDev.getDeviceCode();
                    dcHandler = this.getDeviceCommandHandler(dcsName);
                    if (dcHandler == null) {
                        if (StringTools.isBlank(dcsName)) {
                            Print.logWarn("ServerID is blank");
                            m = i18n.getString("DeviceInfo.deviceCommandsNotDefined","Unknown {0} type (blank)", devTitles);
                        } else {
                            Print.logWarn("DeviceCmdHandler not found: " + dcsName);
                            m = i18n.getString("DeviceInfo.deviceCommandsNotFound","{0} not supported", devTitles);
                        }
                        error = true;
                        uiCmd = false; // not supported / blank
                    } else
                    if (!dcHandler.deviceSupportsCommands(selDev)) {
                        Print.logWarn("ServerID does not support Device commands: " + dcsName);
                        m = i18n.getString("DeviceInfo.deviceCommandsNotSupported","{0} Commands not supported", devTitles);
                        error = true;
                        uiCmd = false; // not supported
                    } else {
                        uiCmd = true;
                    }
                }
            } else
            if (SubmitMatch(submitSms,i18n.getString("DeviceInfo.sms","SMS"))) {
                if (!allowSmsCmd) {
                    uiSms = false; // not authorized
                } else
                if (selDev == null) {
                    m = i18n.getString("DeviceInfo.pleaseSelectDevice","Please select a {0}", devTitles);
                    error = true;
                    uiSms = false; // not selected
                } else {
                    dcHandler = SMSCommandHandler;
                    if (dcHandler == null) {
                        Print.logWarn("SMS DeviceCmdHandler not found.");
                        m = i18n.getString("DeviceInfo.deviceSmsNotSupported","{0} SMS not supported", devTitles);
                        error = true;
                        uiSms = false; // not supported
                    } else
                    if (!dcHandler.deviceSupportsCommands(selDev)) {
                        Print.logWarn("SMS not supported by device");
                        m = i18n.getString("DeviceInfo.deviceSmsNotSupported","{0} SMS not supported", devTitles);
                        error = true;
                        uiSms = false; // not supported
                    } else {
                        uiSms = true;
                    }
                }
            }
        }

        /* delete device? */
        if (deleteDevice) {
            // 'selDev' guaranteed non-null here
            try {
                // delete device
                Device.Key devKey = (Device.Key)selDev.getRecordKey();
                Print.logWarn("Deleting Device: " + devKey);
                devKey.delete(true); // will also delete dependencies
                selDevID = "";
                selDev = null;
                reqState.clearDeviceList();
                // select another device
                devList = reqState.getDeviceIDList(true/*inclInactv*/);
                if (!ListTools.isEmpty(devList)) {
                    selDevID = devList.get(0);
                    try {
                        selDev = !selDevID.equals("")? Device.getDevice(currAcct, selDevID) : null; // may still be null
                    } catch (DBException dbe) {
                        // ignore
                    }
                }
            } catch (DBException dbe) {
                Print.logException("Deleting Device", dbe);
                m = i18n.getString("DeviceInfo.errorDelete","Internal error deleting {0}", devTitles);
                error = true;
            }
            uiList = true;
        }

        /* new device? */
        if (newDevice) {
            boolean createDeviceOK = true;
            for (int d = 0; d < devList.size(); d++) {
                if (newDeviceID.equalsIgnoreCase(devList.get(d))) {
                    m = i18n.getString("DeviceInfo.alreadyExists","This {0} already exists", devTitles);
                    error = true;
                    createDeviceOK = false;
                    break;
                }
            }
            if (createDeviceOK) {
                try {
                    Device device = Device.createNewDevice(currAcct, newDeviceID, null); // already saved
                    reqState.clearDeviceList();
                    devList  = reqState.getDeviceIDList(true/*inclInactv*/);
                    selDev   = device;
                    selDevID = device.getDeviceID();
                    
                    DBCamera objcmr = new  DBCamera();
                    objcmr.Device_update2(selDevID, currAcct.getAccountID(), 0, 0, 0,0,0,0).close();
                    
                    m = i18n.getString("DeviceInfo.createdUser","New {0} has been created", devTitles);
                } catch (Exception dbe) {
                    Print.logException("Deleting Creating", dbe);
                    m = i18n.getString("DeviceInfo.errorCreate","Internal error creating {0}", devTitles);
                    error = true;
                }
            }
            uiList = true;
        }

        /* update the device info? */
        if (updateDevice) {
            // 'selDev' guaranteed non-null here
            selDev.clearChanged();
            m = _updateDeviceTable(reqState);
            if (selDev.hasError()) {
                // stay on this page
                uiEdit = true;
            } else {
                uiList = true;
            }
        }

        /* send command */
        if (sendCommand) {
            // 'selDev' and 'dcHandler' guaranteed non-null here
            m = dcHandler.handleDeviceCommands(reqState, selDev);
            Print.logInfo("Returned Message: " + m);
            error = true;
            uiList = true;
        }

        /* update SMS */
        if (updateSms) {
            // 'selDev' and 'dcHandler' guaranteed non-null here
            m = dcHandler.handleDeviceCommands(reqState, selDev);
            Print.logInfo("Returned Message: " + m);
            error = true;
            uiList = true;
        }

        /* last event from device */
        try {
            EventData evd[] = (selDev != null)? selDev.getLatestEvents(1L,false) : null;
            if ((evd != null) && (evd.length > 0)) {
                reqState.setLastEventTime(new DateTime(evd[0].getTimestamp()));
            }
        } catch (DBException dbe) {
            // ignore
        }

        /* PushpinChooser */
        final boolean showPushpinChooser = privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showPushpinChooser,false);

        /* Style */
        HTMLOutput HTML_CSS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                String cssDir = DeviceInfo.this.getCssDirectory();
                WebPageAdaptor.writeCssLink(out, reqState, "DeviceInfo.css", cssDir);
                if (showPushpinChooser) {
                    WebPageAdaptor.writeCssLink(out, reqState, "PushpinChooser.css", cssDir);
                }
            }
        };

        /* JavaScript */
        HTMLOutput HTML_JS = new HTMLOutput() {
            public void write(PrintWriter out) throws IOException {
                MenuBar.writeJavaScript(out, pageName, reqState);
                JavaScriptTools.writeJSInclude(out, JavaScriptTools.qualifyJSFileRef(SORTTABLE_JS), request);
                JavaScriptTools.writeJSInclude(out, JavaScriptTools.qualifyJSFileRef("DeviceInfo.js"), request);
                if (showPushpinChooser) {
                    PushpinIcon.writePushpinChooserJS(out, reqState, true);
                }
            }
        };

        /* Content */
        final Device  _selDev       = selDev; // may be null !!!
        final OrderedSet<String> _deviceList = devList;
        final String  _selDevID     = selDevID ;
        final boolean _allowEdit    = allowEdit;
        final boolean _allowView    = allowView;
        final boolean _allowCommand = allowCommand;
        final boolean _allowSmsCmd  = allowSmsCmd ;
        final boolean _allowNew     = allowNew ;
        final boolean _allowDelete  = allowDelete;
        final boolean _uiCmd        = _allowCommand && uiCmd ;
        final boolean _uiSms        = _allowSmsCmd  && uiSms ;
        final boolean _uiEdit       = _allowEdit    && uiEdit;
        final boolean _search		= uiSearch;
        final boolean _uiView       = _uiEdit || uiView;
        final boolean _uiList       = uiList || (!_uiEdit && !_uiView && !_uiCmd && !_uiSms && !_search);

        HTMLOutput HTML_CONTENT     = null;
        if (_uiList) {

            final boolean _viewUniqID  = viewUniqID;
            final boolean _viewServID  = viewServID;
            final boolean _viewSIM     = viewSIM;
            final boolean _viewActive  = viewActive;
          
            HTML_CONTENT = new HTMLOutput(CommonServlet.CSS_CONTENT_FRAME, m) {
                public void write(PrintWriter out) throws IOException {
                	
                	
                    String selectURL  = DeviceInfo.this.encodePageURL(reqState);//,RequestProperties.TRACK_BASE_URI());
                    String refreshURL = DeviceInfo.this.encodePageURL(reqState); // TODO: add "refresh" page command
                    //String editURL    = DeviceInfo.this.encodePageURL(reqState);//,RequestProperties.TRACK_BASE_URI());
                    String newURL     = DeviceInfo.this.encodePageURL(reqState);//,RequestProperties.TRACK_BASE_URI());

                    /* show expected ACKs */
                    boolean showAcks = privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showExpectedAcks,false);
                   
                    String cbdieuhoa =AttributeTools.getRequestString(request, "ckDieuhoa", "0");	
                	String cbcua =AttributeTools.getRequestString(request,"ckCua","0");
                	String cbnd =AttributeTools.getRequestString(request,"ckND","0");
                	String cbxang =AttributeTools.getRequestString(request,"ckXang","0");
                    // frame header
                    String frameTitle = _allowEdit? 
                        i18n.getString("DeviceInfo.viewEditDevice","View/Edit {0} Information", devTitles) : 
                        i18n.getString("DeviceInfo.viewDevice","View {0} Information", devTitles);
                    out.write("<span class='"+CommonServlet.CSS_MENU_TITLE+"'>"+frameTitle+"</span>&nbsp;\n");
                    out.write("<a href='"+refreshURL+"'><span class=''>"+i18n.getString("DeviceInfo.refresh","Refresh")+"</a>\n");
                    out.write("<br/>\n");
                    out.write("<hr>\n");

                    // device selection table (Select, Device ID, Description, ...)
                    out.write("<h1 class='"+CommonServlet.CSS_ADMIN_SELECT_TITLE+"'>"+i18n.getString("DeviceInfo.selectDevice","Select a {0}",devTitles)+":</h1>\n");
                    out.write("<div>");
                    out.write("<form name='"+FORM_DEVICE_SELECT+"' method='post' action='"+selectURL+"' target='_self'>"); // target='_top'
                    out.write("<input type='hidden' name='"+PARM_COMMAND+"' value='"+COMMAND_INFO_SEL_DEVICE+"' />");
                    
                    
                    out.println("<table class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE+"' cellspacing='0' callpadding='0' border='0' width='450px' style='padding:15px 0 15px 15px'>\n");
                    
                    out.print  ("<tr style='height:40px;'>\n");
                    out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER+"' width='100px' >Điều hòa:</td>\n");
                    out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_DATA+"' width='20px'>\n");
                    out.print  ("<input type='checkbox' value='1' name='ckDieuhoa' />");    
                    out.print  ("</td>");
                    out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER+"' width='50px' >Cửa:</td>\n");
                    out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_DATA+"' width='20px'>\n");
                    out.print  ("<input type='checkbox' value='1' name='ckCua' />");    
                    out.print  ("</td>");
                    out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER+"' width='120px' >Quy chuẩn 31:</td>\n");
                    out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_DATA+"' width='20px'>\n");
                    out.print  ("<input type='checkbox' value='1' name='ckND' />");    
                    out.print("</td>");    
                    out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER+"' width='130px' >Đo nhiên liệu:</td>\n");
                    out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_DATA+"' width='20px'>\n");
                    out.print  ("<input type='checkbox' value='1' name='ckXang' />");    
                    out.print("</td>");  
                    out.print("</tr>");
                    out.println("</table>");
                    
                    out.print("<div class='viewhoz'>");
                    out.println("<table style='padding-left:50px' >");
                    out.print("<tr >");
                    
                    out.write("<td style='padding-left:5px;'>");
                    out.write("<input type='submit' name='timkiem' value='Tìm kiếm' class='button1'>");
                    out.write("</td>\n"); 
                    if (_allowView) { 
                        out.write("<td style='padding-left:5px;'>");
                        out.write("<input type='submit' name='"+PARM_SUBMIT_VIEW+"' value='"+i18n.getString("DeviceInfo.view","View")+"'  class='button1'>");
                        out.write("</td>\n"); 
                    }
                    if (_allowEdit) { 
                        out.write("<td style='padding-left:5px;'>");
                        out.write("<input type='submit' name='"+PARM_SUBMIT_EDIT+"' value='"+i18n.getString("DeviceInfo.edit","Edit")+"'  class='button1'>");
                        out.write("</td>\n"); 
                    }
                    if (_allowDelete) {
                    	out.write("<td style='padding-left:5px;'>");
                        out.write("<input type='submit' name='"+PARM_SUBMIT_DEL +"' value='"+i18n.getString("DeviceInfo.delete","Delete")+"' "+Onclick_ConfirmDelete(locale)+"  class='button1'>");
                        out.write("</td>\n"); 
                    } else {
                        
                    }
                    
                    
                    out.println("</tr>");
                    
                    out.println("</table>");
                    out.print("</div>");
                    PrivateLabel privLabel = reqState.getPrivateLabel();
//                    final RequestProperties reqState
                    out.write(LoadDeviceAll(reqState.getCurrentAccountID(),reqState,privLabel));
             
                    out.write("</form>\n");
                    out.write("</div>\n");
                    out.write("<hr>\n");

                    /* new device */
                    if (_allowNew) {
                    String createText = i18n.getString("DeviceInfo.createNewDevice","Create a new device");
                    if (allowNewDevMode == NEWDEV_SYSADMIN) {
                        createText += " " + i18n.getString("DeviceInfo.sysadminOnly","(System Admin only)");
                    }
                 
                    }

                }
            };

        } else
        if (_uiEdit || _uiView) {

            final boolean _viewServID  = _uiView && viewServID;
            final boolean _viewCodeVer = _uiView && viewCodeVer;
            final boolean _editUniqID  = _uiEdit && editUniqID;
            final boolean _viewUniqID  = _uiView && viewUniqID;
            final boolean _editActive  = _uiEdit && editActive;
            final boolean _viewActive  = _uiView && viewActive;
            final boolean _editRules   = _uiEdit && editRules;
            final boolean _viewRules   = _uiView && viewRules;
            final boolean _editSMSEm   = _uiEdit && editSMSEm;
            final boolean _viewSMSEm   = _uiView && viewSMSEm;
            final boolean _editSIM     = _uiEdit && editSIM;
            final boolean _viewSIM     = _uiView && viewSIM;
            final boolean _editIMEI    = _uiEdit && editIMEI;
            final boolean _viewIMEI    = _uiView && viewIMEI;
            final boolean _editSERIAL  = _uiEdit && editSERIAL;
            final boolean _viewSERIAL  = _uiView && viewSERIAL;
            final boolean _editDATKEY  = _uiEdit && editDATKEY;
            final boolean _viewDATKEY  = _uiView && viewDATKEY;
           
            HTML_CONTENT = new HTMLOutput(CommonServlet.CSS_CONTENT_FRAME, m) {
                public void write(PrintWriter out) throws IOException {
                    String editURL       = DeviceInfo.this.encodePageURL(reqState);//,RequestProperties.TRACK_BASE_URI());
                    boolean ntfyOK       = _viewRules && _showNotificationFields(privLabel);
                    boolean ntfyEdit     = ntfyOK && _editRules;
                    boolean ignOK        = privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showIgnitionIndex,SHOW_IGNITION_NDX);
                    boolean ppidOK       = privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showPushpinID,SHOW_PUSHPIN_ID);
                    boolean dcolorOK     = privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showDisplayColor,SHOW_DISPLAY_COLOR);
                    boolean notesOK      = privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showNotes,SHOW_NOTES);
                    boolean maintNotesOK = privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showMaintenanceNotes,SHOW_MAINTENANCE_NOTES);
                    boolean dataKeyOK    = privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showDataKey,SHOW_DATA_KEY);
                    boolean speedLimOK   = Device.hasRuleFactory(); // || privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showSpeedLimit,SHOW_SPEED_LIMIT);
                    boolean showFuelEcon = DBConfig.hasExtraPackage();
                    boolean fixLocOK     = (_selDev != null) && Device.supportsFixedLocation() && 
                        privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showFixedLocation,SHOW_FIXED_LOCATION);
                    boolean editServID   = ((_selDev == null) || (_selDev.getLastConnectTime() <= 0L)) &&
                        privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_allowEditServerID,EDIT_SERVER_ID);
                    
                    String btnXem =AttributeTools.getRequestString(request, "d_subview", "");
                    
                    /* distance units description */
                    Account.DistanceUnits distUnits = Account.getDistanceUnits(currAcct);
                    String distUnitsStr = distUnits.toString(locale);

                    /* speed units description */
                    Account.SpeedUnits speedUnits = Account.getSpeedUnits(currAcct);
                    String speedUnitsStr = speedUnits.toString(locale);

                    /* volume units description */
                    Account.VolumeUnits volmUnits = Account.getVolumeUnits(currAcct);
                    String volmUnitsStr = volmUnits.toString(locale);

                    /* volume units description */
                    Account.EconomyUnits econUnits = Account.getEconomyUnits(currAcct);
                    String econUnitsStr = econUnits.toString(locale);

                    /* custom attributes */
                    Collection<String> customKeys = new OrderedSet<String>();
                    Collection<String> ppKeys = privLabel.getPropertyKeys(PrivateLabel.PROP_DeviceInfo_custom_);
                    for (String ppKey : ppKeys) {
                        customKeys.add(ppKey.substring(PrivateLabel.PROP_DeviceInfo_custom_.length()));
                    }
                    if (_selDev != null) {
                        customKeys.addAll(_selDev.getCustomAttributeKeys());
                    }

                    /* last connect times */
                    String lastConnectTime = (_selDev != null)? reqState.formatDateTime(_selDev.getLastTotalConnectTime()) : "";
                    if (StringTools.isBlank(lastConnectTime)) { lastConnectTime = i18n.getString("DeviceInfo.neverConnected","never"); }
                    String lastEventTime   = reqState.formatDateTime(reqState.getLastEventTime());
                    if (StringTools.isBlank(lastEventTime  )) { lastEventTime   = i18n.getString("DeviceInfo.noLastEvent"   ,"none" ); }

                    // frame header
                    String frameTitle = _allowEdit? 
                        i18n.getString("DeviceInfo.viewEditDevice","View/Edit {0} Information", devTitles) : 
                        i18n.getString("DeviceInfo.viewDevice","View {0} Information", devTitles);
                    out.write("<span class='"+CommonServlet.CSS_MENU_TITLE+"'>"+frameTitle+"</span><br/>\n");
                    out.write("<hr>\n");

                    /* start of form */
                    out.write("<form name='"+FORM_DEVICE_EDIT+"' method='post' action='"+editURL+"' target='_self'>\n"); // target='_top'
                    out.write("<input type='hidden' name='"+PARM_COMMAND+"' value='"+COMMAND_INFO_UPD_DEVICE+"'/>\n");

                    /* Device fields */
                    ComboOption devActive = ComboOption.getYesNoOption(locale, ((_selDev != null) && _selDev.isActive()));
                    String firmVers  = (_selDev!=null)? _selDev.getCodeVersion() : "";
                    long   createTS  = (_selDev!=null)? _selDev.getCreationTime() : 0L;
                    String createStr = reqState.formatDateTime(createTS, "--");
                    out.println("<table class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE+"' cellspacing='0' callpadding='0' border='0'>");
                    out.println(FormRow_TextField(PARM_DEVICE           , false      , i18n.getString("DeviceInfo.deviceID","{0} ID",devTitles)+":"        , _selDevID, 40, 32));
                    //minhnv add loại xe
                    String allow ="";
                    
                    
                    if(btnXem.equals("Xem"))
                    {
                    	allow="disabled";
                    }
                    String deviceID=    AttributeTools.getRequestString(request, "device", "");
                    DBCamera db = new DBCamera();
                    out.write("<tr><td class='adminViewTableHeader' nowrap=''>Loại xe:</td><td class='adminViewTableHeader' style='text-align:left;padding-left:0px'>"+db.LoaiXe(allow,currAcct.getAccountID(),deviceID)+"</td></tr>");                                                          
                    
                    out.println(FormRow_TextField(PARM_CREATE_DATE      , false      , i18n.getString("DeviceInfo.creationDate","Creation Date")+":"       , createStr, 40, 30));
                    String deviceCode = (_selDev != null)? DCServerFactory.getServerConfigDescription(_selDev.getDeviceCode()) : "";
                    String autoFill = null;
                    if (StringTools.isBlank(deviceCode)) {
                        autoFill = i18n.getString("DeviceInfo.serverIdAutoFill","(automatically entered by the DCS)");
                    }
                    if (_viewServID) {
                    out.println(FormRow_TextField(PARM_SERVER_ID        , editServID , i18n.getString("DeviceInfo.serverID","Server ID")+":"               , deviceCode, 40, 20, autoFill));
                    }
                    if (_viewCodeVer) {
                    out.println(FormRow_TextField(PARM_CODE_VERS        , false      , i18n.getString("DeviceInfo.firmwareVers","Firmware Version")+":"    , firmVers, 40, 28));
                    }
                    if (_viewUniqID) {
                    out.println(FormRow_TextField(PARM_DEV_UNIQ         , _editUniqID, i18n.getString("DeviceInfo.uniqueID","Unique ID")+":"               , (_selDev!=null)?_selDev.getUniqueID():""       , 40, 30));
                    }
                    if (_viewActive) {
                    out.println(FormRow_ComboBox (PARM_DEV_ACTIVE       , false, i18n.getString("DeviceInfo.active","Active")+":"                    , devActive, ComboMap.getYesNoMap(locale), ""    , 40));
                    }
                    out.println(FormRow_TextField(PARM_DEV_DESC         , _uiEdit    , i18n.getString("DeviceInfo.deviceDesc","{0} Description",devTitles) +":", (_selDev!=null)?_selDev.getDescription():"", 40, 64));
                   //hienntd add combo lái xe
                    out.println("<tr><td nowrap='' class='adminViewTableHeader'>Lái xe hiện tại:</td>"); 
                    out.println("<td style='text-align:left;padding-left:0px' class='adminViewTableHeader'>"+CreateCbbDriver(currAcct.getAccountID(), _selDev.getDriverID(), _selDev.getDeviceID(),btnXem.equals("Xem"))+"</td></tr>");
                    out.println(FormRow_TextField(PARM_DEV_NAME         , _uiEdit    , "Tên Lái Xe:"  , (_selDev!=null)?_selDev.getDisplayName():""    , 40, 64));
                    
                    out.println(FormRow_TextField(PARM_LICENSE_PLATE    , _uiEdit    , i18n.getString("DeviceInfo.licensePlate","Số Bằng Lái") +":"       , (_selDev!=null)?_selDev.getLicensePlate():""   , 40, 24));
                    double speedLimUnits = (_selDev!=null)? Account.getSpeedUnits(currAcct).convertFromKPH(_selDev.getSpeedLimitKPH()) : 0.0;
                    String speedLimStr   = StringTools.format(speedLimUnits, "0.0");
                    out.println(FormRow_TextField(PARM_DEV_SPEED_LIMIT, _uiEdit  , i18n.getString("DeviceInfo.speedLimit","Tốc độ cho phép") +":"        , speedLimStr                                    , 40, 10, speedUnitsStr));
                    out.println(FormRow_TextField(PARM_VEHICLE_ID       , _uiEdit    , i18n.getString("DeviceInfo.vehicleID","Vehicle ID") +":"            , (_selDev!=null)?_selDev.getVehicleID():""      , 40, 24));
                    // out.println(FormRow_TextField(PARM_VEHICLE_ID       , _uiEdit    , "Số máy:"            , (_selDev!=null)?_selDev.getLicenseVehicle():""      , 40, 24));
                    out.println(FormRow_TextField(PARM_DEV_EQUIP_TYPE   , _uiEdit    , "SĐT chủ xe:"    , (_selDev!=null)?_selDev.getEquipmentType():""  , 40, 40));
                    if (_viewIMEI) {
                    out.println(FormRow_TextField(PARM_DEV_IMEI         , _editIMEI  , i18n.getString("DeviceInfo.imeiNumber","IMEI/ESN Number") +":"      , (_selDev!=null)?_selDev.getImeiNumber():""     , 40, 18));
                    }
                    if (_viewSERIAL) {
                    out.println(FormRow_TextField(PARM_DEV_SERIAL_NO    , _editSERIAL, i18n.getString("DeviceInfo.serialNumber","Serial Number") +":"      , (_selDev!=null)?_selDev.getSerialNumber():""   , 40, 24));
                    }
                    if (dataKeyOK && _viewDATKEY) {
                    out.println(FormRow_TextField(PARM_DATA_KEY         , _editDATKEY, i18n.getString("DeviceInfo.dataKey","Data Key") +":"                , (_selDev!=null)?_selDev.getDataKey():""        , 40, 200));
                    }
                    if (_viewSIM) {
                    out.println(FormRow_TextField(PARM_DEV_SIMPHONE     , _editSIM   , i18n.getString("DeviceInfo.simPhoneNumber","SIM Phone#") +":"       , (_selDev!=null)?_selDev.getSimPhoneNumber():"" , 40, 18));
                    }
                    if (_viewSMSEm) {
                    out.println(FormRow_TextField(PARM_SMS_EMAIL        , _editSMSEm , i18n.getString("DeviceInfo.smsEmail","SMS Email Address") +":"      , (_selDev!=null)?_selDev.getSmsEmail():""       , 40, 60));
                    }
                    if (ppidOK) {
                        String ppDesc = i18n.getString("DeviceInfo.mapPushpinID","{0} Pushpin ID",grpTitles)+":";
                        String ppid = (_selDev != null)? _selDev.getPushpinID() : "";
                        if (showPushpinChooser) {
                            String ID_ICONSEL = "PushpinChooser";
                            String onclick    = _uiEdit? "javascript:ppcShowPushpinChooser('"+ID_ICONSEL+"')" : null;
                            out.println(FormRow_TextField(ID_ICONSEL, PARM_ICON_ID, _uiEdit, ppDesc, ppid, onclick, 40, 32, null));
                        } else {
                            ComboMap ppList = new ComboMap(reqState.getMapProviderPushpinIDs());
                            ppList.insert(""); // insert a blank as the first entry
                            out.println(FormRow_ComboBox(PARM_ICON_ID, _uiEdit, ppDesc, ppid, ppList, "", 40));
                        }
                    }
                    if (dcolorOK) {
                        String dcDesc = i18n.getString("DeviceInfo.mapRouteColor","Map Route Color")+":";
                        String dcolor = (_selDev != null)? _selDev.getDisplayColor() : "";
                        double P = 0.30;
                        ComboMap dcCombo = new ComboMap();
                        dcCombo.add(""                                        ,i18n.getString("DeviceInfo.color.default","Default"));
                        dcCombo.add(ColorTools.BLACK.toString(true)           ,i18n.getString("DeviceInfo.color.black"  ,"Black"  ));
                        dcCombo.add(ColorTools.BROWN.toString(true)           ,i18n.getString("DeviceInfo.color.brown"  ,"Brown"  ));
                        dcCombo.add(ColorTools.RED.toString(true)             ,i18n.getString("DeviceInfo.color.red"    ,"Red"    ));
                        dcCombo.add(ColorTools.ORANGE.darker(P).toString(true),i18n.getString("DeviceInfo.color.orange" ,"Orange" ));
                      //dcCombo.add(ColorTools.YELLOW.darker(P).toString(true),i18n.getString("DeviceInfo.color.yellow" ,"Yellow" ));
                        dcCombo.add(ColorTools.GREEN.darker(P).toString(true) ,i18n.getString("DeviceInfo.color.green"  ,"Green"  ));
                        dcCombo.add(ColorTools.BLUE.toString(true)            ,i18n.getString("DeviceInfo.color.blue"   ,"Blue"   ));
                        dcCombo.add(ColorTools.PURPLE.toString(true)          ,i18n.getString("DeviceInfo.color.purple" ,"Purple" ));
                        dcCombo.add(ColorTools.DARK_GRAY.toString(true)       ,i18n.getString("DeviceInfo.color.gray"   ,"Gray"   ));
                      //dcCombo.add(ColorTools.WHITE.toString(true)           ,i18n.getString("DeviceInfo.color.white"  ,"White"  ));
                        dcCombo.add(ColorTools.CYAN.darker(P).toString(true)  ,i18n.getString("DeviceInfo.color.cyan"   ,"Cyan"   ));
                        dcCombo.add(ColorTools.PINK.toString(true)            ,i18n.getString("DeviceInfo.color.pink"   ,"Pink"   ));
                        dcCombo.add("none"                                    ,i18n.getString("DeviceInfo.color.none"   ,"None"   ));
                        out.println(FormRow_ComboBox(PARM_DISPLAY_COLOR, _uiEdit, dcDesc, dcolor, dcCombo, "", 40));
                    }
                    if (ignOK) {
                        ComboMap ignList = new ComboMap(new String[] { "n/a", "ign" }); //,"0","1","2","3","4","5","6","7" });
                        int maxIgnNdx = privLabel.getIntProperty(PrivateLabel.PROP_DeviceInfo_maximumIgnitionIndex,7);
                        for (int igx = 0; igx <= maxIgnNdx; igx++) {
                            String igs = String.valueOf(igx);
                            ignList.add(igs, igs);
                        }
                        int ignNdx = (_selDev != null)? _selDev.getIgnitionIndex() : -1;
                        String ignSel = "";
                        if (ignNdx < 0) {
                            ignSel = "n/a";
                        } else
                        if (ignNdx == StatusCodes.IGNITION_INPUT_INDEX) {
                            ignSel = "ign";
                        } else {
                            ignSel = String.valueOf(ignNdx);
                        }
                        out.println(FormRow_ComboBox( PARM_IGNITION_INDEX, _uiEdit   , i18n.getString("DeviceInfo.ignitionIndex","Ignition Input") +":" , ignSel, ignList, "", 40, i18n.getString("DeviceInfo.ignitionIndexDesc","(ignition input line, if applicable)")));
                    }
                    // fuel capacity
                    double fuelCapUnits = (_selDev!=null)? volmUnits.convertFromLiters(_selDev.getFuelCapacity()) : 0.0;
                    String fuelCapStr   = StringTools.format(fuelCapUnits, "0.0");
                    out.println(FormRow_TextField(PARM_DEV_FUEL_CAP     , _uiEdit    , i18n.getString("DeviceInfo.fuelCapacity","Fuel Capacity") +":"      , fuelCapStr                                     , 40, 10, volmUnitsStr));
                    // fuel economy
                    if (showFuelEcon) {
                    double fuelEconUnits = (_selDev!=null)? econUnits.convertFromKPL(_selDev.getFuelEconomy()) : 0.0;
                    String fuelEconStr   = StringTools.format(fuelEconUnits, "0.0");
                    out.println(FormRow_TextField(PARM_DEV_FUEL_ECON    , _uiEdit    , i18n.getString("DeviceInfo.fuelEconomy","Fuel Economy") +":"        , fuelEconStr                                    , 40, 10, econUnitsStr));
                    }

                    if (Device.supportsPeriodicMaintenance()) {
                        // add separator before odometer if maintenance is supported
                        out.println(FormRow_Separator());
                    }
                    if (Device.supportsLastOdometer()) {
                        double odomKM   = (_selDev != null)? _selDev.getLastOdometerKM() : 0.0;
                        double offsetKM = (_selDev != null)? _selDev.getOdometerOffsetKM() : 0.0;
                        double rptOdom  = distUnits.convertFromKM(odomKM + offsetKM);
                        String odomStr  = StringTools.format(rptOdom, "0.0");
                        out.println(FormRow_TextField(PARM_REPORT_ODOM, _uiEdit, i18n.getString("DeviceInfo.reportOdometer","Reported Odometer") +":" , odomStr, 40, 11, distUnitsStr));
                    }
                    if (Device.supportsLastEngineHours()) {
                        double engHR    = (_selDev != null)? _selDev.getLastEngineHours() : 0.0;
                        double offsetHr = (_selDev != null)? _selDev.getEngineHoursOffset() : 0.0;
                        String hourStr  = StringTools.format(engHR + offsetHr, "0.00");
                        String lastHrT  = i18n.getString("DeviceInfo.lastEngineHours","Last Engine Hours");
                        out.println(FormRow_TextField(PARM_REPORT_HOURS, _uiEdit, i18n.getString("DeviceInfo.reportEngineHours","Reported Engine Hours") +":" , hourStr, 40, 11, null));
                    }
                    if (Device.supportsPeriodicMaintenance()) {
                        double offsetKM = (_selDev != null)? _selDev.getOdometerOffsetKM() : 0.0;
                        double offsetHR = 0.0;
                        // Maintenance Notes
                        if (maintNotesOK) {
                            String noteText = (_selDev!=null)? StringTools.decodeNewline(_selDev.getMaintNotes()) : "";
                            out.println(FormRow_TextArea(PARM_MAINT_NOTES, _uiEdit, i18n.getString("DeviceInfo.maintNotes" ,"Maintenance Notes")+":", noteText, 3, 70));
                        }
                        // Odometer Maintenance / Interval
                        for (int ndx = 0; ndx < Device.getPeriodicMaintOdometerCount(); ndx++) {
                            String ndxStr = String.valueOf(ndx + 1);
                            out.println(FormRow_SubSeparator());
                            double lastMaintKM = (_selDev != null)? _selDev.getMaintOdometerKM(ndx) : 0.0;
                            double lastMaint   = distUnits.convertFromKM(lastMaintKM + offsetKM);
                            String lastMaintSt = StringTools.format(lastMaint, "0.0");
                            out.println(FormRow_TextField(PARM_MAINT_LASTKM_+ndx, false, i18n.getString("DeviceInfo.mainLast","Last Maintenance #{0}",ndxStr) +":" , lastMaintSt, 40, 11, distUnitsStr));
                            double intrvKM = (_selDev != null)? _selDev.getMaintIntervalKM(ndx) : 0.0;
                            double intrv   = distUnits.convertFromKM(intrvKM);
                            out.print("<tr>");
                            out.print("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER+"' nowrap>"+i18n.getString("DeviceInfo.maintInterval","Maintenance Interval #{0}",ndxStr)+":</td>");
                            out.print("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_DATA+"'>");
                            out.print(Form_TextField(PARM_MAINT_INTERVKM_+ndx, _uiEdit, String.valueOf((long)intrv), 40, 11));
                            out.print("&nbsp;" + distUnitsStr);
                            if (_uiEdit) {
                                out.print(" &nbsp;&nbsp;(" + i18n.getString("DeviceInfo.maintReset","Check to Reset Service") + " ");
                                out.print(Form_CheckBox(PARM_MAINT_RESETKM_+ndx, PARM_MAINT_RESETKM_+ndx, _uiEdit, false, null, null));
                                out.print(")");
                            }
                            out.print("</td>");
                            out.print("</tr>\n");
                        }
                        // EngineHours Maintenance / Interval
                        for (int ndx = 0; ndx < Device.getPeriodicMaintEngHoursCount(); ndx++) {
                            String ndxStr = String.valueOf(ndx + 1);
                            out.println(FormRow_SubSeparator());
                            double lastMaintHR = (_selDev != null)? _selDev.getMaintEngHoursHR(ndx) : 0.0;
                            double lastMaint   = lastMaintHR + offsetHR;
                            String lastMaintSt = StringTools.format(lastMaint, "0.00");
                            out.println(FormRow_TextField(PARM_MAINT_LASTHR_+ndx, false, i18n.getString("DeviceInfo.maintEngHours","Last Eng Hours Maint",ndxStr) +":" , lastMaintSt, 40, 11, null));
                            double intrvHR = (_selDev != null)? _selDev.getMaintIntervalHR(ndx) : 0.0;
                            double intrv   = intrvHR;
                            out.print("<tr>");
                            out.print("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER+"' nowrap>"+i18n.getString("DeviceInfo.maintIntervalHR","Eng Hours Maint Interval",ndxStr)+":</td>");
                            out.print("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_DATA+"'>");
                            out.print(Form_TextField(PARM_MAINT_INTERVHR_+ndx, _uiEdit, String.valueOf((long)intrv), 40, 11));
                            out.print("&nbsp;");
                            if (_uiEdit) {
                                out.print(" &nbsp;&nbsp;(" + i18n.getString("DeviceInfo.maintResetHR","Check to Reset Service") + " ");
                                out.print(Form_CheckBox(PARM_MAINT_RESETHR_+ndx, PARM_MAINT_RESETHR_+ndx, _uiEdit, false, null, null));
                                out.print(")");
                            }
                            out.print("</td>");
                            out.print("</tr>\n");
                        }
                    }
                    // Notification Section
                    if (ntfyOK) {
                        ComboOption allowNotfy = ComboOption.getYesNoOption(locale, ((_selDev!=null) && _selDev.getAllowNotify()));
                        out.println(FormRow_Separator());
                        out.println(FormRow_ComboBox (PARM_DEV_RULE_ALLOW, ntfyEdit  , i18n.getString("DeviceInfo.notifyAllow","Notify Enable")+":"     , allowNotfy, ComboMap.getYesNoMap(locale), "", 40));
                        out.println(FormRow_TextField(PARM_DEV_RULE_EMAIL, ntfyEdit  , i18n.getString("DeviceInfo.notifyEMail","Notify Email")+":"      , (_selDev!=null)?_selDev.getNotifyEmail():"", 40, 125));
                        out.print("<tr>");
                        out.print("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER+"' nowrap>"+i18n.getString("DeviceInfo.lastAlertTime","Last Alert Time")+":</td>");
                        out.print("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_DATA+"'>");
                        long lastNotifyTime = (_selDev != null)? _selDev.getLastNotifyTime() : 0L;
                        if (lastNotifyTime <= 0L) {
                            String na = i18n.getString("DeviceInfo.noAlert","n/a");
                            out.print(Form_TextField(PARM_LAST_ALERT_TIME, false, na, 40, 10));
                        } else {
                            String lastAlertTime = reqState.formatDateTime(lastNotifyTime,"--");
                            out.print(Form_TextField(PARM_LAST_ALERT_TIME, false, lastAlertTime, 40, 24));
                            if (ntfyEdit) {
                                out.print(" &nbsp;&nbsp;(" + i18n.getString("DeviceInfo.lastAlertReset","Check to Reset Alert") + " ");
                                out.print(Form_CheckBox(PARM_LAST_ALERT_RESET, PARM_LAST_ALERT_RESET, ntfyEdit, false, null, null));
                                out.print(")");
                            }
                        }
                        out.print("</td>");
                        out.print("</tr>\n");
                        // -- notify selector, subject, body ...
                        String ntfySel  = (_selDev!=null)?_selDev.getNotifySelector():"";
                        String ntfyDesc = (_selDev!=null)?_selDev.getNotifyDescription():"";
                        String ntfySubj = (_selDev!=null)?_selDev.getNotifySubject():"";
                        String ntfyText = (_selDev!=null)? StringTools.decodeNewline(_selDev.getNotifyText()) : "";
                        if (!DeviceInfo.ShowNotifySelector()) {
                            // the Device rule selector is not used, don't show
                        } else
                        if (SHOW_NOTIFY_SELECTOR           || // always show?
                            !Device.hasENRE()              || // show if non-ENRE is installed
                            !StringTools.isBlank(ntfySel)  || //   or if the selector or message is specified
                            !StringTools.isBlank(ntfySubj) || 
                            !StringTools.isBlank(ntfyText)   ) {
                            out.println(FormRow_SubSeparator());
                            String notifyRuleTitle = i18n.getString("DeviceInfo.notifyRule","Notify Rule");
                            out.println(FormRow_TextField(PARM_DEV_RULE_SEL  , ntfyEdit  , i18n.getString("DeviceInfo.ruleSelector","Rule Selector")+":"     , ntfySel  , 75, 90));
                            out.println(FormRow_TextField(PARM_DEV_RULE_SUBJ , ntfyEdit  , i18n.getString("DeviceInfo.notifySubj"  ,"Notify Subject")+":"    , ntfySubj , 75, 90));
                            out.println(FormRow_TextArea( PARM_DEV_RULE_TEXT , ntfyEdit  , i18n.getString("DeviceInfo.notifyText"  ,"Notify Message")+":"    , ntfyText ,  5, 70));                            
                        }

                    }
                    if (Device.supportsActiveCorridor()) {
                        String actvGC   = (_selDev != null)? _selDev.getActiveCorridor() : "";
                        String gcTitle  = i18n.getString("DeviceInfo.activeCorridor","Active Corridor") + ":";
                        String gcList[] = Device.getCorridorIDsForAccount(reqState.getCurrentAccountID());
                        out.println(FormRow_Separator());
                        if (gcList != null) {
                            ComboMap gcCombo = new ComboMap(gcList);
                            gcCombo.insert("", i18n.getString("DeviceInfo.corridorNone","(none)"));
                            out.println(FormRow_ComboBox( PARM_ACTIVE_CORRIDOR, _uiEdit, gcTitle, actvGC, gcCombo, "", 40));
                        } else {
                            out.println(FormRow_TextField(PARM_ACTIVE_CORRIDOR, _uiEdit, gcTitle, actvGC, 40, 20));
                        }
                    }
                    
                    if (Device.supportsLinkURL()) {
                        out.println(FormRow_Separator());
                        out.println(FormRow_TextField(PARM_LINK_URL         , _uiEdit, i18n.getString("DeviceInfo.linkURL"        ,"Link URL")         +":" , (_selDev!=null)?String.valueOf(_selDev.getLinkURL()):""        , 40, 70));
                        out.println(FormRow_TextField(PARM_LINK_DESC        , _uiEdit, i18n.getString("DeviceInfo.linkDescription","Link Description") +":" , (_selDev!=null)?String.valueOf(_selDev.getLinkDescription()):"", 40, 24));
                    }
                    if (fixLocOK) {
                        out.println(FormRow_Separator());
                        out.println(FormRow_TextField(PARM_FIXED_LAT        , _uiEdit, i18n.getString("DeviceInfo.fixedLatitude" ,"Fixed Latitude")  +":"  , (_selDev!=null)?String.valueOf(_selDev.getFixedLatitude()) :"0.0",  40, 10));
                        out.println(FormRow_TextField(PARM_FIXED_LON        , _uiEdit, i18n.getString("DeviceInfo.fixedLongitude","Fixed Longitude") +":"  , (_selDev!=null)?String.valueOf(_selDev.getFixedLongitude()):"0.0", 40, 11));
                    }
                    if (SHOW_LAST_CONNECT) {
                        out.println(FormRow_Separator());
                        out.println(FormRow_TextField(PARM_DEV_LAST_CONNECT , false  , i18n.getString("DeviceInfo.lastConnect","Last Connect")+":" , lastConnectTime, 40, 30, i18n.getString("DeviceInfo.serverTime","(Server time)"))); // read-only
                        out.println(FormRow_TextField(PARM_DEV_LAST_EVENT   , false  , i18n.getString("DeviceInfo.lastEvent"  ,"Last Event"  )+":" , lastEventTime  , 40, 30, i18n.getString("DeviceInfo.deviceTime","(Device time)"))); // read-only
                    }
                    if (notesOK) {
                        String noteText = (_selDev!=null)? StringTools.decodeNewline(_selDev.getNotes()) : "";
                        out.println(FormRow_Separator());
                        out.println(FormRow_TextArea(PARM_DEV_NOTES, _uiEdit, i18n.getString("DeviceInfo.notes" ,"General Notes")+":", noteText, 5, 70));
                    }
                    if (!ListTools.isEmpty(customKeys)) {
                        out.println(FormRow_Separator());
                        for (String key : customKeys) {
                            String desc  = privLabel.getStringProperty(PrivateLabel.PROP_DeviceInfo_custom_ + key, key);
                            String value = (_selDev != null)? _selDev.getCustomAttribute(key) : "";
                            out.println(FormRow_TextField(PARM_DEV_CUSTOM_ + key, _uiEdit, desc + ":", value, 40, 50));
                        }
                    }
                    out.println(FormRow_Separator());
                   // String deviceID=    AttributeTools.getRequestString(request, "device", "");
                    String aloww="";
                   
                    if(btnXem.equals("Xem"))
                    {
                	   aloww="disabled"; 
                	   
                   }
                  
                   out.write(load_(deviceID,currAcct.toString(),aloww));
                    out.println("</table>");

                    /* DeviceGroup membership */
                    //minhnv
                    out.write("<table ><tr><td>");
                    out.write("<span style='margin-left:4px; margin-top:10px; font-weight:bold;'>");
                    out.write(  i18n.getString("DeviceInfo.groupMembership","{0} Membership:",grpTitles));
                    out.write(  "</span></td><td><span style='margin-left:4px; margin-top:10px; font-weight:bold;'>Ch&#x1ECD;n l&#x00E1;i xe:</span></td></tr><tr><td>\n");
                    out.write("<div style='border: 1px solid black; margin: 2px 20px 5px 10px; height:80px; width:400px; overflow-x: hidden; overflow-y: scroll; background-color:#ffffff'>\n");
                    out.write("<table style='color:#000000'>\n");
                    final OrderedSet<String> grpList = reqState.getDeviceGroupIDList(true);
                    for (int g = 0; g < grpList.size(); g++) {
                        String grp  = grpList.get(g);
                        String name = PARM_DEV_GROUP_ + grp;
                        String desc = reqState.getDeviceGroupDescription(grp,false/*!rtnDispName*/);
                        desc += desc.equals(grp)? ":" : (" ["+grp+"]:");
                        out.write("<tr><td>"+desc+"</td><td>");
                        if (grp.equalsIgnoreCase(DeviceGroup.DEVICE_GROUP_ALL)) {
                            out.write(Form_CheckBox(null,name,false,true,null,null));
                        } else {
                            boolean devInGroup = (_selDev != null)? 
                                DeviceGroup.isDeviceInDeviceGroup(_selDev.getAccountID(), grp, _selDevID) :
                                false;
                            out.write(Form_CheckBox(null,name,_uiEdit,devInGroup,null,null));
                        }
                        out.write("</td></tr>\n");
                    }
                    //minhnv start
                    HttpServletRequest request = reqState.getHttpServletRequest();                    
                    
                   // String deviceID=    AttributeTools.getRequestString(request, "device", "");
                                      
                    out.write("</table>\n");
                    out.write("</div></td><td><div style='border: 1px solid black; margin: 2px 20px 5px 10px; height:80px; width:400px; overflow-x: hidden; overflow-y: scroll;background-color:#ffffff'>"
                   +load_LaiXe(deviceID,currAcct.toString(),allow)+"</div></td></tr></table></t\n");                                     
                    
                    //minhnv end
                    /* end of form */
                    out.write("<hr style='margin-bottom:5px;'>\n");
                    out.write("<span style='padding-left:210px'>&nbsp;</span>\n");
                    if (_uiEdit) {
                        out.write("<input type='submit' name='"+PARM_SUBMIT_CHG+"' value='"+i18n.getString("DeviceInfo.change","Change")+"' class='button'>\n");
                        out.write("<span style='padding-left:10px'>&nbsp;</span>\n");
                        out.write("<input type='button' name='"+PARM_BUTTON_CANCEL+"' value='"+i18n.getString("DeviceInfo.cancel","Cancel")+"' onclick=\"javascript:openURL('"+editURL+"','_self');\"  class='button'>\n"); // target='_top'
                    } else {
                        out.write("<input type='button' name='"+PARM_BUTTON_BACK+"' value='"+i18n.getString("DeviceInfo.back","Back")+"' onclick=\"javascript:openURL('"+editURL+"','_self');\"  class='button'>\n"); // target='_top'
                    }
                    out.write("</form>\n");
                    
                }
            };

        }else
        	if(_search)
        	{
        		 final boolean _viewUniqID  = viewUniqID;
                 final boolean _viewServID  = viewServID;
                 final boolean _viewSIM     = viewSIM;
                 final boolean _viewActive  = viewActive;
               
                 HTML_CONTENT = new HTMLOutput(CommonServlet.CSS_CONTENT_FRAME, m) {
                     public void write(PrintWriter out) throws IOException {
                     	
                     	
                         String selectURL  = DeviceInfo.this.encodePageURL(reqState);//,RequestProperties.TRACK_BASE_URI());
                        // String refreshURL = DeviceInfo.this.encodePageURL(reqState); // TODO: add "refresh" page command
                         //String editURL    = DeviceInfo.this.encodePageURL(reqState);//,RequestProperties.TRACK_BASE_URI());
                         String newURL     = DeviceInfo.this.encodePageURL(reqState);//,RequestProperties.TRACK_BASE_URI());

                         /* show expected ACKs */
                         boolean showAcks = privLabel.getBooleanProperty(PrivateLabel.PROP_DeviceInfo_showExpectedAcks,false);
                        
                        String cbdieuhoa =AttributeTools.getRequestString(request, "ckDieuhoa", "0");	
                     	String cbcua =AttributeTools.getRequestString(request,"ckCua","0");
                     	String cbnd =AttributeTools.getRequestString(request,"ckND","0");
                     	String cbxang =AttributeTools.getRequestString(request,"ckXang","0");
                     	String ckDH="";
                     	String ckCua="";
                     	String ckND="";
                     	String ckXang="";
                     	if(cbdieuhoa.equals("1"))
                     		ckDH="checked";
                     	if(cbcua.equals("1"))
                     		ckCua="checked";
                     	if(cbnd.equals("1"))
                     		ckND="checked";
                     	if(cbxang.equals("1"))
                     		ckXang="checked";
                     	
                         // frame header
                         String frameTitle = _allowEdit? 
                             i18n.getString("DeviceInfo.viewEditDevice","View/Edit {0} Information", devTitles) : 
                             i18n.getString("DeviceInfo.viewDevice","View {0} Information", devTitles);
                         out.write("<span class='"+CommonServlet.CSS_MENU_TITLE+"'>"+frameTitle+"</span>&nbsp;\n");
                         //out.write("<a href='"+refreshURL+"'><span class=''>"+i18n.getString("DeviceInfo.refresh","Refresh")+"</a>\n");
                         out.write("<br/>\n");
                         out.write("<hr>\n");

                         // device selection table (Select, Device ID, Description, ...)
                         out.write("<h1 class='"+CommonServlet.CSS_ADMIN_SELECT_TITLE+"'>"+i18n.getString("DeviceInfo.selectDevice","Select a {0}",devTitles)+":</h1>\n");
                         out.write("<div>");
                         out.write("<form name='"+FORM_DEVICE_SELECT+"' method='post' action='"+selectURL+"' target='_self'>"); // target='_top'
                         out.write("<input type='hidden' name='"+PARM_COMMAND+"' value='"+COMMAND_INFO_SEL_DEVICE+"' />");
                         
                         
                         out.println("<table class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE+"' cellspacing='0' callpadding='0' border='0' width='350x' style='padding:15px 0 15px 15px'>\n");
                         
                         out.print  ("<tr style='height:40px;'>\n");
                         out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER+"' width='100px' >Điều hòa:</td>\n");
                         out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_DATA+"' width='20px'>\n");
                         out.print  ("<input type='checkbox' value='1' name='ckDieuhoa' "+ckDH+"  />");    
                         out.print  ("</td>");
                         out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER+"' width='50px' >Cửa:</td>\n");
                         out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_DATA+"' width='20px'>\n");
                         out.print  ("<input type='checkbox' value='1' name='ckCua' "+ckCua+"   />");    
                         out.print  ("</td>");
                         out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER+"' width='100px' >Nghị định 91:</td>\n");
                         out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_DATA+"' width='20px'>\n");
                         out.print  ("<input type='checkbox' value='1' name='ckND' "+ckND+"  />");    
                         out.print("</td>");     
                         out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_HEADER+"' width='100px' >Đo nhiên liệu:</td>\n");
                         out.print  ("<td class='"+CommonServlet.CSS_ADMIN_VIEW_TABLE_DATA+"' width='20px'>\n");
                         out.print  ("<input type='checkbox' value='1' name='ckXang' "+ckXang+"  />");    
                         out.print("</td>");
                         out.print("</tr>");
                         out.println("</table>");
                         
                         out.print("<div class='viewhoz'>");
                         out.println("<table style='padding-left:50px' >");
                         out.print("<tr >");
                         
                         out.write("<td style='padding-left:5px;'>");
                         out.write("<input type='submit' name='timkiem' value='Tìm kiếm' class='button1'>");
                         out.write("</td>\n"); 
                         if (_allowView) { 
                             out.write("<td style='padding-left:5px;'>");
                             out.write("<input type='submit' name='"+PARM_SUBMIT_VIEW+"' value='"+i18n.getString("DeviceInfo.view","View")+"'  class='button1'>");
                             out.write("</td>\n"); 
                         }
                         if (_allowEdit) { 
                             out.write("<td style='padding-left:5px;'>");
                             out.write("<input type='submit' name='"+PARM_SUBMIT_EDIT+"' value='"+i18n.getString("DeviceInfo.edit","Edit")+"'  class='button1'>");
                             out.write("</td>\n"); 
                         }
                         if (_allowDelete) {
                         	out.write("<td style='padding-left:5px;'>");
                             out.write("<input type='submit' name='"+PARM_SUBMIT_DEL +"' value='"+i18n.getString("DeviceInfo.delete","Delete")+"' "+Onclick_ConfirmDelete(locale)+"  class='button1'>");
                             out.write("</td>\n"); 
                         } else {
                             
                         }
                         
                         
                         out.println("</tr>");
                         
                         out.println("</table>");
                         out.print("</div>");
                         
                         String sql= FillterDevice( Integer.parseInt(cbdieuhoa), Integer.parseInt(cbcua), Integer.parseInt(cbnd),Integer.parseInt(cbxang),currAcct.getAccountID(),reqState,privLabel);
                         out.write(sql);
                    
                         out.write("</form>\n");
                         out.write("</div>\n");
                         out.write("<hr>\n");

                         /* new device */
                         if (_allowNew) {
                         String createText = i18n.getString("DeviceInfo.createNewDevice","Create a new device");
                         if (allowNewDevMode == NEWDEV_SYSADMIN) {
                             createText += " " + i18n.getString("DeviceInfo.sysadminOnly","(System Admin only)");
                         }
                         out.write("<h1 class='"+CommonServlet.CSS_ADMIN_SELECT_TITLE+"'>"+createText+":</h1>\n");
                         out.write("<div style='margin-top:5px; margin-left:5px; margin-bottom:5px;'>\n");
                         out.write("<form name='"+FORM_DEVICE_NEW+"' method='post' action='"+newURL+"' target='_self'>"); // target='_top'
                         out.write(" <input type='hidden' name='"+PARM_COMMAND+"' value='"+COMMAND_INFO_NEW_DEVICE+"'/>");
                         out.write(i18n.getString("DeviceInfo.deviceID","{0} ID",devTitles)+": <input type='text' class='"+CommonServlet.CSS_TEXT_INPUT+"' name='"+PARM_NEW_NAME+"' value='' size='32' maxlength='32'><br>\n");
                         out.write(" <input type='submit' name='"+PARM_SUBMIT_NEW+"' value='"+i18n.getString("DeviceInfo.new","New")+"' style='margin-top:5px; margin-left:10px;'  class='button'>\n");
                         out.write("</form>\n");
                         out.write("</div>\n");
                         out.write("<hr>\n");
                         }

                     }
                 };
        	}
        else
        if (_uiCmd) {

            final boolean _editProps = _allowCommand && (selDev != null);
            final DeviceCmdHandler _dcHandler = dcHandler; // non-null here
            HTML_CONTENT = new HTMLOutput(CommonServlet.CSS_CONTENT_FRAME, m) {
                public void write(PrintWriter out) throws IOException {

                    /* frame title */
                    String frameTitle = i18n.getString("DeviceInfo.setDeviceProperties","({0}) Set {1} Properties", 
                        _dcHandler.getServerDescription(), devTitles[0]);
                    out.write("<span class='"+CommonServlet.CSS_MENU_TITLE+"'>"+frameTitle+"</span><br/>\n");
                    out.write("<hr>\n");

                    /* Device Command/Properties content */
                    String editURL = DeviceInfo.this.encodePageURL(reqState);//, RequestProperties.TRACK_BASE_URI());
                    _dcHandler.writeCommandForm(out, reqState, _selDev, editURL, _editProps);

                }
            };
        } else
        if (_uiSms) {

            final boolean _editSmsCmd = _allowSmsCmd && (selDev != null);
            final DeviceCmdHandler _dcHandler = dcHandler; // non-null here
            HTML_CONTENT = new HTMLOutput(CommonServlet.CSS_CONTENT_FRAME, m) {
                public void write(PrintWriter out) throws IOException {

                    /* frame title */
                    String frameTitle = i18n.getString("DeviceInfo.setDeviceSMS","({0}) Send {1} SMS", 
                        _dcHandler.getServerDescription(), devTitles[0]);
                    out.write("<span class='"+CommonServlet.CSS_MENU_TITLE+"'>"+frameTitle+"</span><br/>\n");
                    out.write("<hr>\n");

                    /* Device Command/Properties content */
                    String editURL = DeviceInfo.this.encodePageURL(reqState);//, RequestProperties.TRACK_BASE_URI());
                    _dcHandler.writeCommandForm(out, reqState, _selDev, editURL, _editSmsCmd);

                }
            };
        }

        /* write frame */
        String onloadAlert = error? JS_alert(true,m) : null;
        String onload = (_uiCmd || _uiSms)? "javascript:devCommandOnLoad();" : onloadAlert;
        CommonServlet.writePageFrame(
            reqState,
            onload,null,                // onLoad/onUnload
            HTML_CSS,                   // Style sheets
            HTML_JS,                    // Javascript
            null,                       // Navigation
            HTML_CONTENT);              // Content
    }
    
    // ------------------------------------------------------------------------
}
