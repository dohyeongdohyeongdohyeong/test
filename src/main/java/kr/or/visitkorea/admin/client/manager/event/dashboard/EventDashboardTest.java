package kr.or.visitkorea.admin.client.manager.event.dashboard;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import gwt.material.design.amcharts.client.Am4Charts;
import gwt.material.design.amcharts.client.XYChart;
import gwt.material.design.amcharts.client.axis.CategoryAxis;
import gwt.material.design.amcharts.client.axis.ValueAxis;
import gwt.material.design.amcharts.client.bullet.LabelBullet;
import gwt.material.design.amcharts.client.series.ColumnSeries;
import gwt.material.design.amcore.client.Am4Core;
import gwt.material.design.amcore.client.base.Percent;
import gwt.material.design.amcore.client.data.ChartData;
import gwt.material.design.amcore.client.data.DataProvider;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventDashboardTest extends AbstractContentPanel {
    private XYChart chart;
	
	public EventDashboardTest() {
		super();
	}

	public EventDashboardTest(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}

	@Override
	public void init() {
		MaterialPanel panel = new MaterialPanel();
		panel.setWidth("100%");
		panel.setHeight("85%");
		
		chart = (XYChart) Am4Core.create(panel, Am4Charts.XYChart);

        List<HashMap<String, Object>> people = new ArrayList<>();
		HashMap<String, Object> map = new HashMap<>();
		map.put("name", "The first");
		map.put("value", 600);
		people.add(map);
		HashMap<String, Object> map1 = new HashMap<>();
		map1.put("name", "The second");
		map1.put("value", 300);
		people.add(map1);
		HashMap<String, Object> map2 = new HashMap<>();
		map2.put("name", "The third");
		map2.put("value", 200);
		people.add(map2);
		HashMap<String, Object> map3 = new HashMap<>();
		map3.put("name", "The fourth");
		map3.put("value", 180);
		people.add(map3);
		HashMap<String, Object> map4 = new HashMap<>();
		map4.put("name", "The fifth");
		map4.put("value", 50);
		people.add(map4);
		HashMap<String, Object> map5 = new HashMap<>();
		map5.put("name", "The sixth");
		map5.put("value", 20);
		people.add(map5);
		HashMap<String, Object> map6 = new HashMap<>();
		map6.put("name", "The seventh");
		map6.put("value", 10);
		people.add(map6);

        ChartData<HashMap<String, Object>> data = new ChartData<>();
        data.setData(people);

        data.add(new DataProvider<HashMap<String, Object>>() {
            @Override
            public String getProperty() {
                return "name";
            }

            @Override
            public JSONValue getValue(HashMap<String, Object> object) {
                return new JSONString(object.get("name").toString());
            }
        });
        
        data.add(new DataProvider<HashMap<String, Object>>() {
            @Override
            public String getProperty() {
                return "value";
            }

            @Override
            public JSONValue getValue(HashMap<String, Object> object) {
                return new JSONString(object.get("value").toString());
            }
        });
        // Add data
        
        chart.data = data.get();

        // Create axes
        CategoryAxis categoryAxis = (CategoryAxis) chart.xAxes.push(new CategoryAxis());
        categoryAxis.dataFields.category = "name";
        categoryAxis.numberFormatter.numberFormat = "#";
        categoryAxis.renderer.grid.template.location = 0;
        categoryAxis.renderer.cellStartLocation = 0.1;
        categoryAxis.renderer.cellEndLocation = 0.9;

        ValueAxis valueAxis = (ValueAxis) chart.yAxes.push(new ValueAxis());
        valueAxis.renderer.opposite = false;

        // Create series
        createSeries("value", "value");
        
        this.add(panel);
	}

	private void createSeries(String field, String name) {
        ColumnSeries series = (ColumnSeries) chart.series.push(new ColumnSeries());
        series.dataFields.valueY = field;
        series.dataFields.categoryX = "name";
        series.name = name;
        series.columns.template.tooltipText = "{name}: [bold]{valueY}[/]";
        series.columns.template.height = new Percent(100);
        series.sequencedInterpolation = true;

        LabelBullet valueLabel = (LabelBullet) series.bullets.push(new LabelBullet());
        valueLabel.label.text = "{valueY}";
        valueLabel.label.hideOversized = false;
        valueLabel.label.truncate = false;
    }
}
