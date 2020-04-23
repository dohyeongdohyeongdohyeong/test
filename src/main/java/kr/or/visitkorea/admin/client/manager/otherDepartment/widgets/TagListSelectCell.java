package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets;

import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;

public class TagListSelectCell extends SelectionPanel implements VisitKoreaListCell{

	public TagListSelectCell() {
		super();
	}
	
	public void init() {
		
	}

	@Override
	public boolean isDivBorder() {
		return true;
	}

	@Override
	public String getCellWidth() {
		return this.getWidth()+"px";
	}

	@Override
	public String getValue() {
		return getSelectedText();
	}
}
