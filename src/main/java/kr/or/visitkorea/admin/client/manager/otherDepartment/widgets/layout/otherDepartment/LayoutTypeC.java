package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.otherDepartment;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.MainAreaComponent;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ServiceCompStack;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ServiceMain;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ViewPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.LayoutType;

public class LayoutTypeC extends LayoutType {

	private MaterialLink viewServiceMain;

	public LayoutTypeC(ServiceMain serviceMain, ServiceCompStack compStack) {
		super(serviceMain, compStack);
	}

	@Override
	public void initAreaMap() {
		
		this.areaMap.put(LayoutType.TAG, new ArrayList<MainAreaComponent>());
		this.areaMap.put(LayoutType.SHOWCASE_WITH_TITLE, new ArrayList<MainAreaComponent>());
		this.areaMap.put(LayoutType.TAB_MENU, new ArrayList<MainAreaComponent>());
		this.areaMap.put(LayoutType.A, new ArrayList<MainAreaComponent>());
		this.areaMap.put(LayoutType.B, new ArrayList<MainAreaComponent>());
		this.areaMap.put(LayoutType.C, new ArrayList<MainAreaComponent>());
		
	}

	@Override
	public void initPanelMap() {

		this.panelMap.put(LayoutType.TAG, new ViewPanel());
		this.panelMap.put(LayoutType.SHOWCASE_WITH_TITLE, new ViewPanel());
		this.panelMap.put(LayoutType.TAB_MENU, new ViewPanel());
		this.panelMap.put(LayoutType.A, new ViewPanel());
		this.panelMap.put(LayoutType.B, new ViewPanel());
		this.panelMap.put(LayoutType.C, new ViewPanel());
			
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
		tagsPanel.setAreaName("TAGS");
		tagsPanel.getElement().setAttribute("area", "TAG");
		tagsPanel.setAreaName("태그 영역");
		tagsPanel.getAreaNameLabel().setLineHeight(tagAreaHeight-10);
		tagsPanel.setLayoutPosition(Position.ABSOLUTE);
		tagsPanel.setTop(0);
		tagsPanel.setLeft(0);
		tagsPanel.setBorder("1px solid #aaaaaa");
		tagsPanel.setRight(0);
		tagsPanel.setHeight(tagAreaHeight + "px");
		this.serviceMain.add(tagsPanel);
		setupDefaultEvent(tagsPanel);

		ViewPanel showcaseWithTitlePanel = this.panelMap.get(LayoutType.SHOWCASE_WITH_TITLE);
		showcaseWithTitlePanel.getElement().setAttribute("area", "SHOWCASE_WITH_TITLE");
		showcaseWithTitlePanel.setAreaName("쇼케이스 및 문구 영역");
		showcaseWithTitlePanel.getAreaNameLabel().setLineHeight(150);
		showcaseWithTitlePanel.setLayoutPosition(Position.ABSOLUTE);
		showcaseWithTitlePanel.setTop(tagAreaHeight);
		showcaseWithTitlePanel.setLeft(0);
		showcaseWithTitlePanel.setWidth("100%");
		showcaseWithTitlePanel.setHeight("150px");
		showcaseWithTitlePanel.setBorder("1px solid #aaaaaa");
		showcaseWithTitlePanel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		this.serviceMain.add(showcaseWithTitlePanel);
		setupDefaultEvent(showcaseWithTitlePanel);
		
		int aPanelTop = 200;
		
		ViewPanel tabMenuPanel = this.panelMap.get(LayoutType.TAB_MENU);
		tabMenuPanel.getElement().setAttribute("area", "TAB_MENU");
		tabMenuPanel.setAreaName("탭메뉴 영역");
		tabMenuPanel.getAreaNameLabel().setLineHeight(150);
		tabMenuPanel.setLayoutPosition(Position.ABSOLUTE);
		tabMenuPanel.setTop(aPanelTop);
		tabMenuPanel.setLeft(0);
		tabMenuPanel.setWidth("100%");
		tabMenuPanel.setHeight("150px");
		tabMenuPanel.setBorder("1px solid #aaaaaa");
		tabMenuPanel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		this.serviceMain.add(tabMenuPanel);
		setupDefaultEvent(tabMenuPanel);

		int aPanelHeight = 570 - 350;
		aPanelTop = 350;
		
		ViewPanel aPanel = this.panelMap.get(LayoutType.A);
		aPanel.getElement().setAttribute("area", "A");
		aPanel.setAreaName("A 영역");
		aPanel.getAreaNameLabel().setLineHeight(aPanelHeight);
		aPanel.setLayoutPosition(Position.ABSOLUTE);
		aPanel.setTop(aPanelTop);
		aPanel.setLeft(0);
		aPanel.setWidth("33.3333%");
		aPanel.setHeight(aPanelHeight + "px");
		aPanel.setBorder("1px solid #aaaaaa");
		aPanel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		this.serviceMain.add(aPanel);
		setupDefaultEvent(aPanel);
		
		ViewPanel bPanel = this.panelMap.get(LayoutType.B);
		bPanel.getElement().setAttribute("area", "B");
		bPanel.setAreaName("B 영역");
		bPanel.getAreaNameLabel().setLineHeight(aPanelHeight);
		bPanel.setLayoutPosition(Position.ABSOLUTE);
		bPanel.setTop(aPanelTop);
		bPanel.setLeft(this.leftB);
		bPanel.setWidth("33.3333%");
		bPanel.setHeight(aPanelHeight + "px");
		bPanel.setBorder("1px solid #aaaaaa");
		bPanel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		this.serviceMain.add(bPanel);
		setupDefaultEvent(bPanel);
		
		ViewPanel cPanel = this.panelMap.get(LayoutType.C);
		cPanel.getElement().setAttribute("area", "C");
		cPanel.setAreaName("C 영역");
		cPanel.getAreaNameLabel().setLineHeight(aPanelHeight);
		cPanel.setLayoutPosition(Position.ABSOLUTE);
		cPanel.setTop(aPanelTop);
		cPanel.setLeft(this.leftC);
		cPanel.setWidth("33.3333%");
		cPanel.setHeight(aPanelHeight + "px");
		cPanel.setBorder("1px solid #aaaaaa");
		cPanel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		this.serviceMain.add(cPanel);
		setupDefaultEvent(cPanel);
		
		viewServiceMain = appendLinkIcon(this.topMenuPanel, IconType.WEB, com.google.gwt.dom.client.Style.Float.LEFT, "");
		viewServiceMain.setTooltip("미리보기");
		viewServiceMain.addClickHandler(event->{
			Registry.openPreview(viewServiceMain, (String) Registry.get("service.server")  + serviceMain.getPreview() + serviceMain.getOtdId());
		});
	}

	@Override
	public void redrawContentList() {
		
	}

	@Override
	protected void renderData() {
		 
		JSONArray aArray = data.get("body").isObject().get("result").isObject().get("A").isArray();
		JSONArray bArray = data.get("body").isObject().get("result").isObject().get("B").isArray();
		JSONArray cArray = data.get("body").isObject().get("result").isObject().get("C").isArray();
		JSONArray showcaseArray = data.get("body").isObject().get("result").isObject().get("showcase").isArray();
		JSONArray swtArray = data.get("body").isObject().get("result").isObject().get("SWT").isArray();

		setArea("A", area(aArray));
		setArea("B", area(bArray));
		setArea("C", area(cArray));
		setArea("SHOWCASE_WITH_TITLE", area(swtArray));
		setArea("S", area(showcaseArray));
		
	}

	@Override
	public void loadData() {

	}

}
