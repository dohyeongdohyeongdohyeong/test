package kr.or.visitkorea.admin.client.manager.event.tree;

import com.google.gwt.json.client.JSONObject;

import gwt.material.design.client.constants.IconType;
import kr.or.visitkorea.admin.client.manager.event.model.EventGift;

public class EventGiftTreeItem extends EventTreeItem {
	
	private JSONObject obj;
	private EventGift giftObj;

	public EventGiftTreeItem(int slidingValue) {
		super(slidingValue);
	}
	
	public EventGiftTreeItem(String text, IconType iconType) {
		super(-1, text, iconType);
	}

	public EventGiftTreeItem(int slidingValue, String text, IconType iconType) {
		super(slidingValue, text, iconType);
	}

	public JSONObject getObj() {
		return obj;
	}

	public void setObj(JSONObject obj) {
		this.obj = obj;
	}

	public EventGift getGiftObj() {
		return giftObj;
	}

	public void setGiftObj(EventGift giftObj) {
		this.giftObj = giftObj;
	}
	
}
