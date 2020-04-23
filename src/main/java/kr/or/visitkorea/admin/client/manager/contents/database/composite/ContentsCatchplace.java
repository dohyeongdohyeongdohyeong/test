package kr.or.visitkorea.admin.client.manager.contents.database.composite;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanelWithNoImage;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentsCatchplace extends AbtractContents {
	private MaterialPanel panel;
	private MaterialTextBox contents;
	private MaterialTextBox textColor;
	private SelectionPanel selectBg;
	private MaterialImage bg_Image;
	private SelectionPanel isUse;
	private SelectionPanel isFlowEffect;
	private MaterialRow imgRow;
	private String contentsVal, textColorVal = null;
	private int selectBgVal, isUseVal, isFlowEffectVal;
	private String imgId, saveName, savePath = null;
	private String mode = "add";
	private String originImgId = "";
	private MaterialIcon saveIcon;
	
	public ContentsCatchplace(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("캐치프레이즈 관리");
		saveIcon = this.showSaveIconAndGetIcon();
		buildContent();
	}
	
	private void buildContent() {
		panel = new MaterialPanel();
		panel.setPadding(20);
		this.add(panel);
		
		// row1
		MaterialRow row1 = addRow(panel);
		addLabel(row1, "* 문구", TextAlign.CENTER, Color.GREY_LIGHTEN_3,"s2");
		this.contents = addInputText(row1, "문구를 입력해주세요", "s10");
		this.contents.setMaxLength(30);
		
		// row2
		MaterialRow row2 = addRow(panel);
		addLabel(row2, "사용여부", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> useMap = new HashMap<>();
		useMap.put("OFF", 0);
		useMap.put("ON", 1);
		this.isUse = addSelectionPanel(row2, "s2", TextAlign.LEFT, useMap, 3, 5, 5, true);		
		this.isUse.setSelectionOnSingleMode("OFF");
		
		// row3
		addLabel(row2, "배경이미지", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> bgMap = new HashMap<>();
		bgMap.put("기본", 0);
		bgMap.put("수동", 1);
		this.selectBg = addSelectionPanel(row2, "s2", TextAlign.LEFT, bgMap, 3, 5, 5, true);		
		this.selectBg.setSelectionOnSingleMode("기본");
		
		addLabel(row2, "글자흐름효과", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> flowMap = new HashMap<>();
		flowMap.put("OFF", 0);
		flowMap.put("ON", 1);
		this.isFlowEffect = addSelectionPanel(row2, "s2", TextAlign.LEFT, flowMap, 3, 5, 5, true);
		this.isFlowEffect.setSelectionOnSingleMode("OFF");
		
		imgRow = addRow(panel);
		imgRow.setHeight("150px");
		imgRow.setDisplay(Display.NONE);
		
		UploadPanelWithNoImage uploadPanel = new UploadPanelWithNoImage(0, 0, (String) Registry.get("image.server") + "/img/call");
		uploadPanel.setGrid("s2");
		uploadPanel.setPaddingTop(15);
		uploadPanel.setHeight("100%");
		uploadPanel.setPaddingLeft(65);
		uploadPanel.getUploader().addSuccessHandler(event -> {
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			JSONObject result = resultObj.get("body").isObject().get("result").isArray().get(0).isObject();
			String tempImageId = uploadValue.substring(0, uploadValue.lastIndexOf("."));
			
			savePath = "";
			String[] imgMainSplitArr = tempImageId.split("-");
			for (String splitArrMember : imgMainSplitArr) {
				savePath += "/" + splitArrMember.substring(0, 2);
			}
			savePath += "/" +uploadValue;
			saveName = result.get("saveName").isString().stringValue();
			imgId = IDUtil.uuid();
			
			bg_Image.setUrl(Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + saveName);
		});
		
		bg_Image = new MaterialImage();
		bg_Image.setGrid("s10");
		bg_Image.setHeight("150px");
		
		imgRow.add(uploadPanel);
		imgRow.add(bg_Image);
		
		this.selectBg.addStatusChangeEvent(event -> {
			imgRow.setDisplay(this.selectBg.getSelectedText().equals("수동") ? Display.BLOCK : Display.NONE);
		});
		
		MaterialRow row3 = addRow(panel);
		addLabel(row3, "글자색상", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.textColor = addInputText(row3, "문자열 색상값을 입력해주세요.", "s10");
		this.textColor.setMaxLength(7);
		
		saveIcon.addClickHandler(event -> {
			savaData();
		});
	}
	
	public String isValidate() {
		String msg = null;
		if (contents.getValue().equals(""))
			msg = "문구를 입력하세요.";
		if (selectBg.getSelectedText().equals("수동") & imgId == null)
			msg = "이미지를 선택해주세요.";
		return msg;
	}
	
	public void savaData() {
		String msg = isValidate();
		if (msg != null) {
			this.getWindow().alert("알림", msg, 450); 	return;
		}

		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("mode", new JSONString(this.mode));
		parameterJSON.put("COT_ID", new JSONString(this.getCotId()));
		parameterJSON.put("CONTENTS", new JSONString(contents.getValue()));
		parameterJSON.put("IS_USE", new JSONNumber((int) isUse.getSelectedValue()));
		parameterJSON.put("SELECT_BG", new JSONNumber((int) selectBg.getSelectedValue()));
		parameterJSON.put("IS_FLOW", new JSONNumber((int) isFlowEffect.getSelectedValue()));
		parameterJSON.put("TEXT_COLOR", new JSONString(textColor.getValue()));
		if ((int) selectBg.getSelectedValue() == 1) {	
			parameterJSON.put("imgId", new JSONString(imgId));
			parameterJSON.put("imgPath", new JSONString(savePath));
			parameterJSON.put("originImgId", new JSONString(originImgId));
		}
		
		invokeQuery("INSERT_CATCH_PLACE", parameterJSON, (param1, param2, param3) -> {
			getWindow().alert("변경사항이 성공적으로 저장되었습니다.");
			this.mode = "modify";
		});
	}
	
	public void loadData() {
		clearForm();
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cotId", new JSONString(this.getCotId()));

		invokeQuery("GET_CATCH_WITH_COTID", parameterJSON, (param1, param2, param3) -> {
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
			JSONObject headerObj = (JSONObject) resultObj.get("header");
			String processResult = headerObj.get("process").isString().toString();
			processResult = processResult.replaceAll("\"", "");

			if (processResult.equals("success")) {
				JSONObject bodyObj =  resultObj.get("body").isObject().get("result").isObject();
				
				contentsVal = (bodyObj.get("CONTENTS") != null) ? bodyObj.get("CONTENTS").isString().stringValue() : "";
				textColorVal = (bodyObj.get("TEXT_COLOR") != null) ? bodyObj.get("TEXT_COLOR").isString().stringValue() : "";
				isUseVal = (int) bodyObj.get("IS_USE").isNumber().doubleValue();
				selectBgVal = (int) bodyObj.get("SELECT_BG").isNumber().doubleValue();
				isFlowEffectVal = (int) bodyObj.get("IS_FLOW").isNumber().doubleValue();
				imgId = (bodyObj.get("IMG_ID") != null) ? bodyObj.get("IMG_ID").isString().stringValue() : null;
				if (imgId != null) {
					originImgId = imgId;
					savePath = (bodyObj.get("IMAGE_PATH") != null) ? bodyObj.get("IMAGE_PATH").isString().stringValue() : null;
					saveName = savePath.substring(savePath.lastIndexOf("/") + 1);
				}
				
				this.mode = "modify";
				setup();
			}				
		});
	}

	protected void setup() {
		this.contents.setText(this.contentsVal);
		this.isUse.setSelectionOnSingleMode(isUseVal == 0 ? "OFF" : "ON");
		this.textColor.setText(this.textColorVal);
		this.isFlowEffect.setSelectionOnSingleMode(isFlowEffectVal == 0 ? "OFF" : "ON");
		if (selectBgVal == 0) {
			this.selectBg.setSelectionOnSingleMode("기본");
			this.imgRow.setDisplay(Display.NONE);
		} else {
			this.selectBg.setSelectionOnSingleMode("수동");
			this.imgRow.setDisplay(Display.BLOCK);
		}
		if (saveName != null) {
			this.bg_Image.setUrl(Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + saveName);
		}
	}

	protected void clearForm() {
		this.mode = "add";
		this.originImgId = "";
		this.contents.setText(null);
		this.isUse.setSelectionOnSingleMode("OFF");
		this.selectBg.setSelectionOnSingleMode("기본");
		this.textColor.setText(null);
		this.isFlowEffect.setSelectionOnSingleMode("OFF");
		this.bg_Image.setUrl(GWT.getHostPageBaseURL() + "images/notfound.png");
		this.imgRow.setDisplay(Display.NONE);
	}
	
	@Override
	public void setReadOnly(boolean readFlag) {
		
	}

	protected MaterialTextBox addInputText(MaterialRow row, String placeholder, String grid) {
		MaterialTextBox box = new MaterialTextBox();
		box.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		box.setPlaceholder(placeholder);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}
	
	protected SelectionPanel addSelectionPanel(MaterialRow row, String grid, TextAlign align, 
			HashMap<String, Object> valueMap, int margin, int padding, int radius, boolean isSingleSelection) {
		
		SelectionPanel box = new SelectionPanel();
		box.setElementMargin(margin);
		box.setElementPadding(padding);
		box.setElementRadius(radius);
		box.setTextAlign(align);
		box.setSingleSelection(isSingleSelection);
		box.setValues(valueMap);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		box.setLineHeight(46.25);
		box.setHeight("46.25px");
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setTextAlign(align);
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}
}
