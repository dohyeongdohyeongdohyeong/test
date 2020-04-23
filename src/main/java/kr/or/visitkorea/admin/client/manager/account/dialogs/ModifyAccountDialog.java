package kr.or.visitkorea.admin.client.manager.account.dialogs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCollection;
import gwt.material.design.client.ui.MaterialCollectionItem;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialSwitch;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.account.AccountListContent;
import kr.or.visitkorea.admin.client.manager.account.AccountListItem;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ModifyAccountDialog extends DialogContent {

	private MaterialButton createButton;
	private HashMap<String, MaterialWidget> materialWidgetCollection;
	private UploadPanel uploadPanel;
	private String targetId;

	public ModifyAccountDialog(MaterialExtentsWindow window) {
		super(window);
	}
	
	@Override
	public void init() {

		materialWidgetCollection = new HashMap<String, MaterialWidget>();
		addDefaultButtons();

		// dialog title define
		MaterialLabel dialogTitle = new MaterialLabel("사용자 정보 수정");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		this.add(dialogTitle); 
		
		MaterialCollection collection = new MaterialCollection();
		buildTextInputForm(collection, "STF_ID", "아이디", false);
		buildTextInputForm(collection, "AUTH", "비밀번호", true);
		buildTextInputForm(collection, "AUTH_CHK", "비밀번호 확인", true);
		buildRadioInputForm(collection, "CHK_USE", "활성", "비활성");
		
		collection.setPaddingLeft(250);
		collection.setPaddingRight(35);

		this.add(collection);
		
		uploadPanel = new UploadPanel();
		uploadPanel.setLayoutPosition(Position.ABSOLUTE);
		uploadPanel.setLeft(30);
		uploadPanel.setButtonPostion(false);
		uploadPanel.setTop(50);
		uploadPanel.setWidth("210px");
		uploadPanel.setHeight("351px");
		this.add(uploadPanel);
		
		createButton = new MaterialButton("정보 수정");
		createButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		createButton.addClickHandler(event -> {
			MaterialLoader.loading(true, getPanel());
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("USER_UPDATE"));
			
			Set<String> keySet = materialWidgetCollection.keySet();
			for (String key : keySet) {
				MaterialWidget widget = materialWidgetCollection.get(key);				
				if (widget instanceof MaterialSwitch) {
					if (((MaterialSwitch) widget).getValue()) {
						parameterJSON.put(key, new JSONNumber(1));
					}else {
						parameterJSON.put(key, new JSONNumber(0));
					}
				}else if (widget instanceof MaterialTextBox) {
					String value = ((MaterialTextBox) widget).getValue();
					parameterJSON.put(key, new JSONString(value));
				}
			}
			
			MaterialSwitch chkFlagSwitch = (MaterialSwitch) materialWidgetCollection.get("CHK_USE");
			int chkUseInt = 0;
			if (chkFlagSwitch.getValue()) {
				chkUseInt = 1;
			}
			parameterJSON.put("auth", new JSONString(((MaterialTextBox)materialWidgetCollection.get("AUTH")).getValue()));
			parameterJSON.put("chkUse",  new JSONNumber(chkUseInt));
			parameterJSON.put("img", uploadPanel.getImageUrl());
			parameterJSON.put("usrId", new JSONString(this.targetId));
			VisitKoreaBusiness.post("call", parameterJSON.toString(), (p1, p2, p3) -> {
				JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) p1)).isObject();
				JSONObject headerObj = resultObj.get("header").isObject();
				String process = headerObj.get("process").isString().stringValue();

				MaterialLoader.loading(false, getPanel());
				if (process.equals("success")) {
					getMaterialExtentsWindow().closeDialog();
					
					Set<String> keySet2 = materialWidgetCollection.keySet();
					for (String key : keySet2) {
						MaterialWidget widget = materialWidgetCollection.get(key);				
						if (widget instanceof MaterialSwitch) {
							((MaterialSwitch) widget).setValue(false);
						}else if (widget instanceof MaterialTextBox) {
							((MaterialTextBox) widget).setValue("");
						}
					}
					uploadPanel.setImageUrl("");
					AccountListContent accountListContent = (AccountListContent)Registry.get("AccountListContent");
					accountListContent.buildAccountCollection();
				} else {
					String ment = headerObj.containsKey("ment") ? headerObj.get("ment").isString().stringValue() : "";
					this.alert("사용자 생성 실패", 350, 250, new String[] { ment });
				}
			});
		});
		
		addButton(createButton);
	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		
		super.setParameters(parameters);
		
		MaterialLoader.loading(true, getPanel());
		AccountListItem userItem = (AccountListItem)parameters.get("SELECTED_ACCOUNT");
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("USER_SELECT"));
		parameterJSON.put("usrId", new JSONString(userItem.getAccountId()));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {

					JSONObject bodyObj = resultObj.get("body").isObject();
					JSONObject resltObj = bodyObj.get("result").isObject();
					
					targetId = resltObj.get("USR_ID").isString().stringValue();
					String stfId = resltObj.get("STF_ID").isString().stringValue();
					double chkUse = resltObj.get("CHK_USE").isNumber().doubleValue();
					JSONValue retValue = resltObj.get("IMAGE_PATH");
					if (retValue == null) {
						uploadPanel.setImageUrl("");
					}else {
						uploadPanel.setImageUrl(retValue.isString().stringValue());
					}
					
					boolean userchk = false;
					if (chkUse > 0) userchk = true; 
					((MaterialTextBox)materialWidgetCollection.get("STF_ID")).setText(stfId);
					((MaterialTextBox)materialWidgetCollection.get("STF_ID")).setEnabled(false);
					((MaterialSwitch)materialWidgetCollection.get("CHK_USE")).setValue(userchk);
					
				}

				MaterialLoader.loading(false, getPanel());
			}
		});
	}

	private void buildRadioInputForm(MaterialCollection collection, String inputId, String title1, String title2) {
		MaterialCollectionItem item = new MaterialCollectionItem();
		MaterialSwitch component = new MaterialSwitch();
		component.setOnLabel("사용");
		component.setOffLabel("사용중지");
		item.add(component);
		collection.add(item);
		materialWidgetCollection.put(inputId, component );
	}

	private void buildTextInputForm(MaterialCollection collection, String inputId, String inputTitle, boolean isPassword) {
		MaterialCollectionItem item = new MaterialCollectionItem();
		MaterialTextBox component = new MaterialTextBox();
		component.setPlaceholder("값을 입력해 주세요.");
		component.setLabel(inputTitle);
		component.setActive(true);
		if (isPassword) {
			component.setType(InputType.PASSWORD);
		}
		item.add(component);
		collection.add(item);
		materialWidgetCollection.put(inputId, component);
	}

	@Override
	protected void onLoad() {
        super.onLoad();
    }

	@Override
	public int getHeight() {
		return 500;
	}

}
