package kr.or.visitkorea.admin.client.manager.account.dialogs;

import java.util.HashMap;
import java.util.Set;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

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
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.account.AccountListContent;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CreateAccountDialog extends DialogContent {

	private MaterialButton createButton;
	private HashMap<String, MaterialWidget> materialWidgetCollection;
	private UploadPanel uploadPanel;

	public CreateAccountDialog(MaterialExtentsWindow window) {
		super(window);
	}
	
	@Override
	public void init() {

		materialWidgetCollection = new HashMap<String, MaterialWidget>();
		addDefaultButtons();

		// dialog title define
		MaterialLabel dialogTitle = new MaterialLabel("사용자 생성");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		
		this.add(dialogTitle); 
		MaterialCollection collection = new MaterialCollection();
		buildTextInputForm(collection, "stfId", "아이디", false);
		buildTextInputForm(collection, "auth", "비밀번호", true);
		buildTextInputForm(collection, "authChk", "비밀번호 확인", true);
		buildRadioInputForm(collection, "chkUse", "활성", "비활성");
		
		collection.setPaddingLeft(250);
		collection.setPaddingRight(35);

		this.add(collection);
		
		uploadPanel = new UploadPanel();
		uploadPanel.setLayoutPosition(Position.ABSOLUTE);
		uploadPanel.setLeft(30);
		uploadPanel.setTop(50);
		uploadPanel.setButtonPostion(false);
		uploadPanel.setWidth("210px");
		uploadPanel.setHeight("351px");
		this.add(uploadPanel);
		
		createButton = new MaterialButton("사용자 생성");
		createButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		createButton.addClickHandler(event->{

			MaterialLoader.loading(true, getPanel());
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("usrId", new JSONString(Registry.getUserId()));
			parameterJSON.put("cmd", new JSONString("USER_INSERT"));
			
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
			
			parameterJSON.put("img", uploadPanel.getImageUrl());
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
