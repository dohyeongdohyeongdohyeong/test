package kr.or.visitkorea.admin.client.manager.account.panels;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.account.AccountMain;
import kr.or.visitkorea.admin.client.manager.memberActivity.widgets.CommentTable;

public class CurrentSessionManagePanel extends AccountBasePanel {
	private final int LIMIT = 20;
	private CommentTable table;
	private int offset;
	private int totCnt;
	private MaterialLabel countLabel;
	
	public CurrentSessionManagePanel(AccountMain main) {
		super(main);
	}

	@Override
	protected void init() {
		table = new CommentTable();
		table.getRowContainer().setHeight("500px");
		table.appendTitle("관리자 ID", 10)
			 .appendTitle("접속시간", 15)
			 .appendTitle("세션생성", 15)
			 .appendTitle("최근갱신", 15)
			 .appendTitle("남은시간", 15)
			 .appendTitle("활성시간", 20)
			 .appendTitle("관리항목", 10);
		
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
		paramJson.put("cmd", new JSONString("SELECT_SESSION_LIST"));
		VisitKoreaBusiness.post("call", paramJson.toString(), (p1, p2, p3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) p1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyResultObj = resultObj.get("body").isObject();
				JSONArray bodyResultArr = bodyResultObj.get("result").isArray();
				
				for (int i = 0; i < bodyResultArr.size(); i++) {
					JSONObject obj = bodyResultArr.get(i).isObject();

					MaterialButton sessionClearbtn = new MaterialButton();
					sessionClearbtn.setText("세션 해제");
					sessionClearbtn.setPadding(0);
					sessionClearbtn.getElement().getStyle().setProperty("margin", "0 auto");
					sessionClearbtn.addClickHandler(e -> {
						JSONObject parameterJson = new JSONObject();
						parameterJson.put("cmd", new JSONString("SESSION_DESTROY"));
						parameterJson.put("sessionId", new JSONString(obj.get("SESSION_ID").isString().stringValue()));
						VisitKoreaBusiness.post("call", parameterJson.toString(), (pp1, pp2, pp3) -> {});
					});
					
					int sessionTimeLimit = (int) Registry.globalMap.get(Registry.GLOBAL_SESSION_TIME_LIMIT) * 60;
					int activeTime = (int) obj.get("ACTIVE_TIME").isNumber().doubleValue() / 1000;
					int remainSessionTime = (int) obj.get("REMAIN_SESSION_TIME").isNumber().doubleValue() / 1000;
					
					StringBuilder sb = new StringBuilder();
				
					if (activeTime >= 3600) {
						sb.append(activeTime / 3600 + "시간 ");
					}
					if (activeTime >= 60) {
						if (activeTime > 3600) {
							activeTime %= 3600;
						}
						sb.append(activeTime / 60 + "분 ");
					}
					sb.append(activeTime % 60 + "초");
					
					table.addCommentRow(new int[] {}
							, obj.get("STF_ID").isString().stringValue()
							, obj.get("LAST_LOGIN_DATETIME").isString().stringValue()
							, obj.get("CREATE_TIME").isString().stringValue()
							, obj.get("LAST_ACCESS_TIME").isString().stringValue()
							, ((sessionTimeLimit - remainSessionTime) / 60) + "분 " + ((sessionTimeLimit - remainSessionTime) % 60) + "초"
							, sb.toString()
							, sessionClearbtn
							);
					sessionClearbtn.setWidth("7%");
					
				}
			}
		});
	}
	
}
