package kr.or.visitkorea.admin.client.manager.contents.database;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconSize;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTreeItem;

public class DatabaseContentItem {

	private String name;
	private IconType iconType;
	private int goIndex;
	private String fontSize;
	private Color fontColor;
	private DatabaseContentType contentType;
	private String[] paramters;
	
	public DatabaseContentItem() {
		super();
	}

	public DatabaseContentItem(Object[] objects) {
		this.name = (String)objects[0];
		this.iconType = (IconType)objects[1];
		this.goIndex = (int)objects[2];
		this.fontSize = (String)objects[3];
		this.fontColor = (Color)objects[4];
		this.contentType = (DatabaseContentType)objects[5];
		
		if (this.contentType != null && this.contentType.equals(DatabaseContentType.INPUT_SELECT)) {
			this.paramters = (String[])objects[6];
		}
	}

	public ContentTreeItem getTreeItem() {
		
		ContentTreeItem tItem104 = new ContentTreeItem(this.goIndex);
		tItem104.setTextAlign(TextAlign.LEFT);
		tItem104.setIconSize(IconSize.TINY);
		tItem104.setFontSize(fontSize);
		tItem104.setTextColor(this.fontColor);
		tItem104.setText(this.name);
		tItem104.setIconType(this.iconType);
		tItem104.setContentType(this.contentType);
		
		if (this.contentType != null && this.contentType.equals(DatabaseContentType.INPUT_SELECT)) {
			tItem104.setParamters(this.paramters);
		}
		
		return tItem104;

	}
}
