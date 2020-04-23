package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.otherDepartment;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.ContentLayoutPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.MainAreaComponent;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ServiceCompStack;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ServiceMain;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ViewPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.LayoutType;

public class LayoutTypeMain extends LayoutType {

	private ArrayList<ContentLayoutPanel> contentLayoutList;
	private MaterialPanel topMenuPanel;
	private ViewPanel contentPanel;
	private ViewPanel tagsPanel;
	private ViewPanel showcasePanel;
	private ViewPanel curationPanel;
	private ViewPanel marketingPanel;
	private ViewPanel localGovPanel;
	private ViewPanel aPanel;
	private ViewPanel bPanel;
	private ViewPanel cPanel;
	
	private MaterialLink viewServiceMain;
	private ViewPanel ImageBannerPanel;

	public LayoutTypeMain(ServiceMain serviceMain, ServiceCompStack compStack) {
		super(serviceMain, compStack);
	}


	@Override
	public void initAreaMap() {
		
		this.areaMap.put(LayoutType.MAIN_SHOWCASE, new ArrayList<MainAreaComponent>());
		this.areaMap.put(LayoutType.CURAITION, new ArrayList<MainAreaComponent>());
		this.areaMap.put(LayoutType.TAG, new ArrayList<MainAreaComponent>());
		this.areaMap.put(LayoutType.MARKETING, new ArrayList<MainAreaComponent>());
		this.areaMap.put(LayoutType.LOCAL_GOV, new ArrayList<MainAreaComponent>());
		this.areaMap.put(LayoutType.A, new ArrayList<MainAreaComponent>());
		this.areaMap.put(LayoutType.B, new ArrayList<MainAreaComponent>());
		this.areaMap.put(LayoutType.C, new ArrayList<MainAreaComponent>());
		
	}

	@Override
	public void initPanelMap() {

		this.panelMap.put(LayoutType.MAIN_SHOWCASE, new ViewPanel());
		this.panelMap.put(LayoutType.CURAITION, new ViewPanel());
		this.panelMap.put(LayoutType.IMAGE_BANNER, new ViewPanel());
		this.panelMap.put(LayoutType.TAG, new ViewPanel());
		this.panelMap.put(LayoutType.MARKETING, new ViewPanel());
		this.panelMap.put(LayoutType.LOCAL_GOV, new ViewPanel());
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
		
		int titleGap = 10;
		
		int scHeight = 80;
		showcasePanel = this.panelMap.get(LayoutType.MAIN_SHOWCASE);
		showcasePanel.getElement().setAttribute("area", "MAIN_SHOWCASE");
		showcasePanel.setAreaName("쇼케이스");
		showcasePanel.getAreaNameLabel().setLineHeight(scHeight-titleGap);
		showcasePanel.setLayoutPosition(Position.ABSOLUTE);
		showcasePanel.setTop(0);
		showcasePanel.setLeft(0);
		showcasePanel.setWidth("100%");
		showcasePanel.setHeight(scHeight + "px");
		showcasePanel.setBorder("1px solid #aaaaaa");
		showcasePanel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		this.serviceMain.add(showcasePanel);
		setupDefaultEvent(showcasePanel);
	
		int tagHeight = 40;
		tagsPanel = this.panelMap.get(LayoutType.TAG);
		tagsPanel.getElement().setAttribute("area", "TAG");
		tagsPanel.setAreaName("태그");
		tagsPanel.getAreaNameLabel().setLineHeight(tagHeight-titleGap);
		tagsPanel.setLayoutPosition(Position.ABSOLUTE);
		tagsPanel.setTop(scHeight);
		tagsPanel.setLeft(0);
		tagsPanel.setWidth("100%");		
		tagsPanel.setBorder("1px solid #aaaaaa");
		tagsPanel.setHeight(tagHeight + "px");
		this.serviceMain.add(tagsPanel);
		setupDefaultEvent(tagsPanel);
		
		int curationBannerHeight = 50;
		ImageBannerPanel = this.panelMap.get(LayoutType.IMAGE_BANNER);
		ImageBannerPanel.getElement().setAttribute("area", "IMAGE_BANNER");
		ImageBannerPanel.setAreaName("이미지 배너");
		ImageBannerPanel.getAreaNameLabel().setLineHeight(curationBannerHeight-titleGap);
		ImageBannerPanel.setLayoutPosition(Position.ABSOLUTE);
		ImageBannerPanel.setTop(tagHeight+scHeight);
		ImageBannerPanel.setLeft(0);
		ImageBannerPanel.setWidth("50%");		
		ImageBannerPanel.setBorder("1px solid #aaaaaa");
		ImageBannerPanel.setHeight(curationBannerHeight + "px");
		this.serviceMain.add(ImageBannerPanel);
		setupDefaultEvent(ImageBannerPanel);
		
		int curationHeight = 50;
		curationPanel = this.panelMap.get(LayoutType.CURAITION);
		curationPanel.getElement().setAttribute("area", "CURAITION");
		curationPanel.setAreaName("큐레이션");
		curationPanel.getAreaNameLabel().setLineHeight(curationHeight-titleGap);
		curationPanel.setLayoutPosition(Position.ABSOLUTE);
		curationPanel.setTop(tagHeight+scHeight+curationBannerHeight);
		curationPanel.setLeft(0);
		curationPanel.setWidth("50%");		
		curationPanel.setBorder("1px solid #aaaaaa");
		curationPanel.setHeight(curationHeight + "px");
		this.serviceMain.add(curationPanel);
		setupDefaultEvent(curationPanel);
		
		
		int marketingHeight = curationHeight+curationBannerHeight ;
		marketingPanel = this.panelMap.get(LayoutType.MARKETING);
		marketingPanel.getElement().setAttribute("area", "MARKETING");
		marketingPanel.setAreaName("마케팅");
		marketingPanel.getAreaNameLabel().setLineHeight(marketingHeight-titleGap);
		marketingPanel.setLayoutPosition(Position.ABSOLUTE);
		marketingPanel.setTop(tagHeight+scHeight);
		marketingPanel.setLeft((this.totalWidth / 2)-1);
		marketingPanel.setWidth("50%");		
		marketingPanel.setBorder("1px solid #aaaaaa");
		marketingPanel.setHeight(marketingHeight + "px");
		this.serviceMain.add(marketingPanel);
		setupDefaultEvent(marketingPanel);
		
		int localGovHeight = 120;
		localGovPanel = this.panelMap.get(LayoutType.LOCAL_GOV);
		localGovPanel.getElement().setAttribute("area", "LOCAL_GOV");
		localGovPanel.setAreaName("지자체");
		localGovPanel.getAreaNameLabel().setLineHeight(localGovHeight-titleGap);
		localGovPanel.setLayoutPosition(Position.ABSOLUTE);
		localGovPanel.setTop(scHeight + marketingHeight+tagHeight);
		localGovPanel.setLeft(0);
		localGovPanel.setWidth("100%");		
		localGovPanel.setBorder("1px solid #aaaaaa");
		localGovPanel.setHeight(localGovHeight + "px");
		this.serviceMain.add(localGovPanel);
		setupDefaultEvent(localGovPanel);
		
		int aPanelTop = scHeight + tagHeight+ marketingHeight + localGovHeight;
		int aPanelHeight = 229;
		aPanel = this.panelMap.get(LayoutType.A);
		aPanel.getElement().setAttribute("area", "A");
		aPanel.setAreaName("A 영역");
		aPanel.getAreaNameLabel().setLineHeight(aPanelHeight);
		aPanel.setLayoutPosition(Position.ABSOLUTE);
		aPanel.setTop(aPanelTop);
		aPanel.setLeft(0);
		aPanel.setWidth("33.3333333333%");
		aPanel.setHeight(aPanelHeight + "px");
		aPanel.setBorder("1px solid #aaaaaa");
		aPanel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		this.serviceMain.add(aPanel);
		setupDefaultEvent(aPanel);
		
		int bPanelTop = aPanelTop;
		int bPanelHeight = aPanelHeight;
		bPanel = this.panelMap.get(LayoutType.B);
		bPanel.getElement().setAttribute("area", "B");
		bPanel.setAreaName("B 영역");
		bPanel.getAreaNameLabel().setLineHeight(bPanelHeight);
		bPanel.setLayoutPosition(Position.ABSOLUTE);
		bPanel.setTop(bPanelTop);
		bPanel.setLeft(this.leftB);
		bPanel.setWidth("33.333333333%");
		bPanel.setHeight(bPanelHeight + "px");
		bPanel.setBorder("1px solid #aaaaaa");
		bPanel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		this.serviceMain.add(bPanel);
		setupDefaultEvent(bPanel);

		cPanel = this.panelMap.get(LayoutType.C);
		cPanel.getElement().setAttribute("area", "C");
		cPanel.setAreaName("C 영역");
		cPanel.getAreaNameLabel().setLineHeight(aPanelHeight);
		cPanel.setLayoutPosition(Position.ABSOLUTE);
		cPanel.setTop(bPanelTop);
		cPanel.setLeft(this.leftC);
		cPanel.setWidth("33.3333333333%");
		cPanel.setHeight(aPanelHeight + "px");
		cPanel.setBorder("1px solid #aaaaaa");
		cPanel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		this.serviceMain.add(cPanel);
		setupDefaultEvent(cPanel);
		
		viewServiceMain = appendLinkIcon(this.topMenuPanel, IconType.WEB, com.google.gwt.dom.client.Style.Float.LEFT, "");
		viewServiceMain.setTooltip("미리보기");
		viewServiceMain.addClickHandler(event->{
			Registry.openPreview(viewServiceMain, (String) Registry.get("service.server")  + serviceMain.getPreview());
		});
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
		 
		JSONArray aArray = data.get("body").isObject().get("result").isObject().get("A").isArray();
		JSONArray bArray = data.get("body").isObject().get("result").isObject().get("B").isArray();
		JSONArray cArray = data.get("body").isObject().get("result").isObject().get("C").isArray();
		JSONArray showcaseArray = data.get("body").isObject().get("result").isObject().get("showcase").isArray();

		setArea("A", area(aArray));
		setArea("B", area(bArray));
		setArea("C", area(cArray));
		setArea("S", area(showcaseArray));
		 
	}

	@Override
	public void loadData() {

	}

}
