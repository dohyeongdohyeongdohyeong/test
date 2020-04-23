package kr.or.visitkorea.admin.client.manager.contents.author.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.Style.Float;
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
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.author.AuthorApplication;
import kr.or.visitkorea.admin.client.manager.contents.author.panel.AuthorPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.dialog.ButtonInfo;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AuthorTypeDialog extends DialogContent {
	private ContentTable table;
	private AuthorPanel main;
	private MaterialLabel dialogTitle;
	private MaterialLabel countLabel;
	private int offset;
	private int index;
	private int totcnt;
	
	public AuthorTypeDialog(MaterialExtentsWindow tgrWindow) {
		super(tgrWindow);
	}

	@Override
	public void init() {
		addDefaultButtons();

		dialogTitle = new MaterialLabel("작가 구분 관리");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setPadding(15);
		this.add(dialogTitle);
		
		table = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setTop(45);
		table.setWidth("97.2%");
		table.setMargin(10);
		table.setHeight(250);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.appendTitle("구분", 100, TextAlign.CENTER);
		table.appendTitle("작가항목", 200, TextAlign.CENTER);
		table.appendTitle("약어", 100, TextAlign.CENTER);
		table.appendTitle("인원수", 200, TextAlign.CENTER);
		
		MaterialIcon addBtn = new MaterialIcon(IconType.ADD);
		addBtn.addClickHandler(event -> {
			DialogContent dialog = this.getMaterialExtentsWindow().getDialog(AuthorApplication.AUTHOR_TYPE_EDIT_DIALOG);
			HashMap<String, Object> params = new HashMap<>();
			params.put("host", this);
			params.put("mode", "ADD");
			
			this.open(dialog, params, 700);
		});
		table.getTopMenu().addIcon(addBtn, "작가 구분 추가", Float.RIGHT, "1.8em", "26px", 24, false);
		
		MaterialIcon removeBtn = new MaterialIcon(IconType.REMOVE);
		removeBtn.setTextAlign(TextAlign.CENTER);
		removeBtn.addClickHandler(event -> {
			if (this.table.getSelectedRows().size() == 0) {
				return;
			}
			removeProcess();
		});
		table.getButtomMenu().addIcon(removeBtn, "작가 구분 삭제", Float.LEFT);
		
		MaterialIcon modifyBtn = new MaterialIcon(IconType.EDIT);
		modifyBtn.setTextAlign(TextAlign.CENTER);
		modifyBtn.addClickHandler(event -> {
			if (this.table.getSelectedRows().size() == 0) {
				return;
			}
			ContentTableRow tableRow = this.table.getSelectedRows().get(0);
			DialogContent dialog = this.getMaterialExtentsWindow().getDialog(AuthorApplication.AUTHOR_TYPE_EDIT_DIALOG);
			
			HashMap<String, Object> params = new HashMap<>();
			params.put("host", this);
			params.put("mode", "MODIFY");
			params.put("typeId", tableRow.get("typeId"));
			params.put("typeName", tableRow.get("typeName"));
			params.put("keyword", tableRow.get("keyword"));
			this.open(dialog, params, 700);
		});
		table.getButtomMenu().addIcon(modifyBtn, "작가 구분 수정", Float.LEFT);
		
		MaterialIcon moreBtn = new MaterialIcon(IconType.ARROW_DOWNWARD);
		moreBtn.setTextAlign(TextAlign.CENTER);
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
	
	@Override
	protected void onLoad() {
		this.main = (AuthorPanel) this.getParameters().get("host");
		this.table.getSelectedRows().clear();
		qryList(true);
	}

	private void removeProcess() {
		if (this.table.getSelectedRows().size() == 0) {
			return;
		} else {
			ContentTableRow selectedRow = this.table.getSelectedRows().get(0);
			List<ButtonInfo> infoList = new ArrayList<ButtonInfo>();
			ButtonInfo btnInfo = new ButtonInfo("확인", Color.BLUE, Float.RIGHT, event -> {
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("DELETE_AUTHOR_TYPE"));
				parameterJSON.put("typeId", new JSONString(selectedRow.get("typeId").toString()));
				VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {});
				selectedRow.removeFromParent();
				alert("삭제 완료", 400, 300, new String[] { "선택한 항목이 삭제되었습니다." });
				qryList(true);
			});
			infoList.add(btnInfo);
			open("삭제 알림", 400, 300, new String[] { "선택한 항목을 삭제하시겠습니까?" }, infoList);
		}
	}
	
	public void qryList(boolean bstart) {
		if (bstart) {
			this.table.clearRows();
			index = 0;
			offset = 0;
		} else offset += 20;
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_AUTHOR_TYPE"));
		parameterJSON.put("offset", new JSONNumber(this.offset));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			JSONArray bodyResultArr = resultObj.get("body").isObject().get("result").isArray();
			JSONObject bodyResultCnt = resultObj.get("body").isObject().get("resultCnt").isObject();

			int size = bodyResultArr.size();
			if (size == 0) {
				MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
			}
			
			this.totcnt = (int) bodyResultCnt.get("CNT").isNumber().doubleValue();
			this.countLabel.setText(totcnt + " 건");
			
			for (int i = 0; i < size; i++) {
				JSONObject obj = bodyResultArr.get(i).isObject();
				String typeId = obj.containsKey("TYPE_ID") ? obj.get("TYPE_ID").isString().stringValue() : "";
				String typeName = obj.containsKey("TYPE_NAME") ? obj.get("TYPE_NAME").isString().stringValue() : "";
				String keyword = obj.containsKey("KEYWORD") ? obj.get("KEYWORD").isString().stringValue() : "";
				ContentTableRow tableRow = this.table.addRow(Color.WHITE
						, 1 + (index++)
						, typeName
						, keyword
						, obj.containsKey("AUTHOR_COUNT") ? obj.get("AUTHOR_COUNT").isNumber().doubleValue() : ""
					);
				tableRow.put("typeId", typeId);
				tableRow.put("typeName", typeName);
				tableRow.put("keyword", keyword);
			}
		});
	}
	
	@Override
	public int getHeight() {
		return 400;
	}

	public AuthorPanel getMain() {
		return main;
	}

}
