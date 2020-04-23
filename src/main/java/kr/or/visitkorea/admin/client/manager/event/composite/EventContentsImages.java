package kr.or.visitkorea.admin.client.manager.event.composite;

import java.util.EnumSet;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.widgets.ContentsImage;
import kr.or.visitkorea.admin.client.manager.event.EventContentsTree;
import kr.or.visitkorea.admin.client.manager.event.EventStatus;
import kr.or.visitkorea.admin.client.manager.event.widgets.FetchCallback;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventContentsImages extends ContentsImage implements IEventContents {

	private String evtId;
	private EventStatus eventStatus;
	private EventContentsTree host;
	
	public EventContentsImages(MaterialExtentsWindow materialExtendsWindow) {
		super(materialExtendsWindow);
	}

	public EventContentsImages(EventContentsTree host, MaterialExtentsWindow materialExtendsWindow) {
		super(materialExtendsWindow);
		this.host = host;
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("이미지 업로드");
		this.imageTable.setHeight(535);
		this.uploadPanel.setHeight(400);
	}

	@Override
	public void saveData() {
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SAVE_EVENT_IMAGE"));
		paramJson.put("model", this.buildEventModel());
		VisitKoreaBusiness.post("call", paramJson.toString(), (p1, p2, p3) -> {});
	}
	
	@Override
	public void loadData(FetchCallback callback) {
		this.imageTable.clearRows();
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("GET_EVENT_IMAGE"));
		paramJson.put("evtId", new JSONString(this.getEvtId()));
		paramJson.put("cotId", new JSONString(this.getCotId()));
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().toString();
			process = process.replaceAll("\"", "");

			if (process.equals("success")) {
				JSONArray bodyResultArr = resultObj.get("body").isObject().get("result").isArray();

				int usrCnt = bodyResultArr.size();
				
				for (int i = 0; i < usrCnt; i++) {
					JSONObject obj = bodyResultArr.get(i).isObject();
					
					this.setupContentValue(obj);
				}
			}
		});
	}

	@Override
	public void setupContentValue(JSONObject obj) {
		String imgId = obj.containsKey("IMG_ID") ? obj.get("IMG_ID").isString().stringValue() : "";
		String imgDesc = obj.containsKey("IMAGE_DESCRIPTION") ? obj.get("IMAGE_DESCRIPTION").isString().stringValue() : "";
		String imgPath = obj.containsKey("IMAGE_PATH") ? obj.get("IMAGE_PATH").isString().stringValue() : "";
		
		ContentTableRow tableRow = imageTable.addRow(Color.WHITE, imgId);
		tableRow.put("IMG_ID", imgId);
		tableRow.put("IMG_DESC", imgDesc);
		tableRow.put("IMG_PATH", imgPath);
		tableRow.addClickHandler(e -> {
			this.imageIdLabel.setText(imgId);
			this.imageDescLabel.setText(imgDesc);
			this.uploadPanel.setImageId(imgId);
			this.uploadPanel.setImageUrl(Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" 
					+ imgPath.substring(imgPath.lastIndexOf("/") + 1));
					
			this.setSelectedRow(tableRow);
		});
	}

	@Override
	public JSONObject buildEventModel() {
		JSONObject model = new JSONObject();
		JSONArray array = new JSONArray();
		model.put("IMAGES", array);
		this.imageTable.getRowsList().forEach(item -> {
			
			
			imageTable.getWidgetIndex(item);
			JSONObject obj = new JSONObject();
			obj.put("COT_ID", new JSONString(this.getCotId()));
			if (item.get("IMG_ID") != null)
				obj.put("IMG_ID", new JSONString(item.get("IMG_ID").toString()));
			if (item.get("IMG_PATH") != null)
				obj.put("IMG_PATH", new JSONString(item.get("IMG_PATH").toString()));
			if (item.get("IMG_DESC") != null)
				obj.put("IMG_DESC", new JSONString(item.get("IMG_DESC").toString()));
			if (item.get("IS_DELETE") != null)
				obj.put("IS_DELETE", JSONBoolean.getInstance((boolean) item.get("IS_DELETE")));
			if(item.get("IMG_UPDATE") != null) {
				obj.put("IMG_UPDATE", new JSONString(item.get("IMG_UPDATE").toString()));
				item.put("IMG_UPDATE", null);
					if(item.get("MAIN_IMG") != null) {
						if((Boolean)item.get("MAIN_IMG") == true) {
							obj.put("MAIN_IMG", new JSONString("MAINIMAGE"));
							this.selectedRow.put("MAIN_IMG", false);
						}
					}
			}
			if(item.get("NEW_IMAGE") != null) {
				if((Boolean)item.get("NEW_IMAGE") == true){
					item.put("NEW_IMAGE", false);
				}
			}
			Console.log("obj :: " + obj);
			array.set(array.size(), obj);
		});
		
		return model;
	}

	@Override
	public void statusChangeProcess(EventStatus status) {
		this.eventStatus = status;
		this.formEnabled(this.host.getEditPossSet().contains(this.eventStatus));
	}

	private void formEnabled(boolean isEnabled) {
		this.addIcon.setVisible(isEnabled);
		this.removeIcon.setVisible(isEnabled);
		this.refreshIcon.setVisible(isEnabled);
		this.masterImageIcon.setVisible(isEnabled);
		this.imageDescLabel.setEnabled(isEnabled);
		this.uploadPanel.getBtn().setVisible(isEnabled);
	}

	public String getEvtId() {
		return evtId;
	}

	public void setEvtId(String evtId) {
		this.evtId = evtId;
	}
}
