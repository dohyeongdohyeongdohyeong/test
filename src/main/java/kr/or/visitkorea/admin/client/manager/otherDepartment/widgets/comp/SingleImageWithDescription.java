package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp;

import com.google.gwt.dom.client.Style.Float;

import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;

public class SingleImageWithDescription extends AreaComponent {

	public SingleImageWithDescription(Float flt) {
		super();
		setCompType("05.png");
		buildUi();
		this.setFloat(flt);
	}

	
	
}
