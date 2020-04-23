package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.OtherDepartmentMainEditor;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival.MonthlyContentDetailPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival.SeasonContentDetailPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.MainAreaComponent;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ServiceCompStack;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ServiceMain;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ViewPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.CourseDescription;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.HorizontalTagList;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.MainTitle;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.PairCircleImage;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.PairSquareImage;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.Showcase;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.SingleImageComponent;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.SingleImageWithDescription;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.SingleLargeImage;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.SingleSmallImage;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.SliderImageComponent;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.SubTitle;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.VerticalArticleList;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.VerticalSmallImageList;

public abstract class LayoutType {
	
	public static final String S = "S";
	public static final String A = "A";
	public static final String B = "B";
	public static final String C = "C";
	public static final String TAG = "TAG";
	public static final String SHOWCASE = "SHOWCASE";
	public static final String SHOWCASE_WITH_TITLE = "SHOWCASE_WITH_TITLE";
	public static final String TAB_MENU = "TAB_MENU";
	public static final String TOP_MENU = "TOP_MENU";
	public static final String BOTTOM_MENU = "BOTTOM_MENU";
	public static final String CONTENT_LIST = "CONTENT_LIST";
	public static final String CONTENT_VIEW = "CONTENT_VIEW";
	public static final String CONTENTS = "CONTENTS";
	public static final String SEASON = "SEASON";
	public static final String MONTHLY = "MONTHLY";
	public static final String MAIN_SHOWCASE = "MAIN_SHOWCASE";
	public static final String CURAITION = "CURAITION";
	public static final String MARKETING = "MARKETING";
	public static final String CALENDAR = "CALENDAR";
	public static final String AD = "AD";
	public static final String LOCAL_GOV = "LOCAL_GOV";
	public static final String IMAGE_BANNER = "IMAGE_BANNER";
	
	public LayoutType(ServiceMain serviceMain, ServiceCompStack serviceCompStack) {
		
		this.serviceMain = serviceMain;
		this.compStack = serviceCompStack;
		this.areaMap = new HashMap<String, List<MainAreaComponent>>();
		this.panelMap = new HashMap<String, ViewPanel>();
		initAreaMap();
		initPanelMap();
		
	}
	

	/**
	 * define abstract method
	 */
	public abstract void loadData();
	public abstract void layout();
	public abstract void redrawContentList();
	public abstract void initAreaMap();
	public abstract void initPanelMap();

	/**
	 * internal protected define.
	 */
	protected MaterialPanel tempPanel;
	protected OtherDepartmentMainEditor masterPanel;
	protected ServiceCompStack compStack;
	protected Map<String, List<MainAreaComponent>> areaMap;
	protected Map<String, ViewPanel> panelMap;
	protected MaterialPanel topMenuPanel;

	protected Boolean mainFlag;
	protected Boolean isCategoryMain = false;
	protected Boolean isMain;

	/**
	 * service main setup.
	 */
	
	protected ServiceMain serviceMain;
	protected int totalWidth;
	protected int totalHeight;
	protected int showcaseHeight;
	protected int tagAreaHeight;
	protected int leftB;
	protected int leftC;
	protected String otdId;
	protected String categoryType;
	protected JSONObject data;
	
	public void setServiceMain(ServiceMain serviceMain) {
		this.serviceMain = serviceMain;
	}
	
	public ServiceMain getServiceMain() {
		return this.serviceMain;
	}

	
	/**
	 * init layout values
	 */

	public void setupLayoutValues(int totalWidth, int totalHeight, int showcaseHeight, int tagAreaHeight) {
		
		this.totalWidth = totalWidth;
		this.totalHeight = totalHeight;
		this.showcaseHeight = showcaseHeight;
		this.tagAreaHeight = tagAreaHeight;
		this.leftB = totalWidth / 3;
		this.leftC = this.leftB * 2;

	}

	/**
	 * setup event handler
	 */
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
					
					this.compStack.appendComponentItems(null);
					this.compStack.setTargetArea(null); 
					
				}else if (areaString.startsWith("MONTHLY_")) {
					MaterialWidget seasonWidget = this.masterPanel.go(14);
					MonthlyContentDetailPanel mcdp = (MonthlyContentDetailPanel)seasonWidget;
					mcdp.setViewPanel(targetViewPanel);
					this.compStack.appendComponentItems(null);
					this.compStack.setTargetArea(null); 
				} else if (areaString.equals("IMAGE_BANNER")) {
					this.masterPanel.go(15);
					this.compStack.appendComponentItems(null);
					this.compStack.setTargetArea(null);
				} else {
					this.compStack.appendComponentItems(getComponentItems(areaString));
					this.compStack.setTargetArea(getAreaItems(areaString)); 
				}
			}
		});
		
	}
	
	protected ViewPanel getAreaItems(String areaName) {
		return this.panelMap.get(areaName);
	}

	protected List<MainAreaComponent> getComponentItems(String areaName){
		return this.areaMap.get(areaName);
	}

	protected ViewPanel getTargetAreaForS() {
		return this.panelMap.get(LayoutType.S);
	}

	protected ViewPanel getTargetAreaForA() {
		return this.panelMap.get(LayoutType.A);
	}

	protected ViewPanel getTargetAreaForB() {
		return this.panelMap.get(LayoutType.B);
	}

	protected ViewPanel getTargetAreaForC() {
		return this.panelMap.get(LayoutType.C);
	}

	public List<MainAreaComponent> getItemsForS() {
		return this.areaMap.get(LayoutType.S);
	}

	public List<MainAreaComponent> getItemsForA() {
		return this.areaMap.get(LayoutType.A);
	}

	public List<MainAreaComponent> getItemsForB() {
		return this.areaMap.get(LayoutType.B);
	}

	public List<MainAreaComponent> getItemsForC() {
		return this.areaMap.get(LayoutType.C);
	}

	public List<MainAreaComponent> getItemsForTags() {
		return this.areaMap.get(LayoutType.TAG);
	}
	
	public List<MainAreaComponent> getItemForShowcase() {
		return this.areaMap.get(LayoutType.SHOWCASE);
	}

	protected void executeBusiness(JSONObject jObj) {
		
		VisitKoreaBusiness.post("call", jObj.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {}
			
		});
		
	}
	
	public void setCompnentStack(ServiceCompStack compList) {
		this.compStack = compList;
	}
	
	public void setOtdId(String oTD_ID) {
		this.otdId = oTD_ID;
	}
	
	public void setMasterPanel(OtherDepartmentMainEditor masterPanel) {
		this.masterPanel = masterPanel;
	}
	
	public void setArea(String areaName, List<MainAreaComponent> area) {
		this.areaMap.put(areaName, area);
		if (this.panelMap.get(areaName) != null) this.panelMap.get(areaName).setComponentCount(area.size());
	}
	
	public void isMain(Boolean mainFlag) {
		this.mainFlag = mainFlag;
	}

	public void isCategoryMain(Boolean isCategoryMain) {
		this.isCategoryMain = isCategoryMain;
	}
	
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	protected List<MainAreaComponent> area(JSONArray aArray) {
		
		List<MainAreaComponent> compList = new ArrayList<MainAreaComponent>();
		
		String COMP_ID = null;
		int size = aArray.size();
		AreaComponent component = null;
		for (int i=0; i<size ; i++) {
			
			JSONObject jObj = aArray.get(i).isObject();
			
			String objCompId = "";

			if (jObj.get("COMP_ID") != null) objCompId = jObj.get("COMP_ID").isString().stringValue();
			
			if (objCompId == COMP_ID) {
				
				component.appendInfo(jObj);
				
			}else {

				COMP_ID = objCompId;
				int templateId = Integer.parseInt(jObj.get("TEMPLATE_ID").isString().stringValue());
				
				if (templateId == 1) {
					component = new SingleLargeImage(com.google.gwt.dom.client.Style.Float.LEFT);
				}else if (templateId == 2) {
					component = new PairSquareImage(com.google.gwt.dom.client.Style.Float.LEFT);
				}else if (templateId == 3) {
					component = new VerticalSmallImageList(com.google.gwt.dom.client.Style.Float.LEFT);
				}else if (templateId == 4) {
					component = new VerticalArticleList(com.google.gwt.dom.client.Style.Float.LEFT);
				}else if (templateId == 5) {
					component = new SingleImageWithDescription(com.google.gwt.dom.client.Style.Float.LEFT);
				}else if (templateId == 6) {
					component = new SingleSmallImage(com.google.gwt.dom.client.Style.Float.LEFT);
				}else if (templateId == 7) {
					component = new PairCircleImage(com.google.gwt.dom.client.Style.Float.LEFT);
				}else if (templateId == 8) {
					component = new CourseDescription(com.google.gwt.dom.client.Style.Float.LEFT);
				}else if (templateId == 9) {
					component = new HorizontalTagList(com.google.gwt.dom.client.Style.Float.LEFT);
				}else if (templateId == 10) {
					component = new Showcase(com.google.gwt.dom.client.Style.Float.LEFT);
				}else if (templateId == 11) {
					component = new MainTitle(com.google.gwt.dom.client.Style.Float.LEFT);
				}else if (templateId == 12) {
					component = new SubTitle(com.google.gwt.dom.client.Style.Float.LEFT);
				}else if (templateId == 13) {
					component = new SingleImageComponent(com.google.gwt.dom.client.Style.Float.LEFT);
				}else if (templateId == 14) {
					component = new SliderImageComponent(com.google.gwt.dom.client.Style.Float.LEFT);
				}

				component.appendInfo(jObj);
				compList.add(component);
				
				component.setCOMP_ID(jObj.get("COMP_ID").isString().stringValue());
				component.setCOMP_ORDER((int) jObj.get("COMP_ORDER").isNumber().doubleValue());
				component.setTEMPLATE_ID(jObj.get("TEMPLATE_ID").isString().stringValue());
				component.setMAIN_AREA(jObj.get("MAIN_AREA").isString().stringValue());
				component.setOTD_ID(jObj.get("OTD_ID").isString().stringValue());
				component.setODM_ID(jObj.get("ODM_ID").isString().stringValue());
				if (jObj.get("TITLE") != null) component.setTITLE(jObj.get("TITLE").isString().stringValue());
				component.setVIEW_TITLE((int) jObj.get("VIEW_TITLE").isNumber().doubleValue());
				
			}
			
		}
		
		return compList;
		
	}

	public MaterialLink appendLinkIcon(MaterialPanel tgrPanel, IconType iconType, Float floatStyle, String rightBorderStyle) {
		MaterialLink tmpLink = new MaterialLink();
		tmpLink.setIconType(iconType);
		tmpLink.setFloat(floatStyle);
		tmpLink.setBorderRight(rightBorderStyle);
		tmpLink.getIcon().setMargin(0);
		tgrPanel.add(tmpLink);
		
		return tmpLink;
	}

	public void setData(JSONObject data, Boolean isMain, Boolean isCategoryMain) {
		this.data = data;
		this.isMain = isMain;
		this.isCategoryMain = isCategoryMain;
		renderData();
	}

	protected abstract void renderData();

}
