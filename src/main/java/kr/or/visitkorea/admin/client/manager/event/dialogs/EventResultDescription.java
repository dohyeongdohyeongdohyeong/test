package kr.or.visitkorea.admin.client.manager.event.dialogs;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.dnd.client.util.StringUtil;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventResultDescription extends DialogContent {

	private MaterialLabel alertLabel;
	private SelectionPanel isWinDesc;
	private MaterialTextArea contents;

	private MaterialButton insertButton;

	private String evtId;
	private String cotId; 
	
	public EventResultDescription(MaterialExtentsWindow window) {
		super(window);
	}
	
	@Override
	public void init() {

	}
	
	
	@Override
	protected void onLoad() {
		super.onLoad();
		this.evtId = String.valueOf(getParameters().get("evtId"));
		this.cotId = String.valueOf(getParameters().get("cotId"));
		buildContent();
		loadData();
	}
	
	public void buildContent() {
		addDefaultButtons();
		setBackgroundColor(Color.GREY_LIGHTEN_5);
		
		MaterialLabel title = new MaterialLabel("이벤트 당첨자 발표 본문");
		title.setFontSize("1.4em");
		title.setFontWeight(FontWeight.BOLD);
		title.setTextColor(Color.BLUE);
		title.setPaddingTop(10);
		title.setPaddingLeft(30);
		this.add(title);
		this.add(buildWriteArea());
		

		insertButton = new MaterialButton("등록");
		insertButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		insertButton.addClickHandler(event -> {
			
			String isShow = isWinDesc.getSelectedValue().toString();
			String content = contents.getText().replaceAll("\r\n", "<br>").replaceAll("\n", "<br>");
			Console.log(content);
			if (StringUtil.isEmpty(isShow)) {
				MaterialToast.fireToast("사용여부를 선택해 주세요.", 3000);
				return;
			}
			
			if (StringUtil.isEmpty(content) && isShow == "1") {
					MaterialToast.fireToast("본문 문구를 입력해 주세요", 3000);
					return;
			}
			
			JSONObject paramJson = new JSONObject();
			JSONObject model = this.buildEventModel();
			if (model == null) {
				return;
			}
			paramJson.put("cmd", new JSONString("SAVE_EVENT_WIN_DESC"));
			paramJson.put("model", model);
			VisitKoreaBusiness.post("call", paramJson.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().stringValue();
					if (processResult.equals("success")) {
						MaterialToast.fireToast("본문 내용이 저장 되었습니다.", 3000);
						getMaterialExtentsWindow().closeDialog();
					} else {
						MaterialToast.fireToast("등록에 실패했습니다. 관리자에게 문의하세요.", 3000);
					}
				}
			});

		});
		this.addButton(insertButton);
	}
	
	private MaterialPanel buildWriteArea() {
		
		MaterialPanel panel = new MaterialPanel();
		
		MaterialRow row = null;


		//	Row Define >>
		row = addRow(panel);
		HashMap<String, Object> isWinDescValueMap = new HashMap<>();
		isWinDescValueMap.put("미사용", 0);
		isWinDescValueMap.put("사용", 1);
		addLabel(row, "사용여부", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.isWinDesc = addSelectionPanel(row, "s10", TextAlign.LEFT, isWinDescValueMap, 5, 5, 5, true);
		
		
		row = addRow(panel);
		addLabel(row, "본문문구", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");

		this.contents = new MaterialTextArea();
		this.contents.setHeight("300px");
		this.contents.setText("");
		contents.getElement().getStyle().setProperty("margin", "auto");
		contents.getElement().getFirstChildElement().getStyle().setProperty("maxHeight", "300px");
		contents.getElement().getFirstChildElement().getStyle().setProperty("minHeight", "300px");
		contents.getElement().getFirstChildElement().getStyle().setOverflowY(Overflow.SCROLL);
		contents.getElement().getFirstChildElement().getStyle().setHeight(300, Unit.PX);
		contents.getElement().getFirstChildElement().getStyle().setBorderWidth(1, Unit.PX);
		contents.getElement().getFirstChildElement().getStyle().setBorderStyle(BorderStyle.SOLID);
		contents.getElement().getFirstChildElement().getStyle().setBorderColor("gainsboro");
		contents.getElement().getFirstChildElement().getStyle().setMargin(6, Unit.PX);
		contents.getElement().getFirstChildElement().getStyle().setPadding(0, Unit.PX);
		
		MaterialPanel editorPanel = new MaterialPanel();
		editorPanel.setGrid("s9");
		editorPanel.add(contents);
		editorPanel.setHeight("300px");
		row.add(editorPanel);
		
		return panel;
	}
	

	@Override
	public int getHeight() {
		return 440;
	}
	
	
	public void loadData() {
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("GET_EVENT_BASE"));
		paramJson.put("evtId", new JSONString(this.evtId));
		paramJson.put("cotId", new JSONString(this.cotId));
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyResultObj = resultObj.get("body").isObject().get("result").isObject();
				setupContentValue(bodyResultObj);
			}
		});
	}

	public void setupContentValue(JSONObject obj) {
		if (obj.containsKey("WIN_DESC"))
			this.contents.setText(obj.get("WIN_DESC").isString().stringValue().replaceAll("<br>", "\n"));
		if (obj.containsKey("IS_WIN_DESC")) {
			this.isWinDesc.setSelectionOnSingleMode(obj.get("IS_WIN_DESC").isNumber().doubleValue() == 0 ? "미사용" : "사용");
		}else {
			this.isWinDesc.setSelectionOnSingleMode("미사용");
		}
	}
	
	
	public JSONObject buildEventModel() {
		JSONObject obj = new JSONObject();
		obj.put("cotId", new JSONString(this.cotId));
		obj.put("evtId", new JSONString(this.evtId));
		obj.put("isWinDesc", new JSONNumber((int) this.isWinDesc.getSelectedValue()));
		obj.put("winDesc", new JSONString(this.contents.getValue().replaceAll("\r\n", "<br>").replaceAll("\n", "<br>")));
		return obj;
	}
	
	protected MaterialRow addRow(MaterialWidget parent) {
		MaterialRow row = new MaterialRow();
		parent.add(row);
		return row;
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
	
	protected MaterialLabel addLabel(MaterialRow row, String defaultValue, TextAlign tAlign, Color bgColor, String grid) {
		MaterialLabel tmpLabel = new MaterialLabel(defaultValue);
		tmpLabel.setTextAlign(tAlign);
		tmpLabel.setLineHeight(46.25);
		tmpLabel.setHeight("46.25px");
		tmpLabel.setBackgroundColor(bgColor);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(tmpLabel);
		row.add(col1);
		return tmpLabel;
	}
	
}
