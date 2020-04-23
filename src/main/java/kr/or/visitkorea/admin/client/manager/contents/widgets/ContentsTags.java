package kr.or.visitkorea.admin.client.manager.contents.widgets;

import java.util.List;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.database.composite.AbtractContents;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommApplication;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentsTags extends AbtractContents {
	protected MaterialTextBox inputBox;
	protected ContentTable table;
	protected MaterialIcon removeIcon;
	protected MaterialIcon masterTagSelectIcon;
	protected ContentTable searchTable;
	protected MaterialIcon addIcon;
	protected MaterialPanel enablePanel;
	protected MaterialLabel masterTag;

	public ContentsTags(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		super.init();
		this.setLayoutPosition(Position.RELATIVE);
		this.setTitle("태그");
		buildContent();
	}

	private void buildContent() {
		inputBox = new MaterialTextBox();
		inputBox.setLabel("멤버 태그 검색");
		inputBox.setIconType(IconType.SEARCH);
		inputBox.setLayoutPosition(Position.ABSOLUTE);
		inputBox.setRight(60);
		inputBox.setLeft(350);
		inputBox.setTop(70);
		inputBox.addKeyUpHandler(event -> {
			if (event.getNativeKeyCode() == 13 && inputBox.getValue().length() > 0) {
				searchTag(inputBox.getValue());
			}
		}); 
		this.add(inputBox);

		masterTag = new MaterialLabel();
		masterTag.setLayoutPosition(Position.ABSOLUTE);
		masterTag.setWidth("300px");
		masterTag.setLeft(30);
		masterTag.setTop(100);
		masterTag.setFontSize("1.3em");
		masterTag.setFontWeight(FontWeight.BOLD);
		masterTag.setTextColor(Color.RED_LIGHTEN_2);
		this.add(masterTag);
		
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setHeight(370);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setLeft(30);
		table.setWidth("300px");
		table.setTop(140);
		table.appendTitle("멤버 태그", 300, TextAlign.LEFT);
		this.add(table);

		removeIcon = new MaterialIcon(IconType.DELETE);
		removeIcon.setTextAlign(TextAlign.CENTER);
		removeIcon.addClickHandler(event->{
			removeMemberTag();
		});
		
		masterTagSelectIcon = new MaterialIcon(IconType.SPELLCHECK);
		masterTagSelectIcon.setTextAlign(TextAlign.CENTER);
		masterTagSelectIcon.addClickHandler(event->{
			setupMasterTag();
		});
		
		table.getButtomMenu().addIcon(removeIcon, "삭제", com.google.gwt.dom.client.Style.Float.LEFT);
		table.getButtomMenu().addIcon(masterTagSelectIcon, "대표태그 선택", com.google.gwt.dom.client.Style.Float.LEFT);
	
		searchTable = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		searchTable.setHeight(370);
		searchTable.setLayoutPosition(Position.ABSOLUTE);
		searchTable.setLeft(350);
		searchTable.setRight(30);
		searchTable.setTop(140);
		searchTable.appendTitle("검색 태그", 702, TextAlign.LEFT);
		this.add(searchTable);
		
		addIcon = new MaterialIcon(IconType.ADD);
		addIcon.setTextAlign(TextAlign.CENTER);
		addIcon.addClickHandler(event->{
			appendMemberTag();
		});

		searchTable.getButtomMenu().addIcon(addIcon, "등록", com.google.gwt.dom.client.Style.Float.LEFT);
	
		enablePanel = new MaterialPanel();
		enablePanel.setLayoutPosition(Position.ABSOLUTE);
		enablePanel.setLeft(30);
		enablePanel.setRight(27);
		enablePanel.setTop(70);
		enablePanel.setBottom(26);
		enablePanel.setBackgroundColor(Color.BLACK);
		enablePanel.setOpacity(0.2);
		enablePanel.setVisibility(Visibility.HIDDEN);
		this.add(enablePanel);		
	}

	//	마스터 태그 설정 처리
	protected void setupMasterTag() {
		if (this.table.getSelectedRows() == null || this.table.getSelectedRows().size() == 0)
			return;

		ContentTableRow selectedRow = this.table.getSelectedRows().get(0);
		masterTag.setText("#" + selectedRow.get("TAG_NAME"));
	}

	//	태그 삭제 처리
	protected void removeMemberTag() {
		if (this.table.getSelectedRows() == null || this.table.getSelectedRows().size() == 0)
			return;
		
		ContentTableRow selectedRow = this.table.getSelectedRows().get(0);
		String tagName = selectedRow.get("TAG_NAME").toString().replaceAll("#", "");

		if (tagName.equals(this.masterTag.getText().replaceAll("#", ""))) {
			this.masterTag.setText("");
		}
		selectedRow.put("STATUS", -1);
		selectedRow.setDisplay(Display.NONE);
		this.table.getSelectedRows().clear();
	}

	//	태그 추가 처리
	protected void appendMemberTag() {
		if (this.searchTable.getSelectedRows() == null || this.searchTable.getSelectedRows().size() == 0)
			return;
		
		ContentTableRow selectedRow = this.searchTable.getSelectedRows().get(0);
		
		List<ContentTableRow> beforeRow = this.table.getRowsList();
		
		boolean Rowcheck = false;
		for (int i = 0; i < beforeRow.size(); i++) {
			
			if(beforeRow.get(i).get("TAG_ID") == selectedRow.get("TAG_ID")) {
				Rowcheck = true;
			}
			
		}
		
		if(Rowcheck == false) {
		ContentTableRow tableRow = this.table.addRow(Color.WHITE, "#" + selectedRow.get("TAG_NAME"));
		tableRow.put("TAG_ID", selectedRow.get("TAG_ID"));
		tableRow.put("TAG_NAME", selectedRow.get("TAG_NAME"));
		tableRow.put("STATUS", 1);
		} else {
			MaterialToast.fireToast("이미 등록된 태그입니다.");
		}
		
	}

	//	태그 검색 처리
	protected void searchTag(String keyword) {
		if(keyword.indexOf(" ") != -1) {
			MaterialToast.fireToast("태그에는 공백이 들어 갈 수 없습니다");
			return;
		}
		searchTable.clearRows(); 

		JSONObject informationJSON = (JSONObject)Registry.get(Registry.BASE_INFORMATION);
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SEARCH_TAGS"));
		parameterJSON.put("keyword", new JSONString(keyword.trim()));
		parameterJSON.put("usrId", new JSONString(informationJSON.get("USR_ID").isString().stringValue()));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			JSONObject headerObj = resultObj.get("header").isObject();
			String process = headerObj.get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyObj =  resultObj.get("body").isObject();
				JSONArray bodyResultArr = bodyObj.get("result").isArray();

				int size = bodyResultArr.size();
				if (size == 0) {
					getWindow().openDialog(RecommApplication.MORE_RESULT_IS_NOT_FOUNT, 500);
				}

				for (int i = 0; i < size; i++) {
					JSONObject obj = bodyResultArr.get(i).isObject();
					ContentTableRow tableRow = searchTable.addRow(Color.WHITE,  getString(obj, "TAG_NAME"));
					tableRow.put("TAG_ID", getString(obj, "TAG_ID").replaceAll("#", ""));
					tableRow.put("TAG_NAME", getString(obj, "TAG_NAME").replaceAll("#", ""));
				}
				loading(false);
			}
		});		
	}

	@Override
	public void loading(boolean loadFlag) {
		super.loading(loadFlag);
	}

	@Override
	public void setReadOnly(boolean readFlag) {
		
	}

	protected String getString(JSONObject recObj, String key) {
		if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return "-";
		else return "#"+recObj.get(key).isString().stringValue();
	}

}