<div id="dialog_rs31" title="<? echo $la['REPORTS_OF_STANDARD_312014']; ?>">
  <div id="rs31_tabs">
    <ul>
      <li><a href="#rs31_car_journey_tab"><? echo $la['REPORTS_CAR_JOURNEY'] ?></a></li>
      <li><a href="#rs31_car_speed_tab"><? echo $la['REPORTS_CAR_SPEED'] ?></a></li>
      <li><a href="#rs31_car_stop_tab"><? echo $la['REPORTS_CAR_STOP'] ?></a></li>
      <li><a href="#rs31_by_date_tab"><? echo $la['REPORTS_BY_DATE'] ?></a></li>
      <li><a href="#rs31_over_speed_tab"><? echo $la['REPORTS_OVER_SPEED'] ?></a></li>
      <li><a href="#rs31_driving_time_tab"><? echo $la['REPORTS_DRIVING_TIME'] ?></a></li>
      <li><a href="#rs31_sumarization_tab"><? echo $la['REPORTS_SUMMARIZATION'] ?></a></li>
      <li><a href="#rs31_by_car_tab"><? echo $la['REPORTS_BY_CAR'] ?></a></li>
      <li><a href="#rs31_by_driver_tab"><? echo $la['REPORTS_BY_DRIVER'] ?></a></li>
    </ul>

    <div id="rs31_car_journey_tab">
      <div class="row">
        <div class="block width33">
          <div class="container">
            <div class="row2">
              <div class="width30"><? echo $la['OBJECT']; ?></div>
              <div class="width70"><select class="width100" id="rs31_object_list"></select></div>
            </div>
          </div>
        </div>
        <div class="block width33">
          <div class="container">
            <div class="row2">
    					<div class="width30"><? echo $la['TIME_FROM']; ?></div>
    					<div class="width40">
    						<input readonly class="inputbox-calendar inputbox width100" id="rs31_date_from" type="text" value=""/>
    					</div>
    					<div class="width2"></div>
    					<div class="width13">
    						<select class="width100" id="rs31_hour_from">
    						<? include ("inc/inc_dt.hours.php"); ?>
    						</select>
    					</div>
    					<div class="width2"></div>
    					<div class="width13">
    						<select class="width100" id="rs31_minute_from">
    						<? include ("inc/inc_dt.minutes.php"); ?>
    						</select>
    					</div>
    				</div>
            <div class="row3">
              <div class="width30"><? echo $la['TIME_TO']; ?></div>
              <div class="width40">
                <input readonly class="inputbox-calendar inputbox width100" id="rs31_date_to" type="text" value=""/>
              </div>
              <div class="width2"></div>
              <div class="width13">
                <select class="width100" id="rs31_hour_to">
                <? include ("inc/inc_dt.hours.php"); ?>
                </select>
              </div>
              <div class="width2"></div>
              <div class="width13">
                <select class="width100" id="rs31_minute_to">
                <? include ("inc/inc_dt.minutes.php"); ?>
                </select>
              </div>
            </div>
    			</div>
        </div>
        <div class="row3">
          <input style="width: 100px; margin-right: 15px;" class="button" type="button" value="<? echo $la['SHOW']; ?>" onclick="rs31Show();"/>
          <input style="width: 100px; margin-right: 15px;" class="button" type="button" value="<? echo $la['EXPORT_EXCEL']; ?>" onclick="rs31ExportCSV();"/>
          <input style="width: 100px;" class="button" type="button" value="<? echo $la['EXPORT_PDF']; ?>" onclick="rs31ExportPDF();"/>
        </div>
      </div>

      <table id="rs31_car_journey_list_grid"></table>
    	<div id="rs31_car_journey_list_grid_pager"></div>
    </div>

    <div id="rs31_car_speed_tab">
      <div class="row">
        <div class="block width33">
          <div class="container">
            <div class="row2">
              <div class="width30"><? echo $la['OBJECT']; ?></div>
              <div class="width70"><select class="width100" id="rs31_object_list"></select></div>
            </div>
          </div>
        </div>
        <div class="block width33">
          <div class="container">
            <div class="row2">
    					<div class="width30"><? echo $la['TIME_FROM']; ?></div>
    					<div class="width40">
    						<input readonly class="inputbox-calendar inputbox width100" id="rs31_date_from" type="text" value=""/>
    					</div>
    					<div class="width2"></div>
    					<div class="width13">
    						<select class="width100" id="rs31_hour_from">
    						<? include ("inc/inc_dt.hours.php"); ?>
    						</select>
    					</div>
    					<div class="width2"></div>
    					<div class="width13">
    						<select class="width100" id="rs31_minute_from">
    						<? include ("inc/inc_dt.minutes.php"); ?>
    						</select>
    					</div>
    				</div>
            <div class="row3">
              <div class="width30"><? echo $la['TIME_TO']; ?></div>
              <div class="width40">
                <input readonly class="inputbox-calendar inputbox width100" id="rs31_date_to" type="text" value=""/>
              </div>
              <div class="width2"></div>
              <div class="width13">
                <select class="width100" id="rs31_hour_to">
                <? include ("inc/inc_dt.hours.php"); ?>
                </select>
              </div>
              <div class="width2"></div>
              <div class="width13">
                <select class="width100" id="rs31_minute_to">
                <? include ("inc/inc_dt.minutes.php"); ?>
                </select>
              </div>
            </div>
    			</div>
        </div>
        <div class="row3">
          <input style="width: 100px; margin-right: 15px;" class="button" type="button" value="<? echo $la['SHOW']; ?>" onclick="rs31Show();"/>
          <input style="width: 100px; margin-right: 15px;" class="button" type="button" value="<? echo $la['EXPORT_EXCEL']; ?>" onclick="rs31ExportCSV();"/>
        </div>
      </div>

      <table id="rs31_car_speed_list_grid"></table>
    	<div id="rs31_car_speed_list_grid_pager"></div>
    </div>

    <div id="rs31_car_stop_tab">
      <div class="row">
        <div class="block width33">
          <div class="container">
            <div class="row2">
              <div class="width30"><? echo $la['OBJECT']; ?></div>
              <div class="width70"><select class="width100" id="rs31_object_list"></select></div>
            </div>
          </div>
        </div>
        <div class="block width33">
          <div class="container">
            <div class="row2">
              <div class="width30"><? echo $la['TIME_FROM']; ?></div>
              <div class="width40">
                <input readonly class="inputbox-calendar inputbox width100" id="rs31_date_from" type="text" value=""/>
              </div>
              <div class="width2"></div>
              <div class="width13">
                <select class="width100" id="rs31_hour_from">
                <? include ("inc/inc_dt.hours.php"); ?>
                </select>
              </div>
              <div class="width2"></div>
              <div class="width13">
                <select class="width100" id="rs31_minute_from">
                <? include ("inc/inc_dt.minutes.php"); ?>
                </select>
              </div>
            </div>
            <div class="row3">
              <div class="width30"><? echo $la['TIME_TO']; ?></div>
              <div class="width40">
                <input readonly class="inputbox-calendar inputbox width100" id="rs31_date_to" type="text" value=""/>
              </div>
              <div class="width2"></div>
              <div class="width13">
                <select class="width100" id="rs31_hour_to">
                <? include ("inc/inc_dt.hours.php"); ?>
                </select>
              </div>
              <div class="width2"></div>
              <div class="width13">
                <select class="width100" id="rs31_minute_to">
                <? include ("inc/inc_dt.minutes.php"); ?>
                </select>
              </div>
            </div>
          </div>
        </div>
        <div class="row3">
          <input style="width: 100px; margin-right: 15px;" class="button" type="button" value="<? echo $la['SHOW']; ?>" onclick="rs31Show();"/>
          <input style="width: 100px; margin-right: 15px;" class="button" type="button" value="<? echo $la['EXPORT_EXCEL']; ?>" onclick="rs31ExportCSV();"/>
        </div>
      </div>

      <table id="rs31_car_stop_list_grid"></table>
    	<div id="rs31_car_stop_list_grid_pager"></div>
    </div>

    <div id="rs31_by_date_tab">
      <div class="row">
        <div class="block width33">
          <div class="container">
            <div class="row3">
              <div class="width40"><? echo $la['CHOOSE_DATE']; ?></div>
              <div class="width60">
                <input readonly class="inputbox-calendar inputbox width100" id="rs31_date_to" type="text" value=""/>
              </div>
            </div>
          </div>
        </div>

        <div class="row3">
          <input style="width: 100px; margin-right: 15px;" class="button" type="button" value="<? echo $la['SHOW']; ?>" onclick="rs31Show();"/>
          <input style="width: 100px; margin-right: 15px;" class="button" type="button" value="<? echo $la['EXPORT_EXCEL']; ?>" onclick="rs31ExportCSV();"/>
          <input style="width: 100px;" class="button" type="button" value="<? echo $la['EXPORT_PDF']; ?>" onclick="rs31ExportPDF();"/>
        </div>
      </div>

      <table id="rs31_by_date_list_grid"></table>
      <div id="rs31_by_date_list_grid_pager"></div>
    </div>

    <div id="rs31_over_speed_tab">
      <div class="row">
        <div class="block width33">
          <div class="container">
            <div class="row2">
              <div class="width30"><? echo $la['OBJECT']; ?></div>
              <div class="width70"><select class="width100" id="rs31_object_list"></select></div>
            </div>
          </div>
        </div>
        <div class="block width33">
          <div class="container">
            <div class="row2">
              <div class="width30"><? echo $la['TIME_FROM']; ?></div>
              <div class="width40">
                <input readonly class="inputbox-calendar inputbox width100" id="rs31_date_from" type="text" value=""/>
              </div>
              <div class="width2"></div>
              <div class="width13">
                <select class="width100" id="rs31_hour_from">
                <? include ("inc/inc_dt.hours.php"); ?>
                </select>
              </div>
              <div class="width2"></div>
              <div class="width13">
                <select class="width100" id="rs31_minute_from">
                <? include ("inc/inc_dt.minutes.php"); ?>
                </select>
              </div>
            </div>
            <div class="row3">
              <div class="width30"><? echo $la['TIME_TO']; ?></div>
              <div class="width40">
                <input readonly class="inputbox-calendar inputbox width100" id="rs31_date_to" type="text" value=""/>
              </div>
              <div class="width2"></div>
              <div class="width13">
                <select class="width100" id="rs31_hour_to">
                <? include ("inc/inc_dt.hours.php"); ?>
                </select>
              </div>
              <div class="width2"></div>
              <div class="width13">
                <select class="width100" id="rs31_minute_to">
                <? include ("inc/inc_dt.minutes.php"); ?>
                </select>
              </div>
            </div>
          </div>
        </div>
        <div class="row3">
          <input style="width: 100px; margin-right: 15px;" class="button" type="button" value="<? echo $la['SHOW']; ?>" onclick="rs31Show();"/>
          <input style="width: 100px; margin-right: 15px;" class="button" type="button" value="<? echo $la['EXPORT_EXCEL']; ?>" onclick="rs31ExportCSV();"/>
        </div>
      </div>

      <table id="rs31_over_speed_list_grid"></table>
      <div id="rs31_over_speed_list_grid_pager"></div>
    </div>

    <div id="rs31_driving_time_tab">
      <div class="row">
        <div class="block width33">
          <div class="container">
            <div class="row2">
              <div class="width30"><? echo $la['OBJECT']; ?></div>
              <div class="width70"><select class="width100" id="rs31_object_list"></select></div>
            </div>
          </div>
        </div>
        <div class="block width33">
          <div class="container">
            <div class="row2">
    					<div class="width30"><? echo $la['TIME_FROM']; ?></div>
    					<div class="width40">
    						<input readonly class="inputbox-calendar inputbox width100" id="rs31_date_from" type="text" value=""/>
    					</div>
    					<div class="width2"></div>
    					<div class="width13">
    						<select class="width100" id="rs31_hour_from">
    						<? include ("inc/inc_dt.hours.php"); ?>
    						</select>
    					</div>
    					<div class="width2"></div>
    					<div class="width13">
    						<select class="width100" id="rs31_minute_from">
    						<? include ("inc/inc_dt.minutes.php"); ?>
    						</select>
    					</div>
    				</div>
            <div class="row3">
              <div class="width30"><? echo $la['TIME_TO']; ?></div>
              <div class="width40">
                <input readonly class="inputbox-calendar inputbox width100" id="rs31_date_to" type="text" value=""/>
              </div>
              <div class="width2"></div>
              <div class="width13">
                <select class="width100" id="rs31_hour_to">
                <? include ("inc/inc_dt.hours.php"); ?>
                </select>
              </div>
              <div class="width2"></div>
              <div class="width13">
                <select class="width100" id="rs31_minute_to">
                <? include ("inc/inc_dt.minutes.php"); ?>
                </select>
              </div>
            </div>
    			</div>
        </div>
        <div class="block width33">
          <div class="container">
            <div class="row2">
              <div class="width30"><? echo $la['DRIVING_TIME']; ?></div>
              <div class="width70"><select class="width100" id="rs31_driving_time_list"></select></div>
            </div>
          </div>
        </div>

        <div class="row3">
          <input style="width: 100px; margin-right: 15px;" class="button" type="button" value="<? echo $la['SHOW']; ?>" onclick="rs31Show();"/>
          <input style="width: 100px; margin-right: 15px;" class="button" type="button" value="<? echo $la['EXPORT_EXCEL']; ?>" onclick="rs31ExportCSV();"/>
          <input style="width: 100px;" class="button" type="button" value="<? echo $la['EXPORT_PDF']; ?>" onclick="rs31ExportPDF();"/>
        </div>
      </div>

      <table id="rs31_driving_time_list_grid"></table>
      <div id="rs31_driving_time_list_grid_pager"></div>
    </div>
    <div id="rs31_sumarization_tab">
      <table id="rs31_sumarization_list_grid"></table>
      <div id="rs31_sumarization_list_grid_pager"></div>
    </div>
    <div id="rs31_by_car_tab">
      <table id="rs31_by_car_list_grid"></table>
      <div id="rs31_by_car_list_grid_pager"></div>
    </div>
    <div id="rs31_by_driver_tab">
      <table id="rs31_by_driver_list_grid"></table>
      <div id="rs31_by_driver_list_grid_pager"></div>
    </div>
  </div>
</div>
