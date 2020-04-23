package kr.or.visitkorea.admin.client.manager.contents.database.composite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialListValueBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentsBase extends AbtractContents {

	private MaterialLabel cidLabel;
	private MaterialTextBox titleInput;
	private MaterialListValueBox<String> bigArea;
	private MaterialListValueBox<String> midArea;
	private SelectionPanel articleStatus;
	private MaterialPanel panel;
	private MaterialLabel editDate;
	private MaterialLabel registDate;
	private MaterialLabel createUser;
	private MaterialListValueBox<String> midCategory;
	private MaterialListValueBox<String> bigCategory;
	private MaterialListValueBox<String> smlCategory;
	private MaterialInput startDate;
	private MaterialInput endDate;
	private MaterialIcon saveIcon;
	private MaterialIcon editIcon;
	
	private String bigCategoryKey;
	private String midCategoryKey;
	private String smlCategoryKey;
	
	private Set<String> bigKeyStringSet = null;
	private List<String> bigKeyStringList = null;
	
	private Set<String> midKeyStringSet = null;
	private List<String> midKeyStringList = null;
	
	private Set<String> smlKeyStringSet = null;
	private List<String> smlKeyStringList = null;

	private String title;
	private double areaCode;
	private String areaName;
	private double sigugunCode;
	private String sigugunName;
	private String contentStatus;
	private String modifiedDate;
	private String createDate;
	private String cid;
	private String creator;
	private String cat1;
	private String cat2;
	private String cat3;
	private String authCode;
	protected JSONArray festivalObject;
	private MaterialRow row5;
	private boolean setupFlag = true;
	private JSONObject LoadDataResultObj;
	private MaterialTextBox AuthCodeBox;
	private MaterialButton CheckButton;
	private boolean AuthCodecheck;
	private MaterialIcon CheckIcon;
	private MaterialLabel AuthCodeLabel;
	
	public ContentsBase(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("기본 정보");
		buildContent();
		SettingBaseIcon();
	}

	private void buildContent() {
		
		panel =  new MaterialPanel();
		panel.setPadding(20);
		this.add(panel);
		
		//첫 줄
		MaterialRow row1 = addRow(panel);
		addLabel(row1, "CID", TextAlign.CENTER, Color.GREY_LIGHTEN_3,"s2");
		this.cidLabel = addLabel(row1, "1212121212", TextAlign.LEFT, Color.WHITE, "s4");
		
		addLabel(row1, "지역", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.bigArea = addList(
				row1, 
				new String[] {},
				"s2", "DATABASE_MASTER", "AREA_CODE");
		this.midArea = addList(
				row1, 
				new String[] {},
				"s2", "DATABASE_MASTER", "SIGUGUN_CODE");
		
		Map<String, String> map = (Map<String, String>) Registry.get("ADDRESS_BIG_CODE");
		bigArea.addItem("시도 선택");
		for (String key : map.keySet()) {
			bigArea.addItem(map.get(key));
		}
		
		this.bigArea.addValueChangeHandler(event->{
			
			midArea.clear();
			midArea.addItem("구군 선택");
			String bigAreaText = bigArea.getSelectedItemText();
			String bigInteger = getBigKey(bigAreaText);
			if (bigInteger != null) {
				
				Map<String, Map<String, String>> midMap = (Map<String, Map<String, String>>) Registry.get("ADDRESS_BIG_WITH_MID_CODE");
				Map<String, String> selectMidMap = midMap.get(bigInteger);

				midArea.getElement().setPropertyString("BIG", bigInteger);
				
				for (String key : selectMidMap.keySet()) {
					String midString = selectMidMap.get(key);
					String bigString = bigArea.getSelectedItemText();
					if (midString == null || midString.trim().equals("") || midString.equals(bigString)) {
					}else {
						midArea.addItem(selectMidMap.get(key));
					}
				}
				
				areaCode = Double.parseDouble(bigInteger);
				
			}
			
		});

		this.midArea.addValueChangeHandler(event->{
			
			Map<String, Map<String, String>> midMap = (Map<String, Map<String, String>>) Registry.get("ADDRESS_BIG_WITH_MID_CODE");
			
			String area = midArea.getElement().getPropertyString("BIG");
			midArea.getSelectedIndex();
			
			sigugunCode = Double.parseDouble(new ArrayList<String>(midMap.get(area).keySet()).get(midArea.getSelectedIndex()));
			
		});

		// 두번 째 줄
		MaterialRow row2 = addRow(panel);
		addLabel(row2, "제목", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.titleInput = addInputText(row2, "", "s10", null, null);

		// 세번 째 줄
		MaterialRow row3 = addRow(panel);
		addLabel(row3, "카테고리", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.bigCategory = addList(
				row3, 
				new String[] {},
				"s4", "DATABASE_MASTER", "CAT1");
		
		this.midCategory = addList(
				row3, 
				new String[] {},
				"s3", "DATABASE_MASTER", "CAT2");
		
		this.smlCategory = addList(
				row3, 
				new String[] {},
				"s3", "DATABASE_MASTER", "CAT3");
		
		// 네번 째 줄
		MaterialRow row4 = addRow(panel);
		addLabel(row4, "처리상태", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put("작업 미완료", 0);
		valueMap.put("작업 완료", 2);
		valueMap.put("비노출", 7);
		valueMap.put("삭제", 8);
		valueMap.put("작업 보류", 9);
		valueMap.put("상태 미등록", 1);
		this.articleStatus = addSelectionPanel(row4, "s10", TextAlign.LEFT, valueMap, 1, 2, 1, true, null, null);

		// 여섯번 째 줄
		row5 = addRow(panel);
		addLabel(row5, "축제 시작일", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		//this.startDate = addDatePicker(row5, TextAlign.LEFT, Color.WHITE, "s4");
		this.startDate = addDatePicker(row5, TextAlign.LEFT, Color.WHITE, "s4", null, null);
		
		addLabel(row5, "축제 종료일", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.endDate = addDatePicker(row5, TextAlign.LEFT, Color.WHITE, "s4", null, null);
		
		// 여섯번 째 줄
		MaterialRow row6 = addRow(panel);
		addLabel(row6, "수정일", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.editDate = addLabel(row6, "2017-11-05 17:55", TextAlign.LEFT, Color.WHITE, "s4");
		addLabel(row6, "등록일", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.registDate = addLabel(row6, "2017-11-05 17:55", TextAlign.LEFT, Color.WHITE, "s4");
		
		// 일곱번 째 줄
		MaterialRow row7 = addRow(panel);
		addLabel(row7, "최초작성자", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.createUser = addLabel(row7, "관리자", TextAlign.LEFT, Color.WHITE, "s4");
		
		// 여덟번 째 줄
		AuthCodeLabel = addLabel(row7, "인증번호", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		AuthCodeBox = addInputText(row7, "", "s3", null, null);
		AuthCodeBox.setEnabled(false);
		AuthCodeBox.addKeyUpHandler(handler -> {
			if(handler.isAnyModifierKeyDown() || handler.isLeftArrow() || handler.isRightArrow()) {
				return;
			}
			
			String value = AuthCodeBox.getValue();
			String result = convertAuthCodeToForm(value);
			
			AuthCodeBox.setValue(result.toUpperCase());
		});
		AuthCodeBox.addFocusHandler(event->{
			if(AuthCodecheck == true) {
				AuthCodeBox.setEnabled(false);
				getWindow().confirm("","인증번호를 변경할 경우 다시 중복확인을 진행해야합니다. 정말 변경하시겠습니까?",525, e->{
					if(((MaterialButton)e.getSource()).getId().equals("yes")) {
						AuthCodecheck = false;
						CheckIcon.setTextColor(Color.RED_ACCENT_4);
						AuthCodeBox.setEnabled(true);
					} else {
						AuthCodeBox.setEnabled(true);
					}
				});
			}
		});
		AuthCodeBox.setMaxLength(20);
		
		CheckButton = addButton(row7, "중복확인", TextAlign.CENTER, Color.BLUE_ACCENT_2, "s1",0);
		CheckButton.setBorderRadius("0px");
		CheckButton.setPaddingLeft(12);
		CheckButton.setPaddingRight(12);
		CheckButton.setEnabled(false);
		CheckButton.addClickHandler(event->{
			
			if(AuthCodecheck == true && AuthCodeBox.getValue() != "")
				MaterialToast.fireToast("중복체크가 완료된 인증번호입니다.");
			else if(AuthCodeBox.getValue() != "")
				AuthCodecheck();
			else
				MaterialToast.fireToast("인증번호를 입력해주세요");
		});

		CheckIcon = new MaterialIcon(IconType.CHECK);
		CheckIcon.setFontSize("30px");
		CheckIcon.setTextAlign(TextAlign.LEFT);
		CheckIcon.setPadding(9);
		CheckIcon.setLayoutPosition(Position.ABSOLUTE);
		CheckIcon.setRight(0);
		CheckIcon.setTextColor(Color.RED_ACCENT_4);
		
		AuthCodeBox.add(CheckIcon);
		
		CheckIcon.setVisible(false);
	}
	
	private void AuthCodecheck() {
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("authCode", new JSONString(AuthCodeBox.getValue()));
		parameterJSON.put("cmd", new JSONString("VALIDATE_AUTH_CODE"));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if(processResult.equals("success")){
					MaterialToast.fireToast("중복확인이 완료되었습니다.");
					CheckIcon.setTextColor(Color.GREEN_ACCENT_4);
					AuthCodecheck = true;
				} else {
					CheckIcon.setTextColor(Color.RED_ACCENT_4);
					getWindow().alert("인증번호 형식이 맞지 않거나, 중복된 인증번호가 존재합니다.");
				}
			}
		});
	}
	
	private String convertAuthCodeToForm(String value) {
		String valueRemoveHyphen = value.replaceAll("-", "");
		valueRemoveHyphen = valueRemoveHyphen.replaceAll(" ", "");
		
		if(valueRemoveHyphen.length() > 10) {
			valueRemoveHyphen = valueRemoveHyphen.substring(0, 10);
		}
		
		if(value.length() != 0 && value.charAt(value.length() - 1) != '-') {
			for(int i=1; i<=value.length(); i++) {
				if(i == 3 || i == 6 || i == 8 || i == 10) {
				    String front = valueRemoveHyphen.substring(0, i-1);
				    String back = valueRemoveHyphen.substring(i-1, valueRemoveHyphen.length());
					
				    valueRemoveHyphen = front + "-" + back;
				}
			}
		} else {
			for(int i=1; i<value.length()-1; i++) {
				if(value.length() >= i) {
					if(i == 2 || i == 5 || i == 7 || i == 9) {
						String front = valueRemoveHyphen.substring(0, i);
						String back = valueRemoveHyphen.substring(i, valueRemoveHyphen.length());
						
						valueRemoveHyphen = front + "-" + back;
					}
				}
			}
		}
		
		return valueRemoveHyphen;
	}

	public void setReadOnly(boolean readFlag) {
		if (readFlag) {
			
		}else {
			
		}
	}
	
	public void loadData() {
		LoadDataResultObj = null;
		setupCategory();
		SettingVisible(false);
		

		if(!getOtdId().equals("456a84d1-84c4-11e8-8165-020027310001")) {
			AuthCodeBox.setVisible(false);
			AuthCodeLabel.setVisible(false);
			CheckIcon.setVisible(false);
			CheckButton.setVisible(false);
		} else {
			AuthCodeBox.setVisible(true);
			AuthCodeLabel.setVisible(true);
			CheckIcon.setVisible(true);
			CheckButton.setVisible(true);
		}
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cotId", new JSONString(this.getCotId()));
		this.cidLabel.addClickHandler(ee->{
			String tgrUrl = (String) Registry.get("service.server")  + "/detail/detail_view.html?cotid="+this.getCotId();
//			Registry.openPreview(new MaterialIcon(IconType.WEB), tgrUrl);
			Window.open(tgrUrl,"","");
		});

		Func3<Object, String, Object> callBackFunction = new Func3<Object, String, Object>(){

			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {

					JSONObject bodyObj =  resultObj.get("body").isObject();
					LoadDataResultObj = bodyObj.get("result").isObject();

					title = LoadDataResultObj.get("TITLE").isString().stringValue();
					if (LoadDataResultObj.get("AREA_CODE") != null) areaCode = LoadDataResultObj.get("AREA_CODE").isNumber().doubleValue();
					if (LoadDataResultObj.get("AREA_NAME") != null) areaName = LoadDataResultObj.get("AREA_NAME").isString().stringValue();
					if (LoadDataResultObj.get("SIGUGUN_CODE") != null) sigugunCode = LoadDataResultObj.get("SIGUGUN_CODE").isNumber().doubleValue();
					if (LoadDataResultObj.get("SIGUGUN_NAME") != null) sigugunName = LoadDataResultObj.get("SIGUGUN_NAME").isString().stringValue();
					contentStatus = LoadDataResultObj.get("CONTENT_STATUS").isString().stringValue();
					modifiedDate = LoadDataResultObj.get("MODIFIED_DATE").isString().stringValue();
					createDate = LoadDataResultObj.get("CREATE_DATE").isString().stringValue();
					cid = LoadDataResultObj.get("CID").isString().stringValue();
					creator = LoadDataResultObj.get("USER_NAME").isString().stringValue();
					if (LoadDataResultObj.get("CAT1") != null) cat1 = LoadDataResultObj.get("CAT1").isString().stringValue();
					if (LoadDataResultObj.get("CAT2") != null) cat2 = LoadDataResultObj.get("CAT2").isString().stringValue();
					if (LoadDataResultObj.get("CAT3") != null) cat3 = LoadDataResultObj.get("CAT3").isString().stringValue();
					if (LoadDataResultObj.get("AUTH_CODE") != null) authCode = LoadDataResultObj.get("AUTH_CODE").isString().stringValue();
					festivalObject = LoadDataResultObj.get("festivalIntro").isArray();
					setuplReturnObj(LoadDataResultObj);
					
				}				
				
			}
			
		};
		
		invokeQuery("GET_BASE_WITH_COTID", parameterJSON, callBackFunction);
		
	}

	protected void setuplReturnObj(JSONObject returnResultObj) {
		
		this.cidLabel.setText(this.cid);
		this.bigArea.setSelectedValue(this.areaName);
		ValueChangeEvent.fire(bigArea, bigArea.getSelectedItemText());
		this.midArea.setSelectedValue(this.sigugunName);
		this.editDate.setText(modifiedDate);
		this.registDate.setText(createDate);
		this.createUser.setText(creator);
		this.titleInput.setText(title.replaceAll("\'", "'"));
		this.AuthCodeBox.setValue("");
		this.AuthCodeBox.setText(authCode);
		if(authCode != "") {
			AuthCodecheck = true;
			CheckIcon.setTextColor(Color.GREEN_ACCENT_4);
		}
			
		this.articleStatus.reset();
		this.articleStatus.setSelectionOnSingleMode(contentStatus+"");
		if (this.festivalObject.size() > 0 && this.festivalObject.get(0).isObject().keySet().size() > 0) {
			row5.setVisible(true);
			JSONObject festivalInfo = this.festivalObject.get(0).isObject();
			
			String startDt = festivalInfo.get("EVENT_START_DATE").isString().stringValue();
			if (startDt != null && startDt.length() > 0) {
				String dt = startDt.substring(0,  4) + "-" + startDt.substring(4, 6) + "-" + startDt.substring(6, 8);
				this.startDate.setValue(dt, true);
			}

			String endDt = festivalInfo.get("EVENT_END_DATE").isString().stringValue();
			if (endDt != null && endDt.length() > 0) { 
				String dt = endDt.substring(0,  4) + "-" + endDt.substring(4, 6) + "-" + endDt.substring(6, 8);
				this.endDate.setValue(dt, true);
			}
		}else {
			row5.setVisible(false);
		}
		
		
		if (bigKeyStringList.indexOf(cat1) > -1) {
			
			this.bigCategory.setSelectedIndex(bigKeyStringList.indexOf(cat1));
			String bigItemText = bigCategory.getSelectedItemText();
			ValueChangeEvent.fire(bigCategory, bigItemText);
			
			this.midCategory.setSelectedIndex(midKeyStringList.indexOf(cat2.substring(3)));
			String midItemText = this.midCategory.getSelectedItemText();
			ValueChangeEvent.fire(midCategory, midItemText);
	
			if (smlKeyStringList != null && cat3 != null && cat3.substring(5,7) != null) {
				String smlItemText = this.midCategory.getSelectedItemText();
				
				if (smlKeyStringList.indexOf(cat3.substring(5,7)) > -1) {
					if(cat1.equals("C01")){
						this.smlCategory.setSelectedIndex(1);
					} else {
						this.smlCategory.setSelectedIndex(smlKeyStringList.indexOf(cat3.substring(5,7)));
					}
					ValueChangeEvent.fire(smlCategory, smlItemText);
				}
			}
			
		}
	}

	private void setupCategory() {
		
		if (setupFlag) {
		
			setupFlag = false;
			
			Map<String, Map<String, Map<String, Map<String, String>>>> bigMap = (Map<String, Map<String, Map<String, Map<String, String>>>>) Registry.get("CATEGORIES");
			
			bigKeyStringSet = bigMap.keySet();
			bigKeyStringList = new ArrayList<String>(bigKeyStringSet);
			
			for (String key : bigKeyStringList) {
				this.bigCategory.add(bigMap.get(key).get("00").get("00").get("00"));
			}
			
			this.bigCategory.addValueChangeHandler(event->{
				
				this.midCategory.clear();
				this.smlCategory.clear();
	
				int selectedIndex = this.bigCategory.getSelectedIndex();
				this.bigCategoryKey = bigKeyStringList.get(selectedIndex);
				
				midKeyStringSet = bigMap.get(bigCategoryKey).keySet();
				midKeyStringList = new ArrayList<String>(midKeyStringSet);
			
				for (String key : midKeyStringList) {
					if (key.equals("00")) {
						this.midCategory.add("선택해 주세요.");
					}else {
						this.midCategory.add(bigMap.get(bigCategoryKey).get(key).get("00").get("00"));
					}
				}
			});
			
			this.midCategory.addValueChangeHandler(event->{
				
				this.smlCategory.clear();
				
				int selectedIndex = this.midCategory.getSelectedIndex();
				if (selectedIndex > -1) {
					this.midCategoryKey = midKeyStringList.get(selectedIndex);
					
					smlKeyStringSet = bigMap.get(bigCategoryKey).get(midCategoryKey).keySet();
					smlKeyStringList = new ArrayList<String>(smlKeyStringSet);
					
					for (String key : smlKeyStringList) {
						if (key.equals("00")) {
							if(bigCategoryKey.equals("C01")) {
								
								if(midCategoryKey.equals("00"))
									this.smlCategory.add("선택해 주세요.");
								else {
									this.smlCategory.add("선택해 주세요.");									
									this.smlCategory.add(bigMap.get(bigCategoryKey).get(midCategoryKey).get(key).get("01"));
								}
							} else {
								this.smlCategory.add("선택해 주세요.");
							}
						}else {
								this.smlCategory.add(bigMap.get(bigCategoryKey).get(midCategoryKey).get(key).get("00"));
						}
					}
					this.smlCategoryKey = "00";
				}
			});
			
			this.smlCategory.addValueChangeHandler(event->{
				int selectedIndex = this.smlCategory.getSelectedIndex();
				
				if(bigCategoryKey.equals("C01")) {
					if(!midCategoryKey.equals("00")) {
						if(selectedIndex == 0) 
							smlCategoryKey = "00";
						else
							this.smlCategoryKey = smlKeyStringList.get(selectedIndex-1);
					}
				} else {
					this.smlCategoryKey = smlKeyStringList.get(selectedIndex);
				}
			});
			
		}
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
		bigArea.setEnabled(Visible);
		midArea.setEnabled(Visible);
		titleInput.setEnabled(Visible);
		bigCategory.setEnabled(Visible);
		midCategory.setEnabled(Visible);
		smlCategory.setEnabled(Visible);
		articleStatus.setEnabled(Visible);
		AuthCodeBox.setEnabled(Visible);
		CheckButton.setEnabled(Visible);
	}

	public void SaveBase() {
		
		JSONObject paramObj = new JSONObject();
		paramObj.put("cmd", new JSONString("UPDATE_DATABASE_MASTER"));
		paramObj.put("mode", new JSONString("Base"));
		paramObj.put("title", new JSONString(titleInput.getText()));
		paramObj.put("cotId", new JSONString(getCotId()));
		paramObj.put("bigcategory", new JSONString(bigCategoryKey));
		paramObj.put("midcategory", new JSONString(bigCategoryKey+midCategoryKey));
		String subcategoryKey = "00";
		if(bigCategoryKey.equals("C01") && !midCategoryKey.equals("00")) subcategoryKey = "01";
		
		paramObj.put("smlcategory", new JSONString(bigCategoryKey+midCategoryKey+smlCategoryKey+subcategoryKey));
		paramObj.put("bigarea", new JSONNumber(areaCode));
		paramObj.put("midarea", new JSONNumber(sigugunCode));
		Integer selectedInt = (Integer)articleStatus.getSelectedValue();
		paramObj.put("status", new JSONNumber(selectedInt));
		
		if(startDate.getValue().length() > 0 || endDate.getValue().length() > 0) {
			paramObj.put("startdate", new JSONString(startDate.getValue().replaceAll("-", "")));
			paramObj.put("enddate", new JSONString(endDate.getValue().replaceAll("-", "")));
		}
		paramObj.put("authcode", new JSONString(AuthCodeBox.getValue()));
		
		VisitKoreaBusiness.post("call", paramObj.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if(processResult.equals("success")){
					
					LoadDataResultObj.put("TITLE",new JSONString(titleInput.getText()));
					LoadDataResultObj.put("CONTENT_STATUS",new JSONString(articleStatus.getSelectedText()));
					LoadDataResultObj.put("CAT1",new JSONString(bigCategoryKey));
					LoadDataResultObj.put("CAT2",new JSONString(bigCategoryKey+midCategoryKey));
					String subcategoryKey = "00";
					if(bigCategoryKey.equals("C01") && !midCategoryKey.equals("00")) subcategoryKey = "01";
					LoadDataResultObj.put("CAT3",new JSONString(bigCategoryKey+midCategoryKey+smlCategoryKey+subcategoryKey));
					LoadDataResultObj.put("AREA_CODE", new JSONNumber(areaCode));
					LoadDataResultObj.put("SIGUGUN_CODE", new JSONNumber(sigugunCode));
					LoadDataResultObj.put("AUTH_CODE", new JSONString(AuthCodeBox.getValue()));
					MaterialToast.fireToast("내용 변경에 성공하였습니다.");
					SettingVisible(false);
				}
			}
		});
		
	}
	
	
	public void UpdateCheck() {
		if(midCategoryKey.equals("00")) {
			MaterialToast.fireToast("두번째 카테고리를 선택해주세요.");
			return;
		}
		
		if(AuthCodecheck == false && AuthCodeBox.getText() != "") {
			MaterialToast.fireToast("인증번호 중복체크를 해주세요.");
			return;
		}
		
		String BeforeTitle = LoadDataResultObj.get("TITLE").isString().stringValue().replaceAll("\'", "'");
		Double BeforeAreacode = LoadDataResultObj.get("AREA_CODE") != null ? LoadDataResultObj.get("AREA_CODE").isNumber().doubleValue() : 0;
		Double BeforeSiguguncode = LoadDataResultObj.get("SIGUGUN_CODE") != null ? LoadDataResultObj.get("SIGUGUN_CODE").isNumber().doubleValue() : 0;
		String BeforeStatus = LoadDataResultObj.get("CONTENT_STATUS").isString().stringValue();
		String Beforecat1 = LoadDataResultObj.get("CAT1") != null ? cat1 = LoadDataResultObj.get("CAT1").isString().stringValue() : "";
		String Beforecat2 = LoadDataResultObj.get("CAT2") != null ? cat2 = LoadDataResultObj.get("CAT2").isString().stringValue() : "";
		String Beforecat3 = LoadDataResultObj.get("CAT3") != null ? cat3 = LoadDataResultObj.get("CAT3").isString().stringValue() : "";
		String BeforeauthCode = LoadDataResultObj.get("AUTH_CODE") != null ? LoadDataResultObj.get("AUTH_CODE").isString().stringValue() : "";
		
		String BeforestartDate = "";
		String BeforeendDate = "";
		if (this.festivalObject.size() > 0 && this.festivalObject.get(0).isObject().keySet().size() > 0) {
			JSONObject festivalInfo = this.festivalObject.get(0).isObject();
			String startDt = festivalInfo.get("EVENT_START_DATE").isString().stringValue();
			if (startDt != null && startDt.length() > 0) {
				String dt = startDt.substring(0,  4) + "-" + startDt.substring(4, 6) + "-" + startDt.substring(6, 8);
				BeforestartDate = dt.replaceAll("-", "");
			}

			String endDt = festivalInfo.get("EVENT_END_DATE").isString().stringValue();
			if (endDt != null && endDt.length() > 0) { 
				String dt = endDt.substring(0,  4) + "-" + endDt.substring(4, 6) + "-" + endDt.substring(6, 8);
				BeforeendDate =dt.replaceAll("-", "");
			}
			
		}

		String subcategoryKey = "00";
		if(bigCategoryKey.equals("C01") && !midCategoryKey.equals("00")) subcategoryKey = "01";
		
		if(BeforeTitle != titleInput.getText()
			|| BeforeAreacode != areaCode
			|| BeforeSiguguncode != sigugunCode
			|| BeforeStatus != articleStatus.getSelectedText()
			|| BeforestartDate != startDate.getValue().replaceAll("-", "")
			|| BeforeendDate != endDate.getValue().replaceAll("-", "")
			|| Beforecat1 != bigCategoryKey
			|| Beforecat2 != bigCategoryKey+midCategoryKey
			|| Beforecat3 != bigCategoryKey+midCategoryKey+smlCategoryKey+subcategoryKey
			|| BeforeauthCode != AuthCodeBox.getValue()
			) {
			getWindow().confirm("내용이 변경되었습니다. 실제 데이터에 반영하시겠습니까?", event->{
				if (event.getSource().toString().contains("yes")) {
					SaveBase();
				}
			});
		} else
			SettingVisible(false);

	}
}
