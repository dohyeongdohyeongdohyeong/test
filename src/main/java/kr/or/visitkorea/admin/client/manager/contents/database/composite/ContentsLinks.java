package kr.or.visitkorea.admin.client.manager.contents.database.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.html.Option;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.database.widget.LinkDetail;
import kr.or.visitkorea.admin.client.manager.contents.database.widget.LinkDetailList;
import kr.or.visitkorea.admin.client.manager.contents.database.widget.LinkDetailPanel;
import kr.or.visitkorea.admin.client.manager.contents.database.widget.LinkDetailType;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanelWithNoImage;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentsLinks extends AbtractContents {

	private MaterialColumn commandColumn;
	private MaterialColumn displayColumn;
	private MaterialColumn detailColumn;
	private LinkDetailList linkDetailList;
	private LinkDetailList beforeDetailList;
	private LinkDetailPanel detailPanel;
	private MaterialTextBox textBox;
	private MaterialListBox listBox;
	private MaterialTextBox backgroundColorBox;
	private MaterialTextBox titleTextColorBox;
	private MaterialTextBox urlBox;
	private MaterialListBox presetBox;
	private MaterialInput startDateBox;
	private MaterialInput endDateBox;
	private MaterialIcon saveIcon;
	private MaterialIcon editIcon;
	private UploadPanelWithNoImage uploadPanel1;
	private UploadPanelWithNoImage uploadPanel2;
	private MaterialLink defaultAppendAllCommand;
	private MaterialLink appendCommand;
	private MaterialLink removeCommand;
	private MaterialLink IndexUpCommand;
	private MaterialLink IndexDownCommand;
	private List<String> DeleteLinks;
	
	public ContentsLinks(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		super.init();
		this.setLayoutPosition(Position.RELATIVE);
		this.setTitle("링크(연결) 정보");
		buildLayout();
		buildLayoutItemDetail();
		appendCommand();
		SettingBaseIcon();
	}

	public void loadData() {
		DeleteLinks = new ArrayList<String>();
		clearDetailPanel();
		SettingVisible(false);
		beforeDetailList = new LinkDetailList();
		linkDetailList.clear();
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cotId", new JSONString(this.getCotId()));

		Func3<Object, String, Object> callBackFunction = new Func3<Object, String, Object>(){

			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {

					JSONObject bodyObj =  resultObj.get("body").isObject();
					JSONArray returnResultObj = bodyObj.get("result").isArray();

					//@TODO setup all kinds of link information
					
					int objSize = returnResultObj.size();
					for(int i=0; i<objSize; i++) {
						
						JSONObject member = returnResultObj.get(i).isObject();
						
						String adiId = "";
						String linkType = "0";
						String title = "";
						String backgroundColor = "";
						String textColor = "";
						String linkUrl = "";
						String imgId = "";
						String startDate = "";
						String endDate = "";
	
						if (member.get("ADI_ID") != null) adiId = member.get("ADI_ID").isString().stringValue();
						if (member.get("LINK_TYPE") != null) linkType = member.get("LINK_TYPE").isString().stringValue();
						if ( member.get("DISPLAY_TITLE") != null) title = member.get("DISPLAY_TITLE").isString().stringValue();
						if (member.get("BACKGROUND_COLOR") != null) backgroundColor = member.get("BACKGROUND_COLOR").isString().stringValue();
						if (member.get("TEXT_COLOR") != null) textColor = member.get("TEXT_COLOR").isString().stringValue();
						if (member.get("LINK_URL") != null) linkUrl = member.get("LINK_URL").isString().stringValue();
						if (member.get("IMG_ID") != null) imgId = member.get("IMG_ID").isString().stringValue();
						
						LinkDetail linkDetail = null;
						LinkDetail beforelinkDetail = null;
						
						if (linkType.equals("0")) {
							linkDetail = new LinkDetail(LinkDetailType.NORMAL, detailPanel, adiId);
							beforelinkDetail = new LinkDetail(LinkDetailType.NORMAL, detailPanel, adiId);
						}else if (linkType.equals("1")) {
							linkDetail = new LinkDetail(LinkDetailType.FULL_IMAGE, detailPanel, adiId);
							beforelinkDetail = new LinkDetail(LinkDetailType.FULL_IMAGE, detailPanel, adiId);
						}
						
						appendDisplayItem(beforelinkDetail, title, backgroundColor, textColor, linkUrl, imgId, startDate, endDate);
						appendDisplayItem(linkDetail, title, backgroundColor, textColor, linkUrl, imgId, startDate, endDate);
						beforeDetailList.add(beforelinkDetail);
						linkDetailList.add(linkDetail);
					}
				}				
			}
			
		};
		
		invokeQuery("GET_ALL_LINK_INFORMATION", parameterJSON, callBackFunction);				
		
		
	}

	private void buildLayoutItemDetail() {

		listBox = new MaterialListBox();
		listBox.getElement().getStyle().setMarginTop(0, Unit.PX);
		listBox.add(new Option("기본형"));
		listBox.setWidth("100%");
		listBox.add(new Option("전체 이미지형"));
		listBox.addValueChangeHandler(event->{
			
			LinkDetail linkDetail = linkDetailList.getSelectedItem();
			
			if (linkDetail != null){
				
				if (listBox.getIndex(event.getValue()) == 0) {
					linkDetail.setDefaultType(LinkDetailType.NORMAL);
				}else if (listBox.getIndex(event.getValue()) == 1) {
					linkDetail.setDefaultType(LinkDetailType.FULL_IMAGE);
				}
				
				
			}
		});
		addRows(detailPanel, listBox);
		
		textBox = new MaterialTextBox();
		textBox.setWidth("100%");
		textBox.getElement().getStyle().setMarginTop(0, Unit.PX);
		textBox.getElement().getFirstChildElement().getStyle().setMarginBottom(0, Unit.PX);
		textBox.setPlaceholder("연결정보의 타이틀을 입력해 주세요.");
		textBox.addKeyUpHandler(event->{
			LinkDetail linkDetail = linkDetailList.getSelectedItem();
			linkDetail.setTitle(textBox.getText());
		});
		addRows(detailPanel, textBox);
		
		backgroundColorBox = new MaterialTextBox();
		backgroundColorBox.getElement().getFirstChildElement().getStyle().setMarginBottom(0, Unit.PX);
		backgroundColorBox.getElement().getStyle().setMarginTop(0, Unit.PX);
		backgroundColorBox.setPlaceholder("바탕색 값을 입력해 주세요.");
		backgroundColorBox.addKeyUpHandler(event->{
			LinkDetail linkDetail = linkDetailList.getSelectedItem();
			linkDetail.setBGColor(backgroundColorBox.getText());
			linkDetail.reload();
		});
		
		titleTextColorBox = new MaterialTextBox();
		titleTextColorBox.getElement().getFirstChildElement().getStyle().setMarginBottom(0, Unit.PX);
		titleTextColorBox.getElement().getStyle().setMarginTop(0, Unit.PX);
		titleTextColorBox.setPlaceholder("타이틀 문자열 색상 값을 입력해 주세요.");
		titleTextColorBox.addKeyUpHandler(event->{
			LinkDetail linkDetail = linkDetailList.getSelectedItem();
			linkDetail.setTitleTextColor(titleTextColorBox.getText());
			linkDetail.reload();
		});
		addRows(detailPanel, backgroundColorBox, titleTextColorBox);
		
		urlBox = new MaterialTextBox();
		urlBox.setWidth("100%");
		urlBox.getElement().getFirstChildElement().getStyle().setMarginBottom(0, Unit.PX);
		urlBox.getElement().getStyle().setMarginTop(0, Unit.PX);
		urlBox.setPlaceholder("링크 URL 을 입력해 주세요.");
		urlBox.addKeyUpHandler(event->{
			LinkDetail linkDetail = linkDetailList.getSelectedItem();
			linkDetail.setUrl(urlBox.getText());
			linkDetail.reload();
		});
		addRows(detailPanel, urlBox);
		
		startDateBox = new MaterialInput(InputType.DATE); 
//		startDateBox.getElement().getFirstChildElement().getStyle().setMarginBottom(0, Unit.PX);
//		startDateBox.getElement().getStyle().setMarginTop(0, Unit.PX);
		startDateBox.addBlurHandler(event->{
			LinkDetail linkDetail = linkDetailList.getSelectedItem();
			if (linkDetail != null) {
				linkDetail.setStartDate(startDateBox.getValue());
			}
		});
		
		endDateBox = new MaterialInput(InputType.DATE); 
		endDateBox.addValueChangeHandler(event->{
		});
		endDateBox.addBlurHandler(event->{
			LinkDetail linkDetail = linkDetailList.getSelectedItem();
			if (linkDetail != null) {
				linkDetail.setEndDate(endDateBox.getValue());
			}
		});
		addRows(detailPanel, startDateBox, endDateBox);
		
		String domain = (String) Registry.get("image.server");
		
		uploadPanel1 = new UploadPanelWithNoImage(150, 150, domain + "/img/call");
		uploadPanel1.setLayoutPosition(Position.ABSOLUTE);
		uploadPanel1.setLeft(15);
		uploadPanel1.setBottom(-150);
		uploadPanel1.getBtn().setTop(42);
		uploadPanel1.getBtn().setRight(33);
		uploadPanel1.setWidth("150px");
		uploadPanel1.setHeight("150px");
		uploadPanel1.getUploader().setAcceptedFiles("image/*");
		uploadPanel1.getUploader().addSuccessHandler(event->{

			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			String tempImageId = uploadValue.substring(0, uploadValue.lastIndexOf("."));
			
			String url = domain + "/img/call?cmd=TEMP_VIEW&name=" + uploadValue;
			
			String savePath = "";
			String[] imgMainSplitArr = tempImageId.split("-");
			for (String splitArrMember : imgMainSplitArr) {
				savePath += "/" + splitArrMember.substring(0, 2);
			}
			savePath += "/" +uploadValue;
			LinkDetail linkDetail = linkDetailList.getSelectedItem();
			if (linkDetail != null) {
				linkDetail.setImagepath(savePath);
				linkDetail.setImageSource(url);
				linkDetail.reload();
				linkDetail.setImgId(tempImageId);
				
				// 0 : 이미지 없음 , 1 : 이미지 있음, 2 : 신규 이미지 등록 , 3: 이미지 변경
				if(linkDetail.getImageCheck() == 0) 
					linkDetail.setImageCheck(2); //신규 이미지 등록
				else if (linkDetail.getImageCheck() == 1)
					linkDetail.setImageCheck(3); // 기존 이미지 변경
			}
			
		});
		addRows(detailPanel, uploadPanel1);

		MaterialLabel uploadPanel1Label = new MaterialLabel("[ 이미지 ]");
		uploadPanel1Label.setLayoutPosition(Position.ABSOLUTE);
		uploadPanel1Label.setLeft(60);
		uploadPanel1Label.setBottom(-175);
		addRows(detailPanel, uploadPanel1Label);
		
		uploadPanel2 = new UploadPanelWithNoImage(150, 150, (String) Registry.get("image.server") + "/img/call");
		uploadPanel2.setLayoutPosition(Position.ABSOLUTE);
		uploadPanel2.setLeft(185);
		uploadPanel2.setBottom(-150);
		uploadPanel2.getBtn().setTop(42);
		uploadPanel2.getBtn().setRight(33);
		uploadPanel2.setWidth("150px");
		uploadPanel2.setHeight("150px");
		uploadPanel2.getUploader().setAcceptedFiles("application/*");
		uploadPanel2.getUploader().addSuccessHandler(event->{
			
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			
			String url = (String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + uploadValue;
			LinkDetail linkDetail = linkDetailList.getSelectedItem();
			if (linkDetail != null) {
				urlBox.setText(url);
				linkDetail.setUrl(urlBox.getText());
				linkDetail.reload();
			}
		
		});
		addRows(detailPanel, uploadPanel2);

		MaterialLabel uploadPanel2Label = new MaterialLabel("[ 첨부 파일 ]");
		uploadPanel2Label.setLayoutPosition(Position.ABSOLUTE);
		uploadPanel2Label.setLeft(200);
		uploadPanel2Label.setBottom(-175);
		addRows(detailPanel, uploadPanel2Label);

	}


	private void addRows(LinkDetailPanel detailPanel, MaterialWidget... widgets) {
		
		MaterialRow row = new MaterialRow();
		row.setWidth("100%");
		row.setMarginBottom(0);
		
		int unitCount = 12 / widgets.length;
		
		for (MaterialWidget widget : widgets) {
			
			MaterialColumn col = new MaterialColumn();
			col.setPadding(0);
			col.setGrid("s"+unitCount);
			col.add(widget);
			row.add(col);
			
		}

		detailPanel.add(row);
		
	}

	private void appendCommand() {
		
		defaultAppendAllCommand = new MaterialLink();
		defaultAppendAllCommand.setTooltip("기본 세트 추가");
		defaultAppendAllCommand.setIconType(IconType.LIBRARY_ADD);
		defaultAppendAllCommand.setMarginTop(20);
		defaultAppendAllCommand.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		defaultAppendAllCommand.setDisplay(Display.INLINE_BLOCK);
		defaultAppendAllCommand.setLineHeight(66);
		defaultAppendAllCommand.addClickHandler(event->{
			
			addNewAllLinkDetail();
			
		});
		commandColumn.add(defaultAppendAllCommand);
		
		appendCommand = new MaterialLink();
		appendCommand.setIconType(IconType.ADD);
		appendCommand.setTooltip("추가");
		appendCommand.setMarginTop(20);
		appendCommand.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		appendCommand.setDisplay(Display.INLINE_BLOCK);
		appendCommand.setLineHeight(66);
		appendCommand.addClickHandler(event->{
			
			addNewLinkDetail(presetBox.getSelectedIndex());

		});
		commandColumn.add(appendCommand);
		
		presetBox = new MaterialListBox();
		presetBox.setWidth("250px");
		presetBox.setMarginRight(15);
		presetBox.getElement().getStyle().setMarginTop(0, Unit.PX);
		presetBox.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		presetBox.add(new Option("기본 스타일"));
		presetBox.add(new Option("PDF 아이콘 스타일"));
		presetBox.add(new Option("이벤트 아이콘 스타일"));
		presetBox.add(new Option("홈 아이콘 스타일"));
		presetBox.add(new Option("안내 아이콘 스타일"));
		presetBox.add(new Option("지도 아이콘 스타일"));
		
		presetBox.add(new Option("지도검색 아이콘 스타일"));
		presetBox.add(new Option("네비게이션 아이콘 스타일"));
		presetBox.add(new Option("전화걸기 아이콘 스타일"));
		presetBox.add(new Option("다음 블로그 아이콘 스타일"));
		presetBox.add(new Option("다음 카페 아이콘 스타일"));
		presetBox.add(new Option("페이스북 아이콘 스타일"));
		presetBox.add(new Option("인스타그램 아이콘 스타일"));
		
		presetBox.add(new Option("카카오스토리 아이콘 스타일"));
		presetBox.add(new Option("네이버 블로그 아이콘 스타일"));
		presetBox.add(new Option("네이버 카페 아이콘 스타일"));
		presetBox.add(new Option("네이버 포스트 아이콘 스타일"));
		
		presetBox.add(new Option("카카오 플러스 프랜즈 아이콘 스타일"));
		presetBox.add(new Option("티스토리 아이콘 스타일"));
		presetBox.add(new Option("트위터 아이콘 스타일"));
		presetBox.add(new Option("유튜브 아이콘 스타일"));
		presetBox.addValueChangeHandler(event->{
		});
		commandColumn.add(presetBox);
	
	}

	private void addNewAllLinkDetail() {
		
		for (int i=0; i<21; i++) {
			addNewLinkDetail(i);
		}
	}

	private void addNewLinkDetail(int styleIndex) {
		
		LinkDetail linkDetail = new LinkDetail(LinkDetailType.NORMAL, detailPanel, null);

		switch(styleIndex) {
		case 0  : setupLinkDetail(linkDetail, "기본형 스타일", "#ffffff", "#111111", "http://www.google.co.kr", "basic", "", "",IDUtil.uuid()); break;
		case 1  : setupLinkDetail(linkDetail, "PDF 아이콘 스타일", "#ff6af5", "#ffffff", "http://www.google.co.kr", "pdf", "", "", IDUtil.uuid()); break;
		case 2  : setupLinkDetail(linkDetail, "이벤트 아이콘 스타일", "#6bf2ff", "#111111", "http://www.google.co.kr", "event", "", "", IDUtil.uuid()); break;
		case 3  : setupLinkDetail(linkDetail, "홈 아이콘 스타일", "#fc9637", "#ffffff", "http://www.google.co.kr", "home", "", "", IDUtil.uuid()); break;
		case 4  : setupLinkDetail(linkDetail, "안내 아이콘 스타일", "#f71188", "#ffffff", "http://www.google.co.kr", "information", "", "", IDUtil.uuid()); break;
		case 5  : setupLinkDetail(linkDetail, "지도 아이콘 스타일", "#9966ff", "#ffffff", "http://www.google.co.kr", "map", "", "", IDUtil.uuid()); break;
		case 6  : setupLinkDetail(linkDetail, "지도검색 아이콘 스타일", "#49c9a3", "#ffffff", "http://www.google.co.kr", "searchMap", "", "", IDUtil.uuid()); break;
		case 7  : setupLinkDetail(linkDetail, "네비게이션 아이콘 스타일", "#1b9efb", "#ffffff", "http://www.google.co.kr", "navigation", "", "", IDUtil.uuid()); break;
		case 8  : setupLinkDetail(linkDetail, "전화걸기 아이콘 스타일", "#e84c57", "#ffffff", "http://www.google.co.kr", "call", "", "", IDUtil.uuid()); break;
		case 9  : setupLinkDetail(linkDetail, "다음 블로그 아이콘 스타일", "#ffffff", "#111111", "http://www.google.co.kr", "daumBlog", "", "", IDUtil.uuid()); break;
		case 10 : setupLinkDetail(linkDetail, "다음 카페 아이콘 스타일", "#ffffff", "#111111", "http://www.google.co.kr", "daumCafe", "", "", IDUtil.uuid()); break;
		case 11 : setupLinkDetail(linkDetail, "페이스북 아이콘 스타일", "#ffffff", "#111111", "http://www.google.co.kr", "facebook", "", "", IDUtil.uuid()); break;
		case 12 : setupLinkDetail(linkDetail, "인스타그램 아이콘 스타일", "#ffffff", "#111111", "http://www.google.co.kr", "instagram", "", "", IDUtil.uuid()); break;
		case 13 : setupLinkDetail(linkDetail, "카카오 스토리 아이콘 스타일", "#ffffff", "#111111", "http://www.google.co.kr", "kakaoStory", "", "", IDUtil.uuid()); break;
		case 14 : setupLinkDetail(linkDetail, "네이버 블로그 아이콘 스타일", "#ffffff", "#111111", "http://www.google.co.kr", "naverBlog", "", "", IDUtil.uuid()); break;
		case 15 : setupLinkDetail(linkDetail, "네이버 카페 아이콘 스타일", "#ffffff", "#111111", "http://www.google.co.kr", "naverCafe", "", "", IDUtil.uuid()); break;
		case 16 : setupLinkDetail(linkDetail, "네이버 포스트 아이콘 스타일", "#ffffff", "#111111", "http://www.google.co.kr", "naverPost", "", "", IDUtil.uuid()); break;
		case 17 : setupLinkDetail(linkDetail, "카카오 플러스 아이콘 스타일", "#ffffff", "#111111", "http://www.google.co.kr", "kakaoPlus", "", "", IDUtil.uuid()); break;
		case 18 : setupLinkDetail(linkDetail, "티스토리 아이콘 스타일", "#ffffff", "#111111", "http://www.google.co.kr", "tStory", "", "", IDUtil.uuid()); break;
		case 19 : setupLinkDetail(linkDetail, "트위터 아이콘 스타일", "#ffffff", "#111111", "http://www.google.co.kr", "twitter", "", "", IDUtil.uuid()); break;
		case 20 : setupLinkDetail(linkDetail, "유튜브 아이콘 스타일", "#ffffff", "#111111", "http://www.google.co.kr", "youtube", "", "", IDUtil.uuid()); break;
		}

		linkDetailList.add(linkDetail);

	}

	private void setupLinkDetail(LinkDetail linkDetail, String title, String backgroundColor, String textColor, String linkUrl,
			String imgId, String startDate , String endDate,String ADI_ID) {

		linkDetail.setAdiId(ADI_ID);
		appendDisplayItem(linkDetail, title, backgroundColor, textColor, linkUrl, imgId, startDate, endDate);
		
	}

	private void appendDisplayItem(LinkDetail linkDetail, String title, String backgroundColor, String textColor,
			String linkUrl, String imgId, String startDate, String endDate) {

		//이미지 ID가 있을경우
		if(imgId.length() == 36) 
			linkDetail.setImageCheck(1);
		else 
			linkDetail.setImageCheck(0);
		
		linkDetail.setTitle(title);
		linkDetail.setBGColor(backgroundColor);
		linkDetail.setTitleTextColor(textColor);
		linkDetail.setUrl(linkUrl);
		linkDetail.setImageSource((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+imgId);
		linkDetail.setImgId(imgId);
		linkDetail.setStartDate(startDate);
		linkDetail.setEndDate(endDate);
		linkDetail.reload();
		linkDetail.addClickHandler(cevent->{
			
			LinkDetail tgrLinkDetail = linkDetailList.getSelectedItem();
			if (tgrLinkDetail != null) {
   				tgrLinkDetail.setBorder("1px solid #e6e6e6");
			}
			
			linkDetailList.setSelectedItem(linkDetail);
			linkDetail.setBorder("1px solid #ff0066");
			textBox.setText(linkDetail.getTitle()); 
			
			backgroundColorBox.setText(linkDetail.getBGColor()); 
			
			titleTextColorBox.setText(linkDetail.getTitleTextColor()); 
			
			urlBox.setText(linkDetail.getUrl()); 
			
			startDateBox.setValue(linkDetail.getStartDate());
			
			endDateBox.setValue(linkDetail.getEndDate());
			
			if (linkDetail.getLinkDetailType().equals(LinkDetailType.NORMAL)) {
				listBox.setSelectedIndex(0);
			}else if (linkDetail.getLinkDetailType().equals(LinkDetailType.FULL_IMAGE)) {
				listBox.setSelectedIndex(1);
			}
			
		});
		
		LinkDetail tgrLinkDetail = linkDetailList.getSelectedItem();
		if (tgrLinkDetail != null) {
				tgrLinkDetail.setBorder("1px solid #e6e6e6");
		}
		linkDetailList.setSelectedItem(linkDetail);
		linkDetail.setBorder("1px solid #ff0066");

	}

	private void buildLayout() {
		
		MaterialRow row = new MaterialRow();
		add(row);
		
		commandColumn = new MaterialColumn();
		commandColumn.setGrid("s12");
		commandColumn.setPadding(10);
		row.add(commandColumn);
		
		displayColumn = new MaterialColumn();
		displayColumn.setOverflow(Overflow.AUTO);
		displayColumn.setGrid("s7");
		displayColumn.setPadding(10);
		displayColumn.setBackgroundColor(Color.GREY_LIGHTEN_3);
		row.add(displayColumn);
		
		detailColumn = new MaterialColumn();
		detailColumn.setGrid("s5");
		detailColumn.setPadding(10);
		detailColumn.setBackgroundColor(Color.GREY_LIGHTEN_3);
		row.add(detailColumn);

		linkDetailList = new LinkDetailList();
		
		displayColumn.add(linkDetailList);
		displayColumn.setHeight("430px");
		
		detailPanel =  new LinkDetailPanel();
		detailPanel.setLayoutPosition(Position.RELATIVE);
		detailPanel.setPaddingLeft(15);
		detailPanel.setPaddingRight(15);
		detailColumn.add(detailPanel);
		detailColumn.setHeight("430px");
		
		MaterialColumn bottomColumn = new MaterialColumn();
		bottomColumn.setPaddingLeft(-10);
		bottomColumn.setGrid("s12");
		row.add(bottomColumn);
		
		MaterialPanel bottomCommandPanel =new MaterialPanel();
		bottomCommandPanel.setLayoutPosition(Position.RELATIVE);
		bottomColumn.add(bottomCommandPanel);
		
		removeCommand = new MaterialLink();
		removeCommand.setLayoutPosition(Position.ABSOLUTE);
		removeCommand.setTop(0);
		removeCommand.setLeft(-10);
		removeCommand.setWidth("24px");
		removeCommand.setBorderRight("1px solid #efefef");
		removeCommand.setTooltip("삭제");
		removeCommand.setIconType(IconType.REMOVE);
		removeCommand.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		removeCommand.setDisplay(Display.INLINE_BLOCK);
		removeCommand.addClickHandler(event->{
			
			LinkDetail tgrLinkDetail = linkDetailList.getSelectedItem();
			if (tgrLinkDetail != null) {
				
				int maxIndex = linkDetailList.getWidgetCount()-1;
				int widgetIndex = linkDetailList.getWidgetIndex(tgrLinkDetail);
				if (widgetIndex == maxIndex) {
					widgetIndex--;
				}
				DeleteLinks.add(tgrLinkDetail.getAdiId());
				removeLink(tgrLinkDetail, widgetIndex);
			}

		});
		
		bottomCommandPanel.add(removeCommand);

		IndexUpCommand = new MaterialLink();
		IndexUpCommand.setLayoutPosition(Position.ABSOLUTE);
		IndexUpCommand.setTop(0);
		IndexUpCommand.setLeft(15);
		IndexUpCommand.setBorderRight("1px solid #efefef");
		IndexUpCommand.setTooltip("위로");
		IndexUpCommand.setIconType(IconType.ARROW_UPWARD);
		IndexUpCommand.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		IndexUpCommand.setDisplay(Display.INLINE_BLOCK);
		IndexUpCommand.setWidth("24px");
		
		IndexUpCommand.addClickHandler(event->{
			
			
			
			LinkDetail tgrLinkDetail = linkDetailList.getSelectedItem();
			if (tgrLinkDetail != null) {
				
				int widgetIndex = linkDetailList.getWidgetIndex(tgrLinkDetail);
				
				if (widgetIndex == 0) {
					MaterialToast.fireToast("더이상 올릴 수 없습니다."); 
				} else {
				IndexMoveLink(tgrLinkDetail,widgetIndex,1);
				}
			}
			
		});
		
		bottomCommandPanel.add(IndexUpCommand);
		
		IndexDownCommand = new MaterialLink();
		IndexDownCommand.setLayoutPosition(Position.ABSOLUTE);
		IndexDownCommand.setTop(0);
		IndexDownCommand.setLeft(40);
		IndexDownCommand.setBorderRight("1px solid #efefef");
		IndexDownCommand.setTooltip("아래로");
		IndexDownCommand.setIconType(IconType.ARROW_DOWNWARD);
		IndexDownCommand.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		IndexDownCommand.setDisplay(Display.INLINE_BLOCK);
		IndexDownCommand.setWidth("24px");
		IndexDownCommand.addClickHandler(event->{
			
			LinkDetail tgrLinkDetail = linkDetailList.getSelectedItem();
			if (tgrLinkDetail != null) {
				
				int widgetIndex = linkDetailList.getWidgetIndex(tgrLinkDetail);
				
				int maxIndex = linkDetailList.getWidgetCount()-1;
				if (widgetIndex == maxIndex) {
					MaterialToast.fireToast("더이상 내릴 수 없습니다."); 
				} else {
				IndexMoveLink(tgrLinkDetail,widgetIndex,0);
				}
			}
		});
		bottomCommandPanel.add(IndexDownCommand);
		
	}
	

	private void removeLink(LinkDetail tgrLinkDetail, int widgetIndex) {

		if (tgrLinkDetail.getAdiId() == null) {
			
			linkDetailList.remove(tgrLinkDetail);
			if (linkDetailList.getChildrenList().size() > 0) {
				linkDetailList.getItemList().remove(widgetIndex);
				
				linkDetailList.setSelectedItem((LinkDetail)linkDetailList.getChildrenList().get(widgetIndex));
				LinkDetail firstDetail = linkDetailList.getSelectedItem();
				firstDetail.setBorder("1px solid #ff0066");
				
    			textBox.setText(firstDetail.getTitle()); 
    			
    			backgroundColorBox.setText(firstDetail.getBGColor()); 
    			
    			titleTextColorBox.setText(firstDetail.getTitleTextColor()); 
    			
    			urlBox.setText(firstDetail.getUrl()); 
    			
    			if (firstDetail.getLinkDetailType().equals(LinkDetailType.NORMAL)) {
    				listBox.setSelectedIndex(0);
    			}else if (firstDetail.getLinkDetailType().equals(LinkDetailType.FULL_IMAGE)) {
    				listBox.setSelectedIndex(1);
    			}
			}
			
		}else {
			linkDetailList.remove(tgrLinkDetail);
				if (linkDetailList.getChildrenList().size() > 0) {
					linkDetailList.getItemList().remove(widgetIndex);
					linkDetailList.setSelectedItem((LinkDetail)linkDetailList.getChildrenList().get(widgetIndex));
					LinkDetail firstDetail = linkDetailList.getSelectedItem();
					firstDetail.setBorder("1px solid #ff0066");
	    			textBox.setText(firstDetail.getTitle()); 
	    			backgroundColorBox.setText(firstDetail.getBGColor()); 
	    			titleTextColorBox.setText(firstDetail.getTitleTextColor()); 
	    			urlBox.setText(firstDetail.getUrl()); 
	    			if (firstDetail.getLinkDetailType().equals(LinkDetailType.NORMAL)) {
	    				listBox.setSelectedIndex(0);
	    			}else if (firstDetail.getLinkDetailType().equals(LinkDetailType.FULL_IMAGE)) {
	    				listBox.setSelectedIndex(1);
	    			}
				}
			}
		}
		

	private void IndexMoveLink(LinkDetail tgrLinkDetail, int widgetIndex,int kind) {
		
		if(tgrLinkDetail.getAdiId() == null) {
				linkDetailList.remove(tgrLinkDetail);
			
			if (linkDetailList.getChildrenList().size() > 0) {
				
				linkDetailList.setSelectedItem((LinkDetail)linkDetailList.getChildrenList().get(widgetIndex));
				LinkDetail firstDetail = linkDetailList.getSelectedItem();
				firstDetail.setBorder("1px solid #ff0066");
				
    			textBox.setText(firstDetail.getTitle()); 
    			
    			backgroundColorBox.setText(firstDetail.getBGColor()); 
    			
    			titleTextColorBox.setText(firstDetail.getTitleTextColor()); 
    			
    			urlBox.setText(firstDetail.getUrl()); 
    			
    			if (firstDetail.getLinkDetailType().equals(LinkDetailType.NORMAL)) {
    				listBox.setSelectedIndex(0);
    			}else if (firstDetail.getLinkDetailType().equals(LinkDetailType.FULL_IMAGE)) {
    				listBox.setSelectedIndex(1);
    			}
			}
		} else {
			
			
			JSONObject parameterJSON = new JSONObject();
			if(kind == 1) {
				
				LinkDetail afterDetail = (LinkDetail) linkDetailList.getWidget(widgetIndex-1);
				
				linkDetailList.clear();
				List<Widget> detailList = linkDetailList.getItemList();
				Collections.swap(detailList, widgetIndex, widgetIndex-1);

				LinkDetailList LinkDetailList2 = new LinkDetailList();
				
				detailList.forEach(item -> {
					LinkDetailList2.add(item);
				});
				
				linkDetailList = LinkDetailList2;
				displayColumn.add(linkDetailList);
				linkDetailList.setSelectedItem((LinkDetail)linkDetailList.getChildrenList().get(widgetIndex-1));
			} else {

				LinkDetail afterDetail = (LinkDetail) linkDetailList.getWidget(widgetIndex+1);
				
				linkDetailList.clear();
				List<Widget> detailList = linkDetailList.getItemList();
				Collections.swap(detailList, widgetIndex, widgetIndex+1);

				LinkDetailList LinkDetailList2 = new LinkDetailList();
				
				detailList.forEach(item -> {
					LinkDetailList2.add(item);
				});
				
				linkDetailList = LinkDetailList2;
				displayColumn.add(linkDetailList);
				linkDetailList.setSelectedItem((LinkDetail)linkDetailList.getChildrenList().get(widgetIndex+1));
			}
		}
		
	}
	
	@Override
	public void setReadOnly(boolean readFlag) {
		
	}

	public void clearDetailPanel() {
		this.linkDetailList.clear();
		this.linkDetailList.getItemList().clear();
	}
	
	public void SettingBaseIcon() {
		saveIcon = this.showSaveIconAndGetIcon();
		editIcon = this.showEditIconAndGetIcon();
		saveIcon.setTooltip("컨텐츠 수정 해제");
		
		SettingVisible(false);
		
		saveIcon.addClickHandler(event->{
			UpdateCheck();
		});
		
		editIcon.addClickHandler(event->{
			SettingVisible(true);
		});
	}
	
	public void SettingVisible(Boolean Visible) {
		
		editIcon.setVisible(!Visible);
		saveIcon.setVisible(Visible);
		backgroundColorBox.setEnabled(Visible);
		startDateBox.setEnabled(Visible);
		endDateBox.setEnabled(Visible);
		titleTextColorBox.setEnabled(Visible);
		textBox.setEnabled(Visible);
		urlBox.setEnabled(Visible);
		listBox.setEnabled(Visible);
		uploadPanel1.getBtn().setEnabled(Visible);
		uploadPanel2.getBtn().setEnabled(Visible);
		defaultAppendAllCommand.setEnabled(Visible);
		appendCommand.setEnabled(Visible);
		removeCommand.setEnabled(Visible);
		IndexUpCommand.setEnabled(Visible);
		IndexDownCommand.setEnabled(Visible);
		presetBox.setEnabled(Visible);
	}
	
	public void SaveBase() {
		
		JSONArray links = new JSONArray();
		for (int i = 0; i < linkDetailList.getItemList().size(); i++) {
			LinkDetail linkdetail = (LinkDetail) linkDetailList.getItemList().get(i);
			JSONObject LinkObj = new JSONObject();
			LinkObj.put("adiId", new JSONString(linkdetail.getAdiId()));
			LinkObj.put("linkType", new JSONString("0"));
			LinkObj.put("contentOrder", new JSONNumber(i));
			LinkObj.put("chkUse", new JSONNumber(1));
			LinkObj.put("displayTitle", new JSONString(linkdetail.getTitle()));
			LinkObj.put("link", new JSONString(linkdetail.getUrl()));
			LinkObj.put("imgId", new JSONString(linkdetail.getImgId()));
			LinkObj.put("backgroundColor", new JSONString(linkdetail.getBGColor()));
			LinkObj.put("textColor", new JSONString(linkdetail.getTitleTextColor()));
			LinkObj.put("ImageCheck", new JSONNumber(linkdetail.getImageCheck()));
			if(linkdetail.getImagepath() != null)
				LinkObj.put("ImagePath",  new JSONString(linkdetail.getImagepath()));
			if(linkdetail.getLinkDetailType() == LinkDetailType.NORMAL)
				LinkObj.put("LinkDetailType", new JSONString("0"));
			else
				LinkObj.put("LinkDetailType", new JSONString("1"));
			links.set(i, LinkObj);
		}
		
		JSONArray Deletelinks = new JSONArray();
		for (int i = 0; i < DeleteLinks.size(); i++) {
			Deletelinks.set(i, new JSONString(DeleteLinks.get(i)));
		}
		
		JSONObject paramObj = new JSONObject();
		paramObj.put("cmd", new JSONString("UPDATE_DATABASE_MASTER"));
		paramObj.put("mode", new JSONString("Link"));
		paramObj.put("cotId", new JSONString(getCotId()));
		if(links.size() > 0)
			paramObj.put("Links", links);
		if(Deletelinks.size() > 0)
			paramObj.put("DeleteLinks", Deletelinks);
		
		VisitKoreaBusiness.post("call", paramObj.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if(processResult.equals("success")){
					MaterialToast.fireToast("내용 변경에 성공하였습니다.");
					SettingVisible(false);
					loadData();
				}
			}
		});
		
	}
	
	public void UpdateCheck() {
		
		if(beforeDetailList.getItemList().size() != linkDetailList.getItemList().size()) {
			getWindow().confirm("내용이 변경되었습니다. 실제 데이터에 반영하시겠습니까?", event->{
				if (event.getSource().toString().contains("yes")) {
					SaveBase();
				} 
			});
		} else {
			
			if(beforeDetailList.getItemList().size() < 1) {
				SettingVisible(false);
				return;
			}
			int check = 0;
			for (int i = 0; i < beforeDetailList.getItemList().size(); i++) {
			 LinkDetail beforelinkdetail = (LinkDetail) beforeDetailList.getItemList().get(i);
			 LinkDetail afterlinkdetail = (LinkDetail) linkDetailList.getItemList().get(i);

			 
			 if(beforelinkdetail.getLinkDetailType() != afterlinkdetail.getLinkDetailType()
				 || beforelinkdetail.getBGColor() != afterlinkdetail.getBGColor()
				 || beforelinkdetail.getAdiId() != afterlinkdetail.getAdiId()
				 || beforelinkdetail.getTitle() != afterlinkdetail.getTitle()
				 || beforelinkdetail.getTitleTextColor() != afterlinkdetail.getTitleTextColor()
				 || beforelinkdetail.getUrl() != afterlinkdetail.getUrl()
				 || beforelinkdetail.getLinkDetailType() != afterlinkdetail.getLinkDetailType()
				 || beforelinkdetail.getImgId() != afterlinkdetail.getImgId()
				 ) {
				 
				 getWindow().confirm("내용이 변경되었습니다. 실제 데이터에 반영하시겠습니까?", event->{
						if (event.getSource().toString().contains("yes")) {
							SaveBase();
							return;
						} else {
							return;
						}
					});
					
				 
			 }else {
				 if(i == beforeDetailList.getItemList().size()-1 && check == beforeDetailList.getItemList().size()-1) {
					SettingVisible(false);
					return;
				 }
				 check++;
			 }
			 
			}
		}
		
	}
}
