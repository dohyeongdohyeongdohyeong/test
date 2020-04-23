package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.contents;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.AbtractOtherDepartmentMainContents;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.OtherDepartmentMainEditor;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class OtherDepartmentContentListDetail extends AbtractOtherDepartmentMainContents {

	private MaterialPanel panel;
	private MaterialLabel cotLabel;
	private MaterialLabel cidLabel;
	private MaterialLabel sectionTitle;
	private SelectionPanel selectStatus;
	private SelectionPanel selectStatus1;
	private MaterialLabel tag1;
	private ContentTable table;
	private AreaComponent areaCompo;
	private OtherDepartmentMainEditor otherDepartmentMainEditor;
	private String COMP_ID;
	private MaterialIcon courseIcon5;
	private String otdId;
	private String manId;
	protected String odmId;

	public OtherDepartmentContentListDetail(MaterialExtentsWindow materialExtentsWindow, OtherDepartmentMainEditor otherDepartmentMainEditor) {
		super(materialExtentsWindow);
		this.otherDepartmentMainEditor = otherDepartmentMainEditor;
	}

	@Override
	public void init() {
		super.init();
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("100%");
		this.setTitle("컨텐츠 리스트 영역 상세");
		buildContent();
	}

	private void buildContent() {

		panel =  new MaterialPanel();
		panel.setPadding(20);
		this.add(panel);
		
		//첫 줄
		MaterialRow row1 = addRow(panel);
		addLabel(row1, "노출 컨텐츠 설정", TextAlign.CENTER, Color.GREY_LIGHTEN_3,"s3");
		addLabel(row1, "  (*) 부서에서 관리하는 컨텐츠 기준으로 노출", TextAlign.LEFT, Color.WHITE, "s6");
	
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("DB 컨텐츠", 1);
		map.put("기사 컨텐츠", 0);
		this.selectStatus1 = addSelectionPanel(row1, "s3", TextAlign.CENTER, map);
		this.selectStatus1.setSelectionOnSingleMode("기사 컨텐츠");
		this.selectStatus1.addStatusChangeEvent(event->{
			
			JSONObject jObj = new JSONObject();
			jObj.put("cmd", new JSONString("UPDATE_COMP_TITLE_VIEW"));
			jObj.put("MODE", new JSONNumber((int) selectStatus1.getSelectedValue()));
			jObj.put("ODM_ID", new JSONString(odmId));

			executeBusiness(jObj);
			
		});
			
		//두번째 줄
		MaterialRow row2 = addRow(panel);
		addLabel(row1, "정렬 설정", TextAlign.CENTER, Color.GREY_LIGHTEN_3,"s3");
		addLabel(row1, "  (*) 인기순 : PV누적 수 기준으로 리스트 노출", TextAlign.LEFT, Color.WHITE, "s6");
	
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("최신순(갱신순)", "0");
		map1.put("인기순", "1");
		this.selectStatus = addSelectionPanel(row1, "s3", TextAlign.CENTER, map1);
		this.selectStatus.setSelectionOnSingleMode("최신순(갱신순)");
		this.selectStatus.addStatusChangeEvent(event->{
			
			JSONObject jObj = new JSONObject();
			jObj.put("cmd", new JSONString("UPDATE_COMP_CONNECT_MORE_VIEW"));
			jObj.put("MODE", new JSONString((String) selectStatus.getSelectedValue()));
			jObj.put("ODM_ID", new JSONString(odmId));
			
			executeBusiness(jObj);
		});
		
	}

	@Override
	public void loading(boolean loadFlag) {
		super.loading(loadFlag);
	}

	@Override
	public void setReadOnly(boolean readFlag) {}

	private String otdId() {
		this.otdId = (String) getWindow().getValueMap().get("OTD_ID");
		Console.log("OtherDepartmentContentListDetail.OTD_ID :: " + this.otdId);
		return this.otdId;
	}

	@Override
	public void loadData() {

		String cmd = "GET_OTHER_DEPARTMENT_CONTENT_LIST";
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString(cmd)); 
		String otdId = otdId();
		if (otdId != null) {
			parameterJSON.put("otdId", new JSONString(otdId));
		}

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
	
			@Override
			public void call(Object param1, String param2, Object param3) {
	

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				Console.log("OtherDepartmentContentListDetail.loadData().GET_OTHER_DEPARTMENT_CONTENT_LIST.call() :: " + resultObj.toString());

				if (processResult.equals("success")) {
		
					JSONObject contentObject = (JSONObject) resultObj.get("body").isObject().get("result").isObject().get("contents").isArray().get(0).isObject();
					double isTitleView = contentObject.get("VIEW_TITLE").isNumber().doubleValue();
					double isConnectMoreView = Double.parseDouble(contentObject.get("VIEW_CONNECT_MORE").isString().toString().replaceAll("\"", ""));
					odmId = contentObject.get("ODM_ID").isString().toString().replaceAll("\"", "");
					
					if (isTitleView == 0) {
						selectStatus1.setSelectionOnSingleMode("기사 컨텐츠");
					}else {
						selectStatus1.setSelectionOnSingleMode("DB 컨텐츠");
					}
					
					if (isConnectMoreView == 0) {
						selectStatus.setSelectionOnSingleMode("최신순(갱신순)");
					}else {
						selectStatus.setSelectionOnSingleMode("인기순");
					}
					
				}
			}

		});
		
	}

}