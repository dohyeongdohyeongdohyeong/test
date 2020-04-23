package kr.or.visitkorea.admin.client.manager.repair;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RepairManDelDialog extends DialogContent {

	private MaterialLabel dialogTitle;
	private ContentTable table;
	private ArrayList<String> sbremids = new ArrayList<String>();
	public RepairManDelDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		sbremids.clear();
		table.clearRows();
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_REPAIRMAN_LIST"));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
					for(int i= 0;i< bodyResultObj.size();i++) {
						JSONObject obj = (JSONObject)bodyResultObj.get(i);
						ContentTableRow tableRow = table.addRow(Color.WHITE, new int[]{2}
						, obj.get("name")!=null?obj.get("name").isString().stringValue():""
						, obj.get("tel")!=null?obj.get("tel").isString().stringValue():""
						, "삭제하기"
						);
						tableRow.addClickHandler(event->{
							ContentTableRow ctr = (ContentTableRow)event.getSource();
							String remid = obj.get("remid").isString().stringValue();
							if(ctr.getSelectedColumn() == 2) {
								boolean bdel = true;
								for(int ii = sbremids.size()-1;ii>=0;ii--) {
									if(sbremids.get(ii).equals(remid)) {
										ctr.getChildren().get(2).getElement().getChild(0).setNodeValue("삭제하기");
										sbremids.remove(remid);
										bdel = false;
										break;
									}
								}
								if(bdel) {
									ctr.getChildren().get(2).getElement().getChild(0).setNodeValue("취소(삭제)");
									sbremids.add(remid);
								}
							}
						});
					}
				
				}
			}
		});
	}

	public void buildContent() {
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("유지보수 담당자 삭제"); this.add(dialogTitle);
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		
		buildBody();
	}
	
	private void buildBody() {
		MaterialRow mr1 = new MaterialRow(); this.add(mr1);
		mr1.setLayoutPosition(Position.RELATIVE);
		mr1.setMarginTop(20); mr1.setPaddingLeft(30);	mr1.setPaddingRight(30);
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE); mr1.add(table);
		table.setWidth("100%"); table.setHeight(300);
		table.appendTitle("이름", 120, TextAlign.CENTER);
		table.appendTitle("전화번호", 120, TextAlign.CENTER);
		table.appendTitle("상태", 150, TextAlign.CENTER);
		
	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
	}
	
	private MaterialPanel buttonAreaPanel;
	protected MaterialButton saveButton;
	protected MaterialButton closeButton;
	protected void addDefaultButtons() {
		buttonAreaPanel = new MaterialPanel(); this.add(buttonAreaPanel); 
		buttonAreaPanel.setLayoutPosition(Position.ABSOLUTE);
		buttonAreaPanel.setWidth("100%");
		buttonAreaPanel.setPaddingLeft(20);
		buttonAreaPanel.setPaddingRight(20);
		buttonAreaPanel.setLeft(0); 
		buttonAreaPanel.setBottom(20); 
		
		closeButton = new MaterialButton("닫기"); buttonAreaPanel.add(closeButton);
		closeButton.setLayoutPosition(Position.RELATIVE);
		closeButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		closeButton.setMarginLeft(20);
		closeButton.setId("close");
		closeButton.addClickHandler(event->{
			getMaterialExtentsWindow().closeDialog();
		});
		
		saveButton = new MaterialButton("저장"); buttonAreaPanel.add(saveButton);
		saveButton.setLayoutPosition(Position.RELATIVE);
		saveButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		saveButton.setId("save");
		saveButton.addClickHandler(event->{
			String remids = "";
			for(int ii = 0; ii<sbremids.size();ii++) {
				if(ii == 0) {
					remids += "'"+sbremids.get(ii)+"'";
				} else remids += ",'"+sbremids.get(ii)+"'";
			}
			if(!remids.equals("")) {
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("DELETE_REPAIRMAN"));
				parameterJSON.put("remid", new JSONString(remids));
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
					@Override
					public void call(Object param1, String param2, Object param3) {
						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
						if (((JSONObject)resultObj.get("header")).get("process").isString().stringValue().equals("success")) {
							getMaterialExtentsWindow().closeDialog();
							if(handler != null) {
								handler.onClick(event);
							}
						} else { 
							MaterialToast.fireToast("저장 실패 !", 3000);
						}
					}
				});
			} else {
				getMaterialExtentsWindow().closeDialog();
				if(handler != null) {
					handler.onClick(event);
				}
			}
			
		});
	}

	@Override
	public int getHeight() {
		return 520;
	}
}
