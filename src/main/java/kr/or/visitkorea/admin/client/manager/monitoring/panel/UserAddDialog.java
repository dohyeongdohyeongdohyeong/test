package kr.or.visitkorea.admin.client.manager.monitoring.panel;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.inputmask.MaterialInputMask;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRadioButton;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.incubator.client.alert.Alert;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.monitoring.MonitoringMain;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class UserAddDialog extends DialogContent{

public UserAddDialog(MaterialExtentsWindow window,MonitoringMain host) {
	super(window);
	this.host = host;
}	
	private MaterialInputMask<Object> Name;
	private MaterialInputMask<Object> SMSNumber;
	private MaterialRadioButton SMSEnableYes;
	private MaterialRadioButton SMSEnableNO;
	private MonitoringMain host;
	@Override
	public void init() {
		build();
	}

	@Override
	public int getHeight() {
		return 400;
	}

	public void build() {
		
		MaterialLabel dialogTitle = new MaterialLabel("-담당자 추가");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		this.add(dialogTitle);
		
		Name = new MaterialInputMask<Object>();
		Name.setLabel("이름");
		
		Name.setLayoutPosition(Position.ABSOLUTE);
		
		Name.setTop(60);
		Name.setLeft(30);
		Name.setWidth("200px");
		Name.setHeight("50px");
		this.add(Name);
		
		SMSNumber = new MaterialInputMask<Object>();
		SMSNumber.setLabel("휴대폰 번호(-포함하여 입력)");
		
		SMSNumber.setLayoutPosition(Position.ABSOLUTE);
		SMSNumber.setTop(140);
		SMSNumber.setLeft(30);
		SMSNumber.setWidth("250px");
		SMSNumber.setHeight("50px");
		this.add(SMSNumber);
		
		MaterialPanel RadioPanel = new MaterialPanel();
		RadioPanel.setLayoutPosition(Position.ABSOLUTE);
		RadioPanel.setTop(230);
		RadioPanel.setLeft(30);
		RadioPanel.setWidth("250px");
		RadioPanel.setHeight("60px");
		this.add(RadioPanel);
		
		MaterialLabel RadioLabel = new MaterialLabel("SMS 수신 여부");
		RadioLabel.setFontSize("0.8em");
		RadioLabel.setMarginBottom(15);
		RadioLabel.getElement().getStyle().setProperty("color", "#9e9e9e");
		RadioPanel.add(RadioLabel);
		
		
		SMSEnableYes = new MaterialRadioButton("use");
		SMSEnableNO = new MaterialRadioButton("use");
		SMSEnableYes.getElement().getStyle().setMargin(15, Unit.PX);
		SMSEnableYes.setText("허용");
		SMSEnableNO.setText("거부");
		SMSEnableNO.getElement().getStyle().setMargin(15, Unit.PX);
		SMSEnableYes.setValue(true,false);
		RadioPanel.add(SMSEnableYes);
		RadioPanel.add(SMSEnableNO);
		
		MaterialButton savebutton = new MaterialButton("저장");
		savebutton.setLayoutPosition(Position.ABSOLUTE);
		savebutton.setRight(170);
		savebutton.setBottom(40);
		this.add(savebutton);
		savebutton.addClickHandler(event->{
			InsertUser();
		});
		
		MaterialButton closebutton = new MaterialButton("취소");
		closebutton.setLayoutPosition(Position.ABSOLUTE);
		closebutton.setBottom(40);
		closebutton.setRight(40);
		this.add(closebutton);
		closebutton.addClickHandler(event->{
			getMaterialExtentsWindow().closeDialog();
		});
	
	}
	
	public void InsertUser() {
		
		if(check()) {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("MONITORING_INSERT_USER"));
		parameterJSON.put("name",new JSONString(Name.getText()));
		parameterJSON.put("sms",new JSONString(SMSNumber.getText()));
		parameterJSON.put("enable",new JSONString(SMSEnableYes.getValue()+""));
		
		
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {
						
						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
						JSONObject bodyObj = resultObj.get("body").isObject();
						String result = bodyObj.get("Result").isString().stringValue();
						if(result.equals("sucess")) {
							getMaterialExtentsWindow().closeDialog();
							MaterialToast.fireToast("사용자 추가에 성공하였습니다");
							host.clearUsertree();
							host.readUser();
							clear();
						} else {
							MaterialToast.fireToast("사용자 추가에 실패하였습니다");
						}
					}

				});

			}

		
		

	}
	
	public Boolean check()  {
		String numbermatch = "^01(?:0|1[1-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
		
		if(Name.getText().equals("")) {
			MaterialToast.fireToast("이름을 입력해주세요.");
			return false;
		}
		
		if(SMSNumber.getText().equals("")) {
			MaterialToast.fireToast("번호를 입력해주세요.");
			return false;
		}
		
		if(!SMSNumber.getText().matches(numbermatch)) {
			MaterialToast.fireToast("번호를 양식대로 입력해주세요.");
			SMSNumber.setText("");
			SMSNumber.setFocus(true);
			return false;
		} 
		
		if(Name.getText().matches("^[0-9]*$")) {
			MaterialToast.fireToast("이름에 숫자가 들어갈 수 없습니다.");
			Name.setText("");
			Name.setFocus(true);
			return false;
		} 
		
		return true;
	}
	
	public void clear() {
		Name.setText("");
		SMSNumber.setText("");
	}
}
