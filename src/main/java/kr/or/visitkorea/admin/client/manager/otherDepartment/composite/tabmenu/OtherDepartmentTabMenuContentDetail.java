package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.tabmenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.AbtractOtherDepartmentMainContents;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.OtherDepartmentMainEditor;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.SearchBodyWidget;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.SearchTagWidgetForTabMenu;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListLabelCell;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListRow;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.VisitKoreaListCell;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class OtherDepartmentTabMenuContentDetail extends AbtractOtherDepartmentMainContents {

	private MaterialPanel panel;
	private MaterialLabel cotLabel;
	private MaterialLabel cidLabel;
	private MaterialLabel sectionTitle;
	private SelectionPanel selectStatus;
	private SelectionPanel selectStatus1;
	private MaterialLabel tag1;
	private AreaComponent areaCompo;
	private OtherDepartmentMainEditor otherDepartmentMainEditor;
	private String COMP_ID;
	private MaterialIcon courseIcon5;
	private String otdId;
	private SearchTagWidgetForTabMenu search;
	private SearchBodyWidget searchBody;
	private String odmId;

	public OtherDepartmentTabMenuContentDetail(MaterialExtentsWindow materialExtentsWindow, OtherDepartmentMainEditor otherDepartmentMainEditor, String otdId) {
		super(materialExtentsWindow);
		this.otdId = otdId;
		this.otherDepartmentMainEditor = otherDepartmentMainEditor;
		Registry.put("OtherDepartmentTabMenuContentDetail", this);
		buildContent();
	}

	@Override
	public void init() {
		super.init();
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("100%");
		this.setTitle("탭메뉴 영역 컨텐츠 상세");
	}

	private void buildContent() {

		panel =  new MaterialPanel();
		panel.setPadding(20);
		this.add(panel);
		
		//첫 줄
		MaterialRow row1 = addRow(panel);
		row1.setMarginBottom(0);
		
		addLabel(row1, "노출 설정", TextAlign.CENTER, Color.GREY_LIGHTEN_3,"s3");
		this.sectionTitle = addLabel(row1, "", TextAlign.CENTER, Color.WHITE, "s6");
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("노출", 1);
		map.put("비노출", 0);
		this.selectStatus1 = addSelectionPanel(row1, "s3", TextAlign.CENTER, map);
		this.selectStatus1.setSelectionOnSingleMode("비노출");
		this.selectStatus1.addStatusChangeEvent(event->{
			
			JSONObject jObj = new JSONObject();
			jObj.put("cmd", new JSONString("UPDATE_COMP_TITLE_VIEW"));
			jObj.put("MODE", new JSONNumber((int) selectStatus1.getSelectedValue()));
			jObj.put("ODM_ID", new JSONString(odmId));

			executeBusiness(jObj);
			
		});
			
		//두번째 줄
		addLabel(row1, "정렬 설정", TextAlign.CENTER, Color.GREY_LIGHTEN_3,"s3");
		this.tag1 = addLabel(row1, "  (*) 인기순 : PV누적 수 기준으로 리스트 노출", TextAlign.LEFT, Color.WHITE, "s6");
	
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("최신순(갱신순)", "0");
		map1.put("인기순", "1");
		this.selectStatus = addSelectionPanel(row1, "s3", TextAlign.CENTER, map1);
		this.selectStatus.setSelectionOnSingleMode("일반");
		this.selectStatus.addStatusChangeEvent(event->{
			
			JSONObject jObj = new JSONObject();
			jObj.put("cmd", new JSONString("UPDATE_COMP_CONNECT_MORE_VIEW"));
			jObj.put("MODE", new JSONString((String) selectStatus.getSelectedValue()));
			jObj.put("ODM_ID", new JSONString(odmId));

			executeBusiness(jObj);
			
		});
		
		search = new SearchTagWidgetForTabMenu(otdId, getWindow());
		search.setHeaderVisible(false);
		search.setPadding(20);
		
		searchBody = new SearchBodyWidget();
		searchBody.setHeaderTitleVisible(false);
		searchBody.setMarginLeft(30);
		searchBody.setWidth("820px");
		searchBody.setHeight("260px");
		
		MaterialLink upLink = new MaterialLink(IconType.KEYBOARD_ARROW_UP);
		MaterialLink dnLink = new MaterialLink(IconType.KEYBOARD_ARROW_DOWN);
		MaterialLink rmLink = new MaterialLink(IconType.REMOVE);
		
		searchBody.addLink(dnLink, com.google.gwt.dom.client.Style.Float.RIGHT);
		searchBody.addLink(upLink, com.google.gwt.dom.client.Style.Float.RIGHT);
		searchBody.addLink(rmLink, com.google.gwt.dom.client.Style.Float.LEFT);

		search.setBody(searchBody);
		searchBody.setSearch(search);
		searchBody.addRowAll(new ArrayList<TagListRow>());
		
		this.add(search);
		this.add(searchBody);
		
		rmLink.addClickHandler(event->{
			searchBody.removeSelectedPanel();
		});
		
		upLink.addClickHandler(event->{
			searchBody.selectedPanelMoveUp();
		});
		
		dnLink.addClickHandler(event->{
			searchBody.selectedPanelMoveDown();
		});
		
	}
	
	private void addRows(List<TagListRow> listRows, String tagName, int cnt) {
		
		List<VisitKoreaListCell> rowInfoList = new ArrayList<VisitKoreaListCell>();
		rowInfoList.add(new TagListLabelCell(tagName, Float.LEFT, "50%",  "50", 50, FontWeight.BOLDER, true, TextAlign.CENTER));
		rowInfoList.add(new TagListLabelCell(cnt+"", Float.LEFT, "50%",  "50", 50, FontWeight.BOLDER, true, TextAlign.CENTER));
		listRows.add(new TagListRow(rowInfoList));
	}
	
	private void addRow(List<TagListRow> listRows, String tagName, int cnt) {
		
		List<VisitKoreaListCell> rowInfoList = new ArrayList<VisitKoreaListCell>();
		rowInfoList.add(new TagListLabelCell(tagName, Float.LEFT, "50%",  "50", 50, FontWeight.BOLDER, true, TextAlign.CENTER));
		rowInfoList.add(new TagListLabelCell(cnt+"", Float.LEFT, "50%",  "50", 50, FontWeight.BOLDER, true, TextAlign.CENTER));
		searchBody.addRow(new TagListRow(rowInfoList));
	}

	@Override
	public void loading(boolean loadFlag) {
		super.loading(loadFlag);
	}

	@Override
	public void setReadOnly(boolean readFlag) {}

	public void setAreaComponent(AreaComponent aac) {
		
	}
	@Override
	public void loadData() {

		String cmd = "GET_OTHER_DEPARTMENT_TAB_MENU_CONTENT";
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString(cmd)); 
		String otdId = otdId();
		
		search.setOtdId(otdId);
		
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
				
				if (processResult.equals("success")) {
		
					JSONObject contentObject = (JSONObject) resultObj.get("body").isObject().get("result").isObject().get("contents").isArray().get(0).isObject();
					double isTitleView = contentObject.get("VIEW_TITLE").isNumber().doubleValue();
					double isConnectMoreView = Double.parseDouble(contentObject.get("VIEW_CONNECT_MORE").isString().toString().replaceAll("\"", ""));
					odmId = contentObject.get("ODM_ID").isString().toString().replaceAll("\"", "");
					
					if (isTitleView == 0) {
						selectStatus1.setSelectionOnSingleMode("비노출");
					}else {
						selectStatus1.setSelectionOnSingleMode("노출");
					}
					
					if (isConnectMoreView == 0) {
						selectStatus.setSelectionOnSingleMode("최신순(갱신순)");
					}else {
						selectStatus.setSelectionOnSingleMode("인기순");
					}
					
					search.setOdmId(odmId);
					loadTags(odmId);
				}
			}

		});
		
	}

	protected void loadTags(String ODM_ID) {
		
		String cmd = "GET_TAGS_FROM_ODM_ID";
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString(cmd)); 
		parameterJSON.put("odmId", new JSONString(ODM_ID));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
	
			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					JSONArray contentArray = resultObj.get("body").isObject().get("result").isObject().get("contents").isArray();
					
					for (int i=0; i<contentArray.size(); i++) {
						JSONObject recObject = contentArray.get(i).isObject();
						String keywordString = recObject.get("TAG_KEYWORD").isString().stringValue();
						
						List<VisitKoreaListCell> rowInfoList = new ArrayList<VisitKoreaListCell>();
						rowInfoList.add(new TagListLabelCell(keywordString, Float.LEFT, "50%",  "50", 50, FontWeight.BOLDER, true, TextAlign.CENTER));
						rowInfoList.add(new TagListLabelCell(".", Float.LEFT, "50%",  "50", 50, FontWeight.BOLDER, true, TextAlign.CENTER));
						TagListRow tlr = new TagListRow(rowInfoList);
						tlr.setTagName(keywordString);
						searchBody.addRowForLastIndex(tlr);
					}
				}
			}
		});
	}

	private String otdId() {
		this.otdId = (String) getWindow().getValueMap().get("OTD_ID");
		Console.log("OtherDepartmentTabMenuContentDetail.OTD_ID :: " + this.otdId);
		return this.otdId;
	}

}