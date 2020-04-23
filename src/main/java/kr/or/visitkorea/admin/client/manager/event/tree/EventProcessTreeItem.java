package kr.or.visitkorea.admin.client.manager.event.tree;

import com.google.gwt.json.client.JSONObject;

import gwt.material.design.client.constants.IconType;
import kr.or.visitkorea.admin.client.manager.event.model.EventProcess;

public class EventProcessTreeItem extends EventTreeItem {
	
	private JSONObject obj;
	private EventProcess processObj;
	
	public EventProcessTreeItem(int slidingValue) {
		super(slidingValue);
	}
	
	public EventProcessTreeItem(String text, IconType iconType) {
		super(-1, text, iconType);
	}
	
	public EventProcessTreeItem(int slidingValue, String text, IconType iconType) {
		super(slidingValue, text, iconType);
	}

	public JSONObject getObj() {
		return obj;
	}

	public void setObj(JSONObject obj) {
		this.obj = obj;
	}

	public EventProcess getProcessObj() {
		return processObj;
	}

	public void setProcessObj(EventProcess processObj) {
		this.processObj = processObj;
	}

	
}
