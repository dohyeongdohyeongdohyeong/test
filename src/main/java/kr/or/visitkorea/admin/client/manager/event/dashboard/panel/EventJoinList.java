package kr.or.visitkorea.admin.client.manager.event.dashboard.panel;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.event.dashboard.EventDashboardJoinApplication;
import kr.or.visitkorea.admin.client.manager.event.dashboard.EventDashboardMain;
import kr.or.visitkorea.admin.client.manager.event.dashboard.model.EventMasterModel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;


public class EventJoinList extends EventDashboardBasePanel {

	private ContentTable table;
	private MaterialLabel countLabel;
	private MaterialComboBox<Object> status;
	private MaterialInput startDate;
	private MaterialInput endDate;
	private int index;
	private int totcnt;
	private int offset;
	private int statusType = -1;

	private EventDashboardMain host;
	

 
	public EventJoinList(EventDashboardMain host) {
		super(host);
		this.host = host;
		qryList(true);
	}
	
	@Override
	public void init() {

		this.setFloat(Float.LEFT);

		MaterialPanel tablePanel = new MaterialPanel();
		tablePanel.setPadding(10);
		this.add(tablePanel);
		
		MaterialRow row = new MaterialRow();
		row.setMargin(0);
		tablePanel.add(row);

		this.startDate = addInputDate(row, "s2", "시작일");
		this.endDate = addInputDate(row, "s2", "마감일");

		this.status = addCombobox(row, "s2", "");
		this.status.addItem("진행상태",-1);
		this.status.addItem("진행중",4);
		this.status.addItem("종료",5);
		this.status.addItem("당첨자발표",6);
		this.status.setSelectedIndex(0);
		
		table = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setWidth("100%");
		table.setHeight(500);
		table.setLayoutPosition(Position.RELATIVE);
		table.setTop(-25);
		table.appendTitle("번호", 100, TextAlign.CENTER);
		table.appendTitle("시작일", 120, TextAlign.CENTER);
		table.appendTitle("종료일", 120, TextAlign.CENTER);
		table.appendTitle("이벤트 명", 300, TextAlign.CENTER);
		table.appendTitle("당첨자발표", 150, TextAlign.CENTER);
		table.appendTitle("진행상태", 200, TextAlign.CENTER);
		table.appendTitle("적용게임", 250, TextAlign.CENTER);
		table.appendTitle("이벤트수", 200, TextAlign.CENTER);
		
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

		MaterialIcon searchIcon = new MaterialIcon(IconType.SEARCH);
		searchIcon.addClickHandler(event -> {
			qryList(true);
		});

		table.getTopMenu().addIcon(searchIcon, "검색", Float.RIGHT, "26px", false);

	} 

	
	public void qryList(boolean bstart) {

		if (bstart) {
			offset = 0;
			index = 1;
			totcnt = 0;
			this.table.clearRows();
		} else offset += DEFAULT_PAGE_NUM_ROWS;
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SELECT_EVENT_DASHBOARD_LIST"));
		if(this.status.getSingleValue() != null) {
			statusType = Integer.parseInt(String.valueOf(this.status.getSingleValue()));
		}
		
		
		paramJson.put("startDate", new JSONString(this.startDate.getValue()));
		paramJson.put("endDate", new JSONString(this.endDate.getValue()));
		paramJson.put("status", new JSONNumber(statusType));
		paramJson.put("offset", new JSONNumber(this.offset));
		paramJson.put("adminPermission", new JSONString(EventDashboardJoinApplication.ADMIN_PERMISSION));
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
				for (int i = 0; i < bodyResultArr.size(); i++) {
					JSONObject obj = bodyResultArr.get(i).isObject();
					
					String evtId = obj.containsKey("EVT_ID") ? obj.get("EVT_ID").isString().stringValue() : "";
					String cotId = obj.containsKey("COT_ID") ? obj.get("COT_ID").isString().stringValue() : "";
					String title = obj.containsKey("TITLE") ? obj.get("TITLE").isString().stringValue() : "";
					String startDate = convertDateFormat(obj.containsKey("START_DATE") ? obj.get("START_DATE").isString().stringValue() : "");
					String endDate = convertDateFormat(obj.containsKey("END_DATE") ? obj.get("END_DATE").isString().stringValue() : "");
					String announceDate = convertDateFormat(obj.containsKey("ANNOUNCE_DATE") ? obj.get("ANNOUNCE_DATE").isString().stringValue() : "");
					int status = obj.containsKey("STATUS") ? (int) obj.get("STATUS").isNumber().doubleValue() : -1;
					int evtCnt = obj.containsKey("EVENT_CNT") ? (int) obj.get("EVENT_CNT").isNumber().doubleValue() : -1;
					String gamesNm = obj.containsKey("GAMES_NM") ? obj.get("GAMES_NM").isString().stringValue() : "";
					
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
					
					addTableRow(evtId, cotId, title, startDate, endDate, announceDate, status, statusStr, evtCnt, gamesNm);

				}
			}
		});
	}
	

	private void addTableRow(String evtId, String cotId, String title, String startDate, String endDate, String announceDate, int status, String statusNm, int evtCnt, String gamesNm) {
		ContentTableRow tableRow = this.table.addRow(Color.WHITE, new int[] {3}
			, index++
			, startDate
			, endDate
			, title
			, announceDate
			, statusNm
			, gamesNm
			, evtCnt

		);
		tableRow.addClickHandler(e -> {
			ContentTableRow ctr = (ContentTableRow) e.getSource();
			if(!"".equals(gamesNm)) {
				if (ctr.getSelectedColumn() == 3) {
					EventMasterModel model = new EventMasterModel();
					model.setEvtId(evtId);
					model.setCotId(cotId);
					model.setTitle(title);
					model.setStartDate(startDate);
					model.setEndDate(endDate);
					model.setAnnounceDate(announceDate);
					model.setStatus(String.valueOf(status));
					model.setStatusNm(statusNm);
					host.switchToDetailPanel(model);
				}
			}
		});
	}
	
	

}
