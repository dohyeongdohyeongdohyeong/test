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
import com.google.gwt.user.client.Window;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.event.dashboard.EventDashboardMain;
import kr.or.visitkorea.admin.client.manager.event.dashboard.model.EventMasterModel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.dnd.client.util.StringUtil;
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


public class EventJoinDetail extends EventDashboardBasePanel {

	private XYChart chart;
	private ContentTable table;
	private MaterialLabel countLabel;
	private MaterialTextBox gameMethod;
	private MaterialPanel chartPanel;
	private EventMasterModel model ;
	private MaterialComboBox status;
	private MaterialPanel tablePanel ;
	private MaterialComboBox<Object> subEvtCombo;
	private MaterialButton btn1;
	private MaterialButton btn2;
	
	private String subEvtId;
	private List<HashMap<String, Object>> chartDataList1;
	private List<HashMap<String, Object>> chartDataList2;
	private List<HashMap<String, Object>> chartDataList3;

	
	private int index;
	private int totcnt;
	private int offset;
	
	public EventJoinDetail(EventDashboardMain host) {
		super(host);
		this.model = host.getModel();
		init2();
	}

	@Override
	public void init() {
		
	}

	public void init2() {
		this.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		this.setHeight("660px");

		MaterialLabel titleLabel = new MaterialLabel(model.getTitle());
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setLineHeight(46.25);
		titleLabel.setHeight("46.25px");
		MaterialColumn col = new MaterialColumn();
		col.add(titleLabel);
		this.add(col);
		this.add(createBackIcon()); 
		
		this.add(getEventInfoPanel());

		chartPanel = new MaterialPanel();
		chartPanel.setWidth("100%");
		chartPanel.setHeight("250px");
		this.add(chartPanel);
		
		this.add(getButtonPanel());
		
		tablePanel = new MaterialPanel();
		tablePanel.add(getBaseTablePanel());
		
		this.add(tablePanel);

	} 

	private MaterialPanel getButtonPanel() {
		MaterialPanel buttonPanel = new MaterialPanel();
		btn1 = new MaterialButton("참여통계");
		btn2 = new MaterialButton("참여정보");
		
		
		MaterialButton btnXlsDn = new MaterialButton("엑셀 다운로드");
		btnXlsDn.setLeft(10);
		btnXlsDn.setFloat(Float.LEFT);
		btnXlsDn.addClickHandler(e->{
			String downurl = "";
			if(Color.GREY.equals(btn1.getBackgroundColor())) {
				downurl = "./call?cmd=FILE_DOWNLOAD_XLS&select_type=event_result_join_user";
			}else {
				downurl = "./call?cmd=FILE_DOWNLOAD_XLS&select_type=event_result_join";
			}
				
			StringBuffer  sb = new StringBuffer();
			sb.append(downurl);
			sb.append("&subEvtId=");
			sb.append(subEvtId);
			sb.append("&evtId=");
			sb.append(model.getEvtId());
			
			Window.open(sb.toString(),"_self", "enabled");
		});

		buttonPanel.add(btnXlsDn);
		

		
		btn2.setBackgroundColor(Color.GREY);
		btn2.setFloat(Float.RIGHT);
		btn2.setMarginLeft(10);
		btn2.addClickHandler(e->{
			btn1.setBackgroundColor(Color.GREY);
			btn2.setBackgroundColor(Color.BLUE);
			tablePanel.clear();
			tablePanel.add(getInfoTablePanel());
			getJoinUserList(true);
		});
		
		buttonPanel.add(btn2);
		
		
		btn1.setFloat(Float.RIGHT);
		btn1.addClickHandler(e->{
			btn1.setBackgroundColor(Color.BLUE);
			btn2.setBackgroundColor(Color.GREY);
			tablePanel.clear();
			tablePanel.add(getBaseTablePanel());
			getJoinList();
		});
		buttonPanel.add(btn1);
		
		addRow(buttonPanel);
		
		return buttonPanel;
	}
	
	private MaterialPanel getEventInfoPanel() {
		MaterialPanel panel = new MaterialPanel();
		MaterialRow row = null;

		row = addRow(panel);
		addLabel(row, "이벤트 기간", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2"); //
		addInputText(row, model.getStartDate() + " ~ " + model.getEndDate(), "s4");
		
		addLabel(row, "당첨자 발표", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		addInputText(row, model.getAnnounceDate(), "s4");
		
		
		row = addRow(panel);
		addLabel(row, "진행상태", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		addInputText(row, model.getStatusNm(), "s4");
		
		
		addLabel(row, "프로그램 선택", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		
		subEvtCombo = new MaterialComboBox<Object>();
		subEvtCombo.setMargin(0);
		subEvtCombo.addValueChangeHandler(ee->{
			String str = subEvtCombo.getSingleValue().toString() ;
			String [] strArray = StringUtil.toStringArray(str, "|");
			subEvtId = strArray[0];
			gameMethod.setText(strArray[1]);
			btn1.setBackgroundColor(Color.BLUE);
			btn2.setBackgroundColor(Color.GREY);
			tablePanel.clear();
			tablePanel.add(getBaseTablePanel());
			getJoinList();
		});

		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_EVENT_SUB_LIST"));
		parameterJSON.put("evtId", new JSONString(String.valueOf(model.getEvtId())));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");

					int usrCnt = bodyResultObj.size();
					for(int i= 0;i< usrCnt;i++) {
						JSONObject obj = (JSONObject)bodyResultObj.get(i);
						subEvtCombo.addItem(obj.get("TITLE").isString().stringValue(), obj.get("SUB_EVT_ID").isString().stringValue() + "|" + obj.get("EVT_TYPE_NM").isString().stringValue() );
					}
					
					subEvtCombo.setSelectedIndex(0); 
					String str = subEvtCombo.getSingleValue().toString() ;
					String [] strArray = StringUtil.toStringArray(str, "|");
					gameMethod.setText(strArray[1]);
					subEvtId = strArray[0];
					getJoinList();
				}
			}
		});
		addColumn(row,subEvtCombo,"s4");
		

		
		
		row = addRow(panel);
		addLabel(row, "상태", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		List<Object> typeList = new ArrayList<>();
		typeList.add("참여건수");
		typeList.add("실참여건수");
		typeList.add("당첨건수");
		status = addCombobox(row, "s4", typeList);
		status.addValueChangeHandler(ee->{
			if(status.getSelectedIndex() == 1) {
				drawChart(chartDataList2);
			}else if(status.getSelectedIndex() == 2) {
				drawChart(chartDataList3);
			}else {
				drawChart(chartDataList1);
			}
		});
		
		addLabel(row, "게임방식", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		gameMethod = addInputText(row, "", "s4");
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
		table.appendTitle("일자", 200, TextAlign.CENTER);
		table.appendTitle("참여건수", 200, TextAlign.CENTER);
		table.appendTitle("실참여건수", 200, TextAlign.CENTER);
		table.appendTitle("당첨건수", 200, TextAlign.CENTER);
		table.appendTitle("미당첨건수", 200, TextAlign.CENTER);
		table.appendTitle("PC", 200, TextAlign.CENTER);
		table.appendTitle("모바일", 200, TextAlign.CENTER);

		
		tablePanel.add(table);
		
		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLDER);
		
		/*
		MaterialIcon moreIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		moreIcon.addClickHandler(event -> {
			if (offset + 20 > totcnt)
				return;
			getJoinList(false);
		});
		
		
		table.getButtomMenu().addIcon(moreIcon, "다음 20건", Float.RIGHT);
		table.getButtomMenu().addLabel(countLabel, Float.RIGHT);
		*/
		
		return tablePanel;
	}
	
	
	private MaterialPanel getInfoTablePanel() {

		MaterialPanel tablePanel = new MaterialPanel();
		tablePanel.setPadding(10);
		this.add(tablePanel);
		
		table = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setWidth("100%");
		table.setHeight(250);
		table.setLayoutPosition(Position.RELATIVE);
		table.setTop(-25);
		table.appendTitle("참여일시", 200, TextAlign.CENTER);
		table.appendTitle("이름", 200, TextAlign.CENTER);
		table.appendTitle("HP", 250, TextAlign.CENTER);
		table.appendTitle("당첨여부", 200, TextAlign.CENTER);
		table.appendTitle("경품명", 250, TextAlign.CENTER);
		table.appendTitle("참여방법", 250, TextAlign.CENTER);


		
		tablePanel.add(table);
		
		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLDER);
		
		MaterialIcon moreIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		moreIcon.addClickHandler(event -> {
			if (offset + DEFAULT_PAGE_NUM_ROWS > totcnt)
				return;
			getJoinUserList(false);
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
        //chart.scrollbarY = new Scrollbar();
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
	
	public void getJoinList() {

		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SELECT_EVENT_DASHBOARD_JOIN_LIST"));
		paramJson.put("subEvtId", new JSONString(subEvtId));

			
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				
				/*
				List<ContentTableRow> tableRowList   = this.table.getRowsList();
				for(ContentTableRow row : tableRowList ) {
					row.removeFromParent();
					row.clear();
				}
				*/
				
				JSONObject bodyObj = resultObj.get("body").isObject();
				JSONArray bodyResultArr = bodyObj.get("result").isArray();
				chartDataList1 = new ArrayList<HashMap<String, Object>>();
				chartDataList2 = new ArrayList<HashMap<String, Object>>();
				chartDataList3 = new ArrayList<HashMap<String, Object>>();
				
				for (int i = 0; i < bodyResultArr.size(); i++) {
					JSONObject obj = bodyResultArr.get(i).isObject();
					
					String dt = obj.containsKey("DT") ? obj.get("DT").isString().stringValue() : "";
					int cnt = obj.containsKey("CNT") ? (int) obj.get("CNT").isNumber().doubleValue() : -1;
					int dedupCnt = obj.containsKey("DEDUP_CNT") ?  (int) obj.get("DEDUP_CNT").isNumber().doubleValue() : -1;
					int winCnt = obj.containsKey("WIN_CNT") ?  (int) obj.get("WIN_CNT").isNumber().doubleValue() : -1;
					int mcnt = obj.containsKey("MCNT") ?  (int) obj.get("MCNT").isNumber().doubleValue() : -1;
					int pcnt = obj.containsKey("PCNT") ?  (int) obj.get("PCNT").isNumber().doubleValue() : -1;
					this.table.addRow(Color.WHITE
						, dt
						, cnt
						, dedupCnt
						, winCnt
						, (cnt - winCnt)
						, pcnt
						, mcnt
					);
					
					HashMap<String, Object> map1 = new HashMap<>();
					HashMap<String, Object> map2 = new HashMap<>();
					HashMap<String, Object> map3 = new HashMap<>();
					map1.put("name", dt);
					map1.put("value", cnt);
					map2.put("name", dt);
					map2.put("value", dedupCnt);					
					map3.put("name", dt);
					map3.put("value", winCnt);

					chartDataList1.add(map1);
					chartDataList2.add(map2);
					chartDataList3.add(map3);
				}
				
				if(status.getSelectedIndex() == 1) {
					drawChart(chartDataList2);
				}else if(status.getSelectedIndex() == 2) {
					drawChart(chartDataList3);
				}else {
					drawChart(chartDataList1);
				}
					
			}
		});
	}
	

	public void getJoinUserList(boolean bstart) {
		if (bstart) {
			offset = 0;
			index = 1;
			totcnt = 0;
			this.table.clearRows();
		} else offset += DEFAULT_PAGE_NUM_ROWS;
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SELECT_EVENT_DASHBOARD_JOIN_USER_LIST"));
		paramJson.put("subEvtId", new JSONString(subEvtId));
		paramJson.put("offset", new JSONNumber(this.offset));
			
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				/*
				List<ContentTableRow> tableRowList   = this.table.getRowsList();
				for(ContentTableRow row : tableRowList ) {
					row.removeFromParent();
					row.clear();
				}
				*/
				
				JSONObject bodyObj = resultObj.get("body").isObject();
				JSONArray bodyResultArr = bodyObj.get("result").isArray();

				int bodyResultCnt = (int) bodyObj.get("resultCnt").isObject().get("CNT").isNumber().doubleValue();
				this.countLabel.setText(bodyResultCnt + " 건");
				totcnt = bodyResultCnt;
				
				for (int i = 0; i < bodyResultArr.size(); i++) {
					JSONObject obj = bodyResultArr.get(i).isObject();
					
					String dt = obj.containsKey("DT") ? obj.get("DT").isString().stringValue() : "";
					String name = obj.containsKey("NAME") ? obj.get("NAME").isString().stringValue() : "";
					String tel = obj.containsKey("TEL") ?  obj.get("TEL").isString().stringValue() : "";
					String winYn = obj.containsKey("GFT_ID") ?  "당첨" : "미당첨";
					String giftNm = obj.containsKey("GFT_NM") ?  obj.get("GFT_NM").isString().stringValue() : "-";
					String deviceType = obj.containsKey("DEVICE_TYPE") ?  obj.get("DEVICE_TYPE").isString().stringValue() : "";
					this.table.addRow(Color.WHITE
						, dt
						, name
						, tel
						, winYn
						, giftNm
						, ("M".equals(deviceType) ? "모바일" : "PC" )
					);
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
	
	
	
	protected MaterialIcon createBackIcon() {
		MaterialIcon backIcon = new MaterialIcon(IconType.KEYBOARD_ARROW_LEFT);
		backIcon.setLayoutPosition(Position.ABSOLUTE);
		backIcon.setTooltip("목록으로");
		backIcon.setRight(30);
		backIcon.setTop(25);
		backIcon.setWidth("24");
		backIcon.setBorder("1px solid #e0e0e0");
		backIcon.addClickHandler(event -> {
			((EventDashboardMain)host).switchToListPanel();
		});
		return backIcon;
	}
	
	

}
