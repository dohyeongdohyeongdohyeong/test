package kr.or.visitkorea.admin.client.manager.contents.author.panel;

import java.util.Date;
import java.util.HashMap;

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

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.author.AuthorApplication;
import kr.or.visitkorea.admin.client.manager.contents.author.AuthorMain;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;

public class AuthorPanel extends BasePanel {
	private AuthorMain host;
	private ContentTable table;
	private MaterialLabel countLabel;
	private MaterialComboBox<String> authorType;
	private MaterialTextBox keyword;
	private int totcnt;
	private int index;
	private int offset;
	private JSONArray authorTypeList; 
	
	public AuthorPanel(AuthorMain host) {
		this.host = host;
		init();
	}

	public void init() {
		fetchAuthorType();
		buildSearchForm();
		buildContentBody();
	}
	
	public void buildSearchForm() {
		MaterialRow row = new MaterialRow();
		row.setWidth("100%");
		row.setHeight("80px");
		row.setPadding(10);
		row.setMargin(0);
		
		authorType = new MaterialComboBox<String>();
		authorType.setLabel("검색조건");
		authorType.addItem("전체", "");
		authorType.setGrid("s2");
		
		row.add(authorType);
		
		keyword = new MaterialTextBox();
		keyword.setGrid("s5");
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
		table = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.appendTitle("번호", 150, TextAlign.CENTER);
		table.appendTitle("작가구분", 250, TextAlign.CENTER);
		table.appendTitle("이름", 350, TextAlign.CENTER);
		table.appendTitle("등록일시", 250, TextAlign.CENTER);
		table.appendTitle("활성화 여부", 200, TextAlign.CENTER);
		table.appendTitle("컨텐츠 개수", 150, TextAlign.CENTER);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setTop(50); 
		table.setWidth("98.7%");
		table.setHeight(590);
		table.setMargin(10);
		
		MaterialIcon authorAddBtn = new MaterialIcon(IconType.ADD);
		authorAddBtn.addClickHandler(event -> {
			HashMap<String, Object> params = new HashMap<>();
			params.put("mode", "ADD");
			params.put("authorTypeList", this.authorTypeList);
			this.host.getMaterialExtentsWindow().openDialog(AuthorApplication.AUTHOR_EDIT_DIALOG, params, 700);
		});
		MaterialIcon searchBtn = new MaterialIcon(IconType.SEARCH);
		searchBtn.addClickHandler(event -> {
			qryList(true);
		});
		table.getTopMenu().addIcon(searchBtn, "검색", Float.RIGHT, "1.8em", "26px", 24, false);
		table.getTopMenu().addIcon(authorAddBtn, "작가 추가", Float.RIGHT, "1.8em", "26px", 24, false);

		MaterialIcon authorTypeBtn = new MaterialIcon(IconType.EDIT);
		authorTypeBtn.addClickHandler(event -> {
			HashMap<String, Object> params = new HashMap<>();
			params.put("host", this);			
			this.host.getMaterialExtentsWindow().openDialog(AuthorApplication.AUTHOR_TYPE_DIALOG, params, 700);
		});
		table.getButtomMenu().addIcon(authorTypeBtn, "작가 구분 관리", Float.LEFT);
		
		MaterialIcon moreBtn = new MaterialIcon(IconType.ARROW_DOWNWARD);
		moreBtn.addClickHandler(event -> {
			qryList(false);
		});
		table.getButtomMenu().addIcon(moreBtn, "다음 20개", Float.RIGHT);

		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLDER);
		countLabel.setTextColor(Color.BLACK);
		table.getButtomMenu().addLabel(countLabel, Float.RIGHT);
		
		this.add(table);
	}
	
	public void qryList(boolean bstart) {
		if (bstart) {
			this.table.clearRows();
			this.index = 0;
			this.offset = 0; 
		} else offset += 20;
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_AUTHOR_LIST"));
		parameterJSON.put("offset", new JSONNumber(this.offset));
		parameterJSON.put("keyword", new JSONString(this.keyword.getValue()));
		if (this.authorType.getSelectedIndex() != 0) {
			parameterJSON.put("type", new JSONString(this.authorType.getSelectedValue().get(0)));
		}
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
					String type = obj.containsKey("TYPE_NAME") ? 
							obj.get("TYPE_NAME").isString().stringValue() + "(" + obj.get("KEYWORD").isString().stringValue() + ")" : "";
					String name = obj.containsKey("NAME") ? obj.get("NAME").isString().stringValue() : "";
					Date createDate = obj.containsKey("CREATE_DATE") ? DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.s").parse(obj.get("CREATE_DATE").isString().stringValue()) : null;
					String status = obj.containsKey("STATUS") ? obj.get("STATUS").isNumber().doubleValue() == 1 ? "활동" : "미활동" : "";
					int contentCnt = obj.containsKey("CONTENT_CNT") ? (int) obj.get("CONTENT_CNT").isNumber().doubleValue() : -1;
					
					ContentTableRow tableRow = this.table.addRow(Color.WHITE, new int[] {5}
							, 1 + (index++)
							, type
							, name
							, DateTimeFormat.getFormat("yyyy-MM-dd").format(createDate)
							, status
							, contentCnt
						);
					tableRow.addClickHandler(event -> {
						ContentTableRow row = (ContentTableRow) event.getSource();
						if (row.getSelectedColumn() == 5) {
							HashMap<String, Object> params = new HashMap<>();
							params.put("name", name);
							params.put("athId", athId);
							this.host.getMaterialExtentsWindow().openDialog(AuthorApplication.AUTHOR_CONTENT_DIALOG, params, 700);
						}
					});
					tableRow.addDoubleClickHandler(event -> {
						HashMap<String, Object> params = new HashMap<>();
						params.put("mode", "MODIFY");
						params.put("authorTypeList", this.authorTypeList);
						params.put("athId", athId);
						params.put("name", name);
						params.put("type", type);
						params.put("status", status);
						this.host.getMaterialExtentsWindow().openDialog(AuthorApplication.AUTHOR_EDIT_DIALOG, params, 700);
					});
				}
			}
		});    
	}

	public void fetchAuthorType() {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_AUTHOR_TYPE"));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONArray bodyResultObj = resultObj.get("body").isObject().get("result").isArray();
				
				this.authorTypeList = bodyResultObj;

				addAuthorTypeItem(authorType);
			}
		});
	}
	
	public void addAuthorTypeItem(MaterialComboBox<String> authorType) {
		for (int i = 0; i < this.authorTypeList.size(); i++) {
			JSONObject obj = this.authorTypeList.get(i).isObject();
			String name = obj.containsKey("TYPE_NAME") ? obj.get("TYPE_NAME").isString().stringValue() : null;
			String typeId = obj.containsKey("TYPE_ID") ? obj.get("TYPE_ID").isString().stringValue() : null;
			authorType.addItem(name, typeId);
		}
	}
}
