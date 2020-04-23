package kr.or.visitkorea.admin.client.manager.event;

import java.util.Date;
import java.util.HashMap;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.FlexJustifyContent;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.event.widgets.EventGantChart;
import kr.or.visitkorea.admin.client.manager.event.widgets.ProgressStatusPanel;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventContentsList extends AbstractContentPanel {
	public EventApplication host;
    private EventGantChart ganttChart;
	private ContentTable table;
	private MaterialLabel countLabel;
	private MaterialComboBox<Object> type;
	private MaterialTextBox keyword;
	private MaterialTextBox createDate;
	private MaterialTextBox startDate;
	private MaterialTextBox endDate;
	private int index;
	private int totcnt;
	private int offset;
	private boolean EventClickcheck;
	private int qryStatus = 1;
	private HashMap<Integer, ProgressStatusPanel> progressPanelMap;
	private boolean permission;
	
	static {
		MaterialDesignBase.injectCss(EventContentBundle.INSTANCE.eventCss());
	}
 
	public EventContentsList(MaterialExtentsWindow window) {
		super(window);
		qryList(true);
		EventClickcheck = true;
	}
	
	@Override
	public void init() {
		this.setLayoutPosition(Position.RELATIVE);
		this.setFloat(Float.LEFT);
		this.permission = Registry.getPermission(EventApplication.ADMIN_PERMISSION);
		
		ganttChart = new EventGantChart();
		ganttChart.setPadding(10);
		ganttChart.setHeight("300px");
		this.add(ganttChart);
		
		MaterialPanel progressPanel = new MaterialPanel();
		progressPanel.setDisplay(Display.FLEX);
		progressPanel.setFlexJustifyContent(FlexJustifyContent.CENTER);
		progressPanel.setFlexAlignItems(FlexAlignItems.CENTER);
		this.add(progressPanel);
		
		this.progressPanelMap = new HashMap<>();
		this.addProgressStatusPanel(progressPanel, 1, "작성중").setClicked(true);
		this.addProgressStatusPanel(progressPanel, 2, "승인대기");
		this.addProgressStatusPanel(progressPanel, 3, "진행대기");
		this.addProgressStatusPanel(progressPanel, 4, "진행중");
		this.addProgressStatusPanel(progressPanel, 5, "이벤트종료");
		this.addProgressStatusPanel(progressPanel, 6, "당첨자발표");
		this.addProgressStatusPanel(progressPanel, 7, "거절");
		
		MaterialPanel tablePanel = new MaterialPanel();
		tablePanel.setPadding(10);
		this.add(tablePanel);
		
		MaterialRow row = new MaterialRow();
		row.setMargin(0);
		row.setVisible(this.permission);
		tablePanel.add(row);
		
		this.type = addCombobox(row, "s1", "검색조건");
		this.type.addItem("제목");
		
		this.keyword = addTextbox(row, "s4", "검색어 입력");
		this.keyword.addKeyUpHandler(e -> {
			if (e.getNativeKeyCode() == 13) 
				this.qryList(true);
		});
		
		this.createDate = addInputDate(row, "s2", "생성일");
		this.startDate = addInputDate(row, "s2", "시작일");
		this.endDate = addInputDate(row, "s2", "종료일");
		
		table = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setWidth("100%");
		table.setHeight(this.permission ? 350 : 410);
		table.setLayoutPosition(Position.RELATIVE);
		table.setTop(-25);
		table.appendTitle("번호", 100, TextAlign.CENTER);
		table.appendTitle("구분", 198, TextAlign.CENTER);
		table.appendTitle("이벤트 제목", 400, TextAlign.CENTER);
		table.appendTitle("등록일", 120, TextAlign.CENTER);
		table.appendTitle("시작일", 120, TextAlign.CENTER);
		table.appendTitle("종료일", 120, TextAlign.CENTER);
		table.appendTitle("작성자", 200, TextAlign.CENTER);
		table.appendTitle("당첨자 발표", 200, TextAlign.CENTER);
		tablePanel.add(table);
		
		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLDER);

		MaterialIcon moreIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		moreIcon.addClickHandler(event -> {
			
			Console.log("offset" +offset);
			Console.log("totcnt" +totcnt);
			if (offset + 20 > totcnt)
				return;
			qryList(false);
		});
		
		table.getButtomMenu().addIcon(moreIcon, "다음 20건", Float.RIGHT);
		table.getButtomMenu().addLabel(countLabel, Float.RIGHT);

		MaterialIcon searchIcon = new MaterialIcon(IconType.SEARCH);
		searchIcon.addClickHandler(event -> {
			qryList(true);
		});
		
		MaterialIcon addIcon = new MaterialIcon(IconType.ADD);
		addIcon.addClickHandler(event -> {
			EventContentsTree contentsTree = (EventContentsTree) getMaterialExtentsWindow().getContentPanel(1);
			contentsTree.setcheckfirst(true);
			contentsTree.initialize();
			contentsTree.setEvtId(IDUtil.uuid());
			contentsTree.setCotId(IDUtil.uuid());
			contentsTree.setEventStatus(EventStatus.WRITING);
			contentsTree.setContentTitle("신규 이벤트 생성");
			contentsTree.visibleProgressChangeBtn(true, false);
			contentsTree.contentLoadData();
			getMaterialExtentsWindow().goContentSlider(getMaterialExtentsWindow().getWidth() * 1 * -1);
		});
		table.getTopMenu().addIcon(searchIcon, "검색", Float.RIGHT, "26px", false);
		table.getTopMenu().addIcon(addIcon, "신규 이벤트 생성", Float.RIGHT, "26px", false);
	} 

	protected String convertDateFormat(String strDate) {
		if (strDate.equals("") || strDate == null) return "";
		Date date = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.s").parse(strDate);
		return DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
	}
	
	public void qryList(boolean bstart) {
		this.ganttChart.clearData();
		if (bstart) {
			offset = 0;
			index = 1;
			totcnt = 0;
			this.table.clearRows();
		} else offset += 20;
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SELECT_EVENT_LIST"));
		paramJson.put("type", new JSONNumber(this.type.getSelectedIndex()));
		paramJson.put("keyword", new JSONString(this.keyword.getValue()));
		paramJson.put("createDate", new JSONString(this.createDate.getValue()));
		paramJson.put("startDate", new JSONString(this.startDate.getValue()));
		paramJson.put("endDate", new JSONString(this.endDate.getValue()));
		paramJson.put("offset", new JSONNumber(this.offset));
		paramJson.put("status", new JSONNumber(this.qryStatus));
		paramJson.put("adminPermission", new JSONString(EventApplication.ADMIN_PERMISSION));
		paramJson.put("userPermission", new JSONString(EventApplication.USER_PERMISSION));
		paramJson.put("usrId", new JSONString(Registry.getUserId()));
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyObj = resultObj.get("body").isObject();
				JSONArray bodyResultArr = bodyObj.get("result").isArray();
				JSONArray bodyResultStatusCnt = bodyObj.get("resultStatusCnt").isArray();
				int bodyResultCnt = (int) bodyObj.get("resultCnt").isObject().get("CNT").isNumber().doubleValue();
				
				this.countLabel.setText(bodyResultCnt + " 건");
				this.totcnt = bodyResultCnt;
				this.setProgressCount(bodyResultStatusCnt);

				for (int i = 0; i < bodyResultArr.size(); i++) {
					JSONObject obj = bodyResultArr.get(i).isObject();
					
					String evtId = obj.containsKey("EVT_ID") ? obj.get("EVT_ID").isString().stringValue() : "";
					String cotId = obj.containsKey("COT_ID") ? obj.get("COT_ID").isString().stringValue() : "";
					String title = obj.containsKey("TITLE") ? obj.get("TITLE").isString().stringValue() : "";
					String createDate = convertDateFormat(obj.containsKey("CREATE_DATE") ? obj.get("CREATE_DATE").isString().stringValue() : "");
					String startDate = convertDateFormat(obj.containsKey("START_DATE") ? obj.get("START_DATE").isString().stringValue() : "");
					String endDate = convertDateFormat(obj.containsKey("END_DATE") ? obj.get("END_DATE").isString().stringValue() : "");
					int status = obj.containsKey("STATUS") ? (int) obj.get("STATUS").isNumber().doubleValue() : -1;
					String createUser = obj.containsKey("STF_ID") ? obj.get("STF_ID").isString().stringValue() : "";
					
					addTableRow(evtId, cotId, title, createDate, startDate, endDate, status, createUser);
					addChartRow(title, startDate, endDate);
				}
			}
		});
	}
	
	private void setProgressCount(JSONArray array) {
		this.progressPanelMap.entrySet().forEach(item -> {
			ProgressStatusPanel panel = item.getValue();
			panel.setText(panel.getTitle());
		});

		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.get(i).isObject();
			int status = obj.containsKey("STATUS") ? (int) obj.get("STATUS").isNumber().doubleValue() : -1;
			int count = obj.containsKey("CNT") ? (int) obj.get("CNT").isNumber().doubleValue() : -1;
			
			ProgressStatusPanel panel = this.progressPanelMap.get(status);
			panel.setText(panel.getTitle() + "(" + count + ")");
		}
	}
	
	private void addTableRow(String evtId, String cotId, String title, String createDate, String startDate, String endDate, int status, String createUser) {
		String statusStr = null;
		
		if (status == 1) {
			statusStr = "작성중";
		} else if (status == 2) {
			statusStr = "승인대기";
		} else if (status == 3) {
			statusStr = "진행대기";
		} else if (status == 4) {
			statusStr = "진행중";
		} else if (status == 5) {
			statusStr = "이벤트 종료";
		} else if (status == 6) {
			statusStr = "당첨자 발표";
		} else if (status == 7) {
			statusStr = "거절";
		}
		
		ContentTableRow tableRow = this.table.addRow(Color.WHITE, new int[] {2}
			, index++
			, statusStr
			, title
			, createDate
			, startDate
			, endDate
			, createUser
		);
		tableRow.put("status", status);
		tableRow.addClickHandler(e -> {
			ContentTableRow ctr = (ContentTableRow) e.getSource();
			if (ctr.getSelectedColumn() == 2) {
				
				if(EventClickcheck == true) {
					EventClickcheck = false;
				EventContentsTree contentsTree = (EventContentsTree) getMaterialExtentsWindow().getContentPanel(1);
				//당첨자발표 링크 동적변경을 위해  initialize 앞에 evtId 셋팅 by ymjang
				contentsTree.setEvtParamInitialize(cotId, evtId);
				contentsTree.initialize();
				contentsTree.setCotId(cotId);
				contentsTree.setEvtId(evtId);
				contentsTree.setEventStatus(EventStatus.values()[(int) tableRow.get("status") - 1]);
				contentsTree.setContentTitle(title);
				contentsTree.contentLoadData();
				getMaterialExtentsWindow().goContentSlider(getMaterialExtentsWindow().getWidth() * 1 * -1);
				}
			}
		});
	}
	
	
	public ContentTable getTable() {
		return table;
	}

	private MaterialComboBox<Object> addCombobox(MaterialWidget parent, String grid, String label) {
		MaterialComboBox<Object> combo = new MaterialComboBox<Object>();
		combo.setLabel(label);
		combo.setMargin(0);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(combo);
		parent.add(col);
		return combo;
	}

	private MaterialTextBox addTextbox(MaterialWidget parent, String grid, String label) {
		MaterialTextBox tbox = new MaterialTextBox();
		tbox.setMargin(0);
		tbox.setLabel(label);
		tbox.getElement().getFirstChildElement().getStyle().setMargin(0, Unit.PX);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(tbox);
		parent.add(col);
		return tbox;
	}
	
	private MaterialTextBox addInputDate(MaterialWidget parent, String grid, String label) {
		MaterialTextBox input = new MaterialTextBox();
		input.setMargin(0);
		input.setType(InputType.DATE);
		input.setLabel(label);
		input.setText(" ");
		input.setText("");
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(input);
		parent.add(col);
		return input;
	}
	
	private ProgressStatusPanel addProgressStatusPanel(MaterialWidget parent, int key, String text) {
		ProgressStatusPanel panel = new ProgressStatusPanel();
		panel.setText(text);
		panel.setTitle(text);
		panel.addClickHandler(event -> {
			this.qryStatus = key;
			qryList(true);
		});
		this.progressPanelMap.put(key, panel);
		parent.add(panel);
		return panel;
	}
	
	private void addChartRow(String name, String fromDate, String toDate) {
		ganttChart.addData(name, fromDate, toDate);
	}
	
	public void setEventClickcheck(boolean click) {
		EventClickcheck = click;
	}
	
}
