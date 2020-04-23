package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
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
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SearchTagWidgetForTabMenu extends MaterialPanel implements VisitKoreaSearch{

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

	private MaterialLink saveButton;

	private String odmId;

	public SearchTagWidgetForTabMenu(String otdId, MaterialExtentsWindow materialExtentsWindow) {
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
		addButton.getElement().getFirstChildElement().getStyle().setMarginRight(10, Unit.PX);
		addButton.setTextAlign(TextAlign.CENTER);;
		addButton.setEnabled(false);
		
		saveButton = new MaterialLink();
		saveButton.setIconType(IconType.SAVE);
		saveButton.setDisplay(Display.INLINE_BLOCK);
		saveButton.getElement().getFirstChildElement().getStyle().setLineHeight(46, Unit.PX);
		saveButton.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);
		saveButton.setTextAlign(TextAlign.CENTER);;
		saveButton.setEnabled(true);

		MaterialRow row = new MaterialRow();
		row.setMarginBottom(0);
		
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid("s0");
		col1.add(seasonTitle);
		row.add(col1);

		MaterialColumn col2 = new MaterialColumn();
		col2.setGrid("s8");
		col2.add(searchKeywordTextBox);
		row.add(col2);

		MaterialColumn col3 = new MaterialColumn();
		col3.setVerticalAlign(VerticalAlign.MIDDLE);
		col3.setTextAlign(TextAlign.CENTER);
		col3.setGrid("s2");
		col3.add(addButton);
		col3.add(saveButton);
		row.add(col3);
		
		this.add(row);
		
		mentLabel = new MaterialLabel(" ");
		mentLabel.setTextAlign(TextAlign.LEFT);
		mentLabel.setTextColor(Color.RED_ACCENT_2);
		mentLabel.setHeight("22.4px");
		mentLabel.setMarginLeft(100);
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
			
			
			List<VisitKoreaListCell> rowInfoList = new ArrayList<VisitKoreaListCell>();
			rowInfoList.add(new TagListLabelCell(searchKeywordTextBox.getText(), Float.LEFT, "50%",  "50", 50, FontWeight.BOLDER, true, TextAlign.CENTER));
			rowInfoList.add(new TagListLabelCell(targetCountString +"", Float.LEFT, "50%",  "50", 50, FontWeight.BOLDER, true, TextAlign.CENTER));
			
			TagListRow tagListRow = new TagListRow(rowInfoList);
			tagListRow.setTagName(searchKeywordTextBox.getText().trim());
			tagListRow.setCount(this.searchCount);
			
			this.body.addRow(tagListRow);
			
		});
		
		saveButton.addClickHandler(event->{
			
			String appendQuery = "";
			List<Widget> rows = this.body.getRows();
			int orderIndex = 0;
			for (Widget widget : rows) {
				TagListRow tagListRow = (TagListRow)widget;
				TagListLabelCell labelCell = (TagListLabelCell)tagListRow.getCells().get(0);
				String tagKeyWord = labelCell.getText();
				appendQuery += ",('"+odmId+"','"+tagKeyWord+"', "+orderIndex+")";
				orderIndex++;
			}
			
			appendQuery = appendQuery.substring(1);
			
			JSONObject jObj = new JSONObject();
			jObj.put("cmd", new JSONString("INSERT_ALL_TAG_KEYWORDS"));
			jObj.put("odmId", new JSONString(odmId));
			jObj.put("querycondition", new JSONString(appendQuery));
			VisitKoreaBusiness.post("call", jObj.toString(), new Func3<Object, String, Object>() {

				@Override
				public void call(Object param1, String param2, Object param3) {

					
					
				}
				
			});
			
			Console.log("SearchTagWidgetForTabMenu.appendQuery :: " + appendQuery);
			
		});
		
	}

	private void loadData() {
		
		JSONObject jObj = new JSONObject();
		jObj.put("cmd", new JSONString("SEARCH_TAB_MENU"));
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
						mentLabel.setText("- 지정한 키워드 기준 총 " + countString + " 개의 콘텐츠가 검색 되었습니다.");
						addButton.setEnabled(true);
					}else {
						mentLabel.setText("- 지정한 키워드를 포함하는 컨텐츠를 찾을 수 없습니다.");
						addButton.setEnabled(false);
					}
					
				}
				
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

	public void setOdmId(String odmId) {
		this.odmId = odmId;
	}
	
}
