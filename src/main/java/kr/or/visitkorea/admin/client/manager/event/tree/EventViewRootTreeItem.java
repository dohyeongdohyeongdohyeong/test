package kr.or.visitkorea.admin.client.manager.event.tree;

import gwt.material.design.client.constants.IconType;

public class EventViewRootTreeItem extends EventTreeItem {

	public EventViewRootTreeItem(int slidingValue) {
		super(slidingValue);
	}

	public EventViewRootTreeItem(int slidingValue, String text, IconType iconType) {
		super(slidingValue, text, iconType);
	}
	
}
