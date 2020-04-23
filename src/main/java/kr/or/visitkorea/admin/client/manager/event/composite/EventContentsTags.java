package kr.or.visitkorea.admin.client.manager.event.composite;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.widgets.ContentsTags;
import kr.or.visitkorea.admin.client.manager.event.EventContentsTree;
import kr.or.visitkorea.admin.client.manager.event.EventStatus;
import kr.or.visitkorea.admin.client.manager.event.widgets.FetchCallback;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventContentsTags extends ContentsTags implements IEventContents {

	private String evtId;
	private EventStatus eventStatus;
	private EventContentsTree host;

	public EventContentsTags(MaterialExtentsWindow materialExtendsWindow) {
		super(materialExtendsWindow);
	}
	
	public EventContentsTags(EventContentsTree host, MaterialExtentsWindow materialExtendsWindow) {
		super(materialExtendsWindow);
		this.host = host;
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("태그");
		this.searchTable.setHeight(470);
		this.table.setHeight(470);
	}
	
	@Override
	public void saveData() {
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SAVE_CONTENTS_TAGS"));
		paramJson.put("model", this.buildEventModel());
		VisitKoreaBusiness.post("call", paramJson.toString(), (p1, p2, p3) -> {});
	}

	@Override
	public JSONObject buildEventModel() {
		JSONObject model = new JSONObject();
		JSONArray array = new JSONArray();
		model.put("TAGS", array);

		this.table.getRowsList().forEach(item -> {
			JSONObject obj = new JSONObject();
			String tagName = item.get("TAG_NAME").toString().replaceAll("#", "");
			obj.put("tagId", new JSONString(item.get("TAG_ID").toString()));
			obj.put("cotId", new JSONString(this.getCotId()));
			if (item.get("STATUS") != null)
				obj.put("STATUS", new JSONNumber((int) item.get("STATUS")));
			if (tagName.equals(this.masterTag.getText().replaceAll("#", ""))) {
				obj.put("isMasterTag", new JSONNumber(1));
			} else {
				obj.put("isMasterTag", new JSONNumber(0));
			}
			array.set(array.size(), obj);
		});
		
		return model;
	}

	@Override
	public void loadData(FetchCallback callback) {
		inputBox.setValue("");
		table.clearRows();
		searchTable.clearRows();
		
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("GET_CONTENTS_TAGS"));
		paramJson.put("evtId", new JSONString(this.getEvtId()));
		paramJson.put("cotId", new JSONString(this.getCotId()));
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();

			if (process.equals("success")) {
				JSONArray resultArr = resultObj.get("body").isObject().get("result").isArray();

				int usrCnt = resultArr.size();
				
				for (int i = 0; i < usrCnt; i++) {
					JSONObject obj = resultArr.get(i).isObject();
					
					if (obj.get("MASTER_TAG").isNumber().doubleValue() == 1) {
						this.setupContentValue(obj);
					}
					ContentTableRow tableRow = table.addRow(Color.WHITE, getString(obj, "TAG_NAME"));
					tableRow.put("TAG_ID", getString(obj, "TAG_ID").replaceAll("#", ""));
					tableRow.put("TAG_NAME", getString(obj, "TAG_NAME").replaceAll("#", ""));
					tableRow.put("STATUS", 0);
				}
				loading(false);
			}
		});
	}

	@Override
	public void setupContentValue(JSONObject obj) {
		if (obj.containsKey("TAG_NAME")) {
			masterTag.setValue("#" + obj.get("TAG_NAME").isString().stringValue());
		} else {
			masterTag.setValue("대표 태그 없음");
		}
	}
	
	@Override
	public void statusChangeProcess(EventStatus status) {
		this.eventStatus = status;
		this.formEnabled(this.host.getEditPossSet().contains(this.eventStatus));
	}

	private void formEnabled(boolean isEnabled) {
		this.masterTagSelectIcon.setVisible(isEnabled);
		this.addIcon.setVisible(isEnabled);
		this.removeIcon.setVisible(isEnabled);
		this.inputBox.setEnabled(isEnabled);
	}
	
	public String getEvtId() {
		return evtId;
	}

	public void setEvtId(String evtId) {
		this.evtId = evtId;
	}
}
