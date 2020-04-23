package kr.or.visitkorea.admin.client.manager.event.composite;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialDatePicker;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.event.EventContentsTree;
import kr.or.visitkorea.admin.client.manager.event.EventStatus;
import kr.or.visitkorea.admin.client.manager.event.model.EventGift;
import kr.or.visitkorea.admin.client.manager.event.model.EventProcess;
import kr.or.visitkorea.admin.client.manager.event.tree.EventProcessTreeItem;
import kr.or.visitkorea.admin.client.manager.event.widgets.FetchCallback;
import kr.or.visitkorea.admin.client.manager.event.widgets.GiftContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanelWithNoImage;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventContentsProcess extends AbstractEventContents {
	private EventContentsTree host;
	private String subEvtId;
	private MaterialTextBox processTitle;
	private MaterialComboBox<Object> subEventType;
	private MaterialDatePicker startDate;
	private MaterialDatePicker endDate;
	private SelectionPanel immediType;
	private SelectionPanel electType;
	private MaterialLabel immediElectLabel;
	private MaterialPanel giftContentArea;
	private List<EventProcess> processList = new ArrayList<>();
	private EventProcess selectedProcess;
	private EventProcessTreeItem selectedProcessTreeItem;
	private MaterialLabel notWinLabel;
	private SelectionPanel notWin;
	private EventGift notWinObj;
	private SelectionPanel randomType;
	private MaterialLabel randomTypeLabel;
	private MaterialTextBox announcePer;
	private MaterialTextBox userPredictCnt;
	private MaterialRow xlsUploadRow;
	private MaterialRow percentRow;
	private MaterialButton xlsUploadBtn;
	private MaterialButton xlsDownloadBtn;
	private MaterialLabel ExcelFileNm;
	private JSONArray rullsetArray;
	private SelectionPanel winconduse;
	private MaterialLabel wincondvalueLabel;
	private MaterialTextBox wincondvalue;
	private MaterialLabel wincondinfo;
	private MaterialRow Winuserow;
	private MaterialButton addButton;
	
	public EventContentsProcess(EventContentsTree host, MaterialExtentsWindow window) {
		super(window);
		this.host = host;
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("진행방식 설정");
		
	}

	@Override
	public MaterialWidget render() {
		MaterialPanel panel = new MaterialPanel();
		panel.setPaddingTop(10);
		panel.setPaddingBottom(10);
		panel.setHeight("94.5%");
		panel.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		
		MaterialRow row = null;
		
		// Row Define >>
		row = addRow(panel);
		addLabel(row, "프로그램명", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		processTitle = addInputText(row, "프로그램명을 입력해주세요", "s4");
		processTitle.addKeyUpHandler(event -> {
			this.selectedProcessTreeItem.setText(this.processTitle.getValue());
			this.selectedProcess.setTitle(this.processTitle.getValue());
		});

		addLabel(row, "게임선택", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		subEventType = addCombobox(row, "s4");
		subEventType.addValueChangeHandler(event -> {
			this.selectedProcess.setEvtTypeCd(this.subEventType.getSelectedIndex() == 0 ? 2 : 4 );
			if(this.subEventType.getSelectedIndex() == 1) {
				Winuserow.setVisible(true);
				MaterialLink link = (MaterialLink)electType.getChildrenList().get(0);
				link.setVisible(true);
				this.selectedProcess.setElectType((int) electType.getSelectedValue());
			}
			else { 
				Winuserow.setVisible(false);
				MaterialLink link = (MaterialLink)electType.getChildrenList().get(0);
				link.setVisible(false);
				 this.electType.setSelectionOnSingleMode("즉시선정");
				 this.selectedProcess.setElectType((int) electType.getSelectedValue());
				 immediElectLabel.setVisible(true);
					immediType.setVisible(true);
					if (immediType.getSelectedValue().equals(1)) {
						randomTypeLabel.setVisible(true);
						randomType.setVisible(true);
						notWinLabel.setVisible(true);
						notWin.setVisible(true);
						if (randomType.getSelectedValue().equals(0)) {
							xlsUploadRow.setVisible(false);
							percentRow.setVisible(true);
						} else {
							xlsUploadRow.setVisible(true);
							percentRow.setVisible(false);
						}
					}
			}
		});

		// Row Define >>
		row = addRow(panel);
		addLabel(row, "프로그램 기간", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		startDate = addDatePicker(row, "s3");
		startDate.addBlurHandler(event -> {
			this.selectedProcess.setStartDate(convertDateToString(startDate.getValue()));
		});
		
		endDate = addDatePicker(row, "s3");
		endDate.addBlurHandler(event -> {
			this.selectedProcess.setEndDate(convertDateToString(endDate.getValue()));
		});

		// Row Define >>
		row = addRow(panel);
		addLabel(row, "선정방식", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> electValueMap = new HashMap<>();
		electValueMap.put("추후선정", 0);
		electValueMap.put("즉시선정", 1);
		electType = addSelectionPanel(row, "s4", TextAlign.LEFT, electValueMap);
		electType.addStatusChangeEvent(event -> {
			this.selectedProcess.setElectType((int) electType.getSelectedValue());
			
			if (electType.getSelectedValue().equals(0)) {
				immediElectLabel.setVisible(false);
				immediType.setVisible(false);
				notWinLabel.setVisible(false);
				notWin.setVisible(false);
				randomTypeLabel.setVisible(false);
				randomType.setVisible(false);
				xlsUploadRow.setVisible(false);
				percentRow.setVisible(false);
			} else {
				immediElectLabel.setVisible(true);
				immediType.setVisible(true);
				if (immediType.getSelectedValue().equals(1)) {
					randomTypeLabel.setVisible(true);
					randomType.setVisible(true);
					notWinLabel.setVisible(true);
					notWin.setVisible(true);
					if (randomType.getSelectedValue().equals(0)) {
						xlsUploadRow.setVisible(false);
						percentRow.setVisible(true);
					} else {
						xlsUploadRow.setVisible(true);
						percentRow.setVisible(false);
					}
				}
			}
		});
		
		immediElectLabel = addLabel(row, "즉시선정방식", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> immediValueMap = new HashMap<>();
		immediValueMap.put("선착순", 0);
		immediValueMap.put("랜덤", 1);
		immediType = addSelectionPanel(row, "s4", TextAlign.LEFT, immediValueMap);
		immediType.addStatusChangeEvent(event -> {
			this.selectedProcess.setImmediType((int) immediType.getSelectedValue());
			
			if (immediType.getSelectedValue().equals(0)) {
				randomTypeLabel.setVisible(false);
				randomType.setVisible(false);
				notWinLabel.setVisible(false);
				notWin.setVisible(false);
				xlsUploadRow.setVisible(false);
				percentRow.setVisible(false);
			} else {
				randomTypeLabel.setVisible(true);
				randomType.setVisible(true);
				notWinLabel.setVisible(true);
				notWin.setVisible(true);
				if (randomType.getSelectedValue().equals(0)) {
					xlsUploadRow.setVisible(false);
					percentRow.setVisible(true);
				} else {
					xlsUploadRow.setVisible(true);
					percentRow.setVisible(false);
				}
			}
		});
		
		// Row Define >>
		Winuserow = addRow(panel);
		MaterialLabel WinuseLabel = addLabel(Winuserow, "당첨조건설정", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> winconduseMap = new HashMap<>();
		winconduseMap.put("설정", 1);
		winconduseMap.put("미설정", 0);
		winconduse = addSelectionPanel(Winuserow, "s4", TextAlign.LEFT, winconduseMap);
		winconduse.addStatusChangeEvent(event -> {
			this.selectedProcess.setWinCondUse((int) winconduse.getSelectedValue());
			
			if (winconduse.getSelectedValue().equals(0)) {
				wincondvalueLabel.setVisible(false);
				wincondvalue.setVisible(false);
				wincondinfo.setVisible(false);
			} else {
				wincondvalueLabel.setVisible(true);
				wincondvalue.setVisible(true);
				wincondinfo.setVisible(true);
			}
		});
		
		this.winconduse.setSelectionOnSingleMode("설정");
		
		wincondvalueLabel = addLabel(Winuserow, "정답 수 ", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		
		wincondvalue = addInputText(Winuserow, "", "s3");
		wincondvalue.addKeyUpHandler(e -> {
			if (e.getNativeKeyCode() < 48 || e.getNativeKeyCode() > 57 && e.getNativeKeyCode() < 96 || e.getNativeKeyCode() > 106) {
				if(e.getNativeKeyCode() == 8) {
					this.selectedProcess.setWinCondValue(Integer.parseInt(wincondvalue.getValue()));
				}
				e.preventDefault();
				return;
			} 
			this.selectedProcess.setWinCondValue(Integer.parseInt(wincondvalue.getValue()));
		});
		
		wincondinfo = addLabel(Winuserow, "* 정답 수 설정시 정답 기준 충족 사용자만 당첨 대상에 포함 됨 ",
				TextAlign.LEFT, Color.GREY_LIGHTEN_5, "s10");
		wincondinfo.setMarginTop(5);
		wincondinfo.setMarginBottom(-20);
		wincondinfo.setMarginLeft(170);
		
		
		
		
		// Row Define >>
		row = addRow(panel);
		randomTypeLabel = addLabel(row, "랜덤선정방식", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		randomTypeLabel.setVisible(false);
		HashMap<String, Object> randomTypeValueMap = new HashMap<>();
		randomTypeValueMap.put("통계확률", 0);
		randomTypeValueMap.put("엑셀업로드", 1);
		randomType = addSelectionPanel(row, "s4", TextAlign.LEFT, randomTypeValueMap);
		randomType.setVisible(false);
		randomType.addStatusChangeEvent(e -> {
			if (randomType.getSelectedValue().equals(0)) {
				xlsUploadRow.setVisible(false);
				percentRow.setVisible(true);
			} else {
				xlsUploadRow.setVisible(true);
				percentRow.setVisible(false);
			}
			this.selectedProcess.setRandomType((int) randomType.getSelectedValue());
		});
		
		notWinLabel = addLabel(row, "꽝여부", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		notWinLabel.setVisible(false);
		HashMap<String, Object> notWinValueMap = new HashMap<>();
		notWinValueMap.put("꽝있음", true);
		notWinValueMap.put("꽝없음", false);
		notWin = addSelectionPanel(row, "s4", TextAlign.LEFT, notWinValueMap);
		notWin.setVisible(false);
		notWin.addStatusChangeEvent(e -> {
			boolean isNotWin = (boolean) notWin.getSelectedValue();
			this.selectedProcess.setNotWin(isNotWin);
			
			if (isNotWin) {
				if (this.notWinObj == null) {
					
					EventGift gift = new EventGift();
					gift.setEvtId(this.getEvtId());
					gift.setSubEvtId(this.getSubEvtId());
					gift.setOrder(this.selectedProcess.getGiftList().size());
					gift.setNotWin(true);
					gift.setDelete(false);
					gift.setTitle("꽝");
					gift.setGftId(IDUtil.uuid());
					this.notWinObj = gift;
					this.selectedProcess.getGiftList().add(gift);
				} else {
					this.notWinObj.setDelete(false);
				}
			} else {
				if (this.notWinObj != null) {
					this.notWinObj.setDelete(true);
					selectedProcess.getGiftDeleteList().add(notWinObj);
					giftContentArea.remove(notWinObj.getOrder());
					selectedProcess.getGiftList().remove(notWinObj.getOrder());
					for (int i = notWinObj.getOrder(); i < giftContentArea.getChildrenList().size(); i++) {
						GiftContentPanel gift = (GiftContentPanel)giftContentArea.getChildrenList().get(i);
						gift.setOrder(i);
						gift.setIndex(gift.getIndex()=="꽝" ? "꽝" : "경품" +(i+1));
					}
				}
			}
			
			this.refreshGiftPanel();
		});

		// Row Define >>
		xlsUploadRow = addRow(panel);
		xlsUploadRow.setVisible(false);
		addLabel(xlsUploadRow, "엑셀 파일", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		ExcelFileNm = addLabel(xlsUploadRow, "", TextAlign.LEFT, Color.WHITE, "s5");
		
		UploadPanelWithNoImage xlsFileUploader = new UploadPanelWithNoImage(0, 0
				, GWT.getHostPageBaseURL() + "call?cmd=EVENT_RULLSET_XLS&requestType=event");
		xlsFileUploader.setWidth("100%");

		xlsUploadBtn = xlsFileUploader.getBtn();
		xlsUploadBtn.setHeight("46.25px");
		xlsUploadBtn.setWidth("160px");
		xlsUploadBtn.setType(ButtonType.FLAT);
		xlsUploadBtn.setTop(-8);
		xlsUploadBtn.setPadding(0);
		xlsUploadBtn.setText("엑셀업로드");
		xlsUploadBtn.remove(0);
		xlsUploadBtn.setLineHeight(0);
		xlsUploadBtn.setTextColor(Color.WHITE);
		xlsUploadBtn.setTextAlign(TextAlign.CENTER);
		xlsFileUploader.getUploader().setAcceptedFiles(".xls, .xlsx");
		xlsFileUploader.getUploader().addSuccessHandler(event -> {
			JSONObject resultObj = JSONParser.parseStrict(event.getResponse().getBody()).isObject();
			ExcelFileNm.setText(event.getTarget().getName().toString());

			JSONObject headerObj = resultObj.get("header").isObject();
			String process = headerObj.get("process").isString().stringValue();
			if (process.equals("success")) {
				JSONObject bodyResult = resultObj.get("body").isObject();
				JSONArray resultArr = bodyResult.get("resultArr").isArray();
				
				this.rullsetArray = new JSONArray();
				
				for (int i = 1; i < resultArr.size(); i++) {
					JSONArray resultRow = resultArr.get(i).isArray();

					int entryNum = (int) Double.parseDouble(resultRow.get(0).isString().stringValue());
					JSONObject cellObj = new JSONObject();
					cellObj.put("STF_ID", new JSONString(Registry.getStfId()));
					cellObj.put("SUB_EVT_ID", new JSONString(getSubEvtId()));
					cellObj.put("ENTRY_NUM", new JSONNumber(entryNum));
					cellObj.put("GFT_ID", resultRow.get(1).isString());
					this.rullsetArray.set(rullsetArray.size(), cellObj);
				}
			} else {
				this.getMaterialExtendsWindow().alert(headerObj.get("ment").isString().stringValue());
			}
			
			this.selectedProcess.setExcelFileNm(ExcelFileNm.getText());
		});
		xlsUploadRow.add(xlsFileUploader);
		xlsDownloadBtn = new MaterialButton();
		xlsDownloadBtn.setLayoutPosition(Position.RELATIVE);
		xlsDownloadBtn.setHeight("46.25px");
		xlsDownloadBtn.setWidth("160px");
		xlsDownloadBtn.setRight(-115);
		xlsDownloadBtn.setTop(-26);
		xlsDownloadBtn.setText("양식 다운로드");
		xlsDownloadBtn.setTextColor(Color.WHITE);
		xlsDownloadBtn.setTextAlign(TextAlign.CENTER);
		xlsDownloadBtn.addClickHandler(event -> {

		
			StringBuffer  sb = new StringBuffer();
			sb.append("./call?cmd=FILE_DOWNLOAD_XLS&select_type=event_win_Sample");
			sb.append("&subEvtId=");
			sb.append(subEvtId);
			
			Window.open(sb.toString(),"_self", "enabled");
			
		});
		
		xlsUploadRow.add(xlsDownloadBtn);
		
		// Row Define >>
		percentRow = addRow(panel);
		percentRow.setVisible(false);
		addLabel(percentRow, "총 참여 예상수", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		userPredictCnt = addInputText(percentRow, "참여 예상 유저 수 (모수)", "s4");
		userPredictCnt.setType(InputType.NUMBER);
		userPredictCnt.addKeyUpHandler(e -> {
			if (e.getNativeKeyCode() < 48 || e.getNativeKeyCode() > 57 && e.getNativeKeyCode() < 96 || e.getNativeKeyCode() > 106) {
				if(e.getNativeKeyCode() == 8) {
					this.selectedProcess.setUserPredictCnt(Integer.parseInt(userPredictCnt.getValue()));
				}
				e.preventDefault();
				return;
			} 
			this.selectedProcess.setUserPredictCnt(Integer.parseInt(userPredictCnt.getValue()));
		});
		
		addLabel(percentRow, "당첨확률(%)", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		announcePer = addInputText(percentRow, "당첨확률", "s4");
		announcePer.setType(InputType.NUMBER);
		announcePer.addKeyUpHandler(e -> {
			if (e.getNativeKeyCode() < 48 || e.getNativeKeyCode() > 57 && e.getNativeKeyCode() < 96 || e.getNativeKeyCode() > 106) {
				if(e.getNativeKeyCode() == 8) {
					this.selectedProcess.setAnnouncePer(Integer.parseInt(announcePer.getValue()));
				}
				e.preventDefault();
				return;
			} 
			this.selectedProcess.setAnnouncePer(Integer.parseInt(announcePer.getValue()));
		});
		
		giftContentArea = new MaterialPanel();
		giftContentArea.setWidth("100%");
		panel.add(giftContentArea);
		
		row = new MaterialRow();
		addButton = new MaterialButton("경품추가");
		row.add(addButton);
		addButton.setBorderRadius("5px");
		addButton.setHeight("46px");
		addButton.setBackgroundColor(Color.BLUE_DARKEN_4);
		panel.add(row);
		addButton.addClickHandler(e->{
			
				if (selectedProcess.getGiftList().size() > 7) {
					
					MaterialToast.fireToast("경품은 최대 8개까지만 추가 가능합니다.");
					
				} else {
					
				EventGift gift = new EventGift();
				gift.setGftId(IDUtil.uuid());
				gift.setEvtId(selectedProcess.getEvtId());
				gift.setSubEvtId(selectedProcess.getSubEvtId());
				gift.setNotWin(false);
				gift.setOrder(selectedProcess.getGiftList().size());
				selectedProcess.getGiftList().add(gift);
				addGiftContentPanel(gift);
				}
			
		});
		return panel;
	}
	
	
	@Override
	public void loadData(FetchCallback callback) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("GET_EVENT_PROCESS"));
		paramJson.put("evtId", new JSONString(this.getEvtId()));
		paramJson.put("cotId", new JSONString(this.getCotId()));
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONArray bodyResultArr = resultObj.get("body").isObject().get("result").isArray();
				JSONArray bodyResultTypeArr = resultObj.get("body").isObject().get("resultType").isArray();
				
				this.processList.clear();
				for (int i = 0; i < bodyResultArr.size(); i++) {
					JSONObject obj = bodyResultArr.get(i).isObject();
					
					EventProcess eventProcess = EventProcess.fromJson(obj);
					this.processList.add(eventProcess);
				}
				
				this.host.appendProcessTreeItem(this.processList);
				
				for (int i = 0; i < bodyResultTypeArr.size(); i++) {
					JSONObject obj = bodyResultTypeArr.get(i).isObject();
					
					int typoCode = (int) obj.get("EVT_TYPE_CD").isNumber().doubleValue();
					String typeName = obj.get("EVT_TYPE_NM").isString().stringValue();
					this.subEventType.addItem(typeName, typoCode);
				}

				callback.onSuccess(true);
			} else {
				callback.onSuccess(false);
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
		paramJson.put("cmd", new JSONString("SAVE_EVENT_PROCESS"));
		paramJson.put("model", model);
		VisitKoreaBusiness.post("call", paramJson.toString(), this.saveCallback());
	}

	public void saveData2(EventContentsComponents c) {
		JSONObject model = this.buildEventModel();
		if (model == null) {
			return;
		}
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SAVE_EVENT_PROCESS"));
		paramJson.put("model", model);
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				c.saveData();
				this.getMaterialExtendsWindow().alert("성공적으로 저장되었습니다.");
			} 
			
		});
		
	}
	
	@Override
	public void setupContentValue(JSONObject obj) {
		
	}

	public void setupContentValue(EventProcess eventProcess) {
		this.selectedProcess = eventProcess;
		this.subEvtId = eventProcess.getSubEvtId();
		this.processTitle.setText(eventProcess.getTitle());
		this.subEventType.setSelectedIndex(eventProcess.getEvtTypeCd() == 4 ? 1 : 0);
		this.immediType.setSelectionOnSingleMode(eventProcess.getImmediType() == 1 ? "랜덤" : "선착순"); 
		this.randomType.setSelectionOnSingleMode(eventProcess.getRandomType() == 1 ? "엑셀업로드" : "통계확률");
		if(subEventType.getSelectedIndex() == 0) {
			MaterialLink link = (MaterialLink)electType.getChildrenList().get(0);
			link.setVisible(false);
			 Winuserow.setVisible(false);
			 this.electType.setSelectionOnSingleMode("즉시선정");
			 this.selectedProcess.setElectType((int) electType.getSelectedValue());
			 immediElectLabel.setVisible(true);
				immediType.setVisible(true);
				if (immediType.getSelectedValue().equals(1)) {
					randomTypeLabel.setVisible(true);
					randomType.setVisible(true);
					notWinLabel.setVisible(true);
					notWin.setVisible(true);
					if (randomType.getSelectedValue().equals(0)) {
						xlsUploadRow.setVisible(false);
						percentRow.setVisible(true);
					} else {
						xlsUploadRow.setVisible(true);
						percentRow.setVisible(false);
					}
				}
		} else {
			MaterialLink link = (MaterialLink)electType.getChildrenList().get(0);
			link.setVisible(true);
			Winuserow.setVisible(true);
			this.selectedProcess.setElectType((int) electType.getSelectedValue());
			this.electType.setSelectionOnSingleMode(eventProcess.getElectType() == 1 ? "즉시선정" : "추후선정");
		}
		this.startDate.setValue(eventProcess.getStartDate() != null ? convertStringToDate(eventProcess.getStartDate()) : new Date());
		this.endDate.setValue(eventProcess.getEndDate() != null ? convertStringToDate(eventProcess.getEndDate()) : new Date());
		this.userPredictCnt.setValue(eventProcess.getUserPredictCnt() + "");
		this.announcePer.setValue(eventProcess.getAnnouncePer() + "");
		this.notWin.setSelectionOnSingleMode(eventProcess.isNotWin() ? "꽝있음" : "꽝없음");
		this.winconduse.setSelectionOnSingleMode(eventProcess.getWinCondUse() == 1 ? "설정" : "미설정");
		if (winconduse.getSelectedValue().equals(0)) {
			wincondvalueLabel.setVisible(false);
			wincondvalue.setVisible(false);
			wincondinfo.setVisible(false);
		} else {
			wincondvalueLabel.setVisible(true);
			wincondvalue.setVisible(true);
			wincondinfo.setVisible(true);
		}
		this.ExcelFileNm.setText(eventProcess.getExcelFileNm());
		this.wincondvalue.setText(eventProcess.getWinCondValue()+"");
		if (this.electType.getSelectedValue().equals(0)) {
			this.immediElectLabel.setVisible(false);
			this.immediType.setVisible(false);
			this.randomTypeLabel.setVisible(false);
			this.randomType.setVisible(false);
			this.notWinLabel.setVisible(false);
			this.notWin.setVisible(false);
			this.percentRow.setVisible(false);
			this.xlsUploadRow.setVisible(false);
		} else {
			this.immediElectLabel.setVisible(true);
			this.immediType.setVisible(true);
			this.randomTypeLabel.setVisible(true);
			this.randomType.setVisible(true);
			if (this.immediType.getSelectedValue().equals(1)) {
				this.notWinLabel.setVisible(true);
				this.notWin.setVisible(true);
				if (randomType.getSelectedValue().equals(1)) {
					this.percentRow.setVisible(false);
					this.xlsUploadRow.setVisible(true);
				} else {
					this.percentRow.setVisible(true);
					this.xlsUploadRow.setVisible(false);
				}
			}
		}
		this.refreshGiftPanel();
	}
	
	private void refreshGiftPanel() {
		this.giftContentArea.clear();
		
		this.giftContentArea.setBorderTop("1px #E0E0E0 solid");
		this.giftContentArea.setPaddingTop(20);
		this.giftContentArea.setMarginTop(30);
		
		
		
		this.selectedProcess.getGiftList().stream()
								  .filter(o -> !o.isDelete())
								  .sorted((o1, o2) -> o1.getOrder() > o2.getOrder() ? 1 : -1)
								  .forEach(this::addGiftContentPanel);
		
	}
	
	public void addGiftContentPanel(EventGift gift) {
		GiftContentPanel giftContent = new GiftContentPanel(gift,selectedProcess);
		giftContent.setIndex(gift.isNotWin() ? "꽝" : "경품 " + (gift.getOrder() + 1));
		giftContent.setGftId(gift.getGftId());
		giftContent.setEvtId(gift.getEvtId());
		giftContent.setSubEvtId(gift.getSubEvtId());
		giftContent.setTitle(gift.getTitle());
		giftContent.setNumbers(gift.getCount());
		giftContent.setOrder(gift.getOrder());
		giftContent.formEnabled(!gift.isNotWin());
		giftContent.setParent(giftContentArea);
		giftContentArea.add(giftContent);
		if (gift.isNotWin()) {
			this.notWinObj = gift;
		}
		
		EnumSet<EventStatus> statusSet = EnumSet.of(EventStatus.APPROVAL_WAIT);
		if (statusSet.contains(this.eventStatus)) {
			giftContent.formEnabled(false);
		}
	}
	
	@Override
	public JSONObject buildEventModel() {
		JSONObject model = new JSONObject();
		JSONArray processArr = new JSONArray();
		model.put("PROCESS", processArr);
		
		if (this.rullsetArray != null)
			model.put("RULLSET", this.rullsetArray);
		
		this.processList.forEach(process -> {
			processArr.set(processArr.size(), process.toJson());
		});
		return model;
	}

	@Override
	public void statusChangeProcess(EventStatus status) {
		this.eventStatus = status;
		this.formEnabled(this.host.getEditPossSet().contains(this.eventStatus));
	}
	
	private void formEnabled(boolean isEnable) {
		this.processTitle.setEnabled(isEnable);
		this.electType.setEnabled(isEnable);
		this.endDate.setEnabled(isEnable);
		this.immediType.setEnabled(isEnable);
		this.startDate.setEnabled(isEnable);
		this.subEventType.setEnabled(isEnable);
		this.announcePer.setEnabled(isEnable);
		this.userPredictCnt.setEnabled(isEnable);
		this.xlsUploadBtn.setEnabled(isEnable);
		this.xlsDownloadBtn.setEnabled(isEnable);
		this.randomType.setEnabled(isEnable);
		this.notWin.setEnabled(isEnable);
		this.giftContentArea.forEach(item -> {
			if (item instanceof GiftContentPanel) {
				GiftContentPanel gift = (GiftContentPanel) item;
				gift.formEnabled(isEnable);
			}
		});
	}

	public String getSubEvtId() {
		return subEvtId;
	}

	public void setSubEvtId(String subEvtId) {
		this.subEvtId = subEvtId;
	}

	public List<EventProcess> getProcessList() {
		return processList;
	}

	public EventProcessTreeItem getSelectedProcessTreeItem() {
		return selectedProcessTreeItem;
	}

	public void setSelectedProcessTreeItem(EventProcessTreeItem selectedProcessTreeItem) {
		this.selectedProcessTreeItem = selectedProcessTreeItem;
	}

	public EventGift getNotWinObj() {
		return notWinObj;
	}

	public void setNotWinObj(EventGift notWinObj) {
		this.notWinObj = notWinObj;
	}

	
}
