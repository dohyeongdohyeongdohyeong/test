package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.localGov;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.fileuploader.base.UploadFile;
import gwt.material.design.addins.client.fileuploader.events.SuccessEvent;
import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextArea.ResizeRule;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.AbtractOtherDepartmentMainContents;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.AreaLink;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ImageNoButton;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ImagewithUploadPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.OtherDepartmentMainEditor;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.UploadSuccessEventFunc;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class LocalGovernmentContentDetail extends AbtractOtherDepartmentMainContents {

	private OtherDepartmentMainEditor otherDepartmentMainEditor;
	private MaterialPanel topCenterPanel;
	private int topCenterPanelLeftInt;
	private int bottomCenterPanelLeftInt;
	private MaterialPanel bottomCenterPanel;
	private AreaLink topSelectedLink;
	private AreaLink bottomSelectedLink;
	private MaterialPanel metroUIPanel;
	private MaterialPanel metroContentPanel;
	private JSONObject infoObject;
	private MaterialLabel areaName;
	private Map<String, AreaLink> areaLinks;
	private ImageNoButton uploadPanel;
	private MaterialInput information;
	private MaterialTextArea titleArea;
	private MaterialInput subTitleArea;
	private MaterialLink detailInfoLink;
	private ImagewithUploadPanel image;
	private MaterialInput imageLinkUrl;
	private JSONArray linkObjList;
	private MaterialPanel sigunguUrlPanel;
	private MaterialTextBox urlInput;
	private int urlPanelInitPositon = -100;
	private String areaKey;
	private String otdId;
	private MaterialLink headLink;

	public LocalGovernmentContentDetail(MaterialExtentsWindow materialExtentsWindow, OtherDepartmentMainEditor otherDepartmentMainEditor, String otdId) {
		super(materialExtentsWindow);
		this.otherDepartmentMainEditor = otherDepartmentMainEditor;
		this.otdId = otdId;
		Registry.put("ShowcaseContentDetail", this);
	}
	
	
	public void initLayout() {
		super.init();
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("100%");
		this.setPadding(20);
		this.setTitle("메인 지방 자치 단체");
		this.areaLinks = new HashMap<String, AreaLink>();		
		
		buildContent();
	}

	public LocalGovernmentContentDetail getPanel(){
		return this;
	}
	
	private void buildContent() {
		
		headLink = this.addLink(new MaterialLink());
		headLink.setLayoutPosition(Position.ABSOLUTE);
		headLink.setTooltip("서버 반영");
		headLink.setTop(25);
		headLink.setRight(10);
		headLink.setIconType(IconType.SAVE);
		headLink.setIconColor(Color.WHITE);
		headLink.addClickHandler(event->{
			saveContent(event);
		});
		
		String borderDefine = "1px solid #aaaaaa";
		String mainBox = "2px solid #aaaaaa";

		MaterialPanel borderPanel = new MaterialPanel();
		borderPanel.setLayoutPosition(Position.RELATIVE);
		borderPanel.setWidth("100%");
		borderPanel.setHeight("490px");
		borderPanel.setBorder(borderDefine);
		borderPanel.setPadding(20);
		this.add(borderPanel);
		
		MaterialPanel masterPanel = new MaterialPanel();
		masterPanel.setLayoutPosition(Position.RELATIVE);
		masterPanel.setWidth("100%");
		masterPanel.setHeight("450px");
		masterPanel.setBorder(mainBox);
		borderPanel.add(masterPanel);
		

		MaterialPanel headerPanel = new MaterialPanel();
		headerPanel.setLayoutPosition(Position.ABSOLUTE);
		headerPanel.setTop(0);
		headerPanel.setLeft(0);
		headerPanel.setWidth("100%");
		headerPanel.setHeight("40px");
		headerPanel.setBorderBottom(mainBox);
		masterPanel.add(headerPanel);
		buildAreaCode(headerPanel);
		
		MaterialPanel contentPanel = new MaterialPanel();
		contentPanel.setLayoutPosition(Position.ABSOLUTE);
		contentPanel.setTop(41);
		contentPanel.setLeft(0);
		contentPanel.setWidth("100%");
		contentPanel.setHeight("368px");
		contentPanel.setPadding(20);
		contentPanel.setOverflow(Overflow.HIDDEN);
		masterPanel.add(contentPanel);
		buildContentArea(contentPanel);
		
		MaterialPanel bottomPanel = new MaterialPanel();
		bottomPanel.setLayoutPosition(Position.ABSOLUTE);
		bottomPanel.setBottom(0);
		bottomPanel.setLeft(0);
		bottomPanel.setWidth("100%");
		bottomPanel.setHeight("40px");
		bottomPanel.setBorderTop(mainBox);
		masterPanel.add(bottomPanel);
		buildSigunguCode(bottomPanel);
	}

	private void saveContent(ClickEvent event) {
		// save content
		
		JSONArray masterInfoArray = new JSONArray();
		
		List<String> keyList = new ArrayList<String>(this.areaLinks.keySet());

		int setIdx = 0;
		for (String key : keyList) {
			
			AreaLink aLink = areaLinks.get(key);
			masterInfoArray.set(setIdx, aLink.getInfoObject());
			setIdx++;
		}
		
		JSONObject parameterJSON = new JSONObject();
		
		parameterJSON.put("cmd", new JSONString("INSERT_SM_LOCAL_GOV"));
		parameterJSON.put("INFO_OBJECT", masterInfoArray);
		
		Console.log("OTD_ID :: " + otdId);
		parameterJSON.put("OTD_ID", new JSONString(otdId));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {}
		});
	}

	private void buildAreaCode(MaterialPanel tgrPanel) {
		
		MaterialLink leftLink = new MaterialLink();
		leftLink.setWidth("24px");
		leftLink.setHeight("40px");
		leftLink.setLineHeight(40);
		leftLink.setTextAlign(TextAlign.CENTER);
		leftLink.setVerticalAlign(VerticalAlign.MIDDLE);
		leftLink.setBorderRight("1px solid #aaaaaa");
		leftLink.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		leftLink.setIconType(IconType.ARROW_BACK);
		leftLink.getElement().getFirstChildElement().getStyle().setLineHeight(40, Unit.PX);
		leftLink.addClickHandler(event->{
			areaLeftLinkClick();
		});
		tgrPanel.add(leftLink);
		
		MaterialPanel topCenterViewPanel = new MaterialPanel();
		topCenterViewPanel.setLayoutPosition(Position.ABSOLUTE);
		topCenterViewPanel.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		topCenterViewPanel.setTop(0);
		topCenterViewPanel.setLeft(24);
		topCenterViewPanel.setWidth("749px");
		topCenterViewPanel.setHeight("100%");
		topCenterViewPanel.setOverflow(Overflow.HIDDEN);
		tgrPanel.add(topCenterViewPanel);
		
		topCenterPanel = new MaterialPanel();
		topCenterPanel.setLayoutPosition(Position.ABSOLUTE);
		topCenterPanel.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		topCenterPanel.setTop(0);
		topCenterPanel.setLeft(0);
		topCenterPanel.setWidth("1700px");
		topCenterPanel.setHeight("100%");
		topCenterPanel.setBackgroundColor(Color.GREY_LIGHTEN_2);
		topCenterViewPanel.add(topCenterPanel);
		buildTopCenterContent(topCenterPanel);
		
		MaterialLink rightLink = new MaterialLink();
		rightLink.setWidth("24px");
		rightLink.setHeight("40px");
		rightLink.setLineHeight(40);
		rightLink.setTextAlign(TextAlign.CENTER);
		rightLink.setVerticalAlign(VerticalAlign.MIDDLE);
		rightLink.setBorderLeft("1px solid #aaaaaa");
		rightLink.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		rightLink.setIconType(IconType.ARROW_FORWARD);
		rightLink.getElement().getFirstChildElement().getStyle().setLineHeight(40, Unit.PX);
		rightLink.addClickHandler(event->{
			areaRightLinkClick();
		});
		tgrPanel.add(rightLink);
	}
	
	//메인 좌측
	private void buildTopCenterContent(MaterialPanel topCenterViewPanel) {
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) Registry.get("ADDRESS_BIG_CODE");
		
		List<String> keyList = new ArrayList<String>(map.keySet());
		keyList.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				
				int i1 = Integer.parseInt(o1);
				int i2 = Integer.parseInt(o2);
				
				if (i1 < i2) return 0;
				else if (i1 > i2) return 1;
				else return 0;
				
			}
			
		});
		
		
		String borderStr = "1px solid #aaaaaa";
		for (String key : keyList) {
			
			if (key.equals("0"))
				continue;
			
			String areaStringName = map.get(key);
			
			AreaLink areaLink = new AreaLink();
			areaLink.setKey(key);
			areaLink.setMarginLeft(5);
			areaLink.setMarginRight(5);
			areaLink.setMarginTop(5);
			areaLink.setPadding(5);
			areaLink.setBorderRadius("10px 10px 0px 0px");
			areaLink.setText(areaStringName);
			areaLink.setFontWeight(FontWeight.BOLD);
			areaLink.setBackgroundColor(Color.WHITE);
			areaLink.setBorderTop(borderStr);
			areaLink.setBorderLeft(borderStr);
			areaLink.setBorderRight(borderStr);
			areaLink.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
			areaLink.addMouseOutHandler(event->{
				if (topSelectedLink != areaLink) {
					areaLink.setBackgroundColor(Color.WHITE);
					areaLink.setTextColor(Color.BLUE);
				}
			});
			areaLink.addMouseOverHandler(event->{
				if (topSelectedLink != areaLink) {
					areaLink.setBackgroundColor(Color.BLUE);
					areaLink.setTextColor(Color.WHITE);
				}
			});
			areaLink.addClickHandler(event->{
				selectAreaItem(key);
			});
			
			areaLink.getInformation(otdId);
			areaLinks.put(key, areaLink);
			topCenterViewPanel.add(areaLink);
		}
	}

	private void selectAreaItem(String key) {
		
		this.areaKey = key;
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) Registry.get("ADDRESS_BIG_CODE");
		String areaStringName = map.get(key);
		AreaLink areaLink = areaLinks.get(key);
		if (topSelectedLink != null && topSelectedLink != areaLink) {
			topSelectedLink.setBackgroundColor(Color.WHITE);
			topSelectedLink.setTextColor(Color.BLUE);
		}
		
		areaName.setText(areaStringName);
		topSelectedLink = areaLink;
		areaLink.setBackgroundColor(Color.RED);
		areaLink.setTextColor(Color.WHITE);
		
		setupMainContent(areaLink);
		buildBottomCenterContent(key);
		
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(1000);
		bottomCenterPanel.setTransition(cfg);
		bottomCenterPanel.setTransform("translate("+ ( 0 )+"px,0);");
		bottomCenterPanel.setLeft(0);
		bottomCenterPanelLeftInt = 0;

	}

	private void setupMainContent(AreaLink areaLink) {
		infoObject = areaLink.getInfoObject();
		if (infoObject != null) {
			setupContents();
		}
	}

	private void setupContents() {
		
		if (infoObject != null) {
			
			// fesitival image
			if (this.infoObject.get("LOG_IMG_ID") == null) {
				this.uploadPanel.setImageId("a99c43ca-0cb8-482d-82e6-9454f983bb69");
			}else {
				this.uploadPanel.setImageId(this.infoObject.get("LOG_IMG_ID").isString().stringValue());
			}
		
			// main information			
			if (this.infoObject.get("INFORMATION") != null) this.information.setValue(this.infoObject.get("INFORMATION").isString().stringValue());
			
			// title
			if (this.infoObject.get("TITLE") != null) this.titleArea.setValue(this.infoObject.get("TITLE").isString().stringValue());
			
			// subtitle
			if (this.infoObject.get("SUB_TITLE") != null) this.subTitleArea.setValue(this.infoObject.get("SUB_TITLE").isString().stringValue());
			
			// fesitival image
			if (this.infoObject.get("IMG_ID") == null) {
				this.image.setImageId("a99c43ca-0cb8-482d-82e6-9454f983bb69");
			}else {
				this.image.setImageId(this.infoObject.get("IMG_ID").isString().stringValue());
			}
			
			// fesitival image link
			if (this.infoObject.get("LINK_URL") != null) this.imageLinkUrl.setText(this.infoObject.get("LINK_URL").isString().stringValue());

			// setp bannerobject array
			if (this.infoObject.get("banner") == null) {
				linkObjList = new JSONArray();
			}else {
				linkObjList = this.infoObject.get("banner").isArray();
			}
			
			// setup banner
			if (this.infoObject.get("BANNER_STYLE") == null) {
				this.infoObject.put("BANNER_STYLE", new JSONNumber(3));
				buildMetroUIContentTemplate((int) this.infoObject.get("BANNER_STYLE").isNumber().doubleValue());
			}else {
				buildMetroUIContentTemplate((int) this.infoObject.get("BANNER_STYLE").isNumber().doubleValue());
			}
			
		}
	}

	private void areaLeftLinkClick() {
		
		int newPosition = 0;
		
		if (topCenterPanelLeftInt < 0) {
			newPosition = topCenterPanelLeftInt + 400;
		}else {
			newPosition = 0;
		}
		
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(1000);
		topCenterPanel.setTransition(cfg);
		topCenterPanel.setTransform("translate("+ ( newPosition )+"px,0);");
		topCenterPanel.setLeft(newPosition);
		topCenterPanelLeftInt = newPosition;
	
	}

	private void areaRightLinkClick() {
		
		
		int newPosition = 0;
		
		if (topCenterPanelLeftInt >= -201) {
			newPosition = topCenterPanelLeftInt - 400;
		}else {
			newPosition = topCenterPanelLeftInt;
		}
		
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(1000);
		topCenterPanel.setTransition(cfg);
		topCenterPanel.setTransform("translate("+ ( newPosition )+"px,0);");
		topCenterPanel.setLeft(newPosition);
		topCenterPanelLeftInt = newPosition;
	
	}

	/**
	 * 메인 컨텐츠
	 * @param contentPanel
	 */
	private void buildContentArea(MaterialPanel contentPanel) {
		
		MaterialPanel leftPanel = new MaterialPanel();
		leftPanel.setLayoutPosition(Position.RELATIVE);
		leftPanel.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		leftPanel.setTop(0);
		leftPanel.setMarginRight(20);
		leftPanel.setWidth("31.5%");
		leftPanel.setHeight("100%");
		//leftPanel.setBorder(borderDefine);
		contentPanel.add(leftPanel);
		buildLeftContentPanel(leftPanel);

		MaterialPanel centerPanel = new MaterialPanel();
		centerPanel.setLayoutPosition(Position.RELATIVE);
		centerPanel.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		centerPanel.setTop(0);
		centerPanel.setWidth("31.5%");
		centerPanel.setHeight("100%");
		centerPanel.getElement().getStyle().setCursor(Cursor.POINTER);
		contentPanel.add(centerPanel);
		buildCenterContentPanel(centerPanel);

		metroUIPanel = new MaterialPanel();
		metroUIPanel.setLayoutPosition(Position.RELATIVE);
		metroUIPanel.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		metroUIPanel.setTop(0);
		metroUIPanel.setMarginLeft(20);
		metroUIPanel.setWidth("31.55%");
		metroUIPanel.setHeight("100%");
//		metroUIPanel.setBorder("1px dashed #cccccc");
		contentPanel.add(metroUIPanel);

		metroContentPanel = new MaterialPanel();
		metroContentPanel.setLayoutPosition(Position.ABSOLUTE);
		metroContentPanel.setTop(0);
		metroContentPanel.setLeft(2);
		metroContentPanel.setWidth("232px");
		metroContentPanel.setHeight("100px");
		metroUIPanel.add(metroContentPanel);
		buildRightContentPanel(metroUIPanel);
		
		sigunguUrlPanel = new MaterialPanel();
		sigunguUrlPanel.setLayoutPosition(Position.ABSOLUTE);
		sigunguUrlPanel.setBackgroundColor(Color.GREY_LIGHTEN_3);
		sigunguUrlPanel.setBottom(-100);
		sigunguUrlPanel.setLeft(0);
		sigunguUrlPanel.setWidth("100%");
		sigunguUrlPanel.setHeight("90px");
		contentPanel.add(sigunguUrlPanel);
		
		urlInput = new MaterialTextBox();
		urlInput.setLabel("홈페이지 입력");
		urlInput.setLayoutPosition(Position.ABSOLUTE);
		urlInput.setWidth("740px");
		urlInput.setTop(10);
		urlInput.setLeft(10);
		urlInput.addValueChangeHandler(event->{
			if (bottomSelectedLink != null) {
				bottomSelectedLink.setLinkUrl(urlInput.getValue());
				bottomSelectedLink.save(areaKey, bottomSelectedLink.getKey(), urlInput.getValue(), otdId);
			}
		});
		sigunguUrlPanel.add(urlInput);
		
		MaterialLink closeLink = new MaterialLink();
		closeLink.setIconType(IconType.ARROW_DOWNWARD);
		closeLink.setLayoutPosition(Position.ABSOLUTE);
		closeLink.setTop(10);
		closeLink.setRight(0);
		closeLink.addClickHandler(event->{
			upDown(-100);
			urlPanelInitPositon = -100;
		});
		sigunguUrlPanel.add(closeLink);
		
		MaterialLink removeTextLink = new MaterialLink();
		removeTextLink.setIconType(IconType.CLOSE);
		removeTextLink.setLayoutPosition(Position.ABSOLUTE);
		removeTextLink.setTop(45);
		removeTextLink.setRight(0);
		removeTextLink.addClickHandler(event->{
			urlInput.setText(null);
			urlInput.setFocus(false);
		});
		sigunguUrlPanel.add(removeTextLink);
		
	}

	public void upDown(int position) {
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("bottom");
		cfg.setDuration(100);
		sigunguUrlPanel.setTransition(cfg);
		sigunguUrlPanel.setTransform("translate("+position+"px,0);");
		sigunguUrlPanel.setBottom(position);
	}

	//main right area
	private void buildRightContentPanel(MaterialPanel rightPanel) {

		// add button
		MaterialLink li_01 = new MaterialLink();
		li_01.setIconType(IconType.LAYERS);
		li_01.setLayoutPosition(Position.ABSOLUTE);
		li_01.setTop(5);
		li_01.setRight(5);
		li_01.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);
		li_01.setBorder("1px solid #bbbbbb");
		li_01.setBackgroundColor(Color.WHITE);
		li_01.addClickHandler(event->{
			
			Map<String, Object> paramMap = new HashMap<String, Object> ();
			paramMap.put("TARGET", getPanel());
			paramMap.put("WINDOW", getWindow());
			this.getWindow().openDialog(OtherDepartmentMainApplication.LOCAL_GOV_METRO_UI, paramMap, 600);
			
		});
		rightPanel.add(li_01);

	}

	//main center area
	private void buildCenterContentPanel(MaterialPanel centerPanel) {
	
		// base image
		image = new ImagewithUploadPanel(238, 300, (String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=e74fa99a-7959-42da-938d-a8013824a6d5.png");
		image.setVisibleAddButton(false);
		image.setVisibleRemoveButton(false);
		image.setLayoutPosition(Position.ABSOLUTE);
		image.setLineHeight(317);
		image.updateImageInformation(true);
		image.setUploadSuccessEvent(new UploadSuccessEventFunc() {

			@Override
			public void invoke(SuccessEvent<UploadFile> event) {
				infoObject.put("IMG_ID", new JSONString(image.getImageId()));
			}
			
		});
		centerPanel.add(image);		
		 
		imageLinkUrl = new MaterialInput(InputType.TEXT);
		imageLinkUrl.setLayoutPosition(Position.ABSOLUTE);
		imageLinkUrl.setHeight("20px");
		imageLinkUrl.setWidth("220px");
		imageLinkUrl.setFontSize("10px");
		imageLinkUrl.setBottom(-10);
		imageLinkUrl.setLeft(5);
		imageLinkUrl.setRight(15);
		imageLinkUrl.addKeyUpHandler(event->{
			this.infoObject.put("LINK_URL", new JSONString(imageLinkUrl.getValue()));
		});
		centerPanel.add(imageLinkUrl);

	}

	//main intro area
	private void buildLeftContentPanel(MaterialPanel leftPanel) {
		
		uploadPanel = new ImageNoButton(30, 30, (String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=e74fa99a-7959-42da-938d-a8013824a6d5.png");
		uploadPanel.setLayoutPosition(Position.ABSOLUTE);
		uploadPanel.setTop(5);
		uploadPanel.setLeft(5);
		uploadPanel.updateImageInformation(true);
		uploadPanel.setUploadSuccessEventFunc(new UploadSuccessEventFunc() {

			@Override
			public void invoke(SuccessEvent<UploadFile> event) {
				infoObject.put("LOG_IMG_ID", new JSONString(uploadPanel.getImageId()));
			}
			
		});
		leftPanel.add(uploadPanel);
		
		areaName = new MaterialLabel();
		areaName.setTextAlign(TextAlign.LEFT);
		areaName.setLayoutPosition(Position.ABSOLUTE);
		areaName.setWidth("170px");
		areaName.setHeight("30px");
		areaName.setFontSize("28px");
		areaName.setFontWeight(FontWeight.BOLDER);
		areaName.setTop(0);
		areaName.setLeft(40);
		leftPanel.add(areaName);
		 
		information = new MaterialInput(InputType.TEXT);
		information.setLayoutPosition(Position.ABSOLUTE);
		information.setWidth("205px");
		information.setHeight("20px");
		information.setFontSize("10px");
		information.setFontWeight(FontWeight.BOLDER);
		information.setTop(40);
		information.setLeft(5);
		information.addKeyUpHandler(event->{
			infoObject.put("INFORMATION", new JSONString(information.getValue()));
			
		});
		leftPanel.add(information);
		
		titleArea = new MaterialTextArea();
		titleArea.setResizeRule(ResizeRule.NONE);
		titleArea.setLayoutPosition(Position.ABSOLUTE);
		titleArea.setWidth("205px");
		titleArea.setHeight("65px");
		titleArea.setTop(90);
		titleArea.setLeft(5);
		titleArea.getElement().getFirstChildElement().getStyle().setFontSize(26,Unit.PX);
		titleArea.getElement().getFirstChildElement().getStyle().setFontWeight(FontWeight.BOLDER);
		titleArea.getElement().getFirstChildElement().getStyle().setPadding(0, Unit.PX);
		titleArea.addValueChangeHandler(event->{
			infoObject.put("TITLE", new JSONString(event.getValue()));
		});
		leftPanel.add(titleArea);
		
		subTitleArea = new MaterialInput(InputType.TEXT);
		subTitleArea.setLayoutPosition(Position.ABSOLUTE);
		subTitleArea.setWidth("205px");
		subTitleArea.setHeight("20px");
		subTitleArea.setFontSize("9px");
		subTitleArea.setFontWeight(FontWeight.BOLDER);
		subTitleArea.setTop(200);
		subTitleArea.setLeft(5);
		subTitleArea.addKeyUpHandler(event->{
			infoObject.put("SUB_TITLE", new JSONString(subTitleArea.getValue()));
		});
		leftPanel.add(subTitleArea);
		
		detailInfoLink = new MaterialLink();
		detailInfoLink.setPadding(5);
		detailInfoLink.setBorder("1px solid #aaaaaa");
		detailInfoLink.setLayoutPosition(Position.ABSOLUTE);
		detailInfoLink.setTop(270);
		detailInfoLink.setLeft(5);
		detailInfoLink.setText("자세히 보기");
		detailInfoLink.setIconType(IconType.KEYBOARD_ARROW_RIGHT);
		detailInfoLink.setIconPosition(IconPosition.RIGHT);
		detailInfoLink.addClickHandler(event->{
			
			Map<String, Object> paramMap = new HashMap<String, Object> ();
			paramMap.put("INFO_OBJ", infoObject);
			paramMap.put("LINK_VALUE", infoObject.get("CONNECT_URL").isString().stringValue());
			this.getWindow().openDialog(OtherDepartmentMainApplication.ONLY_URL, paramMap, 800);
			
		});
		leftPanel.add(detailInfoLink);
		
	}

	private void buildSigunguCode(MaterialPanel tgrPanel) {
		
		MaterialLink leftLink = new MaterialLink();
		leftLink.setWidth("24px");		
		leftLink.setHeight("40px");
		leftLink.setLineHeight(40);
		leftLink.setTextAlign(TextAlign.CENTER);
		leftLink.setVerticalAlign(VerticalAlign.MIDDLE);
		leftLink.setBorderRight("1px solid #aaaaaa");
		leftLink.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		leftLink.setIconType(IconType.ARROW_BACK);
		leftLink.getElement().getFirstChildElement().getStyle().setLineHeight(40, Unit.PX);
		leftLink.addClickHandler(event->{
			sigunguLeftLinkClick();
		});
		tgrPanel.add(leftLink);
		
		MaterialPanel bottomCenterViewPanel = new MaterialPanel();
		bottomCenterViewPanel.setLayoutPosition(Position.ABSOLUTE);
		bottomCenterViewPanel.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		bottomCenterViewPanel.setTop(0);
		bottomCenterViewPanel.setLeft(24);
		bottomCenterViewPanel.setWidth("749px");
		bottomCenterViewPanel.setHeight("100%");
		bottomCenterViewPanel.setOverflow(Overflow.HIDDEN);
		tgrPanel.add(bottomCenterViewPanel);
		
		bottomCenterPanel = new MaterialPanel();
		bottomCenterPanel.setLayoutPosition(Position.ABSOLUTE);
		bottomCenterPanel.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		bottomCenterPanel.setTop(0);
		bottomCenterPanel.setLeft(0);
//		bottomCenterPanel.setWidth("1700px");
		bottomCenterPanel.setHeight("100%");
		bottomCenterPanel.setBackgroundColor(Color.GREY_LIGHTEN_2);
		bottomCenterViewPanel.add(bottomCenterPanel);
		buildBottomCenterContent("1");
		
		MaterialLink rightLink = new MaterialLink();
		rightLink.setWidth("24px");
		rightLink.setHeight("40px");
		rightLink.setLineHeight(40);
		rightLink.setTextAlign(TextAlign.CENTER);
		rightLink.setVerticalAlign(VerticalAlign.MIDDLE);
		rightLink.setBorderLeft("1px solid #aaaaaa");
		rightLink.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		rightLink.setIconType(IconType.ARROW_FORWARD);
		rightLink.getElement().getFirstChildElement().getStyle().setLineHeight(40, Unit.PX);
		rightLink.addClickHandler(event->{
			sigunguRightLinkClick();
		});
		tgrPanel.add(rightLink);
		
	}

	private void buildBottomCenterContent(String index) {
		
		bottomCenterPanel.clear();
		
		int contentLength = 0;
		
		Map<String, Map<String, String>> masterMap = (Map<String, Map<String, String>>) Registry.get("ADDRESS_BIG_WITH_MID_CODE");
		Map<String, String> map = masterMap.get(index);
		
		List<String> keyList = new ArrayList<String>(map.keySet());
		keyList.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				
				int i1 = Integer.parseInt(o1);
				int i2 = Integer.parseInt(o2);
				
				if (i1 < i2) return 0;
				else if (i1 > i2) return 1;
				else return 0;
				
			}
			
		});
		
		String borderStr = "1px solid #aaaaaa";
		for (String key : keyList) {
		
			String areaName = map.get(key);
			
			Console.log("areaName :: " + areaName);
			
			if (areaName.trim().length() > 0) {
				
				AreaLink areaLink = new AreaLink();
				areaLink.setKey(key);
				areaLink.setMarginLeft(5);
				areaLink.setMarginRight(5);
				areaLink.setMarginBottom(5);
				areaLink.setPadding(5);
				areaLink.setBorderRadius("0px 0px 10px 10px");
				areaLink.setText(areaName);
				areaLink.setFontWeight(FontWeight.BOLD);
				areaLink.setBackgroundColor(Color.WHITE);
				areaLink.setBorderBottom(borderStr);
				areaLink.setBorderLeft(borderStr);
				areaLink.setBorderRight(borderStr);
				areaLink.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
				bottomCenterPanel.add(areaLink);
				
				contentLength += areaLink.getOffsetWidth() + 10;
				
				bottomCenterPanel.setWidth(contentLength + "px");
				areaLink.addMouseOutHandler(event->{
					if (bottomSelectedLink != areaLink) {
						areaLink.setBackgroundColor(Color.WHITE);
						areaLink.setTextColor(Color.BLUE);
					}
				});
				areaLink.addMouseOverHandler(event->{
					if (bottomSelectedLink != areaLink) {
						areaLink.setBackgroundColor(Color.BLUE);
						areaLink.setTextColor(Color.WHITE);
					}
				});
				areaLink.addClickHandler(event->{
	
					if (bottomSelectedLink != null && bottomSelectedLink != areaLink) {
						bottomSelectedLink.setBackgroundColor(Color.WHITE);
						bottomSelectedLink.setTextColor(Color.BLUE);
					}
					
					bottomSelectedLink = areaLink;
					areaLink.setBackgroundColor(Color.RED);
					areaLink.setTextColor(Color.WHITE);
					
					if (urlPanelInitPositon < 0) {
						upDown(0);
						urlPanelInitPositon = 0;
					}
					
					urlInput.setLabel(areaName + " 홈페이지 입력");
					urlInput.setEnabled(false);
					
					JSONObject parameterJSON = new JSONObject();
					parameterJSON.put("cmd", new JSONString("GET_CITY_CAPITAL"));
					parameterJSON.put("AREA_CODE", new JSONString(this.areaKey));
					parameterJSON.put("SIGUNGU_CODE", new JSONString(key));
					parameterJSON.put("OTD_ID", new JSONString(otdId));
					
					VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

						@Override
						public void call(Object param1, String param2, Object param3) {

							JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
							JSONObject headerObj = (JSONObject) resultObj.get("header");
							String processResult = headerObj.get("process").isString().toString();
							processResult = processResult.replaceAll("\"", "");

							if (processResult.equals("success")) {

								JSONObject resultObject = resultObj.get("body").isObject().get("result").isObject();
								if (resultObject.get("HOMEPAGE") != null) {
									bottomSelectedLink.setLinkUrl(resultObject.get("HOMEPAGE").isString().stringValue());
									urlInput.setValue(bottomSelectedLink.getLinkUrl());
									urlInput.setEnabled(true);
								}
								
							}
						}
					});
					
					
				});
			
			}
			
		}		
	}

	private void sigunguLeftLinkClick() {
		
		int newPosition = 0;
		
		if (bottomCenterPanelLeftInt < 0) {
			newPosition = bottomCenterPanelLeftInt + 400;
		}else {
			newPosition = 0;
		}
		
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(1000);
		bottomCenterPanel.setTransition(cfg);
		bottomCenterPanel.setTransform("translate("+ ( newPosition )+"px,0);");
		bottomCenterPanel.setLeft(newPosition);
		bottomCenterPanelLeftInt = newPosition;
		
	}

	private void sigunguRightLinkClick() {
		
		
		int newPosition = 0;
		
		newPosition = bottomCenterPanelLeftInt - 400;
/*
		if (bottomCenterPanelLeftInt >= -201) {
		}else {
			newPosition = bottomCenterPanelLeftInt;
		}
*/		
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(1000);
		bottomCenterPanel.setTransition(cfg);
		bottomCenterPanel.setTransform("translate("+ ( newPosition )+"px,0);");
		bottomCenterPanel.setLeft(newPosition);
		bottomCenterPanelLeftInt = newPosition;
	}

	@Override
	public void setReadOnly(boolean readFlag) {
	}

	public void buildMetroUIContentTemplate(int contentCnt) {
		
		this.infoObject.put("BANNER_STYLE", new JSONNumber(contentCnt));
		
		metroContentPanel.clear();
		switch(contentCnt){
		case 3 : buildUI3(); break;
		case 4 : buildUI4(); break;
		case 5 : buildUI5(); break;
		}
		
	}

	private void buildUI3() {
		//metroUIPanel
		MetroUIContentPanel panel1 = new MetroUIContentPanel(MetroUI.FULL);
		panel1.setLayoutPosition(Position.ABSOLUTE);
		panel1.setDiaplayIndex(0);
		panel1.setWindow(getWindow());
		panel1.setTop(35);
		panel1.setWidth("100%");
		panel1.setHeight("90px");
		panel1.setList(linkObjList);
		metroContentPanel.add(panel1);
		
		MetroUIContentPanel panel2 = new MetroUIContentPanel(MetroUI.FULL);
		panel2.setLayoutPosition(Position.ABSOLUTE);
		panel2.setDiaplayIndex(1);
		panel2.setWindow(getWindow());
		panel2.setTop(135);
		panel2.setWidth("100%");
		panel2.setHeight("90px");
		panel2.setList(linkObjList);
		metroContentPanel.add(panel2);
		
		MetroUIContentPanel panel3 = new MetroUIContentPanel(MetroUI.FULL);
		panel3.setLayoutPosition(Position.ABSOLUTE);
		panel3.setDiaplayIndex(2);
		panel3.setWindow(getWindow());
		panel3.setTop(235);
		panel3.setWidth("100%");
		panel3.setHeight("90px");
		panel3.setList(linkObjList);
		metroContentPanel.add(panel3);
	}

	private void buildUI4() {
		//metroUIPanel
		MetroUIContentPanel panel1 = new MetroUIContentPanel(MetroUI.FULL);
		panel1.setWindow(getWindow());
		panel1.setDiaplayIndex(0);
		panel1.setLayoutPosition(Position.ABSOLUTE);
		panel1.setTop(35);
		panel1.setWidth("100%");
		panel1.setHeight("90px");
		panel1.setList(linkObjList);
		metroContentPanel.add(panel1);
		
		MetroUIContentPanel panel2 = new MetroUIContentPanel(MetroUI.FULL);
		panel2.setLayoutPosition(Position.ABSOLUTE);
		panel2.setDiaplayIndex(1);
		panel2.setWindow(getWindow());
		panel2.setTop(135);
		panel2.setWidth("100%");
		panel2.setHeight("90px");
		panel2.setList(linkObjList);
		metroContentPanel.add(panel2);
		
		MetroUIContentPanel panel3 = new MetroUIContentPanel(MetroUI.HALF);
		panel3.setLayoutPosition(Position.ABSOLUTE);
		panel3.setDiaplayIndex(2);
		panel3.setWindow(getWindow());
		panel3.setTop(235);
		panel3.setWidth("110px");
		panel3.setHeight("90px");
		panel3.setList(linkObjList);
		metroContentPanel.add(panel3);
		
		MetroUIContentPanel panel4 = new MetroUIContentPanel(MetroUI.HALF);
		panel4.setLayoutPosition(Position.ABSOLUTE);
		panel4.setDiaplayIndex(3);
		panel4.setWindow(getWindow());
		panel4.setTop(235);
		panel4.setLeft(122);
		panel4.setWidth("110px");
		panel4.setHeight("90px");
		panel4.setList(linkObjList);
		metroContentPanel.add(panel4);
		
	}

	private void buildUI5() {
		//metroUIPanel
		MetroUIContentPanel panel1 = new MetroUIContentPanel(MetroUI.FULL);
		panel1.setLayoutPosition(Position.ABSOLUTE);
		panel1.setDiaplayIndex(0);
		panel1.setWindow(getWindow());
		panel1.setTop(35);
		panel1.setWidth("100%");
		panel1.setHeight("90px");
		panel1.setList(linkObjList);
		metroContentPanel.add(panel1);
		
		MetroUIContentPanel panel2 = new MetroUIContentPanel(MetroUI.HALF);
		panel2.setLayoutPosition(Position.ABSOLUTE);
		panel2.setDiaplayIndex(1);
		panel2.setWindow(getWindow());
		panel2.setTop(135);
		panel2.setWidth("110px");
		panel2.setHeight("90px");
		panel2.setList(linkObjList);
		metroContentPanel.add(panel2);
		
		MetroUIContentPanel panel3 = new MetroUIContentPanel(MetroUI.HALF);
		panel3.setLayoutPosition(Position.ABSOLUTE);
		panel3.setDiaplayIndex(2);
		panel3.setWindow(getWindow());
		panel3.setTop(135);
		panel3.setLeft(122);
		panel3.setWidth("110px");
		panel3.setHeight("90px");
		panel3.setList(linkObjList);
		metroContentPanel.add(panel3);
		
		MetroUIContentPanel panel4 = new MetroUIContentPanel(MetroUI.HALF);
		panel4.setLayoutPosition(Position.ABSOLUTE);
		panel4.setDiaplayIndex(3);
		panel4.setWindow(getWindow());
		panel4.setTop(235);
		panel4.setWidth("110px");
		panel4.setHeight("90px");
		panel4.setList(linkObjList);
		metroContentPanel.add(panel4);
		
		MetroUIContentPanel panel5 = new MetroUIContentPanel(MetroUI.HALF);
		panel5.setLayoutPosition(Position.ABSOLUTE);
		panel5.setDiaplayIndex(4);
		panel5.setWindow(getWindow());
		panel5.setTop(235);
		panel5.setLeft(122);
		panel5.setWidth("110px");
		panel5.setHeight("90px");
		panel5.setList(linkObjList);
		metroContentPanel.add(panel5);
		
	}
	
	private void setPermissionRole() {
		
		headLink.setEnabled(true);
		if (otdId.equals("0a01eb7b-96de-11e8-8165-020027310001")) { // PC 버전 기준
			headLink.setEnabled(Registry.getPermission("3d115f15-31e7-4396-b9d2-08b30bec5488"));
		} else if (otdId.equals("ab097fc9-daa6-423d-8fcb-50aec7852e21")) { // 모바일 버전 기준
			headLink.setEnabled(Registry.getPermission("826d8615-7759-4514-8851-88e56afa30ba"));
		}
	}
	
	public void setOtdId(String OTDID) {
		otdId = OTDID;
		this.initLayout();
		this.setPermissionRole();
	}

	@Override
	public void loadData() {
	}
	
	public void initSelectedData() {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				selectAreaItem("1");
			}
		});
	}
}
