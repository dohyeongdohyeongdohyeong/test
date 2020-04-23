package kr.or.visitkorea.admin.client.manager.widgets.editor.dialog;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

public class ContentDialog extends MaterialPanel {

	private MaterialPanel menuArea;

	public ContentDialog() {
		super();
		init();
	}

	public ContentDialog(String... initialClass) {
		super(initialClass);
		init();
	}

	private void init() {
		
		this.setLayoutPosition(Position.ABSOLUTE);
		this.setTop(-400);
		this.setOverflow(Overflow.HIDDEN);
		this.setClass("dialog");
		
		this.menuArea = new MaterialPanel();
		this.menuArea.setBottom(0);
		this.menuArea.setHeight("30px");
		
		this.add(menuArea);
		
	}
	
	public void appendCommand(MaterialLink link) {
		
		this.menuArea.add(link);
		
	}
	
	public MaterialPanel getMenuArea() {
		
		return this.menuArea;
		
	}
	 
}
