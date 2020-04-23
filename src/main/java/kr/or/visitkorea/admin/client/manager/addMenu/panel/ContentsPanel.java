package kr.or.visitkorea.admin.client.manager.addMenu.panel;

import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.manager.addMenu.AddMenuMain;

public class ContentsPanel extends MaterialRow {

	private AddMenuMain host;
	
	public ContentsPanel(AddMenuMain host) {
		this.host = host;

		this.setWidth("100%");
		this.setMargin(0);
	}

}
