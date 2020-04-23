package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.service;

import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;

public class ContentTab  extends AbstractContentPanel {

	private String tabTitle = null;
	
	public ContentTab(String title) {
		this.tabTitle = title;
	}

	public String getTitle() {
		return this.tabTitle;
	}
	
	@Override
	public void init() {
		
		
	}

}
