package kr.or.visitkorea.admin.client.manager.widgets.editor.parts;

import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialCollapsibleItem;

public class EditorItemPanel extends MaterialCollapsible {

	public EditorItemPanel() {
		super();
		init();
	}

	public EditorItemPanel(MaterialCollapsibleItem... widgets) {
		super(widgets);
		init();
	}
	
	private void init() {
		
		this.setMarginTop(0);
		this.setMarginBottom(0);

	}
	

}
