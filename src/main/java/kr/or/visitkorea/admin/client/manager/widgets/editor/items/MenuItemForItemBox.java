package kr.or.visitkorea.admin.client.manager.widgets.editor.items;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;

import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;

public class MenuItemForItemBox extends MaterialLink {
	
	public MenuItemForItemBox() {
		super();
		init();
	}

	public MenuItemForItemBox(ButtonType type, String text, MaterialIcon icon) {
		super(type, text, icon);
		init();
	}

	public MenuItemForItemBox(IconType iconType) {
		super(iconType);
		init();
	}

	public MenuItemForItemBox(String text, MaterialIcon icon) {
		super(text, icon);
		init();
	}

	public MenuItemForItemBox(String text, String href, IconType icon) {
		super(text, href, icon);
		init();
	}

	public MenuItemForItemBox(String text, String href) {
		super(text, href);
		init();
	}

	public MenuItemForItemBox(String text) {
		super(text);
		init();
	}

	private void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		
		this.addMouseOverHandler(event->{
			this.setTextColor(Color.RED);
		});
		
		this.addMouseOutHandler(event->{
			this.setTextColor(Color.BLUE);
		});
		
		this.addMouseDownHandler(event->{
			this.setTextColor(Color.RED_ACCENT_4);
		});
		
		this.setBackgroundColor(Color.WHITE);

	}
	
	
}
