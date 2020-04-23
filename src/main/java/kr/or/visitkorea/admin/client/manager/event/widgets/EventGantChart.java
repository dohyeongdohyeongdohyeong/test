package kr.or.visitkorea.admin.client.manager.event.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import gwt.material.design.amcharts.client.Am4Charts;
import gwt.material.design.amcharts.client.XYChart;
import gwt.material.design.amcharts.client.axis.Axis;
import gwt.material.design.amcharts.client.axis.CategoryAxis;
import gwt.material.design.amcharts.client.axis.DateAxis;
import gwt.material.design.amcharts.client.base.TimeInterval;
import gwt.material.design.amcharts.client.series.ColumnSeries;
import gwt.material.design.amcore.client.Am4Core;
import gwt.material.design.amcore.client.base.Percent;
import gwt.material.design.amcore.client.color.ColorSet;
import gwt.material.design.amcore.client.constants.TimeUnit;
import gwt.material.design.amcore.client.data.ChartData;
import gwt.material.design.amcore.client.data.DataProvider;
import gwt.material.design.amcore.client.scrollbar.Scrollbar;
import gwt.material.design.amcore.client.theme.AnimatedTheme;
import gwt.material.design.client.ui.MaterialPanel;

public class EventGantChart extends MaterialPanel {

    private XYChart chart;
	private List<HashMap<String, Object>> eventList = new ArrayList<>();
    private ChartData<HashMap<String, Object>> chartData = new ChartData<>();
    
	public EventGantChart() {
		super();
		init();
	}

	public void init() {
		Am4Core.useTheme(new AnimatedTheme());
		
		chart = (XYChart) Am4Core.create(this, Am4Charts.XYChart);
        chart.hiddenState.properties.opacity = 0;

        chartData.add(this.addProvider("name"));
        chartData.add(this.addProvider("fromDate"));
        chartData.add(this.addProvider("toDate"));

        chart.paddingRight = 30;
        chart.dateFormatter.inputDateFormat = "yyyy-MM-dd";

        ColorSet colorSet = new ColorSet();
        colorSet.saturation = 0.4;

        Axis categoryAxis = chart.yAxes.push(new CategoryAxis());
        categoryAxis.dataFields.category = "name";
        categoryAxis.renderer.grid.template.location = 0;
        categoryAxis.renderer.inversed = true;

        DateAxis dateAxis = (DateAxis) chart.xAxes.push(new DateAxis());
        dateAxis.dateFormatter.dateFormat = "yyyy-MM-dd";
        dateAxis.renderer.minGridDistance = 80;
        dateAxis.dateFormats.setKey("day", "yyyy-MM-dd");
        dateAxis.dateFormats.setKey("month", "yyyy-MM");
        dateAxis.dateFormats.setKey("year", "yyyy");
        dateAxis.periodChangeDateFormats.setKey("day", "yyyy-MM-dd"); 
        dateAxis.periodChangeDateFormats.setKey("month", "yyyy-MM");
        dateAxis.periodChangeDateFormats.setKey("year", "yyyy");

        TimeInterval interval = new TimeInterval();
        interval.count = 1;
        interval.timeUnit = TimeUnit.DAY;
        
        dateAxis.baseInterval = interval;
        dateAxis.strictMinMax = true;
        dateAxis.renderer.tooltipLocation = 0;

        ColumnSeries series1 = (ColumnSeries) chart.series.push(new ColumnSeries());
        series1.columns.template.width = new Percent(10);
        series1.columns.template.tooltipText = "{name}: {openDateX} - {dateX}";
        
        series1.dataFields.openDateX = "fromDate";
        series1.dataFields.dateX = "toDate";
        series1.dataFields.categoryY = "name";
        series1.columns.template.strokeOpacity = 1;

        chart.scrollbarY = new Scrollbar();
        chart.scrollbarX = new Scrollbar();
	}
	
	public void addData(String name, String fromDate, String toDate) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("fromDate", fromDate);
		map.put("toDate", toDate);
		eventList.add(map);

		chartData.setData(eventList);
		chart.data = chartData.get();
	}

	public void clearData() {
		this.eventList.clear();
		this.chartData.setData(this.eventList);
		chart.data = this.chartData.get();
	}
	
	private DataProvider<HashMap<String, Object>> addProvider(String property) {
		return new DataProvider<HashMap<String,Object>>() {
			@Override
			public String getProperty() {
				return property;
			}

			@Override
			public JSONValue getValue(HashMap<String, Object> object) {
				return new JSONString(object.get(property).toString());
			}
		};
	}
}
