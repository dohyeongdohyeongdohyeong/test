package kr.or.visitkorea.admin.client.manager.event.dashboard.panel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;


import gwt.material.design.addins.client.combobox.MaterialComboBox;
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
import gwt.material.design.amcore.client.scrollbar.Scrollbar;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.event.dashboard.EventDashboardAccessApplication;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventAccessList extends EventDashboardBasePanel {
	private XYChart chart;
	private ContentTable table;
	private MaterialLabel countLabel;
	private MaterialPanel chartPanel;
	private MaterialPanel tablePanel ;
	private MaterialInput startDate;
	private MaterialInput endDate;
	private MaterialTextBox searchWord;
	private MaterialComboBox status;
	

	//private String subEvtId;
	private List<HashMap<String, Object>> chartDataList1;
	private List<HashMap<String, Object>> chartDataList2;
	private List<HashMap<String, Object>> chartDataList3;
	private List<HashMap<String, Object>> chartDataList4;
	private List<HashMap<String, Object>> chartDataList5;

	private int index;
	private int totcnt;
	private int offset;
	

	public EventAccessList(MaterialExtentsWindow window) {
		super(window);
		qryList(true);
	}

	
	@Override
	public void init() {
		this.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		this.setHeight("660px");

		MaterialLabel titleLabel = new MaterialLabel("접속통계");
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setLineHeight(46.25);
		titleLabel.setHeight("46.25px");
		MaterialColumn col = new MaterialColumn();
		col.add(titleLabel);
		this.add(col);
		this.add(getEventInfoPanel());

		chartPanel = new MaterialPanel();
		chartPanel.setWidth("100%");
		chartPanel.setHeight("250px");
		this.add(chartPanel);
		
		tablePanel = new MaterialPanel();
		tablePanel.add(getBaseTablePanel());
		
		this.add(tablePanel);

	} 

	
	private MaterialPanel getEventInfoPanel() {
		MaterialPanel panel = new MaterialPanel();
		MaterialRow row = new MaterialRow();
		chartDataList1 = new ArrayList<HashMap<String, Object>>();
		chartDataList2 = new ArrayList<HashMap<String, Object>>();
		chartDataList3 = new ArrayList<HashMap<String, Object>>();
		chartDataList4 = new ArrayList<HashMap<String, Object>>();
		chartDataList5 = new ArrayList<HashMap<String, Object>>();
		
		this.startDate = addInputDate(row, "s2", "시작일");
		this.endDate = addInputDate(row, "s2", "마감일");
		this.searchWord = addInputText(row, "이벤트명 검색", "s4", false);
		MaterialIcon searchIcon = new MaterialIcon(IconType.SEARCH);
		addIcon(searchIcon, "검색", Float.RIGHT, "26px", false);
		searchIcon.addClickHandler(event -> {
			qryList(true);
		});
		
		panel.add(row);

		row = addRow(panel);
		addLabel(row, "상태", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		List<Object> typeList = new ArrayList<>();
		typeList.add("총참여건수");
		typeList.add("중복제거건수");
		typeList.add("페이지뷰");
		typeList.add("공유건수");
		typeList.add("즐겨찾기");
		status = addCombobox(row, "s4", typeList);
		status.addValueChangeHandler(e->{
			if(status.getSelectedIndex() == 1) {
				drawChart(chartDataList2);
			}else if(status.getSelectedIndex() == 2) {
				drawChart(chartDataList3);
			}else if(status.getSelectedIndex() == 3) {
				drawChart(chartDataList4);
			}else if(status.getSelectedIndex() == 4) {
				drawChart(chartDataList5);
			}else {
				drawChart(chartDataList1);
			}
		});

		return panel;
		
	}

	
	private MaterialPanel getBaseTablePanel() {

		MaterialPanel tablePanel = new MaterialPanel();
		tablePanel.setPadding(10);
		this.add(tablePanel);
		
		table = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setWidth("100%");
		table.setHeight(250);
		table.setLayoutPosition(Position.RELATIVE);
		table.setTop(-25);
		table.appendTitle("번호", 50, TextAlign.CENTER);
		table.appendTitle("시작일", 150, TextAlign.CENTER);
		table.appendTitle("종료일", 150, TextAlign.CENTER);
		table.appendTitle("이벤트명", 200, TextAlign.CENTER);
		table.appendTitle("총참여건수", 150, TextAlign.CENTER);
		table.appendTitle("중복제거건수", 150, TextAlign.CENTER);
		table.appendTitle("페이지뷰", 100, TextAlign.CENTER);
		table.appendTitle("공유건수", 100, TextAlign.CENTER);
		table.appendTitle("즐겨찾기", 100, TextAlign.CENTER);
		table.appendTitle("PC", 100, TextAlign.CENTER);
		table.appendTitle("Mobile", 100, TextAlign.CENTER);
		
		tablePanel.add(table);
		
		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLDER);
		

		MaterialIcon moreIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		moreIcon.addClickHandler(event -> {
			if (offset + DEFAULT_PAGE_NUM_ROWS > totcnt)
				return;
			qryList(false);
		});
		
		
		table.getButtomMenu().addIcon(moreIcon, "다음"+ DEFAULT_PAGE_NUM_ROWS +"건", Float.RIGHT);
		table.getButtomMenu().addLabel(countLabel, Float.RIGHT);
		
		
		return tablePanel;
	}
	

	private void drawChart(List<HashMap<String, Object>> dataList) {

		
        ChartData<HashMap<String, Object>> data = new ChartData<>();
        data.setData(dataList);
        
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
        
        
		chart = (XYChart) Am4Core.create(chartPanel, Am4Charts.XYChart);
        chart.data = data.get();
        chart.scrollbarX = new Scrollbar();

        CategoryAxis categoryAxis = (CategoryAxis) chart.xAxes.push(new CategoryAxis());
        categoryAxis.dataFields.category = "name";
        categoryAxis.numberFormatter.numberFormat = "#";
        categoryAxis.renderer.grid.template.location = 0;
        categoryAxis.renderer.cellStartLocation = 0.1;
        categoryAxis.renderer.cellEndLocation = 0.9;

        ValueAxis valueAxis = (ValueAxis) chart.yAxes.push(new ValueAxis());
        valueAxis.renderer.opposite = false;
        
        createSeries("value", "value");

	}

	protected String convertDateFormat(String strDate) {
		if (strDate.equals("") || strDate == null) return "";
		Date date = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.s").parse(strDate);
		return DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
	}
	
	public void qryList(boolean bstart) {
		if (bstart) {
			offset = 0;
			index = 1;
			totcnt = 0;
			this.table.clearRows();
		} else offset += DEFAULT_PAGE_NUM_ROWS;
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SELECT_EVENT_DASHBOARD_ACCESS_LIST"));
		paramJson.put("startDate", new JSONString(this.startDate.getValue()));
		paramJson.put("endDate", new JSONString(this.endDate.getValue()));
		paramJson.put("searchWord", new JSONString(searchWord.getText()));
		paramJson.put("offset", new JSONNumber(this.offset));
		paramJson.put("adminPermission", new JSONString(EventDashboardAccessApplication.ADMIN_PERMISSION));
		paramJson.put("usrId", new JSONString(Registry.getUserId()));
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {

				
				JSONObject bodyObj = resultObj.get("body").isObject();
				JSONArray bodyResultArr = bodyObj.get("result").isArray();
				int bodyResultCnt = (int) bodyObj.get("resultCnt").isObject().get("CNT").isNumber().doubleValue();
				this.countLabel.setText(bodyResultCnt + " 건");
				totcnt = bodyResultCnt;
				if (bstart) {
					chartDataList1.clear();
					chartDataList2.clear();
					chartDataList3.clear();
					chartDataList4.clear();
					chartDataList5.clear();
				}
				
				for (int i = 0; i < bodyResultArr.size(); i++) {
					JSONObject obj = bodyResultArr.get(i).isObject();
					
					String startDate = obj.containsKey("START_DATE") ? obj.get("START_DATE").isString().stringValue() : "";
					String endDate = obj.containsKey("END_DATE") ? obj.get("END_DATE").isString().stringValue() : "";
					String title = obj.containsKey("TITLE") ? obj.get("TITLE").isString().stringValue() : "";
					int cnt = obj.containsKey("TOT_CNT") ? (int) obj.get("TOT_CNT").isNumber().doubleValue() : -1;
					int removeCnt = obj.containsKey("REMOVE_CNT") ?  (int) obj.get("REMOVE_CNT").isNumber().doubleValue() : -1;
					int contentCnt = obj.containsKey("CONTENT_CNT") ?  (int) obj.get("CONTENT_CNT").isNumber().doubleValue() : -1;
					int shareCnt = obj.containsKey("SHARE_CNT") ?  (int) obj.get("SHARE_CNT").isNumber().doubleValue() : -1;
					int favoritesCnt = obj.containsKey("FAVORITES_CNT") ?  (int) obj.get("FAVORITES_CNT").isNumber().doubleValue() : -1;
					int pcCnt = obj.containsKey("PC_CNT") ?  (int) obj.get("PC_CNT").isNumber().doubleValue() : -1;
					int mobileCnt = obj.containsKey("MOBILE_CNT") ?  (int) obj.get("MOBILE_CNT").isNumber().doubleValue() : -1;
					this.table.addRow(Color.WHITE
						, index++	
						, startDate
						, endDate
						, title
						, cnt
						, removeCnt
						, contentCnt
						, shareCnt
						, favoritesCnt
						, pcCnt
						, mobileCnt
					);
					
					HashMap<String, Object> map1 = new HashMap<>();
					HashMap<String, Object> map2 = new HashMap<>();
					HashMap<String, Object> map3 = new HashMap<>();
					HashMap<String, Object> map4 = new HashMap<>();
					HashMap<String, Object> map5 = new HashMap<>();
					map1.put("name", title);
					map1.put("value", cnt);
					map2.put("name", title);
					map2.put("value", removeCnt);					
					map3.put("name", title);
					map3.put("value", contentCnt);
					map4.put("name", title);
					map4.put("value", shareCnt);
					map5.put("name", title);
					map5.put("value", favoritesCnt);
					
					chartDataList1.add(map1);
					chartDataList2.add(map2);
					chartDataList3.add(map3);
					chartDataList4.add(map4);
					chartDataList5.add(map5);
				}
				
				if(status.getSelectedIndex() == 1) {
					drawChart(chartDataList2);
				}else if(status.getSelectedIndex() == 2) {
					drawChart(chartDataList3);
				}else if(status.getSelectedIndex() == 3) {
					drawChart(chartDataList4);
				}else if(status.getSelectedIndex() == 4) {
					drawChart(chartDataList5);
				}else {
					drawChart(chartDataList1);
				}
					
			}
		});
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
