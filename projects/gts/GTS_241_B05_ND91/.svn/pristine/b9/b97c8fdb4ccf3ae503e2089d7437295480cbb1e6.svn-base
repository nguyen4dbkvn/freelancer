var xmlHttp;
var AutoUpdateAlert;
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
function LoadJob() {
	/*if(value_hid.indexOf(',')==0){
		value_hid = value_hid.substring(1);
	};*/
	GetXmlHttpOject();
	
	xmlHttp.onreadystatechange = function() {
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
			document.getElementById("alertEvents").innerHTML = '';
			document.getElementById("alertEvents").innerHTML = xmlHttp.responseText;
		}
	}	
	xmlHttp.open("GET", "Track?page=rpt.lastDistanceAjax", true);
	xmlHttp.send();
}
function stopAutoLoadAlert()
{
    if (AutoUpdateAlert != null) {
        clearInterval(AutoUpdateAlert); // clearTimeout
        AutoUpdateAlert = null;
    }
}

function startAutoLoadAlert()
{
	stopAutoLoadAlert();
	AutoUpdateAlert = setInterval('LoadJob()',2000); // setTimeout
}
