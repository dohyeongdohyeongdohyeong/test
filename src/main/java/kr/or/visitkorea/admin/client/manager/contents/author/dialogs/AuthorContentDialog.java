package kr.or.visitkorea.admin.client.manager.contents.author.dialogs;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AuthorContentDialog extends DialogContent {
	private String mode = null;
	private MaterialLabel dialogTitle;
	private ContentTable table;
	private MaterialLabel countLabel;
	private int index;
	private int offset;
	private int totcnt;
	
	public AuthorContentDialog(MaterialExtentsWindow tgrWindow) {
		super(tgrWindow);
	}

	@Override
	public void init() {
		addDefaultButtons();

		dialogTitle = new MaterialLabel("작가 컨텐츠 갯수");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setPadding(15);
		this.add(dialogTitle);
		this.add(buildContent());
	}
	
	public Widget buildContent() {
		MaterialPanel panel = new MaterialPanel();
		panel.setWidth("100%");
		panel.setPadding(5);
		
		table = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.appendTitle("번호", 100, TextAlign.CENTER);
		table.appendTitle("컨텐츠 제목", 300, TextAlign.CENTER);
		table.appendTitle("작성일", 188, TextAlign.CENTER);
		table.appendTitle("조회수", 100, TextAlign.CENTER);
		table.setWidth("100%");
		table.setHeight(350);
		
		MaterialIcon moreBtn = new MaterialIcon(IconType.ARROW_DOWNWARD);
		moreBtn.setTextAlign(TextAlign.CENTER);
		moreBtn.addClickHandler(event -> {
			qryList(false);
		});
		
		this.table.getButtomMenu().addIcon(moreBtn, "다음 20개", Float.RIGHT);

		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLDER);
		countLabel.setTextColor(Color.BLACK);
		this.table.getButtomMenu().addLabel(this.countLabel, Float.RIGHT);
		
		panel.add(table);
		return panel;
	}
	
	private void qryList(boolean bstart) {
		if (bstart) {
			this.table.clearRows();
			offset = 0;
			index = 0;
		} else offset += 20;
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_AUTHOR_CONTENT_LIST"));
		parameterJSON.put("athId", new JSONString(this.getParameters().get("athId").toString()));
		parameterJSON.put("offset", new JSONNumber(this.offset));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyObj = resultObj.get("body").isObject();
				JSONArray bodyResultObj = bodyObj.get("result").isArray();
				JSONObject bodyResultCnt = bodyObj.get("resultCnt").isObject();
				
				if (bodyResultObj.size() == 0) {
					MaterialToast.fireToast("검색 결과가 없습니다.", 3000);
				}
				
				totcnt = (int) bodyResultCnt.get("CNT").isNumber().doubleValue();
				this.countLabel.setText(totcnt + " 건");
				
				for (int i = 0; i < bodyResultObj.size(); i++) {
					JSONObject obj = bodyResultObj.get(i).isObject();
					String cotId = obj.containsKey("COT_ID") ? obj.get("COT_ID").isString().stringValue() : "";
					String title = obj.containsKey("TITLE") ? obj.get("TITLE").isString().stringValue() : "";
					String createDate = obj.containsKey("CREATE_DATE") ? obj.get("CREATE_DATE").isString().stringValue() : "";
					int readCount = obj.containsKey("READ_COUNT") ? (int) obj.get("READ_COUNT").isNumber().doubleValue() : 0;
					
					ContentTableRow tableRow = this.table.addRow(Color.WHITE
							, 1 + (index++)
							, title
							, createDate
							, readCount );
				}
			}
		});
	}
	@Override
	public int getHeight() {
		return 500;
	}

	@Override
	protected void onLoad() {
		this.dialogTitle.setText(this.getParameters().get("name").toString() + " 작가 컨텐츠 갯수");
		qryList(true);
	}

}
