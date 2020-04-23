package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets;
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

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.OtherDepartmentMainEditor;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.ContentLayoutPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.course.CourseContentLayout;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.recommand.RecommandContentLayoutA;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.recommand.RecommandContentLayoutB;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.recommand.RecommandContentLayoutC;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.sitesee.Sitesee11ContentLayout;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.sitesee.SiteseeCurationContentLayout;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.CourseDescription;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.HorizontalTagList;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.MainTitle;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.PairCircleImage;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.PairSquareImage;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.Showcase;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.SingleImageWithDescription;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.SingleLargeImage;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.SingleSmallImage;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.SubTitle;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.VerticalArticleList;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.VerticalSmallImageList;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.LayoutType;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.main.LayoutTypeCourseMain;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.main.LayoutTypeEventMain;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.main.LayoutTypeFestivalMain;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.main.LayoutTypeRecommandMain;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.main.LayoutTypeSightMain;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.otherDepartment.LayoutTypeA;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.otherDepartment.LayoutTypeB;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.otherDepartment.LayoutTypeC;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.otherDepartment.LayoutTypeD;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.layout.otherDepartment.LayoutTypeMain;

/**
 * @author yaken
 *
 */
public class ServiceMain extends MaterialPanel {

	private MainType mainType;
	private String title;
	private String serviceId;
	private MainAreaComponent tags;
	private MainAreaComponent showCase;
	private List<MainAreaComponent> areaA;
	private List<MainAreaComponent> areaB;
	private List<MainAreaComponent> areaC;
	private List<MainAreaComponent> areaTags;
	private List<MainAreaComponent> areaShowcase;
	private List<ContentLayoutPanel> contentLayoutList;

	private ViewPanel tagsPanel;
	private ViewPanel showcasePanel;
	private ViewPanel aPanel;
	private ViewPanel bPanel;
	private ViewPanel cPanel;
	
	private int totalWidth;
	private int totalHeight;
	private int showcaseHeight;
	private int tagAreaHeight;
	private int leftB;
	private int leftC;

	private ServiceCompStack compStack;

	private MaterialPanel tempPanel;
	private MaterialPanel topMenuPanel;
	private MaterialLink viewServiceMain;
	private OtherDepartmentMainEditor masterPanel;
	private String otdId;
	private ViewPanel curationPanel;
	private ViewPanel marketingPanel;
	private ViewPanel localGovPanel;
	private ViewPanel calendarPanel;
	private ViewPanel adPanel;
	private Boolean mainFlag;
	private Boolean isCategoryMain = false;
	private String categoryType;
	private MaterialLink appendAreaMain;
	private ViewPanel contentPanel;
	private LayoutType layoutType = null;
	private String preview;
	
	public ServiceMain(MainType mainType, int totalWidth, int totalHeight,  int showcaseHeight, int tagAreaHeight) {
		super();

		this.areaTags = new ArrayList<MainAreaComponent>();
		this.areaShowcase = new ArrayList<MainAreaComponent>();
		this.areaA = new ArrayList<MainAreaComponent>();
		this.areaB = new ArrayList<MainAreaComponent>();
		this.areaC = new ArrayList<MainAreaComponent>();
		this.mainType = mainType;
		
		Console.log("this.mainType :: " + this.mainType);
		
		this.setBorder("1px solid #aaaaaa");
		setupLayoutValues(totalWidth, totalHeight, showcaseHeight, tagAreaHeight);
		
	}


	private void setupLayoutValues(int totalWidth, int totalHeight, int showcaseHeight, int tagAreaHeight) {
		this.totalWidth = totalWidth;
		this.totalHeight = totalHeight;
		this.showcaseHeight = showcaseHeight;
		this.tagAreaHeight = tagAreaHeight;
		this.leftB = totalWidth / 3;
		this.leftC = this.leftB * 2;
		
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight(totalHeight + "px");
		this.setWidth(totalWidth + "px");
	}
	
	
	public int getTotalWidth() {
		return totalWidth;
	}


	public void setTotalWidth(int totalWeight) {
		this.totalWidth = totalWeight;
	}


	public int getTotalHeight() {
		return totalHeight;
	}


	public void setTotalHeight(int totalHeight) {
		this.totalHeight = totalHeight;
	}


	private void setupIconEvent() {
		
		this.viewServiceMain.addClickHandler(event->{
			Registry.openPreview(viewServiceMain, (String) Registry.get("service.server")  + "/other/otherService.html?otdid=" + otdId);
		});
		
	}

	public List<MainAreaComponent> getAreaA() {
		return (this.layoutType == null) ? areaA : this.layoutType.getItemsForA();
	}

	public void setAreaA(List<MainAreaComponent> areaA) {
		if (layoutType == null) {
			this.areaA = areaA;
			this.aPanel.setComponentCount(areaA.size());
		}else {
			this.layoutType.setArea(LayoutType.A, areaA);
		}
	}

	public List<MainAreaComponent> getAreaB() {
		return (this.layoutType == null) ? areaB : this.layoutType.getItemsForB();
	}

	public void setAreaB(List<MainAreaComponent> areaB) {
		if (this.layoutType == null) {
			this.areaB = areaB;
			this.bPanel.setComponentCount(areaB.size());
		}else {
			this.layoutType.setArea(LayoutType.B, areaB);
		}
	}

	public List<MainAreaComponent> getAreaC() {
		return (this.layoutType == null) ? areaC : this.layoutType.getItemsForA();
	}

	public void setAreaC(List<MainAreaComponent> areaC) {
		if (this.layoutType == null) {
			this.areaC = areaC;
			this.cPanel.setComponentCount(areaC.size());
		}else {
			this.layoutType.setArea(LayoutType.C,areaC);
		}
	}

	public List<MainAreaComponent> getAreaTags() {
		return (this.layoutType == null) ? areaTags : this.layoutType.getItemsForTags();
	}

	public void setAreaTags(List<MainAreaComponent> areaTags) {
		if (this.layoutType == null) {
			this.areaTags = areaTags;
			this.tagsPanel.setComponentCount(areaTags.size());
		}else {
			this.layoutType.setArea(LayoutType.TAG, areaTags);
		}
	}

	public List<MainAreaComponent> getAreaShowcase() {
		return (this.layoutType == null) ? areaShowcase : this.layoutType.getItemForShowcase();
	}

	public void setAreaShowcase(List<MainAreaComponent> areaShowcase) {
		if (this.layoutType == null) {
			this.areaShowcase = areaShowcase;
			this.showcasePanel.setComponentCount(areaShowcase.size());
		}else {
			this.layoutType.setArea(LayoutType.S, areaShowcase);
		}
	}

	private void initLayout() {

		if (this.mainType.equals(MainType.A)) {
			
			layoutType = new LayoutTypeA(this, this.compStack);
			layoutType.setMasterPanel(this.masterPanel);
			layoutType.setupLayoutValues(totalWidth, totalHeight, showcaseHeight, tagAreaHeight);
			layoutType.layout();
				
			this.setLayoutPosition(Position.RELATIVE);
			this.setHeight(totalHeight + "px");
			this.setWidth(totalWidth + "px");
			
		}else if (this.mainType.equals(MainType.B)) {
			
			layoutType = new LayoutTypeB(this, this.compStack);
			layoutType.setMasterPanel(this.masterPanel);
			layoutType.setupLayoutValues(totalWidth, totalHeight, showcaseHeight, tagAreaHeight);
			layoutType.layout();
			
			this.setLayoutPosition(Position.RELATIVE);
			this.setHeight(totalHeight + "px");
			this.setWidth(totalWidth + "px");
			
		}else if (this.mainType.equals(MainType.C)) {
			
			layoutType = new LayoutTypeC(this, this.compStack);
			layoutType.setMasterPanel(this.masterPanel);
			layoutType.setupLayoutValues(totalWidth, totalHeight, showcaseHeight, tagAreaHeight);
			layoutType.layout();
			
			this.setLayoutPosition(Position.RELATIVE);
			this.setHeight(totalHeight + "px");
			this.setWidth(totalWidth + "px");
			
		}else if (this.mainType.equals(MainType.D)) {
			
			layoutType = new LayoutTypeD(this, this.compStack);
			layoutType.setMasterPanel(this.masterPanel);
			layoutType.setupLayoutValues(totalWidth, totalHeight, showcaseHeight, tagAreaHeight);
			layoutType.layout();
			
			this.setLayoutPosition(Position.RELATIVE);
			this.setHeight(totalHeight + "px");
			this.setWidth(totalWidth + "px");
			
		}else if (this.mainType.equals(MainType.MA)) {
			
			layoutType = new LayoutTypeMain(this, this.compStack);
			layoutType.setMasterPanel(this.masterPanel);
			layoutType.setupLayoutValues(totalWidth, totalHeight, showcaseHeight, tagAreaHeight);
			layoutType.layout();
			
			this.setLayoutPosition(Position.RELATIVE);
			this.setHeight(totalHeight + "px");
			this.setWidth(totalWidth + "px");
		
		}else if (this.mainType.equals(MainType.CAA)) {

			this.totalWidth += 130;
			
			layoutType = new LayoutTypeRecommandMain(this, this.compStack);
			layoutType.setMasterPanel(this.masterPanel);
			layoutType.setupLayoutValues(totalWidth, totalHeight, showcaseHeight, tagAreaHeight);
			layoutType.layout();
			
			this.compStack.setVisible(false);
			this.setLayoutPosition(Position.RELATIVE);
			this.setHeight(totalHeight + "px");
			this.setWidth(totalWidth + "px");

		}else if (this.mainType.equals(MainType.CAB)) {

			this.totalWidth += 130;
			
			layoutType = new LayoutTypeCourseMain(this, this.compStack);
			layoutType.setMasterPanel(this.masterPanel);
			layoutType.setupLayoutValues(totalWidth, totalHeight, showcaseHeight, tagAreaHeight);
			layoutType.layout();
			
			this.compStack.setVisible(false);
			this.setLayoutPosition(Position.RELATIVE);
			this.setHeight(totalHeight + "px");
			this.setWidth(totalWidth + "px");
			
		}else if (this.mainType.equals(MainType.CAC)) {

			this.totalWidth += 130;
			
			layoutType = new LayoutTypeSightMain(this, this.compStack);
			layoutType.setMasterPanel(this.masterPanel);
			layoutType.setupLayoutValues(totalWidth, totalHeight, showcaseHeight, tagAreaHeight);
			layoutType.layout();
			
			this.compStack.setVisible(false);
			this.setLayoutPosition(Position.RELATIVE);
			this.setHeight(totalHeight + "px");
			this.setWidth(totalWidth + "px");

		}else if (this.mainType.equals(MainType.CAD)) {

			this.totalWidth += 130;
			
			layoutType = new LayoutTypeFestivalMain(this, this.compStack);
			layoutType.setMasterPanel(this.masterPanel);
			layoutType.setupLayoutValues(totalWidth, totalHeight, showcaseHeight, tagAreaHeight);
			layoutType.layout();
			
			this.compStack.setVisible(false);
			this.setLayoutPosition(Position.RELATIVE);
			this.setHeight(totalHeight + "px");
			this.setWidth(totalWidth + "px");
			
		}else if (this.mainType.equals(MainType.CAE)) {

			this.totalWidth += 130;
			
			layoutType = new LayoutTypeEventMain(this, this.compStack);
			layoutType.setMasterPanel(this.masterPanel);
			layoutType.setupLayoutValues(totalWidth, totalHeight, showcaseHeight, tagAreaHeight);
			layoutType.layout();
			
			this.compStack.setVisible(false);
			this.setLayoutPosition(Position.RELATIVE);
			this.setHeight(totalHeight + "px");
			this.setWidth(totalWidth + "px");

		}
		
	}

	private void addAppendAreaMain(int typeIndex) {
		
		if (typeIndex == 2) {
			appendAreaMain.addClickHandler(event->{
				buildContentTemplate("course");
			});
		}else{
			appendAreaMain.addClickHandler(event->{
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("TARGET", this);
				map.put("DISP_TYPE", typeIndex);
				
				masterPanel
				.getMaterialExtentsWindow()
				.openDialog( OtherDepartmentMainApplication.CATEGORY_MAIN_SELECT_CONTENT_LAYOUT, map, 800);
				
			});
		}
		
	}


	private MaterialLink appendLinkIcon(MaterialPanel tgrPanel, IconType iconType, Float floatStyle, String rightBorderStyle) {
		MaterialLink tmpLink = new MaterialLink();
		tmpLink.setIconType(iconType);
		tmpLink.setFloat(floatStyle);
		tmpLink.setBorderRight(rightBorderStyle);
		tmpLink.getIcon().setMargin(0);
		tgrPanel.add(tmpLink);
		
		return tmpLink;
	}

	private void redrawContentList() {
		contentPanel.clear();
		for (ContentLayoutPanel panel : contentLayoutList) {
			panel.loadData();
			contentPanel.add(panel);
		}
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
			
			String areaString = tgrPanel.getArea().toUpperCase();
			this.compStack.setAreaValue(areaString);
			
			if (areaString != null && areaString.length() > 1) {

				this.compStack.appendComponentItems(null);
				this.compStack.setTargetArea(null);
				
				if (areaString.equals("TAG")) {
					this.masterPanel.go(1);
				}else if (areaString.equals("MAIN_SHOWCASE")) {
					this.masterPanel.go(3);
				}else if (areaString.equals("CURAITION")) {
					this.masterPanel.go(4);
				}else if (areaString.equals("MARKETING")) {
					this.masterPanel.go(5);
				}else if (areaString.equals("LOCAL_GOV")) {
					this.masterPanel.go(6);
				}else if (areaString.equals("CALENDAR")) {
					this.masterPanel.go(7);
				}else if (areaString.equals("AD")) {
					this.masterPanel.go(8);
				}else if (areaString.equals("CONTENTS")) {
					this.masterPanel.go(10);
				}else if (areaString.equals("S")) {
					this.compStack.appendComponentItems(areaShowcase);
					this.compStack.setTargetArea(showcasePanel);
				}else if (areaString.equals("A")) {
					this.compStack.appendComponentItems(areaA);
					this.compStack.setTargetArea(aPanel);
				}else if (areaString.equals("B")) {
					this.compStack.appendComponentItems(areaB);
					this.compStack.setTargetArea(bPanel);
				}else if (areaString.equals("C")) {
					this.compStack.appendComponentItems(areaC);
					this.compStack.setTargetArea(cPanel);
				}
				
			}
		});
		
	}

	public void addTAGS(String ... tags ) {
		
		for (String tag : tags) {
			MaterialLabel tagLabel = new MaterialLabel(tag);
			tagLabel.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
			tagLabel.setFontWeight(FontWeight.BOLD);
			tagLabel.setMargin(10);
			tagLabel.setLineHeight(30);
			tagsPanel.add(tagLabel);
		}

	}
	
	public MaterialPanel getTagPanel() {
		return this.tagsPanel;
	}
	
	public MaterialPanel getAPanel() {
		return this.aPanel;
	}
	
	public MaterialPanel getBPanel() {
		return this.bPanel;
	}
	
	public MaterialPanel getCPanel() {
		return this.cPanel;
	}
	
	public MaterialPanel getShowcasePanel() {
		return this.showcasePanel;
	}

	public void setCompStack(ServiceCompStack compList) {
		if (this.layoutType == null) {
			this.compStack = compList;
		}else {
			this.layoutType.setCompnentStack(compList);
		}
	}

	public native String openWindow(String url)/*-{
	return $wnd.open(url,
	'target=_blank')
	}-*/;

	public void setMasterPanel(OtherDepartmentMainEditor otherDepartmentMainEditor) {
		if (this.layoutType == null) {
			this.masterPanel = otherDepartmentMainEditor;
		}else { 
			this.layoutType.setMasterPanel(otherDepartmentMainEditor);
		}
	}

	public void setOtdId(String oTD_ID) {
		if (this.layoutType == null) {
			this.otdId = oTD_ID;
		}else {
			this.layoutType.setOtdId(oTD_ID);
		}
		
	}
	
	
	public void setPreview(String preview) {
		this.preview = preview;
	}
	
	public String getOtdId() {
		return this.otdId;
	}
	
	public String getPreview() {
		return preview;
	}
	
	public void setTempate(String serviceTemplate) {
		clear();
		
		if (layoutType == null) {
			this.mainType = MainType.valueOf(serviceTemplate);
			initLayout();
			//setupIconEvent();
		}else {
			this.mainType = MainType.valueOf(serviceTemplate);
			initLayout();
		}
		
	}
	
	public void isMain(Boolean mainFlag) {
		if (this.layoutType == null) {
			this.mainFlag = mainFlag;
		}else {
			this.layoutType.isMain(mainFlag);
		}
	}

	public void isCategoryMain(Boolean isCategoryMain) {
		this.isCategoryMain = isCategoryMain;
		if (this.layoutType == null) {
			this.isCategoryMain = isCategoryMain;
		}else {
			this.layoutType.isCategoryMain(isCategoryMain);
		}
	}

	public void setCategoryType(String categoryType) {
		if (this.layoutType == null) {
			this.categoryType = categoryType;
		}else {
			this.layoutType.setCategoryType(categoryType);
		}
	}

	public ContentLayoutPanel buildContentTemplate(String contentType) {
		return buildContentTemplate(contentType, null);
	}
	
	public ContentLayoutPanel buildContentTemplate(String contentType, JSONObject value) {
		
		ContentLayoutPanel retPanel = null;
		
		if (contentType.equals("3")) {
			RecommandContentLayoutA layoutA = new RecommandContentLayoutA();
			layoutA.setWindow(this.masterPanel.getMaterialExtentsWindow());
			retPanel = layoutA;
			this.contentLayoutList.add(layoutA);
		}else if (contentType.equals("4")) {
			RecommandContentLayoutB layoutB = new RecommandContentLayoutB();			
			layoutB.setWindow(this.masterPanel.getMaterialExtentsWindow());
			retPanel = layoutB;
			this.contentLayoutList.add(layoutB);
		}else if (contentType.equals("5")) {
			RecommandContentLayoutC layoutC = new RecommandContentLayoutC();			
			layoutC.setWindow(this.masterPanel.getMaterialExtentsWindow());
			retPanel = layoutC;
			this.contentLayoutList.add(layoutC);
		}else if (contentType.equals("11content")) {
			Sitesee11ContentLayout sitesee11ContentLayout = new Sitesee11ContentLayout();			
			sitesee11ContentLayout.setWindow(this.masterPanel.getMaterialExtentsWindow());
			retPanel = sitesee11ContentLayout;
			this.contentLayoutList.add(sitesee11ContentLayout);
		}else if (contentType.equals("curation")) {
			SiteseeCurationContentLayout siteseeCurationContentLayout = new SiteseeCurationContentLayout();			
			siteseeCurationContentLayout.setWindow(this.masterPanel.getMaterialExtentsWindow());
			retPanel = siteseeCurationContentLayout;
			this.contentLayoutList.add(siteseeCurationContentLayout);
		}else if (contentType.equals("course")) {
			CourseContentLayout courseContentLayout = new CourseContentLayout();			
			courseContentLayout.setWindow(this.masterPanel.getMaterialExtentsWindow());
			retPanel = courseContentLayout;
			this.contentLayoutList.add(courseContentLayout);
		}
		
		retPanel.setData(value);
		
		redrawContentList();
		
		return retPanel;
	}

	protected void buildComponent(JSONObject tgrCompObj) {
		
		String compType = tgrCompObj.get("COMP_TYPE").isString().stringValue();
		buildContentTemplate(compType, tgrCompObj);
		
	}

	private void executeBusiness(JSONObject jObj) {
		
		VisitKoreaBusiness.post("call", jObj.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {}
		});
	}


	public void setData(JSONObject resultObj, Boolean isMain, Boolean isCategoryMain) {
		if (this.layoutType == null) {
			
			JSONArray aArray = resultObj.get("body").isObject().get("result").isObject().get("A").isArray();
			JSONArray bArray = resultObj.get("body").isObject().get("result").isObject().get("B").isArray();
			JSONArray cArray = resultObj.get("body").isObject().get("result").isObject().get("C").isArray();
			JSONArray showcaseArray = resultObj.get("body").isObject().get("result").isObject().get("showcase").isArray();
		
			isMain(isMain);
			setAreaA(area(aArray));
			setAreaB(area(bArray));
			setAreaC(area(cArray));
			
			if (!isMain) {
				setAreaShowcase(area(showcaseArray));
			}

		}else {
			this.layoutType.setData(resultObj, isMain, isCategoryMain);
		}
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
				}
				
				if (component != null) {
					
					compList.add(component);
					component.appendInfo(jObj);					

					if (jObj.get("COMP_ID") != null) component.setCOMP_ID(jObj.get("COMP_ID").isString().stringValue());
					if (jObj.get("COMP_ORDER") != null) component.setCOMP_ORDER((int) jObj.get("COMP_ORDER").isNumber().doubleValue());
					if (jObj.get("TEMPLATE_ID") != null) component.setTEMPLATE_ID(jObj.get("TEMPLATE_ID").isString().stringValue());
					if (jObj.get("MAIN_AREA") != null) component.setMAIN_AREA(jObj.get("MAIN_AREA").isString().stringValue());
					if (jObj.get("OTD_ID") != null) component.setOTD_ID(jObj.get("OTD_ID").isString().stringValue());
					if (jObj.get("ODM_ID") != null) component.setODM_ID(jObj.get("ODM_ID").isString().stringValue());
					if (jObj.get("VIEW_TITLE") != null) component.setVIEW_TITLE((int) jObj.get("VIEW_TITLE").isNumber().doubleValue());
					if (jObj.get("TITLE") != null) component.setTITLE(jObj.get("TITLE").isString().stringValue());
					
				}
				
			}
			
		}
		
		return compList;
		
	}

	
}