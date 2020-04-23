package kr.or.visitkorea.admin.client.manager.otherDepartment.composite;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentBundle;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ad.AdContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.banner.MainImageBanner;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.calendar.CalendarContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.contents.OtherDepartmentContentListDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.curation.CurationContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.detail.OtherDepartmentMainContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.detail.OtherDepartmentMainContentDetailForShowcase;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival.MonthlyContentDetailPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival.SeasonContentDetailPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.localGov.LocalGovernmentContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.marketing.MarketingContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.showcase.ShowcaseContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.tabmenu.OtherDepartmentTabMenuContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.tags.OtherDepartmentMainContentsTags;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.title.OtherDepartmentTitleContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.MainType;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ServiceCompStack;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ServiceMain;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class OtherDepartmentMainEditor extends AbstractContentPanel {

	private MaterialTextBox authors;
	private MaterialTextArea noticeComment;
	private SelectionPanel selectOne;
	private MaterialPanel noticeButton;
	private MaterialPanel panel;
	private UploadPanel uploadPanel;
	private MaterialColumn col1;
	private MaterialColumn col2;
	private int nowPosition;
	private MaterialPanel contentArea;
	private ServiceMain serviceMain;
	private ServiceCompStack compStack;
	private String OTD_ID;
	private String MAN_ID;
	private CurationContentDetail curationDetail;
	private ShowcaseContentDetail showcaseDetail;
	private MarketingContentDetail marketingDetail;
	private LocalGovernmentContentDetail localGovDetail;
	private OtherDepartmentMainContentsTags odmct;
	private MainImageBanner MainImageBannerPanel;

	static {
		MaterialDesignBase.injectCss(RecommContentBundle.INSTANCE.contentCss());
	}

	public OtherDepartmentMainEditor(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		appendMainStyleComponent();
	}

	private OtherDepartmentMainEditor getPanel() {
		return this;
	}
	
	@Override
	public void onLoad() {
		
		OTD_ID = (String)this.getWindowParameters().get("OTD_ID");
		MAN_ID = (String)this.getWindowParameters().get("MAN_ID");
		Console.log("MAN_ID:::::::::::::::::::::::::::::::::::::::::::::::::::"+ MAN_ID);
		// setup otdid;
		this.serviceMain.setOtdId(OTD_ID);
		this.compStack.setOtdId(OTD_ID);
		this.curationDetail.setOtdId(OTD_ID);
		this.showcaseDetail.setOtdId(OTD_ID);
		this.localGovDetail.setOtdId(OTD_ID);
		
		// setup manid;
		this.compStack.setManId(MAN_ID);
		this.curationDetail.setManId(MAN_ID);
		this.MainImageBannerPanel.setManId(MAN_ID);
		this.showcaseDetail.setManId(MAN_ID);
		this.marketingDetail.setManId(MAN_ID);
		this.odmct.setManId(MAN_ID);
		
		// setup preview
		if (this.getWindowParameters().get("PREVIEW_URL") != null)
			this.serviceMain.setPreview((String) this.getWindowParameters().get("PREVIEW_URL"));
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_OTHER_DEPARTMENT_MAIN"));
		parameterJSON.put("otdId", new JSONString(OTD_ID));
		parameterJSON.put("chk", new JSONString(IDUtil.uuid()));
		Boolean IS_MAIN = (Boolean)getWindowParameters().get("IS_MAIN");
		if (IS_MAIN != null) parameterJSON.put("isMain", new JSONString(IS_MAIN+""));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					
					JSONObject service = resultObj.get("body").isObject().get("result").isObject().get("service").isObject();
					String serviceTemplate = service.get("TEMPLATE_TYPE").isString().stringValue();

					Boolean IS_MAIN = (Boolean)getWindowParameters().get("IS_MAIN");
					Boolean IS_CATEGORY_MAIN = (Boolean)getWindowParameters().get("IS_CATEGORY_MAIN");
					
					if (IS_CATEGORY_MAIN == null) IS_CATEGORY_MAIN = false;
					
					String IS_CATEGORY_MAIN_TYPE = null;
					if (IS_CATEGORY_MAIN) {
						IS_CATEGORY_MAIN_TYPE = (String)getWindowParameters().get("IS_CATEGORY_MAIN_TYPE");
						serviceMain.isCategoryMain(IS_CATEGORY_MAIN);
						serviceMain.setCategoryType(IS_CATEGORY_MAIN_TYPE);
						serviceMain.setTempate(IS_CATEGORY_MAIN_TYPE);
					}else {
						serviceMain.setTempate(serviceTemplate);
					}

					serviceMain.isMain(IS_MAIN);
					serviceMain.setData(resultObj, IS_MAIN, IS_CATEGORY_MAIN);
					
				}
			}
		});
				

		
	}

	private void appendMainStyleComponent() {
		
		compStack = new ServiceCompStack();
		compStack.setBorder("1px solid #aaaaaa");
		compStack.setTop(60);
		compStack.setLeft(428);
		compStack.setLayoutPosition(Position.ABSOLUTE);
		compStack.setWidth("130px");
		compStack.setHeight("570px");
		compStack.setMasterPanel(this);
		
		serviceMain = new ServiceMain(MainType.A, 400, 570, 250, 50);
		serviceMain.setTop(60);
		serviceMain.setLeft(30);
		serviceMain.setMasterPanel(this);
		serviceMain.setCompStack(compStack);
		compStack.setServiceMain(serviceMain);
		
		this.add(serviceMain);
		this.add(compStack);
		
		MaterialPanel editArea = new MaterialPanel();
		editArea.setTop(60);
		editArea.setOverflow(Overflow.HIDDEN);
		editArea.setLeft(575);
		editArea.setRight(50);
		editArea.setBottom(30);
		editArea.setBorder("1px solid #aaaaaa");
		editArea.setLayoutPosition(Position.ABSOLUTE);
		this.add(editArea);

		contentArea = new MaterialPanel();
		contentArea.setLayoutPosition(Position.ABSOLUTE);
		contentArea.setTop(0);
		contentArea.setLeft(0);
		contentArea.setBottom(0);
		contentArea.setHeight("100%");
		contentArea.addAttachHandler(event->{
			contentArea.getWidgetCount();
			contentArea.setWidth((883.01 * contentArea.getWidgetCount()) + "px");
		});
		editArea.add(contentArea);
		
		// -- 0 : 태그 
		odmct = new OtherDepartmentMainContentsTags(getMaterialExtentsWindow(), this);
		odmct.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		contentArea.add(odmct);
		
		// -- 1 : 컨텐츠 상세
		OtherDepartmentMainContentDetail odmcd = new OtherDepartmentMainContentDetail(getMaterialExtentsWindow(), this);
		odmcd.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		contentArea.add(odmcd);
		
		// -- 2 : 메인 쇼케이스 컨텐츠 상세
		showcaseDetail = new ShowcaseContentDetail(getMaterialExtentsWindow(), this, MAN_ID);
		showcaseDetail.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		showcaseDetail.setOtdId(OTD_ID);
		contentArea.add(showcaseDetail);
		
		// -- 3 : 메인 큐레이션 컨텐츠 상세
		curationDetail = new CurationContentDetail(getMaterialExtentsWindow(), this, MAN_ID);
		curationDetail.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		curationDetail.setOtdId(OTD_ID);
		contentArea.add(curationDetail);
		
		// -- 4 : 메인 마케팅 컨텐츠 상세
		marketingDetail = new MarketingContentDetail(getMaterialExtentsWindow(), this);
		marketingDetail.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		contentArea.add(marketingDetail);
		
		// -- 5 : 메인 지자체 컨텐츠 상세
		localGovDetail = new LocalGovernmentContentDetail(getMaterialExtentsWindow(), this, OTD_ID);
		localGovDetail.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		contentArea.add(localGovDetail);
		
		// -- 6 : 메인 달력 컨텐츠 상세
		CalendarContentDetail calendarDetail = new CalendarContentDetail(getMaterialExtentsWindow(), this);
		calendarDetail.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		contentArea.add(calendarDetail);
		
		// -- 7 : 메인 광고 컨텐츠 상세
		AdContentDetail adDetail = new AdContentDetail(getMaterialExtentsWindow(), this);
		adDetail.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		contentArea.add(adDetail);
		
		// -- 8 : 타부서 쇼케이스 상세 ( type C, D )
		OtherDepartmentMainContentDetailForShowcase compDetailForShowcase = new OtherDepartmentMainContentDetailForShowcase(getMaterialExtentsWindow(), this);
		compDetailForShowcase.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		contentArea.add(compDetailForShowcase);
		
		// -- 9 : 타부서 제목/서브 제목 상세 ( type C, D )
		OtherDepartmentTitleContentDetail otherDepartmentTitleContentDetail = new OtherDepartmentTitleContentDetail(getMaterialExtentsWindow(), this);
		otherDepartmentTitleContentDetail.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		contentArea.add(otherDepartmentTitleContentDetail);
		 
		// -- 10 : 탭메뉴 영역 상세 ( type C, D )
		OtherDepartmentTabMenuContentDetail otherDepartmentTabMenuContentDetail = new OtherDepartmentTabMenuContentDetail(getMaterialExtentsWindow(), this, OTD_ID);
		otherDepartmentTabMenuContentDetail.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		contentArea.add(otherDepartmentTabMenuContentDetail);
		
		// -- 11 : 컨텐츠 리스트 영역 상세 ( type C, D )
		OtherDepartmentContentListDetail otherDepartmentContentListDetail = new OtherDepartmentContentListDetail(getMaterialExtentsWindow(), this);
		otherDepartmentContentListDetail.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		contentArea.add(otherDepartmentContentListDetail);
		
		// -- 12 : 계절 컨텐츠 설정 패널
		SeasonContentDetailPanel contentDetailInformationPanel = new SeasonContentDetailPanel(getMaterialExtentsWindow(), this, OTD_ID);
		contentDetailInformationPanel.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		contentArea.add(contentDetailInformationPanel);
		
		// -- 13 : 월별 컨텐츠 설정 패널
		MonthlyContentDetailPanel monthlyContentDetailPanel = new MonthlyContentDetailPanel(getMaterialExtentsWindow(), this);
		monthlyContentDetailPanel.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		contentArea.add(monthlyContentDetailPanel);
		
		// -- 14 : 월별 컨텐츠 설정 패널
		
		Console.log("MAN_ID ::" + MAN_ID);
		MainImageBannerPanel = new MainImageBanner(getMaterialExtentsWindow(), this,MAN_ID);
		MainImageBannerPanel.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		contentArea.add(MainImageBannerPanel);
	}
	
	public MaterialWidget go(int position) {
		int newPanelIndex = position-1;
		this.nowPosition = -883 * newPanelIndex;
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(50);
		contentArea.setTransition(cfg);
		contentArea.setTransform("translate("+nowPosition+"px,0);");
		contentArea.setLeft(this.nowPosition);
		
		// 메인 지방 자치 단체 로드 시 초기 데이터 설정
		if (position==6)
			localGovDetail.initSelectedData();
		
		return (MaterialWidget) contentArea.getChildrenList().get(newPanelIndex);
	}
	
	public void onoff(boolean onoff) {
		contentArea.setVisible(onoff);
	}
	
	public String getOtdId() {
		return this.OTD_ID;
	}
}
