	<div id="loading_panel">
		<div class="table">
			<div class="table-cell center-middle">
				<div id="loading_panel_text">
					<div class="row">
						<img class="logo" src="<? echo $gsValues['URL_ROOT'].'/img/'.$gsValues['LOGO']; ?>" />
					</div>
					<div class="row">
						<div class="loader">
							<span></span><span></span><span></span><span></span><span></span><span></span><span></span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div id="loading_data_panel" style="display: none;">
		<div class="table">
			<div class="table-cell center-middle">
				<div class="loader">
					<span></span><span></span><span></span><span></span><span></span><span></span><span></span>
				</div>
			</div>
		</div>
	</div>
		
	<div id="blocking_panel">
		<div class="table">
			<div class="table-cell center-middle">
				<div id="blocking_panel_text">
					<div class="row">
						<img class="logo" src="<? echo $gsValues['URL_ROOT'].'/img/'.$gsValues['LOGO']; ?>" />
					</div>
					<? echo sprintf($la['SESSION_HAS_EXPIRED'], $gsValues['URL_LOGIN']); ?>	
				</div>
			</div>
		</div>
	</div>
	
	<div id="top_panel">
		<ul class="left-menu">
			<li class="map-btn">
				<a title="<? echo $la['MAP']; ?>" href="tracking.php">
					<img src="theme/images/globe.svg" />
				</a>
			</li>
			
			<? if (($_SESSION["cpanel_privileges"] == 'super_admin') || ($_SESSION["cpanel_privileges"] == 'admin')) { ?>
			<li class="select-view">
				<select id="cpanel_manager_list" onchange="switchCPManager(this.value);"/></select>
			</li>
			<? } ?>
			<li>
				<a title="<? echo $la['USER_LIST']; ?>" class="user-list-btn active" id="top_panel_button_user_list" href="#" onClick="switchCPTab('user_list');">
					<img src="theme/images/user.svg" />
					<span id="user_list_stats"></span>
				</a>
			</li>
			<li>
				<a title="<? echo $la['OBJECT_LIST']; ?>" class="object-list-btn" id="top_panel_button_object_list" href="#" onClick="switchCPTab('object_list');">
					<img src="theme/images/marker.svg" />
					<span id="object_list_stats"></span>
				</a>
			</li>
			<? if (($_SESSION["cpanel_privileges"] == 'super_admin') || ($_SESSION["cpanel_privileges"] == 'admin')) { ?>
			<li>
				<a title="<? echo $la['UNUSED_OBJECT_LIST']; ?>" class="unused-object-list-btn" id="top_panel_button_unused_object_list" href="#" onClick="switchCPTab('unused_object_list');">
				<img src="theme/images/marker-crossed.svg" />
				<span id="unused_object_list_stats"></span>
				</a>
			</li>
			<? } ?>
			<? if ($_SESSION["billing"] == true) { ?>
			<li>
				<a title="<? echo $la['BILLING_PLAN_LIST']; ?>" class="billing-plan-list-btn" id="top_panel_button_billing_plan_list" href="#" onClick="switchCPTab('billing_plan_list');">
				<img src="theme/images/billing.svg" />
				<span id="billing_plan_list_stats"></span>
				</a>
			</li>
			<? } ?>
			<? if ($_SESSION["cpanel_privileges"] == 'super_admin') { ?>
			<li>
				<a title="<? echo $la['MANAGE_SERVER']; ?>" class="manage-server-btn" id="top_panel_button_manage_server" href="#" onClick="switchCPTab('manage_server');">
					<img src="theme/images/settings.svg" />
				</a>
			</li>
			<? } ?>
		</ul>
		
		<ul class="right-menu">
			<li class="select-language"><select id="system_language" onChange="switchLanguageCPanel();"><? echo getLanguageList(); ?></select></li>
			<li>
				<a class="user-btn" href="#" onclick="userEdit('<? echo $_SESSION["user_id"]; ?>');" title="<? echo $la['MY_ACCOUNT']; ?>">
					<img src="theme/images/user.svg" border="0"/>
					<span class="user-btn-text"><? echo $_SESSION["username"];?></span>
				</a>
			</li>
			<li class="logout-btn">
				<a title="<? echo $la['LOGOUT']; ?>" href="#" onclick="connectLogout();">
					<img src="theme/images/logout.svg" />
				</a>
			</li>
		</ul>
	</div>
	
	<div id="cpanel_user_list">
		<div class="float-left cpanel-title">
			<div class="version">v<? echo $gsValues['VERSION']; ?></div>
			<h1 class="title"><? echo $la['CONTROL_PANEL']; ?> <span> - <? echo $la['USER_LIST']; ?></span></h1>
		</div>
		<table id="cpanel_user_list_grid"></table>
		<div id="cpanel_user_list_grid_pager"></div>
	</div>
	
	<div id="cpanel_object_list" style="display:none;">
		<div class="float-left cpanel-title">
			<div class="version">v<? echo $gsValues['VERSION']; ?></div>
			<h1 class="title"><? echo $la['CONTROL_PANEL']; ?> <span> - <? echo $la['OBJECT_LIST']; ?></span></h1>
		</div>	
		<table id="cpanel_object_list_grid"></table>
		<div id="cpanel_object_list_grid_pager"></div>
	</div>
	
	<? if (($_SESSION["cpanel_privileges"] == 'super_admin') || ($_SESSION["cpanel_privileges"] == 'admin')) { ?>
	<div id="cpanel_unused_object_list" style="display:none;">
		<div class="float-left cpanel-title">
			<div class="version">v<? echo $gsValues['VERSION']; ?></div>
			<h1 class="title"><? echo $la['CONTROL_PANEL']; ?> <span> - <? echo $la['UNUSED_OBJECT_LIST']; ?></span></h1>
		</div>	
		<table id="cpanel_unused_object_list_grid"></table>
		<div id="cpanel_unused_object_list_grid_pager"></div>
	</div>
	<? } ?>
	
	<? if ($_SESSION["billing"] == true) {?>
	<div id="cpanel_billing_plan_list" style="display:none;">
		<div class="float-left cpanel-title">
			<div class="version">v<? echo $gsValues['VERSION']; ?></div>
			<h1 class="title"><? echo $la['CONTROL_PANEL']; ?> <span> - <? echo $la['BILLING_PLAN_LIST']; ?></span></h1>
		</div>	
		<table id="cpanel_billing_plan_list_grid"></table>
		<div id="cpanel_billing_plan_list_grid_pager"></div>
	</div>
	<? } ?>