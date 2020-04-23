package kr.or.visitkorea.admin.client.manager.news.composite;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;

import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jquery.client.api.Functions.EventFunc1;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.editor.EditorBase;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.FileUploadSimplePanel;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MainContentPanel extends AbstractContentPanel {

	private MaterialPanel infoPanel;
	private MaterialPanel mainContent;
	private MaterialPanel addLinkPanel;
	private MaterialPanel attFilePanel;
//	private boolean editable;
	private MaterialLabel cotIdLabel;
	private MaterialLabel readCountLabel;
	private MaterialLabel createDateLabel;
	private MaterialLabel editDateLabel;
	private SelectionPanel statusSelectionPane;
	private SelectionPanel displayOutSelectionPanel;
	private SelectionPanel typeSelectionPanel;
	private MaterialInput titleContentInput;
	private MaterialInput buttonNameContentInput;
	private MaterialInput linkUrlContentInput;
	private MaterialInput fileDescriptionContentInput;
	private MaterialInput fileUploadUploadInstance;
//	private String newsId;
	private MaterialLabel owner;
	public MainContentPanel(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		buildBasicInformation();
		buildMainContent();
		buildAttachedFiles();
	}

	public void clearDetail() {
		clear();
		cotIdLabel.setText("");
		readCountLabel.setText("");
		titleContentInput.setValue("");
		createDateLabel.setText("");
		editDateLabel.setText("");
		statusSelectionPane.setSelectionOnSingleMode("작업 전");
		owner.setText(Registry.getStfId());
		displayOutSelectionPanel.setSelectionOnSingleMode("미노출");
		typeSelectionPanel.setSelectionOnSingleMode("뉴스");
		
		crbody.setHTML("");
		buttonNameContentInput.setValue("");
		linkUrlContentInput.setValue("");
		
		fileUploadUploadInstance.setValue("");
		fileDescriptionContentInput.setValue("");
	}
	public void setNwsid() {
		clearDetail();
		cotIdLabel.setText(IDUtil.uuid());
		crbody.SetNwsId(cotIdLabel.getText());
		titleContentInput.setFocus(true);
	}
	public void setData(String nwsid) {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_NEWS"));
		parameterJSON.put("nwsid", new JSONString(nwsid));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					JSONObject bodyObj =  resultObj.get("body").isObject();
					JSONArray resultArray = bodyObj.get("result").isArray();
					int usrCnt = resultArray.size();
					if (usrCnt == 0) {
						getMaterialExtentsWindow().alert("결과값이 없습니다.", 500);
					}
					JSONObject obj = resultArray.get(0).isObject();
					cotIdLabel.setText(obj.get("NWS_ID").isString().stringValue());
					crbody.SetNwsId(cotIdLabel.getText());
					readCountLabel.setText((obj.get("READ_COUNT")!=null?obj.get("READ_COUNT").isNumber().doubleValue():0)+ " 건");
					titleContentInput.setValue(obj.get("TITLE")!=null?obj.get("TITLE").isString().stringValue():"");
					createDateLabel.setText(obj.get("CREATE_DATE")!=null?obj.get("CREATE_DATE").isString().stringValue():"");
					editDateLabel.setText(obj.get("MODIFIED_DATE")!=null?obj.get("MODIFIED_DATE").isString().stringValue():"");
					owner.setText(obj.get("STF_ID")!=null?obj.get("STF_ID").isString().stringValue():"");
					int isact = 0;
					if(obj.get("IS_ACTIVATION") != null) 
						isact = (int)obj.get("IS_ACTIVATION").isNumber().doubleValue();
					if(isact == 0) {
						statusSelectionPane.setSelectionOnSingleMode("작업 전");
					} else if(isact == 1) {
						statusSelectionPane.setSelectionOnSingleMode("작업 완료");
					} else {
						statusSelectionPane.setSelectionOnSingleMode("작업 보류");
					}
					int isview = 0;
					if(obj.get("IS_VIEW") != null) 
						isview = (int)obj.get("IS_VIEW").isNumber().doubleValue();
					if(isview == 1) {
						displayOutSelectionPanel.setSelectionOnSingleMode("노출");
					} else {
						displayOutSelectionPanel.setSelectionOnSingleMode("미노출");
					}
					String nwskind = "0";
					if(obj.get("NWS_KIND") != null) 
						nwskind = obj.get("NWS_KIND").isString().stringValue();
					if(nwskind.equals("0")) {
						typeSelectionPanel.setSelectionOnSingleMode("뉴스");
					} else if(nwskind.equals("1")) {
						typeSelectionPanel.setSelectionOnSingleMode("공지사항");
					} else {
						typeSelectionPanel.setSelectionOnSingleMode("이벤트");
					}
					
					crbody.setHTML(obj.get("BODY")!=null?obj.get("BODY").isString().stringValue():"");
					buttonNameContentInput.setValue(obj.get("BUTTON_NAME")!=null?obj.get("BUTTON_NAME").isString().stringValue():"");
					linkUrlContentInput.setValue(obj.get("BUTTON_LINK_URL")!=null?obj.get("BUTTON_LINK_URL").isString().stringValue():"");
					
					fileUploadUploadInstance.setValue(obj.get("LINK_FILE_URL")!=null?obj.get("LINK_FILE_URL").isString().stringValue():"");
					fileDescriptionContentInput.setValue(obj.get("FILE_DESCRIPTION")!=null?obj.get("FILE_DESCRIPTION").isString().stringValue():"");
				} else {
					Console.log("에러인가?");
				}
			}
		});
	}
	public void insertDetail() {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("INSERT_NEWS"));
		parameterJSON.put("nwsid", new JSONString(cotIdLabel.getText()));
		parameterJSON.put("usrid", new JSONString(Registry.getUserId()));
		parameterJSON.put("title", new JSONString(titleContentInput.getValue()));
		parameterJSON.put("body", new JSONString(crbody.getHTML()));
		parameterJSON.put("nwskind", new JSONNumber(((int)typeSelectionPanel.getSelectedValue())));
		parameterJSON.put("isview", new JSONNumber(Math.abs(((int)displayOutSelectionPanel.getSelectedValue())-1)));
		parameterJSON.put("isactivation", new JSONNumber(((int)statusSelectionPane.getSelectedValue())));
		parameterJSON.put("bname", new JSONString(buttonNameContentInput.getValue()));
		parameterJSON.put("blinkurl", new JSONString(linkUrlContentInput.getValue()));
		parameterJSON.put("linkfileurl", new JSONString(fileUploadUploadInstance.getValue()));
		parameterJSON.put("filedes", new JSONString(fileDescriptionContentInput.getValue()));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					getMaterialExtentsWindow().alert(titleContentInput.getValue()+"\n\n소식이 추가 되었습니다.", 500);
					clear();
				} else {
					getMaterialExtentsWindow().alert("삭제 실패했습니다. 관리자에게 문의하세요.", 500);
				}
			}
		});
	}
	
	public void updateDetail(String nwsid) {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("UPDATE_NEWS"));
		parameterJSON.put("nwsid", new JSONString(nwsid));
		parameterJSON.put("usrid", new JSONString(Registry.getUserId()));
		parameterJSON.put("title", new JSONString(titleContentInput.getValue()));
		parameterJSON.put("body", new JSONString(crbody.getHTML()));
		parameterJSON.put("nwskind", new JSONNumber(((int)typeSelectionPanel.getSelectedValue())));
		parameterJSON.put("isview", new JSONNumber(Math.abs(((int)displayOutSelectionPanel.getSelectedValue())-1)));
		parameterJSON.put("isactivation", new JSONNumber(((int)statusSelectionPane.getSelectedValue())));
		parameterJSON.put("bname", new JSONString(buttonNameContentInput.getValue()));
		parameterJSON.put("blinkurl", new JSONString(linkUrlContentInput.getValue()));
		parameterJSON.put("linkfileurl", new JSONString(fileUploadUploadInstance.getValue()));
		parameterJSON.put("filedes", new JSONString(fileDescriptionContentInput.getValue()));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					getMaterialExtentsWindow().alert(titleContentInput.getValue()+"\n\n이(가) 업데이트 되었습니다.", 500);
					clear();
				} else {
					getMaterialExtentsWindow().alert("업데이트 실패했습니다. 관리자에게 문의하세요.", 500);
				}
			}
		});
	}
	private void buildBasicInformation() {

		infoPanel = new MaterialPanel();
		infoPanel.setPadding(20);
		
		MaterialLabel titleLabel =new MaterialLabel("- 기본 정보");
		titleLabel.setTextColor(Color.BLUE);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setFontSize("1.2em");
		titleLabel.setMarginBottom(10);
		infoPanel.add(titleLabel);
		
		
		// line 01
		MaterialRow row01 = new MaterialRow();
		infoPanel.add(row01);
		createLabelInstance("s2", 46.25, row01, "NEWS_ID", Color.GREY_LIGHTEN_3, TextAlign.CENTER,0);
		this.cotIdLabel = createLabelInstance("s4", 46.25, row01, "", null, TextAlign.LEFT, 0);
		createLabelInstance("s2", 46.25, row01, "조회수", Color.GREY_LIGHTEN_3, TextAlign.CENTER, 0);
		this.readCountLabel = createLabelInstance("s4", 46.25, row01, "", null, TextAlign.LEFT, 0);
		
		// line 02
		MaterialRow row02 = new MaterialRow();
		infoPanel.add(row02);
		createLabelInstance("s2", 46.25, row02, "제목", Color.GREY_LIGHTEN_3, TextAlign.CENTER, 0);
		this.titleContentInput = new MaterialInput(); //createContentInputInstance("s10", 46.25, row02, "", "제목을 입력해 주세요.");
		titleContentInput.setWidth("690px");
		titleContentInput.setHeight("46.25px");
		titleContentInput.setTextAlign(TextAlign.LEFT);
		titleContentInput.setBackgroundColor(Color.WHITE);
		row02.add(titleContentInput);

		// line 03
		MaterialRow row03 = new MaterialRow();
		infoPanel.add(row03);
		createLabelInstance("s2", 46.25, row03, "작성일", Color.GREY_LIGHTEN_3, TextAlign.CENTER, 0);
		this.createDateLabel = createLabelInstance("s4", 46.25, row03, "2019-09-19 15:33:00", null, TextAlign.LEFT, 0);
		createLabelInstance("s2", 46.25, row03, "수정일", Color.GREY_LIGHTEN_3, TextAlign.CENTER, 0);
		this.editDateLabel = createLabelInstance("s4", 46.25, row03, "2019-09-19 15:33:00", null, TextAlign.LEFT, 0);

		// line 04
		MaterialRow row04 = new MaterialRow();
		infoPanel.add(row04);
		createLabelInstance("s2", 46.25, row04, "처리상태", Color.GREY_LIGHTEN_3, TextAlign.CENTER, 0);
		this.statusSelectionPane = buildSelectionPanel("s4", row04, "작업 전", "작업 완료", "작업 보류");
		createLabelInstance("s2", 46.25, row04, "작성자", Color.GREY_LIGHTEN_3, TextAlign.CENTER, 0);
		this.owner = createLabelInstance("s4", 46.25, row04, "홍길동", null, TextAlign.LEFT, 0);

		// line 05
		MaterialRow row05 = new MaterialRow();
		infoPanel.add(row05);
		MaterialLabel MainLabel = createLabelInstance("s2", 46.25, row05, "메인노출", Color.GREY_LIGHTEN_3, TextAlign.CENTER, 0);
		this.displayOutSelectionPanel = buildSelectionPanel("s4", row05, "노출", "미노출");

		// 프론트 footer에 노출되는 소식을 이벤트플랫폼 이벤트로 변경함으로 인하여 숨김 (2020-02-12 홍준호) 
		MainLabel.setVisible(false);
		displayOutSelectionPanel.setVisible(false);
		displayOutSelectionPanel.setEnabled(false);
		
		createLabelInstance("s2", 46.25, row05, "구분", Color.GREY_LIGHTEN_3, TextAlign.CENTER, 0);
		this.typeSelectionPanel = buildSelectionPanel("s4", row05, "뉴스", "공지사항", "이벤트");
	
		this.add(infoPanel);
	}

	private SelectionPanel buildSelectionPanel(String grid, MaterialRow row04, String ... params) {
		HashMap<String, Object> valueMap = new HashMap<String,Object>();
		for (int i=0; i<params.length; i++) {
			valueMap.put(params[i], i);
		}
		
		SelectionPanel box = new SelectionPanel();
		box.setGrid(grid);
		box.setElementMargin(5);
		box.setElementPadding(5);
		box.setElementRadius(6);
		box.setTextAlign(TextAlign.LEFT);
		box.setSingleSelection(true);
		box.setValues(valueMap);
		box.setFontSize("1.0em");
		box.setMarginTop(0);
		box.setMarginBottom(0);
		box.setLineHeight(46.25);
		box.setHeight("46.25px");
		box.setSelectionOnSingleMode(params[0]);
		row04.add(box);
		
		return box;
	}
	
	private  MaterialLabel createLabelInstance(String grid, double lineHeight, MaterialRow tgrRow, String title, Color bgColor, TextAlign textAlign, int leftmargin) {
		MaterialLabel retLabel = new MaterialLabel(title);
		if (bgColor != null) retLabel.setBackgroundColor(bgColor);
		retLabel.setTooltip(title);
		retLabel.setTextAlign(textAlign);
		retLabel.setOverflow(Overflow.HIDDEN);
		retLabel.setGrid(grid);
		retLabel.setHeight(lineHeight+"px");
		retLabel.setLineHeight(lineHeight);
		retLabel.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		retLabel.setMarginLeft(leftmargin);
		tgrRow.add(retLabel);
		
		return retLabel;
	}
	
//	private  ContentInput createContentInputInstance(String grid, double lineHeight, MaterialRow tgrRow, String title, String placeholder) {
//	
//		String inputWidth = "";
//		String inputLabelWidth = "";
//		
//		if (grid.equals("s10")) {
//			inputWidth = "620px";
//			inputLabelWidth = "690px";
//		}else if (grid.equals("s4")) {
//			inputWidth = "200px";
//			inputLabelWidth = "260px";
//		}
//		
//		ContentInput retCompo = new ContentInput(InputType.TEXT, grid, placeholder, inputWidth, inputLabelWidth);
//		retCompo.setTooltip(title);
//		retCompo.setOverflow(Overflow.HIDDEN);
//		retCompo.setHeight(lineHeight+"px");
//		retCompo.setLineHeight(lineHeight);
//		tgrRow.add(retCompo);
//		
//		return retCompo;
//	}
	private EditorBase crbody;
	private void buildMainContent() {

		mainContent = new MaterialPanel();
		mainContent.setPadding(20);
		
		MaterialLabel titleLabel =new MaterialLabel("- 주 컨텐츠 (이미지를 포함한 최대 입력 크기가 900KB를 넘지 않도록 해주세요!)");
		titleLabel.setTextColor(Color.BLUE);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setFontSize("1.2em");
		titleLabel.setMarginBottom(10);
		mainContent.add(titleLabel);

		crbody = new EditorBase();
		crbody.setMode("NEWS");
		mainContent.add(crbody);
		this.add(mainContent);
		

			
		
		addLinkPanel = new MaterialPanel();
		addLinkPanel.setPadding(20);
		
		MaterialLabel title2Label =new MaterialLabel("- 버튼 링크 정보");
		title2Label.setTextColor(Color.BLUE);
		title2Label.setFontWeight(FontWeight.BOLD);
		title2Label.setTextAlign(TextAlign.LEFT);
		title2Label.setFontSize("1.2em");
		title2Label.setMarginBottom(10);
		addLinkPanel.add(title2Label);

		this.add(addLinkPanel);
		
		MaterialRow linkrow = new MaterialRow();
		addLinkPanel.add(linkrow);

		createLabelInstance("s2", 46.25, linkrow, "버튼 명", Color.GREY_LIGHTEN_3, TextAlign.CENTER, 0);
		this.buttonNameContentInput = new MaterialInput(); //createContentInputInstance("s4", 46.25, linkrow, "", "표시할 버튼 값을 입력해 주세요.");
		buttonNameContentInput.setWidth("250px");
		buttonNameContentInput.setHeight("46.25px");
		buttonNameContentInput.setTextAlign(TextAlign.LEFT);
		buttonNameContentInput.setBackgroundColor(Color.WHITE);
		buttonNameContentInput.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		linkrow.add(buttonNameContentInput);

		createLabelInstance("s2", 46.25, linkrow, "링크 값", Color.GREY_LIGHTEN_3, TextAlign.CENTER, 35);
		this.linkUrlContentInput = new MaterialInput(); //createContentInputInstance("s4", 46.25, linkrow, "", "링크 주소 값을 입력해 주세요.");
		linkUrlContentInput.setWidth("270px");
		linkUrlContentInput.setHeight("46.25px");
		linkUrlContentInput.setTextAlign(TextAlign.LEFT);
		linkUrlContentInput.setBackgroundColor(Color.WHITE);
		buttonNameContentInput.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		linkrow.add(linkUrlContentInput);
	}
	
	private void buildAttachedFiles() {
		
		attFilePanel = new MaterialPanel();
		attFilePanel.setPadding(20);
		
		MaterialLabel title2Label =new MaterialLabel("- 파일 정보");
		title2Label.setTextColor(Color.BLUE);
		title2Label.setFontWeight(FontWeight.BOLD);
		title2Label.setTextAlign(TextAlign.LEFT);
		title2Label.setFontSize("1.2em");
		title2Label.setMarginBottom(10);
		attFilePanel.add(title2Label);

	
		this.add(attFilePanel);
		
		MaterialRow linkrow = new MaterialRow();
		attFilePanel.add(linkrow);
//		createLabelInstance("s2", 46.25, linkrow, "파일 업로드", Color.GREY_LIGHTEN_3, TextAlign.CENTER, 0);
		FileUploadSimplePanel fileUploadUpload = new FileUploadSimplePanel(150,47, (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=a9158773-bd4d-49f2-a901-d3a7e204a773&chk=e1b88981-5050-4e79-989d-7006b8b41714");  //createUploadInstance("s4", 46.25, linkrow);
		fileUploadUpload.setLayoutPosition(Position.RELATIVE);
		fileUploadUpload.setBackgroundColor(Color.GREY_LIGHTEN_3);
		fileUploadUpload.setHeight("46.25px");
		fileUploadUpload.setWidth("150px");
		fileUploadUpload.setTextAlign(TextAlign.LEFT);
		fileUploadUpload.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		fileUploadUpload.getUploader().addSuccessHandler(event->{
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			String sourceValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("fileName").isString().stringValue();
			fileUploadUploadInstance.setText(uploadValue);
			fileDescriptionContentInput.setText(sourceValue);
//			mlfile.setText((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + uploadValue);
		});
		linkrow.add(fileUploadUpload);
		
		fileUploadUploadInstance = new MaterialInput(); //createContentInputInstance("s4", 46.25, linkrow, "", "파일에 대한 설명을 입력해 주세요.");
		fileUploadUploadInstance.setWidth("240px");
		fileUploadUploadInstance.setHeight("46.25px");
		fileUploadUploadInstance.setTextAlign(TextAlign.LEFT);
		fileUploadUploadInstance.setBackgroundColor(Color.WHITE);
		fileUploadUploadInstance.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		linkrow.add(fileUploadUploadInstance);
		
		
		createLabelInstance("s2", 46.25, linkrow, "파일 설명", Color.GREY_LIGHTEN_3, TextAlign.CENTER, 35);
		this.fileDescriptionContentInput = new MaterialInput(); //createContentInputInstance("s4", 46.25, linkrow, "", "파일에 대한 설명을 입력해 주세요.");
		fileDescriptionContentInput.setWidth("270px");
		fileDescriptionContentInput.setHeight("46.25px");
		fileDescriptionContentInput.setTextAlign(TextAlign.LEFT);
		fileDescriptionContentInput.setBackgroundColor(Color.WHITE);
		fileDescriptionContentInput.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		linkrow.add(fileDescriptionContentInput);

	}

//	private FileUploadSimplePanel createUploadInstance(String grid, double lineHeight, MaterialRow linkrow) {
//		FileUploadSimplePanel inputCompo = new FileUploadSimplePanel(); 
//		inputCompo.setLayoutPosition(Position.STATIC);
//		inputCompo.setOverflow(Overflow.HIDDEN);
//		inputCompo.setGrid(grid);
//		inputCompo.setHeight(lineHeight+"px");
//		inputCompo.setLineHeight(lineHeight);
////		inputCompo.setBackgroundColor(Color.BLUE_DARKEN_3);
////		inputCompo.setLayoutPosition(Position.ABSOLUTE);
////		inputCompo.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
////		inputCompo.setWidth("250px");
//		linkrow.add(inputCompo);
//		
//		return inputCompo;
//	}
	
	public void clear() {

		cotIdLabel.setText("");
		readCountLabel.setText("");
		createDateLabel.setText("");
		editDateLabel.setText("");
		owner.setText("");
		
		statusSelectionPane.reset();
		displayOutSelectionPanel.reset();
		typeSelectionPanel.reset();
		
		titleContentInput.setValue("");
		buttonNameContentInput.setValue("");
		linkUrlContentInput.setValue("");
		fileDescriptionContentInput.setValue("");
	}
	
}
