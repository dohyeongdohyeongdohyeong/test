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

public class StaffModifyHistoryPanel extends AccountBasePanel {
	private final int LIMIT = 20;
	private CommentTable table;
	private int offset;
	private int totCnt;
	private MaterialLabel countLabel;
	
	public StaffModifyHistoryPanel(AccountMain main) {
		super(main);
	}

	@Override
	protected void init() {
		table = new CommentTable();
		table.getRowContainer().setHeight("500px");
		table.appendTitle("#", 10)
			 .appendTitle("관리자 ID", 15)
			 .appendTitle("IP", 25)
			 .appendTitle("정보수정 내역", 30)
			 .appendTitle("수정일시", 20);
		
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
		paramJson.put("cmd", new JSONString("SELECT_STAFF_MODIFY_HISTORY"));
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
					
					String modified = null;
					
					if (!obj.containsKey("ORIGIN_PW")) {
						modified = "계정생성";
					} else if (obj.containsKey("CHK_USE")) {
						modified = "사용여부 변경";
					} else if (obj.containsKey("ORIGIN_PW")) {
						modified = "비밀번호 변경";
					} else {
						modified = "-";
					}
					
					table.addCommentRow(new int[] {}
							, obj.get("NO").isNumber().doubleValue()
							, obj.get("STF_ID").isString().stringValue()
							, obj.get("IP_ADDR").isString().stringValue()
							, modified
							, obj.get("CREATE_DATE").isString().stringValue()
							);
				}
			}
		});
	}
}
