package kr.or.visitkorea.admin.client.manager.contents.database.dialogs.panel;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.UI;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.SelectInformOfferDialog;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;

public class OfferDialogZikimiPanel extends AbstractOfferDialogContentPanel {

	private MaterialTextBox keyword;
	private MaterialComboBox<String> type;
	private MaterialComboBox<String> reqType;
	private int offset;
	private int totcnt;
	private int index;
	
	public OfferDialogZikimiPanel(SelectInformOfferDialog dContent) {
		super(dContent);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void buildSelectForm() {
		type = addComboBox(this.selectFormRow, "검색조건", "s2", "아이디", "이름(기관명)", "컨텐츠명");
		keyword = addTextBox(this.selectFormRow, "검색어 입력", "s4");
		addSearchIcon(this.selectFormRow);
		reqType = addComboBox(this.selectFormRow, "요청구분", "s2", "수정요청", "신규요청");
	}

	@Override
	public void buildContentTable() {
		table.appendTitle("번호", 100, TextAlign.CENTER);
		table.appendTitle("요청구분", 180, TextAlign.CENTER);
		table.appendTitle("카테고리", 150, TextAlign.CENTER);
		table.appendTitle("컨텐츠명", 300, TextAlign.CENTER);
		table.appendTitle("작성자", 200, TextAlign.CENTER);
		table.appendTitle("처리일시", 150, TextAlign.CENTER);
		table.appendTitle("선택", 188, TextAlign.CENTER);
	}

	@Override
	public void qryList(boolean bstart) {
		if (bstart) {
			offset = 0; 
			index = 1;
			table.clearRows();
		} else offset += 20;
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_ZIKIMI_LIST"));

		String idname = keyword.getText();
		if(!"".equals(idname)) {
			if(type.getSelectedIndex() == 0)
				parameterJSON.put("id", new JSONString(idname));
			else if(type.getSelectedIndex() == 1)
				parameterJSON.put("name", new JSONString(idname));
			else if(type.getSelectedIndex() == 2)
				parameterJSON.put("title", new JSONString(idname));
		}
		
		// 요청 구분 적용
		parameterJSON.put("reqType", new JSONString(reqType.getSelectedIndex()+""));
		parameterJSON.put("offset", new JSONString(offset+""));
		
		table.loading(true);
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
			JSONObject headerObj = (JSONObject) resultObj.get("header");
			String process = headerObj.get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyObj = (JSONObject) resultObj.get("body");
				JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
				JSONObject bodyResultcnt = (JSONObject) bodyObj.get("resultCnt");
				
				totcnt = Integer.parseInt(bodyResultcnt.get("CNT").toString());
				countLabel.setText(totcnt + " 건");
				
				int usrCnt = bodyResultObj.size();
				if (usrCnt == 0) {
					MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
				}

				for (int i = 0; i < usrCnt; i++) {
					JSONObject obj = (JSONObject) bodyResultObj.get(i);
					String ctype = UI.getContentType(
							(int)(obj.get("contenttype") != null ? obj.get("contenttype").isNumber().doubleValue() : -1));
					
					ContentTableRow tableRow = table.addRow(Color.WHITE, new int[] {4}
							,""+(index++)
							,obj.get("reqType").isNumber().doubleValue() == 0 ? "수정요청" : "신규등록"
							,ctype
							,reqType.getSelectedValue().get(0).toString().equals("1") ? 
									obj.get("reqtitle") != null ? obj.get("reqtitle").isString().stringValue() : ""
									: obj.get("title") != null ? obj.get("title").isString().stringValue()  :""
							,obj.get("snsid") != null ? obj.get("snsid").isString().stringValue() : ""
							,obj.get("udate") != null ? obj.get("udate").isString().stringValue() : ""
							,"[선택]"
							);
					
					String snsId;
					String snsUsername;
					if (obj.get("SNS_ID") != null) {
						snsId = obj.get("SNS_ID").isString().stringValue();
					} else {
						continue;
					}

					if (this.dContent.getSnsId() != null && this.dContent.getSnsId().equals(snsId)) {
						MaterialLabel selectColumn = (MaterialLabel) tableRow.getColumnObject(6);
						selectColumn.setFontWeight(FontWeight.BOLD);
						selectColumn.setText("[V선택됨]");
					}
					if (obj.get("snsid") != null) {
						snsUsername = obj.get("snsid").isString().stringValue();
					} else {
						continue;
					}
					tableRow.put("SNS_ID", snsId);
					tableRow.put("SNS_USR_NAME", snsUsername);
					tableRow.addClickHandler(event -> {
						ContentTableRow ctr = (ContentTableRow) event.getSource();
						if (ctr.getSelectedColumn() == 6) {
							offerSelectProcess(ctr.get("SNS_ID").toString(),ctr.get("SNS_USR_NAME").toString());
						}
					});
				}
			} else if (process.equals("fail")) {
				dContent.alert("검색 실패", 500, 300, new String[] {
						"검색이 실패했습니다.",
						"곤리자에게 문의하세요."
				});
			}
			table.loading(false);
		});
	}
}
