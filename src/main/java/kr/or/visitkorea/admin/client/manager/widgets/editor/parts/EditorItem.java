package kr.or.visitkorea.admin.client.manager.widgets.editor.parts;

import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleHeader;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialLink;

public class EditorItem extends MaterialCollapsibleItem {

	private MaterialLink titleLink;
	private MaterialCollapsibleHeader header;
	private MaterialCollapsibleBody body;

	public EditorItem() {
		super();
		init();
	}

	public EditorItem(Widget... widgets) {
		super(widgets);
		init();
	}
	
	private void init() {

		this.titleLink = new MaterialLink();
		
		this.header = new MaterialCollapsibleHeader();
		this.header.add(titleLink);
		this.body = new MaterialCollapsibleBody();
		this.body.setPadding(10);
	
		this.add(header);
		this.add(body);
	}

	@Override
    public void setText(String text) {
		this.titleLink.setText(text);
    }

    @Override
    public void setTextColor(Color textColor) {
    	this.titleLink.setTextColor(textColor);
    }

	public void setIconType(IconType iconType) {
		this.titleLink.setIconType(iconType);
	}
	
	public void setIconPosition(IconPosition iconPosition) {
		this.titleLink.setIconPosition(iconPosition);
	}

	public void addContent(Widget editorItemContent) {
		this.body.add(editorItemContent);
	}
	
}
