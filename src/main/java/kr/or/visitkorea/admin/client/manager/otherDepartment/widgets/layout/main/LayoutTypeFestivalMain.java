package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.ContentLayoutPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival.MonthlyContentDetailPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival.SeasonContentDetailPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.MainAreaComponent;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.SeasonViewPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ServiceCompStack;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ServiceMain;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListImageCell;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListLabelCell;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListRow;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ViewPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.VisitKoreaListCell;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.LayoutType;

public class LayoutTypeFestivalMain extends LayoutType {

	private ArrayList<ContentLayoutPanel> contentLayoutList;
	private ViewPanel contentPanel;
	private ViewPanel seasonPanel;
	private ViewPanel monthlyPanel;
	private MaterialPanel topMenuPanel;
	private MaterialLink viewServiceMain;

	public LayoutTypeFestivalMain(ServiceMain serviceMain, ServiceCompStack compStack) {
		super(serviceMain, compStack);
	}

	@Override
	public void initAreaMap() {
		this.areaMap.put(LayoutType.TAG, new ArrayList<MainAreaComponent>());
		
		this.areaMap.put("SEASON_1", new ArrayList<MainAreaComponent>());
		this.areaMap.put("SEASON_2", new ArrayList<MainAreaComponent>());
		this.areaMap.put("SEASON_3", new ArrayList<MainAreaComponent>());
		this.areaMap.put("SEASON_4", new ArrayList<MainAreaComponent>());
		
		this.areaMap.put("MONTHLY_1", new ArrayList<MainAreaComponent>());
		this.areaMap.put("MONTHLY_2", new ArrayList<MainAreaComponent>());
		this.areaMap.put("MONTHLY_3", new ArrayList<MainAreaComponent>());
		this.areaMap.put("MONTHLY_4", new ArrayList<MainAreaComponent>());
		this.areaMap.put("MONTHLY_5", new ArrayList<MainAreaComponent>());
		this.areaMap.put("MONTHLY_6", new ArrayList<MainAreaComponent>());
		this.areaMap.put("MONTHLY_7", new ArrayList<MainAreaComponent>());
		this.areaMap.put("MONTHLY_8", new ArrayList<MainAreaComponent>());
		this.areaMap.put("MONTHLY_9", new ArrayList<MainAreaComponent>());
		this.areaMap.put("MONTHLY_10", new ArrayList<MainAreaComponent>());
		this.areaMap.put("MONTHLY_11", new ArrayList<MainAreaComponent>());
		this.areaMap.put("MONTHLY_12", new ArrayList<MainAreaComponent>());
	}

	@Override
	public void initPanelMap() {
		this.panelMap.put(LayoutType.TAG, new ViewPanel());
		
		this.panelMap.put("SEASON_1", new ViewPanel());
		this.panelMap.put("SEASON_2", new ViewPanel());
		this.panelMap.put("SEASON_3", new ViewPanel());
		this.panelMap.put("SEASON_4", new ViewPanel());
		
		this.panelMap.put("MONTHLY_1", new ViewPanel());
		this.panelMap.put("MONTHLY_2", new ViewPanel());
		this.panelMap.put("MONTHLY_3", new ViewPanel());
		this.panelMap.put("MONTHLY_4", new ViewPanel());
		this.panelMap.put("MONTHLY_5", new ViewPanel());
		this.panelMap.put("MONTHLY_6", new ViewPanel());
		this.panelMap.put("MONTHLY_7", new ViewPanel());
		this.panelMap.put("MONTHLY_8", new ViewPanel());
		this.panelMap.put("MONTHLY_9", new ViewPanel());
		this.panelMap.put("MONTHLY_10", new ViewPanel());
		this.panelMap.put("MONTHLY_11", new ViewPanel());
		this.panelMap.put("MONTHLY_12", new ViewPanel());
	}

	@Override
	public void layout() {
		
		topMenuPanel = new MaterialPanel();
		topMenuPanel.setWidth("100%");
		topMenuPanel.setHeight("24px");
		topMenuPanel.setLayoutPosition(Position.ABSOLUTE);
		topMenuPanel.setTop(-30);
		topMenuPanel.setLeft(0);
		this.serviceMain.add(topMenuPanel);
		
		ViewPanel tagsPanel = this.panelMap.get(LayoutType.TAG);
		tagsPanel.setAreaName("태그 영역");
		tagsPanel.getAreaNameLabel().setLineHeight(tagAreaHeight-10);
		tagsPanel.getElement().setAttribute("area", "TAG");
		tagsPanel.setLayoutPosition(Position.RELATIVE);
		tagsPanel.setBorder("1px solid #aaaaaa");
		tagsPanel.setLeft(0);
		tagsPanel.setRight(0);
		tagsPanel.setBackgroundColor(Color.WHITE);
		tagsPanel.setHeight(tagAreaHeight + "px");
		this.serviceMain.add(tagsPanel);
		setupDefaultEvent(tagsPanel);

		// 4계절 영역
		seasonPanel = new ViewPanel();
		seasonPanel.setAreaName("");
		seasonPanel.getAreaNameLabel().setLineHeight((tagAreaHeight * 3)-10);
		seasonPanel.getElement().setAttribute("area", "TAG");
		seasonPanel.setLayoutPosition(Position.RELATIVE);
		seasonPanel.setBorder("1px solid #aaaaaa");
		seasonPanel.setLeft(0);
		seasonPanel.setRight(0);
		seasonPanel.setBackgroundColor(Color.WHITE);
		seasonPanel.setHeight(((tagAreaHeight * 3) + 20) + "px");
		
		// 12개월 영역
		monthlyPanel = new ViewPanel();
		monthlyPanel.setAreaName("");
		monthlyPanel.getAreaNameLabel().setLineHeight(this.showcaseHeight);
		monthlyPanel.setLayoutPosition(Position.RELATIVE);
		monthlyPanel.setLeft(0);
		monthlyPanel.setRight(0);
		monthlyPanel.setHeight((tagAreaHeight * 7) + "px");
		monthlyPanel.setBorder("1px solid #aaaaaa");
		monthlyPanel.setBackgroundColor(Color.WHITE);
		
		viewServiceMain = appendLinkIcon(this.topMenuPanel, IconType.WEB, com.google.gwt.dom.client.Style.Float.LEFT, "");
		viewServiceMain.setTooltip("미리보기");
		viewServiceMain.addClickHandler(event->{
			Registry.openPreview(viewServiceMain, (String) Registry.get("service.server")  + serviceMain.getPreview());
		});
	}

	private void buildSeasonPanel(JSONArray seasonComp) {
		
		String[] seasonArray = new String[] {"봄","여름","가을","겨울"};
		
		// title of target panel
		ViewPanel titlePanel = new ViewPanel();
		titlePanel.setLayoutPosition(Position.RELATIVE);
		titlePanel.setWidth("100%");
		titlePanel.setHeight("40px");
		titlePanel.setFloat(Float.LEFT);
		titlePanel.setBackgroundColor(Color.WHITE);
		titlePanel.setAreaName("계절별 컨텐츠 설정 영역");
		seasonPanel.add(titlePanel);

		for (int i=0; i<seasonArray.length; i++) {
			
			String areaName = LayoutType.SEASON + "_" + (i+1);
			
			ViewPanel childPanel = this.panelMap.get(areaName);
			childPanel.setAreaName(seasonArray[i]);
			childPanel.getAreaNameLabel().setLineHeight(tagAreaHeight-10);
			childPanel.getElement().setAttribute("area", areaName);
			childPanel.setLayoutPosition(Position.RELATIVE);
			childPanel.setWidth("25%");
			childPanel.getElement().getStyle().setProperty("height", "calc(100% - 40px)");
			childPanel.setFloat(Float.LEFT);
			childPanel.setBorder("1px solid #efefef");
			childPanel.setBackgroundColor(Color.WHITE);
			
			for (int j=0; j<seasonComp.size(); j++) {
				String activatedComp = seasonComp.get(j).isObject().get("NAME").isString().stringValue();
				
				if (!seasonArray[i].equals(activatedComp))
					continue;
				
				childPanel.setComponentCount((int) seasonComp.get(j).isObject().get("CNT").isNumber().doubleValue());
			}
			
			setupDefaultEvent(childPanel);
			seasonPanel.add(childPanel);
		}

		this.serviceMain.add(seasonPanel);
	}
	

	@Override
	protected void setupDefaultEvent(ViewPanel targetViewPanel) {

		targetViewPanel.addMouseOutHandler(event->{
			if (targetViewPanel != tempPanel) {
				targetViewPanel.getElement().getStyle().setCursor(Cursor.DEFAULT);
				targetViewPanel.setBackgroundColor(Color.WHITE);
				targetViewPanel.setTextColor(Color.BLACK);
			}
		});

		targetViewPanel.addMouseOverHandler(event->{
			if (targetViewPanel != tempPanel) {
				targetViewPanel.getElement().getStyle().setCursor(Cursor.POINTER);
				targetViewPanel.setBackgroundColor(Color.BLUE_LIGHTEN_4);
				targetViewPanel.setTextColor(Color.WHITE);
			}
		});

		targetViewPanel.addClickHandler(event->{
			
			
			if (tempPanel != null) {
				tempPanel.setBackgroundColor(Color.WHITE);
				tempPanel.setTextColor(Color.BLACK);
			}
			
			targetViewPanel.getElement().getStyle().setCursor(Cursor.POINTER);
			targetViewPanel.setBackgroundColor(Color.BLUE);
			targetViewPanel.setTextColor(Color.WHITE);
			tempPanel = targetViewPanel;
			
			String areaString = targetViewPanel.getArea().toUpperCase();
			
			if (areaString != null && areaString.length() > 0) {
				
				if (this.compStack != null ) {
					this.compStack.clearRow();
					this.compStack.setAreaValue(areaString);
				}
				
				if (areaString.equals("TAG")) {
					this.masterPanel.go(1);
					this.compStack.appendComponentItems(null);
					this.compStack.setTargetArea(null);
				}else if (areaString.equals("MAIN_SHOWCASE")) {
					this.masterPanel.go(3);
					this.compStack.appendComponentItems(null);
					this.compStack.setTargetArea(null);
				}else if (areaString.equals("CURAITION")) {
					this.masterPanel.go(4);
					this.compStack.appendComponentItems(null);
					this.compStack.setTargetArea(null);
				}else if (areaString.equals("MARKETING")) {
					this.masterPanel.go(5);
					this.compStack.appendComponentItems(null);
					this.compStack.setTargetArea(null);
				}else if (areaString.equals("LOCAL_GOV")) {
					this.masterPanel.go(6);
					this.compStack.appendComponentItems(null);
					this.compStack.setTargetArea(null);
				}else if (areaString.equals("CALENDAR")) {
					this.masterPanel.go(7);
					this.compStack.appendComponentItems(null);
					this.compStack.setTargetArea(null);
				}else if (areaString.equals("AD")) {
					this.masterPanel.go(8);
					this.compStack.appendComponentItems(null);
					this.compStack.setTargetArea(null);
				}else if (areaString.equals("CONTENTS")) {
					this.masterPanel.go(10);
					this.compStack.appendComponentItems(null);
					this.compStack.setTargetArea(null);
				}else if (areaString.equals("SHOWCASE_WITH_TITLE")) {
					this.compStack.appendComponentItems(getComponentItems(areaString));
					this.compStack.setTargetArea(getAreaItems(areaString)); 
				}else if (areaString.equals("TAB_MENU")) {
					this.masterPanel.go(11);
					this.compStack.appendComponentItems(null);
					this.compStack.setTargetArea(null); 
				}else if (areaString.equals("CONTENT_LIST")) {
					this.masterPanel.go(12);
					this.compStack.appendComponentItems(null);
					this.compStack.setTargetArea(null); 
				}else if (areaString.startsWith("SEASON_")) {
					
					this.compStack.appendComponentItems(null);
					this.compStack.setTargetArea(null); 
					
					MaterialWidget seasonWidget = this.masterPanel.go(13);
					
					SeasonContentDetailPanel scdp = (SeasonContentDetailPanel)seasonWidget;
					scdp.setSeasonTitle(targetViewPanel.getAreaNameLabel().getText());
					
					if (targetViewPanel.getAreaNameLabel().getText().equals("봄"))
						scdp.setSeasonId("277c55c5-9647-11e9-ba55-e0cb4e4f79cd");
					else if (targetViewPanel.getAreaNameLabel().getText().equals("여름"))
						scdp.setSeasonId("277c7c52-9647-11e9-ba55-e0cb4e4f79cd");
					else if (targetViewPanel.getAreaNameLabel().getText().equals("가을"))
						scdp.setSeasonId("277e12cd-9647-11e9-ba55-e0cb4e4f79cd");
					else if (targetViewPanel.getAreaNameLabel().getText().equals("겨울"))
						scdp.setSeasonId("277e38fe-9647-11e9-ba55-e0cb4e4f79cd");
						
					scdp.setVeiw(targetViewPanel);
					
					if (targetViewPanel.getData("REC_CONTAINER") == null) {
						setData(scdp.getSeasonId(), targetViewPanel, scdp);
					}else {
						scdp.redrawList(); 
					}
					
				}else if (areaString.startsWith("MONTHLY_")) {
					MaterialWidget seasonWidget = this.masterPanel.go(14);
					MonthlyContentDetailPanel mcdp = (MonthlyContentDetailPanel)seasonWidget;
					mcdp.setViewPanel(targetViewPanel);
					this.compStack.appendComponentItems(null);
					this.compStack.setTargetArea(null); 
				}else {
					this.compStack.appendComponentItems(getComponentItems(areaString));
					this.compStack.setTargetArea(getAreaItems(areaString)); 
				}
			}
		});		
	}
	
	public void setData(String seasonId, ViewPanel targetViewPanel, SeasonContentDetailPanel scdp) {
		
		JSONObject jObj = new JSONObject();
		jObj.put("cmd", new JSONString("SELECT_TARGET_SEASON_CONTENTS"));
		jObj.put("SEASON_ID", new JSONString(seasonId));
		
		VisitKoreaBusiness.post("call", jObj.toString(), new Func3<Object, String, Object>() {
			
			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					
					JSONObject bodyObj =  resultObj.get("body").isObject();
					JSONObject result = bodyObj.get("result").isObject();
					targetViewPanel.setData("REC_CONTAINER", result);
					scdp.redrawList();
				}
			}
		});
	}

	private void buildMonthlyPanel(JSONArray monthlyComp) {
		String[] monthArray = new String[] {"1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"};
		
		// title of target panel
		ViewPanel titlePanel = new ViewPanel();
		titlePanel.setLayoutPosition(Position.RELATIVE);
		titlePanel.setWidth("100%");
		titlePanel.setHeight("40px");
		titlePanel.setFloat(Float.LEFT);
		titlePanel.setBackgroundColor(Color.WHITE);
		titlePanel.setAreaName("월별 컨텐츠 설정 영역");
		monthlyPanel.add(titlePanel);
		
		for (int i=0; i<monthArray.length; i++) {
			
			String areaName = LayoutType.MONTHLY + "_" + (i+1);
			
			ViewPanel childPanel = this.panelMap.get(areaName);
			childPanel.setAreaName(monthArray[i]);
			childPanel.getAreaNameLabel().setLineHeight(tagAreaHeight-10);
			childPanel.getElement().setAttribute("area", areaName);
			childPanel.setLayoutPosition(Position.RELATIVE);
			childPanel.setWidth("16.6666666%");
			childPanel.getElement().getStyle().setProperty("height", "calc(50% - 20px)");
			childPanel.setFloat(Float.LEFT);
			childPanel.setBorder("1px solid #efefef");
			childPanel.setBackgroundColor(Color.WHITE);
			
			for (int j=0; j<monthlyComp.size(); j++) {
				int activatedComp = (int) monthlyComp.get(j).isObject().get("MAIN_TYPE").isNumber().doubleValue() - 3;
				if (i != activatedComp)
					continue;
				
				childPanel.setComponentCount((int) monthlyComp.get(j).isObject().get("CNT").isNumber().doubleValue());
			}
			
			setupDefaultEvent(childPanel);
			monthlyPanel.add(childPanel);
		}
		this.serviceMain.add(monthlyPanel);
	}

	@Override
	public void redrawContentList() {
		contentPanel.clear();
		for (ContentLayoutPanel panel : contentLayoutList) {
			panel.loadData();
			contentPanel.add(panel);
		}
	}

	@Override
	protected void renderData() {
		loadData();
	}

	@Override
	public void loadData() {
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_FESTIVAL_MAIN_COMP_CNT"));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					JSONObject bodyObj =  resultObj.get("body").isObject();
					
					JSONArray seasonObj = bodyObj.get("result").isObject().get("SEASON_CNT").isArray();
					buildSeasonPanel(seasonObj);
					
					JSONArray monthlyObj = bodyObj.get("result").isObject().get("MONTHLY_CNT").isArray();
					buildMonthlyPanel(monthlyObj);
				}
			}
		});
	}
}
