package kr.or.visitkorea.admin.client.application.component;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.DialogType;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialDialog;
import gwt.material.design.client.ui.MaterialDialogContent;
import gwt.material.design.client.ui.MaterialDialogFooter;
import gwt.material.design.client.ui.MaterialDialogHeader;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.application.command.ApplicationViewBusiness;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;

public class GlobalPropertyManageDialog extends MaterialDialog {
	private MaterialDialogHeader dialogHeader = new MaterialDialogHeader();
	private MaterialDialogContent dialogContent = new MaterialDialogContent();
	private MaterialDialogFooter dialogFooter = new MaterialDialogFooter();
	private JSONArray properties;
	
	public GlobalPropertyManageDialog() {
		super();
		this.init();
	}
	
	private void init() {
		this.setType(DialogType.DEFAULT);
    	
    	MaterialLabel titleLabel = new MaterialLabel();
    	titleLabel.setText("시스템 프로퍼티 설정");
    	titleLabel.setFontSize("1.4em");
    	titleLabel.setTextColor(Color.BLUE);
    	titleLabel.setPadding(15);
    	titleLabel.setFontWeight(FontWeight.BOLD);
    	
    	dialogHeader.add(titleLabel);
    	
    	MaterialButton saveBtn = new MaterialButton("저장");
    	saveBtn.setBackgroundColor(Color.BLUE);
    	saveBtn.setMarginRight(10);
    	saveBtn.addClickHandler(e -> {
    		this.save();
    	});
    	
    	MaterialButton closeBtn = new MaterialButton("닫기");
    	closeBtn.setBackgroundColor(Color.GREY);
    	closeBtn.addClickHandler(e -> {
    		this.close();
    	});
    	
    	dialogFooter.add(closeBtn);
    	dialogFooter.add(saveBtn);
    	
    	this.add(dialogHeader);
    	this.add(dialogContent);
    	this.add(dialogFooter);
	}
	
	public void render(JSONArray array) {
		this.dialogContent.clear();
		this.properties = array;
		
    	MaterialRow headerRow = new MaterialRow();
    	headerRow.setDisplay(Display.FLEX);
    	headerRow.setHeight("40px");
    	headerRow.setMargin(0);
    	
    	MaterialLabel contentCol1 = new MaterialLabel("이름");
    	contentCol1.setGrid("s4");
    	contentCol1.setFontWeight(FontWeight.BOLD);
    	MaterialLabel contentCol2 = new MaterialLabel("설정값");
    	contentCol2.setGrid("s3");
    	contentCol2.setFontWeight(FontWeight.BOLD);
    	MaterialLabel contentCol3 = new MaterialLabel("비고");
    	contentCol3.setGrid("s5");
    	contentCol3.setFontWeight(FontWeight.BOLD);
    	
    	headerRow.add(contentCol1);
    	headerRow.add(contentCol2);
    	headerRow.add(contentCol3);
    	
    	dialogContent.add(headerRow);
		
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.get(i).isObject();
			
			String name = obj.get("NAME").isString().stringValue();
			double value = obj.get("VALUE").isNumber().doubleValue();
			String comment = obj.get("COMMENT").isString().stringValue().replaceAll("/", "<br/>");
			
			MaterialRow row = new MaterialRow();
			row.setDisplay(Display.FLEX);
			row.setHeight("65px");
			
			MaterialLabel nameLabel = new MaterialLabel();
			nameLabel.setGrid("s4");
			nameLabel.setText(name);
			nameLabel.setDisplay(Display.FLEX);
			nameLabel.setFlexAlignItems(FlexAlignItems.CENTER);
			
			MaterialTextBox valueBox = new MaterialTextBox();
			valueBox.setGrid("s3");
			valueBox.setValue(value + "");
			valueBox.setLineHeight(65);
			valueBox.setMargin(0);
			valueBox.getElement().getFirstChildElement().getStyle().clearMargin();
			valueBox.addValueChangeHandler(e -> {
				try {
					obj.put("VALUE", new JSONNumber(Integer.parseInt(e.getValue())));
				} catch (NumberFormatException ignored) {}
			});
			
			MaterialLabel commentLabel = new MaterialLabel();
			commentLabel.setDisplay(Display.FLEX);
			commentLabel.setFlexAlignItems(FlexAlignItems.CENTER);
			commentLabel.setGrid("s5");
			commentLabel.getElement().setInnerHTML(comment);
			
			row.add(nameLabel);
			row.add(valueBox);
			row.add(commentLabel);
			
			this.dialogContent.add(row);
		}
	}
	
	public void save() {
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("UPDATE_GLOBAL_VARIABLES"));
		paramJson.put("array", this.properties);
		VisitKoreaBusiness.post("call", paramJson.toString(), (p1, p2, p3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) p1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				Window.alert("변경사항이 성공적으로 수정되었습니다.");
				ApplicationViewBusiness.fetchGlobalVariables(Registry::initGlobalVariable);
			}
		});
	}
}
