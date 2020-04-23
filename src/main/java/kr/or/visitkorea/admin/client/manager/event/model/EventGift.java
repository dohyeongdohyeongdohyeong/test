package kr.or.visitkorea.admin.client.manager.event.model;

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class EventGift {
	private String gftId;
	private String subEvtId;
	private String evtId;
	private String title;
	private int count;
	private int order;
	private boolean isNotWin;
	private boolean isDelete;
	
	public static EventGift fromJson(JSONObject obj) {
		EventGift gift = new EventGift();
		gift.setDelete(false);
		if (obj.containsKey("GFT_ID"))
			gift.setGftId(obj.get("GFT_ID").isString().stringValue());
		if (obj.containsKey("SUB_EVT_ID"))
			gift.setSubEvtId(obj.get("SUB_EVT_ID").isString().stringValue());
		if (obj.containsKey("EVT_ID"))
			gift.setEvtId(obj.get("EVT_ID").isString().stringValue());
		if (obj.containsKey("TITLE"))
			gift.setTitle(obj.get("TITLE").isString().stringValue());
		if (obj.containsKey("COUNT"))
			gift.setCount((int) obj.get("COUNT").isNumber().doubleValue());
		if (obj.containsKey("ORDER"))
			gift.setOrder((int) obj.get("ORDER").isNumber().doubleValue());
		if (obj.containsKey("IS_NOT_WIN"))
			gift.setNotWin(obj.get("IS_NOT_WIN").isBoolean().booleanValue());
		
		return gift;
	}
	
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		if (this.gftId != null)
			obj.put("GFT_ID", new JSONString(this.gftId));
		if (this.subEvtId != null)
			obj.put("SUB_EVT_ID", new JSONString(this.subEvtId));
		if (this.evtId != null)
			obj.put("EVT_ID", new JSONString(this.evtId));
		if (this.title != null)
			obj.put("TITLE", new JSONString(this.title));
		obj.put("COUNT", new JSONNumber(this.count));
		obj.put("ORDER", new JSONNumber(this.order));
		obj.put("IS_NOT_WIN", JSONBoolean.getInstance(this.isNotWin));
		obj.put("IS_DELETE", JSONBoolean.getInstance(this.isDelete));
		return obj;
	}

	public String getGftId() {
		return gftId;
	}

	public void setGftId(String gftId) {
		this.gftId = gftId;
	}

	public String getSubEvtId() {
		return subEvtId;
	}

	public void setSubEvtId(String subEvtId) {
		this.subEvtId = subEvtId;
	}

	public String getEvtId() {
		return evtId;
	}

	public void setEvtId(String evtId) {
		this.evtId = evtId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean isNotWin() {
		return isNotWin;
	}

	public void setNotWin(boolean isNotWin) {
		this.isNotWin = isNotWin;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	@Override
	public String toString() {
		return "EventGift ["
				+ "\n\t gftId=" + gftId
				+ ",\n\t subEvtId=" + subEvtId
				+ ",\n\t evtId=" + evtId
				+ ",\n\t title=" + title
				+ ",\n\t count=" + count
				+ ",\n\t order=" + order
				+ ",\n\t isNotWin=" + isNotWin
				+ ",\n\t isDelete=" + isDelete
				+ ",\n]";
	}
	
}
