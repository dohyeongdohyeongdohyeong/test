package kr.or.visitkorea.admin.client.manager.contents.components;

import gwt.material.design.client.constants.IconType;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTreeItem;

public class ContentComponentTreeFactory {
	
	public static ContentTreeItem getInstance(ContentComponentType componentType) {
		ContentTreeItem treeItem = new ContentTreeItem(-1);
		
		switch (componentType) {
			case TEXT: {
				treeItem.setText("텍스트");
				treeItem.setIconType(IconType.VIEW_HEADLINE);
			} break;
	
			case IMAGE: {
				treeItem.setText("이미지");
				treeItem.setIconType(IconType.IMAGE);
			} break;
		}
		
		return treeItem;
	}
}
