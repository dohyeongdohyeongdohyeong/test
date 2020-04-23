package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp;

import com.google.gwt.dom.client.Style.Float;

import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;

public class SubTitle extends AreaComponent {

	public SubTitle(Float flt) {
		super();
		this.setCompDetailPanelIndex(10);
		setCompType("12.png");
		buildUi();
		this.setFloat(flt);
	}

}
