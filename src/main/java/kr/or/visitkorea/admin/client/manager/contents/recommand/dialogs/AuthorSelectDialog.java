package kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs;

import java.util.Date;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
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
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AuthorSelectDialog extends DialogContent {
	private MaterialLabel dialogTitle;
	private ContentTable searchTable;
	private ContentTable selectedTable;
	private MaterialTextBox keyword;
	private MaterialLabel countLabel;
	private int offset;
	private int totcnt;
	private String cotId;
	private MaterialTextBox authorTextBox;
	private String authorInputValue;
	private SelectionPanel relatedViewSelection;
	
	public AuthorSelectDialog(MaterialExtentsWindow tgrWindow) {
		super(tgrWindow);
	}

	@Override
	protected void onLoad() {
		this.searchTable.clearRows();
		this.selectedTable.clearRows();
		this.cotId = this.getParameters().get("cotId").toString();
		this.authorTextBox = (MaterialTextBox) this.getParameters().get("authorTextBox");
		this.relatedViewSelection = (SelectionPanel) this.getParameters().get("relatedViewSelection");
		selectedAuthorSetup();
	}
	
	@Override
	public void init() {
		addDefaultButtons();

		dialogTitle = new MaterialLabel("작가 검색");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setPadding(15);
		this.add(dialogTitle);
		
		MaterialButton submitBtn = new MaterialButton("확인");
		submitBtn.setMarginRight(10);
		submitBtn.setFloat(Float.RIGHT);
		submitBtn.addClickHandler(event -> {
			submitProcess();
		});
		this.getButtonArea().add(submitBtn);
		
		buildSearchForm();
		buildContentBody();
	}
	
	public JSONArray buildSaveModel() {
		authorInputValue = "";
		JSONArray authorArray = new JSONArray();
		this.selectedTable.getRowContainer().getChildrenList().forEach(widget -> {
			ContentTableRow row = (ContentTableRow) widget;
			JSONObject obj = new JSONObject();
			obj.put("cotId", new JSONString(this.cotId));
			obj.put("athId", new JSONString(row.get("athId").toString()));
			obj.put("relatedView", new JSONNumber(0));
			authorArray.set(authorArray.size(), obj);
			authorInputValue += row.get("name").toString() + "(" + row.get("keyword").toString() + ")" + ", ";
		});
		
		this.authorTextBox.setValue(authorInputValue.substring(0, authorInputValue.lastIndexOf(",")));
		this.relatedViewSelection.setSelectionOnSingleMode("OFF");
		return authorArray;
	}
	
	public void submitProcess() {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("INSERT_ARTICLE_AUTHOR_INFO"));
		parameterJSON.put("cotId", new JSONString(this.cotId));
		parameterJSON.put("authorList", buildSaveModel());
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			alert("변경 성공", 400, 300, new String[] { "작가 정보가 수정되었습니다." });
		});
		
	}
	
	public void buildSearchForm() {
		MaterialRow row = new MaterialRow();
		row.setWidth("100%");
		row.setHeight("80px");
		row.setMargin(0);
		
		keyword = new MaterialTextBox();
		keyword.setGrid("s5");
		keyword.setMargin(0);
		keyword.setLabel("검색어 입력");
		keyword.addKeyUpHandler(event -> {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				qryList(true);
			}
		});
		row.add(keyword);
		
		this.add(row);
	}
	
	public void buildContentBody() {
		searchTable = addTable(-60, 300);
		
		MaterialIcon searchBtn = new MaterialIcon(IconType.SEARCH);
		searchBtn.addClickHandler(event -> {
			qryList(true);
		});
		searchTable.getTopMenu().addIcon(searchBtn, "검색", Float.RIGHT, "1.8em", "26px", 24, false);
		
		MaterialIcon moreBtn = new MaterialIcon(IconType.ARROW_DOWNWARD);
		moreBtn.setTextAlign(TextAlign.CENTER);
		moreBtn.addClickHandler(event -> {
			qryList(false);
		});
		searchTable.getButtomMenu().addIcon(moreBtn, "다음 20개", Float.RIGHT);

		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLDER);
		countLabel.setTextColor(Color.BLACK);
		searchTable.getButtomMenu().addLabel(countLabel, Float.RIGHT);
		
		selectedTable = addTable(-75, 200);
		
		this.add(searchTable);
		this.add(selectedTable);
	}
	
	public ContentTable addTable(int top, int height) {
		ContentTable table = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.appendTitle("작가구분", 150, TextAlign.CENTER);
		table.appendTitle("이름", 200, TextAlign.CENTER);
		table.appendTitle("등록일시", 187, TextAlign.CENTER);
		table.appendTitle("활성화 여부", 150, TextAlign.CENTER);
		table.appendTitle("추가/삭제", 100, TextAlign.CENTER);
		table.setLayoutPosition(Position.RELATIVE);
		table.setTop(top); 
		table.setWidth("98.7%");
		table.setHeight(height);
		table.setMargin(6);
		return table;
	}
	
	public void selectedAuthorSetup() {
		
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("ARTICLE_ETC_SELECT_WITH_COTID"));
		parameterJSON.put("cotId", new JSONString(this.cotId));
		
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if(process.equals("success")) {
				
				JSONObject bodyObj =  resultObj.get("body").isObject();
				JSONArray returnRelatedResultArr = bodyObj.get("resultRelatedView").isArray();
				
				for (int i = 0; i < returnRelatedResultArr.size(); i++) {
					JSONObject obj = returnRelatedResultArr.get(i).isObject();
					
					String athId = obj.containsKey("ATH_ID") ? obj.get("ATH_ID").isString().stringValue() : "";
					String keyword = obj.containsKey("KEYWORD") ? obj.get("KEYWORD").isString().stringValue() : "";
					String type = obj.containsKey("TYPE_NAME") ? 
							obj.get("TYPE_NAME").isString().stringValue() + "(" + keyword + ")" : "";
					String name = obj.containsKey("NAME") ? obj.get("NAME").isString().stringValue() : "";
					Date createDate = obj.containsKey("CREATE_DATE") ? DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.s").parse(obj.get("CREATE_DATE").isString().stringValue()) : null;
					String status = obj.containsKey("STATUS") ? obj.get("STATUS").isNumber().doubleValue() == 1 ? "활동" : "미활동" : "";
					
					ContentTableRow selectedTableRow = this.selectedTable.addRow(Color.WHITE
							, type
							, name
							, DateTimeFormat.getFormat("yyyy-MM-dd").format(createDate)
							, status
							, "삭제"
							);
					selectedTableRow.put("keyword", keyword);
					selectedTableRow.put("name", name);
					selectedTableRow.put("athId", athId);
					selectedTableRow.put("type", type);
					selectedTableRow.addClickHandler(e -> {
						ContentTableRow selectedRow = (ContentTableRow) e.getSource();
						if (selectedRow.getSelectedColumn() == 4) {
							selectedRow.removeFromParent();
						}
					});
				}
				
				
			}
			
		});    
			
	}
	
	public void qryList(boolean bstart) {
		if (bstart) {
			this.searchTable.clearRows();
			this.offset = 0; 
		} else offset += 20;
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_AUTHOR_LIST"));
		parameterJSON.put("offset", new JSONNumber(this.offset));
		parameterJSON.put("keyword", new JSONString(this.keyword.getValue()));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyObj = resultObj.get("body").isObject();
				JSONArray bodyResultObj = bodyObj.get("result").isArray();
				JSONObject bodyResultCnt = bodyObj.get("resultCnt").isObject();

				if (bodyResultObj.size() == 0) {
					MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
				}
				
				this.totcnt = (int) bodyResultCnt.get("CNT").isNumber().doubleValue();
				this.countLabel.setText(totcnt + " 건");
				
				int cnt = bodyResultObj.size();
				for (int i = 0; i < cnt; i++) {
					JSONObject obj = bodyResultObj.get(i).isObject();
					
					String athId = obj.containsKey("ATH_ID") ? obj.get("ATH_ID").isString().stringValue() : "";
					String keyword = obj.containsKey("KEYWORD") ? obj.get("KEYWORD").isString().stringValue() : "";
					String type = obj.containsKey("TYPE_NAME") ? 
							obj.get("TYPE_NAME").isString().stringValue() + "(" + keyword + ")" : "";
					String name = obj.containsKey("NAME") ? obj.get("NAME").isString().stringValue() : "";
					Date createDate = obj.containsKey("CREATE_DATE") ? DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.s").parse(obj.get("CREATE_DATE").isString().stringValue()) : null;
					String status = obj.containsKey("STATUS") ? obj.get("STATUS").isNumber().doubleValue() == 1 ? "활동" : "미활동" : "";
					
					ContentTableRow tableRow = this.searchTable.addRow(Color.WHITE
							, type
							, name
							, DateTimeFormat.getFormat("yyyy-MM-dd").format(createDate)
							, status
							, "추가"
						);
					tableRow.put("athId", athId);
					tableRow.put("type", type);
					tableRow.put("keyword", keyword);
					tableRow.put("name", name);
					tableRow.addClickHandler(event -> {
						ContentTableRow row = (ContentTableRow) event.getSource();
						if (row.getSelectedColumn() == 4) {
							int size = this.selectedTable.getRowContainer().getChildrenList().size();
//							for (int j = 0; j < size; j++) {
//								ContentTableRow tRow = (ContentTableRow) this.selectedTable.getRowContainer().getChildrenList().get(j);
//								if (row.get("type").toString().equals(tRow.get("type").toString())) {
//									alert("알림", 400, 300, new String[] { "작가 유형은 한가지씩만 등록 가능합니다." });
//									return;
//								}
//							}
							for (int j = 0; j < size; j++) {
								ContentTableRow tRow = (ContentTableRow) this.selectedTable.getRowContainer().getChildrenList().get(j);
								if (row.get("athId").toString().equals(tRow.get("athId").toString())) {
									alert("알림", 400, 300, new String[] { "같은 작가를 중복해서 추가 할 수 없습니다." });
									return;
								}
							}
						
							ContentTableRow selectedTableRow = this.selectedTable.addRow(Color.WHITE
									, type
									, name
									, DateTimeFormat.getFormat("yyyy-MM-dd").format(createDate)
									, status
									, "삭제"
									);
							selectedTableRow.put("keyword", tableRow.get("keyword"));
							selectedTableRow.put("name", tableRow.get("name"));
							selectedTableRow.put("athId", tableRow.get("athId"));
							selectedTableRow.put("type", tableRow.get("type"));
							selectedTableRow.addClickHandler(e -> {
								ContentTableRow selectedRow = (ContentTableRow) e.getSource();
								if (selectedRow.getSelectedColumn() == 4) {
									selectedRow.removeFromParent();
								}
							});
						}
					});
				}
			}
		});    
	}
	
	@Override
	public int getHeight() {
		return 660;
	}

}
