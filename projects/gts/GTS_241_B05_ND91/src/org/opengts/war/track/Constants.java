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
// ----------------------------------------------------------------------------
package org.opengts.war.track;

import java.util.*;

public interface Constants
{
 
    public  static final String FORM_MONTH_CHG              = "MonthChgForm";
    public  static final String FORM_COMMAND                = "CommandForm";
    public  static final String FORM_COMMAND_CSV            = "CommandFormCSV";

    // ------------------------------------------------------------------------
    // URI base address

    public  static final String DEFAULT_BASE_URI            = "/Track";
    public  static final String _DEFAULT_BASE_URI           = "." + DEFAULT_BASE_URI;

    public  static final String DEFAULT_ATTACH_URI          = "/Attach";

    public  static final String DEFAULT_MARKER_URI          = "/Marker";
 
    public  static final String PARM_REQSTATE               = "$REQSTATE";

    public  static final String PARM_USEREMAIL              = "userEmail";

    public  static final String PARM_ACCOUNT                = "account";
    public  static final String PARM_USER                   = "user";
    public  static final String PARM_PASSWORD               = "password";
    public  static final String PARM_ENCPASS                = "encpass";
    public  static final String PARM_RESTOREPW              = "restorePWD";
    public  static final String PARM_LOCALE                 = "locale";

    public  static final String PARM_REGION                 = "region";

    public  static final String PARM_ACCOUNT_A[]            = new String[] { PARM_ACCOUNT , "act" };
    public  static final String PARM_USER_A[]               = new String[] { PARM_USER    , "usr" };
    public  static final String PARM_PASSWORD_A[]           = new String[] { PARM_PASSWORD, "pwd" };

    public  static final String PARM_DEVICE                 = "device";
    public  static final String PARM_GROUP                  = "group";
    
    public  static final String PARM_MAP_LIMIT              = "limit";      // map limit
    public  static final String PARM_MAP_LIMIT_TYPE         = "limType";    // map limit type

    public  static final String PARM_DEVICE_COMMAND         = "devcmd";

    public  static final String PARM_RULE                   = "rule";
    
    public  static final String PARM_SYSADMIN_RELOGIN       = "saLogin";

    // ------------------------------------------------------------------------
    // Geocode cached vars
    
    public  static final String LAST_GEOCODE_CACHE          = "LastGeocodeRequest";
    public  static final String LAST_GEOCODE_ADDRESS        = "LastGeocodeRequest.address";
    public  static final String LAST_GEOCODE_LATITUDE       = "LastGeocodeRequest.latitude";
    public  static final String LAST_GEOCODE_LONGITUDE      = "LastGeocodeRequest.longitude";

    public  static final String LAST_REVERSEGEOCODE         = "LastReverseGeocodeRequest";

    // ------------------------------------------------------------------------
    // Page definitions [PARM_PAGE argument values]

    public  static final String PAGE_LOGIN                  = "login";                  // login page
    public  static final String PAGE_OFFLINE                = "offline";                // offline page

    public  static final String PAGE_MENU_TOP               = "menu.top";               // Top menu

    public  static final String PAGE_ACCOUNT_NEW            = "acct.new";               // new account
    public  static final String PAGE_ACCOUNT_INFO           = "acct.info";              // Account information

    public  static final String PAGE_USER_INFO              = "user.info";              // User information

    public  static final String PAGE_ROLE_INFO              = "role.info";              // Role information

    public  static final String PAGE_CODE_INFO              = "code.info";              // StatusCode information

    public  static final String PAGE_DRIVER_INFO            = "driver.info";            // Driver information

    public  static final String PAGE_DEVICE_INFO            = "dev.info";    
     
    public  static final String PAGE_JOBDETAILS             = "jobdetails.info";    //Hienntd Add Camera
    public  static final String PAGE_Vehicles               = "Vehicles.info";
    public  static final String PAGE_PICTURE            	= "picture.info";  			//Hienntd Add Camera
    public  static final String PAGE_DEVICE_PROPS           = "dev.props";              // Device properties

    public  static final String PAGE_GROUP_INFO             = "group.info";             // DeviceGroup information
    public  static final String PAGE_GROUP_INFO1            = "group.info1";             // Lienptk
    public  static final String PAGE_DEVICE_ALERTS          = "dev.alerts";             // Device alerts
    public  static final String PAGE_ALERT_PANEL            = "alert.panel";            // Alert Panel

    public  static final String PAGE_MENU_REPORT            = "menu.rpt";               // Report menu
    public  static final String PAGE_MENU_RPT_DEVDETAIL     = "menu.rpt.devDetail";     // "device.detail"
    public  static final String PAGE_MENU_RPT_QLSIM     = "menu.rpt.qlSim";     // "device.detail"
    public  static final String PAGE_MENU_RPT_BAOCAOTONGHOPTHEOXE   = "menu.rpt.BaoCaoTongHopTheoXe"; // Hienntd add
    public  static final String PAGE_MENU_RPT_BAOCAOTONGHOPTHEOXETRUOC   = "menu.rpt.BaoCaoTongHopTheoXeTruoc"; // Hienntd add
    public  static final String PAGE_MENU_RPT_BAOCAOTONGHOPTHEOLAIXE   = "menu.rpt.BaoCaoTongHopTheoLaiXe"; // Hienntd add
    public  static final String PAGE_MENU_RPT_BAOCAOTONGHOPTHEOLAIXETRUOC   = "menu.rpt.BaoCaoTongHopTheoLaiXeTruoc"; // Hienntd add
    public  static final String PAGE_MENU_RPT_OVERSPEED     = "menu.rpt.Baocaoquatocdo"; // Hienntd add
    public  static final String PAGE_MENU_RPT_DRIVINGTIME   = "menu.rpt.Baocaotglaixe"; // Hienntd add
    public  static final String PAGE_MENU_RPT_DOXANG  		= "menu.rpt.BaocaoDoxang"; // created by hiendinhngoc 26-09
    public  static final String PAGE_MENU_RPT_FUEL		    = "menu.rpt.menu.rpt.reportFuelByAcc";     		// hienntd add
    public  static final String PAGE_MENU_RPT_FUEL_DATE		    = "menu.rpt.menu.rpt.reportFuelDate";     		// hienntd add
    public  static final String PAGE_MENU_GETDEVICE_AJAX	= "menu.GetDeviceAjax";    	// hienntd add
    public  static final String PAGE_INSERT_DEVICE_FULE		= "menu.rpt.InsertDeviceFuel";     		// hienntd add
    public  static final String PAGE_MENU_RPT_GRPSUMMRY     = "menu.rpt.grpSummary";    // "fleet.summary"
    public  static final String PAGE_MENU_RPT_GRPDETAIL     = "menu.rpt.grpDetail";     // "fleet.detail"
    public  static final String PAGE_MENU_RPT_PERFORM       = "menu.rpt.devPerf";       // "device.performance"
    public  static final String PAGE_MENU_RPT_IFTA          = "menu.rpt.iftaDetail";    // "ifta.detail"
    public  static final String PAGE_MENU_RPT_SYSADMIN      = "menu.rpt.sysSummary";    // "sysadmin.summary"
    public  static final String PAGE_REPORT_ZONE_BY_DAY		="menu.rpt.reportZoneByDay";
    public  static final String PAGE_TONGHOPDOIXE			="menu.rpt.tongHopDoiXe";
    public  static final String PAGE_BAOCAODAU		    	="menu.rpt.baoCaoDau";
    public  static final String PAGE_BAOCAOTRAM 			="menu.rpt.baoCaoTram";
    public  static final String PAGE_VUOTTOCDO   			="menu.rpt.vuotTocDo";      // Lienptk them
    public  static final String PAGE_THIET_BI_CHUA_DUOC_GAN	="menu.rpt.thietBiChuaGan"; //Lienptk them   
    public  static final String PAGE_BAOCAODONGMO  			="menu.rpt.dongmo";
    public  static final String PAGE_BAOCAODONGMOCUA  		="menu.rpt.dongmocua";
    public  static final String PAGE_BAOCAODUNGDO  			="menu.rpt.dungdo";
    public  static final String PAGE_BAOCAOBATTAT  			="menu.rpt.baocaobattatcancau";
    public  static final String PAGE_REPORT					="menu.rpt.Report";
    public  static final String PAGE_GEOZONEARRIVAL 		="menu.rpt.GeoZoneArrival";
    public  static final String PAGE_CAMERA                 ="menu.rpt.Camera";
    public  static final String PAGE_NEWFUEL                ="menu.rpt.fuel";
    public 	static final String PAGE_NEWFUELTG              ="menu.rpt.fueltg";			// created by hiendinhngoc
    public  static final String PAGE_JOBS_MANAGER     		="menu.jobsManager";
    public 	static final String PAGE_LAI_XE_HIEN_TAI		= "menu.rpt.LaiXeHienTai";		// MinhNV them
    public 	static final String PAGE_CHI_TIET_LAI_XE		= "menu.rpt.chiTietLaiXe";			// MinhNV them
    public 	static final String PAGE_HANH_TRINH_CUA_XE		= "menu.rpt.HanhTrinhCuaXe";		// MinhNV them
    public  static final String PAGE_BAOCAONHIENLIEU  		="menu.rpt.nhienlieu";          // Minhnv them
    public 	static final String PAGE_BAO_CAO_NGAY   		= "menu.rpt.baoCaoNgay";		// MinhNV them
    public 	static final String PAGE_BAO_CAO_TONG_HOP   	= "menu.rpt.baoCaoTongHop";		// MinhNV them
    public 	static final String PAGE_BCTH_TRUOC   			= "menu.rpt.baoCaoTongHopTruoc";		// MinhNV them
    public  static final String PAGE_BAOCAOHOATDONGXETHEONGAY = "menu.rpt.BaoCaoHoatDongXeTheoNgay"; // Mr viet add
    
    public 	static final String PAGE_REPORTFUEL          	= "menu.rpt.reportFuel";		// báo cáo nhiên liệu Lienptk 
    public  static final String PAGE_REPORT_SHOW            = "report.show";            // Report display
    public  static final String PAGE_REPORT_MAYXUC_LIVE     ="menu.rpt.MayXucLiveReport";
    public  static final String PAGE_J1587_SHOW             = "j1587.show";             // J1587 description display

    public  static final String PAGE_FUELCONSUMPTION       	= "Fuel.Consumption";		// Fuel's consumption based on running time
    public  static final String PAGE_FUELCONSUMPTION2       = "Fuel.Consumptionbtck";		// Fuel's consumption based on running time

    public  static final String PAGE_FUELCONSUMPTION232    	= "Fuel.Consumption232";	// Fuel's consumption based on key status
    public  static final String PAGE_PASSWD                 = "passwd";                 // Change password
    public  static final String PAGE_PASSWD_EMAIL           = "passwd.email";           // Forgot password

    public  static final String PAGE_MAP_DEVICE             = "map.device";             // GPS map tracking
    public  static final String PAGE_MAP_FLEET              = "map.fleet";              // GPS map tracking

    public  static final String PAGE_ZONE_INFO              = "zone.info";              // Geozone information

    public  static final String PAGE_WORKZONE_INFO          = "workZone.info";          // WorkZone information
    public  static final String PAGE_WORKORDER_INFO         = "workOrder.info";         // WorkOrder information

    public  static final String PAGE_CORRIDOR_INFO          = "corridor.info";          // GeoCorridor information
    
    public  static final String PAGE_LAF_INFO               = "laf.info";               // Look-and-Feel information

    public  static final String PAGE_SYSADMIN_INFO          = "sysAdmin.info";          // System Administration Information
    public  static final String PAGE_SYSADMIN_ACCOUNTS      = "sysAdmin.accounts";      // System Administration Account
    public  static final String PAGE_SYSADMIN_DEVICES       = "sysAdmin.devices";       // System Administration Devices

    public  static final String PAGE_ENTITY_INFO            = "entity.admin";           // Entity Admin

    public  static final String PAGE_RULE_INFO              = "rule.info";              // Rule info

    public  static final String PAGE_HTML_WRAP              = "htmlWrapper";            // HTML wrapper
   
    public  static final String COMMAND_LOGOUT              = "logout";                 // arg=YYYY/MM

    // ------------------------------------------------------------------------
    public 	static final String PAGE_LAST_DISTANCE			= "rpt.lastDistance";
    public 	static final String PAGE_LAST_DISTANCE_AJAX		= "rpt.lastDistanceAjax";
}
