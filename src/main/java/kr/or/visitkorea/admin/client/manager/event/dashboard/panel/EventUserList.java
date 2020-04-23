package kr.or.visitkorea.admin.client.manager.event.dashboard.panel;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.event.dashboard.EventDashboardUserApplication;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;


public  class EventUserList extends EventDashboardBasePanel implements IEventConstant {

	private ContentTable table;
	private MaterialLabel countLabel;
	private MaterialComboBox<Object> searchWhere;
	private MaterialTextBox searchWord;
	
	private MaterialInput startDate;
	private MaterialInput endDate;
	private int index;
	private int totcnt;
	private int offset;
	
	private EventDashboardUserApplication app;
 
	public EventUserList(MaterialExtentsWindow window, EventDashboardUserApplication app) {
		super(window);
		this.app = app;
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
		
		
		List<Object> searchWhereList = new ArrayList<Object>();
		searchWhereList.add("이벤트명");
		searchWhereList.add("닉네임");
		searchWhereList.add("HP뒷자리");
		searchWhereList.add("이름");
		this.searchWhere = addCombobox(row, "s2", searchWhereList);
		
		this.searchWord = addInputText(row, "검색어를 입력해주세요.", "s4", false);

		
		table = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setWidth("100%");
		table.setHeight(500);
		table.setLayoutPosition(Position.RELATIVE);
		table.setTop(-25);
		table.appendTitle("번호", 100, TextAlign.CENTER);
		table.appendTitle("시작일", 120, TextAlign.CENTER);
		table.appendTitle("종료일", 120, TextAlign.CENTER);
		table.appendTitle("이벤트 명", 300, TextAlign.CENTER);
		table.appendTitle("닉네임(별명)", 150, TextAlign.CENTER);
		table.appendTitle("HP", 200, TextAlign.CENTER);
		table.appendTitle("이름", 250, TextAlign.CENTER);
		table.appendTitle("참여건수", 100, TextAlign.CENTER);
		table.appendTitle("당첨건수", 100, TextAlign.CENTER);
		
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
		paramJson.put("cmd", new JSONString("SELECT_EVENT_DASHBOARD_USER_LIST"));

		paramJson.put("startDate", new JSONString(this.startDate.getValue()));
		paramJson.put("endDate", new JSONString(this.endDate.getValue()));
		paramJson.put("searchWhere", new JSONNumber(searchWhere.getSelectedIndex()));
		paramJson.put("searchWord", new JSONString(searchWord.getText()));
		paramJson.put("offset", new JSONNumber(this.offset));
		paramJson.put("adminPermission", new JSONString(EventDashboardUserApplication.ADMIN_PERMISSION));
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
					
					String tel = obj.containsKey("TEL") ? obj.get("TEL").isString().stringValue() : "";
					String snsUsrName = obj.containsKey("SNS_USR_NAME") ? obj.get("SNS_USR_NAME").isString().stringValue() : "";
					String snsIdentify = obj.containsKey("SNS_IDENTIFY") ? obj.get("SNS_IDENTIFY").isString().stringValue() : "";
					String title = obj.containsKey("TITLE") ? obj.get("TITLE").isString().stringValue() : "";
					String name = obj.containsKey("NAME") ? obj.get("NAME").isString().stringValue() : "";
					String startDate = convertDateFormat(obj.containsKey("START_DATE") ? obj.get("START_DATE").isString().stringValue() : "");
					String endDate = convertDateFormat(obj.containsKey("END_DATE") ? obj.get("END_DATE").isString().stringValue() : "");
					int joinCnt = obj.containsKey("JOIN_CNT") ? (int) obj.get("JOIN_CNT").isNumber().doubleValue() : -1;
					int winCnt = obj.containsKey("WIN_CNT") ? (int) obj.get("WIN_CNT").isNumber().doubleValue() : -1;
					ContentTableRow tableRow =  this.table.addRow(Color.WHITE
							,index++
							, startDate
							, endDate
							, title
							, snsUsrName
							, tel
							, name
							, joinCnt
							, winCnt
					);
					tableRow.addClickHandler(e -> {
						ContentTableRow ctr = (ContentTableRow) e.getSource();
						Map<String, Object> parametersMap = new HashMap<String, Object>();
						if(ctr.getSelectedColumn() == 4 && snsIdentify != "") {//파트너정보 상세 보기
							Map<String, Object> valueMap = new HashMap<String, Object>();
							MaterialLink link =  new MaterialLink();
							link.setText("일반관리");
							valueMap.put("LINK", link);
							valueMap.put("APP_KEY", "WINDOW_MEMBER");
							valueMap.put("DIVISION_NAME", "회원 관리");
							valueMap.put("WIN_WIDTH", 1500);
							valueMap.put("WIN_HEIGHT", 700);
							
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("SNS_ID",snsIdentify);
							app.getAppView().openManagementWindow(valueMap, params);

						}
						
					});

				}
			}
		});
	}

	
	
	
	

}
