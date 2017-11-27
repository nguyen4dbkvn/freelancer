package org.opengts.war.track.page;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opengts.db.tables.CheckSIM;
import org.opengts.dbtools.DBCamera;
import org.opengts.util.DateTime;

@WebServlet("/AjaxCheckSMS")
public class AjaxCheckSMS extends HttpServlet {

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String time = request.getParameter("time").trim();
		String account = request.getParameter("account").trim();
		String user = request.getParameter("user").trim();
		String device = request.getParameter("device").trim();
		String check_sim = request.getParameter("check_sim").trim();
		String date_tz = request.getParameter("date_tz").trim();

		String strscr = "false";

		if (time != null || !"".equals(time)) {
			try {
				DBCamera objcmr = new DBCamera();

				CheckSIM ed = objcmr.StaticInforlQLSIMSMS(account, device,
						date_tz, check_sim, time);

				if (ed != null)
				{	/*check = "<h3>Hệ thống đã cập nhật thông tin thành công. <a href='./Track?page=menu.rpt.qlSim&account="
							+ account
							+ "&user="
							+ user
							+ "&check_sim="
							+ check_sim
							+ "&device="
							+ device
							+ "&fmt=map&time="
							+ time
							+ "&date_tz=GMT%2b07%3a00&r_report=EventDetail' > Nhấn vào đây </a>, hoặc bấm Xem để xem thông tin.</h3>";*/
					String stringType=" lưu lượng ";
		    		if(Integer.parseInt(check_sim, 16)==2)
		    			stringType=" tài khoản ";
		    		String day = ConvertFromEpoch( ed.gettimestamp(),date_tz);
		    		
				strscr ="<div class='detailQLSIM' style='padding:10px;'><div class='item-data'><h3>Thông tin quản lý SIM thiết bị của xe "+ed.getdeviceID()+"</h3></div><hr/>";    		
				strscr +="<div class='item-data'><label style='display:inline-block;text-align: right; padding: 5px; font-weight:bold;width:150px'>Biển số xe: </label><label>"+ed.getdeviceID()+"</label></div>";
				strscr +="<div class='item-data'><label style='display:inline-block;text-align: right;padding: 5px; font-weight:bold;width:150px'>IMEI: </label><label>"+ed.getimei()+"</label></div>";
				strscr +="<div class='item-data'><label style='display:inline-block;text-align: right; padding: 5px; font-weight:bold;width:150px'>Số SIM thiết bị: </label><label>"+ed.getsimPhoneNumber()+"</label></div>";				
				strscr +="<div class='item-data'><label style='display:inline-block;text-align: right; padding: 5px; font-weight:bold;width:150px'>Thông tin"+stringType+": </label><label>"+ed.getcheckThongtin()+"</label></div>";
				strscr +="<div class='item-data'><label style='display:inline-block;text-align: right; padding: 5px; font-weight:bold;width:150px'>Thời điểm: </label><label>"+	day +"</label></div>";
				strscr +="</div><hr/><div style='padding: 10px; font-weight:bold;line-height:20px;'><i>Lưu ý: Bạn phải nạp tiền cho SIM thiết bị nếu Tài Khoản Chính < 10.000d <br/>và Tài Khoản Lưu Lượng(LLKM1) < 500Kb tính đến ngày 25 hàng tháng.</i></div>";
				strscr +="<div style='padding: 10px; font-weight:bold;line-height:20px;'><button type='submit' style='padding:7px;font-weight:bold;' name='nap_tien'>Nạp tiền</button></div>";
				}
			} catch (Exception e) {
			}
		}
		
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(strscr);
	}
	private String ConvertFromEpoch(long epoch, String timezone){
	  	  TimeZone tz = DateTime.getTimeZone(timezone, null);
	  	  DateTime dt = new DateTime(epoch);
	  	        String dtFmt = dt.format("dd/MM/yyyy HH:mm:ss", tz);
	  	  return dtFmt;
	  	 }	
	 
}
