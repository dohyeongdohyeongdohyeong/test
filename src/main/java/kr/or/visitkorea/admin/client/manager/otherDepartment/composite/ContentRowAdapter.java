package kr.or.visitkorea.admin.client.manager.otherDepartment.composite;

import gwt.material.design.client.constants.Color;

public interface ContentRowAdapter {

	public void addPanel(ContentRow child);
	public ContentRow getContentRow();
	public void setBackgroundColor(Color color);
	
}
