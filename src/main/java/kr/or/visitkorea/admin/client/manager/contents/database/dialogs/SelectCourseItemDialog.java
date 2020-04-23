package kr.or.visitkorea.admin.client.manager.contents.database.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jquery.client.api.JQuery;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommApplication;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.InputCourseItem;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SelectCourseItemDialog extends DialogContent {

	private ContentTable table;
	private MaterialListBox listBox;
	private MaterialTextBox searchBox;

	public SelectCourseItemDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	public void buildContent() {
		
		addDefaultButtons();

		// dialog title define
		MaterialLabel dialogTitle = new MaterialLabel("코스 컨텐츠 선택");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);

		this.add(dialogTitle);
		this.add(buildSearchArea());
		
		this.table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE, Position.ABSOLUTE);
		this.table.setWidth("484px");
		this.table.setLeft(30);
		this.table.setTop(120);
		this.table.setHeight(280);
		this.table.appendTitle("CID", 80, TextAlign.RIGHT);
		this.table.appendTitle("지역", 80, TextAlign.CENTER);
		this.table.appendTitle("제목", 220, TextAlign.LEFT);
		this.table.appendTitle("대표태그", 100, TextAlign.CENTER);
		
		MaterialIcon icon3 = new MaterialIcon(IconType.VIEW_STREAM);
		icon3.setTextAlign(TextAlign.CENTER);
		icon3.addClickHandler(event->{
		});

		this.table.getButtomMenu().addIcon(icon3, "미리 보기", com.google.gwt.dom.client.Style.Float.LEFT);
		
		this.add(this.table);
		
//		this.add(buildTable());
		
		MaterialButton selectButton = new MaterialButton("선택");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event -> {

			JSONObject recObject = (JSONObject) table.getSelectedRows().get(0).get("RETOBJ");
			
			MaterialPanel dispContent = (MaterialPanel)getParameters().get("DISPLAY_PANEL");
			String cotId = (String)getParameters().get("COT_ID");
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("INSERT_COURSE_ITEM"));
			parameterJSON.put("cotId", new JSONString(cotId));
			parameterJSON.put("cid", new JSONString(getString(recObject, "CID", "")));
			parameterJSON.put("imgId", new JSONString(getString(recObject, "IMG_ID", "")));
			parameterJSON.put("overview", new JSONString(getString(recObject, "OVER_VIEW", "")));
			parameterJSON.put("title", new JSONString(getString(recObject, "TITLE", "")));

			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			//VK.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

				@Override
				public void call(Object param1, String param2, Object param3) {
					
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().toString();
					processResult = processResult.replaceAll("\"", "");
					if (processResult.equals("success")) {

						JSONObject bodyObj = resultObj.get("body").isObject();
						JSONArray resltObj = bodyObj.get("result").isArray();
						InputCourseItem ici = (InputCourseItem)dispContent.getChildrenList().get(0);
						ici.setupItems(resltObj);
					}
				}
			});
			
			getMaterialExtentsWindow().closeDialog();

		});

		this.addButton(selectButton);

	}

	private MaterialPanel buildSearchArea() {

		MaterialPanel searchPanel = new MaterialPanel();
		searchPanel.setLayoutPosition(Position.ABSOLUTE);
		searchPanel.setLeft(30);
		searchPanel.setWidth("480px");
		searchPanel.setHeight("80px");
		
		listBox = new MaterialListBox();
		listBox.setLayoutPosition(Position.ABSOLUTE);
		listBox.addItem("컨텐츠명");
		listBox.addItem("CID");
		listBox.addValueChangeHandler(event->{
			
		});
		
		searchPanel.add(listBox);
		
		searchBox = new MaterialTextBox();
		searchBox.setLayoutPosition(Position.ABSOLUTE);
		searchBox.setPlaceholder("검색할 단어를 입력해 주세요.");
		searchBox.setLeft(180);
		searchBox.setRight(0);
		searchBox.setTextAlign(TextAlign.CENTER);
		searchBox.addKeyUpHandler(event->{
			
			if (event.getNativeKeyCode() == 13 && searchBox.getValue().length() > 0) {
				
				table.loading(false);
				table.clearRows();
				
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("LIST_COURSE"));
				parameterJSON.put("mode", new JSONNumber(listBox.getSelectedIndex()));
				parameterJSON.put("keyword", new JSONString(searchBox.getValue()));

				//VK.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

					@Override
					public void call(Object param1, String param2, Object param3) {

						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
						JSONObject headerObj = (JSONObject) resultObj.get("header");
						String processResult = headerObj.get("process").isString().toString();
						processResult = processResult.replaceAll("\"", "");
						
						if (processResult.equals("success")) {

							JSONObject bodyObj =  resultObj.get("body").isObject();
							JSONArray resultArray = bodyObj.get("result").isArray();
	
							int usrCnt = resultArray.size();
							if (usrCnt == 0) {
								getMaterialExtentsWindow().openDialog(RecommApplication.MORE_RESULT_IS_NOT_FOUNT, 500);
							}
							
							for (int i=0; i<usrCnt; i++) {
								
								JSONObject recObj = resultArray.get(i).isObject();
								ContentTableRow tableRow = table.addRow(
										Color.WHITE, 
										getString(recObj, "CID", "·"),
										getString(recObj, "AREA_NAME", "·"),
										getString(recObj, "TITLE", "·"),
										getString(recObj, "MASTER_TAG", "·"));
								
								tableRow.put("RETOBJ", recObj);
								
							}
						}
						
						table.loading(false);
					}
				});

				
			}

		});
		
		searchPanel.add(searchBox);

		return searchPanel;
	}

	private ContentTable buildTable() {
		
		// 1435  1585
		this.table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		this.table.setLayoutPosition(Position.ABSOLUTE);
		this.table.setLeft(30);
		this.table.setTop(120);
		this.table.setHeight(280);
		this.table.appendTitle("CID", 80, TextAlign.RIGHT);
		this.table.appendTitle("지역", 80, TextAlign.CENTER);
		this.table.appendTitle("제목", 220, TextAlign.LEFT);
		this.table.appendTitle("대표태그", 100, TextAlign.CENTER);
		
		MaterialIcon icon3 = new MaterialIcon(IconType.VIEW_STREAM);
		icon3.setTextAlign(TextAlign.CENTER);
		icon3.addClickHandler(event->{
		});

		this.table.getButtomMenu().addIcon(icon3, "미리 보기", com.google.gwt.dom.client.Style.Float.LEFT);
		
		Console.log(this.table.toString());
		
		return this.table;
	}
	
	private String getString(JSONObject recObj, String key, String defaultValue) {
		if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return defaultValue;
		else return recObj.get(key).isString().stringValue();
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
	}

	@Override
	public int getHeight() {
		return 520;
	}

}
