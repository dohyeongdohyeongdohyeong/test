package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.ui.MaterialPanel;

public class ScrollContentPanel extends ContentPanel {

	private ContentPanel contentPanel;
	private int nowPosition;
	private Widget parent;

	public ScrollContentPanel(Widget parent) {
		super();
		this.parent = parent;
		this.contentPanel = new ContentPanel();
		this.contentPanel.setLayoutPosition(Position.ABSOLUTE);
		this.contentPanel.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(100);
		this.contentPanel.setTransition(cfg);
		this.add(this.contentPanel);
	}

	public ContentPanel getContentPanel() {
		return contentPanel;
	}

	public void setContentPanel(ContentPanel contentPanel) {
		this.contentPanel = contentPanel;
	}
	
	public void add(MaterialPanel contentTable, int width, int height) {
		contentTable.setWidth(width + "px");
		contentTable.setHeight(height + "px");
		contentTable.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.contentPanel.setWidth(((this.contentPanel.getChildrenList().size() + 1) * width) + "px");
		this.contentPanel.add(contentTable);
	}

	public void translate(int widgetIndex) {
		this.nowPosition = this.getWidth() * widgetIndex * -1;
		this.contentPanel.setTransform("translate("+nowPosition+"px,0);");
		this.contentPanel.setLeft(this.nowPosition);
	}

}
