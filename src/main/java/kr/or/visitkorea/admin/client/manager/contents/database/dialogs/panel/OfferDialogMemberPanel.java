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
import kr.or.visitkorea.admin.client.manager.contents.database.composite.ContentsETC;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.SelectInformOfferDialog;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;

public class OfferDialogMemberPanel extends AbstractOfferDialogContentPanel {

	private MaterialTextBox keyword;
	private MaterialComboBox<String> cbType;
	private int offset;
	private int totcnt;
	private int index;
	
	public OfferDialogMemberPanel(SelectInformOfferDialog dContent) {
		super(dContent);
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void buildSelectForm() {
		cbType = addComboBox(this.selectFormRow, "검색조건", "s2", "일반", "파트너스");
		keyword = addTextBox(this.selectFormRow, "검색어 입력", "s4");
		addSearchIcon(this.selectFormRow);
	}

	@Override
	public void buildContentTable() {
		table.appendTitle("번호", 150, TextAlign.CENTER);
		table.appendTitle("작성자", 300, TextAlign.CENTER);
		table.appendTitle("기관명/유형", 300, TextAlign.CENTER);
		table.appendTitle("가입일시", 300, TextAlign.CENTER);
		table.appendTitle("선택", 218, TextAlign.CENTER);
	}

	@Override
	public void qryList(boolean bstart) {
		if(bstart) {
			offset = 0; 
			index = 1;
			table.clearRows();
		} else offset += 20;
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_SNS_LIST"));
		parameterJSON.put("snstype", new JSONString((cbType.getSelectedIndex() == 0 ? 20 : 10) + ""));
		parameterJSON.put("idname", new JSONString(keyword.getValue()));
		parameterJSON.put("orderCrDate", new JSONString("desc"));
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
				countLabel.setText(bodyResultcnt.get("CNT") + " 건");
				totcnt = Integer.parseInt(bodyResultcnt.get("CNT").toString());
				
				int usrCnt = bodyResultObj.size();
				if (usrCnt == 0) {
					MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
				}
				
				for (int i = 0; i < usrCnt; i++) {
					JSONObject obj = (JSONObject) bodyResultObj.get(i);
					String id = obj.get("SNS_IDENTIFY").isString().stringValue();
					if (obj.get("SNS_TYPE").isNumber().doubleValue() != 10) {
						id = obj.get("SNS_IDENTIFY").isString().stringValue()+"("+obj.get("SNS_USR_NAME").isString().stringValue()+")";
					}
					String snstype = UI.getType((int) obj.get("SNS_TYPE").isNumber().doubleValue());
					ContentTableRow tableRow = table.addRow(Color.WHITE, new int[] {1}
							,""+(index++)
							,id
							,snstype == null ? obj.get("SNS_USR_NAME") != null ? obj.get("SNS_USR_NAME").isString().stringValue() : "" : snstype
							,obj.get("CREATE_DATE")!=null?obj.get("CREATE_DATE").isString().stringValue():""
							,"[선택]"
							);
					
					String snsId = obj.get("SNS_ID").isString().stringValue();
					if (this.dContent.getSnsId() != null && this.dContent.getSnsId().equals(snsId)) {
						MaterialLabel selectColumn = (MaterialLabel) tableRow.getColumnObject(4);
						selectColumn.setFontWeight(FontWeight.BOLD);
						selectColumn.setText("[V선택됨]");
					}
					
					tableRow.put("SNS_ID", snsId);
					tableRow.put("SNS_USR_NAME", obj.get("SNS_USR_NAME").isString().stringValue());
					tableRow.addClickHandler(event -> {
						ContentTableRow ctr = (ContentTableRow) event.getSource();
						if (ctr.getSelectedColumn() == 4) {
							this.dContent.getMaterialExtentsWindow().closeDialog();
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
