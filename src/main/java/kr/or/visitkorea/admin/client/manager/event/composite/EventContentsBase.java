package kr.or.visitkorea.admin.client.manager.event.composite;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.addins.client.richeditor.base.constants.ToolbarButton;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.event.EventApplication;
import kr.or.visitkorea.admin.client.manager.event.EventContentsTree;
import kr.or.visitkorea.admin.client.manager.event.EventStatus;
import kr.or.visitkorea.admin.client.manager.event.widgets.FetchCallback;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanelWithNoImage;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventContentsBase extends AbstractEventContents {
	private EventContentsTree host;
	private MaterialLabel cotId;
	private MaterialTextBox title;
	private MaterialRichEditor contents;
	private MaterialTextBox reject;
	private SelectionPanel announceType;
	private SelectionPanel isLogin;
	private MaterialComboBox<Object> status;
	private MaterialInput startDate;
	private MaterialInput endDate;
	private MaterialInput announceDate;
	private MaterialIcon evtIdCopyIcon;
	private ContentsBaseInfoCollect infoCollectRow;
	private ContentsBaseCaution cautionRow;
	private ContentsBaseStaff staffRow;
	private ContentsBaseTemplate designTemplateRow;
	private SelectionPanel cautionTemplate;
	private SelectionPanel staffTemplate;
	private MaterialPanel editorPanel;
	private MaterialRow rejectRow;
	private SelectionPanel isBlacklist;
	private MaterialTextBox BlacklistFile;
	private MaterialButton xlsUploadBtn;
	private MaterialColumn ButtonColumn;
	private MaterialColumn blacklistinfocm;
	private String blacklistFileName;
	private JSONArray blacklist;
	
	public EventContentsBase(EventContentsTree host, MaterialExtentsWindow window) {
		super(window);
		this.host = host;
	}

	@Override
	public void setCotId(String cotId) {
		super.setCotId(cotId);
		this.cotId.setText(cotId);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("기본정보");
	}
	
	@Override
	public MaterialWidget render() {
		MaterialPanel panel = new MaterialPanel();
		panel.setHeight("634px");
		panel.setPaddingTop(15);
		panel.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		
		MaterialRow row = null;

		//	Row Define >>
		row = addRow(panel);
		

			
		addLabel(row, "COT ID", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.cotId  = addLabel(row, "", TextAlign.CENTER, Color.WHITE, "s4");
		
		this.evtIdCopyIcon = new MaterialIcon(IconType.CONTENT_COPY);
		this.evtIdCopyIcon.setGrid("");
		this.evtIdCopyIcon.setMarginTop(11);
		this.evtIdCopyIcon.setTooltip("COTID 복사");
		this.evtIdCopyIcon.addClickHandler(event -> {
			this.copy(this.getCotId());
			MaterialToast.fireToast("COTID가 복사되었습니다.");
		});
		row.add(this.evtIdCopyIcon);
		
		addLabel(row, "상태", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		
		this.status = addCombobox(row, "s3");
		this.status.addItem("작성중", EventStatus.WRITING);
		this.status.addItem("승인대기", EventStatus.APPROVAL_WAIT);
		this.status.addItem("진행대기", EventStatus.PROGRESS_WAIT);
		this.status.addItem("진행중", EventStatus.PROGRESSING);
		this.status.addItem("이벤트종료", EventStatus.END);
		this.status.addItem("당첨자발표", EventStatus.ANNOUNCE);
		this.status.addItem("승인거절", EventStatus.NEGATIVE);
		this.status.setEnabled(Registry.getPermission(EventApplication.ADMIN_PERMISSION));
		this.status.addValueChangeHandler(e -> {
			EventStatus status = (EventStatus) this.status.getSelectedValue().get(0);
			this.host.setEventStatus(status);
			this.host.getSaveIcon().setVisible(true);
			
			boolean isNegative = status == EventStatus.NEGATIVE;
			this.rejectRow.setVisible(isNegative);
			this.reject.setEnabled(isNegative);
		});

		//	Row Define >>
		rejectRow = addRow(panel);
		rejectRow.setVisible(false);
		addLabel(rejectRow, "거절사유", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.reject = addInputText(rejectRow, "사유를 입력하세요", "s10");
		
		//	Row Define >>
		row = addRow(panel);
		addLabel(row, "제목", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.title = addInputText(row, "제목을 입력해주세요.", "s10");

		//	Row Define >>
		row = addRow(panel);
		addLabel(row, "이벤트 기간", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.startDate = addInputDate(row, "s2");
		this.endDate = addInputDate(row, "s2");
		
		addLabel(row, "당첨 발표일", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.announceDate = addInputDate(row, "s4");

		//	Row Define >>
		row = addRow(panel);
		addLabel(row, "당첨자 선발방법", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> announceValueMap = new HashMap<>();
		announceValueMap.put("직접 등록", 0);
		announceValueMap.put("템플릿 사용", 1);
		announceValueMap.put("선발안함", 2);
		this.announceType = addSelectionPanel(row, "s4", TextAlign.LEFT, announceValueMap, 5, 5, 5, true);
		this.announceType.setSelectionOnSingleMode("직접 등록");

		announceType.addStatusChangeEvent(event->{
			if (announceType.getSelectedValue().equals(0)) {
				host.DetailPanelReload(0);
				host.setAnnouce_type(0);
			} else if (announceType.getSelectedValue().equals(1)) {
				host.DetailPanelReload(1);
				host.setAnnouce_type(1);
			} else {
				host.DetailPanelReload(2);
				host.setAnnouce_type(2);
			}
		});
		
		HashMap<String, Object> isLoginValueMap = new HashMap<>();
		isLoginValueMap.put("필수", 1);
		isLoginValueMap.put("미필수", 0);
		addLabel(row, "로그인 필수여부", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.isLogin = addSelectionPanel(row, "s4", TextAlign.LEFT, isLoginValueMap, 5, 5, 5, true);
		this.isLogin.setSelectionOnSingleMode("필수");

		//	Row Define >>
		row = addRow(panel);
		addLabel(row, "블랙리스트 여부", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> blacklistValueMap = new HashMap<>();
		blacklistValueMap.put("설정", 1);
		blacklistValueMap.put("미설정", 0);
		this.isBlacklist = addSelectionPanel(row, "s4", TextAlign.LEFT, blacklistValueMap, 5, 5, 5, true);
		this.isBlacklist.setSelectionOnSingleMode("설정");
		isBlacklist.addStatusChangeEvent(event -> {
			if (isBlacklist.getSelectedValue().equals(0)) {
				BlacklistFile.setVisible(false);
				xlsUploadBtn.setVisible(false);
				blacklistinfocm.setVisible(false);
				ButtonColumn.setVisible(false);
			} else {
				BlacklistFile.setVisible(true);
				xlsUploadBtn.setVisible(true);
				blacklistinfocm.setVisible(true);
				ButtonColumn.setVisible(true);
			}
		});
		BlacklistFile = addInputText(row, ".", "s3");
		BlacklistFile.setEnabled(false);
		
		UploadPanelWithNoImage xlsFileUploader = new UploadPanelWithNoImage(0, 0
				, GWT.getHostPageBaseURL() + "call?cmd=BLACK_LIST_XLS&requestType=event");
		xlsFileUploader.setWidth("100%");
		
		xlsUploadBtn = xlsFileUploader.getBtn();
		xlsUploadBtn.setHeight("46.25px");
		xlsUploadBtn.setWidth("150px");
		xlsUploadBtn.setType(ButtonType.FLAT);
		xlsUploadBtn.setTop(-8);
		xlsUploadBtn.setRight(-20);
		xlsUploadBtn.setPadding(0);
		xlsUploadBtn.setText("엑셀 파일 업로드");
		xlsUploadBtn.setBorderRadius("5px");
		xlsUploadBtn.setBackgroundColor(Color.BLUE_DARKEN_1);
		xlsUploadBtn.remove(0);
		xlsUploadBtn.setLineHeight(0);
		xlsUploadBtn.setTextColor(Color.WHITE);
		xlsUploadBtn.setTextAlign(TextAlign.CENTER);
		xlsFileUploader.getUploader().setAcceptedFiles(".xls, .xlsx");
		xlsFileUploader.getUploader().addSuccessHandler(event -> {
			
		JSONObject resultObj = JSONParser.parseStrict(event.getResponse().getBody()).isObject();
		blacklistFileName = event.getTarget().getName().toString();
		BlacklistFile.setText(blacklistFileName);
		JSONObject headerObj = resultObj.get("header").isObject();
		String process = headerObj.get("process").isString().stringValue();
		
		if (process.equals("success")) {
			JSONObject bodyResult = resultObj.get("body").isObject();
			JSONArray resultArr = bodyResult.get("resultArr").isArray();
			this.blacklist = new JSONArray();
			
			for (int i = 1; i < resultArr.size(); i++) {
				JSONArray resultRow = resultArr.get(i).isArray();
				
				
				this.blacklist.set(blacklist.size(), resultRow.isArray().get(0).isString());
			}
			
		} else {
			this.getMaterialExtendsWindow().alert(headerObj.get("ment").isString().stringValue());
		}
		
			
		});
		row.add(xlsFileUploader);
		
		blacklistinfocm = new MaterialColumn();
		blacklistinfocm.setGrid("s6");
		blacklistinfocm.setMarginTop(20);
		blacklistinfocm.setMarginLeft(160);
		MaterialLabel Blacklistinfo = infolabel("※ 엑셀 양식 다운로드 선택 시 블랙리스트 양식이 다운로드 됩니다.");
		MaterialLabel Blacklistinfo2 = infolabel("　기본 리스트 다운로드 선택 시 관리자가 업로드한 리스트가 다운로드 됩니다.");
		Blacklistinfo2.setMarginTop(5);
		blacklistinfocm.add(Blacklistinfo);
		blacklistinfocm.add(Blacklistinfo2);
		row.add(blacklistinfocm);
		
		
		ButtonColumn = new MaterialColumn();
		ButtonColumn.setGrid("s4");
		ButtonColumn.setMarginTop(20);
		MaterialButton SampleExcel = new MaterialButton("엑셀 양식 다운로드 ");
		SampleExcel.setHeight("40px");
		SampleExcel.setWidth("150px");
		SampleExcel.setPadding(0);
		SampleExcel.setType(ButtonType.FLAT);
		SampleExcel.setBorderRadius("5px");
		SampleExcel.setBackgroundColor(Color.GREY_DARKEN_1);
		SampleExcel.setTextColor(Color.WHITE);
		SampleExcel.addClickHandler(event -> {
			StringBuffer  sb = new StringBuffer();
			sb.append("./call?cmd=FILE_DOWNLOAD_XLS&select_type=BlackList_Sample");
			Window.open(sb.toString(),"_self", "enabled");
		});
		
		ButtonColumn.add(SampleExcel);
		MaterialButton BaseExcel = new MaterialButton("기본 파일 다운로드 ");
		BaseExcel.setTextColor(Color.WHITE);
		BaseExcel.setHeight("40px");
		BaseExcel.setPadding(0);
		BaseExcel.setMarginLeft(20);
		BaseExcel.setWidth("150px");
		BaseExcel.setType(ButtonType.FLAT);
		BaseExcel.setBorderRadius("5px");
		BaseExcel.setBackgroundColor(Color.GREY_DARKEN_1);
		BaseExcel.addClickHandler(event -> {
			String downurl = "./call?cmd=BLACK_LIST_XLS_DOWNLOAD&select_type=BlackList_Base";
			
			
			StringBuffer  sb = new StringBuffer();
			sb.append(downurl);
			Window.open(sb.toString(),"_self", "enabled");
		});
		
		ButtonColumn.add(BaseExcel);
		row.add(ButtonColumn);
		//	Row Define >>
		row = addRow(panel);
		addLabel(row, "이벤트 소개", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");

		this.contents = new MaterialRichEditor("입력하세요");
		this.contents.setHeight("300px");
		this.contents.setText("");
		this.contents.setMiscOptions(ToolbarButton.CODE_VIEW);
		
		
		editorPanel = new MaterialPanel();
		editorPanel.setGrid("s10");
		editorPanel.setHeight("480px");
		editorPanel.add(contents);
		row.add(editorPanel);

		//	Row Define >>
		this.infoCollectRow = new ContentsBaseInfoCollect(this, "응모자 정보 수집", Color.GREY_LIGHTEN_3);
		panel.add(infoCollectRow);

		
		//	Row Define >>
		this.cautionRow = new ContentsBaseCaution(this, "유의사항", Color.GREY_LIGHTEN_3);
		panel.add(cautionRow);

		//	Row Define >>
		row = addRow(panel);
		addLabel(row, "유의사항 템플릿", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s3");
		HashMap<String, Object> cautionValueMap = new HashMap<String, Object>();
		cautionValueMap.put("일반", 0);
		cautionValueMap.put("와이드", 1);
		this.cautionTemplate = addSelectionPanel(row, "s4", TextAlign.LEFT, cautionValueMap, 5, 5, 5, true);
		this.cautionTemplate.setSelectionOnSingleMode("일반");

		//	Row Define >>
		this.staffRow = new ContentsBaseStaff(this, "문의 담당자 정보", Color.GREY_LIGHTEN_3);
		panel.add(staffRow);

		//	Row Define >>
		row = addRow(panel);
		addLabel(row, "문의 담당자 템플릿", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s3");
		HashMap<String, Object> staffValueMap = new HashMap<String, Object>();
		staffValueMap.put("일반", 0);
		staffValueMap.put("와이드", 1);
		this.staffTemplate = addSelectionPanel(row, "s4", TextAlign.LEFT, staffValueMap, 5, 5, 5, true);
		this.staffTemplate.setSelectionOnSingleMode("일반");

		//	Row Define >>
		designTemplateRow = new ContentsBaseTemplate(this, "디자인 템플릿", Color.GREY_LIGHTEN_3);
//		panel.add(designTemplateRow);
		
		return panel;
	}
	
	@Override
	public void loadData(FetchCallback callback) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("GET_EVENT_BASE"));
		paramJson.put("evtId", new JSONString(this.getEvtId()));
		paramJson.put("cotId", new JSONString(this.getCotId()));
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyResultObj = resultObj.get("body").isObject().get("result").isObject();
				setupContentValue(bodyResultObj);
			}
		});
	}

	@Override
	public void saveData() {
		JSONObject model = this.buildEventModel();
		if (model == null) {
			return;
		}
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SAVE_EVENT_BASE"));
		paramJson.put("model", model);
		VisitKoreaBusiness.post("call", paramJson.toString(), this.saveCallback());
	}
	

	public void saveData2(EventContentsProcess p , EventContentsComponents c) {
		JSONObject model = this.buildEventModel();
		if (model == null) {
			return;
		}
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SAVE_EVENT_BASE"));
		paramJson.put("model", model);
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				p.saveData2(c);
				this.getMaterialExtendsWindow().alert("성공적으로 저장되었습니다.");
			} 
			
		});
		
	}
	
	
	@Override
	public JSONObject buildEventModel() {
		JSONObject obj = new JSONObject();
		obj.put("cotId", new JSONString(this.getCotId()));
		obj.put("evtId", new JSONString(this.getEvtId()));
		obj.put("usrId", new JSONString(Registry.getUserId()));
		obj.put("title", new JSONString(this.title.getValue()));
		obj.put("contents", new JSONString(this.contents.getValue()));
		obj.put("reject", new JSONString(this.reject.getValue()));
		obj.put("startDate", new JSONString(this.startDate.getValue()));
		obj.put("endDate", new JSONString(this.endDate.getValue()));
		if(this.host.getContentImages().getMasterImageId() != null)
			obj.put("imgId", new JSONString(this.host.getContentImages().getMasterImageId()));
		obj.put("announceDate", new JSONString(this.announceDate.getValue()));
		obj.put("announceType", new JSONNumber((int) this.announceType.getSelectedValue()));
		obj.put("status", new JSONNumber(this.status.getSelectedIndex() + 1));
		obj.put("isCaution", JSONBoolean.getInstance(this.cautionRow.getCheckbox().getValue()));
		obj.put("isStaff", JSONBoolean.getInstance(this.staffRow.getCheckbox().getValue()));
		obj.put("isCollect", JSONBoolean.getInstance(this.infoCollectRow.getCheckbox().getValue()));
		obj.put("isLogin", JSONBoolean.getInstance((int) this.isLogin.getSelectedValue() == 1 ? true : false));
		obj.put("templateType", new JSONNumber(1));
		obj.put("caution", new JSONString(this.cautionRow.getCaution()));
		obj.put("userInfoCollect", this.infoCollectRow.buildModel());
		obj.put("staffInfo", this.staffRow.buildModel());
		obj.put("cautionTemplate", new JSONNumber((int) this.cautionTemplate.getSelectedValue()));
		obj.put("staffTemplate", new JSONNumber((int) this.staffTemplate.getSelectedValue()));
		obj.put("infoCollectView", new JSONNumber(0));
		obj.put("terms", new JSONString(this.infoCollectRow.getTerms().replaceAll("\n", "<br>")));
		obj.put("blacklist", this.blacklist);
		obj.put("blacklistFile",new JSONString(blacklistFileName != null ? blacklistFileName : ""));
		obj.put("isBlacklist", new JSONNumber((int) this.isBlacklist.getSelectedValue()));
		return obj;
	}
	
	@Override
	public void setupContentValue(JSONObject obj) {
		if (obj.containsKey("COT_ID"))
			this.cotId.setValue(obj.get("COT_ID").isString().stringValue());
		if (obj.containsKey("TITLE"))
			this.title.setValue(obj.get("TITLE").isString().stringValue());
		if (obj.containsKey("CONTENTS"))
			this.contents.setText(obj.get("CONTENTS").isString().stringValue());
		if (obj.containsKey("REJECT"))
			this.reject.setValue(obj.get("REJECT").isString().stringValue());
		if (obj.containsKey("IMG_ID"))
			this.host.getContentImages().setMasterImageId(obj.get("IMG_ID").isString().stringValue());
		if (obj.containsKey("START_DATE"))
			this.startDate.setValue(convertDateFormat(obj.get("START_DATE").isString().stringValue()));
		if (obj.containsKey("END_DATE"))
			this.endDate.setValue(convertDateFormat(obj.get("END_DATE").isString().stringValue()));
		if (obj.containsKey("ANNOUNCE_DATE"))
			this.announceDate.setValue(convertDateFormat(obj.get("ANNOUNCE_DATE").isString().stringValue()));
		if (obj.containsKey("ANNOUNCE_TYPE")) {
			int typeNum = (int) obj.get("ANNOUNCE_TYPE").isNumber().doubleValue();
			
			if (typeNum == 0) {
				this.announceType.setSelectionOnSingleMode("직접 등록");
			} else if (typeNum == 1) {
				this.announceType.setSelectionOnSingleMode("템플릿 사용");
			} else if (typeNum == 2) {
				this.announceType.setSelectionOnSingleMode("선발안함");
			}
		}
		
		if (obj.containsKey("BLACKLIST_USE_YN") && obj.get("BLACKLIST_USE_YN") != null)
			this.isBlacklist.setSelectionOnSingleMode(obj.get("BLACKLIST_USE_YN").isBoolean().booleanValue() ? "설정" : "미설정");
		if (obj.containsKey("BLACKLIST_FILE_NM"))
			this.blacklistFileName = obj.get("BLACKLIST_FILE_NM").isString().stringValue();
			this.BlacklistFile.setText(blacklistFileName);
		if (obj.containsKey("IS_LOGIN"))
			this.isLogin.setSelectionOnSingleMode(obj.get("IS_LOGIN").isBoolean().booleanValue() ? "필수" : "미필수");
		if (obj.containsKey("IS_CAUTION"))
			this.cautionRow.isVisibleContent(obj.get("IS_CAUTION").isBoolean().booleanValue());
		if (obj.containsKey("IS_STAFF"))
			this.staffRow.isVisibleContent(obj.get("IS_STAFF").isBoolean().booleanValue());
		if (obj.containsKey("IS_COLLECT"))
			this.infoCollectRow.isVisibleContent(obj.get("IS_COLLECT").isBoolean().booleanValue());
		if (obj.containsKey("CAUTION_TEMPLATE"))
			this.cautionTemplate.setSelectionOnSingleMode(obj.get("CAUTION_TEMPLATE").isNumber().doubleValue() == 1 ? "와이드" : "일반");
		if (obj.containsKey("STAFF_TEMPLATE"))
			this.staffTemplate.setSelectionOnSingleMode(obj.get("STAFF_TEMPLATE").isNumber().doubleValue() == 1 ? "와이드" : "일반");
		if (obj.containsKey("STATUS")) {
			int statusNum = (int) obj.get("STATUS").isNumber().doubleValue();
			EventStatus status = EventStatus.values()[statusNum - 1];
			this.rejectRow.setVisible(status == EventStatus.NEGATIVE);
			this.host.setEventStatus(status);
			this.status.setSelectedIndex(status.getValue() - 1);
			
			this.contents.removeFromParent();
			this.editorPanel.getElement().setInnerHTML("");
			this.editorPanel.setHeight("");
			if (this.host.getEditPossSet().contains(status)) {
				this.editorPanel.setHeight("480px");
				this.editorPanel.add(this.contents);
			} else {
				this.editorPanel.getElement().setInnerHTML(this.contents.getText());
			}
		}
		this.cautionRow.setValue(obj);
		this.infoCollectRow.setValue(obj);
		this.staffRow.setValue(obj);
		this.designTemplateRow.setValue(obj);
	}
	
	public void formEnabled(boolean isEnable) {
		this.cautionRow.setVisibleEditIcon(isEnable);
		this.infoCollectRow.setVisibleEditIcon(isEnable);
		this.staffRow.setVisibleEditIcon(isEnable);
		this.designTemplateRow.setVisibleEditIcon(isEnable);
		this.cautionRow.getCheckbox().setEnabled(isEnable);
		this.infoCollectRow.getCheckbox().setEnabled(isEnable);
		this.staffRow.getCheckbox().setEnabled(isEnable);
		this.designTemplateRow.getCheckbox().setEnabled(isEnable);
		this.reject.setEnabled(isEnable);
		this.title.setEnabled(isEnable);
		this.isLogin.setEnabled(isEnable);
		this.isBlacklist.setEnabled(isEnable);
		this.xlsUploadBtn.setEnabled(isEnable);
		this.announceDate.setEnabled(isEnable);
		this.announceType.setEnabled(isEnable);
		this.staffTemplate.setEnabled(isEnable);
		this.cautionTemplate.setEnabled(isEnable);
		this.designTemplateRow.setEnabled(isEnable);
		this.startDate.setEnabled(isEnable);
		this.endDate.setEnabled(isEnable);
	}
	
	@Override
	public void statusChangeProcess(EventStatus status) {
		this.eventStatus = status;
		switch (this.eventStatus) {
			case NEGATIVE:
				this.reject.setEnabled(Registry.getPermission(EventApplication.ADMIN_PERMISSION));
			case PROGRESSING:
			case END:
			case ANNOUNCE:
			case WRITING: {
				this.formEnabled(true);
			} break;
			
			default: {
				this.formEnabled(false);
			}
		}
	}

	public void setStatus(EventStatus status) {
		this.status.setSelectedIndex(status.getValue() - 1);
	}
	
	private MaterialLabel infolabel(String text) {
		
		MaterialLabel info = new MaterialLabel(text);
		info.setHeight("20px");
		info.setFontSize("14px");
		info.setHeight("20px");
		info.setTextAlign(TextAlign.LEFT);
		
		
		
		return info;
	}

	public native void copy(String cotId) /*-{
		var dummy = document.createElement("textarea");
		document.body.appendChild(dummy);
		dummy.value = cotId;
		dummy.select();
		document.execCommand("copy");
		document.body.removeChild(dummy);
	}-*/;
}
