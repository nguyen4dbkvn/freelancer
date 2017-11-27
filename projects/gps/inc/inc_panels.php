<div id="map"></div>

<div class="map-layer-control">
	<div class="row4">
		<select id="map_layer" onChange="switchMapLayer($(this).val());"></select>
	</div>
</div>

<div id="history_view_control" class="history-view-control">
	<div class="row4">
		<div class="margin-right-3"><input id="history_view_control_route" type="checkbox" class="checkbox" onclick="historyRouteToggleRoute();" checked/></div>
		<div class="margin-right-3"><? echo $la['ROUTE']; ?></div>
		<div class="margin-right-3"><input id="history_view_control_snap" type="checkbox" class="checkbox" onclick="historyRouteToggleSnap();"/></div>
		<div class="margin-right-3"><? echo $la['SNAP']; ?></div>
		<div class="margin-right-3"><input id="history_view_control_arrows" type="checkbox" class="checkbox" onclick="historyRouteToggleArrows();"/></div>
		<div class="margin-right-3"><? echo $la['ARROWS']; ?></div>
		<div class="margin-right-3"><input id="history_view_control_data_points" type="checkbox" class="checkbox" onclick="historyRouteToggleDataPoints();"/></div>
		<div class="margin-right-3"><? echo $la['DATA_POINTS']; ?></div>
		<div class="margin-right-3"><input id="history_view_control_stops" type="checkbox" class="checkbox" onclick="historyRouteToggleStops();" checked/></div>
		<div class="margin-right-3"><? echo $la['STOPS']; ?></div>
		<div class="margin-right-3"><input id="history_view_control_events" type="checkbox" class="checkbox" onclick="historyRouteToggleEvents();" checked/></div>
		<div class="margin-right-3"><? echo $la['EVENTS']; ?></div>
		<div class="margin-left-3">
			<a class="button icon-close" href="#" onclick="historyHideRoute();" title="<? echo $la['HIDE'];?>">&nbsp;</a>
		</div>
	</div>
</div>

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
		<li class="about-btn">
			<a href="#" onclick="$('#dialog_about').dialog('open');" title="<? echo $la['ABOUT']; ?>">
				<img src="<? echo $gsValues['URL_ROOT'].'/img/'.$gsValues['LOGO_SMALL']; ?>" border="0"/>
			</a>
		</li>
		<li>
			<a class="help_btn" href="<? echo $gsValues['URL_HELP']; ?>" target="_blank" title="<? echo $la['HELP']; ?>">
				<img src="theme/images/info.svg" border="0"/>
			</a>
		</li>
		<li>
			<a class="settings_btn" href="#" onclick="settingsOpen();" title="<? echo $la['SETTINGS']; ?>">
				<img src="theme/images/settings.svg" border="0"/>
			</a>
		</li>
		<li>
			<a class="point_btn" href="#" onclick="$('#dialog_show_point').dialog('open');" title="<? echo $la['SHOW_POINT']; ?>">
				<img src="theme/images/marker.svg" border="0"/>
			</a>
		</li>
		<li>
			<a class="search_btn" href="#" onclick="$('#dialog_address_search').dialog('open');" title="<? echo $la['ADDRESS_SEARCH']; ?>">
				<img src="theme/images/search.svg" border="0"/>
			</a>
		</li>
		<? if ($_SESSION["privileges_reports"] == true){?>
		<li>
			<a class="report_btn" href="#" onclick="reportsOpen();" title="<? echo $la['REPORTS']; ?>">
				<img src="theme/images/report.svg" border="0"/>
			</a>
		</li>
		<? } ?>
		<li>
			<a class="report_31_btn" href="#" onclick="rs31Open()" title="<? echo $la['REPORTS_OF_STANDARD_312014']?>">
				<img src="theme/images/export.svg" border="0"/>
			</a>
		</li>
		<? if ($_SESSION["privileges_rilogbook"] == true){?>
		<li>
			<a class="rilogbook_btn" href="#" onclick="rilogbookOpen();" title="<? echo $la['RFID_AND_IBUTTON_LOGBOOK']; ?>">
				<img src="theme/images/logbook.svg" border="0"/>
			</a>
		</li>
		<? } ?>
		<? if ($_SESSION["privileges_dtc"] == true){?>
		<li>
			<a class="dtc_btn" href="#" onclick="dtcOpen();" title="<? echo $la['DIAGNOSTIC_TROUBLE_CODES']; ?>">
				<img src="theme/images/dtc.svg" border="0"/>
			</a>
		</li>
		<? } ?>
		<? if ($_SESSION["privileges_object_control"] == true){?>
		<li>
			<a class="cmd_btn" href="#" onclick="cmdOpen();" title="<? echo $la['OBJECT_CONTROL']; ?>">
				<img src="theme/images/cmd.svg" border="0"/>
			</a>
		</li>
		<? } ?>
		<? if ($_SESSION["privileges_image_gallery"] == true){?>
		<li>
			<a class="gallery_btn" href="#" onclick="imgOpen();" title="<? echo $la['IMAGE_GALLERY']; ?>">
				<img src="theme/images/gallery.svg" border="0"/>
			</a>
		</li>
		<? } ?>
		<? if ($_SESSION["privileges_chat"] == true){?>
		<li>
			<a class="chat_btn" href="#" onclick="chatOpen();" title="<? echo $la['CHAT']; ?>">
				<img class="float-left" src="theme/images/chat.svg" border="0"/>
				<span id="chat_msg_count" class="chat-msg-count float-right">0</span>
			</a>
		</li>
		<? } ?>
	</ul>

	<ul class="right-menu">
		<li class="select-language <? if ($_SESSION["cpanel_privileges"]){?>cp<? }?>">
			<select id="system_language" onChange="switchLanguageTracking();">
			<? echo getLanguageList(); ?>
			</select>
		</li>
		<? if ($_SESSION["cpanel_privileges"]){?>
		<li class="cpanel-btn">
			<a href="cpanel.php" title="<? echo $la['CONTROL_PANEL']; ?>">
				<img src="theme/images/cogs-white.svg" border="0"/>
			</a>
		</li>
		<? }?>
		<? if ($_SESSION["billing"] == true){?>
		<li class="billing-btn">
			<a href="#" onclick="billingOpen();" title="<? echo $la['BILLING']; ?>">
				<img class="float-left" src="theme/images/cart-white.svg" border="0"/>
				<span id="billing_plan_count" class="billing-plan-count float-right">0</span>
			</a>
		</li>
		<? }?>
		<li>
			<a class="user-btn" href="#" onclick="settingsOpenUser();" title="<? echo $la['MY_ACCOUNT']; ?>">
				<img src="theme/images/user.svg" border="0"/>
				<span class="user-btn-text"><? echo $_SESSION["username"];?></span>
			</a>
		</li>
		<li>
			<a class="mobile_btn" href="mobile/tracking.php" title="<? echo $la['MOBILE_VERSION']; ?>">
				<img src="theme/images/mobile.svg" border="0"/>
			</a>
		</li>
		<li class="logout-btn">
			<a href="#" onclick="connectLogout();" title="<? echo $la['LOGOUT']; ?>">
				<img src="theme/images/logout.svg" border="0"/>
			</a>
		</li>
	</ul>
</div>

<div id="side_panel">
	<ul>
		<li><a href="#side_panel_objects"><? echo $la['OBJECTS']; ?></a></li>
		<li><a href="#side_panel_events"><? echo $la['EVENTS']; ?></a></li>
		<li><a href="#side_panel_places" id="side_panel_places_tab"><? echo $la['PLACES']; ?></a></li>
		<li><a href="#side_panel_history"><? echo $la['HISTORY']; ?></a></li>
	</ul>

	<div id="side_panel_objects">
		<div id="side_panel_objects_object_list">
			<table id="side_panel_objects_object_list_grid"></table>
		</div>
		<div id="side_panel_objects_dragbar">
		</div>
		<div id="side_panel_objects_object_data_list">
			<table id="side_panel_objects_object_data_list_grid"></table>
		</div>
	</div>

	<div id="side_panel_events">
		<div id="side_panel_events_event_list">
		       <table id="side_panel_events_event_list_grid"></table>
		       <div id="side_panel_events_event_list_grid_pager"></div>
	       </div>
	       <div id="side_panel_events_dragbar">
	       </div>
	       <div id="side_panel_events_event_data_list">
		       <table id="side_panel_events_event_data_list_grid"></table>
	       </div>
	</div>

	<div id="side_panel_places">
		<ul>
			<li><a href="#side_panel_places_markers" id="side_panel_places_markers_tab"><span><? echo $la['MARKERS']; ?> </span><span id="side_panel_places_markers_num"></span></a></li>
			<li><a href="#side_panel_places_routes" id="side_panel_places_routes_tab"><span><? echo $la['ROUTES']; ?> </span><span id="side_panel_places_routes_num"></span></a></li>
			<li><a href="#side_panel_places_zones" id="side_panel_places_zones_tab"><span><? echo $la['ZONES']; ?> </span><span id="side_panel_places_zones_num"></span></a></li>
		</ul>

		<div id="side_panel_places_markers">
			<div id="side_panel_places_marker_list">
				<table id="side_panel_places_marker_list_grid"></table>
				<div id="side_panel_places_marker_list_grid_pager"></div>
			</div>
		</div>

		<div id="side_panel_places_routes">
			<div id="side_panel_places_route_list">
				<table id="side_panel_places_route_list_grid"></table>
				<div id="side_panel_places_route_list_grid_pager"></div>
			</div>
		</div>

		<div id="side_panel_places_zones">
			<div id="side_panel_places_zone_list">
				<table id="side_panel_places_zone_list_grid"></table>
				<div id="side_panel_places_zone_list_grid_pager"></div>
			</div>
		</div>
	</div>

	<div id="side_panel_history">
		<div id="side_panel_history_parameters">
			<div class="row2">
			    <div class="width35"><? echo $la['OBJECT']; ?></div>
			    <div class="width65"><select id="side_panel_history_object_list" class="width100"></select></div>
			</div>
			<div class="row2">
				<div class="width35"><? echo $la['FILTER'];?></div>
				<div class="width65">
				    <select id="side_panel_history_filter" class="width100" onchange="switchHistoryReportsDateFilter('history');">
					<option value="0" selected></option>
					<option value="1"><? echo $la['LAST_HOUR'];?></option>
					<option value="2"><? echo $la['TODAY'];?></option>
					<option value="3"><? echo $la['YESTERDAY'];?></option>
					<option value="4"><? echo $la['BEFORE_2_DAYS'];?></option>
					<option value="5"><? echo $la['BEFORE_3_DAYS'];?></option>
					<option value="6"><? echo $la['THIS_WEEK'];?></option>
					<option value="7"><? echo $la['LAST_WEEK'];?></option>
					<option value="8"><? echo $la['THIS_MONTH'];?></option>
					<option value="9"><? echo $la['LAST_MONTH'];?></option>
				    </select>
				</div>
			</div>
			<div class="row2">
				<div class="width35"><? echo $la['TIME_FROM']; ?></div>
				<div class="width31">
					<input readonly class="inputbox-calendar inputbox width100" id="side_panel_history_date_from" type="text" value=""/>
				</div>
				<div class="width2"></div>
				<div class="width15">
					<select class="width100" id="side_panel_history_hour_from">
					<? include ("inc/inc_dt.hours.php"); ?>
					</select>
				</div>
				<div class="width2"></div>
				<div class="width15">
					<select class="width100" id="side_panel_history_minute_from">
					<? include ("inc/inc_dt.minutes.php"); ?>
					</select>
				</div>
			</div>
			<div class="row2">
				<div class="width35"><? echo $la['TIME_TO']; ?></div>
				<div class="width31">
					<input readonly class="inputbox-calendar inputbox width100" id="side_panel_history_date_to" type="text" value=""/>
				</div>
				<div class="width2"></div>
				<div class="width15">
					<select class="width100" id="side_panel_history_hour_to">
					<? include ("inc/inc_dt.hours.php"); ?>
					</select>
				</div>
				<div class="width2"></div>
				<div class="width15">
					<select class="width100" id="side_panel_history_minute_to">
					<? include ("inc/inc_dt.minutes.php"); ?>
					</select>
				</div>
			</div>

			<div class="row3">
				<div class="width35"><? echo $la['STOPS']; ?></div>
				<div class="width31">
					<select id="side_panel_history_stop_duration" class="width100">
						<option value=1>> 1 min</option>
						<option value=2>> 2 min</option>
						<option value=5>> 5 min</option>
						<option value=10>> 10 min</option>
						<option value=20>> 20 min</option>
						<option value=30>> 30 min</option>
						<option value=60>> 1 h</option>
						<option value=120>> 2 h</option>
						<option value=300>> 5 h</option>
					</select>
				</div>
			</div>

			<div class="row3">
				<input style="width: 100px; margin-right: 3px;" class="button" type="button" value="<? echo $la['SHOW']; ?>" onclick="historyLoadRoute();"/>
				<input style="width: 100px; margin-right: 3px;" class="button" type="button" value="<? echo $la['HIDE']; ?>" onclick="historyHideRoute();"/>
				<input style="width: 134px;" id="side_panel_history_import_export_action_menu_button" class="button" type="button" value="<? echo $la['IMPORT_EXPORT']; ?>"/>
			</div>
		</div>

		<div id="side_panel_history_route">
			<table id="side_panel_history_route_detail_list_grid"></table>
		</div>

		<div id="side_panel_history_dragbar">
		</div>

		<div id="side_panel_history_route_data_list">
			<table id="side_panel_history_route_data_list_grid"></table>
		</div>
	</div>
</div>

<div id="bottom_panel">
	<div id="bottom_panel_tabs" style="height: 100%;">
		<ul>
		    <li><a href="#bottom_panel_graph"><? echo $la['GRAPH']; ?></a></li>
		    <li><a href="#bottom_panel_msg"><? echo $la['MESSAGES']; ?></a></li>
		</ul>

		<div id="bottom_panel_graph">
			<div class="graph-controls">
				<div class="graph-controls-left">
					<select style="min-width:100px;" id="bottom_panel_graph_data_source" onchange="historyRouteChangeGraphSource();"></select>

					<a href="#" onclick="historyRoutePlay();" title="<? echo $la['PLAY'];?>">
						<img src="theme/images/play.svg" width="10px" border="0"/>
					</a>

					<a href="#" onclick="historyRoutePause();" title="<? echo $la['PAUSE'];?>">
						<img src="theme/images/pause.svg" width="10px" border="0"/>
					</a>

					<a href="#" onclick="historyRouteStop();" title="<? echo $la['STOP'];?>">
						<img src="theme/images/stop.svg" width="10px" border="0"/>
					</a>

					<select id="bottom_panel_graph_play_speed">
						<option value=1>x1</option>
						<option value=2>x2</option>
						<option value=3>x3</option>
						<option value=4>x4</option>
						<option value=5>x5</option>
						<option value=6>x6</option>
					</select>
				</div>
				<div class="graph-controls-right">
					<span id="bottom_panel_graph_label"></span>

					<a href="#" onclick="graphPanLeft();" title="<? echo $la['PAN_LEFT'];?>">
						<img src="theme/images/arrow-left.svg" width="10px" border="0"/>
					</a>

					<a href="#" onclick="graphPanRight();" title="<? echo $la['PAN_RIGHT'];?>">
						<img src="theme/images/arrow-right.svg" width="10px" border="0"/>
					</a>

					<a href="#" onclick="graphZoomIn();" title="<? echo $la['ZOOM_IN'];?>">
						<img src="theme/images/plus.svg" width="10px" border="0"/>
					</a>

					<a href="#" onclick="graphZoomOut();" title="<? echo $la['ZOOM_OUT'];?>">
						<img src="theme/images/minus.svg" width="10px" border="0"/>
					</a>
				</div>
			</div>

			<div id="bottom_panel_graph_plot"></div>
		</div>

		<div id="bottom_panel_msg">
			<table id="bottom_panel_msg_list_grid"></table>
			<div id="bottom_panel_msg_list_grid_pager"></div>
		</div>
	</div>
</div>

<a href="#" onclick="showHideLeftPanel();">
	<div id="side_panel_dragbar">
	</div>
</a>

<a href="#" onclick="showBottomPanel();">
	<div id="bottom_panel_dragbar">
	</div>
</a>
