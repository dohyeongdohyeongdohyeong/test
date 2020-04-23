package kr.or.visitkorea.admin.client.manager.repair;

import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.TextBox;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RepairManAddDialog extends DialogContent {

	private MaterialLabel dialogTitle;
	private MaterialTextBox name;
	private MaterialTextBox tel;
	private MaterialTextBox mail;
	
	public RepairManAddDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		name.setText("");
		tel.setText("");
		mail.setText("");
	}

	public void buildContent() {
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("유지보수 담당자 추가"); this.add(dialogTitle);
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
		
		name = new MaterialTextBox(); mr1.add(name);
		name.setWidth("100%"); name.setLabel("이름");
		tel = new MaterialTextBox(); mr1.add(tel);
		tel.setWidth("100%"); tel.setLabel("전화번호");
		tel.setType(InputType.TEL);
//		tel.addFocusHandler(event->{
//		});
		tel.addKeyPressHandler(event->{
			if (!Character.isDigit(event.getCharCode()) && event.getCharCode() != '-') {
	          ((TextBox) event.getSource()).cancelKey();
	        }
		});
		mail = new MaterialTextBox(); mr1.add(mail);
		mail.setWidth("100%"); mail.setLabel("이메일");
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
			
			if(name.getText().trim().equals("")) {
				MaterialToast.fireToast("담당자 이름은 필수 입력항목입니다.", 3000);
				return;
			}
			JSONObject parameterJSON = new JSONObject();
			
			parameterJSON.put("cmd", new JSONString("INSERT_REPAIRMAN"));
			parameterJSON.put("remid", new JSONString(IDUtil.uuid()));
			parameterJSON.put("name", new JSONString(name.getText()));
			if(!tel.getText().equals(""))
				parameterJSON.put("tel", new JSONString(tel.getText()));
			if(!mail.getText().equals(""))
				parameterJSON.put("mail", new JSONString(mail.getText()));
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					if (((JSONObject)resultObj.get("header")).get("process").isString().stringValue().equals("success")) {
						getMaterialExtentsWindow().closeDialog();
					} else {
						MaterialToast.fireToast("저장 실패 !", 3000);
					}
					if(handler != null) {
						handler.onClick(event);
					}
				}
			});
		});
	}

	@Override
	public int getHeight() {
		return 400;
	}
}
