package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival;

import gwt.material.design.client.ui.MaterialPanel;

public class FestivalContentLeft extends MaterialPanel {

	private FestivalContentRightArea rightArea;

	public FestivalContentLeft() {
		super();
	}

	public FestivalContentLeft(String... initialClass) {
		super(initialClass);
	}

	public void setRightPanel(FestivalContentRightArea rightArea) {
		this.rightArea = rightArea;
	}

	
}
