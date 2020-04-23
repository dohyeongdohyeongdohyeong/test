package kr.or.visitkorea.admin.client.manager.widgets;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;

public class ContentLinkButton extends MaterialButton {

	private ContentLinkButton selectedLink;

	public ContentLinkButton(String text, IconType icon) {
		super(text, icon);
		init(text);
	}

	public ContentLinkButton(String text) {
		super(text);
		init(text);
	}

	private void init(String text) {
		
		this.setBackgroundColor(Color.GREY_LIGHTEN_4);
		this.setTextColor(Color.BLUE);
		this.setLineHeight(this.getOffsetHeight());
		this.setMargin(1);
		this.setPadding(2);
		this.setPaddingLeft(10);
		this.setPaddingRight(10);
		this.setDisplay(Display.INLINE);
		this.setBorderRadius("4px");

		this.addMouseOutHandler(event->{
			this.setBackgroundColor(Color.GREY_LIGHTEN_4);
			this.setTextColor(Color.BLUE);
		});
		
		this.addMouseOverHandler(event->{
			this.setBackgroundColor(Color.BLUE);
			this.setTextColor(Color.WHITE);
		});
		
		this.addMouseDownHandler(event->{
			this.setBackgroundColor(Color.RED);
			this.setTextColor(Color.WHITE);
		});
		
		this.addMouseUpHandler(event->{
			this.setBackgroundColor(Color.GREY_LIGHTEN_4);
			this.setTextColor(Color.BLUE);
		});
		
	}
	
}
