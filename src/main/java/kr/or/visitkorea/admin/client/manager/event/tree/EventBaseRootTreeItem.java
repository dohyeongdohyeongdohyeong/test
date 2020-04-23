package kr.or.visitkorea.admin.client.manager.event.tree;

import gwt.material.design.client.constants.IconType;

public class EventBaseRootTreeItem extends EventTreeItem {

	public EventBaseRootTreeItem(int slidingValue) {
		super(slidingValue);
	}

	public EventBaseRootTreeItem(int slidingValue, String text, IconType iconType) {
		super(slidingValue, text, iconType);
	}
	
}
