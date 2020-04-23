package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival;

import gwt.material.design.client.ui.MaterialPanel;

public class FestivalContentRightArea extends MaterialPanel {

	private FestivalContentLeft leftArea;

	public FestivalContentRightArea() {
		super();
		init();
	}

	private void init() {
		
		this.setBorder("1px solid #c8c8c8");
		this.setPaddingTop(10);
		this.setHeight("430px");
		
	}
	
	public void setPanel(MaterialPanel innerPanel) {
		this.clear();
		this.add(innerPanel);
	}

	public void setLeftPanel(FestivalContentLeft leftArea) {
		this.leftArea = leftArea;
	}

	
}
