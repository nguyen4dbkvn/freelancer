var chart;
//var chartData = [];
var chartCursor;

AmCharts.ready(function() {
	// generate some data first
	//generateChartData();

	// SERIAL CHART
	chart = new AmCharts.AmSerialChart();
	chart.pathToImages = "images/";
	chart.panEventsEnabled = true;
	chart.zoomOutButton = {
		backgroundColor : "#000000",
		backgroundAlpha : 0.15
	};
	chart.dataProvider = chartData
	chart.categoryField = "timestamp";
	chart.marginRight = 0;
	chart.balloon.bulletSize = 5;

	// listen for "dataUpdated" event (fired when chart is rendered) and call
	// zoomChart method when it happens
	chart.addListener("dataUpdated", zoomChart);

	// AXES
	// category
	var categoryAxis = chart.categoryAxis;
	categoryAxis.parseDates = false; // as our data is date-based, we set
									// parseDates to true
	categoryAxis.minPeriod = "DD"; // our data is daily, so we set minPeriod to
									// DD
	categoryAxis.dashLength = 1;
	categoryAxis.gridAlpha = 0.15;
	categoryAxis.axisColor = "#DADADA";

	// value
	var valueAxis = new AmCharts.ValueAxis();
	valueAxis.axisAlpha = 0;
	valueAxis.dashLength = 1;
	chart.addValueAxis(valueAxis);

	// GRAPH
	var graph = new AmCharts.AmGraph();
	graph.title = "red line";
	graph.balloonText = "[[detail]]";
	graph.valueField = "fuelLevel";
	graph.bullet = "round";
	graph.bulletBorderColor = "#FFFFFF";
	graph.bulletBorderThickness = 2;
	graph.lineThickness = 2;
	graph.lineColor = "#5fb503";
	graph.negativeLineColor = "#efcc26";
	graph.hideBulletsCount = 50; // this makes the chart to hide bullets when
									// there are more than 50 series in
									// selection
	chart.addGraph(graph);

	// CURSOR
	chartCursor = new AmCharts.ChartCursor();
	chartCursor.cursorPosition = "mouse";
	chartCursor.zoomable = true;
	//chartCursor.pan = true; // set it to fals if you want the cursor to work in
							// "select" mode
	chart.addChartCursor(chartCursor);

	// SCROLLBAR
	var chartScrollbar = new AmCharts.ChartScrollbar();
	chartScrollbar.graph = graph;
	chartScrollbar.autoGridCount = true;
	chartScrollbar.scrollbarHeight = 25;
	chart.addChartScrollbar(chartScrollbar);

	// WRITE
	chart.write("chartdiv");
});

// generate some random data, quite different range
/*function generateChartData() {
	var firstDate = new Date();
	firstDate.setDate(firstDate.getDate() - 500);

	for ( var i = 0; i < 500; i++) {
		var newDate = new Date(firstDate);
		newDate.setDate(newDate.getDate() + i);

		var visits = Math.round(Math.random() * 40) - 20;

		chartData.push({
			date : newDate,
			visits : visits
		});
	}
}*/

// this method is called when chart is first inited as we listen for
// "dataUpdated" event
function zoomChart() {
	// different zoom methods can be used - zoomToIndexes, zoomToDates,
	// zoomToCategoryValues
	chart.zoomToIndexes(chartData.length - 40, chartData.length - 1);
}

// changes cursor mode from pan to select
/*function setPanSelect() {
	if (document.getElementById("rb1").checked) {
		chartCursor.pan = false;
		chartCursor.zoomable = true;
	} else {
		chartCursor.pan = true;
	}
	chart.validateNow();
}*/
