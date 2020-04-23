package kr.or.visitkorea.admin.client.manager.account.panels;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.account.AccountMain;
import kr.or.visitkorea.admin.client.manager.memberActivity.widgets.CommentTable;

public class StaffPermissionHistoryPanel extends AccountBasePanel {
	private final int LIMIT = 20;
	private CommentTable table;
	private int offset;
	private int totCnt;
	private MaterialLabel countLabel;
	
	public StaffPermissionHistoryPanel(AccountMain main) {
		super(main);
	}

	@Override
	protected void init() {
		table = new CommentTable();
		table.getRowContainer().setHeight("500px");
		table.appendTitle("#", 5)
			 .appendTitle("상위항목", 20)
			 .appendTitle("권한명", 25)
			 .appendTitle("활성여부", 5)
			 .appendTitle("수정한 사용자", 15)
			 .appendTitle("수정된 사용자", 15)
			 .appendTitle("일시", 15);
		
		MaterialIcon moreIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		moreIcon.addClickHandler(e -> {
			if (offset + 20 <= totCnt) {
				this.fetch(false);
			}
		});
		
		countLabel = new MaterialLabel();
		countLabel.setFontWeight(FontWeight.BOLD);
		
		table.getBottomArea().addIcon(moreIcon, LIMIT + "개 더보기", Float.RIGHT);
		table.getBottomArea().addLabel(countLabel, Float.RIGHT);
		
		this.add(table);
	}

	@Override
	public void fetch(boolean isStart) {
		if (isStart) {
			offset = 0;
			this.table.clearRows();
		} else {
			offset += LIMIT;
		}
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SELECT_PERMISSION_HISTORY"));
		paramJson.put("offset", new JSONNumber(this.offset));
		paramJson.put("limit", new JSONNumber(LIMIT));
		VisitKoreaBusiness.post("call", paramJson.toString(), (p1, p2, p3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) p1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyResultObj = resultObj.get("body").isObject();
				JSONArray bodyResultArr = bodyResultObj.get("result").isArray();
				totCnt = (int) bodyResultObj.get("resultCnt").isNumber().doubleValue();
				
				countLabel.setText(totCnt + " 건");
				
				for (int i = 0; i < bodyResultArr.size(); i++) {
					JSONObject obj = bodyResultArr.get(i).isObject();
					
					String isActive = null;
					
					if (obj.containsKey("PERMISSION")) {
						isActive = obj.get("PERMISSION").isBoolean().booleanValue() ? "활성화" : "비활성화";
					} else {
						isActive = "-";
					}
					
					table.addCommentRow(new int[] {}
							, obj.get("NO").isNumber().doubleValue()
							, obj.containsKey("PARENT_CAPTION") ? obj.get("PARENT_CAPTION").isString().stringValue() : "-"
							, obj.get("CAPTION").isString().stringValue()
							, isActive
							, obj.get("EDIT_STF_ID").isString().stringValue()
							, obj.get("STF_ID").isString().stringValue()
							, obj.get("CREATE_DATE").isString().stringValue()
							);
				}
			}
		});
	}
}
