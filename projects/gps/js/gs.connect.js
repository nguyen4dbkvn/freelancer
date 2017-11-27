//#################################################
// VARS
//#################################################

// language array/vars
var la = [];

//#################################################
// END VARS
//#################################################

//#################################################
// NOTIFY/CONFIRM DIALOGS/POPUPS
//#################################################

function notifyDialog(text)
{
	document.getElementById('dialog_notify_text').innerHTML = text;
	
	$('#dialog_notify').dialog('open');
}

//#################################################
// END NOTIFY/CONFIRM DIALOGS/POPUPS
//#################################################

function getUrlVars()
{
	var vars = {};
	var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
		vars[key] = value;
	});
	return vars;
}

function initGui()
{
	// define dialogs
	$("#dialog_notify").dialog({
		autoOpen: false,
		width: "auto",
		height: "auto",
		minHeight: "auto",
		modal: true,
		resizable: false,
		draggable: false,
		dialogClass: 'dialog-notify-titlebar'
	});
	
	$(".custom-checkbox").click(function() {
		$(this).toggleClass('checked')
	});
	
	var img_file = 'img/login-background.jpg';
	
	if (fileExist(img_file))
	{
		document.getElementById('login').style.backgroundImage = "url('"+img_file+"')";
		document.getElementById('login').style.backgroundPosition = "center center";
		document.getElementById('login').style.backgroundSize = "cover";
	}
}

function connectLoad()
{
	loadLanguage(function(response){});
	
	initGui();
	
	// set language selectbox
	var cookie = getCookie("gs_language");
	if (cookie!=null && cookie!="")
  	{
		document.getElementById("system_language").value = cookie;
	}
	
	if (getUrlVars()["cmd"] == "recover")
	{
		if (getUrlVars()['token'] != undefined)
		{
			var token = getUrlVars()['token'];
			connectRecover(token);
                }
	}
	
	document.getElementById("username").focus();
}

function connectServer()
{
	var server = document.getElementById("server").value;
	window.open (server,'_self',false);
}

function connectLogin()
{
	var username = document.getElementById("username").value;
	var password = document.getElementById("password").value;
	var remember_me = document.getElementById("remember_me").checked;
	
	if ((username == '') || (password == ''))
	{
		return;
	}
	
	var data = {
		cmd: 'login',
		username: username,
		password: password,
		remember_me: remember_me,
		mobile: 'false'
	};
	    
	jQuery.ajax({
		type: "POST",
		url: "func/fn_connect.php",
		data: data,
		success: function(result)
		{
			if (result == 'LOGIN_TRACKING')
			{
				window.open ('tracking.php','_self',false);
			}
			else if (result == 'LOGIN_CPANEL')
			{
				window.open ('cpanel.php','_self',false);
			}
			else if (result == 'ERROR_USERNAME_PASSWORD_INCORRECT')
			{
				notifyDialog(la['USERNAME_OR_PASSWORD_INCORRECT']);
			}
			else if (result == 'ERROR_ACCOUNT_LOCKED')
			{
				notifyDialog(la['THIS_ACCOUNT_IS_LOCKED']);
			}
		}
	});
}

function connectRecoverURL()
{
	var email = document.getElementById("rec_email").value;
	var seccode = document.getElementById("rec_seccode").value;
	
	if(!isEmailValid(email))
	{
		notifyDialog(la['THIS_EMAIL_IS_NOT_VALID']);
		return;
	}
	
	var data = {
		cmd: 'recover_url',
		email: email,
		seccode: seccode
	};
	
	jQuery.ajax({
		type: "POST",
		url: "func/fn_connect.php",
		data: data,
		success: function(result)
		{
			if (result == 'OK')
			{
                                notifyDialog(la['RECOVERY_LINK_SENT'] + ' ' + la['PLEASE_CHECK_YOUR_EMAIL']);
                        }
			else if (result == 'ERROR_NOT_SENT')
			{
                                notifyDialog(la['CANT_SEND_EMAIL'] + ' ' + la['CONTACT_ADMINISTRATOR']);
                        }
			else if (result == 'ERROR_EMAIL_NOT_FOUND')
			{
                                notifyDialog(la['THIS_EMAIL_IS_NOT_REGISTERED']);
                        }
			else if (result == 'ERROR_INCORRECT_SECURITY_CODE')
			{
                                notifyDialog(la['SECURITY_CODE_IS_INCORRECT']);
                        }
		}
	});
}

function connectRecover(token)
{
	var data = {
		cmd: 'recover',
		token: token
	};
	
	jQuery.ajax({
		type: "POST",
		url: "func/fn_connect.php",
		data: data,
		success: function(result)
		{
		    	if (result == 'OK')
			{
                                notifyDialog(la['USERNAME_PASSWORD_SENT'] + ' ' + la['PLEASE_CHECK_YOUR_EMAIL']);
                        }
			else if (result == 'ERROR_NOT_SENT')
			{
				notifyDialog(la['CANT_SEND_EMAIL'] + ' ' + la['CONTACT_ADMINISTRATOR']);
			}
			else if (result == 'ERROR_EMAIL_NOT_FOUND')
			{
                                notifyDialog(la['THIS_EMAIL_IS_NOT_REGISTERED']);
                        }
			else if (result == 'ERROR_RECOVER_EXPIRED')
			{
                                notifyDialog(la['RECOVERY_LINK_EXPIRED']);
                        }
		}
	});
}

function connectRegister()
{
	var email = document.getElementById("reg_email").value;
	var seccode = document.getElementById("reg_seccode").value;
	
	if(!isEmailValid(email))
	{
		notifyDialog(la['THIS_EMAIL_IS_NOT_VALID']);
		return;
	}
	
	var data = {
		cmd: 'register',
		email: email,
		seccode: seccode
	};
	
	jQuery.ajax({
		type: "POST",
		url: "func/fn_connect.php",
		data: data,
		success: function(result)
		{
			if (result == 'OK')
			{
                                notifyDialog(la['REGISTRATION_SUCCESSFUL'] + ' ' + la['PLEASE_CHECK_YOUR_EMAIL']);
                        }
			else if (result == 'ERROR_EMAIL_EXISTS')
			{
				notifyDialog(la['THIS_EMAIL_ALREADY_EXISTS']);
			}
			else if (result == 'ERROR_NOT_SENT')
			{
				notifyDialog(la['CANT_SEND_EMAIL'] + ' ' + la['CONTACT_ADMINISTRATOR']);
			}
			else if (result == 'ERROR_INCORRECT_SECURITY_CODE')
			{
                                notifyDialog(la['SECURITY_CODE_IS_INCORRECT']);
                        }
		}
	});
}

function connectLogout()
{
	var data = {
		cmd: 'logout'
	};
	    
	jQuery.ajax({
		type: "POST",
		url: "func/fn_connect.php",
		data: data,
		success: function(result)
		{
			window.open (result,'_self',false);
		}
	});
}