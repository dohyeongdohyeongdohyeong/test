package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
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
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.sitesee.Sitesee11ContentLayout;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.sitesee.SiteseeCurationContentLayout;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.MainAreaComponent;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ServiceCompStack;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ServiceMain;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ViewPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.LayoutType;

public class LayoutTypeSightMain extends LayoutType {

	private ArrayList<ContentLayoutPanel> contentLayoutList;
	private MaterialPanel topMenuPanel;
	private ViewPanel contentPanel;
	private ViewPanel tagsPanel;
	private MaterialLink appendAreaMain;
	private MaterialLink viewServiceMain;

	public LayoutTypeSightMain(ServiceMain serviceMain, ServiceCompStack compStack) {
		super(serviceMain, compStack);
	}

	@Override
	public void initAreaMap() {
		
		this.areaMap.put(LayoutType.TAG, new ArrayList<MainAreaComponent>());
		this.areaMap.put(LayoutType.TOP_MENU, new ArrayList<MainAreaComponent>());
		this.areaMap.put(LayoutType.CONTENT_VIEW, new ArrayList<MainAreaComponent>());
		this.areaMap.put(LayoutType.CONTENTS, new ArrayList<MainAreaComponent>());
		this.areaMap.put(LayoutType.BOTTOM_MENU, new ArrayList<MainAreaComponent>());
	}

	@Override
	public void initPanelMap() {

		this.panelMap.put(LayoutType.TAG, new ViewPanel());
		this.panelMap.put(LayoutType.TOP_MENU, new ViewPanel());
		this.panelMap.put(LayoutType.CONTENT_VIEW, new ViewPanel());
		this.panelMap.put(LayoutType.CONTENTS, new ViewPanel());
		this.panelMap.put(LayoutType.BOTTOM_MENU, new ViewPanel());
	}

	@Override
	public void layout() {

		contentLayoutList = new ArrayList<ContentLayoutPanel>();
		
		topMenuPanel = new MaterialPanel();
		topMenuPanel.setWidth("100%");
		topMenuPanel.setHeight("24px");
		topMenuPanel.setLayoutPosition(Position.ABSOLUTE);
		topMenuPanel.setTop(-30);
		topMenuPanel.setLeft(0);
		
		this.serviceMain.add(topMenuPanel);

		tagsPanel = new ViewPanel();
		tagsPanel.setAreaName("태그 영역");
		tagsPanel.getAreaNameLabel().setLineHeight(tagAreaHeight-10);
		tagsPanel.getElement().setAttribute("area", "TAG");
		tagsPanel.setLayoutPosition(Position.ABSOLUTE);
		tagsPanel.setTop(0);
		tagsPanel.setLeft(0);
		tagsPanel.setBorder("1px solid #aaaaaa");
		tagsPanel.setRight(0);
		tagsPanel.setBackgroundColor(Color.WHITE);
		tagsPanel.setHeight(tagAreaHeight + "px");
		this.serviceMain.add(tagsPanel);
		setupDefaultEvent(tagsPanel);

		MaterialPanel topMenuPannel = new MaterialPanel();
		topMenuPannel.getElement().setAttribute("area", "TOP_MENU");
		topMenuPannel.setLayoutPosition(Position.ABSOLUTE);
		topMenuPannel.setLeft(0);
		topMenuPannel.setBorder("1px solid #aaaaaa");
		topMenuPannel.setRight(0);
		topMenuPannel.setTop(50);
		topMenuPannel.setHeight("26px");
		topMenuPannel.setBackgroundColor(Color.WHITE);
		this.serviceMain.add(topMenuPannel);

		MaterialLink saveLink = new MaterialLink();
		saveLink.setWidth("24px");
		saveLink.setIconType(IconType.SAVE);
		saveLink.setFloat(Float.RIGHT);
		saveLink.setBorderLeft("1px solid #aaaaaa");
		saveLink.addClickHandler(event->{
			
			JSONArray parameterArray = new JSONArray();
			
			int idx=0;
			for (ContentLayoutPanel clPanel : contentLayoutList) {
				JSONObject clObject = clPanel.getJSONObject();
				clObject.put("COMP_ORDER", new JSONNumber(idx));
				parameterArray.set(idx, clObject);
				idx++;
			}

			save(parameterArray);

		});
		topMenuPannel.add(saveLink);

		ViewPanel contentViewPanel = new ViewPanel();
		contentViewPanel.getAreaNameLabel().setLineHeight(510);
		contentViewPanel.getElement().setAttribute("area", "CONTENT_VIEW");
		contentViewPanel.setLayoutPosition(Position.ABSOLUTE);
		contentViewPanel.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		contentViewPanel.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		contentViewPanel.setTop(76);
		contentViewPanel.setLeft(0);
		contentViewPanel.setBorder("1px solid #aaaaaa");
		contentViewPanel.setRight(0);
		contentViewPanel.setBottom(26);
		contentViewPanel.setBackgroundColor(Color.WHITE);
		this.serviceMain.add(contentViewPanel);

		contentPanel = new ViewPanel();
		contentPanel.getAreaNameLabel().setLineHeight(510);
		contentPanel.getElement().setAttribute("area", "CONTENTS");
		contentPanel.setLayoutPosition(Position.ABSOLUTE);
		contentPanel.setTop(0);
		contentPanel.setLeft(1);
		contentPanel.setRight(1);
		contentPanel.setBottom(0);
		contentPanel.setBackgroundColor(Color.WHITE);
		contentViewPanel.add(contentPanel);

		MaterialPanel bottomMenuPannel = new MaterialPanel();
		bottomMenuPannel.getElement().setAttribute("area", "BOTTOM_MENU");
		bottomMenuPannel.setLayoutPosition(Position.ABSOLUTE);
		bottomMenuPannel.setLeft(0);
		bottomMenuPannel.setBorder("1px solid #aaaaaa");
		bottomMenuPannel.setRight(0);
		bottomMenuPannel.setBottom(0);
		bottomMenuPannel.setHeight("26px");
		bottomMenuPannel.setBackgroundColor(Color.WHITE);
		this.serviceMain.add(bottomMenuPannel);

		MaterialLink removeLink = new MaterialLink();
		removeLink.setWidth("24px");
		removeLink.setIconType(IconType.DELETE);
		removeLink.setFloat(Float.LEFT);
		removeLink.setBorderRight("1px solid #aaaaaa");
		removeLink.addClickHandler(event->{
			contentLayoutList.remove(contentPanel.getSelectedItem());
			redrawContentList();
		});
		bottomMenuPannel.add(removeLink);

		MaterialLink upLink = new MaterialLink();
		upLink.setWidth("24px");
		upLink.setIconType(IconType.ARROW_UPWARD);
		upLink.setFloat(Float.RIGHT);
		upLink.setBorderLeft("1px solid #aaaaaa");
		upLink.addClickHandler(event->{
			
			int csi = contentPanel.getSelectedItemIndex();
			if (csi > 0) {
				Collections.swap(contentLayoutList, csi, csi-1);
				contentPanel.setSelectedItemIndex(csi-1);
				redrawContentList();
			}
			
		});
		bottomMenuPannel.add(upLink);

		MaterialLink downLink = new MaterialLink();
		downLink.setWidth("24px");
		downLink.setIconType(IconType.ARROW_DOWNWARD);
		downLink.setFloat(Float.RIGHT);
		downLink.setBorderLeft("1px solid #aaaaaa");
		downLink.addClickHandler(event->{
			int csi = contentPanel.getSelectedItemIndex();
			if (csi < contentLayoutList.size()-1) {
				Collections.swap(contentLayoutList, csi, csi+1);
				contentPanel.setSelectedItemIndex(csi+1);
				redrawContentList();
			}
		});
		bottomMenuPannel.add(downLink);
		
		appendAreaMain = appendLinkIcon(this.topMenuPanel, IconType.ADD, com.google.gwt.dom.client.Style.Float.RIGHT, "");
		appendAreaMain.addClickHandler(event->{
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("TARGET", this);
			map.put("DISP_TYPE", 3);
			
			masterPanel
			.getMaterialExtentsWindow()
			.openDialog( OtherDepartmentMainApplication.CATEGORY_MAIN_SELECT_CONTENT_LAYOUT, map, 800);
			
		});
		appendAreaMain.setTooltip("컨텐츠 영역 추가");
		
		viewServiceMain = appendLinkIcon(this.topMenuPanel, IconType.WEB, com.google.gwt.dom.client.Style.Float.LEFT, "");
		viewServiceMain.setTooltip("미리보기");
		viewServiceMain.addClickHandler(event->{
			Registry.openPreview(viewServiceMain, (String) Registry.get("service.server")  + serviceMain.getPreview());
		});
	}

	// save sight main
	private void save(JSONArray parameterArray) {
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("INSERT_SIGHT_MAIN")); 
		parameterJSON.put("CONTENTS", parameterArray);
		
		executeBusiness(parameterJSON);
			
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
		parameterJSON.put("cmd", new JSONString("GET_SIGHT_MAIN")); 

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
	
			@Override
			public void call(Object param1, String param2, Object param3) {
	
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					
					JSONObject bodyObj =  resultObj.get("body").isObject();
					JSONObject detailResultObj = bodyObj.get("result").isObject();
					
					if (detailResultObj.get("COMPONENT") != null) {
						JSONArray compArray = detailResultObj.get("COMPONENT").isArray();
						int compSize = compArray.size();
						
						for (int i=0; i<compSize; i++) {
							JSONObject tgrCompObj =  compArray.get(i).isObject();
							String compType = tgrCompObj.get("COMP_TYPE").isString().stringValue();
							buildContentTemplate(compType, tgrCompObj);
						}
					}
				}
			}


		});
	}
	
	
	public ContentLayoutPanel buildContentTemplate(String contentType, JSONObject value) {
		
		Console.log("value :: " + value);
		
		ContentLayoutPanel retPanel = null;
		
		if (contentType.equals("11content")) {
			Sitesee11ContentLayout sitesee11ContentLayout = new Sitesee11ContentLayout();			
			sitesee11ContentLayout.setWindow(this.masterPanel.getMaterialExtentsWindow());
			retPanel = sitesee11ContentLayout;
		}else if (contentType.equals("curation")) {
			SiteseeCurationContentLayout siteseeCurationContentLayout = new SiteseeCurationContentLayout();			
			siteseeCurationContentLayout.setWindow(this.masterPanel.getMaterialExtentsWindow());
			retPanel = siteseeCurationContentLayout;
		}
		
		retPanel.setData(value);
		this.contentLayoutList.add(retPanel);
		redrawContentList();
		
		return retPanel;
	}


}
