package kr.or.visitkorea.admin.client.manager.contents.recommand.composite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
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
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentBundle;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.StatusChangeEvent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RecommContentsBase extends AbtractRecommContents {

	private MaterialLabel cidLabel;
	private MaterialListValueBox<String> categoryList;
	private MaterialTextBox titleInput;
	private MaterialListValueBox<String> bigArea;
	private MaterialListValueBox<String> midArea;
	private SelectionPanel articleStatus;
	private SelectionPanel season;
	private SelectionPanel selectionMonth;
	private MaterialPanel panel;
	private MaterialLabel editDate;
	private MaterialLabel registDate;
	private MaterialLabel createUser;
	private MaterialLink cotLink;
	private String bigInteger;
	private ContentTableRow tableRow;
	private MaterialLabel treeTitleLabel;
	private MaterialListValueBox<String> articleCategoryone;
	private MaterialListValueBox<String> articleCategorytwo;
	private MaterialLabel FinaleditDate;
	private MaterialLink FinalSaveIcon;
	private SelectionPanel articleDivision;
	private boolean setupFlag = true;
	private Set<String> CategoryStringSet = null;
	private List<String> CategoryStringList = null;
	static {
		MaterialDesignBase.injectCss(RecommContentBundle.INSTANCE.contentCss());
	}

	public RecommContentsBase(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("기본 정보");
		buildContent();
		setupEvent();
	}

	private void setupEvent() {
	
		// title event.
		this.titleInput.addValueChangeHandler(event->{

			treeTitleLabel.setText(titleInput.getValue());
			MaterialLabel titleLabel = (MaterialLabel) tableRow.getChildrenList().get(3);
			titleLabel.setText(titleInput.getValue());
			
			JSONObject paramObj1 = new JSONObject();
			paramObj1.put("tbl", new JSONString("CONTENT_MASTER"));
			paramObj1.put("value", new JSONString(this.titleInput.getText()));
			paramObj1.put("colTitle", new JSONString("TITLE"));
			paramObj1.put("cotId", new JSONString(getCotId()));
			
			Func3<Object, String, Object> callback1 =  new Func3<Object, String, Object>() {
				
				@Override
				public void call(Object param1, String param2, Object param3) {}
				
			};
			
			invokeQuery("UPDATE_SINGLE_ROW", paramObj1, callback1);
			
			JSONObject paramObj2 = new JSONObject();
			paramObj2.put("tbl", new JSONString("ARTICLE_MASTER"));
			paramObj2.put("value", new JSONString(this.titleInput.getText()));
			paramObj2.put("colTitle", new JSONString("DISPLAY_TITLE"));
			paramObj2.put("cotId", new JSONString(getCotId()));
			
			Func3<Object, String, Object> callback2 =  new Func3<Object, String, Object>() {
				
				@Override
				public void call(Object param1, String param2, Object param3) {}
				
			};
			
			invokeQuery("UPDATE_SINGLE_ROW", paramObj2, callback2);

		});
		
		this.articleCategoryone.addValueChangeHandler(event->{
			if(articleCategoryone.getSelectedValue().equals("")) return;
			
			Func3<Object, String, Object> callback =  new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {}
			};
			JSONObject paramObj = new JSONObject();
			paramObj.put("tbl", new JSONString("ARTICLE_MASTER"));
			paramObj.put("value", new JSONString(articleCategoryone.getSelectedValue()));
			paramObj.put("colTitle", new JSONString("CONTENT_CATEGORY"));
			paramObj.put("cotId", new JSONString(getCotId()));
			invokeQuery("UPDATE_SINGLE_ROW", paramObj, callback);
			
			paramObj = new JSONObject();
			paramObj.put("tbl", new JSONString("CONTENT_MASTER"));
			paramObj.put("value", new JSONString((Integer.parseInt(articleCategoryone.getSelectedValue())+10000)+""));
			paramObj.put("colTitle", new JSONString("CONTENT_TYPE"));
			paramObj.put("cotId", new JSONString(getCotId()));
			invokeQuery("UPDATE_SINGLE_ROW", paramObj, callback);

			MaterialLabel titleLabel = (MaterialLabel) tableRow.getChildrenList().get(1);
			titleLabel.setText(articleCategoryone.getSelectedItemText());
		});
		
		this.articleCategorytwo.addValueChangeHandler(event->{
			if(articleCategorytwo.getSelectedValue().equals("")) return;
			
			Func3<Object, String, Object> callback =  new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {}
			};
			JSONObject paramObj = new JSONObject();
			paramObj.put("tbl", new JSONString("ARTICLE_MASTER"));
			paramObj.put("value", new JSONString(articleCategorytwo.getSelectedValue()));
			paramObj.put("colTitle", new JSONString("CONTENT_CATEGORY_TWO"));
			paramObj.put("cotId", new JSONString(getCotId()));
			invokeQuery("UPDATE_SINGLE_ROW", paramObj, callback);
			
		});
		
		
		this.bigArea.addValueChangeHandler(event->{
			
			midArea.clear();
			midArea.addItem("구군 선택");
			String bigAreaText = bigArea.getSelectedItemText();
			bigInteger = getKey(bigAreaText);
			
			if (bigInteger != null) {
				
				Map<String, Map<String, String>> midMap = (Map<String, Map<String, String>>) Registry.get("ADDRESS_BIG_WITH_MID_CODE");
				Map<String, String> selectMidMap = midMap.get(bigInteger);
				
				for (String key : selectMidMap.keySet()) {
					String midString = selectMidMap.get(key);
					String bigString = bigArea.getSelectedItemText();
					if (midString == null || midString.trim().equals("") || midString.equals(bigString)) {
					}else {
						midArea.addItem(selectMidMap.get(key));
						MaterialLabel titleLabel = (MaterialLabel) tableRow.getChildrenList().get(2);
						titleLabel.setText(bigString);
					}
				}
				
				JSONObject paramObj = new JSONObject();
				paramObj.put("tbl", new JSONString("ARTICLE_MASTER"));
				paramObj.put("value", new JSONNumber(Double.parseDouble(bigInteger)));
				paramObj.put("colTitle", new JSONString("AREA_CODE"));
				paramObj.put("cotId", new JSONString(getCotId()));
				
				Func3<Object, String, Object> callback =  new Func3<Object, String, Object>() {
					
					@Override
					public void call(Object param1, String param2, Object param3) {
						
					}
					
				};
				
				invokeQuery("UPDATE_SINGLE_ROW", paramObj, callback);

			}
		});

		// 구/군 event
		this.midArea.addValueChangeHandler(event->{
			
			Map<String, Map<String, String>> midMap = (Map<String, Map<String, String>>) Registry.get("ADDRESS_BIG_WITH_MID_CODE");
			Map<String, String> selectMidMap = midMap.get(bigInteger);
			
			if (selectMidMap.values().contains(this.midArea.getSelectedItemText())){
	
				String tgrKey = null;
				for (String midKey : selectMidMap.keySet()) {
					if (this.midArea.getSelectedValue().equals(selectMidMap.get(midKey))) {
						tgrKey = midKey;
						break;
					}
				}
				
				if (tgrKey != null) {
					
					JSONObject paramObj = new JSONObject();
					paramObj.put("tbl", new JSONString("ARTICLE_MASTER"));
					paramObj.put("value", new JSONNumber(Double.parseDouble(tgrKey)));
					paramObj.put("colTitle", new JSONString("SIGUGUN_CODE"));
					paramObj.put("cotId", new JSONString(getCotId()));
					
					Func3<Object, String, Object> callback =  new Func3<Object, String, Object>() {
						
						@Override
						public void call(Object param1, String param2, Object param3) {}
						
					};
					
					invokeQuery("UPDATE_SINGLE_ROW", paramObj, callback);
					
				}
			}
		});
		
		//articleStatus event
		this.articleStatus.addStatusChangeEvent(new StatusChangeEvent() {
			
			@Override
			public void fire(Object selecteBaseLink) {

				Integer selectedInt = (Integer)articleStatus.getSelectedValue();

				JSONObject paramObj = new JSONObject();
				paramObj.put("tbl", new JSONString("CONTENT_MASTER"));
				paramObj.put("colTitle", new JSONString("CONTENT_STATUS"));
				paramObj.put("cotId", new JSONString(getCotId()));
				paramObj.put("value", new JSONNumber(selectedInt));
				
				Func3<Object, String, Object> callback =  new Func3<Object, String, Object>() {

					@Override
					public void call(Object param1, String param2, Object param3) {
						MaterialLabel titleLabel = (MaterialLabel) tableRow.getChildrenList().get(5);
						titleLabel.setText(articleStatus.getSelectedText());
					}
					
				};
				
				invokeQuery("UPDATE_SINGLE_ROW", paramObj, callback);
				
				
			}
		});
		
		//season event
		this.season.addStatusChangeEvent(new StatusChangeEvent() {
			
			@Override
			public void fire(Object selecteBaseLink) {

				List<Object> objects = season.getSelectedValues();
				
				String valueString = "";
				
				for (Object obj : objects) {
					if (obj instanceof Integer) {
						valueString += (Integer)obj + "|"; 
					}else if (obj instanceof String) {
						valueString += (String)obj + "|"; 
					}
				}
				
				JSONObject paramObj = new JSONObject();
				paramObj.put("tbl", new JSONString("ARTICLE_MASTER"));
				paramObj.put("colTitle", new JSONString("SEASON"));
				paramObj.put("cotId", new JSONString(getCotId()));
				paramObj.put("value", new JSONString(valueString));
				
				Func3<Object, String, Object> callback =  new Func3<Object, String, Object>() {

					@Override
					public void call(Object param1, String param2, Object param3) {}
					
				};
				
				invokeQuery("UPDATE_SINGLE_ROW", paramObj, callback);
				
				
			}
		});

		//selectionMonth event
		this.selectionMonth.addStatusChangeEvent(new StatusChangeEvent() {
			
			@Override
			public void fire(Object selecteBaseLink) {

				List<Object> objects = selectionMonth.getSelectedValues();
				
				String valueString = "";
				
				for (Object obj : objects) {
					if (obj instanceof Integer) {
						valueString += (Integer)obj + "|"; 
					}else if (obj instanceof String) {
						valueString += (String)obj + "|"; 
					}
				}

				JSONObject paramObj = new JSONObject();
				paramObj.put("tbl", new JSONString("ARTICLE_MONTH"));
				paramObj.put("colTitle", new JSONString("MONTH"));
				paramObj.put("cotId", new JSONString(getCotId()));
				paramObj.put("value", new JSONString(valueString));
				
				Func3<Object, String, Object> callback =  new Func3<Object, String, Object>() {

					@Override
					public void call(Object param1, String param2, Object param3) {}
					
				};
				
				invokeQuery("PROCESS_MONTH_ROW", paramObj, callback);
				
				
			}
		});
		
	}

	private void buildContent() {
		
		panel =  new MaterialPanel();
		panel.setPadding(20);
		this.add(panel);
		
		//첫줄
		MaterialRow row1 = addRow(panel);
		addLabel(row1, "COT", TextAlign.CENTER, Color.GREY_LIGHTEN_3,"s2");
		
		this.cotLink = addLink(row1, "1212121212", TextAlign.LEFT, Color.WHITE, "s4");
		this.cotLink.setFontSize("0.9em");
		this.cotLink.addClickHandler(event->{
			String tgrUrl = (String) Registry.get("service.server") + "/detail/rem_detail.html?cotid=" + cotLink.getText();
			Window.open(tgrUrl,"","");
		});
		
		
		addLabel(row1, "구분", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> valueMap2 = new HashMap<String, Object>();
		valueMap2.put("일반기사", 0);
		valueMap2.put("테마기사", 1);
		this.articleDivision = addSelectionPanel(row1, "s4", TextAlign.LEFT, valueMap2, 5, 5, 8, "15px", true);
		this.articleDivision.addStatusChangeEvent(new StatusChangeEvent() {
			
			@Override
			public void fire(Object selecteBaseLink) {

				Integer selectedInt = (Integer)articleDivision.getSelectedValue();

				JSONObject paramObj = new JSONObject();
				paramObj.put("tbl", new JSONString("CONTENT_MASTER"));
				paramObj.put("colTitle", new JSONString("CONTENT_DIV"));
				paramObj.put("cotId", new JSONString(getCotId()));
				paramObj.put("value", new JSONNumber(selectedInt));
				
				Func3<Object, String, Object> callback =  new Func3<Object, String, Object>() {

					@Override
					public void call(Object param1, String param2, Object param3) {
						
					}
					
				};
				
				invokeQuery("UPDATE_SINGLE_ROW", paramObj, callback);
				
				
			}
		});
		
		
/*
		addLabel(row1, "CID", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.cidLabel = addLabel(row1, "1212121212", TextAlign.LEFT, Color.WHITE, "s4");
*/
		// 두번째줄
		MaterialRow row2 = addRow(panel);
		
		
		addLabel(row2, "1순위 분류", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.articleCategoryone = addList(
				row2, 
				new String[] {},
				"s4");
		
		addLabel(row2, "2순위 분류", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.articleCategorytwo = addList(
				row2, 
				new String[] {},
				"s4");
		
		
		// 세번째줄
		MaterialRow row3 = addRow(panel);
		
		addLabel(row3, "제목", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.titleInput = addInputText(row3, "", "s10");
		
		
		
		
		// 네번째줄
		MaterialRow row4 = addRow(panel);
		
		
		addLabel(row4, "지역", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.bigArea = addList(
				row4, 
				new String[] {},
				"s2");
		this.midArea = addList(
				row4, 
				new String[] {},
				"s2");
		
		addLabel(row4, "처리상태", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put("작업 미완료", 0);
		valueMap.put("작업 완료", 2);
		valueMap.put("비노출", 7);
		valueMap.put("삭제", 8);
		valueMap.put("작업 보류", 9);
		this.articleStatus = addSelectionPanel(row4, "s4", TextAlign.LEFT, valueMap, 1, 2, 1, "0.8em", true);
		
		
		Map<String, String> map = (Map<String, String>) Registry.get("ADDRESS_BIG_CODE");
		bigArea.addItem("시도 선택");
		for (String key : map.keySet()) {
			bigArea.addItem(map.get(key));
		}
		
		
		
		// 다섯번째줄
		MaterialRow row5 = addRow(panel);
		
		addLabel(row5, "계절", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> seasonMap = new HashMap<String, Object>();
		seasonMap.put("봄", 1);
		seasonMap.put("여름", 2);
		seasonMap.put("가을", 3);
		seasonMap.put("겨울", 4);
		this.season = addSelectionPanel(row5, "s4", TextAlign.LEFT, seasonMap, 5, 5, 8, "1.0em", false);
		
		addLabel(row5, "월 선택", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> monthMap = new HashMap<String, Object>();
		monthMap.put("01", 1);
		monthMap.put("02", 2);
		monthMap.put("03", 3);
		monthMap.put("04", 4);
		monthMap.put("05", 5);
		monthMap.put("06", 6);
		monthMap.put("07", 7);
		monthMap.put("08", 8);
		monthMap.put("09", 9);
		monthMap.put("10", 10);
		monthMap.put("11", 11);
		monthMap.put("12", 12);
		this.selectionMonth = addSelectionPanel(row5, "s4", TextAlign.LEFT, monthMap, 2, 4, 4, "0.8em", false);
		
		
		
		// 여섯번째줄
		MaterialRow row6 = addRow(panel);
		
		addLabel(row6, "최종 수정일", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.FinaleditDate = addLabel(row6, "", TextAlign.LEFT, Color.WHITE, "s3");
		
		FinalSaveIcon = addLink(row6,"",TextAlign.LEFT, Color.BLACK, "s1");
		FinalSaveIcon.setIconType(IconType.SAVE);
		FinalSaveIcon.getElement().getFirstChildElement().getStyle().setPaddingTop(8, Unit.PX);
		FinalSaveIcon.getElement().getFirstChildElement().getStyle().setFontSize(1.8, Unit.EM);
		FinalSaveIcon.getElement().getFirstChildElement().getStyle().setColor("black");
		FinalSaveIcon.setTooltip("최종 수정일 반영");
		FinalSaveIcon.addClickHandler(event->{
			ArticleFinalSave();
		});
		
		
		addLabel(row6, "수정일", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.editDate = addLabel(row6, "2017-11-05 17:55", TextAlign.LEFT, Color.WHITE, "s4");
		
		// 일곱번째줄
		MaterialRow row7 = addRow(panel);
		addLabel(row7, "등록일", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.registDate = addLabel(row7, "2017-11-05 17:55", TextAlign.LEFT, Color.WHITE, "s4");
		
		addLabel(row7, "최초작성자", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.createUser = addLabel(row7, "관리자", TextAlign.LEFT, Color.WHITE, "s4");
	}
	
	public void setReadOnly(boolean readFlag) {
		if (readFlag) {
			
		}else {
			
		}
	}
	
	private String getKey(String bigAreaText) {
		Map<String, String> map = (Map<String, String>) Registry.get("ADDRESS_BIG_CODE");
		for (String key : map.keySet()) {
			if (bigAreaText.equals(map.get(key))) {
				return key;
			}
		}
		return null;
	}
	
	public void loadData() {

		setupCategory();
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("ARTICLE_SELECT_WITH_COTID"));
		parameterJSON.put("cotId", new JSONString(this.getCotId()));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
	
			@Override
			public void call(Object param1, String param2, Object param3) {
	
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
// 
				
				if (processResult.equals("success")) {

					JSONObject bodyObj =  resultObj.get("body").isObject();
					JSONObject returnResultObj = bodyObj.get("result").isObject();

					JSONObject cotIdObject = null;
					if (returnResultObj.get("result") != null) cotIdObject = returnResultObj.get("result").isObject();
					JSONArray monthArrayObj = returnResultObj.get("selectMonth").isArray();
					
					if (cotIdObject.get("COT_ID") == null) {
						cotLink.setText("");
					}else {
						cotLink.setText(cotIdObject.get("COT_ID").isString().stringValue());
					}

					if (cotIdObject.get("TITLE") == null) {
						titleInput.setValue("");
					}else {
						titleInput.setValue(cotIdObject.get("TITLE").isString().stringValue());
					}
					if (cotIdObject.get("AREA_NAME") == null) {
						bigArea.setValueSelected("시도 선택", true);
						midArea.setValueSelected("구군 선택", true);
					}else {
						bigArea.setSelectedValue(cotIdObject.get("AREA_NAME").isString().stringValue());
						ValueChangeEvent.fire(bigArea, cotIdObject.get("AREA_NAME").isString().stringValue());
						if (cotIdObject.get("SIGUGUN_NAME") != null) midArea.setSelectedValue(cotIdObject.get("SIGUGUN_NAME").isString().stringValue());
					}
					if (cotIdObject.get("MODIFIED_DATE") == null) {
						editDate.setText("");
					}else {
						editDate.setText(cotIdObject.get("MODIFIED_DATE").isString().stringValue());
					}
					
					if (cotIdObject.get("FINAL_MODIFIED_DATE") == null) {
						FinaleditDate.setText("");
					}else {
						FinaleditDate.setText(cotIdObject.get("FINAL_MODIFIED_DATE").isString().stringValue());
					}
					
					if (cotIdObject.get("CREATE_DATE") == null) {
						registDate.setText("");
					}else {
						registDate.setText(cotIdObject.get("CREATE_DATE").isString().stringValue());
					}
					if (cotIdObject.get("USER_NAME") == null) {
						createUser.setText("");
					}else {
						createUser.setText(cotIdObject.get("USER_NAME").isString().stringValue());
					}
					articleStatus.reset();
					if (cotIdObject.get("CONTENT_STATUS") != null) {
						articleStatus.setSelectionOnSingleMode(cotIdObject.get("CONTENT_STATUS").isString().stringValue());
					}

					articleDivision.reset();
					if (cotIdObject.get("CONTENT_DIV") != null) {
						
						if(cotIdObject.get("CONTENT_DIV").isNumber().doubleValue() != 0)
							articleDivision.setSelectionOnSingleMode("테마기사");
						else
							articleDivision.setSelectionOnSingleMode("일반기사");
							
					}
					
					if (cotIdObject.get("CONTENT_CATEGORY") == null) {
						articleCategoryone.setValueSelected("", true);
					}else {
						String contentCategory = cotIdObject.get("CONTENT_CATEGORY").isNumber().doubleValue()+"";
						articleCategoryone.setValueSelected(contentCategory, true);
					}
					
					if (cotIdObject.get("CONTENT_CATEGORY_TWO") == null) {
						articleCategorytwo.setValueSelected("", true);
					}else {
						String contentCategory = cotIdObject.get("CONTENT_CATEGORY_TWO").isNumber().doubleValue()+"";
						articleCategorytwo.setValueSelected(contentCategory, true);
					}
					
					
					season.reset();
					JSONValue sValue = cotIdObject.get("SEASON");
					if (sValue != null) {
						String[] seasonArray = sValue.isString().stringValue().split("|");
						for (String seasonIndex: seasonArray) {
							String saveString = "";
							if (seasonIndex.equals("1")) {
								saveString = "봄";
							}else if (seasonIndex.equals("2")) {
								saveString = "여름";
							}else if (seasonIndex.equals("3")) {
								saveString = "가을";
							}else if (seasonIndex.equals("4")) {
								saveString = "겨울";
							}
							
							if(saveString != "")
							season.setSelectionOnMultipleMode(saveString, true);
						}
					}
					
					selectionMonth.reset();
					int monthSize = monthArrayObj.size();
					for (int i=0; i<monthSize; i++) {
						JSONObject tgrObj = monthArrayObj.get(i).isObject();
						selectionMonth.setSelectionOnMultipleMode(tgrObj.get("A_MONTH").isString().stringValue(), true);
					}
				}

				loading(false);
			}

		});
		
	}
	
	public void setTableRow(ContentTableRow tableRow) {
		this.tableRow = tableRow;
	}
	
	public static native String openWindow(String url)/*-{
	return $wnd.open(url,
	'target=_blank')
	}-*/;

	public void setTitleArea(MaterialLabel titleLabel) {
		this.treeTitleLabel = titleLabel;
	}
	
	private void ArticleFinalSave() {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("UPDATE_CONTENT_FINAL_SAVE"));
		parameterJSON.put("cotId", new JSONString(getCotId()));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (o1, o2, o3) -> {
			MaterialToast.fireToast("저장되었습니다. ", 3000);
		});
	}
	private void setupCategory() {
		if (setupFlag) {
			setupFlag = false;
			
			Map<String, Map<String,String>> map =(Map<String, Map<String,String>>) Registry.get("ARTICLE_CATEGORIES");
			
			CategoryStringSet = map.keySet();
			CategoryStringList = new ArrayList<String>(CategoryStringSet);
			this.articleCategorytwo.add("선택해 주세요.");
			for (String key : CategoryStringList) {
					this.articleCategoryone.addItem(key,map.get(key).get("VALUE"));
					this.articleCategorytwo.addItem(key,map.get(key).get("VALUE"));
			}
			
		}
	}
	
}
