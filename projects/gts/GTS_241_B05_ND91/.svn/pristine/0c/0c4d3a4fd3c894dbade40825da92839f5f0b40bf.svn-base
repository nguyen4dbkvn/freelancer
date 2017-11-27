var xmlHttp;
function GetXmlHttpOject() {
	try {
		// Firefox, Opera, Safari
		xmlHttp = new XMLHttpRequest();

	} catch (e) {
		// Internet Exploer
		try {
			xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
	}
	return xmlHttp;
}
function LoadDeviceByAccount(accid) {
	GetXmlHttpOject();
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			document.getElementById("divDevice").innerHTML = '';
			document.getElementById("divDevice").innerHTML = xmlHttp.responseText;
			$( "#device" ).combobox();
		}
	}
	xmlHttp.open("POST", "Track?page=menu.GetDeviceAjax&accid=" + accid);
	xmlHttp.send(null);
}

function RequestInsertFuelDevice()
{
	var accid=document.getElementById('hidAccount').value;
	var deviceid=document.getElementById('hidDevice').value;
	var url="Track?page=menu.rpt.InsertDeviceFuel&accid="+accid+"&deviceid="+deviceid;
	openResizableWindow(url,'InsertFuelDevice',900,900);
}