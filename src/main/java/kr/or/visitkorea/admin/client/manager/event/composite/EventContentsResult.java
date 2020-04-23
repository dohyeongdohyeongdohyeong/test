package kr.or.visitkorea.admin.client.manager.event.composite;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.addins.client.richeditor.base.constants.ToolbarButton;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.event.EventStatus;
import kr.or.visitkorea.admin.client.manager.event.widgets.FetchCallback;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventContentsResult extends AbstractEventContents {
    

	private SelectionPanel isWinDesc;
	
	private MaterialTextArea contents;

	public EventContentsResult(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("당첨자 발표 본문");
		// eventContentsResult = host.getEventContentsResult();
	}
	
	@Override
	public MaterialWidget render() {
		MaterialPanel panel = new MaterialPanel();
		panel.setHeight("534px");
		panel.setPaddingTop(15);
		panel.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		
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
		editorPanel.setHeight("480px");
		row.add(editorPanel);
		
		return panel;
	}
	
	@Override
	public void loadData(FetchCallback callback) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("GET_EVENT_BASE"));
		paramJson.put("evtId", new JSONString(this.getEvtId()));
		paramJson.put("cotId", new JSONString(this.getCotId()));
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyResultObj = resultObj.get("body").isObject().get("result").isObject();
				setupContentValue(bodyResultObj);
			}
		});
	}

	@Override
	public void saveData() {
		JSONObject paramJson = new JSONObject();
		JSONObject model = this.buildEventModel();
		if (model == null) {
			return;
		}
		paramJson.put("cmd", new JSONString("SAVE_EVENT_WIN_DESC"));
		paramJson.put("model", model);
		VisitKoreaBusiness.post("call", paramJson.toString(), this.saveCallback());
		
		
	}

	@Override
	public JSONObject buildEventModel() {
		JSONObject obj = new JSONObject();
		obj.put("cotId", new JSONString(this.getCotId()));
		obj.put("evtId", new JSONString(this.getEvtId()));
		obj.put("isWinDesc", new JSONNumber((int) this.isWinDesc.getSelectedValue()));
		obj.put("winDesc", new JSONString(this.contents.getValue().replaceAll("\r\n", "<br>").replaceAll("\n", "<br>")));
		return obj;
	}

	@Override
	public void setupContentValue(JSONObject obj) {
		if (obj.containsKey("WIN_DESC"))
			this.contents.setText(obj.get("WIN_DESC").isString().stringValue().replaceAll("<br>", "\n"));
		if (obj.containsKey("IS_WIN_DESC")) {
			this.isWinDesc.setSelectionOnSingleMode(obj.get("IS_WIN_DESC").isNumber().doubleValue() == 0 ? "미사용" : "사용");
		}
	}

	@Override
	public void statusChangeProcess(EventStatus status) {
		
	}
	
}
