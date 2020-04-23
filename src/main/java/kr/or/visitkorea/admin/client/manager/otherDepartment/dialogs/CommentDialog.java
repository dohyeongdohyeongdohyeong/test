package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommApplication;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CommentDialog extends DialogContent {

	private ContentTable table;
	private MaterialListBox listBox;
	private MaterialTextBox searchBox;
	private String contentType;
	private MaterialLabel dialogTitle;
	private boolean multipleContents;
	private ContentTable targetTable;
	private String tbl;
	private String cotId;
	private Object tgrPanel;
	private String otdId;

	public CommentDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	public void buildContent() {
		
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("링크 컨텐츠 설정");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);

		this.add(dialogTitle);
		this.add(buildSearchArea());
		this.add(buildTable());
		
		MaterialButton selectButton = new MaterialButton("선택");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
/*		
		selectButton.addClickHandler(event -> {
	
			String CID = ((MaterialLabel)table.getSelectedRows().get(0).getChildrenList().get(0)).getText();
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("INSERT_RELATED_CONTENT"));
			parameterJSON.put("tbl", new JSONString(tbl));
			parameterJSON.put("cotId", new JSONString(cotId));
			parameterJSON.put("cid", new JSONString(CID));

			VK.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

				@Override
				public void call(Object param1, String param2, Object param3) {

					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().toString();
					processResult = processResult.replaceAll("\"", "");
					
					if (processResult.equals("success")) {
						if (table.getSelectedRows().size() > 0) {
							
							ContentTableRow tgrRow = table.getSelectedRows().get(0);
							JSONObject retObjects = (JSONObject) tgrRow.get("RETOBJ");
							String cid = retObjects.get("CONTENT_ID").isString().stringValue();
							
							if (tgrPanel instanceof RecommContentsRelatedCourseAndFestival) {
								
								
								if (tbl.equals("FESTIVAL")) {
									
									((RecommContentsRelatedCourseAndFestival)tgrPanel).
									doFestivalDataload();
									
								}else if (tbl.equals("COURSE")) {
									
									((RecommContentsRelatedCourseAndFestival)tgrPanel).
									doCourseDataload();
									
								}
								
								
							}else if (tgrPanel instanceof RecommContentsRelatedRecommandAndSights) {
	
								if (tbl.equals("RECOMMAND")) {
									
									((RecommContentsRelatedRecommandAndSights)tgrPanel).
									doRecommDataload();
									
								}else if (tbl.equals("SIGHT")) {
									
									((RecommContentsRelatedRecommandAndSights)tgrPanel).
									doSightDataload();
									
								}

							}
						}
					}
				}
			});
			
			
			getMaterialExtentsWindow().closeDialog();

		});
*/
		this.addButton(selectButton);

	}

	private MaterialPanel buildPreview() {

		MaterialPanel previewPanel = new MaterialPanel();
		previewPanel.setLayoutPosition(Position.ABSOLUTE);
		previewPanel.setTop(60);
		previewPanel.setLeft(530);
		previewPanel.setRight(30);
		previewPanel.setBottom(123);
		previewPanel.setBorder("1px solid #dddddd");

		return previewPanel;
	}

	private MaterialPanel buildSearchArea() {

		MaterialPanel searchPanel = new MaterialPanel();
		searchPanel.setLayoutPosition(Position.ABSOLUTE);
		searchPanel.setLeft(30);
		searchPanel.setRight(30);
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
				
				int chkSMode = 1;
				
				if (this.multipleContents){
					chkSMode = 0;
				}
				
				String OTD_ID = (String) this.getParameters().get("OTD_ID");
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("GET_OTHER_DEPARTMENT_CONTENT"));
				parameterJSON.put("otdId", new JSONString(OTD_ID));
				parameterJSON.put("mode", new JSONNumber(listBox.getSelectedIndex()));
				parameterJSON.put("keyword", new JSONString(searchBox.getText()));
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

					@Override
					public void call(Object param1, String param2, Object param3) {

						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
						JSONObject headerObj = (JSONObject) resultObj.get("header");
						String processResult = headerObj.get("process").isString().toString();
						processResult = processResult.replaceAll("\"", "");
						
						if (processResult.equals("success")) {

							JSONObject bodyObj =  resultObj.get("body").isObject();
							JSONArray resultArray = bodyObj.get("result").isObject().get("contents").isArray();
	
							int usrCnt = resultArray.size();
							if (usrCnt == 0) {
								getMaterialExtentsWindow().openDialog(RecommApplication.MORE_RESULT_IS_NOT_FOUNT, 500);
							}
							
							for (int i=0; i<usrCnt; i++) {
								
								JSONObject recObj = resultArray.get(i).isObject();
								ContentTableRow tableRow = table.addRow(
										Color.WHITE, 
										getString(recObj, "COT_ID", "·"),
										getString(recObj, "TITLE", "·"));
								
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
		
		ContentTable table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setLeft(30);
		table.setRight(30);
		table.setTop(120);
		table.setHeight(280);
		table.appendTitle("COT_ID", 200, TextAlign.RIGHT);
		table.appendTitle("제목", 350, TextAlign.LEFT);
		
		this.table = table;
		
		return table;
	}
	
	private String getString(JSONObject recObj, String key, String defaultValue) {
		if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return defaultValue;
		else return recObj.get(key).isString().stringValue();
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
		searchBox.setText("");
		table.clearRows();
		listBox.setSelectedIndex(0);
	}

	private void setContentType(String contentType) {
		this.contentType = contentType;
		if (this.contentType.startsWith("(")) {
			this.multipleContents = true;
		}else {
			this.multipleContents = false;
		}
	}

	private String getContentType(String object) {
		return this.contentType;
	}

	@Override
	public int getHeight() {
		return 520;
	}

}
