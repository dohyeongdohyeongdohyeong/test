package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialPanel;

public class LayoutA extends ViewPanelLayout {

	private MaterialPanel topMenuPanel;
	private Map<String, ViewPanel> viewPanelMap = new HashMap<String, ViewPanel>();
	private ViewPanel tempPanel;
	private ServiceCompStack compStack;
	private ViewPanel cPanel;
	private ViewPanel bPanel;
	private ViewPanel aPanel;
	private ArrayList<MainAreaComponent> areaShowcase;
	private ArrayList<MainAreaComponent> areaA;
	private ArrayList<MainAreaComponent> areaB;
	private ArrayList<MainAreaComponent> areaC;
	
	private int tagAreaHeight;
	private int totalHeight;
	private int totalWeight;
	
	private double showcaseHeight;
	private double leftB;
	private double leftC;

	public LayoutA(ServiceCompStack compStack, int tagAreaHeight, int totalWeight, int totalHeight, double showcaseHeight) {
		
		this.compStack = compStack;
		this.areaShowcase = new ArrayList<MainAreaComponent>();
		this.areaA = new ArrayList<MainAreaComponent>();
		this.areaB = new ArrayList<MainAreaComponent>();
		this.areaC = new ArrayList<MainAreaComponent>();
		
		this.tagAreaHeight = tagAreaHeight;
		this.totalWeight = totalWeight;
		this.totalHeight = totalHeight;

		this.showcaseHeight = showcaseHeight;
		this.leftB = totalWeight / 3;
		this.leftC = this.leftB * 2;
		
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight(totalHeight + "px");
		this.setWidth(totalWeight + "px");

		
	}

	@Override
	public void layout() {
		
		topMenuPanel = new MaterialPanel();
		topMenuPanel.setWidth("100%");
		topMenuPanel.setHeight("24px");
		topMenuPanel.setLayoutPosition(Position.ABSOLUTE);
		topMenuPanel.setTop(-30);
		topMenuPanel.setLeft(0);
		
		this.add(topMenuPanel);

		ViewPanel tagsPanel = getViewPanelInstance("tagsPanel");
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
		this.add(tagsPanel);
		setupDefaultEvent(tagsPanel);

		ViewPanel showcasePanel = getViewPanelInstance("showcasePanel");
		showcasePanel.getElement().setAttribute("area", "SHOWCASE");
		showcasePanel.setAreaName("쇼케이스 영역");
		showcasePanel.getAreaNameLabel().setLineHeight(this.showcaseHeight);
		showcasePanel.setLayoutPosition(Position.ABSOLUTE);
		showcasePanel.setTop(tagAreaHeight);
		showcasePanel.setLeft(0);
		showcasePanel.setWidth("66.6666%");
		showcasePanel.setHeight(this.showcaseHeight + "px");
		showcasePanel.setBorder("1px solid #aaaaaa");
		showcasePanel.setBackgroundColor(Color.WHITE);
		this.add(showcasePanel);
		setupDefaultEvent(showcasePanel);
		
		int aPanelTop = (int) (tagAreaHeight + showcaseHeight);
		int offsetHeight = this.totalHeight;
		int offsetWeight = this.totalWeight / 3;
		int aPanelHeight = offsetHeight - aPanelTop;
		
		if (aPanelHeight < 0) aPanelHeight = 100;
		
		aPanel = getViewPanelInstance("aPanel");
		aPanel.getElement().setAttribute("area", "A");
		aPanel.setAreaName("A 영역");
		aPanel.getAreaNameLabel().setLineHeight(aPanelHeight);
		aPanel.setLayoutPosition(Position.ABSOLUTE);
		aPanel.setTop(aPanelTop);
		aPanel.setLeft(0);
		aPanel.setWidth("33.3333%");
		aPanel.setHeight(aPanelHeight + "px");
		aPanel.setBorder("1px solid #aaaaaa");
		aPanel.setBackgroundColor(Color.WHITE);
		this.add(aPanel);
		setupDefaultEvent(aPanel);
		
		bPanel = getViewPanelInstance("bPanel");
		bPanel.getElement().setAttribute("area", "B");
		bPanel.setAreaName("B 영역");
		bPanel.getAreaNameLabel().setLineHeight(aPanelHeight);
		bPanel.setLayoutPosition(Position.ABSOLUTE);
		bPanel.setTop(aPanelTop);
		bPanel.setLeft(this.leftB);
		bPanel.setWidth("33.3333%");
		bPanel.setHeight(aPanelHeight + "px");
		bPanel.setBorder("1px solid #aaaaaa");
		bPanel.setBackgroundColor(Color.WHITE);
		this.add(bPanel);
		setupDefaultEvent(bPanel);
		
		cPanel = getViewPanelInstance("cPanel");
		cPanel.getElement().setAttribute("area", "C");
		cPanel.setAreaName("C 영역");
		cPanel.getAreaNameLabel().setLineHeight((offsetHeight - tagAreaHeight));
		cPanel.setLayoutPosition(Position.ABSOLUTE);
		cPanel.setTop(tagAreaHeight);
		cPanel.setLeft(this.leftC);
		cPanel.setWidth("33.6%");
		cPanel.setHeight((offsetHeight - tagAreaHeight) + "px");
		cPanel.setBorder("1px solid #aaaaaa");
		cPanel.setBackgroundColor(Color.WHITE);
		this.add(cPanel);
		setupDefaultEvent(cPanel);		
	}
	
	private void setupDefaultEvent(ViewPanel tgrPanel) {

		tgrPanel.addMouseOutHandler(event->{
			if (tgrPanel != tempPanel) {
				tgrPanel.getElement().getStyle().setCursor(Cursor.DEFAULT);
				tgrPanel.setBackgroundColor(Color.WHITE);
				tgrPanel.setTextColor(Color.BLACK);
			}
		});

		tgrPanel.addMouseOverHandler(event->{
			if (tgrPanel != tempPanel) {
				tgrPanel.getElement().getStyle().setCursor(Cursor.POINTER);
				tgrPanel.setBackgroundColor(Color.BLUE_LIGHTEN_4);
				tgrPanel.setTextColor(Color.WHITE);
			}
		});

		tgrPanel.addClickHandler(event->{
			
			if (tempPanel != null) {
				tempPanel.setBackgroundColor(Color.WHITE);
				tempPanel.setTextColor(Color.BLACK);
			}
			
			tgrPanel.getElement().getStyle().setCursor(Cursor.POINTER);
			tgrPanel.setBackgroundColor(Color.BLUE);
			tgrPanel.setTextColor(Color.WHITE);
			tempPanel = tgrPanel;
			
			this.compStack.clearRow();
			
			String areaString = tgrPanel.getElement().getAttribute("area");
			this.compStack.setAreaValue(areaString);
			
			if (areaString.equals("A")) {
				this.compStack.appendComponentItems(areaA);
				this.compStack.setTargetArea(aPanel);
			}else if (areaString.equals("B")) {
				this.compStack.appendComponentItems(areaB);
				this.compStack.setTargetArea(bPanel);
			}else if (areaString.equals("C")) {
				this.compStack.appendComponentItems(areaC);
				this.compStack.setTargetArea(cPanel);
			}
			
		});
	}

	private ViewPanel getViewPanelInstance(String panelName) {
		
		if (viewPanelMap.get(panelName) == null) {
			viewPanelMap.put(panelName, new ViewPanel());
		}
		
		return viewPanelMap.get(panelName);
	}


}
