package kr.or.visitkorea.admin.client.manager.memberActivity.panels;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.memberActivity.ActivityApplication;
import kr.or.visitkorea.admin.client.manager.memberActivity.MemberActivityMain;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;

public class CommentPanel extends BasePanel {
	private ContentTable table;
	private MaterialLabel countLabel;
	private MaterialTextBox startDate, endDate;
	private MaterialComboBox<Object> searchType;
	private MaterialTextBox keyword;
	private int totcnt;
	private int offset;
	private int index;
	private MaterialButton btnXlsDn;
	
	public CommentPanel(MemberActivityMain host) {
		super(host);
	}
	
	@Override
	protected Widget createBody() {
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setTop(80);
		table.setWidth("97.5%");
		table.setHeight(555);
		table.setMargin(10);
		
		table.appendTitle("번호", 150, TextAlign.CENTER);
		table.appendTitle("CID", 150, TextAlign.CENTER);
		table.appendTitle("콘텐츠명", 660, TextAlign.CENTER);
		table.appendTitle("최근댓글등록일시", 210, TextAlign.CENTER);
		table.appendTitle("댓글수", 150, TextAlign.CENTER);
		table.appendTitle("미리보기", 140, TextAlign.CENTER);
		
		btnXlsDn = new MaterialButton("엑셀다운로드");
		table.getButtomMenu().addButton(btnXlsDn, com.google.gwt.dom.client.Style.Float.LEFT);
		btnXlsDn.addClickHandler(e->{
			if(countLabel.getText().startsWith("0")) {
				host.getMaterialExtentsWindow().alert("결과 목록이 없습니다.", 500);
				return;
			}
			String downurl = "./call?cmd=FILE_DOWNLOAD_XLS";
			downurl += "&select_type=comment";
			
				downurl += "&rtype="+((int)searchType.getValues().get(searchType.getSelectedIndex()));
			
			String strsdate = startDate.getText().toString();
			if(!"".equals(strsdate)) {
				downurl += "&sdate="+strsdate;
			}
			String stredate = endDate.getText().toString();
			if(!"".equals(stredate)) {
				downurl += "&edate="+stredate+" 23:59:59";
			}
			
			String idname = keyword.getText();
			if(!"".equals(idname))
				downurl += "&idname="+idname;
			
			Window.open(downurl,"_self", "enabled");
		});
		this.add(table);
		
		MaterialIcon searchIcon = new MaterialIcon(IconType.SEARCH);																						
		searchIcon.setTextAlign(TextAlign.CENTER);
		searchIcon.addClickHandler(event->{
			qryList(true);
		});
		table.getTopMenu().addIcon(searchIcon, "검색", com.google.gwt.dom.client.Style.Float.RIGHT, "1.8em", "26px", 24, false);
		
		MaterialIcon nextRowsIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		nextRowsIcon.setTextAlign(TextAlign.CENTER);
		nextRowsIcon.addClickHandler(event->{
			if((offset + 20) >= totcnt) {
				MaterialToast.fireToast("더이상의 결과 값이 없습니다.", 3000);
			} else {
				qryList(false);
			}
		});
		table.getButtomMenu().addIcon(nextRowsIcon, "다음 20개", com.google.gwt.dom.client.Style.Float.RIGHT);

		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLDER);
		countLabel.setTextColor(Color.BLACK);
		table.getButtomMenu().addLabel(countLabel, com.google.gwt.dom.client.Style.Float.RIGHT);
		
		return table;
	}
	
	@Override
	protected Widget createSearchForm() {
		MaterialPanel row = new MaterialPanel();
		row.setWidth("100%");
		row.setHeight("80px");
		row.setPadding(10);
		
		searchType = new MaterialComboBox<>();
		searchType.setLayoutPosition(Position.ABSOLUTE);
		searchType.setLabel("검색조건");
		searchType.setTop(45);
		searchType.setLeft(15);
		searchType.setWidth("200px");
		searchType.addItem("콘텐츠명", 0);
		searchType.addItem("아이디", 1);
		searchType.addItem("이름(별명)", 2);
		row.add(searchType);
		
		keyword = new MaterialTextBox();
		keyword.setLayoutPosition(Position.ABSOLUTE);
		keyword.setLabel("검색어입력");
		keyword.setTop(45);
		keyword.setLeft(225);
		keyword.setWidth("500px");
		keyword.addKeyPressHandler(event -> {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				qryList(true);
			}
		});
		row.add(keyword);
		
		startDate = new MaterialTextBox();
		startDate.setType(InputType.DATE);
		startDate.setLayoutPosition(Position.ABSOLUTE);
		startDate.setLabel("기간");
		startDate.setText("-");
		startDate.setTop(45);
		startDate.setRight(240);
		startDate.setWidth("150px");
		row.add(startDate);
		
		endDate = new MaterialTextBox();
		endDate.setType(InputType.DATE);
		endDate.setLayoutPosition(Position.ABSOLUTE);
		endDate.setTop(45);
		endDate.setRight(80);
		endDate.setWidth("150px");
		row.add(endDate);
		return row;
	}
	
	public void qryList(boolean bstart) {
		if(bstart) {
			offset = 0; 
			index = 1;
			table.clearRows();
		} else offset += 20;
		table.loading(true);
		
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SELECT_CONTENT_MASTER_LIST"));
		paramJson.put("searchType", new JSONNumber((int) this.searchType.getSelectedValue().get(0)));
		paramJson.put("keyword", new JSONString(this.keyword.getValue()));
		paramJson.put("startDate", new JSONString(this.startDate.getValue()));
		paramJson.put("endDate", new JSONString(this.endDate.getValue()));
		paramJson.put("order", new JSONString("DESC"));
		paramJson.put("offset", new JSONNumber(offset));
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			JSONObject headerObj = resultObj.get("header").isObject();
			String processResult = headerObj.get("process").isString().stringValue();
			if (processResult.equals("success")) {
				JSONObject bodyObj = resultObj.get("body").isObject();
				JSONArray bodyResultObj = bodyObj.get("result").isArray();
				JSONObject bodyResultcnt = bodyObj.get("resultCnt").isObject();
				
				totcnt = (int) bodyResultcnt.get("CNT").isNumber().doubleValue();
				countLabel.setText(totcnt + " 건");
				
				int usrCnt = bodyResultObj.size();
				if (usrCnt == 0) {
					MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
				}

				for (int i = 0; i < usrCnt; i++) {
					JSONObject obj = bodyResultObj.get(i).isObject();
					
					this.addTableRow(obj);
				}
			} else {
				host.getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
				countLabel.setText("실패?");
			}
			table.loading(false);
		});
	}
	
	private ContentTableRow addTableRow(JSONObject obj) {
		String cotId = obj.containsKey("COT_ID") ? obj.get("COT_ID").isString().stringValue() : "";
		String contentId = obj.containsKey("CONTENT_ID") ? obj.get("CONTENT_ID").isString().stringValue() : "";
		String title = obj.containsKey("TITLE") ? obj.get("TITLE").isString().stringValue() : "";
		String createDate = obj.containsKey("CREATE_DATE") ? obj.get("CREATE_DATE").isString().stringValue() : "";
		String cnt = obj.containsKey("CNT") ? obj.get("CNT").isNumber().toString() : "";
		
		ContentTableRow tableRow = table.addRow(Color.WHITE, new int[]{2,5}
			, index++
			, contentId
			, title
			, createDate
			, cnt
			, "미리보기"
		);
		tableRow.put("cotId", cotId);
		tableRow.put("title", title);
		tableRow.put("contentId", contentId);
		tableRow.put("createDate", createDate);
		tableRow.put("modifiedDate", cnt);
		tableRow.addClickHandler(this.setTableClickHandler());
		
		return tableRow;
	}
	
	private ClickHandler setTableClickHandler() {
		return e -> {
			ContentTableRow ctr = (ContentTableRow) e.getSource();

			//	댓글 상세보기
			if (ctr.getSelectedColumn() == 2) {
				HashMap<String, Object> params = new HashMap<>();
				params.put("title", ctr.get("title").toString());
				params.put("cotId", ctr.get("cotId").toString());
				params.put("contentId", ctr.get("contentId").toString());
				params.put("createDate", ctr.get("createDate").toString());
				params.put("modifiedDate", ctr.get("modifiedDate").toString());
				this.host.getMaterialExtentsWindow().openDialog(ActivityApplication.COMMENT_LIST, params, 1350);
				
			//	콘텐츠 미리보기
			} else if (ctr.getSelectedColumn() == 5) {
				MaterialIcon courseIcon5 = new MaterialIcon(IconType.WEB);
				courseIcon5.setTextAlign(TextAlign.CENTER);
				String tgrUrl = Registry.get("service.server") + "/detail/detail_view.html?cotid=" + ctr.get("cotId").toString();
				Registry.openPreview(courseIcon5, tgrUrl);
			}
		};
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		
	}
}