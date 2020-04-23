package kr.or.visitkorea.admin.client.manager.event.tree;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTreeItem;

public class EventTreeItem extends ContentTreeItem {
	protected EventTreeSelectHandler selectHandler;
	
	public EventTreeItem(int slidingValue) {
		super(slidingValue);
	}

	public EventTreeItem(int slidingValue, String text, IconType iconType) {
		super(slidingValue);
		this.setTextAlign(TextAlign.LEFT);
		this.setFontSize("1.0em");
		this.setTextColor(Color.BLUE);
		this.setText(text);
		this.setIconType(iconType);
	}
	
	public void addEventTreeSelectHandler(EventTreeSelectHandler handler) {
		this.selectHandler = handler;
	}

	public EventTreeSelectHandler getSelectHandler() {
		return selectHandler;
	}

}
