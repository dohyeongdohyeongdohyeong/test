package kr.or.visitkorea.admin.client.manager.event.components;

import gwt.material.design.client.constants.IconType;
import kr.or.visitkorea.admin.client.manager.contents.components.ContentComponentType;
import kr.or.visitkorea.admin.client.manager.event.tree.EventTreeItem;

public class EventComponentTreeFactory {
	
	public static EventTreeItem getInstance(EventComponentType componentType) {
		EventTreeItem treeItem = new EventTreeItem(-1);
		
		switch (componentType) {
			case TEXT: {
				treeItem.setText("텍스트");
				treeItem.setIconType(IconType.VIEW_HEADLINE);
			} break;
	
			case IMAGE: {
				treeItem.setText("이미지");
				treeItem.setIconType(IconType.IMAGE);
			} break;
			
			case ROULETTE: {
				treeItem.setText("룰렛");
				treeItem.setIconType(IconType.TIMELAPSE);
			} break;
			
			case OXQUIZ: {
				treeItem.setText("OX퀴즈");
				treeItem.setIconType(IconType.HELP_OUTLINE);
			} break;
		}
		
		return treeItem;
	}
}
