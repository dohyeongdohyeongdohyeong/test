package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp;

import com.google.gwt.dom.client.Style.Float;

import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;

public class SingleLargeImage extends AreaComponent {

	public SingleLargeImage(Float flt) {
		super();
		setCompType("01.png");
		buildUi();
		this.setFloat(flt);
	}

	
	
}
