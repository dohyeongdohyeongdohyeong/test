package kr.or.visitkorea.admin.client.manager.event.widgets;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialLabel;

public class ProgressStatusPanel extends MaterialLabel {
	private String text;
	private boolean isClicked;
	
	public ProgressStatusPanel() {
		super();
		init();
	}

	public void init() {
		this.setWidth("170px");
		this.setPaddingLeft(5);
		this.setPaddingRight(5);
		this.setPaddingTop(5);
		this.setPaddingBottom(5);
		this.setMargin(5);
		this.setTextColor(Color.WHITE);
		this.setBackgroundColor(Color.GREY_LIGHTEN_1);
		this.setFontSize("1.5em");
		this.setFontWeight(FontWeight.BOLD);
		this.getElement().getStyle().setCursor(Cursor.POINTER);
		this.setHoverable(true);
		this.addClickHandler(event -> {
			MaterialWidget parent = (MaterialWidget) this.getParent();
			this.setClicked(true);
			parent.getChildrenList().forEach(widget -> {
				if (widget instanceof ProgressStatusPanel && !widget.equals(this)) {
					((ProgressStatusPanel) widget).setClicked(false);
				}
			});
		});
	}
	
	public HandlerRegistration addClickHandler(MaterialWidget parent, ClickHandler handler) {
		return super.addClickHandler(handler);
	}

	public boolean isClicked() {
		return isClicked;
	}

	public void setClicked(boolean isClicked) {
		this.isClicked = isClicked;
		if (this.isClicked) {
			this.setBackgroundColor(Color.BLUE);
		} else {
			this.setBackgroundColor(Color.GREY_LIGHTEN_1);
		}
	}

	@Override
	public void setText(String text) {
		this.text = text;
		super.setText(text);
	}

	public String getText() {
		return text;
	}
	
}
