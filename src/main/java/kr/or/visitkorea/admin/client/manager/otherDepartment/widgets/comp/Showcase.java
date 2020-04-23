package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp;

import com.google.gwt.dom.client.Style.Float;

import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;

public class Showcase extends AreaComponent {

	public Showcase(Float flt) {
		super();
		this.setCompDetailPanelIndex(9);
		setCompType("10.png");
		buildUi();
		this.setFloat(flt);
	}

}
