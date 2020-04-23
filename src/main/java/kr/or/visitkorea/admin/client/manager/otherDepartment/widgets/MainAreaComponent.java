package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets;

import gwt.material.design.client.ui.MaterialLabel;

public interface MainAreaComponent {

	public String getCompType();
	
	public void setCompType(String compType);
	
	public void buildUi();
	
	public MaterialLabel getTitleLabel();
	
}
