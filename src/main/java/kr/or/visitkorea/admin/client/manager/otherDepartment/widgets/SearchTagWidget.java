package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SearchTagWidget extends MaterialPanel implements VisitKoreaSearch{
	private String ment;
	private String keyword;
	private MaterialTextBox searchKeywordTextBox;
	private MaterialLink addButton;
	private MaterialLabel mentLabel;
	private VisitKoreaListBody body;
	private String otdId;
	private double searchCount;
	private MaterialLabel seasonTitle;
	private MaterialExtentsWindow materialExtentsWindow;
	protected String searchTagId;
	private Map<String, Object> internalMap = new HashMap<String, Object>();
	private Object view;
	private ViewPanel targetView;
	
	public SearchTagWidget(String otdId, MaterialExtentsWindow materialExtentsWindow) {
		this.otdId = otdId;
		this.materialExtentsWindow = materialExtentsWindow;
		init();
	}
	
	public void init() {
		
		seasonTitle = new MaterialLabel();
		seasonTitle.setFontSize("2.0em");
		seasonTitle.setFontWeight(FontWeight.BOLDER);
		seasonTitle.setTextColor(Color.BLUE);
		seasonTitle.setText("여름");

		searchKeywordTextBox = new MaterialTextBox();
		searchKeywordTextBox.setPlaceholder("키워드를 입력해 주세요.");;
		searchKeywordTextBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		searchKeywordTextBox.setMarginTop(0);
		searchKeywordTextBox.setMarginBottom(0);
		
		addButton = new MaterialLink();
		addButton.setIconType(IconType.ADD);
		addButton.setDisplay(Display.INLINE_BLOCK);
		addButton.getElement().getFirstChildElement().getStyle().setLineHeight(46, Unit.PX);
		addButton.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);
		addButton.setTextAlign(TextAlign.CENTER);;
		addButton.setEnabled(false);

		MaterialRow row = new MaterialRow();
		row.setMarginBottom(0);
		
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid("s3");
		col1.add(seasonTitle);
		row.add(col1);

		MaterialColumn col2 = new MaterialColumn();
		col2.setGrid("s8");
		col2.add(searchKeywordTextBox);
		row.add(col2);

		MaterialColumn col3 = new MaterialColumn();
		col3.setVerticalAlign(VerticalAlign.MIDDLE);
		col3.setTextAlign(TextAlign.CENTER);
		col3.setGrid("s1");
		col3.add(addButton);
		row.add(col3);
		
		this.add(row);
		
		mentLabel = new MaterialLabel(" ");
		mentLabel.setTextAlign(TextAlign.LEFT);
		mentLabel.setTextColor(Color.RED_ACCENT_2);
		mentLabel.setHeight("22.4px");
		mentLabel.setMarginLeft(225);
		mentLabel.setFontWeight(FontWeight.BOLDER);
		this.add(mentLabel);
		
		setupEvent();
	}

	private void setupEvent() {
		
		searchKeywordTextBox.addKeyUpHandler(event->{
			int keyCode = event.getNativeKeyCode();
			if (keyCode == 13) {
				searchKeywordTextBox.setEnabled(false);
				addButton.setEnabled(false);
				mentLabel.setText("- 태그 [ "+searchKeywordTextBox.getText() + " ] 의 정보를 조회합니다.");
				loadData();
			}
		});
		
		addButton.addClickHandler(event->{
			
			double dispCount = searchCount;
			if (searchCount > 10) {
				dispCount = 10;
			}
			
			NumberFormat fmt = NumberFormat.getDecimalFormat();
			String targetCountString = fmt.format(searchCount);
			String selectedString = fmt.format(dispCount);
			String isAuto = "자동";

			List<VisitKoreaListCell> cellArray = new ArrayList<VisitKoreaListCell>();
			cellArray.add(new TagListImageCell(GWT.getHostPageBaseURL() + "images/small_log.png", Float.LEFT, "30px",  "50", 50, FontWeight.BOLDER, false));
			cellArray.add(new TagListLabelCell(searchKeywordTextBox.getText(), Float.LEFT, "269px",  "50", 50, FontWeight.BOLDER, true, TextAlign.LEFT));
			cellArray.add(new TagListLabelCell(targetCountString +"", Float.LEFT, "200px",  "50", 50, FontWeight.BOLDER, true, TextAlign.CENTER));
			cellArray.add(new TagListLabelCell(selectedString+"", Float.LEFT, "150px",  "50", 50, FontWeight.BOLDER, true, TextAlign.CENTER));
			cellArray.add(new TagListLabelCell(isAuto, Float.LEFT, "130px",  "50", 50, FontWeight.BOLDER, false, TextAlign.CENTER));
			
			TagListRow tagListRow = new TagListRow(cellArray);
			tagListRow.setTagName(searchKeywordTextBox.getText().trim());
			tagListRow.setCount(this.searchCount);
			tagListRow.setIsAutoCount(Integer.parseInt(selectedString));
			tagListRow.put("TAG_ID", this.searchTagId);
			tagListRow.setIsAuto(isAuto == "자동" ? 0 : 1);
			tagListRow.put("INDEX", 0);
			tagListRow.addDoubleClickHandler(ev->{
				
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("SELECTED_CONDITION", tagListRow);
				paramMap.put("VIEW_PANEL", targetView);
				materialExtentsWindow.openDialog(OtherDepartmentMainApplication.SEASON_CONTENT, paramMap, 1100);
			});
			
			JSONObject newJson = new JSONObject();
			newJson.put("SEASON_ID", new JSONString((String)this.getData("SEASON_ID")));
			newJson.put("IS_AUTO", new JSONNumber(0));
			newJson.put("CNT", new JSONNumber(0));
			newJson.put("SAT_ID", new JSONString(IDUtil.uuid()));
			newJson.put("TAG_ID", new JSONString(this.searchTagId));
			newJson.put("TAGS_CNT", new JSONNumber(this.searchCount));
			newJson.put("TAG_NAME", new JSONString(searchKeywordTextBox.getText().trim()));
			newJson.put("CHILDREN", new JSONArray());
		
			JSONArray tempArr = new JSONArray();
			tempArr.set(0, newJson);
		
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("RESULT_OBJECT", getData("RESULT"));
			resultMap.put("NEW_OBJECT", tempArr);
			
			this.body.addRow(tagListRow, resultMap);
			
		});
	}
	
	private JSONValue getSeasonId() {
		return new JSONString("SEASON_ID");
	}

	private void loadData() {
		
		JSONObject jObj = new JSONObject();
		jObj.put("cmd", new JSONString("SEARCH_SEASON_CONTENT"));
		jObj.put("keyword", new JSONString(searchKeywordTextBox.getText()));
		if (this.otdId != null) {
			jObj.put("otdId", new JSONString(this.otdId));
		}
		
		VisitKoreaBusiness.post("call", jObj.toString(), new Func3<Object, String, Object>() {
			
			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					
					searchCount = resultObj.get("body").isObject().get("result").isObject().get("count").isNumber().doubleValue();
					
					if (searchCount > 0) {
						NumberFormat fmt = NumberFormat.getDecimalFormat();
						String countString = fmt.format(searchCount);
						searchTagId = resultObj.get("body").isObject().get("result").isObject().get("tagId").isString().stringValue();
						mentLabel.setText("- 지정한 키워드 기준 총 " + countString + " 개의 콘텐츠가 검색 되었습니다.");
						addButton.setEnabled(true);
					}else {
						mentLabel.setText("- 지정한 키워드를 포함하는 컨텐츠를 찾을 수 없습니다.");
						addButton.setEnabled(false);
						searchTagId = null;
					}
				}
				
				searchKeywordTextBox.setSelectionRange(0, searchKeywordTextBox.getText().length());
				searchKeywordTextBox.setFocus(true);
				searchKeywordTextBox.setEnabled(true);
			}
		});
	}
	
	public void executeBusiness(JSONObject parameterJSON) {
	}
	
	@Override
	public void setBody(VisitKoreaListBody searchBody) {
		this.body = searchBody;
	}
	
	public void setSeasonTile(String seasonTitle) {
		this.seasonTitle.setText(seasonTitle);
		this.mentLabel.setText("");
	}
	
	public void setHeaderVisible(boolean visible) {
		this.seasonTitle.setVisibility(Visibility.HIDDEN);
		this.mentLabel.setText("");
	}
	
	public void setOtdId(String OTD_ID) {
		this.otdId = OTD_ID;
	}
	
	public String getSeasonTitle() {
		return seasonTitle.getText(); 
	}
	
	public void setData(String key, Object value) {
		this.internalMap.put(key, value);
	}
	
	public Object getData(String key) {
		return this.internalMap.get(key);
	}

	public void setView(ViewPanel targetView) {
		this.targetView = targetView;
	}
}
